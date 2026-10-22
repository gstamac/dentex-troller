package im.helmsman.helmsmanandroid.presenter;

import android.util.Log;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.UpLoadWayPointSuccess;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity;
import im.helmsman.helmsmanandroid.ui.activity.MyFollowingRouteActivity;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.view.AddRouteView;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.CoordinateTransform;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.SphericalUtil;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.DataChangeSubscribe;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.component.BoatControlComponent;
import im.helmsman.lib.error.HMError;
import im.helmsman.lib.mission.HMMissionManager;
import im.helmsman.lib.mission.HMWayPoint;
import im.helmsman.lib.mission.HMWayPointMission;
import im.helmsman.lib.mission.common.HMMission;
import im.helmsman.lib.util.HMCommonCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public class AddRoutePresenter extends BasePresenter<AddRouteView> {
    private List<MLatLng> mBeginLatlngs = new ArrayList();
    private BoatControlDataChangeOberver observer;

    @Override // im.helmsman.helmsmanandroid.presenter.common.BasePresenter
    public void attachView(AddRouteView addRouteView) {
        super.attachView(addRouteView);
        BoatControlDataChangeOberver boatControlDataChangeOberver = new BoatControlDataChangeOberver();
        this.observer = boatControlDataChangeOberver;
        DataChangeSubscribe.subscribe(boatControlDataChangeOberver);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.common.BasePresenter
    public void detachView() {
        super.detachView();
        DataChangeSubscribe.unSubscrible(this.observer);
    }

    public void drawMapById(int i) {
        RouteModel wayPointListById;
        if (i == 0 || this.view == 0 || (wayPointListById = DaoUtil.getInstance().getWayPointListById(i)) == null) {
            return;
        }
        List<MLatLng> listLatLngWGS84ToGCJ02 = CoordinateTransform.latLngWGS84ToGCJ02(DaoUtil.getInstance().wayPointListToLatLng(wayPointListById.getWaypoint()));
        int i2 = Config.MAP_TYPE;
        if (i2 == 1) {
            ((AddRouteView) this.view).drawLineAndPointToGoogleMap(MLatLng.toGoogleLatlngs(listLatLngWGS84ToGCJ02), listLatLngWGS84ToGCJ02);
            Collections.copy(listLatLngWGS84ToGCJ02, this.mBeginLatlngs);
        } else {
            if (i2 != 2) {
                return;
            }
            ((AddRouteView) this.view).drawLineAndPointToAMap(MLatLng.toAmapLatlngs(listLatLngWGS84ToGCJ02), listLatLngWGS84ToGCJ02);
            Collections.copy(listLatLngWGS84ToGCJ02, this.mBeginLatlngs);
        }
    }

    public void uploadWayPoint(List<MLatLng> list, int i) {
        float fComputerAllPointDis;
        List<MLatLng> listLatLngGCJ024ToWGS84 = CoordinateTransform.latLngGCJ024ToWGS84(list);
        if (HelmsmanSDK.productIsNull()) {
            if (this.view != 0) {
                ((AddRouteView) this.view).nowifiError();
                return;
            }
            return;
        }
        int i2 = MyApplication.getInstance().gpsState;
        if (i2 == 0 || i2 == 4) {
            if (this.view != 0) {
                ((AddRouteView) this.view).noGpsError();
                return;
            }
            return;
        }
        ABaseProduct product = HelmsmanSDK.getProduct();
        MLatLng mLatLng = new MLatLng(product.boatControlComponent.gpsProperty.getLatitude(), product.boatControlComponent.gpsProperty.getLongitude());
        if (listLatLngGCJ024ToWGS84.size() > 0) {
            MLatLng mLatLng2 = listLatLngGCJ024ToWGS84.get(0);
            ArrayList arrayList = new ArrayList();
            arrayList.add(mLatLng2);
            arrayList.add(mLatLng);
            fComputerAllPointDis = (float) CoordinateParserUtil.computerAllPointDis(arrayList);
        } else {
            fComputerAllPointDis = 0.0f;
        }
        if (checkPolyLineDisIsMoreThan2Km(listLatLngGCJ024ToWGS84)) {
            ((AddRouteView) this.view).showTooFarMessage();
            return;
        }
        if (fComputerAllPointDis <= 2000.0f || MyApplication.getInstance().isApkInDebug()) {
            ((AddRouteView) this.view).showProgressDialog("");
            HMWayPointMission hMWayPointMission = new HMWayPointMission();
            for (MLatLng mLatLng3 : listLatLngGCJ024ToWGS84) {
                hMWayPointMission.addWayPoint(new HMWayPoint(mLatLng3.latitude, mLatLng3.longitude));
            }
            final HMMissionManager hMMissionManager = HMMissionManager.getInstance();
            hMMissionManager.prepareMission(hMWayPointMission, new HMMission.HMMissionProgressHandler() { // from class: im.helmsman.helmsmanandroid.presenter.AddRoutePresenter.1
                @Override // im.helmsman.lib.mission.common.HMMission.HMMissionProgressHandler
                public void onProgress(HMMission hMMission, float f, float f2) {
                    if (AddRoutePresenter.this.view != null) {
                        ((AddRouteView) AddRoutePresenter.this.view).updateProgressMessage(f2 + "/" + f);
                    }
                }

                @Override // im.helmsman.lib.mission.common.HMMission.HMMissionProgressHandler
                public void onPrepareFinish(HMMission hMMission) {
                    if (AddRoutePresenter.this.view != null) {
                        ((AddRouteView) AddRoutePresenter.this.view).markersToPoints();
                        ((AddRouteView) AddRoutePresenter.this.view).dismissProgress();
                        hMMissionManager.releaseMissionManager();
                        ((AddRouteView) AddRoutePresenter.this.view).screenShotAndSave(new AddRouteActivity.OnSaveWayPointListener() { // from class: im.helmsman.helmsmanandroid.presenter.AddRoutePresenter.1.1
                            @Override // im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.OnSaveWayPointListener
                            public void onSaveComplete(int i3) {
                                EventBus.getDefault().post(new UpLoadWayPointSuccess(i3));
                                Base2Activity.getTopActivity().finish();
                                Base2Activity.finishActivity(MyFollowingRouteActivity.class);
                            }
                        });
                    }
                    Log.e("addroute", "onPrepareFinish");
                }
            }, new HMCommonCallback.HMCompletionCallback() { // from class: im.helmsman.helmsmanandroid.presenter.AddRoutePresenter.2
                @Override // im.helmsman.lib.util.HMCommonCallback.HMCompletionCallback
                public void onResult(HMError hMError) {
                    hMMissionManager.releaseMissionManager();
                    if (hMError.code != -1 || AddRoutePresenter.this.view == null) {
                        return;
                    }
                    ((AddRouteView) AddRoutePresenter.this.view).uploadFailureAndRetry();
                    ((AddRouteView) AddRoutePresenter.this.view).dismissProgress();
                }
            });
            return;
        }
        ((AddRouteView) this.view).showOrignTooFarMessage();
    }

    public void saveWayPoint() {
        if (this.view != 0) {
            ((AddRouteView) this.view).moveToMapCenter();
            ((AddRouteView) this.view).screenShotAndSave(null);
        }
    }

    private boolean checkPolyLineDisIsMoreThan2Km(List<MLatLng> list) {
        for (int i = 1; i < list.size(); i++) {
            if (SphericalUtil.computeDistanceBetween(list.get(i - 1), list.get(i)) > 2000.0d && this.view != 0) {
                return true;
            }
        }
        return false;
    }

    class BoatControlDataChangeOberver extends BoatControlComponent.BoatControlComponentDataChangeAdapter {
        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void aidAck() {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void batteryElectricityLevel(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void boatControlModel(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void boatSpeed(float f) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void deviceInfo(int i, int i2, int i3, String str) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void firmwareAck() {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void gpsSignal(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void onBoatControlConnectStateChange(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void requestFirmwareData(long j, int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeDownloadWaypoint(float f, float f2, int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeDownloadWaypointCount(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeUploadComplete() {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeUploadRequestWaypoint(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void targeterPoint(int i) {
        }

        BoatControlDataChangeOberver() {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void boatDegress(float f) {
            Log.e("degress", f + "");
            float degrees = (float) Math.toDegrees(f);
            if (AddRoutePresenter.this.view != null) {
                ((AddRouteView) AddRoutePresenter.this.view).boatDirection(degrees);
            }
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void boatPosition(float f, float f2) {
            if (AddRoutePresenter.this.view != null) {
                ((AddRouteView) AddRoutePresenter.this.view).boatPosition(new MLatLng(f, f2));
            }
        }
    }
}
