package com.ileja.framework.service.telcomm;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aitelcomm.music.IAlbumQueryCallback;
import com.ileja.aitelcomm.music.IMusicQueryCallback;
import com.ileja.aitelcomm.music.IMusicQueryService;
import com.ileja.aitelcomm.music.bean.AlbumInfo;
import com.ileja.aitelcomm.music.bean.Channel;
import com.ileja.aitelcomm.music.bean.Keyword;
import com.ileja.aitelcomm.music.bean.MusicInfo;
import com.ileja.aitelcomm.music.source.ContentSource;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;
import com.ileja.framework.service.telcomm.listener.AlbumQueryListener;
import com.ileja.framework.service.telcomm.listener.ChannelMusicQueryListener;
import com.ileja.framework.service.telcomm.listener.MusicQueryListener;
import com.tencent.bugly.Bugly;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class HFMusicQueryService extends HFService {
    private static final String CLS_NAME = "com.ileja.aitelcomm.music.service.MusicQueryService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private static final String TAG = "HFMusicQueryService";
    private IMusicQueryService mBinder;
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.telcomm.HFMusicQueryService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "fm music service connected", LogLevel.RELEASE);
            HFMusicQueryService.this.mBinder = IMusicQueryService.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFMusicQueryService.this.mBinder = null;
            AILog.m4029d(HudSystem.TAG, "fm music service disconnected", LogLevel.RELEASE);
        }
    };

    private class AlbumQueryCallbackImpl extends IAlbumQueryCallback.Stub {

        /* JADX INFO: renamed from: a */
        AlbumQueryListener f6576a;

        public AlbumQueryCallbackImpl(HFMusicQueryService hFMusicQueryService, AlbumQueryListener albumQueryListener) {
            this.f6576a = albumQueryListener;
        }

        @Override // com.ileja.aitelcomm.music.IAlbumQueryCallback
        public void onError(int i, String str) {
            AILog.m4029d(HFMusicQueryService.TAG, "AlbumQueryCallbackImpl OnError errCode:" + i + ",errMsg:" + str, LogLevel.RELEASE);
            this.f6576a.onError(i, str);
        }

        @Override // com.ileja.aitelcomm.music.IAlbumQueryCallback
        public void onResult(List<AlbumInfo> list) {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("AlbumQueryCallbackImpl onResult :");
            if (list == null) {
                str = "albumInfos is null";
            } else {
                str = "albumInfos size:" + list.size();
            }
            sb.append(str);
            AILog.m4029d(HFMusicQueryService.TAG, sb.toString(), LogLevel.RELEASE);
            this.f6576a.onResult(list);
        }
    }

    private class ChannelMusicQueryCallbackImpl extends IMusicQueryCallback.Stub {

        /* JADX INFO: renamed from: a */
        ChannelMusicQueryListener f6577a;

        public ChannelMusicQueryCallbackImpl(HFMusicQueryService hFMusicQueryService, ChannelMusicQueryListener channelMusicQueryListener) {
            this.f6577a = channelMusicQueryListener;
        }

        @Override // com.ileja.aitelcomm.music.IMusicQueryCallback
        public void onError(int i, String str) {
            AILog.m4029d(HFMusicQueryService.TAG, "MusicQueryCallbackImpl OnError errCode:" + i + ",errMsg:" + str, LogLevel.RELEASE);
            this.f6577a.onError(i, str);
        }

        @Override // com.ileja.aitelcomm.music.IMusicQueryCallback
        public void onResult(List<MusicInfo> list) {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("MusicQueryCallbackImpl onResult :");
            if (list == null) {
                str = "musicInfos is null";
            } else {
                str = "musicInfos size:" + list.size();
            }
            sb.append(str);
            AILog.m4029d(HFMusicQueryService.TAG, sb.toString(), LogLevel.RELEASE);
            this.f6577a.onResult(list);
        }
    }

    private class MusicQueryCallbackImpl extends IMusicQueryCallback.Stub {

        /* JADX INFO: renamed from: a */
        MusicQueryListener f6578a;

        public MusicQueryCallbackImpl(HFMusicQueryService hFMusicQueryService, MusicQueryListener musicQueryListener) {
            this.f6578a = musicQueryListener;
        }

        @Override // com.ileja.aitelcomm.music.IMusicQueryCallback
        public void onError(int i, String str) {
            AILog.m4029d(HFMusicQueryService.TAG, "MusicQueryCallbackImpl OnError errCode:" + i + ",errMsg:" + str, LogLevel.RELEASE);
            this.f6578a.onError(i, str);
        }

        @Override // com.ileja.aitelcomm.music.IMusicQueryCallback
        public void onResult(List<MusicInfo> list) {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("MusicQueryCallbackImpl onResult:");
            if (list == null) {
                str = "musicInfos is null";
            } else {
                str = "musicInfos size:" + list.size();
            }
            sb.append(str);
            AILog.m4029d(HFMusicQueryService.TAG, sb.toString(), LogLevel.RELEASE);
            this.f6578a.onResult(list);
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4035i(TAG, "oncreate service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    public void deleteMusic(final MusicInfo musicInfo) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicQueryService.6
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFMusicQueryService.TAG, "delete:" + musicInfo);
                try {
                    HFMusicQueryService.this.mBinder.deleteMusic(musicInfo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicQueryServicedeleteMusic");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        AILog.m4035i(TAG, "destroy service", LogLevel.RELEASE);
        if (this.mBinder != null) {
            m4406g(application);
        }
    }

    public void favoriteMusic(final Channel channel, final ContentSource contentSource, final MusicInfo musicInfo) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicQueryService.7
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFMusicQueryService.TAG, "set Music fav:" + musicInfo);
                try {
                    HFMusicQueryService.this.mBinder.favorateMusic(channel, contentSource.ordinal(), musicInfo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicQueryServicefavorateMusic");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFMusicQueryService;
    }

    public void queryAlbum(final Keyword keyword, final ContentSource contentSource, final AlbumQueryListener albumQueryListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicQueryService.4
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFMusicQueryService.TAG, "queryMusic:");
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("mbinder is null ?");
                    sb.append(HFMusicQueryService.this.mBinder == null ? "true" : Bugly.SDK_IS_DEV);
                    AILog.m4034i(HFMusicQueryService.TAG, sb.toString());
                    HFMusicQueryService.this.mBinder.queryAlbum(keyword, contentSource.ordinal(), new AlbumQueryCallbackImpl(HFMusicQueryService.this, albumQueryListener));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicQueryServicequeryAlbum");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void queryAlbumContent(final AlbumInfo albumInfo, final MusicQueryListener musicQueryListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicQueryService.5
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFMusicQueryService.TAG, "queryAlbumContent:");
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("mbinder is null ?");
                    sb.append(HFMusicQueryService.this.mBinder == null ? "true" : Bugly.SDK_IS_DEV);
                    AILog.m4034i(HFMusicQueryService.TAG, sb.toString());
                    HFMusicQueryService.this.mBinder.queryAlbumContent(albumInfo, new MusicQueryCallbackImpl(HFMusicQueryService.this, musicQueryListener));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicQueryServicequeryAlbumContent");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void queryByChannel(final Channel channel, final ContentSource contentSource, final ChannelMusicQueryListener channelMusicQueryListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicQueryService.2
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4035i(HFMusicQueryService.TAG, "queryByChannel:" + channel + " ,source:" + contentSource, LogLevel.RELEASE);
                try {
                    HFMusicQueryService.this.mBinder.queryChannel(channel, contentSource.ordinal(), new ChannelMusicQueryCallbackImpl(HFMusicQueryService.this, channelMusicQueryListener));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicQueryService_query_last_channel");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public void queryMusic(final Keyword keyword, final MusicQueryListener musicQueryListener) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFMusicQueryService.3
            @Override // java.lang.Runnable
            public void run() {
                AILog.m4034i(HFMusicQueryService.TAG, "queryMusic:");
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("mbinder is null ?");
                    sb.append(HFMusicQueryService.this.mBinder == null ? "true" : Bugly.SDK_IS_DEV);
                    AILog.m4034i(HFMusicQueryService.TAG, sb.toString());
                    HFMusicQueryService.this.mBinder.queryMusic(keyword, new MusicQueryCallbackImpl(HFMusicQueryService.this, musicQueryListener));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.music.service.MusicQueryServicequeryMusic");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.mBinder != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }
}