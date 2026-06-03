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
    
    private Map<String, DataItemView> activeItems = new HashMap<>();
    private List<String> selectedItems = new ArrayList<>();
    
    // 可用的数据项配置
    private final String[] ITEM_KEYS = {"coolant_temp", "avg_fuel", "instant_fuel", "battery", "throttle", "engine_load"};
    private final String[] ITEM_ICONS = {"🌡️", "⛽", "⚡", "🔋", "🚦", "💪"};
    private final String[] ITEM_NAMES = {"水温", "平均油耗", "瞬时油耗", "电池电压", "节气门开度", "发动机负荷"};
    private final String[] ITEM_UNITS = {"°C", "L/100km", "L/100km", "V", "%", "%"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        loadSettings();
        
        dataManager = OBDDataManager.getInstance();
        dataManager.addListener(this);
        dataManager.init();
        
        startTimeUpdate();
        
        // 长按左侧区域打开选择器
        llLeftArea.setOnLongClickListener(v -> {
            openItemSelector();
            return true;
        });
        
        // 点击时间也打开选择器
        tvTimeValue.setOnClickListener(v -> openItemSelector());
    }
    
    private void initViews() {
        llLeftArea = findViewById(R.id.ll_left_area);
        tvSpeedValue = findViewById(R.id.tv_speed_value);
        tvRpmValue = findViewById(R.id.tv_rpm_value);
        tvTimeValue = findViewById(R.id.tv_time_value);
    }
    
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("dashboard_settings", MODE_PRIVATE);
        String selectedStr = prefs.getString("selected_items", "coolant_temp,avg_fuel,battery");
        selectedItems.clear();
        for (String key : selectedStr.split(",")) {
            selectedItems.add(key);
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
            // 找到对应的索引
            int index = -1;
            for (int i = 0; i < ITEM_KEYS.length; i++) {
                if (ITEM_KEYS[i].equals(key)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                DataItemView itemView = new DataItemView(this);
                itemView.setData(key, ITEM_ICONS[index], ITEM_UNITS[index], 0);
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
        
        // 构建显示名称
        String[] displayNames = new String[ITEM_KEYS.length];
        for (int i = 0; i < ITEM_KEYS.length; i++) {
            displayNames[i] = ITEM_ICONS[i] + " " + ITEM_NAMES[i];
        }
        
        // 当前选中状态
        final boolean[] checked = new boolean[ITEM_KEYS.length];
        for (int i = 0; i < ITEM_KEYS.length; i++) {
            checked[i] = selectedItems.contains(ITEM_KEYS[i]);
        }
        
        final AlertDialog dialog = builder.create();
        
        // 设置 ListView
        listView.setAdapter(new android.widget.ArrayAdapter<>(this, 
            android.R.layout.simple_list_item_multiple_choice, displayNames));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        for (int i = 0; i < checked.length; i++) {
            listView.setItemChecked(i, checked[i]);
        }
        
        // 监听选中变化
        listView.setOnItemClickListener((parent, view, position, id) -> {
            checked[position] = listView.isItemChecked(position);
        });
        
        // 确定按钮
        btnConfirm.setOnClickListener(v -> {
            selectedItems.clear();
            for (int i = 0; i < ITEM_KEYS.length; i++) {
                if (checked[i]) {
                    selectedItems.add(ITEM_KEYS[i]);
                }
            }
            saveSettings();
            dialog.dismiss();
            Toast.makeText(this, "显示内容已更新", Toast.LENGTH_SHORT).show();
        });
        
        // 取消按钮
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
                    
                    // 更新速度
                    tvSpeedValue.setText(String.format(Locale.US, "%.0f", speed));
                    if (speed > 120) {
                        tvSpeedValue.setTextColor(0xFFF44336);
                    } else if (speed > 80) {
                        tvSpeedValue.setTextColor(0xFFFF9800);
                    } else {
                        tvSpeedValue.setTextColor(0xFF4CAF50);
                    }
                    
                    // 更新转速
                    tvRpmValue.setText(String.format(Locale.US, "%.0f", rpm));
                    if (rpm > 5000) {
                        tvRpmValue.setTextColor(0xFFF44336);
                    } else if (rpm > 4000) {
                        tvRpmValue.setTextColor(0xFFFF9800);
                    } else {
                        tvRpmValue.setTextColor(0xFFFFFFFF);
                    }
                    
                    // 更新左侧各项
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
}
