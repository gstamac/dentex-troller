package cn.jiguang.ai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class i implements Serializable {
    private List a;
    private short b;
    private short c;

    public i() {
        this.a = new ArrayList(1);
        this.b = (short) 0;
        this.c = (short) 0;
    }

    public i(j jVar) {
        this();
        b(jVar);
    }

    private static String a(Iterator it) {
        StringBuffer stringBuffer = new StringBuffer();
        while (it.hasNext()) {
            j jVar = (j) it.next();
            stringBuffer.append("[");
            stringBuffer.append(jVar.a());
            stringBuffer.append("]");
            if (it.hasNext()) {
                stringBuffer.append(" ");
            }
        }
        return stringBuffer.toString();
    }

    private synchronized Iterator a(boolean z, boolean z2) {
        int i;
        List listSubList;
        int size = this.a.size();
        int i2 = z ? size - this.b : this.b;
        if (i2 == 0) {
            return Collections.EMPTY_LIST.iterator();
        }
        if (!z) {
            i = size - this.b;
        } else if (z2) {
            if (this.c >= i2) {
                this.c = (short) 0;
            }
            i = this.c;
            this.c = (short) (i + 1);
        } else {
            i = 0;
        }
        ArrayList arrayList = new ArrayList(i2);
        if (z) {
            arrayList.addAll(this.a.subList(i, i2));
            if (i != 0) {
                listSubList = this.a.subList(0, i);
            }
            return arrayList.iterator();
        }
        listSubList = this.a.subList(i, size);
        arrayList.addAll(listSubList);
        return arrayList.iterator();
    }

    private void b(j jVar) {
        if (this.b == 0) {
            this.a.add(jVar);
        } else {
            List list = this.a;
            list.add(list.size() - this.b, jVar);
        }
    }

    private synchronized long c() {
        return b().f();
    }

    public final synchronized Iterator a() {
        return a(true, true);
    }

    public final synchronized void a(j jVar) {
        if (this.a.size() == 0) {
            b(jVar);
            return;
        }
        j jVarB = b();
        if (!jVar.a(jVarB)) {
            throw new IllegalArgumentException("record does not match rrset");
        }
        if (jVar.f() != jVarB.f()) {
            if (jVar.f() > jVarB.f()) {
                jVar = jVar.g();
                jVar.a(jVarB.f());
            } else {
                for (int i = 0; i < this.a.size(); i++) {
                    j jVarG = ((j) this.a.get(i)).g();
                    jVarG.a(jVar.f());
                    this.a.set(i, jVarG);
                }
            }
        }
        if (!this.a.contains(jVar)) {
            b(jVar);
        }
    }

    public final synchronized j b() {
        if (this.a.size() == 0) {
            throw new IllegalStateException("rrset is empty");
        }
        return (j) this.a.get(0);
    }

    public final String toString() {
        if (this.a.size() == 0) {
            return "{empty}";
        }
        StringBuffer stringBuffer = new StringBuffer("{ ");
        stringBuffer.append(b().b() + " ");
        stringBuffer.append(c() + " ");
        stringBuffer.append(a(a(true, false)));
        if (this.b > 0) {
            stringBuffer.append(" sigs: ");
            stringBuffer.append(a(a(false, false)));
        }
        stringBuffer.append(" }");
        return stringBuffer.toString();
    }
}
