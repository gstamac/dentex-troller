package com.tencent.mm.sdk.modelbase;

import android.os.Bundle;
import com.tencent.mm.sdk.b.a;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseReq {
    public String openId;
    public String transaction;

    public abstract boolean checkArgs();

    public void fromBundle(Bundle bundle) {
        this.transaction = a.b(bundle, "_wxapi_basereq_transaction");
        this.openId = a.b(bundle, "_wxapi_basereq_openid");
    }

    public abstract int getType();

    public void toBundle(Bundle bundle) {
        bundle.putInt("_wxapi_command_type", getType());
        bundle.putString("_wxapi_basereq_transaction", this.transaction);
        bundle.putString("_wxapi_basereq_openid", this.openId);
    }
}
