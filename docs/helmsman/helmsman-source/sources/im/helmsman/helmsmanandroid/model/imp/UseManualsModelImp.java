package im.helmsman.helmsmanandroid.model.imp;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.model.UseManualsModel;
import im.helmsman.helmsmanandroid.presenter.UseManualsPresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnDownLoadManualsListener;
import java.io.File;
import okhttp3.Call;

/* JADX INFO: loaded from: classes2.dex */
public class UseManualsModelImp implements UseManualsModel {
    private OnDownLoadManualsListener listener;

    public UseManualsModelImp(OnDownLoadManualsListener onDownLoadManualsListener) {
        this.listener = onDownLoadManualsListener;
    }

    @Override // im.helmsman.helmsmanandroid.model.UseManualsModel
    public void downloadManual(final int i, String str, final String str2) {
        File file = new File(UseManualsPresenter.MANUAL_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        OkHttpUtils.get().url(str2).build().execute(new FileCallBack(UseManualsPresenter.MANUAL_PATH, str) { // from class: im.helmsman.helmsmanandroid.model.imp.UseManualsModelImp.1
            @Override // com.zhy.http.okhttp.callback.Callback
            public void inProgress(float f, long j, int i2) {
                super.inProgress(f, j, i2);
                UseManualsModelImp.this.listener.onDownLoadProgress((int) (f * 100.0f));
            }

            @Override // com.zhy.http.okhttp.callback.Callback
            public void onError(Call call, Exception exc, int i2) {
                UseManualsModelImp.this.listener.onDownLoadError(new ErrorCode(exc.getMessage()));
            }

            @Override // com.zhy.http.okhttp.callback.Callback
            public void onResponse(File file2, int i2) {
                UseManualsModelImp.this.listener.onDownLoadFinish(i, str2);
            }
        });
    }
}
