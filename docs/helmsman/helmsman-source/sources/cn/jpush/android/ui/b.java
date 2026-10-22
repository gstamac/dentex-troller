package cn.jpush.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.ProxyActivityAction;
import cn.jpush.android.t.e;
import cn.jpush.android.webview.bridge.HostJsScope;
import cn.jpush.android.webview.bridge.d;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class b extends ProxyActivityAction {
    public static d a;
    private String b;
    private WebView c;
    private cn.jpush.android.d.d d = null;

    private cn.jpush.android.d.d a(Activity activity, Intent intent) {
        cn.jpush.android.d.d dVarA = cn.jpush.android.o.b.a(activity.getApplicationContext(), activity.getIntent());
        if (dVarA != null) {
            return dVarA;
        }
        Logger.dd("PopWinActivityImpl", "parse entity form plugin plateform");
        String string = intent.getData() != null ? intent.getData().toString() : null;
        if (TextUtils.isEmpty(string) && intent.getExtras() != null) {
            string = intent.getExtras().getString("JMessageExtra");
        }
        return cn.jpush.android.s.b.a(activity, string, "");
    }

    private void a() {
        try {
            e.a(this.c, "addJavascriptInterface", new Class[]{Object.class, String.class}, new Object[]{a, "JPushWeb"});
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("PopWinActivityImpl", "addJavascriptInterface failed:" + e.toString());
        }
    }

    private void a(Activity activity) {
        if (activity.getIntent() != null) {
            try {
                cn.jpush.android.d.d dVarA = a(activity, activity.getIntent());
                this.d = dVarA;
                if (dVarA != null) {
                    this.b = dVarA.c;
                    b(activity);
                    c(activity);
                } else {
                    Logger.ww("PopWinActivityImpl", "Warning，null message entity! Close PopWinActivity!");
                    activity.finish();
                }
                return;
            } catch (Exception e) {
                Logger.ee("PopWinActivityImpl", "Extra data is not serializable!");
                e.printStackTrace();
            }
        } else {
            Logger.ww("PopWinActivityImpl", "PopWinActivity get NULL intent!");
        }
        activity.finish();
    }

    private void b(Activity activity) {
        int identifier = activity.getResources().getIdentifier("jpush_popwin_layout", "layout", activity.getPackageName());
        if (identifier == 0) {
            Logger.ee("PopWinActivityImpl", "Please add layout resource jpush_popwin_layout.xml to res/layout !");
            activity.finish();
            return;
        }
        activity.setContentView(identifier);
        int identifier2 = activity.getResources().getIdentifier("wvPopwin", "id", activity.getPackageName());
        if (identifier2 == 0) {
            Logger.ee("PopWinActivityImpl", "Please use default code in jpush_popwin_layout.xml!");
            activity.finish();
            return;
        }
        WebView webView = (WebView) activity.findViewById(identifier2);
        this.c = webView;
        if (webView == null) {
            Logger.ee("PopWinActivityImpl", "Can not get webView in layout file!");
            activity.finish();
            return;
        }
        webView.setScrollbarFadingEnabled(true);
        this.c.setScrollBarStyle(33554432);
        WebSettings settings = this.c.getSettings();
        settings.setDomStorageEnabled(true);
        cn.jpush.android.t.a.a(settings);
        cn.jpush.android.t.a.a(this.c);
        settings.setSavePassword(false);
        this.c.setBackgroundColor(0);
        a = new d(activity, this.d);
        Logger.dd("PopWinActivityImpl", "Android sdk version greater than or equal to 17, Java—Js interact by annotation!");
        a();
        this.c.setWebChromeClient(new cn.jpush.android.webview.bridge.a("JPushWeb", HostJsScope.class, null, null));
        this.c.setWebViewClient(new a(this.d, activity));
        HostJsScope.setWebViewHelper(a);
    }

    private void c(Activity activity) {
        String str = this.d.Q;
        String str2 = this.d.O;
        Logger.d("PopWinActivityImpl", "showUrl = " + str2);
        if (TextUtils.isEmpty(str) || !new File(str.replace("file://", "")).exists()) {
            this.c.loadUrl(str2);
        } else {
            this.c.loadUrl(str);
        }
        cn.jpush.android.helper.c.a(this.b, 1000, activity);
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onBackPressed(Activity activity) {
        cn.jpush.android.helper.c.a(this.b, 1006, activity.getApplicationContext());
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onConfigurationChanged(Activity activity, Configuration configuration) {
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onCreate(Activity activity, Bundle bundle) {
        cn.jpush.android.t.a.d(activity);
        a(activity);
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public boolean onCreateOptionsMenu(Activity activity, Menu menu) {
        return false;
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onDestroy(Activity activity) {
        WebView webView = this.c;
        if (webView != null) {
            webView.removeAllViews();
            this.c.destroy();
            this.c = null;
        }
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onEvent(Activity activity, String str, Bundle bundle) {
        if (TextUtils.isEmpty(str) || !str.equals("start_push_activity") || this.d == null || this.c == null) {
            return;
        }
        String string = bundle.getString("url");
        if (!TextUtils.isEmpty(string)) {
            this.d.O = string;
            Intent intent = new Intent(activity, (Class<?>) PushActivity.class);
            intent.putExtra("msg_data", this.d.c());
            intent.putExtra(PushActivity.FROM_OTHER_WAY, true);
            intent.setFlags(335544320);
            activity.startActivity(intent);
        }
        activity.finish();
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public boolean onKeyDown(Activity activity, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onNewIntent(Activity activity, Intent intent) {
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onPause(Activity activity) {
        if (this.c != null) {
            this.c.onPause();
        }
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onRestart(Activity activity) {
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onResume(Activity activity) {
        if (this.c != null) {
            this.c.onResume();
            HostJsScope.setWebViewHelper(a);
        }
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onStart(Activity activity, Intent intent) {
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onStop(Activity activity) {
    }
}
