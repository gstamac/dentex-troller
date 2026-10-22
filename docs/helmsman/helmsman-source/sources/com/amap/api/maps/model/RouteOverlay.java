package com.amap.api.maps.model;

import com.autonavi.ae.gmap.gloverlay.BaseRouteOverlay;
import com.autonavi.ae.gmap.gloverlay.GLRouteProperty;

/* JADX INFO: loaded from: classes.dex */
public class RouteOverlay {
    BaseRouteOverlay a;

    public RouteOverlay(BaseRouteOverlay baseRouteOverlay) {
        this.a = baseRouteOverlay;
    }

    public void addRouteItem(int i, GLRouteProperty[] gLRoutePropertyArr, boolean z, long j, int i2) {
        BaseRouteOverlay baseRouteOverlay = this.a;
        if (baseRouteOverlay != null) {
            baseRouteOverlay.addRouteItem(i, gLRoutePropertyArr, z, j, i2);
        }
    }

    public void removeRouteName() {
        BaseRouteOverlay baseRouteOverlay = this.a;
        if (baseRouteOverlay != null) {
            baseRouteOverlay.removeRouteName();
        }
    }

    public void remove() {
        BaseRouteOverlay baseRouteOverlay = this.a;
        if (baseRouteOverlay != null) {
            baseRouteOverlay.remove();
        }
    }
}
