package im.helmsman.helmsmanandroid.model.imp;

import android.util.Log;
import com.google.gson.Gson;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.CancelFollowUserResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.inet.model.UserInfoResultModel;
import im.helmsman.helmsmanandroid.model.FriendsMessageInfoModel;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsMessageInfoModelImp implements FriendsMessageInfoModel {
    private OnGetFriendsMessageInfoListener listener;

    public FriendsMessageInfoModelImp(OnGetFriendsMessageInfoListener onGetFriendsMessageInfoListener) {
        this.listener = onGetFriendsMessageInfoListener;
    }

    @Override // im.helmsman.helmsmanandroid.model.FriendsMessageInfoModel
    public void getFriendsMessageByUserId(int i, int i2, int i3) {
        RequestInetUtils.instance().getFriendsMessageByUserId(i, i2, i3, new Callback<GetFriendsMessageResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.FriendsMessageInfoModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<GetFriendsMessageResultModel> response) {
                FriendsMessageInfoModelImp.this.listener.onGetFriendMessageSuccess(response.body());
                Log.e("onResponse", new Gson().toJson(response.body()));
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                FriendsMessageInfoModelImp.this.listener.onGetFriendMessageFailed(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.FriendsMessageInfoModel
    public void cancelFollowUserByUserId(int i) {
        RequestInetUtils.instance().cancelFollowUserbyId(i, new Callback<CancelFollowUserResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.FriendsMessageInfoModelImp.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<CancelFollowUserResultModel> response) {
                FriendsMessageInfoModelImp.this.listener.onCancelFollowUserSuccessListener(null);
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                FriendsMessageInfoModelImp.this.listener.onCancelFollowUserFailedListener(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.FriendsMessageInfoModel
    public void getUserInfoByUserId(int i) {
        RequestInetUtils.instance().getUserInfoById(i, new Callback<UserInfoResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.FriendsMessageInfoModelImp.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<UserInfoResultModel> response) {
                FriendsMessageInfoModelImp.this.listener.onGetUserInfoSuccesslistener(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                FriendsMessageInfoModelImp.this.listener.onGetFriendMessageFailed(errorCode.getMessage());
            }
        });
    }
}
