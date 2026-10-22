package im.helmsman.helmsmanandroid.model;

import im.helmsman.helmsmanandroid.presenter.listener.OnUserListener;

/* JADX INFO: loaded from: classes2.dex */
public interface UserModel {
    void register(String str, String str2, String str3, OnUserListener onUserListener);

    void resetPassword(String str, OnUserListener onUserListener);

    void signin(String str, String str2, OnUserListener onUserListener);
}
