package com.ileja.aibase.phone;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.ileja.aibase.AiBase;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.FileUtil;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.config.HttpConfig;
import com.ileja.aibase.config.ServerConfig;
import com.ileja.aibase.utils.SystemPropertiesProxy;
import com.tendcloud.tenddata.C1580cl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public final class DeviceUtil {
    private static final String BLINKSDKS_PROP = "persist.sys.start.blink";
    private static final String BT_PROP_DISABLE = "0";
    private static final String BT_PROP_ENABLE = "1";
    private static final String GOCSDKS_PROP = "persist.sys.start.gocsdks";
    private static final String TAG = "DeviceUtil";

    public enum CarrobotChannel {
        C2E("C2E"),
        C2S("C2S"),
        C2M("C2M");

        private String channel;

        CarrobotChannel(String str) {
            this.channel = str;
        }

        public String getMessage() {
            return this.channel;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.channel;
        }
    }

    public static void changeLightValueByLevel(int i) throws Throwable {
        AILog.m4029d("DeviceUtil", " changeLightValueByLevel " + i, LogLevel.RELEASE);
        String serialNumber = getSerialNumber();
        if (i == 1) {
            if (serialNumber.startsWith("P16")) {
                write2DDP_PWM("38");
                return;
            } else {
                write2DDP_PWM("37");
                return;
            }
        }
        if (i == 2) {
            write2DDP_PWM("39");
        } else if (i == 3) {
            write2DDP_PWM("44");
        }
    }

    public static synchronized String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getAppAndRomVersion(Context context, String str) {
        if (getInstalledAppInfo(context, str) == null) {
            return getShowhqRomVersion();
        }
        return getInstalledAppInfo(context, str).versionName + "_" + getShowhqRomVersion();
    }

    public static String getBacklightTemperature() {
        try {
            String serialNumber = getSerialNumber();
            if (TextUtils.isEmpty(serialNumber)) {
                return "";
            }
            String str = null;
            if (serialNumber.startsWith("C2MS")) {
                str = "/sys/devices/virtual/thermal/thermal_zone10/temp";
            } else if (serialNumber.startsWith("C2M")) {
                str = "/sys/devices/virtual/thermal/thermal_zone5/temp";
            }
            return str != null ? FileUtil.readFileContent(str) : "";
        } catch (Exception unused) {
            return "";
        }
    }

    public static String getBindDeviceChannel() {
        String str;
        String serialNumber = getSerialNumber();
        if (!TextUtils.isEmpty(serialNumber)) {
            if (serialNumber.startsWith("C2MS")) {
                return serialNumber.substring(0, 5);
            }
            if (serialNumber.startsWith("C2M")) {
                return serialNumber.substring(0, 4);
            }
            if (serialNumber.startsWith("C2E")) {
                return "C2E";
            }
            if (serialNumber.startsWith("C2S")) {
                return "C2S";
            }
            if (serialNumber.startsWith("MP")) {
                String str2 = Build.MODEL;
                if (!"LJ-C2-L".equals(str2)) {
                    str = "LJ-C2".equals(str2) ? "LJC2" : "LJC2L";
                }
                return str;
            }
            if (serialNumber.startsWith("HI")) {
                return "HI";
            }
        }
        return null;
    }

    public static int getCPUNumCores() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() { // from class: com.ileja.aibase.phone.DeviceUtil.1
                @Override // java.io.FileFilter
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]", file.getName());
                }
            }).length;
        } catch (Exception unused) {
            return 1;
        }
    }

    public static String getCPUTemperature() {
        try {
            return FileUtil.readFileContent("/sys/devices/virtual/thermal/thermal_zone1/temp");
        } catch (Exception unused) {
            return "";
        }
    }

    public static synchronized CarrobotChannel getCarrobotChannel() {
        CarrobotChannel carrobotChannel = CarrobotChannel.C2E;
        String serialNumber = getSerialNumber();
        if (!TextUtils.isEmpty(serialNumber)) {
            if (serialNumber.startsWith(CarrobotChannel.C2E.getMessage())) {
                carrobotChannel = CarrobotChannel.C2E;
            } else if (serialNumber.startsWith(CarrobotChannel.C2S.getMessage())) {
                carrobotChannel = CarrobotChannel.C2S;
            } else if (serialNumber.startsWith(CarrobotChannel.C2M.getMessage())) {
                carrobotChannel = CarrobotChannel.C2M;
            }
        }
        AILog.m4028d("DeviceUtil", "carrobotChannel : " + carrobotChannel);
        return CarrobotChannel.C2M;
    }

    public static String getCatSystemConf(String str) {
        try {
            Process processExec = Runtime.getRuntime().exec("cat " + str);
            InputStreamReader inputStreamReader = new InputStreamReader(processExec.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            bufferedReader.close();
            inputStreamReader.close();
            processExec.destroy();
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint({"all"})
    public static synchronized String getDeviceId(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null || TextUtils.isEmpty(telephonyManager.getDeviceId())) {
            telephonyManager = (TelephonyManager) context.getSystemService("phone1");
        }
        return telephonyManager != null ? telephonyManager.getDeviceId() : null;
    }

    public static String getHardwareVersion() {
        return getSystemConf("ro.hardware");
    }

    @SuppressLint({"HardwareIds"})
    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        return (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0 && telephonyManager != null) ? telephonyManager.getSubscriberId() : "";
    }

    public static PackageInfo getInstalledAppInfo(Context context, String str) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        if (installedPackages == null) {
            return null;
        }
        for (PackageInfo packageInfo : installedPackages) {
            if (packageInfo != null && packageInfo.packageName.equals(str)) {
                return packageInfo;
            }
        }
        return null;
    }

    public static String getMac(Context context) {
        WifiInfo connectionInfo;
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(HttpConfig.TAG.SET_WIFI);
            if (wifiManager == null || (connectionInfo = wifiManager.getConnectionInfo()) == null || connectionInfo.getMacAddress() == null) {
                return null;
            }
            return connectionInfo.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /* JADX WARN: Not initialized variable reg: 4, insn: 0x008a: MOVE (r3 I:??[OBJECT, ARRAY]) = (r4 I:??[OBJECT, ARRAY]), block:B:37:0x008a */
    /* JADX WARN: Removed duplicated region for block: B:48:0x008d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getMiotMacAddress() throws java.lang.Throwable {
        /*
            java.io.File r0 = new java.io.File
            java.lang.String r1 = "/sdcard/Android/factorytest/xmLicense.txt"
            r0.<init>(r1)
            boolean r1 = r0.exists()
            java.lang.String r2 = "DeviceUtil"
            r3 = 0
            if (r1 == 0) goto L96
            boolean r1 = r0.canRead()
            if (r1 != 0) goto L18
            goto L96
        L18:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L77 java.lang.Exception -> L79
            java.io.InputStreamReader r5 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L77 java.lang.Exception -> L79
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L77 java.lang.Exception -> L79
            r6.<init>(r0)     // Catch: java.lang.Throwable -> L77 java.lang.Exception -> L79
            java.lang.String r0 = "UTF-8"
            r5.<init>(r6, r0)     // Catch: java.lang.Throwable -> L77 java.lang.Exception -> L79
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L77 java.lang.Exception -> L79
        L2e:
            java.lang.String r0 = r4.readLine()     // Catch: java.lang.Exception -> L75 java.lang.Throwable -> L89
            if (r0 == 0) goto L38
            r1.add(r0)     // Catch: java.lang.Exception -> L75 java.lang.Throwable -> L89
            goto L2e
        L38:
            r4.close()     // Catch: java.io.IOException -> L3c
            goto L40
        L3c:
            r0 = move-exception
            r0.printStackTrace()
        L40:
            int r0 = r1.size()
            r4 = 2
            if (r0 != r4) goto L74
            r0 = 0
            java.lang.Object r0 = r1.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r0 = r0.trim()
            java.lang.String r1 = "\\|"
            java.lang.String[] r0 = r0.split(r1)
            int r1 = r0.length
            r5 = 3
            if (r1 != r5) goto L74
            r3 = r0[r4]
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "miot mac: "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            com.ileja.aibase.common.logger.LogLevel r1 = com.ileja.aibase.common.logger.LogLevel.RELEASE
            com.ileja.aibase.common.AILog.m4029d(r2, r0, r1)
        L74:
            return r3
        L75:
            r0 = move-exception
            goto L7b
        L77:
            r0 = move-exception
            goto L8b
        L79:
            r0 = move-exception
            r4 = r3
        L7b:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L89
            if (r4 == 0) goto L88
            r4.close()     // Catch: java.io.IOException -> L84
            goto L88
        L84:
            r0 = move-exception
            r0.printStackTrace()
        L88:
            return r3
        L89:
            r0 = move-exception
            r3 = r4
        L8b:
            if (r3 == 0) goto L95
            r3.close()     // Catch: java.io.IOException -> L91
            goto L95
        L91:
            r1 = move-exception
            r1.printStackTrace()
        L95:
            throw r0
        L96:
            com.ileja.aibase.common.logger.LogLevel r0 = com.ileja.aibase.common.logger.LogLevel.RELEASE
            java.lang.String r1 = "file not exist or read ."
            com.ileja.aibase.common.AILog.m4029d(r2, r1, r0)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ileja.aibase.phone.DeviceUtil.getMiotMacAddress():java.lang.String");
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getOSversion() {
        return Build.VERSION.RELEASE;
    }

    public static Context getOuterContext(Context context, String str) {
        try {
            return context.createPackageContext(str, 3);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPackageVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getPackageVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getRomSoftVersion() {
        String systemConf = getSystemConf("ro.function.code");
        if (TextUtils.isEmpty(systemConf)) {
            return 0;
        }
        return Integer.parseInt(systemConf);
    }

    public static String getRomVersion() {
        return getSystemConf("ro.mediatek.version.release");
    }

    public static synchronized String getSDcardID() {
        try {
            String[] strArr = {"/sys/block/mmcblk0", "/sys/block/mmcblk1", "/sys/block/mmcblk2"};
            for (int i = 0; i < 3; i++) {
                String str = strArr[i];
                File file = new File(str);
                if (file.exists() && file.isDirectory()) {
                    String line = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cat " + str + "/device/cid").getInputStream())).readLine();
                    if (!TextUtils.isEmpty(line)) {
                        return line;
                    }
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static float[] getScreenDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return new float[]{displayMetrics.density, displayMetrics.densityDpi, displayMetrics.scaledDensity};
    }

    public static int getScreenOffTimeOut(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "screen_off_timeout");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 15000;
        }
    }

    public static int[] getScreenResolution(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    public static Point getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static String getSerialNumber() {
        String str;
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            str = (String) cls.getMethod("get", String.class).invoke(cls, "ro.serialno");
        } catch (Exception unused) {
            str = null;
        }
        AILog.m4028d("DeviceUtil", "serialNumber : " + str);
        return str;
    }

    public static String getShowhqRomVersion() {
        String romVersion = getRomVersion();
        if (!TextUtils.isEmpty(romVersion)) {
            String[] strArrSplit = romVersion.split("_");
            if (strArrSplit.length >= 2) {
                String str = strArrSplit[1];
                if (strArrSplit.length >= 3) {
                    str = str + "_" + strArrSplit[2];
                }
                if (strArrSplit.length < 4) {
                    return str;
                }
                return str + "_" + strArrSplit[3];
            }
        }
        return "leja";
    }

    @SuppressLint({"HardwareIds"})
    public static String getSimSerialNum(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        return (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0 && telephonyManager != null) ? telephonyManager.getSimSerialNumber() : "";
    }

    public static synchronized String getSimSerialNumber(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            return "";
        }
        return telephonyManager != null ? telephonyManager.getSimSerialNumber() : "";
    }

    public static String getSystemConf(String str) {
        Application context = AiBase.getInst().getContext();
        if (context != null) {
            String str2 = SystemPropertiesProxy.get(context, str);
            AILog.m4028d("DeviceUtil", "getSystemConf configName = " + str + " , value = " + str2);
            return str2;
        }
        try {
            Process processExec = Runtime.getRuntime().exec("getprop " + str);
            InputStreamReader inputStreamReader = new InputStreamReader(processExec.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            bufferedReader.close();
            inputStreamReader.close();
            processExec.destroy();
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWifiBSSID(Context context) {
        WifiInfo connectionInfo;
        if (context == null || (connectionInfo = ((WifiManager) context.getSystemService(HttpConfig.TAG.SET_WIFI)).getConnectionInfo()) == null) {
            return null;
        }
        return connectionInfo.getBSSID();
    }

    public static String getWifiMacAddress() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b : hardwareAddress) {
                        sb.append(String.format("%02X:", Byte.valueOf(b)));
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }

    public static boolean isActivityForeground(Context context, String str) {
        List<ActivityManager.RunningTaskInfo> runningTasks;
        return (context == null || TextUtils.isEmpty(str) || (runningTasks = ((ActivityManager) context.getSystemService(C1580cl.a.f7824g)).getRunningTasks(1)) == null || runningTasks.size() <= 0 || !str.equals(runningTasks.get(0).topActivity.getClassName())) ? false : true;
    }

    public static boolean isAppOnForeground(Context context, String str) {
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(C1580cl.a.f7824g);
        String packageName = null;
        if (Build.VERSION.SDK_INT >= 21) {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            if (runningAppProcesses != null && runningAppProcesses.size() > 0 && (runningAppProcessInfo = runningAppProcesses.get(0)) != null && runningAppProcessInfo.importance == 100) {
                packageName = runningAppProcessInfo.processName;
            }
        } else {
            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
            if (runningTasks != null && runningTasks.size() > 0) {
                packageName = runningTasks.get(0).topActivity.getPackageName();
            }
        }
        Log.i("DeviceUtil", "sys.ver: " + Build.VERSION.SDK_INT + ", top-pkg: " + packageName + ", pkg: " + str);
        return TextUtils.equals(str, packageName);
    }

    public static boolean isHFPBT(Context context) {
        String str = SystemPropertiesProxy.get(context, GOCSDKS_PROP, "");
        String str2 = SystemPropertiesProxy.get(context, BLINKSDKS_PROP, "");
        AILog.m4029d("DeviceUtil", "gocProp = " + str + ", blinkProp = " + str2, LogLevel.RELEASE);
        return ("0".equals(str) || "0".equals(str2)) ? false : true;
    }

    public static boolean isNeedHandleAcc() {
        String serialNumber = getSerialNumber();
        boolean z = !TextUtils.isEmpty(serialNumber) && (serialNumber.startsWith("C2MS3") || serialNumber.startsWith("C2M3"));
        AILog.m4029d("DeviceUtil", "isNeedCloseFm：" + z + "  ,serialNumber : " + serialNumber, LogLevel.RELEASE);
        return z;
    }

    public static boolean isNotSupportMultiMainTheme() {
        return isSupportTesla() || isSupportHikai();
    }

    public static boolean isSNStartWithP16() {
        String serialNumber = getSerialNumber();
        if (TextUtils.isEmpty(serialNumber)) {
            serialNumber = getSerialNumber();
        }
        AILog.m4029d("DeviceUtil", "sn is " + serialNumber, LogLevel.RELEASE);
        return serialNumber.startsWith("P16");
    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService("power")).isScreenOn();
    }

    public static boolean isSupportBTAudio() {
        CarrobotChannel carrobotChannel = getCarrobotChannel();
        if (carrobotChannel == CarrobotChannel.C2E) {
            return false;
        }
        if (carrobotChannel == CarrobotChannel.C2S) {
            return true;
        }
        CarrobotChannel carrobotChannel2 = CarrobotChannel.C2M;
        return false;
    }

    public static boolean isSupportDm() {
        return TextUtils.equals(ServerConfig.CHANNEL_ID, "CMSD0001");
    }

    public static boolean isSupportDriveRecord() {
        CarrobotChannel carrobotChannel = getCarrobotChannel();
        if (carrobotChannel == CarrobotChannel.C2E || carrobotChannel == CarrobotChannel.C2S) {
            return false;
        }
        CarrobotChannel carrobotChannel2 = CarrobotChannel.C2M;
        return false;
    }

    public static boolean isSupportHikai() {
        return TextUtils.equals(ServerConfig.CHANNEL_ID, "CMSC0001");
    }

    public static boolean isSupportHotpot() {
        CarrobotChannel carrobotChannel = getCarrobotChannel();
        return carrobotChannel == CarrobotChannel.C2E || carrobotChannel == CarrobotChannel.C2S || carrobotChannel == CarrobotChannel.C2M;
    }

    public static boolean isSupportKillProcess() {
        String serialNumber = getSerialNumber();
        boolean z = TextUtils.isEmpty(serialNumber) || !serialNumber.startsWith("C2M3");
        AILog.m4029d("DeviceUtil", "isSupportKillAPP：" + z + "  ,serialNumber : " + serialNumber, LogLevel.RELEASE);
        return z;
    }

    public static boolean isSupportMiot() {
        String serialNumber = getSerialNumber();
        boolean z = !TextUtils.isEmpty(serialNumber) && (serialNumber.startsWith("C2MS1") || serialNumber.startsWith("C2M1"));
        AILog.m4029d("DeviceUtil", "isSupportMiot：" + z + "  ,serialNumber : " + serialNumber, LogLevel.RELEASE);
        return z;
    }

    public static boolean isSupportSccl() {
        return TextUtils.equals(ServerConfig.CHANNEL_ID, "CMSA0001");
    }

    public static boolean isSupportSwitchVoice() {
        CarrobotChannel carrobotChannel = getCarrobotChannel();
        return carrobotChannel != CarrobotChannel.C2E && (carrobotChannel == CarrobotChannel.C2S || carrobotChannel == CarrobotChannel.C2M);
    }

    public static boolean isSupportTesla() {
        return TextUtils.equals(ServerConfig.CHANNEL_ID, "C2MSE0001");
    }

    public static boolean isSupportTirePressure() {
        CarrobotChannel carrobotChannel = getCarrobotChannel();
        if (carrobotChannel == CarrobotChannel.C2E) {
            return false;
        }
        if (carrobotChannel == CarrobotChannel.C2S) {
            return true;
        }
        CarrobotChannel carrobotChannel2 = CarrobotChannel.C2M;
        return false;
    }

    public static boolean isSupportWeChat() {
        CarrobotChannel carrobotChannel = getCarrobotChannel();
        if (carrobotChannel == CarrobotChannel.C2E || carrobotChannel == CarrobotChannel.C2S) {
            return false;
        }
        CarrobotChannel carrobotChannel2 = CarrobotChannel.C2M;
        return false;
    }

    public static void setSystemConf(String str, String str2) {
        Application context = AiBase.getInst().getContext();
        if (context != null) {
            SystemPropertiesProxy.set(context, str, str2);
            return;
        }
        try {
            Runtime.getRuntime().exec("setprop " + str + StringUtils.SPACE + str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void write2DDP_PWM(String str) throws Throwable {
        AILog.m4029d("DeviceUtil", " write2DDP_PWM " + str, LogLevel.RELEASE);
        BufferedWriter bufferedWriter = null;
        try {
            try {
                try {
                    File file = new File("sys/class/ddp_pwm_debug/ddp_pwm");
                    if (!file.exists()) {
                        AILog.m4029d("DeviceUtil", "the ddp file does not exist", LogLevel.RELEASE);
                    }
                    AILog.m4034i("DeviceUtil", "write data to file ....");
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                    try {
                        bufferedWriter2.write(str);
                        bufferedWriter2.flush();
                        bufferedWriter2.close();
                    } catch (FileNotFoundException e) {
                        e = e;
                        bufferedWriter = bufferedWriter2;
                        e.printStackTrace();
                        if (bufferedWriter == null) {
                        } else {
                            bufferedWriter.close();
                        }
                    } catch (IOException e2) {
                        e = e2;
                        bufferedWriter = bufferedWriter2;
                        e.printStackTrace();
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
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e4) {
                    e = e4;
                } catch (IOException e5) {
                    e = e5;
                }
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}