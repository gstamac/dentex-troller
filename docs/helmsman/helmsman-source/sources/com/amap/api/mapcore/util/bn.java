package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMapException;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: compiled from: ProtocalHandler.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class bn<T, V> {
    protected T a;
    protected int b = 3;
    protected Context c;

    protected abstract String a();

    protected abstract JSONObject a(ex.a aVar);

    protected abstract V b(JSONObject jSONObject) throws AMapException;

    protected abstract Map<String, String> b();

    public bn(Context context, T t) {
        a(context, t);
    }

    private void a(Context context, T t) {
        this.c = context;
        this.a = t;
    }

    public V c() throws AMapException {
        if (this.a != null) {
            return d();
        }
        return null;
    }

    protected V d() throws AMapException {
        int i = 0;
        V vB = null;
        ex.a aVarA = null;
        while (i < this.b) {
            try {
                aVarA = ex.a(this.c, dw.e(), a(), b());
                vB = b(a(aVarA));
                i = this.b;
            } catch (Throwable th) {
                fn.b(th, "ProtocalHandler", "getDataMayThrow AMapException");
                th.printStackTrace();
                i++;
                if (i < this.b) {
                    continue;
                } else {
                    if (aVarA != null && aVarA.a != null) {
                        throw new AMapException(aVarA.a);
                    }
                    vB = null;
                }
            }
        }
        return vB;
    }
}
