package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface FriendsMessageInfoView extends MvpView {
    void addDataTolistView(List<GetFriendsMessageResultModel.MessagesBean> list);

    void changeFollowTextViewState(String str, boolean z);

    void disableLoadMore();

    void enableLoadMore();

    void getUserId(int i);

    void hideFollowTextView();

    void hideProgressBar();

    void loadDataFailed(String str);

    void loadmoreComplete();

    void refreshComplete();

    void refreshListView(List<GetFriendsMessageResultModel.MessagesBean> list);

    void setAvatar(String str);

    void setContent(String str);

    void setFollowerCount(int i);

    void setFollowingCount(int i);

    void setFriendName(String str);

    void showCacncelFollowUserFailedMessage(String str);

    void showFollowFailedMessage(String str);

    void showFollowTextView();

    void showNoMoreMessageMessage();
}
