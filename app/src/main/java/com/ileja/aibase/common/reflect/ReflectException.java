package com.ileja.aibase.common.reflect;

/* JADX INFO: loaded from: classes.dex */
public class ReflectException extends RuntimeException {
    private static final long serialVersionUID = -2243843843843438438L;

    public ReflectException(String str) {
        super(str);
    }

    public ReflectException(String str, Throwable th) {
        super(str, th);
    }

    public ReflectException() {
    }

    public ReflectException(Throwable th) {
        super(th);
    }
}