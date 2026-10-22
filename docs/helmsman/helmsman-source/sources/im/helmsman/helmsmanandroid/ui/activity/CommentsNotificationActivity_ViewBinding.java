package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class CommentsNotificationActivity_ViewBinding implements Unbinder {
    private CommentsNotificationActivity target;

    public CommentsNotificationActivity_ViewBinding(CommentsNotificationActivity commentsNotificationActivity) {
        this(commentsNotificationActivity, commentsNotificationActivity.getWindow().getDecorView());
    }

    public CommentsNotificationActivity_ViewBinding(CommentsNotificationActivity commentsNotificationActivity, View view) {
        this.target = commentsNotificationActivity;
        commentsNotificationActivity.lvCommentNotif = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_comment_notif, "field 'lvCommentNotif'", ListView.class);
        commentsNotificationActivity.swipeRefresh = (PtrClassicFrameLayout) Utils.findRequiredViewAsType(view, R.id.swipe_refresh, "field 'swipeRefresh'", PtrClassicFrameLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        CommentsNotificationActivity commentsNotificationActivity = this.target;
        if (commentsNotificationActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        commentsNotificationActivity.lvCommentNotif = null;
        commentsNotificationActivity.swipeRefresh = null;
    }
}
