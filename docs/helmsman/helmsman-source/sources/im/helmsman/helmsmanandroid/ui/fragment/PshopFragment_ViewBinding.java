package im.helmsman.helmsmanandroid.ui.fragment;

import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class PshopFragment_ViewBinding implements Unbinder {
    private PshopFragment target;

    public PshopFragment_ViewBinding(PshopFragment pshopFragment, View view) {
        this.target = pshopFragment;
        pshopFragment.lvShopCommodity = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_shop_commodity, "field 'lvShopCommodity'", ListView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        PshopFragment pshopFragment = this.target;
        if (pshopFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        pshopFragment.lvShopCommodity = null;
    }
}
