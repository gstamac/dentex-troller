package cn.jiguang.ae;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.net.HttpRequest;
import cn.jiguang.net.HttpResponse;
import cn.jiguang.net.HttpUtils;
import cn.jiguang.net.SSLTrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    public static SSLTrustManager a;

    public static n a(Context context, String str, byte[] bArr, int i, int i2, int i3) {
        try {
            HttpRequest httpRequest = new HttpRequest(str);
            httpRequest.setConnectTimeout(30000);
            httpRequest.setReadTimeout(30000);
            httpRequest.setDoOutPut(true);
            httpRequest.setDoInPut(true);
            httpRequest.setUseCaches(false);
            httpRequest.setBody(bArr);
            httpRequest.setRequestProperty("Content-Length", String.valueOf(bArr.length));
            httpRequest.setNeedRetryIfHttpsFailed(true);
            if (a == null) {
                try {
                    if (!TextUtils.isEmpty("-----BEGIN CERTIFICATE-----\nMIIDjjCCAnagAwIBAgIQAzrx5qcRqaC7KGSxHQn65TANBgkqhkiG9w0BAQsFADBh\nMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\nd3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBH\nMjAeFw0xMzA4MDExMjAwMDBaFw0zODAxMTUxMjAwMDBaMGExCzAJBgNVBAYTAlVT\nMRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5j\nb20xIDAeBgNVBAMTF0RpZ2lDZXJ0IEdsb2JhbCBSb290IEcyMIIBIjANBgkqhkiG\n9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzfNNNx7a8myaJCtSnX/RrohCgiN9RlUyfuI\n2/Ou8jqJkTx65qsGGmvPrC3oXgkkRLpimn7Wo6h+4FR1IAWsULecYxpsMNzaHxmx\n1x7e/dfgy5SDN67sH0NO3Xss0r0upS/kqbitOtSZpLYl6ZtrAGCSYP9PIUkY92eQ\nq2EGnI/yuum06ZIya7XzV+hdG82MHauVBJVJ8zUtluNJbd134/tJS7SsVQepj5Wz\ntCO7TG1F8PapspUwtP1MVYwnSlcUfIKdzXOS0xZKBgyMUNGPHgm+F6HmIcr9g+UQ\nvIOlCsRnKPZzFBQ9RnbDhxSJITRNrw9FDKZJobq7nMWxM4MphQIDAQABo0IwQDAP\nBgNVHRMBAf8EBTADAQH/MA4GA1UdDwEB/wQEAwIBhjAdBgNVHQ4EFgQUTiJUIBiV\n5uNu5g/6+rkS7QYXjzkwDQYJKoZIhvcNAQELBQADggEBAGBnKJRvDkhj6zHd6mcY\n1Yl9PMWLSn/pvtsrF9+wX3N3KjITOYFnQoQj8kVnNeyIv/iPsGEMNKSuIEyExtv4\nNeF22d+mQrvHRAiGfzZ0JFrabA0UWTW98kndth/Jsw1HKj2ZL7tcu7XUIOGZX1NG\nFdtom/DzMNU+MeKNhJ7jitralj41E6Vf8PlwUHBHQRFXGU7Aj64GxJUTFy8bJZ91\n8rGOmaFvE7FBcf6IKshPECBV1/MUReXgRPTqh5Uykw7+U0b6LJ3/iyK5S9kJRaTe\npLiaWN0bfVKfjllDiIGknibVb63dDcY3fe0Dkhvld1927jyNxF1WW6LZZm6zNTfl\nMrY=\n-----END CERTIFICATE-----")) {
                        a = new SSLTrustManager("-----BEGIN CERTIFICATE-----\nMIIDjjCCAnagAwIBAgIQAzrx5qcRqaC7KGSxHQn65TANBgkqhkiG9w0BAQsFADBh\nMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\nd3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBH\nMjAeFw0xMzA4MDExMjAwMDBaFw0zODAxMTUxMjAwMDBaMGExCzAJBgNVBAYTAlVT\nMRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5j\nb20xIDAeBgNVBAMTF0RpZ2lDZXJ0IEdsb2JhbCBSb290IEcyMIIBIjANBgkqhkiG\n9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzfNNNx7a8myaJCtSnX/RrohCgiN9RlUyfuI\n2/Ou8jqJkTx65qsGGmvPrC3oXgkkRLpimn7Wo6h+4FR1IAWsULecYxpsMNzaHxmx\n1x7e/dfgy5SDN67sH0NO3Xss0r0upS/kqbitOtSZpLYl6ZtrAGCSYP9PIUkY92eQ\nq2EGnI/yuum06ZIya7XzV+hdG82MHauVBJVJ8zUtluNJbd134/tJS7SsVQepj5Wz\ntCO7TG1F8PapspUwtP1MVYwnSlcUfIKdzXOS0xZKBgyMUNGPHgm+F6HmIcr9g+UQ\nvIOlCsRnKPZzFBQ9RnbDhxSJITRNrw9FDKZJobq7nMWxM4MphQIDAQABo0IwQDAP\nBgNVHRMBAf8EBTADAQH/MA4GA1UdDwEB/wQEAwIBhjAdBgNVHQ4EFgQUTiJUIBiV\n5uNu5g/6+rkS7QYXjzkwDQYJKoZIhvcNAQELBQADggEBAGBnKJRvDkhj6zHd6mcY\n1Yl9PMWLSn/pvtsrF9+wX3N3KjITOYFnQoQj8kVnNeyIv/iPsGEMNKSuIEyExtv4\nNeF22d+mQrvHRAiGfzZ0JFrabA0UWTW98kndth/Jsw1HKj2ZL7tcu7XUIOGZX1NG\nFdtom/DzMNU+MeKNhJ7jitralj41E6Vf8PlwUHBHQRFXGU7Aj64GxJUTFy8bJZ91\n8rGOmaFvE7FBcf6IKshPECBV1/MUReXgRPTqh5Uykw7+U0b6LJ3/iyK5S9kJRaTe\npLiaWN0bfVKfjllDiIGknibVb63dDcY3fe0Dkhvld1927jyNxF1WW6LZZm6zNTfl\nMrY=\n-----END CERTIFICATE-----");
                    }
                } catch (Throwable unused) {
                }
            }
            SSLTrustManager sSLTrustManager = a;
            if (sSLTrustManager != null) {
                httpRequest.setSslTrustManager(sSLTrustManager);
            }
            httpRequest.setRequestProperty("Accept", "application/jason");
            httpRequest.setRequestProperty("Accept-Encoding", "gzip");
            httpRequest.setRequestProperty("X-App-Key", cn.jiguang.ac.a.a(context));
            httpRequest.setRequestProperty("Charset", "UTF-8");
            String strA = cn.jiguang.ar.d.a(i);
            httpRequest.setHaveRspData(false);
            httpRequest.setRspDatazip(false);
            httpRequest.setRequestProperty("Authorization", "Basic " + k.a(context, cn.jiguang.f.i.a(bArr), strA));
            while (i2 > 0) {
                i2--;
                HttpResponse httpResponseHttpPost = HttpUtils.httpPost(context, httpRequest);
                int responseCode = httpResponseHttpPost.getResponseCode();
                cn.jiguang.ad.d.c("HttpHelper", "status code:" + responseCode + " retry left:" + i2);
                if (responseCode == 200) {
                    return new n(0, httpResponseHttpPost.getResponseBody());
                }
                if (responseCode == 401) {
                    return new n(-3, httpResponseHttpPost.getResponseBody());
                }
                if (responseCode == 404 || responseCode == 410 || responseCode == 429) {
                    return new n(-1, httpResponseHttpPost.getResponseBody());
                }
                if (responseCode == 503) {
                    return new n(-2, httpResponseHttpPost.getResponseBody());
                }
                if (responseCode != 3005) {
                    return responseCode >= 500 ? new n(-1, httpResponseHttpPost.getResponseBody()) : new n(-2, httpResponseHttpPost.getResponseBody());
                }
            }
            return new n(-2, "Failed - retry enough");
        } catch (AssertionError e) {
            return new n(-2, "Catch AssertionError to avoid http close crash - " + e.getMessage());
        } catch (Exception e2) {
            return new n(-2, "Exception - " + e2.getMessage());
        } catch (Throwable th) {
            return new n(-2, "Exception - " + th.getMessage());
        }
    }

    public static n a(String str, String str2, Context context, boolean z, int i, int i2) {
        try {
            try {
                try {
                    byte[] bArrA = cn.jiguang.f.k.a(str2.getBytes("UTF-8"));
                    HttpRequest httpRequest = new HttpRequest(str);
                    httpRequest.setConnectTimeout(30000);
                    httpRequest.setReadTimeout(30000);
                    httpRequest.setDoOutPut(true);
                    httpRequest.setDoInPut(true);
                    httpRequest.setUseCaches(false);
                    String strA = cn.jiguang.ar.d.a(cn.jiguang.ar.d.a());
                    byte[] bArrA2 = cn.jiguang.ar.d.a(bArrA, strA, "0102030405060708", true);
                    httpRequest.setBody(bArrA2);
                    httpRequest.setRequestProperty("Content-Length", String.valueOf(bArrA2.length));
                    httpRequest.setNeedRetryIfHttpsFailed(true);
                    if (a == null) {
                        try {
                            if (!TextUtils.isEmpty("-----BEGIN CERTIFICATE-----\nMIIDjjCCAnagAwIBAgIQAzrx5qcRqaC7KGSxHQn65TANBgkqhkiG9w0BAQsFADBh\nMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\nd3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBH\nMjAeFw0xMzA4MDExMjAwMDBaFw0zODAxMTUxMjAwMDBaMGExCzAJBgNVBAYTAlVT\nMRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5j\nb20xIDAeBgNVBAMTF0RpZ2lDZXJ0IEdsb2JhbCBSb290IEcyMIIBIjANBgkqhkiG\n9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzfNNNx7a8myaJCtSnX/RrohCgiN9RlUyfuI\n2/Ou8jqJkTx65qsGGmvPrC3oXgkkRLpimn7Wo6h+4FR1IAWsULecYxpsMNzaHxmx\n1x7e/dfgy5SDN67sH0NO3Xss0r0upS/kqbitOtSZpLYl6ZtrAGCSYP9PIUkY92eQ\nq2EGnI/yuum06ZIya7XzV+hdG82MHauVBJVJ8zUtluNJbd134/tJS7SsVQepj5Wz\ntCO7TG1F8PapspUwtP1MVYwnSlcUfIKdzXOS0xZKBgyMUNGPHgm+F6HmIcr9g+UQ\nvIOlCsRnKPZzFBQ9RnbDhxSJITRNrw9FDKZJobq7nMWxM4MphQIDAQABo0IwQDAP\nBgNVHRMBAf8EBTADAQH/MA4GA1UdDwEB/wQEAwIBhjAdBgNVHQ4EFgQUTiJUIBiV\n5uNu5g/6+rkS7QYXjzkwDQYJKoZIhvcNAQELBQADggEBAGBnKJRvDkhj6zHd6mcY\n1Yl9PMWLSn/pvtsrF9+wX3N3KjITOYFnQoQj8kVnNeyIv/iPsGEMNKSuIEyExtv4\nNeF22d+mQrvHRAiGfzZ0JFrabA0UWTW98kndth/Jsw1HKj2ZL7tcu7XUIOGZX1NG\nFdtom/DzMNU+MeKNhJ7jitralj41E6Vf8PlwUHBHQRFXGU7Aj64GxJUTFy8bJZ91\n8rGOmaFvE7FBcf6IKshPECBV1/MUReXgRPTqh5Uykw7+U0b6LJ3/iyK5S9kJRaTe\npLiaWN0bfVKfjllDiIGknibVb63dDcY3fe0Dkhvld1927jyNxF1WW6LZZm6zNTfl\nMrY=\n-----END CERTIFICATE-----")) {
                                a = new SSLTrustManager("-----BEGIN CERTIFICATE-----\nMIIDjjCCAnagAwIBAgIQAzrx5qcRqaC7KGSxHQn65TANBgkqhkiG9w0BAQsFADBh\nMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\nd3cuZGlnaWNlcnQuY29tMSAwHgYDVQQDExdEaWdpQ2VydCBHbG9iYWwgUm9vdCBH\nMjAeFw0xMzA4MDExMjAwMDBaFw0zODAxMTUxMjAwMDBaMGExCzAJBgNVBAYTAlVT\nMRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5j\nb20xIDAeBgNVBAMTF0RpZ2lDZXJ0IEdsb2JhbCBSb290IEcyMIIBIjANBgkqhkiG\n9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzfNNNx7a8myaJCtSnX/RrohCgiN9RlUyfuI\n2/Ou8jqJkTx65qsGGmvPrC3oXgkkRLpimn7Wo6h+4FR1IAWsULecYxpsMNzaHxmx\n1x7e/dfgy5SDN67sH0NO3Xss0r0upS/kqbitOtSZpLYl6ZtrAGCSYP9PIUkY92eQ\nq2EGnI/yuum06ZIya7XzV+hdG82MHauVBJVJ8zUtluNJbd134/tJS7SsVQepj5Wz\ntCO7TG1F8PapspUwtP1MVYwnSlcUfIKdzXOS0xZKBgyMUNGPHgm+F6HmIcr9g+UQ\nvIOlCsRnKPZzFBQ9RnbDhxSJITRNrw9FDKZJobq7nMWxM4MphQIDAQABo0IwQDAP\nBgNVHRMBAf8EBTADAQH/MA4GA1UdDwEB/wQEAwIBhjAdBgNVHQ4EFgQUTiJUIBiV\n5uNu5g/6+rkS7QYXjzkwDQYJKoZIhvcNAQELBQADggEBAGBnKJRvDkhj6zHd6mcY\n1Yl9PMWLSn/pvtsrF9+wX3N3KjITOYFnQoQj8kVnNeyIv/iPsGEMNKSuIEyExtv4\nNeF22d+mQrvHRAiGfzZ0JFrabA0UWTW98kndth/Jsw1HKj2ZL7tcu7XUIOGZX1NG\nFdtom/DzMNU+MeKNhJ7jitralj41E6Vf8PlwUHBHQRFXGU7Aj64GxJUTFy8bJZ91\n8rGOmaFvE7FBcf6IKshPECBV1/MUReXgRPTqh5Uykw7+U0b6LJ3/iyK5S9kJRaTe\npLiaWN0bfVKfjllDiIGknibVb63dDcY3fe0Dkhvld1927jyNxF1WW6LZZm6zNTfl\nMrY=\n-----END CERTIFICATE-----");
                            }
                        } catch (Throwable unused) {
                        }
                    }
                    SSLTrustManager sSLTrustManager = a;
                    if (sSLTrustManager != null) {
                        httpRequest.setSslTrustManager(sSLTrustManager);
                    }
                    httpRequest.setRequestProperty("Accept", "application/jason");
                    httpRequest.setRequestProperty("Accept-Encoding", "gzip");
                    httpRequest.setRequestProperty("X-App-Key", cn.jiguang.ac.a.a(context));
                    httpRequest.setHaveRspData(true);
                    httpRequest.setRspDatazip(true);
                    httpRequest.setNeedErrorInput(true);
                    httpRequest.setRequestProperty("FillType", "1");
                    httpRequest.setRequestProperty("Authorization", k.a(strA));
                    httpRequest.setRequestProperty("Charset", "UTF-8");
                    while (i > 0) {
                        i--;
                        HttpResponse httpResponseHttpPost = HttpUtils.httpPost(context, httpRequest);
                        int responseCode = httpResponseHttpPost.getResponseCode();
                        cn.jiguang.ad.d.c("HttpHelper", "status code:" + responseCode + " retry left:" + i);
                        if (responseCode == 200) {
                            return new n(0, httpResponseHttpPost.getResponseBody());
                        }
                        if (responseCode == 401) {
                            return new n(-3, httpResponseHttpPost.getResponseBody());
                        }
                        if (responseCode == 404 || responseCode == 410 || responseCode == 429) {
                            return new n(-1, httpResponseHttpPost.getResponseBody());
                        }
                        if (responseCode == 503) {
                            return new n(-2, httpResponseHttpPost.getResponseBody());
                        }
                        if (responseCode != 3005) {
                            return responseCode >= 500 ? new n(-1, httpResponseHttpPost.getResponseBody()) : new n(-2, httpResponseHttpPost.getResponseBody());
                        }
                    }
                    return new n(-2, "Failed - retry enough");
                } catch (IOException e) {
                    return new n(-2, "zip err:" + e.getMessage());
                }
            } catch (UnsupportedEncodingException e2) {
                return new n(-2, "Exception - " + e2.getMessage());
            }
        } catch (AssertionError e3) {
            return new n(-2, "Catch AssertionError to avoid http close crash - " + e3.getMessage());
        } catch (Exception e4) {
            return new n(-2, "Exception - " + e4.getMessage());
        } catch (Throwable th) {
            return new n(-2, "Exception - " + th.getMessage());
        }
    }
}
