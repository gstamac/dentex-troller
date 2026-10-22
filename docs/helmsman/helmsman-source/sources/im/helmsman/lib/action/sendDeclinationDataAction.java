package im.helmsman.lib.action;

import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.entity.Declination;
import im.helmsman.lib.net.NetUtils;
import im.helmsman.lib.product.HMMotorH1;
import im.helmsman.lib.util.DecodeDeclinationDataFile;
import im.helmsman.lib.util.LocationUtil;
import im.helmsman.lib.util.MathUtil;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
public class sendDeclinationDataAction {
    public void action() {
        double[] position = LocationUtil.getPosition();
        if (position != null) {
            new NetUtils().downloadDeclinationDataFile(position[0], position[1], new NetUtils.OndownloadDeclinationDataFileCallback() { // from class: im.helmsman.lib.action.sendDeclinationDataAction.1
                @Override // im.helmsman.lib.net.NetUtils.OndownloadDeclinationDataFileCallback
                public void onComplete(File file) {
                    new DecodeDeclinationDataFile().decode(file, new DecodeDeclinationDataFile.OnDecodeDeclinationFinishListener() { // from class: im.helmsman.lib.action.sendDeclinationDataAction.1.1
                        @Override // im.helmsman.lib.util.DecodeDeclinationDataFile.OnDecodeDeclinationFinishListener
                        public void onFinish(Declination declination) {
                            sendDeclinationDataAction.this.sendDeclinationData(declination.getDeclination_uncertainty(), declination.getDeclination());
                        }
                    });
                }

                @Override // im.helmsman.lib.net.NetUtils.OndownloadDeclinationDataFileCallback
                public void onError() {
                    sendDeclinationDataAction.this.sendDeclinationData(0.0d, 0.0d);
                }
            });
            return;
        }
        sendDeclinationData(0.0d, 0.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDeclinationData(double d, double d2) {
        HMMotorH1.BoatControlComponentH1 boatControlComponentH1 = (HMMotorH1.BoatControlComponentH1) ((HMMotorH1) HelmsmanSDK.getProduct()).boatControlComponent;
        boatControlComponentH1.setParam((short) 9, 0.0f, MathUtil.double2Bytes(d2));
        boatControlComponentH1.setParam((short) 9, 0.0f, MathUtil.double2Bytes(d));
    }
}
