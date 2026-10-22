package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;
import im.helmsman.helmsmanandroid.model.FriendsModel;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsModelImp implements FriendsModel {
    private OnGetFriendsListener onGetFriendsListener;

    public FriendsModelImp(OnGetFriendsListener onGetFriendsListener) {
        this.onGetFriendsListener = onGetFriendsListener;
    }

    @Override // im.helmsman.helmsmanandroid.model.FriendsModel
    public void getMyFollower(int i, int i2) {
        RequestInetUtils.instance().getMyFollower(i, i2, new Callback<GetMyFollowerResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.FriendsModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<GetMyFollowerResultModel> response) {
                FriendsModelImp.this.onGetFriendsListener.onGetMyFollowerSuccessListener(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                FriendsModelImp.this.onGetFriendsListener.onGetMyFollowerFailedlistener(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.FriendsModel
    public void getMyFollowing(int i, int i2) {
        RequestInetUtils.instance().getMyFollowing(i, i2, new Callback<GetMyFollowerResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.FriendsModelImp.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<GetMyFollowerResultModel> response) {
                FriendsModelImp.this.onGetFriendsListener.onGetMyFollowingSuccessListener(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                FriendsModelImp.this.onGetFriendsListener.onGetMyFollowingFailedListener(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.FriendsModel
    public void followUserByUserId(int i) {
        RequestInetUtils.instance().followUser(i, new Callback<FollowerUserModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.FriendsModelImp.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<FollowerUserModel> response) {
                FriendsModelImp.this.onGetFriendsListener.onFollowSuccessListener(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                FriendsModelImp.this.onGetFriendsListener.onFollowFailedListener(errorCode.getMessage());
            }
        });
    }
}
