package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;

/* JADX INFO: loaded from: classes2.dex */
public interface OnGetMyShareRouteListener {
    void onGetMyShareRouteFailed(String str);

    void onGetMyShareRouteSuccess(LikeRouteListModel likeRouteListModel);
}
