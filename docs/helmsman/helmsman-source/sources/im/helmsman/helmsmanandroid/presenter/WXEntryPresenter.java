package im.helmsman.helmsmanandroid.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.BindEmailResultModel;
import im.helmsman.helmsmanandroid.inet.model.BindWxResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.GetAccessTokenResponseModel;
import im.helmsman.helmsmanandroid.inet.model.SendWXInfoToServerResultModel;
import im.helmsman.helmsmanandroid.inet.model.UpLoadImageTokenResponse;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.inet.model.WXUserInfoResponseModel;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.activity.AccountActivity;
import im.helmsman.helmsmanandroid.ui.activity.LoginActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.common.BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.WXEntryView;
import im.helmsman.helmsmanandroid.utils.BitMapUtils;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.File;
import org.json.JSONObject;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class WXEntryPresenter extends BasePresenter<WXEntryView> {
    private String accesstoken;
    Bitmap avatarBitmap;
    private String openid;
    private String refreshToken;
    private int sex;
    UserMe userMe = new UserMe();

    public void getAccessToken(String str) {
        RequestInetUtils.instance().getAccessToken("wx6e8c1cff7f9272dd", Config.WX_APPSECRET, str, "authorization_code", new Callback<GetAccessTokenResponseModel>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<GetAccessTokenResponseModel> response) {
                GetAccessTokenResponseModel getAccessTokenResponseModelBody = response.body();
                WXEntryPresenter.this.accesstoken = getAccessTokenResponseModelBody.getAccess_token();
                WXEntryPresenter.this.refreshToken = getAccessTokenResponseModelBody.getRefresh_token();
                WXEntryPresenter.this.openid = getAccessTokenResponseModelBody.getOpenid();
                WXEntryPresenter wXEntryPresenter = WXEntryPresenter.this;
                wXEntryPresenter.getWxUserInfo(wXEntryPresenter.accesstoken, WXEntryPresenter.this.openid);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).showLoginFailedMessage(errorCode);
                    ((WXEntryView) WXEntryPresenter.this.view).finishActivity();
                }
            }
        });
    }

    public void getWxUserInfo(String str, String str2) {
        RequestInetUtils.instance().getWXUserInfo(str, str2, new Callback<WXUserInfoResponseModel>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<WXUserInfoResponseModel> response) {
                WXUserInfoResponseModel wXUserInfoResponseModelBody = response.body();
                WXEntryPresenter.this.userMe.setSex(wXUserInfoResponseModelBody.getSex());
                WXEntryPresenter.this.userMe.setAvatar_url(wXUserInfoResponseModelBody.getHeadimgurl());
                WXEntryPresenter.this.sex = response.body().getSex();
                String unionid = response.body().getUnionid();
                if (UserMe.isLogin()) {
                    WXEntryPresenter wXEntryPresenter = WXEntryPresenter.this;
                    wXEntryPresenter.bindWeixin(unionid, wXEntryPresenter.sex);
                } else {
                    WXEntryPresenter.this.SendWxInfoToServer(unionid, wXUserInfoResponseModelBody.getHeadimgurl(), WXEntryPresenter.this.sex);
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).showLoginFailedMessage(errorCode);
                    ((WXEntryView) WXEntryPresenter.this.view).finishActivity();
                }
            }
        });
    }

    public void downloadAvatarBitmap(String str) {
        Log.e("downloadAcvatarBitmap", "头像连接" + str);
        RequestInetUtils.instance().getMyAvatarBitmap(str, new Callback<Bitmap>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<Bitmap> response) {
                WXEntryPresenter.this.avatarBitmap = response.body();
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).setAvatar(WXEntryPresenter.this.avatarBitmap);
                }
            }
        });
    }

    public void uploadAvatarByBitMap() {
        if (this.view != 0 && this.avatarBitmap == null) {
            ((WXEntryView) this.view).uploadAvatarSuccess();
        } else {
            RequestInetUtils.instance().uploadImage(this.avatarBitmap, new Callback<UpLoadImageTokenResponse>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.4
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<UpLoadImageTokenResponse> response) {
                    if (WXEntryPresenter.this.view != null) {
                        ((WXEntryView) WXEntryPresenter.this.view).uploadAvatarSuccess();
                    }
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    if (WXEntryPresenter.this.view != null) {
                        ((WXEntryView) WXEntryPresenter.this.view).uploadAvatarFailure();
                    }
                }
            }, new UpCompletionHandler() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.5
                @Override // com.qiniu.android.storage.UpCompletionHandler
                public void complete(String str, ResponseInfo responseInfo, JSONObject jSONObject) {
                    if (WXEntryPresenter.this.view != null) {
                        ((WXEntryView) WXEntryPresenter.this.view).uploadAvatarSuccess();
                    }
                }
            });
        }
    }

    public void bindWeixin(String str, int i) {
        RequestInetUtils.instance().BindWeixin(this.accesstoken, this.refreshToken, str, this.openid, i, new Callback<BindWxResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.6
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<BindWxResultModel> response) {
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).finishActivity();
                    ((WXEntryView) WXEntryPresenter.this.view).showBindWXSuccessMessage();
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).showBindFailedMessage(errorCode.getMessage());
                }
            }
        });
    }

    public void SendWxInfoToServer(String str, final String str2, int i) {
        RequestInetUtils.instance().sendWxInfoToServer(this.accesstoken, this.refreshToken, str, this.openid, i, new Callback<SendWXInfoToServerResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.7
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<SendWXInfoToServerResultModel> response) {
                Base2Activity.finishActivity(LoginActivity.class);
                boolean zIsIs_registered = response.body().isIs_registered();
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).dismissLoadingProgressDialog();
                    if (zIsIs_registered) {
                        BaseActivity.getTopActivity().startActivity(new Intent(BaseActivity.getTopActivity(), (Class<?>) AccountActivity.class));
                        ((WXEntryView) WXEntryPresenter.this.view).finishActivity();
                    } else {
                        WXEntryPresenter.this.downloadAvatarBitmap(str2);
                        WXEntryPresenter.this.getUserInfo();
                    }
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Log.e("WX_onFailure", errorCode.toString());
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).dismissLoadingProgressDialog();
                    ((WXEntryView) WXEntryPresenter.this.view).showLoginFailedMessage(errorCode);
                    ((WXEntryView) WXEntryPresenter.this.view).finishActivity();
                }
            }
        });
    }

    public void getUserInfo() {
        RequestInetUtils.instance().getUserInfo(new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.8
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<UserMe> response) {
                UserMe userMeBody = response.body();
                if (WXEntryPresenter.this.view == null || userMeBody == null) {
                    return;
                }
                ((WXEntryView) WXEntryPresenter.this.view).setUserInfo(userMeBody);
            }
        });
    }

    public void getPhotoURLByAlbum(int i, int i2, Intent intent) {
        if (i == 100) {
            BitMapUtils.getPhotoURLByAlbum(Base2Activity.getTopActivity(), intent, new BitMapUtils.PhotoCallBack() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.9
                @Override // im.helmsman.helmsmanandroid.utils.BitMapUtils.PhotoCallBack
                public void onSuccess(String str) throws Throwable {
                    if (TextUtils.isEmpty(str)) {
                        onFailure();
                        return;
                    }
                    final File file = new File(str);
                    RequestInetUtils.instance().uploadImage(BitMapUtils.scal(Uri.parse(str)).getPath(), (Callback<UpLoadImageTokenResponse>) null, new UpCompletionHandler() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.9.1
                        @Override // com.qiniu.android.storage.UpCompletionHandler
                        public void complete(String str2, ResponseInfo responseInfo, JSONObject jSONObject) {
                            if (WXEntryPresenter.this.view != null) {
                                ((WXEntryView) WXEntryPresenter.this.view).loadAvatarToImageView(file);
                            }
                            ViewUtils.ShowToast(R.string.main_success);
                        }
                    });
                }

                @Override // im.helmsman.helmsmanandroid.utils.BitMapUtils.PhotoCallBack
                public void onFailure() {
                    ViewUtils.ShowToast(R.string.loaddatafailed);
                }
            });
        }
    }

    public void bindEmail(String str) {
        if (TextUtils.isEmpty(str)) {
            if (this.view != 0) {
                ((WXEntryView) this.view).onBindEmailFailure();
                ((WXEntryView) this.view).emailFormatError();
                return;
            }
            return;
        }
        if (!StringUtils.isEmail(str)) {
            if (this.view != 0) {
                ((WXEntryView) this.view).emailFormatError();
                return;
            }
            return;
        }
        RequestInetUtils.instance().bindEmail(str, new Callback<BindEmailResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.10
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<BindEmailResultModel> response) {
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).onBindEmailSuccess();
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).onBindEmailFailure();
                }
            }
        });
    }

    public void saveUserinfo(String str, String str2, String str3, String str4, int i) {
        if (this.view == 0) {
            return;
        }
        if (str == null) {
            ((WXEntryView) this.view).contryNotNull();
            return;
        }
        if (!StringUtils.checkUserName(str2)) {
            ((WXEntryView) this.view).nickNameFormatError();
            return;
        }
        if (!StringUtils.isEmail(str4)) {
            ((WXEntryView) this.view).emailFormatError();
            return;
        }
        this.userMe.setCountry_code(str);
        this.userMe.setUsername(str2);
        this.userMe.setAbout_me(str3);
        this.userMe.setEmail(str4);
        this.userMe.setSex(i);
        ViewUtils.showProgressDialog(MyApplication.getInstance().getString(R.string.addroute_saving_route));
        RequestInetUtils.instance().updateUserInfo(this.userMe, new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.presenter.WXEntryPresenter.11
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<UserMe> response) {
                ViewUtils.cancelProgressDialog();
                ViewUtils.ShowToast(R.string.save_success);
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).onSaveUserInfoSuccess();
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                ViewUtils.cancelProgressDialog();
                ViewUtils.ShowToast(R.string.save_failed);
                if (WXEntryPresenter.this.view != null) {
                    ((WXEntryView) WXEntryPresenter.this.view).onSaveUserInfoFailure();
                }
            }
        });
    }
}
