package com.zendesk.sdk.requests;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.WrappedZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.NetworkAwareActionbarActivity;
import com.zendesk.sdk.ui.ToolbarSherlock;

/* JADX INFO: loaded from: classes2.dex */
public class RequestActivity extends NetworkAwareActionbarActivity {
    public static final String FRAGMENT_TAG = "request_list_fragment_tag";
    private static final String LOG_TAG = "RequestActivity";
    private ZendeskFeedbackConfiguration configuration;
    private RequestListFragment mRequestListFragment;

    public static void startActivity(Context context, ZendeskFeedbackConfiguration zendeskFeedbackConfiguration) {
        boolean z = false;
        if (context == null) {
            Logger.e(LOG_TAG, "Context is null, cannot start the Activity.", new Object[0]);
            return;
        }
        Intent intent = new Intent(context, (Class<?>) RequestActivity.class);
        if (zendeskFeedbackConfiguration != null && !(zendeskFeedbackConfiguration instanceof WrappedZendeskFeedbackConfiguration)) {
            z = true;
        }
        if (z) {
            zendeskFeedbackConfiguration = new WrappedZendeskFeedbackConfiguration(zendeskFeedbackConfiguration);
        }
        intent.putExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION, zendeskFeedbackConfiguration);
        context.startActivity(intent);
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean z = false;
        if (!ZendeskConfig.INSTANCE.isAuthenticationAvailable()) {
            Logger.w(LOG_TAG, "Cannot show the conversations because no authentication has been set up in the SDK.", new Object[0]);
            finish();
        }
        setContentView(R.layout.activity_requests);
        ToolbarSherlock.installToolBar(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent().hasExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION) && (getIntent().getSerializableExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION) instanceof ZendeskFeedbackConfiguration)) {
            z = true;
        }
        if (z) {
            this.configuration = (ZendeskFeedbackConfiguration) getIntent().getSerializableExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION);
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragmentFindFragmentByTag != null && (fragmentFindFragmentByTag instanceof RequestListFragment)) {
            this.mRequestListFragment = (RequestListFragment) fragmentFindFragmentByTag;
            return;
        }
        this.mRequestListFragment = createFragment();
        FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
        fragmentTransactionBeginTransaction.add(R.id.activity_request_fragment, this.mRequestListFragment, FRAGMENT_TAG);
        fragmentTransactionBeginTransaction.commit();
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, com.zendesk.sdk.network.NetworkAware
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        if (this.mRequestListFragment != null) {
            Logger.d(LOG_TAG, "Dispatching onNetworkAvailable() to current fragment.", new Object[0]);
            this.mRequestListFragment.onNetworkAvailable();
        }
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, com.zendesk.sdk.network.NetworkAware
    public void onNetworkUnavailable() {
        super.onNetworkUnavailable();
        if (this.mRequestListFragment != null) {
            Logger.d(LOG_TAG, "Dispatching onNetworkUnavailable() to current fragment.", new Object[0]);
            this.mRequestListFragment.onNetworkUnavailable();
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_request_list_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.activity_request_list_add_icon) {
            ContactZendeskActivity.startActivity(this, this.configuration);
            return true;
        }
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private RequestListFragment createFragment() {
        RequestListFragment requestListFragment = new RequestListFragment();
        requestListFragment.setRetainInstance(true);
        return requestListFragment;
    }
}
