package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.CameraUpdateMessage;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.FPoint3;
import com.autonavi.amap.mapcore.FileUtil;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.VirtualEarthProjection;
import com.qiniu.android.common.Constants;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: compiled from: Util.java */
/* JADX INFO: loaded from: classes.dex */
public class dw {
    private static FPoint[] a = {FPoint.obtain(), FPoint.obtain(), FPoint.obtain(), FPoint.obtain()};
    private static List<Float> b = new ArrayList(4);
    private static List<Float> c = new ArrayList(4);

    public static double a(double d, double d2, double d3, double d4, double d5, double d6) {
        return ((d3 - d) * (d6 - d2)) - ((d5 - d) * (d4 - d2));
    }

    private static float a(float f) {
        if (f == 50.0f) {
            return 0.082f;
        }
        if (f == 54.0f) {
            return 0.15f;
        }
        if (f == 57.0f) {
            return 0.2f;
        }
        return f == 60.0f ? 0.25f : 0.0f;
    }

    public static float a(float f, float f2) {
        if (f <= 40.0f) {
            return f;
        }
        if (f2 <= 15.0f) {
            return 40.0f;
        }
        if (f2 <= 16.0f) {
            return 50.0f;
        }
        if (f2 <= 17.0f) {
            return 54.0f;
        }
        return f2 <= 18.0f ? 57.0f : 60.0f;
    }

    public static boolean a() {
        return true;
    }

    public static boolean a(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        double d9 = d3 - d;
        double d10 = d8 - d6;
        double d11 = d4 - d2;
        double d12 = d7 - d5;
        double d13 = (d9 * d10) - (d11 * d12);
        if (d13 != 0.0d) {
            double d14 = d2 - d6;
            double d15 = d - d5;
            double d16 = ((d12 * d14) - (d10 * d15)) / d13;
            double d17 = ((d14 * d9) - (d15 * d11)) / d13;
            if (d16 >= 0.0d && d16 <= 1.0d && d17 >= 0.0d && d17 <= 1.0d) {
                return true;
            }
        }
        return false;
    }

    public static boolean b() {
        return true;
    }

    public static boolean c() {
        return true;
    }

    public static boolean d() {
        return true;
    }

    public static Bitmap a(Context context, String str) {
        try {
            InputStream inputStreamOpen = ds.a(context).open(str);
            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpen);
            inputStreamOpen.close();
            return bitmapDecodeStream;
        } catch (Throwable th) {
            fn.b(th, "Util", "fromAsset");
            return null;
        }
    }

    public static void a(Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    public static String a(String str, Object obj) {
        return str + "=" + String.valueOf(obj);
    }

    public static float a(MapConfig mapConfig, float f) {
        if (mapConfig != null) {
            if (f > mapConfig.maxZoomLevel) {
                return mapConfig.maxZoomLevel;
            }
            return f < mapConfig.minZoomLevel ? mapConfig.minZoomLevel : f;
        }
        float f2 = 19.0f;
        if (f <= 19.0f) {
            f2 = 3.0f;
            if (f >= 3.0f) {
                return f;
            }
        }
        return f2;
    }

    public static FloatBuffer a(float[] fArr) {
        try {
            ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
            byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
            FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect.asFloatBuffer();
            floatBufferAsFloatBuffer.put(fArr);
            floatBufferAsFloatBuffer.position(0);
            return floatBufferAsFloatBuffer;
        } catch (Throwable th) {
            fn.b(th, "Util", "makeFloatBuffer1");
            th.printStackTrace();
            return null;
        }
    }

    public static FloatBuffer a(float[] fArr, FloatBuffer floatBuffer) {
        try {
            floatBuffer.clear();
            floatBuffer.put(fArr);
            floatBuffer.position(0);
            return floatBuffer;
        } catch (Throwable th) {
            fn.b(th, "Util", "makeFloatBuffer2");
            th.printStackTrace();
            return null;
        }
    }

    public static int a(int i, int i2) {
        return a(0, Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888), true);
    }

    public static int a(Bitmap bitmap) {
        return a(bitmap, false);
    }

    public static int a(Bitmap bitmap, boolean z) {
        return a(0, bitmap, z);
    }

    public static int a(int i, Bitmap bitmap, boolean z) {
        int iB = b(i, bitmap, z);
        if (bitmap != null) {
            bitmap.recycle();
        }
        return iB;
    }

    public static int b(int i, Bitmap bitmap, boolean z) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        if (i == 0) {
            int[] iArr = {0};
            GLES20.glGenTextures(1, iArr, 0);
            i = iArr[0];
        }
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameterf(3553, 10241, 9729.0f);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        if (z) {
            GLES20.glTexParameterf(3553, 10242, 10497.0f);
            GLES20.glTexParameterf(3553, 10243, 10497.0f);
        } else {
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
        }
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        return i;
    }

    public static int a(int i, Bitmap bitmap, int i2, int i3) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        if (i == 0) {
            int[] iArr = {0};
            GLES20.glGenTextures(1, iArr, 0);
            i = iArr[0];
        }
        GLES20.glBindTexture(3553, i);
        GLUtils.texSubImage2D(3553, 0, i2, i3, bitmap);
        return i;
    }

    public static String a(String... strArr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String str : strArr) {
            sb.append(str);
            if (i != strArr.length - 1) {
                sb.append(",");
            }
            i++;
        }
        return sb.toString();
    }

    public static int a(Object[] objArr) {
        return Arrays.hashCode(objArr);
    }

    public static Bitmap a(Bitmap bitmap, float f) {
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * f), (int) (bitmap.getHeight() * f), true);
    }

    public static String a(Context context) {
        File file = new File(FileUtil.getMapBaseStorage(context), "data");
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(file.toString() + File.separator);
        if (!file2.exists()) {
            file2.mkdir();
        }
        return file.toString() + File.separator;
    }

    public static String b(Context context) {
        String strA = a(context);
        if (strA == null) {
            return null;
        }
        File file = new File(strA, "VMAP2");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.toString() + File.separator;
    }

    public static String a(int i) {
        if (i < 1000) {
            return i + "m";
        }
        return (i / 1000) + "km";
    }

    public static boolean c(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        NetworkInfo.State state;
        return (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || (state = activeNetworkInfo.getState()) == null || state == NetworkInfo.State.DISCONNECTED || state == NetworkInfo.State.DISCONNECTING) ? false : true;
    }

    public static void b(int i) {
        GLES20.glDeleteTextures(1, new int[]{i}, 0);
    }

    public static String a(InputStream inputStream) {
        try {
            return new String(b(inputStream), Constants.UTF_8);
        } catch (Throwable th) {
            fn.b(th, "Util", "decodeAssetResData");
            th.printStackTrace();
            return null;
        }
    }

    public static byte[] b(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[2048];
        while (true) {
            int i = inputStream.read(bArr, 0, 2048);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:101:0x00dd A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:102:0x00df A[Catch: IOException -> 0x00e3, TRY_ENTER, TRY_LEAVE, TryCatch #12 {IOException -> 0x00e3, blocks: (B:102:0x00df, B:93:0x00ce), top: B:109:0x00c0 }] */
    /* JADX WARN: Code duplicated, block: B:119:0x00c2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:58:0x0087 A[Catch: IOException -> 0x002b, TRY_ENTER, TRY_LEAVE, TryCatch #8 {IOException -> 0x002b, blocks: (B:10:0x0026, B:18:0x0037, B:58:0x0087, B:49:0x0076, B:80:0x00b6, B:71:0x00a5, B:9:0x0023, B:64:0x0099), top: B:117:0x0008, inners: #2, #15 }] */
    /* JADX WARN: Code duplicated, block: B:80:0x00b6 A[Catch: IOException -> 0x002b, TRY_ENTER, TRY_LEAVE, TryCatch #8 {IOException -> 0x002b, blocks: (B:10:0x0026, B:18:0x0037, B:58:0x0087, B:49:0x0076, B:80:0x00b6, B:71:0x00a5, B:9:0x0023, B:64:0x0099), top: B:117:0x0008, inners: #2, #15 }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v1, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r7v23 */
    /* JADX WARN: Type inference failed for: r7v5 */
    public static String a(File file) throws Throwable {
        ?? r7;
        FileInputStream fileInputStream;
        IOException e;
        BufferedReader bufferedReader;
        FileNotFoundException e2;
        StringBuffer stringBuffer = new StringBuffer();
        FileInputStream fileInputStream2 = null;
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, Constants.UTF_8));
                        while (true) {
                            try {
                                String line = bufferedReader.readLine();
                                if (line != null) {
                                    stringBuffer.append(line);
                                } else {
                                    try {
                                        try {
                                            break;
                                        } catch (IOException e3) {
                                            e3.printStackTrace();
                                            bufferedReader.close();
                                        }
                                    } catch (Throwable th) {
                                        try {
                                            bufferedReader.close();
                                        } catch (IOException e4) {
                                            e4.printStackTrace();
                                        }
                                        throw th;
                                    }
                                }
                            } catch (FileNotFoundException e5) {
                                e2 = e5;
                                fn.b(e2, "Util", "readFile fileNotFound");
                                e2.printStackTrace();
                                if (fileInputStream != null) {
                                    try {
                                        try {
                                            fileInputStream.close();
                                            if (bufferedReader != null) {
                                                bufferedReader.close();
                                            }
                                        } catch (IOException e6) {
                                            e6.printStackTrace();
                                            if (bufferedReader != null) {
                                                bufferedReader.close();
                                            }
                                        }
                                    } catch (Throwable th2) {
                                        if (bufferedReader != null) {
                                            try {
                                                bufferedReader.close();
                                            } catch (IOException e7) {
                                                e7.printStackTrace();
                                            }
                                        }
                                        throw th2;
                                    }
                                } else if (bufferedReader != null) {
                                    bufferedReader.close();
                                }
                            } catch (IOException e8) {
                                e = e8;
                                fn.b(e, "Util", "readFile io");
                                e.printStackTrace();
                                if (fileInputStream != null) {
                                    try {
                                        try {
                                            fileInputStream.close();
                                            if (bufferedReader != null) {
                                                bufferedReader.close();
                                            }
                                        } catch (IOException e9) {
                                            e9.printStackTrace();
                                            if (bufferedReader != null) {
                                                bufferedReader.close();
                                            }
                                        }
                                    } catch (Throwable th3) {
                                        if (bufferedReader != null) {
                                            try {
                                                bufferedReader.close();
                                            } catch (IOException e10) {
                                                e10.printStackTrace();
                                            }
                                        }
                                        throw th3;
                                    }
                                } else if (bufferedReader != null) {
                                    bufferedReader.close();
                                }
                            }
                        }
                        fileInputStream.close();
                        bufferedReader.close();
                    } catch (FileNotFoundException e11) {
                        e2 = e11;
                        bufferedReader = null;
                    } catch (IOException e12) {
                        e = e12;
                        bufferedReader = null;
                    } catch (Throwable th4) {
                        th = th4;
                        file = null;
                        fileInputStream2 = fileInputStream;
                        r7 = file;
                        try {
                            try {
                                if (fileInputStream2 != null) {
                                    try {
                                        fileInputStream2.close();
                                        if (r7 != 0) {
                                            r7.close();
                                        }
                                    } catch (IOException e13) {
                                        e13.printStackTrace();
                                        if (r7 != 0) {
                                            r7.close();
                                        }
                                        throw th;
                                    }
                                } else if (r7 != 0) {
                                    r7.close();
                                }
                            } catch (Throwable th5) {
                                if (r7 != 0) {
                                    try {
                                        r7.close();
                                    } catch (IOException e14) {
                                        e14.printStackTrace();
                                    }
                                }
                                throw th5;
                            }
                        } catch (IOException e15) {
                            e15.printStackTrace();
                        }
                        throw th;
                    }
                } catch (IOException e16) {
                    e16.printStackTrace();
                }
            } catch (FileNotFoundException e17) {
                fileInputStream = null;
                e2 = e17;
                bufferedReader = null;
            } catch (IOException e18) {
                fileInputStream = null;
                e = e18;
                bufferedReader = null;
            } catch (Throwable th6) {
                th = th6;
                r7 = 0;
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                    if (r7 != 0) {
                        r7.close();
                    }
                } else if (r7 != 0) {
                    r7.close();
                }
                throw th;
            }
            return stringBuffer.toString();
        } catch (Throwable th7) {
            th = th7;
        }
    }

    /* JADX WARN: Code duplicated, block: B:21:0x00a8  */
    public static boolean a(LatLng latLng, List<LatLng> list) {
        double d;
        List<LatLng> list2 = list;
        double d2 = latLng.longitude;
        double d3 = latLng.latitude;
        double d4 = latLng.latitude;
        boolean z = false;
        if (list.size() < 3) {
            return false;
        }
        if (!list2.get(0).equals(list2.get(list.size() - 1))) {
            list2.add(list2.get(0));
        }
        int i = 0;
        int i2 = 0;
        while (i < list.size() - 1) {
            double d5 = list2.get(i).longitude;
            double d6 = list2.get(i).latitude;
            int i3 = i + 1;
            double d7 = list2.get(i3).longitude;
            double d8 = list2.get(i3).latitude;
            boolean z2 = z;
            double d9 = d4;
            double d10 = d3;
            if (b(d2, d3, d5, d6, d7, d8)) {
                return true;
            }
            if (Math.abs(d8 - d6) < 1.0E-9d) {
                d = d2;
            } else if (b(d5, d6, d2, d10, 180.0d, d9)) {
                if (d6 > d8) {
                    i2++;
                }
                d = d2;
            } else if (b(d7, d8, d2, d10, 180.0d, d9)) {
                if (d8 > d6) {
                    i2++;
                }
                d = d2;
            } else {
                d = d2;
                if (a(d5, d6, d7, d8, d2, d10, 180.0d, d9)) {
                    i2++;
                }
            }
            d2 = d;
            z = z2;
            d4 = d9;
            d3 = d10;
            i = i3;
            list2 = list;
        }
        boolean z3 = z;
        if (i2 % 2 != 0) {
            return true;
        }
        return z3;
    }

    public static boolean b(double d, double d2, double d3, double d4, double d5, double d6) {
        return Math.abs(a(d, d2, d3, d4, d5, d6)) < 1.0E-9d && (d - d3) * (d - d5) <= 0.0d && (d2 - d4) * (d2 - d6) <= 0.0d;
    }

    public static List<FPoint> a(FPoint[] fPointArr, List<FPoint> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(list);
        for (byte b2 = 0; b2 < 4; b2 = (byte) (b2 + 1)) {
            arrayList.clear();
            int size = arrayList2.size();
            int i = 0;
            while (true) {
                if (i >= (z ? size : size - 1)) {
                    break;
                }
                FPoint fPoint = (FPoint) arrayList2.get(i % size);
                int i2 = i + 1;
                FPoint fPoint2 = (FPoint) arrayList2.get(i2 % size);
                if (i == 0 && a(fPoint, fPointArr[b2], fPointArr[(b2 + 1) % fPointArr.length])) {
                    arrayList.add(fPoint);
                }
                int i3 = b2 + 1;
                if (a(fPoint, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    if (a(fPoint2, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                        arrayList.add(fPoint2);
                    } else {
                        arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint, fPoint2));
                    }
                } else if (a(fPoint2, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint, fPoint2));
                    arrayList.add(fPoint2);
                }
                i = i2;
            }
            arrayList2.clear();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                arrayList2.add(arrayList.get(i4));
            }
        }
        return arrayList2;
    }

    public static List<FPoint> b(FPoint[] fPointArr, List<FPoint> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(list);
        for (byte b2 = 0; b2 < 4; b2 = (byte) (b2 + 1)) {
            arrayList.clear();
            int size = arrayList2.size();
            int i = 0;
            while (true) {
                if (i >= (z ? size : size - 1)) {
                    break;
                }
                FPoint3 fPoint3 = (FPoint3) arrayList2.get(i % size);
                int i2 = i + 1;
                FPoint3 fPoint4 = (FPoint3) arrayList2.get(i2 % size);
                if (i == 0 && a(fPoint3, fPointArr[b2], fPointArr[(b2 + 1) % fPointArr.length])) {
                    arrayList.add(fPoint3);
                }
                int i3 = b2 + 1;
                if (a(fPoint3, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    if (a(fPoint4, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                        arrayList.add(fPoint4);
                    } else {
                        arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint3, fPoint4));
                    }
                } else if (a(fPoint4, fPointArr[b2], fPointArr[i3 % fPointArr.length])) {
                    arrayList.add(a(fPointArr[b2], fPointArr[i3 % fPointArr.length], fPoint3, fPoint4));
                    arrayList.add(fPoint4);
                }
                i = i2;
            }
            arrayList2.clear();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                arrayList2.add(arrayList.get(i4));
            }
        }
        return arrayList2;
    }

    public static FPoint[] a(k kVar, boolean z) {
        int i;
        int i2;
        float mapHeight = (float) (((double) kVar.getMapHeight()) * ((double) a(kVar.getCameraAngle())));
        if (z) {
            i = 100;
            i2 = 10;
        } else {
            i = 0;
            i2 = 0;
        }
        FPoint fPointObtain = FPoint.obtain();
        int i3 = -i;
        int i4 = (int) (mapHeight - i2);
        kVar.b(i3, i4, fPointObtain);
        a[0].set(fPointObtain.x, fPointObtain.y);
        FPoint fPointObtain2 = FPoint.obtain();
        kVar.b(kVar.getMapWidth() + i, i4, fPointObtain2);
        a[1].set(fPointObtain2.x, fPointObtain2.y);
        FPoint fPointObtain3 = FPoint.obtain();
        kVar.b(kVar.getMapWidth() + i, kVar.getMapHeight() + i, fPointObtain3);
        a[2].set(fPointObtain3.x, fPointObtain3.y);
        FPoint fPointObtain4 = FPoint.obtain();
        kVar.b(i3, kVar.getMapHeight() + i, fPointObtain4);
        a[3].set(fPointObtain4.x, fPointObtain4.y);
        fPointObtain.recycle();
        fPointObtain2.recycle();
        fPointObtain3.recycle();
        fPointObtain4.recycle();
        return a;
    }

    private static FPoint3 a(FPoint fPoint, FPoint fPoint2, FPoint3 fPoint3, FPoint3 fPoint4) {
        FPoint3 fPoint5 = new FPoint3(0.0f, 0.0f, fPoint3.colorIndex);
        double d = ((fPoint2.y - fPoint.y) * (fPoint.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint.y - fPoint3.y));
        double d2 = ((fPoint2.y - fPoint.y) * (fPoint4.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint4.y - fPoint3.y));
        fPoint5.x = (float) (((double) fPoint3.x) + ((((double) (fPoint4.x - fPoint3.x)) * d) / d2));
        fPoint5.y = (float) (((double) fPoint3.y) + ((((double) (fPoint4.y - fPoint3.y)) * d) / d2));
        return fPoint5;
    }

    private static FPoint a(FPoint fPoint, FPoint fPoint2, FPoint fPoint3, FPoint fPoint4) {
        FPoint fPointObtain = FPoint.obtain(0.0f, 0.0f);
        double d = ((fPoint2.y - fPoint.y) * (fPoint.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint.y - fPoint3.y));
        double d2 = ((fPoint2.y - fPoint.y) * (fPoint4.x - fPoint3.x)) - ((fPoint2.x - fPoint.x) * (fPoint4.y - fPoint3.y));
        fPointObtain.x = (float) (((double) fPoint3.x) + ((((double) (fPoint4.x - fPoint3.x)) * d) / d2));
        fPointObtain.y = (float) (((double) fPoint3.y) + ((((double) (fPoint4.y - fPoint3.y)) * d) / d2));
        return fPointObtain;
    }

    public static boolean a(FPoint fPoint, FPoint[] fPointArr) {
        if (fPointArr == null) {
            return false;
        }
        byte b2 = 0;
        while (b2 < fPointArr.length) {
            FPoint fPoint2 = fPointArr[b2];
            int i = b2 + 1;
            if (!a(fPoint, fPoint2, fPointArr[i % fPointArr.length])) {
                return false;
            }
            b2 = (byte) i;
        }
        return true;
    }

    private static boolean a(FPoint fPoint, FPoint fPoint2, FPoint fPoint3) {
        return ((double) (((fPoint3.x - fPoint2.x) * (fPoint.y - fPoint2.y)) - ((fPoint.x - fPoint2.x) * (fPoint3.y - fPoint2.y)))) >= 0.0d;
    }

    public static List<IPoint> a(IPoint[] iPointArr, List<IPoint> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(list);
        for (byte b2 = 0; b2 < 4; b2 = (byte) (b2 + 1)) {
            arrayList.clear();
            int size = arrayList2.size();
            int i = 0;
            while (true) {
                if (i >= (z ? size : size - 1)) {
                    break;
                }
                IPoint iPoint = (IPoint) arrayList2.get(i % size);
                int i2 = i + 1;
                IPoint iPoint2 = (IPoint) arrayList2.get(i2 % size);
                if (i == 0 && a(iPoint, iPointArr[b2], iPointArr[(b2 + 1) % iPointArr.length])) {
                    arrayList.add(iPoint);
                }
                int i3 = b2 + 1;
                if (a(iPoint, iPointArr[b2], iPointArr[i3 % iPointArr.length])) {
                    if (a(iPoint2, iPointArr[b2], iPointArr[i3 % iPointArr.length])) {
                        arrayList.add(iPoint2);
                    } else {
                        arrayList.add(a(iPointArr[b2], iPointArr[i3 % iPointArr.length], iPoint, iPoint2));
                    }
                } else if (a(iPoint2, iPointArr[b2], iPointArr[i3 % iPointArr.length])) {
                    arrayList.add(a(iPointArr[b2], iPointArr[i3 % iPointArr.length], iPoint, iPoint2));
                    arrayList.add(iPoint2);
                }
                i = i2;
            }
            arrayList2.clear();
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                arrayList2.add(arrayList.get(i4));
            }
        }
        return arrayList2;
    }

    private static IPoint a(IPoint iPoint, IPoint iPoint2, IPoint iPoint3, IPoint iPoint4) {
        IPoint iPointObtain = IPoint.obtain(0, 0);
        double d = (((double) (iPoint2.y - iPoint.y)) * ((double) (iPoint.x - iPoint3.x))) - (((double) (iPoint2.x - iPoint.x)) * ((double) (iPoint.y - iPoint3.y)));
        double d2 = (((double) (iPoint2.y - iPoint.y)) * ((double) (iPoint4.x - iPoint3.x))) - (((double) (iPoint2.x - iPoint.x)) * ((double) (iPoint4.y - iPoint3.y)));
        iPointObtain.x = (int) (((double) iPoint3.x) + ((((double) (iPoint4.x - iPoint3.x)) * d) / d2));
        iPointObtain.y = (int) (((double) iPoint3.y) + ((((double) (iPoint4.y - iPoint3.y)) * d) / d2));
        return iPointObtain;
    }

    public static boolean a(List<IPoint> list, int i, int i2) {
        if (i2 < 3) {
            return false;
        }
        int i3 = i2 - 1;
        double d = 0.0d;
        for (int i4 = 0; i4 < i2; i4++) {
            IPoint iPoint = list.get(i3);
            IPoint iPoint2 = list.get(i4);
            d += ((((double) iPoint.x) / 1000000.0d) * (((double) iPoint2.y) / 1000000.0d)) - ((((double) iPoint2.x) / 1000000.0d) * (((double) iPoint.y) / 1000000.0d));
            i3 = i4;
        }
        return d < 0.0d;
    }

    private static boolean a(IPoint iPoint, IPoint iPoint2, IPoint iPoint3) {
        return a(iPoint.x, iPoint.y, iPoint2, iPoint3);
    }

    private static boolean a(int i, int i2, IPoint iPoint, IPoint iPoint2) {
        return (((double) (iPoint2.x - iPoint.x)) * ((double) (i2 - iPoint.y))) - (((double) (i - iPoint.x)) * ((double) (iPoint2.y - iPoint.y))) >= 0.0d;
    }

    public static Bitmap a(int i, int i2, int i3, int i4) {
        int i5 = i3 * i4;
        try {
            int[] iArr = new int[i5];
            int[] iArr2 = new int[i5];
            IntBuffer intBufferWrap = IntBuffer.wrap(iArr);
            intBufferWrap.position(0);
            GLES20.glReadPixels(i, i2, i3, i4, 6408, 5121, intBufferWrap);
            for (int i6 = 0; i6 < i4; i6++) {
                for (int i7 = 0; i7 < i3; i7++) {
                    int i8 = iArr[(i6 * i3) + i7];
                    iArr2[(((i4 - i6) - 1) * i3) + i7] = (i8 & (-16711936)) | ((i8 << 16) & 16711680) | ((i8 >> 16) & 255);
                }
            }
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i3, i4, Bitmap.Config.ARGB_8888);
            bitmapCreateBitmap.setPixels(iArr2, 0, i3, 0, 0, i3, i4);
            return bitmapCreateBitmap;
        } catch (Throwable th) {
            fn.b(th, "AMapDelegateImpGLSurfaceView", "SavePixels");
            th.printStackTrace();
            return null;
        }
    }

    public static ff e() {
        try {
            if (g.f == null) {
                return new ff.a("3dmap", "5.4.0", g.d).a(new String[]{"com.amap.api.maps", "com.amap.api.mapcore", "com.autonavi.amap.mapcore", "com.amap.api.3dmap.admic", "com.amap.api.trace", "com.amap.api.trace.core"}).a("5.4.0").a();
            }
            return g.f;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static void b(View view) {
        int i = 0;
        if (!(view instanceof ViewGroup)) {
            if (view instanceof TextView) {
                ((TextView) view).setHorizontallyScrolling(false);
            }
        } else {
            while (true) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (i >= viewGroup.getChildCount()) {
                    return;
                }
                b(viewGroup.getChildAt(i));
                i++;
            }
        }
    }

    public static Bitmap a(View view) {
        try {
            b(view);
            view.destroyDrawingCache();
            view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            Bitmap drawingCache = view.getDrawingCache();
            if (drawingCache != null) {
                return drawingCache.copy(Bitmap.Config.ARGB_8888, false);
            }
            return null;
        } catch (Throwable th) {
            fn.b(th, "Utils", "getBitmapFromView");
            th.printStackTrace();
            return null;
        }
    }

    public static DPoint a(LatLng latLng) {
        double d = (latLng.longitude / 360.0d) + 0.5d;
        double dSin = Math.sin(Math.toRadians(latLng.latitude));
        return DPoint.obtain(d * 1.0d, (((Math.log((dSin + 1.0d) / (1.0d - dSin)) * 0.5d) / (-6.283185307179586d)) + 0.5d) * 1.0d);
    }

    public static boolean a(Rect rect, int i, int i2) {
        return rect.contains(i, i2);
    }

    public static Pair<Float, IPoint> a(CameraUpdateMessage cameraUpdateMessage, GLMapState gLMapState, MapConfig mapConfig) {
        return a(mapConfig, Math.max(cameraUpdateMessage.paddingLeft, 1), Math.max(cameraUpdateMessage.paddingRight, 1), Math.max(cameraUpdateMessage.paddingTop, 1), Math.max(cameraUpdateMessage.paddingBottom, 1), cameraUpdateMessage.bounds, cameraUpdateMessage.width, cameraUpdateMessage.height);
    }

    public static Pair<Float, IPoint> a(MapConfig mapConfig, int i, int i2, int i3, int i4, LatLngBounds latLngBounds, int i5, int i6) {
        int i7;
        float f;
        float f2;
        int i8;
        Point pointLatLongToPixels = VirtualEarthProjection.LatLongToPixels(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude, 20);
        Point pointLatLongToPixels2 = VirtualEarthProjection.LatLongToPixels(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude, 20);
        int i9 = pointLatLongToPixels.x - pointLatLongToPixels2.x;
        int i10 = pointLatLongToPixels2.y - pointLatLongToPixels.y;
        int i11 = i5 - (i + i2);
        int i12 = i6 - (i3 + i4);
        if (i9 < 0 && i10 < 0) {
            return null;
        }
        if (i9 <= 0) {
            i9 = 1;
        }
        if (i10 <= 0) {
            i10 = 1;
        }
        Pair<Float, Boolean> pairA = a(mapConfig, pointLatLongToPixels.x, pointLatLongToPixels.y, pointLatLongToPixels2.x, pointLatLongToPixels2.y, i11 <= 0 ? 1 : i11, i12 <= 0 ? 1 : i12);
        float fFloatValue = ((Float) pairA.first).floatValue();
        boolean zBooleanValue = ((Boolean) pairA.second).booleanValue();
        float fA = a(mapConfig.getMapZoomScale(), fFloatValue, i9);
        float fA2 = a(mapConfig.getMapZoomScale(), fFloatValue, i10);
        if (fFloatValue >= mapConfig.maxZoomLevel) {
            i7 = (int) (pointLatLongToPixels2.x + ((((i2 - i) + fA) * i9) / (fA * 2.0f)));
            i8 = pointLatLongToPixels.y;
        } else {
            if (zBooleanValue) {
                i7 = (int) (pointLatLongToPixels2.x + ((((i5 / 2) - i) / fA) * i9));
                i8 = pointLatLongToPixels.y;
            } else {
                i7 = (int) (pointLatLongToPixels2.x + ((((i2 - i) + fA) * i9) / (fA * 2.0f)));
                f = pointLatLongToPixels.y;
                f2 = (((i6 / 2) - i3) / fA2) * i10;
            }
            return new Pair<>(Float.valueOf(fFloatValue), IPoint.obtain((int) (i7 + a(mapConfig.getMapZoomScale(), fFloatValue, mapConfig.getAnchorX() - (mapConfig.getMapWidth() >> 1))), (int) (((int) (f + f2)) + a(mapConfig.getMapZoomScale(), fFloatValue, mapConfig.getAnchorY() - (mapConfig.getMapHeight() >> 1)))));
        }
        f = i8;
        f2 = (((i4 - i3) + fA2) * i10) / (fA2 * 2.0f);
        return new Pair<>(Float.valueOf(fFloatValue), IPoint.obtain((int) (i7 + a(mapConfig.getMapZoomScale(), fFloatValue, mapConfig.getAnchorX() - (mapConfig.getMapWidth() >> 1))), (int) (((int) (f + f2)) + a(mapConfig.getMapZoomScale(), fFloatValue, mapConfig.getAnchorY() - (mapConfig.getMapHeight() >> 1)))));
    }

    public static double a(float f, double d, double d2) {
        return 20.0d - (Math.log(d2 / (d * ((double) f))) / Math.log(2.0d));
    }

    private static float a(float f, float f2, double d) {
        return (float) (d / (Math.pow(2.0d, 20.0f - f2) * ((double) f)));
    }

    private static float a(float f, float f2, float f3) {
        return (float) (((double) f3) * Math.pow(2.0d, 20.0f - f2) * ((double) f));
    }

    public static Pair<Float, Boolean> a(MapConfig mapConfig, int i, int i2, int i3, int i4, int i5, int i6) {
        float fMin;
        mapConfig.getS_z();
        boolean z = true;
        if (i == i3 && i2 == i4) {
            fMin = mapConfig.getMaxZoomLevel();
        } else {
            float fA = (float) a(mapConfig.getMapZoomScale(), i6, Math.abs(i4 - i2));
            float fA2 = (float) a(mapConfig.getMapZoomScale(), i5, Math.abs(i3 - i));
            float fMin2 = Math.min(fA2, fA);
            z = fMin2 == fA2;
            fMin = Math.min(mapConfig.getMaxZoomLevel(), Math.max(mapConfig.getMinZoomLevel(), fMin2));
        }
        return new Pair<>(Float.valueOf(fMin), Boolean.valueOf(z));
    }

    public static float b(MapConfig mapConfig, int i, int i2, int i3, int i4, int i5, int i6) {
        float s_z = mapConfig.getS_z();
        if (i == i3 || i2 == i4) {
            return s_z;
        }
        return Math.max((float) a(mapConfig.getMapZoomScale(), i5, Math.abs(i3 - i)), (float) a(mapConfig.getMapZoomScale(), i6, Math.abs(i4 - i2)));
    }

    public static boolean b(int i, int i2) {
        if (i > 0 && i2 > 0) {
            return true;
        }
        Log.w("3dmap", "the map must have a size");
        return false;
    }

    public static synchronized int[] a(int i, int i2, int i3, int i4, MapConfig mapConfig, GLMapState gLMapState, int i5, int i6) {
        int mapWidth;
        int mapHeight;
        int anchorX;
        int anchorY;
        mapWidth = mapConfig.getMapWidth();
        mapHeight = mapConfig.getMapHeight();
        anchorX = mapConfig.getAnchorX();
        anchorY = mapConfig.getAnchorY();
        return new int[]{(int) Math.max(i3 + a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), anchorX), Math.min(i5, i - a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), mapWidth - anchorX))), (int) Math.max(i2 + a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), anchorY), Math.min(i6, i4 - a(mapConfig.getMapZoomScale(), gLMapState.getMapZoomer(), mapHeight - anchorY)))};
    }

    public static void a(Rect rect) {
        if (rect != null) {
            rect.set(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public static void b(Rect rect, int i, int i2) {
        if (rect != null) {
            if (i < rect.left) {
                rect.left = i;
            }
            if (i > rect.right) {
                rect.right = i;
            }
            if (i2 > rect.top) {
                rect.top = i2;
            }
            if (i2 < rect.bottom) {
                rect.bottom = i2;
            }
        }
    }
}
