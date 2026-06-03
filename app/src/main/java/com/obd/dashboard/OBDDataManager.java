package com.obd.dashboard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.ileja.aicar.obd.IObdService;
import com.ileja.aicar.obd.data.AIObdDataMessage;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.core.data.CommonConfig;
import com.ileja.core.data.DataCallback;
import com.ileja.core.data.IDataService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OBDDataManager {
    
    private static final String TAG = "OBDDataManager";
    private static OBDDataManager instance;
    
    private Context context;
    private IDataService dataService;
    private boolean isBound = false;
    
    private List<OBDDataListener> listeners = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    
    private DataCallback callback = new DataCallback.Stub() {
        @Override
        public void callback(String filter, Bundle bundle) throws RemoteException {
            if (AIObdDataMessage.OBD_REAL_MESSAGE_FILTER.equals(filter)) {
                AIObdRealtimeData data = bundle.getParcelable(CommonConfig.Data);
                if (data != null && data.valid) {
                    String dataStr = String.format(Locale.US, "%.1f,%.0f,%.0f,%.1f,%.1f,%.1f,%.1f,%.1f",
                        data.obdCoolantTemprature, data.obdSpeed, data.obdRotateSpeed,
                        data.obdBattery, data.obdRealtimeFuelConsume, data.obdAverageFuelConsume,
                        data.obdThrottleOpenRatio, data.obdEngineLoadRatio);
                    
                    Log.d(TAG, "收到数据: 水温=" + data.obdCoolantTemprature + "°C, 车速=" + data.obdSpeed + "km/h");
                    notifyDataReceived(dataStr);
                }
            }
        }
    };
    
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dataService = IDataService.Stub.asInterface(service);
            Log.d(TAG, "DataService 连接成功");
            registerListener();
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataService = null;
            Log.w(TAG, "DataService 断开连接");
            notifyConnectionChanged(false);
        }
    };
    
    private OBDDataManager() {}
    
    public static synchronized OBDDataManager getInstance() {
        if (instance == null) {
            instance = new OBDDataManager();
        }
        return instance;
    }
    
    public void init(Context context) {
        this.context = context.getApplicationContext();
        bindDataService();
    }
    
    private void bindDataService() {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.ileja.carrobot", "com.ileja.framework.service.core.HFDataService");
            boolean success = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
            if (success) {
                Log.d(TAG, "正在绑定 DataService...");
            } else {
                Log.e(TAG, "DataService 绑定失败");
                startMockData();
            }
        } catch (Exception e) {
            Log.e(TAG, "绑定服务异常: " + e.getMessage());
            startMockData();
        }
    }
    
    private void registerListener() {
        if (dataService != null) {
            try {
                dataService.register(AIObdDataMessage.OBD_REAL_MESSAGE_FILTER, callback);
                Log.d(TAG, "已注册数据监听");
                notifyConnectionChanged(true);
            } catch (RemoteException e) {
                Log.e(TAG, "注册监听失败", e);
            }
        }
    }
    
    // 模拟数据（备用）
    private Thread mockThread;
    private boolean useMockData = false;
    private float mockTemp = 85, mockSpeed = 60, mockRpm = 2500;
    private boolean increasing = true;
    
    private void startMockData() {
        if (useMockData) return;
        useMockData = true;
        mockThread = new Thread(() -> {
            while (useMockData) {
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
                if (increasing) {
                    mockTemp += 0.3; mockSpeed += 2; mockRpm += 50;
                    if (mockTemp >= 95) increasing = false;
                } else {
                    mockTemp -= 0.3; mockSpeed -= 2; mockRpm -= 50;
                    if (mockTemp <= 80) increasing = true;
                }
                String dataStr = String.format(Locale.US, "%.1f,%.0f,%.0f,12.5,7.5,8.2,0,0",
                    mockTemp, mockSpeed, mockRpm);
                notifyDataReceived(dataStr);
            }
        });
        mockThread.start();
        notifyConnectionChanged(true);
    }
    
    public void addListener(OBDDataListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(OBDDataListener listener) {
        listeners.remove(listener);
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
    
    public void destroy() {
        if (dataService != null) {
            try {
                dataService.unregister(AIObdDataMessage.OBD_REAL_MESSAGE_FILTER, callback);
            } catch (RemoteException e) { }
        }
        if (isBound && context != null) {
            context.unbindService(connection);
        }
        useMockData = false;
        listeners.clear();
    }
}
