package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public interface BindEmailView extends MvpView {
    void bindEmailError(String str);

    void bindEmailSuccess();

    void dismissDialog();

    void emailFormatError();
}
