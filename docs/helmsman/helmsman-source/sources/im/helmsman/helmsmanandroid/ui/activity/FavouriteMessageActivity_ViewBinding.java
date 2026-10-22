package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class FavouriteMessageActivity_ViewBinding implements Unbinder {
    private FavouriteMessageActivity target;

    public FavouriteMessageActivity_ViewBinding(FavouriteMessageActivity favouriteMessageActivity) {
        this(favouriteMessageActivity, favouriteMessageActivity.getWindow().getDecorView());
    }

    public FavouriteMessageActivity_ViewBinding(FavouriteMessageActivity favouriteMessageActivity, View view) {
        this.target = favouriteMessageActivity;
        favouriteMessageActivity.lvFavouriteMessageList = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_favourite_message_list, "field 'lvFavouriteMessageList'", ListView.class);
        favouriteMessageActivity.swipeRefresh = (PtrClassicFrameLayout) Utils.findRequiredViewAsType(view, R.id.swipe_refresh, "field 'swipeRefresh'", PtrClassicFrameLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        FavouriteMessageActivity favouriteMessageActivity = this.target;
        if (favouriteMessageActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        favouriteMessageActivity.lvFavouriteMessageList = null;
        favouriteMessageActivity.swipeRefresh = null;
    }
}
