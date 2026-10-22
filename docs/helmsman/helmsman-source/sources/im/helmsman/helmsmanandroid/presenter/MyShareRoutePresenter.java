package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.model.MyShareRouteModel;
import im.helmsman.helmsmanandroid.model.imp.MyShareRouteModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetMyShareRouteListener;
import im.helmsman.helmsmanandroid.ui.view.MyShareRouteRouteView;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class MyShareRoutePresenter extends BasePresenter<MyShareRouteRouteView> implements OnGetMyShareRouteListener {
    private int nextPage;
    private int page = 1;
    private int sinceId = 0;
    private boolean isLoadMore = false;
    private List<LikeRouteListModel.MessagesBean> routesBeens = new ArrayList();
    private MyShareRouteModel myShareRouteModel = new MyShareRouteModelImp();

    public void getMyFavouriteRoute() {
        this.page = 1;
        this.sinceId = 0;
        this.isLoadMore = false;
        this.routesBeens.clear();
        this.myShareRouteModel.getMyShareRoute(this.page, this.sinceId, this);
    }

    public void loadMore() {
        int i = this.nextPage;
        int i2 = this.page;
        if (i < i2) {
            if (this.view != 0) {
                ((MyShareRouteRouteView) this.view).noMessage();
                ((MyShareRouteRouteView) this.view).LoadMoreComplete();
            }
        } else {
            int i3 = i2 + 1;
            this.page = i3;
            this.myShareRouteModel.getMyShareRoute(i3, this.sinceId, this);
        }
        this.isLoadMore = true;
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetMyShareRouteListener
    public void onGetMyShareRouteSuccess(LikeRouteListModel likeRouteListModel) {
        if (this.view == 0) {
            return;
        }
        List<LikeRouteListModel.MessagesBean> messages = likeRouteListModel.getMessages();
        if (messages.size() > 0) {
            this.routesBeens.addAll(messages);
            this.sinceId = this.routesBeens.get(0).getId();
            if (!this.isLoadMore) {
                ((MyShareRouteRouteView) this.view).setRecyclerViewData(messages);
            } else {
                ((MyShareRouteRouteView) this.view).addRecyclerViewData(messages);
            }
            this.nextPage = likeRouteListModel.getNext_page();
            ((MyShareRouteRouteView) this.view).enableLoadMore();
            ((MyShareRouteRouteView) this.view).dismissDiaLog();
            ((MyShareRouteRouteView) this.view).showRefreshSuccessMessage();
        } else {
            ((MyShareRouteRouteView) this.view).noMessage();
        }
        ((MyShareRouteRouteView) this.view).RefreshComplete();
        ((MyShareRouteRouteView) this.view).LoadMoreComplete();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetMyShareRouteListener
    public void onGetMyShareRouteFailed(String str) {
        if (this.view != 0) {
            ((MyShareRouteRouteView) this.view).LoadMoreComplete();
            ((MyShareRouteRouteView) this.view).RefreshComplete();
            ((MyShareRouteRouteView) this.view).dismissDiaLog();
            ((MyShareRouteRouteView) this.view).showRefreshFailedMessage(str);
        }
    }
}
