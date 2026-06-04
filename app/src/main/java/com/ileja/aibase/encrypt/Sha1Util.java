package com.ileja.aibase.encrypt;

import com.aispeech.aios.TtsSupportNode;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public class Sha1Util {
    private static String byte2Hex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                stringBuffer.append(TtsSupportNode.NOREVERSE);
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString();
    }

    public static String sha1(String str) {
        byte[] bArrDigest = MessageDigest.getInstance("SHA1").digest(str.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArrDigest) {
            stringBuffer.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    public static String sha256StrJava(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            return byte2Hex(messageDigest.digest());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return "";
        }
    }
}