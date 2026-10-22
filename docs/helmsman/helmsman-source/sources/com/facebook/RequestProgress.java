package com.facebook;

import android.os.Handler;

/* JADX INFO: loaded from: classes.dex */
class RequestProgress {
    private final Handler callbackHandler;
    private long lastReportedProgress;
    private long maxProgress;
    private long progress;
    private final GraphRequest request;
    private final long threshold = FacebookSdk.getOnProgressThreshold();

    RequestProgress(Handler handler, GraphRequest graphRequest) {
        this.request = graphRequest;
        this.callbackHandler = handler;
    }

    long getProgress() {
        return this.progress;
    }

    long getMaxProgress() {
        return this.maxProgress;
    }

    void addProgress(long j) {
        long j2 = this.progress + j;
        this.progress = j2;
        if (j2 >= this.lastReportedProgress + this.threshold || j2 >= this.maxProgress) {
            reportProgress();
        }
    }

    void addToMax(long j) {
        this.maxProgress += j;
    }

    void reportProgress() {
        if (this.progress > this.lastReportedProgress) {
            GraphRequest.Callback callback = this.request.getCallback();
            final long j = this.maxProgress;
            if (j <= 0 || !(callback instanceof GraphRequest.OnProgressCallback)) {
                return;
            }
            final long j2 = this.progress;
            final GraphRequest.OnProgressCallback onProgressCallback = (GraphRequest.OnProgressCallback) callback;
            Handler handler = this.callbackHandler;
            if (handler == null) {
                onProgressCallback.onProgress(j2, j);
            } else {
                handler.post(new Runnable() { // from class: com.facebook.RequestProgress.1
                    @Override // java.lang.Runnable
                    public void run() {
                        onProgressCallback.onProgress(j2, j);
                    }
                });
            }
            this.lastReportedProgress = this.progress;
        }
    }
}
