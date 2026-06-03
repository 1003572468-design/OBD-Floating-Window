package com.obd.dashboard;

public interface OBDDataListener {
    void onDataReceived(String data);
    void onConnectionChanged(boolean connected);
    void onError(String error);
}
