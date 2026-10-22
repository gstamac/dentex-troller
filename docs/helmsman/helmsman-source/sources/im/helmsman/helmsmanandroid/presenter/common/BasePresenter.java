package im.helmsman.helmsmanandroid.presenter.common;

import android.content.Intent;
import android.util.Log;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public class BasePresenter<T extends MvpView> {
    protected T view;

    public void attachView(T t) {
        this.view = t;
        if (t != null) {
            Log.d("presenter_attachView", t.getClass().getName());
        }
    }

    public void detachView() {
        T t = this.view;
        if (t != null) {
            Log.d("presenter_detachView", t.getClass().getName());
        }
        this.view = null;
    }

    public void startActivity(Intent intent) {
        Base2Activity.getTopActivity().startActivity(intent);
    }
}
