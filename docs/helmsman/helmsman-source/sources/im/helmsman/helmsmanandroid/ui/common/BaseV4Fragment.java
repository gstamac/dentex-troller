package im.helmsman.helmsmanandroid.ui.common;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseV4Fragment<V extends MvpView, T extends BasePresenter<V>> extends Fragment {
    public T presenter;

    public abstract T initPresenter();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        T t = (T) initPresenter();
        this.presenter = t;
        if (t != null) {
            t.attachView((MvpView) this);
        }
    }

    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        T t = this.presenter;
        if (t != null) {
            t.detachView();
        }
    }
}
