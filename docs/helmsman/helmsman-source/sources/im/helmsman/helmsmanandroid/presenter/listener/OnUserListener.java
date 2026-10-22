package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.RegisterResponse;
import im.helmsman.helmsmanandroid.inet.model.RestPasswordResponse;
import im.helmsman.helmsmanandroid.inet.model.TokenResponse;

/* JADX INFO: loaded from: classes2.dex */
public interface OnUserListener {
    void onLoginError(String str);

    void onLoginSuccess(TokenResponse tokenResponse);

    void onRegisterError(String str);

    void onRegisterSuccess(RegisterResponse registerResponse);

    void onResetPasswordError(String str);

    void onResetPasswordSuccess(RestPasswordResponse restPasswordResponse);
}
