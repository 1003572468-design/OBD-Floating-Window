package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AIObdRealtimeData implements Parcelable {
    public float obdCoolantTemprature;
    public float obdSpeed;
    public float obdRotateSpeed;
    public float obdBattery;
    public float obdRealtimeFuelConsume;
    public float obdAverageFuelConsume;
    public float obdThrottleOpenRatio;
    public float obdEngineLoadRatio;
    public boolean valid;
    
    public AIObdRealtimeData() {}
    
    protected AIObdRealtimeData(Parcel in) {
        obdCoolantTemprature = in.readFloat();
        obdSpeed = in.readFloat();
        obdRotateSpeed = in.readFloat();
        obdBattery = in.readFloat();
        obdRealtimeFuelConsume = in.readFloat();
        obdAverageFuelConsume = in.readFloat();
        obdThrottleOpenRatio = in.readFloat();
        obdEngineLoadRatio = in.readFloat();
        valid = in.readByte() != 0;
    }
    
    public static final Creator<AIObdRealtimeData> CREATOR = new Creator<AIObdRealtimeData>() {
        @Override
        public AIObdRealtimeData createFromParcel(Parcel in) {
            return new AIObdRealtimeData(in);
        }
        @Override
        public AIObdRealtimeData[] newArray(int size) {
            return new AIObdRealtimeData[size];
        }
    };
    
    @Override
    public int describeContents() { return 0; }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(obdCoolantTemprature);
        dest.writeFloat(obdSpeed);
        dest.writeFloat(obdRotateSpeed);
        dest.writeFloat(obdBattery);
        dest.writeFloat(obdRealtimeFuelConsume);
        dest.writeFloat(obdAverageFuelConsume);
        dest.writeFloat(obdThrottleOpenRatio);
        dest.writeFloat(obdEngineLoadRatio);
        dest.writeByte((byte) (valid ? 1 : 0));
    }
}
