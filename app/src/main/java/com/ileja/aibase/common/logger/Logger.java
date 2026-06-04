package com.ileja.aibase.common.logger;

/* JADX INFO: loaded from: classes.dex */
public final class Logger {
    private static final String DEFAULT_TAG = "PRETTYLOGGER";
    private static final Printer printer = new LoggerPrinter();

    private Logger() {
    }

    /* JADX INFO: renamed from: d */
    public static void m4047d(String str, Object... objArr) {
        printer.mo4056d(str, objArr);
    }

    /* JADX INFO: renamed from: e */
    public static void m4048e(String str, Object... objArr) {
        printer.mo4058e(null, str, objArr);
    }

    /* JADX INFO: renamed from: i */
    public static void m4050i(String str, Object... objArr) {
        printer.mo4059i(str, objArr);
    }

    public static Settings init() {
        return printer.init(DEFAULT_TAG);
    }

    public static void json(String str) {
        printer.json(str);
    }

    /* JADX INFO: renamed from: t */
    public static Printer m4052t(String str) {
        Printer printer2 = printer;
        return printer2.mo4060t(str, printer2.getSettings().getMethodCount());
    }

    /* JADX INFO: renamed from: v */
    public static void m4054v(String str, Object... objArr) {
        printer.mo4061v(str, objArr);
    }

    /* JADX INFO: renamed from: w */
    public static void m4055w(String str, Object... objArr) {
        printer.mo4062w(str, objArr);
    }

    public static void wtf(String str, Object... objArr) {
        printer.wtf(str, objArr);
    }

    public static void xml(String str) {
        printer.xml(str);
    }

    /* JADX INFO: renamed from: e */
    public static void m4049e(Throwable th, String str, Object... objArr) {
        printer.mo4058e(th, str, objArr);
    }

    public static Settings init(String str) {
        return printer.init(str);
    }

    /* JADX INFO: renamed from: t */
    public static Printer m4051t(int i) {
        return printer.mo4060t(null, i);
    }

    /* JADX INFO: renamed from: t */
    public static Printer m4053t(String str, int i) {
        return printer.mo4060t(str, i);
    }
}