package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.view.CustomScrollView;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class WebViewActivity extends Base2Activity {
    private CustomScrollView mCustomScrollView;
    private ProgressBar mPBWebView;
    private WebView mWebView;
    private TitleBar titleBar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_web_view);
        initView();
        initEvent();
    }

    protected void initView() {
        this.mWebView = (WebView) findViewById(R.id.webview);
        this.mPBWebView = (ProgressBar) findViewById(R.id.pb_webview);
        this.mCustomScrollView = (CustomScrollView) findViewById(R.id.custom_scroll_view);
        this.titleBar = (TitleBar) findViewById(R.id.titleBar);
    }

    protected void initEvent() {
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("weburl");
        String stringExtra2 = intent.getStringExtra("title");
        if (TextUtils.isEmpty(stringExtra2)) {
            stringExtra2 = getString(R.string.setting_instructions);
        }
        this.titleBar.setCenterViewText(stringExtra2);
        this.mWebView.loadUrl(stringExtra);
        this.mWebView.setWebViewClient(new MyWebViewClient());
        this.mWebView.setWebChromeClient(new WebChromeClient() { // from class: im.helmsman.helmsmanandroid.ui.activity.WebViewActivity.1
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                WebViewActivity.this.mCustomScrollView.setVisibility(0);
                WebViewActivity.this.mPBWebView.setProgress(i);
                if (i == 100) {
                    WebViewActivity.this.mPBWebView.setVisibility(8);
                }
                super.onProgressChanged(webView, i);
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            webView.loadData("", "text/html", "UTF-8");
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
        }
    }
}
