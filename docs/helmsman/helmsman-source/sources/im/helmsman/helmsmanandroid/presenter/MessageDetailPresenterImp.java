package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.model.MyRouteModel;
import im.helmsman.helmsmanandroid.model.imp.MyRouteModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetMyRouteListener;
import im.helmsman.helmsmanandroid.ui.view.MessageDetailView;

/* JADX INFO: loaded from: classes2.dex */
public class MessageDetailPresenterImp extends BasePresenter<MessageDetailView> implements OnGetMyRouteListener {
    private MyRouteModel myRouteModel = new MyRouteModelImp(this);

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetMyRouteListener
    public void onGetMyRouteDataFailed(String str) {
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnGetMyRouteListener
    public void onGetMyRouteDataSuccess(LikeRouteListModel likeRouteListModel) {
    }

    public void downloadWayPointByRouteId(int i) {
        this.myRouteModel.downLoadRouteById(i, -1);
    }
}
