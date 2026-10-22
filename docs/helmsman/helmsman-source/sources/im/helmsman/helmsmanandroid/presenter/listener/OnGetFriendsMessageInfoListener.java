package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.CancelFollowUserResultModel;
import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.inet.model.UserInfoResultModel;

/* JADX INFO: loaded from: classes2.dex */
public interface OnGetFriendsMessageInfoListener {
    void onCancelFollowUserFailedListener(String str);

    void onCancelFollowUserSuccessListener(CancelFollowUserResultModel cancelFollowUserResultModel);

    void onGetFriendMessageFailed(String str);

    void onGetFriendMessageSuccess(GetFriendsMessageResultModel getFriendsMessageResultModel);

    void onGetUserInfoFailedListener(String str);

    void onGetUserInfoSuccesslistener(UserInfoResultModel userInfoResultModel);
}
