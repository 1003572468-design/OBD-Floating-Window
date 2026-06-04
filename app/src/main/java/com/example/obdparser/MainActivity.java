package com.obd.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "OBD_Dashboard";
    
    // UI 组件
    private TextView tvSpeed, tvRpm, tvTemp, tvVoltage;
    private TextView tvAvgFuel, tvInstantFuel, tvTotalMileage, tvErrorCodes;
    private TextView tvConnectionStatus;
    private View ledStatus;
    
    // 广播接收器
    private OBDReceiver obdReceiver;
    
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
        
        // 注册所有 OBD 相关的广播
        filter.addAction("com.cpsdna.obdport.data");        // OBD 实时数据
        filter.addAction("com.cpsdna.obdport.fire");        // 点火状态
        filter.addAction("OBD_STATUS_CONNECTED");           // 连接成功
        filter.addAction("OBD_STATUS_ERROR");               // 连接错误
        filter.addAction("OBD_VIN");                        // VIN 码
        filter.addAction("com.cpsdna.obdport.tire");        // 胎压数据
        filter.addAction("OBD_single_MILE");                // 单次里程
        
        registerReceiver(obdReceiver, filter);
        Log.d(TAG, "OBD 广播接收器已注册");
    }
    
    private class OBDReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            
            Log.d(TAG, "收到广播: " + action);
            
            if ("com.cpsdna.obdport.data".equals(action)) {
                // OBD 实时数据
                if (extras != null) {
                    parseOBDData(extras);
                }
            } 
            else if ("OBD_STATUS_CONNECTED".equals(action)) {
                updateConnectionStatus(true);
                Log.d(TAG, "OBD 已连接");
            }
            else if ("OBD_STATUS_ERROR".equals(action)) {
                updateConnectionStatus(false);
                Log.d(TAG, "OBD 连接错误");
            }
            else if ("com.cpsdna.obdport.fire".equals(action)) {
                boolean isFire = extras != null && extras.getBoolean("fire", false);
                Log.d(TAG, isFire ? "车辆已点火" : "车辆已熄火");
            }
            else if ("OBD_VIN".equals(action)) {
                String vin = extras != null ? extras.getString("OBD_VIN") : "";
                Log.d(TAG, "VIN码: " + vin);
            }
            else if ("OBD_single_MILE".equals(action)) {
                int mile = extras != null ? extras.getInt("OBD_single_MILE", 0) : 0;
                Log.d(TAG, "单次里程: " + mile);
            }
        }
        
        private void parseOBDData(Bundle extras) {
            try {
                // 从 AILejaObdReader 中确认的数据 Key
                int rpm = extras.getInt("EngineRPM", 0);
                int speed = extras.getInt("VehicleSpeed", 0);
                int temp = extras.getInt("EngineCoolantTemperature", 0);
                float voltage = extras.getFloat("VehicleVoltage", 0);
                float instantFuel = extras.getFloat("INSTANT_FUEL", 0);
                float avgFuel = extras.getFloat("AVERAGE_FUEL", 0);
                int totalMileage = extras.getInt("TotalMileage", 0);
                int errorCount = extras.getInt("DTCStoredInthisECU", 0);
                String[] errorCodes = extras.getStringArray("TroubleCodeArray");
                
                // 胎压数据
                int tireLF = extras.getInt("TirePressueLF", 0);
                int tireRF = extras.getInt("TirePressueRF", 0);
                int tireLB = extras.getInt("TirePressueLB", 0);
                int tireRB = extras.getInt("TirePressueRB", 0);
                
                Log.d(TAG, String.format(
                    "OBD | 车速:%d km/h | 转速:%d rpm | 水温:%d°C | 电压:%.1fV | 平均油耗:%.1fL/100km",
                    speed, rpm, temp, voltage, avgFuel
                ));
                
                // 更新 UI
                runOnUiThread(() -> updateUI(speed, rpm, temp, voltage, avgFuel, instantFuel, totalMileage, errorCodes));
                
            } catch (Exception e) {
                Log.e(TAG, "解析 OBD 数据失败: " + e.getMessage());
            }
        }
    }
    
    private void updateUI(int speed, int rpm, int temp, float voltage, 
                          float avgFuel, float instantFuel, int totalMileage,
                          String[] errorCodes) {
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
            tvErrorCodes.setText("无");
            tvErrorCodes.setTextColor(Color.parseColor("#00d8ff"));
        }
    }
    
    private void updateConnectionStatus(boolean connected) {
        runOnUiThread(() -> {
            if (connected) {
                tvConnectionStatus.setText("已连接");
                tvConnectionStatus.setTextColor(Color.parseColor("#00ff88"));
                ledStatus.setBackgroundResource(R.drawable.led_green);
            } else {
                tvConnectionStatus.setText("未连接");
                tvConnectionStatus.setTextColor(Color.parseColor("#888888"));
                ledStatus.setBackgroundResource(R.drawable.led_red);
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (obdReceiver != null) {
            unregisterReceiver(obdReceiver);
        }
    }
}
