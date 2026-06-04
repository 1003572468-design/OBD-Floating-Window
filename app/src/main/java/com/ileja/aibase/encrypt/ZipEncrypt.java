package com.ileja.aibase.encrypt;

import android.os.Environment;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.swiftp.Defaults;

/* JADX INFO: loaded from: classes.dex */
public class ZipEncrypt {
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    private static void decrypt(String str, String str2, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, key);
        FileInputStream fileInputStream = new FileInputStream(str);
        FileOutputStream fileOutputStream = new FileOutputStream(str2);
        byte[] bArr = new byte[64];
        while (fileInputStream.read(bArr) != -1) {
            fileOutputStream.write(cipher.doFinal(bArr));
        }
        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void decryptUnzip(String str, String str2, String str3) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        File file = new File(Environment.getExternalStorageDirectory() + "/autonavi/uploadlog" + UUID.randomUUID().toString() + ".zip");
        file.deleteOnExit();
        privateKey = (PrivateKey) getKey(str3);
        decrypt(str, file.getAbsolutePath(), privateKey);
        unZip(str2, file.getAbsolutePath());
        file.delete();
    }

    private static void directoryZip(ZipOutputStream zipOutputStream, File file, String str, FileFilter fileFilter) throws IOException {
        if (file.isDirectory()) {
            File[] fileArrListFiles = file.listFiles(fileFilter);
            zipOutputStream.putNextEntry(new ZipEntry(str + Defaults.chrootDir));
            String str2 = str.length() == 0 ? "" : str + Defaults.chrootDir;
            for (int i = 0; i < fileArrListFiles.length; i++) {
                directoryZip(zipOutputStream, fileArrListFiles[i], str2 + fileArrListFiles[i].getName(), fileFilter);
            }
            return;
        }
        zipOutputStream.putNextEntry(new ZipEntry(str));
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[2048];
        while (true) {
            int i2 = fileInputStream.read(bArr);
            if (i2 == -1) {
                fileInputStream.close();
                return;
            }
            zipOutputStream.write(bArr, 0, i2);
        }
    }

    private static void encrypt(File file, String str, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, key);
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        byte[] bArr = new byte[53];
        while (fileInputStream.read(bArr) != -1) {
            fileOutputStream.write(cipher.doFinal(bArr));
        }
        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void encryptZip(String str, String str2, String str3) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (new File(str3).exists()) {
            publicKey = (PublicKey) getKey(str3);
            File file = new File(str + Defaults.chrootDir + UUID.randomUUID().toString() + ".zip");
            file.deleteOnExit();
            zip(str, file);
            encrypt(file, str2, publicKey);
            file.delete();
        }
    }

    private static void fileUnZip(ZipInputStream zipInputStream, File file) throws IOException {
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        while (nextEntry != null) {
            File file2 = new File(file.getAbsolutePath() + Defaults.chrootDir + nextEntry.getName());
            if (nextEntry.isDirectory()) {
                file2.mkdirs();
                fileUnZip(zipInputStream, file);
            } else {
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                byte[] bArr = new byte[2048];
                while (true) {
                    int i = zipInputStream.read(bArr);
                    if (i == -1) {
                        break;
                    } else {
                        fileOutputStream.write(bArr, 0, i);
                    }
                }
                fileOutputStream.close();
            }
            nextEntry = zipInputStream.getNextEntry();
        }
    }

    private static void fileZip(ZipOutputStream zipOutputStream, File file) throws IOException {
        if (!file.isFile()) {
            directoryZip(zipOutputStream, file, "", new FileFilter() { // from class: com.ileja.aibase.encrypt.ZipEncrypt.1
                @Override // java.io.FileFilter
                public boolean accept(File file2) {
                    return (file2.getName().endsWith(".zip") || file2.getName().equals("publickey") || file2.getName().equals("privatekey")) ? false : true;
                }
            });
            return;
        }
        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[2048];
        while (true) {
            int i = fileInputStream.read(bArr);
            if (i == -1) {
                fileInputStream.close();
                return;
            }
            zipOutputStream.write(bArr, 0, i);
        }
    }

    public static Key getKey(String str) {
        return (Key) new ObjectInputStream(new FileInputStream(str)).readObject();
    }

    public static void unZip(String str, String str2) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str2));
            File file = new File(str);
            file.mkdirs();
            fileUnZip(zipInputStream, file);
            zipInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void zip(String str, File file) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
            fileZip(zipOutputStream, new File(str));
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}