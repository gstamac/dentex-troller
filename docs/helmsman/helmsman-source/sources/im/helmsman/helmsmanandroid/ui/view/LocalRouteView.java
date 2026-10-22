package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface LocalRouteView extends MvpView {
    void dimissDownloadRouteProgress();

    void dismissProgress();

    void downloadFaile();

    void notRoute();

    void onDownloadFinish(List<MLatLng> list);

    void onDownloadProgress(int i, int i2);

    void showNoWifiMessage();

    void showProgressDialog();

    void showTooFarMessage();

    void showdownloadRouteProgress(int i, int i2);

    void updateProgress(String str);

    void uploadFailed(String str);

    void uploadFailureAndRetry();

    void uploadSuccess();
}
