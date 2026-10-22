package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
class aq {
    private static volatile long f;
    private com.tencent.wxop.stat.a.e a;
    private StatReportStrategy b;
    private boolean c;
    private Context d;
    private long e = System.currentTimeMillis();

    public aq(com.tencent.wxop.stat.a.e eVar) {
        this.b = null;
        this.c = false;
        this.d = null;
        this.a = eVar;
        this.b = StatConfig.getStatSendStrategy();
        this.c = eVar.f();
        this.d = eVar.e();
    }

    private void a(h hVar) {
        i.b(StatServiceImpl.t).a(this.a, hVar);
    }

    private void b() {
        if (this.a.d() != null && this.a.d().isSendImmediately()) {
            this.b = StatReportStrategy.INSTANT;
        }
        if (StatConfig.j && a.a(StatServiceImpl.t).e()) {
            this.b = StatReportStrategy.INSTANT;
        }
        if (StatConfig.isDebugEnable()) {
            StatServiceImpl.q.i("strategy=" + this.b.name());
        }
        switch (ag.a[this.b.ordinal()]) {
            case 1:
                c();
                break;
            case 2:
                au.a(this.d).a(this.a, (h) null, this.c, false);
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("PERIOD currTime=" + this.e + ",nextPeriodSendTs=" + StatServiceImpl.c + ",difftime=" + (StatServiceImpl.c - this.e));
                }
                if (StatServiceImpl.c == 0) {
                    StatServiceImpl.c = com.tencent.wxop.stat.common.p.a(this.d, "last_period_ts", 0L);
                    if (this.e > StatServiceImpl.c) {
                        StatServiceImpl.e(this.d);
                    }
                    long sendPeriodMinutes = this.e + ((long) (StatConfig.getSendPeriodMinutes() * 60 * 1000));
                    if (StatServiceImpl.c > sendPeriodMinutes) {
                        StatServiceImpl.c = sendPeriodMinutes;
                    }
                    d.a(this.d).a();
                }
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("PERIOD currTime=" + this.e + ",nextPeriodSendTs=" + StatServiceImpl.c + ",difftime=" + (StatServiceImpl.c - this.e));
                }
                if (this.e > StatServiceImpl.c) {
                    StatServiceImpl.e(this.d);
                }
                break;
            case 3:
            case 4:
                au.a(this.d).a(this.a, (h) null, this.c, false);
                break;
            case 5:
                au.a(this.d).a(this.a, (h) new ar(this), this.c, true);
                break;
            case 6:
                if (a.a(StatServiceImpl.t).c() != 1) {
                    au.a(this.d).a(this.a, (h) null, this.c, false);
                } else {
                    c();
                }
                break;
            case 7:
                if (com.tencent.wxop.stat.common.k.e(this.d)) {
                    a(new as(this));
                }
                break;
            default:
                StatServiceImpl.q.error("Invalid stat strategy:" + StatConfig.getStatSendStrategy());
                break;
        }
    }

    private void c() {
        if (au.b().a <= 0 || !StatConfig.l) {
            a(new at(this));
        } else {
            au.b().a(this.a, (h) null, this.c, true);
            au.b().a(-1);
        }
    }

    private boolean d() {
        if (StatConfig.h <= 0) {
            return false;
        }
        if (this.e > StatServiceImpl.h) {
            StatServiceImpl.g.clear();
            long unused = StatServiceImpl.h = this.e + StatConfig.i;
            if (StatConfig.isDebugEnable()) {
                StatServiceImpl.q.i("clear methodsCalledLimitMap, nextLimitCallClearTime=" + StatServiceImpl.h);
            }
        }
        Integer numValueOf = Integer.valueOf(this.a.a().a());
        Integer num = (Integer) StatServiceImpl.g.get(numValueOf);
        if (num == null) {
            StatServiceImpl.g.put(numValueOf, 1);
            return false;
        }
        StatServiceImpl.g.put(numValueOf, Integer.valueOf(num.intValue() + 1));
        if (num.intValue() <= StatConfig.h) {
            return false;
        }
        if (StatConfig.isDebugEnable()) {
            StatServiceImpl.q.e("event " + this.a.g() + " was discard, cause of called limit, current:" + num + ", limit:" + StatConfig.h + ", period:" + StatConfig.i + " ms");
        }
        return true;
    }

    public void a() {
        if (d()) {
            return;
        }
        if (StatConfig.m > 0 && this.e >= f) {
            StatServiceImpl.flushDataToDB(this.d);
            f = this.e + StatConfig.n;
            if (StatConfig.isDebugEnable()) {
                StatServiceImpl.q.i("nextFlushTime=" + f);
            }
        }
        if (!a.a(this.d).f()) {
            au.a(this.d).a(this.a, (h) null, this.c, false);
            return;
        }
        if (StatConfig.isDebugEnable()) {
            StatServiceImpl.q.i("sendFailedCount=" + StatServiceImpl.a);
        }
        if (!StatServiceImpl.a()) {
            b();
            return;
        }
        au.a(this.d).a(this.a, (h) null, this.c, false);
        if (this.e - StatServiceImpl.b > 1800000) {
            StatServiceImpl.d(this.d);
        }
    }
}
