package org.litepal.crud.async;

import org.litepal.crud.callback.FindMultiCallback;

/* JADX INFO: loaded from: classes2.dex */
public class FindMultiExecutor extends AsyncExecutor {
    private FindMultiCallback cb;

    public void listen(FindMultiCallback findMultiCallback) {
        this.cb = findMultiCallback;
        execute();
    }

    public FindMultiCallback getListener() {
        return this.cb;
    }
}
