package com.ileja.aibase.common.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.ileja.aibase.common.AILog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class NetWorkMonitorManager {
    protected static final String TAG = "NetWorkMonitorManager";
    private static volatile NetWorkMonitorManager mInstance;
    private boolean isRegisteredNetWorkReceiver;
    private List<NetWorkListener> mListeners = new ArrayList();
    private boolean mIsWifiConnected = false;
    private boolean mIsMobileConnected = false;
    public final BroadcastReceiver ConnectivityCheckReceiver = new BroadcastReceiver() { // from class: com.ileja.aibase.common.net.NetWorkMonitorManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                boolean z = false;
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
                boolean z2 = true;
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(1);
                boolean zIsConnected = networkInfo2 != null ? networkInfo2.isConnected() : false;
                boolean zIsConnected2 = networkInfo != null ? networkInfo.isConnected() : false;
                if (NetWorkMonitorManager.this.mIsWifiConnected != zIsConnected) {
                    NetWorkMonitorManager.this.mIsWifiConnected = zIsConnected;
                    z = true;
                }
                if (NetWorkMonitorManager.this.mIsMobileConnected != zIsConnected2) {
                    NetWorkMonitorManager.this.mIsMobileConnected = zIsConnected2;
                } else {
                    z2 = z;
                }
                if (z2) {
                    if (NetWorkMonitorManager.this.mIsWifiConnected || NetWorkMonitorManager.this.mIsMobileConnected) {
                        NetWorkMonitorManager.this.notifyConnectedListener();
                    } else {
                        NetWorkMonitorManager.this.notifyDisConnectedListener();
                    }
                }
                AILog.m4028d(NetWorkMonitorManager.TAG, "changed:" + z2 + " , isWifiConnected:" + NetWorkMonitorManager.this.mIsWifiConnected + " , isMobileConnected:" + NetWorkMonitorManager.this.mIsMobileConnected);
            }
        }
    };

    public interface NetWorkListener {

        public enum NetState {
            NO_CONNECT,
            MOBILE_CONNECT,
            WIFI_CONNECT
        }

        void onConnected(NetState netState);

        void onDisConnected();
    }

    private NetWorkMonitorManager() {
    }

    public static NetWorkMonitorManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkMonitorManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkMonitorManager();
                }
            }
        }
        return mInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnectedListener() {
        for (NetWorkListener netWorkListener : this.mListeners) {
            if (netWorkListener != null) {
                if (this.mIsMobileConnected) {
                    netWorkListener.onConnected(NetWorkListener.NetState.MOBILE_CONNECT);
                } else if (this.mIsWifiConnected) {
                    netWorkListener.onConnected(NetWorkListener.NetState.WIFI_CONNECT);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDisConnectedListener() {
        for (NetWorkListener netWorkListener : this.mListeners) {
            if (netWorkListener != null) {
                netWorkListener.onDisConnected();
            }
        }
    }

    private void registerNetWorkReceiver(Context context) {
        if (this.isRegisteredNetWorkReceiver) {
            return;
        }
        AILog.m4028d(TAG, "registerNetWorkReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        context.getApplicationContext().registerReceiver(this.ConnectivityCheckReceiver, intentFilter);
        this.isRegisteredNetWorkReceiver = true;
    }

    private void unregisterNetWorkReceiver(Context context) {
        if (this.isRegisteredNetWorkReceiver) {
            AILog.m4028d(TAG, "unregisterNetWorkReceiver");
            if (context != null) {
                context.getApplicationContext().unregisterReceiver(this.ConnectivityCheckReceiver);
            }
            this.isRegisteredNetWorkReceiver = false;
        }
    }

    public void init(Context context) {
        registerNetWorkReceiver(context);
    }

    public boolean isMobileConnected(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnected() || activeNetworkInfo.getType() != 0) {
            return false;
        }
        AILog.m4034i(TAG, "TYPE_MOBILE CONNECTED");
        return true;
    }

    public boolean isNetWorkConnected(Context context) {
        return isWifiConnected(context) || isMobileConnected(context);
    }

    public boolean isWifiConnected(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnected() || activeNetworkInfo.getType() != 1) {
            return false;
        }
        AILog.m4034i(TAG, "TYPE_WIFI CONNECTED");
        return true;
    }

    public void registerListener(NetWorkListener netWorkListener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(netWorkListener)) {
                this.mListeners.add(netWorkListener);
            }
        }
    }

    public void release(Context context) {
        unregisterNetWorkReceiver(context);
    }

    public void unregisterListener(NetWorkListener netWorkListener) {
        synchronized (this.mListeners) {
            if (this.mListeners.contains(netWorkListener)) {
                this.mListeners.remove(netWorkListener);
            }
        }
    }
}