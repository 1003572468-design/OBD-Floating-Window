package com.ileja.aibase.phone;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.ileja.aibase.AiBase;

/* JADX INFO: loaded from: classes.dex */
public class NetworkStateUtil {
    public static boolean isNetWorkOK() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) AiBase.getInst().getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }
}