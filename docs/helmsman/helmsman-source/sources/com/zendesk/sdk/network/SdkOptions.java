package com.zendesk.sdk.network;

import java.util.List;
import okhttp3.ConnectionSpec;

/* JADX INFO: loaded from: classes2.dex */
public interface SdkOptions {

    public interface ServiceOptions {
        List<ConnectionSpec> getConnectionSpecs();
    }

    ServiceOptions getServiceOptions();

    boolean overrideResourceLoadingInWebview();
}
