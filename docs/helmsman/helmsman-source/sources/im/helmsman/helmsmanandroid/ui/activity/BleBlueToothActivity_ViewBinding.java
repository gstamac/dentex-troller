package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class BleBlueToothActivity_ViewBinding implements Unbinder {
    private BleBlueToothActivity target;
    private View view7f08018f;
    private View view7f080295;

    public BleBlueToothActivity_ViewBinding(BleBlueToothActivity bleBlueToothActivity) {
        this(bleBlueToothActivity, bleBlueToothActivity.getWindow().getDecorView());
    }

    public BleBlueToothActivity_ViewBinding(final BleBlueToothActivity bleBlueToothActivity, View view) {
        this.target = bleBlueToothActivity;
        bleBlueToothActivity.ivBleBluetoothSwipeing = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_ble_bluetooth_swipeing, "field 'ivBleBluetoothSwipeing'", ImageView.class);
        bleBlueToothActivity.tvBleBluetoothCount = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_ble_bluetooth_count, "field 'tvBleBluetoothCount'", TextView.class);
        bleBlueToothActivity.lvBleBluetooth = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_ble_bluetooth, "field 'lvBleBluetooth'", ListView.class);
        bleBlueToothActivity.swipeBleBluetooth = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.swipe_ble_bluetooth, "field 'swipeBleBluetooth'", SwipeRefreshLayout.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.rel_blebluetooth_permission, "field 'mRelPermissionSetting' and method 'onClick'");
        bleBlueToothActivity.mRelPermissionSetting = (RelativeLayout) Utils.castView(viewFindRequiredView, R.id.rel_blebluetooth_permission, "field 'mRelPermissionSetting'", RelativeLayout.class);
        this.view7f080295 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                bleBlueToothActivity.onClick(view2);
            }
        });
        bleBlueToothActivity.mTvPermissionHint = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_blebluetooth_permission_miss_hint, "field 'mTvPermissionHint'", TextView.class);
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.iv_ble_connect_delete, "field 'ivBleConnectDelete' and method 'onClick'");
        bleBlueToothActivity.ivBleConnectDelete = (ImageView) Utils.castView(viewFindRequiredView2, R.id.iv_ble_connect_delete, "field 'ivBleConnectDelete'", ImageView.class);
        this.view7f08018f = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                bleBlueToothActivity.onClick(view2);
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        BleBlueToothActivity bleBlueToothActivity = this.target;
        if (bleBlueToothActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        bleBlueToothActivity.ivBleBluetoothSwipeing = null;
        bleBlueToothActivity.tvBleBluetoothCount = null;
        bleBlueToothActivity.lvBleBluetooth = null;
        bleBlueToothActivity.swipeBleBluetooth = null;
        bleBlueToothActivity.mRelPermissionSetting = null;
        bleBlueToothActivity.mTvPermissionHint = null;
        bleBlueToothActivity.ivBleConnectDelete = null;
        this.view7f080295.setOnClickListener(null);
        this.view7f080295 = null;
        this.view7f08018f.setOnClickListener(null);
        this.view7f08018f = null;
    }
}
