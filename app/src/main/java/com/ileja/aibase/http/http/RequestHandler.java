package com.ileja.aibase.http.http;

import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.http.constants.APPCacheType;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

/* JADX INFO: loaded from: classes.dex */
public abstract class RequestHandler<T> extends Request<T> {
    private static final float REQUEST_BACKOFF_MULT = 1.0f;
    private static final int REQUEST_MAX_RETRIES = 3;
    private static final int REQUEST_TIMEOUT_MS = 60000;
    private final String PROTOCOL_CONTENT_TYPE_JSON;
    private int mCgiId;
    private Response.Listener<T> mListener;
    private boolean mPostWithJson;
    private int mReturnCode;

    public RequestHandler() {
        this(null, null);
    }

    @Override // com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        super.deliverError(volleyError);
    }

    @Override // com.android.volley.Request
    protected void deliverResponse(T t, boolean z) {
        this.mListener.onResponse(t, z);
    }

    @Override // com.android.volley.Request
    public String getBodyContentType() {
        return (this.mPostWithJson && getMethod() == 1) ? this.PROTOCOL_CONTENT_TYPE_JSON : super.getBodyContentType();
    }

    @Override // com.android.volley.Request
    public APPCacheType getCacheType() {
        return APPCacheType.CGI;
    }

    protected int getCgiId() {
        return this.mCgiId;
    }

    protected String getCookie() {
        return "";
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(getCookie())) {
            map.put("Cookie", getCookie());
        }
        return map;
    }

    public abstract ParamEntity getParamEntity();

    @Override // com.android.volley.Request
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    @Override // com.android.volley.Request
    public Request.Priority getPriority() {
        return Request.Priority.IMMEDIATE;
    }

    public abstract String getRequstName();

    protected int getReturnCode() {
        return this.mReturnCode;
    }

    public void handleParseResult(T t) {
        UICacheManager.getInstance().onHandleRequstResult(this, t);
    }

    protected boolean isLogicErr(int i) {
        return false;
    }

    public String makeRequestUrl() {
        ParamEntity paramEntity = getParamEntity();
        return URLBuilderFactory.build(paramEntity).makeUrl() + paramEntity.getCommonParam();
    }

    public abstract T parse(String str);

    @Override // com.android.volley.Request
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            T t = parse(str);
            if (this.mReturnCode != 0 && !isLogicErr(this.mReturnCode)) {
                VolleyLog.m3930e("parseNetworkResponse mReturnCode != 0, the value is : %s", Integer.valueOf(this.mReturnCode));
                return Response.error(new ParseError(networkResponse, this.mReturnCode));
            }
            if (t == null) {
                VolleyLog.m3930e("parseNetworkResponse responseString==null, the url=%s", getUrl());
                return Response.error(new ParseError(networkResponse, this.mReturnCode));
            }
            handleParseResult(t);
            AILog.m4029d("AppEngine", "send success , response : " + str, LogLevel.RELEASE);
            return Response.success(t, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            VolleyLog.m3931e(e, "parseNetworkResponse UnsupportedEncodingException, the url=%s", getUrl());
            return Response.error(new ParseError(e));
        } catch (OutOfMemoryError e2) {
            VolleyLog.m3930e("Caught OOM for %d byte image, url=%s", Integer.valueOf(networkResponse.data.length), getUrl());
            return Response.error(new ParseError(e2));
        } catch (JSONException e3) {
            VolleyLog.m3931e(e3, "parseNetworkResponse JSONException, the url=%s", getUrl());
            return Response.error(new ParseError(e3));
        }
    }

    protected void setCgiId(int i) {
        this.mCgiId = i;
    }

    @Override // com.android.volley.Request
    public void setErrorListener(Response.ErrorListener errorListener) {
        super.setErrorListener(errorListener);
    }

    public void setListener(Response.Listener<T> listener) {
        this.mListener = listener;
    }

    protected void setPostWithJson(boolean z) {
        this.mPostWithJson = z;
    }

    public RequestHandler(Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(0, null, 1, errorListener);
        this.PROTOCOL_CONTENT_TYPE_JSON = String.format("application/json; charset=%s", getParamsEncoding());
        this.mReturnCode = 0;
        this.mPostWithJson = false;
        this.mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(60000, 3, 1.0f));
    }
}