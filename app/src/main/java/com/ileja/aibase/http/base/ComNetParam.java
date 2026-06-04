package com.ileja.aibase.http.base;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.ileja.aibase.AiBase;
import com.ileja.aibase.AiBasePrefHelper;
import com.ileja.aibase.encrypt.AES;
import com.ileja.aibase.encrypt.Sha1Util;
import com.ileja.aibase.phone.DeviceUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class ComNetParam {
    private static final String CH_TAG = "channel";
    private static final String DIU_TAG = "diu";
    private static final String EX_TAG = "ex";
    private static final String HD_TAG = "hd";
    private static final String MAC_TAG = "mc";
    private static final String MD_TAG = "model";
    private static final String MULACC_TAG = "mulacc";
    private static final String RM_TAG = "rm";
    private static final String RS_TAG = "rs";
    private static final String SECRET_KEY = "##ileja_2015##";
    private static final String SIGN_TAG = "sign";
    private static final String SI_TAG = "si";
    private static final String SN_TAG = "sn";
    private static final String SS_TAG = "sserial";
    private static final String TS_TAG = "ts";
    private static String VALUE_SN = Build.SERIAL;
    private static final String VERS_TAG = "vers";
    private static String diu = "";
    private static int div = 0;

    /* JADX INFO: renamed from: hd */
    private static String f5905hd = "";
    private static String mac = "";

    /* JADX INFO: renamed from: rm */
    private static String f5906rm = "";

    /* JADX INFO: renamed from: rs */
    private static int f5907rs = 0;

    /* JADX INFO: renamed from: si */
    private static String f5908si = "";

    /* JADX INFO: renamed from: ss */
    private static String f5909ss = "";

    public static String getCommonParam() {
        String strEncode;
        boolean zIsPhone = AiBase.getInst().isPhone();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("&");
        stringBuffer.append(VERS_TAG);
        stringBuffer.append("=");
        stringBuffer.append(getDiv());
        String mac2 = getMac();
        if (!TextUtils.isEmpty(mac2)) {
            stringBuffer.append("&");
            stringBuffer.append(MAC_TAG);
            stringBuffer.append("=");
            stringBuffer.append(mac2);
        }
        if (!TextUtils.isEmpty(mac2) && zIsPhone) {
            VALUE_SN = mac2;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        String sign = null;
        try {
            sign = getSign(jCurrentTimeMillis);
        } catch (NoSuchAlgorithmException unused) {
        }
        if (!TextUtils.isEmpty(sign)) {
            stringBuffer.append("&");
            stringBuffer.append(SIGN_TAG);
            stringBuffer.append("=");
            stringBuffer.append(sign);
        }
        stringBuffer.append("&");
        stringBuffer.append("sn");
        stringBuffer.append("=");
        stringBuffer.append(VALUE_SN);
        stringBuffer.append("&");
        stringBuffer.append(TS_TAG);
        stringBuffer.append("=");
        stringBuffer.append(jCurrentTimeMillis);
        String extra = getExtra();
        if (!TextUtils.isEmpty(extra)) {
            stringBuffer.append("&");
            stringBuffer.append(EX_TAG);
            stringBuffer.append("=");
            stringBuffer.append(extra);
        }
        stringBuffer.append("&");
        stringBuffer.append(DIU_TAG);
        stringBuffer.append("=");
        stringBuffer.append(getDiu());
        String si = getSI();
        if (!TextUtils.isEmpty(si)) {
            stringBuffer.append("&");
            stringBuffer.append(SI_TAG);
            stringBuffer.append("=");
            stringBuffer.append(si);
        }
        String ss = getSS();
        if (!TextUtils.isEmpty(ss)) {
            stringBuffer.append("&");
            stringBuffer.append(SS_TAG);
            stringBuffer.append("=");
            stringBuffer.append(ss);
        }
        String hardVersion = getHardVersion();
        if (!TextUtils.isEmpty(hardVersion)) {
            stringBuffer.append("&");
            stringBuffer.append(HD_TAG);
            stringBuffer.append("=");
            stringBuffer.append(hardVersion);
        }
        String romVersion = getRomVersion();
        if (!TextUtils.isEmpty(romVersion)) {
            stringBuffer.append("&");
            stringBuffer.append(RM_TAG);
            stringBuffer.append("=");
            stringBuffer.append(romVersion);
        }
        int romSoftVersion = getRomSoftVersion();
        if (romSoftVersion > 0) {
            stringBuffer.append("&");
            stringBuffer.append(RS_TAG);
            stringBuffer.append("=");
            stringBuffer.append(romSoftVersion);
        }
        String channel = AiBase.getInst().getChannel();
        if (!TextUtils.isEmpty(channel)) {
            stringBuffer.append("&");
            stringBuffer.append("channel");
            stringBuffer.append("=");
            stringBuffer.append(channel);
        }
        try {
            strEncode = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            strEncode = "";
        }
        if (!TextUtils.isEmpty(strEncode)) {
            stringBuffer.append("&");
            stringBuffer.append("model");
            stringBuffer.append("=");
            stringBuffer.append(strEncode);
        }
        stringBuffer.append("&");
        stringBuffer.append(MULACC_TAG);
        stringBuffer.append("=");
        stringBuffer.append("true");
        return stringBuffer.toString();
    }

    public static String getDiu() {
        if (TextUtils.isEmpty(diu)) {
            diu = DeviceUtil.getDeviceId(AiBase.getInst().getContext());
        }
        if (diu == null) {
            diu = "";
        }
        return diu;
    }

    public static String getDiv() {
        if (div == 0) {
            div = DeviceUtil.getPackageVersionCode(AiBase.getInst().getContext());
        }
        return div + "";
    }

    public static String getExtra() {
        try {
            return AES.encode(((int) (AiBase.getInst().getLatitude() * 1000000.0d)) + "," + ((int) (AiBase.getInst().getLongitude() * 1000000.0d)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getHardVersion() {
        if (TextUtils.isEmpty(f5905hd)) {
            f5905hd = DeviceUtil.getHardwareVersion();
        }
        return f5905hd;
    }

    public static String getMac() {
        if (TextUtils.isEmpty(mac)) {
            mac = AiBasePrefHelper.getMacAddr(AiBase.getInst().getContext());
        }
        if (TextUtils.isEmpty(mac)) {
            String mac2 = DeviceUtil.getMac(AiBase.getInst().getContext());
            if (TextUtils.isEmpty(mac2)) {
                mac = AES.encode(UUID.randomUUID().toString());
                AiBasePrefHelper.setMacAddr(AiBase.getInst().getContext(), mac);
            } else if ("02:00:00:00:00:00".equals(mac2)) {
                mac = AES.encode(DeviceUtil.getWifiMacAddress());
                AiBasePrefHelper.setMacAddr(AiBase.getInst().getContext(), mac);
            } else {
                mac = AES.encode(mac2);
                AiBasePrefHelper.setMacAddr(AiBase.getInst().getContext(), mac);
            }
        }
        return mac;
    }

    public static int getRomSoftVersion() {
        if (f5907rs <= 0) {
            f5907rs = DeviceUtil.getRomSoftVersion();
        }
        return f5907rs;
    }

    public static String getRomVersion() {
        if (TextUtils.isEmpty(f5906rm)) {
            f5906rm = DeviceUtil.getRomVersion();
        }
        return f5906rm;
    }

    public static String getSI() {
        if (TextUtils.isEmpty(f5908si)) {
            f5908si = DeviceUtil.getIMSI(AiBase.getInst().getContext());
        }
        return f5908si;
    }

    public static String getSS() {
        f5909ss = DeviceUtil.getSimSerialNum(AiBase.getInst().getContext());
        Log.d("ComNetParam", "##### " + f5909ss);
        return f5909ss;
    }

    public static String getSign(long j) {
        return Sha1Util.sha1(VALUE_SN + "_" + j + "_" + SECRET_KEY);
    }
}