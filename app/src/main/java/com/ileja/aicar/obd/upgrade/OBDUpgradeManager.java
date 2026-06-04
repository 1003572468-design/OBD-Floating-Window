package com.ileja.aicar.obd.upgrade;

import android.os.Environment;
import android.text.TextUtils;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.FileUtil;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.encrypt.MD5Util;
import com.ileja.aibase.http.base.ComNetParam;
import com.ileja.aicar.obd.upgrade.net.ServerHttpUtils;
import com.ileja.ailbs.location.provider.AIBaseLocationProvider;
import com.ximalaya.ting.android.opensdk.player.statistic.OpenSdkPlayStatisticUpload;
import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class OBDUpgradeManager {
    public static final String OBD_DN_CHANNEL = "OBD_DN_0001";
    public static final String OBD_MY_CHANNEL = "OBD_MY_0001";
    public static final String OBD_SRD_CHANNEL = "OBD_SRD_0001";
    public static final String OBD_ZY_CHANNEL = "OBD_ZY_0001";
    private static final String TAG = "OBDUpgradeManager";
    private static volatile OBDUpgradeManager mInstance;
    private final int OBD_SRD_FIRMWARE = 3;
    private final int OBD_DN_FIRMWARE = 4;
    private final int OBD_MY_FIRMWARE = 5;
    private final int OBD_ZY_FIRMWARE = 6;
    private final String OBD_VERSION_FILE = Environment.getExternalStorageDirectory().getPath() + File.separator + "carrobotOBDUpgrade";
    private boolean isUpgrading = false;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(5);
    private List<IOBDUpgradeListener> listeners = new CopyOnWriteArrayList();

    private OBDUpgradeManager() {
    }

    private boolean checkChannel(String str, int i) {
        if (i == 3 && TextUtils.equals(str, OBD_SRD_CHANNEL)) {
            return true;
        }
        if (i == 4 && TextUtils.equals(str, OBD_DN_CHANNEL)) {
            return true;
        }
        if (i == 5 && TextUtils.equals(str, OBD_MY_CHANNEL)) {
            return true;
        }
        return i == 6 && TextUtils.equals(str, OBD_ZY_CHANNEL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkMD5(String str) {
        File file = new File(this.OBD_VERSION_FILE + File.separator + str);
        if (!file.exists()) {
            AILog.m4029d(TAG, "checkMD5 checkMD5 = false, file not exist", LogLevel.RELEASE);
            return false;
        }
        String fileMD5 = MD5Util.getFileMD5(file);
        AILog.m4029d(TAG, "checkMD5 md5 = " + str + ", md5Sum = " + fileMD5, LogLevel.RELEASE);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(fileMD5) || !TextUtils.equals(str.toLowerCase(), fileMD5.toLowerCase())) {
            AILog.m4029d(TAG, "checkMD5 checkMD5 = false, md5 not match", LogLevel.RELEASE);
            return false;
        }
        AILog.m4029d(TAG, "checkMD5 checkMD5 = true", LogLevel.RELEASE);
        return true;
    }

    private boolean checkoutCode(byte[] bArr) {
        byte[] bArr2 = new byte[4];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (i2 < 4 || i2 > 7) {
                i += bArr[i2] & 255;
            } else {
                bArr2[i2 - 4] = bArr[i2];
            }
        }
        int i3 = ((bArr2[0] & 255) << 24) | ((bArr2[1] & 255) << 16) | ((bArr2[2] & 255) << 8) | (bArr2[3] & 255);
        AILog.m4035i(TAG, " byteSum => " + i + ", checkCode => " + i3, LogLevel.RELEASE);
        return i3 == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteVersion() {
        File file = new File(this.OBD_VERSION_FILE);
        if (file.exists()) {
            FileUtil.deleteAllFiles(file);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0125 A[PHI: r4
  0x0125: PHI (r4v2 java.net.HttpURLConnection) = (r4v1 java.net.HttpURLConnection), (r4v6 java.net.HttpURLConnection) binds: [B:54:0x0123, B:33:0x00fa] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void download(java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.String r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 307
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ileja.aicar.obd.upgrade.OBDUpgradeManager.download(java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public static OBDUpgradeManager getInstance() {
        if (mInstance == null) {
            synchronized (OBDUpgradeManager.class) {
                if (mInstance == null) {
                    mInstance = new OBDUpgradeManager();
                }
            }
        }
        return mInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnDownloadFinished(String str, String str2, String str3) {
        synchronized (this.listeners) {
            for (IOBDUpgradeListener iOBDUpgradeListener : this.listeners) {
                if (iOBDUpgradeListener != null) {
                    iOBDUpgradeListener.onDownloadFinished(str, str2, str3);
                }
            }
        }
    }

    private void notifyOnDownloadProgress(long j, long j2) {
        synchronized (this.listeners) {
            for (IOBDUpgradeListener iOBDUpgradeListener : this.listeners) {
                if (iOBDUpgradeListener != null) {
                    iOBDUpgradeListener.onDownloadProgress(j, j2);
                }
            }
        }
    }

    private void notifyOnDownloadStart() {
        synchronized (this.listeners) {
            for (IOBDUpgradeListener iOBDUpgradeListener : this.listeners) {
                if (iOBDUpgradeListener != null) {
                    iOBDUpgradeListener.onDownloadStart();
                }
            }
        }
    }

    private void notifyOnError(String str, String str2) {
        synchronized (this.listeners) {
            for (IOBDUpgradeListener iOBDUpgradeListener : this.listeners) {
                if (iOBDUpgradeListener != null) {
                    iOBDUpgradeListener.onError(str, str2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnHasNewVersion(boolean z, String str) {
        synchronized (this.listeners) {
            for (IOBDUpgradeListener iOBDUpgradeListener : this.listeners) {
                if (iOBDUpgradeListener != null) {
                    iOBDUpgradeListener.onHasNewVersion(z, str);
                }
            }
        }
    }

    private void notifyOnUnzipFinished(String str, String str2, String str3) {
        synchronized (this.listeners) {
            for (IOBDUpgradeListener iOBDUpgradeListener : this.listeners) {
                if (iOBDUpgradeListener != null) {
                    iOBDUpgradeListener.onUnzipFinished(str, str2, str3);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:121:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x02b6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x02c1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void parasePatch(java.lang.String r17, java.lang.String r18) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 716
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ileja.aicar.obd.upgrade.OBDUpgradeManager.parasePatch(java.lang.String, java.lang.String):void");
    }

    public void checkHasNewVersion(final String str, final String str2) {
        AILog.m4029d(TAG, "checkHasNewVersion cur_ver：" + str2 + ", isUpgrading：" + this.isUpgrading, LogLevel.RELEASE);
        if (this.isUpgrading) {
            return;
        }
        this.mExecutorService.execute(new Runnable() { // from class: com.ileja.aicar.obd.upgrade.OBDUpgradeManager.1
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                JSONObject jSONObjectOptJSONObject;
                int i = 0;
                while (true) {
                    int i2 = i + 1;
                    if (i > 20) {
                        return;
                    }
                    OBDUpgradeManager.this.isUpgrading = true;
                    String str3 = "http://upgrade.ileja.com/upgrade/update_apks/v2/get_conf?upgrade_channel=" + str + "&cur_ver=" + str2 + "&userId=" + ComNetParam.getDiu() + "&appcode=RI00001&vers=" + ComNetParam.getRomSoftVersion() + "&deviceId=" + ComNetParam.getDiu();
                    String strDoHttpRequest = ServerHttpUtils.doHttpRequest("POST", str3);
                    try {
                        JSONObject jSONObject = new JSONObject(strDoHttpRequest);
                        if (TextUtils.equals(jSONObject.optString("code"), "00")) {
                            JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("data");
                            if (jSONObjectOptJSONObject2 != null && jSONObjectOptJSONObject2.has("OBD") && (jSONObjectOptJSONObject = jSONObjectOptJSONObject2.optJSONObject("OBD")) != null && jSONObjectOptJSONObject.has("url")) {
                                String strOptString = jSONObjectOptJSONObject.optString("md5");
                                String strOptString2 = jSONObjectOptJSONObject.optString("url");
                                String strOptString3 = jSONObjectOptJSONObject.optString(OpenSdkPlayStatisticUpload.KEY_VERSION);
                                OBDUpgradeManager.this.notifyOnHasNewVersion(true, "V" + strOptString3);
                                if (OBDUpgradeManager.this.checkMD5(strOptString)) {
                                    OBDUpgradeManager.this.isUpgrading = false;
                                    OBDUpgradeManager.this.notifyOnDownloadFinished(OBDUpgradeManager.this.OBD_VERSION_FILE + File.separator + strOptString, strOptString, strOptString3);
                                    OBDUpgradeManager.this.parasePatch(str, OBDUpgradeManager.this.OBD_VERSION_FILE + File.separator + strOptString);
                                } else {
                                    OBDUpgradeManager.this.download(str, strOptString2, strOptString, "V" + strOptString3);
                                }
                            }
                        } else {
                            OBDUpgradeManager.this.isUpgrading = false;
                            OBDUpgradeManager.this.deleteVersion();
                            OBDUpgradeManager.this.notifyOnHasNewVersion(false, str2.split("_")[1]);
                        }
                        AILog.m4035i(OBDUpgradeManager.TAG, "checkHasNewVersion requestUrl=>" + str3 + " ,responseMsg => " + strDoHttpRequest, LogLevel.RELEASE);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        OBDUpgradeManager.this.isUpgrading = false;
                        try {
                            Thread.sleep(AIBaseLocationProvider.LOCATION_INVALID_TIME);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        i = i2;
                    }
                }
            }
        });
    }

    public void registerListener(IOBDUpgradeListener iOBDUpgradeListener) {
        if (iOBDUpgradeListener != null) {
            synchronized (this.listeners) {
                if (!this.listeners.contains(iOBDUpgradeListener)) {
                    this.listeners.add(iOBDUpgradeListener);
                }
            }
        }
    }

    public void unRegisterListener(IOBDUpgradeListener iOBDUpgradeListener) {
        if (iOBDUpgradeListener != null) {
            synchronized (this.listeners) {
                if (this.listeners.contains(iOBDUpgradeListener)) {
                    this.listeners.remove(iOBDUpgradeListener);
                }
            }
        }
    }
}