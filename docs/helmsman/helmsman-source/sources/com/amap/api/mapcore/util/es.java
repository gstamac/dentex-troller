package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: TraceHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class es extends eq<List<TraceLocation>, List<LatLng>> implements Runnable {
    private List<TraceLocation> i;
    private Handler j;
    private int k;
    private int l;
    private String m;

    public es(Context context, Handler handler, List<TraceLocation> list, int i, String str, int i2, int i3) {
        super(context, list);
        this.i = list;
        this.j = handler;
        this.l = i2;
        this.k = i3;
        this.m = str;
    }

    @Override // com.amap.api.mapcore.util.eq
    protected String f() {
        JSONArray jSONArray = new JSONArray();
        long j = 0;
        for (int i = 0; i < this.i.size(); i++) {
            TraceLocation traceLocation = this.i.get(i);
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("x", traceLocation.getLongitude());
                jSONObject.put("y", traceLocation.getLatitude());
                jSONObject.put("ag", (int) traceLocation.getBearing());
                long time = traceLocation.getTime();
                if (i == 0) {
                    if (time == 0) {
                        time = (System.currentTimeMillis() - OkHttpUtils.DEFAULT_MILLISECONDS) / 1000;
                    }
                    jSONObject.put("tm", time / 1000);
                } else if (time != 0) {
                    long j2 = time - j;
                    if (j2 < 1000) {
                        jSONObject.put("tm", 1);
                    } else {
                        jSONObject.put("tm", j2 / 1000);
                    }
                } else {
                    jSONObject.put("tm", 1);
                }
                j = time;
                jSONObject.put("sp", (int) traceLocation.getSpeed());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jSONArray.put(jSONObject);
        }
        this.e = c() + "&" + jSONArray.toString();
        return jSONArray.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.ep
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public List<LatLng> a(String str) throws eo {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jSONArrayOptJSONArray = new JSONObject(str).optJSONArray("points");
            if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() != 0) {
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                    arrayList.add(new LatLng(Double.parseDouble(jSONObjectOptJSONObject.optString("y")), Double.parseDouble(jSONObjectOptJSONObject.optString("x"))));
                }
                return arrayList;
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        new ArrayList();
        try {
            try {
                eu.a().a(this.m, this.k, d());
                eu.a().a(this.m).a(this.j);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } catch (eo e) {
            eu.a().a(this.j, this.l, e.a());
        }
    }
}
