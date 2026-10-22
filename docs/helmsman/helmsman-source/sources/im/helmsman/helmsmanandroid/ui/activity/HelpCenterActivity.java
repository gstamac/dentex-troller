package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;

/* JADX INFO: loaded from: classes2.dex */
public class HelpCenterActivity extends Base2Activity {

    @BindView(R.id.webview)
    WebView webview;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_help_center);
        ButterKnife.bind(this);
        WebSettings settings = this.webview.getSettings();
        settings.setBlockNetworkImage(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setLoadsImagesAutomatically(true);
        this.webview.setWebChromeClient(new WebChromeClient());
        this.webview.loadUrl(getIntent().getStringExtra("url"));
    }
}
