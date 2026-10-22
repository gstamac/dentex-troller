package com.tencent.mm.sdk.b;

import android.os.Looper;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public final class d implements e.a {
    private e aJ;
    private int aL;
    private ConcurrentHashMap<Runnable, WeakReference<g>> aK = new ConcurrentHashMap<>();
    private LinkedList<WeakReference<g>> aM = new LinkedList<>();

    public d() {
        e eVar = new e(this);
        this.aJ = eVar;
        if (eVar.getLooper().getThread().getName().equals("initThread")) {
            b.a("MicroMsg.MMHandler", "MMHandler can not init handler with initThread looper, stack %s", h.u());
        }
    }

    public d(Looper looper) {
        this.aJ = new e(looper, this);
        if (looper.getThread().getName().equals("initThread")) {
            b.a("MicroMsg.MMHandler", "MMHandler can not init handler with initThread looper, stack %s", h.u());
        }
    }

    @Override // com.tencent.mm.sdk.b.e.a
    public final void a(Runnable runnable, g gVar) {
        this.aK.put(runnable, new WeakReference<>(gVar));
    }

    @Override // com.tencent.mm.sdk.b.e.a
    public final void b(Runnable runnable, g gVar) {
        WeakReference<g> weakReference = this.aK.get(runnable);
        if (weakReference == null || weakReference.get() == null || weakReference.get() != gVar) {
            return;
        }
        this.aK.remove(runnable);
        if (this.aL > 0) {
            if (this.aM.size() == this.aL) {
                this.aM.pop();
            }
            this.aM.add(weakReference);
        }
    }

    public final boolean post(Runnable runnable) {
        return this.aJ.post(runnable);
    }

    public final String toString() {
        return "MMHandler(" + getClass().getName() + ")";
    }
}
