package com.autonavi.ae.gmap.maploader;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public class SingalThread extends Thread {
    private boolean isWaiting;
    public String logTag;
    private final Lock mLock;
    private final Condition mWaiting;

    public SingalThread() {
        ReentrantLock reentrantLock = new ReentrantLock();
        this.mLock = reentrantLock;
        this.mWaiting = reentrantLock.newCondition();
        this.isWaiting = true;
        this.logTag = "SingalThread";
    }

    public void doWait() throws InterruptedException {
        try {
            this.mLock.lock();
            this.isWaiting = true;
            this.mWaiting.await();
        } finally {
            this.mLock.unlock();
        }
    }

    public void doAwake() {
        if (this.isWaiting) {
            try {
                this.mLock.lock();
                this.isWaiting = false;
                this.mWaiting.signal();
            } finally {
                this.mLock.unlock();
            }
        }
    }
}
