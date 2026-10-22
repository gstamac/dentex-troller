package com.zendesk.sdk.network;

import com.zendesk.sdk.model.request.CommentsResponse;
import com.zendesk.sdk.model.request.CreateRequestWrapper;
import com.zendesk.sdk.model.request.RequestResponse;
import com.zendesk.sdk.model.request.RequestsResponse;
import com.zendesk.sdk.model.request.UpdateRequestWrapper;
import com.zendesk.sdk.model.request.fields.RawTicketFormResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/* JADX INFO: loaded from: classes2.dex */
public interface RequestService {
    @PUT("/api/mobile/requests/{id}.json")
    Call<UpdateRequestWrapper> addComment(@Header("Authorization") String str, @Path("id") String str2, @Body UpdateRequestWrapper updateRequestWrapper);

    @POST("/api/mobile/requests.json")
    Call<RequestResponse> createRequest(@Header("Authorization") String str, @Header(Constants.SDK_GUID_HEADER) String str2, @Body CreateRequestWrapper createRequestWrapper);

    @GET("/api/mobile/requests.json?sort_by=updated_at&sort_order=desc")
    Call<RequestsResponse> getAllRequests(@Header("Authorization") String str, @Query("status") String str2, @Query("include") String str3);

    @GET("/api/mobile/requests/{id}/comments.json?sort_by=updated_at&sort_order=desc")
    Call<CommentsResponse> getComments(@Header("Authorization") String str, @Path("id") String str2);

    @GET("/api/mobile/requests/show_many.json?sort_by=updated_at&sort_order=desc")
    Call<RequestsResponse> getManyRequests(@Header("Authorization") String str, @Query("tokens") String str2, @Query("status") String str3, @Query("include") String str4);

    @GET("/api/mobile/requests/{id}.json")
    Call<RequestResponse> getRequest(@Header("Authorization") String str, @Path("id") String str2);

    @GET("/api/v2/ticket_forms/show_many.json?active=true")
    Call<RawTicketFormResponse> getTicketFormsById(@Header("Authorization") String str, @Header(Constants.ACCEPT_LANGUAGE_HEADER) String str2, @Query("ids") String str3, @Query("include") String str4);
}
