package com.obd.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

public class AIObdRealtimeData implements Parcelable {
    public String id;
    public float obdBattery;           // 电瓶电压
    public int obdSpeed;               // 车速 km/h
    public int obdRotateSpeed;         // 转速 rpm
    public int obdCoolantTemprature;   // 水温 ℃
    public float obdAverageFuelConsume; // 平均油耗 L/100km
    public float obdRealtimeFuelConsume; // 瞬时油耗
    public int obdDrivingRange;        // 本次行驶里程
    public int obdTotalDrivingRange;   // 总里程
    public int obdFuelConsume;         // 本次耗油量
    public int obdTotalFuelConsume;    // 累计耗油量
    public int obdErrorCount;          // 故障码数量
    public float obdThrottleOpenRatio; // 节气门开度
    public float obdEngineLoadRatio;   // 发动机负荷
    
    public AIObdRealtimeData() {}
    
    protected AIObdRealtimeData(Parcel in) {
        id = in.readString();
        obdBattery = in.readFloat();
        obdSpeed = in.readInt();
        obdRotateSpeed = in.readInt();
        obdCoolantTemprature = in.readInt();
        obdAverageFuelConsume = in.readFloat();
        obdRealtimeFuelConsume = in.readFloat();
        obdDrivingRange = in.readInt();
        obdTotalDrivingRange = in.readInt();
        obdFuelConsume = in.readInt();
        obdTotalFuelConsume = in.readInt();
        obdErrorCount = in.readInt();
        obdThrottleOpenRatio = in.readFloat();
        obdEngineLoadRatio = in.readFloat();
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
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeFloat(obdBattery);
        dest.writeInt(obdSpeed);
        dest.writeInt(obdRotateSpeed);
        dest.writeInt(obdCoolantTemprature);
        dest.writeFloat(obdAverageFuelConsume);
        dest.writeFloat(obdRealtimeFuelConsume);
        dest.writeInt(obdDrivingRange);
        dest.writeInt(obdTotalDrivingRange);
        dest.writeInt(obdFuelConsume);
        dest.writeInt(obdTotalFuelConsume);
        dest.writeInt(obdErrorCount);
        dest.writeFloat(obdThrottleOpenRatio);
        dest.writeFloat(obdEngineLoadRatio);
    }
    
    @Override
    public String toString() {
        return "AIObdRealtimeData{" +
                "obdSpeed=" + obdSpeed +
                ", obdRotateSpeed=" + obdRotateSpeed +
                ", obdCoolantTemprature=" + obdCoolantTemprature +
                '}';
    }
}
