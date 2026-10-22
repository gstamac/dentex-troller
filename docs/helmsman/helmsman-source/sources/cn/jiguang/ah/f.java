package cn.jiguang.ah;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
final class f implements Comparator<d> {
    f() {
    }

    @Override // java.util.Comparator
    public final /* synthetic */ int compare(d dVar, d dVar2) {
        d dVar3 = dVar;
        d dVar4 = dVar2;
        if (dVar3.equals(dVar4)) {
            return 0;
        }
        if (dVar3.b > dVar4.b) {
            return -1;
        }
        if (dVar3.b < dVar4.b) {
            return 1;
        }
        if (dVar3.e > dVar4.e) {
            return -1;
        }
        if (dVar3.e < dVar4.e) {
            return 1;
        }
        if (dVar3.d != 0 && dVar4.d != 0) {
            if (dVar3.d < dVar4.d) {
                return -1;
            }
            if (dVar3.d > dVar4.d) {
                return 1;
            }
        }
        if (dVar3.c == 0 || dVar4.c == 0) {
            return 0;
        }
        if (dVar3.c > dVar4.c + 180000) {
            return -1;
        }
        return dVar3.c < dVar4.c - 180000 ? 1 : 0;
    }
}
