package im.helmsman.helmsmanandroid.utils;

import android.content.Intent;
import android.net.Uri;
import im.helmsman.helmsmanandroid.MyApplication;

/* JADX INFO: loaded from: classes2.dex */
public class PermissionUtils {
    public static void startAppSettingActivity() {
        MyApplication myApplication = MyApplication.getInstance();
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", myApplication.getPackageName(), null));
        myApplication.startActivity(intent);
    }
}
