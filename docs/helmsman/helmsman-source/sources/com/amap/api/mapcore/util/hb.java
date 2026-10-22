package com.amap.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.api.maps.AMapException;

/* JADX INFO: compiled from: NetManger.java */
/* JADX INFO: loaded from: classes.dex */
public class hb extends gw {
    private static hb a;
    private hl b;
    private Handler c;

    public static hb a(boolean z) {
        return a(z, 5);
    }

    private static synchronized hb a(boolean z, int i) {
        try {
            hb hbVar = a;
            if (hbVar == null) {
                a = new hb(z, i);
            } else if (z && hbVar.b == null) {
                hbVar.b = hl.a(i);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return a;
    }

    private hb(boolean z, int i) {
        if (z) {
            try {
                this.b = hl.a(i);
            } catch (Throwable th) {
                fn.b(th, "NetManger", "NetManger1");
                th.printStackTrace();
                return;
            }
        }
        if (Looper.myLooper() == null) {
            this.c = new a(Looper.getMainLooper(), null);
        } else {
            this.c = new a();
        }
    }

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.hb$1, reason: invalid class name */
    /* JADX INFO: compiled from: NetManger.java */
    class AnonymousClass1 extends hm {
        final /* synthetic */ hc a;
        final /* synthetic */ hd b;
        final /* synthetic */ hb c;

        @Override // com.amap.api.mapcore.util.hm
        public void a() {
            try {
                this.c.a(this.c.b(this.a, false), this.b);
            } catch (ev e) {
                this.c.a(e, this.b);
            }
        }
    }

    @Override // com.amap.api.mapcore.util.gw
    public byte[] b(hc hcVar) throws ev {
        try {
            he heVarA = a(hcVar, false);
            if (heVarA != null) {
                return heVarA.a;
            }
            return null;
        } catch (ev e) {
            throw e;
        } catch (Throwable th) {
            th.printStackTrace();
            fn.a().c(th, "NetManager", "makeSyncPostRequest");
            throw new ev(AMapException.ERROR_UNKNOWN);
        }
    }

    public byte[] d(hc hcVar) throws ev {
        try {
            he heVarB = b(hcVar, false);
            if (heVarB != null) {
                return heVarB.a;
            }
            return null;
        } catch (ev e) {
            throw e;
        } catch (Throwable unused) {
            throw new ev(AMapException.ERROR_UNKNOWN);
        }
    }

    public he b(hc hcVar, boolean z) throws ev {
        try {
            c(hcVar);
            return new gz(hcVar.f, hcVar.g, hcVar.h == null ? null : hcVar.h, z).a(hcVar.c(), hcVar.a(), hcVar.b());
        } catch (ev e) {
            throw e;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new ev(AMapException.ERROR_UNKNOWN);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(ev evVar, hd hdVar) {
        hf hfVar = new hf();
        hfVar.a = evVar;
        hfVar.b = hdVar;
        Message messageObtain = Message.obtain();
        messageObtain.obj = hfVar;
        messageObtain.what = 1;
        this.c.sendMessage(messageObtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(he heVar, hd hdVar) {
        hdVar.a(heVar.b, heVar.a);
        hf hfVar = new hf();
        hfVar.b = hdVar;
        Message messageObtain = Message.obtain();
        messageObtain.obj = hfVar;
        messageObtain.what = 0;
        this.c.sendMessage(messageObtain);
    }

    /* JADX INFO: compiled from: NetManger.java */
    static class a extends Handler {
        /* synthetic */ a(Looper looper, AnonymousClass1 anonymousClass1) {
            this(looper);
        }

        private a(Looper looper) {
            super(looper);
        }

        public a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                int i = message.what;
                if (i == 0) {
                    ((hf) message.obj).b.a();
                } else {
                    if (i != 1) {
                        return;
                    }
                    hf hfVar = (hf) message.obj;
                    hfVar.b.a(hfVar.a);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
