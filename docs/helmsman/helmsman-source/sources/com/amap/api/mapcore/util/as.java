package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;

/* JADX INFO: compiled from: OfflineMapRemoveTask.java */
/* JADX INFO: loaded from: classes.dex */
public class as {
    private Context a;

    public as(Context context) {
        this.a = context;
    }

    public void a(al alVar) {
        b(alVar);
    }

    private boolean b(al alVar) {
        if (alVar != null) {
            String pinyin = alVar.getPinyin();
            boolean zA = a(pinyin, this.a, "vmap/");
            if (pinyin.equals("quanguogaiyaotu")) {
                pinyin = "quanguo";
            }
            boolean z = a(pinyin, this.a, "map/") || zA;
            if (z) {
                alVar.i();
                return z;
            }
            alVar.h();
        }
        return false;
    }

    private boolean a(String str, Context context, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String strA = dw.a(context);
        try {
            File file = new File(strA + str2 + str + ".dat");
            if (file.exists() && !bj.b(file)) {
                bj.a("deleteDownload delete some thing wrong!");
                return false;
            }
            try {
                bj.b(strA + str2);
                bj.b(str, context);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        } catch (Exception e4) {
            e4.printStackTrace();
            return false;
        }
    }
}
