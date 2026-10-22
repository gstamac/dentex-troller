package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.webkit.WebView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class HelpCenterActivity_ViewBinding implements Unbinder {
    private HelpCenterActivity target;

    public HelpCenterActivity_ViewBinding(HelpCenterActivity helpCenterActivity) {
        this(helpCenterActivity, helpCenterActivity.getWindow().getDecorView());
    }

    public HelpCenterActivity_ViewBinding(HelpCenterActivity helpCenterActivity, View view) {
        this.target = helpCenterActivity;
        helpCenterActivity.webview = (WebView) Utils.findRequiredViewAsType(view, R.id.webview, "field 'webview'", WebView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        HelpCenterActivity helpCenterActivity = this.target;
        if (helpCenterActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        helpCenterActivity.webview = null;
    }
}
