package com.qiniu.android.storage;

import android.os.Looper;
import android.util.Log;
import com.qiniu.android.http.Client;
import com.qiniu.android.utils.AndroidNetwork;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public final class UploadOptions {
    final UpCancellationSignal cancellationSignal;
    final boolean checkCrc;
    final String mimeType;
    final NetReadyHandler netReadyHandler;
    final Map<String, String> params;
    final UpProgressHandler progressHandler;

    public UploadOptions(Map<String, String> map, String str, boolean z, UpProgressHandler upProgressHandler, UpCancellationSignal upCancellationSignal) {
        this(map, str, z, upProgressHandler, upCancellationSignal, null);
    }

    public UploadOptions(Map<String, String> map, String str, boolean z, UpProgressHandler upProgressHandler, UpCancellationSignal upCancellationSignal, NetReadyHandler netReadyHandler) {
        this.params = filterParam(map);
        this.mimeType = mime(str);
        this.checkCrc = z;
        this.progressHandler = upProgressHandler == null ? new UpProgressHandler() { // from class: com.qiniu.android.storage.UploadOptions.1
            @Override // com.qiniu.android.storage.UpProgressHandler
            public void progress(String str2, double d) {
                Log.d("Qiniu.UploadProgress", "" + d);
            }
        } : upProgressHandler;
        this.cancellationSignal = upCancellationSignal == null ? new UpCancellationSignal() { // from class: com.qiniu.android.storage.UploadOptions.2
            @Override // com.qiniu.android.http.CancellationHandler
            public boolean isCancelled() {
                return false;
            }
        } : upCancellationSignal;
        this.netReadyHandler = netReadyHandler == null ? new NetReadyHandler() { // from class: com.qiniu.android.storage.UploadOptions.3
            @Override // com.qiniu.android.storage.NetReadyHandler
            public void waitReady() {
                if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                    return;
                }
                for (int i = 0; i < 6; i++) {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (AndroidNetwork.isNetWorkReady()) {
                        return;
                    }
                }
            }
        } : netReadyHandler;
    }

    private static Map<String, String> filterParam(Map<String, String> map) {
        HashMap map2 = new HashMap();
        if (map == null) {
            return map2;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().startsWith("x:") && entry.getValue() != null && !entry.getValue().equals("")) {
                map2.put(entry.getKey(), entry.getValue());
            }
        }
        return map2;
    }

    static UploadOptions defaultOptions() {
        return new UploadOptions(null, null, false, null, null);
    }

    private static String mime(String str) {
        return (str == null || str.equals("")) ? Client.DefaultMime : str;
    }
}
