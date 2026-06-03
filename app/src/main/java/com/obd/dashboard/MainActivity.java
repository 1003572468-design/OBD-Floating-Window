package com.obd.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Time;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OBDDataListener {

    private LinearLayout llLeftArea;
    private TextView tvSpeedValue;
    private TextView tvRpmValue;
    private TextView tvTimeValue;
    
    private OBDDataManager dataManager;
    private Handler timeHandler = new Handler(Looper.getMainLooper());
    private Runnable timeRunnable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        
        dataManager = OBDDataManager.getInstance();
        dataManager.init(this);
        dataManager.addListener(this);
        
        startTimeUpdate();
        
        Toast.makeText(this, "OBD仪表盘已启动", Toast.LENGTH_SHORT).show();
    }
    
    private void initViews() {
        llLeftArea = findViewById(R.id.ll_left_area);
        tvSpeedValue = findViewById(R.id.tv_speed_value);
        tvRpmValue = findViewById(R.id.tv_rpm_value);
        tvTimeValue = findViewById(R.id.tv_time_value);
    }
    
    private void startTimeUpdate() {
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.post(timeRunnable);
    }
    
    private void updateTime() {
        Time time = new Time();
        time.setToNow();
        String timeStr = String.format(Locale.US, "%02d:%02d", time.hour, time.minute);
        tvTimeValue.setText(timeStr);
    }
    
    @Override
    public void onDataReceived(String data) {
        runOnUiThread(() -> {
            String[] parts = data.split(",");
            if (parts.length >= 3) {
                try {
                    float speed = Float.parseFloat(parts[1]);
                    float rpm = Float.parseFloat(parts[2]);
                    
                    tvSpeedValue.setText(String.format(Locale.US, "%.0f", speed));
                    tvRpmValue.setText(String.format(Locale.US, "%.0f", rpm));
                    
                    if (speed > 120) {
                        tvSpeedValue.setTextColor(0xFFF44336);
                    } else if (speed > 80) {
                        tvSpeedValue.setTextColor(0xFFFF9800);
                    } else {
                        tvSpeedValue.setTextColor(0xFF4CAF50);
                    }
                    
                } catch (NumberFormatException e) {
                    // 忽略
                }
            }
        });
    }
    
    @Override
    public void onConnectionChanged(boolean connected) {
        runOnUiThread(() -> {
            Toast.makeText(this, connected ? "OBD已连接" : "OBD模拟模式", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onError(String error) {
        runOnUiThread(() -> Toast.makeText(this, error, Toast.LENGTH_LONG).show());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataManager != null) {
            dataManager.removeListener(this);
            dataManager.destroy();
        }
        if (timeHandler != null && timeRunnable != null) {
            timeHandler.removeCallbacks(timeRunnable);
        }
    }
}
