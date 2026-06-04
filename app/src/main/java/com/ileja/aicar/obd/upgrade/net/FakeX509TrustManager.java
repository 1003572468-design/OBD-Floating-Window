package com.ileja.aicar.obd.upgrade.net;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* JADX INFO: loaded from: classes.dex */
public class FakeX509TrustManager implements X509TrustManager {
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[0];
    private static TrustManager[] trustManagers;

    public static void allowAllSSL() {
        SSLContext sSLContext;
        NoSuchAlgorithmException e;
        KeyManagementException e2;
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { // from class: com.ileja.aicar.obd.upgrade.net.FakeX509TrustManager.1
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }
        });
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new FakeX509TrustManager()};
        }
        try {
            sSLContext = SSLContext.getInstance("TLS");
            try {
                sSLContext.init(null, trustManagers, new SecureRandom());
            } catch (KeyManagementException e3) {
                e2 = e3;
                e2.printStackTrace();
            } catch (NoSuchAlgorithmException e4) {
                e = e4;
                e.printStackTrace();
            }
        } catch (KeyManagementException e5) {
            sSLContext = null;
            e2 = e5;
        } catch (NoSuchAlgorithmException e6) {
            sSLContext = null;
            e = e6;
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }

    public boolean isClientTrusted(X509Certificate[] x509CertificateArr) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] x509CertificateArr) {
        return true;
    }
}