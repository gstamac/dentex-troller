package com.tencent.wxop.stat.common;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class b {
    static d a;
    private static StatLogger d = k.b();
    private static JSONObject e = new JSONObject();
    Integer b;
    String c;

    public b(Context context) {
        this.b = null;
        this.c = null;
        try {
            a(context);
            this.b = k.m(context.getApplicationContext());
            this.c = com.tencent.wxop.stat.a.a(context).b();
        } catch (Throwable th) {
            d.e(th);
        }
    }

    static synchronized d a(Context context) {
        if (a == null) {
            a = new d(context.getApplicationContext());
        }
        return a;
    }

    public static void a(Context context, Map<String, String> map) throws JSONException {
        if (map == null) {
            return;
        }
        for (Map.Entry entry : new HashMap(map).entrySet()) {
            e.put((String) entry.getKey(), entry.getValue());
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void a(JSONObject jSONObject, Thread thread) {
        String str;
        Object string;
        JSONObject jSONObject2 = new JSONObject();
        try {
            d dVar = a;
            if (dVar != null) {
                dVar.a(jSONObject2, thread);
            }
            q.a(jSONObject2, "cn", this.c);
            Integer num = this.b;
            if (num != null) {
                jSONObject2.put("tn", num);
            }
            if (thread == null) {
                str = "ev";
                string = jSONObject2;
            } else {
                str = "errkv";
                string = jSONObject2.toString();
            }
            jSONObject.put(str, string);
            JSONObject jSONObject3 = e;
            if (jSONObject3 == null || jSONObject3.length() <= 0) {
                return;
            }
            jSONObject.put("eva", e);
        } catch (Throwable th) {
            d.e(th);
        }
    }
}
