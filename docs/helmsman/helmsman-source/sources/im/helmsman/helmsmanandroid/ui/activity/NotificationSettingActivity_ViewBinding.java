package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.Switch;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationSettingActivity_ViewBinding implements Unbinder {
    private NotificationSettingActivity target;

    public NotificationSettingActivity_ViewBinding(NotificationSettingActivity notificationSettingActivity) {
        this(notificationSettingActivity, notificationSettingActivity.getWindow().getDecorView());
    }

    public NotificationSettingActivity_ViewBinding(NotificationSettingActivity notificationSettingActivity, View view) {
        this.target = notificationSettingActivity;
        notificationSettingActivity.switchComment = (Switch) Utils.findRequiredViewAsType(view, R.id.switch_comment, "field 'switchComment'", Switch.class);
        notificationSettingActivity.switchAt = (Switch) Utils.findRequiredViewAsType(view, R.id.switch_at, "field 'switchAt'", Switch.class);
        notificationSettingActivity.switchFavourite = (Switch) Utils.findRequiredViewAsType(view, R.id.switch_favourite, "field 'switchFavourite'", Switch.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        NotificationSettingActivity notificationSettingActivity = this.target;
        if (notificationSettingActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        notificationSettingActivity.switchComment = null;
        notificationSettingActivity.switchAt = null;
        notificationSettingActivity.switchFavourite = null;
    }
}
