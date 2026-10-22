package cn.jiguang.ab;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import cn.jiguang.f.i;
import cn.jiguang.l.e;
import cn.jiguang.l.g;
import cn.jiguang.net.DefaultHostVerifier;
import cn.jiguang.net.HttpUtils;
import cn.jiguang.net.SSLTrustManager;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.codeless.internal.Constants;
import com.qiniu.android.http.Client;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import kotlin.UByte;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    public static String a = "";

    public static cn.jiguang.aa.a a(JSONObject jSONObject) {
        String str = "disable";
        cn.jiguang.aa.a aVar = new cn.jiguang.aa.a();
        if (jSONObject == null) {
            return aVar;
        }
        try {
            int iOptInt = jSONObject.optInt("app_wakeup_stat", -1);
            if (iOptInt < 0) {
                return aVar;
            }
            jSONObject.optInt("app_report_app_list_threshold", -1);
            int iOptInt2 = jSONObject.optInt("app_wakeup_threshold", -1) * 1000;
            if (iOptInt2 > 0) {
                aVar.e = iOptInt2;
            }
            int iOptInt3 = jSONObject.optInt("app_get_threshold", -1) * 1000;
            if (iOptInt3 > 0) {
                aVar.f = iOptInt3;
            }
            int iOptInt4 = jSONObject.optInt("app_report_threshold", -1) * 1000;
            if (iOptInt4 > 0) {
                aVar.g = iOptInt4;
            }
            if (iOptInt == 0 || iOptInt == 1) {
                aVar.c = true;
            } else if (iOptInt == 2) {
                aVar.c = false;
            } else {
                cn.jiguang.w.a.d("JWakeConfigHelper", "parseWakeConfigJson error: " + jSONObject.optString("errmsg"));
            }
            int iOptInt5 = jSONObject.optInt("app_wakeup_disable", -1);
            if (iOptInt5 == 0) {
                aVar.d = true;
            } else if (iOptInt5 == 1) {
                aVar.d = false;
            } else if (iOptInt5 == 2) {
                aVar.d = true;
            }
            aVar.a = !"disable".equals(jSONObject.optString("app_wakeup_config", "enable"));
            aVar.b = "disable".equals(jSONObject.optString("app_wakeuped_config", "enable")) ? false : true;
            String strOptString = jSONObject.optString("app_package_config", "disable");
            if (!strOptString.isEmpty()) {
                str = strOptString;
            }
            aVar.h = str;
            aVar.i = a(jSONObject, "app_package_list");
            aVar.j = a(jSONObject, "app_blacklist");
        } catch (JSONException e) {
            cn.jiguang.w.a.d("JWakeConfigHelper", "parseWakeConfig exception:" + e.toString());
        }
        return aVar;
    }

    private static String a(byte[] bArr) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("MD5").digest(bArr);
            StringBuilder sb = new StringBuilder();
            for (byte b : bArrDigest) {
                int i = b & UByte.MAX_VALUE;
                if (i < 16) {
                    sb.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                }
                sb.append(Integer.toHexString(i));
            }
            return sb.toString();
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWakeConfigHelper", "get md5 throwable:" + th.getMessage());
            return "";
        }
    }

    private static List<String> a(JSONObject jSONObject, String str) {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(str);
        if (jSONArrayOptJSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
            arrayList.add(jSONArrayOptJSONArray.get(i).toString());
        }
        return arrayList;
    }

    /* JADX WARN: Code duplicated, block: B:147:0x036c A[PHI: r5 r9
  0x036c: PHI (r5v14 java.net.HttpURLConnection) = 
  (r5v10 java.net.HttpURLConnection)
  (r5v11 java.net.HttpURLConnection)
  (r5v12 java.net.HttpURLConnection)
  (r5v15 java.net.HttpURLConnection)
 binds: [B:113:0x02c4, B:124:0x02fc, B:135:0x0333, B:146:0x036a] A[DONT_GENERATE, DONT_INLINE]
  0x036c: PHI (r9v9 java.io.InputStream) = (r9v5 java.io.InputStream), (r9v6 java.io.InputStream), (r9v7 java.io.InputStream), (r9v10 java.io.InputStream) binds: [B:113:0x02c4, B:124:0x02fc, B:135:0x0333, B:146:0x036a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:160:0x02aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:165:0x0319 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:169:0x02e2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static JSONObject a(Context context) {
        Throwable th;
        JSONException jSONException;
        IOException iOException;
        UnsupportedEncodingException unsupportedEncodingException;
        InputStream inputStream;
        InputStream inputStream2;
        String str;
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                try {
                    if (!cn.jiguang.d.b.a(context)) {
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig failed because is not validRegistered");
                        return null;
                    }
                    String strA = cn.jiguang.ac.a.a(context);
                    if (TextUtils.isEmpty(strA)) {
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig failed because can't get appKey");
                        return null;
                    }
                    long jD = cn.jiguang.d.a.d(context);
                    if (jD == 0) {
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig failed because can't get uid");
                        return null;
                    }
                    String strF = cn.jiguang.d.a.f(context);
                    if (TextUtils.isEmpty(strF)) {
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig failed because need register first");
                        return null;
                    }
                    e.c(context, "JWakeConfigHelper");
                    String str2 = "https://bjuser.jpush.cn/v1/appawake/status";
                    if (g.a && !TextUtils.isEmpty(a)) {
                        cn.jiguang.w.a.a("JWakeConfigHelper", "use test url:" + a);
                        str2 = a;
                    }
                    HashMap map = new HashMap();
                    HashMap map2 = new HashMap();
                    String str3 = Build.MANUFACTURER;
                    String str4 = Build.MODEL;
                    map2.put("appkey", strA);
                    map2.put("uid", String.valueOf(jD));
                    if (!TextUtils.isEmpty(str3)) {
                        map2.put("manufacturer", str3);
                    }
                    if (!TextUtils.isEmpty(str4)) {
                        map2.put("model", str4);
                    }
                    String urlWithValueEncodeParas = HttpUtils.getUrlWithValueEncodeParas(str2, map2);
                    cn.jiguang.w.a.a("JWakeConfigHelper", "url:" + urlWithValueEncodeParas);
                    map.put(Client.ContentTypeHeader, "text/plain");
                    map.put("Accept", "application/json");
                    map.put("X-Http-Platform", Constants.PLATFORM);
                    map.put("X-Http-Appkey", strA);
                    String strE = i.e(jD + i.e(strF) + a("".getBytes("UTF-8")));
                    if (TextUtils.isEmpty(strE)) {
                        return null;
                    }
                    String strEncodeToString = Base64.encodeToString((jD + ":" + strE).getBytes(), 10);
                    if (TextUtils.isEmpty(strEncodeToString)) {
                        return null;
                    }
                    map.put("Authorization", "Basic " + strEncodeToString);
                    map.put("Charset", "UTF-8");
                    HttpURLConnection httpURLConnectionWithProxy = HttpUtils.getHttpURLConnectionWithProxy(context, urlWithValueEncodeParas);
                    try {
                        httpURLConnectionWithProxy.setConnectTimeout(30000);
                        httpURLConnectionWithProxy.setReadTimeout(30000);
                        for (Map.Entry entry : map.entrySet()) {
                            httpURLConnectionWithProxy.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                        }
                        if (httpURLConnectionWithProxy instanceof HttpsURLConnection) {
                            try {
                                SSLContext sSLContext = SSLContext.getInstance("TLS");
                                sSLContext.init(null, new TrustManager[]{new SSLTrustManager("-----BEGIN CERTIFICATE-----\nMIIETzCCAzegAwIBAgIDAjpvMA0GCSqGSIb3DQEBCwUAMEIxCzAJBgNVBAYTAlVT\nMRYwFAYDVQQKEw1HZW9UcnVzdCBJbmMuMRswGQYDVQQDExJHZW9UcnVzdCBHbG9i\nYWwgQ0EwHhcNMTMxMTA1MjEzNjUwWhcNMjIwNTIwMjEzNjUwWjBEMQswCQYDVQQG\nEwJVUzEWMBQGA1UEChMNR2VvVHJ1c3QgSW5jLjEdMBsGA1UEAxMUR2VvVHJ1c3Qg\nU1NMIENBIC0gRzMwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDjvn4K\nhqPPa209K6GXrUkkTdd3uTR5CKWeop7eRxKSPX7qGYax6E89X/fQp3eaWx8KA7UZ\nU9ulIZRpY51qTJEMEEe+EfpshiW3qwRoQjgJZfAU2hme+msLq2LvjafvY3AjqK+B\n89FuiGdT7BKkKXWKp/JXPaKDmJfyCn3U50NuMHhiIllZuHEnRaoPZsZVP/oyFysx\nj0ag+mkUfJ2fWuLrM04QprPtd2PYw5703d95mnrU7t7dmszDt6ldzBE6B7tvl6QB\nI0eVH6N3+liSxsfQvc+TGEK3fveeZerVO8rtrMVwof7UEJrwEgRErBpbeFBFV0xv\nvYDLgVwts7x2oR5lAgMBAAGjggFKMIIBRjAfBgNVHSMEGDAWgBTAephojYn7qwVk\nDBF9qn1luMrMTjAdBgNVHQ4EFgQU0m/3lvSFP3I8MH0j2oV4m6N8WnwwEgYDVR0T\nAQH/BAgwBgEB/wIBADAOBgNVHQ8BAf8EBAMCAQYwNgYDVR0fBC8wLTAroCmgJ4Yl\naHR0cDovL2cxLnN5bWNiLmNvbS9jcmxzL2d0Z2xvYmFsLmNybDAvBggrBgEFBQcB\nAQQjMCEwHwYIKwYBBQUHMAGGE2h0dHA6Ly9nMi5zeW1jYi5jb20wTAYDVR0gBEUw\nQzBBBgpghkgBhvhFAQc2MDMwMQYIKwYBBQUHAgEWJWh0dHA6Ly93d3cuZ2VvdHJ1\nc3QuY29tL3Jlc291cmNlcy9jcHMwKQYDVR0RBCIwIKQeMBwxGjAYBgNVBAMTEVN5\nbWFudGVjUEtJLTEtNTM5MA0GCSqGSIb3DQEBCwUAA4IBAQCg1Pcs+3QLf2TxzUNq\nn2JTHAJ8mJCi7k9o1CAacxI+d7NQ63K87oi+fxfqd4+DYZVPhKHLMk9sIb7SaZZ9\nY73cK6gf0BOEcP72NZWJ+aZ3sEbIu7cT9clgadZM/tKO79NgwYCA4ef7i28heUrg\n3Kkbwbf7w0lZXLV3B0TUl/xJAIlvBk4BcBmsLxHA4uYPL4ZLjXvDuacu9PGsFj45\nSVGeF0tPEDpbpaiSb/361gsDTUdWVxnzy2v189bPsPX1oxHSIFMTNDcFLENaY9+N\nQNaFHlHpURceA1bJ8TCt55sRornQMYGbaLHZ6PPmlH7HrhMvh+3QJbBo+d4IWvMp\nzNSS\n-----END CERTIFICATE-----")}, new SecureRandom());
                                ((HttpsURLConnection) httpURLConnectionWithProxy).setSSLSocketFactory(sSLContext.getSocketFactory());
                                ((HttpsURLConnection) httpURLConnectionWithProxy).setHostnameVerifier(new DefaultHostVerifier(httpURLConnectionWithProxy.getURL().getHost()));
                            } catch (Throwable th2) {
                                cn.jiguang.w.a.d("JWakeConfigHelper", "set ssl config error:" + th2.getMessage());
                            }
                        }
                        if (httpURLConnectionWithProxy.getResponseCode() == 200) {
                            inputStream2 = httpURLConnectionWithProxy.getInputStream();
                            try {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                byte[] bArr = new byte[1024];
                                while (true) {
                                    int i = inputStream2.read(bArr);
                                    if (i == -1) {
                                        break;
                                    }
                                    byteArrayOutputStream.write(bArr, 0, i);
                                }
                                str = new String(byteArrayOutputStream.toByteArray());
                            } catch (UnsupportedEncodingException e) {
                                httpURLConnection = httpURLConnectionWithProxy;
                                inputStream = inputStream2;
                                unsupportedEncodingException = e;
                            } catch (IOException e2) {
                                httpURLConnection = httpURLConnectionWithProxy;
                                inputStream = inputStream2;
                                iOException = e2;
                                cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + iOException.getMessage());
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Exception e3) {
                                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + e3.getMessage());
                                    }
                                }
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                return null;
                            } catch (JSONException e4) {
                                httpURLConnection = httpURLConnectionWithProxy;
                                inputStream = inputStream2;
                                jSONException = e4;
                                cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + jSONException.getMessage());
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Exception e5) {
                                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + e5.getMessage());
                                    }
                                }
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                return null;
                            } catch (Throwable th3) {
                                httpURLConnection = httpURLConnectionWithProxy;
                                inputStream = inputStream2;
                                th = th3;
                                cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + th.getMessage());
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Exception e6) {
                                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + e6.getMessage());
                                    }
                                }
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                return null;
                            }
                        } else {
                            inputStream2 = null;
                            str = null;
                        }
                        if (TextUtils.isEmpty(str)) {
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (Exception e7) {
                                    cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + e7.getMessage());
                                }
                            }
                            if (httpURLConnectionWithProxy == null) {
                                return null;
                            }
                            httpURLConnectionWithProxy.disconnect();
                            return null;
                        }
                        cn.jiguang.w.a.a("JWakeConfigHelper", "request wakeConfig success,response body:" + str);
                        JSONObject jSONObject = new JSONObject(str);
                        if (inputStream2 != null) {
                            try {
                                inputStream2.close();
                            } catch (Exception e8) {
                                cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + e8.getMessage());
                            }
                        }
                        if (httpURLConnectionWithProxy != null) {
                            httpURLConnectionWithProxy.disconnect();
                        }
                        return jSONObject;
                    } catch (UnsupportedEncodingException e9) {
                        unsupportedEncodingException = e9;
                        httpURLConnection = httpURLConnectionWithProxy;
                        inputStream = null;
                    } catch (IOException e10) {
                        iOException = e10;
                        httpURLConnection = httpURLConnectionWithProxy;
                        inputStream = null;
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + iOException.getMessage());
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return null;
                    } catch (JSONException e11) {
                        jSONException = e11;
                        httpURLConnection = httpURLConnectionWithProxy;
                        inputStream = null;
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + jSONException.getMessage());
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return null;
                    } catch (Throwable th4) {
                        th = th4;
                        httpURLConnection = httpURLConnectionWithProxy;
                        inputStream = null;
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + th.getMessage());
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return null;
                    }
                } catch (UnsupportedEncodingException e12) {
                    unsupportedEncodingException = e12;
                    inputStream = null;
                } catch (IOException e13) {
                    iOException = e13;
                    inputStream = null;
                } catch (JSONException e14) {
                    jSONException = e14;
                    inputStream = null;
                } catch (Throwable th5) {
                    th = th5;
                    inputStream = null;
                }
            } catch (Throwable th6) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e15) {
                        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + e15.getMessage());
                    }
                }
                if (httpURLConnection == null) {
                    throw th6;
                }
                httpURLConnection.disconnect();
                throw th6;
            }
        } catch (UnsupportedEncodingException e16) {
            unsupportedEncodingException = e16;
            httpURLConnection = null;
        } catch (IOException e17) {
            iOException = e17;
            httpURLConnection = null;
        } catch (JSONException e18) {
            jSONException = e18;
            httpURLConnection = null;
        } catch (Throwable th7) {
            th = th7;
            httpURLConnection = null;
        }
        inputStream = null;
        cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + unsupportedEncodingException.getMessage());
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e19) {
                cn.jiguang.w.a.d("JWakeConfigHelper", "request wakeConfig exception:" + e19.getMessage());
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        return null;
    }

    public static void a(Context context, JSONObject jSONObject) {
        if (jSONObject != null) {
            cn.jiguang.w.a.a("JWakeConfigHelper", "write wakeConfigJson:" + jSONObject);
            cn.jiguang.o.c.a(context, "bwc.catch", jSONObject);
        }
    }
}
