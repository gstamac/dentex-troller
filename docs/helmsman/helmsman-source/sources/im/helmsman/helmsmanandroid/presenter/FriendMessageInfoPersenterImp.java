package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.CancelFollowUserResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;
import im.helmsman.helmsmanandroid.inet.model.GetUserInfoByUserNameResultModel;
import im.helmsman.helmsmanandroid.inet.model.UserInfoResultModel;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.model.FriendsMessageInfoModel;
import im.helmsman.helmsmanandroid.model.FriendsModel;
import im.helmsman.helmsmanandroid.model.imp.FriendsMessageInfoModelImp;
import im.helmsman.helmsmanandroid.model.imp.FriendsModelImp;
import im.helmsman.helmsmanandroid.model.imp.StartModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener;
import im.helmsman.helmsmanandroid.ui.view.FriendsMessageInfoView;
import java.util.List;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class FriendMessageInfoPersenterImp extends BasePresenter<FriendsMessageInfoView> implements OnGetFriendsMessageInfoListener, OnGetFriendsListener {
    private int page = 1;
    private int sinceId = 0;
    private int nextPage = 0;
    private boolean isLoadMore = false;
    private FriendsMessageInfoModel messageInfoModel = new FriendsMessageInfoModelImp(this);
    private FriendsModel friendsModel = new FriendsModelImp(this);

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowerFailedlistener(String str) {
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowerSuccessListener(GetMyFollowerResultModel getMyFollowerResultModel) {
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowingFailedListener(String str) {
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowingSuccessListener(GetMyFollowerResultModel getMyFollowerResultModel) {
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener
    public void onGetUserInfoFailedListener(String str) {
    }

    public void getFriendsMessageInfo(int i) {
        this.isLoadMore = false;
        this.page = 1;
        this.sinceId = 0;
        this.messageInfoModel.getFriendsMessageByUserId(i, 0, 1);
    }

    public void loadMoreFriendMessageInfo(int i) {
        this.isLoadMore = true;
        int i2 = this.nextPage;
        int i3 = this.page;
        if (i2 > i3) {
            int i4 = i3 + 1;
            this.page = i4;
            this.messageInfoModel.getFriendsMessageByUserId(i, this.sinceId, i4);
        } else if (this.view != 0) {
            ((FriendsMessageInfoView) this.view).showNoMoreMessageMessage();
            ((FriendsMessageInfoView) this.view).loadmoreComplete();
            ((FriendsMessageInfoView) this.view).disableLoadMore();
        }
    }

    public void followFriend(int i) {
        this.friendsModel.followUserByUserId(i);
    }

    public void cancelFollowUserById(int i) {
        this.messageInfoModel.cancelFollowUserByUserId(i);
    }

    public void getUserInfoById(int i) {
        this.messageInfoModel.getUserInfoByUserId(i);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener
    public void onGetFriendMessageSuccess(GetFriendsMessageResultModel getFriendsMessageResultModel) {
        if (this.view == 0) {
            return;
        }
        this.nextPage = getFriendsMessageResultModel.getNext_page();
        List<GetFriendsMessageResultModel.MessagesBean> messages = getFriendsMessageResultModel.getMessages();
        if (messages.size() > 0) {
            messages.get(0).getAuthor();
            if (!this.isLoadMore) {
                ((FriendsMessageInfoView) this.view).refreshListView(getFriendsMessageResultModel.getMessages());
            } else {
                ((FriendsMessageInfoView) this.view).addDataTolistView(getFriendsMessageResultModel.getMessages());
            }
            ((FriendsMessageInfoView) this.view).enableLoadMore();
            ((FriendsMessageInfoView) this.view).loadmoreComplete();
        } else {
            ((FriendsMessageInfoView) this.view).disableLoadMore();
        }
        ((FriendsMessageInfoView) this.view).refreshComplete();
        this.sinceId = getFriendsMessageResultModel.getSince_id();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener
    public void onGetFriendMessageFailed(String str) {
        if (this.view != 0) {
            ((FriendsMessageInfoView) this.view).loadDataFailed(str);
            ((FriendsMessageInfoView) this.view).disableLoadMore();
            ((FriendsMessageInfoView) this.view).refreshComplete();
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener
    public void onCancelFollowUserSuccessListener(CancelFollowUserResultModel cancelFollowUserResultModel) {
        if (this.view != 0) {
            ((FriendsMessageInfoView) this.view).hideProgressBar();
            ((FriendsMessageInfoView) this.view).changeFollowTextViewState("Follow", false);
        }
        new StartModelImp().downloadContacts();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener
    public void onCancelFollowUserFailedListener(String str) {
        if (this.view != 0) {
            ((FriendsMessageInfoView) this.view).showFollowFailedMessage(str);
            ((FriendsMessageInfoView) this.view).hideProgressBar();
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsMessageInfoListener
    public void onGetUserInfoSuccesslistener(UserInfoResultModel userInfoResultModel) {
        boolean zIsMySelef = UserMe.isMySelef(userInfoResultModel.getId());
        boolean zIsIs_following = userInfoResultModel.isIs_following();
        if (this.view != 0) {
            String avatar_url = userInfoResultModel.getAvatar_url();
            int followers_count = userInfoResultModel.getFollowers_count();
            int following_count = userInfoResultModel.getFollowing_count();
            String username = userInfoResultModel.getUsername();
            String about_me = userInfoResultModel.getAbout_me();
            ((FriendsMessageInfoView) this.view).setAvatar(avatar_url);
            ((FriendsMessageInfoView) this.view).setFollowerCount(followers_count);
            ((FriendsMessageInfoView) this.view).setFollowingCount(following_count);
            ((FriendsMessageInfoView) this.view).setFriendName(username);
            ((FriendsMessageInfoView) this.view).setContent(about_me);
            String str = zIsIs_following ? "Unfollow" : "Follow";
            if (zIsMySelef) {
                ((FriendsMessageInfoView) this.view).hideFollowTextView();
            } else {
                ((FriendsMessageInfoView) this.view).showFollowTextView();
            }
            ((FriendsMessageInfoView) this.view).hideProgressBar();
            ((FriendsMessageInfoView) this.view).changeFollowTextViewState(str, zIsIs_following);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onFollowSuccessListener(FollowerUserModel followerUserModel) {
        if (this.view != 0) {
            ((FriendsMessageInfoView) this.view).changeFollowTextViewState("Unfollow", true);
            ((FriendsMessageInfoView) this.view).hideProgressBar();
        }
        new StartModelImp().downloadContacts();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onFollowFailedListener(String str) {
        if (this.view != 0) {
            ((FriendsMessageInfoView) this.view).showCacncelFollowUserFailedMessage(str);
        }
    }

    public void getUserIdByUserName(String str) {
        RequestInetUtils.instance().getUserInfoByUsername(str, new Callback<GetUserInfoByUserNameResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.FriendMessageInfoPersenterImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<GetUserInfoByUserNameResultModel> response) {
                int id = response.body().getId();
                if (FriendMessageInfoPersenterImp.this.view != null) {
                    ((FriendsMessageInfoView) FriendMessageInfoPersenterImp.this.view).getUserId(id);
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (FriendMessageInfoPersenterImp.this.view != null) {
                    ((FriendsMessageInfoView) FriendMessageInfoPersenterImp.this.view).loadDataFailed(errorCode.getMessage());
                }
            }
        });
    }
}
