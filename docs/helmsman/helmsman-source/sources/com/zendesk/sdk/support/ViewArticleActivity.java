package com.zendesk.sdk.support;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.deeplinking.ZendeskDeepLinkingParser;
import com.zendesk.sdk.model.helpcenter.Article;
import com.zendesk.sdk.model.helpcenter.Attachment;
import com.zendesk.sdk.model.helpcenter.AttachmentType;
import com.zendesk.sdk.model.helpcenter.SimpleArticle;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.ListRowView;
import com.zendesk.sdk.ui.LoadingState;
import com.zendesk.sdk.ui.NetworkAwareActionbarActivity;
import com.zendesk.sdk.ui.ToolbarSherlock;
import com.zendesk.sdk.util.UiUtils;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.SafeZendeskCallback;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.FileUtils;
import com.zendesk.util.LocaleUtil;
import com.zendesk.util.StringUtils;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/* JADX INFO: loaded from: classes2.dex */
public class ViewArticleActivity extends NetworkAwareActionbarActivity implements AdapterView.OnItemClickListener {
    private static final String ARTICLE_DATE_FORMAT = "MMMM d, yyyy hh:mm";
    private static final String ARTICLE_DETAIL_FORMAT_STRING = "%s %s %s";
    private static final String CSS_FILE = "file:///android_asset/help_center_article_style.css";
    public static final String EXTRA_ARTICLE = "article";
    public static final String EXTRA_SIMPLE_ARTICLE = "article_simple";
    private static final long FETCH_ATTACHMENTS_DELAY_MILLIS = 250;
    private static final String LOG_TAG = "ViewArticleActivity";
    private static final String TYPE_TEXT_HTML = "text/html";
    private static final String UTF_8_ENCODING_TYPE = "UTF-8";
    private Article mArticle;
    private WebView mArticleContentWebView;
    private Long mArticleId;
    private ListView mAttachmentListView;
    private SafeZendeskCallback<List<Attachment>> mAttachmentRequestCallback;
    private final Handler mHandler = new Handler();
    private ProgressBar mProgressView;

    public static void startActivity(Context context, Article article) {
        Intent intent = new Intent(context, (Class<?>) ViewArticleActivity.class);
        intent.putExtra(EXTRA_ARTICLE, article);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, SimpleArticle simpleArticle) {
        Intent intent = new Intent(context, (Class<?>) ViewArticleActivity.class);
        intent.putExtra(EXTRA_SIMPLE_ARTICLE, simpleArticle);
        context.startActivity(intent);
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_view_article);
        ToolbarSherlock.installToolBar(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        WebView webView = (WebView) findViewById(R.id.view_article_content_webview);
        this.mArticleContentWebView = webView;
        webView.setWebChromeClient(new WebChromeClient());
        this.mArticleContentWebView.getSettings().setJavaScriptEnabled(true);
        setupRequestInterceptor();
        this.mArticleContentWebView.getSettings().setMixedContentMode(0);
        this.mProgressView = (ProgressBar) findViewById(R.id.view_article_progress);
        this.mAttachmentListView = (ListView) findViewById(R.id.view_article_attachment_list);
        Bundle extras = getIntent().getExtras();
        boolean z = extras != null && extras.containsKey(EXTRA_ARTICLE) && (extras.getSerializable(EXTRA_ARTICLE) instanceof Article);
        boolean z2 = extras != null && extras.containsKey(EXTRA_SIMPLE_ARTICLE) && (extras.getSerializable(EXTRA_SIMPLE_ARTICLE) instanceof SimpleArticle);
        if (z) {
            this.mArticle = (Article) extras.getSerializable(EXTRA_ARTICLE);
            loadArticleBody();
            return;
        }
        if (z2) {
            SimpleArticle simpleArticle = (SimpleArticle) extras.getSerializable(EXTRA_SIMPLE_ARTICLE);
            if (simpleArticle.getId() != null) {
                fetchArticle(simpleArticle.getId().longValue());
                this.mArticleId = simpleArticle.getId();
                return;
            } else {
                Logger.e(LOG_TAG, "EXTRA_SIMPLE_ARTICLE is present, but missing an article ID. Cannot continue.", new Object[0]);
                finish();
                return;
            }
        }
        Logger.e(LOG_TAG, "EXTRA_ARTICLE or EXTRA_SIMPLE_ARTICLE is a required extra for this activity. Cannot continue.", new Object[0]);
        finish();
    }

    private void setupRequestInterceptor() {
        if (this.mArticleContentWebView == null) {
            Logger.w(LOG_TAG, "The webview is null. Make sure you initialise it before trying to add the interceptor", new Object[0]);
            return;
        }
        final OkHttpClient okHttpClient = new OkHttpClient();
        final ZendeskDeepLinkingParser zendeskDeepLinkingParser = new ZendeskDeepLinkingParser();
        final boolean zOverrideResourceLoadingInWebview = ZendeskConfig.INSTANCE.getSdkOptions().overrideResourceLoadingInWebview();
        this.mArticleContentWebView.setWebViewClient(new WebViewClient() { // from class: com.zendesk.sdk.support.ViewArticleActivity.1
            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                InputStream inputStreamByteStream;
                String str2;
                String strName;
                if (!zOverrideResourceLoadingInWebview) {
                    Logger.w(ViewArticleActivity.LOG_TAG, "Cannot add request interceptor because sdk options prohibit it. See ZendeskConfig.INSTANCE.getSdkOptions()", new Object[0]);
                    return super.shouldInterceptRequest(webView, str);
                }
                String zendeskUrl = ZendeskConfig.INSTANCE.getZendeskUrl();
                if (StringUtils.isEmpty(zendeskUrl) || !str.startsWith(zendeskUrl)) {
                    Logger.w(ViewArticleActivity.LOG_TAG, "Will not intercept request because the url is not hosted by Zendesk" + str, new Object[0]);
                    return super.shouldInterceptRequest(webView, str);
                }
                String storedAccessTokenAsBearerToken = ZendeskConfig.INSTANCE.storage().identityStorage().getStoredAccessTokenAsBearerToken();
                if (StringUtils.isEmpty(storedAccessTokenAsBearerToken)) {
                    Logger.w(ViewArticleActivity.LOG_TAG, "We will not intercept the request there is no authentication present", new Object[0]);
                    return super.shouldInterceptRequest(webView, str);
                }
                InputStream inputStream = null;
                String str3 = null;
                try {
                    Response responseExecute = okHttpClient.newCall(new Request.Builder().url(str).get().addHeader("Authorization", storedAccessTokenAsBearerToken).build()).execute();
                    if (responseExecute == null || !responseExecute.isSuccessful() || responseExecute.body() == null) {
                        strName = null;
                        str2 = null;
                    } else {
                        inputStreamByteStream = responseExecute.body().byteStream();
                        try {
                            MediaType mediaTypeContentType = responseExecute.body().get$contentType();
                            if (mediaTypeContentType != null) {
                                str2 = (StringUtils.hasLength(mediaTypeContentType.type()) && StringUtils.hasLength(mediaTypeContentType.subtype())) ? String.format(Locale.US, "%s/%s", mediaTypeContentType.type(), mediaTypeContentType.subtype()) : null;
                                try {
                                    Charset charset = mediaTypeContentType.charset();
                                    strName = charset != null ? charset.name() : null;
                                } catch (Exception e) {
                                    e = e;
                                    Logger.e(ViewArticleActivity.LOG_TAG, "Exception encountered when trying to intercept request", e, new Object[0]);
                                }
                            } else {
                                strName = null;
                                str2 = null;
                            }
                            inputStream = inputStreamByteStream;
                        } catch (Exception e2) {
                            e = e2;
                            str2 = null;
                        }
                    }
                    inputStreamByteStream = inputStream;
                    str3 = strName;
                } catch (Exception e3) {
                    e = e3;
                    inputStreamByteStream = null;
                    str2 = null;
                }
                return new WebResourceResponse(str2, str3, inputStreamByteStream);
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Intent intent = zendeskDeepLinkingParser.parse(str, ViewArticleActivity.this);
                ViewArticleActivity viewArticleActivity = ViewArticleActivity.this;
                if (intent == null) {
                    return false;
                }
                try {
                    viewArticleActivity.startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException unused) {
                    Logger.w(ViewArticleActivity.LOG_TAG, "Could not open Intent: %s", intent);
                    return false;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), 0);
        View view = null;
        int measuredHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            view = adapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(iMakeMeasureSpec, -2));
            }
            view.measure(iMakeMeasureSpec, 0);
            measuredHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = measuredHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mAttachmentRequestCallback = SafeZendeskCallback.from(new AttachmentRequestCallback());
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mAttachmentRequestCallback.cancel();
        this.mAttachmentRequestCallback = null;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchAttachmentsForArticle(long j) {
        setLoadingState(LoadingState.LOADING);
        ZendeskConfig.INSTANCE.provider().helpCenterProvider().getAttachments(Long.valueOf(j), AttachmentType.BLOCK, this.mAttachmentRequestCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchArticle(long j) {
        setLoadingState(LoadingState.LOADING);
        ZendeskConfig.INSTANCE.provider().helpCenterProvider().getArticle(Long.valueOf(j), new ZendeskCallback<Article>() { // from class: com.zendesk.sdk.support.ViewArticleActivity.2
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(Article article) {
                ViewArticleActivity.this.mArticle = article;
                ViewArticleActivity.this.loadArticleBody();
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                ViewArticleActivity.this.setLoadingState(LoadingState.ERRORED);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadArticleBody() {
        ZendeskConfig.INSTANCE.getTracker().helpCenterArticleViewed();
        ZendeskConfig.INSTANCE.provider().helpCenterProvider().submitRecordArticleView(this.mArticle.getId(), LocaleUtil.forLanguageTag(this.mArticle.getLocale()), new ZendeskCallback<Void>() { // from class: com.zendesk.sdk.support.ViewArticleActivity.3
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(Void r1) {
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                Logger.e(ViewArticleActivity.LOG_TAG, String.format(Locale.US, "Error submitting Help Center reporting: [reason] %s [isNetworkError] %s [status] %d", errorResponse.getReason(), Boolean.valueOf(errorResponse.isNetworkError()), Integer.valueOf(errorResponse.getStatus())), new Object[0]);
            }
        });
        setLoadingState(LoadingState.DISPLAYING);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(this.mArticle.getTitle());
        }
        String name = this.mArticle.getAuthor() == null ? null : this.mArticle.getAuthor().getName();
        String str = this.mArticle.getCreatedAt() != null ? new SimpleDateFormat(ARTICLE_DATE_FORMAT, Locale.getDefault()).format(this.mArticle.getCreatedAt()) : null;
        this.mArticleContentWebView.loadDataWithBaseURL(ZendeskConfig.INSTANCE.getZendeskUrl(), String.format("<HTML><HEAD><LINK href='%s' type='text/css' rel='stylesheet'/></HEAD><body><h1>%s</h1>%s<footer><p>%s</p></footer></body></HTML>", CSS_FILE, this.mArticle.getTitle(), this.mArticle.getBody(), (str == null || name == null) ? "" : String.format(Locale.US, ARTICLE_DETAIL_FORMAT_STRING, name, getString(R.string.view_article_seperator), str)), TYPE_TEXT_HTML, "UTF-8", null);
        this.mHandler.postDelayed(new Runnable() { // from class: com.zendesk.sdk.support.ViewArticleActivity.4
            @Override // java.lang.Runnable
            public void run() {
                ViewArticleActivity viewArticleActivity = ViewArticleActivity.this;
                viewArticleActivity.fetchAttachmentsForArticle(viewArticleActivity.mArticle.getId().longValue());
            }
        }, FETCH_ATTACHMENTS_DELAY_MILLIS);
    }

    protected void setLoadingState(LoadingState loadingState) {
        if (loadingState == null) {
            Logger.w(LOG_TAG, "LoadingState was null, nothing to do", new Object[0]);
            return;
        }
        int i = AnonymousClass6.$SwitchMap$com$zendesk$sdk$ui$LoadingState[loadingState.ordinal()];
        if (i == 1) {
            UiUtils.setVisibility(this.mProgressView, 0);
            UiUtils.setVisibility(this.mAttachmentListView, 8);
            onRetryUnavailable();
        } else if (i == 2) {
            UiUtils.setVisibility(this.mProgressView, 8);
            UiUtils.setVisibility(this.mAttachmentListView, 0);
            onRetryUnavailable();
        } else {
            if (i != 3) {
                return;
            }
            UiUtils.setVisibility(this.mProgressView, 8);
            UiUtils.setVisibility(this.mAttachmentListView, 8);
            onRetryAvailable(getString(R.string.view_article_attachments_error), new View.OnClickListener() { // from class: com.zendesk.sdk.support.ViewArticleActivity.5
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ViewArticleActivity.this.mArticleId == null || ViewArticleActivity.this.mArticle != null) {
                        if (ViewArticleActivity.this.mArticle != null) {
                            ViewArticleActivity viewArticleActivity = ViewArticleActivity.this;
                            viewArticleActivity.fetchAttachmentsForArticle(viewArticleActivity.mArticle.getId().longValue());
                            return;
                        }
                        return;
                    }
                    ViewArticleActivity viewArticleActivity2 = ViewArticleActivity.this;
                    viewArticleActivity2.fetchArticle(viewArticleActivity2.mArticleId.longValue());
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.zendesk.sdk.support.ViewArticleActivity$6, reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$sdk$ui$LoadingState;

        static {
            int[] iArr = new int[LoadingState.values().length];
            $SwitchMap$com$zendesk$sdk$ui$LoadingState = iArr;
            try {
                iArr[LoadingState.LOADING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$ui$LoadingState[LoadingState.DISPLAYING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$ui$LoadingState[LoadingState.ERRORED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = adapterView.getItemAtPosition(i);
        if (itemAtPosition instanceof Attachment) {
            Attachment attachment = (Attachment) itemAtPosition;
            if (attachment.getContentUrl() != null) {
                Uri uri = Uri.parse(attachment.getContentUrl());
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(uri);
                startActivity(intent);
                return;
            }
            Logger.w(LOG_TAG, "Unable to launch viewer, unable to parse URI for attachment", new Object[0]);
        }
    }

    class AttachmentRequestCallback extends ZendeskCallback<List<Attachment>> {
        AttachmentRequestCallback() {
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onSuccess(List<Attachment> list) {
            ViewArticleActivity.this.mAttachmentListView.setAdapter((ListAdapter) new ArticleAttachmentAdapter(ViewArticleActivity.this, list));
            ViewArticleActivity.this.mAttachmentListView.setOnItemClickListener(ViewArticleActivity.this);
            ViewArticleActivity.setListViewHeightBasedOnChildren(ViewArticleActivity.this.mAttachmentListView);
            ViewArticleActivity.this.setLoadingState(LoadingState.DISPLAYING);
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onError(ErrorResponse errorResponse) {
            ViewArticleActivity.this.setLoadingState(LoadingState.ERRORED);
            Logger.e(ViewArticleActivity.LOG_TAG, errorResponse);
        }
    }

    private static class ArticleAttachmentAdapter extends ArrayAdapter<Attachment> {
        public ArticleAttachmentAdapter(Context context, List<Attachment> list) {
            super(context, R.layout.row_article_attachment, list);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ArticleAttachmentRow articleAttachmentRow;
            if (view instanceof ArticleAttachmentRow) {
                articleAttachmentRow = (ArticleAttachmentRow) view;
            } else {
                articleAttachmentRow = new ArticleAttachmentRow(getContext());
            }
            articleAttachmentRow.bind(getItem(i));
            return articleAttachmentRow.getView();
        }
    }

    private static class ArticleAttachmentRow extends RelativeLayout implements ListRowView<Attachment> {
        private final Context mContext;
        private TextView mFileName;
        private TextView mFileSize;

        @Override // com.zendesk.sdk.ui.ListRowView
        public View getView() {
            return this;
        }

        public ArticleAttachmentRow(Context context) {
            super(context);
            this.mContext = context;
            initialise();
        }

        private void initialise() {
            View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.row_article_attachment, this);
            if (viewInflate != null) {
                this.mFileName = (TextView) viewInflate.findViewById(R.id.article_attachment_row_filename_text);
                this.mFileSize = (TextView) viewInflate.findViewById(R.id.article_attachment_row_filesize_text);
            }
        }

        @Override // com.zendesk.sdk.ui.ListRowView
        public void bind(Attachment attachment) {
            this.mFileName.setText(attachment.getFileName());
            this.mFileSize.setText(FileUtils.humanReadableFileSize(attachment.getSize()));
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        WebView webView = this.mArticleContentWebView;
        if (webView != null) {
            webView.destroy();
        }
    }
}
