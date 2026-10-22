package com.qiniu.android.common;

import com.qiniu.android.http.Client;
import com.qiniu.android.http.CompletionHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpToken;
import com.qiniu.android.utils.UrlSafeBase64;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class AutoZone extends Zone {
    public static final AutoZone autoZone = new AutoZone();
    private Client client;
    private final String ucServer;
    private Map<ZoneIndex, ZoneInfo> zones;

    public AutoZone() {
        this("https://uc.qbox.me");
    }

    AutoZone(String str) {
        this.zones = new ConcurrentHashMap();
        this.client = new Client();
        this.ucServer = str;
    }

    private void getZoneJsonAsync(ZoneIndex zoneIndex, CompletionHandler completionHandler) {
        this.client.asyncGet(this.ucServer + "/v2/query?ak=" + zoneIndex.accessKey + "&bucket=" + zoneIndex.bucket, null, UpToken.NULL, completionHandler);
    }

    private ResponseInfo getZoneJsonSync(ZoneIndex zoneIndex) {
        return this.client.syncGet(this.ucServer + "/v2/query?ak=" + zoneIndex.accessKey + "&bucket=" + zoneIndex.bucket, null);
    }

    ZoneInfo zoneInfo(String str, String str2) {
        return this.zones.get(new ZoneIndex(str, str2));
    }

    ZoneInfo queryByToken(String str) {
        try {
            String[] strArrSplit = str.split(":");
            return zoneInfo(strArrSplit[0], new JSONObject(new String(UrlSafeBase64.decode(strArrSplit[2]), Constants.UTF_8)).getString("scope").split(":")[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void preQueryIndex(final ZoneIndex zoneIndex, final Zone.QueryHandler queryHandler) {
        if (zoneIndex == null) {
            queryHandler.onFailure(-5);
        } else if (this.zones.get(zoneIndex) != null) {
            queryHandler.onSuccess();
        } else {
            getZoneJsonAsync(zoneIndex, new CompletionHandler() { // from class: com.qiniu.android.common.AutoZone.1
                @Override // com.qiniu.android.http.CompletionHandler
                public void complete(ResponseInfo responseInfo, JSONObject jSONObject) {
                    if (responseInfo.isOK() && jSONObject != null) {
                        try {
                            AutoZone.this.zones.put(zoneIndex, ZoneInfo.buildFromJson(jSONObject));
                            queryHandler.onSuccess();
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            queryHandler.onFailure(-1);
                            return;
                        }
                    }
                    queryHandler.onFailure(responseInfo.statusCode);
                }
            });
        }
    }

    boolean preQueryIndex(ZoneIndex zoneIndex) {
        if (zoneIndex != null) {
            if (this.zones.get(zoneIndex) != null) {
                return true;
            }
            try {
                this.zones.put(zoneIndex, ZoneInfo.buildFromJson(getZoneJsonSync(zoneIndex).response));
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override // com.qiniu.android.common.Zone
    public synchronized String upHost(String str, boolean z, String str2) {
        ZoneInfo zoneInfoQueryByToken = queryByToken(str);
        if (zoneInfoQueryByToken == null) {
            return null;
        }
        return super.upHost(zoneInfoQueryByToken, z, str2);
    }

    @Override // com.qiniu.android.common.Zone
    public void preQuery(String str, Zone.QueryHandler queryHandler) {
        preQueryIndex(ZoneIndex.getFromToken(str), queryHandler);
    }

    @Override // com.qiniu.android.common.Zone
    public boolean preQuery(String str) {
        return preQueryIndex(ZoneIndex.getFromToken(str));
    }

    @Override // com.qiniu.android.common.Zone
    public synchronized void frozenDomain(String str) {
        ZoneInfo value;
        if (str != null) {
            String host = URI.create(str).getHost();
            Iterator<Map.Entry<ZoneIndex, ZoneInfo>> it = this.zones.entrySet().iterator();
            do {
                if (!it.hasNext()) {
                    value = null;
                    break;
                }
                value = it.next().getValue();
            } while (!value.upDomainsList.contains(host));
            if (value != null) {
                value.frozenDomain(host);
            }
        }
    }

    static class ZoneIndex {
        final String accessKey;
        final String bucket;

        ZoneIndex(String str, String str2) {
            this.accessKey = str;
            this.bucket = str2;
        }

        static ZoneIndex getFromToken(String str) {
            String[] strArrSplit = str.split(":");
            try {
                return new ZoneIndex(strArrSplit[0], new JSONObject(new String(UrlSafeBase64.decode(strArrSplit[2]), Constants.UTF_8)).getString("scope").split(":")[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public int hashCode() {
            return (this.accessKey.hashCode() * 37) + this.bucket.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (obj != null && (obj instanceof ZoneIndex)) {
                    ZoneIndex zoneIndex = (ZoneIndex) obj;
                    if (!zoneIndex.accessKey.equals(this.accessKey) || !zoneIndex.bucket.equals(this.bucket)) {
                    }
                }
                return false;
            }
            return true;
        }
    }
}
