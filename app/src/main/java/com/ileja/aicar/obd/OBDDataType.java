package com.ileja.aicar.obd;

/* JADX INFO: loaded from: classes.dex */
public enum OBDDataType {
    ROTATESPEED("krpm", 10),
    WATERTEMPRATURE("°C", 250),
    AVERAGEFUELCONSUME("L/100km", 30),
    REALTIMEFUELCONSUME("L/100km", 30),
    BATTERY("V", 36);

    private final int grade;
    private final String unit;

    OBDDataType(String str, int i) {
        this.unit = str;
        this.grade = i;
    }

    public static OBDDataType getType(String str) {
        return valueOf(str);
    }

    public int getGrade() {
        return this.grade;
    }

    public String getUnit() {
        return this.unit;
    }
}