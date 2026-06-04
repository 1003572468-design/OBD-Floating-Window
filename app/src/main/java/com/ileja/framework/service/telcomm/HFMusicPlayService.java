package com.ileja.framework.service.telcomm;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aitelcomm.music.IMusicPlayService;
import com.ileja.aitelcomm.music.IPlayerNotifyer;
import com.ileja.aitelcomm.music.bean.MusicInfo;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;
import com.ileja.framework.service.telcomm.listener.MusicPlayListener;

/* JADX INFO: loaded from: classes.dex */
public class HFMusicPlayService extends HFService {
    private static final String CLS_NAME = "com.ileja.aitelcomm.music.service.MusicPlayService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private static final String TAG = "HFMusicPlayService";
    private IMusicPlayService mBinder;
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.8
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "fm music service connected", LogLevel.RELEASE);
            HFMusicPlayService.this.mBinder = IMusicPlayService.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFMusicPlayService.this.mBinder = null;
            AILog.m4029d(HudSystem.TAG, "fm music service disconnected", LogLevel.RELEASE);
        }
    };

    private class PlayerNotifyerImpl extends IPlayerNotifyer.Stub {

        /* JADX INFO: renamed from: a */
        MusicPlayListener f6554a;

        public PlayerNotifyerImpl(HFMusicPlayService hFMusicPlayService, MusicPlayListener musicPlayListener) {
            this.f6554a = musicPlayListener;
        }

        @Override // com.ileja.aitelcomm.music.IPlayerNotifyer
        public void onComplete(int i) {
            AILog.m4029d(HFMusicPlayService.TAG, "onComplete, playSecond:" + i, LogLevel.RELEASE);
            this.f6554a.onComplete(i);
        }

        @Override // com.ileja.aitelcomm.music.IPlayerNotifyer
        public void onProgressStr(float f, int i) {
            this.f6554a.onProgressStr(f, i);
        }

        @Override // com.ileja.aitelcomm.music.IPlayerNotifyer
        public void onReady() {
            AILog.m4029d(HFMusicPlayService.TAG, "onReady", LogLevel.RELEASE);
            this.f6554a.onReady();
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4035i(TAG, "oncreate service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        AILog.m4035i(TAG, "destroy service", LogLevel.RELEASE);
        if (this.mBinder != null) {
            m4406g(application);
        }
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFMusicPlayService;
    }

    public void init() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFMusicPlayService.this.mBinder.init();
                    AILog.m4034i(HFMusicPlayService.TAG, "init:");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    AILog.m4035i(HFMusicPlayService.TAG, "exception " + e, LogLevel.RELEASE);
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicPlayService_init");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public boolean pause() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFMusicPlayService.this.mBinder.pause();
                    AILog.m4035i(HFMusicPlayService.TAG, "pause", LogLevel.RELEASE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicPlayService_control_music");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        IMusicPlayService iMusicPlayService = this.mBinder;
        boolean zPause = false;
        if (iMusicPlayService != null) {
            try {
                zPause = iMusicPlayService.pause();
                AILog.m4035i(TAG, "pause", LogLevel.RELEASE);
            } catch (RemoteException e) {
                e.printStackTrace();
                AILog.m4035i(TAG, "exception " + e, LogLevel.RELEASE);
            }
        }
        m4403d(pendingAction);
        return zPause;
    }

    public void play(final MusicInfo musicInfo, final boolean z, final MusicPlayListener musicPlayListener) {
        AILog.m4035i(TAG, "prepare play -> " + musicInfo + " , continueLastPlay:" + z, LogLevel.RELEASE);
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFMusicPlayService.this.mBinder.play(musicInfo, z, new PlayerNotifyerImpl(HFMusicPlayService.this, musicPlayListener));
                    AILog.m4035i(HFMusicPlayService.TAG, "start play:" + musicInfo + " , continueLastPlay:" + z, LogLevel.RELEASE);
                } catch (RemoteException e) {
                    AILog.m4035i(HFMusicPlayService.TAG, "exception " + e, LogLevel.RELEASE);
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicPlayService_operation_music");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        } else {
            AILog.m4034i(TAG, "play, but mBinder is null now!!!");
        }
        m4403d(pendingAction);
    }

    public void release() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFMusicPlayService.this.mBinder.release();
                    AILog.m4035i(HFMusicPlayService.TAG, "release ", LogLevel.RELEASE);
                } catch (RemoteException e) {
                    AILog.m4035i(HFMusicPlayService.TAG, "exception " + e, LogLevel.RELEASE);
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicPlayService_operation_music");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        } else {
            AILog.m4034i(TAG, "release, but mBinder is null now!!!");
        }
        m4403d(pendingAction);
    }

    public boolean resume() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFMusicPlayService.this.mBinder.resume();
                    AILog.m4035i(HFMusicPlayService.TAG, "resume", LogLevel.RELEASE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    AILog.m4035i(HFMusicPlayService.TAG, "exception " + e, LogLevel.RELEASE);
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicPlayService_control_music");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        IMusicPlayService iMusicPlayService = this.mBinder;
        boolean zResume = false;
        if (iMusicPlayService != null) {
            try {
                zResume = iMusicPlayService.resume();
                AILog.m4035i(TAG, "resume", LogLevel.RELEASE);
            } catch (RemoteException e) {
                e.printStackTrace();
                AILog.m4035i(TAG, "exception " + e, LogLevel.RELEASE);
            }
        }
        m4403d(pendingAction);
        return zResume;
    }

    public void setupVolume(final float f, final int i) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFMusicPlayService.this.mBinder.setupVolume(f, i);
                    AILog.m4035i(HFMusicPlayService.TAG, "setupVolume volume:" + f + ",pan:" + i, LogLevel.RELEASE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    AILog.m4035i(HFMusicPlayService.TAG, "exception " + e, LogLevel.RELEASE);
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicPlayService_setup_volume");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void stop() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicPlayService.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFMusicPlayService.this.mBinder.stop();
                    AILog.m4035i(HFMusicPlayService.TAG, "stop ", LogLevel.RELEASE);
                } catch (RemoteException e) {
                    AILog.m4035i(HFMusicPlayService.TAG, "exception " + e, LogLevel.RELEASE);
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicPlayService_operation_music");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        } else {
            AILog.m4034i(TAG, "stop, but mBinder is null now!!!");
        }
        m4403d(pendingAction);
    }
}