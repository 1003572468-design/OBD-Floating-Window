package com.ileja.aibase.common.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v4.view.InputDeviceCompat;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import com.ileja.aibase.common.AILog;
import com.ileja.aitelcomm.music.utils.MusicQueryConfig;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes.dex */
public class NetSignalChangeObserver {
    private static boolean PRINT_LOG = false;
    private static final String TAG = "NetSignalChangeObserver";
    public static final int WIFI_SIGNAL_LEVELS = 4;
    private Context mContext;
    private int mMobileLevel;
    private int mMobileNetWorkClass;
    private SignalStrength mMobileSignalStrength;
    private SignalChangeListener mSignalChangeListener;
    private TelephonyManager mTelephonyManager;
    private ServiceState mServiceState = null;
    protected int mWifiLevel = 0;
    private final PhoneStateListener mPhoneStateListenerImpl = new PhoneStateListener() { // from class: com.ileja.aibase.common.net.NetSignalChangeObserver.1
        private boolean hasService() {
            if (NetSignalChangeObserver.this.mServiceState != null) {
                int state = NetSignalChangeObserver.this.mServiceState.getState();
                if (state == 1 || state == 2) {
                    if (NetSignalChangeObserver.this.mServiceState.getState() == 0) {
                        return true;
                    }
                } else if (state != 3) {
                    return true;
                }
            }
            return false;
        }

        private int invokeMethod(Object obj, String str) {
            try {
                try {
                    return ((Integer) obj.getClass().getMethod(str, new Class[0]).invoke(obj, new Object[0])).intValue();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return 0;
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    return 0;
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    return 0;
                }
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
                return -1;
            } catch (Exception e5) {
                e5.printStackTrace();
                return -1;
            }
        }

        private synchronized void updateTelephonySignalStrength() {
            int i = NetSignalChangeObserver.this.mMobileNetWorkClass;
            int iInvokeMethod = NetSignalChangeObserver.this.mMobileLevel;
            boolean z = false;
            if (!hasService() && NetSignalChangeObserver.this.mServiceState != null && NetSignalChangeObserver.this.mServiceState.getState() != 0) {
                AILog.m4028d(NetSignalChangeObserver.TAG, "updateTelephonySignalStrength: No Service");
                NetSignalChangeObserver.this.mMobileLevel = 0;
                NetSignalChangeObserver.this.mMobileNetWorkClass = 0;
            } else if (NetSignalChangeObserver.this.mMobileSignalStrength == null || NetSignalChangeObserver.this.mServiceState == null) {
                AILog.m4028d(NetSignalChangeObserver.TAG, "updateTelephonySignalStrength: mMobileSignalStrength == null mServiceState == null");
                NetSignalChangeObserver.this.mMobileLevel = 0;
                NetSignalChangeObserver.this.mMobileNetWorkClass = 0;
            } else {
                NetSignalChangeObserver.this.mMobileNetWorkClass = TelephonyManagerImpl.getNetworkClass(invokeMethod(NetSignalChangeObserver.this.mServiceState, "getDataNetworkType"));
            }
            if (NetSignalChangeObserver.this.mMobileSignalStrength != null) {
                iInvokeMethod = invokeMethod(NetSignalChangeObserver.this.mMobileSignalStrength, "getLevel");
            }
            if (NetSignalChangeObserver.this.mMobileLevel != iInvokeMethod || i != NetSignalChangeObserver.this.mMobileNetWorkClass) {
                NetSignalChangeObserver.this.mMobileLevel = iInvokeMethod;
                z = true;
            }
            if (NetSignalChangeObserver.PRINT_LOG) {
                if (NetSignalChangeObserver.this.mMobileNetWorkClass != 0) {
                    AILog.m4038v(NetSignalChangeObserver.TAG, "network class: " + (NetSignalChangeObserver.this.mMobileNetWorkClass + 1) + "G , Level:" + NetSignalChangeObserver.this.mMobileLevel);
                } else {
                    AILog.m4038v(NetSignalChangeObserver.TAG, "network class: unknow , Level:" + NetSignalChangeObserver.this.mMobileLevel);
                }
            }
            if (z) {
                NetSignalChangeObserver.this.callback(NetSignalChangeObserver.this.mMobileNetWorkClass, NetSignalChangeObserver.this.mMobileLevel);
            }
        }

        @Override // android.telephony.PhoneStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            NetSignalChangeObserver.this.mServiceState = serviceState;
            updateTelephonySignalStrength();
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            NetSignalChangeObserver.this.mMobileSignalStrength = signalStrength;
            updateTelephonySignalStrength();
        }
    };
    private final BroadcastReceiver mWifiRssiReceiver = new BroadcastReceiver() { // from class: com.ileja.aibase.common.net.NetSignalChangeObserver.2
        private String TAG = "ConnectivityReceiver";

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra;
            if ("android.net.wifi.RSSI_CHANGED".equals(intent.getAction())) {
                intExtra = intent.getIntExtra("newRssi", MusicQueryConfig.ErrCode.RESPONSE_DATA_IS_NULL);
                AILog.m4030e(this.TAG, "RSSI_CHANGED_ACTION, wifiRssi:" + intExtra);
            } else {
                intExtra = 0;
            }
            int iCalculateSignalLevel = WifiManager.calculateSignalLevel(intExtra, 4);
            NetSignalChangeObserver netSignalChangeObserver = NetSignalChangeObserver.this;
            if (netSignalChangeObserver.mWifiLevel != iCalculateSignalLevel) {
                netSignalChangeObserver.mWifiLevel = iCalculateSignalLevel;
                netSignalChangeObserver.callbackWifiSignal(iCalculateSignalLevel);
            }
        }
    };

    public interface SignalChangeListener {
        void wapSignalChange(int i, int i2);

        void wifiSignalChange(int i);
    }

    public static class TelephonyManagerImpl {
        public static final int NETWORK_CLASS_2_G = 1;
        public static final int NETWORK_CLASS_3_G = 2;
        public static final int NETWORK_CLASS_4_G = 3;
        public static final int NETWORK_CLASS_UNKNOWN = 0;

        public static int getNetworkClass(int i) {
            switch (i) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    return 1;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return 2;
                case 13:
                    return 3;
                default:
                    return 0;
            }
        }
    }

    public NetSignalChangeObserver(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callback(int i, int i2) {
        if (this.mSignalChangeListener != null) {
            AILog.m4038v(TAG, "callback:" + (i + 1) + "G , Level:" + i2);
            this.mSignalChangeListener.wapSignalChange(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callbackWifiSignal(int i) {
        if (this.mSignalChangeListener != null) {
            AILog.m4038v(TAG, "callback wifiSignal, Level:" + i);
            this.mSignalChangeListener.wifiSignalChange(i);
        }
    }

    public void start(SignalChangeListener signalChangeListener) {
        this.mSignalChangeListener = signalChangeListener;
        if (this.mTelephonyManager == null) {
            TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            this.mTelephonyManager = telephonyManager;
            telephonyManager.listen(this.mPhoneStateListenerImpl, InputDeviceCompat.SOURCE_KEYBOARD);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.RSSI_CHANGED");
            this.mContext.registerReceiver(this.mWifiRssiReceiver, intentFilter);
        }
    }

    public void stop() {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            telephonyManager.listen(this.mPhoneStateListenerImpl, 0);
            this.mTelephonyManager = null;
            this.mContext.unregisterReceiver(this.mWifiRssiReceiver);
        }
        this.mSignalChangeListener = null;
    }
}