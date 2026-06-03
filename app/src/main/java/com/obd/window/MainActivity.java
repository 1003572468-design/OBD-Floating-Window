package com.obd.window;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private static final int OVERLAY_PERMISSION_REQUEST = 1001;
    
    private Button btnStartService, btnStopService, btnSelectData;
    private SeekBar seekBarSize;
    private TextView tvSizeValue, tvStatus;
    private boolean isServiceRunning = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        checkOverlayPermission();
    }
    
    private void initViews() {
        btnStartService = findViewById(R.id.btn_start_service);
        btnStopService = findViewById(R.id.btn_stop_service);
        btnSelectData = findViewById(R.id.btn_select_data);
        seekBarSize = findViewById(R.id.seekbar_size);
        tvSizeValue = findViewById(R.id.tv_size_value);
        tvStatus = findViewById(R.id.tv_status);
        
        btnStartService.setOnClickListener(v -> startIslandService());
        btnStopService.setOnClickListener(v -> stopIslandService());
        btnSelectData.setOnClickListener(v -> showDataSelector());
        
        seekBarSize.setProgress(50);
        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSizeValue.setText(progress + " dp");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        updateStatus();
    }
    
    private void startIslandService() {
        if (hasOverlayPermission()) {
            Intent intent = new Intent(this, DynamicIslandService.class);
            startService(intent);
            isServiceRunning = true;
            Toast.makeText(this, "悬浮窗已启动", Toast.LENGTH_SHORT).show();
            updateStatus();
        } else {
            requestOverlayPermission();
        }
    }
    
    private void stopIslandService() {
        Intent intent = new Intent(this, DynamicIslandService.class);
        stopService(intent);
        isServiceRunning = false;
        Toast.makeText(this, "悬浮窗已关闭", Toast.LENGTH_SHORT).show();
        updateStatus();
    }
    
    private void showDataSelector() {
        if (isServiceRunning) {
            Toast.makeText(this, "请在悬浮窗上长按选择数据", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请先启动悬浮窗服务", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void checkOverlayPermission() {
        if (!hasOverlayPermission()) {
            new AlertDialog.Builder(this)
                .setTitle("需要悬浮窗权限")
                .setMessage("悬浮窗功能需要权限，请在设置中允许")
                .setPositiveButton("去设置", (d, w) -> requestOverlayPermission())
                .setNegativeButton("取消", null)
                .show();
        }
    }
    
    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }
    
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST);
        }
    }
    
    private void updateStatus() {
        tvStatus.setText("状态: " + (isServiceRunning ? "运行中" : "未启动"));
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST && hasOverlayPermission()) {
            Toast.makeText(this, "权限已获取", Toast.LENGTH_SHORT).show();
        }
    }
}
