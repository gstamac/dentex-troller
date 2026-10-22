package com.amap.api.mapcore.util;

import com.amap.api.maps.model.MultiPointItem;
import com.autonavi.amap.mapcore.IPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: QuadTree2.java */
/* JADX INFO: loaded from: classes.dex */
public class ak {
    private final ah a;
    private final int b;
    private int c;
    private List<MultiPointItem> d;
    private List<ak> e;

    private int a(int i) {
        switch (i) {
            case 0:
                return 50;
            case 1:
                return 30;
            case 2:
            case 3:
                return 20;
            case 4:
            case 5:
                return 10;
            case 6:
            default:
                return 5;
        }
    }

    protected ak(ah ahVar) {
        this(ahVar, 0);
    }

    private ak(int i, int i2, int i3, int i4, int i5) {
        this(new ah(i, i2, i3, i4), i5);
    }

    private ak(ah ahVar, int i) {
        this.c = 30;
        this.e = null;
        this.a = ahVar;
        this.b = i;
        this.c = a(i);
    }

    protected void a(MultiPointItem multiPointItem) {
        IPoint iPoint = multiPointItem.getIPoint();
        if (this.a.a(iPoint.x, iPoint.y)) {
            a(iPoint.x, iPoint.y, multiPointItem);
        }
    }

    private void a(int i, int i2, MultiPointItem multiPointItem) {
        if (this.d == null) {
            this.d = new ArrayList();
        }
        if (this.d.size() > this.c && this.b < 40) {
            if (this.e == null) {
                c();
            }
            if (this.e != null) {
                if (i2 < this.a.f) {
                    if (i < this.a.e) {
                        this.e.get(0).a(i, i2, multiPointItem);
                        return;
                    } else {
                        this.e.get(1).a(i, i2, multiPointItem);
                        return;
                    }
                }
                if (i < this.a.e) {
                    this.e.get(2).a(i, i2, multiPointItem);
                    return;
                } else {
                    this.e.get(3).a(i, i2, multiPointItem);
                    return;
                }
            }
            return;
        }
        this.d.add(multiPointItem);
    }

    private void c() {
        ArrayList arrayList = new ArrayList(4);
        this.e = arrayList;
        arrayList.add(new ak(this.a.a, this.a.e, this.a.b, this.a.f, this.b + 1));
        this.e.add(new ak(this.a.e, this.a.c, this.a.b, this.a.f, this.b + 1));
        this.e.add(new ak(this.a.a, this.a.e, this.a.f, this.a.d, this.b + 1));
        this.e.add(new ak(this.a.e, this.a.c, this.a.f, this.a.d, this.b + 1));
    }

    protected void a() {
        this.e = null;
        List<MultiPointItem> list = this.d;
        if (list != null) {
            list.clear();
        }
    }

    protected void a(ah ahVar, Collection<MultiPointItem> collection, double d) {
        a(ahVar, collection, 1.0f, d);
    }

    private void a(ah ahVar, Collection<MultiPointItem> collection, float f, double d) {
        if (this.a.a(ahVar)) {
            List<MultiPointItem> list = this.d;
            if (list != null) {
                int size = (int) (list.size() * f);
                for (int i = 0; i < size; i++) {
                    MultiPointItem multiPointItem = this.d.get(i);
                    if (ahVar.a(multiPointItem.getIPoint())) {
                        collection.add(multiPointItem);
                    }
                }
            }
            if (d > 0.0d) {
                double d2 = ((((double) this.a.d) - ((double) this.a.b)) * (((double) this.a.c) - ((double) this.a.a))) / d;
                if (d2 < 0.7d) {
                    return;
                } else {
                    f = d2 > 1.0d ? 1.0f : (float) ((((4.8188d * d2) * d2) - (d2 * 4.9339d)) + 1.1093d);
                }
            }
            List<ak> list2 = this.e;
            if (list2 != null) {
                Iterator<ak> it = list2.iterator();
                while (it.hasNext()) {
                    it.next().a(ahVar, collection, f, d);
                }
            }
        }
    }

    public ah b() {
        return this.a;
    }
}
