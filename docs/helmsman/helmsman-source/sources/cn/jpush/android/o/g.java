package cn.jpush.android.o;

import android.content.Context;
import android.text.TextUtils;
import androidx.core.view.PointerIconCompat;
import cn.jpush.android.helper.Logger;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class g {
    public static boolean a(ArrayList<String> arrayList, Context context, String str, String str2, boolean z) throws Throwable {
        Logger.v("RichHelper", "action:loadHtmlImageResources - urlPrefix:" + str);
        boolean z2 = true;
        if (cn.jpush.android.t.a.a(str) && context != null && arrayList.size() > 0 && !TextUtils.isEmpty(str2)) {
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                byte[] bArrB = cn.jpush.android.n.a.b((next == null || next.startsWith("http://")) ? next : str + next, 5, 5000L);
                if (bArrB != null) {
                    try {
                        if (next.startsWith("http://")) {
                            next = cn.jpush.android.t.c.b(next);
                        }
                        String string = (!z ? new StringBuilder().append(cn.jpush.android.t.c.b(context, str2)).append(next) : new StringBuilder().append(cn.jpush.android.t.c.c(context, str2)).append(next)).toString();
                        cn.jpush.android.t.c.a(string, bArrB);
                        Logger.v("RichHelper", "Succeed to load image - " + string);
                    } catch (Exception e) {
                        Logger.ww("RichHelper", "Write storage error,  create img file fail.", e);
                        z2 = false;
                    }
                } else {
                    cn.jpush.android.helper.c.a(str2, PointerIconCompat.TYPE_GRAB, null, context);
                }
                z2 = false;
            }
        }
        return z2;
    }
}
