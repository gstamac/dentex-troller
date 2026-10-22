package im.helmsman.helmsmanandroid.ui.view;

import com.amap.api.maps.model.LatLng;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface AddRouteView extends MvpView {
    void boatDirection(float f);

    void boatPosition(MLatLng mLatLng);

    void dismissProgress();

    void drawLineAndPointToAMap(List<LatLng> list, List<MLatLng> list2);

    void drawLineAndPointToGoogleMap(List<com.google.android.gms.maps.model.LatLng> list, List<MLatLng> list2);

    void markersToPoints();

    void moveToMapCenter();

    void noGpsError();

    void nowifiError();

    void screenShotAndSave(AddRouteActivity.OnSaveWayPointListener onSaveWayPointListener);

    void showMoreThan2KmDistanceDialog();

    void showOrignTooFarMessage();

    void showProgressDialog(String str);

    void showTooFarMessage();

    void updateProgressMessage(String str);

    void uploadFailureAndRetry();
}
