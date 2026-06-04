package com.ileja.aibase.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class NumStrUtils {
    private static final String TAG = "com.ileja.aibase.common.NumStrUtils";
    private static final String[] mDigitMap = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] mUnitMap = {"", "十", "百", "千", "万"};

    public static void main(String[] strArr) {
        System.out.println(transPhoneContact("哈123ss456"));
    }

    private static String numSeqToStr(String str) {
        if (str.length() > 5) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        int length = str.length() - 1;
        for (int length2 = str.length() - 1; length2 >= 0; length2--) {
            int iCharAt = str.charAt(length2) - '0';
            if (iCharAt != 0) {
                sb.append(mUnitMap[length - length2]);
                sb.append(mDigitMap[iCharAt]);
            }
        }
        return sb.reverse().toString();
    }

    private static String numToStr(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(mDigitMap[str.charAt(i) - '0']);
        }
        return sb.toString();
    }

    public static String transPhoneContact(String str) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = Pattern.compile("([0-9]+)").matcher(str);
        int iEnd = 0;
        while (matcher.find()) {
            int iStart = matcher.start();
            String strGroup = matcher.group();
            if (iStart > iEnd) {
                sb.append(str.subSequence(iEnd, iStart));
            }
            sb.append(numToStr(strGroup));
            iEnd = matcher.end();
        }
        if (iEnd < str.length()) {
            sb.append(str.subSequence(iEnd, str.length()));
        }
        return sb.toString();
    }
}