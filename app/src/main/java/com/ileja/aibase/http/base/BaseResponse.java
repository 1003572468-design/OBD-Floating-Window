package com.ileja.aibase.http.base;

import android.text.TextUtils;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseResponse {
    String serviceMessage;
    int serviceStatus;

    public String getServiceMessage() {
        return this.serviceMessage;
    }

    public int getServiceStatus() {
        return this.serviceStatus;
    }

    public abstract void parse(String str);

    protected void parseHeader(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        JSONObject jSONObject = new JSONObject(str);
        this.serviceMessage = jSONObject.optString("msg");
        this.serviceStatus = jSONObject.optInt("status");
    }

    public void setServiceMessage(String str) {
        this.serviceMessage = str;
    }

    public void setServiceStatus(int i) {
        this.serviceStatus = i;
    }

    public String toString() {
        return "BaseResponse{serviceMessage='" + this.serviceMessage + "', serviceStatus=" + this.serviceStatus + '}';
    }
}