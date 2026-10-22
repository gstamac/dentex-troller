package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class BleBlueToothAliasActivity_ViewBinding implements Unbinder {
    private BleBlueToothAliasActivity target;
    private View view7f08018b;

    public BleBlueToothAliasActivity_ViewBinding(BleBlueToothAliasActivity bleBlueToothAliasActivity) {
        this(bleBlueToothAliasActivity, bleBlueToothAliasActivity.getWindow().getDecorView());
    }

    public BleBlueToothAliasActivity_ViewBinding(final BleBlueToothAliasActivity bleBlueToothAliasActivity, View view) {
        this.target = bleBlueToothAliasActivity;
        bleBlueToothAliasActivity.titleBar = (TitleBar) Utils.findRequiredViewAsType(view, R.id.titleBar, "field 'titleBar'", TitleBar.class);
        bleBlueToothAliasActivity.etBlebluetoothAlias = (EditText) Utils.findRequiredViewAsType(view, R.id.et_blebluetooth_alias, "field 'etBlebluetoothAlias'", EditText.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_alalias_tick, "field 'ivAlaliasTick' and method 'onClick'");
        bleBlueToothAliasActivity.ivAlaliasTick = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_alalias_tick, "field 'ivAlaliasTick'", ImageView.class);
        this.view7f08018b = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothAliasActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                bleBlueToothAliasActivity.onClick();
            }
        });
        bleBlueToothAliasActivity.tvTextLenght = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_text_lenght, "field 'tvTextLenght'", TextView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        BleBlueToothAliasActivity bleBlueToothAliasActivity = this.target;
        if (bleBlueToothAliasActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        bleBlueToothAliasActivity.titleBar = null;
        bleBlueToothAliasActivity.etBlebluetoothAlias = null;
        bleBlueToothAliasActivity.ivAlaliasTick = null;
        bleBlueToothAliasActivity.tvTextLenght = null;
        this.view7f08018b.setOnClickListener(null);
        this.view7f08018b = null;
    }
}
