package com.tencent.wxop.stat.a;

/* JADX INFO: loaded from: classes2.dex */
public enum f {
    PAGE_VIEW(1),
    SESSION_ENV(2),
    ERROR(3),
    CUSTOM(1000),
    ADDITION(1001),
    MONITOR_STAT(1002),
    MTA_GAME_USER(1003),
    NETWORK_MONITOR(1004),
    NETWORK_DETECTOR(1005);

    private int j;

    f(int i) {
        this.j = i;
    }

    public final int a() {
        return this.j;
    }
}
