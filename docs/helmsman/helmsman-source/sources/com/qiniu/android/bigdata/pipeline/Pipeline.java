package com.qiniu.android.bigdata.pipeline;

import com.qiniu.android.bigdata.Configuration;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.CompletionHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.utils.StringMap;
import com.qiniu.android.utils.StringUtils;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class Pipeline {
    private static final String HTTPHeaderAuthorization = "Authorization";
    private static final String TEXT_PLAIN = "text/plain";
    private final Client client;
    private final Configuration config;

    public interface PumpCompleteHandler {
        void complete(ResponseInfo responseInfo);
    }

    public Pipeline(Configuration configuration) {
        Configuration configurationCopy = Configuration.copy(configuration);
        this.config = configurationCopy;
        this.client = new Client(configurationCopy.proxy, configurationCopy.connectTimeout, configurationCopy.responseTimeout, null, null);
    }

    public <V> void pump(String str, Map<String, V> map, String str2, PumpCompleteHandler pumpCompleteHandler) {
        StringBuilder sb = new StringBuilder();
        Points.formatPoint((Map) map, sb);
        send(str, sb, str2, pumpCompleteHandler);
    }

    public void pump(String str, Object obj, String str2, PumpCompleteHandler pumpCompleteHandler) {
        StringBuilder sb = new StringBuilder();
        Points.formatPoint(obj, sb);
        send(str, sb, str2, pumpCompleteHandler);
    }

    public <V> void pumpMulti(String str, Map<String, V>[] mapArr, String str2, PumpCompleteHandler pumpCompleteHandler) {
        send(str, Points.formatPoints((Map[]) mapArr), str2, pumpCompleteHandler);
    }

    public void pumpMultiObjects(String str, Object[] objArr, String str2, PumpCompleteHandler pumpCompleteHandler) {
        send(str, Points.formatPoints(objArr), str2, pumpCompleteHandler);
    }

    public <V> void pumpMultiObjects(String str, List<V> list, String str2, PumpCompleteHandler pumpCompleteHandler) {
        send(str, Points.formatPointsObjects(list), str2, pumpCompleteHandler);
    }

    public <V> void pumpMulti(String str, List<Map<String, V>> list, String str2, PumpCompleteHandler pumpCompleteHandler) {
        send(str, Points.formatPoints(list), str2, pumpCompleteHandler);
    }

    private void send(String str, StringBuilder sb, String str2, final PumpCompleteHandler pumpCompleteHandler) {
        if (pumpCompleteHandler == null) {
            throw new IllegalArgumentException("no CompletionHandler");
        }
        if (StringUtils.isBlank(str2)) {
            throw new IllegalArgumentException("no token");
        }
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException("no repo");
        }
        byte[] bytes = sb.toString().getBytes();
        StringMap stringMap = new StringMap();
        stringMap.put("Authorization", str2);
        stringMap.put(Client.ContentTypeHeader, TEXT_PLAIN);
        this.client.asyncPost(url(str), bytes, stringMap, null, bytes.length, null, new CompletionHandler() { // from class: com.qiniu.android.bigdata.pipeline.Pipeline.1
            @Override // com.qiniu.android.http.CompletionHandler
            public void complete(ResponseInfo responseInfo, JSONObject jSONObject) {
                pumpCompleteHandler.complete(responseInfo);
            }
        }, null);
    }

    private String url(String str) {
        return this.config.pipelineHost + "/v2/repos/" + str + "/data";
    }
}
