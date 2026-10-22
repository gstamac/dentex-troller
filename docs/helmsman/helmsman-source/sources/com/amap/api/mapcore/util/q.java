package com.amap.api.mapcore.util;

import com.autonavi.amap.mapcore.animation.GLAlphaAnimation;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* JADX INFO: compiled from: MaskLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class q {
    public FloatBuffer a;
    public ShortBuffer b;
    short[] d;
    private GLAlphaAnimation i;
    private float e = 0.0f;
    private float f = 0.0f;
    private float g = 0.0f;
    private float h = 0.7f;
    float[] c = {-1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};

    public q() {
        short[] sArr = {0, 1, 3, 0, 3, 2};
        this.d = sArr;
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(sArr.length * 2);
        byteBufferAllocateDirect.order(ByteOrder.nativeOrder());
        ShortBuffer shortBufferAsShortBuffer = byteBufferAllocateDirect.asShortBuffer();
        this.b = shortBufferAsShortBuffer;
        shortBufferAsShortBuffer.put(this.d);
        this.b.position(0);
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(this.c.length * 4);
        byteBufferAllocateDirect2.order(ByteOrder.nativeOrder());
        FloatBuffer floatBufferAsFloatBuffer = byteBufferAllocateDirect2.asFloatBuffer();
        this.a = floatBufferAsFloatBuffer;
        floatBufferAsFloatBuffer.put(this.c);
        this.a.position(0);
    }

    public void a(int i, int i2, int i3, int i4) {
        this.e = i / 255.0f;
        this.f = i2 / 255.0f;
        this.g = i3 / 255.0f;
        this.h = i4 / 255.0f;
    }

    public void a(GLAlphaAnimation gLAlphaAnimation) {
        GLAlphaAnimation gLAlphaAnimation2 = this.i;
        if (gLAlphaAnimation2 != null && !gLAlphaAnimation2.hasEnded()) {
            this.i.cancel();
        }
        if (gLAlphaAnimation == null) {
            return;
        }
        this.i = gLAlphaAnimation;
        gLAlphaAnimation.start();
    }
}
