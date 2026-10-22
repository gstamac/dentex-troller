package com.amap.api.maps.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.FrameLayout;
import com.amap.api.mapcore.util.dl;
import com.amap.api.mapcore.util.dw;
import com.amap.api.mapcore.util.o;
import java.io.FileInputStream;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class BitmapDescriptorFactory {
    public static final float HUE_AZURE = 210.0f;
    public static final float HUE_BLUE = 240.0f;
    public static final float HUE_CYAN = 180.0f;
    public static final float HUE_GREEN = 120.0f;
    public static final float HUE_MAGENTA = 300.0f;
    public static final float HUE_ORANGE = 30.0f;
    public static final float HUE_RED = 0.0f;
    public static final float HUE_ROSE = 330.0f;
    public static final float HUE_VIOLET = 270.0f;
    public static final float HUE_YELLOW = 60.0f;

    public static BitmapDescriptor fromResource(int i) {
        try {
            Context context = o.a;
            if (context != null) {
                return fromBitmap(BitmapFactory.decodeStream(context.getResources().openRawResource(i)));
            }
        } catch (Throwable unused) {
        }
        return null;
    }

    public static BitmapDescriptor fromView(View view) {
        try {
            Context context = o.a;
            if (context != null) {
                FrameLayout frameLayout = new FrameLayout(context);
                frameLayout.addView(view);
                frameLayout.setDrawingCacheEnabled(true);
                return fromBitmap(dw.a(frameLayout));
            }
        } catch (Throwable unused) {
        }
        return null;
    }

    public static BitmapDescriptor fromPath(String str) {
        try {
            return fromBitmap(BitmapFactory.decodeFile(str));
        } catch (Throwable unused) {
            return null;
        }
    }

    public static BitmapDescriptor fromAsset(String str) {
        try {
            Context context = o.a;
            if (context != null) {
                return fromBitmap(dw.a(context, str));
            }
            InputStream resourceAsStream = BitmapDescriptorFactory.class.getResourceAsStream("/assets/" + str);
            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(resourceAsStream);
            resourceAsStream.close();
            return fromBitmap(bitmapDecodeStream);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static BitmapDescriptor fromFile(String str) {
        try {
            Context context = o.a;
            if (context != null) {
                FileInputStream fileInputStreamOpenFileInput = context.openFileInput(str);
                Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(fileInputStreamOpenFileInput);
                fileInputStreamOpenFileInput.close();
                BitmapDescriptor bitmapDescriptorFromBitmap = fromBitmap(bitmapDecodeStream);
                bitmapDecodeStream.recycle();
                return bitmapDescriptorFromBitmap;
            }
        } catch (Throwable unused) {
        }
        return null;
    }

    public static BitmapDescriptor defaultMarker() {
        try {
            return fromAsset(dl.a.marker_default.name() + ".png");
        } catch (Throwable unused) {
            return null;
        }
    }

    public static BitmapDescriptor defaultMarker(float f) {
        try {
            float f2 = (((int) (f + 15.0f)) / 30) * 30;
            if (f2 > 330.0f) {
                f2 = 330.0f;
            } else if (f2 < 0.0f) {
                f2 = 0.0f;
            }
            String str = "";
            if (f2 == 0.0f) {
                str = "RED";
            } else if (f2 == 30.0f) {
                str = "ORANGE";
            } else if (f2 == 60.0f) {
                str = "YELLOW";
            } else if (f2 == 120.0f) {
                str = "GREEN";
            } else if (f2 == 180.0f) {
                str = "CYAN";
            } else if (f2 == 210.0f) {
                str = "AZURE";
            } else if (f2 == 240.0f) {
                str = "BLUE";
            } else if (f2 == 270.0f) {
                str = "VIOLET";
            } else if (f2 == 300.0f) {
                str = "MAGENTA";
            } else if (f2 == 330.0f) {
                str = "ROSE";
            }
            return fromAsset(str.concat(".png"));
        } catch (Throwable unused) {
            return null;
        }
    }

    public static BitmapDescriptor fromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            return new BitmapDescriptor(bitmap);
        } catch (Throwable unused) {
            return null;
        }
    }
}
