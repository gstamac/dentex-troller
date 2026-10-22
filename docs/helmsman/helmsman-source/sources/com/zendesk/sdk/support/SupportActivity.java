package com.zendesk.sdk.support;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.WrappedZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.model.helpcenter.SearchArticle;
import com.zendesk.sdk.network.RetryAction;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.requests.RequestActivity;
import com.zendesk.sdk.support.help.HelpSearchFragment;
import com.zendesk.sdk.support.help.SupportHelpFragment;
import com.zendesk.sdk.ui.ToolbarSherlock;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SupportActivity extends AppCompatActivity implements SupportMvp.View {
    static final String EXTRA_CATEGORIES_COLLAPSED = "extra_categories_collapsed";
    static final String EXTRA_CATEGORY_IDS = "extra_category_ids";
    static final String EXTRA_LABEL_NAMES = "extra_label_names";
    static final String EXTRA_SECTION_IDS = "extra_section_ids";
    static final String EXTRA_SHOW_CONTACT_US_BUTTON = "extra_show_contact_us_button";
    static final String EXTRA_SHOW_CONVERSATIONS_MENU_BUTTON = "extra_show_conversations_menu_button";
    private static final String LOG_TAG = "SupportActivity";
    static final String SAVED_STATE_FAB_VISIBLE = "fab_visibility";
    private FloatingActionButton contactUsButton;
    private MenuItem conversationsMenuItem;
    private Snackbar errorSnackbar;
    private ZendeskFeedbackConfiguration feedbackConfiguration;
    private View loadingView;
    private SupportMvp.Presenter presenter;
    private MenuItem searchViewMenuItem;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_support);
        ToolbarSherlock.installToolBar(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) {
            Logger.e(LOG_TAG, "This activity requires an AppCompat theme with an action bar, finishing activity...", new Object[0]);
            setResult(0, getErrorIntent(new ErrorResponseAdapter("This activity requires an AppCompat theme with an action bar, finishing activity...")));
            finish();
            return;
        }
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        this.feedbackConfiguration = getFeedbackConfiguration();
        this.loadingView = findViewById(R.id.loading_view);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.contact_us_button);
        this.contactUsButton = floatingActionButton;
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.support.SupportActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SupportActivity.this.showContactZendesk();
            }
        });
        this.presenter = new SupportPresenter(this, new SupportModel(), ZendeskConfig.INSTANCE.provider().networkInfoProvider());
        Bundle extras = getIntent().getExtras() != null ? getIntent().getExtras() : new Bundle();
        if (bundle != null) {
            extras.putAll(bundle);
        }
        this.presenter.initWithBundle(extras);
        addOnBackStackChangedListener();
    }

    private void addOnBackStackChangedListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { // from class: com.zendesk.sdk.support.SupportActivity.2
            @Override // androidx.fragment.app.FragmentManager.OnBackStackChangedListener
            public void onBackStackChanged() {
                if (SupportActivity.this.getCurrentFragment().isHidden()) {
                    SupportActivity.this.getSupportFragmentManager().beginTransaction().show(SupportActivity.this.getCurrentFragment()).commit();
                    if (SupportActivity.this.getCurrentFragment() instanceof SupportHelpFragment) {
                        ((SupportHelpFragment) SupportActivity.this.getCurrentFragment()).setPresenter(SupportActivity.this.presenter);
                    }
                }
            }
        });
    }

    private boolean noFragmentAdded() {
        return getCurrentFragment() == null;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.presenter.onResume(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.presenter.onPause();
    }

    private Intent getErrorIntent(ErrorResponse errorResponse) {
        Intent intent = new Intent();
        intent.putExtra(ContactZendeskActivity.RESULT_ERROR_IS_NETWORK_ERROR, false);
        intent.putExtra(ContactZendeskActivity.RESULT_ERROR_REASON, errorResponse.getReason());
        intent.putExtra(ContactZendeskActivity.RESULT_ERROR_STATUS_CODE, errorResponse.getStatus());
        return intent;
    }

    private ZendeskFeedbackConfiguration getFeedbackConfiguration() {
        if (getIntent().hasExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION) && (getIntent().getSerializableExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION) instanceof ZendeskFeedbackConfiguration)) {
            return (ZendeskFeedbackConfiguration) getIntent().getSerializableExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION);
        }
        return null;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_help_menu_conversations, menu);
        this.conversationsMenuItem = menu.findItem(R.id.fragment_help_menu_contact);
        MenuItem menuItemFindItem = menu.findItem(R.id.fragment_help_menu_search);
        this.searchViewMenuItem = menuItemFindItem;
        if (menuItemFindItem == null) {
            return true;
        }
        SearchView searchView = (SearchView) menuItemFindItem.getActionView();
        searchView.setImeOptions(searchView.getImeOptions() | 268435456);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.zendesk.sdk.support.SupportActivity.3
            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                return false;
            }

            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                if (SupportActivity.this.presenter == null) {
                    return false;
                }
                SupportActivity.this.presenter.onSearchSubmit(str);
                return true;
            }
        });
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem;
        MenuItem menuItem2;
        SupportMvp.Presenter presenter = this.presenter;
        if (presenter != null && (menuItem2 = this.searchViewMenuItem) != null) {
            menuItem2.setVisible(presenter.shouldShowSearchMenuItem());
        }
        SupportMvp.Presenter presenter2 = this.presenter;
        if (presenter2 != null && (menuItem = this.conversationsMenuItem) != null) {
            menuItem.setVisible(presenter2.shouldShowConversationsMenuItem());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
            return true;
        }
        if (itemId != R.id.fragment_help_menu_contact) {
            return false;
        }
        showRequestList();
        return true;
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showContactUsButton() {
        this.contactUsButton.setVisibility(0);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showHelp(SupportUiConfig supportUiConfig) {
        if (noFragmentAdded()) {
            SupportHelpFragment supportHelpFragmentNewInstance = SupportHelpFragment.newInstance(supportUiConfig);
            supportHelpFragmentNewInstance.setPresenter(this.presenter);
            addFragment(supportHelpFragmentNewInstance);
        } else if (getCurrentFragment() instanceof SupportHelpFragment) {
            ((SupportHelpFragment) getCurrentFragment()).setPresenter(this.presenter);
        }
        invalidateOptionsMenu();
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).commit();
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showLoadingState() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null && currentFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(getCurrentFragment()).commit();
        }
        this.loadingView.setVisibility(0);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void hideLoadingState() {
        this.loadingView.setVisibility(8);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showSearchResults(List<SearchArticle> list, String str) {
        getSearchFragment().updateResults(list, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private HelpSearchFragment getSearchFragment() {
        if (getCurrentFragment() instanceof HelpSearchFragment) {
            Logger.d(LOG_TAG, "showSearchResults: current fragment is a HelpSearchFragment", new Object[0]);
            return (HelpSearchFragment) getCurrentFragment();
        }
        HelpSearchFragment helpSearchFragmentNewInstance = HelpSearchFragment.newInstance(this.contactUsButton.getVisibility() == 0);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, helpSearchFragmentNewInstance).addToBackStack(null).commit();
        return helpSearchFragmentNewInstance;
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void clearSearchResults() {
        if (getCurrentFragment() instanceof HelpSearchFragment) {
            ((HelpSearchFragment) getCurrentFragment()).clearResults();
        }
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public boolean isShowingHelp() {
        return getCurrentFragment() instanceof SupportHelpFragment;
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showRequestList() {
        RequestActivity.startActivity(this, this.feedbackConfiguration);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showContactZendesk() {
        ContactZendeskActivity.startActivity(this, this.feedbackConfiguration);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showErrorWithRetry(SupportMvp.ErrorType errorType, final RetryAction retryAction) {
        String string;
        if (errorType == null) {
            Logger.w(LOG_TAG, "ErrorType was null, falling back to 'retry' as label", new Object[0]);
            string = getString(R.string.retry_view_button_label);
        } else {
            int i = AnonymousClass5.$SwitchMap$com$zendesk$sdk$support$SupportMvp$ErrorType[errorType.ordinal()];
            if (i == 1) {
                string = getString(R.string.categories_list_fragment_error_message);
            } else if (i == 2) {
                string = getString(R.string.sections_list_fragment_error_message);
            } else if (i == 3) {
                string = getString(R.string.articles_list_fragment_error_message);
            } else {
                Logger.w(LOG_TAG, "Unknown or unhandled error type, falling back to error type name as label", new Object[0]);
                string = getString(R.string.help_search_no_results_label) + " " + errorType.name();
            }
        }
        Snackbar snackbarMake = Snackbar.make(this.contactUsButton, string, -2);
        this.errorSnackbar = snackbarMake;
        snackbarMake.setAction(R.string.retry_view_button_label, new View.OnClickListener() { // from class: com.zendesk.sdk.support.SupportActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SupportActivity.this.errorSnackbar.dismiss();
                retryAction.onRetry();
            }
        });
        this.errorSnackbar.show();
    }

    /* JADX INFO: renamed from: com.zendesk.sdk.support.SupportActivity$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$sdk$support$SupportMvp$ErrorType;

        static {
            int[] iArr = new int[SupportMvp.ErrorType.values().length];
            $SwitchMap$com$zendesk$sdk$support$SupportMvp$ErrorType = iArr;
            try {
                iArr[SupportMvp.ErrorType.CATEGORY_LOAD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$support$SupportMvp$ErrorType[SupportMvp.ErrorType.SECTION_LOAD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$support$SupportMvp$ErrorType[SupportMvp.ErrorType.ARTICLES_LOAD.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void showError(int i) {
        Snackbar snackbarMake = Snackbar.make(this.contactUsButton, i, -2);
        this.errorSnackbar = snackbarMake;
        snackbarMake.show();
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void dismissError() {
        Snackbar snackbar = this.errorSnackbar;
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public Context getContext() {
        return getApplicationContext();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(SAVED_STATE_FAB_VISIBLE, this.contactUsButton.getVisibility() == 0);
    }

    @Override // com.zendesk.sdk.support.SupportMvp.View
    public void exitActivity() {
        finish();
    }

    public static class Builder {
        private final Bundle args = new Bundle();

        public Builder withArticlesForCategoryIds(long... jArr) {
            if (this.args.getLongArray(SupportActivity.EXTRA_SECTION_IDS) != null) {
                Logger.w(SupportActivity.LOG_TAG, "Builder: sections have already been specified. Removing section IDs to set category IDs.", new Object[0]);
                this.args.remove(SupportActivity.EXTRA_SECTION_IDS);
            }
            this.args.putLongArray(SupportActivity.EXTRA_CATEGORY_IDS, jArr);
            return this;
        }

        public Builder withArticlesForSectionIds(long... jArr) {
            if (this.args.getLongArray(SupportActivity.EXTRA_CATEGORY_IDS) != null) {
                Logger.w(SupportActivity.LOG_TAG, "Builder: categories have already been specified. Removing category IDs to set section IDs.", new Object[0]);
                this.args.remove(SupportActivity.EXTRA_CATEGORY_IDS);
            }
            this.args.putLongArray(SupportActivity.EXTRA_SECTION_IDS, jArr);
            return this;
        }

        public Builder showContactUsButton(boolean z) {
            this.args.putBoolean(SupportActivity.EXTRA_SHOW_CONTACT_US_BUTTON, z);
            return this;
        }

        public Builder withContactConfiguration(ZendeskFeedbackConfiguration zendeskFeedbackConfiguration) {
            if (zendeskFeedbackConfiguration != null) {
                zendeskFeedbackConfiguration = new WrappedZendeskFeedbackConfiguration(zendeskFeedbackConfiguration);
            }
            this.args.putSerializable(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION, zendeskFeedbackConfiguration);
            return this;
        }

        public Builder withLabelNames(String... strArr) {
            if (CollectionUtils.isNotEmpty(strArr)) {
                this.args.putStringArray(SupportActivity.EXTRA_LABEL_NAMES, strArr);
            }
            return this;
        }

        public Builder withCategoriesCollapsed(boolean z) {
            this.args.putBoolean(SupportActivity.EXTRA_CATEGORIES_COLLAPSED, z);
            return this;
        }

        public Builder showConversationsMenuButton(boolean z) {
            this.args.putBoolean(SupportActivity.EXTRA_SHOW_CONVERSATIONS_MENU_BUTTON, z);
            return this;
        }

        public void show(Context context) {
            Logger.d(SupportActivity.LOG_TAG, "show: showing SupportActivity", new Object[0]);
            context.startActivity(intent(context));
        }

        Intent intent(Context context) {
            Logger.d(SupportActivity.LOG_TAG, "intent: creating Intent", new Object[0]);
            Intent intent = new Intent(context, (Class<?>) SupportActivity.class);
            intent.putExtras(this.args);
            return intent;
        }
    }
}
