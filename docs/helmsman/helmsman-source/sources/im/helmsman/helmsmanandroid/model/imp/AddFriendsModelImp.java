package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.FindFriendsResultModel;
import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.model.AddFriendsModel;
import im.helmsman.helmsmanandroid.presenter.listener.OnSearchFrinedListener;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class AddFriendsModelImp implements AddFriendsModel {
    @Override // im.helmsman.helmsmanandroid.model.AddFriendsModel
    public void searchFriendsByUserName(String str, int i, int i2, final OnSearchFrinedListener onSearchFrinedListener) {
        RequestInetUtils.instance().findFriendsByUserName(str, i, i2, new Callback<FindFriendsResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.AddFriendsModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<FindFriendsResultModel> response) {
                onSearchFrinedListener.onSearchFriendsSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                onSearchFrinedListener.onSearchFriendsError(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.AddFriendsModel
    public void followFriendById(int i, final OnSearchFrinedListener onSearchFrinedListener) {
        RequestInetUtils.instance().followUser(i, new Callback<FollowerUserModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.AddFriendsModelImp.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<FollowerUserModel> response) {
                onSearchFrinedListener.onFollowUserSuccessListener(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                onSearchFrinedListener.onFollowUserFailedListener(errorCode.getMessage());
            }
        });
    }
}
