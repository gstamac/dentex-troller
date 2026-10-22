package com.zendesk.sdk.util;

import android.content.Context;
import com.zendesk.sdk.network.impl.ApplicationScope;
import java.util.List;
import java.util.Locale;
import okhttp3.ConnectionSpec;

/* JADX INFO: loaded from: classes2.dex */
public class BaseInjector {
    public static String injectUrl(ApplicationScope applicationScope) {
        return applicationScope.getUrl();
    }

    public static String injectAppId(ApplicationScope applicationScope) {
        return applicationScope.getAppId();
    }

    public static String injectOAuthToken(ApplicationScope applicationScope) {
        return applicationScope.getOAuthToken();
    }

    public static Context injectApplicationContext(ApplicationScope applicationScope) {
        return applicationScope.getApplicationContext();
    }

    public static List<ConnectionSpec> injectConnectionSpec(ApplicationScope applicationScope) {
        return applicationScope.getSdkOptions().getServiceOptions().getConnectionSpecs();
    }

    public static Locale injectLocale(ApplicationScope applicationScope) {
        return applicationScope.getLocale();
    }

    public static String injectUserAgentHeader(ApplicationScope applicationScope) {
        return applicationScope.getUserAgentHeader();
    }
}
