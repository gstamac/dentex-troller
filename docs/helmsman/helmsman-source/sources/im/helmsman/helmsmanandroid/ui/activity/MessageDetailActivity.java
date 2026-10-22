package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.OnClick;
import com.amap.api.maps.AMap;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.LikeRouteSucessMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SendCommentSuccessMessage;
import im.helmsman.helmsmanandroid.presenter.MessageDetailPresenterImp;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.MessageDetailView;
import im.helmsman.helmsmanandroid.utils.CoordinateTransform;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.MapUtil;
import im.helmsman.helmsmanandroid.utils.ShareUtils;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.TitleBar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

/* JADX INFO: loaded from: classes2.dex */
public class MessageDetailActivity extends Mvp2BaseActivity<MessageDetailView, MessageDetailPresenterImp> implements MessageDetailView, OnMapReadyCallback, View.OnClickListener {
    private AMap aMap;
    private String avatarUrl;

    @BindView(R.id.civ_messagedetail)
    CircleImageView civMessagedetail;
    private String description;
    private float distance;

    @BindView(R.id.frame_item_my_following_favourite)
    FrameLayout frameItemMyFollowingFavourite;
    private String identifier;
    private boolean isLike;
    private ImageView ivFaceBookShare;

    @BindView(R.id.iv_myroute_heart_like)
    ImageView ivMyrouteHeartLike;
    private ImageView ivWeixinShare;
    private ImageView ivWexinShareTimeLine;

    @BindView(R.id.lin_item_my_following_comment)
    LinearLayout linItemMyFollowingComment;
    private GoogleMap mGoogleMap;
    private Bitmap mapBitmap;
    private String mapUrl;
    private int messageId;
    private List<MLatLng> mlatLngs;
    private int routeId;
    private String routeName;
    private String shareurl;

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @BindView(R.id.tlinear_item_my_following_download)
    LinearLayout tlinearItemMyFollowingDownload;

    @BindView(R.id.tv_item_my_following_comment)
    TextView tvItemMyFollowingComment;

    @BindView(R.id.tv_item_my_following_download)
    TextView tvItemMyFollowingDownload;

    @BindView(R.id.tv_messagedetail_description)
    TextView tvMessagedetailDescription;
    private int userId;

    @BindView(R.id.vs_gaode_map)
    ViewStub vsGaodeMap;

    @BindView(R.id.vs_google_map)
    ViewStub vsGoogleMap;
    private int waypointCount;
    private int downLoadCount = 0;
    private int commentCount = 0;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_message_detail);
        initData();
        setMapAsync();
        registerEventBus();
        initEvent();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public MessageDetailPresenterImp initPresenter() {
        return new MessageDetailPresenterImp();
    }

    private void initEvent() {
        this.titleBar.setCenterViewOnClickListener(this);
        this.titleBar.setRightButtonOnclickListener(this);
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity$1] */
    private void initData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        this.waypointCount = intent.getIntExtra("waypointcount", 0);
        this.shareurl = intent.getStringExtra("shareurl");
        this.avatarUrl = intent.getStringExtra("avatarurl");
        this.isLike = intent.getBooleanExtra("islike", false);
        this.description = intent.getStringExtra("description");
        this.messageId = intent.getIntExtra("messageid", 0);
        this.routeId = intent.getIntExtra("routeid", 0);
        ArrayList parcelableArrayList = extras.getParcelableArrayList("latlngs");
        this.mlatLngs = parcelableArrayList;
        this.mlatLngs = CoordinateTransform.latLngWGS84ToGCJ02(parcelableArrayList);
        this.userId = intent.getIntExtra("userid", 0);
        this.identifier = intent.getStringExtra("identifier");
        this.routeName = intent.getStringExtra("routename");
        this.commentCount = intent.getIntExtra("commentcount", 0);
        this.downLoadCount = intent.getIntExtra("downloadcount", 0);
        this.routeId = intent.getIntExtra("routeid", 0);
        this.distance = intent.getFloatExtra("distance", this.distance);
        this.mapUrl = intent.getStringExtra("mapurl");
        String str = this.description;
        if (str == null || str.equals("")) {
            this.tvMessagedetailDescription.setVisibility(8);
        } else {
            SpannableString spannableStringUserNameToBlueColor = StringUtils.userNameToBlueColor(this.description);
            this.tvMessagedetailDescription.setMovementMethod(LinkMovementMethod.getInstance());
            this.tvMessagedetailDescription.setText(spannableStringUserNameToBlueColor);
        }
        this.ivMyrouteHeartLike.setImageResource(this.isLike ? R.drawable.icon_redheart : R.drawable.icon_favourite_empty_heart);
        Glide.with((FragmentActivity) this).load(this.avatarUrl).into(this.civMessagedetail);
        this.tvItemMyFollowingComment.setText(this.commentCount == 0 ? getString(R.string.Comment) : this.commentCount + "");
        this.tvItemMyFollowingDownload.setText(this.downLoadCount == 0 ? getString(R.string.download) : this.downLoadCount + "");
        new Thread() { // from class: im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                super.run();
                try {
                    MessageDetailActivity.this.mapBitmap = Picasso.with(Base2Activity.getTopActivity()).load(MessageDetailActivity.this.mapUrl).resize(50, 50).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override // com.google.android.gms.maps.OnMapReadyCallback
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        paintMarkerAndLine(this.mlatLngs);
    }

    private void setMapAsync() {
        int i = Config.MAP_TYPE;
        if (i == 1) {
            this.vsGoogleMap.inflate();
            ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMapAsync(this);
        } else {
            if (i != 2) {
                return;
            }
            this.vsGaodeMap.inflate();
            com.amap.api.maps.MapFragment mapFragment = (com.amap.api.maps.MapFragment) getFragmentManager().findFragmentById(R.id.gaode_map);
            mapFragment.onCreate(null);
            this.aMap = mapFragment.getMap();
            paintMarkerAndLine(this.mlatLngs);
            this.aMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    private void paintMarkerAndLine(List<MLatLng> list) {
        if (list == null) {
            return;
        }
        int i = Config.MAP_TYPE;
        int i2 = 0;
        if (i == 1) {
            ArrayList arrayList = new ArrayList();
            for (int i3 = 1; i3 < list.size(); i3++) {
                this.mGoogleMap.addPolyline(new PolylineOptions().add(list.get(i3 - 1).toGoogleLatlng(), list.get(i3).toGoogleLatlng()).width(8.0f).color(-16776961));
            }
            while (i2 < list.size()) {
                View viewInflate = View.inflate(this, R.layout.item_add_route_under_marker, null);
                int i4 = i2 + 1;
                ((TextView) viewInflate.findViewById(R.id.tv_add_route_marker)).setText(String.valueOf(i4));
                Bitmap viewBitmap = ViewUtils.getViewBitmap(viewInflate);
                arrayList.add(this.mGoogleMap.addMarker(new MarkerOptions().position(list.get(i2).toGoogleLatlng()).icon(BitmapDescriptorFactory.fromBitmap(viewBitmap))));
                if (viewBitmap != null && !viewBitmap.isRecycled()) {
                    viewBitmap.recycle();
                }
                i2 = i4;
            }
            LatLngBounds latLngBounds = new LatLngBounds(MapUtil.findSouthWestMarker(arrayList), MapUtil.findNorthEastMarker(arrayList));
            for (int i5 = 2; i5 < arrayList.size(); i5++) {
                latLngBounds.including(((Marker) arrayList.get(i5)).getPosition());
            }
            this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 1080, 1920, 300));
            return;
        }
        if (i != 2) {
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i6 = 1; i6 < list.size(); i6++) {
            MLatLng mLatLng = list.get(i6 - 1);
            MLatLng mLatLng2 = list.get(i6);
            com.amap.api.maps.model.PolylineOptions polylineOptions = new com.amap.api.maps.model.PolylineOptions();
            polylineOptions.add(mLatLng.toAmapLatlng(), mLatLng2.toAmapLatlng());
            polylineOptions.width(8.0f).color(-16776961);
            this.aMap.addPolyline(polylineOptions);
        }
        while (i2 < list.size()) {
            View viewInflate2 = View.inflate(this, R.layout.item_add_route_under_marker, null);
            int i7 = i2 + 1;
            ((TextView) viewInflate2.findViewById(R.id.tv_add_route_marker)).setText(String.valueOf(i7));
            Bitmap viewBitmap2 = ViewUtils.getViewBitmap(viewInflate2);
            com.amap.api.maps.model.MarkerOptions markerOptions = new com.amap.api.maps.model.MarkerOptions();
            markerOptions.position(list.get(i2).toAmapLatlng());
            markerOptions.icon(com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap(viewBitmap2));
            arrayList2.add(this.aMap.addMarker(markerOptions));
            if (viewBitmap2 != null && !viewBitmap2.isRecycled()) {
                viewBitmap2.recycle();
            }
            com.amap.api.maps.model.LatLngBounds latLngBounds2 = new com.amap.api.maps.model.LatLngBounds(MapUtil.findSouthWestLatLng(list).toAmapLatlng(), MapUtil.findNorthEastLatLng(list).toAmapLatlng());
            for (int i8 = 2; i8 < list.size(); i8++) {
                latLngBounds2.including(list.get(i8).toAmapLatlng());
            }
            this.aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngBounds(latLngBounds2, 1080, 1920, 300));
            i2 = i7;
        }
    }

    @Override // android.view.View.OnClickListener
    @OnClick({R.id.frame_item_my_following_favourite, R.id.tlinear_item_my_following_download, R.id.lin_item_my_following_comment})
    public void onClick(View view) {
        Intent intent;
        int id = view.getId();
        if (id != R.id.frame_item_my_following_favourite) {
            if (id == R.id.lin_item_my_following_comment) {
                if (this.commentCount > 0) {
                    intent = new Intent(this, (Class<?>) CommentContentActivity.class);
                    intent.putExtra("routeid", this.routeId);
                } else {
                    intent = new Intent(this, (Class<?>) SendMessageActivity.class);
                    intent.putExtra("model", (short) 3);
                }
                intent.putExtra("messageid", this.messageId);
                startActivity(intent);
            } else if (id == R.id.tlinear_item_my_following_download) {
                DaoUtil.getInstance().saveWayPointList(this.routeName, CoordinateTransform.latLngGCJ024ToWGS84(this.mlatLngs), null, this.identifier);
                ViewUtils.ShowToast(R.string.myfanrouteadapter_route_download_complete);
                int i = this.downLoadCount + 1;
                this.downLoadCount = i;
                this.tvItemMyFollowingDownload.setText(i == 0 ? getString(R.string.download) : this.downLoadCount + "");
                ((MessageDetailPresenterImp) this.presenter).downloadWayPointByRouteId(this.routeId);
            }
        } else {
            if (this.isLike) {
                ViewUtils.ShowToast(R.string.yououhavecollected);
                return;
            }
            Intent intent2 = new Intent(this, (Class<?>) SendMessageActivity.class);
            intent2.putExtra("model", (short) 1);
            intent2.putExtra("routeid", this.routeId);
            startActivity(intent2);
        }
        if (view.getId() == this.titleBar.getCenterViewId()) {
            Intent intent3 = new Intent(this, (Class<?>) FriendsMessageInfoActivity.class);
            intent3.putExtra("userId", this.userId);
            startActivity(intent3);
            return;
        }
        if (view.getId() == this.titleBar.getRightViewId()) {
            View viewInflate = LayoutInflater.from(this).inflate(R.layout.popup_shareboard, (ViewGroup) null, false);
            PopupWindow popupWindow = new PopupWindow(viewInflate, -1, ViewUtils.dpToPx(120));
            popupWindow.setContentView(viewInflate);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.showAtLocation(view, 80, 0, 0);
            backgroundAlpha(0.8f);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.MessageDetailActivity.2
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    MessageDetailActivity.this.backgroundAlpha(1.0f);
                }
            });
            this.ivWeixinShare = (ImageView) viewInflate.findViewById(R.id.iv_weixin_share);
            this.ivWexinShareTimeLine = (ImageView) viewInflate.findViewById(R.id.iv_weixin_share_timeline);
            this.ivFaceBookShare = (ImageView) viewInflate.findViewById(R.id.iv_facebook_share);
            this.ivWeixinShare.setOnClickListener(this);
            this.ivWexinShareTimeLine.setOnClickListener(this);
            this.ivFaceBookShare.setOnClickListener(this);
            return;
        }
        if (view.equals(this.ivWeixinShare)) {
            if (!MyApplication.getWxApi().isWXAppInstalled()) {
                ViewUtils.showSnackBar((View) this.ivWeixinShare, R.string.you_need_install_wx, true);
                return;
            } else {
                ShareUtils.shareToWx(false, this.shareurl, this.mapBitmap, this.description, this.distance + "", this.waypointCount);
                return;
            }
        }
        if (view.equals(this.ivWexinShareTimeLine)) {
            if (!MyApplication.getWxApi().isWXAppInstalled()) {
                ViewUtils.showSnackBar((View) this.ivWeixinShare, R.string.you_need_install_wx, true);
                return;
            } else {
                ShareUtils.shareToWx(true, this.shareurl, this.mapBitmap, this.description, this.distance + "", this.waypointCount);
                return;
            }
        }
        if (view.equals(this.ivFaceBookShare)) {
            ShareUtils.shareToFaceBook(this.shareurl, this.mapUrl, this.description, this.distance + "", this.waypointCount);
        }
    }

    @Subscribe
    public void onEventMainThread(LikeRouteSucessMessage likeRouteSucessMessage) {
        this.isLike = true;
        this.ivMyrouteHeartLike.setImageResource(R.drawable.icon_redheart);
    }

    @Subscribe
    public void onEventMainThread(SendCommentSuccessMessage sendCommentSuccessMessage) {
        int i = this.commentCount + 1;
        this.commentCount = i;
        this.tvItemMyFollowingComment.setText(i == 0 ? getString(R.string.Comment) : this.commentCount + "");
    }

    public void backgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = f;
        getWindow().setAttributes(attributes);
    }
}
