package com.ileja.aibase.phone;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothData {
    private static BluetoothData ins;
    private String remoteDeviceName;
    private String remoteDevicesAddress;

    private BluetoothData() {
    }

    public static BluetoothData getIns() {
        if (ins == null) {
            ins = new BluetoothData();
        }
        return ins;
    }

    public String getRemoteDeviceName() {
        return this.remoteDeviceName;
    }

    public String getRemoteDevicesAddress() {
        return this.remoteDevicesAddress;
    }

    public void setRemoteDeviceInfo(String str, String str2) {
        this.remoteDevicesAddress = str;
        this.remoteDeviceName = str2;
    }
}