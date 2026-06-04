package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class AIObdData implements Parcelable {
    public static final int Obd_Msg_Min_Len = 5;
    public static final String Obd_Msg_Start = "$";

    /* JADX INFO: renamed from: c */
    protected boolean f5952c;
    public String message;

    public AIObdData() {
        this.f5952c = false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AIObdData)) {
            return false;
        }
        AIObdData aIObdData = (AIObdData) obj;
        return this.f5952c == aIObdData.f5952c && TextUtils.equals(this.message, aIObdData.message);
    }

    public abstract void fromMessage(String str);

    public boolean isValid() {
        return this.f5952c;
    }

    public String toString() {
        return "[valid:" + this.f5952c + " ,msg:" + this.message + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.message);
        parcel.writeByte(this.f5952c ? (byte) 1 : (byte) 0);
    }

    protected AIObdData(Parcel parcel) {
        this.f5952c = false;
        this.message = parcel.readString();
        this.f5952c = parcel.readByte() != 0;
    }
}