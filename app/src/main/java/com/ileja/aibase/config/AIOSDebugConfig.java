package com.ileja.aibase.config;

import android.net.Uri;
import com.ileja.core.export.CommDataProviderConstant;

/* JADX INFO: loaded from: classes.dex */
public class AIOSDebugConfig {
    private static final String AUTHORITY = "com.aispeech.aios.provider";
    private static final String TAG = "AIOSDebugConfig";
    private static final Uri CONTENT_URI = Uri.parse("content://com.aispeech.aios.provider/config");
    private static final String[] projection = {"key", CommDataProviderConstant.CommDataEntry.COLUMN_NAME_VALUE};

    /* JADX WARN: Removed duplicated region for block: B:39:0x00b0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getRemoteServer(android.content.Context r10) throws java.lang.Throwable {
        /*
            java.lang.String r0 = "AIOSDebugConfig"
            java.lang.String r1 = "127.0.0.1:50001"
            r2 = 0
            android.content.ContentResolver r3 = r10.getContentResolver()     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8d
            android.net.Uri r4 = com.ileja.aibase.config.AIOSDebugConfig.CONTENT_URI     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8d
            java.lang.String[] r5 = com.ileja.aibase.config.AIOSDebugConfig.projection     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8d
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r10 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L8b java.lang.Exception -> L8d
            if (r10 != 0) goto L1c
            if (r10 == 0) goto L1b
            r10.close()
        L1b:
            return r1
        L1c:
            r3 = 0
        L1d:
            boolean r4 = r10.moveToNext()     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            if (r4 == 0) goto L79
            java.lang.String r4 = "key"
            int r4 = r10.getColumnIndex(r4)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r4 = r10.getString(r4)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r5 = "isRemote"
            boolean r5 = r4.equals(r5)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r6 = "value"
            if (r5 == 0) goto L54
            int r3 = r10.getColumnIndex(r6)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            int r3 = r10.getInt(r3)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            r4.<init>()     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r5 = "getRemoteServer(), isRemote:"
            r4.append(r5)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            r4.append(r3)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            com.ileja.aibase.common.AILog.m4028d(r0, r4)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            goto L1d
        L54:
            java.lang.String r5 = "ip"
            boolean r4 = r4.equals(r5)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            if (r4 == 0) goto L1d
            int r2 = r10.getColumnIndex(r6)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r2 = r10.getString(r2)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            r4.<init>()     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r5 = "getRemoteServer(), ip:"
            r4.append(r5)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            r4.append(r2)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            com.ileja.aibase.common.AILog.m4028d(r0, r4)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            goto L1d
        L79:
            r4 = 1
            if (r3 != r4) goto L83
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Exception -> L89 java.lang.Throwable -> Lac
            if (r3 != 0) goto L83
            r1 = r2
        L83:
            if (r10 == 0) goto L97
        L85:
            r10.close()
            goto L97
        L89:
            r2 = move-exception
            goto L91
        L8b:
            r0 = move-exception
            goto Lae
        L8d:
            r10 = move-exception
            r9 = r2
            r2 = r10
            r10 = r9
        L91:
            r2.printStackTrace()     // Catch: java.lang.Throwable -> Lac
            if (r10 == 0) goto L97
            goto L85
        L97:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r2 = "getRemoteServer(), BusClient.DEFAULT_BUS_SERVER: "
            r10.append(r2)
            r10.append(r1)
            java.lang.String r10 = r10.toString()
            com.ileja.aibase.common.AILog.m4034i(r0, r10)
            return r1
        Lac:
            r0 = move-exception
            r2 = r10
        Lae:
            if (r2 == 0) goto Lb3
            r2.close()
        Lb3:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ileja.aibase.config.AIOSDebugConfig.getRemoteServer(android.content.Context):java.lang.String");
    }
}