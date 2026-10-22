package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;
import im.helmsman.helmsmanandroid.model.FriendsModel;
import im.helmsman.helmsmanandroid.model.imp.FriendsModelImp;
import im.helmsman.helmsmanandroid.model.imp.StartModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener;
import im.helmsman.helmsmanandroid.ui.view.MyFollowerView;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsPersenterImp extends BasePresenter implements OnGetFriendsListener {
    private MyFollowerView myFollowerView;
    private int userid;
    private int myFollowerPage = 1;
    private int myFollowerNextPage = 0;
    private int myFollowingPage = 1;
    private int myFollowingNextPage = 0;
    private boolean isRefresh = false;
    private FriendsModel friendsModel = new FriendsModelImp(this);

    public FriendsPersenterImp(MyFollowerView myFollowerView, int i) {
        this.userid = i;
        this.myFollowerView = myFollowerView;
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowerSuccessListener(GetMyFollowerResultModel getMyFollowerResultModel) {
        this.myFollowerNextPage = getMyFollowerResultModel.getNext_page();
        if (this.isRefresh) {
            this.isRefresh = false;
            this.myFollowerView.refreshRecyclerView(getMyFollowerResultModel.getUsers());
        } else {
            this.myFollowerView.addDataToRecycler(getMyFollowerResultModel.getUsers());
        }
        this.myFollowerView.refreshComplete();
        this.myFollowerView.loadMoreComplete();
        if (this.myFollowerNextPage > this.myFollowerPage) {
            this.myFollowerView.enableLoadMore();
        } else {
            this.myFollowerView.disableLoadMore();
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowerFailedlistener(String str) {
        this.myFollowerView.showGetDataFailedMessage(str);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowingSuccessListener(GetMyFollowerResultModel getMyFollowerResultModel) {
        this.myFollowingNextPage = getMyFollowerResultModel.getNext_page();
        if (this.isRefresh) {
            this.isRefresh = false;
            this.myFollowerView.refreshRecyclerView(getMyFollowerResultModel.getUsers());
        } else {
            this.myFollowerView.addDataToRecycler(getMyFollowerResultModel.getUsers());
        }
        this.myFollowerView.refreshComplete();
        if (getMyFollowerResultModel.getUsers().size() > 0) {
            this.myFollowerView.enableLoadMore();
            this.myFollowerView.loadMoreComplete();
        } else {
            this.myFollowerView.disableLoadMore();
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onGetMyFollowingFailedListener(String str) {
        this.myFollowerView.showGetDataFailedMessage(str);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onFollowSuccessListener(FollowerUserModel followerUserModel) {
        getMyFollowerList(this.userid);
        updateContacts();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetFriendsListener
    public void onFollowFailedListener(String str) {
        this.myFollowerView.showFollowFailedMessage(str);
    }

    public void getMyFollowerList(int i) {
        this.isRefresh = true;
        this.myFollowerPage = 1;
        this.friendsModel.getMyFollower(i, 1);
    }

    public void getMyFollowingList(int i) {
        this.isRefresh = true;
        this.myFollowingPage = 1;
        this.friendsModel.getMyFollowing(i, 1);
    }

    public void loadMoreMyFollower(int i) {
        int i2 = this.myFollowerNextPage;
        int i3 = this.myFollowerPage;
        if (i2 > i3) {
            int i4 = i3 + 1;
            this.myFollowerPage = i4;
            this.friendsModel.getMyFollower(i, i4);
        } else {
            this.myFollowerView.showNoMoreDataMessage();
            this.myFollowerView.loadMoreComplete();
        }
    }

    public void loadMoreMyFollowing(int i) {
        int i2 = this.myFollowingNextPage;
        int i3 = this.myFollowingPage;
        if (i2 > i3) {
            int i4 = i3 + 1;
            this.myFollowingPage = i4;
            this.friendsModel.getMyFollowing(i, i4);
        } else {
            this.myFollowerView.showNoMoreDataMessage();
            this.myFollowerView.loadMoreComplete();
        }
    }

    public void followerUserById(int i) {
        this.friendsModel.followUserByUserId(i);
        this.isRefresh = true;
    }

    public void updateContacts() {
        new StartModelImp().downloadContacts();
    }
}
