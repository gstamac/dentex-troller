package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/* JADX INFO: compiled from: InfoWindowDelegate.java */
/* JADX INFO: loaded from: classes.dex */
public class ae {
    AMap.InfoWindowAdapter a;
    Context b;
    private View d;
    private TextView e;
    private TextView f;
    private ad h;
    private ad i;
    private AMap.InfoWindowAdapter j;
    private boolean c = true;
    private Drawable g = null;

    public ae(Context context) {
        this.a = null;
        AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() { // from class: com.amap.api.mapcore.util.ae.1
            @Override // com.amap.api.maps.AMap.InfoWindowAdapter
            public View getInfoContents(Marker marker) {
                return null;
            }

            @Override // com.amap.api.maps.AMap.InfoWindowAdapter
            public View getInfoWindow(Marker marker) {
                try {
                    if (ae.this.g == null) {
                        ae aeVar = ae.this;
                        aeVar.g = dp.a(aeVar.b, "infowindow_bg.9.png");
                    }
                    if (ae.this.d == null) {
                        ae.this.d = new LinearLayout(ae.this.b);
                        ae.this.d.setBackground(ae.this.g);
                        ae.this.e = new TextView(ae.this.b);
                        ae.this.e.setText(marker.getTitle());
                        ae.this.e.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                        ae.this.f = new TextView(ae.this.b);
                        ae.this.f.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                        ae.this.f.setText(marker.getSnippet());
                        ((LinearLayout) ae.this.d).setOrientation(1);
                        ((LinearLayout) ae.this.d).addView(ae.this.e);
                        ((LinearLayout) ae.this.d).addView(ae.this.f);
                    }
                } catch (Throwable th) {
                    fn.b(th, "InfoWindowDelegate", "showInfoWindow decodeDrawableFromAsset");
                    th.printStackTrace();
                }
                return ae.this.d;
            }
        };
        this.j = infoWindowAdapter;
        this.b = context;
        this.a = infoWindowAdapter;
    }

    public void a(ad adVar) {
        this.h = adVar;
        if (adVar != null) {
            adVar.a(this);
        }
    }

    public void b(ad adVar) {
        this.i = adVar;
        if (adVar != null) {
            adVar.a(this);
        }
    }

    public synchronized boolean a() {
        return this.c;
    }

    public void a(String str, String str2) {
        TextView textView = this.e;
        if (textView != null) {
            textView.setText(str);
        }
        TextView textView2 = this.f;
        if (textView2 != null) {
            textView2.setText(str2);
        }
        View view = this.d;
        if (view != null) {
            view.requestLayout();
        }
    }

    public synchronized void a(AMap.InfoWindowAdapter infoWindowAdapter) {
        this.a = infoWindowAdapter;
        if (infoWindowAdapter == null) {
            this.a = this.j;
            this.c = true;
        } else {
            this.c = false;
        }
        ad adVar = this.i;
        if (adVar != null) {
            adVar.a_();
        }
        ad adVar2 = this.h;
        if (adVar2 != null) {
            adVar2.a_();
        }
    }

    public void b() {
        this.b = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.j = null;
        this.a = null;
        dw.a(this.g);
        this.g = null;
    }

    public View a(Marker marker) {
        AMap.InfoWindowAdapter infoWindowAdapter = this.a;
        if (infoWindowAdapter != null) {
            return infoWindowAdapter.getInfoWindow(marker);
        }
        return null;
    }

    public View b(Marker marker) {
        AMap.InfoWindowAdapter infoWindowAdapter = this.a;
        if (infoWindowAdapter != null) {
            return infoWindowAdapter.getInfoContents(marker);
        }
        return null;
    }

    public View c(Marker marker) {
        AMap.InfoWindowAdapter infoWindowAdapter = this.a;
        if (infoWindowAdapter == null || !(infoWindowAdapter instanceof AMap.MultiPositionInfoWindowAdapter)) {
            return null;
        }
        return ((AMap.MultiPositionInfoWindowAdapter) infoWindowAdapter).getInfoWindowClick(marker);
    }

    public View d(Marker marker) {
        AMap.InfoWindowAdapter infoWindowAdapter = this.a;
        if (infoWindowAdapter == null || !(infoWindowAdapter instanceof AMap.MultiPositionInfoWindowAdapter)) {
            return null;
        }
        return ((AMap.MultiPositionInfoWindowAdapter) infoWindowAdapter).getOverturnInfoWindow(marker);
    }

    public View e(Marker marker) {
        AMap.InfoWindowAdapter infoWindowAdapter = this.a;
        if (infoWindowAdapter == null || !(infoWindowAdapter instanceof AMap.MultiPositionInfoWindowAdapter)) {
            return null;
        }
        return ((AMap.MultiPositionInfoWindowAdapter) infoWindowAdapter).getOverturnInfoWindowClick(marker);
    }

    public long c() {
        AMap.InfoWindowAdapter infoWindowAdapter = this.a;
        if (infoWindowAdapter == null || !(infoWindowAdapter instanceof AMap.ImageInfoWindowAdapter)) {
            return 0L;
        }
        return ((AMap.ImageInfoWindowAdapter) infoWindowAdapter).getInfoWindowUpdateTime();
    }

    public void d() {
        ad adVarE = e();
        if (adVarE != null) {
            adVarE.b();
        }
    }

    public synchronized ad e() {
        AMap.InfoWindowAdapter infoWindowAdapter = this.a;
        if (infoWindowAdapter == null) {
            return null;
        }
        if (infoWindowAdapter instanceof AMap.ImageInfoWindowAdapter) {
            return this.i;
        }
        if (infoWindowAdapter instanceof AMap.MultiPositionInfoWindowAdapter) {
            return this.i;
        }
        return this.h;
    }

    public boolean a(MotionEvent motionEvent) {
        ad adVarE = e();
        if (adVarE != null) {
            return adVarE.a(motionEvent);
        }
        return false;
    }

    public void f() {
        ad adVarE = e();
        if (adVarE != null) {
            adVarE.a_();
        }
    }

    public void a(cf cfVar) throws RemoteException {
        ad adVarE = e();
        if (adVarE != null) {
            adVarE.a(cfVar);
        }
    }

    public Drawable g() {
        if (this.g == null) {
            try {
                this.g = dp.a(this.b, "infowindow_bg.9.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.g;
    }
}
