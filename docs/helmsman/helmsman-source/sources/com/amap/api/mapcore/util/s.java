package com.amap.api.mapcore.util;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.RemoteException;
import com.amap.api.maps.model.AMapCameraInfo;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.TileProjection;
import com.amap.api.maps.model.VisibleRegion;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: compiled from: ProjectionDelegateImp.java */
/* JADX INFO: loaded from: classes.dex */
class s implements m {
    private k a;

    public s(k kVar) {
        this.a = kVar;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public LatLng fromScreenLocation(Point point) throws RemoteException {
        if (point == null) {
            return null;
        }
        DPoint dPointObtain = DPoint.obtain();
        this.a.b(point.x, point.y, dPointObtain);
        LatLng latLng = new LatLng(dPointObtain.y, dPointObtain.x);
        dPointObtain.recycle();
        return latLng;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public Point toScreenLocation(LatLng latLng) throws RemoteException {
        if (latLng == null) {
            return null;
        }
        IPoint iPointObtain = IPoint.obtain();
        this.a.b(latLng.latitude, latLng.longitude, iPointObtain);
        Point point = new Point(iPointObtain.x, iPointObtain.y);
        iPointObtain.recycle();
        return point;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public VisibleRegion getVisibleRegion() throws RemoteException {
        int mapWidth = this.a.getMapWidth();
        int mapHeight = this.a.getMapHeight();
        LatLng latLngFromScreenLocation = fromScreenLocation(new Point(0, 0));
        LatLng latLngFromScreenLocation2 = fromScreenLocation(new Point(mapWidth, 0));
        LatLng latLngFromScreenLocation3 = fromScreenLocation(new Point(0, mapHeight));
        LatLng latLngFromScreenLocation4 = fromScreenLocation(new Point(mapWidth, mapHeight));
        return new VisibleRegion(latLngFromScreenLocation3, latLngFromScreenLocation4, latLngFromScreenLocation, latLngFromScreenLocation2, LatLngBounds.builder().include(latLngFromScreenLocation3).include(latLngFromScreenLocation4).include(latLngFromScreenLocation).include(latLngFromScreenLocation2).build());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public PointF toMapLocation(LatLng latLng) throws RemoteException {
        if (latLng == null) {
            return null;
        }
        FPoint fPointObtain = FPoint.obtain();
        this.a.a(latLng.latitude, latLng.longitude, fPointObtain);
        PointF pointF = new PointF(fPointObtain.x, fPointObtain.y);
        fPointObtain.recycle();
        return pointF;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public float toMapLenWithWin(int i) {
        if (i <= 0) {
            return 0.0f;
        }
        return this.a.g(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public TileProjection fromBoundsToTile(LatLngBounds latLngBounds, int i, int i2) throws RemoteException {
        if (latLngBounds == null || i < 0 || i > 20 || i2 <= 0) {
            return null;
        }
        IPoint iPointObtain = IPoint.obtain();
        IPoint iPointObtain2 = IPoint.obtain();
        this.a.a(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude, iPointObtain);
        this.a.a(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude, iPointObtain2);
        int i3 = 20 - i;
        int i4 = (iPointObtain.x >> i3) / i2;
        int i5 = (iPointObtain.y >> i3) / i2;
        int i6 = (iPointObtain2.x >> i3) / i2;
        int i7 = (iPointObtain2.y >> i3) / i2;
        int i8 = (iPointObtain.x - ((i4 << i3) * i2)) >> i3;
        int i9 = iPointObtain2.y;
        iPointObtain.recycle();
        iPointObtain2.recycle();
        return new TileProjection(i8, (i9 - ((i7 << i3) * i2)) >> i3, i4, i6, i7, i5);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public LatLngBounds getMapBounds(LatLng latLng, float f) throws RemoteException {
        k kVar = this.a;
        if (kVar == null || latLng == null) {
            return null;
        }
        return kVar.a(latLng, f, 0.0f, 0.0f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IProjection
    public AMapCameraInfo getCameraInfo() {
        return this.a.getCamerInfo();
    }
}
