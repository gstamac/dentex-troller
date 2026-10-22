package com.qiniu.android.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class ZoneInfo {
    private static int DOMAIN_FROZEN_SECONDS = 600;
    private final int ttl;
    public final List<String> upDomainsList;
    public final Map<String, Long> upDomainsMap;

    public ZoneInfo(int i, List<String> list, Map<String, Long> map) {
        this.ttl = i;
        this.upDomainsList = list;
        this.upDomainsMap = map;
    }

    public static ZoneInfo buildFromJson(JSONObject jSONObject) throws JSONException {
        int i = jSONObject.getInt("ttl");
        ArrayList arrayList = new ArrayList();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        JSONObject jSONObject2 = jSONObject.getJSONObject("up");
        String[] strArr = {"acc", "src", "old_acc", "old_src"};
        for (int i2 = 0; i2 < 4; i2++) {
            JSONObject jSONObject3 = jSONObject2.getJSONObject(strArr[i2]);
            JSONArray jSONArray = jSONObject3.getJSONArray("main");
            for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                String string = jSONArray.getString(i3);
                arrayList.add(string);
                concurrentHashMap.put(string, 0L);
            }
            try {
                JSONArray jSONArray2 = jSONObject3.getJSONArray("backup");
                if (jSONArray2 != null) {
                    for (int i4 = 0; i4 < jSONArray2.length(); i4++) {
                        String string2 = jSONArray2.getString(i4);
                        arrayList.add(string2);
                        concurrentHashMap.put(string2, 0L);
                    }
                }
            } catch (JSONException unused) {
            }
        }
        return new ZoneInfo(i, arrayList, concurrentHashMap);
    }

    public void frozenDomain(String str) {
        this.upDomainsMap.put(str, Long.valueOf((System.currentTimeMillis() / 1000) + ((long) DOMAIN_FROZEN_SECONDS)));
    }

    public String toString() {
        HashMap map = new HashMap();
        map.put("ttl", Integer.valueOf(this.ttl));
        map.put("upDomainList", this.upDomainsList);
        map.put("upDomainMap", this.upDomainsMap);
        return new JSONObject(map).toString();
    }
}
