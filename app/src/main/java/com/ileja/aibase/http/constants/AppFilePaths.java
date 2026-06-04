package com.ileja.aibase.http.constants;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class AppFilePaths {
    public static final ArrayList<APPCacheType> ClearCacheConfig = new ArrayList<APPCacheType>() { // from class: com.ileja.aibase.http.constants.AppFilePaths.1
        {
            add(APPCacheType.CGI);
            add(APPCacheType.IMAGES);
        }
    };
    public static final String LOGFILENAME = "ilejalog.txt";

    /* JADX INFO: renamed from: com.ileja.aibase.http.constants.AppFilePaths$2 */
    static /* synthetic */ class C10942 {

        /* JADX INFO: renamed from: a */
        static final /* synthetic */ int[] f5910a;

        static {
            int[] iArr = new int[APPCacheType.values().length];
            f5910a = iArr;
            try {
                iArr[APPCacheType.CGI.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5910a[APPCacheType.IMAGES.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static final String getCacheDirByType(Context context, APPCacheType aPPCacheType) {
        int i = C10942.f5910a[aPPCacheType.ordinal()];
        return i != 1 ? i != 2 ? getImageCacheDir(context) : getImageCacheDir(context) : getDataCacheDir(context);
    }

    public static final String getCacheRootDir(Context context, boolean z) {
        String str;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            str = Environment.getExternalStorageDirectory() + File.separator + ".ileja";
        } else {
            str = context.getCacheDir().getAbsolutePath() + File.separator + ".ileja";
        }
        if (!z) {
            return str;
        }
        return str + File.separator + "cache";
    }

    public static final String getCrashLogDir(Context context) {
        return getCacheRootDir(context, false) + File.separator + "crashlog";
    }

    public static final String getDailyLogDir(Context context) {
        return getCacheRootDir(context, false) + File.separator + "dailylog";
    }

    public static final String getDailyLogZipDir(Context context) {
        return getCacheRootDir(context, false) + File.separator + "dailylogZip";
    }

    public static final String getDataCacheDir(Context context) {
        return getCacheRootDir(context, true) + File.separator + "data";
    }

    public static final String getImageCacheDir(Context context) {
        return getCacheRootDir(context, true) + File.separator + "image";
    }

    public static final String getPlayHistoryImageCacheDir(Context context) {
        return getCacheRootDir(context, true) + File.separator + "playhistory";
    }

    public static final String getUpdateDir(Context context) {
        return getCacheRootDir(context, true) + File.separator + "update" + File.separator;
    }

    public static void makeAppFileDir(String str) {
        File file = new File(str);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    public static final String getDataCacheDir(String str) {
        return str + File.separator + "data";
    }

    public static final String getImageCacheDir(String str) {
        return str + File.separator + "image";
    }

    public static final String getPlayHistoryImageCacheDir(String str) {
        return str + File.separator + "playhistory";
    }

    public static final String getCacheDirByType(String str, APPCacheType aPPCacheType) {
        int i = C10942.f5910a[aPPCacheType.ordinal()];
        if (i == 1) {
            return str + File.separator + "data";
        }
        if (i != 2) {
            return str + File.separator + "image";
        }
        return str + File.separator + "image";
    }
}