package com.ileja.core.p007sp;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import com.autonavi.base.amap.mapcore.tools.GLMapStaticValue;
import com.ileja.C1070R;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.phone.DeviceUtil;
import com.ileja.aicar.obd.OBDDataType;
import com.ileja.ailbs.bean.PoiInfo;
import com.ileja.aitelcomm.music.mode.PlayMode;
import com.ileja.aitelcomm.music.source.ChannelSource;
import com.ileja.core.export.CommDataProviderConstant;
import com.ileja.core.p007sp.IMPSharedPreference;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONException;

/* JADX INFO: loaded from: classes.dex */
public class BasePrefProvider {
    public static final String AIOS_MIC_DEBUG = "mic_debug_v2";
    public static final int DEFAULT_EMULATOR_SPEED = 120;
    public static final long DEFAULT_TIME = 4081507200000L;
    public static final String KAOLA_CHANNEL = "考拉电台";
    public static final String KEY_A2DP_CONNECT_SETTING = "a2dp_connect_setting";
    public static final String KEY_ADDITIONPKG_END_TIME = "additionpkgendtime";
    public static final String KEY_ADJUST_SPEED = "setAdjustSpeed";
    private static final String KEY_AUTO_STOP = "set_auto_stop";
    private static final String KEY_AUTO_UPGRADE = "autoUpgradeState";
    private static final String KEY_BRIGHT_DAY = "brightlevelofday";
    private static final String KEY_BRIGHT_NIGHT = "brightlevelofnight";
    public static final String KEY_BT_AUDIO_ISCONNECTED = "bt_audio_isconnected";
    private static final String KEY_BT_STATE = "phone_bt_state";
    public static final String KEY_CAR_DISPLACEMENT = "car_displacement";
    public static final String KEY_CAR_DISPLACEMENT_UNIT = "car_displacement_unit";
    public static final String KEY_CAR_NUMBER = "car_number";
    public static final String KEY_CAR_POWERTYPE = "powerType";
    public static final String KEY_CAR_VIN_CODE = "car_vin_code";
    public static final String KEY_CHANGE_LAUNCHER_THEME = "change_launcher_theme";
    private static final String KEY_CHAT_OPEN = "chat_open";
    private static final String KEY_CONTROLLERWAKEUP = "controllerwakeup";
    private static final String KEY_CUSTOM_CMD = "customCmd";
    private static final String KEY_DISTRACTION = "set_distraction";
    public static final String KEY_EDOG_STATE = "eDogStateOn";
    private static final String KEY_EMULATOR_SPEED = "EMULATOR_SPEED";
    private static final String KEY_FATIGUE = "set_fatigue";
    public static final String KEY_FIRST_GRAMMAR_CONTACT_NAME = "firstContactName";
    public static final String KEY_FIRST_MUSIC_NAME = "firstMusicName";
    public static final String KEY_FMTX_FREQ = "fmtxFreq";
    public static final String KEY_FMTX_FREQ_SEARCH = "fmtxFreqSearch";
    private static final String KEY_FMTX_OPEN_CNT = "fmtx_open_cnt";
    private static final String KEY_FMTX_SETFREQ_CNT = "fmtx_set_freq_cnt";
    private static final String KEY_FOTA_UPGRADE_ERROR = "fota_upgrade_error";
    private static final String KEY_GESTURE_FLOAT_SWIPE = "floatGestureSwipe";
    private static final String KEY_GESTURE_FLOAT_THUMB = "floatGestureThumb";
    private static final String KEY_GESTURE_STATE = "gestureState";
    private static final String KEY_GESTURE_SWIPE = "gestureSwipe";
    private static final String KEY_GESTURE_THUMB = "gestureThumb";
    private static final String KEY_HOTPOT_CODE = "WifiHotPotCode";
    private static final String KEY_HOTPOT_ENABLE = "WifiHotPotEnable";
    private static final String KEY_HOTPOT_NAME = "WifiHotPotName";
    public static final String KEY_INPUTDEV_STATE = "inputDevState";
    public static final String KEY_INSTRUCT = "instruct";
    public static final String KEY_ISFMTX_ON = "is_fmtx_on";
    private static final String KEY_IS_IMPORT_FROMP_ROVIDER = "is_import_fro_provider";
    public static final String KEY_IS_TTSFINISH_DELAY_SUPPORT = "is_ttsfinish_delay_support";
    public static final String KEY_IS_TTSFINISH_DELAY_TIMES = "is_ttsfinish_delay_times";
    private static final String KEY_LATEST_LOCATION = "lastLocation";
    public static final String KEY_LIMIT = "set_limit";
    private static final String KEY_MAC_ADDR = "hudMac";
    public static final String KEY_MAIN_WAKEUP_WORD = "main_wakeup_word";
    public static final String KEY_MAIN_WAKEUP_WORD_THRESH = "main_wakeup_word_thresh";
    public static final String KEY_MAP_SOURCE = "map_source";
    public static final String KEY_MAP_SOURCE_AUTONAVI = "autonavi_map";
    public static final String KEY_MAP_SOURCE_BAIDU = "baidu_map";
    public static final String KEY_MIOT_BIND_COUNT = "miotboundcount";
    public static final String KEY_MUSIC_PLAYMODE = "music_playmode";
    private static final String KEY_MUSIC_SRC = "musicsource";
    public static final String KEY_NAVI_GOCOMPANY = "company";
    public static final String KEY_NAVI_GOHOME = "home";
    private static final String KEY_NAVI_HAS_USE_STATEGY_AVOID_CONGESTION = "hasAvoidCongestion";
    public static final String KEY_NAVI_MAPMODE = "navi_mapmode";
    public static final String KEY_NAVI_VOICE = "navi_voice";
    private static final String KEY_OBD_HBT = "obd_hbt";
    public static final String KEY_OBD_ID = "obd_id";
    public static final String KEY_OBD_SCREEN_OFF_TIME = "obd_screen_off_time";
    private static final String KEY_OBD_TIMESTAMP = "obd_time";
    private static final String KEY_OBD_TT = "obd_tt";
    public static final String KEY_OIL_METHOD = "car_oil_method";
    public static final String KEY_POSITION_1_OBD_TYPE = "position_1_obd_type";
    public static final String KEY_POSITION_2_OBD_TYPE = "position_2_obd_type";
    public static final String KEY_PUSH_TYPE_ORIGIN = "app_use_push_type";
    public static final String KEY_RESET_SYS_TIME = "reset_sys_time";
    public static final String KEY_SET_HASHCODE = "voiceSourceNum";
    public static final String KEY_SET_PRIVACY = "setPrivacy";
    private static final String KEY_SIM_SERIAL_NUM = "simSerialNum";
    public static final String KEY_TRAFFIC_STATE = "trafficState";
    private static final String KEY_VOICEWAKEUP = "voicewakeup";
    public static final String KEY_VOICE_SOURCE = "voiceSource";
    private static final String KEY_WEATHER_SHOW_COUNT = "weatherShowCount";
    private static final String KEY_WECHAT_SEND_AUDIO = "wechat_send_audio";
    private static final String KEY_WIFICONNECTION_STATE = "wifi_connection_state";
    public static final String KEY_WX_SUFFIX_SETTING = "wx_suffix_setting";
    private static final String PREF_KEY_CONTACTS_SYNC_RESULT = "contacts_sync_result";
    public static final String PREF_KEY_PRESSURE_FL = "pre_fl";
    public static final String PREF_KEY_PRESSURE_FR = "pre_fr";
    public static final String PREF_KEY_PRESSURE_RL = "pre_rl";
    public static final String PREF_KEY_PRESSURE_RR = "pre_rr";
    public static final String PREF_KEY_TEMPERATURE_FL = "tem_fl";
    public static final String PREF_KEY_TEMPERATURE_FR = "tem_fr";
    public static final String PREF_KEY_TEMPERATURE_RL = "tem_rl";
    public static final String PREF_KEY_TEMPERATURE_RR = "tem_rr";
    public static final String PREF_KEY_TIRE_BIND = "tire_bind";
    public static final String PREF_KEY_TIRE_NAMES = "tire_names";
    private static final String PREF_KEY_VOICE_READY = "voice_ready";
    private static final String TAG = "BasePrefProvider";
    public static final String WAKEUP_WORD = "你好萝卜";
    public static final float WAKEUP_WORD_THRESH = 0.36f;
    public static final String XMLY_MUSICCHANNEL = "喜马拉雅电台";

    /* JADX INFO: renamed from: a */
    protected static MMKVSharedPreferences f6330a;

    /* JADX INFO: renamed from: a */
    protected static void m4395a(Context context, String str) {
        m4396b(context).remove(str + "_new");
    }

    public static void addFmTxFreqCnt(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        iMPSharedPreferenceM4396b.putInt(KEY_FMTX_SETFREQ_CNT, iMPSharedPreferenceM4396b.getInt(KEY_FMTX_SETFREQ_CNT, 0) + 1);
    }

    public static void addFmTxOpenCnt(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        iMPSharedPreferenceM4396b.putInt(KEY_FMTX_OPEN_CNT, iMPSharedPreferenceM4396b.getInt(KEY_FMTX_OPEN_CNT, 0) + 1);
    }

    public static void addMiotBindCount(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        iMPSharedPreferenceM4396b.putInt(KEY_MIOT_BIND_COUNT, iMPSharedPreferenceM4396b.getInt(KEY_MIOT_BIND_COUNT, 0) + 1);
    }

    public static void addWeatherShowCount(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        iMPSharedPreferenceM4396b.putInt(KEY_WEATHER_SHOW_COUNT, iMPSharedPreferenceM4396b.getInt(KEY_WEATHER_SHOW_COUNT, 0) + 1);
    }

    /* JADX INFO: renamed from: b */
    protected static IMPSharedPreference m4396b(Context context) {
        if (f6330a == null) {
            f6330a = new MMKVSharedPreferences(context, null, "commonData", 2);
        }
        return f6330a;
    }

    /* JADX INFO: renamed from: c */
    protected static PoiInfo m4397c(Context context, String str) {
        String string = m4396b(context).getString(str + "_new", null);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        PoiInfo poiInfo = new PoiInfo();
        try {
            poiInfo.fromJson2(string);
            return poiInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String changeWifiHotpotCode(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        Random random = new Random(System.currentTimeMillis());
        String str = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        iMPSharedPreferenceM4396b.putString(KEY_HOTPOT_CODE, str);
        return str;
    }

    private static int clearAll(Context context) {
        try {
            return context.getContentResolver().delete(CommDataProviderConstant.CommDataEntry.CONTENT_URI, null, null);
        } catch (Exception e) {
            AILog.m4030e(TAG, "clearAll error: " + e);
            return 0;
        }
    }

    public static void clearLatestFixLocation(Context context) {
        m4395a(context, KEY_LATEST_LOCATION);
    }

    public static void clearNaviCompanySite(Context context) {
        m4395a(context, KEY_NAVI_GOCOMPANY);
    }

    public static void clearNaviHomeSite(Context context) {
        m4395a(context, KEY_NAVI_GOHOME);
    }

    public static void clearTireData(Context context) {
        setTemperatureFl(context, 0);
        setTemperatureFR(context, 0);
        setTemperatureRl(context, 0);
        setTemperatureRR(context, 0);
        setPressureFl(context, 0.0d);
        setPressureFR(context, 0.0d);
        setPressureRl(context, 0.0d);
        setPressureRR(context, 0.0d);
    }

    /* JADX INFO: renamed from: d */
    protected static void m4398d(Context context, String str, PoiInfo poiInfo, boolean z) {
        try {
            m4396b(context).putString(str + "_new", poiInfo.toJson().toString(), z ? IMPSharedPreference.NotifyType.MULTI_PROCESS : IMPSharedPreference.NotifyType.NONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean getA2dpConnectSetting(Context context) {
        return m4396b(context).getBoolean(KEY_A2DP_CONNECT_SETTING, false);
    }

    public static long getAdditionPkgEndTime(Context context) {
        return m4396b(context).getLong(KEY_ADDITIONPKG_END_TIME, DEFAULT_TIME);
    }

    public static int getAdjustSpeed(Context context) {
        return m4396b(context).getInt("setAdjustSpeed", 0);
    }

    public static boolean getAliasStatus(Context context) {
        return m4396b(context).getBoolean("AliasStatus", false);
    }

    public static int getAutoStopTime(Context context) {
        int i = m4396b(context).getInt(KEY_AUTO_STOP, 90);
        if (i < 30) {
            return 30;
        }
        return i;
    }

    public static boolean getAutoUpgradeState(Context context) {
        return m4396b(context).getBoolean(KEY_AUTO_UPGRADE, true);
    }

    public static String getBTSate(Context context) {
        return m4396b(context).getString(KEY_BT_STATE, "disconnected");
    }

    public static int getBrightScale(Context context, boolean z) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (z) {
            int i = iMPSharedPreferenceM4396b.getInt(KEY_BRIGHT_DAY, 4);
            AILog.m4029d(TAG, "day level : " + i, LogLevel.RELEASE);
            return i;
        }
        int i2 = iMPSharedPreferenceM4396b.getInt(KEY_BRIGHT_NIGHT, 2);
        AILog.m4029d(TAG, "night level : " + i2, LogLevel.RELEASE);
        return i2;
    }

    public static String getCarDisplacement(Context context) {
        return m4396b(context).getString(KEY_CAR_DISPLACEMENT, "1.6");
    }

    public static String getCarDisplacementUnit(Context context) {
        return m4396b(context).getString(KEY_CAR_DISPLACEMENT_UNIT, "L");
    }

    public static String getCarNumber(Context context) {
        return m4396b(context).getString(KEY_CAR_NUMBER, "");
    }

    public static int getCarPowertype(Context context) {
        return m4396b(context).getInt("powerType", -1);
    }

    public static String getCarVinCode(Context context) {
        return m4396b(context).getString(KEY_CAR_VIN_CODE, "");
    }

    public static boolean getChatOpenState(Context context) {
        return m4396b(context).getBoolean(KEY_CHAT_OPEN, true);
    }

    public static String getContactsSyncState(Context context) {
        String string = m4396b(context).getString(PREF_KEY_CONTACTS_SYNC_RESULT, null);
        AILog.m4028d(TAG, "getContactsSyncState:" + string);
        return string;
    }

    @Deprecated
    public static boolean getControllerWakeupFlag(Context context) {
        return true;
    }

    public static String getCurrentChannel(Context context) {
        String string = m4396b(context).getString(DTransferConstants.CHANNEL, ChannelSource.FM_MUSIC.getSource());
        AILog.m4028d(TAG, "get curr channel :" + string);
        return string;
    }

    public static String getCustomCmd(Context context) {
        return m4396b(context).getString(KEY_CUSTOM_CMD, null);
    }

    public static String getDisplayGrammarContactsName(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            return iMPSharedPreferenceM4396b.getString(KEY_FIRST_GRAMMAR_CONTACT_NAME, context.getString(C1070R.string.default_contact_name));
        }
        return null;
    }

    public static int getEmulatorSpeed(Context context) {
        return m4396b(context).getInt(KEY_EMULATOR_SPEED, 120);
    }

    public static int getFMTXFreq(Context context) {
        int i = m4396b(context).getInt("fmtxFreq", 0);
        return i == 0 ? getFMTXFreqSearch(context) : i;
    }

    public static int getFMTXFreqSearch(Context context) {
        return m4396b(context).getInt(KEY_FMTX_FREQ_SEARCH, 0);
    }

    public static int getFMTXFreqUser(Context context) {
        return m4396b(context).getInt("fmtxFreq", 0);
    }

    public static String getFirstMusicName(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            return iMPSharedPreferenceM4396b.getString(KEY_FIRST_MUSIC_NAME, context.getString(C1070R.string.default_music_name));
        }
        return null;
    }

    public static boolean getFloatGestureSwipeEnable(Context context) {
        return m4396b(context).getBoolean(KEY_GESTURE_FLOAT_SWIPE, false);
    }

    public static boolean getFloatGestureThumbEnable(Context context) {
        return m4396b(context).getBoolean(KEY_GESTURE_FLOAT_THUMB, false);
    }

    public static int getFmTxFreqCnt(Context context) {
        return m4396b(context).getInt(KEY_FMTX_SETFREQ_CNT, 0);
    }

    public static int getFmTxOpenCnt(Context context) {
        return m4396b(context).getInt(KEY_FMTX_OPEN_CNT, 0);
    }

    public static int getFotaUpgradeErrorTimes(Context context) {
        return m4396b(context).getInt(KEY_FOTA_UPGRADE_ERROR, 0);
    }

    public static boolean getGestrueState(Context context) {
        return m4396b(context).getBoolean(KEY_GESTURE_STATE, false);
    }

    public static boolean getGestureSwipeEnable(Context context) {
        return m4396b(context).getBoolean(KEY_GESTURE_SWIPE, false);
    }

    public static boolean getGestureThumbEnable(Context context) {
        return m4396b(context).getBoolean(KEY_GESTURE_THUMB, false);
    }

    private static String getHotpotCode() {
        Random random = new Random(System.currentTimeMillis());
        return "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
    }

    private static String getHotpotName() {
        String str = Build.SERIAL;
        if (str != null && str.length() > 4) {
            String str2 = Build.SERIAL;
            return str2.substring(str2.length() - 4, Build.SERIAL.length());
        }
        Random random = new Random(System.currentTimeMillis());
        return "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
    }

    public static boolean getInputDevConnected(Context context) {
        return m4396b(context).getBoolean(KEY_INPUTDEV_STATE, false);
    }

    public static boolean getInstruct(Context context) {
        return m4396b(context).getBoolean(KEY_INSTRUCT, false);
    }

    public static PoiInfo getLatestFixLocation(Context context) {
        return m4397c(context, KEY_LATEST_LOCATION);
    }

    public static int getLauncherTheme(Context context) {
        return m4396b(context).getInt(KEY_CHANGE_LAUNCHER_THEME, 0);
    }

    public static boolean getLimitAble(Context context) {
        return m4396b(context).getBoolean(KEY_LIMIT, false);
    }

    public static String getMacAddr(Context context) {
        return m4396b(context).getString(KEY_MAC_ADDR, null);
    }

    public static String getMainWakeupWord(Context context) {
        return m4396b(context).getString(KEY_MAIN_WAKEUP_WORD, "你好萝卜");
    }

    public static float getMainWakeupWordThresh(Context context) {
        return m4396b(context).getFloat(KEY_MAIN_WAKEUP_WORD_THRESH, 0.36f);
    }

    public static String getMapSource(Context context) {
        m4396b(context);
        return KEY_MAP_SOURCE_AUTONAVI;
    }

    public static String getMusicSrc(Context context) {
        return m4396b(context).getString(KEY_MUSIC_SRC, XMLY_MUSICCHANNEL);
    }

    public static PoiInfo getNaviCompanySite(Context context) {
        return m4397c(context, KEY_NAVI_GOCOMPANY);
    }

    public static PoiInfo getNaviHomeSite(Context context) {
        return m4397c(context, KEY_NAVI_GOHOME);
    }

    public static boolean getNaviVoiceState(Context context) {
        return m4396b(context).getBoolean(KEY_NAVI_VOICE, true);
    }

    public static String getOBDId(Context context) {
        return m4396b(context).getString(KEY_OBD_ID, "");
    }

    public static int getOBDScreenOffTime(Context context) {
        int i = m4396b(context).getInt(KEY_OBD_SCREEN_OFF_TIME, 90);
        if (i < 30) {
            return 30;
        }
        return i;
    }

    public static String getObdHBT(Context context) {
        return m4396b(context).getString(KEY_OBD_HBT, "");
    }

    public static String getObdTT(Context context) {
        return m4396b(context).getString(KEY_OBD_TT, "");
    }

    public static long getObdTimestamp(Context context) {
        return m4396b(context).getLong(KEY_OBD_TIMESTAMP, 0L);
    }

    public static int getOilMethod(Context context) {
        return m4396b(context).getInt(KEY_OIL_METHOD, 0);
    }

    public static PlayMode getPlayMode(Context context) {
        return PlayMode.getPlayModeByName(m4396b(context).getString(KEY_MUSIC_PLAYMODE, PlayMode.ORDER.getMode()));
    }

    public static String getPosition1ObdType(Context context) {
        return m4396b(context).getString(KEY_POSITION_1_OBD_TYPE, OBDDataType.AVERAGEFUELCONSUME.toString());
    }

    public static String getPosition2ObdType(Context context) {
        return m4396b(context).getString(KEY_POSITION_2_OBD_TYPE, OBDDataType.ROTATESPEED.toString());
    }

    public static double getPressureFR(Context context) {
        return m4396b(context).getDouble(PREF_KEY_PRESSURE_FR, 0.0d);
    }

    public static double getPressureFl(Context context) {
        return m4396b(context).getDouble(PREF_KEY_PRESSURE_FL, 0.0d);
    }

    public static double getPressureRR(Context context) {
        return m4396b(context).getDouble(PREF_KEY_PRESSURE_RR, 0.0d);
    }

    public static double getPressureRl(Context context) {
        return m4396b(context).getDouble(PREF_KEY_PRESSURE_RL, 0.0d);
    }

    public static String getPrivacyState(Context context) {
        return m4396b(context).getString("setPrivacy", null);
    }

    public static String getPushTypeOrigin(Context context) {
        return m4396b(context).getString(KEY_PUSH_TYPE_ORIGIN, "all");
    }

    public static int getResetSysTime(Context context) {
        return m4396b(context).getInt(KEY_RESET_SYS_TIME, 0);
    }

    public static boolean getRoadState(Context context) {
        return m4396b(context).getBoolean(KEY_TRAFFIC_STATE, false);
    }

    public static int getTTSFinishDelayTimes(Context context) {
        return m4396b(context).getInt(KEY_IS_TTSFINISH_DELAY_TIMES, GLMapStaticValue.ANIMATION_MOVE_TIME);
    }

    public static int getTemperatureFR(Context context) {
        return m4396b(context).getInt(PREF_KEY_TEMPERATURE_FR, 0);
    }

    public static int getTemperatureFl(Context context) {
        return m4396b(context).getInt(PREF_KEY_TEMPERATURE_FL, 0);
    }

    public static int getTemperatureRR(Context context) {
        return m4396b(context).getInt(PREF_KEY_TEMPERATURE_RR, 0);
    }

    public static int getTemperatureRl(Context context) {
        return m4396b(context).getInt(PREF_KEY_TEMPERATURE_RL, 0);
    }

    public static String getTireDevicesNames(Context context) {
        return m4396b(context).getString(PREF_KEY_TIRE_NAMES, "none");
    }

    public static boolean getVoiceReady(Context context) {
        return m4396b(context).getBoolean(PREF_KEY_VOICE_READY, false);
    }

    public static String getVoiceSource(Context context) {
        return m4396b(context).getString(KEY_VOICE_SOURCE, "女声");
    }

    public static boolean getWatchDogState(Context context) {
        return m4396b(context).getBoolean(KEY_EDOG_STATE, true);
    }

    public static boolean getWifiConnectionState(Context context) {
        return m4396b(context).getBoolean(KEY_WIFICONNECTION_STATE, false);
    }

    public static String getWifiHotpotCode(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        String string = iMPSharedPreferenceM4396b.getString(KEY_HOTPOT_CODE, "NOTSET");
        AILog.m4028d(TAG, "hotpot code : " + string);
        if (!"NOTSET".equals(string)) {
            return string;
        }
        String hotpotCode = getHotpotCode();
        iMPSharedPreferenceM4396b.putString(KEY_HOTPOT_CODE, hotpotCode);
        AILog.m4029d(TAG, "hotpot code not set ,new code :" + hotpotCode, LogLevel.RELEASE);
        return hotpotCode;
    }

    public static String getWifiHotpotName(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        String string = iMPSharedPreferenceM4396b.getString(KEY_HOTPOT_NAME, "NOTSET");
        if (!"NOTSET".equals(string)) {
            return string;
        }
        String str = "Carrobot-" + getHotpotName();
        iMPSharedPreferenceM4396b.putString(KEY_HOTPOT_NAME, str);
        return str;
    }

    public static boolean getWxSuffixSetting(Context context) {
        return m4396b(context).getBoolean(KEY_WX_SUFFIX_SETTING, true);
    }

    public static boolean hasBindTireDevices(Context context) {
        return m4396b(context).getBoolean(PREF_KEY_TIRE_BIND, false);
    }

    public static String hasMicTest(Context context) {
        return m4396b(context).getString(AIOS_MIC_DEBUG, null);
    }

    public static boolean hasStategyAvoidCongestion(Context context) {
        return m4396b(context).getBoolean(KEY_NAVI_HAS_USE_STATEGY_AVOID_CONGESTION, false);
    }

    public static boolean isBtAudioConnected(Context context) {
        return m4396b(context).getBoolean(KEY_BT_AUDIO_ISCONNECTED, false);
    }

    public static boolean isDistractionOn(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            return iMPSharedPreferenceM4396b.getBoolean(KEY_DISTRACTION, false);
        }
        return false;
    }

    public static boolean isFatigueOn(Context context) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            return iMPSharedPreferenceM4396b.getBoolean(KEY_FATIGUE, false);
        }
        return false;
    }

    public static boolean isFmtxOpen(Context context) {
        return m4396b(context).getBoolean(KEY_ISFMTX_ON, false);
    }

    public static boolean isNaviMapMode(Context context) {
        return m4396b(context).getBoolean(KEY_NAVI_MAPMODE, true);
    }

    public static boolean isSameSettingHashcode(Context context, int i) {
        return m4396b(context).getInt(KEY_SET_HASHCODE, -1) == i;
    }

    public static boolean isTTSFinishDelaySupport(Context context) {
        return m4396b(context).getBoolean(KEY_IS_TTSFINISH_DELAY_SUPPORT, true);
    }

    public static boolean isWeChatSendAudioOpen(Context context) {
        return m4396b(context).getBoolean(KEY_WECHAT_SEND_AUDIO, false);
    }

    public static boolean isWeatherShown(Context context) {
        return m4396b(context).getInt(KEY_WEATHER_SHOW_COUNT, 0) > 0;
    }

    public static boolean isWifiHotpotEnable(Context context) {
        if (DeviceUtil.isSupportHotpot()) {
            return m4396b(context).getBoolean(KEY_HOTPOT_ENABLE, true);
        }
        return false;
    }

    public static void openWxSuffix(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_WX_SUFFIX_SETTING, z);
    }

    private static Map<String, Object> queryAll(Context context) {
        Object objValueOf;
        HashMap map = new HashMap();
        Cursor cursorQuery = context.getContentResolver().query(CommDataProviderConstant.CommDataEntry.CONTENT_URI, new String[]{"name", CommDataProviderConstant.CommDataEntry.COLUMN_NAME_VALUE}, null, null, null);
        if (cursorQuery != null && cursorQuery.getCount() > 0) {
            cursorQuery.moveToFirst();
            do {
                String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("name"));
                String string2 = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow(CommDataProviderConstant.CommDataEntry.COLUMN_NAME_VALUE));
                try {
                    if (KEY_INSTRUCT.equals(string) || KEY_EDOG_STATE.equals(string) || KEY_TRAFFIC_STATE.equals(string) || KEY_NAVI_MAPMODE.equals(string) || KEY_HOTPOT_ENABLE.equals(string) || KEY_CHAT_OPEN.equals(string) || KEY_WECHAT_SEND_AUDIO.equals(string) || KEY_GESTURE_STATE.equals(string) || KEY_INPUTDEV_STATE.equals(string) || KEY_AUTO_UPGRADE.equals(string) || KEY_NAVI_HAS_USE_STATEGY_AVOID_CONGESTION.equals(string) || KEY_GESTURE_SWIPE.equals(string) || KEY_GESTURE_THUMB.equals(string) || KEY_GESTURE_FLOAT_SWIPE.equals(string) || KEY_GESTURE_FLOAT_THUMB.equals(string) || KEY_CONTROLLERWAKEUP.equals(string) || KEY_FATIGUE.equals(string) || KEY_DISTRACTION.equals(string) || KEY_WIFICONNECTION_STATE.equals(string) || KEY_ISFMTX_ON.equals(string) || KEY_LIMIT.equals(string) || KEY_WX_SUFFIX_SETTING.equals(string) || KEY_A2DP_CONNECT_SETTING.equals(string)) {
                        objValueOf = Boolean.valueOf(string2);
                    } else if (KEY_BRIGHT_DAY.equals(string) || KEY_BRIGHT_NIGHT.equals(string) || "fmtxFreq".equals(string) || KEY_FMTX_FREQ_SEARCH.equals(string) || KEY_WEATHER_SHOW_COUNT.equals(string) || KEY_FOTA_UPGRADE_ERROR.equals(string) || KEY_AUTO_STOP.equals(string) || KEY_SET_HASHCODE.equals(string) || "setAdjustSpeed".equals(string) || KEY_FMTX_OPEN_CNT.equals(string) || KEY_CHANGE_LAUNCHER_THEME.equals(string)) {
                        objValueOf = Integer.valueOf(string2);
                    } else {
                        objValueOf = string2;
                        if (KEY_OBD_TIMESTAMP.equals(string)) {
                            objValueOf = Long.valueOf(string2);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    objValueOf = string2;
                } catch (RuntimeException e2) {
                    e2.printStackTrace();
                    objValueOf = string2;
                }
                map.put(string, objValueOf);
            } while (cursorQuery.moveToNext());
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        AILog.m4028d(TAG, "query all exec result:" + map);
        return map;
    }

    public static void registerOnSharedPreferenceChangeListener(Context context, IMPSharedPreference.LeJaOnSharedPreferenceChangeListener leJaOnSharedPreferenceChangeListener) {
        m4396b(context).registerOnSharedPreferenceChangeListener(leJaOnSharedPreferenceChangeListener);
    }

    public static void removeConstantsCache(Context context) {
        m4396b(context).remove(PREF_KEY_CONTACTS_SYNC_RESULT, IMPSharedPreference.NotifyType.PRIVATE_PROCESS);
    }

    public static void resetlWeatherShowCount(Context context) {
        m4396b(context).putInt(KEY_WEATHER_SHOW_COUNT, 0);
    }

    public static void saveLatestFixLocation(Context context, PoiInfo poiInfo) {
        m4398d(context, KEY_LATEST_LOCATION, poiInfo, false);
    }

    public static void saveRoadState(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_TRAFFIC_STATE, z);
    }

    public static void saveStategyAvoidCongestion(Context context) {
        m4396b(context).putBoolean(KEY_NAVI_HAS_USE_STATEGY_AVOID_CONGESTION, true);
    }

    public static void saveWatchDogState(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_EDOG_STATE, z);
    }

    public static void saveWifiConnectionState(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_WIFICONNECTION_STATE, z);
    }

    public static void setA2dpConnectOn(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_A2DP_CONNECT_SETTING, z);
    }

    public static void setAdditionPkgEndTime(Context context, long j) {
        m4396b(context).putLong(KEY_ADDITIONPKG_END_TIME, j, IMPSharedPreference.NotifyType.PRIVATE_PROCESS);
    }

    public static void setAdjustSpeed(Context context, int i) {
        m4396b(context).putInt("setAdjustSpeed", i);
    }

    public static void setAliasStatus(Context context, boolean z) {
        m4396b(context).putBoolean("AliasStatus", z);
    }

    public static void setAutoStopTime(Context context, int i) {
        m4396b(context).putInt(KEY_AUTO_STOP, i);
    }

    public static void setAutoUpgradeState(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_AUTO_UPGRADE, z);
    }

    public static void setBTSate(Context context, String str) {
        AILog.m4034i(TAG, "update bluetooth state:" + str);
        m4396b(context).putString(KEY_BT_STATE, str);
    }

    public static void setBrightScale(Context context, boolean z, int i) {
        String str;
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (z) {
            AILog.m4029d(TAG, "now is day . level is : " + i, LogLevel.RELEASE);
            str = KEY_BRIGHT_DAY;
        } else {
            AILog.m4029d(TAG, "now is night . level is : " + i, LogLevel.RELEASE);
            str = KEY_BRIGHT_NIGHT;
        }
        iMPSharedPreferenceM4396b.putInt(str, i);
    }

    public static void setBtAudioConnected(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_BT_AUDIO_ISCONNECTED, z);
    }

    public static void setCarDisplacement(Context context, String str) {
        m4396b(context).putString(KEY_CAR_DISPLACEMENT, str);
    }

    public static void setCarDisplacementUnit(Context context, String str) {
        m4396b(context).putString(KEY_CAR_DISPLACEMENT_UNIT, str);
    }

    public static void setCarNumber(Context context, String str) {
        m4396b(context).putString(KEY_CAR_NUMBER, str);
    }

    public static void setCarPowertype(Context context, int i) {
        m4396b(context).putInt("powerType", i);
    }

    public static void setCarVinCode(Context context, String str) {
        if (TextUtils.isEmpty(str) || TextUtils.equals(str, "-1") || TextUtils.equals(str, getCarVinCode(context))) {
            return;
        }
        m4396b(context).putString(KEY_CAR_VIN_CODE, str);
    }

    public static void setChatOpenState(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_CHAT_OPEN, z);
    }

    public static void setContactsSyncState(Context context, String str) {
        m4396b(context).putString(PREF_KEY_CONTACTS_SYNC_RESULT, str, IMPSharedPreference.NotifyType.PRIVATE_PROCESS);
        AILog.m4028d(TAG, "setContactsSyncState:" + str);
    }

    public static void setControllerWakeupFlag(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_CONTROLLERWAKEUP, z);
    }

    public static void setCurrentChannel(String str, Context context) {
        AILog.m4028d(TAG, "set curr channel :" + str);
        m4396b(context).putString(DTransferConstants.CHANNEL, str);
    }

    public static void setCustomCmd(Context context, String str) {
        m4396b(context).putString(KEY_CUSTOM_CMD, str);
    }

    public static void setDisplayGrammarContactsName(Context context, String str) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            iMPSharedPreferenceM4396b.putString(KEY_FIRST_GRAMMAR_CONTACT_NAME, str);
            AILog.m4028d(TAG, "setDisplayGrammarContactsName(), name:" + str);
        }
    }

    public static void setDistractionOn(Context context, boolean z) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            iMPSharedPreferenceM4396b.putBoolean(KEY_DISTRACTION, z);
        }
    }

    public static void setEmulatorSpeed(Context context, int i) {
        m4396b(context).putLong(KEY_EMULATOR_SPEED, i);
    }

    public static void setFMTXFreq(Context context, int i) {
        AILog.m4029d(TAG, "setFMTXFreq : " + i, LogLevel.RELEASE);
        m4396b(context).putInt("fmtxFreq", i);
    }

    public static void setFMTXFreqSearch(Context context, int i) {
        AILog.m4029d(TAG, "setFMTXFreqSearch : " + i, LogLevel.RELEASE);
        m4396b(context).putInt(KEY_FMTX_FREQ_SEARCH, i);
    }

    public static void setFatigueOn(Context context, boolean z) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            iMPSharedPreferenceM4396b.putBoolean(KEY_FATIGUE, z);
        }
    }

    public static void setFirstMusicName(Context context, String str) {
        IMPSharedPreference iMPSharedPreferenceM4396b = m4396b(context);
        if (iMPSharedPreferenceM4396b != null) {
            iMPSharedPreferenceM4396b.putString(KEY_FIRST_MUSIC_NAME, str);
        }
    }

    public static void setFloatGestureSwipeEnable(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_GESTURE_FLOAT_SWIPE, z);
    }

    public static void setFloatGestureThumbEnable(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_GESTURE_FLOAT_THUMB, z);
    }

    public static void setFmtxOpen(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_ISFMTX_ON, z);
    }

    public static void setFotaUpgradeErrorTime(Context context, int i) {
        m4396b(context).putInt(KEY_FOTA_UPGRADE_ERROR, i);
    }

    public static void setGestrueState(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_GESTURE_STATE, z);
    }

    public static void setGestureSwipeEnable(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_GESTURE_SWIPE, z);
    }

    public static void setGestureThumbEnable(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_GESTURE_THUMB, z);
    }

    public static void setInputDevConnected(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_INPUTDEV_STATE, z);
    }

    public static void setInstruct(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_INSTRUCT, z);
    }

    public static void setLauncherTheme(Context context, int i) {
        m4396b(context).putInt(KEY_CHANGE_LAUNCHER_THEME, i);
    }

    public static void setLimitAble(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_LIMIT, z);
    }

    public static void setMacAddr(Context context, String str) {
        m4396b(context).putString(KEY_MAC_ADDR, str);
    }

    public static void setMainWakeupWord(Context context, String str) {
        m4396b(context).putString(KEY_MAIN_WAKEUP_WORD, str);
    }

    public static void setMainWakeupWordThresh(Context context, float f) {
        m4396b(context).putFloat(KEY_MAIN_WAKEUP_WORD_THRESH, f);
    }

    public static void setMapSource(Context context, String str) {
        m4396b(context).putString(KEY_MAP_SOURCE, str);
    }

    public static void setMicTest(Context context, String str) {
        m4396b(context).putString(AIOS_MIC_DEBUG, str);
    }

    public static void setMusicSrc(Context context, String str) {
        m4396b(context).putString(KEY_MUSIC_SRC, str);
    }

    public static void setNaviCompanySite(Context context, PoiInfo poiInfo) {
        m4398d(context, KEY_NAVI_GOCOMPANY, new PoiInfo(poiInfo), true);
    }

    public static void setNaviHomeSite(Context context, PoiInfo poiInfo) {
        m4398d(context, KEY_NAVI_GOHOME, new PoiInfo(poiInfo), true);
    }

    public static void setNaviMapMode(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_NAVI_MAPMODE, z);
    }

    public static void setNaviVoiceState(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_NAVI_VOICE, z);
    }

    public static void setOBDId(Context context, String str) {
        m4396b(context).putString(KEY_OBD_ID, str);
    }

    public static void setOBDScreenOffTime(Context context, int i) {
        m4396b(context).putInt(KEY_OBD_SCREEN_OFF_TIME, i);
    }

    public static void setObdHBT(Context context, String str) {
        m4396b(context).putString(KEY_OBD_HBT, str);
    }

    public static void setObdTT(Context context, String str) {
        m4396b(context).putString(KEY_OBD_TT, str);
    }

    public static void setObdTimeStamp(Context context, long j) {
        m4396b(context).putLong(KEY_OBD_TIMESTAMP, j);
    }

    public static void setOilMethod(Context context, int i) {
        m4396b(context).putInt(KEY_OIL_METHOD, i);
    }

    public static void setPlayMode(Context context, PlayMode playMode) {
        m4396b(context).putString(KEY_MUSIC_PLAYMODE, playMode.getMode());
    }

    public static void setPosition1ObdType(Context context, String str) {
        m4396b(context).putString(KEY_POSITION_1_OBD_TYPE, str);
    }

    public static void setPosition2ObdType(Context context, String str) {
        m4396b(context).putString(KEY_POSITION_2_OBD_TYPE, str);
    }

    public static void setPressureFR(Context context, double d) {
        m4396b(context).putDouble(PREF_KEY_PRESSURE_FR, d);
    }

    public static void setPressureFl(Context context, double d) {
        m4396b(context).putDouble(PREF_KEY_PRESSURE_FL, d);
    }

    public static void setPressureRR(Context context, double d) {
        m4396b(context).putDouble(PREF_KEY_PRESSURE_RR, d);
    }

    public static void setPressureRl(Context context, double d) {
        m4396b(context).putDouble(PREF_KEY_PRESSURE_RL, d);
    }

    public static void setPrivacyState(Context context, String str) {
        m4396b(context).putString("setPrivacy", str);
    }

    public static void setPushTypeOrigin(Context context, String str) {
        m4396b(context).putString(KEY_PUSH_TYPE_ORIGIN, str);
    }

    public static void setResetSysTime(Context context, long j) {
        m4396b(context).putLong(KEY_RESET_SYS_TIME, j);
    }

    public static void setSettingHashcode(Context context, int i) {
        m4396b(context).putInt(KEY_SET_HASHCODE, i);
    }

    public static void setSimSerialNumber(Context context, String str) {
        if (str == null || str.length() <= 0) {
            return;
        }
        m4396b(context).putString(KEY_SIM_SERIAL_NUM, str);
    }

    public static void setTTSFinishDelaySupport(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_IS_TTSFINISH_DELAY_SUPPORT, z);
    }

    public static void setTTSFinishDelayTimes(Context context, int i) {
        m4396b(context).putInt(KEY_IS_TTSFINISH_DELAY_TIMES, i);
    }

    public static void setTemperatureFR(Context context, int i) {
        m4396b(context).putInt(PREF_KEY_TEMPERATURE_FR, i);
    }

    public static void setTemperatureFl(Context context, int i) {
        m4396b(context).putInt(PREF_KEY_TEMPERATURE_FL, i);
    }

    public static void setTemperatureRR(Context context, int i) {
        m4396b(context).putInt(PREF_KEY_TEMPERATURE_RR, i);
    }

    public static void setTemperatureRl(Context context, int i) {
        m4396b(context).putInt(PREF_KEY_TEMPERATURE_RL, i);
    }

    public static void setTireDevicesBind(Context context, boolean z) {
        m4396b(context).putBoolean(PREF_KEY_TIRE_BIND, z);
    }

    public static void setTireDevicesNames(Context context, String str) {
        m4396b(context).putString(PREF_KEY_TIRE_NAMES, str);
    }

    public static void setVoiceReady(Context context, boolean z) {
        m4396b(context).putBoolean(PREF_KEY_VOICE_READY, z);
    }

    public static void setVoiceSource(String str, Context context) {
        m4396b(context).putString(KEY_VOICE_SOURCE, str);
    }

    public static void setWeChatSendAudioEnable(Context context, boolean z) {
        m4396b(context).putBoolean(KEY_WECHAT_SEND_AUDIO, z);
    }

    public static void setWifiHotpotEnable(Context context, boolean z) {
        if (DeviceUtil.isSupportHotpot()) {
            m4396b(context).putBoolean(KEY_HOTPOT_ENABLE, z);
        }
    }

    public static void unregisterOnSharedPreferenceChangeListener(Context context, IMPSharedPreference.LeJaOnSharedPreferenceChangeListener leJaOnSharedPreferenceChangeListener) {
        m4396b(context).unregisterOnSharedPreferenceChangeListener(leJaOnSharedPreferenceChangeListener);
    }
}