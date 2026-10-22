package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class AutoConnectBleActivity_ViewBinding implements Unbinder {
    private AutoConnectBleActivity target;
    private View view7f080293;
    private View view7f0802aa;

    public AutoConnectBleActivity_ViewBinding(AutoConnectBleActivity autoConnectBleActivity) {
        this(autoConnectBleActivity, autoConnectBleActivity.getWindow().getDecorView());
    }

    public AutoConnectBleActivity_ViewBinding(final AutoConnectBleActivity autoConnectBleActivity, View view) {
        this.target = autoConnectBleActivity;
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.rel_search_wifi, "field 'relSearchWifi' and method 'onClick'");
        autoConnectBleActivity.relSearchWifi = (RelativeLayout) Utils.castView(viewFindRequiredView, R.id.rel_search_wifi, "field 'relSearchWifi'", RelativeLayout.class);
        this.view7f0802aa = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AutoConnectBleActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                autoConnectBleActivity.onClick(view2);
            }
        });
        autoConnectBleActivity.titleBar = (TitleBar) Utils.findRequiredViewAsType(view, R.id.titleBar, "field 'titleBar'", TitleBar.class);
        autoConnectBleActivity.switchCheckAutoConnect = (Switch) Utils.findRequiredViewAsType(view, R.id.switch_check_auto_connect, "field 'switchCheckAutoConnect'", Switch.class);
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.rel_alias_bluetooth, "field 'relAliasBluetooth' and method 'onClick'");
        autoConnectBleActivity.relAliasBluetooth = (RelativeLayout) Utils.castView(viewFindRequiredView2, R.id.rel_alias_bluetooth, "field 'relAliasBluetooth'", RelativeLayout.class);
        this.view7f080293 = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AutoConnectBleActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                autoConnectBleActivity.onClick(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        AutoConnectBleActivity autoConnectBleActivity = this.target;
        if (autoConnectBleActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        autoConnectBleActivity.relSearchWifi = null;
        autoConnectBleActivity.titleBar = null;
        autoConnectBleActivity.switchCheckAutoConnect = null;
        autoConnectBleActivity.relAliasBluetooth = null;
        this.view7f0802aa.setOnClickListener(null);
        this.view7f0802aa = null;
        this.view7f080293.setOnClickListener(null);
        this.view7f080293 = null;
    }
}
