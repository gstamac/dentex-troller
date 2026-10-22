package im.helmsman.helmsmanandroid.polyline.it.rambow.master.javautils;

/* JADX INFO: loaded from: classes2.dex */
public class Trackpoint {
    private double altitude;
    private double latDouble;
    private double lonDouble;

    public Trackpoint(double d, double d2) {
        this.latDouble = d;
        this.lonDouble = d2;
    }

    public Trackpoint(double d, double d2, double d3) {
        this.latDouble = d;
        this.lonDouble = d2;
        this.altitude = d3;
    }

    public void setLatDouble(double d) {
        this.latDouble = d;
    }

    public void setLonDouble(double d) {
        this.lonDouble = d;
    }

    public double getLatDouble() {
        return this.latDouble;
    }

    public double getLonDouble() {
        return this.lonDouble;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double d) {
        this.altitude = d;
    }
}
