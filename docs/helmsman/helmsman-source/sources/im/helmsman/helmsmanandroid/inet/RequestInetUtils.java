package im.helmsman.helmsmanandroid.inet;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.amap.api.maps.model.MyLocationStyle;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.places.model.PlaceFields;
import com.google.gson.Gson;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.dao.WayPoint;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.DowloadCompleteMessage;
import im.helmsman.helmsmanandroid.factory.OkHttpFactory;
import im.helmsman.helmsmanandroid.factory.RetrofitFactory;
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
import im.helmsman.helmsmanandroid.inet.model.Description;
import im.helmsman.helmsmanandroid.inet.model.DownloadRouteResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
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
import im.helmsman.helmsmanandroid.utils.FileUtils;
import im.helmsman.helmsmanandroid.utils.LogUtil;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.UIThreadUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javy.lib.jsave.JSave;
import okhttp3.Call;
import okhttp3.Request;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class RequestInetUtils {
    private static RequestInetUtils requestInetUtils;
    private RetrofitFactory factory = RetrofitFactory.builder();

    public static RequestInetUtils instance() {
        if (requestInetUtils == null) {
            synchronized (RequestInetUtils.class) {
                if (requestInetUtils == null) {
                    requestInetUtils = new RequestInetUtils();
                }
            }
        }
        return requestInetUtils;
    }

    public void downloadLaunchImage() {
        this.factory.getServer(RetrofitFactory.I20).getLaunchImageUrl().enqueue(new Callback<LaunchImageResponse>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<LaunchImageResponse> response) {
                MyApplication.lauchImageUrl = response.body().getUrl();
                String str = (String) JSave.get("lauchimagename");
                if (str == null || !str.equals(response.body().getName())) {
                    JSave.put("lauchimagename", response.body().getName());
                    OkHttpUtils.get().url(response.body().getUrl()).build().execute(new BitmapCallback() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.1.1
                        @Override // com.zhy.http.okhttp.callback.Callback
                        public void onError(Call call, Exception exc, int i) {
                        }

                        @Override // com.zhy.http.okhttp.callback.Callback
                        public void onResponse(Bitmap bitmap, int i) {
                        }
                    });
                }
            }
        });
    }

    public void activateRequest(final String str, final String str2, final String str3) {
        Log.e("activateRequest", str + str2 + str3);
        this.factory.getServer(RetrofitFactory.I22).activateRequest(str, str2, str3).enqueue(new Callback<ActivateResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<ActivateResultModel> response) {
                Log.e("activateRequest", new Gson().toJson(response.body()));
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                String str4;
                if (errorCode.getError_code() == 4011) {
                    LocationManager locationManager = (LocationManager) MyApplication.getInstance().getSystemService(PlaceFields.LOCATION);
                    List<String> providers = locationManager.getProviders(true);
                    String str5 = "gps";
                    String str6 = null;
                    if (!providers.contains("gps")) {
                        str5 = "network";
                        if (!providers.contains("network")) {
                            Log.e("reportError", "没有可用的位置提供器");
                            str5 = null;
                        }
                    }
                    if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(MyApplication.getContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                        str4 = null;
                    } else {
                        Location lastKnownLocation = locationManager.getLastKnownLocation(str5);
                        str6 = lastKnownLocation.getLatitude() + "";
                        str4 = lastKnownLocation.getLongitude() + "";
                    }
                    RequestInetUtils.this.reportDinate(TextUtils.isEmpty(str4) ? AppEventsConstants.EVENT_PARAM_VALUE_NO : str4, TextUtils.isEmpty(str6) ? AppEventsConstants.EVENT_PARAM_VALUE_NO : str6, Integer.valueOf(str3).intValue(), Integer.valueOf(str2).intValue(), str);
                }
            }
        });
    }

    public void reportDinate(String str, String str2, int i, int i2, String str3) {
        this.factory.getServer(RetrofitFactory.I22).reportCoordinate(str, str2, i, i2, str3).enqueue(new Callback<ReportCoordinateResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<ReportCoordinateResultModel> response) {
                Log.e("reportDinate", new Gson().toJson(response.body()));
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Log.e("reportDinate", new Gson().toJson(errorCode));
            }
        });
    }

    public void registerUser(String str, String str2, String str3, Callback<RegisterResponse> callback) throws Throwable {
        if (!StringUtils.isEmail(str2)) {
            callback.onFailure(null, new Throwable("邮箱不合法"));
            return;
        }
        if (!StringUtils.isHaveSpecialCharacters(str)) {
            callback.onFailure(null, new Throwable("用户名不合法,用户名只能由数字和字母组成"));
            return;
        }
        if (str3 == null) {
            callback.onFailure(null, new Throwable("密码不能为空"));
            return;
        }
        if (str3.length() < 8) {
            callback.onFailure(null, new Throwable("密码至少由八位字母或数字组成"));
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("username", str);
            jSONObject.put("email", str2);
            jSONObject.put("password", str3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.factory.getServer(RetrofitFactory.I21).registerUser((RegisterRequest) new Gson().fromJson(jSONObject.toString(), RegisterRequest.class)).enqueue(callback);
    }

    public void resetPassword(String str, Callback<RestPasswordResponse> callback) throws Throwable {
        if (!StringUtils.isEmail(str)) {
            callback.onFailure(null, new Throwable("邮箱不合法"));
        } else {
            this.factory.getServer(RetrofitFactory.I21).resetPassword(str).enqueue(callback);
        }
    }

    public void login(String str, String str2, final Callback<TokenResponse> callback) throws Throwable {
        if (!StringUtils.isEmail(str)) {
            callback.onFailure(null, new Exception("邮箱不合法"));
            return;
        }
        if (str2 == null) {
            callback.onFailure(null, new Exception("密码不能为空"));
        } else if (str2.length() < 8) {
            callback.onFailure(null, new Exception("密码至少由八位字母或数字组成"));
        } else {
            this.factory.getServer(RetrofitFactory.I21).login(encodeCredentialsForBasicAuthorization(str, str2)).enqueue(new Callback<TokenResponse>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.4
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<TokenResponse> response) {
                    String token = response.body().getToken();
                    Callback callback2 = callback;
                    if (callback2 == null || token == null) {
                        return;
                    }
                    callback2.onResponse(response);
                    JSave.put(JThirdPlatFormInterface.KEY_TOKEN, token);
                    RequestInetUtils.this.getUserInfo(new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.4.1
                        @Override // im.helmsman.helmsmanandroid.inet.Callback
                        public void onResponse(Response<UserMe> response2) {
                            RequestInetUtils.instance().uploadRegistraionId();
                        }

                        @Override // im.helmsman.helmsmanandroid.inet.Callback
                        public void onFailure(ErrorCode errorCode) {
                            RequestInetUtils.this.getUserInfo(this);
                        }
                    });
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    Callback callback2 = callback;
                    if (callback2 != null) {
                        callback2.onFailure(errorCode);
                    }
                }
            });
        }
    }

    public void uploadImage(final String str, final Callback<UpLoadImageTokenResponse> callback, final UpCompletionHandler upCompletionHandler) {
        this.factory.getServer(RetrofitFactory.I22).requestUpLoadImageToken().enqueue(new Callback<UpLoadImageTokenResponse>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.5
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<UpLoadImageTokenResponse> response) {
                new UploadManager().put(new File(str), response.body().getKey(), response.body().getUpload_token(), new UpCompletionHandler() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.5.1
                    @Override // com.qiniu.android.storage.UpCompletionHandler
                    public void complete(String str2, ResponseInfo responseInfo, JSONObject jSONObject) {
                        upCompletionHandler.complete(str2, responseInfo, jSONObject);
                    }
                }, (UploadOptions) null);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onFailure(errorCode);
                }
            }
        });
    }

    public void uploadImage(final Bitmap bitmap, final Callback<UpLoadImageTokenResponse> callback, final UpCompletionHandler upCompletionHandler) {
        this.factory.getServer(RetrofitFactory.I21).requestUpLoadImageToken().enqueue(new Callback<UpLoadImageTokenResponse>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.6
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<UpLoadImageTokenResponse> response) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                new UploadManager().put(byteArrayOutputStream.toByteArray(), response.body().getKey(), response.body().getUpload_token(), new UpCompletionHandler() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.6.1
                    @Override // com.qiniu.android.storage.UpCompletionHandler
                    public void complete(String str, ResponseInfo responseInfo, JSONObject jSONObject) {
                        upCompletionHandler.complete(str, responseInfo, jSONObject);
                    }
                }, (UploadOptions) null);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                callback.onFailure(errorCode);
            }
        });
    }

    private String encodeCredentialsForBasicAuthorization(String str, String str2) {
        return "Basic " + Base64.encodeToString((str + ":" + str2).getBytes(), 2);
    }

    public void getUserInfo(final Callback<UserMe> callback) {
        if (UserMe.isLogin()) {
            if (JSave.get("jpush_appkey") == null || JSave.get("jpush_mastersecret") == null) {
                getJpushToken();
            }
            this.factory.getServer(RetrofitFactory.I21).getUserInfo().enqueue(new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.7
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<UserMe> response) {
                    Log.e("Userinfo", new Gson().toJson(response.body()));
                    JSave.put("UserInfo", response.body());
                    Callback callback2 = callback;
                    if (callback2 != null) {
                        callback2.onResponse(response);
                    }
                    if (callback == null) {
                        RequestInetUtils.this.uploadRegistraionId();
                        RequestInetUtils.instance().getJpushToken();
                    }
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    try {
                        if (errorCode.getMessage().equals("please send your authentication token")) {
                            JSave.delete("UserInfo");
                        }
                    } catch (NullPointerException unused) {
                    }
                    Callback callback2 = callback;
                    if (callback2 != null) {
                        callback2.onFailure(errorCode);
                    }
                }
            });
        }
    }

    public void updateUserInfo(UserMe userMe, Callback<UserMe> callback) {
        this.factory.getServer(RetrofitFactory.I21).updateUserAccountInfo(userMe).enqueue(callback);
    }

    public void getMyFollowers(Callback<UserPagination<UserRelationship>> callback) {
        this.factory.getServer(RetrofitFactory.I21).getMyFollowers().enqueue(callback);
    }

    public void getFollowersById(int i, Callback<UserPagination<UserRelationship>> callback) {
        this.factory.getServer(RetrofitFactory.I21).getFollowersById(i).enqueue(callback);
    }

    public void followUser(int i, Callback<FollowerUserModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).follow(i).enqueue(callback);
    }

    public void getAvatarById(int i, Callback callback) {
        this.factory.getServer(RetrofitFactory.I21).getAvatarById(i).enqueue(callback);
    }

    public void getFollowedMessageByUserId(int i, Callback<LikeRouteListModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getFollowedMessageByUserId(i).enqueue(callback);
    }

    public void getMyFollowedMessage(int i, int i2, Callback<LikeRouteListModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getMyFollowedMessage(i, i2).enqueue(callback);
    }

    public void getMyAvatarBitmap(String str, final Callback<Bitmap> callback) {
        try {
            new URL("avatarUrl");
            if (TextUtils.isEmpty(str)) {
                return;
            }
            OkHttpFactory.getInstance().getHttpClient(OkHttpFactory.TYPE_HTTPS).newCall(new Request.Builder().get().url(str).build()).enqueue(new okhttp3.Callback() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.8
                @Override // okhttp3.Callback
                public void onFailure(Call call, final IOException iOException) {
                    UIThreadUtil.runOnUiThread(new Runnable() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (callback != null) {
                                callback.onFailure(new ErrorCode(iOException.getMessage()));
                            }
                        }
                    });
                }

                @Override // okhttp3.Callback
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                    UIThreadUtil.runOnUiThread(new Runnable() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.8.2
                        @Override // java.lang.Runnable
                        public void run() {
                            byte[] bArrBytes = new byte[0];
                            try {
                                bArrBytes = response.body().bytes();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (callback != null) {
                                callback.onResponse(Response.success(BitmapFactory.decodeByteArray(bArrBytes, 0, bArrBytes.length)));
                            }
                        }
                    });
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void upLogFile() {
        LogUtil.instance().closeLogFileStream();
        FileUtils.zipFileList(Environment.getExternalStorageDirectory().getPath() + "/helmsman");
        LogUtil.instance().initLogFileStream();
        File[] fileArrListFiles = new File(Environment.getExternalStorageDirectory().getPath() + "/helmsman").listFiles();
        if (fileArrListFiles == null) {
            return;
        }
        for (int i = 0; i < fileArrListFiles.length; i++) {
            if (fileArrListFiles[i].getName().endsWith(".gz")) {
                upLoadFile(fileArrListFiles[i]);
            }
        }
    }

    public void upLoadFile(final File file) {
        String str = MyApplication.getInstance().ssid;
        if (str == null || str.equals("")) {
            return;
        }
        String str2 = RetrofitFactory.getBaseUrl20() + "log?ssid=" + str + "&filename=" + file.getName().replace(".gz", "");
        Log.e("upLoadFile_path", str2);
        OkHttpUtils.postFile().file(file).addHeader("Content-Encoding", "gzip").addHeader(Client.ContentTypeHeader, "application/x-gzip").addHeader("Content-Length", file.length() + "").url(str2).build().execute(new StringCallback() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.9
            @Override // com.zhy.http.okhttp.callback.Callback
            public void onError(Call call, Exception exc, int i) {
                call.request().body();
            }

            @Override // com.zhy.http.okhttp.callback.Callback
            public void onResponse(String str3, int i) {
                file.delete();
            }
        });
    }

    public void shareMyWayPoint(String str, String str2, final RouteModel routeModel, final Callback callback) {
        List<WayPoint> waypoint = routeModel.getWaypoint();
        ArrayList arrayList = new ArrayList();
        final String string = UUID.randomUUID().toString();
        for (int i = 0; i < waypoint.size(); i++) {
            WayPoint wayPoint = waypoint.get(i);
            wayPoint.setSequence(i);
            arrayList.add(wayPoint);
        }
        if (str == null) {
            str = "No Name";
        }
        ShareWaypointBody shareWaypointBody = new ShareWaypointBody(str, str2, arrayList);
        shareWaypointBody.setIdentifier(string);
        this.factory.getServer(RetrofitFactory.I22).shareMyWaypoint(shareWaypointBody).enqueue(new Callback<Object>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.10
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<Object> response) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("identifier", string);
                DataSupport.update(RouteModel.class, contentValues, routeModel.getId());
                callback.onResponse(response);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                callback.onFailure(errorCode);
            }
        });
    }

    public void getMyMessage(int i, int i2, Callback<LikeRouteListModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getMyMessage(i, i2).enqueue(callback);
    }

    public void changePassword(String str, String str2, Callback<ChangePasswordModel> callback) {
        ChangePasswordRequestBodyModel changePasswordRequestBodyModel = new ChangePasswordRequestBodyModel();
        changePasswordRequestBodyModel.setNewX(str2);
        changePasswordRequestBodyModel.setOld(str);
        this.factory.getServer(RetrofitFactory.I22).changePassword(changePasswordRequestBodyModel).enqueue(callback);
    }

    public void likeRouteById(int i, String str, Callback<LikeRouteModel> callback) {
        Description description = new Description();
        description.setDescription(str);
        this.factory.getServer(RetrofitFactory.I22).likeRoute(i, description).enqueue(callback);
    }

    public void getMyFavouriteRoute(int i, int i2, Callback<FavouriteRouteModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getMyFavouriteRoute(i, i2).enqueue(callback);
    }

    public void sendComment(int i, String str, Callback<CommentResultModel> callback) {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setComment(str);
        this.factory.getServer(RetrofitFactory.I22).sendComment(i, commentRequest).enqueue(callback);
    }

    public void getAllCommentByMessageId(int i, int i2, int i3, Callback<CommentListModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getCommentListByMessageId(i, i2, i3).enqueue(callback);
    }

    public void uploadRegistraionId() {
        if (UserMe.isLogin() && UserMe.getUser() != null && Config.getPushSetting() == null) {
            JPushInterface.setAlias(MyApplication.getContext(), UserMe.getUser().getId() + "", (TagAliasCallback) null);
            HashSet hashSet = new HashSet();
            hashSet.add(isZh() ? "CHN" : "ENG");
            if (MyApplication.getInstance().isApkInDebug()) {
                hashSet.add("DEV");
            }
            hashSet.add("mention");
            hashSet.add("comment");
            hashSet.add("favourite");
            JPushInterface.setTags(MyApplication.getContext(), hashSet, new TagAliasCallback() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.11
                @Override // cn.jpush.android.api.TagAliasCallback
                public void gotResult(int i, String str, Set<String> set) {
                    if (i == 0) {
                        Log.d("uploadRegisterSuccess", "uploadRegisterSuccess");
                    } else {
                        if (i != 6002) {
                            return;
                        }
                        RequestInetUtils.this.uploadRegistraionId();
                    }
                }
            });
        }
    }

    public void uploadRegistraionId(String str) {
        UploadRegistrationRequestBean uploadRegistrationRequestBean = new UploadRegistrationRequestBean();
        uploadRegistrationRequestBean.setFcm_registrationID(str);
        this.factory.getServer(RetrofitFactory.I21).uploadRegistration(uploadRegistrationRequestBean).enqueue(new Callback<UploadRegistaionIdResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.12
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<UploadRegistaionIdResultModel> response) {
                Log.e("onResponse", new Gson().toJson(response.body()));
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Log.e("onFailure", "onFailure");
            }
        });
    }

    private boolean isZh() {
        return MyApplication.getInstance().getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }

    public void clearAliasName() {
        JPushInterface.setAlias(MyApplication.getContext(), "", (TagAliasCallback) null);
    }

    public void getMyFollower(int i, int i2, Callback<GetMyFollowerResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getMyFollower(i, i2).enqueue(callback);
    }

    public void getMyFollowing(int i, int i2, Callback<GetMyFollowerResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getMyFolloing(i, i2).enqueue(callback);
    }

    public void getFriendsMessageByUserId(int i, int i2, int i3, Callback<GetFriendsMessageResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getFrinedsMessageByUserId(i, i2, i3).enqueue(callback);
    }

    public void cancelFollowUserbyId(int i, Callback<CancelFollowUserResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).cancelFollowUserByUserId(i).enqueue(callback);
    }

    public void getUserInfoById(int i, Callback<UserInfoResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getUserInfoById(i).enqueue(callback);
    }

    public void deleteCommentById(int i, Callback<DeleteCommentResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).deleteCommentById(i).enqueue(callback);
    }

    public void downLoadRouteById(int i, final int i2, final Callback<DownloadRouteResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).downLoadRouteById(i).enqueue(new Callback<DownloadRouteResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.13
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<DownloadRouteResultModel> response) {
                ViewUtils.ShowToast(R.string.myfanrouteadapter_route_download_complete);
                Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onResponse(response);
                }
                DowloadCompleteMessage dowloadCompleteMessage = new DowloadCompleteMessage();
                dowloadCompleteMessage.setDownLoadCount(response.body().getRoute_download_count());
                dowloadCompleteMessage.setListDataPosition(i2);
                EventBus.getDefault().post(dowloadCompleteMessage);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onFailure(errorCode);
                }
                Log.e("onFailure", MyLocationStyle.ERROR_CODE + errorCode.getMessage());
            }
        });
    }

    public void findFriendsByUserName(String str, int i, int i2, Callback<FindFriendsResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).findFriendsByUserName(str, i, i2).enqueue(callback);
    }

    public void getManualsList(int i, Callback<ManualsListResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getManualsList(i).enqueue(callback);
    }

    public void getSectionList(int i, Callback<SectionResultModel> callback) {
        this.factory.getServer(RetrofitFactory.ZENDESK).getSections(MyApplication.getInstance().getString(R.string.zendesk_language), i).enqueue(callback);
    }

    public void getArticlesList(int i, Callback<ArticlesResultModel> callback) {
        this.factory.getServer(RetrofitFactory.ZENDESK).getArticles(MyApplication.getInstance().getString(R.string.zendesk_language), i).enqueue(callback);
    }

    public void getCommentsNotification(int i, int i2, Callback<CommentNotificationResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getNotificationComments(i, i2).enqueue(callback);
    }

    public void getMessageById(int i, Callback<CommentContentResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getMessageByMsgId(i).enqueue(callback);
    }

    public void getFravouriteMessageNotifitcations(int i, int i2, Callback<FavouriteNotifitcationMessageListResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I21).getFravouriteMessageNotifitcations(i, i2).enqueue(callback);
    }

    public void searchZendeskManualsList(String str, Callback<ZendeskManualsListModelResult> callback) {
        this.factory.getServer(RetrofitFactory.ZENDESK).searchZendeskManualsList(str).enqueue(callback);
    }

    public void getLocalRouteScreenhost(RouteModel routeModel, Callback<GetRouteScreenShostResultModel> callback) {
        List<WayPoint> waypoint = routeModel.getWaypoint();
        RouteScreenshostRequest routeScreenshostRequest = new RouteScreenshostRequest();
        routeScreenshostRequest.setEncoded_polyline(routeModel.getEncodePolyline());
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < waypoint.size(); i++) {
            RouteScreenshostRequest.WaypointsBean waypointsBean = new RouteScreenshostRequest.WaypointsBean();
            WayPoint wayPoint = waypoint.get(i);
            waypointsBean.setLatitude(wayPoint.getLatitude());
            waypointsBean.setLongitude(wayPoint.getLongitude());
            waypointsBean.setSequence(i);
            arrayList.add(waypointsBean);
        }
        routeScreenshostRequest.setWaypoints(arrayList);
        this.factory.getServer(RetrofitFactory.I21).getLocalRouteScreenShost(routeScreenshostRequest).enqueue(callback);
    }

    public void getContacts(Callback<ContactResltModel> callback) {
        if (UserMe.isLogin()) {
            this.factory.getServer(RetrofitFactory.I21).getContacts().enqueue(callback);
        }
    }

    public void getUserInfoByUsername(String str, Callback<GetUserInfoByUserNameResultModel> callback) {
        Log.e("getUserInfoByUsername", str);
        this.factory.getServer(RetrofitFactory.I22).getUserInfoByUserName(str).enqueue(callback);
    }

    public void getAccessToken(String str, String str2, String str3, String str4, Callback<GetAccessTokenResponseModel> callback) {
        this.factory.getServer(RetrofitFactory.WXACCESSTOKEN).getAccessToken(str, str2, str3, str4).enqueue(callback);
    }

    public void getWXUserInfo(String str, String str2, Callback<WXUserInfoResponseModel> callback) {
        this.factory.getServer(RetrofitFactory.WXUSERINFO).getWxUserInfo(str, str2).enqueue(callback);
    }

    public void sendWxInfoToServer(String str, String str2, String str3, String str4, int i, final Callback<SendWXInfoToServerResultModel> callback) {
        SendWxInfoToServerRequest sendWxInfoToServerRequest = new SendWxInfoToServerRequest();
        sendWxInfoToServerRequest.setAccess_token(str);
        sendWxInfoToServerRequest.setOpen_id(str4);
        sendWxInfoToServerRequest.setSex(i);
        sendWxInfoToServerRequest.setRefresh_token(str2);
        sendWxInfoToServerRequest.setUnion_id(str3);
        this.factory.getServer(RetrofitFactory.I22).sendWxInfoToServer(sendWxInfoToServerRequest).enqueue(new Callback<SendWXInfoToServerResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.14
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<SendWXInfoToServerResultModel> response) {
                if (!UserMe.isLogin()) {
                    JSave.put(JThirdPlatFormInterface.KEY_TOKEN, response.body().getToken());
                    RequestInetUtils.this.getUserInfo(null);
                }
                callback.onResponse(response);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                callback.onFailure(errorCode);
            }
        });
    }

    public void getPasswordIsNUll(Callback<PasswordIsNull> callback) {
        this.factory.getServer(RetrofitFactory.I22).getPasswordIsNUll().enqueue(callback);
    }

    public void bindEmail(String str, Callback<BindEmailResultModel> callback) {
        BindEmailRequest bindEmailRequest = new BindEmailRequest();
        bindEmailRequest.setEmail(str);
        this.factory.getServer(RetrofitFactory.I22).bindEmail(bindEmailRequest).enqueue(callback);
    }

    public void getAccountSecuriytState(Callback<AccountSecuriytStateResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I22).getAccountSecuriytState().enqueue(callback);
    }

    public void BindWeixin(String str, String str2, String str3, String str4, int i, Callback<BindWxResultModel> callback) {
        SendWxInfoToServerRequest sendWxInfoToServerRequest = new SendWxInfoToServerRequest();
        sendWxInfoToServerRequest.setAccess_token(str);
        sendWxInfoToServerRequest.setOpen_id(str4);
        sendWxInfoToServerRequest.setSex(i);
        sendWxInfoToServerRequest.setRefresh_token(str2);
        sendWxInfoToServerRequest.setUnion_id(str3);
        this.factory.getServer(RetrofitFactory.I22).bindWeiXin(sendWxInfoToServerRequest).enqueue(callback);
    }

    public void getJpushTagAndAlias(Callback<JpushQueryTagAndAlias> callback) {
        String str = (String) JSave.get("jpush_appkey");
        String str2 = (String) JSave.get("jpush_mastersecret");
        if (str == null || str2 == null) {
            return;
        }
        String registrationID = JPushInterface.getRegistrationID(MyApplication.getContext());
        this.factory.getServer(RetrofitFactory.JPUSH).getJpushTagAndAlias(encodeCredentialsForBasicAuthorization(str, str2), registrationID).enqueue(callback);
    }

    public void getJpushToken() {
        if (UserMe.isLogin()) {
            this.factory.getServer(RetrofitFactory.I22).getJpushToken().enqueue(new Callback<JpushTokenResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.15
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<JpushTokenResultModel> response) {
                    JpushTokenResultModel jpushTokenResultModelBody = response.body();
                    String app_key = jpushTokenResultModelBody.getApp_key();
                    String master_secret = jpushTokenResultModelBody.getMaster_secret();
                    JSave.put("jpush_appkey", app_key);
                    JSave.put("jpush_mastersecret", master_secret);
                }
            });
        }
    }

    public void sendFbUserInfoToServer(SendFbInfoToServerRequest sendFbInfoToServerRequest, final Callback<SendWXInfoToServerResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I22).sendFUserInfoTobServer(sendFbInfoToServerRequest).enqueue(new Callback<SendWXInfoToServerResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.16
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<SendWXInfoToServerResultModel> response) {
                Log.e("onResponse", "onResponse:" + response.body().getToken());
                if (!UserMe.isLogin()) {
                    JSave.put(JThirdPlatFormInterface.KEY_TOKEN, response.body().getToken());
                    Log.e("Token", JSave.get(JThirdPlatFormInterface.KEY_TOKEN) + "");
                    RequestInetUtils.this.getUserInfo(null);
                }
                Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onResponse(response);
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Log.e("onResponse", "onResponse:onFailure");
                Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onFailure(errorCode);
                }
            }
        });
    }

    public void bindFaceBook(String str, String str2, String str3, int i, String str4, Callback<BindFaceBookResultModel> callback) {
        BindFaceBookRequest bindFaceBookRequest = new BindFaceBookRequest();
        bindFaceBookRequest.setEmail(str2);
        bindFaceBookRequest.setSex(i);
        bindFaceBookRequest.setName(str4);
        bindFaceBookRequest.setAccess_token(str3);
        bindFaceBookRequest.setId(str);
        this.factory.getServer(RetrofitFactory.I22).bindFaceBook(bindFaceBookRequest).enqueue(callback);
    }

    public void getPrivatePolicy() {
        this.factory.getServer(RetrofitFactory.I22).getPrivatePolicyInfo(MyApplication.getContext().getString(R.string.language)).enqueue(new Callback<PrivatePolicyResultModel>() { // from class: im.helmsman.helmsmanandroid.inet.RequestInetUtils.17
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<PrivatePolicyResultModel> response) {
                JSave.put("private_policy_url", response.body().getUrl());
                JSave.put("private_policy_time", Long.valueOf(response.body().getUpdate_timestamp()));
            }
        });
    }

    public void getAppVersionInfo(Callback<AppVersionInfoResultModel> callback) {
        this.factory.getServer(RetrofitFactory.I22).getAppVersionInfo().enqueue(callback);
    }
}
