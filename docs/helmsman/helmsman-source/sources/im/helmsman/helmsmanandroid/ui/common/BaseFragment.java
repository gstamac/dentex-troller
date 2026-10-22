package im.helmsman.helmsmanandroid.ui.common;

import android.app.Fragment;
import android.os.Bundle;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseFragment<V extends MvpView, T extends BasePresenter<V>> extends Fragment {
    public T presenter;

    public abstract T initPresenter();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        T t = (T) initPresenter();
        this.presenter = t;
        if (t != null) {
            t.attachView((MvpView) this);
        }
    }

    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        T t = this.presenter;
        if (t != null) {
            t.detachView();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
    }
}
