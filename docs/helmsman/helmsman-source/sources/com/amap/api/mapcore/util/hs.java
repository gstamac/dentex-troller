package com.amap.api.mapcore.util;

import android.text.TextUtils;

/* JADX INFO: compiled from: MapLocFilter.java */
/* JADX INFO: loaded from: classes.dex */
public final class hs {
    private static hs c;
    private hp d = null;
    private long e = 0;
    private long f = 0;
    int a = 0;
    long b = 0;

    private hs() {
    }

    public static synchronized hs a() {
        if (c == null) {
            c = new hs();
        }
        return c;
    }

    public static hp b(hp hpVar) {
        return hpVar;
    }

    public final hp a(hp hpVar) {
        if (im.b() - this.b > 30000) {
            this.d = hpVar;
            this.b = im.b();
            return this.d;
        }
        this.b = im.b();
        if (!hz.a(this.d) || !hz.a(hpVar)) {
            this.e = im.b();
            this.d = hpVar;
            return hpVar;
        }
        if (hpVar.getTime() == this.d.getTime() && hpVar.getAccuracy() < 300.0f) {
            return hpVar;
        }
        if (hpVar.getProvider().equalsIgnoreCase("gps")) {
            this.e = im.b();
            this.d = hpVar;
            return hpVar;
        }
        if (hpVar.c() != this.d.c()) {
            this.e = im.b();
            this.d = hpVar;
            return hpVar;
        }
        if (!hpVar.getBuildingId().equals(this.d.getBuildingId()) && !TextUtils.isEmpty(hpVar.getBuildingId())) {
            this.e = im.b();
            this.d = hpVar;
            return hpVar;
        }
        this.a = hpVar.getLocationType();
        float fA = im.a(new double[]{hpVar.getLatitude(), hpVar.getLongitude(), this.d.getLatitude(), this.d.getLongitude()});
        float accuracy = this.d.getAccuracy();
        float accuracy2 = hpVar.getAccuracy();
        float f = accuracy2 - accuracy;
        long jB = im.b();
        long j = jB - this.e;
        if ((accuracy < 101.0f && accuracy2 > 299.0f) || (accuracy > 299.0f && accuracy2 > 299.0f)) {
            long j2 = this.f;
            if (j2 == 0) {
                this.f = jB;
            } else if (jB - j2 > 30000) {
                this.e = jB;
                this.d = hpVar;
                this.f = 0L;
                return hpVar;
            }
            return this.d;
        }
        if (accuracy2 < 100.0f && accuracy > 299.0f) {
            this.e = jB;
            this.d = hpVar;
            this.f = 0L;
            return hpVar;
        }
        if (accuracy2 <= 299.0f) {
            this.f = 0L;
        }
        if (fA < 10.0f && fA > 0.1d && accuracy2 > 5.0f) {
            if (f < -300.0f && accuracy / accuracy2 >= 2.0f) {
                this.e = jB;
                this.d = hpVar;
                return hpVar;
            }
            return this.d;
        }
        if (f < 300.0f) {
            this.e = im.b();
            this.d = hpVar;
            return hpVar;
        }
        if (j < 30000) {
            return this.d;
        }
        this.e = im.b();
        this.d = hpVar;
        return hpVar;
    }
}
