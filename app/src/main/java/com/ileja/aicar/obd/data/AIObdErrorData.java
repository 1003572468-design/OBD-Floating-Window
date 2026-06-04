package com.ileja.aicar.obd.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.ileja.aibase.config.HttpConfig;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class AIObdErrorData extends AIObdData {
    public static final Parcelable.Creator<AIObdErrorData> CREATOR = new Parcelable.Creator<AIObdErrorData>() { // from class: com.ileja.aicar.obd.data.AIObdErrorData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdErrorData createFromParcel(Parcel parcel) {
            return new AIObdErrorData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AIObdErrorData[] newArray(int i) {
            return new AIObdErrorData[i];
        }
    };
    public static final int ErrorObdDataLen = 2;
    public static final String ErrorObdTag = "$400";
    public String[] errCodes;
    public int errCount;

    public enum ErrType {
        PowerSystem("动力总成系统"),
        CarBody("车身系统"),
        Chassis("底盘系统"),
        Circuit("总线系统");

        private String value;

        ErrType(String str) {
            this.value = str;
        }

        public String getValue() {
            return this.value;
        }
    }

    public AIObdErrorData() {
        this.errCount = 0;
    }

    public static ErrType parserErrType(String str) {
        if (str != null) {
            String upperCase = str.toUpperCase(Locale.US);
            if (upperCase.startsWith("P")) {
                return ErrType.PowerSystem;
            }
            if (upperCase.startsWith(HttpConfig.TAG.SET_MAP_BAIDU)) {
                return ErrType.CarBody;
            }
            if (upperCase.startsWith("C")) {
                return ErrType.Chassis;
            }
            if (upperCase.startsWith("U")) {
                return ErrType.Circuit;
            }
        }
        return null;
    }

    @Override // com.ileja.aicar.obd.data.AIObdData
    public void fromMessage(String str) {
        this.message = str;
        String[] strArrSplit = str.split(",");
        if (strArrSplit == null || strArrSplit.length < 2 || TextUtils.isEmpty(strArrSplit[0])) {
            return;
        }
        this.f5952c = true;
        String[] strArrSplit2 = strArrSplit[0].split("=");
        if (strArrSplit2 == null || strArrSplit2.length != 2) {
            return;
        }
        try {
            int iIntValue = Integer.valueOf(strArrSplit2[1]).intValue();
            this.errCount = iIntValue;
            if (iIntValue <= 0) {
                return;
            }
            if (TextUtils.isEmpty(strArrSplit[1])) {
                this.errCount = 0;
            } else {
                this.errCodes = strArrSplit[1].split("\\|");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override // com.ileja.aicar.obd.data.AIObdData
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[errCount:" + this.errCount + ", ");
        String[] strArr = this.errCodes;
        if (strArr != null) {
            for (String str : strArr) {
                stringBuffer.append(str);
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    @Override // com.ileja.aicar.obd.data.AIObdData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.errCount);
        parcel.writeStringArray(this.errCodes);
    }

    protected AIObdErrorData(Parcel parcel) {
        super(parcel);
        this.errCount = 0;
        this.errCount = parcel.readInt();
        this.errCodes = parcel.createStringArray();
    }
}