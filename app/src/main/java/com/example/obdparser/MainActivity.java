package com.obd.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private TextView tvSpeed, tvRpm, tvTemp, tvVoltage;
    private TextView tvAvgFuel, tvInstantFuel, tvTotalMileage, tvErrorCodes;
    private TextView tvConnectionStatus;
    private View ledStatus;

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isConnected = false;

    private int speed = 0;
    private int rpm = 0;
    private int temp = 90;
    private float voltage = 12.5f;
    private float avgFuel = 8.5f;
    private float instantFuel = 0.0f;
    private int totalMileage = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        startDataSimulation();
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

    private void startDataSimulation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateSimulatedData();
                updateUI();
                handler.postDelayed(this, 500);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnected = true;
                updateConnectionStatus();
            }
        }, 3000);
    }

    private void updateSimulatedData() {
        speed = (int) (Math.random() * 80) + 20;
        rpm = (int) (Math.random() * 2500) + 800;
        
        if (temp < 90) {
            temp += 1;
        } else {
            temp = 90 + (int)(Math.random() * 10 - 5);
        }
        
        voltage = 13.5f + (float)(Math.random() * 1.0);
        avgFuel = 7.5f + (float)(Math.random() * 3);
        instantFuel = (float)(speed / 10.0) + (float)(Math.random() * 2);
        
        if (speed > 5) {
            totalMileage += 0.1;
        }
    }

    private void updateUI() {
        tvSpeed.setText(String.valueOf(speed));
        tvRpm.setText(String.valueOf(rpm));
        tvTemp.setText(String.valueOf(temp));
        tvVoltage.setText(String.format("%.1f", voltage));
        tvAvgFuel.setText(String.format("%.1f", avgFuel));
        tvInstantFuel.setText(String.format("%.1f", instantFuel));
        tvTotalMileage.setText(String.valueOf((int)totalMileage));
        
        if (Math.random() > 0.95) {
            tvErrorCodes.setText("P0300, P0420");
        } else {
            tvErrorCodes.setText("无故障码");
        }
    }

    private void updateConnectionStatus() {
        if (isConnected) {
            tvConnectionStatus.setText("OBD 已连接");
            tvConnectionStatus.setTextColor(Color.parseColor("#00ff88"));
            if (ledStatus != null) {
                ledStatus.setBackgroundResource(R.drawable.led_green);
            }
        } else {
            tvConnectionStatus.setText("等待 OBD 连接...");
            tvConnectionStatus.setTextColor(Color.parseColor("#888888"));
        }
    }
}
