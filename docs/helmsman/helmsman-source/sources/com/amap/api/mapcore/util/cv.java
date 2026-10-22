package com.amap.api.mapcore.util;

import android.opengl.GLES20;

/* JADX INFO: compiled from: GLShader.java */
/* JADX INFO: loaded from: classes.dex */
public class cv {
    private boolean a;
    public int d;
    public int e;
    public int f;

    protected boolean a(String str, String str2) {
        int iB = b(str, str2);
        this.d = iB;
        return iB != 0;
    }

    protected boolean a(String str) {
        int iD = d(str);
        this.d = iD;
        return iD != 0;
    }

    protected int b(String str) {
        return GLES20.glGetAttribLocation(this.d, str);
    }

    protected int c(String str) {
        return GLES20.glGetUniformLocation(this.d, str);
    }

    public void a() {
        GLES20.glUseProgram(this.d);
    }

    public int d(String str) {
        String str2 = "amap_sdk_shaders/" + str;
        String strA = dc.a(str2);
        if (strA == null) {
            throw new IllegalArgumentException("shader file not found: " + str2);
        }
        int iIndexOf = strA.indexOf(36);
        if (iIndexOf < 0 || strA.charAt(iIndexOf + 1) != '$') {
            throw new IllegalArgumentException("not a shader file " + str2);
        }
        return b(strA.substring(0, iIndexOf), strA.substring(iIndexOf + 2));
    }

    public int b(String str, String str2) {
        this.e = a(35633, str);
        this.f = a(35632, str2);
        int iGlCreateProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(iGlCreateProgram, this.e);
        GLES20.glAttachShader(iGlCreateProgram, this.f);
        GLES20.glLinkProgram(iGlCreateProgram);
        return iGlCreateProgram;
    }

    public int a(int i, String str) {
        int iGlCreateShader = GLES20.glCreateShader(i);
        GLES20.glShaderSource(iGlCreateShader, str);
        GLES20.glCompileShader(iGlCreateShader);
        return iGlCreateShader;
    }

    public void b() {
        int i = this.d;
        if (i >= 0) {
            GLES20.glDeleteProgram(i);
        }
        int i2 = this.e;
        if (i2 >= 0) {
            GLES20.glDeleteShader(i2);
        }
        int i3 = this.f;
        if (i3 >= 0) {
            GLES20.glDeleteShader(i3);
        }
        this.a = true;
    }

    public boolean c() {
        return this.a;
    }
}
