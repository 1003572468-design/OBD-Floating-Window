package android.serialport.sample;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends SerialPortActivity {
    
    private TextView statusText;      // 状态信息
    private TextView dataText;        // 数据显示区
    private Handler handler = new Handler();
    private StringBuilder allData = new StringBuilder();  // 累积所有数据
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        statusText = findViewById(R.id.statusText);
        dataText = findViewById(R.id.dataText);
        
        statusText.setText("正在打开串口 /dev/ttyMT1 ...");
        
        // 延迟1秒打开串口
        handler.postDelayed(this::openSerialPort, 1000);
    }
    
    private void openSerialPort() {
        try {
            SerialPort serialPort = new SerialPort(new File("/dev/ttyMT1"), 19200, 0);
            setSerialPort(serialPort);
            statusText.setText("串口已打开，监听中...\n发动汽车后应该会收到数据");
            dataText.setText("等待数据...\n\n提示：\n1. 请发动汽车\n2. 踩油门试试\n3. 数据会自动显示在这里");
            
        } catch (Exception e) {
            e.printStackTrace();
            statusText.setText("串口打开失败: " + e.getMessage());
            dataText.setText("错误: " + e.toString());
        }
    }
    
    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        final String hexData = bytesToHex(buffer, size);
        final String asciiData = new String(buffer, 0, size);
        
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 更新状态栏（显示最新收到的数据）
                statusText.setText("收到 " + size + " 字节\nHEX: " + hexData);
                
                // 累积显示所有数据
                String time = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
                allData.insert(0, "[" + time + "] " + hexData + "\n");
                
                // 限制显示行数（最多50行）
                int lines = allData.toString().split("\n").length;
                if (lines > 50) {
                    int lastIndex = allData.lastIndexOf("\n");
                    if (lastIndex > 0) {
                        allData.delete(lastIndex, allData.length());
                    }
                }
                
                dataText.setText(allData.toString());
                
                // 同时打印到 logcat，方便用 adb 查看
                android.util.Log.d("OBD_RAW", "HEX: " + hexData);
                android.util.Log.d("OBD_RAW", "ASCII: " + asciiData);
            }
        });
    }
    
    private String bytesToHex(byte[] bytes, int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString().trim();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
