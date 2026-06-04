package com.ileja.aibase.encrypt;

import android.util.Log;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: loaded from: classes.dex */
public class AES {
    static final String algorithmStr = "AES/ECB/PKCS5Padding";
    private static Cipher cipher = null;
    static boolean isInited = false;
    private static final String keyBytes = "lejacarrobot2022";
    private static KeyGenerator keyGen;

    static {
        init();
    }

    public static String decode(String str) {
        return new String(decrypt(parseHexStr2Byte(str), keyBytes));
    }

    private static byte[] decrypt(byte[] bArr, String str) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getKey(str), "AES");
            Cipher cipher2 = Cipher.getInstance(algorithmStr);
            cipher2.init(2, secretKeySpec);
            return cipher2.doFinal(bArr);
        } catch (Exception e) {
            Log.e("AES", "AES_decrypt_error", e);
            return null;
        }
    }

    public static String encode(String str) {
        return parseByte2HexStr(encrypt(str, keyBytes));
    }

    private static byte[] encrypt(byte[] bArr, byte[] bArr2) {
        if (!isInited) {
            init();
        }
        try {
            cipher.init(1, new SecretKeySpec(bArr2, "AES"));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            return cipher.doFinal(bArr);
        } catch (BadPaddingException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    private static byte[] genKey() {
        if (!isInited) {
            init();
        }
        return keyGen.generateKey().getEncoded();
    }

    private static byte[] getKey(String str) {
        return str != null ? str.getBytes() : new byte[24];
    }

    private static void init() {
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(128);
        try {
            cipher = Cipher.getInstance(algorithmStr);
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        } catch (NoSuchPaddingException e3) {
            e3.printStackTrace();
        }
        isInited = true;
    }

    public static String parseByte2HexStr(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            stringBuffer.append(hexString.toUpperCase());
        }
        return stringBuffer.toString();
    }

    public static byte[] parseHexStr2Byte(String str) {
        if (str.length() < 1) {
            return null;
        }
        byte[] bArr = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            int i2 = i * 2;
            int i3 = i2 + 1;
            bArr[i] = (byte) ((Integer.parseInt(str.substring(i2, i3), 16) * 16) + Integer.parseInt(str.substring(i3, i2 + 2), 16));
        }
        return bArr;
    }

    private static byte[] encrypt(String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getKey(str2), "AES");
            Cipher cipher2 = Cipher.getInstance(algorithmStr);
            byte[] bytes = str.getBytes("utf-8");
            cipher2.init(1, secretKeySpec);
            return cipher2.doFinal(bytes);
        } catch (Exception e) {
            Log.e("AES", "AES_encrypt_error", e);
            return null;
        }
    }
}