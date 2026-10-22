package com.zendesk.sdk.feedback.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.BaseZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.WrappedZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.NetworkAwareActionbarActivity;
import com.zendesk.sdk.ui.ToolbarSherlock;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;

/* JADX INFO: loaded from: classes2.dex */
public class ContactZendeskActivity extends NetworkAwareActionbarActivity {
    public static final String EXTRA_CONTACT_CONFIGURATION = "extra_contact_configuration";
    private static final String FRAGMENT_TAG = "ContactZendeskActivity";
    private static final String LOG_TAG = "ContactZendeskActivity";
    public static final String RESULT_ERROR_IS_NETWORK_ERROR = "extra_is_nw_error";
    public static final String RESULT_ERROR_REASON = "extra_error_reason";
    public static final String RESULT_ERROR_STATUS_CODE = "extra_status_code";
    private ContactZendeskFragment contactZendeskFragment;
    private final SubmissionListener mSubmissionListener = new SubmissionListener() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskActivity.1
        @Override // com.zendesk.sdk.network.SubmissionListener
        public void onSubmissionCancel() {
        }

        @Override // com.zendesk.sdk.network.SubmissionListener
        public void onSubmissionStarted() {
        }

        @Override // com.zendesk.sdk.network.SubmissionListener
        public void onSubmissionCompleted() {
            ContactZendeskActivity.this.setResult(-1, new Intent());
            ContactZendeskActivity.this.finish();
        }

        @Override // com.zendesk.sdk.network.SubmissionListener
        public void onSubmissionError(ErrorResponse errorResponse) {
            ContactZendeskActivity contactZendeskActivity = ContactZendeskActivity.this;
            contactZendeskActivity.setResult(0, contactZendeskActivity.getErrorIntent(errorResponse));
        }
    };

    public static void startActivity(Context context, ZendeskFeedbackConfiguration zendeskFeedbackConfiguration) {
        boolean z = false;
        if (context == null) {
            Logger.e(LOG_TAG, "Context is null, cannot start the context.", new Object[0]);
            return;
        }
        Intent intent = new Intent(context, (Class<?>) ContactZendeskActivity.class);
        if (zendeskFeedbackConfiguration != null && !(zendeskFeedbackConfiguration instanceof WrappedZendeskFeedbackConfiguration)) {
            z = true;
        }
        if (z) {
            zendeskFeedbackConfiguration = new WrappedZendeskFeedbackConfiguration(zendeskFeedbackConfiguration);
        }
        intent.putExtra(EXTRA_CONTACT_CONFIGURATION, zendeskFeedbackConfiguration);
        context.startActivity(intent);
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        ZendeskFeedbackConfiguration defaultContactConfiguration;
        super.onCreate(bundle);
        setContentView(R.layout.activity_contact_zendesk);
        ToolbarSherlock.installToolBar(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) {
            Logger.e(LOG_TAG, "This activity requires an AppCompat theme with an action bar, finishing activity...", new Object[0]);
            setResult(0, getErrorIntent(new ErrorResponseAdapter("This activity requires an AppCompat theme with an action bar, finishing activity...")));
            finish();
            return;
        }
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        if (ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity() == null) {
            Logger.e(LOG_TAG, "No identity is present. Did you call ZendeskConfig.INSTANCE.setIdentity()?, finishing activity...", new Object[0]);
            setResult(0, getErrorIntent(new ErrorResponseAdapter("No identity is present. Did you call ZendeskConfig.INSTANCE.setIdentity()?, finishing activity...")));
            finish();
            return;
        }
        if (getIntent().hasExtra(EXTRA_CONTACT_CONFIGURATION) && (getIntent().getSerializableExtra(EXTRA_CONTACT_CONFIGURATION) instanceof ZendeskFeedbackConfiguration)) {
            defaultContactConfiguration = (ZendeskFeedbackConfiguration) getIntent().getSerializableExtra(EXTRA_CONTACT_CONFIGURATION);
        } else {
            Logger.d(LOG_TAG, "Contact configuration was not provided. Will use default configuration...", new Object[0]);
            defaultContactConfiguration = new DefaultContactConfiguration();
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        String str = FRAGMENT_TAG;
        Fragment fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(str);
        if (fragmentFindFragmentByTag != null && (fragmentFindFragmentByTag instanceof ContactZendeskFragment)) {
            this.contactZendeskFragment = (ContactZendeskFragment) fragmentFindFragmentByTag;
        } else {
            this.contactZendeskFragment = ContactZendeskFragment.newInstance(defaultContactConfiguration);
            FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
            fragmentTransactionBeginTransaction.add(R.id.activity_contact_zendesk_root, this.contactZendeskFragment, str);
            fragmentTransactionBeginTransaction.commit();
        }
        this.contactZendeskFragment.setFeedbackListener(this.mSubmissionListener);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.contactZendeskFragment == null || !isFinishing()) {
            return;
        }
        Logger.d(LOG_TAG, "Deleting unused attachments", new Object[0]);
        this.contactZendeskFragment.deleteUnusedAttachmentsBeforeShutdown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent getErrorIntent(ErrorResponse errorResponse) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_ERROR_IS_NETWORK_ERROR, errorResponse.isNetworkError());
        intent.putExtra(RESULT_ERROR_REASON, errorResponse.getReason());
        intent.putExtra(RESULT_ERROR_STATUS_CODE, errorResponse.getStatus());
        return intent;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return true;
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, com.zendesk.sdk.network.NetworkAware
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        this.contactZendeskFragment.onNetworkAvailable();
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, com.zendesk.sdk.network.NetworkAware
    public void onNetworkUnavailable() {
        super.onNetworkUnavailable();
        this.contactZendeskFragment.onNetworkUnavailable();
    }

    class DefaultContactConfiguration extends BaseZendeskFeedbackConfiguration {
        DefaultContactConfiguration() {
        }

        @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
        public String getRequestSubject() {
            return ContactZendeskActivity.this.getString(R.string.contact_fragment_request_subject);
        }
    }
}
