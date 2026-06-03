package com.obd.window;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class DynamicIslandService extends Service implements OBDDataListener {

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View floatingView;
    private LinearLayout contentLayout;
    private HorizontalScrollView scrollView;
    
    private Map<String, DataItem> dataItems = new HashMap<>();
    private Map<String, TextView> itemViews = new HashMap<>();
    
    private int screenWidth;
    private int screenHeight;
    private int islandHeight = 50;
    private int islandWidth;
    
    private boolean isExpanded = true;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private OBDDataManager dataManager;
    
    private float initialTouchX, initialTouchY;
    private int initialParamsX, initialParamsY;
    private boolean isDragging = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        dataManager = OBDDataManager.getInstance();
        dataManager.addListener(this);
        
        getScreenSize();
        initDataItems();
        loadSelectedItems();
        createFloatingWindow();
        
        dataManager.startCollecting();
        Toast.makeText(this, "悬浮窗已启动", Toast.LENGTH_SHORT).show();
    }
    
    private void getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        islandWidth = screenWidth;
    }
    
    private void initDataItems() {
        dataItems.put("coolant_temp", new DataItem("🌡️", "水温", "°C", true));
        dataItems.put("speed", new DataItem("🏎️", "车速", "km/h", true));
        dataItems.put("rotate_speed", new DataItem("⚙️", "转速", "rpm", false));
        dataItems.put("battery", new DataItem("🔋", "电压", "V", true));
        dataItems.put("realtime_fuel", new DataItem("⛽", "瞬时油耗", "L/100km", false));
        dataItems.put("avg_fuel", new DataItem("📊", "平均油耗", "L/100km", false));
    }
    
    private void createFloatingWindow() {
        LinearLayout mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setBackgroundColor(Color.BLACK);
        
        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        
        scrollView = new HorizontalScrollView(this);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.setBackgroundColor(Color.BLACK);
        scrollView.addView(contentLayout);
        
        mainContainer.addView(scrollView);
        floatingView = mainContainer;
        
        setupTouchListener();
        setupWindowParams();
        windowManager.addView(floatingView, layoutParams);
        
        createDataItemViews();
    }
    
    private void setupWindowParams() {
        layoutParams = new WindowManager.LayoutParams(
            islandWidth,
            dpToPx(islandHeight),
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        );
        
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        layoutParams.x = 0;
        layoutParams.y = dpToPx(10);
    }
    
    private void setupTouchListener() {
        floatingView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    initialParamsX = layoutParams.x;
                    initialParamsY = layoutParams.y;
                    isDragging = false;
                    return true;
                    
                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getRawX() - initialTouchX;
                    float deltaY = event.getRawY() - initialTouchY;
                    
                    if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                        isDragging = true;
                        layoutParams.x = initialParamsX + (int) deltaX;
                        layoutParams.y = initialParamsY + (int) deltaY;
                        
                        layoutParams.x = Math.max(-screenWidth/2 + islandWidth/2, 
                                    Math.min(screenWidth/2 - islandWidth/2, layoutParams.x));
                        layoutParams.y = Math.max(0, Math.min(screenHeight - dpToPx(islandHeight), layoutParams.y));
                        
                        windowManager.updateViewLayout(floatingView, layoutParams);
                    }
                    return true;
                    
                case MotionEvent.ACTION_UP:
                    if (!isDragging) {
                        toggleExpand();
                    }
                    return true;
            }
            return false;
        });
    }
    
    private void createDataItemViews() {
        contentLayout.removeAllViews();
        itemViews.clear();
        
        for (Map.Entry<String, DataItem> entry : dataItems.entrySet()) {
            DataItem item = entry.getValue();
            if (item.selected) {
                TextView view = createDataItemView(item);
                contentLayout.addView(view);
                itemViews.put(entry.getKey(), view);
            }
        }
        
        if (contentLayout.getChildCount() == 0) {
            TextView emptyView = new TextView(this);
            emptyView.setText("⚡ 长按选择数据");
            emptyView.setTextColor(Color.parseColor("#888888"));
            emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            emptyView.setPadding(dpToPx(8), 0, dpToPx(8), 0);
            contentLayout.addView(emptyView);
        }
    }
    
    private TextView createDataItemView(DataItem item) {
        TextView textView = new TextView(this);
        textView.setTag(item.key);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setPadding(dpToPx(6), 0, dpToPx(6), 0);
        
        if (contentLayout.getChildCount() > 0) {
            View divider = new View(this);
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(dpToPx(1), dpToPx(16));
            dividerParams.setMargins(0, 0, 0, 0);
            divider.setLayoutParams(dividerParams);
            divider.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            contentLayout.addView(divider);
        }
        
        textView.setOnLongClickListener(v -> {
            showDataSelector();
            return true;
        });
        
        textView.setText(item.icon + " --" + item.unit);
        return textView;
    }
    
    private void showDataSelector() {
        String[] items = new String[dataItems.size()];
        boolean[] checked = new boolean[dataItems.size()];
        String[] keys = new String[dataItems.size()];
        
        int i = 0;
        for (Map.Entry<String, DataItem> entry : dataItems.entrySet()) {
            keys[i] = entry.getKey();
            items[i] = entry.getValue().icon + " " + entry.getValue().name;
            checked[i] = entry.getValue().selected;
            i++;
        }
        
        new android.app.AlertDialog.Builder(this)
            .setTitle("选择显示的数据")
            .setMultiChoiceItems(items, checked, (dialog, which, isChecked) -> {
                String key = keys[which];
                DataItem item = dataItems.get(key);
                if (item != null) {
                    item.selected = isChecked;
                }
            })
            .setPositiveButton("确定", (dialog, which) -> {
                refreshDisplay();
                saveSelectedItems();
            })
            .setNegativeButton("取消", null)
            .show();
    }
    
    private void refreshDisplay() {
        createDataItemViews();
    }
    
    private void saveSelectedItems() {
        android.content.SharedPreferences prefs = getSharedPreferences("obd_settings", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        for (Map.Entry<String, DataItem> entry : dataItems.entrySet()) {
            editor.putBoolean("show_" + entry.getKey(), entry.getValue().selected);
        }
        editor.apply();
    }
    
    private void loadSelectedItems() {
        android.content.SharedPreferences prefs = getSharedPreferences("obd_settings", MODE_PRIVATE);
        for (Map.Entry<String, DataItem> entry : dataItems.entrySet()) {
            boolean defaultValue = entry.getValue().selected;
            boolean saved = prefs.getBoolean("show_" + entry.getKey(), defaultValue);
            entry.getValue().selected = saved;
        }
    }
    
    @Override
    public void onDataReceived(String data) {
        mainHandler.post(() -> updateAllData(data));
    }
    
    private void updateAllData(String dataStr) {
        String[] parts = dataStr.split(",");
        if (parts.length >= 6) {
            float temp = Float.parseFloat(parts[0]);
            float speed = Float.parseFloat(parts[1]);
            float rotate = Float.parseFloat(parts[2]);
            float battery = Float.parseFloat(parts[3]);
            float fuelRealtime = Float.parseFloat(parts[4]);
            float fuelAvg = Float.parseFloat(parts[5]);
            
            updateItemView("coolant_temp", temp, "%.1f");
            updateItemView("speed", speed, "%.0f");
            updateItemView("rotate_speed", rotate, "%.0f");
            updateItemView("battery", battery, "%.1f");
            updateItemView("realtime_fuel", fuelRealtime, "%.1f");
            updateItemView("avg_fuel", fuelAvg, "%.1f");
            
            checkAndWarn(temp, rotate, battery);
        }
    }
    
    private void updateItemView(String key, float value, String format) {
        TextView view = itemViews.get(key);
        DataItem item = dataItems.get(key);
        if (view != null && item != null && item.selected) {
            String valueStr = String.format(java.util.Locale.US, format, value);
            view.setText(item.icon + " " + valueStr + item.unit);
            setItemColor(key, view, value);
        }
    }
    
    private void setItemColor(String key, TextView view, float value) {
        switch (key) {
            case "coolant_temp":
                if (value > 100) view.setTextColor(Color.RED);
                else if (value > 90) view.setTextColor(Color.YELLOW);
                else view.setTextColor(Color.WHITE);
                break;
            case "rotate_speed":
                if (value > 5000) view.setTextColor(Color.RED);
                else if (value > 4000) view.setTextColor(Color.YELLOW);
                else view.setTextColor(Color.WHITE);
                break;
            case "battery":
                if (value < 11.5) view.setTextColor(Color.RED);
                else if (value < 12.0) view.setTextColor(Color.YELLOW);
                else view.setTextColor(Color.WHITE);
                break;
            default:
                view.setTextColor(Color.WHITE);
        }
    }
    
    private void checkAndWarn(float temp, float rotate, float battery) {
        StringBuilder warning = new StringBuilder();
        if (temp > 100) warning.append("🔥水温过高！ ");
        if (rotate > 5000) warning.append("⚠️转速过高！ ");
        if (battery < 11.5) warning.append("🔋电压过低！ ");
        if (warning.length() > 0) {
            Toast.makeText(this, warning.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void toggleExpand() {
        isExpanded = !isExpanded;
        int newHeight = dpToPx(isExpanded ? islandHeight : 32);
        layoutParams.height = newHeight;
        windowManager.updateViewLayout(floatingView, layoutParams);
        scrollView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }
    
    public void resizeIsland(int heightDp) {
        islandHeight = Math.max(35, Math.min(60, heightDp));
        layoutParams.height = dpToPx(islandHeight);
        if (windowManager != null && floatingView != null) {
            windowManager.updateViewLayout(floatingView, layoutParams);
        }
    }
    
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    
    @Override
    public void onConnectionChanged(boolean connected) {
        mainHandler.post(() -> Toast.makeText(this, connected ? "OBD已连接" : "OBD已断开", Toast.LENGTH_SHORT).show());
    }
    
    @Override
    public void onError(String error) {
        mainHandler.post(() -> Toast.makeText(this, "错误: " + error, Toast.LENGTH_SHORT).show());
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dataManager != null) {
            dataManager.removeListener(this);
            dataManager.stopCollecting();
        }
        if (floatingView != null && windowManager != null) {
            windowManager.removeView(floatingView);
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private static class DataItem {
        String icon, name, unit;
        boolean selected;
        DataItem(String icon, String name, String unit, boolean selected) {
            this.icon = icon; this.name = name; this.unit = unit; this.selected = selected;
        }
    }
}
