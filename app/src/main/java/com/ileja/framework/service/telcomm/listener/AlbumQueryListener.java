package com.ileja.framework.service.telcomm.listener;

import com.ileja.aitelcomm.music.bean.AlbumInfo;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface AlbumQueryListener {
    void onError(int i, String str);

    void onResult(List<AlbumInfo> list);
}