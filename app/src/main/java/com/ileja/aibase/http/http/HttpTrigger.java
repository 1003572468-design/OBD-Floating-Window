package com.ileja.aibase.http.http;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.FileUtil;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.http.constants.AppFilePaths;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class HttpTrigger {
    private static RequestQueue mRequestQueue;

    public static void clearCache(Context context) {
        FileUtil.deleteAllFiles(new File(AppFilePaths.getCacheRootDir(context, true)));
    }

    public static void init(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    @Deprecated
    private static <T> void send(RequestHandler<T> requestHandler, ResponseHandler<T> responseHandler) {
        throwIfNotOnMainThread();
        sendInNoneUIThread(requestHandler, responseHandler);
    }

    public static <T> void sendInNoneUIThread(final RequestHandler<T> requestHandler, final ResponseHandler<T> responseHandler) {
        String str;
        if (requestHandler == null || responseHandler == null) {
            return;
        }
        String strMakeRequestUrl = requestHandler.makeRequestUrl();
        requestHandler.setUrl(strMakeRequestUrl);
        String str2 = null;
        try {
            byte[] body = requestHandler.getBody();
            if (body != null) {
                str2 = new String(body);
            }
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("begin send data requestUrl : ");
        sb.append(strMakeRequestUrl);
        if (TextUtils.isEmpty(str2)) {
            str = "";
        } else {
            str = "requestbody : " + str2;
        }
        sb.append(str);
        AILog.m4029d("AppEngine", sb.toString(), LogLevel.RELEASE);
        requestHandler.setListener(new Response.Listener<T>() { // from class: com.ileja.aibase.http.http.HttpTrigger.1
            @Override // com.android.volley.Response.Listener
            public void onResponse(T t, boolean z) {
                responseHandler.onSuccess(t, z);
            }
        });
        requestHandler.setErrorListener(new Response.ErrorListener() { // from class: com.ileja.aibase.http.http.HttpTrigger.2
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                AILog.m4031e("AppEngine", volleyError.getClass().getName() + ":" + requestHandler.getRequstName() + ":" + volleyError.toString(), LogLevel.RELEASE);
                if (volleyError instanceof ParseError) {
                    responseHandler.onFailure(((ParseError) volleyError).mReturnCode);
                    return;
                }
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null) {
                    responseHandler.onFailure(networkResponse.statusCode);
                } else {
                    responseHandler.onFailure(0);
                }
            }
        });
        mRequestQueue.add(requestHandler);
    }

    private static void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("feedLoader must be invoked from the main thread.");
        }
    }
}