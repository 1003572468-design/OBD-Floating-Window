package com.ileja.aibase.component;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public enum CommandModel {
    HOMEPAGE("/system/app/open", new String[]{"hui shou ye", "hui zhu ye"}, new String[]{"0.12", "0.12"}, "launcher"),
    NAVI_OVERVIEW("/navigation/overview", new String[]{"quan lan tu"}, new String[]{"0.07"}, new String[0]),
    NAVI_MAPPLUS("/navigation/mapplus", new String[]{"fang da di tu"}, new String[]{"0.27"}, new String[0]),
    NAVI_MAPMINUS("/navigation/mapminus", new String[]{"suo xiao di tu"}, new String[]{"0.21"}, new String[0]),
    NAVI_CMD_LEFTTIME("/navigation/cmd", new String[]{"hai yao duo jiu"}, new String[]{"0.15"}, "剩余时间"),
    TRAFFIC_MAPPLUS("/traffic/mapplus", new String[]{"fang da di tu"}, new String[]{"0.27"}, new String[0]),
    TRAFFIC_MAPMINUS("/traffic/mapminus", new String[]{"suo xiao di tu"}, new String[]{"0.21"}, new String[0]),
    MUSIC_PRE("/fm/premusic", new String[]{"shang yi shou"}, new String[]{"0.37"}, new String[0]),
    MUSIC_NEXT("/fm/nextmusic", new String[]{"xia yi shou"}, new String[]{"0.34"}, new String[0]),
    MUSIC_PAUSE("/fm/pause", new String[]{"zan ting bo fang"}, new String[]{"0.21"}, new String[0]),
    MUSIC_RESUM("/fm/resume", new String[]{"ji xv bo fang"}, new String[]{"0.33"}, new String[0]);

    public final String action;
    public final String[] args;
    public final String[] pinyins;
    public final String[] thresholds;

    CommandModel(String str, String[] strArr, String[] strArr2, String... strArr3) {
        this.action = str;
        this.pinyins = strArr;
        this.thresholds = strArr2;
        this.args = strArr3;
    }

    public static JSONArray formatCommandWord(CommandModel commandModel, JSONArray jSONArray) {
        if (jSONArray == null) {
            jSONArray = new JSONArray();
        }
        if (commandModel.pinyins.length != commandModel.thresholds.length) {
            throw new IllegalArgumentException("Error: pinyin and threshold are different length !");
        }
        for (int i = 0; i < commandModel.pinyins.length; i++) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("pinyin", commandModel.pinyins[i]);
                jSONObject.put("threshold", commandModel.thresholds[i]);
                jSONArray.put(jSONObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONArray;
    }

    public static String formatCommandWords(CommandModel[] commandModelArr) {
        JSONArray jSONArray = new JSONArray();
        for (CommandModel commandModel : commandModelArr) {
            formatCommandWord(commandModel, jSONArray);
        }
        return jSONArray.toString();
    }

    public static CommandModel[] getCommonWords() {
        return new CommandModel[]{HOMEPAGE};
    }

    public static CommandModel[] getMusicWords() {
        return new CommandModel[]{MUSIC_PRE, MUSIC_NEXT, MUSIC_PAUSE, MUSIC_RESUM};
    }

    public static CommandModel[] getNavWords() {
        return new CommandModel[]{NAVI_MAPMINUS, NAVI_MAPPLUS, NAVI_CMD_LEFTTIME, NAVI_OVERVIEW};
    }

    public static CommandModel[] getTrafficWords() {
        return new CommandModel[]{TRAFFIC_MAPMINUS, TRAFFIC_MAPPLUS};
    }

    public static CommandModel getUrl(String str) {
        for (CommandModel commandModel : values()) {
            if (commandModel != null && commandModel.hasPinyin(str)) {
                return commandModel;
            }
        }
        return null;
    }

    public static CommandModel getUrlForNavi(String str) {
        for (CommandModel commandModel : getNavWords()) {
            if (commandModel != null && commandModel.hasPinyin(str)) {
                return commandModel;
            }
        }
        return null;
    }

    public static CommandModel getUrlForTraffic(String str) {
        for (CommandModel commandModel : getTrafficWords()) {
            if (commandModel != null && commandModel.hasPinyin(str)) {
                return commandModel;
            }
        }
        return null;
    }

    public boolean hasPinyin(String str) {
        int i = 0;
        while (true) {
            String[] strArr = this.pinyins;
            if (i >= strArr.length) {
                return false;
            }
            if (strArr[i].equals(str)) {
                return true;
            }
            i++;
        }
    }
}