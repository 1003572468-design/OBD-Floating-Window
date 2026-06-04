package com.ileja.framework.service.telcomm.listener;

/* JADX INFO: loaded from: classes.dex */
public interface TirePressureListener {
    void onGetFLPressure(double d, int i);

    void onGetFRPressure(double d, int i);

    void onGetLRPressure(double d, int i);

    void onGetRRPressure(double d, int i);
}