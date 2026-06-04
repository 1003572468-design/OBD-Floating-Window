package com.ileja.aibase.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.ileja.fota.FotaTransaction;
import com.tendcloud.tenddata.C1615dt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* JADX INFO: loaded from: classes.dex */
public class Util {
    static final int BUFF_SIZE = 10240;
    public static final String UTF8 = "UTF-8";
    public static byte[] MAGIC = {80, 75, 3, 4};
    public static String uniqueID = null;

    public static boolean checkMD5(InputStream inputStream, File file) {
        boolean z;
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] fileMD5String = getFileMD5String(fileInputStream);
                byte[] fileMD5String2 = getFileMD5String(inputStream);
                int length = fileMD5String.length > fileMD5String2.length ? fileMD5String2.length : fileMD5String.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = true;
                        break;
                    }
                    if (fileMD5String[i] != fileMD5String2[i]) {
                        z = false;
                        break;
                    }
                    i++;
                }
                fileInputStream.close();
                return z;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public static boolean checkPermissionAtRunTime(Context context, String str) {
        return context != null && context.checkCallingOrSelfPermission(str) == 0;
    }

    public static int copyResource(Context context, String str) {
        return copyResource(context, str, true);
    }

    public static void executeRunnableInMainOrTestThread(Context context, Runnable runnable) {
        if (context != null) {
            HandlerThread handlerThread = null;
            boolean zIsUnitTesting = isUnitTesting();
            if (zIsUnitTesting) {
                handlerThread = new HandlerThread("TestHandlerThread");
                handlerThread.start();
            }
            new Handler(zIsUnitTesting ? handlerThread.getLooper() : context.getMainLooper()).post(runnable);
        }
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String generateDeviceId16(Context context) {
        if (context == null) {
            return "";
        }
        String simSerialNumber = null;
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        String str = Build.SERIAL;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                return "";
            }
            simSerialNumber = telephonyManager.getSimSerialNumber();
        } catch (Exception unused) {
            Log.e("", "Did you forget add android.permission.READ_PHONE_STATE permission in your application? Add it now to fix this bug!");
        }
        if (TextUtils.isEmpty(string) || TextUtils.equals(string, "9774d56d682e549c")) {
            string = !TextUtils.isEmpty(simSerialNumber) ? simSerialNumber : !TextUtils.isEmpty(str) ? str : "";
        }
        return (string.length() >= 8 ? string : "").toLowerCase();
    }

    public static long generateRandom(int i) {
        Random random = new Random();
        char[] cArr = new char[i];
        cArr[0] = (char) (random.nextInt(9) + 49);
        for (int i2 = 1; i2 < i; i2++) {
            cArr[i2] = (char) (random.nextInt(10) + 48);
        }
        return Long.parseLong(new String(cArr));
    }

    public static File getAvaiableAppDataDirPerInternal(Context context, String str, long j) {
        if (context == null) {
            return null;
        }
        if (getAvailableInternalMemorySize() >= j) {
            return new File(context.getFilesDir(), str);
        }
        if (getAvailableExternalMemorySize() >= j) {
            return new File(getAvaiableExternalDataDir(context), str);
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    public static File getAvaiableExternalDataDir(Context context) {
        if (context != null && externalMemoryAvailable()) {
            return context.getExternalFilesDir(null);
        }
        return null;
    }

    public static long getAvailableExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return -1L;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    public static long getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
    }

    public static String getDisplayInfo(Context context) {
        if (context == null) {
            return null;
        }
        int i = context.getResources().getDisplayMetrics().heightPixels;
        return context.getResources().getDisplayMetrics().widthPixels + "x" + i;
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return context.getExternalCacheDir();
        }
        return null;
    }

    private static byte[] getFileMD5String(InputStream inputStream) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bArr = new byte[BUFF_SIZE];
            while (true) {
                int i = inputStream.read(bArr);
                if (i == -1) {
                    return messageDigest.digest();
                }
                messageDigest.update(bArr, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return new byte[0];
        }
    }

    public static String getIMEI(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        return (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0 && telephonyManager.getDeviceId() != null) ? telephonyManager.getDeviceId() : "";
    }

    public static String getNetWorkType(Context context) {
        if (context == null) {
            return null;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return null;
            }
            int type = activeNetworkInfo.getType();
            if (type == 0) {
                switch (telephonyManager.getNetworkType()) {
                    case 1:
                        return "GPRS";
                    case 2:
                        return "EDGE";
                    case 3:
                        return "UMTS";
                    case 4:
                        return C1615dt.f8082b;
                    case 5:
                        return "EVDO_0";
                    case 6:
                        return "EVDO_A";
                    case 7:
                        return "1xRTT";
                    case 8:
                        return "HSDPA";
                    case 9:
                        return "HSUPA";
                    case 10:
                        return "HSPA";
                    case 11:
                        return "IDEN";
                }
            }
            if (type == 1) {
                return "WIFI";
            }
            return "unknown_type";
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Util", "Did you forget add android.permission.ACCESS_NETWORK_STATE permission in your application? Add it to fix this bug!");
            return null;
        }
    }

    public static int getNetworkQuality(Context context) {
        String netWorkType = getNetWorkType(context);
        return (netWorkType == null || netWorkType.equals("EDGE") || netWorkType.equals("GPRS") || netWorkType.equals("1xRTT") || netWorkType.equals("IDEN")) ? 3 : 8;
    }

    public static String getPkgVersion(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("packageName must not be null...");
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            AILog.m4030e("getPkgVersion", " NameNotFoundException : " + e.toString());
            return "";
        }
    }

    public static int getPkgVersionInt(Context context, String str) {
        StringBuilder sb;
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("packageName must not be null...");
        }
        try {
            try {
                int i = context.getPackageManager().getPackageInfo(str, 0).versionCode;
                AILog.m4028d("getPkgVersionInt", " pkgName:" + str + ", versionCode:" + i);
                return i;
            } catch (PackageManager.NameNotFoundException e) {
                AILog.m4030e("getPkgVersionInt", " NameNotFoundException : " + e.toString());
                sb = new StringBuilder();
                sb.append(" pkgName:");
                sb.append(str);
                sb.append(", versionCode:");
                sb.append(0);
                AILog.m4028d("getPkgVersionInt", sb.toString());
                return 0;
            }
        } catch (Throwable unused) {
            sb = new StringBuilder();
            sb.append(" pkgName:");
            sb.append(str);
            sb.append(", versionCode:");
            sb.append(0);
            AILog.m4028d("getPkgVersionInt", sb.toString());
            return 0;
        }
    }

    public static String getResourceDir(Context context) {
        if (context == null) {
            return null;
        }
        File filesDir = context.getFilesDir();
        if (filesDir == null) {
            filesDir = new File("/data/data/" + context.getPackageName() + "/files");
        }
        return filesDir.getAbsolutePath();
    }

    public static long getTotalExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return -1L;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static long getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception unused) {
            return null;
        }
    }

    public static void installApk(Context context, String str) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(str)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean isAboveAndroid22() {
        return Build.VERSION.SDK_INT > 7;
    }

    public static boolean isUnitTesting() {
        return Thread.currentThread().getName().contains("android.test.InstrumentationTestRunner");
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    public static boolean isZipFile(File file) {
        boolean z;
        byte[] bArr = new byte[MAGIC.length];
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.readFully(bArr);
            int i = 0;
            while (true) {
                if (i >= MAGIC.length) {
                    z = true;
                    break;
                }
                if (bArr[i] != MAGIC[i]) {
                    z = false;
                    break;
                }
                i++;
            }
            randomAccessFile.close();
            return z;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static final void logThread(String str) {
        Thread threadCurrentThread = Thread.currentThread();
        Log.d(str, "<" + threadCurrentThread.getName() + ">id: " + threadCurrentThread.getId() + ", Priority: " + threadCurrentThread.getPriority() + ", Group: " + threadCurrentThread.getThreadGroup().getName());
    }

    public static String newUTF8String(byte[] bArr) {
        try {
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(bArr);
        }
    }

    public static void openDownloadWeb(Context context, String str) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    private static void removeFile(String str) {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void unZip(Context context, File file) {
        byte[] bArr = new byte[BUFF_SIZE];
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
            while (enumerationEntries.hasMoreElements()) {
                ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
                if (zipEntryNextElement.isDirectory()) {
                    new File(getResourceDir(context), zipEntryNextElement.getName()).mkdirs();
                } else {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntryNextElement));
                    Log.d(FotaTransaction.STATE_UNZIP, zipEntryNextElement.getName());
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(getResourceDir(context), zipEntryNextElement.getName())), BUFF_SIZE);
                    while (true) {
                        int i = bufferedInputStream.read(bArr, 0, BUFF_SIZE);
                        if (i == -1) {
                            break;
                        } else {
                            bufferedOutputStream.write(bArr, 0, i);
                        }
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x006a A[Catch: all -> 0x008c, TRY_LEAVE, TryCatch #5 {, blocks: (B:8:0x0008, B:10:0x0011, B:11:0x0019, B:13:0x0025, B:15:0x002b, B:19:0x0031, B:22:0x0036, B:26:0x003e, B:27:0x0046, B:29:0x004c, B:30:0x0050, B:37:0x0060, B:39:0x006a, B:33:0x0058, B:25:0x003b, B:36:0x005d, B:42:0x006f, B:47:0x008e), top: B:62:0x0004, inners: #0, #2, #3, #4, #6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized int copyResource(android.content.Context r8, java.lang.String r9, boolean r10) {
        /*
            java.lang.Class<com.ileja.aibase.common.Util> r0 = com.ileja.aibase.common.Util.class
            monitor-enter(r0)
            r1 = -1
            if (r8 != 0) goto L8
            monitor-exit(r0)
            return r1
        L8:
            android.content.res.AssetManager r2 = r8.getAssets()     // Catch: java.lang.Throwable -> L8c java.io.IOException -> L8e
            java.io.InputStream r2 = r2.open(r9)     // Catch: java.lang.Throwable -> L8c java.io.IOException -> L8e
            r3 = 1
            byte[] r4 = new byte[r3]     // Catch: java.io.IOException -> L6f java.lang.Throwable -> L8c
            r2.read(r4)     // Catch: java.io.IOException -> L6f java.lang.Throwable -> L8c
            r2.reset()     // Catch: java.io.IOException -> L6f java.lang.Throwable -> L8c
            java.io.File r4 = new java.io.File     // Catch: java.lang.Throwable -> L8c
            java.lang.String r5 = getResourceDir(r8)     // Catch: java.lang.Throwable -> L8c
            r4.<init>(r5, r9)     // Catch: java.lang.Throwable -> L8c
            r5 = 0
            if (r10 == 0) goto L36
            boolean r10 = checkMD5(r2, r4)     // Catch: java.lang.Throwable -> L8c
            if (r10 == 0) goto L36
            r2.close()     // Catch: java.io.IOException -> L30 java.lang.Throwable -> L8c
            monitor-exit(r0)
            return r5
        L30:
            r8 = move-exception
            r8.printStackTrace()     // Catch: java.lang.Throwable -> L8c
            monitor-exit(r0)
            return r1
        L36:
            r2.reset()     // Catch: java.io.IOException -> L3a java.lang.Throwable -> L8c
            goto L3e
        L3a:
            r10 = move-exception
            r10.printStackTrace()     // Catch: java.io.FileNotFoundException -> L5c java.lang.Throwable -> L8c
        L3e:
            java.io.FileOutputStream r10 = r8.openFileOutput(r9, r5)     // Catch: java.io.FileNotFoundException -> L5c java.lang.Throwable -> L8c
            r6 = 10240(0x2800, float:1.4349E-41)
            byte[] r6 = new byte[r6]     // Catch: java.io.FileNotFoundException -> L5c java.lang.Throwable -> L8c
        L46:
            int r7 = r2.read(r6)     // Catch: java.io.IOException -> L57 java.lang.Throwable -> L8c
            if (r7 == r1) goto L50
            r10.write(r6, r5, r7)     // Catch: java.io.IOException -> L57 java.lang.Throwable -> L8c
            goto L46
        L50:
            r10.close()     // Catch: java.io.IOException -> L57 java.lang.Throwable -> L8c
            r2.close()     // Catch: java.io.IOException -> L57 java.lang.Throwable -> L8c
            goto L60
        L57:
            r10 = move-exception
            r10.printStackTrace()     // Catch: java.io.FileNotFoundException -> L5c java.lang.Throwable -> L8c
            goto L60
        L5c:
            r10 = move-exception
            r10.printStackTrace()     // Catch: java.lang.Throwable -> L8c
        L60:
            java.io.File r9 = r8.getFileStreamPath(r9)     // Catch: java.lang.Throwable -> L8c
            boolean r9 = isZipFile(r9)     // Catch: java.lang.Throwable -> L8c
            if (r9 == 0) goto L6d
            unZip(r8, r4)     // Catch: java.lang.Throwable -> L8c
        L6d:
            monitor-exit(r0)
            return r3
        L6f:
            java.lang.String r8 = "Util"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8c
            r10.<init>()     // Catch: java.lang.Throwable -> L8c
            java.lang.String r2 = "file"
            r10.append(r2)     // Catch: java.lang.Throwable -> L8c
            r10.append(r9)     // Catch: java.lang.Throwable -> L8c
            java.lang.String r9 = "should be one of the suffix below to avoid be compressed in assets floder.“.jpg”, “.jpeg”, “.png”, “.gif”, “.wav”, “.mp2″, “.mp3″, “.ogg”, “.aac”, “.mpg”, “.mpeg”, “.mid”, “.midi”, “.smf”, “.jet”, “.rtttl”, “.imy”, “.xmf”, “.mp4″, “.m4a”, “.m4v”, “.3gp”, “.3gpp”, “.3g2″, “.3gpp2″, “.amr”, “.awb”, “.wma”, “.wmv”"
            r10.append(r9)     // Catch: java.lang.Throwable -> L8c
            java.lang.String r9 = r10.toString()     // Catch: java.lang.Throwable -> L8c
            android.util.Log.e(r8, r9)     // Catch: java.lang.Throwable -> L8c
            monitor-exit(r0)
            return r1
        L8c:
            r8 = move-exception
            goto Lab
        L8e:
            java.lang.String r8 = "Util"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8c
            r10.<init>()     // Catch: java.lang.Throwable -> L8c
            java.lang.String r2 = "file "
            r10.append(r2)     // Catch: java.lang.Throwable -> L8c
            r10.append(r9)     // Catch: java.lang.Throwable -> L8c
            java.lang.String r9 = " not found in assest floder, Did you forget add it?"
            r10.append(r9)     // Catch: java.lang.Throwable -> L8c
            java.lang.String r9 = r10.toString()     // Catch: java.lang.Throwable -> L8c
            android.util.Log.e(r8, r9)     // Catch: java.lang.Throwable -> L8c
            monitor-exit(r0)
            return r1
        Lab:
            monitor-exit(r0)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ileja.aibase.common.Util.copyResource(android.content.Context, java.lang.String, boolean):int");
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context, String str) {
        File externalCacheDir;
        if (!"mounted".equals(Environment.getExternalStorageState()) || (externalCacheDir = context.getExternalCacheDir()) == null) {
            return null;
        }
        if (!externalCacheDir.exists()) {
            externalCacheDir.mkdirs();
        }
        if (TextUtils.isEmpty(str)) {
            return context.getExternalCacheDir();
        }
        File file = new File(externalCacheDir, str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getAvaiableAppDataDirPerInternal(Context context, String str) {
        return getAvaiableAppDataDirPerInternal(context, str, 10485760L);
    }
}