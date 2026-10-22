package im.helmsman.helmsmanandroid.presenter;

import android.os.Handler;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.inet.model.TokenResponse;
import im.helmsman.helmsmanandroid.model.MyRouteModel;
import im.helmsman.helmsmanandroid.model.imp.MyRouteModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetMyRouteListener;
import im.helmsman.helmsmanandroid.ui.view.MyRouteView;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class MyRoutePresenterImp extends BasePresenter<MyRouteView> implements OnGetMyRouteListener {
    public static final int GET_MY_ROUTE = 0;
    public static final int HOME_ROUTE = 1;
    private MyRouteModel myRouteModel = new MyRouteModelImp(this);
    private MyRouteView myRouteView;
    private int nextPage;
    private int page;
    private int sinceId;
    private int tag;

    public MyRoutePresenterImp(MyRouteView myRouteView, int i) {
        this.myRouteView = myRouteView;
        this.tag = i;
    }

    public void login(String str, String str2) throws Throwable {
        RequestInetUtils.instance().login(str, str2, new Callback<TokenResponse>() { // from class: im.helmsman.helmsmanandroid.presenter.MyRoutePresenterImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<TokenResponse> response) {
                if (MyRoutePresenterImp.this.view != null) {
                    ((MyRouteView) MyRoutePresenterImp.this.view).dimissLoginProgress();
                    new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.presenter.MyRoutePresenterImp.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MyRoutePresenterImp.this.getMyRouteListData();
                        }
                    }, 200L);
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (MyRoutePresenterImp.this.view != null) {
                    ((MyRouteView) MyRoutePresenterImp.this.view).showLoginFaileMessage(errorCode.getMessage());
                    ((MyRouteView) MyRoutePresenterImp.this.view).dimissLoginProgress();
                }
            }
        });
    }

    public void getMyRouteListData() {
        this.page = 1;
        this.sinceId = 0;
        int i = this.tag;
        if (i == 0) {
            this.myRouteModel.getMyRoute(1, 0);
        } else {
            if (i != 1) {
                return;
            }
            this.myRouteModel.getFansRoute(1, 0);
        }
    }

    public void loadMoreMyRouteListData() {
        int i = this.nextPage;
        int i2 = this.page;
        if (i > i2) {
            int i3 = i2 + 1;
            this.page = i3;
            int i4 = this.tag;
            if (i4 == 0) {
                this.myRouteModel.getMyRoute(i3, this.sinceId);
            } else {
                if (i4 != 1) {
                    return;
                }
                this.myRouteModel.getFansRoute(i3, this.sinceId);
            }
        }
    }

    public void downLoadRouteById(int i, int i2) {
        this.myRouteModel.downLoadRouteById(i, i2);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetMyRouteListener
    public void onGetMyRouteDataSuccess(LikeRouteListModel likeRouteListModel) {
        this.myRouteView.hideProgress();
        this.nextPage = likeRouteListModel.getNext_page();
        if (likeRouteListModel.getMessages().size() > 0) {
            if (this.sinceId == 0) {
                this.sinceId = likeRouteListModel.getMessages().get(0).getId();
                this.myRouteView.setRecyclerView(likeRouteListModel.getMessages());
            } else {
                this.myRouteView.addDataToRecycler(likeRouteListModel.getMessages());
            }
            this.myRouteView.enableLoadMore();
            this.myRouteView.loadmoreComplete();
            this.myRouteView.refreshComplete();
            return;
        }
        this.myRouteView.disableLoadMore();
        this.myRouteView.showEmpyBoatIcon();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetMyRouteListener
    public void onGetMyRouteDataFailed(String str) {
        this.myRouteView.hideProgress();
        this.myRouteView.showGetDataErrorMsg(str);
        this.myRouteView.disableLoadMore();
        this.myRouteView.refreshComplete();
    }
}
