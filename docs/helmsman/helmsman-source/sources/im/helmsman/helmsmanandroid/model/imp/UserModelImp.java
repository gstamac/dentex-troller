package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.RegisterResponse;
import im.helmsman.helmsmanandroid.inet.model.RestPasswordResponse;
import im.helmsman.helmsmanandroid.inet.model.TokenResponse;
import im.helmsman.helmsmanandroid.model.UserModel;
import im.helmsman.helmsmanandroid.presenter.listener.OnUserListener;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class UserModelImp implements UserModel {
    @Override // im.helmsman.helmsmanandroid.model.UserModel
    public void register(String str, String str2, String str3, final OnUserListener onUserListener) throws Throwable {
        RequestInetUtils.instance().registerUser(str, str2, str3, new Callback<RegisterResponse>() { // from class: im.helmsman.helmsmanandroid.model.imp.UserModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<RegisterResponse> response) {
                OnUserListener onUserListener2 = onUserListener;
                if (onUserListener2 != null) {
                    onUserListener2.onRegisterSuccess(response.body());
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                OnUserListener onUserListener2 = onUserListener;
                if (onUserListener2 != null) {
                    onUserListener2.onRegisterError(errorCode.getMessage());
                }
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.UserModel
    public void resetPassword(String str, final OnUserListener onUserListener) throws Throwable {
        RequestInetUtils.instance().resetPassword(str, new Callback<RestPasswordResponse>() { // from class: im.helmsman.helmsmanandroid.model.imp.UserModelImp.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<RestPasswordResponse> response) {
                OnUserListener onUserListener2 = onUserListener;
                if (onUserListener2 != null) {
                    onUserListener2.onResetPasswordSuccess(response.body());
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                OnUserListener onUserListener2 = onUserListener;
                if (onUserListener2 != null) {
                    onUserListener2.onResetPasswordError(errorCode.getMessage());
                }
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.UserModel
    public void signin(String str, String str2, final OnUserListener onUserListener) throws Throwable {
        RequestInetUtils.instance().login(str, str2, new Callback<TokenResponse>() { // from class: im.helmsman.helmsmanandroid.model.imp.UserModelImp.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<TokenResponse> response) {
                OnUserListener onUserListener2 = onUserListener;
                if (onUserListener2 != null) {
                    onUserListener2.onLoginSuccess(response.body());
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                OnUserListener onUserListener2 = onUserListener;
                if (onUserListener2 != null) {
                    onUserListener2.onLoginError(errorCode.getMessage());
                }
            }
        });
    }
}
