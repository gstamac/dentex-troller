package com.tencent.wxop.stat;

/* JADX INFO: loaded from: classes2.dex */
class ar implements h {
    final /* synthetic */ aq a;

    ar(aq aqVar) {
        this.a = aqVar;
    }

    @Override // com.tencent.wxop.stat.h
    public void a() {
        StatServiceImpl.c();
        if (au.b().a() >= StatConfig.getMaxBatchReportCount()) {
            au.b().a(StatConfig.getMaxBatchReportCount());
        }
    }

    @Override // com.tencent.wxop.stat.h
    public void b() {
        StatServiceImpl.d();
    }
}
