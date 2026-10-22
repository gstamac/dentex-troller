package org.litepal.crud.async;

import org.litepal.crud.callback.UpdateOrDeleteCallback;

/* JADX INFO: loaded from: classes2.dex */
public class UpdateOrDeleteExecutor extends AsyncExecutor {
    private UpdateOrDeleteCallback cb;

    public void listen(UpdateOrDeleteCallback updateOrDeleteCallback) {
        this.cb = updateOrDeleteCallback;
        execute();
    }

    public UpdateOrDeleteCallback getListener() {
        return this.cb;
    }
}
