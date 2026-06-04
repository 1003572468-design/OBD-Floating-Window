package com.ileja.aibase.utils;

import android.text.TextUtils;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class GsonUtils {
    public static String arrayListToJsonArray(List<String> list) {
        if (list == null || list.size() <= 0) {
            return "";
        }
        String str = "[";
        for (int i = 0; i < list.size(); i++) {
            String str2 = str + String.format("%s", list.get(i));
            str = i != list.size() - 1 ? str2 + "," : str2;
        }
        return str + "]";
    }

    public static ArrayList fromJsonToArrayList(String str, Type type) {
        if (str != null && type != null) {
            try {
                return (ArrayList) new Gson().fromJson(str, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T gsonResolve(String str, Class<T> cls) {
        if (str == null) {
            return null;
        }
        try {
            return (T) new Gson().fromJson(str, (Class) cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> jsonToMap(String str, Type type) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return (HashMap) new Gson().fromJson(str, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String mapToJson(Map<String, T> map) {
        return new Gson().toJson(map);
    }

    public static String toJson(Object obj) {
        System.currentTimeMillis();
        try {
            return new Gson().toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T gsonResolve(String str, Type type) {
        if (str == null) {
            return null;
        }
        try {
            return (T) new Gson().fromJson(str, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}