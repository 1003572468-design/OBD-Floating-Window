package com.ileja.aibase.utils;

import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes.dex */
public class AshmemUtils {
    public static Object fromSHaredMemoryFD(ParcelFileDescriptor parcelFileDescriptor) {
        FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[102400];
        while (true) {
            try {
                int i = fileInputStream.read(bArr);
                if (i <= 0) {
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    fileInputStream.close();
                    byteArrayOutputStream.close();
                    parcelFileDescriptor.close();
                    return ObjectAndByteConvertUtils.toObject(byteArray);
                }
                byteArrayOutputStream.write(bArr, 0, i);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static ParcelFileDescriptor toSharedMemoryFD(String str, Object obj) {
        try {
            byte[] byteArray = ObjectAndByteConvertUtils.toByteArray(obj);
            int length = byteArray.length > 0 ? byteArray.length : 0;
            MemoryFile memoryFile = new MemoryFile(str, length);
            memoryFile.writeBytes(byteArray, 0, 0, length);
            return ParcelFileDescriptor.dup((FileDescriptor) MemoryFile.class.getDeclaredMethod("getFileDescriptor", new Class[0]).invoke(memoryFile, new Object[0]));
        } catch (IOException e) {
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
}