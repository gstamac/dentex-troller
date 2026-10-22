package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: OfflineUpdateCityHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class at extends bn<String, List<OfflineMapProvince>> {
    private Context d;

    @Override // com.amap.api.mapcore.util.bn
    protected String a() {
        return "013";
    }

    public at(Context context, String str) {
        super(context, str);
    }

    public void a(Context context) {
        this.d = context;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.bn
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public List<OfflineMapProvince> b(JSONObject jSONObject) throws AMapException {
        try {
            if (this.d != null) {
                bj.c(jSONObject.toString(), this.d);
            }
        } catch (Throwable th) {
            fn.b(th, "OfflineUpdateCityHandler", "loadData jsonInit");
            th.printStackTrace();
        }
        try {
            Context context = this.d;
            if (context != null) {
                return bj.a(jSONObject, context);
            }
            return null;
        } catch (JSONException e) {
            fn.b(e, "OfflineUpdateCityHandler", "loadData parseJson");
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.bn
    protected JSONObject a(ex.a aVar) {
        JSONObject jSONObject = aVar.e;
        if (!jSONObject.has("result")) {
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("result", new JSONObject().put("offlinemap_with_province_vfour", jSONObject));
                return jSONObject2;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    @Override // com.amap.api.mapcore.util.bn
    protected Map<String, String> b() {
        HashMap map = new HashMap();
        map.put("mapver", this.a);
        return map;
    }
}
