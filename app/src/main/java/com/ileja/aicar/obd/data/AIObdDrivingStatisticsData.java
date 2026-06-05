package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AIObdDrivingStatisticsData extends AIObdData {
    public static final Parcelable.Creator<AIObdDrivingStatisticsData> CREATOR = new Parcelable.Creator<AIObdDrivingStatisticsData>() {
        @Override
        public AIObdDrivingStatisticsData createFromParcel(Parcel parcel) {
            return new AIObdDrivingStatisticsData(parcel);
        }

        @Override
        public AIObdDrivingStatisticsData[] newArray(int i) {
            return new AIObdDrivingStatisticsData[i];
        }
    };
    public static final String DrivingStatisticsDataTag = "$OBD-TT";

    public AIObdDrivingStatisticsData() {
    }

    @Override
    public void fromMessage(String str) {
        this.message = str;
        this.f5952c = true;
    }

    protected AIObdDrivingStatisticsData(Parcel parcel) {
        super(parcel);
    }
}
