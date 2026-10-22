package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public interface NotificationSettingView extends MvpView {
    void onBackPressed();

    void reloadConfig();

    void setIsChange(boolean z);

    void showSetMessage(String str);
}
