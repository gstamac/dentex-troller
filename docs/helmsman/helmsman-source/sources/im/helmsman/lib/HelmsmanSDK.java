package im.helmsman.lib;

import android.content.Context;
import im.helmsman.lib.component.TeleControlComponent;

/* JADX INFO: loaded from: classes2.dex */
public class HelmsmanSDK {
    private static final String HM_SDK_VERSION = "1.0beta";
    private static final int HM_SDK_VERSIONCODE = 1;
    private static Context context;
    private static DataIOManager dataIOManager;

    public String getHM_SDK_VERSION() {
        return HM_SDK_VERSION;
    }

    public int getHM_SDK_VERSIONCODE() {
        return 1;
    }

    public static void init(Context context2) {
        context = context2;
    }

    public static Context getContext() {
        return context;
    }

    public static DataIOManager getDataIOManager() {
        if (dataIOManager == null) {
            synchronized (HelmsmanSDK.class) {
                if (dataIOManager == null) {
                    dataIOManager = new DataIOManager();
                }
            }
        }
        return dataIOManager;
    }

    public static ABaseProduct getProduct() {
        return ABaseProduct.getInstance();
    }

    public static TeleControlComponent getTeleControlComponent() {
        return TeleControlComponent.getTeleControlComponentInstance();
    }

    public static boolean productIsNull() {
        return ABaseProduct.getInstance() == null;
    }
}
