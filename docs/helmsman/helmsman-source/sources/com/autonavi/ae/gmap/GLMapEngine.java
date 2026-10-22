package com.autonavi.ae.gmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.SparseArray;
import com.autonavi.ae.gmap.callback.GLMapCoreCallback;
import com.autonavi.ae.gmap.callback.JniIndoorCallback;
import com.autonavi.ae.gmap.callback.JniMapCoreCallback;
import com.autonavi.ae.gmap.callback.JniMapHeatCallback;
import com.autonavi.ae.gmap.callback.JniScenicCallback;
import com.autonavi.ae.gmap.gesture.EAMapPlatformGestureInfo;
import com.autonavi.ae.gmap.glanimation.ADGLMapAnimFling;
import com.autonavi.ae.gmap.glanimation.ADGLMapAnimGroup;
import com.autonavi.ae.gmap.glanimation.ADGLMapAnimationMgr;
import com.autonavi.ae.gmap.glinterface.GLGeoPoint;
import com.autonavi.ae.gmap.gloverlay.BaseMapOverlay;
import com.autonavi.ae.gmap.gloverlay.GLOverlayBundle;
import com.autonavi.ae.gmap.gloverlay.GLTextureProperty;
import com.autonavi.ae.gmap.listener.MapListener;
import com.autonavi.ae.gmap.listener.RouteBoardDataListener;
import com.autonavi.ae.gmap.maploader.BaseMapLoader;
import com.autonavi.ae.gmap.maploader.ConnectionManager;
import com.autonavi.ae.gmap.maploader.GuideMapLoader;
import com.autonavi.ae.gmap.maploader.HeatMapLoader;
import com.autonavi.ae.gmap.maploader.IndoorMapLoader;
import com.autonavi.ae.gmap.maploader.MapSourceGridData;
import com.autonavi.ae.gmap.maploader.MessageDataThread;
import com.autonavi.ae.gmap.maploader.NetworkState;
import com.autonavi.ae.gmap.maploader.NormalMapLoader;
import com.autonavi.ae.gmap.maploader.OpenLayerMapLoader;
import com.autonavi.ae.gmap.maploader.TilesProcessingCtrl;
import com.autonavi.ae.gmap.maploader.TmcMapLoader;
import com.autonavi.ae.gmap.maploader.VMapDataCache;
import com.autonavi.ae.gmap.scenic.Label3rd;
import com.autonavi.ae.gmap.scenic.MapHeatListener;
import com.autonavi.ae.gmap.scenic.ScenicInfor;
import com.autonavi.ae.gmap.scenic.ScenicListener;
import com.autonavi.ae.gmap.scenic.ScenicWidgetItem;
import com.autonavi.ae.gmap.style.MapTilsCacheAndResManager;
import com.autonavi.ae.gmap.style.MapTilsCacheAndResManagerImpl;
import com.autonavi.ae.gmap.style.StyleItem;
import com.autonavi.ae.gmap.style.StylesIconsUpdate;
import com.autonavi.ae.gmap.utils.GLMapServerUtil;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.autonavi.ae.gmap.utils.GLMapUtil;
import com.autonavi.ae.gmap.utils.GLStateInstance;
import com.autonavi.ae.gmap.utils.TextTextureGenerator;
import com.autonavi.amap.mapcore.CameraUpdateMessage;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IAMap;
import com.autonavi.amap.mapcore.interfaces.IAMapListener;
import com.autonavi.amap.mapcore.message.GestureMapMessage;
import com.autonavi.amap.mapcore.message.HoverGestureMapMessage;
import com.autonavi.amap.mapcore.message.MoveGestureMapMessage;
import com.autonavi.amap.mapcore.message.RotateGestureMapMessage;
import com.autonavi.amap.mapcore.message.ScaleGestureMapMessage;
import com.qiniu.android.common.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public class GLMapEngine implements JniIndoorCallback, JniMapCoreCallback, JniMapHeatCallback, JniScenicCallback {
    public static final int ANIM_ACTION_MOVE = 1;
    public static final int ANIM_ACTION_ROTATECAMERA = 8;
    public static final int ANIM_ACTION_ROTATEMAP = 4;
    public static final int ANIM_ACTION_ZOOM = 2;
    public static int BUILDINGHEIGHT = 0;
    public static final int DATA_IN_DISK = 1;
    public static final int DATA_IN_MEMORY = 2;
    public static final int DATA_NOTEXIT = 0;
    private static final int LONG_LONG_TICK_COUNT = 3;
    private static final int LONG_TICK_COUNT = 2;
    private static final int MAX_CUR_SCREEN_GRIDS_COUNT = 200;
    private static final int NORMAL_TICK_COUNT = 1;
    public static final int TAP_LINES = 2;
    public static final int TAP_POINTS = 1;
    public static final int TAP_POLYGON = 4;
    private static final Charset charset = Charset.forName(Constants.UTF_8);
    private Context mContext;
    private IAMap mGlMapView;
    private long mNativeMapengineInstance;
    private TextTextureGenerator mTextTextureGenerator;
    public String mUserAgent;
    private ADGLMapAnimationMgr map_anims_mgr;
    private List<MapSourceGridData> geometryReqMapGrids = new ArrayList();
    private List<MapSourceGridData> roadReqMapGrids = new ArrayList();
    private List<MapSourceGridData> bldReqMapGrids = new ArrayList();
    private List<MapSourceGridData> regionReqMapGrids = new ArrayList();
    private List<MapSourceGridData> poiReqMapGrids = new ArrayList();
    private List<MapSourceGridData> versionMapGrids = new ArrayList();
    private List<MapSourceGridData> indoorMapGrids = new ArrayList();
    private List<MapSourceGridData> vectmcReqMapGirds = new ArrayList();
    private List<MapSourceGridData> stiReqMapGirds = new ArrayList();
    private List<MapSourceGridData> mapReqModels = new ArrayList();
    private List<MapSourceGridData> guideReqMapGirds = new ArrayList();
    private List<MapSourceGridData> mapHeatReqMapGirds = new ArrayList();
    private List<MapSourceGridData> mCurScreenGirds = new ArrayList();
    private List<MapSourceGridData> mEagleEyeScreenGirds = null;
    private int mCurScreenGridsCount = 0;
    private Object mCurGridFillLock = new Object();
    private ConnectionManager mConnectionManager = null;
    public TilesProcessingCtrl mTileProcessCtrl = null;
    private GLMapCoreCallback mapcoreListener = null;
    private RouteBoardDataListener mBoardDataListener = null;
    private ScenicListener mScenicListener = null;
    public MapListener mMapListener = null;
    public IAMapListener mAMapListener = null;
    private MapHeatListener mMapHeatListener = null;
    private List<CameraUpdateMessage> mStateMessageList = new Vector();
    private List<GestureMapMessage> mGestureMessageList = new Vector();
    private List<GestureMapMessage> mGestureEndMessageList = new Vector();
    private boolean canStopRender = false;
    boolean isMoveCameraStep = false;
    boolean isGestureStep = false;
    private int map_gesture_count = 0;
    private Object mut_lock = new Object();
    private int mShowErrowTipCouont = 0;
    private GLMapState copyGLMapState = null;
    MapTilsCacheAndResManager mCacheMgr = null;
    GLMapState s = new GLMapState();
    private SparseArray<GLStateInstance> mStateSparseArray = new SparseArray<>();
    private byte[] tmp_3072bytes_data = ByteBuffer.allocate(3072).array();

    private static native void nativeAddEngine(int i);

    private static native void nativeAddFlingAnimation(int i, long j, int i2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeAddGeoAndScreenCenterGroupAnimation(int i, long j, int i2, int i3, int i4, int i5, int i6, boolean z);

    private static native void nativeAddGroupAnimation(int i, long j, int i2, int i3, float f, int i4, int i5, int i6, int i7, boolean z);

    private static native void nativeAddLabels3rd(int i, long j, int i2, Label3rd[] label3rdArr, boolean z);

    private static native void nativeAddMapGestureMsg(int i, long j, int i2, float f, float f2, float f3);

    private static native void nativeAddMapNaviMsg(int i, long j, long j2, long j3, Point point, Point point2, float f, float f2, float f3, float f4, float f5);

    private static native boolean nativeAddOverlayTexture(int i, long j, int i2, int i3, float f, float f2, Bitmap bitmap, boolean z, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeAddPivotZoomAnimation(int i, long j, int i2, float f, int i3, int i4);

    private static native void nativeAddPoiFilter(int i, long j, int i2, int i3, int i4, float f, float f2, float f3, float f4, String str, int i5);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeAddZoomRotateAnimation(int i, long j, int i2, float f, int i3, int i4, int i5);

    private static native void nativeAppendOpenLayer(int i, long j, byte[] bArr);

    private static native boolean nativeCanStopRenderMap(int i, long j);

    private static native void nativeChangeMapEnv(int i, long j, String str);

    private static native void nativeChangeSurface(int i, long j, int i2, int i3, int i4, int i5, int i6, int i7, GLMapEngine gLMapEngine);

    private static native void nativeClearAllMessage(int i, long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeClearAnimation(int i, long j);

    private static native void nativeClearAnimationByType(int i, long j, int i2);

    private static native void nativeClearLabels3rd(int i, long j, int i2, boolean z);

    private static native void nativeClearOverlayTexture(int i, long j);

    private static native void nativeClearPoiFilter(int i, long j);

    private static native void nativeClearSelectedScenicPois(int i, long j);

    private static native long nativeCreate(String str, String str2, int i);

    private static native int nativeCreateEngineWithFrame(int i, long j, int i2, int i3, int i4, int i5, int i6, int i7, boolean z);

    protected static native long nativeCreateOverlay(int i, long j, int i2);

    private static native void nativeCreateSurface(int i, long j, GLMapEngine gLMapEngine);

    private static native boolean nativeDeleteOpenLayer(int i, long j, int i2);

    protected static native void nativeDestoryOverlay(int i, long j);

    private static native void nativeDestorySurface(long j, GLMapEngine gLMapEngine);

    private static native void nativeDestroy(long j);

    private static native void nativeDrawFrame(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeFinishAnimations(int i, long j);

    private static native int nativeGetAnimationCount(int i, long j);

    private static native int[] nativeGetEngineIDArray(long j);

    private static native int nativeGetEngineIDWithGestureInfo(long j, EAMapPlatformGestureInfo eAMapPlatformGestureInfo);

    private static native int nativeGetGestureMsgCount(int i, long j);

    private static native long nativeGetGlOverlayMgrPtr(int i, long j);

    private static native boolean nativeGetIsProcessBuildingMark(int i, long j);

    private static native String nativeGetMapCachePath(int i, long j);

    public static native String nativeGetMapEngineVersion(int i);

    private static native String nativeGetMapHeatPoiId(int i, long j);

    private static native int[] nativeGetMapModeState(int i, long j, boolean z);

    private static native long nativeGetMapStateInstance(int i, long j);

    private static native String nativeGetRealCityDataVerSion(int i, long j, int i2);

    private static native void nativeGetScreenGrids(int i, long j, byte[] bArr, int i2);

    private static native boolean nativeGetSrvViewStateBoolValue(int i, long j, int i2);

    private static native float nativeGetSrvViewStateFloatValue(int i, long j, int i2);

    private static native int nativeGetSrvViewStateIntValue(int i, long j, int i2);

    private static native int nativeIndoorBuildingHitTest(int i, long j, int i2, int i3, byte[] bArr);

    private static native void nativeInsertEngine(int i, long j);

    private static native void nativeInsertOpenLayer(int i, long j, byte[] bArr, int i2);

    private static native int nativeIsRealCityAnimateFinish(int i, long j);

    private static native boolean nativeIsShowFeatureSpotIcon(int i, long j);

    private static native boolean nativeIsShowLandMarkBuildingPoi(int i, long j);

    private static native boolean nativeIsShowMask(int i, long j);

    private static native boolean nativeIsSkinExist(int i, long j, int[] iArr);

    private static native int nativeIsSupportRealcity(int i, long j, int i2);

    public static native void nativeLoadStartResource(int i, long j, GLMapEngine gLMapEngine, int[] iArr, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeMapDataControl(int i, long j, int i2, int i3, int i4, int i5, byte[] bArr);

    private static native void nativePopRenderState(int i, long j);

    private static native void nativePostDrawFrame(long j);

    private static native void nativePushRendererState(int i, long j);

    private static native int nativePutMapdata(int i, long j, int i2, byte[] bArr, int i3);

    private static native void nativePutResourceData(int i, long j, byte[] bArr);

    private static native void nativeRemoveEngine(int i, long j);

    private static native void nativeRemovePoiFilter(int i, long j, String str);

    private static native int nativeSelectMapPois(int i, long j, int i2, int i3, int i4, byte[] bArr);

    private static native void nativeSetBackGroundColor(int i, long j, float f, float f2, float f3, float f4);

    private static native void nativeSetIndoorBuildingToBeActive(int i, long j, String str, int i2, String str2);

    private static native void nativeSetInternalTexture(int i, long j, byte[] bArr, int i2);

    private static native void nativeSetMapHeatPoiRegion(int i, long j, String str, int[] iArr, int[] iArr2);

    private static native boolean nativeSetMapModeAndStyle(int i, long j, int[] iArr, boolean z, boolean z2, StyleItem[] styleItemArr);

    private static native void nativeSetMaskColor(int i, long j, int i2);

    private static native void nativeSetParmater(int i, long j, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeSetScenicFilter(int i, long j, int i2, int[] iArr);

    private static native void nativeSetSearchedSubwayIds(int i, long j, String[] strArr);

    private static native void nativeSetServiceViewRect(int i, long j, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native void nativeSetServiceViewVisible(int i, boolean z, long j);

    private static native void nativeSetShowFeatureSpotIcon(int i, long j, boolean z);

    private static native void nativeSetShowMask(int i, long j, boolean z);

    private static native void nativeSetSrvViewStateBoolValue(int i, long j, int i2, boolean z);

    private static native void nativeSetSrvViewStateFloatValue(int i, long j, int i2, float f);

    private static native void nativeSetSrvViewStateIntValue(int i, long j, int i2, int i3);

    private static native int nativeTmcCacheCheckValid(int i, long j, String str, boolean z, boolean z2, byte[] bArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeUpdateStyleOrIcons(int i, long j, String str, int i2);

    private static native void nativeUpdateTmcCache(int i, long j, int i2, byte[] bArr, String str, boolean z);

    public int getEngineIDWithType(int i) {
        return (i == 0 || i != 1) ? 1 : 2;
    }

    public String getMapIndoorAddress() {
        return GLMapServerUtil.INDOOR_MAP_URL_SERVER_PUBLIC;
    }

    public String getMapSvrAddress() {
        return GLMapServerUtil.BASE_MAP_URL_SERVER_PUBLIC;
    }

    public boolean isIndoorGridInScreen(int i, String str, short s) {
        return true;
    }

    public boolean isIndoorGridsInScreen(List<MapSourceGridData> list, int i) {
        return true;
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onLoadTextureByName(int i, String str, int i2) {
    }

    public void onLogOfflineDataStatusReport(int i, String str, String str2, String str3) {
    }

    public void onLogReport(int i, int i2, int i3, String str) {
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onMapProcessEvent(int i) {
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onMapSufaceChanged(int i, int i2, int i3) {
    }

    public void setShowFeatureSpotIcon(boolean z) {
    }

    public void pushRendererState() {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativePushRendererState(1, j);
        }
    }

    public void popRendererState() {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativePopRenderState(1, j);
        }
    }

    public int getStateMessageCount() {
        return this.mStateMessageList.size();
    }

    public void addMessage(CameraUpdateMessage cameraUpdateMessage) {
        List<CameraUpdateMessage> list = this.mStateMessageList;
        if (list != null) {
            list.add(cameraUpdateMessage);
        }
    }

    public synchronized CameraUpdateMessage getStateMessage() {
        List<CameraUpdateMessage> list = this.mStateMessageList;
        if (list != null && list.size() == 0) {
            return null;
        }
        CameraUpdateMessage cameraUpdateMessage = this.mStateMessageList.get(0);
        this.mStateMessageList.remove(cameraUpdateMessage);
        return cameraUpdateMessage;
    }

    public GLMapEngine(Context context, IAMap iAMap) {
        this.mContext = null;
        this.mTextTextureGenerator = null;
        this.mNativeMapengineInstance = 0L;
        this.mGlMapView = null;
        this.mUserAgent = "";
        this.map_anims_mgr = null;
        this.mContext = context;
        this.mGlMapView = iAMap;
        this.map_anims_mgr = new ADGLMapAnimationMgr();
        TextTextureGenerator textTextureGenerator = new TextTextureGenerator();
        this.mTextTextureGenerator = textTextureGenerator;
        String fontVersion = textTextureGenerator.getFontVersion();
        File dir = context.getDir("Map_Assert", 0);
        if (!dir.exists()) {
            dir.mkdir();
        }
        this.mNativeMapengineInstance = nativeCreate(dir.getPath() + "/", fontVersion, BUILDINGHEIGHT);
        this.mUserAgent = System.getProperty("http.agent") + " amap/" + GLMapUtil.getAppVersionName(context);
    }

    public MapTilsCacheAndResManager getMapCacheMgr() {
        return this.mCacheMgr;
    }

    public void initConnectionManager() {
        this.mConnectionManager = new ConnectionManager(this);
        this.mTileProcessCtrl = new TilesProcessingCtrl();
        this.mConnectionManager.start();
    }

    public void loadStartResource(int i, int i2, int i3, int i4, boolean z, int i5, int i6) {
        int[] iArr = {i2, i3, i4, 0, 0};
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeLoadStartResource(i, j, this, iArr, i5, i6);
        }
    }

    public void changeSurface(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeChangeSurface(i, j, i2, i3, i4, i5, i6, i7, this);
        }
    }

    public void destorySurface() {
        TilesProcessingCtrl tilesProcessingCtrl = this.mTileProcessCtrl;
        if (tilesProcessingCtrl != null) {
            tilesProcessingCtrl.clearAll();
        }
        ConnectionManager connectionManager = this.mConnectionManager;
        if (connectionManager != null) {
            connectionManager.threadFlag = false;
            if (this.mConnectionManager.isAlive()) {
                try {
                    this.mConnectionManager.interrupt();
                } catch (Exception unused) {
                }
                this.mConnectionManager.shutDown();
                this.mConnectionManager = null;
            }
        }
        SparseArray<GLStateInstance> sparseArray = this.mStateSparseArray;
        if (sparseArray != null) {
            sparseArray.clear();
        }
        MoveGestureMapMessage.destory();
        HoverGestureMapMessage.destory();
        RotateGestureMapMessage.destory();
        ScaleGestureMapMessage.destory();
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeDestorySurface(j, this);
        }
        this.tmp_3072bytes_data = null;
        synchronized (this.mut_lock) {
            long j2 = this.mNativeMapengineInstance;
            if (j2 != 0) {
                nativeDestroy(j2);
                GLMapState gLMapState = this.copyGLMapState;
                if (gLMapState != null) {
                    gLMapState.recycle();
                    this.copyGLMapState = null;
                }
                this.mNativeMapengineInstance = 0L;
            }
        }
    }

    public void drawFrame() {
        if (this.mNativeMapengineInstance != 0) {
            processMessage();
            synchronized (GLMapEngine.class) {
                nativeDrawFrame(this.mNativeMapengineInstance);
                nativePostDrawFrame(this.mNativeMapengineInstance);
            }
        }
    }

    private void processMessage() {
        try {
            GLMapState cloneMapState = getCloneMapState();
            cloneMapState.reset();
            processGestureMessage(cloneMapState);
            if (this.mGestureMessageList.size() <= 0) {
                processStateMapMessage(cloneMapState);
            } else if (this.mStateMessageList.size() > 0) {
                this.mStateMessageList.clear();
            }
            processAnimations(cloneMapState);
            setMapState(1, cloneMapState);
        } catch (Exception unused) {
        }
    }

    private void processAnimations(GLMapState gLMapState) {
        try {
            if (this.map_anims_mgr.getAnimationsCount() > 0) {
                gLMapState.recalculate();
                this.map_anims_mgr.doAnimations(gLMapState);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void processGestureMessage(GLMapState gLMapState) {
        GestureMapMessage gestureMapMessageRemove;
        if (this.mGestureMessageList.size() <= 0) {
            if (this.isGestureStep) {
                this.isGestureStep = false;
                return;
            }
            return;
        }
        this.isGestureStep = true;
        if (gLMapState == null) {
            return;
        }
        gLMapState.recalculate();
        while (this.mGestureMessageList.size() > 0 && (gestureMapMessageRemove = this.mGestureMessageList.remove(0)) != null) {
            if (gestureMapMessageRemove.width == 0) {
                gestureMapMessageRemove.width = this.mGlMapView.getMapWidth();
            }
            if (gestureMapMessageRemove.height == 0) {
                gestureMapMessageRemove.height = this.mGlMapView.getMapHeight();
            }
            int mapGestureState = gestureMapMessageRemove.getMapGestureState();
            if (mapGestureState == 100) {
                gestureBegin();
            } else if (mapGestureState == 101) {
                gestureMapMessageRemove.runCameraUpdate(gLMapState);
            } else if (mapGestureState == 102) {
                gestureEnd();
            }
            this.mGestureEndMessageList.add(gestureMapMessageRemove);
        }
        if (this.mGestureEndMessageList.size() == 1) {
            recycleMessage();
        }
    }

    private void gestureBegin() {
        this.map_gesture_count++;
    }

    private void gestureEnd() {
        int i = this.map_gesture_count - 1;
        this.map_gesture_count = i;
        if (i == 0) {
            recycleMessage();
        }
    }

    private void recycleMessage() {
        GestureMapMessage gestureMapMessageRemove;
        while (this.mGestureEndMessageList.size() > 0 && (gestureMapMessageRemove = this.mGestureEndMessageList.remove(0)) != null) {
            if (gestureMapMessageRemove instanceof MoveGestureMapMessage) {
                ((MoveGestureMapMessage) gestureMapMessageRemove).recycle();
            } else if (gestureMapMessageRemove instanceof HoverGestureMapMessage) {
                ((HoverGestureMapMessage) gestureMapMessageRemove).recycle();
            } else if (gestureMapMessageRemove instanceof RotateGestureMapMessage) {
                ((RotateGestureMapMessage) gestureMapMessageRemove).recycle();
            } else if (gestureMapMessageRemove instanceof ScaleGestureMapMessage) {
                ((ScaleGestureMapMessage) gestureMapMessageRemove).recycle();
            }
        }
    }

    private void processStateMapMessage(GLMapState gLMapState) {
        CameraUpdateMessage cameraUpdateMessageRemove;
        if (this.mStateMessageList.size() <= 0) {
            if (this.isMoveCameraStep) {
                this.isMoveCameraStep = false;
                return;
            }
            return;
        }
        this.isMoveCameraStep = true;
        if (gLMapState == null) {
            return;
        }
        while (this.mStateMessageList.size() > 0 && (cameraUpdateMessageRemove = this.mStateMessageList.remove(0)) != null) {
            if (cameraUpdateMessageRemove.width == 0) {
                cameraUpdateMessageRemove.width = this.mGlMapView.getMapWidth();
            }
            if (cameraUpdateMessageRemove.height == 0) {
                cameraUpdateMessageRemove.height = this.mGlMapView.getMapHeight();
            }
            gLMapState.recalculate();
            cameraUpdateMessageRemove.runCameraUpdate(gLMapState);
        }
    }

    public Context getContext() {
        return this.mContext.getApplicationContext();
    }

    public void changeMapEnv(int i) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeChangeMapEnv(i, j, "");
            this.mCacheMgr.setMapCachePath(nativeGetMapCachePath(i, this.mNativeMapengineInstance));
            this.mCacheMgr.checkDir();
        }
    }

    public boolean isMapEngineValid() {
        boolean z;
        synchronized (this.mut_lock) {
            z = this.mNativeMapengineInstance != 0;
        }
        return z;
    }

    public void setMapCoreListener(GLMapCoreCallback gLMapCoreCallback) {
        this.mapcoreListener = gLMapCoreCallback;
    }

    public void clearAllMessages(int i) {
        nativeClearAllMessage(i, this.mNativeMapengineInstance);
    }

    public void clearAllRequestTiles() {
        TilesProcessingCtrl tilesProcessingCtrl = this.mTileProcessCtrl;
        if (tilesProcessingCtrl != null) {
            tilesProcessingCtrl.clearAll();
        }
    }

    public synchronized void addGestureMessage(int i, GestureMapMessage gestureMapMessage, boolean z, int i2, int i3) {
        if (gestureMapMessage == null) {
            return;
        }
        gestureMapMessage.isGestureScaleByMapCenter = z;
        this.mGestureMessageList.add(gestureMapMessage);
    }

    public boolean isInMapAction(int i) {
        return nativeGetGestureMsgCount(i, this.mNativeMapengineInstance) > 0;
    }

    public boolean isInMapAnimation(int i) {
        return getAnimateionsCount() > 0;
    }

    public int getAnimateionsCount() {
        if (this.mNativeMapengineInstance != 0) {
            return this.map_anims_mgr.getAnimationsCount();
        }
        return 0;
    }

    public void setMapState(int i, GLMapState gLMapState) {
        setMapState(i, gLMapState, true);
    }

    public synchronized void setMapState(int i, GLMapState gLMapState, boolean z) {
        IAMap iAMap;
        if (this.mNativeMapengineInstance != 0) {
            if (z && (iAMap = this.mGlMapView) != null && iAMap.getMapConfig() != null) {
                this.mGlMapView.checkMapState(gLMapState);
            }
            gLMapState.setNativeMapengineState(i, this.mNativeMapengineInstance);
        }
    }

    public synchronized GLMapState getNewMapState(int i) {
        if (this.mNativeMapengineInstance == 0) {
            return null;
        }
        return new GLMapState(i, this.mNativeMapengineInstance);
    }

    public synchronized GLMapState getCloneMapState() {
        if (this.copyGLMapState == null) {
            this.copyGLMapState = new GLMapState(1, getMapEnginePtr());
        }
        this.copyGLMapState.setMapZoomer(this.mGlMapView.getMapConfig().getS_z());
        this.copyGLMapState.setCameraDegree(this.mGlMapView.getMapConfig().getS_c());
        this.copyGLMapState.setMapAngle(this.mGlMapView.getMapConfig().getS_r());
        this.copyGLMapState.setMapGeoCenter(this.mGlMapView.getMapConfig().getS_x(), this.mGlMapView.getMapConfig().getS_y());
        return this.copyGLMapState;
    }

    public synchronized GLMapState getMapState(int i) {
        if (this.mNativeMapengineInstance == 0) {
            return null;
        }
        this.s.setMapstateInstance(getStateInstanceWithEngineID(i));
        return this.s;
    }

    public long getMapStateInstance(int i) {
        return getStateInstanceWithEngineID(i);
    }

    private List<MapSourceGridData> getReqGridList(int i) {
        if (i == 0) {
            return this.roadReqMapGrids;
        }
        if (i == 1) {
            return this.bldReqMapGrids;
        }
        if (i == 3) {
            return this.stiReqMapGirds;
        }
        if (i == 4) {
            return this.vectmcReqMapGirds;
        }
        if (i == 15) {
            return this.geometryReqMapGrids;
        }
        switch (i) {
            case 7:
                return this.regionReqMapGrids;
            case 8:
                return this.poiReqMapGrids;
            case 9:
                return this.versionMapGrids;
            case 10:
                return this.indoorMapGrids;
            case 11:
                return this.guideReqMapGirds;
            case 12:
                return this.mapHeatReqMapGirds;
            default:
                return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:49:0x0106  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void processRequiredData(final int i, List<MapSourceGridData> list, int i2, String str) {
        BaseMapLoader indoorMapLoader;
        byte[] bArr;
        int iNativeTmcCacheCheckValid;
        String str2;
        if (!NetworkState.getInstance().isInternetConnected(this.mContext)) {
            int i3 = this.mShowErrowTipCouont;
            if (i3 <= 0) {
                this.mShowErrowTipCouont = i3 + 1;
                onException(i, 1002);
            }
            GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
            if (gLMapCoreCallback != null) {
                gLMapCoreCallback.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.1
                    @Override // java.lang.Runnable
                    public void run() {
                        GLMapEngine.this.setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_NETWORK, 1002, 0, 0, 0);
                    }
                });
                return;
            }
            return;
        }
        this.mShowErrowTipCouont = 0;
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        for (int i4 = 0; i4 < size; i4++) {
            MapSourceGridData mapSourceGridData = list.get(i4);
            if (!this.mTileProcessCtrl.isProcessing(mapSourceGridData.getKeyGridName())) {
                if (i2 == 4) {
                    String gridName = mapSourceGridData.getGridName();
                    if (!TextUtils.isEmpty(gridName) && 1 != (iNativeTmcCacheCheckValid = nativeTmcCacheCheckValid(i, this.mNativeMapengineInstance, gridName, true, false, (bArr = new byte[100])))) {
                        if (iNativeTmcCacheCheckValid == 0) {
                            byte b = bArr[0];
                            if (b > 0) {
                                try {
                                    str2 = new String(bArr, 1, b, "UTF-8");
                                } catch (UnsupportedEncodingException unused) {
                                }
                            } else {
                                str2 = null;
                            }
                            if (!TextUtils.isEmpty(str2)) {
                                mapSourceGridData.mObj = str2;
                            }
                            arrayList.add(mapSourceGridData);
                        } else {
                            arrayList.add(mapSourceGridData);
                        }
                    }
                } else {
                    arrayList.add(mapSourceGridData);
                }
            }
        }
        if (arrayList.size() > 0) {
            if (i2 != 4) {
                switch (i2) {
                    case 10:
                        indoorMapLoader = new IndoorMapLoader(i, this, i2);
                        break;
                    case 11:
                        GuideMapLoader guideMapLoader = new GuideMapLoader(i, this, i2);
                        guideMapLoader.setGetRequestMethod(false);
                        indoorMapLoader = guideMapLoader;
                        break;
                    case 12:
                        HeatMapLoader heatMapLoader = new HeatMapLoader(i, this, i2);
                        heatMapLoader.setGetRequestMethod(false);
                        heatMapLoader.setMapHeatPoiId(str);
                        indoorMapLoader = heatMapLoader;
                        break;
                    default:
                        int[] mapModeState = getMapModeState(i, false);
                        if (mapModeState != null) {
                            NormalMapLoader normalMapLoader = new NormalMapLoader(i, this, i2);
                            normalMapLoader.setGetRequestMethod(false);
                            normalMapLoader.setMapParams((int) getMapZoomer(i), mapModeState[0], mapModeState[2]);
                            indoorMapLoader = normalMapLoader;
                        } else {
                            indoorMapLoader = null;
                        }
                        break;
                }
            } else {
                int[] mapModeState2 = getMapModeState(i, false);
                if (mapModeState2 != null) {
                    TmcMapLoader tmcMapLoader = new TmcMapLoader(i, this, i2);
                    tmcMapLoader.setGetRequestMethod(false);
                    tmcMapLoader.setMapParams((int) getMapZoomer(i), mapModeState2[0], mapModeState2[2]);
                    indoorMapLoader = tmcMapLoader;
                } else {
                    indoorMapLoader = null;
                }
            }
            int size2 = arrayList.size();
            for (int i5 = 0; i5 < size2; i5++) {
                MapSourceGridData mapSourceGridData2 = (MapSourceGridData) arrayList.get(i5);
                this.mTileProcessCtrl.addProcessingTile(mapSourceGridData2.getKeyGridName());
                indoorMapLoader.addRequestTiles(mapSourceGridData2);
            }
            ConnectionManager connectionManager = this.mConnectionManager;
            if (connectionManager != null) {
                connectionManager.insertConntionTask(indoorMapLoader);
            }
        }
    }

    public boolean isGridsInScreen(int i, List<MapSourceGridData> list, int i2) {
        try {
            if (list.size() == 0 || !isMapEngineValid()) {
                return false;
            }
            synchronized (this.mCurGridFillLock) {
                int size = list.size();
                for (int i3 = 0; i3 < size; i3++) {
                    if (isGridInList(i, list.get(i3).getGridName())) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public int GetCurrentGrideNameLen(int i) {
        try {
            if (!isMapEngineValid()) {
                return 0;
            }
            synchronized (this.mCurGridFillLock) {
                if (this.mCurScreenGridsCount <= 0) {
                    return 0;
                }
                if (i == 1) {
                    return this.mCurScreenGirds.get(0).getGridName().length();
                }
                if (i != 2) {
                    return 0;
                }
                return this.mEagleEyeScreenGirds.get(0).getGridName().length();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isGridInScreen(int i, int i2, String str) {
        try {
            if (!isMapEngineValid()) {
                return false;
            }
            synchronized (this.mCurGridFillLock) {
                return isGridInList(i, str);
            }
        } catch (Exception unused) {
            return true;
        }
    }

    private boolean isIndoorGridInList(String str, List<MapSourceGridData> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKeyGridName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private boolean isGridInList(int i, String str) {
        int i2 = this.mCurScreenGridsCount;
        for (int i3 = 0; i3 < i2; i3++) {
            if (i == 1) {
                if (this.mCurScreenGirds.get(i3).getGridName().startsWith(str)) {
                    return true;
                }
            } else if (i == 2 && this.mEagleEyeScreenGirds.get(i3).getGridName().startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public void clearException(int i) {
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.clearException(i);
        }
    }

    public void onException(int i, int i2) {
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.onException(i, i2);
        }
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onMapSurfaceRenderer(int i, int i2) {
        MapListener mapListener;
        GLMapCoreCallback gLMapCoreCallback;
        try {
            if (i2 == 0) {
                if (2 != IsRealCityAnimateFinish(i) || (mapListener = this.mMapListener) == null) {
                    return;
                }
                mapListener.onRealCityAnimateFinish(i);
                return;
            }
            if (i2 == 1) {
                MapListener mapListener2 = this.mMapListener;
                if (mapListener2 != null) {
                    mapListener2.beforeDrawFrame(i, getMapState(i));
                    return;
                }
                return;
            }
            if (i2 == 3) {
                IAMapListener iAMapListener = this.mAMapListener;
                if (iAMapListener != null) {
                    iAMapListener.beforeDrawLabel(i, getMapState(i));
                    return;
                }
                return;
            }
            if (i2 == 4) {
                IAMapListener iAMapListener2 = this.mAMapListener;
                if (iAMapListener2 != null) {
                    iAMapListener2.afterDrawLabel(i, getMapState(i));
                    return;
                }
                return;
            }
            if (i2 != 5) {
                if (i2 == 11) {
                    fillCurScreenGrids(i);
                    return;
                } else {
                    if (i2 == 10000 && (gLMapCoreCallback = this.mapcoreListener) != null) {
                        gLMapCoreCallback.resetRenderTimeLong();
                        return;
                    }
                    return;
                }
            }
            MapListener mapListener3 = this.mMapListener;
            if (mapListener3 != null) {
                mapListener3.afterDrawFrame(i, getMapState(i));
            }
            IAMapListener iAMapListener3 = this.mAMapListener;
            if (iAMapListener3 != null) {
                iAMapListener3.afterDrawFrame(i, getMapState(i));
            }
        } catch (Throwable unused) {
        }
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onMapDataRequired(int i, int i2, String[] strArr) {
        List<MapSourceGridData> reqGridList;
        if (strArr == null || strArr.length == 0 || this.mNativeMapengineInstance == 0 || (reqGridList = getReqGridList(i2)) == null) {
            return;
        }
        for (int i3 = 0; i3 < reqGridList.size(); i3++) {
            reqGridList.get(i3).recycle();
        }
        reqGridList.clear();
        int length = strArr.length;
        String strNativeGetMapHeatPoiId = null;
        MapSourceGridData mapSourceGridDataObtain = null;
        for (int i4 = 0; i4 < length; i4++) {
            if (i2 == 12) {
                strNativeGetMapHeatPoiId = nativeGetMapHeatPoiId(i, this.mNativeMapengineInstance);
                if (!TextUtils.isEmpty(strNativeGetMapHeatPoiId)) {
                    mapSourceGridDataObtain = MapSourceGridData.obtain();
                    mapSourceGridDataObtain.setGridData(strArr[i4], strNativeGetMapHeatPoiId, i2);
                }
            } else {
                mapSourceGridDataObtain = MapSourceGridData.obtain();
                mapSourceGridDataObtain.setGridData(strArr[i4], i2);
            }
            if (mapSourceGridDataObtain != null) {
                reqGridList.add(mapSourceGridDataObtain);
            }
        }
        if (i2 != 5) {
            processRequiredData(i, reqGridList, i2, strNativeGetMapHeatPoiId);
        }
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public byte[] onCharBitmapRequired(int i, int i2) {
        byte[] textPixelBuffer = this.mTextTextureGenerator.getTextPixelBuffer(i2);
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.resetRenderTimeLong();
        }
        return textPixelBuffer;
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public byte[] onMapCharsWidthsRequired(int i, int[] iArr, int i2, int i3) {
        return this.mTextTextureGenerator.getCharsWidths(iArr);
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onClearCache(int i) {
        VMapDataCache.getInstance().reset();
    }

    @Override // com.autonavi.ae.gmap.callback.JniIndoorCallback
    public void onIndoorBuildingActivity(int i, byte[] bArr) {
        IAMap iAMap = this.mGlMapView;
        if (iAMap != null) {
            try {
                iAMap.onIndoorBuildingActivity(i, bArr);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    @Override // com.autonavi.ae.gmap.callback.JniIndoorCallback
    public void onIndoorDataRequired(int i, int i2, String[] strArr, int[] iArr, int[] iArr2) {
        List<MapSourceGridData> reqGridList;
        if (strArr == null || strArr.length == 0 || (reqGridList = getReqGridList(i2)) == null) {
            return;
        }
        for (int i3 = 0; i3 < reqGridList.size(); i3++) {
            reqGridList.get(i3).recycle();
        }
        reqGridList.clear();
        for (int i4 = 0; i4 < strArr.length; i4++) {
            MapSourceGridData mapSourceGridDataObtain = MapSourceGridData.obtain();
            mapSourceGridDataObtain.setGridData(strArr[i4], i2, iArr[i4], iArr2[i4]);
            reqGridList.add(mapSourceGridDataObtain);
        }
        if (i2 != 5) {
            processRequiredData(i, reqGridList, i2, null);
        }
    }

    private List<MapSourceGridData> parseTiles(byte[] bArr, int i) {
        ArrayList arrayList = null;
        try {
            byte b = bArr[0];
            if (b <= 0) {
                return null;
            }
            ArrayList arrayList2 = new ArrayList();
            int i2 = 1;
            for (int i3 = 0; i3 < b; i3++) {
                int i4 = i2 + 1;
                try {
                    byte b2 = bArr[i2];
                    String str = new String(bArr, i4, b2, Constants.UTF_8);
                    i2 = i4 + b2 + 1;
                    arrayList2.add(new MapSourceGridData(str, i));
                } catch (Throwable th) {
                    th = th;
                    arrayList = arrayList2;
                }
            }
            return arrayList2;
        } catch (Throwable th2) {
            th = th2;
        }
        th.printStackTrace();
        return arrayList;
    }

    public ArrayList<String> getCurScreenGridList() {
        ArrayList<String> arrayList = new ArrayList<>();
        synchronized (this.mCurGridFillLock) {
            for (int i = 0; i < this.mCurScreenGridsCount; i++) {
                arrayList.add(this.mCurScreenGirds.get(i).getGridName());
            }
        }
        return arrayList;
    }

    private void fillCurScreenGrids(int i) throws UnsupportedEncodingException {
        int i2;
        if (this.mNativeMapengineInstance == 0) {
            return;
        }
        synchronized (this.mCurGridFillLock) {
            try {
                nativeGetScreenGrids(i, this.mNativeMapengineInstance, this.tmp_3072bytes_data, 5);
                byte b = this.tmp_3072bytes_data[0];
                if (b <= 0) {
                    return;
                }
                if (b > 200) {
                    b = 200;
                }
                this.mCurScreenGridsCount = 0;
                if (i == 1) {
                    if (this.mCurScreenGirds != null) {
                        for (int i3 = 0; i3 < this.mCurScreenGirds.size(); i3++) {
                            this.mCurScreenGirds.get(i3).recycle();
                        }
                        this.mCurScreenGirds.clear();
                    } else {
                        this.mCurScreenGirds = new ArrayList();
                    }
                } else if (i == 2) {
                    if (this.mEagleEyeScreenGirds != null) {
                        for (int i4 = 0; i4 < this.mEagleEyeScreenGirds.size(); i4++) {
                            this.mEagleEyeScreenGirds.get(i4).recycle();
                        }
                        this.mEagleEyeScreenGirds.clear();
                    } else {
                        this.mEagleEyeScreenGirds = new ArrayList();
                    }
                }
                int i5 = 1;
                for (int i6 = 0; i6 < b && i5 < 3072; i6++) {
                    int i7 = i5 + 1;
                    byte b2 = this.tmp_3072bytes_data[i5];
                    if (b2 <= 0 || b2 > 20 || (i2 = i7 + b2) > 3072) {
                        break;
                    }
                    String str = new String(this.tmp_3072bytes_data, i7, b2, charset);
                    i5 = i2 + 1;
                    if (i == 1) {
                        MapSourceGridData mapSourceGridDataObtain = MapSourceGridData.obtain();
                        mapSourceGridDataObtain.setGridData(str, 5);
                        this.mCurScreenGirds.add(mapSourceGridDataObtain);
                        this.mCurScreenGridsCount++;
                    } else if (i == 2) {
                        MapSourceGridData mapSourceGridDataObtain2 = MapSourceGridData.obtain();
                        mapSourceGridDataObtain2.setGridData(str, 5);
                        this.mEagleEyeScreenGirds.add(mapSourceGridDataObtain2);
                        this.mCurScreenGridsCount++;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public static long bytes2Long(byte[] bArr) {
        long j = 0;
        for (int i = 0; i < 8; i++) {
            j = (j << 8) | ((long) (bArr[i] & UByte.MAX_VALUE));
        }
        return j;
    }

    public boolean canStopMapRender(int i) {
        synchronized (this.mut_lock) {
            long j = this.mNativeMapengineInstance;
            if (j == 0) {
                return true;
            }
            return nativeCanStopRenderMap(i, j);
        }
    }

    public void setInternaltexture(int i, byte[] bArr, int i2) {
        if (bArr != null) {
            nativeSetInternalTexture(i, this.mNativeMapengineInstance, bArr, i2);
        }
    }

    public synchronized void setMapCenter(int i, int i2, int i3) {
        GLMapState.nativeSetMapCenter(getStateInstanceWithEngineID(i), i2, i3);
    }

    public synchronized Point getMapCenter(int i) {
        Point point;
        point = new Point();
        GLMapState.nativeGetMapCenter(getStateInstanceWithEngineID(i), point);
        return point;
    }

    public synchronized void setMapZoom(int i, float f) {
        if (f > getMaxZoomLevel(i)) {
            f = getMaxZoomLevel(i);
        }
        GLMapState.nativeSetMapZoomer(getStateInstanceWithEngineID(i), f);
    }

    public synchronized byte[] getLabelBuffer(int i, int i2, int i3, int i4) {
        byte[] bArr;
        bArr = new byte[3072];
        nativeSelectMapPois(i, this.mNativeMapengineInstance, i2, i3, i4, bArr);
        return bArr;
    }

    public void clearSelectMapPois(int i) {
        nativeClearSelectedScenicPois(i, this.mNativeMapengineInstance);
    }

    public boolean putMapHeatData(int i, byte[] bArr, int i2, int i3, int i4, String str) {
        boolean z = false;
        if (bArr.length == 0 || str == null || str.length() == 0) {
            return false;
        }
        synchronized (this.mut_lock) {
            if (this.mNativeMapengineInstance != 0) {
                try {
                    byte[] bytes = str.getBytes(Constants.UTF_8);
                    int length = bytes.length;
                    int i5 = length + 1;
                    byte[] bArr2 = new byte[i5 + i3];
                    bArr2[0] = (byte) length;
                    System.arraycopy(bytes, 0, bArr2, 1, length);
                    System.arraycopy(bArr, i2, bArr2, i5, i3);
                    z = nativePutMapdata(i, this.mNativeMapengineInstance, 12, bArr2, i4) > 0;
                    GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
                    if (gLMapCoreCallback != null) {
                        gLMapCoreCallback.resetRenderTimeLong();
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        return z;
    }

    public void putMapTMCData(int i, int i2, byte[] bArr, String str, boolean z) {
        synchronized (this.mut_lock) {
            long j = this.mNativeMapengineInstance;
            if (j != 0) {
                nativeUpdateTmcCache(i, j, i2, bArr, str, z);
                GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
                if (gLMapCoreCallback != null) {
                    gLMapCoreCallback.resetRenderTimeLong();
                }
            }
        }
    }

    public void PutResourceData(int i, byte[] bArr) {
        synchronized (this.mut_lock) {
            long j = this.mNativeMapengineInstance;
            if (j != 0) {
                nativePutResourceData(i, j, bArr);
                GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
                if (gLMapCoreCallback != null) {
                    gLMapCoreCallback.resetRenderTimeLong();
                }
            }
        }
    }

    public void UpdateStyleOrIcons(final int i, final String str, final int i2) {
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback == null || 0 == this.mNativeMapengineInstance) {
            return;
        }
        gLMapCoreCallback.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.2
            @Override // java.lang.Runnable
            public void run() {
                GLMapEngine.nativeUpdateStyleOrIcons(i, GLMapEngine.this.mNativeMapengineInstance, str, i2);
            }
        });
    }

    public boolean putMapData(int i, byte[] bArr, int i2, int i3, int i4, int i5) {
        boolean z;
        synchronized (this.mut_lock) {
            long j = this.mNativeMapengineInstance;
            z = false;
            if (j != 0) {
                if (bArr.length == i3 && i2 == 0) {
                    if (nativePutMapdata(i, j, i4, bArr, i5) > 0) {
                        z = true;
                    }
                } else {
                    byte[] bArr2 = new byte[i3];
                    System.arraycopy(bArr, i2, bArr2, 0, i3);
                    if (nativePutMapdata(i, this.mNativeMapengineInstance, i4, bArr2, i5) > 0) {
                        z = true;
                    }
                }
                GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
                if (gLMapCoreCallback != null) {
                    gLMapCoreCallback.resetRenderTimeLong();
                }
            }
        }
        return z;
    }

    public void setIndoorBuildingToBeActive(int i, String str, int i2, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        nativeSetIndoorBuildingToBeActive(i, this.mNativeMapengineInstance, str, i2, str2);
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.resetRenderTimeLongLong();
        }
    }

    public void resetRenderTime(int i) {
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.resetRenderTimeLongLong();
        }
    }

    public float getMapZoomer(int i) {
        return GLMapState.nativeGetMapZoomer(getStateInstanceWithEngineID(i));
    }

    public int getMaxZoomLevel(int i) {
        return GLMapState.nativeGetMaxZoomLevel(getStateInstanceWithEngineID(i));
    }

    public int getMinZoomLevel(int i) {
        return GLMapState.nativeGetMinZoomLevel(getStateInstanceWithEngineID(i));
    }

    public synchronized void setCameraDegree(int i, float f) {
        if (f == 65.0f) {
            setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_FORCE_NAVI_LABEL, 0, 0, 1, 0);
        }
        GLMapState.nativeSetCameraDegree(getStateInstanceWithEngineID(i), f);
    }

    public synchronized float getCameraDegree(int i) {
        return GLMapState.nativeGetCameraDegree(getStateInstanceWithEngineID(i));
    }

    public synchronized void setMapAngle(int i, float f) {
        if (f < 0.0f) {
            f += 360.0f;
        } else if (f >= 360.0f) {
            f -= 360.0f;
        }
        GLMapState.nativeSetMapAngle(getStateInstanceWithEngineID(i), f);
    }

    public synchronized float getMapAngle(int i) {
        return GLMapState.nativeGetMapAngle(getStateInstanceWithEngineID(i));
    }

    public void screenToMapGPoint(int i, float f, float f2, PointF pointF) {
        GLMapState.nativeScreenToMapGPoint(getStateInstanceWithEngineID(i), f, f2, pointF);
    }

    public void p20ToMapPoint(int i, int i2, int i3, PointF pointF) {
        GLMapState.nativeP20ToMapPoint(getStateInstanceWithEngineID(i), i2, i3, pointF);
    }

    public void p20ToScreenPoint(int i, int i2, int i3, PointF pointF) {
        GLMapState.nativeP20ToScreenPoint(getStateInstanceWithEngineID(i), i2, i3, pointF);
    }

    public void mapToScreenPoint(int i, float f, float f2, PointF pointF) {
        GLMapState.nativeMapToScreenPoint(getStateInstanceWithEngineID(i), f, f2, pointF);
    }

    public void mapToP20Point(int i, float f, float f2, Point point) {
        GLMapState.nativeMapToP20Point(getStateInstanceWithEngineID(i), f, f2, point);
    }

    public void screenToP20Point(int i, float f, float f2, Point point) {
        GLMapState.nativeScreenToP20Point(getStateInstanceWithEngineID(i), f, f2, point);
    }

    public float getGLUnitWithWin(int i, int i2) {
        return GLMapState.nativeGetGLUnitWithWin(getStateInstanceWithEngineID(i), i2);
    }

    public float getGLUnitWithWinByY(int i, int i2, int i3) {
        return GLMapState.nativeGetGLUnitWithWinByY(getStateInstanceWithEngineID(i), i2, i3);
    }

    public void getPixel20Bound(int i, Rect rect) {
        GLMapState.nativeGetPixel20Bound(getStateInstanceWithEngineID(i), rect);
    }

    public void mapToScreenPointWithZ(int i, float f, float f2, float f3, PointF pointF) {
        GLMapState.nativeMapToScreenPointWithZ(getStateInstanceWithEngineID(i), f, f2, f3, pointF);
    }

    public float getGLUnitWithPixel20(int i, int i2) {
        return GLMapState.nativeGetGLUnitWithPixel20(getStateInstanceWithEngineID(i), i2);
    }

    public void clearAnimations(int i, boolean z) {
        if (z) {
            this.map_anims_mgr.clearAnimations();
            return;
        }
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.3
                @Override // java.lang.Runnable
                public void run() {
                    GLMapEngine.this.map_anims_mgr.clearAnimations();
                }
            });
        }
    }

    public void clearAnimations(int i, boolean z, int i2) {
        if (z) {
            this.map_anims_mgr.clearAnimations();
            return;
        }
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.4
                @Override // java.lang.Runnable
                public void run() {
                    GLMapEngine.this.map_anims_mgr.clearAnimations();
                }
            });
        }
    }

    public void AddGroupAnimation(int i, final int i2, final float f, final int i3, final int i4, final int i5, final int i6, boolean z) {
        this.mapcoreListener.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.5
            @Override // java.lang.Runnable
            public void run() {
                ADGLMapAnimGroup aDGLMapAnimGroup = new ADGLMapAnimGroup(i2);
                aDGLMapAnimGroup.setToCameraDegree(i4, 0);
                aDGLMapAnimGroup.setToMapAngle(i3, 0);
                aDGLMapAnimGroup.setToMapLevel(f, 0);
                aDGLMapAnimGroup.setToMapCenterGeo(i5, i6, 0);
                if (GLMapEngine.this.map_anims_mgr == null || !aDGLMapAnimGroup.isValid()) {
                    return;
                }
                GLMapEngine.this.map_anims_mgr.addAnimation(aDGLMapAnimGroup);
            }
        });
    }

    public void AddGeoAndScreenCenterGroupAnimation(final int i, final int i2, final GLGeoPoint gLGeoPoint, final Point point, final boolean z) {
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback == null || gLGeoPoint == null || point == null) {
            return;
        }
        gLMapCoreCallback.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.6
            @Override // java.lang.Runnable
            public void run() {
                GLMapEngine.nativeAddGeoAndScreenCenterGroupAnimation(i, GLMapEngine.this.mNativeMapengineInstance, i2, gLGeoPoint.x, gLGeoPoint.y, point.x, point.y, z);
            }
        });
    }

    public void startPivotZoomRotateAnim(final int i, final Point point, final float f, int i2, final int i3) {
        if (f == -9999.0f && i2 == -9999) {
            return;
        }
        if (i2 != -9999 && i2 < 0) {
            i2 += 360;
        }
        final int i4 = i2;
        this.mapcoreListener.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.7
            @Override // java.lang.Runnable
            public void run() {
                GLMapEngine.nativeClearAnimation(i, GLMapEngine.this.mNativeMapengineInstance);
                if (point != null) {
                    GLMapEngine.nativeAddZoomRotateAnimation(i, GLMapEngine.this.mNativeMapengineInstance, i3, f, i4, point.x, point.y);
                } else {
                    GLMapEngine.nativeAddZoomRotateAnimation(i, GLMapEngine.this.mNativeMapengineInstance, i3, f, i4, 0, 0);
                }
            }
        });
    }

    public void startPivotZoomAnim(final int i, final Point point, final float f, final int i2) {
        if (point != null && point.x <= 0 && point.y <= 0) {
            point.y = 1;
        }
        MapConfig mapConfig = this.mGlMapView.getMapConfig();
        if (mapConfig != null && f <= mapConfig.getMaxZoomLevel() && f >= mapConfig.getMinZoomLevel()) {
            this.mapcoreListener.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.8
                @Override // java.lang.Runnable
                public void run() {
                    GLMapEngine.nativeFinishAnimations(i, GLMapEngine.this.mNativeMapengineInstance);
                    if (point != null) {
                        GLMapEngine.nativeAddPivotZoomAnimation(i, GLMapEngine.this.mNativeMapengineInstance, i2, f, point.x, point.y);
                    } else {
                        GLMapEngine.nativeAddPivotZoomAnimation(i, GLMapEngine.this.mNativeMapengineInstance, i2, f, 0, 0);
                    }
                }
            });
        }
    }

    public void startMapSlidAnim(final int i, final Point point, final float f, final float f2) {
        this.mapcoreListener.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.9
            @Override // java.lang.Runnable
            public void run() {
                if (point == null) {
                    return;
                }
                try {
                    GLMapEngine.this.clearAnimations(i, true);
                    GLMapState cloneMapState = GLMapEngine.this.getCloneMapState();
                    cloneMapState.reset();
                    cloneMapState.recalculate();
                    float f3 = f;
                    float f4 = f2;
                    float fAbs = Math.abs(f3);
                    float fAbs2 = Math.abs(f4);
                    float f5 = 12000;
                    if ((fAbs > fAbs2 ? fAbs : fAbs2) > f5) {
                        if (fAbs > fAbs2) {
                            f3 = f3 > 0.0f ? f5 : -12000;
                            f4 *= f5 / fAbs;
                        } else {
                            f3 *= f5 / fAbs2;
                            f4 = f4 > 0.0f ? f5 : -12000;
                        }
                    }
                    ADGLMapAnimFling aDGLMapAnimFling = new ADGLMapAnimFling(500, GLMapEngine.this.mGlMapView.getMapWidth() / 2, GLMapEngine.this.mGlMapView.getMapHeight() / 2);
                    aDGLMapAnimFling.setPositionAndVelocity(f3, f4);
                    aDGLMapAnimFling.commitAnimation(cloneMapState);
                    GLMapEngine.this.map_anims_mgr.addAnimation(aDGLMapAnimFling);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    public void FinishAnimations(final int i, boolean z) {
        if (z) {
            nativeFinishAnimations(i, this.mNativeMapengineInstance);
            return;
        }
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.10
                @Override // java.lang.Runnable
                public void run() {
                    GLMapEngine.nativeFinishAnimations(i, GLMapEngine.this.mNativeMapengineInstance);
                }
            });
        }
    }

    public void setOvelayBundle(int i, GLOverlayBundle<BaseMapOverlay<?, ?>> gLOverlayBundle) {
        if (this.mStateSparseArray.get(i) == null || i < 0) {
            return;
        }
        this.mStateSparseArray.get(i).mOverlayBundle = gLOverlayBundle;
    }

    public void addOverlayTexture(int i, GLTextureProperty gLTextureProperty) {
        if (gLTextureProperty == null || gLTextureProperty.mBitmap == null || gLTextureProperty.mBitmap.isRecycled()) {
            return;
        }
        nativeAddOverlayTexture(i, this.mNativeMapengineInstance, gLTextureProperty.mId, gLTextureProperty.mAnchor, gLTextureProperty.mXRatio, gLTextureProperty.mYRatio, gLTextureProperty.mBitmap, gLTextureProperty.isGenMimps, gLTextureProperty.isRepeat);
    }

    public GLOverlayBundle<BaseMapOverlay<?, ?>> getOvelayBundle(int i) {
        if (this.mStateSparseArray.get(i) == null || i < 0) {
            return null;
        }
        return this.mStateSparseArray.get(i).mOverlayBundle;
    }

    public long getGlOverlayMgrPtr(int i) {
        return nativeGetGlOverlayMgrPtr(i, this.mNativeMapengineInstance);
    }

    public long getMapEnginePtr() {
        return this.mNativeMapengineInstance;
    }

    public long createOverlay(int i, int i2) {
        return nativeCreateOverlay(i, this.mNativeMapengineInstance, i2);
    }

    public static void destoryOverlay(int i, long j) {
        nativeDestoryOverlay(i, j);
    }

    public synchronized void setParamater(int i, int i2, int i3, int i4, int i5, int i6) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeSetParmater(i, j, i2, i3, i4, i5, i6);
        }
    }

    public void addPoiFilter(int i, int i2, int i3, int i4, float f, float f2, String str, int i5) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeAddPoiFilter(i, j, i2, i3, i4, f, f2, 3.0f, 20.0f, str, i5);
        }
    }

    public void removePoiFilter(int i, String str) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeRemovePoiFilter(i, j, str);
        }
    }

    public void clearPoiFilter(int i) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeClearPoiFilter(i, j);
        }
    }

    public void setSearchedSubwayIds(int i, String[] strArr) {
        if (strArr != null) {
            long j = this.mNativeMapengineInstance;
            if (j != 0) {
                nativeSetSearchedSubwayIds(i, j, strArr);
            }
        }
    }

    public void setNaviRouteBoardDataListener(RouteBoardDataListener routeBoardDataListener) {
        this.mBoardDataListener = routeBoardDataListener;
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onRoadTips(int i, byte[] bArr) {
        RouteBoardDataListener routeBoardDataListener = this.mBoardDataListener;
        if (routeBoardDataListener == null || bArr == null || bArr.length <= 0) {
            return;
        }
        routeBoardDataListener.setRouteBoardData(i, bArr);
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onOfflineMap(int i, String str, int i2) {
        MapListener mapListener;
        if (str.length() == 0 || (mapListener = this.mMapListener) == null) {
            return;
        }
        mapListener.onOfflineMap(i, str, i2);
    }

    public void setScenicWidgetFilter(int i, ScenicWidgetItem scenicWidgetItem) {
        if (scenicWidgetItem == null) {
            nativeSetScenicFilter(i, this.mNativeMapengineInstance, 0, null);
            return;
        }
        int[] iArr = new int[scenicWidgetItem.mFilters.length * 3];
        for (int i2 = 0; i2 < scenicWidgetItem.mFilters.length; i2++) {
            int i3 = i2 * 3;
            iArr[i3] = scenicWidgetItem.mFilters[i2].mMainKey;
            iArr[i3 + 1] = scenicWidgetItem.mFilters[i2].mSubKey;
            iArr[i3 + 2] = scenicWidgetItem.mFilters[i2].mPoiFlag;
        }
        nativeSetScenicFilter(i, this.mNativeMapengineInstance, scenicWidgetItem.mItemIndex, iArr);
    }

    public void putScenicData(int i, byte[] bArr, int i2, String str) {
        synchronized (this.mut_lock) {
            if (this.mNativeMapengineInstance != 0) {
                try {
                    byte[] bytes = str.getBytes(Constants.UTF_8);
                    byte length = (byte) bytes.length;
                    byte[] bArr2 = new byte[length + i2 + 1];
                    bArr2[0] = length;
                    System.arraycopy(bytes, 0, bArr2, 1, length);
                    System.arraycopy(bArr, 0, bArr2, length + 1, i2);
                    nativePutMapdata(i, this.mNativeMapengineInstance, 101, bArr2, 0);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    public void setShowMask(int i, boolean z) {
        nativeSetShowMask(i, this.mNativeMapengineInstance, z);
    }

    public boolean isShowMask(int i) {
        return nativeIsShowMask(i, this.mNativeMapengineInstance);
    }

    public void setMaskColor(int i, int i2) {
        nativeSetMaskColor(i, this.mNativeMapengineInstance, i2);
    }

    public boolean isShowLandMarkBuildingPoi(int i) {
        return nativeIsShowLandMarkBuildingPoi(i, this.mNativeMapengineInstance);
    }

    public boolean isShowFeatureSpotIcon(int i) {
        return nativeIsShowFeatureSpotIcon(i, this.mNativeMapengineInstance);
    }

    @Override // com.autonavi.ae.gmap.callback.JniScenicCallback
    public void onScenicActive(final int i, byte[] bArr) {
        final ScenicInfor scenicInfor = null;
        if (bArr != null) {
            try {
                byte b = bArr[0];
                ScenicInfor scenicInfor2 = new ScenicInfor();
                try {
                    scenicInfor2.mAoiId = new String(bArr, 1, b, Constants.UTF_8);
                    int i2 = b + 1;
                    int i3 = i2 + 1;
                    scenicInfor2.mHasWidget = bArr[i2] != 0;
                    int i4 = i3 + 1;
                    scenicInfor2.mHasGuideMap = bArr[i3] != 0;
                    int i5 = i4 + 1;
                    scenicInfor2.mHasGuideVoice = bArr[i4] != 0;
                    int i6 = i5 + 1;
                    scenicInfor2.mHasFootPrint = bArr[i5] != 0;
                    int i7 = i6 + 1;
                    scenicInfor2.mHasThermal = bArr[i6] != 0;
                    int i8 = i7 + 1;
                    scenicInfor2.mHasRoute = bArr[i7] != 0;
                    scenicInfor2.mRouteNumber = bArr[i8];
                    scenicInfor = scenicInfor2;
                } catch (Throwable th) {
                    th = th;
                    scenicInfor = scenicInfor2;
                    th.printStackTrace();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
        if (gLMapCoreCallback != null) {
            gLMapCoreCallback.postOnUIThread(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.11
                @Override // java.lang.Runnable
                public void run() {
                    if (GLMapEngine.this.mScenicListener != null) {
                        GLMapEngine.this.mScenicListener.onScenicActive(i, scenicInfor);
                    }
                }
            });
        }
    }

    public void setScenicListener(ScenicListener scenicListener) {
        this.mScenicListener = scenicListener;
    }

    public void setScenicGuideEnable(int i, boolean z) {
        setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_PROCESS_GUIDE, z ? 1 : 0, 0, 0, 0);
    }

    public void setMapListener(IAMapListener iAMapListener) {
        this.mAMapListener = iAMapListener;
    }

    public void setMapHeatListener(MapHeatListener mapHeatListener) {
        this.mMapHeatListener = mapHeatListener;
    }

    public void setMapHeatEnable(int i, boolean z) {
        setParamater(i, GLMapStaticValue.AM_PARAMETERNAME_MAP_HEAT, z ? 1 : 0, 0, 0, 0);
    }

    public void setMapHeatPoiRegion(int i, String str, int[] iArr, int[] iArr2) {
        nativeSetMapHeatPoiRegion(i, this.mNativeMapengineInstance, str, iArr, iArr2);
    }

    public void addLabels3rd(int i, int i2, Label3rd[] label3rdArr, boolean z) {
        nativeAddLabels3rd(i, this.mNativeMapengineInstance, i2, label3rdArr, z);
    }

    public void clearLabels3rd(int i, int i2, boolean z) {
        nativeClearLabels3rd(i, this.mNativeMapengineInstance, i2, z);
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapHeatCallback
    public void onMapHeatActive(int i, boolean z) {
        MapHeatListener mapHeatListener = this.mMapHeatListener;
        if (mapHeatListener != null) {
            mapHeatListener.onMapHeatActive(i, z);
        }
    }

    public boolean doMapDataControl(int i, int i2, int i3, int i4, int i5) {
        if (this.mNativeMapengineInstance != 0 && i2 == 1 && i3 == 2 && i5 == 1) {
            new MessageDataThread(i, this, i2, i3, i4, i5).start();
        }
        return true;
    }

    public void setMapDataConrol(final int i, final int i2, final int i3, final int i4, final int i5, final byte[] bArr) {
        GLMapCoreCallback gLMapCoreCallback;
        if (this.mNativeMapengineInstance == 0 || (gLMapCoreCallback = this.mapcoreListener) == null) {
            return;
        }
        gLMapCoreCallback.postQueueEvent(new Runnable() { // from class: com.autonavi.ae.gmap.GLMapEngine.12
            @Override // java.lang.Runnable
            public void run() {
                GLMapEngine.nativeMapDataControl(i, GLMapEngine.this.mNativeMapengineInstance, i2, i3, i4, i5, bArr);
            }
        });
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onTransferParam(int i, int[] iArr) {
        if (iArr == null) {
        }
        switch (iArr[0]) {
            case 10000:
                GLMapCoreCallback gLMapCoreCallback = this.mapcoreListener;
                if (gLMapCoreCallback != null) {
                    gLMapCoreCallback.resetRenderTime(false);
                }
                break;
            case GLMapStaticValue.AM_CALLBACK_CHANGEMAPLOGO /* 10001 */:
                int i2 = iArr[1];
                break;
            case GLMapStaticValue.AM_CALLBACK_NEED_NEXTFRAME /* 10002 */:
                GLMapCoreCallback gLMapCoreCallback2 = this.mapcoreListener;
                if (gLMapCoreCallback2 != null) {
                    int i3 = iArr[1];
                    if (i3 == 2) {
                        gLMapCoreCallback2.resetRenderTimeLong();
                    } else if (i3 != 3) {
                        gLMapCoreCallback2.resetRenderTime(false);
                    } else {
                        gLMapCoreCallback2.resetRenderTimeLongLong();
                    }
                }
                break;
        }
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void onOpenLayerDataRequired(int i, String str, int i2, int i3, int i4, String[] strArr) {
        OpenLayerMapLoader openLayerMapLoader = new OpenLayerMapLoader(i, this, str, i2, i3, i4);
        if (!openLayerMapLoader.createDownUrl(strArr) || this.mConnectionManager == null) {
            return;
        }
        openLayerMapLoader.setGetRequestMethod(false);
        this.mConnectionManager.insertConntionTask(openLayerMapLoader);
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public byte[] OnMapLoadResourceByName(int i, String str) {
        byte[] otherResData = null;
        if (str == null) {
            return null;
        }
        if (this.mGlMapView.getMapConfig().isCustomStyleEnable()) {
            if (str.startsWith("icons_5")) {
                otherResData = getMapCacheMgr().getOtherResDataFromDisk(this.mGlMapView.getMapConfig().getCustomTextureResourcePath());
            } else if (str.startsWith("bktile")) {
                otherResData = getMapCacheMgr().getOtherResData(str);
                int customBackgroundColor = this.mGlMapView.getMapConfig().getCustomBackgroundColor();
                if (customBackgroundColor != 0) {
                    otherResData = writeCustomBackground(otherResData, customBackgroundColor);
                }
            }
            if (otherResData != null) {
                return otherResData;
            }
        }
        return getMapCacheMgr().getOtherResData(str);
    }

    public byte[] writeCustomBackground(byte[] bArr, int i) {
        try {
            Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            Bitmap bitmapCopy = bitmapDecodeByteArray.copy(bitmapDecodeByteArray.getConfig(), true);
            int width = bitmapDecodeByteArray.getWidth();
            int height = bitmapDecodeByteArray.getHeight();
            for (int i2 = 1; i2 < width; i2++) {
                for (int i3 = 1; i3 < height; i3++) {
                    bitmapCopy.setPixel(i2, i3, i);
                }
            }
            byte[] bArrBitmap2bytes = bitmap2bytes(bitmapCopy);
            if (bArrBitmap2bytes == null) {
                bArrBitmap2bytes = bArr;
            }
            bitmapCopy.recycle();
            bitmapDecodeByteArray.recycle();
            return bArrBitmap2bytes;
        } catch (Throwable th) {
            th.printStackTrace();
            return bArr;
        }
    }

    private byte[] bitmap2bytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                return byteArray;
            } catch (Throwable unused) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    th2.printStackTrace();
                }
                return null;
            }
        } catch (Throwable unused2) {
            byteArrayOutputStream = null;
        }
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void OnMapResourceRequired(int i, String str, int i2) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile = new MapTilsCacheAndResManager.RetStyleIconsFile();
        if (this.mCacheMgr == null) {
            return;
        }
        retStyleIconsFile.fullName = str;
        String[] strArrSplit = str.split("_|\\.");
        if (strArrSplit.length < 2) {
            return;
        }
        retStyleIconsFile.name = strArrSplit[0] + "_" + strArrSplit[1];
        try {
            retStyleIconsFile.clientVersion = Integer.parseInt(strArrSplit[2]);
            if (strArrSplit.length > 3) {
                retStyleIconsFile.serverVersion = Integer.parseInt(strArrSplit[3]);
            } else {
                retStyleIconsFile.serverVersion = 1;
            }
        } catch (NumberFormatException unused) {
            retStyleIconsFile.clientVersion = 1;
            retStyleIconsFile.serverVersion = 1;
        }
        retStyleIconsFile.type = i2;
        new StylesIconsUpdate(i, this, retStyleIconsFile).start(getMapSvrAddress() + "/ws/mps/vmap?");
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void OnMapResourceReLoad(int i, String str, int i2) {
        UpdateStyleOrIcons(i, str, i2);
    }

    public void appendOpenLayer(int i, byte[] bArr) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeAppendOpenLayer(i, j, bArr);
        }
    }

    public void deleteOpenLayer(int i, int i2) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            nativeDeleteOpenLayer(i, j, i2);
        }
    }

    public void onClearOverlayTexture(int i) {
        nativeClearOverlayTexture(i, this.mNativeMapengineInstance);
    }

    public int IsSupportRealcity(int i, int i2) {
        return nativeIsSupportRealcity(i, this.mNativeMapengineInstance, i2);
    }

    public String GetRealCityDataVerSion(int i, int i2) {
        return nativeGetRealCityDataVerSion(i, this.mNativeMapengineInstance, i2);
    }

    public int IsRealCityAnimateFinish(int i) {
        return nativeIsRealCityAnimateFinish(i, this.mNativeMapengineInstance);
    }

    public boolean SetMapModeAndStyle(int i, int i2, int i3, int i4, boolean z, boolean z2, StyleItem[] styleItemArr) {
        return nativeSetMapModeAndStyle(i, this.mNativeMapengineInstance, new int[]{i2, i3, i4, 0, 0}, z, z2, styleItemArr);
    }

    public boolean IsSkinExist(int i, int i2, int i3, int i4, int i5, int i6) {
        return nativeIsSkinExist(i, this.mNativeMapengineInstance, new int[]{i2, i3, i4, i5, i6});
    }

    public void createEngineWithFrame(int i, Rect rect, int i2, int i3) {
        if (rect != null) {
            if (i == 2 && this.mEagleEyeScreenGirds == null) {
                this.mEagleEyeScreenGirds = new ArrayList();
            }
            if (this.mCacheMgr == null) {
                MapTilsCacheAndResManagerImpl mapTilsCacheAndResManagerImpl = new MapTilsCacheAndResManagerImpl(this.mContext, nativeGetMapCachePath(i, this.mNativeMapengineInstance));
                this.mCacheMgr = mapTilsCacheAndResManagerImpl;
                mapTilsCacheAndResManagerImpl.checkDir();
            }
            nativeCreateEngineWithFrame(i, this.mNativeMapengineInstance, rect.left, rect.top, rect.width(), rect.height(), i2, i3, false);
            this.mStateSparseArray.put(i, new GLStateInstance(i, nativeGetMapStateInstance(i, this.mNativeMapengineInstance)));
        }
    }

    public void removeEngine(int i) {
        nativeRemoveEngine(i, this.mNativeMapengineInstance);
        SparseArray<GLStateInstance> sparseArray = this.mStateSparseArray;
        if (sparseArray != null) {
            sparseArray.remove(i);
        }
    }

    public void setEngineVisible(int i, boolean z) {
        nativeSetServiceViewVisible(i, z, this.mNativeMapengineInstance);
    }

    public int[] getEngineIDs() {
        return nativeGetEngineIDArray(this.mNativeMapengineInstance);
    }

    public boolean isEngineCreated(int i) {
        int[] engineIDs = getEngineIDs();
        if (engineIDs != null && engineIDs.length > 0) {
            for (int i2 : engineIDs) {
                if (i2 == i) {
                    return true;
                }
            }
        }
        return false;
    }

    private long getStateInstanceWithEngineID(int i) {
        if (this.mStateSparseArray.get(i) == null || i < 0) {
            return 0L;
        }
        return this.mStateSparseArray.get(i).getStateInstance();
    }

    public int getEngineIDWithGestureInfo(EAMapPlatformGestureInfo eAMapPlatformGestureInfo) {
        long j = this.mNativeMapengineInstance;
        if (j != 0) {
            return nativeGetEngineIDWithGestureInfo(j, eAMapPlatformGestureInfo);
        }
        return 1;
    }

    public void setBackGroundColor(int i, float f, float f2, float f3, float f4) {
        nativeSetBackGroundColor(i, this.mNativeMapengineInstance, f, f2, f3, f4);
    }

    public void setServiceViewRect(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        nativeSetServiceViewRect(i, this.mNativeMapengineInstance, i2, i3, i4, i5, i6, i7);
    }

    public boolean getSrvViewStateBoolValue(int i, int i2) {
        return nativeGetSrvViewStateBoolValue(i, this.mNativeMapengineInstance, i2);
    }

    public int getSrvViewStateIntValue(int i, int i2) {
        return nativeGetSrvViewStateIntValue(i, this.mNativeMapengineInstance, i2);
    }

    public float getSrvViewStateFloatValue(int i, int i2) {
        return nativeGetSrvViewStateFloatValue(i, this.mNativeMapengineInstance, i2);
    }

    public void setSrvViewStateBoolValue(int i, int i2, boolean z) {
        nativeSetSrvViewStateBoolValue(i, this.mNativeMapengineInstance, i2, z);
    }

    public void setSrvViewStateIntValue(int i, int i2, int i3) {
        nativeSetSrvViewStateIntValue(i, this.mNativeMapengineInstance, i2, i3);
    }

    public void setSrvViewStateFloatValue(int i, int i2, float f) {
        nativeSetSrvViewStateFloatValue(i, this.mNativeMapengineInstance, i2, f);
    }

    public int[] getMapModeState(int i, boolean z) {
        return nativeGetMapModeState(i, this.mNativeMapengineInstance, z);
    }

    public boolean getIsProcessBuildingMark(int i) {
        return nativeGetIsProcessBuildingMark(i, this.mNativeMapengineInstance);
    }

    @Override // com.autonavi.ae.gmap.callback.JniMapCoreCallback
    public void OnMapAnimationFinished(int i, int i2) {
        MapListener mapListener = this.mMapListener;
        if (mapListener == null || -1 == i2) {
            return;
        }
        mapListener.onMapAnimationFinished(i, i2);
    }
}
