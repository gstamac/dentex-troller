package im.helmsman.helmsmanandroid.model;

import im.helmsman.helmsmanandroid.presenter.listener.OnSearchFrinedListener;

/* JADX INFO: loaded from: classes2.dex */
public interface AddFriendsModel {
    void followFriendById(int i, OnSearchFrinedListener onSearchFrinedListener);

    void searchFriendsByUserName(String str, int i, int i2, OnSearchFrinedListener onSearchFrinedListener);
}
