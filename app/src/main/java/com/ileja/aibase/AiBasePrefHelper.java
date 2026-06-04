package com.ileja.aibase;

import android.content.Context;
import android.content.SharedPreferences;
import com.ileja.aibase.common.FileUtil;
import com.ileja.aibase.phone.DeviceUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes.dex */
public class AiBasePrefHelper {
    private static final String KEY_LOG_TIMESTAMP = "log_timestamp";
    private static final String KEY_MAC_ADDR = "hud_mac_addr";
    public static final String KEY_SP_HUD = "HudBase";
    private static final String SIM_SERIAL_NUM = "simserialnumber";
    private static final String TAG = "AiBasePrefHelper";

    public static boolean getGPSCallback(Context context) {
        return getValue(context).equals("true");
    }

    private static SharedPreferences getHUDSharedPreferences(Context context) {
        return context.getSharedPreferences(KEY_SP_HUD, 0);
    }

    public static long getLogTimeStamp(Context context) {
        return getHUDSharedPreferences(context).getLong(KEY_LOG_TIMESTAMP, 0L);
    }

    public static String getMacAddr(Context context) {
        return getHUDSharedPreferences(context).getString(KEY_MAC_ADDR, null);
    }

    public static String getSimSerialNumber(Context context) {
        return DeviceUtil.getSimSerialNum(context);
    }

    private static String getValue(Context context) {
        File file = new File(FileUtil.getStoragePath(context, false) + File.separator + "carrobot" + File.separator + "track");
        if (!file.exists()) {
            return "";
        }
        try {
            return new BufferedReader(new BufferedReader(new InputStreamReader(new FileInputStream(new File(file.getAbsolutePath() + File.separator + "config.txt")), "UTF-8"))).readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setGPSCallback(Context context, boolean z) throws Throwable {
        setValue(context, String.valueOf(z));
        try {
            Runtime.getRuntime().exec("reboot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLogTimeStamp(Context context, long j) {
        SharedPreferences.Editor editorEdit = getHUDSharedPreferences(context).edit();
        editorEdit.putLong(KEY_LOG_TIMESTAMP, j);
        editorEdit.commit();
    }

    public static void setMacAddr(Context context, String str) {
        SharedPreferences.Editor editorEdit = getHUDSharedPreferences(context).edit();
        editorEdit.putString(KEY_MAC_ADDR, str);
        editorEdit.commit();
    }

    public static void setSimSerialNumber(Context context, String str) {
        if (str == null || str.length() <= 0) {
            return;
        }
        SharedPreferences.Editor editorEdit = getHUDSharedPreferences(context).edit();
        editorEdit.putString(SIM_SERIAL_NUM, str);
        editorEdit.commit();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.io.FileWriter] */
    /* JADX WARN: Type inference failed for: r2v8, types: [java.io.FileWriter, java.io.Writer] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:35:0x009c -> B:49:0x009f). Please report as a decompilation issue!!! */
    private static void setValue(Context context, String str) throws Throwable {
        String storagePath = FileUtil.getStoragePath(context, false);
        FileWriter sb = new StringBuilder();
        sb.append(storagePath);
        sb.append(File.separator);
        sb.append("carrobot");
        sb.append(File.separator);
        sb.append("track");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        BufferedWriter bufferedWriter = null;
        try {
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                sb = new FileWriter(file.getAbsolutePath() + File.separator + "config.txt", false);
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(sb);
                    try {
                        bufferedWriter2.write(str + "\r\n");
                        bufferedWriter2.flush();
                        try {
                            bufferedWriter2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        sb.close();
                    } catch (IOException e3) {
                        e = e3;
                        bufferedWriter = bufferedWriter2;
                        e.printStackTrace();
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (sb != 0) {
                            sb.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        }
                        if (sb == 0) {
                            throw th;
                        }
                        try {
                            sb.close();
                            throw th;
                        } catch (IOException e6) {
                            e6.printStackTrace();
                            throw th;
                        }
                    }
                } catch (IOException e7) {
                    e = e7;
                }
            } catch (IOException e8) {
                e = e8;
                sb = 0;
            } catch (Throwable th2) {
                th = th2;
                sb = 0;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }
}