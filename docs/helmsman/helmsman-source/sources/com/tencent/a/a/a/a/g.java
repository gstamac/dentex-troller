package com.tencent.a.a.a.a;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public final class g {
    private static g V;
    private Map<Integer, f> U;
    private int b = 0;
    private Context c;

    private g(Context context) {
        this.U = null;
        this.c = null;
        this.c = context.getApplicationContext();
        HashMap map = new HashMap(3);
        this.U = map;
        map.put(1, new e(context));
        this.U.put(2, new b(context));
        this.U.put(4, new d(context));
    }

    public static synchronized g E(Context context) {
        if (V == null) {
            V = new g(context);
        }
        return V;
    }

    private c b(List<Integer> list) {
        c cVarO;
        if (list.size() >= 0) {
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                f fVar = this.U.get(it.next());
                if (fVar != null && (cVarO = fVar.o()) != null && h.c(cVarO.c)) {
                    return cVarO;
                }
            }
        }
        return new c();
    }

    public final void a(String str) {
        c cVarP = p();
        cVarP.c = str;
        if (!h.b(cVarP.a)) {
            cVarP.a = h.a(this.c);
        }
        if (!h.b(cVarP.b)) {
            cVarP.b = h.b(this.c);
        }
        cVarP.T = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, f>> it = this.U.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().a(cVarP);
        }
    }

    public final c p() {
        return b(new ArrayList(Arrays.asList(1, 2, 4)));
    }
}
