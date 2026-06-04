package com.ileja.aicar.obd.impl.leja;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import com.cpsdna.obdports.ports.OBDAction;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aicar.obd.AIObdListener;
import com.ileja.aicar.obd.data.AIObdCmd;
import com.ileja.aicar.obd.data.AIObdDrivingStatisticsData;
import com.ileja.aicar.obd.data.AIObdErrorData;
import com.ileja.aicar.obd.data.AIObdHistoryData;
import com.ileja.aicar.obd.data.AIObdRealtimeData;
import com.ileja.aicar.obd.data.AIObdStatus;
import com.ileja.aicar.obd.upgrade.IOBDUpgradeListener;
import com.ileja.aicar.obd.upgrade.OBDUpgradeManager;
import com.ileja.ailbs.utils.StringUtils;
import com.ileja.bluetoothext.BtConstants;
import com.ileja.core.p007sp.BasePrefProvider;
import com.ileja.core.p007sp.IMPSharedPreference;
import com.ileja.module.BaseModuleApplication;
import com.ileja.serialport.SerialPortCallback;
import com.ileja.serialport.SerialPortHandler;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AILejaObdReader implements SerialPortCallback, IOBDUpgradeListener, IMPSharedPreference.LeJaOnSharedPreferenceChangeListener {
    private static final long MAX_RECV_COUNT = 30;
    private static final int OIL_METHOD_TIME = 1;
    private static final int OIL_METHOD_TOTAL = 0;
    private static final String TAG = "AILejaObdReader";
    private static float averageOilConsume = 0.0f;
    private static long lastCalcTime = 0;
    public static int oilMethod = 0;
    private static int totalMiles = -1;
    private static int totalOil = -1;
    private Set<AIObdListener> aiObdListeners;
    private Object mWait;
    private long notRecvDataCount = 0;
    private boolean autoStopTimeSet = false;
    private ArrayList<AIObdRealtimeData> realtimeDataArrayList = new ArrayList<>();
    private int serialNum = -1;

    @SuppressLint({"SimpleDateFormat"})
    private final SimpleDateFormat logSimpleDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
    private volatile boolean isNeedOBDUpgrade = false;
    private volatile boolean isAutoUpgradeCheck = false;
    private Thread mUpgradeThread = null;
    private volatile boolean mUpgradeThreadAlive = false;
    private volatile String mLastOBDUpgradeHex = "";
    private AIObdStatus aiObdStatus = new AIObdStatus();
    private volatile boolean running = false;

    public AILejaObdReader(Set<AIObdListener> set) {
        this.aiObdListeners = set;
    }

    private void addRealtimeHistory(AIObdRealtimeData aIObdRealtimeData) {
        synchronized (this.realtimeDataArrayList) {
            if (this.realtimeDataArrayList.size() >= 2) {
                this.realtimeDataArrayList.remove(0);
            }
            this.realtimeDataArrayList.add(aIObdRealtimeData);
        }
    }

    private void bindServer() {
        SerialPortHandler.getInstance().setSerialPortCallback(this);
        SerialPortHandler.getInstance().openSerialPort(19200);
        OBDUpgradeManager.getInstance().registerListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String bytesToHexString(byte b) {
        StringBuilder sb = new StringBuilder("");
        String hexString = Integer.toHexString(b & 255);
        if (hexString.length() < 2) {
            sb.append(0);
        }
        sb.append(hexString.toUpperCase());
        return sb.toString();
    }

    private synchronized void doSendCmd(String str) {
        if (TextUtils.isEmpty(str) || !str.contains(AIObdCmd.OBD_LEJA_MOCK_UPGRADE)) {
            AILog.m4029d(TAG, "begin send cmd " + str, LogLevel.RELEASE);
            long jCurrentTimeMillis = System.currentTimeMillis();
            SerialPortHandler.getInstance().sendSerialPort(str.getBytes(), null);
            AILog.m4028d(TAG, "end send cmd time comsume = " + (System.currentTimeMillis() - jCurrentTimeMillis));
            return;
        }
        String strReplaceAll = str.replaceAll("\r\n", "");
        String[] strArrSplit = strReplaceAll.split("=");
        SerialPortHandler.getInstance().sendSerialPort(AIObdCmd.OBD_LEJA_GET_VERSION.getBytes(), null);
        this.isAutoUpgradeCheck = false;
        if (strArrSplit.length == 1) {
            this.isNeedOBDUpgrade = true;
            if (strReplaceAll.contains("_")) {
                String[] strArrSplit2 = strReplaceAll.split("_");
                if (strArrSplit2.length > 1 && TextUtils.equals(strArrSplit2[1], "autoUpgradeCheck")) {
                    this.isAutoUpgradeCheck = true;
                }
            }
        } else if (strArrSplit.length > 1) {
            String str2 = strArrSplit[1];
            transferOBDFile2ROM("/sdcard/ilejaLog/Carrobot_OBD_" + str2 + ".bin", str2);
        }
        AILog.m4029d(TAG, "send cmd => " + strReplaceAll, LogLevel.RELEASE);
    }

    private boolean isValidMsg(byte[] bArr) {
        int i;
        boolean z = false;
        if (bArr != null) {
            try {
                if (bArr.length > 8 && (bArr[0] & 255) == 165 && (bArr[bArr.length - 1] & 255) == 165 && (i = bArr[1] & 255) != this.serialNum) {
                    this.serialNum = i;
                    int i2 = bArr[2] & 255;
                    int i3 = bArr[3] & 255;
                    int i4 = 0;
                    byte b = 0;
                    while (i4 < i3 + 3) {
                        i4++;
                        b = (byte) (b ^ bArr[i4]);
                    }
                    if ((bArr[bArr.length - 2] & 255) == (b & 255)) {
                        if (i2 == 1) {
                            z = true;
                        } else if (i2 == 2) {
                            String str = this.logSimpleDateFormat.format(new Date()) + " ## " + StringUtils.bytesToHexString(bArr);
                            AILog.writeOBDLogFile(TAG, str);
                            AILog.m4035i(TAG, "writeOBDLogFile logMsg=> " + str, LogLevel.RELEASE);
                        }
                    }
                    AILog.m4035i(TAG, "valid =>" + z + " ,serialNum =>" + this.serialNum + " ,type =>" + i2 + " ,contentLength => " + i3 + " ,sum => " + bytesToHexString(b) + ",bytes => " + StringUtils.bytesToHexString(bArr), LogLevel.RELEASE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                AILog.m4031e(TAG, "isValidMsg ex =>" + e, LogLevel.RELEASE);
            }
        }
        return z;
    }

    private void notifyDrivingStatisticsListener(AIObdDrivingStatisticsData aIObdDrivingStatisticsData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onDrivingStatisticsMessage(aIObdDrivingStatisticsData);
        }
    }

    private void notifyErrorDataListener(AIObdErrorData aIObdErrorData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onObdErrorMessage(aIObdErrorData);
        }
    }

    private void notifyHistoryListener(AIObdHistoryData aIObdHistoryData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onHistoryMessage(aIObdHistoryData);
        }
    }

    private void notifyOBDUpgradeMessageListener(String str) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onOBDUpgradeMessage(str);
        }
    }

    private void notifyOBDUpgradeThread(String str) {
        Object obj = this.mWait;
        if (obj != null) {
            synchronized (obj) {
                AILog.m4029d(TAG, "notifyOBDUpgradeThread => " + str, LogLevel.RELEASE);
                this.mWait.notify();
            }
        }
    }

    private void notifyOBDVersionMessageListener(String str) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onOBDVersionMessage(str);
        }
    }

    private void notifyRealtimeListener(AIObdRealtimeData aIObdRealtimeData) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onRealtimeMessage(aIObdRealtimeData);
        }
    }

    private void notifyStatusChangeListener(AIObdStatus aIObdStatus) {
        Iterator<AIObdListener> it = this.aiObdListeners.iterator();
        while (it.hasNext()) {
            it.next().onObdStatusChange(aIObdStatus);
        }
    }

    private void parseOBDData(byte[] bArr) {
        String strSubstring;
        String[] strArrSplit;
        float f;
        int i;
        short s;
        int i2;
        int i3;
        float f2;
        float f3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        short s2;
        int i9;
        float f4;
        float f5;
        float f6;
        short s3;
        int i10;
        float f7;
        int i11;
        float f8;
        float f9;
        float f10;
        if (!isValidMsg(bArr)) {
            String str = new String(bArr);
            AILog.m4029d("jiangfw", "message =>" + str, LogLevel.RELEASE);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            String[] strArrSplit2 = str.split("\r\n");
            for (int i12 = 0; strArrSplit2 != null && i12 < strArrSplit2.length; i12++) {
                int iIndexOf = strArrSplit2[i12].indexOf("AT+VERSION");
                if (iIndexOf > -1 && (strArrSplit = (strSubstring = strArrSplit2[i12].substring(iIndexOf)).split("=")) != null) {
                    if (strSubstring.length() > 1) {
                        String str2 = strArrSplit[1];
                        if (this.isNeedOBDUpgrade && !TextUtils.isEmpty(str2) && str2.startsWith("V")) {
                            OBDUpgradeManager.getInstance().checkHasNewVersion(OBDUpgradeManager.OBD_ZY_CHANNEL, "OBD_" + str2);
                            this.isNeedOBDUpgrade = false;
                        }
                        notifyOBDVersionMessageListener(str2);
                    }
                }
            }
            if (str.contains("AT+UPNUM=")) {
                this.notRecvDataCount = MAX_RECV_COUNT;
                if (!str.contains("ERR")) {
                    notifyOBDUpgradeThread(str);
                } else if (!TextUtils.isEmpty(this.mLastOBDUpgradeHex) && this.mLastOBDUpgradeHex.length() > 4) {
                    byte b = StringUtils.hexStringToBytes(this.mLastOBDUpgradeHex.substring(2, 4))[0];
                    SerialPortHandler.getInstance().sendSerialPort(StringUtils.hexStringToBytes(this.mLastOBDUpgradeHex), null);
                    AILog.m4029d(TAG, "retry push serialNum => " + ((int) b) + " ,hexStr =>" + this.mLastOBDUpgradeHex, LogLevel.RELEASE);
                }
            } else if (str.contains("AT+UPGRADE")) {
                this.notRecvDataCount = MAX_RECV_COUNT;
                if (str.contains("TIMEOUT")) {
                    if (this.mUpgradeThread != null) {
                        this.mUpgradeThreadAlive = false;
                        this.mUpgradeThread.interrupt();
                        this.mUpgradeThread = null;
                    }
                    playTTS("OBD升级失败，连接超时");
                } else {
                    notifyOBDUpgradeThread(str);
                }
            } else if (str.contains("AT+UPV")) {
                this.notRecvDataCount = MAX_RECV_COUNT;
                if (str.contains("ERR")) {
                    if (this.mUpgradeThread != null) {
                        this.mUpgradeThreadAlive = false;
                        this.mUpgradeThread.interrupt();
                        this.mUpgradeThread = null;
                    }
                    playTTS("OBD升级失败，版本号错误");
                } else {
                    notifyOBDUpgradeThread(str);
                }
            } else if (str.contains("AT+CKLEN")) {
                this.notRecvDataCount = MAX_RECV_COUNT;
                if (str.contains("OK")) {
                    playTTS("OBD升级成功");
                } else {
                    playTTS("OBD升级失败，长度不对");
                }
                notifyOBDUpgradeThread(str);
            }
            notifyOBDUpgradeMessageListener(str);
            return;
        }
        this.notRecvDataCount = MAX_RECV_COUNT;
        if (this.aiObdStatus.getStatus() == -1) {
            this.aiObdStatus.setStatus(0L);
            notifyStatusChangeListener(this.aiObdStatus);
            setCarDisplacementAndUnit();
            AILog.m4029d("ObdData", OBDAction.OBD_STATUS_CONNECTED, LogLevel.RELEASE);
        }
        if ((bArr[4] & 128) == 128) {
            f = ByteBuffer.wrap(bArr, 7, 2).getShort() / 10.0f;
            i = 9;
        } else {
            f = 0.0f;
            i = 7;
        }
        AILog.m4029d("AIOBD", "电瓶电压=" + f, LogLevel.RELEASE);
        if ((bArr[4] & 64) == 64) {
            s = ByteBuffer.wrap(bArr, i, 2).getShort();
            i += 2;
        } else {
            s = 0;
        }
        AILog.m4029d("AIOBD", "发动机转速=" + ((int) s), LogLevel.RELEASE);
        if ((bArr[4] & 32) == 32) {
            i2 = bArr[i] & 255;
            i++;
        } else {
            i2 = 0;
        }
        AILog.m4029d("AIOBD", "车辆速度=" + i2, LogLevel.RELEASE);
        if ((bArr[4] & 16) == 16) {
            i3 = (bArr[i] & 255) - 40;
            i++;
        } else {
            i3 = 0;
        }
        AILog.m4029d("AIOBD", "水箱温度=" + i3, LogLevel.RELEASE);
        if ((bArr[4] & 8) == 8) {
            f2 = ByteBuffer.wrap(bArr, i, 2).getShort() * 0.01f;
            i += 2;
        } else {
            f2 = 0.0f;
        }
        AILog.m4029d("AIOBD", "瞬时油耗=" + f2, LogLevel.RELEASE);
        if ((bArr[4] & 4) == 4) {
            f3 = ByteBuffer.wrap(bArr, i, 2).getShort() * 0.01f;
            i += 2;
        } else {
            f3 = 0.0f;
        }
        AILog.m4029d("AIOBD", "平均油耗=" + f3, LogLevel.RELEASE);
        if ((bArr[4] & 2) == 2) {
            i4 = ByteBuffer.wrap(bArr, i, 4).getInt();
            i += 4;
        } else {
            i4 = 0;
        }
        AILog.m4029d("AIOBD", "行驶里程=" + i4, LogLevel.RELEASE);
        if ((bArr[4] & 1) == 1) {
            i5 = bArr[i] & 255;
            i++;
        } else {
            i5 = 0;
        }
        AILog.m4029d("AIOBD", "故障码状态=" + i5, LogLevel.RELEASE);
        if ((bArr[5] & 128) == 128) {
            i6 = bArr[i] & 255;
            i++;
        } else {
            i6 = 0;
        }
        AILog.m4029d("AIOBD", "发动机故障码个数=" + i6, LogLevel.RELEASE);
        if ((bArr[5] & 64) == 64) {
            i7 = (bArr[i] & 255) - 40;
            i++;
        } else {
            i7 = 0;
        }
        AILog.m4029d("AIOBD", "进气口温度=" + i7, LogLevel.RELEASE);
        if ((bArr[5] & 32) == 32) {
            i8 = bArr[i] & 255;
            i++;
        } else {
            i8 = 0;
        }
        AILog.m4029d("AIOBD", "进气歧管压力=" + i8, LogLevel.RELEASE);
        if ((bArr[5] & 16) == 16) {
            s2 = ByteBuffer.wrap(bArr, i, 2).getShort();
            i += 2;
        } else {
            s2 = 0;
        }
        AILog.m4029d("AIOBD", "燃油压力=" + ((int) s2), LogLevel.RELEASE);
        if ((bArr[5] & 8) == 8) {
            i9 = bArr[i] & 255;
            i++;
        } else {
            i9 = 0;
        }
        AILog.m4029d("AIOBD", "大气压力=" + i9, LogLevel.RELEASE);
        if ((bArr[5] & 4) == 4) {
            f4 = ByteBuffer.wrap(bArr, i, 2).getShort() / 10.0f;
            i += 2;
        } else {
            f4 = 0.0f;
        }
        AILog.m4029d("AIOBD", "空气流量=" + f4, LogLevel.RELEASE);
        if ((bArr[5] & 2) == 2) {
            f5 = ByteBuffer.wrap(bArr, i, 2).getShort() / 10.0f;
            i += 2;
        } else {
            f5 = 0.0f;
        }
        AILog.m4029d("AIOBD", "绝对节气门位置传感器=" + f5, LogLevel.RELEASE);
        if ((bArr[5] & 1) == 1) {
            f6 = ByteBuffer.wrap(bArr, i, 2).getShort() / 10.0f;
            i += 2;
        } else {
            f6 = 0.0f;
        }
        AILog.m4029d("AIOBD", "油门踏板位置=" + f6, LogLevel.RELEASE);
        if ((bArr[5] & 128) == 128) {
            s3 = ByteBuffer.wrap(bArr, i, 2).getShort();
            i += 2;
        } else {
            s3 = 0;
        }
        AILog.m4029d("AIOBD", "发动机启动后运行时间=" + ((int) s3), LogLevel.RELEASE);
        if ((bArr[6] & 64) == 64) {
            i10 = ByteBuffer.wrap(bArr, i, 4).getInt();
            i += 4;
        } else {
            i10 = 0;
        }
        AILog.m4029d("AIOBD", "故障行驶里程=" + i10, LogLevel.RELEASE);
        if ((bArr[6] & 32) == 32) {
            f7 = ByteBuffer.wrap(bArr, i, 2).getShort() / 10.0f;
            i += 2;
        } else {
            f7 = 0.0f;
        }
        AILog.m4029d("AIOBD", "剩余油量=" + f7, LogLevel.RELEASE);
        if ((bArr[6] & 16) == 16) {
            i11 = bArr[i] & 255;
            i++;
        } else {
            i11 = 0;
        }
        AILog.m4029d("AIOBD", "发动机负荷=" + i11, LogLevel.RELEASE);
        if ((bArr[6] & 8) == 8) {
            f8 = ByteBuffer.wrap(bArr, i, 2).getShort() / 10.0f;
            i += 2;
        } else {
            f8 = 0.0f;
        }
        AILog.m4029d("AIOBD", "长期燃油修正=" + f8, LogLevel.RELEASE);
        if ((bArr[6] & 4) == 4) {
            f9 = (ByteBuffer.wrap(bArr, i, 2).getShort() / 10.0f) - 64.0f;
            i += 2;
        } else {
            f9 = 0.0f;
        }
        AILog.m4029d("AIOBD", "点火提前角=" + f9, LogLevel.RELEASE);
        if ((bArr[6] & 2) == 2) {
            f10 = ByteBuffer.wrap(bArr, i, 2).getShort() * 0.01f;
            i += 2;
        } else {
            f10 = 0.0f;
        }
        AILog.m4029d("AIOBD", "怠速耗油=" + f10, LogLevel.RELEASE);
        int i13 = (bArr[6] & 1) == 1 ? ByteBuffer.wrap(bArr, i, 4).getInt() : 0;
        AILog.m4029d("AIOBD", "总的燃油消耗量=" + i13, LogLevel.RELEASE);
        AIObdRealtimeData aIObdRealtimeData = new AIObdRealtimeData();
        aIObdRealtimeData.type = (byte) 0;
        aIObdRealtimeData.f5953id = "Leja";
        aIObdRealtimeData.obdBattery = f;
        aIObdRealtimeData.obdSpeed = i2;
        aIObdRealtimeData.obdRotateSpeed = s;
        aIObdRealtimeData.obdCoolantTemprature = i3;
        aIObdRealtimeData.obdAverageFuelConsume = f3;
        if (i2 == 0) {
            f2 = f10;
        }
        aIObdRealtimeData.obdRealtimeFuelConsume = f2;
        aIObdRealtimeData.obdDrivingRange = i4;
        aIObdRealtimeData.obdFuelConsume = i13;
        notifyRealtimeListener(aIObdRealtimeData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playTTS(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            Intent intent = new Intent("com.ileja.carrobot.action.MOCK_PUSH");
            intent.setClassName(BtConstants.AIBLUETOOTHEXT_BIND_PKG_NAME, "com.ileja.carrobot.debug.MockPushReceiver");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("hudCmdType", "tts");
            jSONObject.put("data", str);
            intent.putExtra("pushData", jSONObject.toString());
            BaseModuleApplication.getContext().sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCarDisplacementAndUnit() {
        String carDisplacement = BasePrefProvider.getCarDisplacement(BaseModuleApplication.getApplication());
        try {
            carDisplacement = String.valueOf((int) (Float.parseFloat(carDisplacement) * 1000.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = AIObdCmd.OBD_LEJA_CARDISPLACEMENT + carDisplacement + "," + BasePrefProvider.getCarDisplacementUnit(BaseModuleApplication.getApplication()) + "\r\n";
        sendCmd(str);
        AILog.m4029d(TAG, "setCarDisplacementAndUnit cmd :" + str, LogLevel.RELEASE);
    }

    private void transferOBDFile2ROM(final String str, final String str2) {
        AILog.m4029d(TAG, "transferOBDFile2ROM zipFile => " + str + " ,version => " + str2, LogLevel.RELEASE);
        if (this.mUpgradeThread != null) {
            this.mUpgradeThreadAlive = false;
            this.mUpgradeThread.interrupt();
            this.mUpgradeThread = null;
        }
        this.mWait = new Object();
        playTTS("检测到OBD有新版本" + str2 + "，后台自动升级中，请不要断电");
        Thread thread = new Thread(new Runnable() { // from class: com.ileja.aicar.obd.impl.leja.AILejaObdReader.1
            /* JADX WARN: Code restructure failed: missing block: B:133:?, code lost:
            
                return;
             */
            /* JADX WARN: Code restructure failed: missing block: B:68:0x022b, code lost:
            
                r22 = r2;
                r19 = r3;
                r21 = r4;
             */
            /* JADX WARN: Code restructure failed: missing block: B:70:0x0237, code lost:
            
                if (r23.f5959e.mUpgradeThreadAlive != false) goto L76;
             */
            /* JADX WARN: Code restructure failed: missing block: B:71:0x0239, code lost:
            
                r11.close();
             */
            /* JADX WARN: Code restructure failed: missing block: B:73:0x023d, code lost:
            
                r0 = move-exception;
             */
            /* JADX WARN: Code restructure failed: missing block: B:74:0x023e, code lost:
            
                r0.printStackTrace();
             */
            /* JADX WARN: Code restructure failed: missing block: B:75:0x0242, code lost:
            
                return;
             */
            /* JADX WARN: Code restructure failed: missing block: B:76:0x0243, code lost:
            
                r0 = com.ileja.aicar.obd.data.AIObdCmd.OBD_LEJA_UPGRAGE_ROM_SIZE + r10 + r22;
                com.ileja.serialport.SerialPortHandler.getInstance().sendSerialPort(r0.getBytes(), null);
                com.ileja.aibase.common.AILog.m4029d(com.ileja.aicar.obd.impl.leja.AILejaObdReader.TAG, r21 + r0.trim() + r19 + r10, com.ileja.aibase.common.logger.LogLevel.RELEASE);
             */
            /* JADX WARN: Code restructure failed: missing block: B:77:0x0287, code lost:
            
                r11.close();
             */
            /* JADX WARN: Removed duplicated region for block: B:112:0x02b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:135:? A[RETURN, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:136:? A[SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:90:0x02a4 A[Catch: Exception -> 0x02a8, TRY_ENTER, TRY_LEAVE, TryCatch #4 {Exception -> 0x02a8, blocks: (B:77:0x0287, B:90:0x02a4), top: B:108:0x0023 }] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() throws java.lang.Throwable {
                /*
                    Method dump skipped, instruction units count: 700
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.ileja.aicar.obd.impl.leja.AILejaObdReader.RunnableC11241.run():void");
            }
        });
        this.mUpgradeThread = thread;
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void waitOBDUpgradeThread(String str) {
        try {
            AILog.m4029d(TAG, "waitOBDUpgradeThread => " + str + ",wait =>" + this.mWait, LogLevel.RELEASE);
            if (this.mWait != null) {
                synchronized (this.mWait) {
                    this.mWait.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override // com.ileja.serialport.SerialPortCallback
    public void fail() {
    }

    @Override // com.ileja.aicar.obd.upgrade.IOBDUpgradeListener
    public void onDownloadFinished(String str, String str2, String str3) {
    }

    @Override // com.ileja.aicar.obd.upgrade.IOBDUpgradeListener
    public void onDownloadProgress(long j, long j2) {
    }

    @Override // com.ileja.aicar.obd.upgrade.IOBDUpgradeListener
    public void onDownloadStart() {
    }

    @Override // com.ileja.aicar.obd.upgrade.IOBDUpgradeListener
    public void onError(String str, String str2) {
        AILog.m4029d(TAG, "onError errCode=> " + str + " ,errMsg => " + str2, LogLevel.RELEASE);
        StringBuilder sb = new StringBuilder();
        sb.append("OBD升级失败 ");
        sb.append(str2);
        playTTS(sb.toString());
    }

    @Override // com.ileja.aicar.obd.upgrade.IOBDUpgradeListener
    public void onHasNewVersion(boolean z, String str) {
        AILog.m4029d(TAG, "onHasNewVersion hasNewVersion=> " + z + ",version =>" + str, LogLevel.RELEASE);
        if (!z && !this.isAutoUpgradeCheck) {
            playTTS("当前OBD已是最新版本" + str);
        }
        this.isAutoUpgradeCheck = false;
    }

    @Override // com.ileja.core.sp.IMPSharedPreference.LeJaOnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(String str) {
        if (BasePrefProvider.KEY_CAR_DISPLACEMENT.equals(str)) {
            setCarDisplacementAndUnit();
            return;
        }
        if (BasePrefProvider.KEY_OIL_METHOD.equals(str)) {
            AILog.m4029d(TAG, "设置油耗计算方法:" + BasePrefProvider.getOilMethod(BaseModuleApplication.getApplication()), LogLevel.RELEASE);
        }
    }

    @Override // com.ileja.aicar.obd.upgrade.IOBDUpgradeListener
    public void onUnzipFinished(String str, String str2, String str3) {
        transferOBDFile2ROM(str2, str3);
    }

    @Override // com.ileja.serialport.SerialPortCallback
    public void readData(byte[] bArr, int i) {
        byte[] bArrHexStringToBytes = new byte[i];
        System.arraycopy(bArr, 0, bArrHexStringToBytes, 0, i);
        String strBytesToHexString = StringUtils.bytesToHexString(bArrHexStringToBytes);
        AILog.m4029d("ObdData", "encode hex => " + strBytesToHexString + ",size =>" + i, LogLevel.RELEASE);
        if (strBytesToHexString.contains("A602") || strBytesToHexString.contains("A601")) {
            strBytesToHexString = strBytesToHexString.replaceAll("A602", "A5").replaceAll("A601", "A6");
            bArrHexStringToBytes = StringUtils.hexStringToBytes(strBytesToHexString);
            AILog.m4029d("ObdData", "decode hex => " + strBytesToHexString + ",size =>" + i, LogLevel.RELEASE);
        }
        int iIndexOf = strBytesToHexString.indexOf("A5A5");
        if (iIndexOf > 0) {
            int i2 = iIndexOf + 2;
            String strSubstring = strBytesToHexString.substring(0, i2);
            parseOBDData(StringUtils.hexStringToBytes(strSubstring));
            String strSubstring2 = strBytesToHexString.substring(i2);
            parseOBDData(StringUtils.hexStringToBytes(strSubstring2));
            AILog.m4029d("ObdData", "decode hex dataStrHexNew_1 => " + strSubstring + ",dataStrHexNew_2 =>" + strSubstring2 + ",index =>" + iIndexOf, LogLevel.RELEASE);
        } else {
            parseOBDData(bArrHexStringToBytes);
        }
        long j = this.notRecvDataCount - 1;
        this.notRecvDataCount = j;
        if (j <= 0) {
            this.notRecvDataCount = 0L;
            if (this.aiObdStatus.getStatus() == 0) {
                this.realtimeDataArrayList.clear();
                this.aiObdStatus.setStatus(-1L);
                notifyStatusChangeListener(this.aiObdStatus);
                AILog.m4029d("ObdData", "OBD_STATUS_DISCONNECTED", LogLevel.RELEASE);
            }
        }
    }

    public void sendCmd(String str) {
        if (this.autoStopTimeSet) {
            return;
        }
        doSendCmd(str);
    }

    public void sendRtOffCmd() {
    }

    public synchronized void start() {
        BasePrefProvider.registerOnSharedPreferenceChangeListener(BaseModuleApplication.getApplication(), this);
        this.running = true;
        bindServer();
        this.autoStopTimeSet = false;
        this.notRecvDataCount = MAX_RECV_COUNT;
        this.aiObdStatus.setStatus(-1L);
        this.realtimeDataArrayList.clear();
    }

    public synchronized boolean stop() {
        if (!this.running) {
            return false;
        }
        BasePrefProvider.unregisterOnSharedPreferenceChangeListener(BaseModuleApplication.getApplication(), this);
        SerialPortHandler.getInstance().setSerialPortCallback(null);
        sendRtOffCmd();
        this.running = false;
        this.aiObdStatus.setStatus(-1L);
        this.realtimeDataArrayList.clear();
        SerialPortHandler.getInstance().closeSerialPort();
        OBDUpgradeManager.getInstance().unRegisterListener(this);
        return true;
    }

    @Override // com.ileja.serialport.SerialPortCallback
    public void success() {
        SerialPortHandler.getInstance().startReceiveMsg();
    }
}