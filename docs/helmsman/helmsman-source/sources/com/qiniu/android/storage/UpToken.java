package com.qiniu.android.storage;

import com.qiniu.android.utils.UrlSafeBase64;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class UpToken {
    public static UpToken NULL = new UpToken("", "", "");
    public final String accessKey;
    private String returnUrl;
    public final String token;

    private UpToken(String str, String str2, String str3) {
        this.returnUrl = str;
        this.token = str2;
        this.accessKey = str3;
    }

    public static UpToken parse(String str) {
        try {
            String[] strArrSplit = str.split(":");
            if (strArrSplit.length != 3) {
                return NULL;
            }
            try {
                JSONObject jSONObject = new JSONObject(new String(UrlSafeBase64.decode(strArrSplit[2])));
                if (jSONObject.optString("scope").equals("")) {
                    return NULL;
                }
                if (jSONObject.optInt("deadline") == 0) {
                    return NULL;
                }
                return new UpToken(jSONObject.optString("returnUrl"), str, strArrSplit[0]);
            } catch (JSONException unused) {
                return NULL;
            }
        } catch (Exception unused2) {
            return NULL;
        }
    }

    public String toString() {
        return this.token;
    }

    public boolean hasReturnUrl() {
        return !this.returnUrl.equals("");
    }
}
