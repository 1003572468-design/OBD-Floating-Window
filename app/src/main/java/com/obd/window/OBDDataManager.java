package com.obd.window;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OBDDataManager {
    
    private static OBDDataManager instance;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private List<OBDDataListener> listeners;
    private volatile boolean isRunning;
    
    private float mockTemp = 85;
    private float mockSpeed = 60;
    private float mockRotate = 2500;
    private float mockBattery = 12.5f;
    private float mockFuelRealtime = 7.5f;
    private float mockFuelAvg = 8.2f;
    private boolean increasing = true;
    private Random random = new Random();
    
    private OBDDataManager() {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        listeners = new ArrayList<>();
        isRunning = false;
    }
    
    public static synchronized OBDDataManager getInstance() {
        if (instance == null) {
            instance = new OBDDataManager();
        }
        return instance;
    }
    
    public void addListener(OBDDataListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(OBDDataListener listener) {
        listeners.remove(listener);
    }
    
    public void startCollecting() {
        if (isRunning) return;
        isRunning = true;
        
        executorService.execute(() -> {
            notifyConnectionChanged(true);
            
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                    String data = generateMockData();
                    notifyDataReceived(data);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }
    
    public void stopCollecting() {
        isRunning = false;
        notifyConnectionChanged(false);
    }
    
    private String generateMockData() {
        if (increasing) {
            mockTemp += 0.3;
            mockSpeed += 2;
            mockRotate += 50;
            if (mockTemp > 95) increasing = false;
        } else {
            mockTemp -= 0.3;
            mockSpeed -= 2;
            mockRotate -= 50;
            if (mockTemp < 80) increasing = true;
        }
        
        mockBattery = 12.0f + random.nextFloat() * 1.5f;
        mockFuelRealtime = 6.0f + random.nextFloat() * 4.0f;
        
        return String.format(Locale.US, "%.1f,%.0f,%.0f,%.1f,%.1f,%.1f",
            mockTemp, mockSpeed, mockRotate, mockBattery, mockFuelRealtime, mockFuelAvg);
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
    
    public void notifyError(String error) {
        mainHandler.post(() -> {
            for (OBDDataListener listener : listeners) {
                listener.onError(error);
            }
        });
    }
}
