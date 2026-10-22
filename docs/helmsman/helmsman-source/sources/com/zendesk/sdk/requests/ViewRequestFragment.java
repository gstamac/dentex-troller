package com.zendesk.sdk.requests;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zendesk.belvedere.BelvedereCallback;
import com.zendesk.belvedere.BelvedereResult;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.attachment.AttachmentHelper;
import com.zendesk.sdk.attachment.ImageUploadHelper;
import com.zendesk.sdk.attachment.ZendeskBelvedereProvider;
import com.zendesk.sdk.deeplinking.ZendeskDeepLinking;
import com.zendesk.sdk.deeplinking.actions.ActionRefreshComments;
import com.zendesk.sdk.feedback.ui.AttachmentContainerHost;
import com.zendesk.sdk.model.request.Attachment;
import com.zendesk.sdk.model.request.Comment;
import com.zendesk.sdk.model.request.CommentResponse;
import com.zendesk.sdk.model.request.CommentsResponse;
import com.zendesk.sdk.model.request.EndUserComment;
import com.zendesk.sdk.model.request.UploadResponse;
import com.zendesk.sdk.model.request.User;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.RequestProvider;
import com.zendesk.sdk.network.Retryable;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.TextWatcherAdapter;
import com.zendesk.sdk.util.NetworkUtils;
import com.zendesk.sdk.util.UiUtils;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ViewRequestFragment extends Fragment implements ActionRefreshComments.ActionRefreshCommentsListener {
    public static final String EXTRA_REQUEST_ID = "requestId";
    public static final String EXTRA_REQUEST_SUBJECT = "subject";
    private static final int FOURTY_PERCENT_OPACITY = 102;
    private static final int FULL_OPACTITY = 255;
    private static final String TAG = "ViewRequestFragment";
    private Button mAttachmentButton;
    private AttachmentContainerHost mAttachmentContainerHost;
    private AttachmentContainerHost.AttachmentContainerListener mAttachmentContainerListener;
    private RequestCommentsListAdapter mCommentListAdapter;
    private ZendeskCallback<CommentsResponse> mCommentListCallback;
    private ListView mCommentListView;
    private EditText mEditText;
    private ImageUploadHelper mImageUploadHelper;
    private ImageUploadHelper.ImageUploadProgressListener mImageUploadProgressListener;
    private BelvedereCallback<List<BelvedereResult>> mImagesExtracted;
    private ZendeskCallback<Uri> mLoadImageCallback;
    private ProgressBar mProgressBar;
    private String mRequestId;
    private Retryable mRetryable;
    private Button mSendButton;
    private String mSubject;
    private SubmissionListener mSubmissionListener;
    private ZendeskCallback<Comment> mSubmitCommentCallback;

    private enum Action {
        SEND_COMMENT,
        LOAD_COMMENTS,
        LOAD_IMAGE
    }

    public static ViewRequestFragment newInstance(String str) {
        return newInstance(str, null, null);
    }

    public static ViewRequestFragment newInstance(String str, String str2) {
        return newInstance(str, str2, null);
    }

    public static ViewRequestFragment newInstance(String str, String str2, SubmissionListener submissionListener) {
        Bundle bundle = new Bundle();
        bundle.putString("requestId", str);
        if (StringUtils.hasLength(str2)) {
            bundle.putString("subject", str2);
        }
        ViewRequestFragment viewRequestFragment = new ViewRequestFragment();
        viewRequestFragment.setArguments(bundle);
        viewRequestFragment.setRetainInstance(true);
        viewRequestFragment.setSubmissionListener(submissionListener);
        return viewRequestFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_view_request, viewGroup, false);
        this.mEditText = (EditText) viewInflate.findViewById(R.id.view_request_comment_edittext);
        this.mSendButton = (Button) viewInflate.findViewById(R.id.view_request_comment_send_bth);
        this.mAttachmentButton = (Button) viewInflate.findViewById(R.id.view_request_comment_attachment_bth);
        this.mProgressBar = (ProgressBar) viewInflate.findViewById(R.id.view_request_fragment_progress);
        this.mCommentListView = (ListView) viewInflate.findViewById(R.id.view_request_comment_list);
        this.mEditText.addTextChangedListener(new TextWatcherAdapter() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.1
            @Override // com.zendesk.sdk.ui.TextWatcherAdapter, android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                ViewRequestFragment.this.checkSendButtonState();
            }
        });
        AttachmentContainerHost attachmentContainerHost = (AttachmentContainerHost) viewInflate.findViewById(R.id.view_request_attachment_container);
        this.mAttachmentContainerHost = attachmentContainerHost;
        attachmentContainerHost.setParent(viewInflate.findViewById(R.id.view_request_comment_attachment_container));
        this.mAttachmentContainerHost.setState(this.mImageUploadHelper);
        this.mAttachmentContainerHost.setAttachmentContainerListener(this.mAttachmentContainerListener);
        this.mSendButton.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ViewRequestFragment.this.sendComment();
            }
        });
        setSendButtonVisibility(false);
        setHasOptionsMenu(true);
        SafeMobileSettings mobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
        if (mobileSettings != null && AttachmentHelper.isAttachmentSupportEnabled(mobileSettings) && canGetAttachments()) {
            UiUtils.setVisibility(this.mAttachmentButton, 0);
            UiUtils.setVisibility(this.mSendButton, 8);
            setEditTextMarginRight(true);
            this.mAttachmentButton.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ZendeskBelvedereProvider.INSTANCE.getBelvedere(ViewRequestFragment.this.getContext()).showDialog(ViewRequestFragment.this.getChildFragmentManager());
                }
            });
        } else {
            UiUtils.setVisibility(this.mAttachmentButton, 8);
            UiUtils.setVisibility(this.mSendButton, 8);
            setEditTextMarginRight(false);
        }
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mImageUploadProgressListener = new ImageUploadHelper.ImageUploadProgressListener() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.4
            @Override // com.zendesk.sdk.attachment.ImageUploadHelper.ImageUploadProgressListener
            public void allImagesUploaded(Map<File, UploadResponse> map) {
                ViewRequestFragment.this.checkSendButtonState();
            }

            @Override // com.zendesk.sdk.attachment.ImageUploadHelper.ImageUploadProgressListener
            public void imageUploaded(UploadResponse uploadResponse, BelvedereResult belvedereResult) {
                ViewRequestFragment.this.mAttachmentContainerHost.setAttachmentUploaded(belvedereResult.getFile());
            }

            @Override // com.zendesk.sdk.attachment.ImageUploadHelper.ImageUploadProgressListener
            public void imageUploadError(ErrorResponse errorResponse, BelvedereResult belvedereResult) {
                AttachmentHelper.showAttachmentTryAgainDialog(ViewRequestFragment.this.getActivity(), belvedereResult, errorResponse, ViewRequestFragment.this.mImageUploadHelper, ViewRequestFragment.this.mAttachmentContainerHost);
            }
        };
        this.mImageUploadHelper = new ImageUploadHelper(this.mImageUploadProgressListener, ZendeskConfig.INSTANCE.provider().uploadProvider());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mRequestId = getArguments().getString("requestId");
        this.mSubject = getArguments().getString("subject");
        setupCallbacks();
        if (this.mCommentListView.getAdapter() == null) {
            loadRequest();
        }
        ZendeskDeepLinking.INSTANCE.registerAction(this, new ActionRefreshComments(this.mRequestId));
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        tearDownCallbacks();
        ZendeskDeepLinking.INSTANCE.unregisterAction(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mAttachmentContainerListener = new AttachmentContainerHost.AttachmentContainerListener() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.5
            @Override // com.zendesk.sdk.feedback.ui.AttachmentContainerHost.AttachmentContainerListener
            public void attachmentRemoved(File file) {
                ViewRequestFragment.this.mImageUploadHelper.removeImage(file);
            }
        };
        if (context instanceof Retryable) {
            this.mRetryable = (Retryable) context;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mAttachmentContainerListener = null;
        this.mRetryable = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mImageUploadHelper.setImageUploadProgressListener(null);
        this.mImageUploadHelper.deleteAllAttachmentsBeforeShutdown();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mImagesExtracted = new BelvedereCallback<List<BelvedereResult>>() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.6
            @Override // com.zendesk.belvedere.BelvedereCallback
            public void success(List<BelvedereResult> list) {
                SafeMobileSettings mobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
                if (mobileSettings != null) {
                    AttachmentHelper.processAndUploadSelectedFiles(list, ViewRequestFragment.this.mImageUploadHelper, ViewRequestFragment.this.getActivity(), ViewRequestFragment.this.mAttachmentContainerHost, mobileSettings);
                }
                ViewRequestFragment.this.checkSendButtonState();
                ViewRequestFragment.this.setLoadingVisibility(false);
            }
        };
        setLoadingVisibility(true);
        ZendeskBelvedereProvider.INSTANCE.getBelvedere(getContext()).getFilesFromActivityOnResult(i, i2, intent, this.mImagesExtracted);
    }

    public void setSubmissionListener(SubmissionListener submissionListener) {
        this.mSubmissionListener = submissionListener;
    }

    void onNetworkAvailable() {
        checkSendButtonState();
    }

    void onNetworkUnavailable() {
        setAttachmentButtonVisibility(false);
        setSendButtonVisibility(false);
    }

    private void setupCallbacks() {
        this.mCommentListCallback = new ZendeskCallback<CommentsResponse>() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.7
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(CommentsResponse commentsResponse) {
                ZendeskConfig.INSTANCE.storage().requestStorage().setCommentCount(ViewRequestFragment.this.mRequestId, commentsResponse.getComments().size());
                ViewRequestFragment.this.setLoadingVisibility(false);
                ViewRequestFragment.this.renderComments(commentsResponse);
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                ViewRequestFragment.this.setLoadingVisibility(false);
                ViewRequestFragment.this.handleError(errorResponse, Action.LOAD_COMMENTS);
            }
        };
        this.mSubmitCommentCallback = new ZendeskCallback<Comment>() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.8
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(Comment comment) {
                Integer commentCount = ZendeskConfig.INSTANCE.storage().requestStorage().getCommentCount(ViewRequestFragment.this.mRequestId);
                Integer numValueOf = ViewRequestFragment.this.mCommentListAdapter != null ? Integer.valueOf(ViewRequestFragment.this.mCommentListAdapter.getCount() + 1) : null;
                if (commentCount == null || numValueOf == null || numValueOf.intValue() >= commentCount.intValue()) {
                    ViewRequestFragment.this.setLoadingVisibility(false);
                    CommentResponse commentResponse = new CommentResponse();
                    commentResponse.setCreatedAt(new Date());
                    commentResponse.setBody(ViewRequestFragment.this.mEditText.getText().toString());
                    commentResponse.setAttachments(ViewRequestFragment.this.mImageUploadHelper.getUploadedAttachments());
                    ViewRequestFragment.this.mCommentListAdapter.insert(CommentWithUser.build(commentResponse, new User()), 0);
                    ViewRequestFragment.this.checkSendButtonState();
                } else {
                    ViewRequestFragment.this.loadRequest();
                }
                if (ViewRequestFragment.this.mSubmissionListener != null) {
                    ViewRequestFragment.this.mSubmissionListener.onSubmissionCompleted();
                }
                ViewRequestFragment.this.mEditText.setText("");
                ViewRequestFragment.this.mImageUploadHelper.reset();
                ViewRequestFragment.this.mAttachmentContainerHost.reset();
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                ViewRequestFragment.this.setLoadingVisibility(false);
                if (ViewRequestFragment.this.mSubmissionListener != null) {
                    ViewRequestFragment.this.mSubmissionListener.onSubmissionError(errorResponse);
                }
                ViewRequestFragment.this.handleError(errorResponse, Action.SEND_COMMENT);
            }
        };
        this.mLoadImageCallback = new ZendeskCallback<Uri>() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.9
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(Uri uri) {
                ViewRequestFragment.this.setLoadingVisibility(false);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(uri, "image/*");
                ZendeskBelvedereProvider.INSTANCE.getBelvedere(ViewRequestFragment.this.getContext()).grantPermissionsForUri(intent, uri);
                try {
                    ViewRequestFragment.this.startActivity(intent);
                } catch (Exception unused) {
                    onError(new ErrorResponseAdapter("Couldn't start activity to view attachment"));
                }
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                ViewRequestFragment.this.setLoadingVisibility(false);
                ViewRequestFragment.this.handleError(errorResponse, Action.LOAD_IMAGE);
            }
        };
    }

    private void tearDownCallbacks() {
        this.mCommentListCallback = null;
        this.mSubmitCommentCallback = null;
        this.mLoadImageCallback = null;
        this.mImagesExtracted = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadRequest() {
        setTitle(this.mSubject);
        setLoadingVisibility(true);
        try {
            ZendeskConfig.INSTANCE.provider().requestProvider().getComments(this.mRequestId, this.mCommentListCallback);
        } catch (RuntimeException unused) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadImage(Attachment attachment) {
        setLoadingVisibility(true);
        ImageLoader.INSTANCE.loadAndShowImage(attachment, getActivity(), this.mLoadImageCallback, ZendeskBelvedereProvider.INSTANCE.getBelvedere(getContext()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleError(ErrorResponse errorResponse, Action action) {
        String string;
        View.OnClickListener onClickListener;
        if (isVisible()) {
            Logger.w(TAG, "Failed to complete action: Status: " + errorResponse.getStatus() + " Reason: " + errorResponse.getReason(), new Object[0]);
            int i = AnonymousClass13.$SwitchMap$com$zendesk$sdk$requests$ViewRequestFragment$Action[action.ordinal()];
            if (i == 1) {
                string = getString(R.string.view_request_send_comment_error);
                onClickListener = new View.OnClickListener() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ViewRequestFragment.this.sendComment();
                    }
                };
            } else if (i != 2) {
                string = null;
                onClickListener = null;
            } else {
                string = getString(R.string.view_request_load_comments_error);
                onClickListener = new View.OnClickListener() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.11
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        ViewRequestFragment.this.loadRequest();
                    }
                };
            }
            Retryable retryable = this.mRetryable;
            if (retryable != null) {
                retryable.onRetryAvailable(string, onClickListener);
            }
        }
    }

    /* JADX INFO: renamed from: com.zendesk.sdk.requests.ViewRequestFragment$13, reason: invalid class name */
    static /* synthetic */ class AnonymousClass13 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$sdk$requests$ViewRequestFragment$Action;

        static {
            int[] iArr = new int[Action.values().length];
            $SwitchMap$com$zendesk$sdk$requests$ViewRequestFragment$Action = iArr;
            try {
                iArr[Action.SEND_COMMENT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$requests$ViewRequestFragment$Action[Action.LOAD_COMMENTS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$requests$ViewRequestFragment$Action[Action.LOAD_IMAGE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renderComments(CommentsResponse commentsResponse) {
        ArrayList arrayList = new ArrayList(commentsResponse.getComments().size());
        Iterator<CommentResponse> it = commentsResponse.getComments().iterator();
        while (it.hasNext()) {
            CommentResponse next = it.next();
            CommentWithUser commentWithUserBuild = CommentWithUser.build(next, commentsResponse.getUsers());
            if (commentWithUserBuild != null) {
                arrayList.add(commentWithUserBuild);
            } else {
                Locale locale = Locale.US;
                Object[] objArr = new Object[2];
                objArr[0] = Boolean.valueOf(next == null);
                objArr[1] = Boolean.valueOf(CollectionUtils.isEmpty(commentsResponse.getUsers()));
                Logger.w(TAG, String.format(locale, "Comment is null %s, users list is empty %s", objArr), new Object[0]);
            }
        }
        if (getActivity() != null) {
            RequestCommentsListAdapter requestCommentsListAdapter = new RequestCommentsListAdapter(getActivity(), R.layout.row_agent_comment, arrayList, new RequestCommentsListAdapter.AttachmentOnClickListener() { // from class: com.zendesk.sdk.requests.ViewRequestFragment.12
                @Override // com.zendesk.sdk.requests.RequestCommentsListAdapter.AttachmentOnClickListener
                public void onAttachmentClicked(View view, Attachment attachment) {
                    ViewRequestFragment.this.loadImage(attachment);
                }
            });
            this.mCommentListAdapter = requestCommentsListAdapter;
            this.mCommentListView.setAdapter((ListAdapter) requestCommentsListAdapter);
        }
    }

    private void setTitle(String str) {
        if (StringUtils.hasLength(str)) {
            FragmentActivity activity = getActivity();
            if (activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).setTitle(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendComment() {
        setLoadingVisibility(true);
        SubmissionListener submissionListener = this.mSubmissionListener;
        if (submissionListener != null) {
            submissionListener.onSubmissionStarted();
        }
        RequestProvider requestProvider = ZendeskConfig.INSTANCE.provider().requestProvider();
        EndUserComment endUserComment = new EndUserComment();
        endUserComment.setValue(this.mEditText.getText().toString());
        endUserComment.setAttachments(this.mImageUploadHelper.getUploadTokens());
        requestProvider.addComment(this.mRequestId, endUserComment, this.mSubmitCommentCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSendButtonState() {
        if (isVisible()) {
            EditText editText = this.mEditText;
            boolean z = editText != null && StringUtils.hasLength(editText.getText().toString()) && NetworkUtils.isConnected(getActivity());
            SafeMobileSettings mobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
            boolean z2 = mobileSettings != null && AttachmentHelper.isAttachmentSupportEnabled(mobileSettings) && canGetAttachments();
            ImageUploadHelper imageUploadHelper = this.mImageUploadHelper;
            boolean z3 = imageUploadHelper != null && imageUploadHelper.isImageUploadCompleted();
            if (z && z3) {
                setAttachmentButtonVisibility(false);
                setSendButtonVisibility(true);
                this.mSendButton.setEnabled(true);
                this.mSendButton.getBackground().setAlpha(255);
                return;
            }
            if (z) {
                setAttachmentButtonVisibility(false);
                setSendButtonVisibility(true);
                this.mSendButton.setEnabled(false);
                this.mSendButton.getBackground().setAlpha(102);
                return;
            }
            setSendButtonVisibility(false);
            if (z2) {
                setAttachmentButtonVisibility(true);
            }
        }
    }

    private void setSendButtonVisibility(boolean z) {
        Button button = this.mSendButton;
        if (button != null) {
            button.setVisibility(z ? 0 : 8);
        }
        setEditTextMarginRight(z);
    }

    private void setAttachmentButtonVisibility(boolean z) {
        if (this.mSendButton != null) {
            this.mAttachmentButton.setVisibility(z ? 0 : 8);
        }
        setEditTextMarginRight(z);
    }

    private void setEditTextMarginRight(boolean z) {
        EditText editText = this.mEditText;
        if (editText != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
            if (z) {
                layoutParams.rightMargin = UiUtils.dpToPixels(0, getResources().getDisplayMetrics());
            } else {
                layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.view_request_horizontal_margin);
            }
            this.mEditText.setLayoutParams(layoutParams);
        }
    }

    private boolean canGetAttachments() {
        return ZendeskBelvedereProvider.INSTANCE.getBelvedere(getContext()).oneOrMoreSourceAvailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLoadingVisibility(boolean z) {
        Retryable retryable;
        if (z && (retryable = this.mRetryable) != null) {
            retryable.onRetryUnavailable();
        }
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            progressBar.setVisibility(z ? 0 : 4);
        }
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setEnabled(!z);
        }
        AttachmentContainerHost attachmentContainerHost = this.mAttachmentContainerHost;
        if (attachmentContainerHost != null) {
            attachmentContainerHost.setAttachmentsDeletable(!z);
        }
        Button button = this.mSendButton;
        if (button != null && button.getVisibility() == 0 && z) {
            this.mSendButton.setEnabled(false);
            this.mSendButton.getBackground().setAlpha(102);
        }
    }

    @Override // com.zendesk.sdk.deeplinking.actions.ActionRefreshComments.ActionRefreshCommentsListener
    public void refreshComments(ActionRefreshComments.ActionRefreshCommentsData actionRefreshCommentsData) {
        loadRequest();
    }
}
