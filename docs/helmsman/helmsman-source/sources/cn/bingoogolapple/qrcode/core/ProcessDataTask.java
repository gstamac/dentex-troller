package cn.bingoogolapple.qrcode.core;

import android.hardware.Camera;
import android.os.AsyncTask;

/* JADX INFO: loaded from: classes.dex */
public class ProcessDataTask extends AsyncTask<Void, Void, String> {
    private Camera mCamera;
    private byte[] mData;
    private Delegate mDelegate;

    public interface Delegate {
        String processData(byte[] bArr, int i, int i2, boolean z);
    }

    public ProcessDataTask(Camera camera, byte[] bArr, Delegate delegate) {
        this.mCamera = camera;
        this.mData = bArr;
        this.mDelegate = delegate;
    }

    public ProcessDataTask perform() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        return this;
    }

    public void cancelTask() {
        if (getStatus() != AsyncTask.Status.FINISHED) {
            cancel(true);
        }
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        super.onCancelled();
        this.mDelegate = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public String doInBackground(Void... voidArr) {
        Camera.Size previewSize = this.mCamera.getParameters().getPreviewSize();
        int i = previewSize.width;
        int i2 = previewSize.height;
        byte[] bArr = new byte[this.mData.length];
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                bArr[(((i4 * i2) + i2) - i3) - 1] = this.mData[(i3 * i) + i4];
            }
        }
        try {
            try {
                Delegate delegate = this.mDelegate;
                if (delegate == null) {
                    return null;
                }
                return delegate.processData(bArr, i2, i, false);
            } catch (Exception unused) {
                return this.mDelegate.processData(bArr, i2, i, true);
            }
        } catch (Exception unused2) {
            return null;
        }
        return null;
    }
}
