package im.helmsman.helmsmanandroid;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;
import com.bugsnag.android.Bugsnag;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.utils.AppForegroundStateManager;
import im.helmsman.helmsmanandroid.utils.LogUtil;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.HelmsmanSDK;
import javy.lib.jsave.JSave;
import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/* JADX INFO: loaded from: classes2.dex */
public class MyApplication extends LitePalApplication implements AppForegroundStateManager.OnAppForegroundStateChangeListener {
    private static MyApplication application;
    private static IWXAPI iwxapi;
    public static String lauchImageUrl;
    public BluetoothDevice bluetoothDevice;
    public String ssid;
    public int versionCode;
    public boolean motorIsRun = false;
    public boolean wifiState = false;
    public int gpsState = 0;
    private boolean appIsForeground = true;

    public void initToken() {
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        application = this;
        initOtherSDK();
        AppForegroundStateManager.getInstance().addListener(this);
        try {
            this.versionCode = getContext().getPackageManager().getPackageInfo(getInstance().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        initToken();
    }

    public boolean isAppIsForeground() {
        return this.appIsForeground;
    }

    public void setAppIsForeground(boolean z) {
        this.appIsForeground = z;
    }

    public static MyApplication getInstance() {
        return application;
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(context);
    }

    @Override // im.helmsman.helmsmanandroid.utils.AppForegroundStateManager.OnAppForegroundStateChangeListener
    public void onAppForegroundStateChange(AppForegroundStateManager.AppForegroundState appForegroundState) {
        ABaseProduct product = HelmsmanSDK.getProduct();
        if (product == null) {
            return;
        }
        if (AppForegroundStateManager.AppForegroundState.IN_FOREGROUND != appForegroundState) {
            if (!HelmsmanSDK.productIsNull() && HelmsmanSDK.getProduct().motorComponent.isOn()) {
                product.motorComponent.motorOff();
            }
            this.appIsForeground = false;
            LogUtil.instance().intoBackground();
            return;
        }
        LogUtil.instance().inTofront();
        this.appIsForeground = true;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [im.helmsman.helmsmanandroid.MyApplication$1] */
    private void initOtherSDK() {
        if (!isApkInDebug()) {
            Bugsnag.init(this);
            Bugsnag.clearUser();
        }
        LitePal.initialize(this);
        HelmsmanSDK.init(this);
        JSave.init(this);
        im.helmsman.helmsmanandroid.utils.JSave.moveDataToNewJSave();
        ZendeskConfig.INSTANCE.init(this, "https://helmsman.zendesk.com", "1e24e5446b71ea9fa97a1b7ebb0ebd8c4510833ef0712832", "mobile_sdk_client_5b76816ffc054f0159b1");
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        new Thread() { // from class: im.helmsman.helmsmanandroid.MyApplication.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                super.run();
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MyApplication.this));
            }
        }.start();
        Config.initConfig();
        RequestInetUtils.instance().getJpushToken();
        RequestInetUtils.instance().getPrivatePolicy();
    }

    public boolean isApkInDebug() {
        try {
            return (getApplicationInfo().flags & 2) != 0;
        } catch (Exception unused) {
            return false;
        }
    }

    public static IWXAPI getWxApi() {
        if (iwxapi == null) {
            IWXAPI iwxapiCreateWXAPI = WXAPIFactory.createWXAPI(getInstance(), "wx6e8c1cff7f9272dd", true);
            iwxapi = iwxapiCreateWXAPI;
            iwxapiCreateWXAPI.registerApp("wx6e8c1cff7f9272dd");
        }
        return iwxapi;
    }
}
