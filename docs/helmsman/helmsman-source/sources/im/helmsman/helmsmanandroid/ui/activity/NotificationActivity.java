package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationActivity extends Base2Activity {

    @BindView(R.id.activity_notifactions)
    LinearLayout activityNotifactions;

    @BindView(R.id.rel_notification_comments)
    RelativeLayout relNotificationComments;

    @BindView(R.id.rel_notification_favourite)
    RelativeLayout relNotificationFavourite;

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notifactions);
        setStatusBar(R.color.red_state);
    }

    @OnClick({R.id.rel_notification_comments, R.id.rel_notification_favourite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_notification_comments /* 2131231399 */:
                startActivity(new Intent(this, (Class<?>) CommentsNotificationActivity.class));
                break;
            case R.id.rel_notification_favourite /* 2131231400 */:
                startActivity(new Intent(this, (Class<?>) FavouriteMessageActivity.class));
                break;
        }
    }
}
