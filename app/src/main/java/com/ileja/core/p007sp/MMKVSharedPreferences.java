package com.ileja.core.p007sp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.ileja.aibase.common.AILog;
import com.ileja.core.p007sp.IMPSharedPreference;
import com.tencent.mmkv.MMKV;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MMKVSharedPreferences implements IMPSharedPreference {
    private static final String TAG = "MMKVSharedPreferences";
    private static Context mContext;
    private MMKV mMmkv;
    private List<IMPSharedPreference.LeJaOnSharedPreferenceChangeListener> mOnSharedPreferenceChangeListeners;
    private BroadcastReceiver mPreferenceChangeListenerBroadCast;

    /* JADX INFO: renamed from: com.ileja.core.sp.MMKVSharedPreferences$2 */
    static /* synthetic */ class C13022 {

        /* JADX INFO: renamed from: a */
        static final /* synthetic */ int[] f6332a;

        static {
            int[] iArr = new int[IMPSharedPreference.NotifyType.values().length];
            f6332a = iArr;
            try {
                iArr[IMPSharedPreference.NotifyType.MULTI_PROCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f6332a[IMPSharedPreference.NotifyType.PRIVATE_PROCESS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f6332a[IMPSharedPreference.NotifyType.BOTH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public MMKVSharedPreferences(Context context, String str, String str2) {
        this(context, str, str2, 1);
    }

    private void notifyChange(IMPSharedPreference.NotifyType notifyType, String str) {
        int i = C13022.f6332a[notifyType.ordinal()];
        if (i == 1) {
            sendBroadcast(str);
            return;
        }
        if (i == 2) {
            Iterator<IMPSharedPreference.LeJaOnSharedPreferenceChangeListener> it = this.mOnSharedPreferenceChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onSharedPreferenceChanged(str);
            }
        } else {
            if (i != 3) {
                return;
            }
            sendBroadcast(str);
            Iterator<IMPSharedPreference.LeJaOnSharedPreferenceChangeListener> it2 = this.mOnSharedPreferenceChangeListeners.iterator();
            while (it2.hasNext()) {
                it2.next().onSharedPreferenceChanged(str);
            }
        }
    }

    private Map<String, String> queryAll() {
        String[] strArrAllKeys;
        HashMap map = new HashMap();
        MMKV mmkv = this.mMmkv;
        if (mmkv != null && (strArrAllKeys = mmkv.allKeys()) != null && strArrAllKeys.length > 0) {
            for (String str : strArrAllKeys) {
                map.put(str, this.mMmkv.decodeString(str));
            }
        }
        AILog.m4028d(TAG, "query all exec result:" + map);
        return map;
    }

    private void sendBroadcast(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sendBroadcast key:");
        sb.append(str);
        sb.append(mContext == null ? " Context is NULl" : "");
        AILog.m4028d(str2, sb.toString());
        if (mContext != null) {
            Intent intent = new Intent(IMPSharedPreference.INTENT_ACTION_SHAREDSP_STATUS);
            intent.putExtra("key", str);
            mContext.sendBroadcast(intent);
        }
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public void clearAll() {
        MMKV mmkv = this.mMmkv;
        if (mmkv != null) {
            mmkv.clearAll();
        }
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public Map<String, String> getAll() {
        return queryAll();
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean getBoolean(String str, boolean z) {
        MMKV mmkv = this.mMmkv;
        if (mmkv != null) {
            z = mmkv.decodeBool(str, z);
        }
        AILog.m4028d(TAG, "getBoolean key -> " + str + ", result -> " + z);
        return z;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public double getDouble(String str, double d) {
        MMKV mmkv = this.mMmkv;
        return mmkv != null ? mmkv.decodeDouble(str, d) : d;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public float getFloat(String str, float f) {
        MMKV mmkv = this.mMmkv;
        return mmkv != null ? mmkv.decodeFloat(str, f) : f;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public int getInt(String str, int i) {
        MMKV mmkv = this.mMmkv;
        return mmkv != null ? mmkv.decodeInt(str, i) : i;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public long getLong(String str, long j) {
        MMKV mmkv = this.mMmkv;
        return mmkv != null ? mmkv.decodeLong(str, j) : j;
    }

    public MMKV getMmkv() {
        return this.mMmkv;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public String getString(String str, String str2) {
        MMKV mmkv = this.mMmkv;
        return mmkv != null ? mmkv.decodeString(str, str2) : str2;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putBoolean(String str, boolean z) {
        return putBoolean(str, z, IMPSharedPreference.NotifyType.MULTI_PROCESS);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putDouble(String str, double d, IMPSharedPreference.NotifyType notifyType) {
        AILog.m4028d(TAG, "putDouble key -> " + str + ", value -> " + d);
        MMKV mmkv = this.mMmkv;
        if (mmkv == null || !mmkv.encode(str, d)) {
            return false;
        }
        notifyChange(notifyType, str);
        return true;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putFloat(String str, float f) {
        return putFloat(str, f, IMPSharedPreference.NotifyType.MULTI_PROCESS);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putInt(String str, int i) {
        return putInt(str, i, IMPSharedPreference.NotifyType.MULTI_PROCESS);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putLong(String str, long j) {
        return putLong(str, j, IMPSharedPreference.NotifyType.MULTI_PROCESS);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putString(String str, String str2) {
        return putString(str, str2, IMPSharedPreference.NotifyType.MULTI_PROCESS);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public void registerOnSharedPreferenceChangeListener(IMPSharedPreference.LeJaOnSharedPreferenceChangeListener leJaOnSharedPreferenceChangeListener) {
        if (this.mOnSharedPreferenceChangeListeners.contains(leJaOnSharedPreferenceChangeListener)) {
            return;
        }
        this.mOnSharedPreferenceChangeListeners.add(leJaOnSharedPreferenceChangeListener);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean remove(String str) {
        return remove(str, IMPSharedPreference.NotifyType.MULTI_PROCESS);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public void unregisterOnSharedPreferenceChangeListener(IMPSharedPreference.LeJaOnSharedPreferenceChangeListener leJaOnSharedPreferenceChangeListener) {
        this.mOnSharedPreferenceChangeListeners.remove(leJaOnSharedPreferenceChangeListener);
    }

    public MMKVSharedPreferences(Context context, String str, String str2, int i) {
        this.mPreferenceChangeListenerBroadCast = new BroadcastReceiver() { // from class: com.ileja.core.sp.MMKVSharedPreferences.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Iterator it = MMKVSharedPreferences.this.mOnSharedPreferenceChangeListeners.iterator();
                while (it.hasNext()) {
                    ((IMPSharedPreference.LeJaOnSharedPreferenceChangeListener) it.next()).onSharedPreferenceChanged(intent.getStringExtra("key"));
                }
            }
        };
        this.mOnSharedPreferenceChangeListeners = new CopyOnWriteArrayList();
        if (TextUtils.isEmpty(str2)) {
            throw new RuntimeException("table is null");
        }
        mContext = context.getApplicationContext();
        str = TextUtils.isEmpty(str) ? MMKV.getRootDir() : str;
        AILog.m4028d(TAG, "fileDir = " + str);
        this.mMmkv = MMKV.mmkvWithID(str2, i, null, str);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IMPSharedPreference.INTENT_ACTION_SHAREDSP_STATUS);
        mContext.registerReceiver(this.mPreferenceChangeListenerBroadCast, intentFilter);
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putBoolean(String str, boolean z, IMPSharedPreference.NotifyType notifyType) {
        AILog.m4028d(TAG, "putBoolean key -> " + str + ", value -> " + z);
        MMKV mmkv = this.mMmkv;
        if (mmkv == null || !mmkv.encode(str, z)) {
            return false;
        }
        notifyChange(notifyType, str);
        return true;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putFloat(String str, float f, IMPSharedPreference.NotifyType notifyType) {
        AILog.m4028d(TAG, "putString key -> " + str + ", value -> " + f);
        MMKV mmkv = this.mMmkv;
        if (mmkv == null || !mmkv.encode(str, f)) {
            return false;
        }
        notifyChange(notifyType, str);
        return true;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putInt(String str, int i, IMPSharedPreference.NotifyType notifyType) {
        AILog.m4028d(TAG, "putString key -> " + str + ", value -> " + i);
        MMKV mmkv = this.mMmkv;
        if (mmkv == null || !mmkv.encode(str, i)) {
            return false;
        }
        notifyChange(notifyType, str);
        return true;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putLong(String str, long j, IMPSharedPreference.NotifyType notifyType) {
        AILog.m4028d(TAG, "putString key -> " + str + ", value -> " + j);
        MMKV mmkv = this.mMmkv;
        if (mmkv == null || !mmkv.encode(str, j)) {
            return false;
        }
        notifyChange(notifyType, str);
        return true;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putString(String str, String str2, IMPSharedPreference.NotifyType notifyType) {
        AILog.m4028d(TAG, "putString key -> " + str + ", value -> " + str2);
        MMKV mmkv = this.mMmkv;
        if (mmkv == null || !mmkv.encode(str, str2)) {
            return false;
        }
        notifyChange(notifyType, str);
        return true;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean remove(String str, IMPSharedPreference.NotifyType notifyType) {
        AILog.m4028d(TAG, "remove key -> " + str);
        MMKV mmkv = this.mMmkv;
        if (mmkv == null || !mmkv.contains(str)) {
            return false;
        }
        this.mMmkv.removeValueForKey(str);
        notifyChange(notifyType, str);
        return true;
    }

    @Override // com.ileja.core.p007sp.IMPSharedPreference
    public boolean putDouble(String str, double d) {
        return putDouble(str, d, IMPSharedPreference.NotifyType.MULTI_PROCESS);
    }
}