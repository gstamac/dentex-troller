package im.helmsman.helmsmanandroid.presenter;

import android.os.Handler;
import android.os.Looper;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.ConnectWifiView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.component.TeleControlComponent;
import im.helmsman.lib.mission.HMMissionManager;
import im.helmsman.lib.mission.HMTeleControlScanWifiMission;
import im.helmsman.lib.mission.common.HMMission;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectWifiPresenter extends BasePresenter<ConnectWifiView> implements TeleControlComponent.WifiLinker.OnConnectWifiCompleteListener {
    private HMMissionManager manager;
    private HMTeleControlScanWifiMission mission;

    @Override // im.helmsman.helmsmanandroid.presenter.common.BasePresenter
    public void attachView(ConnectWifiView connectWifiView) {
        super.attachView(connectWifiView);
        this.manager = HMMissionManager.getInstance();
        this.mission = new HMTeleControlScanWifiMission();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.common.BasePresenter
    public void detachView() {
        super.detachView();
        HMMissionManager hMMissionManager = this.manager;
        if (hMMissionManager != null) {
            hMMissionManager.releaseMissionManager();
        }
    }

    public void scanWifi() {
        final Handler handler = new Handler(Looper.getMainLooper());
        this.manager.downloadMissionExecution(this.mission, new HMMission.HMMissionDownloadProgress() { // from class: im.helmsman.helmsmanandroid.presenter.ConnectWifiPresenter.1
            @Override // im.helmsman.lib.mission.common.HMMission.HMMissionDownloadProgress
            public void onProgress(HMMission hMMission, float f, float f2) {
                final String str = f2 + " /" + f;
                handler.post(new Runnable() { // from class: im.helmsman.helmsmanandroid.presenter.ConnectWifiPresenter.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (ConnectWifiPresenter.this.view != null) {
                            ((ConnectWifiView) ConnectWifiPresenter.this.view).updateProgress(str);
                        }
                    }
                });
            }

            @Override // im.helmsman.lib.mission.common.HMMission.HMMissionDownloadProgress
            public void downloadFinish(final HMMission hMMission) {
                if (ConnectWifiPresenter.this.view == null || hMMission == null) {
                    return;
                }
                handler.post(new Runnable() { // from class: im.helmsman.helmsmanandroid.presenter.ConnectWifiPresenter.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        HMTeleControlScanWifiMission hMTeleControlScanWifiMission = (HMTeleControlScanWifiMission) hMMission;
                        if (hMTeleControlScanWifiMission != null) {
                            ((ConnectWifiView) ConnectWifiPresenter.this.view).initSSIDList(hMTeleControlScanWifiMission.getHmWayPoints());
                        }
                        ViewUtils.cancelProgressDialog();
                    }
                });
            }
        });
    }

    public void connectWifi(String str, String str2) {
        if (HelmsmanSDK.getTeleControlComponent() != null) {
            HelmsmanSDK.getTeleControlComponent().wifiLinker.ConnectWifi(str, str2);
            HelmsmanSDK.getTeleControlComponent().wifiLinker.setOnConnectWifiCompleteListener(this);
        }
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.WifiLinker.OnConnectWifiCompleteListener
    public void onconnectSuccess() {
        if (HelmsmanSDK.getTeleControlComponent() != null) {
            if (this.view != 0) {
                ((ConnectWifiView) this.view).connectWifiSuccess();
            }
            HelmsmanSDK.getTeleControlComponent().wifiLinker.setOnConnectWifiCompleteListener(null);
        }
    }
}
