package com.ileja.aibase.http.base;

import com.ileja.aibase.http.http.RequestHandler;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseGetRequest<T> extends RequestHandler<T> {
    @Override // com.android.volley.Request
    public int getMethod() {
        return 0;
    }

    @Override // com.ileja.aibase.http.http.RequestHandler
    public String getRequstName() {
        return null;
    }
}