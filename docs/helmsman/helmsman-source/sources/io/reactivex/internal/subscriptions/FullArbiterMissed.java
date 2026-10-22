package io.reactivex.internal.subscriptions;

import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: compiled from: FullArbiter.java */
/* JADX INFO: loaded from: classes2.dex */
class FullArbiterMissed extends FullArbiterPad1 {
    final AtomicLong missedRequested = new AtomicLong();

    FullArbiterMissed() {
    }
}
