package com.ileja.aibase.common.logger;

import android.content.Context;
import android.os.Environment;
import com.ileja.aibase.common.AILog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class LogUtils {
    private static final String KEY_LOG_TIMESTAMP = "log_timestamp";
    public static final int MAX_FILE_WRITE_SIZE = 1048576;
    private static final String logFileName = "hud_bug.txt";
    private static File mLogFile;

    private static File createLogFile(Context context, String str) {
        File file = Environment.getExternalStorageState().equals("mounted") ? new File(context.getExternalFilesDir(null), str) : null;
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String getStackTrace(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter((Writer) stringWriter, true);
        if (th != null) {
            th.printStackTrace(printWriter);
        }
        return stringWriter.getBuffer().toString();
    }

    public static synchronized void saveLog(Context context, String str) {
        boolean z = true;
        if (mLogFile == null || !mLogFile.exists()) {
            mLogFile = createLogFile(context, logFileName);
        } else if (mLogFile.length() > 1048576) {
            z = false;
        }
        if (mLogFile != null && mLogFile.canWrite()) {
            try {
                FileWriter fileWriter = new FileWriter(mLogFile, z);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                StringBuffer stringBuffer = new StringBuffer();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("time", new SimpleDateFormat("yyyy-MM-dd HH-mm", Locale.getDefault()).format(new Date()));
                jSONObject.put("content", str);
                stringBuffer.append(jSONObject.toString() + "\n\n\n");
                bufferedWriter.write(stringBuffer.toString());
                bufferedWriter.close();
                fileWriter.close();
                AILog.m4034i("AIUncaughtExceptionHandler", "path:" + mLogFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}