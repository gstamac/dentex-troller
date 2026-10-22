package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.network.SdkOptions;
import java.util.Collections;
import java.util.List;
import okhttp3.ConnectionSpec;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultSdkOptions implements SdkOptions {
    @Override // com.zendesk.sdk.network.SdkOptions
    public boolean overrideResourceLoadingInWebview() {
        return false;
    }

    @Override // com.zendesk.sdk.network.SdkOptions
    public SdkOptions.ServiceOptions getServiceOptions() {
        return new SdkOptions.ServiceOptions() { // from class: com.zendesk.sdk.network.impl.DefaultSdkOptions.1
            @Override // com.zendesk.sdk.network.SdkOptions.ServiceOptions
            public List<ConnectionSpec> getConnectionSpecs() {
                return Collections.singletonList(ConnectionSpec.MODERN_TLS);
            }
        };
    }
}
