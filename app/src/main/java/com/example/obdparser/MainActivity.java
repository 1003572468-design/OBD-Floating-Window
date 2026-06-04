package com.example.obdparser;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ileja.aicore.data.IDataCallback;
import com.ileja.aicore.data.IDataRepertory;

public class MainActivity extends AppCompatActivity {
    
    private TextView tvLog;
    private IDataRepertory dataRepertory;
    private boolean isBound = false;
    
    private IDataCallback callback = new IDataCallback.Stub() {
        @Override
        public void callback(String filter, Bundle data) throws RemoteException {
            String text = tvLog.getText().toString();
            StringBuilder sb = new StringBuilder(text);
            sb.append("\n\n📡 收到回调: ").append(filter);
            
            if (data != null) {
                for (String key : data.keySet()) {
                    sb.append("\n   ").append(key).append(" = ").append(data.get(key));
                }
            }
            
            runOnUiThread(() -> tvLog.setText(sb.toString()));
        }
    };
    
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            String text = tvLog.getText().toString();
            tvLog.setText(text + "\n✅ 服务已连接");
            
            dataRepertory = IDataRepertory.Stub.asInterface(service);
            isBound = true;
            
            try {
                dataRepertory.register("com.ileja.aicar.obd.real", callback);
                tvLog.setText(tvLog.getText() + "\n📝 已注册实时数据监听");
            } catch (RemoteException e) {
                tvLog.setText(tvLog.getText() + "\n❌ 注册失败: " + e.getMessage());
            }
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            tvLog.setText(tvLog.getText() + "\n⚠️ 服务断开");
            dataRepertory = null;
            isBound = false;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tvLog = new TextView(this);
        tvLog.setText("正在连接 AICore 服务...\n");
        tvLog.setTextSize(14);
        tvLog.setPadding(20, 20, 20, 20);
        setContentView(tvLog);
        
        Intent intent = new Intent();
        intent.setClassName("com.ileja.aicore", "com.ileja.aicore.data.DataRepertoryService");
        boolean result = bindService(intent, connection, Context.BIND_AUTO_CREATE);
        
        tvLog.setText(tvLog.getText() + "绑定结果: " + result + "\n");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound && dataRepertory != null) {
            try {
                dataRepertory.unregister("com.ileja.aicar.obd.real", callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(connection);
        }
    }
}
