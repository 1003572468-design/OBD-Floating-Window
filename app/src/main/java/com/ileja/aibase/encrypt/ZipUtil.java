package com.ileja.aibase.encrypt;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.swiftp.Defaults;

/* JADX INFO: loaded from: classes.dex */
public class ZipUtil {
    public static void fileUnZip(ZipInputStream zipInputStream, File file, Context context) throws IOException {
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        while (nextEntry != null) {
            File file2 = new File(file.getAbsolutePath() + Defaults.chrootDir + nextEntry.getName());
            if (nextEntry.isDirectory()) {
                file2.mkdirs();
                fileUnZip(zipInputStream, file, context);
            } else {
                file2.createNewFile();
                FileOutputStream fileOutputStreamOpenFileOutput = context.openFileOutput(file.getName(), 0);
                byte[] bArr = new byte[2048];
                while (true) {
                    int i = zipInputStream.read(bArr);
                    if (i == -1) {
                        break;
                    } else {
                        fileOutputStreamOpenFileOutput.write(bArr, 0, i);
                    }
                }
                fileOutputStreamOpenFileOutput.close();
            }
            nextEntry = zipInputStream.getNextEntry();
        }
    }

    public static void fileZip(File file, File file2, Context context) throws IOException {
        if (!file2.isFile()) {
            return;
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(context.openFileOutput(file.getName(), 0));
        zipOutputStream.putNextEntry(new ZipEntry(file2.getName()));
        FileInputStream fileInputStreamOpenFileInput = context.openFileInput(file2.getName());
        byte[] bArr = new byte[2048];
        while (true) {
            int i = fileInputStreamOpenFileInput.read(bArr);
            if (i == -1) {
                fileInputStreamOpenFileInput.close();
                zipOutputStream.close();
                return;
            }
            zipOutputStream.write(bArr, 0, i);
        }
    }

    public static ZipInputStream unZip(InputStream inputStream) {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        try {
            zipInputStream.getNextEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipInputStream;
    }
}