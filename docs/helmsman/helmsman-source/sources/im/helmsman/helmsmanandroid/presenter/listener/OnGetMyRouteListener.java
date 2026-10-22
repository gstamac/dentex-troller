package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;

/* JADX INFO: loaded from: classes2.dex */
public interface OnGetMyRouteListener {
    void onGetMyRouteDataFailed(String str);

    void onGetMyRouteDataSuccess(LikeRouteListModel likeRouteListModel);
}
