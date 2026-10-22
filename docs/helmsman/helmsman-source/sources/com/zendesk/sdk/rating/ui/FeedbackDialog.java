package com.zendesk.sdk.rating.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.FeedbackConnector;
import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.storage.RateMyAppStorage;
import com.zendesk.sdk.ui.TextWatcherAdapter;
import com.zendesk.sdk.ui.ZendeskDialog;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class FeedbackDialog extends ZendeskDialog {
    private static final String LOG_TAG = "FeedbackDialog";
    private FeedbackConnector mFeedBackConnector;
    private SubmissionListener mFeedbackListener;

    public void setFeedbackListener(SubmissionListener submissionListener) {
        this.mFeedbackListener = submissionListener;
    }

    public static FeedbackDialog newInstance(FeedbackConnector feedbackConnector) {
        FeedbackDialog feedbackDialog = new FeedbackDialog();
        if (feedbackConnector == null) {
            Logger.e(LOG_TAG, "Supplied connector was null, falling back to a null safe connector but no feedback will be sent!", new Object[0]);
            feedbackConnector = new NullSafeFeedbackConnector();
        }
        feedbackDialog.setFeedBackConnector(feedbackConnector);
        return feedbackDialog;
    }

    @Override // com.zendesk.sdk.ui.ZendeskDialog, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_send_feedback, viewGroup, false);
        final View viewFindViewById = viewInflate.findViewById(R.id.rma_feedback_issue_cancel_button);
        final View viewFindViewById2 = viewInflate.findViewById(R.id.rma_feedback_issue_send_button);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.rma_feedback_issue_edittext);
        final ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.rma_feedback_issue_progress);
        final Context context = layoutInflater.getContext();
        final RateMyAppStorage rateMyAppStorage = new RateMyAppStorage(context);
        if (viewFindViewById != null) {
            Logger.d(LOG_TAG, "Inflating cancel button...", new Object[0]);
            viewFindViewById.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.rating.ui.FeedbackDialog.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Logger.d(FeedbackDialog.LOG_TAG, "Cancel button was clicked...", new Object[0]);
                    FeedbackDialog.this.dismiss();
                    if (FeedbackDialog.this.mFeedbackListener != null) {
                        Logger.d(FeedbackDialog.LOG_TAG, "Notifying feedback listener of submission cancellation", new Object[0]);
                        FeedbackDialog.this.mFeedbackListener.onSubmissionCancel();
                    }
                }
            });
        }
        final FeedbackConnector feedbackConnector = this.mFeedBackConnector;
        String savedFeedback = rateMyAppStorage.getSavedFeedback();
        if (StringUtils.hasLength(savedFeedback)) {
            Logger.d(LOG_TAG, "Saved feedback was found.", new Object[0]);
            if (editText != null) {
                editText.setText(savedFeedback);
            }
            if (viewFindViewById2 != null) {
                viewFindViewById2.setEnabled(true);
            }
        }
        if (viewFindViewById2 == null || editText == null) {
            Logger.w(LOG_TAG, "Could not setOnClickListener because views were null", new Object[0]);
        } else {
            viewFindViewById2.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.rating.ui.FeedbackDialog.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (FeedbackDialog.this.mFeedbackListener != null) {
                        FeedbackDialog.this.mFeedbackListener.onSubmissionStarted();
                    }
                    View view2 = viewFindViewById;
                    if (view2 != null) {
                        view2.setEnabled(false);
                    }
                    viewFindViewById2.setEnabled(false);
                    editText.setEnabled(false);
                    progressBar.setVisibility(0);
                    final String string = editText.getText().toString();
                    feedbackConnector.sendFeedback(string, new ArrayList(), new ZendeskCallback<CreateRequest>() { // from class: com.zendesk.sdk.rating.ui.FeedbackDialog.2.1
                        @Override // com.zendesk.service.ZendeskCallback
                        public void onSuccess(CreateRequest createRequest) {
                            Logger.d(FeedbackDialog.LOG_TAG, "Feedback was submitted successfully.", new Object[0]);
                            ZendeskConfig.INSTANCE.getTracker().rateMyAppFeedbackSent();
                            if (FeedbackDialog.this.mFeedbackListener != null) {
                                Logger.d(FeedbackDialog.LOG_TAG, "Notifying feedback listener of success", new Object[0]);
                                rateMyAppStorage.clearUserData();
                                FeedbackDialog.this.mFeedbackListener.onSubmissionCompleted();
                            }
                            rateMyAppStorage.setDontShowAgain();
                            if (FeedbackDialog.this.isVisible()) {
                                Toast.makeText(context, FeedbackDialog.this.getString(R.string.rate_my_app_dialog_feedback_send_success_toast), 0).show();
                                if (FeedbackDialog.this.isResumed()) {
                                    FeedbackDialog.this.dismiss();
                                }
                            }
                        }

                        @Override // com.zendesk.service.ZendeskCallback
                        public void onError(ErrorResponse errorResponse) {
                            Logger.e(FeedbackDialog.LOG_TAG, errorResponse);
                            rateMyAppStorage.setSavedFeedback(string);
                            if (viewFindViewById != null) {
                                viewFindViewById.setEnabled(true);
                            }
                            viewFindViewById2.setEnabled(true);
                            editText.setEnabled(true);
                            progressBar.setVisibility(8);
                            if (FeedbackDialog.this.isVisible()) {
                                if (errorResponse.isNetworkError()) {
                                    Toast.makeText(context, FeedbackDialog.this.getString(R.string.rate_my_app_dialog_feedback_send_error_no_connectivity_toast), 0).show();
                                } else {
                                    Toast.makeText(context, FeedbackDialog.this.getString(R.string.rate_my_app_dialog_feedback_send_error_toast), 0).show();
                                }
                            }
                        }
                    });
                }
            });
        }
        if (editText != null) {
            editText.addTextChangedListener(new TextWatcherAdapter() { // from class: com.zendesk.sdk.rating.ui.FeedbackDialog.3
                @Override // com.zendesk.sdk.ui.TextWatcherAdapter, android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    if (viewFindViewById2 == null) {
                        Logger.e(FeedbackDialog.LOG_TAG, "ignoring afterTextChanged() because sendButton is null", new Object[0]);
                    } else if (editable == null || editable.length() == 0) {
                        viewFindViewById2.setEnabled(false);
                    } else {
                        viewFindViewById2.setEnabled(true);
                    }
                }
            });
        }
        return viewInflate;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    void setFeedBackConnector(FeedbackConnector feedbackConnector) {
        this.mFeedBackConnector = feedbackConnector;
    }

    static class NullSafeFeedbackConnector implements FeedbackConnector {
        @Override // com.zendesk.sdk.feedback.FeedbackConnector
        public boolean isValid() {
            return false;
        }

        NullSafeFeedbackConnector() {
        }

        @Override // com.zendesk.sdk.feedback.FeedbackConnector
        public void sendFeedback(String str, List<String> list, ZendeskCallback<CreateRequest> zendeskCallback) {
            Logger.e(FeedbackDialog.LOG_TAG, "Connector is null, cannot send feedback!", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("Connector is null, cannot send feedback!"));
            }
        }
    }
}
