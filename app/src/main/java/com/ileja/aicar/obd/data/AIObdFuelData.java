package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class AIObdFuelData extends AIObdData {
    public static final Parcelable.Creator<AIObdFuelData> CREATOR = new Parcelable.Creator<AIObdFuelData>() { // from class: com.ileja.aicar.obd.data.AIObdFuelData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdFuelData createFromParcel(Parcel parcel) {
            return new AIObdFuelData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdFuelData[] newArray(int i) {
            return new AIObdFuelData[i];
        }
    };
    public static final String FuelObdTag = "$047";
    private String remainFuel;

    public AIObdFuelData() {
    }

    @Override // com.ileja.aicar.obd.data.AIObdData
    public void fromMessage(String str) {
        String[] strArrSplit = str.split(",");
        if (strArrSplit != null && strArrSplit.length >= 2 && strArrSplit[0].equals(FuelObdTag)) {
            this.f5952c = true;
            this.remainFuel = strArrSplit[1];
        }
    }

    public String getRemainFuel() {
        return this.remainFuel;
    }

    @Override // com.ileja.aicar.obd.data.AIObdData
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.remainFuel);
        return stringBuffer.toString();
    }

    @Override // com.ileja.aicar.obd.data.AIObdData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.remainFuel);
    }

    protected AIObdFuelData(Parcel parcel) {
        super(parcel);
        this.remainFuel = parcel.readString();
    }
}