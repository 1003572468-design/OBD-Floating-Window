package com.ileja.core.p007sp;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IMPSharedPreference {
    public static final String INTENT_ACTION_SHAREDSP_STATUS = "com.ileja.action.sharedsp.status";

    public interface LeJaOnSharedPreferenceChangeListener {
        void onSharedPreferenceChanged(String str);
    }

    public enum NotifyType {
        NONE,
        BOTH,
        PRIVATE_PROCESS,
        MULTI_PROCESS
    }

    void clearAll();

    Map<String, ?> getAll();

    boolean getBoolean(String str, boolean z);

    double getDouble(String str, double d);

    float getFloat(String str, float f);

    int getInt(String str, int i);

    long getLong(String str, long j);

    String getString(String str, String str2);

    boolean putBoolean(String str, boolean z);

    boolean putBoolean(String str, boolean z, NotifyType notifyType);

    boolean putDouble(String str, double d);

    boolean putDouble(String str, double d, NotifyType notifyType);

    boolean putFloat(String str, float f);

    boolean putFloat(String str, float f, NotifyType notifyType);

    boolean putInt(String str, int i);

    boolean putInt(String str, int i, NotifyType notifyType);

    boolean putLong(String str, long j);

    boolean putLong(String str, long j, NotifyType notifyType);

    boolean putString(String str, String str2);

    boolean putString(String str, String str2, NotifyType notifyType);

    void registerOnSharedPreferenceChangeListener(LeJaOnSharedPreferenceChangeListener leJaOnSharedPreferenceChangeListener);

    boolean remove(String str);

    boolean remove(String str, NotifyType notifyType);

    void unregisterOnSharedPreferenceChangeListener(LeJaOnSharedPreferenceChangeListener leJaOnSharedPreferenceChangeListener);
}