package com.ileja.aibase.common.logger;

import android.text.TextUtils;
import android.util.Log;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class LoggerPrinter implements Printer {
    private static final String BOTTOM_BORDER = "╚════════════════════════════════════════════════════════════════════════════════════════";
    private static final char BOTTOM_LEFT_CORNER = 9562;
    private static final int CHUNK_SIZE = 4000;
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final char HORIZONTAL_DOUBLE_LINE = 9553;
    private static final int JSON_INDENT = 4;
    private static final String MIDDLE_BORDER = "╟────────────────────────────────────────────────────────────────────────────────────────";
    private static final char MIDDLE_CORNER = 9567;
    private static final int MIN_STACK_OFFSET = 3;
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = "╔════════════════════════════════════════════════════════════════════════════════════════";
    private static final char TOP_LEFT_CORNER = 9556;
    private static final Settings settings = new Settings();
    private static String TAG = "PRETTYLOGGER";
    private static final ThreadLocal<String> LOCAL_TAG = new ThreadLocal<>();
    private static final ThreadLocal<Integer> LOCAL_METHOD_COUNT = new ThreadLocal<>();

    LoggerPrinter() {
    }

    private String createMessage(String str, Object... objArr) {
        return objArr.length == 0 ? str : String.format(str, objArr);
    }

    private String formatTag(String str) {
        if (TextUtils.isEmpty(str) || TextUtils.equals(TAG, str)) {
            return TAG;
        }
        return TAG + "-" + str;
    }

    private int getMethodCount() {
        Integer num = LOCAL_METHOD_COUNT.get();
        int methodCount = settings.getMethodCount();
        if (num != null) {
            LOCAL_METHOD_COUNT.remove();
            methodCount = num.intValue();
        }
        if (methodCount >= 0) {
            return methodCount;
        }
        throw new IllegalStateException("methodCount cannot be negative");
    }

    private String getSimpleClassName(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }

    private int getStackOffset(StackTraceElement[] stackTraceElementArr) {
        for (int i = 3; i < stackTraceElementArr.length; i++) {
            String className = stackTraceElementArr[i].getClassName();
            if (!className.equals(LoggerPrinter.class.getName()) && !className.equals(Logger.class.getName())) {
                return i - 1;
            }
        }
        return -1;
    }

    private String getTag() {
        String str = LOCAL_TAG.get();
        if (str == null) {
            return TAG;
        }
        LOCAL_TAG.remove();
        return str;
    }

    private synchronized void log(int i, String str, Object... objArr) {
        if (settings.getLogLevel() == LogLevel.NONE) {
            return;
        }
        String tag = getTag();
        String strCreateMessage = createMessage(str, objArr);
        int methodCount = getMethodCount();
        logTopBorder(i, tag);
        logHeaderContent(i, tag, methodCount);
        byte[] bytes = strCreateMessage.getBytes();
        int length = bytes.length;
        if (length <= 4000) {
            if (methodCount > 0) {
                logDivider(i, tag);
            }
            logContent(i, tag, strCreateMessage);
            logBottomBorder(i, tag);
            return;
        }
        if (methodCount > 0) {
            logDivider(i, tag);
        }
        for (int i2 = 0; i2 < length; i2 += 4000) {
            logContent(i, tag, new String(bytes, i2, Math.min(length - i2, 4000)));
        }
        logBottomBorder(i, tag);
    }

    private void logBottomBorder(int i, String str) {
        logChunk(i, str, BOTTOM_BORDER);
    }

    private void logChunk(int i, String str, String str2) {
        String tag = formatTag(str);
        if (i == 2) {
            Log.v(tag, str2);
            return;
        }
        if (i == 4) {
            Log.i(tag, str2);
            return;
        }
        if (i == 5) {
            Log.w(tag, str2);
            return;
        }
        if (i == 6) {
            Log.e(tag, str2);
        } else if (i != 7) {
            Log.d(tag, str2);
        } else {
            Log.wtf(tag, str2);
        }
    }

    private void logContent(int i, String str, String str2) {
        for (String str3 : str2.split(System.getProperty("line.separator"))) {
            logChunk(i, str, "║ " + str3);
        }
    }

    private void logDivider(int i, String str) {
        logChunk(i, str, MIDDLE_BORDER);
    }

    private void logHeaderContent(int i, String str, int i2) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (settings.isShowThreadInfo()) {
            logChunk(i, str, "║ Thread: " + Thread.currentThread().getName());
            logDivider(i, str);
        }
        int stackOffset = getStackOffset(stackTrace) + settings.getMethodOffset();
        if (i2 + stackOffset > stackTrace.length) {
            i2 = (stackTrace.length - stackOffset) - 1;
        }
        String str2 = "";
        while (i2 > 0) {
            int i3 = i2 + stackOffset;
            if (i3 < stackTrace.length) {
                str2 = str2 + "   ";
                logChunk(i, str, "║ " + str2 + getSimpleClassName(stackTrace[i3].getClassName()) + "." + stackTrace[i3].getMethodName() + StringUtils.SPACE + " (" + stackTrace[i3].getFileName() + ":" + stackTrace[i3].getLineNumber() + ")");
            }
            i2--;
        }
    }

    private void logTopBorder(int i, String str) {
        logChunk(i, str, TOP_BORDER);
    }

    @Override // com.ileja.aibase.common.logger.Printer
    /* JADX INFO: renamed from: d */
    public void mo4056d(String str, Object... objArr) {
        log(3, str, objArr);
    }

    @Override // com.ileja.aibase.common.logger.Printer
    /* JADX INFO: renamed from: e */
    public void mo4057e(String str, Object... objArr) {
        mo4058e(null, str, objArr);
    }

    @Override // com.ileja.aibase.common.logger.Printer
    public Settings getSettings() {
        return settings;
    }

    @Override // com.ileja.aibase.common.logger.Printer
    /* JADX INFO: renamed from: i */
    public void mo4059i(String str, Object... objArr) {
        log(4, str, objArr);
    }

    @Override // com.ileja.aibase.common.logger.Printer
    public Settings init(String str) {
        if (str == null) {
            throw new NullPointerException("tag may not be null");
        }
        if (str.trim().length() == 0) {
            throw new IllegalStateException("tag may not be empty");
        }
        TAG = str;
        return settings;
    }

    @Override // com.ileja.aibase.common.logger.Printer
    public void json(String str) {
        if (TextUtils.isEmpty(str)) {
            mo4056d("Empty/Null json content", new Object[0]);
            return;
        }
        try {
            if (str.startsWith("{")) {
                mo4056d(new JSONObject(str).toString(4), new Object[0]);
            } else if (str.startsWith("[")) {
                mo4056d(new JSONArray(str).toString(4), new Object[0]);
            }
        } catch (JSONException e) {
            mo4057e(e.getCause().getMessage() + StringUtils.f8344LF + str, new Object[0]);
        }
    }

    @Override // com.ileja.aibase.common.logger.Printer
    /* JADX INFO: renamed from: t */
    public Printer mo4060t(String str, int i) {
        if (str != null) {
            LOCAL_TAG.set(str);
        }
        LOCAL_METHOD_COUNT.set(Integer.valueOf(i));
        return this;
    }

    @Override // com.ileja.aibase.common.logger.Printer
    /* JADX INFO: renamed from: v */
    public void mo4061v(String str, Object... objArr) {
        log(2, str, objArr);
    }

    @Override // com.ileja.aibase.common.logger.Printer
    /* JADX INFO: renamed from: w */
    public void mo4062w(String str, Object... objArr) {
        log(5, str, objArr);
    }

    @Override // com.ileja.aibase.common.logger.Printer
    public void wtf(String str, Object... objArr) {
        log(7, str, objArr);
    }

    @Override // com.ileja.aibase.common.logger.Printer
    public void xml(String str) {
        if (TextUtils.isEmpty(str)) {
            mo4056d("Empty/Null xml content", new Object[0]);
            return;
        }
        try {
            StreamSource streamSource = new StreamSource(new StringReader(str));
            StreamResult streamResult = new StreamResult(new StringWriter());
            Transformer transformerNewTransformer = TransformerFactory.newInstance().newTransformer();
            transformerNewTransformer.setOutputProperty("indent", "yes");
            transformerNewTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformerNewTransformer.transform(streamSource, streamResult);
            mo4056d(streamResult.getWriter().toString().replaceFirst(">", ">\n"), new Object[0]);
        } catch (TransformerException e) {
            mo4057e(e.getCause().getMessage() + StringUtils.f8344LF + str, new Object[0]);
        }
    }

    @Override // com.ileja.aibase.common.logger.Printer
    /* JADX INFO: renamed from: e */
    public void mo4058e(Throwable th, String str, Object... objArr) {
        if (th != null && str != null) {
            str = str + " : " + th.toString();
        }
        if (th != null && str == null) {
            str = th.toString();
        }
        if (str == null) {
            str = "No message/exception is set";
        }
        log(6, str, objArr);
    }
}