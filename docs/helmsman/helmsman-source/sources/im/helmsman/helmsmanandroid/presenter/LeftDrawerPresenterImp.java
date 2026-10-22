package im.helmsman.helmsmanandroid.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestBean.SendFbInfoToServerRequest;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.FaceBookUserInfoModel;
import im.helmsman.helmsmanandroid.inet.model.RegisterResponse;
import im.helmsman.helmsmanandroid.inet.model.RestPasswordResponse;
import im.helmsman.helmsmanandroid.inet.model.SendWXInfoToServerResultModel;
import im.helmsman.helmsmanandroid.inet.model.TokenResponse;
import im.helmsman.helmsmanandroid.model.UserModel;
import im.helmsman.helmsmanandroid.model.imp.UserModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnUserListener;
import im.helmsman.helmsmanandroid.ui.activity.AccountActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.view.LeftDrawerView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.wxapi.WXEntryActivity;
import org.json.JSONObject;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class LeftDrawerPresenterImp extends BasePresenter<LeftDrawerView> implements OnUserListener {
    private UserModel mUserModel = new UserModelImp();

    public void signin(String str, String str2) {
        this.mUserModel.signin(str, str2, this);
    }

    public void registerUser(String str, String str2, String str3) {
        this.mUserModel.register(str, str2, str3, this);
    }

    public void resetPassword(String str) {
        this.mUserModel.resetPassword(str, this);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnUserListener
    public void onRegisterSuccess(RegisterResponse registerResponse) {
        ((LeftDrawerView) this.view).showRegisterSuccessMessage();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnUserListener
    public void onRegisterError(String str) {
        ((LeftDrawerView) this.view).showRegisterErrorMessage(str);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnUserListener
    public void onResetPasswordSuccess(RestPasswordResponse restPasswordResponse) {
        ((LeftDrawerView) this.view).showResetPasswordSuccess();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnUserListener
    public void onResetPasswordError(String str) {
        ((LeftDrawerView) this.view).showResetPasswordErrorMessage(str);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnUserListener
    public void onLoginSuccess(TokenResponse tokenResponse) {
        ((LeftDrawerView) this.view).startAccountActivity();
        ((LeftDrawerView) this.view).showLoginSuccessMessage();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnUserListener
    public void onLoginError(String str) {
        if (this.view != 0) {
            ((LeftDrawerView) this.view).showLoginErrorMessage(str);
        }
    }

    public void initFaceBookSdkLogin(LoginButton loginButton, CallbackManager callbackManager) {
        FacebookSdk.sdkInitialize(MyApplication.getContext());
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { // from class: im.helmsman.helmsmanandroid.presenter.LeftDrawerPresenterImp.1
            @Override // com.facebook.FacebookCallback
            public void onSuccess(LoginResult loginResult) {
                Log.e("onSuccess", loginResult.getAccessToken().getToken());
                LeftDrawerPresenterImp.this.initMyProfile(loginResult.getAccessToken());
            }

            @Override // com.facebook.FacebookCallback
            public void onCancel() {
                ViewUtils.cancelProgressDialog();
                if (LeftDrawerPresenterImp.this.view != null) {
                    ((LeftDrawerView) LeftDrawerPresenterImp.this.view).showFaceBookCancleMessage();
                }
            }

            @Override // com.facebook.FacebookCallback
            public void onError(FacebookException facebookException) {
                Log.e("onError", "onError:" + facebookException.getMessage());
                ViewUtils.cancelProgressDialog();
                if (LeftDrawerPresenterImp.this.view != null) {
                    ((LeftDrawerView) LeftDrawerPresenterImp.this.view).showFaceBookError(facebookException.getMessage());
                }
            }
        });
    }

    public void initMyProfile(final AccessToken accessToken) {
        GraphRequest graphRequestNewMeRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() { // from class: im.helmsman.helmsmanandroid.presenter.LeftDrawerPresenterImp.2
            @Override // com.facebook.GraphRequest.GraphJSONObjectCallback
            public void onCompleted(JSONObject jSONObject, GraphResponse graphResponse) {
                Log.e("onCompleted", jSONObject.toString());
                FaceBookUserInfoModel faceBookUserInfoModel = (FaceBookUserInfoModel) new Gson().fromJson(jSONObject.toString(), FaceBookUserInfoModel.class);
                SendFbInfoToServerRequest sendFbInfoToServerRequest = new SendFbInfoToServerRequest();
                sendFbInfoToServerRequest.setAccess_token(accessToken.getToken());
                sendFbInfoToServerRequest.setEmail(faceBookUserInfoModel.getEmail());
                sendFbInfoToServerRequest.setId(faceBookUserInfoModel.getId());
                sendFbInfoToServerRequest.setName(faceBookUserInfoModel.getName());
                sendFbInfoToServerRequest.setSex(0);
                LeftDrawerPresenterImp.this.sendFbUserInfoToServer(sendFbInfoToServerRequest, faceBookUserInfoModel.getPicture().getData().getUrl());
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(GraphRequest.FIELDS_PARAM, "id,name,link,email,birthday,picture");
        graphRequestNewMeRequest.setParameters(bundle);
        graphRequestNewMeRequest.executeAsync();
    }

    public void sendFbUserInfoToServer(SendFbInfoToServerRequest sendFbInfoToServerRequest, final String str) {
        RequestInetUtils.instance().sendFbUserInfoToServer(sendFbInfoToServerRequest, new Callback<SendWXInfoToServerResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.LeftDrawerPresenterImp.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<SendWXInfoToServerResultModel> response) {
                boolean zIsIs_registered = response.body().isIs_registered();
                ViewUtils.cancelProgressDialog();
                if (!zIsIs_registered) {
                    ViewUtils.ShowToast(R.string.rigister_success);
                    RequestInetUtils.instance().uploadRegistraionId();
                    Intent intent = new Intent(Base2Activity.getTopActivity(), (Class<?>) WXEntryActivity.class);
                    intent.putExtra("model", 1);
                    intent.putExtra("avatarurl", str);
                    LeftDrawerPresenterImp.this.startActivity(intent);
                    return;
                }
                ViewUtils.ShowToast(R.string.loginsuccess);
                RequestInetUtils.instance().uploadRegistraionId();
                LeftDrawerPresenterImp.this.startActivity(new Intent(Base2Activity.getTopActivity(), (Class<?>) AccountActivity.class));
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Log.e("errorcode", errorCode.getMessage() + errorCode.getError_code());
                ViewUtils.ShowToast(R.string.loginerror);
                ViewUtils.cancelProgressDialog();
            }
        });
    }
}
