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
import im.helmsman.helmsmanandroid.inet.model.SendWXInfoToServerResultModel;
import im.helmsman.helmsmanandroid.inet.model.TokenResponse;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.activity.AccountActivity;
import im.helmsman.helmsmanandroid.ui.activity.LoginActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.view.LoginView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.wxapi.WXEntryActivity;
import org.json.JSONObject;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class LoginPresenter extends BasePresenter<LoginView> {
    public void initFaceBookSdkLogin(LoginButton loginButton, CallbackManager callbackManager) {
        FacebookSdk.sdkInitialize(MyApplication.getContext());
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { // from class: im.helmsman.helmsmanandroid.presenter.LoginPresenter.1
            @Override // com.facebook.FacebookCallback
            public void onSuccess(LoginResult loginResult) {
                Log.e("onSuccess", loginResult.getAccessToken().getToken());
                LoginPresenter.this.initMyProfile(loginResult.getAccessToken());
            }

            @Override // com.facebook.FacebookCallback
            public void onCancel() {
                ViewUtils.cancelProgressDialog();
                if (LoginPresenter.this.view != null) {
                    ((LoginView) LoginPresenter.this.view).showFaceBookCancleMessage();
                }
            }

            @Override // com.facebook.FacebookCallback
            public void onError(FacebookException facebookException) {
                Log.e("onError", "onError:" + facebookException.getMessage());
                ViewUtils.cancelProgressDialog();
                if (LoginPresenter.this.view != null) {
                    ((LoginView) LoginPresenter.this.view).showFaceBookError(facebookException.getMessage());
                }
            }
        });
    }

    public void initMyProfile(final AccessToken accessToken) {
        GraphRequest graphRequestNewMeRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() { // from class: im.helmsman.helmsmanandroid.presenter.LoginPresenter.2
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
                LoginPresenter.this.sendFbUserInfoToServer(sendFbInfoToServerRequest, faceBookUserInfoModel.getPicture().getData().getUrl());
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(GraphRequest.FIELDS_PARAM, "id,name,link,email,birthday,picture");
        graphRequestNewMeRequest.setParameters(bundle);
        graphRequestNewMeRequest.executeAsync();
    }

    public void sendFbUserInfoToServer(SendFbInfoToServerRequest sendFbInfoToServerRequest, final String str) {
        RequestInetUtils.instance().sendFbUserInfoToServer(sendFbInfoToServerRequest, new Callback<SendWXInfoToServerResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.LoginPresenter.3
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
                    LoginPresenter.this.startActivity(intent);
                } else {
                    ViewUtils.ShowToast(R.string.loginsuccess);
                    RequestInetUtils.instance().uploadRegistraionId();
                    LoginPresenter.this.startActivity(new Intent(Base2Activity.getTopActivity(), (Class<?>) AccountActivity.class));
                }
                Base2Activity.finishActivity(LoginActivity.class);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                Log.e("errorcode", errorCode.getMessage() + errorCode.getError_code());
                ViewUtils.ShowToast(R.string.loginerror);
                ViewUtils.cancelProgressDialog();
            }
        });
    }

    public void login(String str, String str2) throws Throwable {
        RequestInetUtils.instance().login(str, str2, new Callback<TokenResponse>() { // from class: im.helmsman.helmsmanandroid.presenter.LoginPresenter.4
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<TokenResponse> response) {
                if (LoginPresenter.this.view != null) {
                    ((LoginView) LoginPresenter.this.view).showLoginSuccessMessafe();
                    ((LoginView) LoginPresenter.this.view).dismissProgress();
                }
                Base2Activity.getTopActivity().finish();
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (LoginPresenter.this.view != null) {
                    ((LoginView) LoginPresenter.this.view).showLogininFailedMessage(errorCode.getMessage());
                    ((LoginView) LoginPresenter.this.view).dismissProgress();
                }
            }
        });
    }
}
