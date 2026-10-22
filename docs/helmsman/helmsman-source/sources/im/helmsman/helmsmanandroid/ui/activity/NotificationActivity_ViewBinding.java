package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationActivity_ViewBinding implements Unbinder {
    private NotificationActivity target;
    private View view7f0802a7;
    private View view7f0802a8;

    public NotificationActivity_ViewBinding(NotificationActivity notificationActivity) {
        this(notificationActivity, notificationActivity.getWindow().getDecorView());
    }

    public NotificationActivity_ViewBinding(final NotificationActivity notificationActivity, View view) {
        this.target = notificationActivity;
        notificationActivity.titleBar = (TitleBar) Utils.findRequiredViewAsType(view, R.id.titleBar, "field 'titleBar'", TitleBar.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.rel_notification_comments, "field 'relNotificationComments' and method 'onClick'");
        notificationActivity.relNotificationComments = (RelativeLayout) Utils.castView(viewFindRequiredView, R.id.rel_notification_comments, "field 'relNotificationComments'", RelativeLayout.class);
        this.view7f0802a7 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.NotificationActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                notificationActivity.onClick(view2);
            }
        });
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.rel_notification_favourite, "field 'relNotificationFavourite' and method 'onClick'");
        notificationActivity.relNotificationFavourite = (RelativeLayout) Utils.castView(viewFindRequiredView2, R.id.rel_notification_favourite, "field 'relNotificationFavourite'", RelativeLayout.class);
        this.view7f0802a8 = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.NotificationActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                notificationActivity.onClick(view2);
            }
        });
        notificationActivity.activityNotifactions = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.activity_notifactions, "field 'activityNotifactions'", LinearLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        NotificationActivity notificationActivity = this.target;
        if (notificationActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        notificationActivity.titleBar = null;
        notificationActivity.relNotificationComments = null;
        notificationActivity.relNotificationFavourite = null;
        notificationActivity.activityNotifactions = null;
        this.view7f0802a7.setOnClickListener(null);
        this.view7f0802a7 = null;
        this.view7f0802a8.setOnClickListener(null);
        this.view7f0802a8 = null;
    }
}
