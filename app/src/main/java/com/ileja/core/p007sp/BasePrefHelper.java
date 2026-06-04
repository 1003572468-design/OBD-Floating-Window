package com.ileja.core.p007sp;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class BasePrefHelper extends BasePrefProvider {
    private static final String KEY_IS_IMPORT_FROMP_SHARED = "is_import_from_shared";
    private static final String TAG = "BasePrefHelper";

    /* JADX INFO: renamed from: b */
    protected static MMKVSharedPreferences f6329b;

    /* JADX INFO: renamed from: e */
    protected static String m4393e(Context context) {
        return context.getDir("shared_prefs", 0).getAbsolutePath();
    }

    /* JADX INFO: renamed from: f */
    protected static void m4394f(Context context, String str, MMKVSharedPreferences mMKVSharedPreferences) {
    }
}