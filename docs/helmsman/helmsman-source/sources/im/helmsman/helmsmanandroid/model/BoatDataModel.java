package im.helmsman.helmsmanandroid.model;

import android.location.Location;

/* JADX INFO: loaded from: classes2.dex */
public class BoatDataModel {
    private int controllerVersion;
    private float direction;
    private android.telephony.SignalStrength gpsStrength;
    private short gpsStrengthValue;
    private Location location;
    private boolean motorEnable;
    private short motospeed;
    private float power;
    private float speed;
    private int trollingMotorType;
    private boolean wifiConnectioned;

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float f) {
        this.speed = f;
    }

    public short getGpsStrengthValue() {
        return this.gpsStrengthValue;
    }

    public void setGpsStrengthValue(short s) {
        this.gpsStrengthValue = s;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float getDirection() {
        return this.direction;
    }

    public void setDirection(float f) {
        this.direction = f;
    }

    public short getMotospeed() {
        return this.motospeed;
    }

    public void setMotospeed(short s) {
        this.motospeed = s;
    }

    public boolean isMotorEnable() {
        return this.motorEnable;
    }

    public void setMotorEnable(boolean z) {
        this.motorEnable = z;
    }

    public float getPower() {
        return this.power;
    }

    public void setPower(float f) {
        this.power = f;
    }

    public boolean isWifiConnectioned() {
        return this.wifiConnectioned;
    }

    public void setWifiConnectioned(boolean z) {
        this.wifiConnectioned = z;
    }

    public int getTrollingMotorType() {
        return this.trollingMotorType;
    }

    public void setTrollingMotorType(int i) {
        this.trollingMotorType = i;
    }

    public int getControllerVersion() {
        return this.controllerVersion;
    }

    public void setControllerVersion(int i) {
        this.controllerVersion = i;
    }

    public android.telephony.SignalStrength getGpsStrength() {
        return this.gpsStrength;
    }

    public void setGpsStrength(android.telephony.SignalStrength signalStrength) {
        this.gpsStrength = signalStrength;
    }
}
