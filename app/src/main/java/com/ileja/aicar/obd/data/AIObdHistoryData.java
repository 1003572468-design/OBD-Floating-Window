package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class AIObdHistoryData extends AIObdData {
    public static final Parcelable.Creator<AIObdHistoryData> CREATOR = new Parcelable.Creator<AIObdHistoryData>() { // from class: com.ileja.aicar.obd.data.AIObdHistoryData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdHistoryData createFromParcel(Parcel parcel) {
            return new AIObdHistoryData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdHistoryData[] newArray(int i) {
            return new AIObdHistoryData[i];
        }
    };
    public static final String HistoryDataTag = "$OBD-HBT";

    public AIObdHistoryData() {
    }

    @Override // com.ileja.aicar.obd.data.AIObdData
    public void fromMessage(String str) {
        this.message = str;
        this.f5952c = true;
    }

    protected AIObdHistoryData(Parcel parcel) {
        super(parcel);
    }
}