package im.helmsman.helmsmanandroid.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class LocalRouteFragment_ViewBinding implements Unbinder {
    private LocalRouteFragment target;
    private View view7f08018a;

    public LocalRouteFragment_ViewBinding(final LocalRouteFragment localRouteFragment, View view) {
        this.target = localRouteFragment;
        localRouteFragment.lvEdit = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_edit, "field 'lvEdit'", ListView.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_addroute, "field 'ivAddroute' and method 'onClick'");
        localRouteFragment.ivAddroute = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_addroute, "field 'ivAddroute'", ImageView.class);
        this.view7f08018a = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.LocalRouteFragment_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                localRouteFragment.onClick();
            }
        });
        localRouteFragment.swipeRefresh = (PtrClassicFrameLayout) Utils.findRequiredViewAsType(view, R.id.swipe_refresh, "field 'swipeRefresh'", PtrClassicFrameLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        LocalRouteFragment localRouteFragment = this.target;
        if (localRouteFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        localRouteFragment.lvEdit = null;
        localRouteFragment.ivAddroute = null;
        localRouteFragment.swipeRefresh = null;
        this.view7f08018a.setOnClickListener(null);
        this.view7f08018a = null;
    }
}
