package cn.jiguang.ai;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private b[] a = new b[17];

    public final int a(h hVar) {
        int i = -1;
        for (b bVar = this.a[(hVar.hashCode() & Integer.MAX_VALUE) % 17]; bVar != null; bVar = bVar.c) {
            if (bVar.a.equals(hVar)) {
                i = bVar.b;
            }
        }
        return i;
    }

    public final void a(int i, h hVar) {
        if (i > 16383) {
            return;
        }
        int iHashCode = (hVar.hashCode() & Integer.MAX_VALUE) % 17;
        b bVar = new b((byte) 0);
        bVar.a = hVar;
        bVar.b = i;
        bVar.c = this.a[iHashCode];
        this.a[iHashCode] = bVar;
    }
}
