package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.FindFriendsResultModel;
import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;

/* JADX INFO: loaded from: classes2.dex */
public interface OnSearchFrinedListener {
    void onFollowUserFailedListener(String str);

    void onFollowUserSuccessListener(FollowerUserModel followerUserModel);

    void onSearchFriendsError(String str);

    void onSearchFriendsSuccess(FindFriendsResultModel findFriendsResultModel);
}
