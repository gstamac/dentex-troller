package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;

/* JADX INFO: loaded from: classes2.dex */
public interface OnGetFriendsListener {
    void onFollowFailedListener(String str);

    void onFollowSuccessListener(FollowerUserModel followerUserModel);

    void onGetMyFollowerFailedlistener(String str);

    void onGetMyFollowerSuccessListener(GetMyFollowerResultModel getMyFollowerResultModel);

    void onGetMyFollowingFailedListener(String str);

    void onGetMyFollowingSuccessListener(GetMyFollowerResultModel getMyFollowerResultModel);
}
