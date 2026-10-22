package cn.jiguang.ah;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class i implements Runnable {
    public boolean a;
    private Context b;
    private cn.jiguang.am.a c = new cn.jiguang.am.b(8128, 20);
    private ExecutorService d;

    static {
        cn.jiguang.af.b.a("NetworkingClient");
    }

    public i(Context context) {
        this.b = context;
    }

    private boolean a(int i) {
        boolean z;
        while (!this.a) {
            if (i <= 0) {
                cn.jiguang.ad.d.c("NetworkingClient", "login error,retry login too many times");
                e();
                d();
                return false;
            }
            cn.jiguang.ad.d.b("NetworkingClient", "loginTimes:" + i);
            if (!cn.jiguang.d.b.a(this.b) || TextUtils.isEmpty(cn.jiguang.d.a.f(this.b))) {
                int iB = c.b(this.b, this.c);
                if (iB != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("resCode", iB);
                    cn.jiguang.aj.i.a().a(this.b, "tcp_a13", bundle);
                    e();
                    d();
                    z = false;
                } else {
                    cn.jiguang.aj.i.a().a(this.b, "tcp_a11", null);
                    z = true;
                }
            } else {
                z = true;
            }
            if (!z) {
                return false;
            }
            int iA = c.a(this.b, this.c);
            if (iA < 0) {
                d();
                return false;
            }
            if (iA <= 0) {
                cn.jiguang.aj.i.a().a(this.b, "tcp_a10", null);
                return true;
            }
            e();
            if (iA != 108) {
                cn.jiguang.ad.d.a("NetworkingClient", "Action - onLoginFailed - respCode:" + iA);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("resCode", iA);
                cn.jiguang.aj.i.a().a(this.b, "tcp_a12", bundle2);
                return false;
            }
            cn.jiguang.af.b.a(this.b);
            i--;
        }
        return false;
    }

    private boolean a(Context context) {
        String str;
        cn.jiguang.ad.d.b("NetworkingClient", "google:true");
        cn.jiguang.ao.a.a(context);
        try {
            this.c = new k(l.a(context)).a(this);
            if (a(2)) {
                return true;
            }
            str = "login failed";
            cn.jiguang.ad.d.f("NetworkingClient", str);
            return false;
        } catch (Exception e) {
            d();
            str = "sis and connect failed:" + e;
        }
    }

    private void d() {
        cn.jiguang.ad.d.c("NetworkingClient", "Action - closeConnection");
        cn.jiguang.f.k.a(this.c);
        cn.jiguang.aj.i.a().a(this.b, "tcp_a19", null);
    }

    private void e() {
        cn.jiguang.g.b.a(this.b, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.c(true).a((String) null), cn.jiguang.g.a.c(false).a((String) null)});
    }

    public final synchronized void a() {
        ExecutorService executorServiceNewSingleThreadExecutor = Executors.newSingleThreadExecutor();
        this.d = executorServiceNewSingleThreadExecutor;
        try {
            executorServiceNewSingleThreadExecutor.execute(this);
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("NetworkingClient", "execute networkingClient exception :" + th);
        }
    }

    public final synchronized void b() {
        cn.jiguang.ad.d.c("NetworkingClient", "Action - stop");
        cn.jiguang.f.k.a(this.c);
        this.a = true;
        ExecutorService executorService = this.d;
        if (executorService != null) {
            try {
                try {
                    executorService.shutdown();
                    if (!executorService.awaitTermination(100L, TimeUnit.MILLISECONDS)) {
                        executorService.shutdownNow();
                        if (!executorService.awaitTermination(100L, TimeUnit.MILLISECONDS)) {
                            cn.jiguang.ad.d.a("NetworkingClient", "executor did not terminate");
                        }
                    }
                } catch (Throwable th) {
                    cn.jiguang.ad.d.f("NetworkingClient", "shutDown e:" + th);
                }
            } catch (InterruptedException unused) {
                executorService.shutdownNow();
                cn.jiguang.ad.d.a("NetworkingClient", "current thread is interrupted by self");
                Thread.currentThread().interrupt();
            }
        }
    }

    public final cn.jiguang.am.a c() {
        return this.c;
    }

    @Override // java.lang.Runnable
    public final void run() {
        cn.jiguang.ad.d.e("NetworkingClient", "Begin to run in ConnectingThread - id:" + Thread.currentThread().getId());
        try {
            if (!a(this.b)) {
                cn.jiguang.ad.d.c("NetworkingClient", "prepare Push Channel failed , returned");
                return;
            }
            while (!this.a) {
                cn.jiguang.ad.d.c("NetworkingClient", "Network listening...");
                try {
                    ByteBuffer byteBufferA = this.c.a(0);
                    cn.jiguang.af.b.a("NetworkingClient", new v(this.b, byteBufferA.array()));
                    cn.jiguang.ad.d.c("NetworkingClient", "Received bytes - len:" + byteBufferA.array().length + ", pkg:" + cn.jiguang.f.a.b(this.b));
                } catch (cn.jiguang.aj.f e) {
                    cn.jiguang.ad.d.f("NetworkingClient", " recv failed with error:" + e + " ,No Break!!");
                }
            }
            if (this.a) {
                cn.jiguang.ad.d.c("NetworkingClient", "Break receiving by wantStop");
            }
            d();
        } catch (Throwable th) {
            cn.jiguang.ad.d.d("NetworkingClient", "run exception", th);
        }
    }
}
