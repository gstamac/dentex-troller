package com.autonavi.ae.gmap.listener;

/* JADX INFO: loaded from: classes.dex */
public interface MapWidgetListener {
    void invalidateCompassView();

    void invalidateScaleView();

    void invalidateZoomController(float f);

    void setFrontViewVisibility(boolean z);
}
