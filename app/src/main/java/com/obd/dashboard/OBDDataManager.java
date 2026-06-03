package com.obd.dashboard;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OBDDataManager {
    
    private static final String TAG = "OBDDataManager";
    private static final String SERIAL_PORT = "/dev/ttyMT1";
    private static OBDDataManager instance;
    
    private List<OBDDataListener> listeners = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    
    private Thread serialThread;
    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;
    private int dataCount = 0;
    
    private OBDDataManager() {}
    
    public static synchronized OBDDataManager getInstance() {
        if (instance == null) {
            instance = new OBDDataManager();
        }
        return instance;
    }
    
    public void init() {
        if (isRunning) return;
        
        setSerialPortPermission();
        startSerialReader();
        
        Log.d(TAG, "OBDDataManager 已初始化");
    }
    
    private void setSerialPortPermission() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            String cmd = "chmod 666 " + SERIAL_PORT + "\n exit\n";
            process.getOutputStream().write(cmd.getBytes());
            process.waitFor();
            process.destroy();
            Log.d(TAG, "串口权限已设置: " + SERIAL_PORT);
        } catch (Exception e) {
            Log.e(TAG, "设置串口权限失败: " + e.getMessage());
        }
    }
    
    private void startSerialReader() {
        isRunning = true;
        
        serialThread = new Thread(() -> {
            File device = new File(SERIAL_PORT);
            
            int waitCount = 0;
            while (!device.exists() && waitCount < 20 && isRunning) {
                try { 
                    Thread.sleep(500); 
                } catch (InterruptedException e) { 
                    return; 
                }
                waitCount++;
            }
            
            if (!device.exists()) {
                Log.e(TAG, "串口设备不存在: " + SERIAL_PORT);
                notifyError("串口设备不存在");
                return;
            }
            
            Log.d(TAG, "串口设备已找到: " + SERIAL_PORT);
            
            BufferedReader reader = null;
            int reconnectDelay = 1000;
            
            while (isRunning) {
                try {
                    if (!device.canRead()) {
                        setSerialPortPermission();
                        Thread.sleep(1000);
                    }
                    
                    Log.d(TAG, "打开串口: " + SERIAL_PORT);
                    FileInputStream fis = new FileInputStream(device);
                    reader = new BufferedReader(new InputStreamReader(fis));
                    
                    if (!isConnected) {
                        isConnected = true;
                        notifyConnectionChanged(true);
                    }
                    
                    reconnectDelay = 1000;
                    Log.i(TAG, "串口已打开，等待OBD数据...");
                    
                    String line;
                    while (isRunning && (line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.length() > 0) {
                            parseSerialData(line);
                        }
                    }
                    
                } catch (Exception e) {
                    Log.e(TAG, "串口读取错误: " + e.getMessage());
                    
                    if (isConnected) {
                        isConnected = false;
                        notifyConnectionChanged(false);
                    }
                    
                    try { 
                        Thread.sleep(reconnectDelay); 
                    } catch (InterruptedException ie) {}
                    reconnectDelay = Math.min(reconnectDelay * 2, 30000);
                    
                } finally {
                    try {
                        if (reader != null) reader.close();
                    } catch (Exception e) {}
                }
            }
        });
        serialThread.start();
    }
    
    /**
     * 解析串口数据
     * 格式: $OBD-RT,电池,转速,车速,节气门,负荷,水温,瞬时油耗,平均油耗,本次里程,总里程,本次油耗,总油耗,故障码
     */
    private void parseSerialData(String line) {
        if (!line.startsWith("$OBD-RT") && !line.contains("$OBD-RT")) return;
        
        try {
            String[] parts = line.split(",");
            if (parts.length >= 9) {
                dataCount++;
                
                float battery = parseFloat(parts[1]);           // 电池电压
                float rotateSpeed = parseFloat(parts[2]);       // 转速
                float speed = parseFloat(parts[3]);             // 车速
                float throttle = parseFloat(parts[4]);          // 节气门
                float engineLoad = parseFloat(parts[5]);        // 发动机负荷
                float coolantTemp = parseFloat(parts[6]);       // 水温
                float instantFuel = parseFloat(parts[7]);       // 瞬时油耗
                float avgFuel = parseFloat(parts[8]);           // 平均油耗
                
                // 限制油耗最大值
                if (instantFuel >= 50.0f) instantFuel = 49.9f;
                if (avgFuel >= 50.0f) avgFuel = 49.9f;
                
                // 构造数据字符串: 水温,车速,转速,电压,瞬时油耗,平均油耗,节气门,发动机负荷
                String dataStr = String.format(Locale.US, "%.1f,%.0f,%.0f,%.1f,%.1f,%.1f,%.1f,%.1f",
                    coolantTemp, speed, rotateSpeed, battery, instantFuel, avgFuel, throttle, engineLoad);
                
                Log.d(TAG, String.format("[#%d] 水温=%.1f°C, 车速=%.0fkm/h, 转速=%.0frpm, 电压=%.1fV",
                    dataCount, coolantTemp, speed, rotateSpeed, battery));
                
                notifyDataReceived(dataStr);
            } else {
                Log.w(TAG, "数据字段不足: " + parts.length + " - " + line);
            }
        } catch (Exception e) {
            Log.e(TAG, "解析失败: " + e.getMessage() + " - " + line);
        }
    }
    
    private float parseFloat(String str) {
        try {
            if (str == null || str.isEmpty()) return 0;
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void addListener(OBDDataListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(OBDDataListener listener) {
        listeners.remove(listener);
    }
    
    public void stop() {
        isRunning = false;
        if (serialThread != null) {
            serialThread.interrupt();
        }
        notifyConnectionChanged(false);
        Log.d(TAG, "OBDDataManager 已停止");
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public int getDataCount() {
        return dataCount;
    }
    
    private void notifyDataReceived(String data) {
        mainHandler.post(() -> {
            for (OBDDataListener listener : listeners) {
                listener.onDataReceived(data);
            }
        });
    }
    
    private void notifyConnectionChanged(boolean connected) {
        mainHandler.post(() -> {
            for (OBDDataListener listener : listeners) {
                listener.onConnectionChanged(connected);
            }
        });
    }
    
    private void notifyError(String error) {
        mainHandler.post(() -> {
            for (OBDDataListener listener : listeners) {
                listener.onError(error);
            }
        });
    }
    
    public void destroy() {
        stop();
        listeners.clear();
    }
}
