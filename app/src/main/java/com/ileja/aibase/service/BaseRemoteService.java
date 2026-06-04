package com.ileja.aibase.service;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseRemoteService extends KeepLiveService {
    private static final String TAG = "BaseRemoteService";
    protected Application context;
    protected final Map<IBinder, IReceiver> mReceivers = new HashMap();
    protected Handler mHandler = null;
    protected HandlerThread handlerThread = null;

    /* JADX INFO: Access modifiers changed from: protected */
    public interface IReceiver extends IBinder.DeathRecipient {
    }

    protected void addReceiver(IBinder iBinder, IReceiver iReceiver) {
        synchronized (this.mReceivers) {
            try {
                iBinder.linkToDeath(iReceiver, 0);
                this.mReceivers.put(iBinder, iReceiver);
            } catch (RemoteException e) {
                e.printStackTrace();
                AILog.m4031e(TAG, "linkToDeath failed.", LogLevel.RELEASE);
            }
        }
    }

    @Override // com.ileja.aibase.service.KeepLiveService
    public int getCurrentClientNumber() {
        return this.mReceivers.size();
    }

    protected Handler getHandler() {
        if (this.mHandler == null) {
            Looper looperMyLooper = Looper.myLooper();
            if (looperMyLooper == null) {
                looperMyLooper = Looper.getMainLooper();
            }
            this.mHandler = new Handler(looperMyLooper);
        }
        return this.mHandler;
    }

    protected IReceiver getReceiverByBinder(IBinder iBinder) {
        return this.mReceivers.get(iBinder);
    }

    protected boolean isNoReceivers() {
        return this.mReceivers.isEmpty();
    }

    @Override // com.ileja.aibase.service.KeepLiveService, android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // com.ileja.aibase.service.KeepLiveService, android.app.Service
    public void onCreate() {
        super.onCreate();
        this.context = getApplication();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        synchronized (this.mReceivers) {
            this.mReceivers.clear();
        }
        return super.onUnbind(intent);
    }

    protected void removeReceiver(IBinder iBinder, IReceiver iReceiver) {
        synchronized (this.mReceivers) {
            iBinder.unlinkToDeath(iReceiver, 0);
            this.mReceivers.remove(iBinder);
        }
    }
}