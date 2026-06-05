package com.example.obdparser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.ileja.aicar.obd.data.AIObdDataMessage;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.aicar.obd.data.AIObdStatus;
import com.ileja.aicar.obd.data.AIObdFuelData;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.core.data.CommonConfig;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OBD";
    
    private TextView tvSpeed;
    private TextView tvRpm;
    private TextView tvStatus;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    
    private BroadcastReceiver obdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            android.os.Bundle bundle = intent.getExtras();
            
            if (bundle == null) return;
            
            if (AIObdDataMessage.OBD_REAL_MESSAGE_FILTER.equals(action)) {
                AIObdRealtimeData data = bundle.getParcelable(CommonConfig.Data);
                if (data != null && data.isValid()) {
                    float speed = data.obdSpeed;
                    float rpm = data.obdRotateSpeed;
                    
                    mainHandler.post(() -> {
                        if (tvSpeed != null) {
                            tvSpeed.setText(String.format("%.1f km/h", speed));
                        }
                        if (tvRpm != null) {
                            tvRpm.setText(String.format("%.0f rpm", rpm));
                        }
                    });
                    
                    Log.d(TAG, String.format("车速: %.1f km/h, 转速: %.0f rpm", speed, rpm));
                }
            }
            
            else if (AIObdDataMessage.OBD_STATUS_MESSAGE_FILTER.equals(action)) {
                AIObdStatus status = bundle.getParcelable(CommonConfig.Data);
                if (status != null) {
                    String statusText = status.getStatus() == 0 ? "已连接" : "未连接";
                    mainHandler.post(() -> {
                        if (tvStatus != null) {
                            tvStatus.setText("OBD: " + statusText);
                        }
                    });
                    Log.d(TAG, "OBD状态: " + statusText);
                }
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvSpeed = findViewById(R.id.tv_speed);
        tvRpm = findViewById(R.id.tv_rpm);
        tvStatus = findViewById(R.id.tv_status);
        
        registerObdReceiver();
    }
    
    private void registerObdReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AIObdDataMessage.OBD_REAL_MESSAGE_FILTER);
        filter.addAction(AIObdDataMessage.OBD_STATUS_MESSAGE_FILTER);
        filter.addAction(AIObdDataMessage.OBD_FUEL_MESSAGE_FILTER);
        filter.addAction(AIObdDataMessage.OBD_ERROR_MESSAGE_FILTER);
        
        registerReceiver(obdReceiver, filter);
        Log.d(TAG, "广播接收器已注册");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(obdReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
