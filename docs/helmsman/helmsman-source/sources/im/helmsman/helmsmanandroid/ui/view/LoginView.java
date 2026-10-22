package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public interface LoginView extends MvpView {
    void dismissProgress();

    void showFaceBookCancleMessage();

    void showFaceBookError(String str);

    void showLoginSuccessMessafe();

    void showLogininFailedMessage(String str);
}
