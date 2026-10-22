package com.zendesk.sdk.feedback.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.view.KeyEventDispatcher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zendesk.belvedere.BelvedereCallback;
import com.zendesk.belvedere.BelvedereResult;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.attachment.AttachmentHelper;
import com.zendesk.sdk.attachment.ImageUploadHelper;
import com.zendesk.sdk.attachment.ZendeskBelvedereProvider;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConnector;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.sdk.model.request.UploadResponse;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.Retryable;
import com.zendesk.sdk.network.SettingsHelper;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.EmailAddressAutoCompleteTextView;
import com.zendesk.sdk.ui.TextWatcherAdapter;
import com.zendesk.sdk.util.NetworkUtils;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.SafeZendeskCallback;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ContactZendeskFragment extends Fragment {
    public static final String EXTRA_CONFIGURATION = "EXTRA_CONFIGURATION";
    public static final String EXTRA_CONFIGURATION_ADDITIONAL_INFO = "EXTRA_CONFIGURATION_ADDITIONAL";
    public static final String EXTRA_CONFIGURATION_REQUEST_SUBJECT = "EXTRA_CONFIGURATION_SUBJECT";
    public static final String EXTRA_CONFIGURATION_TAGS = "EXTRA_CONFIGURATION_TAGS";
    private static final int FOURTY_PERCENT_OPACITY = 102;
    private static final int FULL_OPACTITY = 255;
    private static final String LOG_TAG = "ContactZendeskFragment";
    private AttachmentContainerHost attachmentContainerHost;
    private AttachmentContainerHost.AttachmentContainerListener attachmentContainerListener;
    private MenuItem attachmentItem;
    private ContactZendeskFeedbackConfiguration contactZendeskFeedbackConfiguration;
    private ViewGroup container;
    private EditText descriptionEditText;
    private MenuItem doneMenuItem;
    private EmailAddressAutoCompleteTextView emailEditText;
    private SubmissionListener feedbackListener;
    private ImageUploadHelper.ImageUploadProgressListener imageUploadProgressListener;
    private ImageUploadHelper imageUploadService;
    private BelvedereCallback<List<BelvedereResult>> imagesExtracted;
    private boolean isEmailFieldVisible;
    private SafeMobileSettings mobileSettings;
    private ProgressBar progressBar;
    private SafeZendeskCallback<CreateRequest> requestCallback;
    private Retryable retryable;
    private SafeZendeskCallback<SafeMobileSettings> settingsCallback;
    private SettingsHelper settingsHelper;

    public static ContactZendeskFragment newInstance(ZendeskFeedbackConfiguration zendeskFeedbackConfiguration) {
        Bundle bundle = new Bundle();
        if (zendeskFeedbackConfiguration == null) {
            Logger.e(LOG_TAG, "Cannot instantiate a ContactZendeskFragment with no configuration", new Object[0]);
            return null;
        }
        bundle.putBundle(EXTRA_CONFIGURATION, configurationToBundle(zendeskFeedbackConfiguration.getTags(), zendeskFeedbackConfiguration.getRequestSubject(), zendeskFeedbackConfiguration.getAdditionalInfo()));
        ContactZendeskFragment contactZendeskFragment = new ContactZendeskFragment();
        contactZendeskFragment.setArguments(bundle);
        contactZendeskFragment.setRetainInstance(true);
        return contactZendeskFragment;
    }

    private static Bundle configurationToBundle(List<String> list, String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CONFIGURATION_REQUEST_SUBJECT, str);
        bundle.putString(EXTRA_CONFIGURATION_ADDITIONAL_INFO, str2);
        bundle.putStringArrayList(EXTRA_CONFIGURATION_TAGS, list == null ? null : new ArrayList<>(list));
        return bundle;
    }

    private static ZendeskFeedbackConfiguration bundleToZendeskFeedbackConfiguration(final Bundle bundle) {
        return new ZendeskFeedbackConfiguration() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.1
            @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
            public List<String> getTags() {
                return bundle.getStringArrayList(ContactZendeskFragment.EXTRA_CONFIGURATION_TAGS);
            }

            @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
            public String getAdditionalInfo() {
                return bundle.getString(ContactZendeskFragment.EXTRA_CONFIGURATION_ADDITIONAL_INFO);
            }

            @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
            public String getRequestSubject() {
                return bundle.getString(ContactZendeskFragment.EXTRA_CONFIGURATION_REQUEST_SUBJECT);
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Retryable) {
            this.retryable = (Retryable) context;
        }
        setUpCallbacks();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        this.settingsHelper = ZendeskConfig.INSTANCE.provider().uiSettingsHelper();
        if (getArguments() != null && getArguments().containsKey(EXTRA_CONFIGURATION)) {
            this.contactZendeskFeedbackConfiguration = new ContactZendeskFeedbackConfiguration(bundleToZendeskFeedbackConfiguration(getArguments().getBundle(EXTRA_CONFIGURATION)));
        } else {
            Logger.w(LOG_TAG, "No configuration passed to the fragment, this will result in no feedback being sent", new Object[0]);
        }
        ImageUploadHelper imageUploadHelper = this.imageUploadService;
        if (imageUploadHelper == null) {
            this.imageUploadService = new ImageUploadHelper(this.imageUploadProgressListener, ZendeskConfig.INSTANCE.provider().uploadProvider());
        } else {
            imageUploadHelper.setImageUploadProgressListener(this.imageUploadProgressListener);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity() != null) {
            View viewInflate = layoutInflater.inflate(R.layout.fragment_contact_zendesk, viewGroup, false);
            AttachmentContainerHost attachmentContainerHost = (AttachmentContainerHost) viewInflate.findViewById(R.id.contact_fragment_attachments);
            this.attachmentContainerHost = attachmentContainerHost;
            attachmentContainerHost.setState(this.imageUploadService);
            this.attachmentContainerHost.setAttachmentContainerListener(this.attachmentContainerListener);
            EmailAddressAutoCompleteTextView emailAddressAutoCompleteTextView = (EmailAddressAutoCompleteTextView) viewInflate.findViewById(R.id.contact_fragment_email);
            this.emailEditText = emailAddressAutoCompleteTextView;
            emailAddressAutoCompleteTextView.addTextChangedListener(new TextWatcherAdapter() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.2
                @Override // com.zendesk.sdk.ui.TextWatcherAdapter, android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    ContactZendeskFragment.this.checkSendButtonState();
                }
            });
            EditText editText = (EditText) viewInflate.findViewById(R.id.contact_fragment_description);
            this.descriptionEditText = editText;
            editText.addTextChangedListener(new TextWatcherAdapter() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.3
                @Override // com.zendesk.sdk.ui.TextWatcherAdapter, android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    ContactZendeskFragment.this.checkSendButtonState();
                }
            });
            this.progressBar = (ProgressBar) viewInflate.findViewById(R.id.contact_fragment_progress);
            this.container = (ViewGroup) viewInflate.findViewById(R.id.contact_fragment_container);
            preloadSettingsAndInit();
            return viewInflate;
        }
        TextView textView = new TextView(layoutInflater.getContext());
        textView.setText("Error, please check that an identity or email address has been set");
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preloadSettingsAndInit() {
        this.progressBar.setVisibility(0);
        this.settingsHelper.loadSetting(this.settingsCallback);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fragment_contact_zendesk_menu, menu);
        this.doneMenuItem = menu.findItem(R.id.fragment_contact_zendesk_menu_done);
        this.attachmentItem = menu.findItem(R.id.fragment_contact_zendesk_attachment);
        if (this.mobileSettings == null || !NetworkUtils.isConnected(getActivity())) {
            disableSendButton();
            disableAttachmentButton();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        checkSendButtonState();
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.fragment_contact_zendesk_menu_done) {
            sendFeedback();
            return true;
        }
        if (menuItem.getItemId() != R.id.fragment_contact_zendesk_attachment) {
            return false;
        }
        ZendeskBelvedereProvider.INSTANCE.getBelvedere(getContext()).showDialog(getChildFragmentManager());
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.retryable = null;
        tearDownCallbacks();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        disableSendButton();
        ZendeskBelvedereProvider.INSTANCE.getBelvedere(getContext()).getFilesFromActivityOnResult(i, i2, intent, this.imagesExtracted);
    }

    public void deleteUnusedAttachmentsBeforeShutdown() {
        ImageUploadHelper imageUploadHelper = this.imageUploadService;
        if (imageUploadHelper != null) {
            imageUploadHelper.deleteAllAttachmentsBeforeShutdown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSendButtonState() {
        EditText editText = this.descriptionEditText;
        boolean z = false;
        boolean z2 = true;
        boolean z3 = editText != null && StringUtils.hasLength(editText.getText().toString());
        boolean zIsImageUploadCompleted = this.imageUploadService.isImageUploadCompleted();
        if (this.isEmailFieldVisible) {
            EmailAddressAutoCompleteTextView emailAddressAutoCompleteTextView = this.emailEditText;
            if (emailAddressAutoCompleteTextView != null && emailAddressAutoCompleteTextView.isInputValid()) {
                z = true;
            }
            z2 = z;
        }
        if (z3 && zIsImageUploadCompleted && z2) {
            enableSendButton();
        } else {
            disableSendButton();
        }
    }

    private void disableAttachmentButton() {
        MenuItem menuItem = this.attachmentItem;
        if (menuItem != null) {
            menuItem.setEnabled(false);
            this.attachmentItem.getIcon().setAlpha(102);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayAttachmentButton() {
        if (!NetworkUtils.isConnected(getActivity())) {
            Logger.d(LOG_TAG, "Ignoring displayAttachmentButton() because there is no network connection", new Object[0]);
            return;
        }
        SafeMobileSettings mobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
        if (mobileSettings == null) {
            return;
        }
        if (!AttachmentHelper.isAttachmentSupportEnabled(mobileSettings)) {
            Logger.d(LOG_TAG, "Ignoring displayAttachmentButton() because attachment support is disabled", new Object[0]);
            return;
        }
        if (!ZendeskBelvedereProvider.INSTANCE.getBelvedere(getContext()).oneOrMoreSourceAvailable()) {
            Logger.d(LOG_TAG, "Ignoring displayAttachmentButton() because we don't have permissions for the camera or gallery", new Object[0]);
            return;
        }
        MenuItem menuItem = this.attachmentItem;
        if (menuItem != null) {
            menuItem.setEnabled(true);
            this.attachmentItem.setVisible(true);
            this.attachmentItem.getIcon().setAlpha(255);
        }
    }

    public void setFeedbackListener(SubmissionListener submissionListener) {
        this.feedbackListener = submissionListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendFeedback() {
        SubmissionListener submissionListener = this.feedbackListener;
        if (submissionListener != null) {
            submissionListener.onSubmissionStarted();
        }
        if (this.isEmailFieldVisible) {
            updateIdentity();
        }
        if (this.contactZendeskFeedbackConfiguration == null) {
            Logger.e(LOG_TAG, "Configuration is null, cannot send feedback...", new Object[0]);
            SubmissionListener submissionListener2 = this.feedbackListener;
            if (submissionListener2 != null) {
                submissionListener2.onSubmissionError(new ErrorResponseAdapter("Configuration is null, cannot send feedback..."));
                return;
            }
            return;
        }
        this.progressBar.setVisibility(0);
        this.descriptionEditText.setEnabled(false);
        this.emailEditText.setEnabled(false);
        disableSendButton();
        this.attachmentContainerHost.setAttachmentsDeletable(false);
        ZendeskFeedbackConnector.defaultConnector(getActivity(), this.contactZendeskFeedbackConfiguration, ZendeskConfig.INSTANCE.getMobileSettings().getContactZendeskTags()).sendFeedback(this.descriptionEditText.getText().toString(), this.imageUploadService.getUploadTokens(), this.requestCallback);
    }

    private void updateIdentity() {
        String str = LOG_TAG;
        Logger.d(str, "Updating existing anonymous identity with an email address", new Object[0]);
        Identity identity = ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity();
        String string = this.emailEditText.getText().toString();
        if (identity == null || !(identity instanceof AnonymousIdentity)) {
            Logger.d(str, "No valid identity found ", new Object[0]);
            return;
        }
        AnonymousIdentity anonymousIdentity = (AnonymousIdentity) identity;
        AnonymousIdentity.Builder builder = new AnonymousIdentity.Builder();
        builder.withEmailIdentifier(string);
        if (StringUtils.hasLength(anonymousIdentity.getName())) {
            builder.withNameIdentifier(anonymousIdentity.getName());
        }
        if (StringUtils.hasLength(anonymousIdentity.getExternalId())) {
            builder.withExternalIdentifier(anonymousIdentity.getExternalId());
        }
        ZendeskConfig.INSTANCE.setIdentity(builder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableSendButton() {
        MenuItem menuItem = this.doneMenuItem;
        if (menuItem != null) {
            menuItem.setEnabled(false);
            this.doneMenuItem.getIcon().setAlpha(102);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableSendButton() {
        if (!NetworkUtils.isConnected(getActivity())) {
            Logger.d(LOG_TAG, "Ignoring enableSendButton() because there is no network connection", new Object[0]);
            return;
        }
        MenuItem menuItem = this.doneMenuItem;
        if (menuItem != null) {
            menuItem.setEnabled(true);
            this.doneMenuItem.getIcon().setAlpha(255);
        }
    }

    void networkNotAvailable() {
        FragmentActivity activity = getActivity();
        if (activity == null || !(activity instanceof ContactZendeskActivity)) {
            return;
        }
        ((ContactZendeskActivity) activity).onNetworkUnavailable();
    }

    void onNetworkAvailable() {
        checkSendButtonState();
        displayAttachmentButton();
        if (this.mobileSettings == null) {
            preloadSettingsAndInit();
        }
    }

    void onNetworkUnavailable() {
        disableAttachmentButton();
        disableSendButton();
    }

    private void tearDownCallbacks() {
        this.requestCallback.cancel();
        this.requestCallback = null;
        this.imagesExtracted.cancel();
        this.imagesExtracted = null;
        AttachmentContainerHost attachmentContainerHost = this.attachmentContainerHost;
        if (attachmentContainerHost != null) {
            attachmentContainerHost.setAttachmentContainerListener(null);
        }
        this.settingsCallback.cancel();
        this.settingsCallback = null;
    }

    private void setUpCallbacks() {
        this.requestCallback = SafeZendeskCallback.from(new RequestCallback());
        this.imagesExtracted = new BelvedereCallback<List<BelvedereResult>>() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.4
            @Override // com.zendesk.belvedere.BelvedereCallback
            public void success(List<BelvedereResult> list) {
                SafeMobileSettings mobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
                if (mobileSettings != null) {
                    AttachmentHelper.processAndUploadSelectedFiles(list, ContactZendeskFragment.this.imageUploadService, ContactZendeskFragment.this.getActivity(), ContactZendeskFragment.this.attachmentContainerHost, mobileSettings);
                }
                ContactZendeskFragment.this.checkSendButtonState();
            }
        };
        this.attachmentContainerListener = new AttachmentContainerHost.AttachmentContainerListener() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.5
            @Override // com.zendesk.sdk.feedback.ui.AttachmentContainerHost.AttachmentContainerListener
            public void attachmentRemoved(File file) {
                ContactZendeskFragment.this.imageUploadService.removeImage(file);
            }
        };
        this.imageUploadProgressListener = new ImageUploadHelper.ImageUploadProgressListener() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.6
            @Override // com.zendesk.sdk.attachment.ImageUploadHelper.ImageUploadProgressListener
            public void allImagesUploaded(Map<File, UploadResponse> map) {
                ContactZendeskFragment.this.checkSendButtonState();
            }

            @Override // com.zendesk.sdk.attachment.ImageUploadHelper.ImageUploadProgressListener
            public void imageUploaded(UploadResponse uploadResponse, BelvedereResult belvedereResult) {
                ContactZendeskFragment.this.attachmentContainerHost.setAttachmentUploaded(belvedereResult.getFile());
            }

            @Override // com.zendesk.sdk.attachment.ImageUploadHelper.ImageUploadProgressListener
            public void imageUploadError(ErrorResponse errorResponse, BelvedereResult belvedereResult) {
                Logger.e(ContactZendeskFragment.LOG_TAG, String.format(Locale.US, "Image upload error; Reason: %s, Status: %s, isNetworkError: %s", errorResponse.getReason(), Integer.valueOf(errorResponse.getStatus()), Boolean.valueOf(errorResponse.isNetworkError())), new Object[0]);
                AttachmentHelper.showAttachmentTryAgainDialog(ContactZendeskFragment.this.getActivity(), belvedereResult, errorResponse, ContactZendeskFragment.this.imageUploadService, ContactZendeskFragment.this.attachmentContainerHost);
            }
        };
        this.settingsCallback = SafeZendeskCallback.from(new ZendeskCallback<SafeMobileSettings>() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.7
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(SafeMobileSettings safeMobileSettings) {
                ContactZendeskFragment.this.mobileSettings = safeMobileSettings;
                ContactZendeskFragment contactZendeskFragment = ContactZendeskFragment.this;
                contactZendeskFragment.isEmailFieldVisible = contactZendeskFragment.isEmailFieldEnabled(safeMobileSettings);
                ContactZendeskFragment.this.emailEditText.setVisibility(ContactZendeskFragment.this.isEmailFieldVisible ? 0 : 8);
                ContactZendeskFragment.this.progressBar.setVisibility(8);
                ContactZendeskFragment.this.container.setVisibility(0);
                if (ContactZendeskFragment.this.doneMenuItem != null && ContactZendeskFragment.this.attachmentItem != null) {
                    ContactZendeskFragment.this.disableSendButton();
                    ContactZendeskFragment.this.displayAttachmentButton();
                }
                if (NetworkUtils.isConnected(ContactZendeskFragment.this.getContext())) {
                    return;
                }
                ContactZendeskFragment.this.networkNotAvailable();
                Logger.d(ContactZendeskFragment.LOG_TAG, "Preload settings: Network not available.", new Object[0]);
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                ContactZendeskFragment.this.progressBar.setVisibility(8);
                if (NetworkUtils.isConnected(ContactZendeskFragment.this.getContext())) {
                    if (ContactZendeskFragment.this.retryable != null) {
                        ContactZendeskFragment.this.retryable.onRetryAvailable(ContactZendeskFragment.this.getString(R.string.rate_my_app_dialog_feedback_send_error_toast), new View.OnClickListener() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.7.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view) {
                                ContactZendeskFragment.this.preloadSettingsAndInit();
                                KeyEventDispatcher.Component activity = ContactZendeskFragment.this.getActivity();
                                if (activity instanceof Retryable) {
                                    ((Retryable) activity).onRetryUnavailable();
                                }
                            }
                        });
                    }
                } else {
                    ContactZendeskFragment.this.networkNotAvailable();
                    Logger.d(ContactZendeskFragment.LOG_TAG, "Preload settings: Network not available.", new Object[0]);
                }
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.imageUploadService.setImageUploadProgressListener(null);
    }

    boolean isEmailFieldEnabled(SafeMobileSettings safeMobileSettings) {
        Identity identity;
        if (safeMobileSettings == null || safeMobileSettings.isConversationsEnabled() || (identity = ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity()) == null || !(identity instanceof AnonymousIdentity) || StringUtils.hasLength(((AnonymousIdentity) identity).getEmail())) {
            return false;
        }
        if (!ZendeskConfig.INSTANCE.isCoppaEnabled()) {
            return true;
        }
        Logger.w(LOG_TAG, "This ticket will not be viewable by the user after submission. Conversations are not supported and COPPA removes any personal information associated with the ticket", new Object[0]);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reinitializeFragment() {
        this.progressBar.setVisibility(8);
        this.attachmentContainerHost.reset();
        this.imageUploadService.reset();
        this.isEmailFieldVisible = isEmailFieldEnabled(this.mobileSettings);
        this.descriptionEditText.setEnabled(true);
        this.descriptionEditText.setText("");
        this.emailEditText.setEnabled(true);
        this.emailEditText.setText("");
        checkSendButtonState();
        displayAttachmentButton();
    }

    class RequestCallback extends ZendeskCallback<CreateRequest> {
        RequestCallback() {
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onSuccess(CreateRequest createRequest) {
            if (createRequest == null || createRequest.getId() == null) {
                Logger.e(ContactZendeskFragment.LOG_TAG, "Attempted to store a null request in callback.", new Object[0]);
            } else {
                ZendeskConfig.INSTANCE.storage().requestStorage().setCommentCount(createRequest.getId(), 1);
            }
            if (ContactZendeskFragment.this.feedbackListener != null) {
                ContactZendeskFragment.this.feedbackListener.onSubmissionCompleted();
            }
            if (!ContactZendeskFragment.this.isAdded() || ContactZendeskFragment.this.getActivity() == null) {
                return;
            }
            ContactZendeskFragment.this.reinitializeFragment();
        }

        @Override // com.zendesk.service.ZendeskCallback
        public void onError(ErrorResponse errorResponse) {
            ContactZendeskFragment.this.enableSendButton();
            if (ContactZendeskFragment.this.feedbackListener != null) {
                ContactZendeskFragment.this.feedbackListener.onSubmissionError(errorResponse);
            }
            ContactZendeskFragment.this.progressBar.setVisibility(8);
            ContactZendeskFragment.this.descriptionEditText.setEnabled(true);
            ContactZendeskFragment.this.emailEditText.setEnabled(true);
            if (ContactZendeskFragment.this.doneMenuItem != null) {
                ContactZendeskFragment.this.doneMenuItem.getIcon().setAlpha(ContactZendeskFragment.this.descriptionEditText.getText().length() == 0 ? 102 : 255);
            }
            if (ContactZendeskFragment.this.retryable != null) {
                ContactZendeskFragment.this.retryable.onRetryAvailable(ContactZendeskFragment.this.getString(R.string.rate_my_app_dialog_feedback_send_error_toast), new View.OnClickListener() { // from class: com.zendesk.sdk.feedback.ui.ContactZendeskFragment.RequestCallback.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ContactZendeskFragment.this.sendFeedback();
                    }
                });
            }
        }
    }

    private class ContactZendeskFeedbackConfiguration implements ZendeskFeedbackConfiguration {
        private final ZendeskFeedbackConfiguration mConfiguration;

        public ContactZendeskFeedbackConfiguration(ZendeskFeedbackConfiguration zendeskFeedbackConfiguration) {
            this.mConfiguration = zendeskFeedbackConfiguration;
        }

        @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
        public List<String> getTags() {
            return this.mConfiguration.getTags();
        }

        @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
        public String getAdditionalInfo() {
            return this.mConfiguration.getAdditionalInfo();
        }

        @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
        public String getRequestSubject() {
            return this.mConfiguration.getRequestSubject();
        }
    }
}
