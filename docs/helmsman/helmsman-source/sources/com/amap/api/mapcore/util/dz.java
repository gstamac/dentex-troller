package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileDescriptor;

/* JADX INFO: compiled from: ImageResizer.java */
/* JADX INFO: loaded from: classes.dex */
public class dz extends ea {
    protected int a;
    protected int b;

    public dz(Context context, int i, int i2) {
        super(context);
        a(i, i2);
    }

    public void a(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    private Bitmap a(int i) {
        return a(this.d, i, this.a, this.b, a());
    }

    @Override // com.amap.api.mapcore.util.ea
    protected Bitmap a(Object obj) {
        return a(Integer.parseInt(String.valueOf(obj)));
    }

    public static Bitmap a(Resources resources, int i, int i2, int i3, dx dxVar) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = a(options, i2, i3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    public static Bitmap a(FileDescriptor fileDescriptor, int i, int i2, dx dxVar) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = a(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static int a(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        if (i3 <= i2 && i4 <= i) {
            return 1;
        }
        int iRound = Math.round(i3 / i2);
        int iRound2 = Math.round(i4 / i);
        if (iRound >= iRound2) {
            iRound = iRound2;
        }
        while ((i4 * i3) / (iRound * iRound) > i * i2 * 2) {
            iRound++;
        }
        return iRound;
    }
}
