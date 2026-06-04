package com.ileja.aibase.common;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.swiftp.Defaults;

/* JADX INFO: loaded from: classes.dex */
public class AssetsFileUtils {
    static final int BUFF_SIZE = 524288;
    public static byte[] MAGIC = {80, 75, 3, 4};
    private static final String TAG = "AssetsFileUtils";

    public static boolean checkMD5(InputStream inputStream, File file) {
        boolean z;
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] fileMD5String = getFileMD5String(fileInputStream);
                byte[] fileMD5String2 = getFileMD5String(inputStream);
                int length = fileMD5String.length > fileMD5String2.length ? fileMD5String2.length : fileMD5String.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = true;
                        break;
                    }
                    if (fileMD5String[i] != fileMD5String2[i]) {
                        z = false;
                        break;
                    }
                    i++;
                }
                fileInputStream.close();
                return z;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    public static void copyAssetsFile(AssetManager assetManager, String str, String str2) {
        try {
            String[] list = assetManager.list(str);
            if (list.length > 0) {
                new File(str2).mkdirs();
                for (String str3 : list) {
                    copyAssetsFile(assetManager, str + Defaults.chrootDir + str3, str2 + Defaults.chrootDir + str3);
                }
                return;
            }
            InputStream inputStreamOpen = assetManager.open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str2));
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStreamOpen.read(bArr);
                if (i == -1) {
                    fileOutputStream.flush();
                    inputStreamOpen.close();
                    fileOutputStream.close();
                    return;
                }
                fileOutputStream.write(bArr, 0, i);
            }
        } catch (Exception e) {
            AILog.m4032e(TAG, "catch assetFileName : " + str + " , err:", e);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(17:10|83|11|86|12|13|(3:89|15|94)|25|(1:27)|(4:75|28|29|(2:72|30))|(4:31|(1:33)(1:93)|74|56)|34|77|35|39|74|56) */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x008e, code lost:
    
        r8 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008f, code lost:
    
        r8.printStackTrace();
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:55:0x00b1 -> B:74:0x00b4). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void copyAssetsFileMD5Verify(android.content.res.AssetManager r8, java.lang.String r9, java.lang.String r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 204
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ileja.aibase.common.AssetsFileUtils.copyAssetsFileMD5Verify(android.content.res.AssetManager, java.lang.String, java.lang.String):void");
    }

    public static synchronized int copyResource(Context context, String str, boolean z) {
        if (context == null) {
            return -1;
        }
        try {
            InputStream inputStreamOpen = context.getAssets().open(str);
            File file = new File(Util.getResourceDir(context), str);
            if (z && checkMD5(inputStreamOpen, file)) {
                try {
                    inputStreamOpen.close();
                    return 0;
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
            try {
                inputStreamOpen.reset();
            } catch (IOException e2) {
                try {
                    e2.printStackTrace();
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                    return -1;
                }
            }
            FileOutputStream fileOutputStreamOpenFileOutput = context.openFileOutput(str, 0);
            byte[] bArr = new byte[524288];
            while (true) {
                try {
                    int i = inputStreamOpen.read(bArr);
                    if (i == -1) {
                        break;
                    }
                    fileOutputStreamOpenFileOutput.write(bArr, 0, i);
                } catch (IOException e4) {
                    e4.printStackTrace();
                    return -1;
                }
            }
            fileOutputStreamOpenFileOutput.close();
            inputStreamOpen.close();
            if (isZipFile(context.getFileStreamPath(str))) {
                unZip(context, file);
            }
            return 1;
        } catch (IOException unused) {
            AILog.m4030e(TAG, "file " + str + " not found in assest folder, Did you forget add it?");
            return -1;
        }
    }

    public static String format(String str) {
        return str.replaceAll("。.", ".").replaceAll("[!！？~]", "").replaceAll("(…{2})", "").replaceAll("(\\.{6})", "");
    }

    private static byte[] getFileMD5String(InputStream inputStream) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bArr = new byte[524288];
            while (true) {
                int i = inputStream.read(bArr);
                if (i == -1) {
                    return messageDigest.digest();
                }
                messageDigest.update(bArr, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return new byte[0];
        }
    }

    public static String getResourceDir(Context context) {
        if (context == null) {
            return null;
        }
        return context.getFilesDir().getAbsolutePath();
    }

    public static boolean isZipFile(File file) {
        boolean z;
        byte[] bArr = new byte[MAGIC.length];
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.readFully(bArr);
            int i = 0;
            while (true) {
                if (i >= MAGIC.length) {
                    z = true;
                    break;
                }
                if (bArr[i] != MAGIC[i]) {
                    z = false;
                    break;
                }
                i++;
            }
            randomAccessFile.close();
            return z;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void unZip(Context context, File file) {
        byte[] bArr = new byte[524288];
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
            while (enumerationEntries.hasMoreElements()) {
                ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
                if (zipEntryNextElement.isDirectory()) {
                    new File(Util.getResourceDir(context), zipEntryNextElement.getName()).mkdirs();
                } else {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntryNextElement));
                    AILog.m4028d(TAG, format(zipEntryNextElement.getName()));
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(getResourceDir(context), format(zipEntryNextElement.getName()))), 524288);
                    while (true) {
                        int i = bufferedInputStream.read(bArr, 0, 524288);
                        if (i == -1) {
                            break;
                        } else {
                            bufferedOutputStream.write(bArr, 0, i);
                        }
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}