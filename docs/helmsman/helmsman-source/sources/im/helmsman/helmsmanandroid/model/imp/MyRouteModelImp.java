package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.model.MyRouteModel;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetMyRouteListener;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class MyRouteModelImp implements MyRouteModel {
    private OnGetMyRouteListener onGetMyRouteListener;

    public MyRouteModelImp(OnGetMyRouteListener onGetMyRouteListener) {
        this.onGetMyRouteListener = onGetMyRouteListener;
    }

    @Override // im.helmsman.helmsmanandroid.model.MyRouteModel
    public void getMyRoute(int i, int i2) {
        RequestInetUtils.instance().getMyMessage(i, i2, new Callback<LikeRouteListModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.MyRouteModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<LikeRouteListModel> response) {
                MyRouteModelImp.this.onGetMyRouteListener.onGetMyRouteDataSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                MyRouteModelImp.this.onGetMyRouteListener.onGetMyRouteDataFailed(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.MyRouteModel
    public void getFansRoute(int i, int i2) {
        RequestInetUtils.instance().getMyFollowedMessage(i2, i, new Callback<LikeRouteListModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.MyRouteModelImp.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<LikeRouteListModel> response) {
                MyRouteModelImp.this.onGetMyRouteListener.onGetMyRouteDataSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                MyRouteModelImp.this.onGetMyRouteListener.onGetMyRouteDataFailed(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.MyRouteModel
    public void downLoadRouteById(int i, int i2) {
        RequestInetUtils.instance().downLoadRouteById(i, i2, null);
    }
}
