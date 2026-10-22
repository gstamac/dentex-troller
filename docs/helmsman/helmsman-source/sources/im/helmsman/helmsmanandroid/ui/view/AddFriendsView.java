package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.FindFriendsResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public interface AddFriendsView extends MvpView {
    void changeFollowBtnState(int i, boolean z);

    void closeLoadMore();

    void dismissFollowingDialog();

    void loadDataToList(FindFriendsResultModel findFriendsResultModel);

    void loadMoreComplete();

    void openLoadMode();

    void showFollowingDialog();

    void showLoadDataErrorMsg(String str);
}
