package com.qiniu.android.storage;

/* JADX INFO: loaded from: classes2.dex */
public interface Recorder {
    void del(String str);

    byte[] get(String str);

    void set(String str, byte[] bArr);
}
