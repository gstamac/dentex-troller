package com.zendesk.sdk.network;

import cn.jpush.android.api.JThirdPlatFormInterface;
import com.zendesk.sdk.model.request.UploadResponseWrapper;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/* JADX INFO: loaded from: classes2.dex */
public interface UploadService {
    @DELETE("/api/mobile/uploads/{token}.json")
    Call<Void> deleteAttachment(@Header("Authorization") String str, @Path(JThirdPlatFormInterface.KEY_TOKEN) String str2);

    @POST("/api/mobile/uploads.json")
    Call<UploadResponseWrapper> uploadAttachment(@Header("Authorization") String str, @Query("filename") String str2, @Body RequestBody requestBody);
}
