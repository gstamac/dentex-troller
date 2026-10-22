package com.zendesk.sdk.network;

import com.zendesk.sdk.model.settings.MobileSettings;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/* JADX INFO: loaded from: classes2.dex */
public interface SdkSettingsService {
    @GET("/api/mobile/sdk/settings/{applicationId}.json")
    Call<MobileSettings> getSettings(@Header(Constants.ACCEPT_LANGUAGE_HEADER) String str, @Path("applicationId") String str2);
}
