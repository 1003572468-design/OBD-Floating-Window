package com.ileja.aibase.http.http;

/* JADX INFO: loaded from: classes.dex */
public abstract class ResponseHandler<T> {
    public static final String TAG = "ResponseHandler";

    public abstract void onFailure(int i);

    public abstract void onSuccess(T t, boolean z);
}