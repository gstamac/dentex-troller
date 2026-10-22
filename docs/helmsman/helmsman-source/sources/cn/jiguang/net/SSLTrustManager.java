package cn.jiguang.net;

import cn.jiguang.ad.d;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/* JADX INFO: loaded from: classes.dex */
public class SSLTrustManager implements X509TrustManager {
    private X509TrustManager a;

    public SSLTrustManager(String str) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
            X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
            byteArrayInputStream.close();
            KeyStore.TrustedCertificateEntry trustedCertificateEntry = new KeyStore.TrustedCertificateEntry(x509Certificate);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setEntry("ca_root", trustedCertificateEntry, null);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    this.a = (X509TrustManager) trustManager;
                    return;
                }
            }
        } catch (Throwable th) {
            d.f("SSLTrustManager", "init trustManager failed, error:" + th);
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
        d.b("SSLTrustManager", "checkClientTrusted");
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        X509Certificate x509Certificate;
        StringBuilder sb;
        String localizedMessage;
        d.b("SSLTrustManager", "checkServerTrusted");
        if (x509CertificateArr == null || x509CertificateArr.length == 0 || (x509Certificate = x509CertificateArr[0]) == null) {
            throw new CertificateException("Check Server x509Certificates is empty");
        }
        try {
            x509Certificate.checkValidity();
        } catch (CertificateExpiredException e) {
            sb = new StringBuilder("checkServerTrusted: CertificateExpiredException:");
            localizedMessage = e.getLocalizedMessage();
            d.f("SSLTrustManager", sb.append(localizedMessage).toString());
        } catch (CertificateNotYetValidException e2) {
            sb = new StringBuilder("checkServerTrusted: CertificateNotYetValidException:");
            localizedMessage = e2.getLocalizedMessage();
            d.f("SSLTrustManager", sb.append(localizedMessage).toString());
        } catch (Throwable th) {
            sb = new StringBuilder("checkServerTrusted failed, error");
            localizedMessage = th.getLocalizedMessage();
            d.f("SSLTrustManager", sb.append(localizedMessage).toString());
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        d.b("SSLTrustManager", "getAcceptedIssuers");
        return this.a.getAcceptedIssuers();
    }
}
