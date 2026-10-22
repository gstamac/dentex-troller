package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import java.io.File;

/* JADX INFO: compiled from: CityObject.java */
/* JADX INFO: loaded from: classes.dex */
public class al extends OfflineMapCity implements au, bk {
    public static final Parcelable.Creator<al> o = new Parcelable.Creator<al>() { // from class: com.amap.api.mapcore.util.al.2
        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public al createFromParcel(Parcel parcel) {
            return new al(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public al[] newArray(int i) {
            return new al[i];
        }
    };
    public bp a;
    public bp b;
    public bp c;
    public bp d;
    public bp e;
    public bp f;
    public bp g;
    public bp h;
    public bp i;
    public bp j;
    public bp k;
    bp l;
    Context m;
    boolean n;
    private String p;
    private String q;
    private long r;

    @Override // com.amap.api.maps.offlinemap.OfflineMapCity, com.amap.api.maps.offlinemap.City, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void a(String str) {
        this.q = str;
    }

    public String a() {
        return this.q;
    }

    @Override // com.amap.api.mapcore.util.au
    public String b() {
        return getUrl();
    }

    public al(Context context, OfflineMapCity offlineMapCity) {
        this(context, offlineMapCity.getState());
        setCity(offlineMapCity.getCity());
        setUrl(offlineMapCity.getUrl());
        setState(offlineMapCity.getState());
        setCompleteCode(offlineMapCity.getcompleteCode());
        setAdcode(offlineMapCity.getAdcode());
        setVersion(offlineMapCity.getVersion());
        setSize(offlineMapCity.getSize());
        setCode(offlineMapCity.getCode());
        setJianpin(offlineMapCity.getJianpin());
        setPinyin(offlineMapCity.getPinyin());
        t();
    }

    public al(Context context, int i) {
        this.a = new br(6, this);
        this.b = new bx(2, this);
        this.c = new bt(0, this);
        this.d = new bv(3, this);
        this.e = new bw(1, this);
        this.f = new bq(4, this);
        this.g = new bu(7, this);
        this.h = new bs(-1, this);
        this.i = new bs(101, this);
        this.j = new bs(102, this);
        this.k = new bs(103, this);
        this.p = null;
        this.q = "";
        this.n = false;
        this.r = 0L;
        this.m = context;
        a(i);
    }

    public void a(int i) {
        if (i != -1) {
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i != 4) {
                                if (i != 6) {
                                    if (i == 7) {
                                        this.l = this.g;
                                    } else {
                                        switch (i) {
                                            case 101:
                                                this.l = this.i;
                                                break;
                                            case 102:
                                                this.l = this.j;
                                                break;
                                            case 103:
                                                this.l = this.k;
                                                break;
                                            default:
                                                if (i < 0) {
                                                    this.l = this.h;
                                                }
                                                break;
                                        }
                                    }
                                } else {
                                    this.l = this.a;
                                }
                            } else {
                                this.l = this.f;
                            }
                        } else {
                            this.l = this.d;
                        }
                    } else {
                        this.l = this.b;
                    }
                } else {
                    this.l = this.e;
                }
            } else {
                this.l = this.c;
            }
        } else {
            this.l = this.h;
        }
        setState(i);
    }

    public void a(bp bpVar) {
        this.l = bpVar;
        setState(bpVar.b());
    }

    public bp c() {
        return this.l;
    }

    public void d() {
        am amVarA = am.a(this.m);
        if (amVarA != null) {
            amVarA.c(this);
        }
    }

    public void e() {
        am amVarA = am.a(this.m);
        if (amVarA != null) {
            amVarA.e(this);
            d();
        }
    }

    public void f() {
        bj.a("CityOperation current State==>" + c().b());
        if (this.l.equals(this.d)) {
            this.l.e();
            return;
        }
        if (this.l.equals(this.c)) {
            this.l.f();
            return;
        }
        if (this.l.equals(this.g) || this.l.equals(this.h)) {
            k();
            this.n = true;
        } else if (this.l.equals(this.j) || this.l.equals(this.i) || this.l.a(this.k)) {
            this.l.d();
        } else {
            c().c();
        }
    }

    public void g() {
        this.l.f();
    }

    public void h() {
        this.l.a(this.k.b());
    }

    public void i() {
        this.l.a();
        if (this.n) {
            this.l.c();
        }
        this.n = false;
    }

    public void j() {
        this.l.equals(this.f);
        this.l.g();
    }

    public void k() {
        am amVarA = am.a(this.m);
        if (amVarA != null) {
            amVarA.a(this);
        }
    }

    public void l() {
        am amVarA = am.a(this.m);
        if (amVarA != null) {
            amVarA.b(this);
        }
    }

    public void m() {
        am amVarA = am.a(this.m);
        if (amVarA != null) {
            amVarA.d(this);
        }
    }

    @Override // com.amap.api.mapcore.util.bl
    public void n() {
        this.r = 0L;
        if (!this.l.equals(this.b)) {
            bj.a("state must be waiting when download onStart");
        }
        this.l.d();
    }

    @Override // com.amap.api.mapcore.util.bl
    public void a(long j, long j2) {
        int i = (int) ((j2 * 100) / j);
        if (i != getcompleteCode()) {
            setCompleteCode(i);
            d();
        }
    }

    @Override // com.amap.api.mapcore.util.bl
    public void o() {
        if (!this.l.equals(this.c)) {
            bj.a("state must be Loading when download onFinish");
        }
        this.l.h();
    }

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.al$3, reason: invalid class name */
    /* JADX INFO: compiled from: CityObject.java */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[bl.a.values().length];
            a = iArr;
            try {
                iArr[bl.a.amap_exception.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[bl.a.file_io_exception.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[bl.a.network_exception.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.amap.api.mapcore.util.bl
    public void a(bl.a aVar) {
        int iB;
        int i = AnonymousClass3.a[aVar.ordinal()];
        if (i == 1) {
            iB = this.j.b();
        } else if (i == 2) {
            iB = this.k.b();
        } else {
            iB = i != 3 ? 6 : this.i.b();
        }
        if (this.l.equals(this.c) || this.l.equals(this.b)) {
            this.l.a(iB);
        }
    }

    @Override // com.amap.api.mapcore.util.bl
    public void p() {
        e();
    }

    @Override // com.amap.api.mapcore.util.be
    public void q() {
        this.r = 0L;
        setCompleteCode(0);
        this.l.equals(this.e);
        this.l.d();
    }

    @Override // com.amap.api.mapcore.util.be
    public void r() {
        this.l.equals(this.e);
        this.l.a(this.h.b());
    }

    @Override // com.amap.api.mapcore.util.be
    public void a(long j) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.r > 500) {
            int i = (int) j;
            if (i > getcompleteCode()) {
                setCompleteCode(i);
                d();
            }
            this.r = jCurrentTimeMillis;
        }
    }

    @Override // com.amap.api.mapcore.util.be
    public void b(String str) {
        this.l.equals(this.e);
        this.q = str;
        String strU = u();
        String strV = v();
        if (TextUtils.isEmpty(strU) || TextUtils.isEmpty(strV)) {
            r();
            return;
        }
        File file = new File(strV + "/");
        File file2 = new File(dw.a(this.m) + File.separator + "map/");
        File file3 = new File(dw.a(this.m));
        if (!file3.exists()) {
            file3.mkdir();
        }
        if (!file2.exists()) {
            file2.mkdir();
        }
        a(file, file2, strU);
    }

    @Override // com.amap.api.mapcore.util.be
    public void s() {
        e();
    }

    protected void t() {
        this.p = am.a + getPinyin() + ".zip.tmp";
    }

    public String u() {
        if (TextUtils.isEmpty(this.p)) {
            return null;
        }
        String str = this.p;
        return str.substring(0, str.lastIndexOf("."));
    }

    public String v() {
        if (TextUtils.isEmpty(this.p)) {
            return null;
        }
        String strU = u();
        return strU.substring(0, strU.lastIndexOf(46));
    }

    private void a(final File file, File file2, final String str) {
        new bd().a(file, file2, -1L, bj.a(file), new bd.a() { // from class: com.amap.api.mapcore.util.al.1
            @Override // com.amap.api.mapcore.util.bd.a
            public void a(String str2, String str3) {
            }

            @Override // com.amap.api.mapcore.util.bd.a
            public void a(String str2, String str3, float f) {
                int i = (int) ((((double) f) * 0.39d) + 60.0d);
                if (i - al.this.getcompleteCode() <= 0 || System.currentTimeMillis() - al.this.r <= 1000) {
                    return;
                }
                al.this.setCompleteCode(i);
                al.this.r = System.currentTimeMillis();
            }

            @Override // com.amap.api.mapcore.util.bd.a
            public void b(String str2, String str3) {
                try {
                    new File(str).delete();
                    bj.b(file);
                    al.this.setCompleteCode(100);
                    al.this.l.h();
                } catch (Exception unused) {
                    al.this.l.a(al.this.k.b());
                }
            }

            @Override // com.amap.api.mapcore.util.bd.a
            public void a(String str2, String str3, int i) {
                al.this.l.a(al.this.k.b());
            }
        });
    }

    public boolean w() {
        bj.a();
        getSize();
        getcompleteCode();
        getSize();
        return false;
    }

    public aw x() {
        setState(this.l.b());
        aw awVar = new aw(this, this.m);
        awVar.a(a());
        bj.a("vMapFileNames: " + a());
        return awVar;
    }

    @Override // com.amap.api.maps.offlinemap.OfflineMapCity, com.amap.api.maps.offlinemap.City, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.q);
    }

    public al(Parcel parcel) {
        super(parcel);
        this.a = new br(6, this);
        this.b = new bx(2, this);
        this.c = new bt(0, this);
        this.d = new bv(3, this);
        this.e = new bw(1, this);
        this.f = new bq(4, this);
        this.g = new bu(7, this);
        this.h = new bs(-1, this);
        this.i = new bs(101, this);
        this.j = new bs(102, this);
        this.k = new bs(103, this);
        this.p = null;
        this.q = "";
        this.n = false;
        this.r = 0L;
        this.q = parcel.readString();
    }

    @Override // com.amap.api.mapcore.util.bk
    public boolean y() {
        return w();
    }

    @Override // com.amap.api.mapcore.util.bk
    public String z() {
        StringBuffer stringBuffer = new StringBuffer(getPinyin());
        stringBuffer.append(".zip");
        return stringBuffer.toString();
    }

    @Override // com.amap.api.mapcore.util.bk
    public String A() {
        return getAdcode();
    }

    @Override // com.amap.api.mapcore.util.bf
    public String B() {
        return u();
    }

    @Override // com.amap.api.mapcore.util.bf
    public String C() {
        return v();
    }

    public bp b(int i) {
        switch (i) {
            case 101:
                return this.i;
            case 102:
                return this.j;
            case 103:
                return this.k;
            default:
                return this.h;
        }
    }
}
