package com.obd.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    
    private CheckBox cbCoolant, cbAvgFuel, cbBattery, cbInstantFuel, cbThrottle, cbEngineLoad;
    private Button btnSave;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        loadSettings();
        
        btnSave.setOnClickListener(v -> saveSettings());
    }
    
    private void initViews() {
        cbCoolant = findViewById(R.id.cb_coolant_temp);
        cbAvgFuel = findViewById(R.id.cb_avg_fuel);
        cbBattery = findViewById(R.id.cb_battery);
        cbInstantFuel = findViewById(R.id.cb_instant_fuel);
        cbThrottle = findViewById(R.id.cb_throttle);
        cbEngineLoad = findViewById(R.id.cb_engine_load);
        btnSave = findViewById(R.id.btn_save);
    }
    
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("dashboard_settings", MODE_PRIVATE);
        cbCoolant.setChecked(prefs.getBoolean("show_coolant", true));
        cbAvg
