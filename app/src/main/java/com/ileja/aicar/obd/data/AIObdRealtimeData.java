package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AIObdRealtimeData extends AIObdData {
    public static final Parcelable.Creator<AIObdRealtimeData> CREATOR = new Parcelable.Creator<AIObdRealtimeData>() {
        @Override
        public AIObdRealtimeData createFromParcel(Parcel parcel) {
            return new AIObdRealtimeData(parcel);
        }

        @Override
        public AIObdRealtimeData[] newArray(int i) {
            return new AIObdRealtimeData[i];
        }
    };
    public static final byte DINA = 0;
    public static final byte MIYN = 1;
    public static final int RealtimeObdDataLen = 15;
    public static final String RealtimeObdTag = "$OBD-RT";
    public static final byte SURD = 2;

    public String f5953id;
    public float obdAverageFuelConsume;   // 平均油耗
    public float obdBattery;              // 电池电压
    public float obdCoolantTemprature;    // 冷却液温度
    public float obdDrivingRange;         // 剩余续航
    public float obdEngineLoadRatio;      // 发动机负载率
    public int obdErrorCount;             // 故障码数量
    public float obdFuelConsume;          // 油耗
    public int obdOilLeft;                // 剩余油量
    public float obdRealtimeFuelConsume;  // 实时油耗
    public float obdRotateSpeed;          // 发动机转速 (RPM)
    public float obdSpeed;                // 车速 (km/h)
    public float obdThrottleOpenRatio;    // 节气门开度
    public float obdTotalDrivingRange;    // 总行驶里程
    public float obdTotalFuelConsume;     // 总油耗
    public byte type;

    public AIObdRealtimeData() {
        this.f5953id = "";
        this.type = (byte) 0;
    }

    @Override
    public void fromMessage(String str) {
        this.message = str;
        String[] strArrSplit = str.split(",");
        if (strArrSplit == null || strArrSplit.length <= 15) {
            return;
        }
        this.type = (byte) 2;
        this.f5953id = "";
        this.obdBattery = Float.parseFloat(strArrSplit[1]);
        this.obdRotateSpeed = Float.parseFloat(strArrSplit[2]);
        this.obdSpeed = Float.parseFloat(strArrSplit[3]);
        this.obdThrottleOpenRatio = Float.parseFloat(strArrSplit[4]);
        this.obdEngineLoadRatio = Float.parseFloat(strArrSplit[5]);
        this.obdCoolantTemprature = Float.parseFloat(strArrSplit[6]);
        float f = Float.parseFloat(strArrSplit[7]);
        this.obdRealtimeFuelConsume = f;
        if (f >= 50.0f) {
            this.obdRealtimeFuelConsume = 49.9f;
        }
        float f2 = Float.parseFloat(strArrSplit[8]);
        this.obdAverageFuelConsume = f2;
        if (f2 >= 50.0f) {
            this.obdAverageFuelConsume = 49.9f;
        }
        this.obdDrivingRange = Float.parseFloat(strArrSplit[9]);
        this.obdTotalDrivingRange = Float.parseFloat(strArrSplit[10]);
        this.obdFuelConsume = Float.parseFloat(strArrSplit[11]);
        this.obdTotalFuelConsume = Float.parseFloat(strArrSplit[12]);
        this.obdErrorCount = Integer.parseInt(strArrSplit[13]);
        if (this.obdSpeed < 220.0f) {
            this.f5952c = true;
        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeFloat(this.obdBattery);
        parcel.writeFloat(this.obdRotateSpeed);
        parcel.writeFloat(this.obdSpeed);
        parcel.writeFloat(this.obdThrottleOpenRatio);
        parcel.writeFloat(this.obdEngineLoadRatio);
        parcel.writeFloat(this.obdCoolantTemprature);
        parcel.writeFloat(this.obdRealtimeFuelConsume);
        parcel.writeFloat(this.obdAverageFuelConsume);
        parcel.writeFloat(this.obdDrivingRange);
        parcel.writeFloat(this.obdTotalDrivingRange);
        parcel.writeFloat(this.obdFuelConsume);
        parcel.writeFloat(this.obdTotalFuelConsume);
        parcel.writeInt(this.obdErrorCount);
        parcel.writeInt(this.obdOilLeft);
        parcel.writeByte(this.type);
        parcel.writeString(this.f5953id);
    }

    protected AIObdRealtimeData(Parcel parcel) {
        super(parcel);
        this.f5953id = "";
        this.type = (byte) 0;
        this.obdBattery = parcel.readFloat();
        this.obdRotateSpeed = parcel.readFloat();
        this.obdSpeed = parcel.readFloat();
        this.obdThrottleOpenRatio = parcel.readFloat();
        this.obdEngineLoadRatio = parcel.readFloat();
        this.obdCoolantTemprature = parcel.readFloat();
        this.obdRealtimeFuelConsume = parcel.readFloat();
        this.obdAverageFuelConsume = parcel.readFloat();
        this.obdDrivingRange = parcel.readFloat();
        this.obdTotalDrivingRange = parcel.readFloat();
        this.obdFuelConsume = parcel.readFloat();
        this.obdTotalFuelConsume = parcel.readFloat();
        this.obdErrorCount = parcel.readInt();
        this.obdOilLeft = parcel.readInt();
        this.type = parcel.readByte();
        this.f5953id = parcel.readString();
    }
}
