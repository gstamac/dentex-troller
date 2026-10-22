package cn.jiguang.aj;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.view.PointerIconCompat;

/* JADX INFO: loaded from: classes.dex */
final class k implements Runnable {
    final /* synthetic */ i a;
    private Context b;
    private String c;
    private Bundle d;

    public k(i iVar, Context context, String str, Bundle bundle) {
        this.a = iVar;
        this.b = context;
        this.c = str;
        this.d = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        i iVar;
        long j;
        String str;
        try {
            if (this.c.equals("tcp_a1")) {
                if (this.a.a != null) {
                    return;
                } else {
                    iVar = this.a;
                }
            } else if (this.c.equals("tcp_a3") || this.c.equals("tcp_a4") || this.c.equals("tcp_a20")) {
                if (this.a.a != null) {
                    if (this.a.a.c() != null) {
                        byte[] byteArray = this.d.getByteArray("body");
                        int i = this.d.getInt("cmd", -1);
                        int i2 = this.d.getInt("ver", -1);
                        long j2 = this.d.getLong("rid", -1L);
                        String string = this.d.getString("sdk_type");
                        cn.jiguang.ad.d.b("JCoreTCPManager", "send data,cmd:" + i + ",ver:" + i2 + ",rid:" + j2 + ",body size:" + byteArray.length);
                        if (i >= 0 && i2 >= 0 && j2 >= 0) {
                            if (this.c.equals("tcp_a3")) {
                                o.a().a(this.b, j2, i, i2, byteArray, string);
                                return;
                            }
                            if (this.c.equals("tcp_a20")) {
                                long j3 = this.d.getLong("uid", 0L);
                                if (j3 == 0) {
                                    cn.jiguang.ad.d.f("JCoreTCPManager", "share response uid is 0");
                                    return;
                                }
                                j = j3;
                            } else {
                                j = 0;
                            }
                            i.a().c().c().a(cn.jiguang.al.b.a(this.b, i, i2, j2, byteArray, j));
                            return;
                        }
                        return;
                    }
                    return;
                }
                cn.jiguang.ad.d.b("JCoreTCPManager", "send data failed:tcp breaked,will restart");
                iVar = this.a;
            } else {
                if (this.c.equals("tcp_a5")) {
                    Bundle bundle = this.d;
                    if (bundle != null) {
                        byte[] byteArray2 = bundle.getByteArray("body");
                        int i3 = this.d.getInt("cmd", -1);
                        int i4 = this.d.getInt("ver", -1);
                        long j4 = this.d.getLong("rid", -1L);
                        String string2 = this.d.getString("sdk_type");
                        long j5 = this.d.getLong("timeout");
                        cn.jiguang.ad.d.b("JCoreTCPManager", "send quest,cmd:" + i3 + ",ver:" + i4 + ",rid:" + j4 + ",body size:" + byteArray2.length);
                        if (i3 >= 0 && i4 >= 0 && j4 >= 0 && !TextUtils.isEmpty(string2)) {
                            o.a().a(this.b, j4, i3, i4, byteArray2, string2, j5);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (this.c.equals("tcp_a19")) {
                    i.d(this.a);
                    return;
                }
                if (this.c.equals("tcp_a11")) {
                    cn.jiguang.ad.d.c("JCoreTCPManager", "resgiter success:" + cn.jiguang.d.a.b(this.b));
                    cn.jiguang.f.a.g(this.b);
                    b bVarA = b.a();
                    Context context = this.b;
                    bVarA.a(context, 0, 0, cn.jiguang.d.a.b(context));
                    e.a(this.b, "on_register", null);
                    return;
                }
                if (this.c.equals("tcp_a10")) {
                    i.e(this.a);
                    return;
                }
                if (this.c.equals("tcp_a9")) {
                    i.a(this.a, this.b);
                    return;
                }
                if (this.c.equals("tcp_a8")) {
                    i.b(this.a, this.b);
                    return;
                }
                if (this.c.equals("tcp_a2")) {
                    g.a().a(this.b, false);
                    this.a.a(this.d);
                    return;
                }
                if (this.c.equals("tcp_a13")) {
                    cn.jiguang.ad.d.c("JCoreTCPManager", "resgiter failed");
                    Bundle bundle2 = this.d;
                    if (bundle2 != null) {
                        int i5 = bundle2.getInt("resCode", 0);
                        this.a.c = i5;
                        Context context2 = this.b;
                        String strA = cn.jiguang.ao.c.a(i5);
                        cn.jiguang.ad.d.i("ConnectingHelper", "Register Failed with server error - code:" + i5);
                        if (!TextUtils.isEmpty(strA)) {
                            cn.jiguang.ad.d.g("ConnectingHelper", "Local error description: " + strA);
                        }
                        b.a().a(context2, 0, i5, strA);
                        String strA2 = cn.jiguang.ac.a.a(context2);
                        if (i5 != 11) {
                            if (i5 == 1012) {
                                cn.jiguang.ah.c.a(context2);
                                return;
                            }
                            if (i5 != 10001) {
                                switch (i5) {
                                    case 1005:
                                        str = "包名: " + context2.getPackageName() + " 与 AppKey:" + strA2 + "不匹配";
                                        break;
                                    case 1006:
                                        str = "包名: " + context2.getPackageName() + " 不存在";
                                        break;
                                    case 1007:
                                        cn.jiguang.ad.d.d("ConnectingHelper", "IMEI is duplicated reported by server. Give up now. ");
                                        return;
                                    case PointerIconCompat.TYPE_TEXT /* 1008 */:
                                        str = " AppKey:" + strA2 + " 是无效的AppKey,请确认与JIGUANG web端的AppKey一致";
                                        break;
                                    case PointerIconCompat.TYPE_VERTICAL_TEXT /* 1009 */:
                                        str = " AppKey:" + strA2 + " 非android AppKey";
                                        break;
                                    default:
                                        cn.jiguang.ad.d.d("ConnectingHelper", "Unhandled server response error code - " + i5);
                                        return;
                                }
                            } else {
                                str = " 未在manifest中配置AppKey";
                            }
                            cn.jiguang.f.a.a(context2, str, -1);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (this.c.equals("tcp_a12")) {
                    cn.jiguang.ad.d.c("JCoreTCPManager", "login failed");
                    Bundle bundle3 = this.d;
                    if (bundle3 != null) {
                        i.b(this.a, bundle3.getInt("resCode", 0));
                        return;
                    }
                    return;
                }
                if (!this.c.equals("tcp_a14")) {
                    if (this.c.equals("tcp_a15")) {
                        this.a.b(this.d);
                        return;
                    }
                    if (this.c.equals("tcp_a16")) {
                        this.a.c(this.d);
                        return;
                    }
                    if (this.c.equals("tcp_a17")) {
                        i.f(this.a);
                        return;
                    }
                    if (this.c.equals("tcp_a18")) {
                        i.g(this.a);
                        return;
                    }
                    if (this.c.equals("tcp_a6")) {
                        Bundle bundle4 = this.d;
                        if (bundle4 != null) {
                            long j6 = bundle4.getLong("rid", -1L);
                            if (j6 > 0) {
                                o.a().a(this.b, j6);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (this.c.equals("tcp_a7")) {
                        Bundle bundle5 = this.d;
                        if (bundle5 != null) {
                            long j7 = bundle5.getLong("rid", -1L);
                            if (j7 > 0) {
                                o.a().a(j7);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (this.c.equals("tcp_a21")) {
                        this.a.e();
                        return;
                    }
                    if (this.c.equals("tcp_a22")) {
                        this.a.b();
                        return;
                    }
                    if (this.c.equals("tcp_a23")) {
                        i.c(this.a, this.b);
                        return;
                    } else {
                        if (this.c.equals("tcp_a24")) {
                            this.a.f();
                            this.a.c = 0;
                            i.c(this.a, 0);
                            this.a.h();
                            return;
                        }
                        return;
                    }
                }
                iVar = this.a;
            }
            iVar.h();
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("JCoreTCPManager", "tcp action failed:" + th.getMessage());
        }
    }
}
