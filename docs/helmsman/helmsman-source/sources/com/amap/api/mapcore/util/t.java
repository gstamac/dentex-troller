package com.amap.api.mapcore.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import com.amap.api.maps.model.Marker;

/* JADX INFO: compiled from: SensorEventHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class t implements SensorEventListener {
    private SensorManager a;
    private Sensor b;
    private float e;
    private Context f;
    private k g;
    private Marker h;
    private long c = 0;
    private final int d = 100;
    private boolean i = true;

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public t(Context context, k kVar) {
        this.f = context.getApplicationContext();
        this.g = kVar;
        try {
            SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
            this.a = sensorManager;
            this.b = sensorManager.getDefaultSensor(3);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void a() {
        Sensor sensor;
        SensorManager sensorManager = this.a;
        if (sensorManager == null || (sensor = this.b) == null) {
            return;
        }
        sensorManager.registerListener(this, sensor, 3);
    }

    public void b() {
        Sensor sensor;
        SensorManager sensorManager = this.a;
        if (sensorManager == null || (sensor = this.b) == null) {
            return;
        }
        sensorManager.unregisterListener(this, sensor);
    }

    public void a(Marker marker) {
        this.h = marker;
    }

    public void a(boolean z) {
        this.i = z;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        try {
            if (System.currentTimeMillis() - this.c < 100) {
                return;
            }
            if ((this.g.a() == null || this.g.a().getAnimateionsCount() <= 0) && sensorEvent.sensor.getType() == 3) {
                float fA = (sensorEvent.values[0] + a(this.f)) % 360.0f;
                if (fA > 180.0f) {
                    fA -= 360.0f;
                } else if (fA < -180.0f) {
                    fA += 360.0f;
                }
                if (Math.abs(this.e - fA) < 3.0f) {
                    return;
                }
                if (Float.isNaN(fA)) {
                    fA = 0.0f;
                }
                this.e = fA;
                Marker marker = this.h;
                if (marker != null) {
                    try {
                        if (this.i) {
                            this.g.a(z.d(fA));
                            this.h.setRotateAngle(-this.e);
                        } else {
                            marker.setRotateAngle(360.0f - fA);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                this.c = System.currentTimeMillis();
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    public static int a(Context context) {
        int rotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
        if (rotation == 1) {
            return 90;
        }
        if (rotation != 2) {
            return rotation != 3 ? 0 : -90;
        }
        return 180;
    }
}
