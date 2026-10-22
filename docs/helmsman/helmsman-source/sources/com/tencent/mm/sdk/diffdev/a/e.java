package com.tencent.mm.sdk.diffdev.a;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

/* JADX INFO: loaded from: classes2.dex */
public final class e {
    public static byte[] b(String str, int i) {
        String string;
        StringBuilder sb;
        String message;
        if (str != null && str.length() != 0) {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(str);
            if (i >= 0) {
                try {
                    HttpConnectionParams.setSoTimeout(defaultHttpClient.getParams(), i);
                } catch (Exception e) {
                    sb = new StringBuilder("httpGet, Exception ex = ");
                    message = e.getMessage();
                    string = sb.append(message).toString();
                    Log.e("MicroMsg.SDK.NetUtil", string);
                    return null;
                } catch (IncompatibleClassChangeError e2) {
                    sb = new StringBuilder("httpGet, IncompatibleClassChangeError ex = ");
                    message = e2.getMessage();
                    string = sb.append(message).toString();
                    Log.e("MicroMsg.SDK.NetUtil", string);
                    return null;
                } catch (Throwable th) {
                    sb = new StringBuilder("httpGet, Throwable ex = ");
                    message = th.getMessage();
                    string = sb.append(message).toString();
                    Log.e("MicroMsg.SDK.NetUtil", string);
                    return null;
                }
            }
            HttpResponse httpResponseExecute = defaultHttpClient.execute(httpGet);
            if (httpResponseExecute.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toByteArray(httpResponseExecute.getEntity());
            }
            Log.e("MicroMsg.SDK.NetUtil", "httpGet fail, status code = " + httpResponseExecute.getStatusLine().getStatusCode());
            return null;
        }
        string = "httpGet, url is null";
        Log.e("MicroMsg.SDK.NetUtil", string);
        return null;
    }
}
