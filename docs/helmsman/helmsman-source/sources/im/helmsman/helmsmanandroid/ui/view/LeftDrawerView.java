package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public interface LeftDrawerView extends MvpView {
    void showFaceBookCancleMessage();

    void showFaceBookError(String str);

    void showLoginErrorMessage(String str);

    void showLoginSuccessMessage();

    void showRegisterErrorMessage(String str);

    void showRegisterSuccessMessage();

    void showResetPasswordErrorMessage(String str);

    void showResetPasswordSuccess();

    void startAccountActivity();
}
