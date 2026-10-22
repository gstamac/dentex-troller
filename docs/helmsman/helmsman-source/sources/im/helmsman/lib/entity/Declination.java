package im.helmsman.lib.entity;

/* JADX INFO: loaded from: classes2.dex */
public class Declination {
    private double declination;
    private double declination_uncertainty;

    public double getDeclination_uncertainty() {
        return this.declination_uncertainty;
    }

    public void setDeclination_uncertainty(double d) {
        this.declination_uncertainty = d;
    }

    public double getDeclination() {
        return this.declination;
    }

    public void setDeclination(double d) {
        this.declination = d;
    }

    public String toString() {
        return "declination:" + this.declination + ",declination_uncertainty:" + this.declination_uncertainty;
    }
}
