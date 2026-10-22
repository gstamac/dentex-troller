package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.GetMyFollowerResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface MyFollowerView extends MvpView {
    void addDataToRecycler(List<GetMyFollowerResultModel.UsersBean> list);

    void disableLoadMore();

    void enableLoadMore();

    void loadMoreComplete();

    void refreshComplete();

    void refreshRecyclerView(List<GetMyFollowerResultModel.UsersBean> list);

    void showFollowFailedMessage(String str);

    void showGetDataFailedMessage(String str);

    void showNoMoreDataMessage();
}
