package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.SearchView;

/* JADX INFO: loaded from: classes2.dex */
public class MyFollowingRouteActivity_ViewBinding implements Unbinder {
    private MyFollowingRouteActivity target;

    public MyFollowingRouteActivity_ViewBinding(MyFollowingRouteActivity myFollowingRouteActivity) {
        this(myFollowingRouteActivity, myFollowingRouteActivity.getWindow().getDecorView());
    }

    public MyFollowingRouteActivity_ViewBinding(MyFollowingRouteActivity myFollowingRouteActivity, View view) {
        this.target = myFollowingRouteActivity;
        myFollowingRouteActivity.ivMyfollowingRouteSearch = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_myfollowing_route_search, "field 'ivMyfollowingRouteSearch'", ImageView.class);
        myFollowingRouteActivity.ivMyfollowingRouteDelete = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_myfollowing_route_delete, "field 'ivMyfollowingRouteDelete'", ImageView.class);
        myFollowingRouteActivity.ivMyfollowingRouteEdit = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_myfollowing_route_edit, "field 'ivMyfollowingRouteEdit'", ImageView.class);
        myFollowingRouteActivity.searchLocalrouteSearch = (SearchView) Utils.findRequiredViewAsType(view, R.id.search_localroute_search, "field 'searchLocalrouteSearch'", SearchView.class);
        myFollowingRouteActivity.ivMyfollowingRouteMessage = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_myfollowing_route_Message, "field 'ivMyfollowingRouteMessage'", ImageView.class);
        myFollowingRouteActivity.ivMyfollowingRouteCopy = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_myfollowing_route_copy, "field 'ivMyfollowingRouteCopy'", ImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MyFollowingRouteActivity myFollowingRouteActivity = this.target;
        if (myFollowingRouteActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        myFollowingRouteActivity.ivMyfollowingRouteSearch = null;
        myFollowingRouteActivity.ivMyfollowingRouteDelete = null;
        myFollowingRouteActivity.ivMyfollowingRouteEdit = null;
        myFollowingRouteActivity.searchLocalrouteSearch = null;
        myFollowingRouteActivity.ivMyfollowingRouteMessage = null;
        myFollowingRouteActivity.ivMyfollowingRouteCopy = null;
    }
}
