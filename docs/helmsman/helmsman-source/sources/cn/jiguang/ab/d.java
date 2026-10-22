package cn.jiguang.ab;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    public static List<cn.jiguang.aa.b> a(String str, String str2, String str3, int i) {
        List<cn.jiguang.aa.b> arrayList;
        cn.jiguang.aa.b bVar = new cn.jiguang.aa.b();
        bVar.a = str2;
        bVar.b = str3;
        if (str == null || TextUtils.isEmpty(str)) {
            arrayList = null;
        } else {
            arrayList = c.a(str);
            cn.jiguang.w.a.a("JWakePackageHelper", "cache cmd wakeTargets:" + arrayList);
        }
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (arrayList.get(size).a.equals(str2) && arrayList.get(size).b.equals(str3)) {
                    arrayList.remove(size);
                    break;
                }
            }
        }
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        if (i == 1) {
            arrayList.add(bVar);
        }
        return arrayList;
    }
}
