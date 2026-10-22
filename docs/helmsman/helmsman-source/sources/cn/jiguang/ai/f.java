package cn.jiguang.ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class f implements Cloneable {
    private static j[] d = new j[0];
    private static i[] e = new i[0];
    private e a;
    private List[] b;
    private int c;

    public f() {
        this(new e());
    }

    private f(c cVar) throws IOException {
        this(new e(cVar));
        for (int i = 0; i < 4; i++) {
            int iB = this.a.b(i);
            if (iB > 0) {
                this.b[i] = new ArrayList(iB);
            }
            for (int i2 = 0; i2 < iB; i2++) {
                this.b[i].add(j.a(cVar, i));
            }
        }
        this.c = cVar.a();
    }

    private f(e eVar) {
        this.b = new List[4];
        this.a = eVar;
    }

    public f(byte[] bArr) {
        this(new c(bArr));
    }

    public static f a(j jVar) {
        f fVar = new f();
        List[] listArr = fVar.b;
        if (listArr[0] == null) {
            listArr[0] = new LinkedList();
        }
        fVar.a.a(0);
        fVar.b[0].add(jVar);
        return fVar;
    }

    public final j a() {
        List list = this.b[0];
        if (list == null || list.size() == 0) {
            return null;
        }
        return (j) list.get(0);
    }

    public final i[] a(int i) {
        boolean z;
        if (this.b[1] == null) {
            return e;
        }
        LinkedList linkedList = new LinkedList();
        List list = this.b[1];
        j[] jVarArr = list == null ? d : (j[]) list.toArray(new j[list.size()]);
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < jVarArr.length; i2++) {
            h hVarB = jVarArr[i2].b();
            if (!hashSet.contains(hVarB)) {
                z = true;
                break;
            }
            int size = linkedList.size() - 1;
            while (true) {
                if (size < 0) {
                    z = true;
                    break;
                }
                i iVar = (i) linkedList.get(size);
                if (iVar.b().d() == jVarArr[i2].d() && iVar.b().e() == jVarArr[i2].e() && iVar.b().b().equals(hVarB)) {
                    iVar.a(jVarArr[i2]);
                    z = false;
                    break;
                }
                size--;
            }
            if (z) {
                linkedList.add(new i(jVarArr[i2]));
                hashSet.add(hVarB);
            }
        }
        return (i[]) linkedList.toArray(new i[linkedList.size()]);
    }

    public final byte[] b(int i) {
        d dVar = new d();
        this.a.a(dVar);
        a aVar = new a();
        this.a.a();
        for (int i2 = 0; i2 < 4; i2++) {
            List list = this.b[i2];
            if (list != null) {
                int size = list.size();
                int iA = dVar.a();
                j jVar = null;
                for (int i3 = 0; i3 < size; i3++) {
                    j jVar2 = (j) this.b[i2].get(i3);
                    if (i2 != 3) {
                        if (jVar != null) {
                            if (!(jVar2.d() == jVar.d() && jVar2.e() == jVar.e() && jVar2.b().equals(jVar.b()))) {
                                iA = dVar.a();
                            }
                        }
                        jVar2.a(dVar, aVar);
                        if (dVar.a() > 65535) {
                            dVar.a(iA);
                            break;
                        }
                        jVar = jVar2;
                    }
                }
            }
        }
        this.c = dVar.a();
        return dVar.b();
    }

    public final Object clone() {
        f fVar = new f();
        int i = 0;
        while (true) {
            List[] listArr = this.b;
            if (i >= listArr.length) {
                fVar.a = (e) this.a.clone();
                fVar.c = this.c;
                return fVar;
            }
            if (listArr[i] != null) {
                fVar.b[i] = new LinkedList(this.b[i]);
            }
            i++;
        }
    }
}
