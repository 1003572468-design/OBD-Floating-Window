package com.obd.dashboard;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OBDDataListener {

    private LinearLayout llLeftArea;
    private TextView tvSpeedValue;
    private TextView tvRpmValue;
    private TextView tvTimeValue;
    
    private OBDDataManager dataManager;
    private Handler timeHandler = new Handler(Looper.getMainLooper());
    private Runnable timeRunnable;
    
    private Map<String, DataItemInfo> availableItems = new HashMap<>();
    private Map<String, DataItemView> activeItems = new HashMap<>();
    private List<String> selectedItems = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        initAvailableItems();
        loadSettings();
        
        dataManager = OBDDataManager.getInstance();
        dataManager.addListener(this);
        dataManager.init();
        
        startTimeUpdate();
        
        llLeftArea.setOnLongClickListener(v -> {
            openItemSelector();
            return true;
        });
        
        tvTimeValue.setOnClickListener(v -> openItemSelector());
    }
    
    private void initViews() {
        llLeftArea = findViewById(R.id.ll_left_area);
        tvSpeedValue = findViewById(R.id.tv_speed_value);
        tvRpmValue = findViewById(R.id.tv_rpm_value);
        tvTimeValue = findViewById(R.id.tv_time_value);
    }
    
    private void initAvailableItems() {
        availableItems.put("coolant_temp", new DataItemInfo("🌡️", "水温", "°C"));
        availableItems.put("avg_fuel", new DataItemInfo("⛽", "平均油耗", "L/100km"));
        availableItems.put("instant_fuel", new DataItemInfo("⚡", "瞬时油耗", "L/100km"));
        availableItems.put("battery", new DataItemInfo("🔋", "电压", "V"));
        availableItems.put("throttle", new DataItemInfo("🚦", "节气门", "%"));
        availableItems.put("engine_load", new DataItemInfo("💪", "发动机负荷", "%"));
    }
    
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("dashboard_settings", MODE_PRIVATE);
        String selectedStr = prefs.getString("selected_items", "coolant_temp,avg_fuel,battery");
        selectedItems.clear();
        for (String key : selectedStr.split(",")) {
            if (availableItems.containsKey(key) && !selectedItems.contains(key)) {
                selectedItems.add(key);
            }
        }
        refreshLeftArea();
    }
    
    private void saveSettings() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(selectedItems.get(i));
        }
        SharedPreferences.Editor editor = getSharedPreferences("dashboard_settings", MODE_PRIVATE).edit();
        editor.putString("selected_items", sb.toString());
        editor.apply();
        
        refreshLeftArea();
    }
    
    private void refreshLeftArea() {
        llLeftArea.removeAllViews();
        activeItems.clear();
        
        for (String key : selectedItems) {
            DataItemInfo info = availableItems.get(key);
            if (info != null) {
                DataItemView itemView = new DataItemView(this);
                itemView.setData(key, info.icon, info.unit, 0);
                llLeftArea.addView(itemView);
                activeItems.put(key, itemView);
            }
        }
        
        if (selectedItems.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("长按此处\n选择数据");
            emptyView.setTextSize(12);
            emptyView.setTextColor(0x88888888);
            emptyView.setGravity(android.view.Gravity.CENTER);
            emptyView.setPadding(0, 20, 0, 20);
            llLeftArea.addView(emptyView);
        }
    }
    
    private void openItemSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_item_selector, null);
        builder.setView(dialogView);
        
        ListView listView = dialogView.findViewById(R.id.lv_data_items);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        
        List<String> itemKeys = new ArrayList<>(availableItems.keySet());
        String[] itemNames = new String[itemKeys.size()];
        boolean[] checked = new boolean[itemKeys.size()];
        
        for (int i = 0; i < itemKeys.size(); i++) {
            String key = itemKeys.get(i);
            DataItemInfo info = availableItems.get(key);
            itemNames[i] = info.icon + " " + info.name;
            checked[i] = selectedItems.contains(key);
        }
        
        AlertDialog dialog = builder.create();
        
        listView.setAdapter(new android.widget.ArrayAdapter<>(this, 
            android.R.layout.simple_list_item_multiple_choice, itemNames));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        for (int i = 0; i < checked.length; i++) {
            listView.setItemChecked(i, checked[i]);
        }
        
        btnConfirm.setOnClickListener(v -> {
            selectedItems.clear();
            for (int i = 0; i < listView.getCount(); i++) {
                if (listView.isItemChecked(i)) {
                    selectedItems.add(itemKeys.get(i));
                }
            }
            saveSettings();
            dialog.dismiss();
        });
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
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
            if (parts.length >= 6) {
                try {
                    float coolantTemp = Float.parseFloat(parts[0]);
                    float speed = Float.parseFloat(parts[1]);
                    float rpm = Float.parseFloat(parts[2]);
                    float battery = Float.parseFloat(parts[3]);
                    float instantFuel = Float.parseFloat(parts[4]);
                    float avgFuel = Float.parseFloat(parts[5]);
                    float throttle = parts.length > 6 ? Float.parseFloat(parts[6]) : 0;
                    float engineLoad = parts.length > 7 ? Float.parseFloat(parts[7]) : 0;
                    
                    tvSpeedValue.setText(String.format(Locale.US, "%.0f", speed));
                    if (speed > 120) {
                        tvSpeedValue.setTextColor(0xFFF44336);
                    } else if (speed > 80) {
                        tvSpeedValue.setTextColor(0xFFFF9800);
                    } else {
                        tvSpeedValue.setTextColor(0xFF4CAF50);
                    }
                    
                    tvRpmValue.setText(String.format(Locale.US, "%.0f", rpm));
                    if (rpm > 5000) {
                        tvRpmValue.setTextColor(0xFFF44336);
                    } else if (rpm > 4000) {
                        tvRpmValue.setTextColor(0xFFFF9800);
                    } else {
                        tvRpmValue.setTextColor(0xFFFFFFFF);
                    }
                    
                    updateLeftItem("coolant_temp", coolantTemp);
                    updateLeftItem("avg_fuel", avgFuel);
                    updateLeftItem("instant_fuel", instantFuel);
                    updateLeftItem("battery", battery);
                    updateLeftItem("throttle", throttle);
                    updateLeftItem("engine_load", engineLoad);
                    
                } catch (NumberFormatException e) {
                    // 忽略
                }
            }
        });
    }
    
    private void updateLeftItem(String key, float value) {
        DataItemView item = activeItems.get(key);
        if (item != null) {
            item.updateValue(value);
        }
    }
    
    @Override
    public void onConnectionChanged(boolean connected) {
        runOnUiThread(() -> {
            Toast.makeText(this, connected ? "OBD已连接" : "OBD已断开", Toast.LENGTH_SHORT).show();
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
    
    private static class DataItemInfo {
        String icon;
        String name;
        String unit;
        
        DataItemInfo(String icon, String name, String unit) {
            this.icon = icon;
            this.name = name;
            this.unit = unit;
        }
    }
}
