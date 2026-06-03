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
        cbAvgFuel.setChecked(prefs.getBoolean("show_avg_fuel", true));
        cbBattery.setChecked(prefs.getBoolean("show_battery", true));
        cbInstantFuel.setChecked(prefs.getBoolean("show_instant_fuel", false));
        cbThrottle.setChecked(prefs.getBoolean("show_throttle", false));
        cbEngineLoad.setChecked(prefs.getBoolean("show_engine_load", false));
    }
    
    private void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences("dashboard_settings", MODE_PRIVATE).edit();
        editor.putBoolean("show_coolant", cbCoolant.isChecked());
        editor.putBoolean("show_avg_fuel", cbAvgFuel.isChecked());
        editor.putBoolean("show_battery", cbBattery.isChecked());
        editor.putBoolean("show_instant_fuel", cbInstantFuel.isChecked());
        editor.putBoolean("show_throttle", cbThrottle.isChecked());
        editor.putBoolean("show_engine_load", cbEngineLoad.isChecked());
        editor.apply();
        
        finish();
    }
}
