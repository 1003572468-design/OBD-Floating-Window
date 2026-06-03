package com.obd.dashboard;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DataItemView extends LinearLayout {

    private TextView tvIcon;
    private TextView tvValue;
    private TextView tvUnit;
    private String dataKey;

    public DataItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setPadding(dpToPx(2), dpToPx(2), dpToPx(2), dpToPx(2));
        setMinimumHeight(dpToPx(32));
        setBackgroundColor(0xFF000000);  // 纯黑不透明背景
        
        tvIcon = new TextView(context);
        tvIcon.setTextSize(12);
        tvIcon.setTextColor(0xFFFFFFFF);
        tvIcon.setGravity(Gravity.CENTER);
        addView(tvIcon, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        tvValue = new TextView(context);
        tvValue.setTextSize(14);
        tvValue.setTextColor(0xFFFFFFFF);
        tvValue.setTypeface(null, android.graphics.Typeface.BOLD);
        tvValue.setGravity(Gravity.CENTER);
        tvValue.setIncludeFontPadding(false);
        addView(tvValue, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        tvUnit = new TextView(context);
        tvUnit.setTextSize(8);
        tvUnit.setTextColor(0x88888888);
        tvUnit.setGravity(Gravity.CENTER);
        addView(tvUnit, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
    
    public void setData(String key, String icon, String unit, float value) {
        this.dataKey = key;
        tvIcon.setText(icon);
        tvValue.setText(formatValue(value));
        tvUnit.setText(unit);
        setColorByValue(key, value);
    }
    
    public void updateValue(float value) {
        tvValue.setText(formatValue(value));
        setColorByValue(dataKey, value);
    }
    
    private String formatValue(float value) {
        if (dataKey.equals("coolant_temp")) {
            return String.format(java.util.Locale.US, "%.0f", value);
        } else if (dataKey.equals("avg_fuel") || dataKey.equals("instant_fuel")) {
            return String.format(java.util.Locale.US, "%.1f", value);
        } else if (dataKey.equals("battery")) {
            return String.format(java.util.Locale.US, "%.1f", value);
        }
        return String.format(java.util.Locale.US, "%.0f", value);
    }
    
    private void setColorByValue(String key, float value) {
        int color = 0xFFFFFFFF;
        if (key.equals("coolant_temp")) {
            if (value > 100) color = 0xFFF44336;
            else if (value > 90) color = 0xFFFF9800;
        } else if (key.equals("battery")) {
            if (value < 11.5) color = 0xFFF44336;
            else if (value < 12.0) color = 0xFFFF9800;
        }
        tvValue.setTextColor(color);
    }
    
    private int dpToPx(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density);
    }
}
