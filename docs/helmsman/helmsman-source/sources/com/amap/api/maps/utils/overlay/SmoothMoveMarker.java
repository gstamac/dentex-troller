package com.amap.api.maps.utils.overlay;

import android.view.animation.LinearInterpolator;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class SmoothMoveMarker {
    private TranslateAnimation animation;
    private BitmapDescriptor descriptor;
    private LatLng endPoint;
    private LatLng lastEndPoint;
    private AMap mAMap;
    private MoveListener moveListener;
    private LatLng tempPosition;
    private long duration = OkHttpUtils.DEFAULT_MILLISECONDS;
    private LinkedList<LatLng> points = new LinkedList<>();
    private LinkedList<Double> eachDistance = new LinkedList<>();
    private double totalDistance = 0.0d;
    private double remainDistance = 0.0d;
    private Marker marker = null;
    private int index = 0;
    private boolean useDefaultDescriptor = false;
    private Thread moveThread = null;
    private Timer timer = null;
    boolean exitFlag = false;

    public interface MoveListener {
        void move(double d);
    }

    static /* synthetic */ int access$908(SmoothMoveMarker smoothMoveMarker) {
        int i = smoothMoveMarker.index;
        smoothMoveMarker.index = i + 1;
        return i;
    }

    public SmoothMoveMarker(AMap aMap) {
        this.mAMap = aMap;
    }

    public void setPoints(List<LatLng> list) {
        synchronized (this) {
            try {
                this.points.clear();
                Iterator<LatLng> it = list.iterator();
                while (it.hasNext()) {
                    this.points.add(it.next());
                }
                if (list.size() > 1) {
                    LinkedList<LatLng> linkedList = this.points;
                    this.endPoint = linkedList.get(linkedList.size() - 1);
                    LinkedList<LatLng> linkedList2 = this.points;
                    this.lastEndPoint = linkedList2.get(linkedList2.size() - 2);
                }
                this.eachDistance.clear();
                this.totalDistance = 0.0d;
                int i = 0;
                while (i < this.points.size() - 1) {
                    LatLng latLng = this.points.get(i);
                    i++;
                    double dCalculateLineDistance = AMapUtils.calculateLineDistance(latLng, this.points.get(i));
                    this.eachDistance.add(Double.valueOf(dCalculateLineDistance));
                    this.totalDistance += dCalculateLineDistance;
                }
                this.remainDistance = this.totalDistance;
                LatLng latLngRemoveFirst = this.points.removeFirst();
                Marker marker = this.marker;
                if (marker != null) {
                    marker.setPosition(latLngRemoveFirst);
                    checkMarkerIcon();
                } else {
                    if (this.descriptor == null) {
                        this.useDefaultDescriptor = true;
                    }
                    this.marker = this.mAMap.addMarker(new MarkerOptions().belowMaskLayer(true).position(latLngRemoveFirst).icon(this.descriptor).title("").anchor(0.5f, 0.5f));
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private void checkMarkerIcon() {
        if (this.useDefaultDescriptor) {
            BitmapDescriptor bitmapDescriptor = this.descriptor;
            if (bitmapDescriptor == null) {
                this.useDefaultDescriptor = true;
            } else {
                this.marker.setIcon(bitmapDescriptor);
                this.useDefaultDescriptor = false;
            }
        }
    }

    public void setTotalDuration(int i) {
        this.duration = i * 1000;
    }

    public void startSmoothMove() {
        if (this.points.size() < 1) {
            return;
        }
        this.index = 0;
        this.exitFlag = false;
        try {
            Thread thread = this.moveThread;
            if (thread != null) {
                thread.interrupt();
            }
            Thread thread2 = new Thread(new Runnable() { // from class: com.amap.api.maps.utils.overlay.SmoothMoveMarker.1
                @Override // java.lang.Runnable
                public void run() {
                    a();
                }

                /* JADX INFO: Access modifiers changed from: private */
                public void a() {
                    try {
                        if (SmoothMoveMarker.this.points.size() < 1) {
                            if (SmoothMoveMarker.this.moveListener != null) {
                                SmoothMoveMarker.this.moveListener.move(0.0d);
                            }
                            SmoothMoveMarker.this.setEndRotate();
                            return;
                        }
                        double dDoubleValue = ((Double) SmoothMoveMarker.this.eachDistance.poll()).doubleValue();
                        long j = (long) (SmoothMoveMarker.this.duration * (dDoubleValue / SmoothMoveMarker.this.totalDistance));
                        SmoothMoveMarker.this.remainDistance -= dDoubleValue;
                        if (SmoothMoveMarker.this.moveListener != null) {
                            if (SmoothMoveMarker.this.remainDistance < 0.0d) {
                                SmoothMoveMarker.this.remainDistance = 0.0d;
                            }
                            SmoothMoveMarker.this.moveListener.move(SmoothMoveMarker.this.remainDistance);
                        }
                        LatLng position = SmoothMoveMarker.this.marker.getPosition();
                        LatLng latLng = (LatLng) SmoothMoveMarker.this.points.poll();
                        if (SmoothMoveMarker.this.timer != null) {
                            SmoothMoveMarker.this.timer.cancel();
                        }
                        SmoothMoveMarker.this.timer = new Timer();
                        SmoothMoveMarker.this.timer.schedule(new TimerTask() { // from class: com.amap.api.maps.utils.overlay.SmoothMoveMarker.1.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                try {
                                    if (SmoothMoveMarker.this.points.size() > 0) {
                                        SmoothMoveMarker.access$908(SmoothMoveMarker.this);
                                        a();
                                    } else {
                                        SmoothMoveMarker.access$908(SmoothMoveMarker.this);
                                        if (SmoothMoveMarker.this.moveListener != null) {
                                            SmoothMoveMarker.this.moveListener.move(0.0d);
                                        }
                                        SmoothMoveMarker.this.setEndRotate();
                                    }
                                } catch (Throwable th) {
                                    th.printStackTrace();
                                }
                            }
                        }, j);
                        if (latLng == null) {
                            return;
                        }
                        if (AMapUtils.calculateLineDistance(position, latLng) > 5.0f) {
                            SmoothMoveMarker.this.marker.setRotateAngle((360.0f - SmoothMoveMarker.this.getRotate(position, latLng)) + SmoothMoveMarker.this.mAMap.getCameraPosition().bearing);
                        }
                        SmoothMoveMarker.this.animation = new TranslateAnimation(latLng);
                        SmoothMoveMarker.this.animation.setInterpolator(new LinearInterpolator());
                        SmoothMoveMarker.this.animation.setDuration(j);
                        if (!SmoothMoveMarker.this.exitFlag && !Thread.interrupted()) {
                            SmoothMoveMarker.this.marker.setAnimation(SmoothMoveMarker.this.animation);
                            SmoothMoveMarker.this.marker.startAnimation();
                            return;
                        }
                        SmoothMoveMarker.this.marker.setAnimation(null);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
            this.moveThread = thread2;
            thread2.start();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEndRotate() {
        if (this.marker == null) {
            return;
        }
        LatLng latLng = this.lastEndPoint;
        if (latLng != null && this.endPoint != null && latLng.latitude != this.endPoint.latitude && this.lastEndPoint.longitude != this.endPoint.longitude) {
            this.marker.setRotateAngle((360.0f - getRotate(this.lastEndPoint, this.endPoint)) + this.mAMap.getCameraPosition().bearing);
        }
        this.marker.setPosition(this.endPoint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getRotate(LatLng latLng, LatLng latLng2) {
        if (latLng == null || latLng2 == null) {
            return 0.0f;
        }
        double d = latLng.latitude;
        double d2 = latLng2.latitude;
        return (float) ((Math.atan2(latLng2.longitude - latLng.longitude, d2 - d) / 3.141592653589793d) * 180.0d);
    }

    public void stopMove() {
        this.exitFlag = true;
        Marker marker = this.marker;
        if (marker != null) {
            marker.setAnimation(null);
        }
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
        }
        Thread thread = this.moveThread;
        if (thread != null) {
            thread.interrupt();
        }
        this.index = 0;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public LatLng getPosition() {
        Marker marker = this.marker;
        if (marker == null) {
            return null;
        }
        return marker.getPosition();
    }

    public int getIndex() {
        return this.index;
    }

    public void resetIndex() {
        this.index = 0;
    }

    public void destroy() {
        stopMove();
        BitmapDescriptor bitmapDescriptor = this.descriptor;
        if (bitmapDescriptor != null) {
            bitmapDescriptor.recycle();
        }
        Marker marker = this.marker;
        if (marker != null) {
            marker.destroy();
            this.marker = null;
        }
        this.points.clear();
        this.eachDistance.clear();
    }

    public void removeMarker() {
        Marker marker = this.marker;
        if (marker != null) {
            marker.remove();
            this.marker = null;
        }
        this.points.clear();
        this.eachDistance.clear();
    }

    public void setPosition(LatLng latLng) {
        Marker marker = this.marker;
        if (marker != null) {
            marker.setPosition(latLng);
        }
    }

    public void setDescriptor(BitmapDescriptor bitmapDescriptor) {
        BitmapDescriptor bitmapDescriptor2 = this.descriptor;
        if (bitmapDescriptor2 != null) {
            bitmapDescriptor2.recycle();
        }
        this.descriptor = bitmapDescriptor;
        Marker marker = this.marker;
        if (marker != null) {
            marker.setIcon(bitmapDescriptor);
        }
    }

    public void setRotate(float f) {
        AMap aMap;
        Marker marker = this.marker;
        if (marker == null || (aMap = this.mAMap) == null) {
            return;
        }
        marker.setRotateAngle((360.0f - f) + aMap.getCameraPosition().bearing);
    }

    public void setVisible(boolean z) {
        Marker marker = this.marker;
        if (marker != null) {
            marker.setVisible(z);
        }
    }

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }
}
