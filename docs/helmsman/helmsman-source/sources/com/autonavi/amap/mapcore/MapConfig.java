package com.autonavi.amap.mapcore;

import com.amap.api.maps.model.LatLngBounds;

/* JADX INFO: loaded from: classes.dex */
public class MapConfig implements Cloneable {
    public static final int DEFAULT_RATIO = 1;
    private static final int GEO_POINT_ZOOM = 20;
    public static final float MAX_ZOOM = 19.0f;
    public static final float MAX_ZOOM_INDOOR = 20.0f;
    public static final float MIN_ZOOM = 3.0f;
    public static final int MSG_ACTION_ONBASEPOICLICK = 20;
    public static final int MSG_ACTION_ONMAPCLICK = 19;
    public static final int MSG_AUTH_FAILURE = 2;
    public static final int MSG_CALLBACK_MAPLOADED = 16;
    public static final int MSG_CALLBACK_ONTOUCHEVENT = 14;
    public static final int MSG_CALLBACK_SCREENSHOT = 15;
    public static final int MSG_CAMERAUPDATE_CHANGE = 10;
    public static final int MSG_CAMERAUPDATE_FINISH = 11;
    public static final int MSG_COMPASSVIEW_CHANGESTATE = 13;
    public static final int MSG_INFOWINDOW_UPDATE = 18;
    public static final int MSG_TILEOVERLAY_REFRESH = 17;
    public static final int MSG_ZOOMVIEW_CHANGESTATE = 12;
    private static final int TILE_SIZE_POW = 8;
    private String customTextureResourcePath;
    private boolean isSetLimitZoomLevel;
    MapConfig last_mapconfig;
    private IPoint[] limitIPoints;
    private LatLngBounds limitLatLngBounds;
    private String mCustomStylePath;
    private int mapHeight;
    private float mapPerPixelUnitLength;
    private int mapWidth;
    public float maxZoomLevel = 19.0f;
    public float minZoomLevel = 3.0f;
    private FPoint[] mapRect = null;
    private Rectangle geoRectangle = new Rectangle();
    private boolean isIndoorEnable = false;
    private boolean isBuildingEnable = true;
    private boolean isMapTextEnable = true;
    private boolean isTrafficEnabled = false;
    private boolean isCustomStyleEnabled = false;
    private int s_x = 221010267;
    private int s_y = 101697799;
    private float s_z = 10.0f;
    private float s_c = 0.0f;
    private float s_r = 0.0f;
    private boolean isCenterChanged = false;
    private boolean isZoomChanged = false;
    private boolean isTiltChanged = false;
    private boolean isBearingChanged = false;
    private boolean isNeedUpdateZoomControllerState = false;
    private boolean isNeedUpdateMapRectNextFrame = false;
    private int mMapStyleMode = 0;
    private int mMapStyleTime = 0;
    private int mMapStyleState = 0;
    private int anchorX = 0;
    private int anchorY = 0;
    private int customBackgroundColor = -1;
    private float mapZoomScale = 1.0f;
    private volatile int changedCounter = 0;
    private volatile double changeRatio = 1.0d;
    private volatile double changeGridRatio = 1.0d;
    private int grid_x = 0;
    private int grid_y = 0;

    public int getAnchorY() {
        return this.anchorY;
    }

    public void setAnchorY(int i) {
        this.anchorY = i;
    }

    public int getAnchorX() {
        return this.anchorX;
    }

    public void setAnchorX(int i) {
        this.anchorX = i;
    }

    public MapConfig(boolean z) {
        this.last_mapconfig = null;
        if (z) {
            MapConfig mapConfig = new MapConfig(false);
            this.last_mapconfig = mapConfig;
            mapConfig.setGridXY(0, 0);
            this.last_mapconfig.setS_x(0);
            this.last_mapconfig.setS_y(0);
            this.last_mapconfig.setS_z(0.0f);
            this.last_mapconfig.setS_c(0.0f);
            this.last_mapconfig.setS_r(0.0f);
        }
    }

    public int getChangedCounter() {
        return this.changedCounter;
    }

    public void resetChangedCounter() {
        this.changedCounter = 0;
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0057  */
    public boolean isMapStateChange() {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig == null) {
            return false;
        }
        int s_x = mapConfig.getS_x();
        int s_y = this.last_mapconfig.getS_y();
        float s_z = this.last_mapconfig.getS_z();
        float s_c = this.last_mapconfig.getS_c();
        float s_r = this.last_mapconfig.getS_r();
        boolean z = s_x != this.s_x;
        this.isCenterChanged = z;
        if (s_y != this.s_y) {
            z = true;
        }
        this.isCenterChanged = z;
        float f = this.s_z;
        boolean z2 = s_z != f;
        this.isZoomChanged = z2;
        if (z2) {
            float f2 = this.minZoomLevel;
            if (s_z <= f2 || f <= f2) {
                this.isNeedUpdateZoomControllerState = true;
            } else {
                float f3 = this.maxZoomLevel;
                if (s_z >= f3 || f >= f3) {
                    this.isNeedUpdateZoomControllerState = true;
                } else {
                    this.isNeedUpdateZoomControllerState = false;
                }
            }
        }
        boolean z3 = s_c != this.s_c;
        this.isTiltChanged = z3;
        boolean z4 = s_r != this.s_r;
        this.isBearingChanged = z4;
        boolean z5 = z || z2 || z3 || z4 || this.isNeedUpdateMapRectNextFrame;
        if (z5) {
            this.isNeedUpdateMapRectNextFrame = false;
            this.changedCounter++;
            int i = (20 - ((int) this.s_z)) + 8;
            setGridXY(this.s_x >> i, this.s_y >> i);
            changeRatio();
        }
        return z5;
    }

    protected void setGridXY(int i, int i2) {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig != null) {
            mapConfig.setGridXY(this.grid_x, this.grid_y);
        }
        this.grid_x = i;
        this.grid_y = i2;
    }

    protected int getGrid_X() {
        return this.grid_x;
    }

    protected int getGrid_Y() {
        return this.grid_y;
    }

    public double getChangeRatio() {
        return this.changeRatio;
    }

    public double getChangeGridRatio() {
        return this.changeGridRatio;
    }

    private void changeRatio() {
        int s_x = this.last_mapconfig.getS_x();
        int s_y = this.last_mapconfig.getS_y();
        float s_z = this.last_mapconfig.getS_z();
        float s_c = this.last_mapconfig.getS_c();
        float s_r = this.last_mapconfig.getS_r();
        this.changeRatio = Math.abs(this.s_x - s_x) + Math.abs(this.s_y - s_y);
        this.changeRatio = this.changeRatio == 0.0d ? 1.0d : this.changeRatio * 2.0d;
        double d = this.changeRatio;
        float f = this.s_z;
        this.changeRatio = d * (s_z == f ? 1.0d : Math.abs(s_z - f));
        float f2 = this.s_c;
        float fAbs = s_c == f2 ? 1.0f : Math.abs(s_c - f2);
        float f3 = this.s_r;
        float fAbs2 = s_r != f3 ? Math.abs(s_r - f3) : 1.0f;
        double d2 = fAbs;
        this.changeRatio *= d2;
        double d3 = fAbs2;
        this.changeRatio *= d3;
        this.changeGridRatio = Math.abs(this.last_mapconfig.getGrid_X() - this.grid_x) + (this.last_mapconfig.getGrid_Y() - this.grid_y);
        this.changeGridRatio = this.changeGridRatio != 0.0d ? this.changeGridRatio * 2.0d : 1.0d;
        this.changeGridRatio *= d2;
        this.changeGridRatio *= d3;
    }

    public String toString() {
        return " s_x: " + this.s_x + " s_y: " + this.s_y + " s_z: " + this.s_z + " s_c: " + this.s_c + " s_r: " + this.s_r;
    }

    public boolean isZoomChanged() {
        return this.isZoomChanged;
    }

    public boolean isTiltChanged() {
        return this.isTiltChanged;
    }

    public boolean isBearingChanged() {
        return this.isBearingChanged;
    }

    public boolean isIndoorEnable() {
        return this.isIndoorEnable;
    }

    public void setIndoorEnable(boolean z) {
        this.isIndoorEnable = z;
    }

    public boolean isBuildingEnable() {
        return this.isBuildingEnable;
    }

    public void setBuildingEnable(boolean z) {
        this.isBuildingEnable = z;
    }

    public boolean isMapTextEnable() {
        return this.isMapTextEnable;
    }

    public void setMapTextEnable(boolean z) {
        this.isMapTextEnable = z;
    }

    public boolean isTrafficEnabled() {
        return this.isTrafficEnabled;
    }

    public void setTrafficEnabled(boolean z) {
        this.isTrafficEnabled = z;
    }

    public boolean isNeedUpdateZoomControllerState() {
        return this.isNeedUpdateZoomControllerState;
    }

    public int getS_x() {
        return this.s_x;
    }

    public void setS_x(int i) {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig != null) {
            mapConfig.setS_x(this.s_x);
        }
        this.s_x = i;
    }

    public int getS_y() {
        return this.s_y;
    }

    public void setS_y(int i) {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig != null) {
            mapConfig.setS_y(this.s_y);
        }
        this.s_y = i;
    }

    public float getS_z() {
        return this.s_z;
    }

    public void setS_z(float f) {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig != null) {
            mapConfig.setS_z(this.s_z);
        }
        this.s_z = f;
    }

    public float getS_c() {
        return this.s_c;
    }

    public void setS_c(float f) {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig != null) {
            mapConfig.setS_c(this.s_c);
        }
        this.s_c = f;
    }

    public float getS_r() {
        return this.s_r;
    }

    public void setS_r(float f) {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig != null) {
            mapConfig.setS_r(this.s_r);
        }
        this.s_r = f;
    }

    public FPoint[] getMapRect() {
        return this.mapRect;
    }

    public void setMapRect(FPoint[] fPointArr) {
        MapConfig mapConfig = this.last_mapconfig;
        if (mapConfig != null) {
            mapConfig.setMapRect(fPointArr);
        }
        this.mapRect = fPointArr;
    }

    public Rectangle getGeoRectangle() {
        return this.geoRectangle;
    }

    public void setMaxZoomLevel(float f) {
        if (f > 19.0f) {
            f = 19.0f;
        }
        if (f < 3.0f) {
            f = 3.0f;
        }
        if (f < getMinZoomLevel()) {
            f = getMinZoomLevel();
        }
        this.isSetLimitZoomLevel = true;
        this.maxZoomLevel = f;
    }

    public void setMinZoomLevel(float f) {
        if (f < 3.0f) {
            f = 3.0f;
        }
        if (f > 19.0f) {
            f = 19.0f;
        }
        if (f > getMaxZoomLevel()) {
            f = getMaxZoomLevel();
        }
        this.isSetLimitZoomLevel = true;
        this.minZoomLevel = f;
    }

    public float getMaxZoomLevel() {
        return this.maxZoomLevel;
    }

    public float getMinZoomLevel() {
        return this.minZoomLevel;
    }

    public IPoint[] getLimitIPoints() {
        return this.limitIPoints;
    }

    public void setLimitIPoints(IPoint[] iPointArr) {
        this.limitIPoints = iPointArr;
    }

    public boolean isSetLimitZoomLevel() {
        return this.isSetLimitZoomLevel;
    }

    public LatLngBounds getLimitLatLngBounds() {
        return this.limitLatLngBounds;
    }

    public void setLimitLatLngBounds(LatLngBounds latLngBounds) {
        this.limitLatLngBounds = latLngBounds;
        if (latLngBounds == null) {
            resetMinMaxZoomPreference();
        }
    }

    public void resetMinMaxZoomPreference() {
        this.minZoomLevel = 3.0f;
        this.maxZoomLevel = 19.0f;
        this.isSetLimitZoomLevel = false;
    }

    public void updateMapRectNextFrame(boolean z) {
        this.isNeedUpdateMapRectNextFrame = z;
    }

    public void setMapPerPixelUnitLength(float f) {
        this.mapPerPixelUnitLength = f;
    }

    public float getMapPerPixelUnitLength() {
        return this.mapPerPixelUnitLength;
    }

    public void setCustomStylePath(String str) {
        this.mCustomStylePath = str;
    }

    public String getCustomStylePath() {
        return this.mCustomStylePath;
    }

    public void setCustomStyleEnable(boolean z) {
        this.isCustomStyleEnabled = z;
    }

    public boolean isCustomStyleEnable() {
        return this.isCustomStyleEnabled;
    }

    public int getMapStyleTime() {
        return this.mMapStyleTime;
    }

    public void setMapStyleTime(int i) {
        this.mMapStyleTime = i;
    }

    public int getMapStyleMode() {
        return this.mMapStyleMode;
    }

    public void setMapStyleMode(int i) {
        this.mMapStyleMode = i;
    }

    public int getMapStyleState() {
        return this.mMapStyleState;
    }

    public void setMapStyleState(int i) {
        this.mMapStyleState = i;
    }

    public void setCustomTextureResourcePath(String str) {
        this.customTextureResourcePath = str;
    }

    public String getCustomTextureResourcePath() {
        return this.customTextureResourcePath;
    }

    public void setCustomBackgroundColor(int i) {
        this.customBackgroundColor = i;
    }

    public int getCustomBackgroundColor() {
        return this.customBackgroundColor;
    }

    public void setMapZoomScale(float f) {
        this.mapZoomScale = f;
    }

    public float getMapZoomScale() {
        return this.mapZoomScale;
    }

    public void setMapWidth(int i) {
        this.mapWidth = i;
    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public void setMapHeight(int i) {
        this.mapHeight = i;
    }

    public int getMapHeight() {
        return this.mapHeight;
    }
}
