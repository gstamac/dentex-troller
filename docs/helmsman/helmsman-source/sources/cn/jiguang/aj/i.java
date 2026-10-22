package cn.jiguang.aj;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import cn.jiguang.internal.JConstants;
import com.MAVLink.enums.MAV_CMD;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.facebook.GraphResponse;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public final class i {
    private static volatile i i;
    private static final Object j = new Object();
    private cn.jiguang.ah.i a;
    private int b;
    private int c;
    private long f;
    private boolean g;
    private Context k;
    private int d = 0;
    private int e = 0;
    private boolean l = false;
    private final AtomicBoolean m = new AtomicBoolean(false);
    private cn.jiguang.an.a n = new j(this);
    private boolean h = true;

    private i() {
    }

    public static i a() {
        if (i == null) {
            synchronized (j) {
                if (i == null) {
                    i = new i();
                }
            }
        }
        return i;
    }

    private synchronized void a(Context context) {
        if (this.l) {
            return;
        }
        if (context == null) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "init context is null");
            return;
        }
        cn.jiguang.ad.d.b("JCoreTCPManager", "init tcp manager...");
        this.k = context.getApplicationContext();
        cn.jiguang.af.b.a("JCoreTCPManager");
        cn.jiguang.an.b.a().a(this.k);
        g.a().a(context, true);
        this.l = true;
    }

    static /* synthetic */ void a(i iVar, Context context) {
        String str;
        cn.jiguang.ad.d.b("JCoreTCPManager", "handleResume...");
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.A().a(false)});
        if (iVar.g) {
            str = "[handleResume] is loggedin";
        } else {
            if (iVar.a == null) {
                iVar.h();
                return;
            }
            str = "[handleResume] tcp is connecting...";
        }
        cn.jiguang.ad.d.b("JCoreTCPManager", str);
    }

    static /* synthetic */ void b(i iVar, int i2) {
        iVar.b = i2;
        if (i2 == 1012) {
            cn.jiguang.ah.c.a(iVar.k);
        }
        iVar.f();
    }

    static /* synthetic */ void b(i iVar, Context context) {
        cn.jiguang.ad.d.b("JCoreTCPManager", "handleStop...");
        if (((Boolean) cn.jiguang.g.b.a(context, cn.jiguang.g.a.A())).booleanValue()) {
            cn.jiguang.ad.d.c("JCoreTCPManager", "tcp already stoped");
            return;
        }
        b.a();
        if (!b.a(0)) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "Action: handleStopPush - can't stop tcp");
        } else {
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.A().a(true)});
            iVar.f();
        }
    }

    static /* synthetic */ int c(i iVar, int i2) {
        iVar.b = 0;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Bundle bundle) {
        if (this.m.get()) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "isBeating, skip this time");
            return;
        }
        if (!(bundle != null ? bundle.getBoolean("force", false) : false)) {
            if (System.currentTimeMillis() - this.f < 18000) {
                cn.jiguang.ad.d.b("JCoreTCPManager", "No need to rtc, Because it have succeed recently");
                return;
            }
        }
        cn.jiguang.ad.d.c("JCoreTCPManager", "Send heart beat");
        cn.jiguang.an.b.a().b(1005);
        if (!this.g) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "socket is closed or push isn't login");
            return;
        }
        this.m.set(true);
        cn.jiguang.an.b.a().b(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT);
        Long lValueOf = Long.valueOf(cn.jiguang.ah.c.b(this.k));
        long jD = cn.jiguang.d.a.d(this.k);
        cn.jiguang.ad.d.b("JCoreTCPManager", "heartbeat - juid:" + jD + ", flag:1");
        byte[] bArrA = cn.jiguang.al.b.a(this.k, cn.jiguang.al.b.a(lValueOf.longValue(), JConstants.tcpSessionId, jD, (short) 1));
        if (bArrA != null) {
            this.a.c().a(bArrA);
        } else {
            cn.jiguang.ad.d.g("JCoreTCPManager", "send hb failed:sendData is null");
        }
        cn.jiguang.an.b.a().b(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, OkHttpUtils.DEFAULT_MILLISECONDS, this.n);
    }

    static /* synthetic */ void c(i iVar, Context context) {
        cn.jiguang.ad.d.b("JCoreTCPManager", "handleUnRegister...");
        if (!((Boolean) cn.jiguang.g.b.a(context, cn.jiguang.g.a.n())).booleanValue()) {
            cn.jiguang.ad.d.c("JCoreTCPManager", "tcp already stoped");
            return;
        }
        b.a();
        if (!b.a(0)) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "Action: handleUnRegister - can't stop tcp");
        } else {
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.n().a(false)});
            iVar.f();
        }
    }

    static /* synthetic */ void d(i iVar) {
        cn.jiguang.ad.d.c("JCoreTCPManager", "Action - onDisconnected");
        if (iVar.g) {
            iVar.g = false;
            b.a().a(iVar.k, -1, -1, "push connect break");
        }
        if (iVar.a == null && ((Boolean) cn.jiguang.g.b.a(iVar.k, cn.jiguang.g.a.A())).booleanValue()) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "push already stopped!!!");
            return;
        }
        iVar.e = 0;
        iVar.f();
        iVar.g();
        iVar.d++;
    }

    static /* synthetic */ void e(i iVar) {
        cn.jiguang.ad.d.c("JCoreTCPManager", "Action - onLoggedIn");
        if (!iVar.g) {
            iVar.g = true;
            b.a().a(iVar.k, 1, 0, GraphResponse.SUCCESS_KEY);
        }
        iVar.b();
        iVar.d = 0;
        iVar.e = 0;
        Bundle bundle = new Bundle();
        bundle.putBoolean("login", true);
        e.a(iVar.k, "periodtask", bundle);
        if (iVar.e()) {
            cn.jiguang.an.b.a().b(MAV_CMD.MAV_CMD_IMAGE_START_CAPTURE, 2000L, iVar.n);
            cn.jiguang.e.a.a().a(iVar.k);
            o.a().a(iVar.k);
            g.a().a(iVar.k, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean e() {
        if (cn.jiguang.ac.b.a().d() || q.a().b(this.k)) {
            return true;
        }
        cn.jiguang.ad.d.b("JCoreTCPManager", "not keep tcp");
        this.h = false;
        f();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        cn.jiguang.ah.i iVar = this.a;
        if (iVar == null) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "tcp has stopeed");
        } else {
            iVar.b();
            this.a = null;
        }
    }

    static /* synthetic */ void f(i iVar) {
        iVar.m.set(false);
        iVar.e++;
        cn.jiguang.ad.d.c("JCoreTCPManager", "Action - onHeartbeatTimeout - timeoutTimes:" + iVar.e);
        cn.jiguang.ad.a.a(JConstants.SDK_TYPE, "JCoreTCPManager", false, 2, "============================================================");
        if (iVar.a != null && !iVar.g) {
            cn.jiguang.ad.d.c("JCoreTCPManager", "Is connecting now. Give up to retry.");
            return;
        }
        if (!iVar.g || iVar.e > 1) {
            iVar.f();
            iVar.g();
        } else {
            cn.jiguang.ad.d.c("JCoreTCPManager", "Already logged in. Give up to retry.");
            cn.jiguang.an.b.a().b(1005, 5000L, iVar.n);
        }
    }

    private void g() {
        cn.jiguang.ad.d.c("JCoreTCPManager", "Action - retryConnect - disconnectedTimes:" + this.d);
        if (!cn.jiguang.f.a.d(this.k.getApplicationContext())) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "[retryConnect] network is not connect");
            return;
        }
        if (this.c > 0) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "[retryConnect] registerErrCode >0,registerErrCode:" + this.c);
            return;
        }
        int iC = cn.jiguang.f.a.c(this.k.getApplicationContext());
        int iPow = (int) (Math.pow(2.0d, this.d) * 3.0d * 1000.0d);
        int i2 = JConstants.DEFAULT_HEARTBEAT_INTERVAL;
        int i3 = (i2 * 1000) / 2;
        if (iPow > i3) {
            iPow = i3;
        }
        cn.jiguang.ad.d.b("JCoreTCPManager", "[retryConnect] mDisconnectedTimes:" + this.d + ",chargedLever:" + iC + ",heartbeatInterval:" + i2 + ",delayTime:" + iPow);
        if (iC != 1 ? this.d >= 5 : this.d >= 30) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "Give up to retry connect.");
        } else if (cn.jiguang.an.b.a().a(1011)) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "Already has MSG_RESTART_CONN");
        } else {
            cn.jiguang.an.b.a().b(1011, iPow, this.n);
        }
    }

    static /* synthetic */ void g(i iVar) {
        cn.jiguang.ad.d.c("JCoreTCPManager", "Action - onHeartbeatSucceed");
        b.a().a(iVar.k, 19, 0, "ack success");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void h() {
        cn.jiguang.ad.d.c("JCoreTCPManager", "Action - restartNetworkingClient, pid:" + Process.myPid());
        if (!this.h) {
            cn.jiguang.ad.d.e("JCoreTCPManager", "need not keep tcp,next start app will re login");
            return;
        }
        if (!cn.jiguang.f.a.d(this.k.getApplicationContext())) {
            cn.jiguang.ad.d.e("JCoreTCPManager", "No network connection. Give up to start connection thread.");
            return;
        }
        if (((Boolean) cn.jiguang.g.b.a(this.k, cn.jiguang.g.a.A())).booleanValue()) {
            cn.jiguang.ad.d.c("JCoreTCPManager", "[restartNetworkingClient] tcp has close by active");
            return;
        }
        if (!((Boolean) cn.jiguang.g.b.a(this.k, cn.jiguang.g.a.n())).booleanValue()) {
            cn.jiguang.ad.d.c("JCoreTCPManager", "[restartNetworkingClient] tcp has close by ups.unregister");
            return;
        }
        int i2 = this.c;
        if (i2 != 1005 && i2 != 1006 && i2 != 1008 && i2 != 1009) {
            if (this.b == 102) {
                cn.jiguang.ad.d.g("JCoreTCPManager", "login failed:102,give up start connection thread.reset from next app start");
                return;
            } else {
                if (this.a != null) {
                    cn.jiguang.ad.d.c("JCoreTCPManager", "NetworkingClient is running");
                    return;
                }
                cn.jiguang.ah.i iVar = new cn.jiguang.ah.i(this.k.getApplicationContext());
                this.a = iVar;
                iVar.a();
                return;
            }
        }
        cn.jiguang.ad.d.b("JCoreTCPManager", "[restartNetworkingClient] registerErrCode >0,registerErrCode:" + this.c);
        b.a().a(this.k, 2002, this.c, "");
    }

    public final void a(Context context, String str, Bundle bundle) {
        a(context);
        cn.jiguang.af.b.a("JCoreTCPManager", new k(this, context, str, bundle));
    }

    public final void a(Bundle bundle) {
        long j2;
        if (((Boolean) cn.jiguang.g.b.a(this.k, cn.jiguang.g.a.A())).booleanValue()) {
            cn.jiguang.ad.d.c("JCoreTCPManager", "[rtc] tcp has close by active");
            return;
        }
        boolean z = true;
        if (bundle != null) {
            z = bundle.getBoolean("force", true);
            j2 = bundle.getLong("delay_time", 0L);
        } else {
            j2 = 0;
        }
        if (this.a == null) {
            h();
            return;
        }
        if (j2 <= 0) {
            c(bundle);
        } else {
            if (z) {
                cn.jiguang.an.b.a().b(1005);
                cn.jiguang.an.b.a().b(1004);
            }
            cn.jiguang.an.b.a().b(z ? 1004 : 1005, j2, this.n);
        }
        cn.jiguang.ad.d.d("JCoreTCPManager", "send rtc force=" + z + " delay=" + j2);
    }

    public final void b() {
        cn.jiguang.an.b.a().b(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT);
        this.f = System.currentTimeMillis();
        this.e = 0;
        this.m.set(false);
        cn.jiguang.ad.d.d("JCoreTCPManager", "update rtc state");
    }

    public final void b(Bundle bundle) {
        if (((Boolean) cn.jiguang.g.b.a(this.k, cn.jiguang.g.a.A())).booleanValue()) {
            cn.jiguang.ad.d.f("JCoreTCPManager", "[netWorkChanged] tcp has close by active");
            return;
        }
        cn.jiguang.an.b.a().b(1006);
        cn.jiguang.an.b.a().b(1007);
        if (!bundle.getBoolean("connected", false)) {
            cn.jiguang.ad.d.b("JCoreTCPManager", "Handle disconnected state.");
            cn.jiguang.an.b.a().b(1007, 3000L, this.n);
            return;
        }
        cn.jiguang.ad.d.b("JCoreTCPManager", "Handle connected state.");
        if (this.a == null) {
            h();
        } else {
            cn.jiguang.an.b.a().b(1006, 3000L, this.n);
        }
    }

    public final cn.jiguang.ah.i c() {
        return this.a;
    }

    public final boolean d() {
        return this.g;
    }
}
