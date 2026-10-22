package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.model.MyShareRouteModel;
import im.helmsman.helmsmanandroid.presenter.listener.OnGetMyShareRouteListener;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class MyShareRouteModelImp implements MyShareRouteModel {
    @Override // im.helmsman.helmsmanandroid.model.MyShareRouteModel
    public void getMyShareRoute(int i, int i2, final OnGetMyShareRouteListener onGetMyShareRouteListener) {
        RequestInetUtils.instance().getMyMessage(i, i2, new Callback<LikeRouteListModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.MyShareRouteModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<LikeRouteListModel> response) {
                onGetMyShareRouteListener.onGetMyShareRouteSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                onGetMyShareRouteListener.onGetMyShareRouteFailed(errorCode.getMessage());
            }
        });
    }
}
