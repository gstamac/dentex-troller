package com.autonavi.ae.gmap.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.TextPaint;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class TextTextureGenerator {
    private static final int ALIGNCENTER = 51;
    private static final int ALIGNLEFT = 49;
    private static final int ALIGNRIGHT = 50;
    static final int AN_LABEL_MAXCHARINLINE = 7;
    static final int AN_LABEL_MULITYLINE_SPAN = 2;
    static final int TEXT_FONTSIZE = 32;
    static final int TEXT_FONTSIZE_TRUE = 30;
    private float base_line;
    private float start_x;
    private Paint text_paint;

    public static int GetNearstSize2N(int i) {
        int i2 = 1;
        while (i > i2) {
            i2 *= 2;
        }
        return i2;
    }

    public TextTextureGenerator() {
        float f;
        this.base_line = 0.0f;
        this.start_x = 0.0f;
        this.text_paint = null;
        Paint paintNewPaint = newPaint(null, 30, 49);
        this.text_paint = paintNewPaint;
        this.start_x = 1.0f;
        float f2 = 7.3242188f;
        try {
            Paint.FontMetrics fontMetrics = paintNewPaint.getFontMetrics();
            f2 = fontMetrics.descent;
            f = fontMetrics.ascent;
        } catch (Throwable unused) {
            f = -27.832031f;
        }
        this.base_line = ((30.0f - (f2 + f)) / 2.0f) + 1.0f + 0.5f;
    }

    public byte[] getTextPixelBuffer(int i) {
        try {
            char c = (char) i;
            char[] cArr = {c};
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ALPHA_8);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            byte[] bArr = new byte[1024];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            float fMeasureText = this.text_paint.measureText(String.valueOf(c));
            Paint.Align textAlign = this.text_paint.getTextAlign();
            float f = fMeasureText - 30.0f;
            if (textAlign != Paint.Align.CENTER && f >= 4.0f) {
                this.text_paint.setTextAlign(Paint.Align.CENTER);
                float f2 = 30.0f - f;
                this.text_paint.setTextSize(f2);
                canvas.drawText(cArr, 0, 1, f2 / 2.0f, this.base_line, this.text_paint);
                this.text_paint.setTextAlign(textAlign);
            } else {
                canvas.drawText(cArr, 0, 1, this.start_x, this.base_line, this.text_paint);
            }
            bitmapCreateBitmap.copyPixelsToBuffer(byteBufferWrap);
            bitmapCreateBitmap.recycle();
            return bArr;
        } catch (Throwable unused) {
            return null;
        }
    }

    public byte[] getCharsWidths(int[] iArr) {
        int length = iArr.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) (this.text_paint.measureText(((char) iArr[i]) + "") + 2.0f);
        }
        return bArr;
    }

    private static Paint newPaint(String str, int i, int i2) {
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(-1);
            textPaint.setTextSize(i);
            textPaint.setAntiAlias(true);
            textPaint.setFilterBitmap(true);
            textPaint.setTypeface(Typeface.DEFAULT);
            switch (i2) {
                case 49:
                    textPaint.setTextAlign(Paint.Align.LEFT);
                    return textPaint;
                case 50:
                    textPaint.setTextAlign(Paint.Align.RIGHT);
                    return textPaint;
                case 51:
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    return textPaint;
                default:
                    textPaint.setTextAlign(Paint.Align.LEFT);
                    return textPaint;
            }
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }

    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    public static void generaAsccIITexturePng() throws Exception {
        if (Environment.getExternalStorageState().equals("mounted")) {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "asccii.png"));
            Paint paintNewPaint = newPaint(null, 32, 49);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
            Paint.FontMetricsInt fontMetricsInt = paintNewPaint.getFontMetricsInt();
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            Paint paintNewPaint2 = newPaint(null, 32, 49);
            float[] fArr = new float[1];
            for (int i = 0; i < 16; i++) {
                for (int i2 = 0; i2 < 16; i2++) {
                    int i3 = i * 16;
                    char c = (char) (i3 + i2);
                    canvas.drawText(c + "", i2 * 16, (i3 - fontMetricsInt.ascent) - 2, paintNewPaint2);
                    paintNewPaint2.getTextWidths(c + "", fArr);
                }
            }
            bitmapCreateBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            bitmapCreateBitmap.recycle();
        }
    }

    public String getFontVersion() {
        byte[] textPixelBuffer = getTextPixelBuffer(39640);
        if (textPixelBuffer == null) {
            return null;
        }
        return GLMD5Util.getByteArrayMD5(textPixelBuffer);
    }
}
