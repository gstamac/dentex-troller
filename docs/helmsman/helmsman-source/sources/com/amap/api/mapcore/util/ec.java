package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: RectPacker.java */
/* JADX INFO: loaded from: classes.dex */
public class ec {
    b a;

    /* JADX INFO: compiled from: RectPacker.java */
    enum a {
        FAIL,
        PERFECT,
        FIT
    }

    public ec(int i, int i2) {
        this.a = new b(new c(0, 0, i, i2));
    }

    public c a(int i, int i2, String str) {
        b bVarA = this.a.a(i, i2, str);
        if (bVarA != null) {
            return new c(bVarA.b.a, bVarA.b.b, bVarA.b.c, bVarA.b.d);
        }
        return null;
    }

    public boolean a(String str) {
        return this.a.a(str);
    }

    public int a() {
        return this.a.b.c;
    }

    public int b() {
        return this.a.b.d;
    }

    /* JADX INFO: compiled from: RectPacker.java */
    class b {
        static final /* synthetic */ boolean e = true;
        String a;
        c b;
        b c = null;
        b d = null;

        b(c cVar) {
            this.b = cVar;
        }

        b a(int i, int i2, String str) {
            if (!a()) {
                b bVarA = this.c.a(i, i2, str);
                return bVarA == null ? this.d.a(i, i2, str) : bVarA;
            }
            if (this.a != null) {
                return null;
            }
            int i3 = AnonymousClass1.a[b(i, i2).ordinal()];
            if (i3 == 1) {
                return null;
            }
            if (i3 == 2) {
                this.a = str;
                return this;
            }
            if (i3 == 3) {
                a(i, i2);
            }
            return this.c.a(i, i2, str);
        }

        boolean a() {
            return this.c == null;
        }

        boolean b() {
            return (this.a == null && a()) ? false : true;
        }

        boolean a(String str) {
            if (a()) {
                if (!str.equals(this.a)) {
                    return false;
                }
                this.a = null;
                return true;
            }
            boolean zA = this.c.a(str);
            if (!zA) {
                zA = this.d.a(str);
            }
            if (zA && !this.c.b() && !this.d.b()) {
                this.c = null;
                this.d = null;
            }
            return zA;
        }

        void a(int i, int i2) {
            c cVar;
            c cVar2;
            int i3 = this.b.c - i;
            int i4 = this.b.d - i2;
            boolean z = e;
            if (!z && i3 < 0) {
                throw new AssertionError();
            }
            if (!z && i4 < 0) {
                throw new AssertionError();
            }
            if (i3 > i4) {
                cVar2 = ec.this.new c(this.b.a, this.b.b, i, this.b.d);
                cVar = ec.this.new c(cVar2.a + i, this.b.b, this.b.c - i, this.b.d);
            } else {
                c cVar3 = ec.this.new c(this.b.a, this.b.b, this.b.c, i2);
                cVar = ec.this.new c(this.b.a, cVar3.b + i2, this.b.c, this.b.d - i2);
                cVar2 = cVar3;
            }
            this.c = ec.this.new b(cVar2);
            this.d = ec.this.new b(cVar);
        }

        a b(int i, int i2) {
            if (i <= this.b.c && i2 <= this.b.d) {
                if (i == this.b.c && i2 == this.b.d) {
                    return a.PERFECT;
                }
                return a.FIT;
            }
            return a.FAIL;
        }
    }

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.ec$1, reason: invalid class name */
    /* JADX INFO: compiled from: RectPacker.java */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[a.values().length];
            a = iArr;
            try {
                iArr[a.FAIL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[a.PERFECT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[a.FIT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX INFO: compiled from: RectPacker.java */
    public class c {
        public int a;
        public int b;
        public int c;
        public int d;

        c(int i, int i2, int i3, int i4) {
            this.a = i;
            this.b = i2;
            this.c = i3;
            this.d = i4;
        }

        public String toString() {
            return "[ x: " + this.a + ", y: " + this.b + ", w: " + this.c + ", h: " + this.d + " ]";
        }
    }
}
