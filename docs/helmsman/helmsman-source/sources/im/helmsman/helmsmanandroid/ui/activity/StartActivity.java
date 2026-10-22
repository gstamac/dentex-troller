package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.fragment.app.FragmentActivity;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import com.bumptech.glide.Glide;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.presenter.StartPresenter;
import im.helmsman.helmsmanandroid.ui.common.MvpBaseActivity;
import im.helmsman.helmsmanandroid.ui.view.StartView;
import javy.lib.jsave.JSave;

/* JADX INFO: loaded from: classes2.dex */
public class StartActivity extends MvpBaseActivity<StartView, StartPresenter> implements StartView {
    private Handler mHandler;
    private ImageView mIvDisPlay2;
    private RelativeLayout mRelDisPlay;

    @Override // im.helmsman.helmsmanandroid.ui.common.MvpBaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.MvpBaseActivity
    public StartPresenter initPresenter() {
        return new StartPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.MvpBaseActivity, im.helmsman.helmsmanandroid.ui.common.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        isPad = getResources().getBoolean(R.bool.isPad);
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory("android.intent.category.LAUNCHER") && action.equals("android.intent.action.MAIN")) {
                finish();
                return;
            }
        }
        setContentView(R.layout.activity_start);
        setJpush();
        setStatusBar(R.color.white);
        this.mHandler = new Handler();
        initView();
        downloadLaunch();
        this.mHandler.postDelayed(new AlphaAnimRunnable(), 1700L);
        loadImage();
        ((StartPresenter) this.presenter).updateContact();
    }

    private void initView() {
        this.mRelDisPlay = (RelativeLayout) findViewById(R.id.rel_start_display);
        this.mIvDisPlay2 = (ImageView) findViewById(R.id.iv_start_display2);
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.bg_start_scale)).into(this.mIvDisPlay2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initImage() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(3000L);
        this.mIvDisPlay2.startAnimation(scaleAnimation);
    }

    class ScaleAnimRunnable implements Runnable {
        ScaleAnimRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            StartActivity.this.initImage();
            StartActivity.this.mHandler.postDelayed(StartActivity.this.new JumpToMainActivityRunnable(), 3000L);
        }
    }

    class AlphaAnimRunnable implements Runnable {
        AlphaAnimRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(1500L);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setInterpolator(new LinearInterpolator());
            StartActivity.this.mRelDisPlay.startAnimation(alphaAnimation);
            StartActivity.this.mHandler.postDelayed(StartActivity.this.new ScaleAnimRunnable(), 400L);
        }
    }

    class JumpToMainActivityRunnable implements Runnable {
        JumpToMainActivityRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            StartActivity.this.startActivity(new Intent(StartActivity.this, (Class<?>) MainActivity.class));
            StartActivity.this.finish();
        }
    }

    private void downloadLaunch() {
        RequestInetUtils.instance().downloadLaunchImage();
    }

    private void loadImage() {
        Bitmap bitmap = (Bitmap) JSave.get("lauchimage");
        if (bitmap != null) {
            this.mIvDisPlay2.setImageBitmap(bitmap);
        }
    }

    private void setJpush() {
        BasicPushNotificationBuilder basicPushNotificationBuilder = new BasicPushNotificationBuilder(this);
        basicPushNotificationBuilder.statusBarDrawable = R.drawable.jpush_notification_icon;
        JPushInterface.setPushNotificationBuilder(1, basicPushNotificationBuilder);
    }
}
