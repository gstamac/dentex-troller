package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import com.facebook.share.internal.ShareConstants;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.LocalRouteListAdapter;
import im.helmsman.helmsmanandroid.adapter.RouteViewPagerAdapter;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.fragment.HomeRouteFragment;
import im.helmsman.helmsmanandroid.ui.fragment.LocalRouteFragment;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.SearchView;
import im.helmsman.helmsmanandroid.view.TitleBar;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class MyFollowingRouteActivity extends Base2Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final int HOME_STATE = 3;
    public static final int LOCAL_ROUTE = 2;
    public static final int REQUEST_DOWNLOAD_INTENT = 100;
    public static final int SEARCH_OPEN = 1;
    public static final int SELECT_STATE = 4;
    private HomeRouteFragment friendRouteFragment;

    @BindView(R.id.iv_myfollowing_route_copy)
    public ImageView ivMyfollowingRouteCopy;

    @BindView(R.id.iv_myfollowing_route_delete)
    public ImageView ivMyfollowingRouteDelete;

    @BindView(R.id.iv_myfollowing_route_edit)
    public ImageView ivMyfollowingRouteEdit;

    @BindView(R.id.iv_myfollowing_route_Message)
    ImageView ivMyfollowingRouteMessage;

    @BindView(R.id.iv_myfollowing_route_search)
    public ImageView ivMyfollowingRouteSearch;
    private LocalRouteFragment localRouteFragment;

    @BindView(R.id.search_localroute_search)
    public SearchView searchLocalrouteSearch;
    private TitleBar titleBar;
    private TextView tvHome;
    private TextView tvMyRoute;
    private View view;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList();
    private int index = 0;
    public int state = 2;

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float f, int i2) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_following_route);
        initView();
        setStatusBar(R.color.red_state);
    }

    private void initView() {
        this.tvHome = (TextView) findViewById(R.id.tv_myfollowing_home);
        this.tvMyRoute = (TextView) findViewById(R.id.tv_myfollowing_MyRoute);
        this.titleBar = (TitleBar) findViewById(R.id.titleBar);
        this.view = findViewById(R.id.view);
        this.viewPager = (ViewPager) findViewById(R.id.viepager);
        this.localRouteFragment = new LocalRouteFragment();
        this.friendRouteFragment = new HomeRouteFragment();
        this.fragments.add(this.localRouteFragment);
        this.fragments.add(this.friendRouteFragment);
        this.viewPager.setAdapter(new RouteViewPagerAdapter(getSupportFragmentManager(), this.fragments));
        this.viewPager.addOnPageChangeListener(this);
        this.tvHome.setOnClickListener(this);
        this.tvMyRoute.setOnClickListener(this);
        this.ivMyfollowingRouteSearch.setOnClickListener(this);
        this.ivMyfollowingRouteMessage.setOnClickListener(this);
    }

    public int getIndex() {
        return this.index;
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        if ((fragment instanceof LocalRouteFragment) && getIntent().getIntExtra(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, 0) == 100) {
            this.localRouteFragment.refreshListView();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        TranslateAnimation translateAnimation;
        Log.e("onPageSelected", i + "," + this.index);
        int screenWidth = ViewUtils.getScreenWidth(this) / 2;
        if (i == 0 && this.index == 1) {
            translateAnimation = new TranslateAnimation(screenWidth, 0.0f, 0.0f, 0.0f);
            this.titleBar.setCenterViewText(getString(R.string.local_rote));
            this.tvMyRoute.setTextColor(getResources().getColor(R.color.a1a1a1));
            this.tvHome.setTextColor(-1);
            changeState(2);
        } else if (i == 1 && this.index == 0) {
            translateAnimation = new TranslateAnimation(0.0f, screenWidth, 0.0f, 0.0f);
            this.titleBar.setCenterViewText(getString(R.string.myfollowingrouteactivity_home));
            this.tvMyRoute.setTextColor(-1);
            this.tvHome.setTextColor(getResources().getColor(R.color.a1a1a1));
            changeState(3);
            UserMe.isLogin();
        } else {
            translateAnimation = null;
        }
        translateAnimation.setDuration(500L);
        translateAnimation.setFillAfter(true);
        this.view.startAnimation(translateAnimation);
        this.index = i;
    }

    public void selectPageIndex(int i) {
        if (i > this.fragments.size()) {
            return;
        }
        this.viewPager.setCurrentItem(i);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.tvMyRoute)) {
            this.viewPager.setCurrentItem(1);
            return;
        }
        if (view.equals(this.tvHome)) {
            this.viewPager.setCurrentItem(0);
            return;
        }
        if (view.equals(this.ivMyfollowingRouteSearch)) {
            changeState(1);
            this.searchLocalrouteSearch.requestFocus();
        } else if (view.equals(this.ivMyfollowingRouteMessage)) {
            startActivity(new Intent(this, (Class<?>) NotificationActivity.class));
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        if (LocalRouteListAdapter.getInstance() != null) {
            changeState(this.state);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        int i = this.state;
        if (i == 1 || i == 4) {
            changeState(2);
        } else {
            super.onBackPressed();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        LocalRouteListAdapter.onDestroy();
    }

    public void changeState(int i) {
        this.state = i;
        this.ivMyfollowingRouteSearch.setVisibility(8);
        if (LocalRouteListAdapter.getInstance() != null) {
            LocalRouteListAdapter.getInstance().setCheckBoxVisiblity(8);
        }
        this.ivMyfollowingRouteDelete.setVisibility(8);
        this.ivMyfollowingRouteEdit.setVisibility(8);
        this.ivMyfollowingRouteMessage.setVisibility(8);
        this.searchLocalrouteSearch.setVisibility(8);
        this.ivMyfollowingRouteCopy.setVisibility(8);
        this.titleBar.showCenterView();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
        inputMethodManager.hideSoftInputFromWindow(this.searchLocalrouteSearch.getEditText().getWindowToken(), 0);
        if (i == 1) {
            this.searchLocalrouteSearch.setVisibility(0);
            this.titleBar.hideCenterView();
            this.searchLocalrouteSearch.clear();
            inputMethodManager.toggleSoftInput(2, 0);
            return;
        }
        if (i == 2) {
            this.ivMyfollowingRouteSearch.setVisibility(0);
            return;
        }
        if (i != 3) {
            if (i != 4) {
                return;
            }
            LocalRouteListAdapter.getInstance().setCheckBoxVisiblity(0);
            this.titleBar.hideCenterView();
            return;
        }
        if (UserMe.isLogin()) {
            this.ivMyfollowingRouteMessage.setVisibility(0);
        } else {
            this.ivMyfollowingRouteMessage.setVisibility(8);
        }
    }
}
