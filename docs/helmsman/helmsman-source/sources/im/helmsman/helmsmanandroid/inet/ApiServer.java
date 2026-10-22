package im.helmsman.helmsmanandroid.inet;

import cn.jpush.android.api.JThirdPlatFormInterface;
import com.facebook.AccessToken;
import com.facebook.places.model.PlaceFields;
import com.google.android.gms.actions.SearchIntents;
import com.google.android.gms.common.Scopes;
import im.helmsman.helmsmanandroid.inet.RequestBean.BindEmailRequest;
import im.helmsman.helmsmanandroid.inet.RequestBean.BindFaceBookRequest;
import im.helmsman.helmsmanandroid.inet.RequestBean.CommentRequest;
import im.helmsman.helmsmanandroid.inet.RequestBean.RegisterRequest;
import im.helmsman.helmsmanandroid.inet.RequestBean.RouteScreenshostRequest;
import im.helmsman.helmsmanandroid.inet.RequestBean.SendFbInfoToServerRequest;
import im.helmsman.helmsmanandroid.inet.RequestBean.SendWxInfoToServerRequest;
import im.helmsman.helmsmanandroid.inet.RequestBean.UploadRegistrationRequestBean;
import im.helmsman.helmsmanandroid.inet.model.AccountSecuriytStateResultModel;
import im.helmsman.helmsmanandroid.inet.model.ActivateResultModel;
import im.helmsman.helmsmanandroid.inet.model.AppVersionInfoResultModel;
import im.helmsman.helmsmanandroid.inet.model.ArticlesResultModel;
import im.helmsman.helmsmanandroid.inet.model.AvatarUrl;
import im.helmsman.helmsmanandroid.inet.model.BindEmailResultModel;
import im.helmsman.helmsmanandroid.inet.model.BindFaceBookResultModel;
import im.helmsman.helmsmanandroid.inet.model.BindWxResultModel;
import im.helmsman.helmsmanandroid.inet.model.CancelFollowUserResultModel;
import im.helmsman.helmsmanandroid.inet.model.ChangePasswordModel;
import im.helmsman.helmsmanandroid.inet.model.ChangePasswordRequestBodyModel;
import im.helmsman.helmsmanandroid.inet.model.CommentContentResultModel;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.inet.model.CommentNotificationResultModel;
import im.helmsman.helmsmanandroid.inet.model.CommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.ContactResltModel;
import im.helmsman.helmsmanandroid.inet.model.DeleteCommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.DeleteMessageModel;
import im.helmsman.helmsmanandroid.inet.model.Description;
import im.helmsman.helmsmanandroid.inet.model.DownloadRouteResultModel;
import im.helmsman.helmsmanandroid.inet.model.FaceBookKeyResultModel;
import im.helmsman.helmsmanandroid.inet.model.FavouriteNotifitcationMessageListResultModel;
import im.helmsman.helmsmanandroid.inet.model.FavouriteRouteModel;
import im.helmsman.helmsmanandroid.inet.model.FindFriendsResultModel;
import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.inet.model.GetAccessTokenResponseModel;
import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;
import im.helmsman.helmsmanandroid.inet.model.GetRouteScreenShostResultModel;
import im.helmsman.helmsmanandroid.inet.model.GetUserInfoByUserNameResultModel;
import im.helmsman.helmsmanandroid.inet.model.JpushQueryTagAndAlias;
import im.helmsman.helmsmanandroid.inet.model.JpushTokenResultModel;
import im.helmsman.helmsmanandroid.inet.model.LaunchImageResponse;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteModel;
import im.helmsman.helmsmanandroid.inet.model.ManualsListResultModel;
import im.helmsman.helmsmanandroid.inet.model.PasswordIsNull;
import im.helmsman.helmsmanandroid.inet.model.PrivatePolicyResultModel;
import im.helmsman.helmsmanandroid.inet.model.QRCodeUrl;
import im.helmsman.helmsmanandroid.inet.model.RegisterResponse;
import im.helmsman.helmsmanandroid.inet.model.ReportCoordinateResultModel;
import im.helmsman.helmsmanandroid.inet.model.RestPasswordResponse;
import im.helmsman.helmsmanandroid.inet.model.SectionResultModel;
import im.helmsman.helmsmanandroid.inet.model.SendWXInfoToServerResultModel;
import im.helmsman.helmsmanandroid.inet.model.ShareWaypointBody;
import im.helmsman.helmsmanandroid.inet.model.TokenResponse;
import im.helmsman.helmsmanandroid.inet.model.UpLoadImageTokenResponse;
import im.helmsman.helmsmanandroid.inet.model.UploadRegistaionIdResultModel;
import im.helmsman.helmsmanandroid.inet.model.UserInfoResultModel;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.inet.model.UserPagination;
import im.helmsman.helmsmanandroid.inet.model.UserRelationship;
import im.helmsman.helmsmanandroid.inet.model.WXUserInfoResponseModel;
import im.helmsman.helmsmanandroid.inet.model.ZendeskManualsListModelResult;
import java.util.Objects;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/* JADX INFO: loaded from: classes2.dex */
public interface ApiServer {
    @PUT("trolling_motor/activate")
    Call<ActivateResultModel> activateRequest(@Query("ssid") String str, @Query("firmware") String str2, @Query("trolling_motor") String str3);

    @PUT("users/me/email")
    Call<BindEmailResultModel> bindEmail(@Body BindEmailRequest bindEmailRequest);

    @POST("user/facebook/link")
    Call<BindFaceBookResultModel> bindFaceBook(@Body BindFaceBookRequest bindFaceBookRequest);

    @POST("user/wechat/link")
    Call<BindWxResultModel> bindWeiXin(@Body SendWxInfoToServerRequest sendWxInfoToServerRequest);

    @POST("users/me/unfollow")
    Call<CancelFollowUserResultModel> cancelFollowUserByUserId(@Query("id") int i);

    @PUT("users/me/password")
    Call<ChangePasswordModel> changePassword(@Body ChangePasswordRequestBodyModel changePasswordRequestBodyModel);

    @DELETE("comments/{id}")
    Call<DeleteCommentResultModel> deleteCommentById(@Path("id") int i);

    @DELETE("messages/{messages_id}")
    Call<DeleteMessageModel> deleteMessageByMessageId(@Path("messages_id") int i);

    @GET("routes/{id}/download")
    Call<DownloadRouteResultModel> downLoadRouteById(@Path("id") int i);

    @GET("users/search")
    Call<FindFriendsResultModel> findFriendsByUserName(@Query("username") String str, @Query("since_id") int i, @Query(PlaceFields.PAGE) int i2);

    @POST("users/me/follow")
    Call<FollowerUserModel> follow(@Query("id") int i);

    @GET("access_token")
    Call<GetAccessTokenResponseModel> getAccessToken(@Query("appid") String str, @Query("secret") String str2, @Query(JThirdPlatFormInterface.KEY_CODE) String str3, @Query("grant_type") String str4);

    @GET("users/me/link")
    Call<AccountSecuriytStateResultModel> getAccountSecuriytState();

    @GET("app/android/version")
    Call<AppVersionInfoResultModel> getAppVersionInfo();

    @GET("api/v2/help_center/{locale}/sections/{id}/articles.json")
    Call<ArticlesResultModel> getArticles(@Path("locale") String str, @Path("id") int i);

    @GET("users/{id}/avatar")
    Call<AvatarUrl> getAvatarById(@Path("id") int i);

    @GET("messages/{message_id}/comments/")
    Call<CommentListModel> getCommentListByMessageId(@Path("message_id") int i, @Query("since_id") int i2, @Query(PlaceFields.PAGE) int i3);

    @GET("users/me/following/short_info/")
    Call<ContactResltModel> getContacts();

    @GET("facebook/secret")
    Call<FaceBookKeyResultModel> getFaceBookKey();

    @GET("users/{id}/followed/")
    Call<UserPagination<UserRelationship>> getFollowedById(@Path("id") int i);

    @GET("users/{user_id}/timeline/messages")
    Call<LikeRouteListModel> getFollowedMessageByUserId(@Path(AccessToken.USER_ID_KEY) int i);

    @GET("users/{id}/followers/")
    Call<UserPagination<UserRelationship>> getFollowersById(@Path("id") int i);

    @GET("users/me/messages/favourited")
    Call<FavouriteNotifitcationMessageListResultModel> getFravouriteMessageNotifitcations(@Query("since_id") int i, @Query(PlaceFields.PAGE) int i2);

    @GET("users/{user_id}/messages/")
    Call<GetFriendsMessageResultModel> getFrinedsMessageByUserId(@Path(AccessToken.USER_ID_KEY) int i, @Query("since_id") int i2, @Query(PlaceFields.PAGE) int i3);

    @GET("v3/devices/{registration_id}")
    Call<JpushQueryTagAndAlias> getJpushTagAndAlias(@Header("Authorization") String str, @Path("registration_id") String str2);

    @GET("user/token/jpush")
    Call<JpushTokenResultModel> getJpushToken();

    @GET("launch_image")
    Call<LaunchImageResponse> getLaunchImageUrl();

    @POST("route/screenshot/fetch")
    Call<GetRouteScreenShostResultModel> getLocalRouteScreenShost(@Body RouteScreenshostRequest routeScreenshostRequest);

    @GET("product_categories/{product_category_id}/user_manuals/")
    Call<ManualsListResultModel> getManualsList(@Path("product_category_id") int i);

    @GET("messages/{msg_id}")
    Call<CommentContentResultModel> getMessageByMsgId(@Path(JThirdPlatFormInterface.KEY_MSG_ID) int i);

    @GET("users/me/favourite/routes/")
    Call<FavouriteRouteModel> getMyFavouriteRoute(@Query(PlaceFields.PAGE) int i, @Query("since_id") int i2);

    @GET("users/{id}/following/")
    Call<GetMyFollowerResultModel> getMyFolloing(@Path("id") int i, @Query(PlaceFields.PAGE) int i2);

    @GET("users/me/followed/")
    Call<UserPagination<UserRelationship>> getMyFollowed();

    @GET("users/me/timeline/messages/")
    Call<LikeRouteListModel> getMyFollowedMessage(@Query("since_id") int i, @Query(PlaceFields.PAGE) int i2);

    @GET("users/{id}/followers/")
    Call<GetMyFollowerResultModel> getMyFollower(@Path("id") int i, @Query(PlaceFields.PAGE) int i2);

    @GET("users/me/followers/")
    Call<UserPagination<UserRelationship>> getMyFollowers();

    @GET("users/me/messages/")
    Call<LikeRouteListModel> getMyMessage(@Query(PlaceFields.PAGE) int i, @Query("since_id") int i2);

    @GET("users/me/follow/qrcode")
    Call<QRCodeUrl> getMyQRcodeUrl();

    @GET("users/me/messages/comments/")
    Call<CommentNotificationResultModel> getNotificationComments(@Query("since_id") int i, @Query(PlaceFields.PAGE) int i2);

    @GET("users/me/password")
    Call<PasswordIsNull> getPasswordIsNUll();

    @GET("privacy_policy")
    Call<PrivatePolicyResultModel> getPrivatePolicyInfo(@Query("language") String str);

    @GET("api/v2/help_center/{locale}/categories/{id}/sections.json")
    Call<SectionResultModel> getSections(@Path("locale") String str, @Path("id") int i);

    @GET("users/me")
    Call<UserMe> getUserInfo();

    @GET("users/{id}")
    Call<UserInfoResultModel> getUserInfoById(@Path("id") int i);

    @GET("user")
    Call<GetUserInfoByUserNameResultModel> getUserInfoByUserName(@Query("username") String str);

    @GET("userinfo")
    Call<WXUserInfoResponseModel> getWxUserInfo(@Query("access_token") String str, @Query(Scopes.OPEN_ID) String str2);

    @POST("messages/favourites/routes/{route_id}")
    Call<LikeRouteModel> likeRoute(@Path("route_id") int i, @Body Description description);

    @GET(JThirdPlatFormInterface.KEY_TOKEN)
    Call<TokenResponse> login(@Header("Authorization") String str);

    @POST("users")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("trolling_motor/coordinate")
    Call<ReportCoordinateResultModel> reportCoordinate(@Query("longitude") String str, @Query("latitude") String str2, @Query("trolling_motor") int i, @Query("firmware") int i2, @Query("ssid") String str3);

    @GET("users/me/avatar/token")
    Call<UpLoadImageTokenResponse> requestUpLoadImageToken();

    @GET("users/password/reset")
    Call<RestPasswordResponse> resetPassword(@Query("email") String str);

    @GET("api/v2/help_center/articles/search.json")
    Call<ZendeskManualsListModelResult> searchZendeskManualsList(@Query(SearchIntents.EXTRA_QUERY) String str);

    @POST("messages/{message_id}/comments")
    Call<CommentResultModel> sendComment(@Path("message_id") int i, @Body CommentRequest commentRequest);

    @POST("user/facebook")
    Call<SendWXInfoToServerResultModel> sendFUserInfoTobServer(@Body SendFbInfoToServerRequest sendFbInfoToServerRequest);

    @POST("user/wechat")
    Call<SendWXInfoToServerResultModel> sendWxInfoToServer(@Body SendWxInfoToServerRequest sendWxInfoToServerRequest);

    @Headers({"Content-Type:application/json"})
    @POST("messages/routes")
    Call<Object> shareMyWaypoint(@Body ShareWaypointBody shareWaypointBody);

    @POST("users/me/unfollow")
    Call<Objects> unfollow(@Query("id") int i);

    @PUT("users/me")
    Call<UserMe> updateUserAccountInfo(@Body UserMe userMe);

    @POST("log")
    @Multipart
    Call<ResponseBody> uploadLogFile(@Query("ssid") String str, @Query("filename") String str2, @Part("description") RequestBody requestBody, @Part MultipartBody.Part part);

    @PUT("users/registrationID")
    Call<UploadRegistaionIdResultModel> uploadRegistration(@Body UploadRegistrationRequestBean uploadRegistrationRequestBean);
}
