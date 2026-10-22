package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import com.amap.api.maps.MapsInitializer;
import com.facebook.appevents.UserDataStore;
import org.json.JSONObject;

/* JADX INFO: compiled from: AuthTask.java */
/* JADX INFO: loaded from: classes.dex */
public class f extends Thread {
    private Context a;
    private k b;

    public f(Context context, k kVar) {
        this.a = context;
        this.b = kVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            if (MapsInitializer.getNetWorkEnable()) {
                ex.a aVarA = ex.a(this.a, dw.e(), "002;11K;001", null);
                if (ex.a != 1) {
                    Message messageObtainMessage = this.b.getMainHandler().obtainMessage();
                    messageObtainMessage.what = 2;
                    if (aVarA.a != null) {
                        messageObtainMessage.obj = aVarA.a;
                    }
                    this.b.getMainHandler().sendMessage(messageObtainMessage);
                }
                if (aVarA != null) {
                    if (aVarA.u != null) {
                        dw.e().a(aVarA.u.a);
                    }
                    if (aVarA.w != null) {
                        new fe(this.a, "3dmap", aVarA.w.a, aVarA.w.b).a();
                    }
                }
                a(aVarA);
                if (aVarA != null && aVarA.v != null) {
                    ex.a.d dVar = aVarA.v;
                    if (dVar != null) {
                        String str = dVar.b;
                        String str2 = dVar.a;
                        String str3 = dVar.c;
                        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
                            new gg(this.a, null, dw.e()).a();
                        } else {
                            new gg(this.a, new gh(str2, str, str3), dw.e()).a();
                        }
                    } else {
                        new gg(this.a, null, dw.e()).a();
                    }
                }
                fn.a(this.a, dw.e());
                interrupt();
                this.b.setRunLowFrame(false);
            }
        } catch (Throwable th) {
            interrupt();
            fn.b(th, "AMapDelegateImpGLSurfaceView", "mVerfy");
            th.printStackTrace();
        }
    }

    private void a(ex.a aVar) {
        try {
            ex.a.C0015a c0015a = aVar.u;
            if (c0015a != null) {
                dt.a(this.a, "maploc", "ue", Boolean.valueOf(c0015a.a));
                JSONObject jSONObject = c0015a.c;
                int iOptInt = jSONObject.optInt(UserDataStore.FIRST_NAME, 1000);
                int iOptInt2 = jSONObject.optInt("mpn", 0);
                if (iOptInt2 > 500) {
                    iOptInt2 = 500;
                }
                if (iOptInt2 < 30) {
                    iOptInt2 = 30;
                }
                hh.a(iOptInt, ex.a(jSONObject.optString("igu"), false));
                dt.a(this.a, "maploc", "opn", Integer.valueOf(iOptInt2));
            }
        } catch (Throwable th) {
            fn.b(th, "AuthUtil", "loadConfigData_uploadException");
        }
    }
}
