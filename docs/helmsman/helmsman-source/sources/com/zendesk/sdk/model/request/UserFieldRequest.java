package com.zendesk.sdk.model.request;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class UserFieldRequest {
    private Map<String, Map<String, String>> user;

    public UserFieldRequest(Map<String, String> map) {
        HashMap map2 = new HashMap();
        map2.put("user_fields", map);
        this.user = map2;
    }
}
