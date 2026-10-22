package im.helmsman.helmsmanandroid.presenter;

import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.BindFaceBookResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.FaceBookUserInfoModel;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.view.AccountSecuriytView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import org.json.JSONObject;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class AccountSecuriytPresenter extends BasePresenter<AccountSecuriytView> {
    public void bindFaceBook(CallbackManager callbackManager) {
        ViewUtils.showProgressDialog(R.string.loading);
        LoginManager.getInstance().logOut();
        LoginButton loginButton = new LoginButton(Base2Activity.getTopActivity());
        loginButton.performClick();
        FacebookSdk.sdkInitialize(MyApplication.getContext());
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() { // from class: im.helmsman.helmsmanandroid.presenter.AccountSecuriytPresenter.1
            @Override // com.facebook.FacebookCallback
            public void onSuccess(LoginResult loginResult) {
                Log.e("onSuccess", loginResult.getAccessToken().getToken());
                AccountSecuriytPresenter.this.initMyProfile(loginResult.getAccessToken());
            }

            @Override // com.facebook.FacebookCallback
            public void onCancel() {
                ViewUtils.cancelProgressDialog();
                if (AccountSecuriytPresenter.this.view != null) {
                    ((AccountSecuriytView) AccountSecuriytPresenter.this.view).showFaceBookCancleMessage();
                }
            }

            @Override // com.facebook.FacebookCallback
            public void onError(FacebookException facebookException) {
                Log.e("onError", "onError:" + facebookException.getMessage());
                ViewUtils.cancelProgressDialog();
                if (AccountSecuriytPresenter.this.view != null) {
                    ((AccountSecuriytView) AccountSecuriytPresenter.this.view).showFaceBookError(facebookException.getMessage());
                }
            }
        });
    }

    public void initMyProfile(AccessToken accessToken) {
        final String token = accessToken.getToken();
        GraphRequest graphRequestNewMeRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() { // from class: im.helmsman.helmsmanandroid.presenter.AccountSecuriytPresenter.2
            @Override // com.facebook.GraphRequest.GraphJSONObjectCallback
            public void onCompleted(JSONObject jSONObject, GraphResponse graphResponse) {
                Log.e("onCompleted", jSONObject.toString());
                FaceBookUserInfoModel faceBookUserInfoModel = (FaceBookUserInfoModel) new Gson().fromJson(jSONObject.toString(), FaceBookUserInfoModel.class);
                AccountSecuriytPresenter.this.bind(faceBookUserInfoModel.getId(), faceBookUserInfoModel.getEmail(), token, 0, faceBookUserInfoModel.getName());
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(GraphRequest.FIELDS_PARAM, "id,name,link,email,birthday,picture");
        graphRequestNewMeRequest.setParameters(bundle);
        graphRequestNewMeRequest.executeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bind(String str, String str2, String str3, int i, String str4) {
        RequestInetUtils.instance().bindFaceBook(str, str2, str3, i, str4, new Callback<BindFaceBookResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.AccountSecuriytPresenter.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<BindFaceBookResultModel> response) {
                ViewUtils.ShowToast(R.string.bind_success);
                ViewUtils.cancelProgressDialog();
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                ViewUtils.cancelProgressDialog();
                ViewUtils.ShowToast(R.string.bind_failed);
                Log.e("bindfailed", errorCode.getMessage());
            }
        });
    }
}
