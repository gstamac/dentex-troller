package com.autonavi.ae.gmap.gloverlay;

import android.content.Context;
import android.graphics.Bitmap;
import com.autonavi.ae.gmap.gloverlay.GLOverlay;
import com.autonavi.amap.mapcore.interfaces.IAMap;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseMapOverlay<T extends GLOverlay, E> implements Serializable {
    private static final long serialVersionUID = 1;
    protected Context mContext;
    protected int mEngineID;
    protected T mGLOverlay;
    protected Vector<E> mItemList;
    protected int mLastFocusedIndex;
    protected IAMap mMapView;

    public abstract void addItem(E e);

    protected abstract void iniGLOverlay();

    public abstract void resumeMarker(Bitmap bitmap);

    public BaseMapOverlay(int i, Context context, IAMap iAMap) {
        this.mItemList = null;
        this.mEngineID = i;
        this.mContext = context;
        this.mMapView = iAMap;
        this.mItemList = new Vector<>();
        iniGLOverlay();
    }

    public T getGLOverlay() {
        return this.mGLOverlay;
    }

    public void setVisible(boolean z) {
        T t = this.mGLOverlay;
        if (t != null) {
            t.setVisible(z);
        }
    }

    public boolean isVisible() {
        T t = this.mGLOverlay;
        if (t != null) {
            return t.isVisible();
        }
        return false;
    }

    public void setClickable(boolean z) {
        T t = this.mGLOverlay;
        if (t != null) {
            t.setClickable(z);
        }
    }

    public boolean isClickable() {
        T t = this.mGLOverlay;
        if (t != null) {
            return t.isClickable();
        }
        return false;
    }

    public int getSize() {
        return this.mItemList.size();
    }

    public boolean clear() {
        try {
            this.mItemList.clear();
            clearFocus();
            T t = this.mGLOverlay;
            if (t == null) {
                return true;
            }
            t.removeAll();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean removeAll() {
        return clear();
    }

    public void clearFocus() {
        this.mLastFocusedIndex = -1;
        this.mGLOverlay.clearFocus();
    }

    public final E getItem(int i) {
        try {
            synchronized (this.mItemList) {
                if (i >= 0) {
                    if (i <= this.mItemList.size() - 1) {
                        return this.mItemList.get(i);
                    }
                }
                return null;
            }
        } catch (IndexOutOfBoundsException unused) {
            return null;
        }
    }

    public void removeItem(int i) {
        if (i >= 0) {
            try {
                if (i > this.mItemList.size() - 1) {
                    return;
                }
                if (i == this.mLastFocusedIndex) {
                    this.mLastFocusedIndex = -1;
                    clearFocus();
                }
                this.mItemList.remove(i);
                T t = this.mGLOverlay;
                if (t != null) {
                    t.removeItem(i);
                }
            } catch (IndexOutOfBoundsException unused) {
            }
        }
    }

    public void removeItem(E e) {
        if (e == null) {
            return;
        }
        try {
            synchronized (this.mItemList) {
                try {
                    removeItem(this.mItemList.indexOf(e));
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (IndexOutOfBoundsException unused) {
        }
    }

    public List<E> getItems() {
        return this.mItemList;
    }

    protected void finalize() throws Throwable {
        releaseInstance();
        super.finalize();
    }

    public void releaseInstance() {
        if (getGLOverlay() != null) {
            IAMap iAMap = this.mMapView;
            if (iAMap != null && iAMap.isMaploaded()) {
                this.mMapView.removeGLOverlay(this);
            }
            getGLOverlay().releaseInstance();
        }
    }
}
