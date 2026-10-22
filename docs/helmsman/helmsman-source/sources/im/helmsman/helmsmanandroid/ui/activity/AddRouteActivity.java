package im.helmsman.helmsmanandroid.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import butterknife.BindView;
import com.MAVLink.enums.MAV_COMPONENT;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Polyline;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SaveLocalRouteCompleteMessage;
import im.helmsman.helmsmanandroid.presenter.AddRoutePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.AddRouteView;
import im.helmsman.helmsmanandroid.utils.ConnectivityUtil;
import im.helmsman.helmsmanandroid.utils.CoordinateParserUtil;
import im.helmsman.helmsmanandroid.utils.CoordinateTransform;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.MapUtil;
import im.helmsman.helmsmanandroid.utils.SphericalUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Publisher;

/* JADX INFO: loaded from: classes2.dex */
public class AddRouteActivity extends Mvp2BaseActivity<AddRouteView, AddRoutePresenter> implements View.OnClickListener, AddRouteView, AMap.OnMarkerClickListener, AMap.OnMapLongClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, AMap.OnMarkerDragListener, GoogleMap.OnMarkerDragListener, GoogleMap.InfoWindowAdapter, AMap.InfoWindowAdapter, GoogleMap.OnMapLoadedCallback {
    public static final int BOAT_POSITION_STATE = 11;
    private Marker aBoatMarker;
    private BitmapDescriptor aDescriptor;
    private AMap amap;
    private Bitmap copyBitmap;
    int dragEndid;
    private com.google.android.gms.maps.model.Marker gBoatMarker;
    private FrameLayout mFrameTop;
    private FrameLayout mFrameTop2;
    private GoogleMap mGoogleMap;
    private ImageView mImageDelete;
    private ImageView mImageDismiss;
    private ImageView mImageMapModeSatellite;
    private ImageView mImageReverse;
    private ImageView mIvLocateBoat;
    private ImageView mIvSaveWayPoint;
    private ImageView mIvSnapshot;
    private MLatLng mLatLng;
    private LinearLayout mLinSelectMarker;
    private View mMarkerInfo;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelUnder;
    private RelativeLayout mRelUnderContent;
    private HorizontalScrollView mScrollView;
    private TextView mTvBottomDis;
    private TextView mTvBottomLatitude;
    private TextView mTvBottomLeftNum;
    private TextView mTvBottomLongitude;
    private TextView mTvMarkerDis;
    private View popupView;

    @BindView(R.id.vs_gaode_map)
    ViewStub vsGaodeMap;

    @BindView(R.id.vs_google_map)
    ViewStub vsGoogleMap;
    private List<View> UnderMarkerChildViews = new ArrayList();
    private List<Polyline> gPolylines = new ArrayList();
    private List<com.amap.api.maps.model.Polyline> aPolylines = new ArrayList();
    private List<com.google.android.gms.maps.model.Marker> gMarkers = new ArrayList();
    private List<Marker> aMarkers = new ArrayList();
    private boolean isMoveCamera = false;
    private List<MLatLng> points = new ArrayList();
    private int mWayPointListid = -1;
    private Handler h = new Handler();
    private boolean isChange = false;
    private boolean isFirst = true;

    public interface OnSaveWayPointListener {
        void onSaveComplete(int i);
    }

    @Override // com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
    public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
        return null;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void nowifiError() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public AddRoutePresenter initPresenter() {
        return new AddRoutePresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_route);
        this.mWayPointListid = getIntent().getIntExtra("id", -1);
        initView();
        initEvent();
        initMap();
    }

    private void initView() {
        this.mIvLocateBoat = (ImageView) findViewById(R.id.iv_add_route_map_route_locateboat);
        this.mImageDismiss = (ImageView) findViewById(R.id.iv_add_route_black_dismiss);
        this.mImageMapModeSatellite = (ImageView) findViewById(R.id.iv_add_route_map_mode_satellite);
        this.mScrollView = (HorizontalScrollView) findViewById(R.id.sc_add_route_under);
        this.mLinSelectMarker = (LinearLayout) findViewById(R.id.lin_add_route_select_marker);
        this.mTvBottomLeftNum = (TextView) findViewById(R.id.tv_add_route_under_left_num);
        this.mTvBottomLongitude = (TextView) findViewById(R.id.tv_add_route_under_longitude);
        this.mTvBottomLatitude = (TextView) findViewById(R.id.tv_add_route_under_latitude);
        this.mTvBottomDis = (TextView) findViewById(R.id.tv_add_route_under_dis);
        this.mIvSnapshot = (ImageView) findViewById(R.id.iv_add_route_start_autopilot);
        this.mIvSaveWayPoint = (ImageView) findViewById(R.id.iv_add_route_save);
        this.mImageReverse = (ImageView) findViewById(R.id.iv_add_route_reverse);
        View viewInflate = View.inflate(this, R.layout.item_add_route_under_marker_info, null);
        this.mMarkerInfo = viewInflate;
        this.mTvMarkerDis = (TextView) viewInflate.findViewById(R.id.tv_add_route_item_dis);
        this.mRelUnder = (RelativeLayout) findViewById(R.id.rel_add_route_under);
        this.mImageDelete = (ImageView) findViewById(R.id.iv_add_route_delete);
        this.mRelUnderContent = (RelativeLayout) findViewById(R.id.rel_add_route_under_bottom);
        this.mFrameTop = (FrameLayout) findViewById(R.id.frame_add_route_top);
        this.mFrameTop2 = (FrameLayout) findViewById(R.id.frame_add_route_top2);
        this.popupView = getLayoutInflater().inflate(R.layout.item_custom_progress_dialog, (ViewGroup) null);
        this.mPopupWindow = new PopupWindow(this.popupView, -2, -2, false);
    }

    private void initEvent() {
        this.mIvLocateBoat.setOnClickListener(this);
        this.mImageMapModeSatellite.setOnClickListener(this);
        this.mIvSaveWayPoint.setOnClickListener(this);
        this.mImageDismiss.setOnClickListener(this);
        this.mIvSnapshot.setOnClickListener(this);
        this.mImageDelete.setOnClickListener(this);
        this.mImageReverse.setOnClickListener(this);
        this.mRelUnderContent.setOnClickListener(null);
        this.mScrollView.getBackground().setAlpha(MAV_COMPONENT.MAV_COMP_ID_SERVO11);
        this.mFrameTop.setOnClickListener(this);
        this.mFrameTop2.setOnClickListener(null);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        PopupWindow popupWindow;
        AMap aMap;
        if (view.equals(this.mIvLocateBoat)) {
            moveToBoatLocation();
            return;
        }
        if (view.equals(this.mImageReverse)) {
            Collections.reverse(this.points);
            cleanAllMarkers();
            drawLineAndPointToGoogleMap(MLatLng.toGoogleLatlngs(this.points), null);
            drawLineAndPointToAMap(MLatLng.toAmapLatlngs(this.points), null);
            this.mRelUnder.setVisibility(8);
            return;
        }
        if (view.equals(this.mImageDismiss)) {
            onBackPressed();
            return;
        }
        if (view.equals(this.mImageMapModeSatellite)) {
            int i = Config.MAP_TYPE;
            if (i != 1) {
                if (i == 2 && (aMap = this.amap) != null) {
                    if (aMap.getMapType() == 1) {
                        this.amap.setMapType(2);
                        this.mImageMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_stander);
                        return;
                    } else {
                        this.amap.setMapType(1);
                        this.mImageMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_satellite);
                        return;
                    }
                }
                return;
            }
            GoogleMap googleMap = this.mGoogleMap;
            if (googleMap != null) {
                if (googleMap.getMapType() == 1) {
                    this.mGoogleMap.setMapType(2);
                    this.mImageMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_stander);
                    return;
                } else {
                    this.mGoogleMap.setMapType(1);
                    this.mImageMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_satellite);
                    return;
                }
            }
            return;
        }
        if (view.equals(this.mImageDelete)) {
            for (int size = this.UnderMarkerChildViews.size() - 1; size >= 0; size--) {
                if (((FrameLayout) this.UnderMarkerChildViews.get(size).findViewById(R.id.frame_add_route_mark_under_bg)).getVisibility() == 0) {
                    this.points.remove(size);
                }
            }
            removeAllUnderMarker();
            drawLineAndPointToGoogleMap(MLatLng.toGoogleLatlngs(this.points), null);
            drawLineAndPointToAMap(MLatLng.toAmapLatlngs(this.points), null);
            this.mRelUnder.setVisibility(8);
            this.isChange = true;
            return;
        }
        if (view.equals(this.mIvSnapshot)) {
            if (this.points.size() <= 0) {
                ViewUtils.ShowToast(R.string.route_no_route);
                return;
            } else if (MyApplication.getInstance().wifiState) {
                this.mFrameTop.setVisibility(0);
                ((AddRoutePresenter) this.presenter).uploadWayPoint(this.points, this.mWayPointListid);
                return;
            } else {
                ViewUtils.ShowToast(R.string.main_no_boat_connected_message);
                return;
            }
        }
        if (view.equals(this.mIvSaveWayPoint)) {
            List<MLatLng> list = this.points;
            if (list != null && list.size() == 0) {
                ViewUtils.ShowToast(R.string.route_no_route);
                return;
            } else {
                ViewUtils.showProgressDialog(R.string.loading).setCancelable(false);
                saveWayPoint(new OnSaveWayPointListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.1
                    @Override // im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.OnSaveWayPointListener
                    public void onSaveComplete(int i2) {
                        ViewUtils.cancelProgressDialog();
                        AddRouteActivity.this.backToMyFollowingRouteActivity();
                    }
                });
                return;
            }
        }
        if (view.equals(this.mFrameTop)) {
            if (!isFinishing() && (popupWindow = this.mPopupWindow) != null && popupWindow.isShowing()) {
                this.mPopupWindow.dismiss();
            }
            this.mFrameTop.setVisibility(8);
            this.h.removeCallbacksAndMessages(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveWayPoint(final OnSaveWayPointListener onSaveWayPointListener) {
        MapUtil.moveToCenter(this.amap, this.mGoogleMap, this.points);
        if (checkPolyLineDisIsMoreThan2Km()) {
            new AlertDialog.Builder(getTopActivity()).setTitle(R.string.addroute_waypoint_distance_greater_than_2km).setNegativeButton(R.string.addroute_re_edit, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.addroute_ignore, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddRouteActivity.this.screenShotAndSave(onSaveWayPointListener);
                }
            }).create().show();
        } else {
            screenShotAndSave(onSaveWayPointListener);
        }
    }

    private Bitmap viewToNumBitMap(int i) {
        View viewInflate = View.inflate(this, R.layout.item_add_route_under_marker, null);
        ((TextView) viewInflate.findViewById(R.id.tv_add_route_marker)).setText(String.valueOf(i));
        return ViewUtils.getViewBitmap(viewInflate);
    }

    private void initMap() {
        int i = Config.MAP_TYPE;
        if (i == 1) {
            this.vsGoogleMap.inflate();
            ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMapAsync(this);
            return;
        }
        if (i != 2) {
            return;
        }
        this.vsGaodeMap.inflate();
        com.amap.api.maps.MapFragment mapFragment = (com.amap.api.maps.MapFragment) getFragmentManager().findFragmentById(R.id.gaode_map);
        mapFragment.onCreate(null);
        AMap map = mapFragment.getMap();
        this.amap = map;
        map.getUiSettings().setRotateGesturesEnabled(false);
        this.amap.setOnMapLongClickListener(this);
        this.amap.setOnMarkerClickListener(this);
        this.amap.setOnMarkerDragListener(this);
        this.amap.setInfoWindowAdapter(this);
        ((AddRoutePresenter) this.presenter).drawMapById(this.mWayPointListid);
    }

    private void moveToBoatLocation() {
        Marker marker = this.aBoatMarker;
        if (marker == null && this.gBoatMarker == null) {
            return;
        }
        AMap aMap = this.amap;
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14.0f));
        } else {
            this.mGoogleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(this.gBoatMarker.getPosition(), 14.0f));
        }
    }

    @Override // com.google.android.gms.maps.OnMapReadyCallback
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        googleMap.setInfoWindowAdapter(this);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        ((AddRoutePresenter) this.presenter).drawMapById(this.mWayPointListid);
        if (this.gMarkers.size() > 0) {
            if (ConnectivityUtil.isConnect() && this.gMarkers.size() > 1) {
                googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(MapUtil.buildLatLngBound(this.gMarkers).getCenter(), 100.0f));
                this.isMoveCamera = true;
                googleMap.setOnMapLoadedCallback(this);
                return;
            }
            googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(this.gMarkers.get(0).getPosition(), 100.0f));
        }
    }

    @Override // com.amap.api.maps.AMap.OnMapLongClickListener
    public void onMapLongClick(LatLng latLng) {
        this.isChange = true;
        MLatLng mLatLng = new MLatLng(latLng);
        View viewInflate = View.inflate(this, R.layout.item_add_route_under_marker, null);
        if (!this.points.isEmpty()) {
            List<MLatLng> list = this.points;
            MLatLng mLatLng2 = list.get(list.size() - 1);
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(mLatLng2.toAmapLatlng(), latLng);
            com.amap.api.maps.model.Polyline polylineAddPolyline = this.amap.addPolyline(polylineOptions);
            if (SphericalUtil.computeDistanceBetween(mLatLng2, MLatLng.toMLatLng(latLng)) > 2000.0d) {
                polylineAddPolyline.setColor(SupportMenu.CATEGORY_MASK);
            } else {
                polylineAddPolyline.setColor(-16776961);
            }
            this.aPolylines.add(polylineAddPolyline);
        }
        this.points.add(mLatLng);
        ((TextView) viewInflate.findViewById(R.id.tv_add_route_marker)).setText(String.valueOf(this.points.size()));
        Bitmap viewBitmap = ViewUtils.getViewBitmap(viewInflate);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatLng.toAmapLatlng());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(viewBitmap));
        Marker markerAddMarker = this.amap.addMarker(markerOptions);
        markerAddMarker.setDraggable(true);
        this.aMarkers.add(markerAddMarker);
        addMarkInLinearLayout(this.points.size());
        viewBitmap.recycle();
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
    public void onMapLongClick(com.google.android.gms.maps.model.LatLng latLng) {
        this.isChange = true;
        MLatLng mLatLng = new MLatLng((float) latLng.latitude, (float) latLng.longitude);
        View viewInflate = View.inflate(this, R.layout.item_add_route_under_marker, null);
        if (!this.points.isEmpty()) {
            List<MLatLng> list = this.points;
            MLatLng mLatLng2 = list.get(list.size() - 1);
            Polyline polylineAddPolyline = this.mGoogleMap.addPolyline(new com.google.android.gms.maps.model.PolylineOptions().add(mLatLng2.toGoogleLatlng(), latLng).width(8.0f).color(-16776961));
            if (SphericalUtil.computeDistanceBetween(mLatLng2, MLatLng.toMLatLng(latLng)) > 2000.0d) {
                polylineAddPolyline.setColor(SupportMenu.CATEGORY_MASK);
            }
            this.gPolylines.add(polylineAddPolyline);
        }
        this.points.add(mLatLng);
        ((TextView) viewInflate.findViewById(R.id.tv_add_route_marker)).setText(String.valueOf(this.points.size()));
        Bitmap viewBitmap = ViewUtils.getViewBitmap(viewInflate);
        com.google.android.gms.maps.model.Marker markerAddMarker = this.mGoogleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(mLatLng.toGoogleLatlng()).icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(viewBitmap)));
        markerAddMarker.setDraggable(true);
        this.gMarkers.add(markerAddMarker);
        addMarkInLinearLayout(this.points.size());
        viewBitmap.recycle();
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
        this.mRelUnder.setVisibility(0);
        int iIndexOf = this.gMarkers.indexOf(marker);
        if (iIndexOf == -1) {
            Log.e("error", "出现-1的问题了,gMarkers:" + this.gMarkers.size() + "UnderMarkerChildViews" + this.UnderMarkerChildViews.size());
            return true;
        }
        ((FrameLayout) this.UnderMarkerChildViews.get(iIndexOf).findViewById(R.id.frame_add_route_mark_under_bg)).setVisibility(0);
        setLinUnderDate(iIndexOf);
        this.mGoogleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }

    private void removeAllUnderMarker() {
        Iterator<View> it = this.UnderMarkerChildViews.iterator();
        while (it.hasNext()) {
            this.mLinSelectMarker.removeView(it.next());
        }
        this.UnderMarkerChildViews.clear();
    }

    private void addMarkInLinearLayout(int i) {
        View viewInflate = View.inflate(this, R.layout.item_add_route_under_marker, null);
        this.UnderMarkerChildViews.add(viewInflate);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_add_route_marker);
        textView.setText(String.valueOf(i));
        viewInflate.setTag(textView);
        final FrameLayout frameLayout = (FrameLayout) viewInflate.findViewById(R.id.frame_add_route_mark_under_bg);
        viewInflate.setLayoutParams(new ViewGroup.LayoutParams(MAV_COMPONENT.MAV_COMP_ID_SERVO11, MAV_COMPONENT.MAV_COMP_ID_SERVO11));
        viewInflate.setBackgroundResource(R.drawable.icon_add_route_mark_green);
        viewInflate.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (frameLayout.getVisibility() == 8) {
                    frameLayout.setVisibility(0);
                    int iIntValue = Integer.valueOf((String) ((TextView) view.getTag()).getText()).intValue() - 1;
                    AddRouteActivity.this.setLinUnderDate(iIntValue);
                    if (Config.MAP_TYPE == 1) {
                        AddRouteActivity.this.mGoogleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(((MLatLng) AddRouteActivity.this.points.get(iIntValue)).toGoogleLatlng(), AddRouteActivity.this.mGoogleMap.getCameraPosition().zoom));
                        return;
                    } else {
                        AddRouteActivity.this.amap.moveCamera(CameraUpdateFactory.changeLatLng(((MLatLng) AddRouteActivity.this.points.get(iIntValue)).toAmapLatlng()));
                        return;
                    }
                }
                frameLayout.setVisibility(8);
            }
        });
        this.mLinSelectMarker.addView(viewInflate);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
    public void onMarkerDragStart(com.google.android.gms.maps.model.Marker marker) {
        this.isChange = true;
        this.mFrameTop2.setVisibility(0);
        for (int i = 0; i < this.gPolylines.size(); i++) {
            if (this.gPolylines.get(i).getColor() == -16711936) {
                this.gPolylines.get(i).setColor(-16776961);
            }
        }
        this.mRelUnder.setVisibility(8);
        marker.setAlpha(0.5f);
        setGFollowLine(marker);
        this.dragEndid = this.gMarkers.indexOf(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
    public void onMarkerDrag(com.google.android.gms.maps.model.Marker marker) {
        this.mFrameTop2.setVisibility(0);
        setGFollowLine(marker);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
    public void onMarkerDragEnd(com.google.android.gms.maps.model.Marker marker) {
        this.mFrameTop2.setVisibility(8);
        setGFollowLine(marker);
        marker.hideInfoWindow();
        cleanAllMarkers();
        drawLineAndPointToGoogleMap(MLatLng.toGoogleLatlngs(this.points), null);
        this.mRelUnder.setVisibility(0);
        marker.setAlpha(1.0f);
        int i = this.dragEndid;
        if (i > 0) {
            Polyline polyline = this.gPolylines.get(i - 1);
            this.mTvMarkerDis.setText(CoordinateParserUtil.distanceParser(SphericalUtil.computeDistanceBetween(MLatLng.toMLatLng(polyline.getPoints().get(0)), MLatLng.toMLatLng(polyline.getPoints().get(1)))));
        }
        setLinUnderDate(this.dragEndid);
    }

    @Override // com.amap.api.maps.AMap.OnMarkerDragListener
    public void onMarkerDragStart(Marker marker) {
        this.isChange = true;
        this.mFrameTop2.setVisibility(0);
        for (int i = 0; i < this.aPolylines.size(); i++) {
            if (this.aPolylines.get(i).getColor() == -16711936) {
                this.aPolylines.get(i).setColor(-16776961);
            }
        }
        this.mRelUnder.setVisibility(8);
        marker.setAlpha(0.5f);
        setAFollowLine(marker);
        this.dragEndid = this.aMarkers.indexOf(marker);
    }

    @Override // com.amap.api.maps.AMap.OnMarkerDragListener
    public void onMarkerDrag(Marker marker) {
        this.mFrameTop2.setVisibility(0);
        setAFollowLine(marker);
    }

    @Override // com.amap.api.maps.AMap.OnMarkerDragListener
    public void onMarkerDragEnd(Marker marker) {
        this.mFrameTop2.setVisibility(8);
        setAFollowLine(marker);
        marker.hideInfoWindow();
        cleanAllMarkers();
        drawLineAndPointToAMap(MLatLng.toAmapLatlngs(this.points), null);
        this.mRelUnder.setVisibility(0);
        marker.setAlpha(1.0f);
        int i = this.dragEndid;
        if (i > 0) {
            com.amap.api.maps.model.Polyline polyline = this.aPolylines.get(i - 1);
            this.mTvMarkerDis.setText(CoordinateParserUtil.distanceParser(SphericalUtil.computeDistanceBetween(MLatLng.toMLatLng(polyline.getPoints().get(0)), MLatLng.toMLatLng(polyline.getPoints().get(1)))));
        }
        setLinUnderDate(this.dragEndid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLinUnderDate(int i) {
        MLatLng mLatLng;
        MLatLng mLatLng2;
        this.mTvBottomLeftNum.setText(String.valueOf(i + 1));
        double d = this.points.get(i).longitude;
        this.mTvBottomLatitude.setText(CoordinateParserUtil.latitudeParser(this.points.get(i).latitude));
        this.mTvBottomLongitude.setText(CoordinateParserUtil.longitudeParser(d));
        if (i != 0) {
            if (Config.MAP_TYPE == 1) {
                Polyline polyline = this.gPolylines.get(i - 1);
                mLatLng = new MLatLng(polyline.getPoints().get(0));
                mLatLng2 = new MLatLng(polyline.getPoints().get(1));
            } else {
                com.amap.api.maps.model.Polyline polyline2 = this.aPolylines.get(i - 1);
                mLatLng = new MLatLng(polyline2.getPoints().get(0));
                mLatLng2 = new MLatLng(polyline2.getPoints().get(1));
            }
            this.mTvBottomDis.setText(CoordinateParserUtil.distanceParser(SphericalUtil.computeDistanceBetween(mLatLng, mLatLng2)));
            return;
        }
        this.mTvBottomDis.setText(CoordinateParserUtil.distanceParser(0.0d));
    }

    private void setGFollowLine(com.google.android.gms.maps.model.Marker marker) {
        com.google.android.gms.maps.model.LatLng position = marker.getPosition();
        int iIndexOf = this.gMarkers.indexOf(marker);
        if (iIndexOf > 0) {
            int i = iIndexOf - 1;
            Polyline polyline = this.gPolylines.get(i);
            polyline.remove();
            polyline.getPoints().set(1, position);
            Polyline polylineAddPolyline = this.mGoogleMap.addPolyline(new com.google.android.gms.maps.model.PolylineOptions().add(polyline.getPoints().get(0), position).width(8.0f).color(-16711936));
            this.gPolylines.set(i, polylineAddPolyline);
            double dComputeDistanceBetween = SphericalUtil.computeDistanceBetween(MLatLng.toMLatLng(polylineAddPolyline.getPoints().get(0)), MLatLng.toMLatLng(polylineAddPolyline.getPoints().get(1)));
            if (dComputeDistanceBetween > 2000.0d) {
                polylineAddPolyline.setColor(SupportMenu.CATEGORY_MASK);
            }
            this.mTvMarkerDis.setText(CoordinateParserUtil.distanceParser(dComputeDistanceBetween));
        }
        if (iIndexOf == 0) {
            if (this.gPolylines.size() > 0) {
                Polyline polyline2 = this.gPolylines.get(0);
                this.mTvMarkerDis.setText(CoordinateParserUtil.distanceParser(SphericalUtil.computeDistanceBetween(MLatLng.toMLatLng(polyline2.getPoints().get(0)), MLatLng.toMLatLng(polyline2.getPoints().get(1)))));
            } else {
                this.mTvMarkerDis.setText("0.0 KM");
            }
        }
        if (iIndexOf < this.gPolylines.size()) {
            Polyline polyline3 = this.gPolylines.get(iIndexOf);
            polyline3.remove();
            polyline3.getPoints().set(0, position);
            Polyline polylineAddPolyline2 = this.mGoogleMap.addPolyline(new com.google.android.gms.maps.model.PolylineOptions().add(position, polyline3.getPoints().get(1)).width(8.0f).color(-16711936));
            this.gPolylines.set(iIndexOf, polylineAddPolyline2);
            if (SphericalUtil.computeDistanceBetween(MLatLng.toMLatLng(position), MLatLng.toMLatLng(polylineAddPolyline2.getPoints().get(1))) > 2000.0d) {
                polylineAddPolyline2.setColor(SupportMenu.CATEGORY_MASK);
            }
        }
        this.points.set(iIndexOf, MLatLng.toMLatLng(position));
        marker.showInfoWindow();
    }

    private void setAFollowLine(Marker marker) {
        LatLng position = marker.getPosition();
        int iIndexOf = this.aMarkers.indexOf(marker);
        if (iIndexOf > 0) {
            int i = iIndexOf - 1;
            com.amap.api.maps.model.Polyline polyline = this.aPolylines.get(i);
            polyline.remove();
            polyline.getPoints().set(1, position);
            com.amap.api.maps.model.Polyline polylineAddPolyline = this.amap.addPolyline(new PolylineOptions().add(polyline.getPoints().get(0), position).width(8.0f).color(-16711936));
            this.aPolylines.set(i, polylineAddPolyline);
            double dComputeDistanceBetween = SphericalUtil.computeDistanceBetween(MLatLng.toMLatLng(polylineAddPolyline.getPoints().get(0)), MLatLng.toMLatLng(polylineAddPolyline.getPoints().get(1)));
            if (dComputeDistanceBetween > 2000.0d) {
                polylineAddPolyline.setColor(SupportMenu.CATEGORY_MASK);
            }
            this.mTvMarkerDis.setText(CoordinateParserUtil.distanceParser(dComputeDistanceBetween));
        }
        if (iIndexOf < this.aPolylines.size()) {
            com.amap.api.maps.model.Polyline polyline2 = this.aPolylines.get(iIndexOf);
            polyline2.remove();
            polyline2.getPoints().set(0, position);
            com.amap.api.maps.model.Polyline polylineAddPolyline2 = this.amap.addPolyline(new PolylineOptions().add(position, polyline2.getPoints().get(1)).width(8.0f).color(-16711936));
            this.aPolylines.set(iIndexOf, polylineAddPolyline2);
            if (SphericalUtil.computeDistanceBetween(MLatLng.toMLatLng(position), MLatLng.toMLatLng(polylineAddPolyline2.getPoints().get(1))) > 2000.0d) {
                polylineAddPolyline2.setColor(SupportMenu.CATEGORY_MASK);
            }
        }
        if (iIndexOf == 0) {
            this.mTvMarkerDis.setText("0.0 KM");
        }
        this.points.set(iIndexOf, MLatLng.toMLatLng(position));
        marker.showInfoWindow();
    }

    private boolean checkPolyLineDisIsMoreThan2Km() {
        for (int i = 1; i < this.points.size(); i++) {
            if (SphericalUtil.computeDistanceBetween(this.points.get(i - 1), this.points.get(i)) > 2000.0d) {
                return true;
            }
        }
        return false;
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void screenShotAndSave(final OnSaveWayPointListener onSaveWayPointListener) {
        this.points = CoordinateTransform.latLngGCJ024ToWGS84(this.points);
        int i = Config.MAP_TYPE;
        if (i == 1) {
            com.google.android.gms.maps.model.Marker marker = this.gBoatMarker;
            if (marker != null) {
                marker.setVisible(false);
            }
            this.mGoogleMap.snapshot(new GoogleMap.SnapshotReadyCallback() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.5
                @Override // com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
                public void onSnapshotReady(Bitmap bitmap) {
                    if (AddRouteActivity.this.gBoatMarker != null) {
                        AddRouteActivity.this.gBoatMarker.setVisible(true);
                    }
                    Flowable.just(bitmap).flatMap(new Function<Bitmap, Publisher<Bitmap>>() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.5.2
                        @Override // io.reactivex.functions.Function
                        public Publisher<Bitmap> apply(Bitmap bitmap2) throws Exception {
                            AddRouteActivity.this.finish();
                            if (AddRouteActivity.this.mWayPointListid == -1) {
                                AddRouteActivity.this.mWayPointListid = DaoUtil.getInstance().saveWayPointList(AddRouteActivity.this.points, bitmap2);
                            } else {
                                AddRouteActivity.this.mWayPointListid = DaoUtil.getInstance().coverSaveWayPointList(AddRouteActivity.this.mWayPointListid, AddRouteActivity.this.points, bitmap2);
                            }
                            bitmap2.recycle();
                            return Flowable.just(bitmap2);
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.5.1
                        @Override // io.reactivex.functions.Consumer
                        public void accept(Bitmap bitmap2) throws Exception {
                            if (onSaveWayPointListener != null) {
                                onSaveWayPointListener.onSaveComplete(AddRouteActivity.this.mWayPointListid);
                            }
                            EventBus.getDefault().post(new SaveLocalRouteCompleteMessage());
                        }
                    });
                }
            });
            return;
        }
        if (i != 2) {
            return;
        }
        Marker marker2 = this.aBoatMarker;
        if (marker2 != null) {
            marker2.setVisible(false);
        }
        this.amap.getMapScreenShot(new AMap.OnMapScreenShotListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.4
            @Override // com.amap.api.maps.AMap.OnMapScreenShotListener
            public void onMapScreenShot(Bitmap bitmap, int i2) {
            }

            @Override // com.amap.api.maps.AMap.OnMapScreenShotListener
            public void onMapScreenShot(Bitmap bitmap) {
                if (AddRouteActivity.this.aBoatMarker != null) {
                    AddRouteActivity.this.aBoatMarker.setVisible(true);
                }
                Flowable.just(bitmap).flatMap(new Function<Bitmap, Publisher<Bitmap>>() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.4.2
                    @Override // io.reactivex.functions.Function
                    public Publisher<Bitmap> apply(Bitmap bitmap2) throws Exception {
                        if (AddRouteActivity.this.mWayPointListid == -1) {
                            AddRouteActivity.this.mWayPointListid = DaoUtil.getInstance().saveWayPointList(AddRouteActivity.this.points, bitmap2);
                        } else {
                            AddRouteActivity.this.mWayPointListid = DaoUtil.getInstance().coverSaveWayPointList(AddRouteActivity.this.mWayPointListid, AddRouteActivity.this.points, bitmap2);
                        }
                        bitmap2.recycle();
                        AddRouteActivity.this.finish();
                        return Flowable.just(bitmap2);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.4.1
                    @Override // io.reactivex.functions.Consumer
                    public void accept(Bitmap bitmap2) throws Exception {
                        if (onSaveWayPointListener != null) {
                            onSaveWayPointListener.onSaveComplete(AddRouteActivity.this.mWayPointListid);
                        }
                        EventBus.getDefault().post(new SaveLocalRouteCompleteMessage());
                    }
                });
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void showTooFarMessage() {
        ViewUtils.showSnackBar((View) this.mImageDelete, R.string.two_point_toofar, true);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void showOrignTooFarMessage() {
        ViewUtils.ShowToast(R.string.pulltorefreshlistadapter_toofar);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void showProgressDialog(String str) {
        ViewUtils.showProgressDialog(getString(R.string.route_uploading));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void updateProgressMessage(String str) {
        ViewUtils.setProgressDialogText(getString(R.string.route_uploading) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void dismissProgress() {
        ViewUtils.cancelProgressDialog();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void uploadFailureAndRetry() {
        ViewUtils.showAlertDialog(R.string.timeout, R.string.faile_and_retry, R.string.app_ok, 0, (DialogInterface.OnClickListener) null);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void noGpsError() {
        ViewUtils.ShowToast(R.string.main_no_gps_data_received);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void markersToPoints() {
        if (this.points.size() != 0) {
            return;
        }
        int i = Config.MAP_TYPE;
        if (i == 1) {
            Iterator<com.google.android.gms.maps.model.Marker> it = this.gMarkers.iterator();
            while (it.hasNext()) {
                this.points.add(new MLatLng(it.next().getPosition()));
            }
            return;
        }
        if (i != 2) {
            return;
        }
        Iterator<Marker> it2 = this.aMarkers.iterator();
        while (it2.hasNext()) {
            this.points.add(new MLatLng(it2.next().getPosition()));
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void moveToMapCenter() {
        MapUtil.moveToCenter(this.amap, this.mGoogleMap, this.points);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void showMoreThan2KmDistanceDialog() {
        new AlertDialog.Builder(getTopActivity()).setTitle(R.string.addroute_waypoint_distance_greater_than_2km).setNegativeButton(R.string.addroute_re_edit, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.addroute_ignore, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AddRouteActivity.this.screenShotAndSave(null);
            }
        }).create().show();
    }

    public void backToMyFollowingRouteActivity() {
        finish();
    }

    @Override // com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
    public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
        return this.mMarkerInfo;
    }

    @Override // com.amap.api.maps.AMap.InfoWindowAdapter
    public View getInfoWindow(Marker marker) {
        return this.mMarkerInfo;
    }

    @Override // com.amap.api.maps.AMap.InfoWindowAdapter
    public View getInfoContents(Marker marker) {
        return this.mMarkerInfo;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
    public void onMapLoaded() {
        PopupWindow popupWindow;
        if (this.isMoveCamera) {
            if (ConnectivityUtil.isConnect()) {
                if (this.gMarkers.size() > 2) {
                    this.mGoogleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(MapUtil.buildLatLngBound(this.gMarkers), 300));
                } else if (this.gMarkers.size() == 2) {
                    MapUtil.UpdateMapCenter(this.mGoogleMap, this.gMarkers.get(0).getPosition());
                }
            } else if (this.gMarkers.size() > 2) {
                MapUtil.UpdateMapCenter(this.mGoogleMap, MapUtil.buildLatLngBound(this.gMarkers).getCenter());
            } else {
                MapUtil.UpdateMapCenter(this.mGoogleMap, this.gMarkers.get(0).getPosition());
            }
            this.isMoveCamera = false;
            return;
        }
        if (!isFinishing() && (popupWindow = this.mPopupWindow) != null && popupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
        screenShotAndSave(null);
        ViewUtils.cancelProgressDialog();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        PopupWindow popupWindow;
        super.onStop();
        if (!isFinishing() && (popupWindow = this.mPopupWindow) != null && popupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
        this.h.removeCallbacksAndMessages(null);
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMapClickListener
    public void onMapClick(com.google.android.gms.maps.model.LatLng latLng) {
        this.mRelUnder.setVisibility(8);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void drawLineAndPointToGoogleMap(List<com.google.android.gms.maps.model.LatLng> list, List<MLatLng> list2) {
        if (this.mGoogleMap == null) {
            return;
        }
        cleanAllMarkers();
        removeAllUnderMarker();
        if (list2 != null) {
            this.points = list2;
        }
        int i = 0;
        while (i < list.size()) {
            int i2 = i + 1;
            Bitmap bitmapViewToNumBitMap = viewToNumBitMap(i2);
            com.google.android.gms.maps.model.MarkerOptions markerOptions = new com.google.android.gms.maps.model.MarkerOptions();
            markerOptions.position(list.get(i));
            markerOptions.icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(bitmapViewToNumBitMap));
            com.google.android.gms.maps.model.Marker markerAddMarker = this.mGoogleMap.addMarker(markerOptions);
            markerAddMarker.setDraggable(true);
            this.gMarkers.add(markerAddMarker);
            addMarkInLinearLayout(i2);
            bitmapViewToNumBitMap.recycle();
            i = i2;
        }
        List<Polyline> list3 = this.gPolylines;
        if (list3 != null) {
            for (int size = list3.size() - 1; size >= 0; size--) {
                this.gPolylines.get(size).remove();
                this.gPolylines.remove(size);
            }
            for (int i3 = 1; i3 < list.size(); i3++) {
                com.google.android.gms.maps.model.LatLng latLng = list.get(i3 - 1);
                com.google.android.gms.maps.model.LatLng latLng2 = list.get(i3);
                com.google.android.gms.maps.model.PolylineOptions polylineOptions = new com.google.android.gms.maps.model.PolylineOptions();
                polylineOptions.add(latLng, latLng2);
                polylineOptions.width(8.0f);
                polylineOptions.color(-16776961);
                double dComputeDistanceBetween = SphericalUtil.computeDistanceBetween(new MLatLng(latLng), new MLatLng(latLng2));
                Polyline polylineAddPolyline = this.mGoogleMap.addPolyline(polylineOptions);
                if (dComputeDistanceBetween > 2000.0d) {
                    polylineAddPolyline.setColor(SupportMenu.CATEGORY_MASK);
                }
                this.gPolylines.add(polylineAddPolyline);
            }
        }
    }

    private void cleanAllMarkers() {
        if (this.amap != null) {
            Iterator<Marker> it = this.aMarkers.iterator();
            while (it.hasNext()) {
                it.next().remove();
            }
            this.aMarkers.clear();
            Iterator<com.amap.api.maps.model.Polyline> it2 = this.aPolylines.iterator();
            while (it2.hasNext()) {
                it2.next().remove();
            }
            this.aPolylines.clear();
        }
        if (this.mGoogleMap != null) {
            Iterator<com.google.android.gms.maps.model.Marker> it3 = this.gMarkers.iterator();
            while (it3.hasNext()) {
                it3.next().remove();
            }
            this.gMarkers.clear();
            Iterator<Polyline> it4 = this.gPolylines.iterator();
            while (it4.hasNext()) {
                it4.next().remove();
            }
            this.gPolylines.clear();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void drawLineAndPointToAMap(List<LatLng> list, List<MLatLng> list2) {
        if (this.amap == null) {
            return;
        }
        cleanAllMarkers();
        if (list2 != null) {
            this.points = list2;
        }
        removeAllUnderMarker();
        LatLngBounds latLngBounds = new LatLngBounds(MapUtil.findSouthWestLatLng(this.points).toAmapLatlng(), MapUtil.findNorthEastLatLng(this.points).toAmapLatlng());
        for (int i = 0; i < list.size(); i++) {
            latLngBounds.including(list.get(i));
        }
        if (list.size() >= 2) {
            this.amap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 1080, 1920, 300));
        } else if (list.isEmpty()) {
            return;
        } else {
            this.amap.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 15.0f));
        }
        int i2 = 0;
        while (i2 < list.size()) {
            int i3 = i2 + 1;
            Bitmap bitmapViewToNumBitMap = viewToNumBitMap(i3);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(list.get(i2));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapViewToNumBitMap));
            Marker markerAddMarker = this.amap.addMarker(markerOptions);
            markerAddMarker.setDraggable(true);
            this.aMarkers.add(markerAddMarker);
            addMarkInLinearLayout(i3);
            bitmapViewToNumBitMap.recycle();
            i2 = i3;
        }
        List<com.amap.api.maps.model.Polyline> list3 = this.aPolylines;
        if (list3 != null) {
            for (int size = list3.size() - 1; size >= 0; size--) {
                this.aPolylines.get(size).remove();
                this.aPolylines.remove(size);
            }
            for (int i4 = 1; i4 < list.size(); i4++) {
                LatLng latLng = list.get(i4 - 1);
                LatLng latLng2 = list.get(i4);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(latLng, latLng2);
                polylineOptions.width(8.0f);
                polylineOptions.color(-16776961);
                double dComputeDistanceBetween = SphericalUtil.computeDistanceBetween(new MLatLng(latLng), new MLatLng(latLng2));
                com.amap.api.maps.model.Polyline polylineAddPolyline = this.amap.addPolyline(polylineOptions);
                if (dComputeDistanceBetween > 2000.0d) {
                    polylineAddPolyline.setColor(SupportMenu.CATEGORY_MASK);
                }
                this.aPolylines.add(polylineAddPolyline);
            }
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void boatPosition(MLatLng mLatLng) {
        AMap aMap;
        MLatLng mLatLngWgs84ToGcj02 = CoordinateTransform.Wgs84ToGcj02(mLatLng);
        Bitmap bitmap = this.copyBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        int i = Config.MAP_TYPE;
        if (i != 1) {
            if (i == 2 && (aMap = this.amap) != null) {
                if (this.aBoatMarker == null) {
                    Marker markerAddMarker = aMap.addMarker(new MarkerOptions().position(mLatLngWgs84ToGcj02.toAmapLatlng()).icon(BitmapDescriptorFactory.fromBitmap(this.copyBitmap)));
                    this.aBoatMarker = markerAddMarker;
                    markerAddMarker.setAnchor(0.5f, 0.5f);
                    if (this.points.size() == 0) {
                        moveToBoatLocation();
                    }
                }
                this.aBoatMarker.setPosition(mLatLngWgs84ToGcj02.toAmapLatlng());
                return;
            }
            return;
        }
        GoogleMap googleMap = this.mGoogleMap;
        if (googleMap != null) {
            if (this.gBoatMarker == null) {
                com.google.android.gms.maps.model.Marker markerAddMarker2 = googleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(mLatLngWgs84ToGcj02.toGoogleLatlng()).icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(this.copyBitmap)));
                this.gBoatMarker = markerAddMarker2;
                markerAddMarker2.setAnchor(0.5f, 0.5f);
                if (this.points.size() == 0) {
                    moveToBoatLocation();
                }
            }
            this.gBoatMarker.setPosition(mLatLngWgs84ToGcj02.toGoogleLatlng());
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AddRouteView
    public void boatDirection(float f) {
        try {
            Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.icon_boat);
            Matrix matrix = new Matrix();
            matrix.setRotate(f, 0.5f, 0.5f);
            this.copyBitmap = Bitmap.createBitmap(bitmapDecodeResource, 0, 0, bitmapDecodeResource.getWidth(), bitmapDecodeResource.getHeight(), matrix, true);
            Matrix matrix2 = new Matrix();
            matrix2.setScale(0.4f, 0.4f);
            Bitmap bitmap = this.copyBitmap;
            this.copyBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), this.copyBitmap.getHeight(), matrix2, true);
            int i = Config.MAP_TYPE;
            if (i != 1) {
                if (i == 2 && this.aBoatMarker != null) {
                    BitmapDescriptor bitmapDescriptor = this.aDescriptor;
                    if (bitmapDescriptor != null) {
                        bitmapDescriptor.recycle();
                        this.aDescriptor = null;
                    }
                    BitmapDescriptor bitmapDescriptorFromBitmap = BitmapDescriptorFactory.fromBitmap(this.copyBitmap);
                    this.aDescriptor = bitmapDescriptorFromBitmap;
                    this.aBoatMarker.setIcon(bitmapDescriptorFromBitmap);
                    return;
                }
                return;
            }
            com.google.android.gms.maps.model.Marker marker = this.gBoatMarker;
            if (marker != null) {
                marker.setIcon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(this.copyBitmap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.amap.api.maps.AMap.OnMarkerClickListener
    public boolean onMarkerClick(Marker marker) {
        this.mRelUnder.setVisibility(0);
        int iIndexOf = this.aMarkers.indexOf(marker);
        if (iIndexOf == -1) {
            Log.e("error", "出现-1的问题了,gMarkers:" + this.gMarkers.size() + "UnderMarkerChildViews" + this.UnderMarkerChildViews.size());
            return true;
        }
        ((FrameLayout) this.UnderMarkerChildViews.get(iIndexOf).findViewById(R.id.frame_add_route_mark_under_bg)).setVisibility(0);
        setLinUnderDate(iIndexOf);
        this.amap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.isChange && this.points.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCustomTitle(View.inflate(this, R.layout.item_custom_dialog2_title, null));
            builder.setNegativeButton(R.string.addroute_discard, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddRouteActivity.this.finish();
                }
            });
            builder.setPositiveButton(R.string.addroute_save, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.8
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddRouteActivity.this.saveWayPoint(new OnSaveWayPointListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.8.1
                        @Override // im.helmsman.helmsmanandroid.ui.activity.AddRouteActivity.OnSaveWayPointListener
                        public void onSaveComplete(int i2) {
                            AddRouteActivity.this.finish();
                        }
                    });
                }
            });
            builder.show();
            return;
        }
        super.onBackPressed();
    }
}
