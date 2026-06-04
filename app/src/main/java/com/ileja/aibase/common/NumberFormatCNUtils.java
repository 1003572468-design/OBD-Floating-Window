package com.ileja.aibase.common;

import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class NumberFormatCNUtils {
    static HashMap<String, String> cnHash;
    static HashMap<String, String> unitHash;
    static String[] cnArray = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    static String[] unitArray = {"十", "百", "千", "万", "亿"};

    public static double cnNumber2IntNumber(String str) {
        int iIntValue;
        HashMap<String, Integer> cN2NumberHash = getCN2NumberHash();
        List listAsList = Arrays.asList(unitArray);
        String str2 = "";
        int i = 0;
        double dIntValue = 0.0d;
        while (i < str.length()) {
            String strValueOf = String.valueOf(str.charAt(i));
            int i2 = i + 1;
            if (i2 < str.length()) {
                String strValueOf2 = String.valueOf(str.charAt(i2));
                if (cN2NumberHash.containsKey(strValueOf)) {
                    if (listAsList.contains(strValueOf2)) {
                        dIntValue += (double) (cN2NumberHash.get(strValueOf).intValue() * ("十".equals(strValueOf2) ? 10 : "百".equals(strValueOf2) ? 100 : "千".equals(strValueOf2) ? 1000 : "万".equals(strValueOf2) ? 10000 : "亿".equals(strValueOf2) ? 10000000 : 1));
                        i = i2;
                    } else if ("十".equals(strValueOf)) {
                        iIntValue = cN2NumberHash.get(strValueOf).intValue() * 10;
                        dIntValue += (double) iIntValue;
                    } else {
                        str2 = str2 + cN2NumberHash.get(strValueOf);
                    }
                }
            } else {
                try {
                    if (cN2NumberHash.containsKey(strValueOf)) {
                        if (TextUtils.isEmpty(str2)) {
                            iIntValue = cN2NumberHash.get(strValueOf).intValue();
                            dIntValue += (double) iIntValue;
                        } else {
                            str2 = str2 + cN2NumberHash.get(strValueOf);
                        }
                    } else if (strValueOf.matches("[0-9]+")) {
                        iIntValue = Integer.valueOf(strValueOf).intValue();
                        dIntValue += (double) iIntValue;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        if (TextUtils.isEmpty(str2) || dIntValue != 0.0d) {
            return dIntValue;
        }
        try {
            return Integer.valueOf(str2).intValue();
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
            return dIntValue;
        }
    }

    public static String[] convert2IntNumber(String str) {
        int iIndexOf;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        List listAsList = Arrays.asList(unitArray);
        HashMap<String, Integer> cN2NumberHash = getCN2NumberHash();
        if (!TextUtils.isEmpty(str) && str.contains("幺") && (iIndexOf = str.indexOf("幺")) >= 0) {
            if (iIndexOf == 0) {
                int i = iIndexOf + 1;
                if (i < str.length() && cN2NumberHash.containsKey(str.substring(iIndexOf, i))) {
                    str = str.replace("幺", "一");
                }
            } else if (iIndexOf == str.length() - 1) {
                int i2 = iIndexOf - 1;
                if (i2 > 0 && cN2NumberHash.containsKey(str.substring(i2, iIndexOf))) {
                    str = str.replace("幺", "一");
                }
            } else {
                String strSubstring = str.substring(iIndexOf - 1, iIndexOf);
                String strSubstring2 = str.substring(iIndexOf, iIndexOf + 1);
                if (cN2NumberHash.containsKey(strSubstring) || cN2NumberHash.containsKey(strSubstring2)) {
                    str = str.replace("幺", "一");
                }
            }
        }
        int i3 = 0;
        while (i3 < str.length()) {
            String strValueOf = String.valueOf(str.charAt(i3));
            if (cN2NumberHash.containsKey(strValueOf)) {
                int i4 = i3 + 1;
                if (i4 < str.length()) {
                    String strValueOf2 = String.valueOf(str.charAt(i4));
                    if (listAsList.contains(strValueOf2)) {
                        stringBuffer2.append(strValueOf);
                        stringBuffer2.append(strValueOf2);
                        stringBuffer.append(strValueOf);
                        stringBuffer.append(strValueOf2);
                        i3 = i4;
                    } else {
                        stringBuffer2.append(strValueOf);
                        stringBuffer.append(strValueOf);
                    }
                } else {
                    stringBuffer.append(strValueOf);
                }
            } else {
                stringBuffer.append(strValueOf);
            }
            i3++;
        }
        int iCnNumber2IntNumber = (int) cnNumber2IntNumber(stringBuffer2.toString());
        Log.d("NumberFormatCNUtils", "convert2IntNumber:" + stringBuffer.toString() + " ,numberStr:" + ((Object) stringBuffer2) + " ,numberStr2IntNumber:" + iCnNumber2IntNumber);
        return !TextUtils.isEmpty(stringBuffer2.toString()) ? new String[]{str, stringBuffer.toString().replace(stringBuffer2.toString(), String.valueOf(iCnNumber2IntNumber))} : new String[]{str, stringBuffer.toString()};
    }

    private static HashMap<String, Integer> getCN2NumberHash() {
        HashMap<String, Integer> map = new HashMap<>();
        int length = cnArray.length;
        for (int i = 0; i < length; i++) {
            map.put(cnArray[i], Integer.valueOf(i));
        }
        return map;
    }

    private static HashMap<String, String> getNumber2CNHash() {
        HashMap<String, String> map = new HashMap<>();
        int length = cnArray.length;
        for (int i = 0; i < length; i++) {
            map.put(String.valueOf(i), cnArray[i]);
        }
        return map;
    }

    private static HashMap<String, String> getUnitHash() {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 2; i < 7; i++) {
            map.put(String.valueOf(i), unitArray[i - 2]);
        }
        return map;
    }
}