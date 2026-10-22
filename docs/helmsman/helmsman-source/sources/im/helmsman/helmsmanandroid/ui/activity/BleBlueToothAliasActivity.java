package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.BleBlueToothConnected;
import im.helmsman.helmsmanandroid.presenter.BleBlueToothAliasPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.BleBlueToothAliasView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.TitleBar;
import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class BleBlueToothAliasActivity extends Mvp2BaseActivity<BleBlueToothAliasView, BleBlueToothAliasPresenter> implements TextWatcher, BleBlueToothAliasView {
    private String address;

    @BindView(R.id.et_blebluetooth_alias)
    EditText etBlebluetoothAlias;

    @BindView(R.id.iv_alalias_tick)
    ImageView ivAlaliasTick;
    private String name;

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @BindView(R.id.tv_text_lenght)
    TextView tvTextLenght;

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ble_blue_tooth_alias);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        this.address = intent.getStringExtra("address");
        this.name = intent.getStringExtra("name");
        initEvent();
        initView();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public BleBlueToothAliasPresenter initPresenter() {
        return new BleBlueToothAliasPresenter();
    }

    private void initView() {
        this.etBlebluetoothAlias.setText(this.name);
        this.etBlebluetoothAlias.setHint(this.name);
    }

    private void initEvent() {
        this.etBlebluetoothAlias.addTextChangedListener(this);
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence.length() > 0) {
            this.ivAlaliasTick.setVisibility(0);
        } else {
            this.ivAlaliasTick.setVisibility(8);
        }
        this.tvTextLenght.setText(charSequence.length() + " / 18");
    }

    @OnClick({R.id.iv_alalias_tick})
    public void onClick() {
        List listFind = DataSupport.where("address=?", this.address).find(BleBlueToothConnected.class);
        if (listFind.size() > 0) {
            BleBlueToothConnected bleBlueToothConnected = (BleBlueToothConnected) listFind.get(0);
            bleBlueToothConnected.setAlais(this.etBlebluetoothAlias.getText().toString());
            bleBlueToothConnected.save();
            ViewUtils.ShowToast(R.string.set_alias_success);
        }
        finishActivity(AutoConnectBleActivity.class);
        finish();
    }
}
