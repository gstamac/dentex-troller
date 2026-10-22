package im.helmsman.helmsmanandroid.ui.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.fragment.PshopFragment;

/* JADX INFO: loaded from: classes2.dex */
public class ShopActivity extends Mvp2BaseActivity {
    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public BasePresenter initPresenter() {
        return null;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_shop);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
        FragmentTransaction fragmentTransactionBeginTransaction = getFragmentManager().beginTransaction();
        if (!isPad) {
            new PshopFragment();
        }
        fragmentTransactionBeginTransaction.replace(R.id.activity_shop_root, new PshopFragment());
        fragmentTransactionBeginTransaction.commit();
    }
}
