package im.helmsman.helmsmanandroid.presenter;

import android.text.TextUtils;
import im.helmsman.helmsmanandroid.inet.model.FindFriendsResultModel;
import im.helmsman.helmsmanandroid.inet.model.FollowerUserModel;
import im.helmsman.helmsmanandroid.model.AddFriendsModel;
import im.helmsman.helmsmanandroid.model.imp.AddFriendsModelImp;
import im.helmsman.helmsmanandroid.model.imp.StartModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnSearchFrinedListener;
import im.helmsman.helmsmanandroid.ui.view.AddFriendsView;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class AddFriendsPresenterImp extends BasePresenter<AddFriendsView> implements OnSearchFrinedListener {
    private int nextPage;
    private int sinceId;
    private String userName;
    private int page = 1;
    private AddFriendsModel model = new AddFriendsModelImp();

    public void searchFriendsByUserName(String str) {
        this.page = 1;
        this.sinceId = 0;
        searchFriends(str, 0, 1);
    }

    private void searchFriends(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            FindFriendsResultModel findFriendsResultModel = new FindFriendsResultModel();
            findFriendsResultModel.setUsers(new ArrayList());
            if (this.view != 0) {
                ((AddFriendsView) this.view).loadDataToList(findFriendsResultModel);
                ((AddFriendsView) this.view).closeLoadMore();
                return;
            }
            return;
        }
        this.model.searchFriendsByUserName(str, i, i2, this);
        this.userName = str;
    }

    public void loadMoreFriendsByUserName() {
        int i = this.nextPage;
        int i2 = this.page;
        if (i > i2) {
            AddFriendsModel addFriendsModel = this.model;
            String str = this.userName;
            int i3 = this.sinceId;
            int i4 = i2 + 1;
            this.page = i4;
            addFriendsModel.searchFriendsByUserName(str, i3, i4, this);
        }
    }

    public void followFriendById(int i) {
        this.model.followFriendById(i, this);
        if (this.view != 0) {
            ((AddFriendsView) this.view).changeFollowBtnState(i, true);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnSearchFrinedListener
    public void onSearchFriendsSuccess(FindFriendsResultModel findFriendsResultModel) {
        if (this.view != 0) {
            ((AddFriendsView) this.view).loadDataToList(findFriendsResultModel);
            this.nextPage = findFriendsResultModel.getNext_page();
            this.sinceId = findFriendsResultModel.getSince_id();
            ((AddFriendsView) this.view).loadMoreComplete();
            if (this.nextPage > this.page) {
                ((AddFriendsView) this.view).openLoadMode();
            } else {
                ((AddFriendsView) this.view).closeLoadMore();
            }
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnSearchFrinedListener
    public void onSearchFriendsError(String str) {
        if (this.view != 0) {
            ((AddFriendsView) this.view).showLoadDataErrorMsg(str);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnSearchFrinedListener
    public void onFollowUserSuccessListener(FollowerUserModel followerUserModel) {
        if (this.view != 0) {
            ((AddFriendsView) this.view).changeFollowBtnState(followerUserModel.getId(), false);
            new StartModelImp().downloadContacts();
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnSearchFrinedListener
    public void onFollowUserFailedListener(String str) {
        if (this.view != 0) {
            ((AddFriendsView) this.view).showLoadDataErrorMsg(str);
        }
    }
}
