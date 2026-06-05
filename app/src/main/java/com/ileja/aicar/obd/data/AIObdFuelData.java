package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AIObdFuelData extends AIObdData {
    public static final Parcelable.Creator<AIObdFuelData> CREATOR = new Parcelable.Creator<AIObdFuelData>() {
        @Override
        public AIObdFuelData createFromParcel(Parcel parcel) {
            return new AIObdFuelData(parcel);
        }

        @Override
        public AIObdFuelData[] newArray(int i) {
            return new AIObdFuelData[i];
        }
    };
    public static final String FuelObdTag = "$047";
    private String remainFuel;

    public AIObdFuelData() {
    }

    @Override
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

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.remainFuel);
        return stringBuffer.toString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.remainFuel);
    }

    protected AIObdFuelData(Parcel parcel) {
        super(parcel);
        this.remainFuel = parcel.readString();
    }
}
