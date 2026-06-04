package com.ileja.framework.service.p008fm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.ileja.aibase.common.AILog;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;

/* JADX INFO: loaded from: classes.dex */
public class HFMusicOperationService extends HFService {
    public static final String ACTION_DELETE = "com.ileja.carrobot.musicaction.delete";
    public static final String ACTION_EXIT = "com.ileja.carrobot.musicaction.exit";
    public static final String ACTION_FAV = "com.ileja.carrobot.musicaction.fav";
    public static final String ACTION_FMTXSTOP = "com.ileja.carrobot.musicaction.txstop";
    public static final String ACTION_FMTXSTRAT = "com.ileja.carrobot.musicaction.txstart";
    public static final String ACTION_NEXTCHANNEL = "com.ileja.carrobot.musicaction.nextchannel";
    public static final String ACTION_NEXTSONG = "com.ileja.carrobot.musicaction.nextsong";
    public static final String ACTION_PAUSE = "com.ileja.carrobot.musicaction.pause";
    public static final String ACTION_PLAY = "com.ileja.carrobot.musicaction.play";
    public static final String ACTION_PLAYMUSIC = "com.ileja.carrobot.musicaction.playmusic";
    public static final String ACTION_PRESONG = "com.ileja.carrobot.musicaction.presong";
    public static final String ACTION_RESUME = "com.ileja.carrobot.musicaction.resume";
    public static final String ACTION_SETCHANNEL = "com.ileja.carrobot.musicaction.setchannel";
    private static final String CLS_NAME = "com.ileja.carrobot.music.MusicService";
    private static final String PKG_NAME = "com.ileja.carrobot";

    public static void sendOrder(Context context, String str) {
        Intent intent = new Intent();
        intent.setClassName("com.ileja.carrobot", CLS_NAME);
        intent.setAction(str);
        context.startService(intent);
        AILog.m4028d(HudSystem.TAG, "sendOrder, action:" + str);
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return null;
    }
}