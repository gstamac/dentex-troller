package com.zendesk.sdk.network;

import com.zendesk.sdk.model.push.PushRegistrationRequestWrapper;
import com.zendesk.sdk.model.push.PushRegistrationResponseWrapper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/* JADX INFO: loaded from: classes2.dex */
public interface PushRegistrationService {
    @POST("/api/mobile/push_notification_devices.json")
    Call<PushRegistrationResponseWrapper> registerDevice(@Header("Authorization") String str, @Body PushRegistrationRequestWrapper pushRegistrationRequestWrapper);

    @DELETE("/api/mobile/push_notification_devices/{id}.json")
    Call<Void> unregisterDevice(@Header("Authorization") String str, @Path("id") String str2);
}
