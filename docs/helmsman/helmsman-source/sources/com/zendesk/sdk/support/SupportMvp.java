package com.zendesk.sdk.support;

import android.content.Context;
import android.os.Bundle;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.RetryAction;
import com.zendesk.service.ZendeskCallback;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface SupportMvp {

    public enum ErrorType {
        CATEGORY_LOAD,
        SECTION_LOAD,
        ARTICLES_LOAD
    }

    public interface Model {
        void getSettings(ZendeskCallback<SafeMobileSettings> zendeskCallback);

        void search(List<Long> list, List<Long> list2, String str, String[] strArr, ZendeskCallback<List<SearchArticle>> zendeskCallback);
    }

    public interface Presenter {
        void initWithBundle(Bundle bundle);

        void onErrorWithRetry(ErrorType errorType, RetryAction retryAction);

        void onLoad();

        void onPause();

        void onResume(View view);

        void onSearchSubmit(String str);

        boolean shouldShowConversationsMenuItem();

        boolean shouldShowSearchMenuItem();
    }

    public interface View {
        void clearSearchResults();

        void dismissError();

        void exitActivity();

        Context getContext();

        void hideLoadingState();

        boolean isShowingHelp();

        void showContactUsButton();

        void showContactZendesk();

        void showError(int i);

        void showErrorWithRetry(ErrorType errorType, RetryAction retryAction);

        void showHelp(SupportUiConfig supportUiConfig);

        void showLoadingState();

        void showRequestList();

        void showSearchResults(List<SearchArticle> list, String str);
    }
}
