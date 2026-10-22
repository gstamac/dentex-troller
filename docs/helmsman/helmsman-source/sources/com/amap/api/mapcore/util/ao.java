package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMapException;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.ServerProtocol;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: compiled from: OfflineInitHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class ao extends bn<String, an> {
    private final String d;
    private final String e;
    private final String f;
    private final String g;

    @Override // com.amap.api.mapcore.util.bn
    protected String a() {
        return "014";
    }

    public ao(Context context, String str) {
        super(context, str);
        this.d = "update";
        this.e = "1";
        this.f = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.g = ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION;
    }

    @Override // com.amap.api.mapcore.util.bn
    protected JSONObject a(ex.a aVar) {
        return aVar.f;
    }

    @Override // com.amap.api.mapcore.util.bn
    protected Map<String, String> b() {
        HashMap map = new HashMap();
        map.put("mapver", this.a);
        return map;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.bn
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public an b(JSONObject jSONObject) throws AMapException {
        an anVar = new an();
        try {
            String strOptString = jSONObject.optString("update", "");
            if (strOptString.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                anVar.a(false);
            } else if (strOptString.equals("1")) {
                anVar.a(true);
            }
            anVar.a(jSONObject.optString(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, ""));
        } catch (Throwable th) {
            fn.b(th, "OfflineInitHandler", "loadData parseJson");
        }
        return anVar;
    }
}
