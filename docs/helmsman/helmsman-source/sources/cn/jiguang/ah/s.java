package cn.jiguang.ah;

import com.MAVLink.common.msg_gps_rtcm_data;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
final class s extends ThreadPoolExecutor {
    final /* synthetic */ r a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    s(r rVar, int i, int i2, long j, TimeUnit timeUnit, BlockingQueue blockingQueue) {
        super(i, i2, 0L, timeUnit, blockingQueue);
        this.a = rVar;
    }

    @Override // java.util.concurrent.ThreadPoolExecutor
    protected final void afterExecute(Runnable runnable, Throwable th) {
        if (getActiveCount() <= 1 && getQueue().isEmpty() && !this.a.c.hasMessages(msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA)) {
            try {
                this.a.d.set(true);
                this.a.h.countDown();
            } catch (Throwable unused) {
            }
        }
    }
}
