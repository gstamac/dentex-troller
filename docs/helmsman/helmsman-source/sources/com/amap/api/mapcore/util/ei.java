package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.amap.api.maps.model.LatLng;

/* JADX INFO: compiled from: LocationView.java */
/* JADX INFO: loaded from: classes.dex */
public class ei extends LinearLayout {
    Bitmap a;
    Bitmap b;
    Bitmap c;
    Bitmap d;
    Bitmap e;
    Bitmap f;
    ImageView g;
    k h;
    boolean i;

    public void a() {
        try {
            removeAllViews();
            Bitmap bitmap = this.a;
            if (bitmap != null) {
                bitmap.recycle();
            }
            Bitmap bitmap2 = this.b;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            if (this.b != null) {
                this.c.recycle();
            }
            this.a = null;
            this.b = null;
            this.c = null;
            Bitmap bitmap3 = this.d;
            if (bitmap3 != null) {
                bitmap3.recycle();
                this.d = null;
            }
            Bitmap bitmap4 = this.e;
            if (bitmap4 != null) {
                bitmap4.recycle();
                this.e = null;
            }
            Bitmap bitmap5 = this.f;
            if (bitmap5 != null) {
                bitmap5.recycle();
                this.f = null;
            }
        } catch (Throwable th) {
            fn.b(th, "LocationView", "destroy");
            th.printStackTrace();
        }
    }

    public ei(Context context, k kVar) {
        super(context);
        this.i = false;
        this.h = kVar;
        try {
            Bitmap bitmapA = dw.a(context, "location_selected.png");
            this.d = bitmapA;
            this.a = dw.a(bitmapA, g.a);
            Bitmap bitmapA2 = dw.a(context, "location_pressed.png");
            this.e = bitmapA2;
            this.b = dw.a(bitmapA2, g.a);
            Bitmap bitmapA3 = dw.a(context, "location_unselected.png");
            this.f = bitmapA3;
            this.c = dw.a(bitmapA3, g.a);
            ImageView imageView = new ImageView(context);
            this.g = imageView;
            imageView.setImageBitmap(this.a);
            this.g.setClickable(true);
            this.g.setPadding(0, 20, 20, 0);
            this.g.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.ei.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (!ei.this.i) {
                        return false;
                    }
                    if (motionEvent.getAction() == 0) {
                        ei.this.g.setImageBitmap(ei.this.b);
                    } else if (motionEvent.getAction() == 1) {
                        try {
                            ei.this.g.setImageBitmap(ei.this.a);
                            ei.this.h.setMyLocationEnabled(true);
                            Location myLocation = ei.this.h.getMyLocation();
                            if (myLocation == null) {
                                return false;
                            }
                            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                            ei.this.h.a(myLocation);
                            ei.this.h.a(z.a(latLng, ei.this.h.g()));
                        } catch (Throwable th) {
                            fn.b(th, "LocationView", "onTouch");
                            th.printStackTrace();
                        }
                    }
                    return false;
                }
            });
            addView(this.g);
        } catch (Throwable th) {
            fn.b(th, "LocationView", "create");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        this.i = z;
        try {
            if (z) {
                this.g.setImageBitmap(this.a);
            } else {
                this.g.setImageBitmap(this.c);
            }
            this.g.invalidate();
        } catch (Throwable th) {
            fn.b(th, "LocationView", "showSelect");
            th.printStackTrace();
        }
    }
}
