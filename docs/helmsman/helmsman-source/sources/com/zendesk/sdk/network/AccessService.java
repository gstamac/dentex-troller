package com.zendesk.sdk.network;

import com.zendesk.sdk.model.access.AuthenticationRequestWrapper;
import com.zendesk.sdk.model.access.AuthenticationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/* JADX INFO: loaded from: classes2.dex */
public interface AccessService {
    @POST("/access/sdk/jwt")
    Call<AuthenticationResponse> getAuthToken(@Body AuthenticationRequestWrapper authenticationRequestWrapper);

    @POST("/access/sdk/anonymous")
    Call<AuthenticationResponse> getAuthTokenForAnonymous(@Body AuthenticationRequestWrapper authenticationRequestWrapper);
}
