package cn.jpush.android.s;

import android.content.Context;
import android.text.TextUtils;
import cn.jpush.android.cache.Key;
import cn.jpush.android.cache.Sp;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.helper.g;
import cn.jpush.android.local.JPushConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static volatile a b;
    private Map<Byte, C0011a> a = new HashMap();

    /* JADX INFO: renamed from: cn.jpush.android.s.a$a, reason: collision with other inner class name */
    private class C0011a {
        public byte a;
        public String b;
        public long c;
        public byte[] d;
        public int e = 0;

        public C0011a(byte b, String str, long j, byte[] bArr) {
            this.a = b;
            this.b = str;
            this.c = j;
            this.d = bArr;
        }

        public String toString() {
            return "PluginPlatformRegIDBean{pluginPlatformType=" + ((int) this.a) + ", regid='" + this.b + "', rid=" + this.c + ", retryCount=" + this.e + '}';
        }
    }

    private a() {
    }

    private C0011a a(long j) {
        for (Map.Entry<Byte, C0011a> entry : this.a.entrySet()) {
            if (entry.getValue().c == j) {
                return entry.getValue();
            }
        }
        Logger.d("PluginPlatformRidUpdate", "can not find uploadBean by rid");
        return null;
    }

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                if (b == null) {
                    b = new a();
                }
            }
        }
        return b;
    }

    private synchronized void a(Context context, C0011a c0011a) {
        JCoreHelper.sendRequest(context, JPushConstants.SDK_TYPE, 27, 1, c0011a.c, OkHttpUtils.DEFAULT_MILLISECONDS, c0011a.d);
    }

    private void b(Context context, byte b2, String str) {
        long jA = g.a();
        Logger.v("PluginPlatformRidUpdate", "sendUpdateRidInfo regid:" + str + ",rid:" + jA + ",whichPlatform:" + ((int) b2));
        C0011a c0011a = new C0011a(b2, str, jA, cn.jpush.android.q.b.a(str, b2));
        this.a.put(Byte.valueOf(b2), c0011a);
        a(context, c0011a);
    }

    public synchronized void a(Context context, byte b2, String str) {
        try {
            if (b2 == 0) {
                Logger.w("PluginPlatformRidUpdate", "unknown plugin platform type");
            } else if (!JPushConstants.isTcpConnected()) {
                Logger.d("PluginPlatformRidUpdate", "tcp disconnected");
            } else {
                if (this.a.containsKey(Byte.valueOf(b2)) && TextUtils.equals(this.a.get(Byte.valueOf(b2)).b, str)) {
                    Logger.d("PluginPlatformRidUpdate", "same regid request, drop it");
                    return;
                }
                b(context, b2, str);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void a(Context context, long j) {
        C0011a c0011aA = a(j);
        Logger.ii("PluginPlatformRidUpdate", "onUpdateRidSuccess rid:" + j + " ,pluginPlatformRegIDBean:" + c0011aA);
        if (c0011aA != null) {
            Sp.set(context, Key.ThirdPush_RegID(c0011aA.a).set(c0011aA.b));
            Sp.set(context, Key.ThirdPush_RegUpload(c0011aA.a).set(true));
            this.a.remove(Byte.valueOf(c0011aA.a));
            c.a().a(context, (int) c0011aA.a, c0011aA.b);
        }
    }

    public void a(Context context, long j, int i) {
        C0011a c0011aA = a(j);
        Logger.ii("PluginPlatformRidUpdate", "onUpdateRidFailed rid:" + j + ",errorCode:" + i + " ,pluginPlatformRegIDBean:" + c0011aA);
        if (c0011aA != null) {
            if (c0011aA.e < 3) {
                c0011aA.e++;
                a(context, c0011aA);
            } else {
                Logger.d("PluginPlatformRidUpdate", "updateRegId failed");
                this.a.remove(Byte.valueOf(c0011aA.a));
            }
        }
    }

    public void b(Context context, long j) {
        C0011a c0011aA = a(j);
        Logger.ii("PluginPlatformRidUpdate", "onUpdateRidTimeout rid:" + j + " ,pluginPlatformRegIDBean:" + c0011aA);
        if (c0011aA != null) {
            if (c0011aA.e < 3) {
                c0011aA.e++;
                a(context, c0011aA);
            } else {
                Logger.d("PluginPlatformRidUpdate", "updateRegId failed by timeout");
                this.a.remove(Byte.valueOf(c0011aA.a));
            }
        }
    }
}
