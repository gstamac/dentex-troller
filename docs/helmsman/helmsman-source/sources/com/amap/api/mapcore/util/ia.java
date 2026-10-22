package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: Beacon.java */
/* JADX INFO: loaded from: classes.dex */
public final class ia implements Comparable<ia> {
    public String a;
    public String b;
    public byte[] c;
    public String d;
    public String e;
    public int f;
    public int g;
    public long h;
    public int i;

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.lang.Comparable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(ia iaVar) {
        int i = this.g;
        int i2 = iaVar.g;
        if (i < i2) {
            return 1;
        }
        return (i != i2 && i > i2) ? -1 : 0;
    }

    public final String toString() {
        return "name = " + this.b + ",uuid = " + this.a + ",major = " + this.d + ",minor = " + this.e + ",TxPower = " + this.f + ",rssi = " + this.g + ",time = " + this.h;
    }
}
