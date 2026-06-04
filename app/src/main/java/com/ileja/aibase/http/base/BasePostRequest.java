package com.ileja.aibase.http.base;

import com.ileja.aibase.http.http.RequestHandler;

/* JADX INFO: loaded from: classes.dex */
public abstract class BasePostRequest<T> extends RequestHandler<T> {
    @Override // com.ileja.aibase.http.http.RequestHandler, com.android.volley.Request
    public String getBodyContentType() {
        return "application/octet-stream";
    }

    @Override // com.android.volley.Request
    public int getMethod() {
        return 1;
    }

    @Override // com.ileja.aibase.http.http.RequestHandler
    public String getRequstName() {
        return null;
    }
}