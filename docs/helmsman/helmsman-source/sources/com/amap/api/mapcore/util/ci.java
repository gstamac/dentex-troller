package com.amap.api.mapcore.util;

import android.graphics.Rect;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.interfaces.IOverlayImage;

/* JADX INFO: compiled from: IOverlayImageDelegate.java */
/* JADX INFO: loaded from: classes.dex */
public interface ci extends IOverlayImage {
    void a(k kVar);

    void a(k kVar, float[] fArr, int i, float f);

    void b(boolean z);

    IMarkerAction getIMarkerAction();

    Rect h();

    boolean i();

    boolean isInfoWindowShown();

    boolean j();

    int k();

    boolean l();
}
