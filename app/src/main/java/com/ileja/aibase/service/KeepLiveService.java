package com.ileja.aibase.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/* JADX INFO: loaded from: classes.dex */
public abstract class KeepLiveService extends Service {
    private Notification buildForegroundNotification(String str, String str2, int i) {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setOngoing(true);
            builder.setContentTitle(str).setContentText(getCurrentClientNumber() + " receivers").setSmallIcon(getIconID()).setTicker(str2);
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract int getAppNameID();

    public abstract int getCurrentClientNumber();

    public abstract int getIconID();

    protected void notifyClientChange() {
        try {
            startForeground(getAppNameID(), buildForegroundNotification(getString(getAppNameID()), getString(getAppNameID()) + " is running", getCurrentClientNumber()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        notifyClientChange();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return 3;
    }
}