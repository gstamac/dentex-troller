package com.zendesk.sdk.rating.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.WrappedZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConnector;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.network.SubmissionListenerAdapter;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.rating.RateMyAppButton;
import com.zendesk.sdk.rating.RateMyAppRule;
import com.zendesk.sdk.rating.impl.IdentityRateMyAppRule;
import com.zendesk.sdk.rating.impl.MetricsRateMyAppRule;
import com.zendesk.sdk.rating.impl.NetworkRateMyAppRule;
import com.zendesk.sdk.rating.impl.RateMyAppDontAskAgainButton;
import com.zendesk.sdk.rating.impl.RateMyAppRules;
import com.zendesk.sdk.rating.impl.RateMyAppSendFeedbackButton;
import com.zendesk.sdk.rating.impl.RateMyAppStoreButton;
import com.zendesk.sdk.rating.impl.SettingsRateMyAppRule;
import com.zendesk.sdk.storage.RateMyAppStorage;
import com.zendesk.sdk.ui.ZendeskDialog;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppDialog extends ZendeskDialog implements Serializable {
    private static final String LOG_TAG = "RateMyAppDialog";
    public static final String RMA_DIALOG_TAG = "rma_dialog";
    private Handler mHandler;
    private OnShowListener mOnShowListener;
    private RateMyAppButtonContainer mRateMyAppButtonContainer;
    private List<RateMyAppButton> mRateMyAppButtonList;
    private RateMyAppRules mRateMyAppRules;
    private RateMyAppButtonContainer.RateMyAppSelectionListener mSelectionListener;
    private boolean mShowAlways;
    private boolean mShowPending;

    public interface OnShowListener extends Serializable {
        void onShow(DialogInterface dialogInterface);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ArrayList arrayList = new ArrayList();
        if (this.mRateMyAppButtonList != null) {
            arrayList = new ArrayList(this.mRateMyAppButtonList);
        } else {
            Logger.e(LOG_TAG, "buttons extra was not an instance of ArrayList<RateMyAppButton>, RateMyAppDialog will be dismissed.", new Object[0]);
        }
        RateMyAppButtonContainer rateMyAppButtonContainer = new RateMyAppButtonContainer(getActivity(), arrayList);
        this.mRateMyAppButtonContainer = rateMyAppButtonContainer;
        rateMyAppButtonContainer.setDismissableListener(new RateMyAppButtonContainer.DismissableListener() { // from class: com.zendesk.sdk.rating.ui.RateMyAppDialog.1
            @Override // com.zendesk.sdk.rating.ui.RateMyAppButtonContainer.DismissableListener
            public void dismissDialog() {
                RateMyAppDialog.this.dismiss();
            }
        });
        this.mRateMyAppButtonContainer.setRateMyAppSelectionListener(this.mSelectionListener);
        this.mRateMyAppButtonContainer.setId(R.id.rma_dialog_root);
        return this.mRateMyAppButtonContainer;
    }

    @Override // com.zendesk.sdk.ui.ZendeskDialog, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (ZendeskConfig.INSTANCE.isInitialized()) {
            return;
        }
        Logger.d(LOG_TAG, "ZendeskConfig has not been initialized, dismissing RateMyAppDialog.", new Object[0]);
        dismiss();
    }

    public void showAlways(FragmentActivity fragmentActivity) {
        this.mShowAlways = true;
        final WeakReference weakReference = new WeakReference(fragmentActivity);
        ZendeskConfig.INSTANCE.provider().uiSettingsHelper().loadSetting(new ZendeskCallback<SafeMobileSettings>() { // from class: com.zendesk.sdk.rating.ui.RateMyAppDialog.2
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(SafeMobileSettings safeMobileSettings) {
                FragmentActivity fragmentActivity2 = (FragmentActivity) weakReference.get();
                if (fragmentActivity2 != null) {
                    RateMyAppDialog.this.showInternal(fragmentActivity2);
                } else {
                    Logger.d(RateMyAppDialog.LOG_TAG, "Host Activity is gone. Cannot display the RateMyAppDialog.", new Object[0]);
                }
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                Logger.w(RateMyAppDialog.LOG_TAG, "Error showing RMA, unable to load settings | reason: %s", errorResponse.getReason());
            }
        });
    }

    public void show(FragmentActivity fragmentActivity) {
        show(fragmentActivity, false);
    }

    public void show(FragmentActivity fragmentActivity, final boolean z) {
        final WeakReference weakReference = new WeakReference(fragmentActivity);
        ZendeskConfig.INSTANCE.provider().uiSettingsHelper().loadSetting(new ZendeskCallback<SafeMobileSettings>() { // from class: com.zendesk.sdk.rating.ui.RateMyAppDialog.3
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(SafeMobileSettings safeMobileSettings) {
                FragmentActivity fragmentActivity2 = (FragmentActivity) weakReference.get();
                if (fragmentActivity2 != null) {
                    RateMyAppDialog.this.show(fragmentActivity2, z, safeMobileSettings);
                } else {
                    Logger.d(RateMyAppDialog.LOG_TAG, "Host Activity is gone. Cannot display the RateMyAppDialog.", new Object[0]);
                }
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                Logger.w(RateMyAppDialog.LOG_TAG, "Error showing RMA, unable to load settings | reason: %s", errorResponse.getReason());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void show(FragmentActivity fragmentActivity, boolean z, SafeMobileSettings safeMobileSettings) {
        String str = LOG_TAG;
        Logger.i(str, "show called, show immediate override is: " + z, new Object[0]);
        if (fragmentActivity == null) {
            Logger.w(str, "Cannot show dialog, activity is null", new Object[0]);
        } else {
            if (!safeMobileSettings.isRateMyAppEnabled()) {
                Logger.w(str, "Cannot show dialog, no rate my app settings have been retrieved from the server or rate my app is not enabled.", new Object[0]);
                return;
            }
            final WeakReference weakReference = new WeakReference(fragmentActivity);
            this.mHandler = new Handler(Looper.getMainLooper());
            this.mHandler.postDelayed(new Runnable() { // from class: com.zendesk.sdk.rating.ui.RateMyAppDialog.4
                @Override // java.lang.Runnable
                public void run() {
                    FragmentActivity fragmentActivity2 = (FragmentActivity) weakReference.get();
                    if (fragmentActivity2 == null || fragmentActivity2.isFinishing()) {
                        return;
                    }
                    RateMyAppDialog.this.showInternal(fragmentActivity2);
                }
            }, z ? 0L : safeMobileSettings.getRateMyAppDelay());
        }
    }

    void setRateMyAppOnShowListener(OnShowListener onShowListener) {
        this.mOnShowListener = onShowListener;
        RateMyAppButtonContainer rateMyAppButtonContainer = this.mRateMyAppButtonContainer;
        if (rateMyAppButtonContainer != null) {
            rateMyAppButtonContainer.setRateMyAppSelectionListener((RateMyAppButtonContainer.RateMyAppSelectionListener) onShowListener);
        }
    }

    void setRateMyAppOnSelectionListener(RateMyAppButtonContainer.RateMyAppSelectionListener rateMyAppSelectionListener) {
        this.mSelectionListener = rateMyAppSelectionListener;
    }

    void setRateMyAppButtonList(List<RateMyAppButton> list) {
        this.mRateMyAppButtonList = list;
    }

    void setRateMyAppRules(RateMyAppRules rateMyAppRules) {
        this.mRateMyAppRules = rateMyAppRules;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInternal(FragmentActivity fragmentActivity) {
        if (this.mShowPending) {
            Logger.i(LOG_TAG, "Can't show the dialog because another call to show is pending", new Object[0]);
            return;
        }
        new RateMyAppStorage(fragmentActivity).incrementNumberOfLaunches();
        if (!(this.mShowAlways || this.mRateMyAppRules.checkRules())) {
            Logger.i(LOG_TAG, "Can't show the dialog due to configuration", new Object[0]);
            return;
        }
        this.mShowPending = true;
        this.mShowAlways = false;
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        DialogFragment dialogFragment = (DialogFragment) supportFragmentManager.findFragmentByTag(RMA_DIALOG_TAG);
        FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
        if (dialogFragment != null) {
            if (dialogFragment.getDialog() != null) {
                dialogFragment.getDialog().dismiss();
            }
            fragmentTransactionBeginTransaction.remove(dialogFragment);
        }
        try {
            show(fragmentTransactionBeginTransaction, RMA_DIALOG_TAG);
            getFragmentManager().executePendingTransactions();
            OnShowListener onShowListener = this.mOnShowListener;
            if (onShowListener != null) {
                onShowListener.onShow(getDialog());
            }
        } catch (Exception e) {
            Logger.e(LOG_TAG, e.getMessage(), e, new Object[0]);
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        this.mShowPending = false;
        return super.onCreateDialog(bundle);
    }

    public void tearDownDialog(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            Logger.d(LOG_TAG, "Supplied FragmentManager was null, cannot continue...", new Object[0]);
            return;
        }
        FragmentTransaction fragmentTransactionBeginTransaction = fragmentManager.beginTransaction();
        Fragment fragmentFindFragmentByTag = fragmentManager.findFragmentByTag(RateMyAppSendFeedbackButton.FEEDBACK_DIALOG_TAG);
        if (fragmentFindFragmentByTag != null) {
            Logger.d(LOG_TAG, "feedback dialog found for removal", new Object[0]);
            fragmentTransactionBeginTransaction.remove(fragmentFindFragmentByTag);
        }
        fragmentTransactionBeginTransaction.commit();
        FragmentTransaction fragmentTransactionBeginTransaction2 = fragmentManager.beginTransaction();
        Fragment fragmentFindFragmentByTag2 = fragmentManager.findFragmentByTag(RMA_DIALOG_TAG);
        if (fragmentFindFragmentByTag2 != null) {
            Logger.d(LOG_TAG, "RateMyApp dialog found for removal", new Object[0]);
            fragmentTransactionBeginTransaction2.remove(fragmentFindFragmentByTag2);
        }
        fragmentTransactionBeginTransaction2.commit();
        fragmentManager.popBackStackImmediate();
        fragmentManager.executePendingTransactions();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public static class Builder {
        private final FragmentActivity mActivity;
        private final ArrayList<RateMyAppButton> mButtons = new ArrayList<>();
        private OnShowListener mOnShowListener;
        private RateMyAppRule[] mRules;
        private RateMyAppButtonContainer.RateMyAppSelectionListener mSelectionListener;

        public Builder(FragmentActivity fragmentActivity) {
            this.mActivity = fragmentActivity;
            if (fragmentActivity == null) {
                Logger.w(RateMyAppDialog.LOG_TAG, "Activity is null, many things will not work", new Object[0]);
            }
        }

        public Builder withButton(RateMyAppButton rateMyAppButton) {
            if (this.mActivity != null && rateMyAppButton != null) {
                this.mButtons.add(rateMyAppButton);
            }
            return this;
        }

        public Builder withAndroidStoreRatingButton() {
            if (this.mActivity != null) {
                this.mButtons.add(new RateMyAppStoreButton(this.mActivity));
            }
            return this;
        }

        public Builder withDontRemindMeAgainButton() {
            if (this.mActivity != null) {
                this.mButtons.add(new RateMyAppDontAskAgainButton(this.mActivity));
            }
            return this;
        }

        public Builder withSendFeedbackButton() {
            return withSendFeedbackButton(new DefaultContactConfiguration(this.mActivity), new SubmissionListenerAdapter());
        }

        public Builder withSendFeedbackButton(ZendeskFeedbackConfiguration zendeskFeedbackConfiguration) {
            return withSendFeedbackButton(zendeskFeedbackConfiguration, new SubmissionListenerAdapter());
        }

        public Builder withSendFeedbackButton(ZendeskFeedbackConfiguration zendeskFeedbackConfiguration, SubmissionListener submissionListener) {
            FragmentActivity fragmentActivity = this.mActivity;
            if (fragmentActivity != null) {
                ZendeskFeedbackConnector zendeskFeedbackConnectorDefaultConnector = ZendeskFeedbackConnector.defaultConnector(fragmentActivity, new WrappedZendeskFeedbackConfiguration(zendeskFeedbackConfiguration), ZendeskConfig.INSTANCE.getMobileSettings().getRateMyAppTags());
                if (zendeskFeedbackConnectorDefaultConnector.isValid()) {
                    RateMyAppSendFeedbackButton rateMyAppSendFeedbackButton = new RateMyAppSendFeedbackButton(this.mActivity, zendeskFeedbackConnectorDefaultConnector);
                    rateMyAppSendFeedbackButton.setFeedbackListener(submissionListener);
                    this.mButtons.add(rateMyAppSendFeedbackButton);
                }
            }
            return this;
        }

        public Builder withSelectionListener(RateMyAppButtonContainer.RateMyAppSelectionListener rateMyAppSelectionListener) {
            this.mSelectionListener = rateMyAppSelectionListener;
            return this;
        }

        public Builder withOnShowListener(OnShowListener onShowListener) {
            this.mOnShowListener = onShowListener;
            return this;
        }

        public Builder withRules(RateMyAppRule... rateMyAppRuleArr) {
            this.mRules = rateMyAppRuleArr;
            return this;
        }

        public RateMyAppDialog build() {
            RateMyAppDialog rateMyAppDialog = new RateMyAppDialog();
            rateMyAppDialog.setRateMyAppOnShowListener(this.mOnShowListener);
            rateMyAppDialog.setRateMyAppOnSelectionListener(this.mSelectionListener);
            rateMyAppDialog.setRateMyAppButtonList(this.mButtons);
            ensureSaneRules(rateMyAppDialog);
            rateMyAppDialog.mShowPending = false;
            rateMyAppDialog.setRetainInstance(true);
            return rateMyAppDialog;
        }

        private void ensureSaneRules(RateMyAppDialog rateMyAppDialog) {
            if (CollectionUtils.isNotEmpty(this.mRules)) {
                rateMyAppDialog.setRateMyAppRules(new RateMyAppRules(this.mRules));
            } else {
                rateMyAppDialog.setRateMyAppRules(new RateMyAppRules(new SettingsRateMyAppRule(this.mActivity), new MetricsRateMyAppRule(this.mActivity), new NetworkRateMyAppRule(this.mActivity), new IdentityRateMyAppRule(this.mActivity)));
            }
        }
    }
}
