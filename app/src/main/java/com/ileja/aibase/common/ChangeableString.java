package com.ileja.aibase.common;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/* JADX INFO: loaded from: classes.dex */
public class ChangeableString extends SpannableStringBuilder {
    private Context mContext;

    public ChangeableString(Context context) {
        this.mContext = context;
    }

    public ChangeableString appendNormalInfo(String str, int i, int i2) {
        int length = length();
        append((CharSequence) str);
        int color = this.mContext.getResources().getColor(i2);
        setSpan(new AbsoluteSizeSpan(i, true), length, length(), 33);
        setSpan(new ForegroundColorSpan(color), length, length(), 33);
        return this;
    }

    public ChangeableString appendSizeInfo(String str, int i) {
        int length = length();
        append((CharSequence) str);
        setSpan(new AbsoluteSizeSpan(i, true), length, length(), 33);
        return this;
    }

    public ChangeableString(Context context, CharSequence charSequence) {
        super(charSequence);
        this.mContext = context;
    }
}