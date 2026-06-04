package com.ileja.core.export;

import android.net.Uri;
import android.provider.BaseColumns;

/* JADX INFO: loaded from: classes.dex */
public class CommDataProviderConstant {
    public static final String AUTHORITY = "com.ileja.common.data.provider";

    public static abstract class CommDataEntry implements BaseColumns {
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final Uri CONTENT_URI = Uri.parse("content://com.ileja.common.data.provider/data");
    }
}