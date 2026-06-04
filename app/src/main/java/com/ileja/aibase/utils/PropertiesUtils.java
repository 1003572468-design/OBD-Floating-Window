package com.ileja.aibase.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/* JADX INFO: loaded from: classes.dex */
public class PropertiesUtils {
    private static final String TAG = "PropertiesUtils";
    private static PropertiesUtils mReadProperties;

    private PropertiesUtils() {
    }

    public static synchronized PropertiesUtils getInstance() {
        if (mReadProperties == null) {
            mReadProperties = new PropertiesUtils();
        }
        return mReadProperties;
    }

    public Properties loadConfig(String str) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(str), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public void saveConfig(String str, Properties properties) {
        saveConfig(str, properties, false);
    }

    public void saveConfig(String str, Properties properties, boolean z) {
        try {
            properties.store(new FileOutputStream(str, z), (String) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}