package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class AIObdDrivingStatisticsData extends AIObdData {
    public static final Parcelable.Creator<AIObdDrivingStatisticsData> CREATOR = new Parcelable.Creator<AIObdDrivingStatisticsData>() { // from class: com.ileja.aicar.obd.data.AIObdDrivingStatisticsData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdDrivingStatisticsData createFromParcel(Parcel parcel) {
            return new AIObdDrivingStatisticsData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdDrivingStatisticsData[] newArray(int i) {
            return new AIObdDrivingStatisticsData[i];
        }
    };
    public static final String DrivingStatisticsDataTag = "$OBD-TT";

    public AIObdDrivingStatisticsData() {
    }

    @Override // com.ileja.aicar.obd.data.AIObdData
    public void fromMessage(String str) {
        this.message = str;
        this.f5952c = true;
    }

    protected AIObdDrivingStatisticsData(Parcel parcel) {
        super(parcel);
    }
}