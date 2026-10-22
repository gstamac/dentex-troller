package im.helmsman.helmsmanandroid.ui.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;

/* JADX INFO: loaded from: classes2.dex */
public class CommodityDetailActivity extends Mvp2BaseActivity {
    private String link;
    private String name;
    private int price;
    private int[] res;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public BasePresenter initPresenter() {
        return null;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        this.name = intent.getStringExtra("name");
        this.price = intent.getIntExtra("price", 9999999);
        this.res = intent.getIntArrayExtra("imageres");
        this.link = intent.getStringExtra("link");
        setContentView(R.layout.activity_commodity_detail);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
        boolean z = isPad;
        FragmentTransaction fragmentTransactionBeginTransaction = getFragmentManager().beginTransaction();
        fragmentTransactionBeginTransaction.replace(R.id.activity_commodity_detail, null);
        fragmentTransactionBeginTransaction.commit();
    }
}
