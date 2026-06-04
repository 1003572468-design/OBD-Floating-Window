package com.ileja.framework.service.telcomm.listener;

import com.ileja.aitelcomm.music.bean.MusicInfo;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ChannelMusicQueryListener {
    void onError(int i, String str);

    void onResult(List<MusicInfo> list);
}