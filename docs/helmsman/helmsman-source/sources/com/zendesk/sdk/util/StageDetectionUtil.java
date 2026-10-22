package com.zendesk.sdk.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.zendesk.logger.Logger;
import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.security.auth.x500.X500Principal;

/* JADX INFO: loaded from: classes2.dex */
public class StageDetectionUtil {
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
    private static final String LOG_TAG = "StageDetectionUtil";

    public static boolean isDebug(Context context) {
        if (context == null) {
            return true;
        }
        return isDebugInternal(context) || checkLogging();
    }

    public static boolean isProduction(Context context) {
        return !isDebug(context);
    }

    private static boolean isDebugInternal(Context context) {
        boolean zCheckDebuggableFlag = checkDebuggableFlag(context);
        boolean zCheckDebugCertificate = checkDebugCertificate(context);
        boolean zCheckBuildFlag = checkBuildFlag(context);
        Logger.d(LOG_TAG, "Debug flag: %s | Debug certificate: %s | Build flag: %s", Boolean.valueOf(zCheckDebuggableFlag), Boolean.valueOf(zCheckDebugCertificate), Boolean.valueOf(zCheckBuildFlag));
        List listAsList = Arrays.asList(Boolean.valueOf(zCheckDebuggableFlag), Boolean.valueOf(zCheckDebugCertificate), Boolean.valueOf(zCheckBuildFlag));
        return Collections.frequency(listAsList, true) >= listAsList.size() - 1;
    }

    private static boolean checkDebuggableFlag(Context context) {
        return (context.getApplicationInfo().flags & 2) != 0;
    }

    private static boolean checkDebugCertificate(Context context) {
        boolean zEquals;
        try {
            Signature[] signatureArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures;
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            if (signatureArr == null) {
                return false;
            }
            zEquals = false;
            for (Signature signature : signatureArr) {
                try {
                    Certificate certificateGenerateCertificate = certificateFactory.generateCertificate(new ByteArrayInputStream(signature.toByteArray()));
                    if ((certificateGenerateCertificate instanceof X509Certificate) && (zEquals = DEBUG_DN.equals(((X509Certificate) certificateGenerateCertificate).getSubjectX500Principal()))) {
                        break;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e = e;
                    Logger.w(LOG_TAG, "Error, not able to read the certificate", e, new Object[0]);
                } catch (CertificateException e2) {
                    e = e2;
                    Logger.w(LOG_TAG, "Error, not able to read the certificate", e, new Object[0]);
                }
            }
            return zEquals;
        } catch (PackageManager.NameNotFoundException | CertificateException e3) {
            e = e3;
            zEquals = false;
        }
        Logger.w(LOG_TAG, "Error, not able to read the certificate", e, new Object[0]);
        return zEquals;
    }

    private static boolean checkBuildFlag(Context context) {
        try {
            return ((Boolean) Class.forName(context.getPackageName() + ".BuildConfig").getField("DEBUG").get(null)).booleanValue();
        } catch (Exception e) {
            Logger.w(LOG_TAG, "Error, not able to receive 'BuildConfig.DEBUG'", e, new Object[0]);
            return false;
        }
    }

    private static boolean checkLogging() {
        return Logger.isLoggable();
    }

    private StageDetectionUtil() {
    }
}
