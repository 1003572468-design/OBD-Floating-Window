package com.ileja.aibase.common;

import android.util.Log;
import com.ileja.aibase.AiBase;
import com.ileja.aibase.AiBasePrefHelper;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.common.logger.LogUtils;
import com.ileja.aibase.common.logger.Logger;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public class AILog {
    private static final String AILOG_FILE = "AILog.txt";
    public static final String AILOG_FILE_LOCATION = "location.txt";
    public static final String AILOG_FILE_OBD = "OBDLog.txt";
    public static final String AILOG_FILE_SIGNAL = "Signal.txt";
    public static final String AILOG_FILE_TESLA = "Tesla.txt";
    private static boolean IS_WRITE_AILOG_TO_FILE = false;
    private static boolean IS_WRITE_TEST_AILOG_TO_FILE = true;
    private static final long MaxOfSize = 20971520;
    private static final String PUBLIC_TAG = "AIOSLOG";
    private static final long SecondOfDay = 86400;
    private static long curTime;
    private static long oldTime;
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);

    /* JADX INFO: renamed from: d */
    public static void m4028d(String str, String str2) {
        if (str2 != null) {
            Log.d(str, str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: e */
    public static void m4030e(String str, String str2) {
        if (str2 != null) {
            Log.e(str, str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: i */
    public static void m4034i(String str, String str2) {
        if (str2 != null) {
            Log.i(str, str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: j */
    public static void m4036j(String str, String str2) {
        if (Logger.init().getLogLevel() != LogLevel.FULL) {
            return;
        }
        Logger.m4052t(str).json(str2);
        writeFile(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean logOutofDate() {
        if (curTime == 0) {
            curTime = DateUtil.getTimeSecondFrom2011();
        }
        if (oldTime == 0) {
            long logTimeStamp = AiBasePrefHelper.getLogTimeStamp(AiBase.getInst().getContext());
            oldTime = logTimeStamp;
            if (logTimeStamp == 0) {
                AiBasePrefHelper.setLogTimeStamp(AiBase.getInst().getContext(), curTime);
            }
        }
        long j = oldTime;
        if (j <= 0 || curTime - j <= SecondOfDay) {
            return false;
        }
        AiBasePrefHelper.setLogTimeStamp(AiBase.getInst().getContext(), curTime);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean logOutofSize(String str, String str2) {
        File file = new File(str + File.separator + str2);
        return file.exists() && file.length() >= MaxOfSize;
    }

    /* JADX INFO: renamed from: v */
    public static void m4038v(String str, String str2) {
        if (str2 != null) {
            Log.v(str, str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: w */
    public static void m4040w(String str, String str2) {
        if (str2 != null) {
            Log.w(str, str2);
        }
        writeFile(str, str2);
    }

    public static void writeFile(final String str, final String str2) {
        if (IS_WRITE_AILOG_TO_FILE) {
            FileUtil.getSaveFileThreadPool().submit(new Runnable() { // from class: com.ileja.aibase.common.AILog.1
                @Override // java.lang.Runnable
                public void run() throws Throwable {
                    try {
                        String path = FileUtil.getExternalCacheDir(AiBase.getInst().getContext()).getPath();
                        if (AILog.logOutofDate() && AILog.logOutofSize(path, AILog.AILOG_FILE)) {
                            new File(path + File.separator + AILog.AILOG_FILE).delete();
                            FileUtil.syncSaveStringToFile(path + File.separator + AILog.AILOG_FILE, AILog.sdf.format(Long.valueOf(System.currentTimeMillis())) + " (" + str + ") delete Old LogFile==============================================" + StringUtils.f8344LF, true);
                        }
                        FileUtil.syncSaveStringToFile(path + File.separator + AILog.AILOG_FILE, AILog.sdf.format(Long.valueOf(System.currentTimeMillis())) + " (" + str + ") " + str2 + StringUtils.f8344LF, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void writeGPSLocationFile(String str, final String str2) {
        FileUtil.getSaveFileThreadPool().submit(new Runnable() { // from class: com.ileja.aibase.common.AILog.3
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                try {
                    if (AILog.logOutofDate() && AILog.logOutofSize("/sdcard/ilejaLog", AILog.AILOG_FILE_LOCATION)) {
                        new File("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_LOCATION).delete();
                        FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_LOCATION, "delete Old LogFile==============================================" + StringUtils.f8344LF, true);
                    }
                    FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_LOCATION, str2 + StringUtils.f8344LF, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void writeOBDLogFile(String str, final String str2) {
        FileUtil.getSaveFileThreadPool().submit(new Runnable() { // from class: com.ileja.aibase.common.AILog.5
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                try {
                    if (AILog.logOutofDate() && AILog.logOutofSize("/sdcard/ilejaLog", AILog.AILOG_FILE_OBD)) {
                        new File("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_OBD).delete();
                        FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_OBD, "delete Old LogFile==============================================" + StringUtils.f8344LF, true);
                    }
                    FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_OBD, str2 + StringUtils.f8344LF, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void writeSignalFile(final String str, final String str2) {
        if (IS_WRITE_TEST_AILOG_TO_FILE) {
            Log.d(str, str2);
            FileUtil.getSaveFileThreadPool().submit(new Runnable() { // from class: com.ileja.aibase.common.AILog.2
                @Override // java.lang.Runnable
                public void run() throws Throwable {
                    try {
                        if (AILog.logOutofDate() && AILog.logOutofSize("/sdcard/ilejaLog", AILog.AILOG_FILE_SIGNAL)) {
                            new File("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_SIGNAL).delete();
                            FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_SIGNAL, AILog.sdf.format(Long.valueOf(System.currentTimeMillis())) + " (" + str + ") delete Old LogFile==============================================" + StringUtils.f8344LF, true);
                        }
                        FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_SIGNAL, AILog.sdf.format(Long.valueOf(System.currentTimeMillis())) + " (" + str + ") " + str2 + StringUtils.f8344LF, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void writeTeslaMsgFile(final String str) {
        FileUtil.getSaveFileThreadPool().submit(new Runnable() { // from class: com.ileja.aibase.common.AILog.4
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                try {
                    if (AILog.logOutofDate() && AILog.logOutofSize("/sdcard/ilejaLog", AILog.AILOG_FILE_TESLA)) {
                        new File("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_TESLA).delete();
                        FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_TESLA, "delete Old LogFile==============================================" + StringUtils.f8344LF, true);
                    }
                    FileUtil.syncSaveStringToFile("/sdcard/ilejaLog" + File.separator + AILog.AILOG_FILE_TESLA, str + StringUtils.f8344LF, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: renamed from: x */
    public static void m4042x(String str, String str2) {
        if (Logger.init().getLogLevel() != LogLevel.FULL) {
            return;
        }
        Logger.m4052t(str).xml(str2);
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: d */
    public static void m4029d(String str, String str2, LogLevel logLevel) {
        if (!Logger.init().getLogLevel().canLog(logLevel)) {
            if (str2 != null) {
                Log.d(str, str2);
                return;
            }
            return;
        }
        if (str2 != null) {
            Log.d(PUBLIC_TAG, str + "##" + str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: e */
    public static void m4032e(String str, String str2, Throwable th) {
        if (str2 != null) {
            Log.e(str, str2, th);
        }
    }

    /* JADX INFO: renamed from: i */
    public static void m4035i(String str, String str2, LogLevel logLevel) {
        if (!Logger.init().getLogLevel().canLog(logLevel)) {
            if (str2 != null) {
                Log.i(str, str2);
                return;
            }
            return;
        }
        if (str2 != null) {
            Log.i(PUBLIC_TAG, str + "##" + str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: v */
    public static void m4039v(String str, String str2, LogLevel logLevel) {
        if (!Logger.init().getLogLevel().canLog(logLevel)) {
            if (str2 != null) {
                Log.v(str, str2);
                return;
            }
            return;
        }
        if (str2 != null) {
            Log.v(PUBLIC_TAG, str + "##" + str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: w */
    public static void m4041w(String str, String str2, LogLevel logLevel) {
        if (!Logger.init().getLogLevel().canLog(logLevel)) {
            if (str2 != null) {
                Log.w(str, str2);
                return;
            }
            return;
        }
        if (str2 != null) {
            Log.w(PUBLIC_TAG, str + "##" + str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: e */
    public static void m4031e(String str, String str2, LogLevel logLevel) {
        if (!Logger.init().getLogLevel().canLog(logLevel)) {
            if (str2 != null) {
                Log.e(str, str2);
                return;
            }
            return;
        }
        if (str2 != null) {
            Log.e(PUBLIC_TAG, str + "##" + str2);
        }
        writeFile(str, str2);
    }

    /* JADX INFO: renamed from: j */
    public static void m4037j(String str, String str2, LogLevel logLevel) {
        if (Logger.init().getLogLevel().canLog(logLevel)) {
            Logger.m4052t(str).json(str2);
            writeFile(str, str2);
        }
    }

    /* JADX INFO: renamed from: x */
    public static void m4043x(String str, String str2, LogLevel logLevel) {
        if (Logger.init().getLogLevel().canLog(logLevel)) {
            Logger.m4052t(str).xml(str2);
            writeFile(str, str2);
        }
    }

    /* JADX INFO: renamed from: e */
    public static void m4033e(String str, String str2, Throwable th, LogLevel logLevel) {
        if (str2 != null) {
            Log.e(PUBLIC_TAG, str + "##" + str2, th);
            Log.e(PUBLIC_TAG, str + "##" + str2 + StringUtils.f8344LF + LogUtils.getStackTrace(th));
            writeFile(str, str2);
        }
    }
}