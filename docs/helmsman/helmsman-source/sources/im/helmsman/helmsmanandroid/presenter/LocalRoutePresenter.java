package im.helmsman.helmsmanandroid.presenter;

import android.util.Log;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.dao.WayPoint;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.UpLoadWayPointSuccess;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.LocalRouteView;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.error.HMError;
import im.helmsman.lib.mission.HMMissionManager;
import im.helmsman.lib.mission.HMWayPoint;
import im.helmsman.lib.mission.HMWayPointMission;
import im.helmsman.lib.mission.common.HMMission;
import im.helmsman.lib.util.HMCommonCallback;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public class LocalRoutePresenter extends BasePresenter<LocalRouteView> {
    private List<HMWayPoint> tempList = new ArrayList();
    private int upLoadId;

    public void sendWayPointToBoat(RouteModel routeModel) {
        if (HelmsmanSDK.productIsNull()) {
            ((LocalRouteView) this.view).showNoWifiMessage();
            return;
        }
        if (MyApplication.getInstance().gpsState == 0 || MyApplication.getInstance().gpsState == 4) {
            ViewUtils.ShowToast(R.string.main_no_gps_data_received);
            return;
        }
        this.tempList.clear();
        ABaseProduct product = HelmsmanSDK.getProduct();
        MLatLng mLatLng = new MLatLng(product.boatControlComponent.gpsProperty.getLatitude(), product.boatControlComponent.gpsProperty.getLongitude());
        if (routeModel.getWaypoint().size() > 0) {
            WayPoint wayPoint = routeModel.getWaypoint().get(0);
            MLatLng mLatLng2 = new MLatLng((float) wayPoint.getLatitude(), (float) wayPoint.getLongitude());
            ArrayList arrayList = new ArrayList();
            arrayList.add(mLatLng2);
            arrayList.add(mLatLng);
            CoordinateParserUtil.computerAllPointDis(arrayList);
        }
        this.upLoadId = routeModel.getId();
        for (WayPoint wayPoint2 : routeModel.getWaypoint()) {
            this.tempList.add(new HMWayPoint((float) wayPoint2.getLatitude(), (float) wayPoint2.getLongitude()));
        }
        if (MyApplication.getInstance().wifiState) {
            ((LocalRouteView) this.view).showProgressDialog();
            HMWayPointMission hMWayPointMission = new HMWayPointMission();
            hMWayPointMission.addWayPoints(this.tempList);
            final HMMissionManager hMMissionManager = HMMissionManager.getInstance();
            hMMissionManager.prepareMission(hMWayPointMission, new HMMission.HMMissionProgressHandler() { // from class: im.helmsman.helmsmanandroid.presenter.LocalRoutePresenter.1
                @Override // im.helmsman.lib.mission.common.HMMission.HMMissionProgressHandler
                public void onProgress(HMMission hMMission, float f, float f2) {
                    if (LocalRoutePresenter.this.view != null) {
                        ((LocalRouteView) LocalRoutePresenter.this.view).updateProgress(MyApplication.getContext().getString(R.string.route_uploading) + ": " + f2 + "/" + f);
                    }
                }

                @Override // im.helmsman.lib.mission.common.HMMission.HMMissionProgressHandler
                public void onPrepareFinish(HMMission hMMission) {
                    EventBus.getDefault().post(new UpLoadWayPointSuccess(LocalRoutePresenter.this.upLoadId));
                    if (LocalRoutePresenter.this.view != null) {
                        ((LocalRouteView) LocalRoutePresenter.this.view).uploadSuccess();
                    }
                    hMMissionManager.startMissionExecution(null);
                    hMMissionManager.releaseMissionManager();
                }
            }, new HMCommonCallback.HMCompletionCallback() { // from class: im.helmsman.helmsmanandroid.presenter.LocalRoutePresenter.2
                @Override // im.helmsman.lib.util.HMCommonCallback.HMCompletionCallback
                public void onResult(HMError hMError) {
                    hMMissionManager.releaseMissionManager();
                    if (hMError.code != -1 || LocalRoutePresenter.this.view == null) {
                        return;
                    }
                    ((LocalRouteView) LocalRoutePresenter.this.view).uploadFailureAndRetry();
                    ((LocalRouteView) LocalRoutePresenter.this.view).dismissProgress();
                }
            });
            return;
        }
        ((LocalRouteView) this.view).showNoWifiMessage();
    }

    public void requestDownloadRoute() {
        final HMMissionManager hMMissionManager = HMMissionManager.getInstance();
        hMMissionManager.downloadMissionExecution(new HMWayPointMission(), new HMMission.HMMissionDownloadProgress() { // from class: im.helmsman.helmsmanandroid.presenter.LocalRoutePresenter.3
            @Override // im.helmsman.lib.mission.common.HMMission.HMMissionDownloadProgress
            public void onProgress(HMMission hMMission, float f, float f2) {
                Log.e("downloadRoute_progress", f2 + "");
                if (LocalRoutePresenter.this.view != null) {
                    if (hMMission == null) {
                        ((LocalRouteView) LocalRoutePresenter.this.view).notRoute();
                    } else {
                        ((LocalRouteView) LocalRoutePresenter.this.view).onDownloadProgress((int) f2, (int) f);
                    }
                }
            }

            @Override // im.helmsman.lib.mission.common.HMMission.HMMissionDownloadProgress
            public void downloadFinish(HMMission hMMission) {
                if (LocalRoutePresenter.this.view != null) {
                    if (hMMission == null) {
                        ((LocalRouteView) LocalRoutePresenter.this.view).downloadFaile();
                    } else {
                        ((LocalRouteView) LocalRoutePresenter.this.view).dimissDownloadRouteProgress();
                        List<HMWayPoint> hmWayPoints = ((HMWayPointMission) hMMission).getHmWayPoints();
                        ArrayList arrayList = new ArrayList();
                        for (HMWayPoint hMWayPoint : hmWayPoints) {
                            arrayList.add(new MLatLng((float) hMWayPoint.latitude, (float) hMWayPoint.longitude));
                        }
                        ((LocalRouteView) LocalRoutePresenter.this.view).onDownloadFinish(arrayList);
                    }
                }
                hMMissionManager.releaseMissionManager();
            }
        });
    }
}
