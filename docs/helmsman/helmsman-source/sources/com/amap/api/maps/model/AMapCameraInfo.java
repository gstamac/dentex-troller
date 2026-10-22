package com.amap.api.maps.model;

/* JADX INFO: loaded from: classes.dex */
public class AMapCameraInfo {
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;

    public AMapCameraInfo(float f, float f2, float f3, float f4, float f5, float f6) {
        this.a = f;
        this.b = f2;
        this.c = f3;
        this.d = f4;
        this.e = f5;
        this.f = f6;
    }

    public float getFov() {
        return this.a;
    }

    public float getAspectRatio() {
        return this.b;
    }

    public float getRotate() {
        return this.c;
    }

    public float getX() {
        return this.d;
    }

    public float getY() {
        return this.e;
    }

    public float getZ() {
        return this.f;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[fov:");
        sb.append(this.a).append(" aspectRatio:");
        sb.append(this.b).append(" rotate:");
        sb.append(this.c).append(" pos_x:");
        sb.append(this.d).append(" pos_y:");
        sb.append(this.e).append(" pos_z:");
        sb.append(this.f).append("]");
        return sb.toString();
    }
}
