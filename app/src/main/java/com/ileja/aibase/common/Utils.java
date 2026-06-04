package com.ileja.aibase.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.aispeech.aios.TtsSupportNode;
import com.ileja.aibase.AiBasePrefHelper;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.phone.DeviceUtil;
import com.ileja.commons.C1300R;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public class Utils {
    private static final int DAY_END = 18;
    private static final int DAY_START = 6;
    public static final String TAG = "DeviceUtil";
    static boolean isRecordWork = false;
    static Object mCameraRecordStateLock = new Object();

    public static boolean checkNumberIsEnergyCar(String str) {
        return !TextUtils.isEmpty(str) && str.matches("^[一-龥]{1}[A-Z]{1}[A-Z_0-9]{6}$");
    }

    public static int getAccState() {
        String systemConf = DeviceUtil.getSystemConf("persist.acc.on");
        if (TextUtils.isEmpty(systemConf)) {
            return 0;
        }
        return Integer.parseInt(systemConf);
    }

    public static byte[] getAssetsFileData(Context context, String str) throws IOException {
        InputStream inputStreamOpen = context.getAssets().open(str);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int i = inputStreamOpen.read(bArr);
            if (i <= 0) {
                inputStreamOpen.close();
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, i);
        }
    }

    public static String getAssetsFileString(Context context, String str) throws IOException {
        byte[] assetsFileData = getAssetsFileData(context, str);
        if (assetsFileData == null || assetsFileData.length <= 0) {
            return null;
        }
        return new String(assetsFileData);
    }

    public static boolean getCameraRecordState() {
        final File file = new File("/sys/class/tw9992mipi_debug/tw9992mipi_reg");
        isRecordWork = false;
        if (!file.exists()) {
            return isRecordWork;
        }
        Thread thread = new Thread(new Runnable() { // from class: com.ileja.aibase.common.Utils.1
            /* JADX WARN: Removed duplicated region for block: B:100:0x01b4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:98:0x01d9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() throws java.lang.Throwable {
                /*
                    Method dump skipped, instruction units count: 483
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.ileja.aibase.common.Utils.RunnableC10871.run():void");
            }
        });
        thread.setName("getCameraRecordState-Thread");
        thread.start();
        try {
            synchronized (mCameraRecordStateLock) {
                mCameraRecordStateLock.wait(2000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isRecordWork;
    }

    public static int getCoverScale(float f) {
        return ((int) (f * 255.0f)) << 24;
    }

    public static int getCurHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(11);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r2v7, types: [com.ileja.aibase.common.logger.LogLevel] */
    /* JADX WARN: Type inference failed for: r2v8 */
    private static int getFactoryKitMode() throws Throwable {
        File file = new File("/proc/boot_status");
        int i = 0;
        if (!file.exists()) {
            return 0;
        }
        ?? r2 = 0;
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    FileInputStream fileInputStream2 = new FileInputStream(file);
                    try {
                        i = fileInputStream2.read();
                        fileInputStream2.close();
                    } catch (IOException e) {
                        e = e;
                        fileInputStream = fileInputStream2;
                        AILog.m4040w(TAG, "getFactoryKitMode:Failure reading /proc/boot_status:" + Log.getStackTraceString(e));
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        r2 = LogLevel.RELEASE;
                        AILog.m4041w(TAG, "getFactoryKitMode:result:" + i, r2);
                        return i;
                    } catch (Throwable th) {
                        th = th;
                        r2 = fileInputStream2;
                        if (r2 != 0) {
                            try {
                                r2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e3) {
                    e = e3;
                }
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            r2 = LogLevel.RELEASE;
            AILog.m4041w(TAG, "getFactoryKitMode:result:" + i, r2);
            return i;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static String getRuninState() {
        return DeviceUtil.getSystemConf("persist.sys.runin");
    }

    public static String getSerialNumber(Context context) {
        String simSerialNumber = AiBasePrefHelper.getSimSerialNumber(context);
        if (simSerialNumber == null || simSerialNumber.length() <= 0) {
            return context.getString(C1300R.string.no_find_sim);
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(simSerialNumber.substring(0, 4));
            sb.append(StringUtils.SPACE);
            sb.append(simSerialNumber.substring(4, 8));
            sb.append(StringUtils.SPACE);
            sb.append(simSerialNumber.substring(8, 12));
            sb.append(StringUtils.SPACE);
            sb.append(simSerialNumber.substring(12, 16));
            sb.append(StringUtils.SPACE);
            sb.append(simSerialNumber.substring(16, simSerialNumber.length()));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return context.getString(C1300R.string.read_sim_error) + simSerialNumber;
        }
    }

    public static String getYYYYMMDDHHMM(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmm").format(date);
    }

    public static boolean isDay(double d, double d2, long j) {
        if (d <= 1.0d) {
            d = 116.33d;
        }
        double d3 = d;
        if (d2 <= 1.0d) {
            d2 = 39.42d;
        }
        return CalcSunRiseAndSunSetTools.isDay(d3, d2, j);
    }

    public static boolean isDayInBeijing() {
        return isDay(0.0d, 0.0d, System.currentTimeMillis());
    }

    public static boolean isKillMode() throws Throwable {
        int factoryKitMode = getFactoryKitMode();
        if (factoryKitMode != 0 && factoryKitMode != 48) {
            Log.d(TAG, "工厂模式不开启");
            return true;
        }
        if (TtsSupportNode.REVERSE.equals(getRuninState())) {
            Log.d(TAG, "test mode");
            return true;
        }
        if (!TtsSupportNode.REVERSE.equals(DeviceUtil.getSystemConf("lejia_app_upgrade_running"))) {
            return false;
        }
        Log.d(TAG, "install mode");
        return true;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0071 -> B:36:0x0074). Please report as a decompilation issue!!! */
    public static void setLedState(boolean z) throws Throwable {
        File file = new File("/proc/irleds_switch");
        if (!file.exists()) {
            AILog.m4041w(TAG, "/proc/irleds_switch not exist", LogLevel.RELEASE);
            return;
        }
        BufferedWriter bufferedWriter = null;
        try {
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(file));
                    try {
                        AILog.m4040w(TAG, "/proc/irleds_switch set to " + z);
                        if (z) {
                            bufferedWriter2.write(49);
                        } else {
                            bufferedWriter2.write(48);
                        }
                        bufferedWriter2.close();
                    } catch (IOException e) {
                        e = e;
                        bufferedWriter = bufferedWriter2;
                        AILog.m4041w(TAG, "/proc/irleds_switch file" + Log.getStackTraceString(e), LogLevel.RELEASE);
                        if (bufferedWriter == null) {
                        } else {
                            bufferedWriter.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException e3) {
                e = e3;
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    public static void systemFotaUpgrade() {
        DeviceUtil.setSystemConf("persist.leja.sys.recovery", TtsSupportNode.REVERSE);
    }

    public static void systemGC() {
        DeviceUtil.setSystemConf("persist.leja.sys.gc", TtsSupportNode.REVERSE);
    }

    public static void systemReboot() {
        DeviceUtil.setSystemConf("persist.leja.sys.reboot", TtsSupportNode.REVERSE);
    }

    public static void systemReset() {
        DeviceUtil.setSystemConf("persist.leja.sys.reset", TtsSupportNode.REVERSE);
    }

    public static void systemResetGPS() {
        DeviceUtil.setSystemConf("persist.leja.sys.resetgps", TtsSupportNode.REVERSE);
    }
}