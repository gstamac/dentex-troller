package com.zendesk.sdk.deeplinking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.helpcenter.SimpleArticle;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.support.ViewArticleActivity;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.HttpUrl;

/* JADX INFO: loaded from: classes2.dex */
public class ZendeskDeepLinkingParser {
    private static final String LOG_TAG = "ZendeskDeepLinkingParser";
    private final List<ZendeskDeepLinkParserModule> mParserModuleList;

    interface ZendeskDeepLinkParserModule {
        Intent parse(String str, Context context);
    }

    public ZendeskDeepLinkingParser() {
        ArrayList arrayList = new ArrayList();
        this.mParserModuleList = arrayList;
        arrayList.add(new HelpCenterParser());
        arrayList.add(new MailParser());
        arrayList.add(new HttpParser());
        arrayList.add(new DefaultParser());
    }

    public Intent parse(String str, Context context) {
        if (StringUtils.hasLength(str) && context != null) {
            Iterator<ZendeskDeepLinkParserModule> it = this.mParserModuleList.iterator();
            while (it.hasNext()) {
                Intent intent = it.next().parse(str, context);
                if (intent != null) {
                    return intent;
                }
            }
        }
        return null;
    }

    static class DefaultParser implements ZendeskDeepLinkParserModule {
        DefaultParser() {
        }

        @Override // com.zendesk.sdk.deeplinking.ZendeskDeepLinkingParser.ZendeskDeepLinkParserModule
        public Intent parse(String str, Context context) {
            if (!StringUtils.hasLength(str)) {
                return null;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            return intent;
        }
    }

    static class MailParser implements ZendeskDeepLinkParserModule {
        private static final String MAIL_PREFIX = "mailto:";

        MailParser() {
        }

        @Override // com.zendesk.sdk.deeplinking.ZendeskDeepLinkingParser.ZendeskDeepLinkParserModule
        public Intent parse(String str, Context context) {
            if (!StringUtils.hasLength(str) || !str.startsWith("mailto:")) {
                return null;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            return intent;
        }
    }

    static class HttpParser implements ZendeskDeepLinkParserModule {
        HttpParser() {
        }

        @Override // com.zendesk.sdk.deeplinking.ZendeskDeepLinkingParser.ZendeskDeepLinkParserModule
        public Intent parse(String str, Context context) {
            if (!StringUtils.hasLength(str) || HttpUrl.parse(str) == null) {
                return null;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            return intent;
        }
    }

    static class HelpCenterParser implements ZendeskDeepLinkParserModule {
        private static final String HC_PATH_ELEMENT_ARTICLE = "articles";
        private static final String HC_PATH_ELEMENT_HC = "hc";
        private static final String HC_PATH_ELEMENT_NAME_SEPARATOR = "-";

        HelpCenterParser() {
        }

        @Override // com.zendesk.sdk.deeplinking.ZendeskDeepLinkingParser.ZendeskDeepLinkParserModule
        public Intent parse(String str, Context context) {
            SimpleArticle simpleArticleExtractArticleId;
            String zendeskUrl = ZendeskConfig.INSTANCE.getZendeskUrl();
            if (!StringUtils.hasLength(zendeskUrl)) {
                Logger.w(ZendeskDeepLinkingParser.LOG_TAG, "Zendesk not initialised.", new Object[0]);
                return null;
            }
            HttpUrl httpUrl = HttpUrl.parse(zendeskUrl);
            HttpUrl httpUrl2 = HttpUrl.parse(str);
            if (httpUrl2 == null || httpUrl == null || !httpUrl.host().equals(httpUrl2.host())) {
                return null;
            }
            List<String> listPathSegments = httpUrl2.pathSegments();
            if (listPathSegments.size() < 4 && listPathSegments.size() > 5) {
                return null;
            }
            int iIndexOf = listPathSegments.indexOf(HC_PATH_ELEMENT_ARTICLE);
            if (HC_PATH_ELEMENT_HC.equals(listPathSegments.get(0)) && ((iIndexOf == 1 || iIndexOf == 2) && iIndexOf + 2 == listPathSegments.size() && (simpleArticleExtractArticleId = extractArticleId(listPathSegments.get(iIndexOf + 1))) != null)) {
                Intent intent = new Intent(context, (Class<?>) ViewArticleActivity.class);
                intent.putExtra(ViewArticleActivity.EXTRA_SIMPLE_ARTICLE, simpleArticleExtractArticleId);
                return intent;
            }
            return null;
        }

        private SimpleArticle extractArticleId(String str) {
            String strTrim;
            String[] strArrSplit = str.split(HC_PATH_ELEMENT_NAME_SEPARATOR);
            if (CollectionUtils.isNotEmpty(strArrSplit)) {
                try {
                    Long lValueOf = Long.valueOf(Long.parseLong(strArrSplit[0]));
                    StringBuilder sb = new StringBuilder(str.length());
                    if (strArrSplit.length > 1) {
                        int length = strArrSplit.length;
                        for (int i = 1; i < length; i++) {
                            sb.append(strArrSplit[i]);
                            sb.append(' ');
                        }
                        strTrim = sb.toString().trim();
                    } else {
                        strTrim = "";
                    }
                    return new SimpleArticle(lValueOf, strTrim);
                } catch (NumberFormatException unused) {
                }
            }
            return null;
        }
    }
}
