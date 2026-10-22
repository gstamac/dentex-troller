package im.helmsman.helmsmanandroid.utils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.AppVersionInfoResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class AppVersionUtils {
    public boolean isWifiNetWork() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) MyApplication.getInstance().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1;
    }

    public void checkAppVersion() {
        boolean z = Config.isChineseCoordinateCorrection;
    }

    private void googleUpdate() {
        if (isWifiNetWork()) {
            RequestInetUtils.instance().getAppVersionInfo(new Callback<AppVersionInfoResultModel>() { // from class: im.helmsman.helmsmanandroid.utils.AppVersionUtils.1
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(final Response<AppVersionInfoResultModel> response) {
                    String version = response.body().getVersion();
                    final int iStringVersionToIntVersionCode = AppVersionUtils.this.StringVersionToIntVersionCode(version);
                    Object obj = javy.lib.jsave.JSave.get("ignore_version");
                    int iIntValue = obj != null ? ((Integer) obj).intValue() : 0;
                    if (iStringVersionToIntVersionCode <= AppVersionUtils.this.getAppVersionNameCode() || iStringVersionToIntVersionCode == iIntValue) {
                        return;
                    }
                    SpannableString spannableString = new SpannableString(MyApplication.getInstance().getString(R.string.upgrade_to) + version);
                    spannableString.setSpan(new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R.color.black_t_54)), 0, spannableString.length(), 33);
                    AlertDialog alertDialogShowAlertDialog = ViewUtils.showAlertDialog(null, spannableString, MyApplication.getInstance().getString(R.string.upgrade), MyApplication.getInstance().getString(R.string.cancle), new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.utils.AppVersionUtils.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String packageName = MyApplication.getInstance().getPackageName();
                            try {
                                Intent intent = new Intent();
                                intent.addCategory("android.intent.category.LAUNCHER");
                                intent.setAction("android.intent.action.MAIN");
                                intent.setComponent(new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity"));
                                intent.setFlags(268435456);
                                intent.setData(Uri.parse("market://details?id=" + packageName));
                                Base2Activity.getTopActivity().startActivity(intent);
                            } catch (ActivityNotFoundException unused) {
                                Intent intent2 = new Intent();
                                intent2.setAction("android.intent.action.VIEW");
                                intent2.setData(Uri.parse(((AppVersionInfoResultModel) response.body()).getDownload_url()));
                                Base2Activity.getTopActivity().startActivity(intent2);
                            }
                        }
                    }, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.utils.AppVersionUtils.1.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            javy.lib.jsave.JSave.put("ignore_version", Integer.valueOf(iStringVersionToIntVersionCode));
                        }
                    });
                    alertDialogShowAlertDialog.setCancelable(false);
                    alertDialogShowAlertDialog.getButton(-2).setTextColor(MyApplication.getInstance().getResources().getColor(R.color.bdbdbd));
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    Log.e("getAppVersion,onFailure", errorCode.toString());
                }
            });
        }
    }

    public int getAppVersionNameCode() {
        try {
            return StringVersionToIntVersionCode(MyApplication.getInstance().getPackageManager().getPackageInfo(MyApplication.getInstance().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 99999;
        }
    }

    public int getVsersionCode() {
        try {
            return MyApplication.getInstance().getPackageManager().getPackageInfo(MyApplication.getInstance().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 999999;
        }
    }

    public int StringVersionToIntVersionCode(String str) {
        String str2 = "";
        for (String str3 : str.split("\\.")) {
            str2 = str2 + str3;
        }
        try {
            return Integer.valueOf(str2).intValue();
        } catch (NumberFormatException unused) {
            return 999999;
        }
    }
}
