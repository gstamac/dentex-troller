package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.tt.whorlviewlibrary.WhorlView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.SearchView;

/* JADX INFO: loaded from: classes2.dex */
public class AddFrinedsActivity_ViewBinding implements Unbinder {
    private AddFrinedsActivity target;

    public AddFrinedsActivity_ViewBinding(AddFrinedsActivity addFrinedsActivity) {
        this(addFrinedsActivity, addFrinedsActivity.getWindow().getDecorView());
    }

    public AddFrinedsActivity_ViewBinding(AddFrinedsActivity addFrinedsActivity, View view) {
        this.target = addFrinedsActivity;
        addFrinedsActivity.searchView = (SearchView) Utils.findRequiredViewAsType(view, R.id.seachview_addFriends_searchfiends_, "field 'searchView'", SearchView.class);
        addFrinedsActivity.lvFriendList = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_addfriend_friendlist, "field 'lvFriendList'", ListView.class);
        addFrinedsActivity.ptrAddfriendsRefresh = (PtrClassicFrameLayout) Utils.findRequiredViewAsType(view, R.id.ptr_addfriends_refresh, "field 'ptrAddfriendsRefresh'", PtrClassicFrameLayout.class);
        addFrinedsActivity.progressbar = (WhorlView) Utils.findRequiredViewAsType(view, R.id.whorlview_addfriend_progressbar, "field 'progressbar'", WhorlView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        AddFrinedsActivity addFrinedsActivity = this.target;
        if (addFrinedsActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        addFrinedsActivity.searchView = null;
        addFrinedsActivity.lvFriendList = null;
        addFrinedsActivity.ptrAddfriendsRefresh = null;
        addFrinedsActivity.progressbar = null;
    }
}
