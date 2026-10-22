package cn.jiguang.u;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.zhy.http.okhttp.OkHttpUtils;

/* JADX INFO: loaded from: classes.dex */
final class h extends Handler {
    final /* synthetic */ f a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(f fVar, Looper looper) {
        super(looper);
        this.a = fVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        String string;
        StringBuilder sbAppend;
        int i = 1001;
        switch (message.what) {
            case 1001:
                try {
                    if (this.a.g != null && this.a.g.equals("network")) {
                        cn.jiguang.w.a.d("JLocationGps", "get gps with network time out ");
                        this.a.g = "gps";
                        this.a.f();
                        this.a.c.requestLocationUpdates(this.a.g, 2000L, 0.0f, this.a.i);
                        cn.jiguang.w.a.a("JLocationGps", "request " + this.a.g + " location");
                        this.a.a.sendEmptyMessageDelayed(1001, OkHttpUtils.DEFAULT_MILLISECONDS);
                    } else {
                        cn.jiguang.w.a.d("JLocationGps", "get " + this.a.g + " time out ");
                        f.b(this.a);
                    }
                } catch (Throwable th) {
                    sbAppend = new StringBuilder("when location time out ").append(th.getMessage());
                }
                break;
            case 1004:
                cn.jiguang.w.a.a("JLocationGps", "LOAD_GPS_ACTION_REQUEST_ONLY_NETWORK");
                i = 1005;
            case 1003:
                try {
                    this.a.c.requestLocationUpdates(this.a.g, 2000L, 0.0f, this.a.i);
                    cn.jiguang.w.a.a("JLocationGps", "request " + this.a.g + " location");
                    this.a.a.sendEmptyMessageDelayed(i, 20000L);
                } catch (SecurityException unused) {
                    string = "No suitable permission when get last known location!";
                } catch (Throwable unused2) {
                    cn.jiguang.w.a.d("JLocationGps", "The provider is illegal argument!");
                    f.b(this.a);
                    return;
                }
                break;
            case 1005:
                sbAppend = new StringBuilder("get only network ").append(this.a.g).append(" time out ");
                string = sbAppend.toString();
                cn.jiguang.w.a.d("JLocationGps", string);
                f.b(this.a);
                break;
        }
    }
}
