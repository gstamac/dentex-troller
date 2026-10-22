package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.presenter.MyRoutePresenterImp;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface MyRouteView extends MvpView {
    void addDataToRecycler(List<LikeRouteListModel.MessagesBean> list);

    void dimissLoginProgress();

    void disableLoadMore();

    void enableLoadMore();

    MyRoutePresenterImp getPresenter();

    void hideProgress();

    void loadmoreComplete();

    void refreshComplete();

    void setRecyclerView(List<LikeRouteListModel.MessagesBean> list);

    void showEmpyBoatIcon();

    void showGetDataErrorMsg(String str);

    void showLoginFaileMessage(String str);

    void showProgress();
}
