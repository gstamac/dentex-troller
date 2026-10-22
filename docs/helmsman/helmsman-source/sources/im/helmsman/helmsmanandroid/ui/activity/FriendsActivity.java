package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.RouteViewPagerAdapter;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.fragment.FollowerFragment;
import im.helmsman.helmsmanandroid.ui.fragment.FollowingFragment;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class FriendsActivity extends Base2Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final int SELECT_FOLLOWER = 0;
    public static final int SELECT_FOLLOWING = 1;
    private TextView tvFollower;
    private TextView tvFollowing;
    public int userId;
    private View viewLine;
    private ViewPager viewPager;
    private int index = 0;
    private List<Fragment> fragments = new ArrayList();

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float f, int i2) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_friends);
        setStatusBar(R.color.red_state);
        initView();
        initEvent();
        Intent intent = getIntent();
        UserMe user = UserMe.getUser();
        if (user == null) {
            finish();
            ViewUtils.ShowToast(R.string.loaddatafailed);
        } else {
            this.userId = intent.getIntExtra("userid", user.getId());
            this.viewPager.setCurrentItem(intent.getIntExtra("index", 0));
        }
    }

    private void initView() {
        this.tvFollower = (TextView) findViewById(R.id.tv_friendsactivity_follower);
        this.tvFollowing = (TextView) findViewById(R.id.tv_friendsactivity_following);
        this.viewLine = findViewById(R.id.view);
        this.viewPager = (ViewPager) findViewById(R.id.viepager_friendactivity);
        this.fragments.add(new FollowerFragment());
        this.fragments.add(new FollowingFragment());
        this.viewPager.setAdapter(new RouteViewPagerAdapter(getSupportFragmentManager(), this.fragments));
        this.viewPager = (ViewPager) findViewById(R.id.viepager_friendactivity);
        this.tvFollower.setTextColor(-1);
        this.tvFollowing.setTextColor(getResources().getColor(R.color.a1a1a1));
    }

    private void initEvent() {
        this.viewPager.addOnPageChangeListener(this);
        this.tvFollowing.setOnClickListener(this);
        this.tvFollower.setOnClickListener(this);
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        TranslateAnimation translateAnimation;
        int screenWidth = ViewUtils.getScreenWidth(this) / 2;
        if (i == 0 && this.index == 1) {
            translateAnimation = new TranslateAnimation(screenWidth, 0.0f, 0.0f, 0.0f);
            this.tvFollower.setTextColor(-1);
            this.tvFollowing.setTextColor(getResources().getColor(R.color.a1a1a1));
        } else if (i == 1 && this.index == 0) {
            translateAnimation = new TranslateAnimation(0.0f, screenWidth, 0.0f, 0.0f);
            this.tvFollower.setTextColor(getResources().getColor(R.color.a1a1a1));
            this.tvFollowing.setTextColor(-1);
        } else {
            translateAnimation = null;
        }
        translateAnimation.setDuration(500L);
        translateAnimation.setFillAfter(true);
        this.viewLine.startAnimation(translateAnimation);
        this.index = i;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.tvFollowing)) {
            this.viewPager.setCurrentItem(1);
        } else if (view.equals(this.tvFollower)) {
            this.viewPager.setCurrentItem(0);
        }
    }
}
