package com.zendesk.sdk.requests;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.ui.NetworkAwareActionbarActivity;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
public class ViewRequestActivity extends NetworkAwareActionbarActivity {
    public static final String EXTRA_REQUEST_ID = "requestId";
    public static final String EXTRA_SUBJECT = "subject";
    private static final String FRAGMENT_TAG = "ViewRequestActivity";
    private static final String LOG_TAG = "ViewRequestActivity";
    private ViewRequestFragment mViewRequestFragment;

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_view_request);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("requestId")) {
            String string = extras.getString("requestId");
            String string2 = extras.getString("subject");
            if (StringUtils.hasLength(string)) {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                String str = FRAGMENT_TAG;
                Fragment fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(str);
                if (fragmentFindFragmentByTag != null && (fragmentFindFragmentByTag instanceof ViewRequestFragment)) {
                    this.mViewRequestFragment = (ViewRequestFragment) fragmentFindFragmentByTag;
                    return;
                }
                this.mViewRequestFragment = ViewRequestFragment.newInstance(string, string2);
                FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
                fragmentTransactionBeginTransaction.add(R.id.activity_request_view_root, this.mViewRequestFragment, str);
                fragmentTransactionBeginTransaction.commit();
                return;
            }
            abort();
            return;
        }
        abort();
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
        ViewRequestFragment viewRequestFragment = this.mViewRequestFragment;
        if (viewRequestFragment != null) {
            viewRequestFragment.onNetworkAvailable();
        }
    }

    @Override // com.zendesk.sdk.ui.NetworkAwareActionbarActivity, com.zendesk.sdk.network.NetworkAware
    public void onNetworkUnavailable() {
        super.onNetworkUnavailable();
        ViewRequestFragment viewRequestFragment = this.mViewRequestFragment;
        if (viewRequestFragment != null) {
            viewRequestFragment.onNetworkUnavailable();
        }
    }

    private void abort() {
        Logger.w(LOG_TAG, "Insufficient data to start activity", new Object[0]);
        finish();
    }
}
