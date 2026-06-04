package com.ileja.aibase.phone;

import android.annotation.TargetApi;
import android.os.StatFs;

/* JADX INFO: loaded from: classes.dex */
public class SDcardUtil {
    public static final String TAG = "SDcardUtil";

    public static long getInnerSDcard() {
        return getSDCard("/sdcard");
    }

    public static long getOUTSDcard() {
        return getSDCard("/storage/sdcard1");
    }

    @TargetApi(18)
    private static long getSDCard(String str) {
        StatFs statFs = new StatFs(str);
        long blockSizeLong = statFs.getBlockSizeLong();
        statFs.getBlockCountLong();
        return blockSizeLong * statFs.getAvailableBlocksLong();
    }
}