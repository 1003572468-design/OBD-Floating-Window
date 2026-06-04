package com.ileja.aibase.http.http;

/* JADX INFO: loaded from: classes.dex */
public class UICacheManager {
    private static final String TAG = "UICacheManager";
    private static byte[] initLock = new byte[0];
    private static UICacheManager mInstance;

    private UICacheManager() {
    }

    public static UICacheManager getInstance() {
        if (mInstance == null) {
            synchronized (initLock) {
                if (mInstance == null) {
                    mInstance = new UICacheManager();
                }
            }
        }
        return mInstance;
    }

    public <T> void onHandleRequstResult(RequestHandler<?> requestHandler, T t) {
        requestHandler.getRequstName();
    }
}