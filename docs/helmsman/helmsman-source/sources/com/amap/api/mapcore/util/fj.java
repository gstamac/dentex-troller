package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: HashCodeBuilder.java */
/* JADX INFO: loaded from: classes.dex */
public class fj {
    private final int a = 37;
    private int b = 17;

    public fj a(boolean z) {
        this.b = (this.b * this.a) + (!z ? 1 : 0);
        return this;
    }

    public fj a(boolean[] zArr) {
        if (zArr == null) {
            this.b *= this.a;
        } else {
            for (boolean z : zArr) {
                a(z);
            }
        }
        return this;
    }

    public fj a(byte b) {
        this.b = (this.b * this.a) + b;
        return this;
    }

    public fj a(byte[] bArr) {
        if (bArr == null) {
            this.b *= this.a;
        } else {
            for (byte b : bArr) {
                a(b);
            }
        }
        return this;
    }

    public fj a(char c) {
        this.b = (this.b * this.a) + c;
        return this;
    }

    public fj a(char[] cArr) {
        if (cArr == null) {
            this.b *= this.a;
        } else {
            for (char c : cArr) {
                a(c);
            }
        }
        return this;
    }

    public fj a(double d) {
        return a(Double.doubleToLongBits(d));
    }

    public fj a(double[] dArr) {
        if (dArr == null) {
            this.b *= this.a;
        } else {
            for (double d : dArr) {
                a(d);
            }
        }
        return this;
    }

    public fj a(float f) {
        this.b = (this.b * this.a) + Float.floatToIntBits(f);
        return this;
    }

    public fj a(float[] fArr) {
        if (fArr == null) {
            this.b *= this.a;
        } else {
            for (float f : fArr) {
                a(f);
            }
        }
        return this;
    }

    public fj a(int i) {
        this.b = (this.b * this.a) + i;
        return this;
    }

    public fj a(int[] iArr) {
        if (iArr == null) {
            this.b *= this.a;
        } else {
            for (int i : iArr) {
                a(i);
            }
        }
        return this;
    }

    public fj a(long j) {
        this.b = (this.b * this.a) + ((int) (j ^ (j >> 32)));
        return this;
    }

    public fj a(long[] jArr) {
        if (jArr == null) {
            this.b *= this.a;
        } else {
            for (long j : jArr) {
                a(j);
            }
        }
        return this;
    }

    public fj a(Object obj) {
        if (obj == null) {
            this.b *= this.a;
        } else if (obj.getClass().isArray()) {
            if (obj instanceof long[]) {
                a((long[]) obj);
            } else if (obj instanceof int[]) {
                a((int[]) obj);
            } else if (obj instanceof short[]) {
                a((short[]) obj);
            } else if (obj instanceof char[]) {
                a((char[]) obj);
            } else if (obj instanceof byte[]) {
                a((byte[]) obj);
            } else if (obj instanceof double[]) {
                a((double[]) obj);
            } else if (obj instanceof float[]) {
                a((float[]) obj);
            } else if (obj instanceof boolean[]) {
                a((boolean[]) obj);
            } else {
                a((Object[]) obj);
            }
        } else {
            this.b = (this.b * this.a) + obj.hashCode();
        }
        return this;
    }

    public fj a(Object[] objArr) {
        if (objArr == null) {
            this.b *= this.a;
        } else {
            for (Object obj : objArr) {
                a(obj);
            }
        }
        return this;
    }

    public fj a(short s) {
        this.b = (this.b * this.a) + s;
        return this;
    }

    public fj a(short[] sArr) {
        if (sArr == null) {
            this.b *= this.a;
        } else {
            for (short s : sArr) {
                a(s);
            }
        }
        return this;
    }

    public int a() {
        return this.b;
    }

    public int hashCode() {
        return a();
    }
}
