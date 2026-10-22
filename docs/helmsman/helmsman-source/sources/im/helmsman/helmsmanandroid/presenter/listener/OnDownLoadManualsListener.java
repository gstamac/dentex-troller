package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.ErrorCode;

/* JADX INFO: loaded from: classes2.dex */
public interface OnDownLoadManualsListener {
    void onDownLoadError(ErrorCode errorCode);

    void onDownLoadFinish(int i, String str);

    void onDownLoadProgress(int i);
}
