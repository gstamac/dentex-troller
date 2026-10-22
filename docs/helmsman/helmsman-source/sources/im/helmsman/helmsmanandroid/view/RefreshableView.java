package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshableView extends LinearLayout implements View.OnTouchListener {
    public static final int SCROLL_SPEED = -20;
    public static final int STATUS_PULL_TO_REFRESH = 0;
    public static final int STATUS_REFRESHING = 2;
    public static final int STATUS_REFRESH_FINISHED = 3;
    public static final int STATUS_RELEASE_TO_REFRESH = 1;
    private boolean ableToPull;
    private ImageView arrow;
    private Context context;
    private int currentStatus;
    private TextView description;
    private View header;
    private ViewGroup.MarginLayoutParams headerLayoutParams;
    private int hideHeaderHeight;
    private int lastStatus;
    private ListView listView;
    private boolean loadOnce;
    private int mId;
    private PullToRefreshListener mListener;
    private ProgressBar progressBar;
    private int touchSlop;
    private float yDown;

    public interface PullToRefreshListener {
        void onRefresh();
    }

    public RefreshableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mId = -1;
        this.currentStatus = 3;
        this.lastStatus = 3;
        this.context = context;
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh, (ViewGroup) null, true);
        this.header = viewInflate;
        this.progressBar = (ProgressBar) viewInflate.findViewById(R.id.progress_bar);
        this.arrow = (ImageView) this.header.findViewById(R.id.arrow);
        this.description = (TextView) this.header.findViewById(R.id.description);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOrientation(1);
        addView(this.header, 0);
    }

    public void setRefreshable(boolean z) {
        if (z) {
            this.touchSlop = ViewConfiguration.get(this.context).getScaledTouchSlop();
        } else {
            this.touchSlop = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (!z || this.loadOnce) {
            return;
        }
        this.hideHeaderHeight = -this.header.getHeight();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.header.getLayoutParams();
        this.headerLayoutParams = marginLayoutParams;
        marginLayoutParams.topMargin = this.hideHeaderHeight;
        ListView listView = (ListView) getChildAt(1);
        this.listView = listView;
        listView.setOnTouchListener(this);
        this.loadOnce = true;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        setIsAbleToPull(motionEvent);
        if (this.ableToPull) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.yDown = motionEvent.getRawY();
            } else if (action == 2) {
                int rawY = (int) (motionEvent.getRawY() - this.yDown);
                if ((rawY <= 0 && this.headerLayoutParams.topMargin <= this.hideHeaderHeight) || rawY < this.touchSlop) {
                    return false;
                }
                if (this.currentStatus != 2) {
                    if (this.headerLayoutParams.topMargin > 0) {
                        this.currentStatus = 1;
                    } else {
                        this.currentStatus = 0;
                    }
                    this.headerLayoutParams.topMargin = (rawY / 2) + this.hideHeaderHeight;
                    this.header.setLayoutParams(this.headerLayoutParams);
                }
            } else {
                int i = this.currentStatus;
                if (i == 1) {
                    new RefreshingTask().execute(new Void[0]);
                } else if (i == 0) {
                    new HideHeaderTask().execute(new Void[0]);
                }
            }
            int i2 = this.currentStatus;
            if (i2 == 0 || i2 == 1) {
                updateHeaderView();
                this.listView.setPressed(false);
                this.listView.setFocusable(false);
                this.listView.setFocusableInTouchMode(false);
                this.lastStatus = this.currentStatus;
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.yDown = motionEvent.getRawY();
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setOnRefreshListener(PullToRefreshListener pullToRefreshListener, int i) {
        this.mListener = pullToRefreshListener;
        this.mId = i;
    }

    public void finishRefreshing() {
        this.currentStatus = 3;
        new HideHeaderTask().execute(new Void[0]);
    }

    private void setIsAbleToPull(MotionEvent motionEvent) {
        View childAt = this.listView.getChildAt(0);
        if (childAt != null) {
            if (this.listView.getFirstVisiblePosition() == 0 && childAt.getTop() == 0) {
                if (!this.ableToPull) {
                    this.yDown = motionEvent.getRawY();
                }
                this.ableToPull = true;
                return;
            } else {
                int i = this.headerLayoutParams.topMargin;
                int i2 = this.hideHeaderHeight;
                if (i != i2) {
                    this.headerLayoutParams.topMargin = i2;
                    this.header.setLayoutParams(this.headerLayoutParams);
                }
                this.ableToPull = false;
                return;
            }
        }
        this.ableToPull = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeaderView() {
        int i = this.lastStatus;
        int i2 = this.currentStatus;
        if (i != i2) {
            if (i2 == 0) {
                this.description.setText(getResources().getString(R.string.route_pull_to_download));
                this.arrow.setVisibility(0);
                this.progressBar.setVisibility(8);
                rotateArrow();
                return;
            }
            if (i2 == 1) {
                this.description.setText(getResources().getString(R.string.route_release_to_download));
                this.arrow.setVisibility(0);
                this.progressBar.setVisibility(8);
                rotateArrow();
                return;
            }
            if (i2 == 2) {
                this.description.setText(getResources().getString(R.string.route_downloading));
                this.progressBar.setVisibility(0);
                this.arrow.clearAnimation();
                this.arrow.setVisibility(8);
            }
        }
    }

    private void rotateArrow() {
        float f;
        float width = this.arrow.getWidth() / 2.0f;
        float height = this.arrow.getHeight() / 2.0f;
        int i = this.currentStatus;
        float f2 = 180.0f;
        if (i == 0) {
            f = 360.0f;
        } else if (i == 1) {
            f = 180.0f;
            f2 = 0.0f;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        RotateAnimation rotateAnimation = new RotateAnimation(f2, f, width, height);
        rotateAnimation.setDuration(100L);
        rotateAnimation.setFillAfter(true);
        this.arrow.startAnimation(rotateAnimation);
    }

    public void refresh() {
        new RefreshingTask().execute(new Void[0]);
    }

    class RefreshingTask extends AsyncTask<Void, Integer, Void> {
        RefreshingTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            int i = RefreshableView.this.headerLayoutParams.topMargin;
            while (true) {
                i -= 20;
                if (i <= 0) {
                    break;
                }
                publishProgress(Integer.valueOf(i));
            }
            RefreshableView.this.currentStatus = 2;
            publishProgress(0);
            if (RefreshableView.this.mListener == null) {
                return null;
            }
            RefreshableView.this.mListener.onRefresh();
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... numArr) {
            RefreshableView.this.updateHeaderView();
            RefreshableView.this.headerLayoutParams.topMargin = numArr[0].intValue();
            RefreshableView.this.header.setLayoutParams(RefreshableView.this.headerLayoutParams);
        }
    }

    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {
        HideHeaderTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            int i = RefreshableView.this.headerLayoutParams.topMargin;
            while (true) {
                i -= 20;
                if (i <= RefreshableView.this.hideHeaderHeight) {
                    return Integer.valueOf(RefreshableView.this.hideHeaderHeight);
                }
                publishProgress(Integer.valueOf(i));
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... numArr) {
            RefreshableView.this.headerLayoutParams.topMargin = numArr[0].intValue();
            RefreshableView.this.header.setLayoutParams(RefreshableView.this.headerLayoutParams);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            RefreshableView.this.headerLayoutParams.topMargin = num.intValue();
            RefreshableView.this.header.setLayoutParams(RefreshableView.this.headerLayoutParams);
            RefreshableView.this.currentStatus = 3;
        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
