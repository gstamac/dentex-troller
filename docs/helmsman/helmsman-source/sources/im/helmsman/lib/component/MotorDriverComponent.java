package im.helmsman.lib.component;

import com.MAVLink.Messages.MAVLinkMessage;
import im.helmsman.lib.AComponentModel;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MotorDriverComponent extends AComponentModel {
    private boolean isOn;
    private int speed;

    @Override // im.helmsman.lib.AComponentModel
    public void decode(MAVLinkMessage mAVLinkMessage) {
    }

    public abstract void maxSpeed();

    public abstract void motorOff();

    public abstract void motorOn();

    public abstract void speedDown();

    public abstract void speedUp();

    public abstract void turnLeft();

    public abstract void turnRight();

    public MotorDriverComponent(int i, int i2) {
        super(i, i2);
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int i) {
        this.speed = i;
    }

    public boolean isOn() {
        return this.isOn;
    }

    public void setOn(boolean z) {
        this.isOn = z;
    }
}
