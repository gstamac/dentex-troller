package com.autonavi.ae.gmap.maploader;

import com.autonavi.ae.gmap.GLMapEngine;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionManager extends SingalThread {
    private static final int MAX_THREAD_COUNT = 5;
    GLMapEngine mGLMapEngine;
    public boolean threadFlag = true;
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(5);
    private ArrayList<AsMapRequestor> mThreadPoolList = new ArrayList<>();
    private ArrayList<BaseMapLoader> mConnPool = new ArrayList<>();

    public ConnectionManager(GLMapEngine gLMapEngine) {
        this.mGLMapEngine = gLMapEngine;
    }

    public void shutDown() {
        if (this.mConnPool != null) {
            try {
                this.mThreadPool.shutdownNow();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void insertConntionTask(BaseMapLoader baseMapLoader) {
        synchronized (this.mConnPool) {
            try {
                this.mConnPool.add(baseMapLoader);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        try {
            doAwake();
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    private void checkListPool() {
        ArrayList arrayList = new ArrayList();
        int size = this.mThreadPoolList.size();
        for (int i = 0; i < size; i++) {
            AsMapRequestor asMapRequestor = this.mThreadPoolList.get(i);
            BaseMapLoader baseMapLoader = asMapRequestor.mMapLoader;
            if (!baseMapLoader.isRequestValid() || baseMapLoader.hasFinished()) {
                arrayList.add(asMapRequestor);
                baseMapLoader.doCancel();
            }
        }
        this.mThreadPoolList.removeAll(arrayList);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            doAsyncRequest();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void doAsyncRequest() {
        boolean z;
        while (this.threadFlag) {
            checkListPool();
            while (true) {
                if (this.mThreadPoolList.size() <= 5) {
                    synchronized (this.mConnPool) {
                        if (this.mConnPool.size() > 0) {
                            BaseMapLoader baseMapLoaderRemove = this.mConnPool.remove(0);
                            if (baseMapLoaderRemove != null) {
                                AsMapRequestor asMapRequestor = new AsMapRequestor(baseMapLoaderRemove);
                                this.mThreadPoolList.add(asMapRequestor);
                                if (!this.mThreadPool.isShutdown()) {
                                    this.mThreadPool.execute(asMapRequestor);
                                }
                            }
                            if (baseMapLoaderRemove == null) {
                            }
                        } else {
                            this.mGLMapEngine.clearAllRequestTiles();
                        }
                    }
                    z = false;
                    break;
                }
                z = true;
                break;
            }
            if (z) {
                sleep(30L);
            } else if (this.threadFlag) {
                doWait();
            }
        }
    }
}
