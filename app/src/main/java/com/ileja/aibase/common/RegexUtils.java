package com.ileja.aibase.common;

import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class RegexUtils {
    public static String charReplace(String str, String str2, String str3) {
        return Pattern.compile(str2).matcher(str).replaceAll(str3);
    }

    public static boolean getString(String str, String str2) {
        return Pattern.compile(str2).matcher(str).find();
    }
}