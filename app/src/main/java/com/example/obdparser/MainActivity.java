package com.obd.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "OBDDashboard";
    private static final String ACTION_OBD_DATA = "com.cpsdna.obdport.data";
    private static final String ACTION_OBD_VIN = "OBD_VIN";
    
    // UI 组件
    private TextView tvSpeed, tvRpm, tvTemp, tvVoltage;
    private TextView tvAvgFuel, tvInstantFuel, tvTotalMileage, tvErrorCodes;
    private TextView tvConnectionStatus;
    private View ledStatus;
    
    // 广播接收器
    private OBDReceiver obdReceiver;
    
    // 连接状态
    private boolean isConnected = false;
    private long lastDataTime = 0;
    private Handler timeoutHandler = new Handler(Looper.getMainLooper());
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        registerOBDReceiver();
    }
    
    private void initViews() {
        tvSpeed = findViewById(R.id.tv_speed);
        tvRpm = findViewById(R.id.tv_rpm);
        tvTemp = findViewById(R.id.tv_temp);
        tvVoltage = findViewById(R.id.tv_voltage);
        tvAvgFuel = findViewById(R.id.tv_avg_fuel);
        tvInstantFuel = findViewById(R.id.tv_instant_fuel);
        tvTotalMileage = findViewById(R.id.tv_total_mileage);
        tvErrorCodes = findViewById(R.id.tv_error_codes);
        tvConnectionStatus = findViewById(R.id.tv_connection_status);
        ledStatus = findViewById(R.id.led_status);
    }
    
    private void registerOBDReceiver() {
        obdReceiver = new OBDReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_OBD_DATA);
        filter.addAction(ACTION_OBD_VIN);
        registerReceiver(obdReceiver, filter);
        Log.d(TAG, "OBD 广播接收器已注册");
    }
    
    /**
     * OBD 广播接收器 - 接收 AICore 发送的真实数据
     */
    private class OBDReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            
            if (extras == null) {
                Log.w(TAG, "广播没有携带数据");
                return;
            }
            
            if (ACTION_OBD_DATA.equals(action)) {
                // 收到真实 OBD 数据
                handleOBDData(extras);
            } else if (ACTION_OBD_VIN.equals(action)) {
                String vin = extras.getString("OBD_VIN");
                Log.d(TAG, "收到 VIN 码: " + vin);
            }
        }
        
        private void handleOBDData(Bundle extras) {
            // 从 AICore 的 AIDinaObdReceiver 中提取真实数据
            // 这些 Key 是从逆向分析中发现的
            int speed = getIntValue(extras, "VehicleSpeed", 0);
            int rpm = getIntValue(extras, "EngineRPM", 0);
            int coolantTemp = getIntValue(extras, "EngineCoolantTemperature", 0);
            float battery = getFloatValue(extras, "VehicleVoltage", 0);
            float instantFuel = getFloatValue(extras, "INSTANT_FUEL", 0);
            float avgFuel = getFloatValue(extras, "AVERAGE_FUEL", 0);
            int totalMileage = getIntValue(extras, "TotalMileage", 0);
            
            // 故障码
            String[] errorCodes = extras.getStringArray("TroubleCodeArray");
            int errorCount = getIntValue(extras, "DTCStoredInthisECU", 0);
            
            // 胎压数据（可选）
            int tireLF = getIntValue(extras, "TirePressueLF", 0);
            int tireRF = getIntValue(extras, "TirePressueRF", 0);
            int tireLR = getIntValue(extras, "TirePressueLB", 0);
            int tireRR = getIntValue(extras, "TirePressueRB", 0);
            
            // 记录日志
            Log.d(TAG, String.format("真实OBD数据 | 车速:%d km/h | 转速:%d rpm | 水温:%d°C | 电压:%.1fV | 平均油耗:%.1fL/100km",
                    speed, rpm, coolantTemp, battery, avgFuel));
            
            // 更新 UI
            updateUI(speed, rpm, coolantTemp, battery, avgFuel, instantFuel, totalMileage, errorCodes);
            
            // 更新连接状态
            if (!isConnected) {
                isConnected = true;
                updateConnectionStatus(true);
            }
            lastDataTime = System.currentTimeMillis();
            
            // 设置超时检测：5秒没收到数据认为断开
            timeoutHandler.removeCallbacks(timeoutRunnable);
            timeoutHandler.postDelayed(timeoutRunnable, 5000);
        }
        
        private int getIntValue(Bundle bundle, String key, int defaultValue) {
            Object value = bundle.get(key);
            if (value instanceof Integer) {
                return (int) value;
            } else if (value instanceof String) {
                try {
                    return Integer.parseInt((String) value);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
            return defaultValue;
        }
        
        private float getFloatValue(Bundle bundle, String key, float defaultValue) {
            Object value = bundle.get(key);
            if (value instanceof Float) {
                return (float) value;
            } else if (value instanceof Integer) {
                return ((Integer) value).floatValue();
            } else if (value instanceof String) {
                try {
                    return Float.parseFloat((String) value);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
            return defaultValue;
        }
    }
    
    private void updateUI(int speed, int rpm, int temp, float voltage, 
                          float avgFuel, float instantFuel, int totalMileage, 
                          String[] errorCodes) {
        runOnUiThread(() -> {
            tvSpeed.setText(String.valueOf(speed));
            tvRpm.setText(String.valueOf(rpm));
            tvTemp.setText(String.valueOf(temp));
            tvVoltage.setText(String.format("%.1f", voltage));
            tvAvgFuel.setText(String.format("%.1f", avgFuel));
            tvInstantFuel.setText(String.format("%.1f", instantFuel));
            tvTotalMileage.setText(String.valueOf(totalMileage));
            
            if (errorCodes != null && errorCodes.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (String code : errorCodes) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(code);
                }
                tvErrorCodes.setText(sb.toString());
                tvErrorCodes.setTextColor(Color.parseColor("#ff4444"));
            } else {
                tvErrorCodes.setText("无故障码");
                tvErrorCodes.setTextColor(Color.parseColor("#00d8ff"));
            }
        });
    }
    
    private void updateConnectionStatus(boolean connected) {
        runOnUiThread(() -> {
            if (connected) {
                tvConnectionStatus.setText("OBD 已连接");
                tvConnectionStatus.setTextColor(Color.parseColor("#00ff88"));
                ledStatus.setBackgroundResource(R.drawable.led_green);
            } else {
                tvConnectionStatus.setText("OBD 未连接");
                tvConnectionStatus.setTextColor(Color.parseColor("#888888"));
                ledStatus.setBackgroundResource(R.drawable.led_red);
            }
        });
    }
    
    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - lastDataTime > 5000) {
                isConnected = false;
                updateConnectionStatus(false);
                Log.w(TAG, "OBD 数据超时，已断开连接");
            }
        }
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (obdReceiver != null) {
            unregisterReceiver(obdReceiver);
        }
        timeoutHandler.removeCallbacks(timeoutRunnable);
    }
}
