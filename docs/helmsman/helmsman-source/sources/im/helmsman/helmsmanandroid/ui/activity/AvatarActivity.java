package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;

/* JADX INFO: loaded from: classes2.dex */
public class AvatarActivity extends Base2Activity {
    private ImageView ivAvatar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_avatar);
        this.ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        Glide.with((FragmentActivity) this).load(getIntent().getStringExtra("avatarurl")).into(this.ivAvatar);
    }
}
