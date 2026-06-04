package com.ileja.aibase.common;

import android.content.Context;
import android.support.v4.os.EnvironmentCompat;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.tencent.bugly.crashreport.CrashReport;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppBlockCanaryContext extends BlockCanaryContext {
    private static final String TAG = "AppBlockCanaryContext";

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public List<String> concernPackages() {
        return null;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean deleteFilesInWhiteList() {
        return true;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean displayNotification() {
        return false;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean filterNonConcernStack() {
        return false;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext, com.github.moduth.blockcanary.BlockInterceptor
    public void onBlock(Context context, BlockInfo blockInfo) {
        AILog.m4028d(TAG, "BlockCanary BlockInfo \n " + blockInfo.toString());
        CrashReport.postCatchedException(new Exception(blockInfo.toString()));
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public int provideBlockThreshold() {
        return 500;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public int provideDumpInterval() {
        return provideBlockThreshold();
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public int provideMonitorDuration() {
        return -1;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String provideNetworkType() {
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String providePath() {
        return "/blockcanary/";
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String provideQualifier() {
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public String provideUid() {
        return "uid";
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public List<String> provideWhiteList() {
        LinkedList linkedList = new LinkedList();
        linkedList.add("org.chromium");
        return linkedList;
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public void upload(File file) {
        throw new UnsupportedOperationException();
    }

    @Override // com.github.moduth.blockcanary.BlockCanaryContext
    public boolean zip(File[] fileArr, File file) {
        return false;
    }
}