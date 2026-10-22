package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.SearchView;
import im.helmsman.helmsmanandroid.view.TitleBar;
import im.helmsman.helmsmanandroid.view.WaveSideBar;

/* JADX INFO: loaded from: classes2.dex */
public class AtFriendsActivity_ViewBinding implements Unbinder {
    private AtFriendsActivity target;
    private View view7f0801d7;

    public AtFriendsActivity_ViewBinding(AtFriendsActivity atFriendsActivity) {
        this(atFriendsActivity, atFriendsActivity.getWindow().getDecorView());
    }

    public AtFriendsActivity_ViewBinding(final AtFriendsActivity atFriendsActivity, View view) {
        this.target = atFriendsActivity;
        atFriendsActivity.wsbAtFriendSidebar = (WaveSideBar) Utils.findRequiredViewAsType(view, R.id.wsb_at_friend_sidebar, "field 'wsbAtFriendSidebar'", WaveSideBar.class);
        atFriendsActivity.lvAtfriendContactList = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_atfriend_contact_list, "field 'lvAtfriendContactList'", ListView.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_search, "field 'ivSearch' and method 'onClick'");
        atFriendsActivity.ivSearch = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_search, "field 'ivSearch'", ImageView.class);
        this.view7f0801d7 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AtFriendsActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                atFriendsActivity.onClick();
            }
        });
        atFriendsActivity.svSearchContact = (SearchView) Utils.findRequiredViewAsType(view, R.id.sv_search_contact, "field 'svSearchContact'", SearchView.class);
        atFriendsActivity.titleBar = (TitleBar) Utils.findRequiredViewAsType(view, R.id.titleBar, "field 'titleBar'", TitleBar.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        AtFriendsActivity atFriendsActivity = this.target;
        if (atFriendsActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        atFriendsActivity.wsbAtFriendSidebar = null;
        atFriendsActivity.lvAtfriendContactList = null;
        atFriendsActivity.ivSearch = null;
        atFriendsActivity.svSearchContact = null;
        atFriendsActivity.titleBar = null;
        this.view7f0801d7.setOnClickListener(null);
        this.view7f0801d7 = null;
    }
}
