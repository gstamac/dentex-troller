package com.amap.api.mapcore.util;

import android.content.Context;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/* JADX INFO: compiled from: Utils.java */
/* JADX INFO: loaded from: classes.dex */
public class gr {
    static PublicKey a() {
        ByteArrayInputStream byteArrayInputStream;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            byteArrayInputStream = new ByteArrayInputStream(fb.b("MIIDRzCCAi+gAwIBAgIEeuDbsDANBgkqhkiG9w0BAQsFADBTMQswCQYDVQQGEwJjbjELMAkGA1UECBMCYmoxCzAJBgNVBAcTAmJqMQ0wCwYDVQQKEwRvcGVuMQ4wDAYDVQQLEwVnYW9kZTELMAkGA1UEAxMCUWkwIBcNMTYwODAxMDE0ODMwWhgPMjA3MTA1MDUwMTQ4MzBaMFMxCzAJBgNVBAYTAmNuMQswCQYDVQQIEwJiajELMAkGA1UEBxMCYmoxDTALBgNVBAoTBG9wZW4xDjAMBgNVBAsTBWdhb2RlMQswCQYDVQQDEwJRaTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKpL13mZm4q6AFP5csQE7130Lwq8m+HICy3rBARd9vbw5Cb1wFF96KdhC5P/aASlrPb+6MSyP1nE97p3ygKJWsgxExyvVuOvh1KUqOFuK15oY7JKTk6L4eLCbkBJZV2DLffpW0HGiRpmFG8LJR0sjNOoubSd5R/6XoBwyRglsyVHprjrK2qDRvT3Edgtfvxp4HnUzMsDD3CJRtgsaDw6ECyF7fhYKEz9I6OEEVsPlpbgzRmhSeFDL77/k1mhPve1ZyKGlPcxvSSdLSAlV0hzr5NKlujHll7BbouwDnr6l/0O44AzZ0V/ieft1iBkSLirnlm56uI/8jdh8ANrD1fW4ZUCAwEAAaMhMB8wHQYDVR0OBBYEFBzudtI5UKRvHGDV+VQRzItIj3PqMA0GCSqGSIb3DQEBCwUAA4IBAQBS2EGndgvIBnf7Ce4IhDbm7F5h4L+6TYGmT9acnQbEFY8oUoFblMDgg+cETT44jU/elwbJJVmKhj/WRQl+AdSALBAgDvxq1AcjlGg+c8H3pa2BWlrxNJP9MFLIEI5bA8m5og/Epjut50uemZ9ggoNmJeW0N/a6D8euhYJKOYngUQqDu6cwLj1Ec0ptwrNRbvRXXgzjfJMPE/ii4K/b8JZ+QN2d/bl7QEvKWBSzVueZifV659qAbMh6C9TCVstWWfV53Z3Vyt+duDNU5ed7aWao42Ppw4VHslrJW0V6BXDUhhzgXx28UWY78W7LmYGCtC8PfDId2+k4tPoTNPM6HHP5"));
            try {
                PublicKey publicKey = ((X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream)).getPublicKey();
                try {
                    a(byteArrayInputStream);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                return publicKey;
            } catch (Throwable th2) {
                th = th2;
                try {
                    a(th, "DyLoader", "init");
                    return null;
                } finally {
                    try {
                        a(byteArrayInputStream);
                    } catch (Throwable th3) {
                        th3.printStackTrace();
                    }
                }
            }
        } catch (Throwable th4) {
            th = th4;
            byteArrayInputStream = null;
        }
    }

    public static int a(String str, String str2) {
        try {
            String[] strArrSplit = str.split("\\.");
            String[] strArrSplit2 = str2.split("\\.");
            int iMin = Math.min(strArrSplit.length, strArrSplit2.length);
            int length = 0;
            for (int i = 0; i < iMin; i++) {
                length = strArrSplit[i].length() - strArrSplit2[i].length();
                if (length != 0 || (length = strArrSplit[i].compareTo(strArrSplit2[i])) != 0) {
                    break;
                }
            }
            return length != 0 ? length : strArrSplit.length - strArrSplit2.length;
        } catch (Exception e) {
            fk.a(e, "Utils", "compareVersion");
            return -1;
        }
    }

    static void a(List<gp> list) {
        int i = 0;
        while (i < list.size() - 1) {
            int i2 = i + 1;
            for (int i3 = i2; i3 < list.size(); i3++) {
                gp gpVar = list.get(i);
                gp gpVar2 = list.get(i3);
                if (a(gpVar2.e(), gpVar.e()) > 0) {
                    list.set(i, gpVar2);
                    list.set(i3, gpVar);
                }
            }
            i = i2;
        }
    }

    static boolean b(String str, String str2) {
        String strA = fc.a(str);
        return strA != null && strA.equalsIgnoreCase(str2);
    }

    static boolean a(Context context, ft ftVar, String str, ff ffVar) {
        return a(ftVar, str, gl.a(context, str), ffVar);
    }

    static boolean a(ft ftVar, String str, String str2, ff ffVar) {
        gp gpVarA = gl.a.a(ftVar, str);
        return gpVarA != null && ffVar.b().equals(gpVarA.d()) && b(str2, gpVarA.b());
    }

    static void a(Throwable th, String str, String str2) {
        fk.a(th, str, str2);
    }

    static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
