package im.helmsman.helmsmanandroid.dao;

import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class WayPoint extends DataSupport {
    private int id;
    private double latitude;
    private double longitude;
    private RouteModel route;
    private int routeId;
    private int sequence;

    public WayPoint() {
    }

    public WayPoint(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int i) {
        this.sequence = i;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double d) {
        this.latitude = d;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double d) {
        this.longitude = d;
    }

    public int getRouteId() {
        return this.routeId;
    }

    public void setRouteId(int i) {
        this.routeId = i;
    }

    public RouteModel getRoute() {
        return this.route;
    }

    public void setRoute(RouteModel routeModel) {
        this.route = routeModel;
    }
}
