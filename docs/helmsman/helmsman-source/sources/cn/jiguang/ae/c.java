package cn.jiguang.ae;

import android.text.TextUtils;
import java.io.File;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public long a;
    public JSONObject b;
    private File c;
    private JSONObject d;
    private boolean e;
    private boolean f;

    private c(File file, JSONObject jSONObject, JSONObject jSONObject2, boolean z) {
        this.c = file;
        this.d = jSONObject;
        this.b = jSONObject2;
        this.a = file.length();
        this.f = z;
        this.e = z;
    }

    public static c a(File file, Set<String> set) {
        JSONObject jSONObjectA = a(file);
        if (jSONObjectA != null) {
            return new c(file, jSONObjectA, cn.jiguang.f.g.a(jSONObjectA, set), false);
        }
        cn.jiguang.f.e.a(file);
        return null;
    }

    public static c a(File file, JSONObject jSONObject) {
        JSONObject jSONObjectA = a(file);
        if (jSONObjectA != null) {
            return new c(file, jSONObjectA, jSONObject, true);
        }
        cn.jiguang.f.e.a(file);
        return null;
    }

    public static JSONObject a(File file) {
        JSONObject jSONObject;
        JSONArray jSONArrayOptJSONArray;
        try {
            String strB = cn.jiguang.f.e.b(file);
            if (TextUtils.isEmpty(strB) || (jSONArrayOptJSONArray = (jSONObject = new JSONObject(strB)).optJSONArray("content")) == null || jSONArrayOptJSONArray.length() <= 0) {
                return null;
            }
            return jSONObject;
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0044 A[Catch: all -> 0x007d, TryCatch #0 {all -> 0x007d, blocks: (B:4:0x0006, B:6:0x0012, B:7:0x001f, B:9:0x0025, B:10:0x002f, B:12:0x003e, B:14:0x0044, B:15:0x0047, B:17:0x0056, B:18:0x005d, B:22:0x0067, B:27:0x0078, B:25:0x006d), top: B:30:0x0006 }] */
    /* JADX WARN: Code duplicated, block: B:17:0x0056 A[Catch: all -> 0x007d, TryCatch #0 {all -> 0x007d, blocks: (B:4:0x0006, B:6:0x0012, B:7:0x001f, B:9:0x0025, B:10:0x002f, B:12:0x003e, B:14:0x0044, B:15:0x0047, B:17:0x0056, B:18:0x005d, B:22:0x0067, B:27:0x0078, B:25:0x006d), top: B:30:0x0006 }] */
    /* JADX WARN: Code duplicated, block: B:20:0x0065  */
    /* JADX WARN: Code duplicated, block: B:21:0x0066  */
    /* JADX WARN: Code duplicated, block: B:25:0x006d A[Catch: all -> 0x007d, TryCatch #0 {all -> 0x007d, blocks: (B:4:0x0006, B:6:0x0012, B:7:0x001f, B:9:0x0025, B:10:0x002f, B:12:0x003e, B:14:0x0044, B:15:0x0047, B:17:0x0056, B:18:0x005d, B:22:0x0067, B:27:0x0078, B:25:0x006d), top: B:30:0x0006 }] */
    /* JADX WARN: Code duplicated, block: B:27:0x0078 A[Catch: all -> 0x007d, TRY_LEAVE, TryCatch #0 {all -> 0x007d, blocks: (B:4:0x0006, B:6:0x0012, B:7:0x001f, B:9:0x0025, B:10:0x002f, B:12:0x003e, B:14:0x0044, B:15:0x0047, B:17:0x0056, B:18:0x005d, B:22:0x0067, B:27:0x0078, B:25:0x006d), top: B:30:0x0006 }] */
    public final boolean a(c cVar, File file) {
        File file2;
        boolean z = true;
        if (cVar != null) {
            try {
                if (this.a + cVar.a <= 40960) {
                    JSONArray jSONArray = this.d.getJSONArray("content");
                    JSONArray jSONArray2 = cVar.d.getJSONArray("content");
                    for (int i = 0; i < jSONArray2.length(); i++) {
                        jSONArray.put(jSONArray2.getJSONObject(i));
                    }
                    this.a += cVar.a;
                    this.e = true;
                    cn.jiguang.f.e.a(cVar.c);
                    return true;
                }
                if (!file.exists()) {
                    file.mkdirs();
                }
                file2 = new File(file, this.c.getName());
                if (this.f) {
                    cn.jiguang.f.g.a(this.d, this.b);
                }
                if (!this.c.equals(file2)) {
                    z = false;
                }
                if (this.e || z) {
                    cn.jiguang.f.e.a(file2, this.d.toString());
                }
                if (z) {
                    cn.jiguang.f.e.a(this.c);
                }
            } catch (Throwable unused) {
            }
        } else {
            if (!file.exists()) {
                file.mkdirs();
            }
            file2 = new File(file, this.c.getName());
            if (this.f) {
                cn.jiguang.f.g.a(this.d, this.b);
            }
            if (!this.c.equals(file2)) {
                z = false;
            }
            if (this.e) {
                cn.jiguang.f.e.a(file2, this.d.toString());
            } else {
                cn.jiguang.f.e.a(file2, this.d.toString());
            }
            if (z) {
                cn.jiguang.f.e.a(this.c);
            }
        }
        return false;
    }
}
