package im.helmsman.helmsmanandroid.ui.view;

import android.graphics.Bitmap;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
public interface WXEntryView extends MvpView {
    void contryNotNull();

    void dismissLoadingProgressDialog();

    void emailFormatError();

    void finishActivity();

    void loadAvatarToImageView(File file);

    void nickNameFormatError();

    void onBindEmailFailure();

    void onBindEmailSuccess();

    void onSaveUserInfoFailure();

    void onSaveUserInfoSuccess();

    void setAvatar(Bitmap bitmap);

    void setUserInfo(UserMe userMe);

    void showBindFailedMessage(String str);

    void showBindWXSuccessMessage();

    void showLoadingProgressDialog();

    void showLoginFailedMessage(ErrorCode errorCode);

    void uploadAvatarFailure();

    void uploadAvatarSuccess();
}
