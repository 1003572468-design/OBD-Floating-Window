package com.ileja.aibase.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

/* JADX INFO: loaded from: classes.dex */
public class BitmapUtil {
    private static int THRESHOLD = 127;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static Bitmap getDrawableBitmap(Context context, int i) {
        Drawable drawable = context.getResources().getDrawable(i);
        if (drawable != null) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return null;
    }

    public static Bitmap getPropThumnail(Context context, int i, int i2, int i3) {
        return ThumbnailUtils.extractThumbnail(drawableToBitmap(context.getResources().getDrawable(i3)), i, i2);
    }

    public static Bitmap small(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        float f;
        float f2;
        float f3;
        float f4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= height) {
            f4 = width / 2;
            f3 = width;
            f2 = f3;
            f = 0.0f;
        } else {
            f = (width - height) / 2;
            f2 = height;
            f3 = width - f;
            width = height;
            f4 = height / 2;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect((int) f, (int) 0.0f, (int) f3, (int) f2);
        Rect rect2 = new Rect((int) 0.0f, (int) 0.0f, (int) f2, (int) f2);
        RectF rectF = new RectF(rect2);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, f4, f4, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect2, paint);
        bitmap.recycle();
        return bitmapCreateBitmap;
    }

    public static Bitmap turnSpecColor(Bitmap bitmap, int[] iArr, int[] iArr2, int[] iArr3, int i, boolean z) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int[] iArr4 = new int[width * height];
        bitmap.getPixels(iArr4, 0, width, 0, 0, width, height);
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int i4 = (width * i2) + i3;
                int i5 = iArr4[i4];
                int iRed = Color.red(i5);
                int iGreen = Color.green(i5);
                int iBlue = Color.blue(i5);
                if (iRed >= iArr[0] && iRed <= iArr[1] && iGreen >= iArr2[0] && iGreen <= iArr2[1] && iBlue >= iArr3[0] && iBlue <= iArr3[1]) {
                    iArr4[i4] = i;
                }
            }
        }
        if (z) {
            bitmap.recycle();
        }
        bitmapCreateBitmap.setPixels(iArr4, 0, width, 0, 0, width, height);
        return bitmapCreateBitmap;
    }

    public static Bitmap turnWhiteToGray(Bitmap bitmap, int i, boolean z) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int[] iArr = new int[width * height];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int i4 = (width * i2) + i3;
                int i5 = iArr[i4];
                int iRed = Color.red(i5);
                int iGreen = Color.green(i5);
                int iBlue = Color.blue(i5);
                int i6 = THRESHOLD;
                if (iRed <= i6 || iGreen <= i6 || iBlue <= i6) {
                    iArr[i4] = 0;
                } else {
                    iArr[i4] = i;
                }
            }
        }
        if (z) {
            bitmap.recycle();
        }
        bitmapCreateBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return bitmapCreateBitmap;
    }

    public static Bitmap getPropThumnail(Bitmap bitmap, int i) {
        Bitmap bitmapExtractThumbnail = ThumbnailUtils.extractThumbnail(bitmap, i, i);
        bitmap.recycle();
        return bitmapExtractThumbnail;
    }
}