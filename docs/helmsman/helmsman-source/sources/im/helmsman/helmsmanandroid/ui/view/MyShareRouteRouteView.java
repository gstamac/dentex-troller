package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface MyShareRouteRouteView extends MvpView {
    void LoadMoreComplete();

    void RefreshComplete();

    void addRecyclerViewData(List<LikeRouteListModel.MessagesBean> list);

    void dismissDiaLog();

    void enableLoadMore();

    void noMessage();

    void setRecyclerViewData(List<LikeRouteListModel.MessagesBean> list);

    void showRefreshFailedMessage(String str);

    void showRefreshSuccessMessage();
}
