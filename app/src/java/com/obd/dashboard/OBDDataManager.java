package com.obd.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OBDDataManager {
    
    private static final String TAG = "OBDDataManager";
    private static OBDDataManager instance;
    
    private Context context;
    private List<OBDDataListener> listeners = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isRegistered = false;
    
    private float coolantTemp = 0;
    private float speed = 0;
    private float rpm = 0;
    private float battery = 0;
    private float instantFuel = 0;
    private float avgFuel = 0;
    
    private BroadcastReceiver obdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            
            if (OBDAction.ACTION_OBDBUNDLE.equals(action)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    coolantTemp = getFloat(bundle, OBDAttachKey.EngineCoolantTemperature);
                    speed = getFloat(bundle, OBDAttachKey.VehicleSpeed);
                    rpm = getFloat(bundle, OBDAttachKey.EngineRPM);
                    battery = getFloat(bundle, OBDAttachKey.VehicleVoltage);
                    instantFuel = getFloat(bundle, OBDAttachKey.INSTANT_FUEL);
                    avgFuel = getFloat(bundle, OBDAttachKey.AVERAGE_FUEL);
                    
                    String dataStr = String.format(Locale.US, "%.1f,%.0f,%.0f,%.1f,%.1f,%.1f",
                        coolantTemp, speed, rpm, battery, instantFuel, avgFuel);
                    
                    Log.d(TAG, String.format("广播数据: 水温=%.1f°C, 车速=%.0fkm/h, 转速=%.0frpm",
                        coolantTemp, speed, rpm));
                    
                    notifyDataReceived(dataStr);
                }
            } else if (OBDAction.OBD_STATUS_CONNECTED.equals(action)) {
                Log.d(TAG, "OBD 已连接");
                notifyConnectionChanged(true);
            } else if (OBDAction.OBD_STATUS_ERROR.equals(action)) {
                Log.e(TAG, "OBD 连接错误");
                notifyConnectionChanged(false);
            } else if (OBDAction.ACTION_OBD_FIRE.equals(action)) {
                boolean isFire = intent.getBooleanExtra(OBDAction.KEY_FIRE, false);
                Log.d(TAG, "点火状态: " + (isFire ? "点火" : "熄火"));
            }
        }
    };
    
    private float getFloat(Bundle bundle, String key) {
        try {
            return bundle.getFloat(key);
        } catch (Exception e) {
            return 0;
        }
    }
    
    private OBDDataManager() {}
    
    public static synchronized OBDDataManager getInstance() {
        if (instance == null) {
            instance = new OBDDataManager();
        }
        return instance;
    }
    
    public void init(Context context) {
        this.context = context.getApplicationContext();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(OBDAction.ACTION_OBDBUNDLE);
        filter.addAction(OBDAction.OBD_STATUS_CONNECTED);
        filter.addAction(OBDAction.OBD_STATUS_ERROR);
        filter.addAction(OBDAction.ACTION_OBD_FIRE);
        
        LocalBroadcastManager.getInstance(this.context).registerReceiver(obdReceiver, filter);
        isRegistered = true;
        
        Log.d(TAG, "广播接收器已注册，等待 OBD 数据...");
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
        if (isRegistered && context != null) {
            try {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(obdReceiver);
            } catch (Exception e) {
                Log.e(TAG, "注销广播失败", e);
            }
        }
        listeners.clear();
    }
}
