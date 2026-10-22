package cn.jpush.android.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.core.view.PointerIconCompat;
import cn.jpush.android.d.d;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushConstants;
import im.helmsman.helmsmanandroid.factory.OkHttpFactory;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class a extends WebViewClient {
    private final d a;
    private final Context b;
    private boolean c = false;

    public a(d dVar, Context context) {
        this.a = dVar;
        this.b = context;
    }

    @Override // android.webkit.WebViewClient
    public void onLoadResource(WebView webView, String str) {
        super.onLoadResource(webView, str);
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        super.onPageStarted(webView, str, bitmap);
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
        if (this.c) {
            sslErrorHandler.proceed();
            return;
        }
        Context context = this.b;
        if (context == null || context.getClass().isAssignableFrom(Activity.class)) {
            sslErrorHandler.cancel();
            return;
        }
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.b);
            builder.setTitle("提示");
            builder.setMessage("SSL 证书异常，是否继续加载？");
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() { // from class: cn.jpush.android.ui.a.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    sslErrorHandler.cancel();
                }
            });
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() { // from class: cn.jpush.android.ui.a.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    a.this.c = true;
                    sslErrorHandler.proceed();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable th) {
            sslErrorHandler.cancel();
            Logger.w("JPushWebViewClient", "show dialog error:" + th);
        }
    }

    /* JADX WARN: Code duplicated, block: B:37:0x014f  */
    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        Intent intent;
        Context context = webView.getContext();
        Logger.i("JPushWebViewClient", "Url vaule is :" + str);
        try {
            webView.getSettings().setSavePassword(false);
            cn.jpush.android.t.a.a(webView);
            String str2 = String.format(Locale.ENGLISH, "{\"url\":\"%s\"}", str);
            if (str.endsWith(".mp3")) {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.setDataAndType(Uri.parse(str), "audio/*");
                webView.getContext().startActivity(intent2);
                return true;
            }
            if (!str.endsWith(".mp4") && !str.endsWith(".3gp")) {
                if (str.endsWith(".apk")) {
                    webView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                    return true;
                }
                if (str.startsWith(OkHttpFactory.TYPE_HTTP)) {
                    cn.jpush.android.helper.c.a(this.a.c, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, str2, JPushConstants.mApplicationContext);
                } else if (str.startsWith("mailto")) {
                    if (str.lastIndexOf("direct=") < 0 && !str.startsWith("mailto")) {
                        str = (str.indexOf("?") > 0 ? new StringBuilder().append(str).append("&direct=false") : new StringBuilder().append(str).append("?direct=false")).toString();
                        str.lastIndexOf("direct=");
                    }
                    int iIndexOf = str.indexOf("?");
                    String strSubstring = str.substring(0, iIndexOf);
                    String strSubstring2 = str.substring(iIndexOf);
                    Logger.v("JPushWebViewClient", "Uri: " + strSubstring);
                    Logger.v("JPushWebViewClient", "QueryString: " + strSubstring2);
                    if (strSubstring.startsWith("mailto")) {
                        String[] strArrSplit = strSubstring.split(":");
                        if (strArrSplit.length == 2) {
                            int iIndexOf2 = strSubstring2.indexOf("title=") + 6;
                            int iIndexOf3 = strSubstring2.indexOf("&content=");
                            String strSubstring3 = strSubstring2.substring(iIndexOf2, iIndexOf3);
                            String strSubstring4 = strSubstring2.substring(iIndexOf3 + 9);
                            String[] strArr = {strArrSplit[1]};
                            intent = new Intent("android.intent.action.SEND");
                            intent.setType("plain/text");
                            intent.putExtra("android.intent.extra.EMAIL", strArr);
                            intent.putExtra("android.intent.extra.SUBJECT", strSubstring3);
                            intent.putExtra("android.intent.extra.TEXT", strSubstring4);
                        } else {
                            intent = null;
                        }
                    } else {
                        intent = null;
                    }
                    if (intent != null) {
                        context.startActivity(intent);
                    }
                    cn.jpush.android.helper.c.a(this.a.c, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, str2, JPushConstants.mApplicationContext);
                    return true;
                }
                return false;
            }
            Intent intent3 = new Intent("android.intent.action.VIEW");
            intent3.setDataAndType(Uri.parse(str), "video/*");
            webView.getContext().startActivity(intent3);
            return true;
        } catch (Exception unused) {
            Logger.e("JPushWebViewClient", "Invalid url");
            return true;
        }
    }
}
