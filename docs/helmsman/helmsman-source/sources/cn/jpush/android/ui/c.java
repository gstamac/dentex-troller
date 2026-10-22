package cn.jpush.android.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.ProxyActivityAction;
import cn.jpush.android.t.e;
import cn.jpush.android.webview.bridge.HostJsScope;
import cn.jpush.android.webview.bridge.d;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class c extends ProxyActivityAction {
    public static d a;
    private String d;
    private WebView e;
    private RelativeLayout f;
    private TextView g;
    private ImageButton h;
    private ProgressBar i;
    private int b = 0;
    private boolean c = false;
    private View.OnClickListener j = new View.OnClickListener() { // from class: cn.jpush.android.ui.c.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view != null) {
                try {
                    if (view.getContext() == null || !(view.getContext() instanceof Activity)) {
                        return;
                    }
                    ((Activity) view.getContext()).onBackPressed();
                } catch (Throwable unused) {
                }
            }
        }
    };

    private cn.jpush.android.d.d a(Activity activity, Intent intent) {
        cn.jpush.android.d.d dVarA = cn.jpush.android.o.b.a(activity, activity.getIntent());
        if (dVarA != null) {
            return dVarA;
        }
        Logger.dd("PushActivityImpl", "parse entity form plugin plateform");
        String string = intent.getData() != null ? intent.getData().toString() : null;
        if (TextUtils.isEmpty(string) && intent.getExtras() != null) {
            string = intent.getExtras().getString("JMessageExtra");
        }
        Logger.d("PushActivityImpl", "content:" + string);
        return cn.jpush.android.s.b.a(activity, string, "");
    }

    private void a(Activity activity, cn.jpush.android.d.d dVar) {
        if (dVar == null) {
            Logger.ww("PushActivityImpl", "Null message entity! Close PushActivity!");
        } else if (dVar.p == 0) {
            activity.setRequestedOrientation(1);
            b(activity, dVar);
            return;
        } else {
            Logger.w("PushActivityImpl", "Invalid msg type to show - " + dVar.p);
            cn.jpush.android.o.b.b(activity, dVar);
        }
        activity.finish();
    }

    private void b() {
        try {
            e.a(this.e, "addJavascriptInterface", new Class[]{Object.class, String.class}, new Object[]{a, "JPushWeb"});
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("PushActivityImpl", "addJavascriptInterface failed:" + e.toString());
        }
    }

    private void b(Activity activity) {
        if (activity.getIntent() != null) {
            try {
                this.c = activity.getIntent().getBooleanExtra(PushActivity.FROM_OTHER_WAY, false);
                cn.jpush.android.d.d dVarA = a(activity, activity.getIntent());
                if (dVarA != null) {
                    this.d = dVarA.c;
                    a(activity, dVarA);
                } else {
                    Logger.ww("PushActivityImpl", "Warning，null message entity! Close PushActivity!");
                    activity.finish();
                }
                return;
            } catch (Exception e) {
                Logger.ee("PushActivityImpl", "Extra data is not serializable!");
                e.printStackTrace();
            }
        } else {
            Logger.ww("PushActivityImpl", "PushActivity get NULL intent!");
        }
        activity.finish();
    }

    private void b(Activity activity, cn.jpush.android.d.d dVar) {
        String str;
        Logger.i("PushActivityImpl", "Action: processShow");
        if (dVar != null) {
            if (dVar.T == 0) {
                this.b = dVar.R;
                int identifier = activity.getResources().getIdentifier("jpush_webview_layout", "layout", activity.getPackageName());
                if (identifier == 0) {
                    str = "Please add layout resource jpush_webview_layout.xml to res/layout !";
                } else {
                    activity.setContentView(identifier);
                    String str2 = dVar.O;
                    if (cn.jpush.android.t.a.a(str2)) {
                        String str3 = dVar.Q;
                        if (dVar.q) {
                            int identifier2 = activity.getResources().getIdentifier("actionbarLayoutId", "id", activity.getPackageName());
                            if (identifier2 == 0) {
                                str = "Please use default code in jpush_webview_layout.xml!";
                            } else {
                                a(activity, activity.findViewById(identifier2), dVar);
                                if (TextUtils.isEmpty(str3) || !new File(str3.replace("file://", "")).exists() || this.c) {
                                    this.e.loadUrl(str2);
                                } else {
                                    this.e.loadUrl(str3);
                                }
                            }
                        }
                        if (!this.c) {
                            cn.jpush.android.helper.c.a(this.d, 1000, activity.getApplicationContext());
                        }
                    } else {
                        cn.jpush.android.o.b.b(activity, dVar);
                    }
                }
                Logger.ee("PushActivityImpl", str);
            }
            int i = dVar.S;
            return;
        }
        Logger.ww("PushActivityImpl", "Null message entity! Close PushActivity!");
        activity.finish();
    }

    private void c(Activity activity) {
        try {
            WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
            attributes.flags &= -1025;
            activity.getWindow().setAttributes(attributes);
            activity.getWindow().clearFlags(512);
        } catch (Throwable unused) {
            Logger.w("PushActivityImpl", "quitFullScreen errno");
        }
    }

    public void a() {
        try {
            RelativeLayout relativeLayout = this.f;
            if (relativeLayout == null || relativeLayout.getVisibility() != 8) {
                return;
            }
            this.f.setVisibility(0);
            c((Activity) this.f.getContext());
            this.h.setOnClickListener(this.j);
            WebView webView = this.e;
            if (webView != null) {
                webView.postDelayed(new Runnable() { // from class: cn.jpush.android.ui.c.3
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (c.this.e != null) {
                                c.this.e.clearHistory();
                            }
                        } catch (Throwable unused) {
                        }
                    }
                }, 1000L);
            }
        } catch (Throwable unused) {
        }
    }

    public void a(Activity activity) {
        activity.finish();
        if (1 == this.b) {
            try {
                ActivityManager activityManager = (ActivityManager) activity.getSystemService("activity");
                ComponentName componentName = activityManager.getRunningTasks(1).get(0).baseActivity;
                ComponentName componentName2 = activityManager.getRunningTasks(1).get(0).topActivity;
                Logger.d("PushActivityImpl", "baseActivity  = " + componentName.toString());
                Logger.d("PushActivityImpl", "topActivity  = " + componentName2.toString());
                if (componentName == null || componentName2 == null || !componentName2.toString().equals(componentName.toString())) {
                    return;
                }
                cn.jpush.android.t.a.e(activity);
            } catch (Exception unused) {
                Logger.ww("PushActivityImpl", "Get running tasks failed.");
                cn.jpush.android.t.a.e(activity);
            }
        }
    }

    public void a(Activity activity, View view, cn.jpush.android.d.d dVar) {
        String str = dVar.P;
        view.setFocusable(true);
        this.e = (WebView) activity.findViewById(activity.getResources().getIdentifier("fullWebView", "id", activity.getPackageName()));
        this.f = (RelativeLayout) activity.findViewById(activity.getResources().getIdentifier("rlRichpushTitleBar", "id", activity.getPackageName()));
        this.g = (TextView) activity.findViewById(activity.getResources().getIdentifier("tvRichpushTitle", "id", activity.getPackageName()));
        this.h = (ImageButton) activity.findViewById(activity.getResources().getIdentifier("imgRichpushBtnBack", "id", activity.getPackageName()));
        this.i = (ProgressBar) activity.findViewById(activity.getResources().getIdentifier("pushPrograssBar", "id", activity.getPackageName()));
        if (this.e == null || this.f == null || this.g == null || this.h == null) {
            Logger.ee("PushActivityImpl", "Please use default code in jpush_webview_layout.xml!");
            activity.finish();
        }
        if (1 == dVar.S) {
            this.f.setVisibility(8);
            activity.getWindow().setFlags(1024, 1024);
        } else {
            this.g.setText(str);
            this.h.setOnClickListener(this.j);
        }
        this.e.setScrollbarFadingEnabled(true);
        this.e.setScrollBarStyle(33554432);
        WebSettings settings = this.e.getSettings();
        cn.jpush.android.t.a.a(settings);
        cn.jpush.android.t.a.a(this.e);
        settings.setSavePassword(false);
        a = new d(activity, dVar);
        Logger.dd("PushActivityImpl", "Android sdk version greater than or equal to 17, Java—Js interact by annotation!");
        b();
        this.e.setWebChromeClient(new cn.jpush.android.webview.bridge.a("JPushWeb", HostJsScope.class, this.i, this.g));
        this.e.setWebViewClient(new a(dVar, activity));
        HostJsScope.setWebViewHelper(a);
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onBackPressed(Activity activity) {
        WebView webView = this.e;
        if (webView != null && webView.canGoBack()) {
            this.e.goBack();
        } else {
            cn.jpush.android.helper.c.a(this.d, 1006, activity);
            a(activity);
        }
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onConfigurationChanged(Activity activity, Configuration configuration) {
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onCreate(Activity activity, Bundle bundle) {
        cn.jpush.android.t.a.d(activity);
        b(activity);
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public boolean onCreateOptionsMenu(Activity activity, Menu menu) {
        return false;
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onDestroy(Activity activity) {
        WebView webView = this.e;
        if (webView != null) {
            webView.removeAllViews();
            this.e.clearSslPreferences();
            this.e.destroy();
            this.e = null;
        }
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onEvent(Activity activity, String str, Bundle bundle) {
        if (TextUtils.isEmpty(str) || !str.equals("push_show_titlebar")) {
            return;
        }
        activity.runOnUiThread(new Runnable() { // from class: cn.jpush.android.ui.c.1
            @Override // java.lang.Runnable
            public void run() {
                c.this.a();
            }
        });
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public boolean onKeyDown(Activity activity, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onNewIntent(Activity activity, Intent intent) {
        activity.setIntent(intent);
        b(activity);
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onPause(Activity activity) {
        if (this.e != null) {
            this.e.onPause();
        }
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onRestart(Activity activity) {
    }

    @Override // cn.jpush.android.local.ProxyActivityAction
    public void onResume(Activity activity) {
        if (this.e != null) {
            this.e.onResume();
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
