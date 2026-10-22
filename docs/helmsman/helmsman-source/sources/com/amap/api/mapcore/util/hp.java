package com.amap.api.mapcore.util;

import android.text.TextUtils;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.facebook.appevents.AppEventsConstants;
import org.json.JSONObject;

/* JADX INFO: compiled from: AMapLocationModel.java */
/* JADX INFO: loaded from: classes.dex */
public final class hp extends Inner_3dMap_location {
    boolean a;
    private String b;
    private String c;
    private int d;
    private String e;
    private String f;
    private JSONObject g;
    private String h;
    private String i;
    private long j;
    private String k;

    public hp(String str) {
        super(str);
        this.b = null;
        this.c = "";
        this.e = "";
        this.f = "new";
        this.g = null;
        this.h = "";
        this.a = true;
        this.i = "";
        this.j = 0L;
        this.k = null;
    }

    public final String a() {
        return this.b;
    }

    public final void a(String str) {
        this.b = str;
    }

    public final String b() {
        return this.c;
    }

    public final void b(String str) {
        this.c = str;
    }

    public final int c() {
        return this.d;
    }

    /* JADX WARN: Code duplicated, block: B:17:0x002d  */
    public final void c(String str) {
        int i;
        if (TextUtils.isEmpty(str)) {
            i = -1;
        } else if (getProvider().equals("gps")) {
            this.d = 0;
            return;
        } else if (str.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
            this.d = 0;
            return;
        } else if (str.equals("1")) {
            i = 1;
        } else {
            i = -1;
        }
        this.d = i;
    }

    public final String d() {
        return this.e;
    }

    public final void d(String str) {
        this.e = str;
    }

    public final JSONObject e() {
        return this.g;
    }

    public final void e(String str) {
        this.desc = str;
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_location
    public final void setFloor(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = str.replace("F", "");
            try {
                Integer.parseInt(str);
            } catch (Throwable th) {
                ij.a(th, "AMapLocationModel", "setFloor");
                str = null;
            }
        }
        this.floor = str;
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_location
    public final JSONObject toJson(int i) {
        try {
            JSONObject json = super.toJson(i);
            if (i == 1) {
                json.put("retype", this.e);
                json.put("cens", this.i);
                json.put("poiid", this.buildingId);
                json.put("floor", this.floor);
                json.put("coord", this.d);
                json.put("mcell", this.h);
                json.put("desc", this.desc);
                json.put("address", getAddress());
                if (this.g != null && im.a(json, "offpct")) {
                    json.put("offpct", this.g.getString("offpct"));
                }
            } else if (i != 2 && i != 3) {
                return json;
            }
            json.put("type", this.f);
            json.put("isReversegeo", this.a);
            return json;
        } catch (Throwable th) {
            ij.a(th, "AMapLocationModel", "toStr");
            return null;
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_location
    public final String toStr(int i) {
        JSONObject json;
        try {
            json = super.toJson(i);
            json.put("nb", this.k);
        } catch (Throwable th) {
            ij.a(th, "AMapLocationModel", "toStr part2");
            json = null;
        }
        if (json == null) {
            return null;
        }
        return json.toString();
    }
}
