package com.qiniu.android.storage;

import com.qiniu.android.http.ResponseInfo;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public interface UpCompletionHandler {
    void complete(String str, ResponseInfo responseInfo, JSONObject jSONObject);
}
