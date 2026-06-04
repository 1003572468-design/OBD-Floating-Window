package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class AIObdStatus implements Parcelable {
    public static final Parcelable.Creator<AIObdStatus> CREATOR = new Parcelable.Creator<AIObdStatus>() { // from class: com.ileja.aicar.obd.data.AIObdStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdStatus createFromParcel(Parcel parcel) {
            return new AIObdStatus(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdStatus[] newArray(int i) {
            return new AIObdStatus[i];
        }
    };
    public static final long OBD_STATUS_CONNECTED = 0;
    public static final long OBD_STATUS_DISCONNECTED = -1;
    private long obdStatus;

    public AIObdStatus() {
        this.obdStatus = -1L;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public long getStatus() {
        return this.obdStatus;
    }

    public void setStatus(long j) {
        this.obdStatus = j;
    }

    public String toString() {
        return "[obdStatus:" + this.obdStatus + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.obdStatus);
    }

    protected AIObdStatus(Parcel parcel) {
        this.obdStatus = parcel.readLong();
    }
}