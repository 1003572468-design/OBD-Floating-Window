package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AIObdHistoryData extends AIObdData {
    public static final Parcelable.Creator<AIObdHistoryData> CREATOR = new Parcelable.Creator<AIObdHistoryData>() {
        @Override
        public AIObdHistoryData createFromParcel(Parcel parcel) {
            return new AIObdHistoryData(parcel);
        }

        @Override
        public AIObdHistoryData[] newArray(int i) {
            return new AIObdHistoryData[i];
        }
    };
    public static final String HistoryDataTag = "$OBD-HBT";

    public AIObdHistoryData() {
    }

    @Override
    public void fromMessage(String str) {
        this.message = str;
        this.f5952c = true;
    }

    protected AIObdHistoryData(Parcel parcel) {
        super(parcel);
    }
}
