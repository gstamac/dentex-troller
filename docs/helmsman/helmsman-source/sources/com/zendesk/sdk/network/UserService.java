package com.zendesk.sdk.network;

import com.zendesk.sdk.model.request.UserFieldRequest;
import com.zendesk.sdk.model.request.UserFieldResponse;
import com.zendesk.sdk.model.request.UserResponse;
import com.zendesk.sdk.model.request.UserTagRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/* JADX INFO: loaded from: classes2.dex */
public interface UserService {
    @POST("/api/mobile/user_tags.json")
    Call<UserResponse> addTags(@Header("Authorization") String str, @Body UserTagRequest userTagRequest);

    @DELETE("/api/mobile/user_tags/destroy_many.json")
    Call<UserResponse> deleteTags(@Header("Authorization") String str, @Query("tags") String str2);

    @GET("/api/mobile/users/me.json")
    Call<UserResponse> getUser(@Header("Authorization") String str);

    @GET("/api/mobile/user_fields.json")
    Call<UserFieldResponse> getUserFields(@Header("Authorization") String str);

    @PUT("/api/mobile/users/me.json")
    Call<UserResponse> setUserFields(@Header("Authorization") String str, @Body UserFieldRequest userFieldRequest);
}
