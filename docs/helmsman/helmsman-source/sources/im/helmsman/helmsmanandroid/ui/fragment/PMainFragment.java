package im.helmsman.helmsmanandroid.ui.fragment;

import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.MAVLink.common.msg_terrain_request;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import im.helmsman.helmsmanandroid.CustomListener.DoubleClickListener;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.controller.BaseIoController;
import im.helmsman.helmsmanandroid.controller.BleBlueBoatDataIOController;
import im.helmsman.helmsmanandroid.dao.BleBlueToothConnected;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.dao.WayPoint;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.UpLoadWayPointSuccess;
import im.helmsman.helmsmanandroid.presenter.MainPresenter;
import im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity;
import im.helmsman.helmsmanandroid.ui.activity.MainActivity;
import im.helmsman.helmsmanandroid.ui.activity.MyFollowingRouteActivity;
import im.helmsman.helmsmanandroid.ui.common.BaseFragment;
import im.helmsman.helmsmanandroid.ui.view.MainView;
import im.helmsman.helmsmanandroid.utils.CoordinateTransform;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.FastBlurUtil;
import im.helmsman.helmsmanandroid.utils.MapUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.AutoArcMenu;
import im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow;
import im.helmsman.helmsmanandroid.view.CtrlButton;
import im.helmsman.helmsmanandroid.view.LightImage;
import im.helmsman.helmsmanandroid.view.ProgressImageView;
import im.helmsman.helmsmanandroid.view.Rudder;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.mission.HMAnchorMission;
import im.helmsman.lib.mission.HMCruiseMission;
import im.helmsman.lib.mission.HMMissionManager;
import im.helmsman.lib.mission.HMNavigationMission;
import im.helmsman.lib.mission.HMRecordMission;
import im.helmsman.lib.mission.common.HMMission;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;
import org.reactivestreams.Publisher;

/* JADX INFO: loaded from: classes2.dex */
public class PMainFragment extends BaseFragment<MainView, MainPresenter> implements MainView, View.OnTouchListener, DrawerLayout.DrawerListener, View.OnClickListener, Rudder.RudderListener, OnMapReadyCallback, AMap.OnMapScreenShotListener, GoogleMap.SnapshotReadyCallback, BluetoothAdapter.LeScanCallback {
    static final int ANCHOR = 2;
    private static final int[] ITEM_DRAWABLES = {R.drawable.icon_main_record, R.drawable.icon_main_cruise, R.drawable.icon_main_navi, R.drawable.icon_main_anchor};
    static final int MAXSPEED = 1;
    private Marker aBoatMarker;
    private BitmapDescriptor aDescriptor;
    private AMap aMap;
    private MainActivity activity;
    private CheckGpsAndWifiPopuWindow checkGpsAndWifiPopuWindow;
    private Bitmap copyBitmap;
    private com.google.android.gms.maps.model.Marker gBoatMarker;
    private Handler handler;
    private Runnable longPressRunnable;
    private AutoArcMenu mArcMenu;
    private ImageButton mBtnEye;
    private ImageView mBtnMotor;
    private CtrlButton mCBBatteryState;
    private CtrlButton mCBModeState;
    private CtrlButton mCBWifiState;
    public DrawerLayout mDrawerLayout;
    private GoogleMap mGoogleMap;
    private CircularProgressBar mGpsCircular;
    private ImageView mImageDelete;
    private ImageView mImageRoute;
    private ImageView mImageSetting;
    private ImageView mImageViewMapModeSatellite;
    private ImageView mIvExitFunction;
    private ImageView mIvGpsSignal;
    private ImageView mIvLocateBoat;
    private ImageView mIvMotorSpeed;
    private LightImage mIvRecord;
    private ImageView mIvRedHeart;
    private ImageView mIvTrack;
    private ImageView mIvTransferStation;
    private LinearLayout mLinearLayoutEye;
    private RelativeLayout mRelTitleBar;
    private Rudder mRudder;
    private ImageView mScrubBacground;
    private TextView mTvSpeed;
    private TextView mTvTitle;
    private Runnable moveTimeOut;
    private MoveToScreenCentre moveToScreenCentreRunnable;
    private ObjectAnimator oa;
    private PopupWindow popupWindow;
    private RoundCornerProgressBar progressBar;
    private ProgressImageView progressImageView;
    private View view;
    private ViewStub vsGaoDeMap;
    private ViewStub vsGoogleMap;
    private long mMotorFirstClickTime = 0;
    private List<Polyline> gPolylines = new ArrayList();
    private List<com.amap.api.maps.model.Polyline> aPolylines = new ArrayList();
    private List<MLatLng> mLatlngs = new ArrayList();
    private List<com.google.android.gms.maps.model.Marker> gMarkers = new ArrayList();
    private List<Marker> aMarker = new ArrayList();
    private boolean isTrack = true;
    private int modelCode = 0;
    private boolean isFlickHeart = false;
    private boolean isMove = false;
    private boolean isConnect = false;
    private float zoom = 14.0f;
    private long longPressTimer = 0;
    private boolean isInitPress = true;
    private boolean isShow = false;
    private int flag = 0;

    @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
    public void onDrawerSlide(View view, float f) {
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
    public void onDrawerStateChanged(int i) {
    }

    @Override // com.amap.api.maps.AMap.OnMapScreenShotListener
    public void onMapScreenShot(Bitmap bitmap, int i) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void onServerCreate(BaseIoController baseIoController) {
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        this.handler = new Handler();
        this.activity = (MainActivity) getActivity();
        this.view = layoutInflater.inflate(R.layout.pactivity_main, viewGroup, false);
        initView();
        initEvent();
        registerEventBus();
        this.longPressRunnable = new CheckLongPressRunnable();
        MoveToScreenCentre moveToScreenCentre = new MoveToScreenCentre();
        this.moveToScreenCentreRunnable = moveToScreenCentre;
        this.handler.post(moveToScreenCentre);
        this.handler.postDelayed(this.longPressRunnable, 100L);
        return this.view;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment, android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initMap();
    }

    protected void initView() {
        this.vsGoogleMap = (ViewStub) this.view.findViewById(R.id.vs_google_map);
        this.vsGaoDeMap = (ViewStub) this.view.findViewById(R.id.vs_gaode_map);
        this.mIvTransferStation = (ImageView) this.view.findViewById(R.id.ib_main_transfer_station);
        this.mImageViewMapModeSatellite = (ImageView) this.view.findViewById(R.id.iv_main_map_mode_satellite);
        this.mTvSpeed = (TextView) this.view.findViewById(R.id.tv_main_speedmeter);
        this.mIvGpsSignal = (ImageView) this.view.findViewById(R.id.iv_main_gpsSignal);
        this.mCBBatteryState = (CtrlButton) this.view.findViewById(R.id.ctrlButton_main_battery);
        this.mCBModeState = (CtrlButton) this.view.findViewById(R.id.ctrlButton_main_mode);
        this.mCBWifiState = (CtrlButton) this.view.findViewById(R.id.ctrlButton_main_wifi);
        this.mIvMotorSpeed = (ImageView) this.view.findViewById(R.id.image_main_motorspeed);
        this.mBtnMotor = (ImageView) this.view.findViewById(R.id.btn_main_motor);
        this.mRudder = (Rudder) this.view.findViewById(R.id.ctr_main_boatctr);
        this.mBtnEye = (ImageButton) this.view.findViewById(R.id.ib_main_eye);
        this.mLinearLayoutEye = (LinearLayout) this.view.findViewById(R.id.lin_main_eye);
        this.mTvSpeed.setText(this.activity.intToStringSpeed(0.0f));
        this.mScrubBacground = (ImageView) this.view.findViewById(R.id.iv_main_scrub_bac);
        this.mImageSetting = (ImageView) this.view.findViewById(R.id.ib_main_setting);
        this.mImageRoute = (ImageView) this.view.findViewById(R.id.iv_main_route);
        this.mTvTitle = (TextView) this.view.findViewById(R.id.tv_main_title);
        this.mIvRedHeart = (ImageView) this.view.findViewById(R.id.iv_main_redheart);
        this.mRelTitleBar = (RelativeLayout) this.view.findViewById(R.id.rel_main_nav);
        this.mImageDelete = (ImageView) this.view.findViewById(R.id.iv_main_delete);
        this.mIvRecord = (LightImage) this.view.findViewById(R.id.iv_main_record);
        this.mIvTrack = (ImageView) this.view.findViewById(R.id.iv_main_icon_no_track_boat);
        this.mGpsCircular = (CircularProgressBar) this.view.findViewById(R.id.pb_main_gps_circular);
        this.mIvExitFunction = (ImageView) this.view.findViewById(R.id.iv_main_exit_function);
        this.mIvLocateBoat = (ImageView) this.view.findViewById(R.id.iv_mian_locate_boat);
        this.checkGpsAndWifiPopuWindow = new CheckGpsAndWifiPopuWindow(getActivity());
        DrawerLayout drawerLayout = (DrawerLayout) this.view.findViewById(R.id.drawer_main_menu);
        this.mDrawerLayout = drawerLayout;
        drawerLayout.setDrawerLockMode(1);
        initArcMenu();
    }

    private void initMap() {
        int i = Config.MAP_TYPE;
        if (i == 1) {
            this.vsGoogleMap.inflate();
            MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
                return;
            }
            return;
        }
        if (i != 2) {
            return;
        }
        this.vsGaoDeMap.inflate();
        com.amap.api.maps.MapFragment mapFragment2 = (com.amap.api.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.gaode_map);
        mapFragment2.onCreate(null);
        AMap map = mapFragment2.getMap();
        this.aMap = map;
        map.setOnCameraChangeListener(new AMap.OnCameraChangeListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.1
            @Override // com.amap.api.maps.AMap.OnCameraChangeListener
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
            }

            @Override // com.amap.api.maps.AMap.OnCameraChangeListener
            public void onCameraChange(CameraPosition cameraPosition) {
                if (PMainFragment.this.aBoatMarker != null) {
                    PMainFragment.this.zoom = cameraPosition.zoom;
                }
                if (PMainFragment.this.moveToScreenCentreRunnable == null) {
                    return;
                }
                if (PMainFragment.this.moveTimeOut != null) {
                    PMainFragment.this.handler.removeCallbacks(PMainFragment.this.moveTimeOut);
                }
                PMainFragment.this.moveTimeOut = new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PMainFragment.this.isMove = false;
                    }
                };
                PMainFragment.this.isMove = true;
                PMainFragment.this.handler.postDelayed(PMainFragment.this.moveTimeOut, 3000L);
            }
        });
        this.aMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    protected void initEvent() {
        this.mIvRecord.setOnClickListener(this);
        this.mIvTransferStation.setOnClickListener(this);
        this.mBtnMotor.setOnClickListener(this);
        this.mBtnEye.setOnClickListener(this);
        this.mImageViewMapModeSatellite.setOnClickListener(this);
        this.mCBModeState.setOnClickListener(new DoubleClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.2
            @Override // im.helmsman.helmsmanandroid.CustomListener.DoubleClickListener
            public void onDoubleClickListener() {
                ((MainPresenter) PMainFragment.this.presenter).sendSwitchModeCommand(13);
            }
        });
        this.mImageSetting.setOnClickListener(this);
        this.mImageRoute.setOnClickListener(this);
        this.mIvTrack.setOnClickListener(this);
        this.mLinearLayoutEye.setOnClickListener(null);
        this.mIvExitFunction.setOnClickListener(this);
        this.mImageDelete.setOnClickListener(this);
        this.mIvLocateBoat.setOnClickListener(this);
        this.mDrawerLayout.addDrawerListener(this);
        this.mScrubBacground.setOnClickListener(this);
        this.mRudder.setRudderListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void recoveryViewState() {
        ((MainPresenter) this.presenter).stopMotorAnimation();
        com.google.android.gms.maps.model.Marker marker = this.gBoatMarker;
        if (marker != null) {
            marker.remove();
            this.gBoatMarker = null;
        }
        Marker marker2 = this.aBoatMarker;
        if (marker2 != null) {
            marker2.remove();
            this.aBoatMarker = null;
        }
        this.mCBBatteryState.setViewDrawable(R.drawable.icon_batterylow);
        this.mCBModeState.setViewDrawable(R.drawable.icon_dashboard_offline);
        this.mIvGpsSignal.setImageResource(R.drawable.icon_gpssignal_red);
        this.mGpsCircular.setProgress(0.0f);
        this.mTvSpeed.setText(this.activity.intToStringSpeed(0.0f));
        this.mIvRedHeart.setVisibility(8);
        this.mTvTitle.setVisibility(0);
        this.mArcMenu.setVisibility(0);
        this.mIvRecord.setVisibility(8);
        this.mIvExitFunction.setVisibility(8);
        this.mArcMenu.getHintView().setImageResource(2131165774);
        setMotorSpeedImageView(R.drawable.icon_motorspeed0);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void wifiState(boolean z) {
        if (z) {
            this.mCBWifiState.setViewDrawable(R.drawable.icon_greedwifi);
        } else {
            this.mCBWifiState.setViewDrawable(R.drawable.icon_nowifi);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void gpsState(int i, int i2) {
        this.mGpsCircular.setProgress(i2);
        if (i == 0 || i == 3 || i == 4) {
            this.mIvGpsSignal.setVisibility(0);
            this.mIvGpsSignal.setImageResource(R.drawable.icon_gpssignal_red);
            MyApplication.getInstance().gpsState = 0;
        } else {
            this.mIvGpsSignal.setVisibility(0);
            this.mIvGpsSignal.setImageResource(R.drawable.icon_gpssignal_green);
            MyApplication.getInstance().gpsState = 2;
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void speedState(float f) {
        this.mTvSpeed.setText(this.activity.intToStringSpeed(f));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void batteryState(int i) {
        int i2 = R.drawable.icon_batterylow;
        boolean z = true;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    i2 = R.drawable.icon_batterymid;
                } else if (i != 4) {
                    return;
                } else {
                    i2 = R.drawable.icon_batteryfull;
                }
            }
            z = false;
        }
        this.mCBBatteryState.setViewDrawable(i2);
        if (z) {
            this.mCBBatteryState.flicker();
        } else {
            this.mCBBatteryState.cancelFlicker();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void boatDirectionState(float f) {
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
            if (i == 1) {
                com.google.android.gms.maps.model.Marker marker = this.gBoatMarker;
                if (marker != null) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.copyBitmap));
                }
            } else if (i == 2) {
                this.aBoatMarker.setRotateAngle(-f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Fragment
    public void onStart() {
        MainActivity mainActivity;
        super.onStart();
        TextView textView = this.mTvSpeed;
        if (textView == null || (mainActivity = this.activity) == null) {
            return;
        }
        textView.setText(mainActivity.intToStringSpeed(0.0f));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void boatModelState(int i) {
        this.modelCode = i;
        this.mIvRecord.setVisibility(8);
        if (i == 0) {
            this.mArcMenu.getHintView().setImageResource(2131165774);
            this.mCBModeState.setViewDrawable(R.drawable.icon_dashboard_manual);
            this.mArcMenu.setVisibility(0);
            this.mIvExitFunction.setVisibility(8);
            return;
        }
        if (i == 1) {
            this.mCBModeState.setViewDrawable(R.drawable.icon_main_anchor2);
            this.mArcMenu.setVisibility(8);
            this.mIvExitFunction.setVisibility(0);
            this.mArcMenu.getHintView().setImageResource(ITEM_DRAWABLES[3]);
            return;
        }
        if (i == 2) {
            this.mCBModeState.setViewDrawable(R.drawable.icon_main_navi2);
            this.mArcMenu.setVisibility(8);
            this.mIvExitFunction.setVisibility(0);
            this.mIvExitFunction.invalidate();
            this.mArcMenu.getHintView().setImageResource(ITEM_DRAWABLES[2]);
            return;
        }
        if (i == 3) {
            this.mCBModeState.setViewDrawable(R.drawable.icon_main_cruise2);
            this.mArcMenu.setVisibility(8);
            this.mIvExitFunction.setVisibility(0);
            this.mArcMenu.getHintView().setImageResource(ITEM_DRAWABLES[1]);
            return;
        }
        if (i == 4) {
            this.mCBModeState.setViewDrawable(R.drawable.icon_main_record2);
            this.mArcMenu.setVisibility(8);
            this.mIvRecord.setVisibility(0);
            this.mArcMenu.getHintView().setImageResource(ITEM_DRAWABLES[0]);
            return;
        }
        if (i == 7) {
            this.mCBModeState.setViewDrawable(R.drawable.icon_pedal);
            this.mArcMenu.setVisibility(8);
            this.mIvExitFunction.setVisibility(8);
            this.mArcMenu.getHintView().setImageResource(ITEM_DRAWABLES[2]);
            return;
        }
        if (i != 32) {
            return;
        }
        this.mCBModeState.setViewDrawable(R.drawable.icon_remote_control);
        this.mArcMenu.setVisibility(8);
        this.mIvExitFunction.setVisibility(8);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void positionState(MLatLng mLatLng) {
        MLatLng mLatLngWgs84ToGcj02 = CoordinateTransform.Wgs84ToGcj02(mLatLng);
        Bitmap bitmap = this.copyBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        int i = Config.MAP_TYPE;
        if (i != 1) {
            if (i == 2 && this.aMap != null) {
                if (this.aBoatMarker == null) {
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.4f, 0.4f);
                    Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.icon_boat);
                    Marker markerAddMarker = this.aMap.addMarker(new MarkerOptions().position(mLatLngWgs84ToGcj02.toAmapLatlng()).icon(com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(bitmapDecodeResource, 0, 0, bitmapDecodeResource.getWidth(), bitmapDecodeResource.getHeight(), matrix, true))));
                    this.aBoatMarker = markerAddMarker;
                    markerAddMarker.setAnchor(0.5f, 0.5f);
                }
                this.aBoatMarker.setPosition(mLatLngWgs84ToGcj02.toAmapLatlng());
                return;
            }
            return;
        }
        GoogleMap googleMap = this.mGoogleMap;
        if (googleMap != null) {
            if (this.gBoatMarker == null) {
                com.google.android.gms.maps.model.Marker markerAddMarker2 = googleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(mLatLngWgs84ToGcj02.toGoogleLatlng()).icon(BitmapDescriptorFactory.fromBitmap(this.copyBitmap)));
                this.gBoatMarker = markerAddMarker2;
                markerAddMarker2.setAnchor(0.5f, 0.5f);
            }
            this.gBoatMarker.setPosition(mLatLngWgs84ToGcj02.toGoogleLatlng());
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void boatTarget(int i) {
        if ((i < this.gMarkers.size() || i < this.aMarker.size()) && this.modelCode == 2) {
            View viewInflate = View.inflate(this.activity, R.layout.item_add_route_under_marker, null);
            ((TextView) viewInflate.findViewById(R.id.tv_add_route_marker)).setText(String.valueOf(i));
            Bitmap viewBitmap = ViewUtils.getViewBitmap(viewInflate);
            View viewInflate2 = View.inflate(this.activity, R.layout.item_add_route_target_marker, null);
            ((TextView) viewInflate2.findViewById(R.id.tv_add_route_marker)).setText(String.valueOf(i + 1));
            Bitmap viewBitmap2 = ViewUtils.getViewBitmap(viewInflate2);
            try {
                int i2 = Config.MAP_TYPE;
                if (i2 == 1) {
                    if (i > 0) {
                        this.gMarkers.get(i - 1).setIcon(BitmapDescriptorFactory.fromBitmap(viewBitmap));
                    }
                    this.gMarkers.get(i).setIcon(BitmapDescriptorFactory.fromBitmap(viewBitmap2));
                } else if (i2 == 2) {
                    if (i > 0) {
                        this.aMarker.get(i - 1).setIcon(com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap(viewBitmap));
                    }
                    this.aMarker.get(i).setIcon(com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap(viewBitmap2));
                }
            } catch (Exception unused) {
            } finally {
                if (viewBitmap != null) {
                    viewBitmap.recycle();
                }
            }
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void flickHeartBeat() {
        this.isFlickHeart = true;
        flickHeart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void flickHeart() {
        if (this.isFlickHeart) {
            this.mTvTitle.setVisibility(8);
            this.mIvRedHeart.setVisibility(0);
            if (this.oa == null) {
                ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mIvRedHeart, "alpha", 1.0f, 0.0f, 1.0f);
                this.oa = objectAnimatorOfFloat;
                objectAnimatorOfFloat.setDuration(1500L);
                this.oa.setRepeatCount(0);
            }
            if (!this.oa.isStarted()) {
                this.oa.start();
            }
            this.handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    PMainFragment.this.flickHeart();
                }
            }, 1200L);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void stopFlickHeartBeat() {
        this.isFlickHeart = false;
        this.mTvTitle.setVisibility(0);
        this.mIvRedHeart.setVisibility(8);
        this.mIvRedHeart.clearAnimation();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void onRemoteControlBatteryLow() {
        new CheckGpsAndWifiPopuWindow(getActivity()).showPopupWindow(this.mRelTitleBar, R.string.remote_control_battery_low_title, R.string.remote_control_battery_low_content);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void clearMotorBtnAnimation() {
        this.mBtnMotor.clearAnimation();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void startmotorBtnAnimation(Animation animation) {
        this.mBtnMotor.startAnimation(animation);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void setMotorSpeedImageView(int i) {
        this.mIvMotorSpeed.setImageResource(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearMap() {
        if (this.mGoogleMap != null) {
            Iterator<com.google.android.gms.maps.model.Marker> it = this.gMarkers.iterator();
            while (it.hasNext()) {
                it.next().remove();
            }
            Iterator<Polyline> it2 = this.gPolylines.iterator();
            while (it2.hasNext()) {
                it2.next().remove();
            }
        }
        if (this.aMap != null) {
            Iterator<Marker> it3 = this.aMarker.iterator();
            while (it3.hasNext()) {
                it3.next().remove();
            }
            Iterator<com.amap.api.maps.model.Polyline> it4 = this.aPolylines.iterator();
            while (it4.hasNext()) {
                it4.next().remove();
            }
        }
        this.aPolylines.clear();
        this.gPolylines.clear();
        this.gMarkers.clear();
        this.aMarker.clear();
    }

    private void initArcMenu() {
        AutoArcMenu autoArcMenu = (AutoArcMenu) this.view.findViewById(R.id.main_arc_menu);
        this.mArcMenu = autoArcMenu;
        autoArcMenu.getHintView().setOnTouchListener(this);
        int length = ITEM_DRAWABLES.length;
        final ImageView[] imageViewArr = new ImageView[4];
        for (int i = 0; i < length; i++) {
            ImageView imageView = new ImageView(this.activity);
            imageView.setPadding(10, 10, 10, 10);
            imageViewArr[i] = imageView;
            imageView.setImageResource(ITEM_DRAWABLES[i]);
            imageView.setTag(Integer.valueOf(i));
            this.mArcMenu.addItem(imageView, new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.4
                @Override // android.view.View.OnClickListener
                public void onClick(final View view) {
                    PMainFragment.this.mScrubBacground.setVisibility(8);
                    view.setClickable(false);
                    view.setEnabled(false);
                    view.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            view.setClickable(true);
                            view.setEnabled(true);
                        }
                    }, 300L);
                    for (final int i2 = 0; i2 < 4; i2++) {
                        imageViewArr[i2].setClickable(false);
                        imageViewArr[i2].setEnabled(false);
                        imageViewArr[i2].postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.4.2
                            @Override // java.lang.Runnable
                            public void run() {
                                imageViewArr[i2].setClickable(true);
                                imageViewArr[i2].setEnabled(true);
                            }
                        }, 300L);
                    }
                    PMainFragment.this.mArcMenu.changeVisbility();
                    PMainFragment.this.checkGpsAndWifiPopuWindow.setOnCheckGpsAndWifi(new CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.4.3
                        @Override // im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener
                        public void GpsAndWifiSuccess() {
                            HMMission hMRecordMission;
                            HMMissionManager hMMissionManager = HMMissionManager.getInstance();
                            int iIntValue = ((Integer) view.getTag()).intValue();
                            if (iIntValue == 0) {
                                hMRecordMission = new HMRecordMission();
                            } else if (iIntValue == 1) {
                                hMRecordMission = new HMCruiseMission();
                            } else if (iIntValue == 2) {
                                if (PMainFragment.this.aMarker.size() <= 0 && PMainFragment.this.gMarkers.size() <= 0) {
                                    ViewUtils.ShowToast(R.string.no_route_subtitle);
                                    return;
                                } else {
                                    if (!MyApplication.getInstance().wifiState) {
                                        ViewUtils.showAlertDialog(R.string.main_no_boat_connected, R.string.main_no_boat_connected_message, R.string.app_ok, 0, (DialogInterface.OnClickListener) null);
                                    }
                                    hMRecordMission = new HMNavigationMission();
                                }
                            } else {
                                hMRecordMission = iIntValue != 3 ? null : new HMAnchorMission();
                            }
                            hMMissionManager.prepareMission(hMRecordMission, null, null);
                            hMMissionManager.releaseMissionManager();
                        }
                    });
                    PMainFragment.this.checkGpsAndWifiPopuWindow.checkGpsAndWifi(PMainFragment.this.mRelTitleBar);
                }
            });
        }
    }

    @Override // com.google.android.gms.maps.OnMapReadyCallback
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.5
            @Override // com.google.android.gms.maps.GoogleMap.OnCameraChangeListener
            public void onCameraChange(com.google.android.gms.maps.model.CameraPosition cameraPosition) {
                if (PMainFragment.this.gBoatMarker != null) {
                    PMainFragment.this.zoom = cameraPosition.zoom;
                }
                if (PMainFragment.this.moveToScreenCentreRunnable == null) {
                    return;
                }
                if (PMainFragment.this.moveTimeOut != null) {
                    PMainFragment.this.handler.removeCallbacks(PMainFragment.this.moveTimeOut);
                }
                PMainFragment.this.moveTimeOut = new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PMainFragment.this.isMove = false;
                    }
                };
                PMainFragment.this.isMove = true;
                PMainFragment.this.handler.postDelayed(PMainFragment.this.moveTimeOut, 3000L);
            }
        });
    }

    private void drawWayPoint() {
        if (this.mLatlngs != null) {
            this.mImageDelete.setVisibility(0);
            paintMarkerAndLine(this.mLatlngs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawWayPoint(int i) {
        if (i != -1) {
            List<MLatLng> list = this.mLatlngs;
            if (list != null) {
                list.clear();
            }
            RouteModel wayPointListById = DaoUtil.getInstance().getWayPointListById(i);
            if (wayPointListById != null) {
                List<WayPoint> waypoint = wayPointListById.getWaypoint();
                this.mLatlngs = new ArrayList();
                for (int i2 = 0; i2 < waypoint.size(); i2++) {
                    this.mLatlngs.add(new MLatLng((float) waypoint.get(i2).getLatitude(), (float) waypoint.get(i2).getLongitude()));
                }
            }
            this.mLatlngs = CoordinateTransform.latLngWGS84ToGCJ02(this.mLatlngs);
            drawWayPoint();
        }
    }

    @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        synchronized (this) {
            if (this.isConnect) {
                return;
            }
            List listFind = DataSupport.where("devices = ?", new Gson().toJson(bluetoothDevice)).find(BleBlueToothConnected.class);
            this.isConnect = true;
            if (listFind.size() > 0) {
                getBlueToothAdapter().stopLeScan(this);
                BleBlueBoatDataIOController.getInstance().connectBlueTooth(this.activity, bluetoothDevice.getAddress(), bluetoothDevice, new BluetoothGattCallback() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.6
                    @Override // android.bluetooth.BluetoothGattCallback
                    public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                        super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
                    }
                });
            }
        }
    }

    private BluetoothAdapter getBlueToothAdapter() {
        return ((BluetoothManager) this.activity.getSystemService("bluetooth")).getAdapter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void flickerRunnable(boolean z) {
        if (z) {
            this.mIvMotorSpeed.setVisibility(0);
        } else if (this.mIvMotorSpeed.getVisibility() == 8) {
            this.mIvMotorSpeed.setVisibility(0);
        } else {
            this.mIvMotorSpeed.setVisibility(8);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void offline() {
        this.mIvRedHeart.setVisibility(8);
        this.mTvTitle.setVisibility(0);
        this.mCBModeState.setViewDrawable(R.drawable.icon_dashboard_offline);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void online() {
        this.mIvRedHeart.setVisibility(0);
        this.mTvTitle.setVisibility(8);
        if (this.mIvRecord.getVisibility() == 8 && this.mIvExitFunction.getVisibility() == 8) {
            this.mArcMenu.setVisibility(0);
        }
    }

    public void closeDrawerlayout() {
        this.mDrawerLayout.closeDrawer(3);
    }

    private void paintMarkerAndLine(List<MLatLng> list) {
        if (this.mGoogleMap == null && this.aMap == null) {
            return;
        }
        View viewInflate = View.inflate(this.activity, R.layout.item_add_route_under_marker, null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_add_route_marker);
        int i = Config.MAP_TYPE;
        int i2 = 0;
        if (i == 1) {
            for (int i3 = 1; i3 < list.size(); i3++) {
                this.gPolylines.add(this.mGoogleMap.addPolyline(new PolylineOptions().add(list.get(i3 - 1).toGoogleLatlng(), list.get(i3).toGoogleLatlng()).width(8.0f).color(-16776961)));
            }
            while (i2 < list.size()) {
                int i4 = i2 + 1;
                textView.setText(String.valueOf(i4));
                Bitmap viewBitmap = ViewUtils.getViewBitmap(viewInflate);
                this.gMarkers.add(this.mGoogleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(list.get(i2).toGoogleLatlng()).icon(BitmapDescriptorFactory.fromBitmap(viewBitmap))));
                if (viewBitmap != null && !viewBitmap.isRecycled()) {
                    viewBitmap.recycle();
                }
                i2 = i4;
            }
            return;
        }
        if (i != 2) {
            return;
        }
        for (int i5 = 1; i5 < list.size(); i5++) {
            this.aPolylines.add(this.aMap.addPolyline(new com.amap.api.maps.model.PolylineOptions().add(list.get(i5 - 1).toAmapLatlng(), list.get(i5).toAmapLatlng()).width(8.0f).color(-16776961)));
        }
        while (i2 < list.size()) {
            int i6 = i2 + 1;
            textView.setText(String.valueOf(i6));
            Bitmap viewBitmap2 = ViewUtils.getViewBitmap(viewInflate);
            this.aMarker.add(this.aMap.addMarker(new MarkerOptions().position(list.get(i2).toAmapLatlng()).icon(com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap(viewBitmap2))));
            if (viewBitmap2 != null && !viewBitmap2.isRecycled()) {
                viewBitmap2.recycle();
            }
            i2 = i6;
        }
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelUp() {
        if (this.isShow || HelmsmanSDK.productIsNull()) {
            return;
        }
        HelmsmanSDK.getProduct().motorComponent.speedUp();
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelBottom() {
        if (this.isShow || HelmsmanSDK.productIsNull()) {
            return;
        }
        HelmsmanSDK.getProduct().motorComponent.speedDown();
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelLeft() {
        ((MainPresenter) this.presenter).turn(0);
        this.isInitPress = true;
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
            this.popupWindow = null;
        }
        this.flag = 0;
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelRight() {
        ((MainPresenter) this.presenter).turn(1);
        this.isInitPress = true;
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
            this.popupWindow = null;
        }
        this.flag = 0;
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onLongPressUp() {
        Log.e("ADDSPEED", "ADDSPEED");
        if (this.longPressTimer == 0) {
            this.longPressTimer = System.currentTimeMillis();
        }
        if (this.flag != 1) {
            PopupWindow popupWindow = this.popupWindow;
            if (popupWindow != null) {
                popupWindow.dismiss();
                this.popupWindow = null;
            }
            this.isShow = false;
            this.flag = 1;
            this.longPressTimer = System.currentTimeMillis();
        }
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onLongPressDown() {
        if (this.longPressTimer == 0) {
            this.longPressTimer = System.currentTimeMillis();
        }
        if (this.flag != 2) {
            PopupWindow popupWindow = this.popupWindow;
            if (popupWindow != null) {
                popupWindow.dismiss();
                this.popupWindow = null;
            }
            this.isShow = false;
            this.flag = 2;
            this.longPressTimer = System.currentTimeMillis();
        }
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onClickStop() {
        ((MainPresenter) this.presenter).turnEnd();
        this.isInitPress = true;
        this.isShow = false;
        this.longPressTimer = 0L;
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
            this.popupWindow = null;
        }
    }

    class MoveToScreenCentre implements Runnable {
        MoveToScreenCentre() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int i = Config.MAP_TYPE;
            if (i != 1) {
                if (i == 2 && PMainFragment.this.aBoatMarker != null && PMainFragment.this.aMap != null && !PMainFragment.this.isMove) {
                    PMainFragment.this.aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PMainFragment.this.aBoatMarker.getPosition(), PMainFragment.this.zoom));
                }
            } else if (PMainFragment.this.gBoatMarker != null && PMainFragment.this.mGoogleMap != null && !PMainFragment.this.isMove) {
                PMainFragment.this.mGoogleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(PMainFragment.this.gBoatMarker.getPosition(), PMainFragment.this.zoom));
            }
            PMainFragment.this.handler.postDelayed(PMainFragment.this.moveToScreenCentreRunnable, 4000L);
        }
    }

    class CheckLongPressRunnable implements Runnable {
        CheckLongPressRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            long jCurrentTimeMillis = System.currentTimeMillis() - PMainFragment.this.longPressTimer;
            if (!PMainFragment.this.isShow && PMainFragment.this.isInitPress && PMainFragment.this.longPressTimer > 0 && jCurrentTimeMillis >= 500 && jCurrentTimeMillis < 2000 && PMainFragment.this.flag != 0) {
                PMainFragment.this.isShow = true;
                if (PMainFragment.this.flag == 1) {
                    if (MyApplication.getInstance().wifiState) {
                        PMainFragment.this.showrockPoPupWindows();
                    }
                } else {
                    new CheckGpsAndWifiPopuWindow(PMainFragment.this.getActivity()).setOnCheckGpsAndWifi(new CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.CheckLongPressRunnable.1
                        @Override // im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener
                        public void GpsAndWifiSuccess() {
                            PMainFragment.this.showAnchorPopupWindows();
                        }
                    }).checkGpsAndWifi(PMainFragment.this.mRelTitleBar);
                }
            }
            if (PMainFragment.this.isShow) {
                if (PMainFragment.this.flag == 1) {
                    if (PMainFragment.this.progressBar != null && PMainFragment.this.progressBar.getSecondaryProgress() < 100.0f) {
                        PMainFragment.this.progressBar.setSecondaryProgress(PMainFragment.this.progressBar.getSecondaryProgress() + 13.33f);
                    } else if (PMainFragment.this.isInitPress && PMainFragment.this.progressBar != null) {
                        PMainFragment.this.isInitPress = false;
                        if (!HelmsmanSDK.productIsNull()) {
                            HelmsmanSDK.getProduct().motorComponent.maxSpeed();
                        }
                        if (PMainFragment.this.popupWindow != null) {
                            PMainFragment.this.popupWindow.dismiss();
                        }
                    }
                } else if (PMainFragment.this.flag == 2 && ((PMainFragment.this.progressImageView == null || PMainFragment.this.progressImageView.getProgress() >= 97) && PMainFragment.this.isInitPress)) {
                    if (PMainFragment.this.popupWindow != null) {
                        PMainFragment.this.popupWindow.dismiss();
                    }
                    if (!HelmsmanSDK.productIsNull()) {
                        HelmsmanSDK.getProduct().boatControlComponent.changeModel(12);
                    }
                    PMainFragment.this.isInitPress = false;
                }
            }
            PMainFragment.this.handler.postDelayed(this, 200L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showrockPoPupWindows() {
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindows_maxspeed_layout, (ViewGroup) null);
        this.progressBar = (RoundCornerProgressBar) viewInflate.findViewById(R.id.rcpb_progress);
        PopupWindow popupWindow = new PopupWindow(viewInflate, ViewUtils.dpToPx(msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST), ViewUtils.dpToPx(msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST));
        this.popupWindow = popupWindow;
        popupWindow.setTouchable(true);
        this.popupWindow.showAtLocation(this.mRudder, 17, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAnchorPopupWindows() {
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindows_anchor_layout, (ViewGroup) null);
        this.progressImageView = (ProgressImageView) viewInflate.findViewById(R.id.piv_progress);
        PopupWindow popupWindow = new PopupWindow(viewInflate, ViewUtils.dpToPx(msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST), ViewUtils.dpToPx(msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST));
        this.popupWindow = popupWindow;
        popupWindow.setTouchable(true);
        this.popupWindow.showAtLocation(this.mRudder, 17, 0, 0);
        this.progressImageView.startProgressAnim(0.0f, 100.0f);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!this.mArcMenu.isExpanded()) {
            GoogleMap googleMap = this.mGoogleMap;
            if (googleMap != null) {
                googleMap.snapshot(this);
            }
            AMap aMap = this.aMap;
            if (aMap != null) {
                aMap.getMapScreenShot(this);
            }
        }
        if (!this.mArcMenu.isExpanded() && !this.mArcMenu.isAllAnimaFinshed()) {
            this.mScrubBacground.setVisibility(0);
        } else if (this.mArcMenu.isExpanded() && this.mArcMenu.isAllAnimaFinshed()) {
            this.mScrubBacground.setVisibility(8);
        }
        return false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        AMap aMap;
        GoogleMap googleMap;
        if (view.equals(this.mIvLocateBoat)) {
            com.google.android.gms.maps.model.Marker marker = this.gBoatMarker;
            if (marker != null && (googleMap = this.mGoogleMap) != null) {
                MapUtil.UpdateMapCenter(googleMap, marker.getPosition());
            } else {
                Marker marker2 = this.aBoatMarker;
                if (marker2 != null && (aMap = this.aMap) != null) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker2.getPosition(), 14.0f));
                } else {
                    ViewUtils.ShowToast(R.string.main_connection_established_map_not_ready);
                }
            }
            this.mLinearLayoutEye.setVisibility(8);
            this.mScrubBacground.setVisibility(8);
            return;
        }
        if (view.equals(this.mIvExitFunction)) {
            ((MainPresenter) this.presenter).sendSwitchModeCommand(13);
            return;
        }
        if (view.equals(this.mIvTrack)) {
            if (this.isTrack) {
                this.mIvTrack.setImageResource(R.drawable.icon_notrack_boat);
                this.isTrack = false;
                this.handler.removeCallbacks(this.moveToScreenCentreRunnable);
                this.moveToScreenCentreRunnable = null;
            } else {
                this.mIvTrack.setImageResource(R.drawable.icon_track_boat);
                this.isTrack = true;
                MoveToScreenCentre moveToScreenCentre = new MoveToScreenCentre();
                this.moveToScreenCentreRunnable = moveToScreenCentre;
                this.handler.post(moveToScreenCentre);
            }
            this.mLinearLayoutEye.setVisibility(8);
            this.mScrubBacground.setVisibility(8);
            return;
        }
        if (view.equals(this.mBtnMotor)) {
            if (System.currentTimeMillis() - this.mMotorFirstClickTime <= 1000) {
                if (MyApplication.getInstance().motorIsRun) {
                    ((MainPresenter) this.presenter).closeMotor();
                } else {
                    ((MainPresenter) this.presenter).startMotor();
                }
                this.mMotorFirstClickTime = 0L;
                return;
            }
            this.mMotorFirstClickTime = System.currentTimeMillis();
            return;
        }
        if (view.equals(this.mBtnEye)) {
            if (this.mLinearLayoutEye.getVisibility() == 0) {
                this.mLinearLayoutEye.setVisibility(8);
                this.mScrubBacground.setVisibility(8);
                return;
            }
            this.mLinearLayoutEye.setVisibility(0);
            this.mScrubBacground.setVisibility(0);
            GoogleMap googleMap2 = this.mGoogleMap;
            if (googleMap2 != null) {
                googleMap2.snapshot(this);
                return;
            }
            AMap aMap2 = this.aMap;
            if (aMap2 != null) {
                aMap2.getMapScreenShot(this);
                return;
            }
            return;
        }
        if (view.equals(this.mImageViewMapModeSatellite)) {
            GoogleMap googleMap3 = this.mGoogleMap;
            if (googleMap3 != null) {
                if (googleMap3.getMapType() == 1) {
                    this.mGoogleMap.setMapType(2);
                    this.mImageViewMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_stander);
                } else {
                    this.mGoogleMap.setMapType(1);
                    this.mImageViewMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_satellite);
                }
            } else {
                AMap aMap3 = this.aMap;
                if (aMap3 != null) {
                    if (aMap3.getMapType() == 1) {
                        this.aMap.setMapType(2);
                        this.mImageViewMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_stander);
                    } else {
                        this.aMap.setMapType(1);
                        this.mImageViewMapModeSatellite.setImageResource(R.drawable.icon_main_map_mode_satellite);
                    }
                }
            }
            this.mLinearLayoutEye.setVisibility(8);
            this.mScrubBacground.setVisibility(8);
            return;
        }
        if (view.equals(this.mScrubBacground)) {
            this.mLinearLayoutEye.setVisibility(8);
            this.mScrubBacground.setVisibility(8);
            if (this.mArcMenu.isExpanded()) {
                this.mArcMenu.clickStartAnimation();
                return;
            }
            return;
        }
        if (view.equals(this.mImageSetting)) {
            this.mDrawerLayout.openDrawer(3);
            return;
        }
        if (view.equals(this.mImageRoute)) {
            startActivity(new Intent(this.activity, (Class<?>) MyFollowingRouteActivity.class));
            return;
        }
        if (view.equals(this.mImageDelete)) {
            this.mImageDelete.setVisibility(8);
            this.mLatlngs.clear();
            clearMap();
            this.gMarkers.clear();
            this.aMarker.clear();
            if (this.modelCode == 2) {
                ((MainPresenter) this.presenter).sendSwitchModeCommand(13);
            }
            ((MainPresenter) this.presenter).sendClearCacheCommand();
            return;
        }
        if (view.equals(this.mIvTransferStation)) {
            startActivity(new Intent(this.activity, (Class<?>) BleBlueToothActivity.class));
        } else if (view.equals(this.mIvRecord)) {
            ((MainPresenter) this.presenter).finishRecordWayPoint();
            Intent intent = new Intent(this.activity, (Class<?>) MyFollowingRouteActivity.class);
            intent.putExtra(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, 100);
            startActivity(intent);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Subscribe
    public void onEventMainThread(final UpLoadWayPointSuccess upLoadWayPointSuccess) {
        this.handler.post(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.7
            @Override // java.lang.Runnable
            public void run() {
                if (PMainFragment.this.mGoogleMap != null || PMainFragment.this.aMap != null) {
                    PMainFragment.this.clearMap();
                }
                PMainFragment.this.drawWayPoint(upLoadWayPointSuccess.getWayPointName());
            }
        });
        ((MainPresenter) this.presenter).sendSwitchModeCommand(15);
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
    public void onDrawerOpened(View view) {
        this.mDrawerLayout.setDrawerLockMode(0);
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
    public void onDrawerClosed(View view) {
        this.mDrawerLayout.setDrawerLockMode(1);
        this.mDrawerLayout.setClickable(true);
    }

    @Override // com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
    public void onSnapshotReady(Bitmap bitmap) {
        this.mScrubBacground.setImageBitmap(FastBlurUtil.doBlur(bitmap, 4, 50, true));
    }

    @Override // com.amap.api.maps.AMap.OnMapScreenShotListener
    public void onMapScreenShot(Bitmap bitmap) {
        Flowable.just(bitmap).flatMap(new Function<Bitmap, Publisher<Bitmap>>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.9
            @Override // io.reactivex.functions.Function
            public Publisher<Bitmap> apply(Bitmap bitmap2) throws Exception {
                return Flowable.just(FastBlurUtil.doBlur(bitmap2, 4, 50, true));
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.PMainFragment.8
            @Override // io.reactivex.functions.Consumer
            public void accept(Bitmap bitmap2) throws Exception {
                PMainFragment.this.mScrubBacground.setImageBitmap(bitmap2);
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.longPressRunnable);
    }
}
