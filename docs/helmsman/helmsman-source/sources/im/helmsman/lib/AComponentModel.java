package im.helmsman.lib;

import com.MAVLink.Messages.MAVLinkMessage;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AComponentModel {
    public static final int ALL_COMPONENT = 0;
    public static final int BOATCONTROL_COMPONENT = 0;
    public static final int CONNECTED = 0;
    public static final int FOOTPLATE_COMPONENT = 0;
    public static final int PHONE_COMPONENT = 0;
    public static final int TELECONTROL_COMPONENT = 0;
    public static final int UNCONNECTED = 1;
    public static final int UNKNOW = 2;
    private static OnConnectStateChangeListener connectStateChangeListener;
    private int ConnectState = 1;
    protected int compId;
    private int firwareVersion;
    private int serialNum;

    public interface OnConnectStateChangeListener {
        void onStateChange(int i);
    }

    public abstract void decode(MAVLinkMessage mAVLinkMessage);

    public AComponentModel(int i, int i2) {
        this.serialNum = i;
        this.firwareVersion = i2;
    }

    public void setOnConnectStateChange(OnConnectStateChangeListener onConnectStateChangeListener) {
        connectStateChangeListener = onConnectStateChangeListener;
    }

    public int getConnectState() {
        return this.ConnectState;
    }

    public void setConnectState(int i) {
        if (i != this.ConnectState) {
            this.ConnectState = i;
            OnConnectStateChangeListener onConnectStateChangeListener = connectStateChangeListener;
            if (onConnectStateChangeListener != null) {
                onConnectStateChangeListener.onStateChange(i);
            }
        }
    }

    public int getFirwareVersion() {
        return this.firwareVersion;
    }

    protected void setFirwareVersion(int i) {
        this.firwareVersion = i;
    }

    public int getSerialNum() {
        return this.serialNum;
    }

    protected void setSerialNum(int i) {
        this.serialNum = i;
    }

    public int getCompId() {
        return this.compId;
    }

    public void setCompId(int i) {
        this.compId = i;
    }
}
