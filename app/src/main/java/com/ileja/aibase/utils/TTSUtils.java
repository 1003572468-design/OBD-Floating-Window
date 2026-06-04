package com.ileja.aibase.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class TTSUtils {
    public static String handlerSpecialTTS(@NonNull String str) {
        return replaceVoiceText(replaceVoiceText(replaceVoiceText(str, "行驶", "刑使"), "限速110", "限速一百一十"), "限速120", "限速一百二十");
    }

    public static String replaceVoiceText(@NonNull String str, @NonNull String str2, @NonNull String str3) {
        return !TextUtils.isEmpty(str) ? str.replace(str2, str3) : str;
    }
}