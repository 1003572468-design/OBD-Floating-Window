package com.ileja.aicar.obd.impl.dina;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import com.cpsdna.obdports.ports.SerialPrefence;
import com.cpsdna.obdports.ports.SerialService;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aicar.obd.AIObdListener;
import com.ileja.aicar.obd.impl.AIObdClient;
import com.ileja.core.p007sp.BasePrefProvider;
import com.ileja.core.p007sp.IMPSharedPreference;
import com.ileja.module.BaseModuleApplication;

/* JADX INFO: loaded from: classes.dex */
public class AIDinaObdClient extends AIObdClient implements IMPSharedPreference.LeJaOnSharedPreferenceChangeListener {
    private static final String TAG = "AIDinaObdClient";

    /* JADX INFO: renamed from: b */
    SerialService.SerialBinder f5955b;
    private boolean hasClearAction = false;
    private ServiceConnection conn = new ServiceConnection() { // from class: com.ileja.aicar.obd.impl.dina.AIDinaObdClient.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AIDinaObdClient.this.f5955b = (SerialService.SerialBinder) iBinder;
            String carDisplacement = BasePrefProvider.getCarDisplacement(BaseModuleApplication.getApplication());
            AILog.m4029d("AIOBD", "设置排量:" + carDisplacement, LogLevel.RELEASE);
            AIDinaObdClient.this.f5955b.getService().setCarDisplacement(AIDinaObdClient.this.formatDisplacement(carDisplacement));
            int oilMethod = BasePrefProvider.getOilMethod(BaseModuleApplication.getApplication());
            AILog.m4029d("AIOBD", "设置油耗计算方法:" + oilMethod, LogLevel.RELEASE);
            AIDinaObdReceiver.oilMethod = oilMethod;
            String vinCode = AIDinaObdClient.this.f5955b.getService().getVinCode();
            if (!TextUtils.isEmpty(vinCode)) {
                BasePrefProvider.setCarVinCode(BaseModuleApplication.getApplication(), vinCode);
            }
            AILog.m4029d("AIOBD", "首次获取 OBD_VIN_CODE = " + vinCode, LogLevel.RELEASE);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            AIDinaObdClient.this.f5955b = null;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public float formatDisplacement(String str) {
        if (TextUtils.isEmpty(str)) {
            return 1.6f;
        }
        char cCharAt = str.charAt(str.length() - 1);
        if (cCharAt < 0 && cCharAt > '\t') {
            str = str.substring(0, str.length() - 2);
        }
        try {
            return Float.valueOf(str).floatValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 1.6f;
        }
    }

    private void startService() {
        AILog.m4029d(TAG, "startService", LogLevel.RELEASE);
        SerialPrefence.setSerial(BaseModuleApplication.getContext(), "/dev/ttyMT1", 19200);
        BaseModuleApplication.getContext().bindService(new Intent(BaseModuleApplication.getContext(), (Class<?>) SerialService.class), this.conn, 1);
    }

    private void stopService() {
        BaseModuleApplication.getContext().unbindService(this.conn);
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void close() {
        stopService();
        BasePrefProvider.unregisterOnSharedPreferenceChangeListener(BaseModuleApplication.getApplication(), this);
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void create() {
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void destroy() {
    }

    @Override // com.ileja.core.sp.IMPSharedPreference.LeJaOnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(String str) {
        if (BasePrefProvider.KEY_OBD_ID.equals(str) && !this.hasClearAction) {
            if (this.f5955b != null) {
                AILog.m4029d("AIOBD", "执行清理***********", LogLevel.RELEASE);
                this.f5955b.getService().clearMileMeterAndOil();
                this.hasClearAction = true;
                return;
            }
            return;
        }
        if (BasePrefProvider.KEY_CAR_DISPLACEMENT.equals(str)) {
            if (this.f5955b != null) {
                String carDisplacement = BasePrefProvider.getCarDisplacement(BaseModuleApplication.getApplication());
                AILog.m4029d("AIOBD", "设置排量:" + carDisplacement, LogLevel.RELEASE);
                this.f5955b.getService().setCarDisplacement(formatDisplacement(carDisplacement));
                return;
            }
            return;
        }
        if (!BasePrefProvider.KEY_OIL_METHOD.equals(str) || this.f5955b == null) {
            return;
        }
        int oilMethod = BasePrefProvider.getOilMethod(BaseModuleApplication.getApplication());
        AILog.m4029d("AIOBD", "设置油耗计算方法:" + oilMethod, LogLevel.RELEASE);
        AIDinaObdReceiver.oilMethod = oilMethod;
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void open() {
        startService();
        BasePrefProvider.registerOnSharedPreferenceChangeListener(BaseModuleApplication.getApplication(), this);
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void registerListener(AIObdListener aIObdListener) {
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void sendCommand(String str) {
    }

    @Override // com.ileja.aicar.obd.impl.AIObdClient
    public void unregisterListener(AIObdListener aIObdListener) {
    }
}