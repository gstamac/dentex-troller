package im.helmsman.helmsmanandroid.ui.common;

import android.os.Bundle;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MvpBaseActivity<V extends MvpView, T extends BasePresenter<V>> extends BaseActivity {
    public T presenter;

    public abstract void initFragment();

    public abstract T initPresenter();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // im.helmsman.helmsmanandroid.ui.common.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        T t = (T) initPresenter();
        this.presenter = t;
        if (t != null) {
            t.attachView((MvpView) this);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(int i) {
        super.setContentView(i);
        initFragment();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        T t = this.presenter;
        if (t != null) {
            t.detachView();
        }
    }
}
