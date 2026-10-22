package im.helmsman.lib;

import com.MAVLink.Messages.MAVLinkMessage;
import im.helmsman.lib.component.BoatControlComponent;
import im.helmsman.lib.component.MotorDriverComponent;
import im.helmsman.lib.component.TeleControlComponent;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ABaseProduct {
    public static final int CONNECTED = 0;
    public static final int UNCONNECT = 2;
    public static final int UNKNOW = 1;
    protected static DataManager dataManager;
    protected static DataIOManager manager;
    private static ABaseProduct product;
    private int ConnectState;
    public BoatControlComponent boatControlComponent;
    private int disconnectProtection = 1;
    protected int firwareVersion;
    protected OnConnectStateChangeListener listener;
    public MotorDriverComponent motorComponent;
    protected OnUploadWayPointRequestListener onUploadWayPointRequestListener;
    private String serialNum;
    public TeleControlComponent teleControlComponent;
    private int type;

    public interface OnConnectStateChangeListener {
        void onStateChange(int i);
    }

    public interface OnDownLoadWaypointListener {
        void downloadWaypointCount(int i);

        void ondownloadProgress(float f, float f2, int i);
    }

    public interface OnUploadWayPointRequestListener {
        void onUploadRequest(int i);

        void onUploadRouteComplete();
    }

    public abstract void initComponent();

    public abstract void onReceiverData(MAVLinkMessage mAVLinkMessage);

    public ABaseProduct(int i, int i2) {
        product = this;
        this.type = i;
        this.firwareVersion = i2;
        manager = HelmsmanSDK.getDataIOManager();
        dataManager = DataManager.getInstance();
    }

    public static ABaseProduct getInstance() {
        return product;
    }

    public int getDisconnectProtection() {
        return this.disconnectProtection;
    }

    public void setDisconnectProtection(int i) {
        this.disconnectProtection = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getConnectState() {
        return this.ConnectState;
    }

    public void setConnectState(int i) {
        if (i != this.ConnectState) {
            this.ConnectState = i;
            OnConnectStateChangeListener onConnectStateChangeListener = this.listener;
            if (onConnectStateChangeListener != null) {
                onConnectStateChangeListener.onStateChange(i);
            }
        }
    }

    public int getFirwareVersion() {
        return this.firwareVersion;
    }

    public void setFirwareVersion(int i) {
        this.firwareVersion = i;
    }

    public String getSerialNum() {
        return this.serialNum;
    }

    public void setSerialNum(String str) {
        this.serialNum = str;
    }

    public void setOnConnectStateChangeListener(OnConnectStateChangeListener onConnectStateChangeListener) {
        this.listener = onConnectStateChangeListener;
    }

    public void setOnUploadWayPointRequestListener(OnUploadWayPointRequestListener onUploadWayPointRequestListener) {
        this.onUploadWayPointRequestListener = onUploadWayPointRequestListener;
    }
}
