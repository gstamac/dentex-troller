package cn.jiguang.q;

import android.text.TextUtils;
import com.bun.miitmdid.core.IIdentifierListener;
import com.bun.miitmdid.supplier.IdSupplier;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class e implements IIdentifierListener {
    private d a;

    public e(d dVar) {
        this.a = dVar;
    }

    public final void OnSupport(boolean z, IdSupplier idSupplier) {
        try {
            try {
                if (idSupplier == null) {
                    this.a.a.put("");
                    return;
                }
                JSONObject jSONObject = new JSONObject();
                String oaid = idSupplier.getOAID();
                String vaid = idSupplier.getVAID();
                String aaid = idSupplier.getAAID();
                if (!TextUtils.isEmpty(oaid)) {
                    jSONObject.put("oaid", oaid);
                }
                if (!TextUtils.isEmpty(vaid)) {
                    jSONObject.put("vaid", vaid);
                }
                if (!TextUtils.isEmpty(aaid)) {
                    jSONObject.put("aaid", aaid);
                }
                this.a.a.put(jSONObject.toString());
                idSupplier.shutDown();
            } catch (Throwable unused) {
            }
        } catch (Throwable unused2) {
            this.a.a.put("");
        }
    }
}
