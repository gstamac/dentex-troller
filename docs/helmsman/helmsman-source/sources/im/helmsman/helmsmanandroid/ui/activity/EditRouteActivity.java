package im.helmsman.helmsmanandroid.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.MAVLink.common.msg_data_transmission_handshake;
import com.bumptech.glide.Glide;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.TimeUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.File;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class EditRouteActivity extends Base2Activity implements View.OnClickListener, View.OnFocusChangeListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    private InputMethodManager imm;
    private boolean isShowKeyboard;
    private int keyboardHeight;
    private Button mBtnDone;
    private EditText mEtName;
    private ImageView mIvWayPointImage;
    private LinearLayout mRelEditRoute;
    private RelativeLayout mRelNoImage;
    private ScrollView mSvEditRoute;
    private TextView mTvDate;
    private TextView mTvDistance;
    private TextView mTvName;
    private TextView mTvWayPointCount;
    private RouteModel mWayPointList;
    private int moveTime;
    private int statusBarHeight;

    private void onHideKeyboard() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_route);
        initView();
        initEvent();
        setStatusBar(R.color.red_state);
    }

    private void initView() {
        this.imm = (InputMethodManager) getSystemService("input_method");
        this.mRelEditRoute = (LinearLayout) findViewById(R.id.rel_edit_route);
        this.statusBarHeight = getStatusBarHeight(getApplicationContext());
        this.mIvWayPointImage = (ImageView) findViewById(R.id.iv_waypoint_image);
        this.mRelNoImage = (RelativeLayout) findViewById(R.id.rel_edit_route_nomap);
        this.mTvDate = (TextView) findViewById(R.id.tv_way_point_time);
        this.mTvName = (TextView) findViewById(R.id.tv_way_point_name);
        this.mTvDistance = (TextView) findViewById(R.id.tv_way_point_dis);
        this.mTvWayPointCount = (TextView) findViewById(R.id.tv_way_point_counter);
        this.mBtnDone = (Button) findViewById(R.id.bt_editroute_done);
        this.mEtName = (EditText) findViewById(R.id.et_editroute_waypointname);
        this.mSvEditRoute = (ScrollView) findViewById(R.id.sv_edit_route);
        RouteModel wayPointListById = DaoUtil.getInstance().getWayPointListById(getIntent().getIntExtra("id", 0));
        this.mWayPointList = wayPointListById;
        if (wayPointListById == null) {
            return;
        }
        this.mTvName.setText(wayPointListById.getName());
        File file = new File(DaoUtil.imagePath + "/" + this.mWayPointList.getPictureName());
        if (file.exists()) {
            Glide.with((FragmentActivity) this).load(file).into(this.mIvWayPointImage);
            this.mIvWayPointImage.setVisibility(0);
            this.mRelNoImage.setVisibility(8);
        }
        this.mTvDate.setText(TimeUtils.format(this.mWayPointList.getDate()));
        this.mTvDistance.setText(CoordinateParserUtil.computerAllPolineDis(this.mWayPointList.getWaypoint()));
        this.mTvWayPointCount.setText(this.mWayPointList.getWaypoint().size() + "");
    }

    private void initEvent() {
        this.mEtName.setOnFocusChangeListener(this);
        this.mRelEditRoute.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.mBtnDone.setOnClickListener(this);
        this.mSvEditRoute.setOnTouchListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.mBtnDone)) {
            if (!"".equals(this.mEtName.getText().toString().trim())) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", this.mEtName.getText().toString());
                DataSupport.update(RouteModel.class, contentValues, this.mWayPointList.getId());
            }
            finish();
        }
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z) {
        if (view.equals(this.mEtName) && z) {
            this.imm.showSoftInputFromInputMethod(this.mEtName.getWindowToken(), 2);
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.moveTime = 0;
        }
        if (motionEvent.getAction() == 2) {
            this.moveTime++;
        }
        if (motionEvent.getAction() == 1 && this.moveTime < 5 && this.mEtName.isFocused() && this.isShowKeyboard) {
            Rect rect = new Rect();
            this.mEtName.getGlobalVisibleRect(rect);
            if (!rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                this.mEtName.clearFocus();
                this.imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return false;
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        int i;
        Rect rect = new Rect();
        this.mRelEditRoute.getWindowVisibleDisplayFrame(rect);
        int height = (this.mRelEditRoute.getRootView().getHeight() - (rect.bottom - rect.top)) - ViewUtils.getNavigationBarHeight(this);
        if (this.keyboardHeight == 0 && height > (i = this.statusBarHeight)) {
            this.keyboardHeight = height - i;
        }
        if (this.isShowKeyboard) {
            if (height <= this.statusBarHeight) {
                this.isShowKeyboard = false;
                onHideKeyboard();
                return;
            }
            return;
        }
        if (height > this.statusBarHeight) {
            this.isShowKeyboard = true;
            onShowKeyboard();
        }
    }

    private void onShowKeyboard() {
        this.mSvEditRoute.fullScroll(msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE);
        this.mEtName.requestFocus();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.mRelEditRoute.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
