package com.zendesk.sdk.rating.impl;

import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.FeedbackConnector;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.rating.ui.FeedbackDialog;
import com.zendesk.sdk.rating.ui.RateMyAppDialog;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppSendFeedbackButton extends BaseRateMyAppButton {
    public static final String FEEDBACK_DIALOG_TAG = "feedback";
    private final transient FeedbackConnector mConnector;
    private transient SubmissionListener mFeedbackListener;
    private final transient FragmentActivity mFragmentActivity;
    private final String mLabel;

    @Override // com.zendesk.sdk.rating.impl.BaseRateMyAppButton, com.zendesk.sdk.rating.RateMyAppButton
    public boolean shouldDismissDialog() {
        return false;
    }

    public RateMyAppSendFeedbackButton(FragmentActivity fragmentActivity, FeedbackConnector feedbackConnector) {
        this.mLabel = fragmentActivity.getString(R.string.rate_my_app_dialog_negative_action_label);
        this.mFragmentActivity = fragmentActivity;
        this.mConnector = feedbackConnector;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() { // from class: com.zendesk.sdk.rating.impl.RateMyAppSendFeedbackButton.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FragmentManager supportFragmentManager = RateMyAppSendFeedbackButton.this.mFragmentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransactionBeginTransaction = supportFragmentManager.beginTransaction();
                Fragment fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(RateMyAppDialog.RMA_DIALOG_TAG);
                if (fragmentFindFragmentByTag != null) {
                    fragmentTransactionBeginTransaction.remove(fragmentFindFragmentByTag);
                }
                fragmentTransactionBeginTransaction.commit();
                supportFragmentManager.popBackStackImmediate();
                FragmentTransaction fragmentTransactionBeginTransaction2 = supportFragmentManager.beginTransaction();
                Fragment fragmentFindFragmentByTag2 = supportFragmentManager.findFragmentByTag(RateMyAppSendFeedbackButton.FEEDBACK_DIALOG_TAG);
                if (fragmentFindFragmentByTag2 != null) {
                    fragmentTransactionBeginTransaction2.remove(fragmentFindFragmentByTag2);
                }
                fragmentTransactionBeginTransaction2.addToBackStack(null);
                FeedbackDialog feedbackDialogNewInstance = FeedbackDialog.newInstance(RateMyAppSendFeedbackButton.this.mConnector);
                feedbackDialogNewInstance.setFeedbackListener(RateMyAppSendFeedbackButton.this.mFeedbackListener);
                feedbackDialogNewInstance.show(fragmentTransactionBeginTransaction2, RateMyAppSendFeedbackButton.FEEDBACK_DIALOG_TAG);
            }
        };
    }

    @Override // com.zendesk.sdk.rating.RateMyAppButton
    public String getLabel() {
        return this.mLabel;
    }

    public void setFeedbackListener(SubmissionListener submissionListener) {
        this.mFeedbackListener = submissionListener;
    }

    @Override // com.zendesk.sdk.rating.impl.BaseRateMyAppButton, com.zendesk.sdk.rating.RateMyAppButton
    public int getId() {
        return R.id.rma_feedback_button;
    }
}
