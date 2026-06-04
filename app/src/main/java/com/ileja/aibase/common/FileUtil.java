package com.ileja.aibase.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static File cachedStorageDir;
    private static ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();

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

    public static void deleteAllFiles(File file) {
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null) {
            for (File file2 : fileArrListFiles) {
                if (file2.isDirectory()) {
                    deleteAllFiles(file2);
                    try {
                        file2.delete();
                    } catch (Exception unused) {
                    }
                } else if (file2.exists()) {
                    file2.delete();
                }
            }
        }
    }

    public static boolean deleteFile(String str) {
        File file = new File(str);
        if (!file.isFile() || !file.exists()) {
            return false;
        }
        file.delete();
        return true;
    }

    public static String getCacheAbsolutePath(Context context) {
        File externalCacheDir = "mounted".equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context) : context.getCacheDir();
        if (externalCacheDir == null) {
            externalCacheDir = context.getCacheDir();
        }
        return externalCacheDir.getPath() + File.separator;
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        if (cachedStorageDir == null && "mounted".equals(Environment.getExternalStorageState())) {
            cachedStorageDir = context.getExternalCacheDir();
        }
        return cachedStorageDir;
    }

    public static byte[] getFileMD5String(InputStream inputStream) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bArr = new byte[102400];
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

    public static File getOutputMediaFile(Context context) {
        if (context == null) {
            return null;
        }
        String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String storagePath = getStoragePath(context, true);
        if (TextUtils.isEmpty(storagePath)) {
            storagePath = getStoragePath(context, false);
        }
        File file = new File(storagePath + File.separator + "carrobotVideoCamera1");
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(file.getAbsolutePath() + File.separator + "VID_" + str + ".mp4");
    }

    public static ExecutorService getSaveFileThreadPool() {
        return mSingleThreadExecutor;
    }

    public static String getStoragePath(Context context, boolean z) {
        StorageManager storageManager = (StorageManager) context.getSystemService("storage");
        try {
            Class<?> cls = Class.forName("android.os.storage.StorageVolume");
            Method method = storageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method method2 = cls.getMethod("getPath", new Class[0]);
            Method method3 = cls.getMethod("isRemovable", new Class[0]);
            Object objInvoke = method.invoke(storageManager, new Object[0]);
            int length = Array.getLength(objInvoke);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(objInvoke, i);
                String str = (String) method2.invoke(obj, new Object[0]);
                if (z == ((Boolean) method3.invoke(obj, new Object[0])).booleanValue()) {
                    return str;
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static boolean isExits(String str) {
        return new File(str).exists();
    }

    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        return Environment.isExternalStorageRemovable();
    }

    public static void prepareNomediaDir(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(str, ".nomedia");
        if (file2.exists()) {
            return;
        }
        try {
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readFile(File file) throws Throwable {
        FileInputStream fileInputStream = null;
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        int length = (int) file.length();
        byte[] bArr = new byte[length];
        try {
            try {
                try {
                    FileInputStream fileInputStream2 = new FileInputStream(file);
                    try {
                        fileInputStream2.read(bArr, 0, length);
                        fileInputStream2.close();
                    } catch (Exception e) {
                        e = e;
                        fileInputStream = fileInputStream2;
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return bArr;
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        return bArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x005d -> B:59:0x0060). Please report as a decompilation issue!!! */
    public static String readFileContent(String str) throws Throwable {
        FileReader fileReader;
        Exception e;
        BufferedReader bufferedReader;
        File file = new File(str);
        ?? r1 = 0;
        r1 = 0;
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            try {
                try {
                    fileReader = new FileReader(file);
                    try {
                        bufferedReader = new BufferedReader(fileReader);
                        while (true) {
                            try {
                                String line = bufferedReader.readLine();
                                if (line != null) {
                                    sb.append(line);
                                } else {
                                    try {
                                        break;
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            } catch (Exception e3) {
                                e = e3;
                                e.printStackTrace();
                                if (bufferedReader != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (Exception e4) {
                                        e4.printStackTrace();
                                    }
                                }
                                if (fileReader != null) {
                                    fileReader.close();
                                }
                                return sb.toString();
                            }
                        }
                        bufferedReader.close();
                        fileReader.close();
                    } catch (Exception e5) {
                        e = e5;
                        bufferedReader = null;
                    } catch (Throwable th) {
                        th = th;
                        if (r1 != 0) {
                            try {
                                r1.close();
                            } catch (Exception e6) {
                                e6.printStackTrace();
                            }
                        }
                        if (fileReader == null) {
                            throw th;
                        }
                        try {
                            fileReader.close();
                            throw th;
                        } catch (Exception e7) {
                            e7.printStackTrace();
                            throw th;
                        }
                    }
                } catch (Exception e8) {
                    fileReader = null;
                    e = e8;
                    bufferedReader = null;
                } catch (Throwable th2) {
                    th = th2;
                    fileReader = null;
                }
            } catch (Exception e9) {
                e9.printStackTrace();
            }
            return sb.toString();
        } catch (Throwable th3) {
            th = th3;
            r1 = file;
        }
    }

    public static void saveStringToFile(final String str, final String str2, final boolean z) {
        mSingleThreadExecutor.submit(new Runnable() { // from class: com.ileja.aibase.common.FileUtil.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:69:0x0086 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:88:? A[Catch: all -> 0x00a9, SYNTHETIC, TryCatch #3 {, blocks: (B:3:0x0001, B:5:0x0009, B:17:0x0040, B:21:0x0048, B:24:0x004d, B:20:0x0045, B:51:0x007c, B:56:0x0086, B:60:0x008e, B:59:0x008b, B:54:0x0081, B:38:0x0067, B:43:0x0071, B:41:0x006c, B:61:0x008f), top: B:71:0x0001, inners: #2, #5, #7, #9 }] */
            /* JADX WARN: Type inference failed for: r0v13 */
            /* JADX WARN: Type inference failed for: r0v16 */
            /* JADX WARN: Type inference failed for: r0v18 */
            /* JADX WARN: Type inference failed for: r0v23 */
            /* JADX WARN: Type inference failed for: r0v25 */
            /* JADX WARN: Type inference failed for: r0v28 */
            /* JADX WARN: Type inference failed for: r0v29 */
            /* JADX WARN: Type inference failed for: r0v30 */
            /* JADX WARN: Type inference failed for: r0v31 */
            /* JADX WARN: Type inference failed for: r0v32 */
            /* JADX WARN: Type inference failed for: r0v6 */
            /* JADX WARN: Type inference failed for: r0v7 */
            /* JADX WARN: Type inference failed for: r0v8, types: [java.io.BufferedWriter] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public synchronized void run() {
                /*
                    r5 = this;
                    monitor-enter(r5)
                    java.lang.String r0 = r1     // Catch: java.lang.Throwable -> La9
                    boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Throwable -> La9
                    if (r0 != 0) goto L8f
                    java.lang.String r0 = r1     // Catch: java.lang.Throwable -> La9
                    java.lang.String r1 = "null\\/.*"
                    boolean r0 = r0.matches(r1)     // Catch: java.lang.Throwable -> La9
                    if (r0 == 0) goto L15
                    goto L8f
                L15:
                    r0 = 0
                    java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    java.lang.String r2 = r1     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    r1.<init>(r2)     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    java.io.File r2 = r1.getParentFile()     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    if (r2 == 0) goto L2c
                    boolean r3 = r2.exists()     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    if (r3 != 0) goto L2c
                    r2.mkdirs()     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                L2c:
                    java.io.FileWriter r2 = new java.io.FileWriter     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    boolean r3 = r2     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    r2.<init>(r1, r3)     // Catch: java.lang.Throwable -> L5d java.io.IOException -> L60
                    java.io.BufferedWriter r1 = new java.io.BufferedWriter     // Catch: java.io.IOException -> L5b java.lang.Throwable -> L79
                    r1.<init>(r2)     // Catch: java.io.IOException -> L5b java.lang.Throwable -> L79
                    java.lang.String r0 = r3     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L56
                    r1.write(r0)     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L56
                    r1.flush()     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L56
                    r1.close()     // Catch: java.io.IOException -> L44 java.lang.Throwable -> La9
                    goto L48
                L44:
                    r0 = move-exception
                    r0.printStackTrace()     // Catch: java.lang.Throwable -> La9
                L48:
                    r2.close()     // Catch: java.lang.Exception -> L4c java.lang.Throwable -> La9
                    goto L77
                L4c:
                    r0 = move-exception
                L4d:
                    r0.printStackTrace()     // Catch: java.lang.Throwable -> La9
                    goto L77
                L51:
                    r0 = move-exception
                    r4 = r1
                    r1 = r0
                    r0 = r4
                    goto L7a
                L56:
                    r0 = move-exception
                    r4 = r1
                    r1 = r0
                    r0 = r4
                    goto L62
                L5b:
                    r1 = move-exception
                    goto L62
                L5d:
                    r1 = move-exception
                    r2 = r0
                    goto L7a
                L60:
                    r1 = move-exception
                    r2 = r0
                L62:
                    r1.printStackTrace()     // Catch: java.lang.Throwable -> L79
                    if (r0 == 0) goto L6f
                    r0.close()     // Catch: java.io.IOException -> L6b java.lang.Throwable -> La9
                    goto L6f
                L6b:
                    r0 = move-exception
                    r0.printStackTrace()     // Catch: java.lang.Throwable -> La9
                L6f:
                    if (r2 == 0) goto L77
                    r2.close()     // Catch: java.lang.Exception -> L75 java.lang.Throwable -> La9
                    goto L77
                L75:
                    r0 = move-exception
                    goto L4d
                L77:
                    monitor-exit(r5)
                    return
                L79:
                    r1 = move-exception
                L7a:
                    if (r0 == 0) goto L84
                    r0.close()     // Catch: java.io.IOException -> L80 java.lang.Throwable -> La9
                    goto L84
                L80:
                    r0 = move-exception
                    r0.printStackTrace()     // Catch: java.lang.Throwable -> La9
                L84:
                    if (r2 == 0) goto L8e
                    r2.close()     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> La9
                    goto L8e
                L8a:
                    r0 = move-exception
                    r0.printStackTrace()     // Catch: java.lang.Throwable -> La9
                L8e:
                    throw r1     // Catch: java.lang.Throwable -> La9
                L8f:
                    java.lang.String r0 = "FileUtil"
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La9
                    r1.<init>()     // Catch: java.lang.Throwable -> La9
                    java.lang.String r2 = "The file to be saved is null! filePath:"
                    r1.append(r2)     // Catch: java.lang.Throwable -> La9
                    java.lang.String r2 = r1     // Catch: java.lang.Throwable -> La9
                    r1.append(r2)     // Catch: java.lang.Throwable -> La9
                    java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> La9
                    android.util.Log.w(r0, r1)     // Catch: java.lang.Throwable -> La9
                    monitor-exit(r5)
                    return
                La9:
                    r0 = move-exception
                    monitor-exit(r5)
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.ileja.aibase.common.FileUtil.RunnableC10781.run():void");
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x007b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0071 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:? A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:0x006a -> B:66:0x006d). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void syncSaveStringToFile(java.lang.String r4, java.lang.String r5, boolean r6) throws java.lang.Throwable {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 != 0) goto L84
            java.lang.String r0 = "null\\/.*"
            boolean r0 = r4.matches(r0)
            if (r0 == 0) goto L10
            goto L84
        L10:
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L54
            r1.<init>(r4)     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L54
            java.io.File r4 = r1.getParentFile()     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L54
            if (r4 == 0) goto L25
            boolean r2 = r4.exists()     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L54
            if (r2 != 0) goto L25
            r4.mkdirs()     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L54
        L25:
            java.io.FileWriter r4 = new java.io.FileWriter     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L54
            r4.<init>(r1, r6)     // Catch: java.lang.Throwable -> L51 java.io.IOException -> L54
            java.io.BufferedWriter r6 = new java.io.BufferedWriter     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4c
            r6.<init>(r4)     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L4c
            r6.write(r5)     // Catch: java.lang.Throwable -> L41 java.io.IOException -> L44
            r6.flush()     // Catch: java.lang.Throwable -> L41 java.io.IOException -> L44
            r6.close()     // Catch: java.io.IOException -> L39
            goto L3d
        L39:
            r5 = move-exception
            r5.printStackTrace()
        L3d:
            r4.close()     // Catch: java.lang.Exception -> L69
            goto L6d
        L41:
            r5 = move-exception
            r0 = r6
            goto L48
        L44:
            r5 = move-exception
            r0 = r6
            goto L4d
        L47:
            r5 = move-exception
        L48:
            r3 = r5
            r5 = r4
            r4 = r3
            goto L6f
        L4c:
            r5 = move-exception
        L4d:
            r3 = r5
            r5 = r4
            r4 = r3
            goto L56
        L51:
            r4 = move-exception
            r5 = r0
            goto L6f
        L54:
            r4 = move-exception
            r5 = r0
        L56:
            r4.printStackTrace()     // Catch: java.lang.Throwable -> L6e
            if (r0 == 0) goto L63
            r0.close()     // Catch: java.io.IOException -> L5f
            goto L63
        L5f:
            r4 = move-exception
            r4.printStackTrace()
        L63:
            if (r5 == 0) goto L6d
            r5.close()     // Catch: java.lang.Exception -> L69
            goto L6d
        L69:
            r4 = move-exception
            r4.printStackTrace()
        L6d:
            return
        L6e:
            r4 = move-exception
        L6f:
            if (r0 == 0) goto L79
            r0.close()     // Catch: java.io.IOException -> L75
            goto L79
        L75:
            r6 = move-exception
            r6.printStackTrace()
        L79:
            if (r5 == 0) goto L83
            r5.close()     // Catch: java.lang.Exception -> L7f
            goto L83
        L7f:
            r5 = move-exception
            r5.printStackTrace()
        L83:
            throw r4
        L84:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "The file to be saved is null! filePath:"
            r5.append(r6)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            java.lang.String r5 = "FileUtil"
            android.util.Log.w(r5, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ileja.aibase.common.FileUtil.syncSaveStringToFile(java.lang.String, java.lang.String, boolean):void");
    }
}