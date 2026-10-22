package cn.jiguang.ah;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.MAVLink.common.msg_gps_rtcm_data;
import com.MAVLink.enums.MAV_CMD;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public final class r {
    private final ThreadPoolExecutor b;
    private final Handler c;
    private Object g;
    private final AtomicBoolean d = new AtomicBoolean(true);
    private volatile long f = 0;
    private CountDownLatch h = new CountDownLatch(1);
    private final int a = MAV_CMD.MAV_CMD_IMAGE_START_CAPTURE;
    private final a<Object> e = null;

    public r(int i, int i2, a<Object> aVar) {
        s sVar = new s(this, 5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        this.b = sVar;
        this.c = new u(sVar);
    }

    private synchronized long b(long j) {
        long j2 = this.f + j;
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (j2 < jUptimeMillis) {
            this.f = jUptimeMillis;
            return -1L;
        }
        this.f = j2;
        return j2;
    }

    static /* synthetic */ Looper c() {
        t tVar = new t("Step");
        tVar.start();
        Looper looper = tVar.getLooper();
        return looper == null ? Looper.getMainLooper() : looper;
    }

    public final synchronized Object a(long j) {
        if (a()) {
            return this.g;
        }
        if (this.d.get()) {
            return null;
        }
        try {
            if (j != -1) {
                this.h.await(j, TimeUnit.MILLISECONDS);
            } else {
                this.h.await();
            }
        } catch (InterruptedException unused) {
        }
        if (this.h.getCount() == 0) {
            this.h = new CountDownLatch(1);
        }
        return this.g;
    }

    public final void a(Object obj) {
        if (a() || obj == null) {
            return;
        }
        this.g = obj;
        this.h.countDown();
        this.c.removeMessages(msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA);
        this.b.shutdownNow();
        try {
            this.c.getLooper().quit();
        } catch (Throwable unused) {
        }
        cn.jiguang.ad.d.b("Step", "done!");
    }

    public final void a(Callable<?> callable) {
        try {
            long jB = b(this.a);
            if (jB < 0) {
                this.b.submit(callable);
            } else {
                Message messageObtain = Message.obtain();
                messageObtain.what = msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA;
                messageObtain.obj = callable;
                this.c.sendMessageAtTime(messageObtain, jB);
            }
            this.d.set(false);
        } catch (Throwable unused) {
        }
    }

    public final boolean a() {
        return this.g != null;
    }

    public final void b() {
        try {
            ThreadPoolExecutor threadPoolExecutor = this.b;
            if (threadPoolExecutor != null) {
                threadPoolExecutor.getQueue().clear();
                this.c.removeMessages(msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA);
            }
        } catch (Throwable th) {
            Log.w("Step", "clean executor e:" + th);
        }
    }
}
