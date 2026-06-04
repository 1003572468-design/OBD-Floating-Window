package com.ileja.aibase.phone;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.TimerCheck;
import com.ileja.aibase.config.HttpConfig;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import org.apache.http.conn.util.InetAddressUtils;

/* JADX INFO: loaded from: classes.dex */
public class WifiUtils {
    public static final int ApCreateApSuccess = 1;
    private static final String TAG = "SZU_WifiUtils";
    private static volatile WifiUtils inst;
    private Application mContext;
    private WifiManager mWifiManager;

    public enum WifiCipherType {
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID
    }

    private WifiUtils(Application application) {
        this.mContext = application;
        this.mWifiManager = (WifiManager) application.getSystemService(HttpConfig.TAG.SET_WIFI);
    }

    private WifiConfiguration createWifiInfo(String str, String str2, WifiCipherType wifiCipherType) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + str + "\"";
        if (wifiCipherType == WifiCipherType.WIFICIPHER_NOPASS) {
            wifiConfiguration.wepKeys[0] = "";
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        }
        if (wifiCipherType == WifiCipherType.WIFICIPHER_WEP) {
            wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedGroupCiphers.set(0);
            wifiConfiguration.allowedGroupCiphers.set(1);
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        }
        if (wifiCipherType != WifiCipherType.WIFICIPHER_WPA) {
            return null;
        }
        wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
        wifiConfiguration.hiddenSSID = true;
        wifiConfiguration.allowedAuthAlgorithms.set(0);
        wifiConfiguration.allowedGroupCiphers.set(2);
        wifiConfiguration.allowedKeyManagement.set(1);
        wifiConfiguration.allowedPairwiseCiphers.set(1);
        wifiConfiguration.allowedGroupCiphers.set(3);
        wifiConfiguration.allowedPairwiseCiphers.set(2);
        return wifiConfiguration;
    }

    public static WifiUtils getInst(Application application) {
        if (inst == null) {
            synchronized (WifiUtils.class) {
                if (inst == null) {
                    inst = new WifiUtils(application);
                }
            }
        }
        return inst;
    }

    private static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    private WifiConfiguration isExsits(String str) {
        for (WifiConfiguration wifiConfiguration : this.mWifiManager.getConfiguredNetworks()) {
            if (wifiConfiguration.SSID.equals("\"" + str + "\"")) {
                return wifiConfiguration;
            }
        }
        return null;
    }

    private void startAp(String str, String str2) {
        try {
            Method method = this.mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = str;
            wifiConfiguration.preSharedKey = str2;
            wifiConfiguration.allowedAuthAlgorithms.set(0);
            wifiConfiguration.allowedProtocols.set(1);
            wifiConfiguration.allowedProtocols.set(0);
            wifiConfiguration.allowedKeyManagement.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(2);
            wifiConfiguration.allowedPairwiseCiphers.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedGroupCiphers.set(2);
            method.invoke(this.mWifiManager, wifiConfiguration, Boolean.TRUE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        } catch (SecurityException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
    }

    public void OpenWifi() {
        if (this.mWifiManager.isWifiEnabled()) {
            return;
        }
        this.mWifiManager.setWifiEnabled(true);
    }

    public void addNetwork(WifiConfiguration wifiConfiguration) {
        this.mWifiManager.enableNetwork(this.mWifiManager.addNetwork(wifiConfiguration), true);
    }

    public void closeWifi() {
    }

    public void closeWifiAp() {
        if (isWifiApEnabled()) {
            try {
                Method method = this.mWifiManager.getClass().getMethod("getWifiApConfiguration", new Class[0]);
                method.setAccessible(true);
                this.mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE).invoke(this.mWifiManager, (WifiConfiguration) method.invoke(this.mWifiManager, new Object[0]), Boolean.FALSE);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
    }

    public boolean connectWifi(String str, String str2, WifiCipherType wifiCipherType) {
        if (!isWifiEnabled()) {
            return false;
        }
        while (this.mWifiManager.getWifiState() == 2) {
            try {
                Thread.currentThread();
                Thread.sleep(500L);
            } catch (InterruptedException unused) {
            }
        }
        WifiConfiguration wifiConfigurationCreateWifiInfo = createWifiInfo(str, str2, wifiCipherType);
        if (wifiConfigurationCreateWifiInfo == null) {
            return false;
        }
        WifiConfiguration wifiConfigurationIsExsits = isExsits(str);
        if (wifiConfigurationIsExsits != null) {
            this.mWifiManager.removeNetwork(wifiConfigurationIsExsits.networkId);
        }
        int iAddNetwork = this.mWifiManager.addNetwork(wifiConfigurationCreateWifiInfo);
        this.mWifiManager.disconnect();
        boolean zEnableNetwork = this.mWifiManager.enableNetwork(iAddNetwork, true);
        this.mWifiManager.reconnect();
        return zEnableNetwork;
    }

    public void disconnectWifi(int i) {
        this.mWifiManager.disableNetwork(i);
    }

    public String getApSSID() {
        Object objInvoke;
        try {
            Method declaredMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
            if (declaredMethod == null || (objInvoke = declaredMethod.invoke(this.mWifiManager, new Object[0])) == null) {
                return null;
            }
            WifiConfiguration wifiConfiguration = (WifiConfiguration) objInvoke;
            if (wifiConfiguration.SSID != null) {
                return wifiConfiguration.SSID;
            }
            Field declaredField = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
            if (declaredField == null) {
                return null;
            }
            declaredField.setAccessible(true);
            Object obj = declaredField.get(wifiConfiguration);
            declaredField.setAccessible(false);
            if (obj == null) {
                return null;
            }
            Field declaredField2 = obj.getClass().getDeclaredField("SSID");
            declaredField2.setAccessible(true);
            Object obj2 = declaredField2.get(obj);
            if (obj2 == null) {
                return null;
            }
            declaredField2.setAccessible(false);
            return (String) obj2;
        } catch (Exception unused) {
            return null;
        }
    }

    public String getBSSID() {
        return this.mWifiManager.getConnectionInfo().getBSSID();
    }

    public String getBroadcastAddress() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterfaceNextElement = networkInterfaces.nextElement();
                if (!networkInterfaceNextElement.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : networkInterfaceNextElement.getInterfaceAddresses()) {
                        if (interfaceAddress.getBroadcast() != null) {
                            AILog.m4028d(TAG, interfaceAddress.getBroadcast().toString().substring(1));
                            return interfaceAddress.getBroadcast().toString().substring(1);
                        }
                    }
                }
            }
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getGPRSLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddressNextElement.getHostAddress())) {
                        return inetAddressNextElement.getHostAddress();
                    }
                }
            }
            return null;
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getLocalIPAddress() {
        return intToIp(this.mWifiManager.getConnectionInfo().getIpAddress());
    }

    public String getMacAddress() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? "NULL" : connectionInfo.getMacAddress();
    }

    public int getNetworkId() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return 0;
        }
        return connectionInfo.getNetworkId();
    }

    public String getSSID() {
        return this.mWifiManager.getConnectionInfo().getSSID();
    }

    public List<ScanResult> getScanResults() {
        return this.mWifiManager.getScanResults();
    }

    public String getServerIPAddress() {
        return intToIp(this.mWifiManager.getDhcpInfo().gateway);
    }

    public int getWifiApStateInt() {
        try {
            return ((Integer) this.mWifiManager.getClass().getMethod("getWifiApState", new Class[0]).invoke(this.mWifiManager, new Object[0])).intValue();
        } catch (Exception unused) {
            return 4;
        }
    }

    public WifiInfo getWifiInfo() {
        return this.mWifiManager.getConnectionInfo();
    }

    public boolean isWifiApEnabled() {
        try {
            Method method = this.mWifiManager.getClass().getMethod("isWifiApEnabled", new Class[0]);
            method.setAccessible(true);
            return ((Boolean) method.invoke(this.mWifiManager, new Object[0])).booleanValue();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public boolean isWifiConnect() {
        return ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getNetworkInfo(1).isConnected();
    }

    public boolean isWifiEnabled() {
        return this.mWifiManager.isWifiEnabled();
    }

    public void removeNetwork(int i) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.removeNetwork(i);
            this.mWifiManager.saveConfiguration();
        }
    }

    public void startScan() {
        this.mWifiManager.startScan();
    }

    public void startWifiAp(String str, String str2, final Handler handler) {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
        startAp(str, str2);
        new TimerCheck() { // from class: com.ileja.aibase.phone.WifiUtils.1
            @Override // com.ileja.aibase.common.TimerCheck
            public void doTimeOutWork() {
                exit();
            }

            @Override // com.ileja.aibase.common.TimerCheck
            public void doTimerCheckWork() {
                if (WifiUtils.this.isWifiApEnabled()) {
                    handler.sendMessage(handler.obtainMessage(1));
                    exit();
                }
            }
        }.start(10, 1000);
    }
}