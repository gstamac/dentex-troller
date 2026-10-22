package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import im.helmsman.helmsmanandroid.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CycleBanner extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private Context context;
    private int count;
    private Handler handler;
    private List<ImageView> imageViews;
    private int index;
    private LinearLayout pointLayout;
    private List<View> points;
    private Runnable runnable;
    private ViewPager viewPager;

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float f, int i2) {
    }

    public CycleBanner(Context context) {
        super(context);
        this.imageViews = new ArrayList();
        this.points = new ArrayList();
        this.handler = new Handler();
        this.index = 0;
        this.context = context;
        initView();
    }

    public CycleBanner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.imageViews = new ArrayList();
        this.points = new ArrayList();
        this.handler = new Handler();
        this.index = 0;
        this.context = context;
        initView();
    }

    public CycleBanner(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.imageViews = new ArrayList();
        this.points = new ArrayList();
        this.handler = new Handler();
        this.index = 0;
        this.context = context;
        initView();
    }

    public void setCycleDatas(int[] iArr) {
        this.count = iArr.length;
        this.index = 0;
        for (int i : iArr) {
            ImageView imageView = new ImageView(this.context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(i);
            this.imageViews.add(imageView);
            View view = new View(this.context);
            view.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.leftMargin = 22;
            view.setEnabled(false);
            view.setLayoutParams(layoutParams);
            this.pointLayout.addView(view);
            this.points.add(view);
        }
        this.points.get(0).setEnabled(true);
        this.viewPager.setAdapter(new MyAdapter(this.imageViews));
        startCycle();
    }

    public void startCycle() {
        Runnable runnable = new Runnable() { // from class: im.helmsman.helmsmanandroid.view.CycleBanner.1
            @Override // java.lang.Runnable
            public void run() {
                CycleBanner.this.viewPager.setCurrentItem(CycleBanner.this.index);
                CycleBanner.this.handler.postDelayed(this, 2500L);
            }
        };
        this.runnable = runnable;
        this.handler.postDelayed(runnable, 2500L);
        this.viewPager.addOnPageChangeListener(this);
        this.viewPager.setCurrentItem(1);
    }

    public void stopCycle() {
        this.handler.removeCallbacks(this.runnable);
    }

    private void initView() {
        this.viewPager = new ViewPager(this.context);
        addView(this.viewPager, new RelativeLayout.LayoutParams(-1, -1));
        this.pointLayout = new LinearLayout(this.context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(12);
        layoutParams.addRule(14);
        layoutParams.bottomMargin = 47;
        addView(this.pointLayout, layoutParams);
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        Iterator<View> it = this.points.iterator();
        while (it.hasNext()) {
            it.next().setEnabled(false);
        }
        this.points.get(i).setEnabled(true);
        int i2 = i + 1;
        this.index = i2;
        if (this.count <= i2) {
            this.index = 0;
        }
    }

    class MyAdapter extends PagerAdapter {
        List<ImageView> views;

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public MyAdapter(List<ImageView> list) {
            this.views = list;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.views.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            ImageView imageView = this.views.get(i);
            viewGroup.addView(imageView);
            return imageView;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView(this.views.get(i));
        }
    }
}
