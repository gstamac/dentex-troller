package com.zendesk.sdk.feedback.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.attachment.ImageUploadHelper;
import com.zendesk.sdk.ui.ZendeskPicassoTransformationFactory;
import com.zendesk.sdk.util.UiUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AttachmentContainerHost extends LinearLayout {
    private static final String LOG_TAG = "AttachmentContainerHost";
    private List<AttachmentContainer> mAttachmentContainerList;
    private AttachmentContainerListener mAttachmentContainerListener;
    private View mParent;

    public interface AttachmentContainerListener {
        void attachmentRemoved(File file);
    }

    public enum AttachmentState {
        UPLOADING,
        UPLOADED,
        DISABLE
    }

    public AttachmentContainerHost(Context context) {
        super(context);
        this.mParent = this;
        this.mAttachmentContainerList = new ArrayList();
    }

    public AttachmentContainerHost(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mParent = this;
        this.mAttachmentContainerList = new ArrayList();
    }

    public AttachmentContainerHost(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mParent = this;
        this.mAttachmentContainerList = new ArrayList();
    }

    public AttachmentContainerHost(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mParent = this;
        this.mAttachmentContainerList = new ArrayList();
    }

    public void setAttachmentContainerListener(AttachmentContainerListener attachmentContainerListener) {
        this.mAttachmentContainerListener = attachmentContainerListener;
    }

    public void setParent(View view) {
        this.mParent = view;
    }

    public void setState(ImageUploadHelper imageUploadHelper) {
        if (imageUploadHelper == null) {
            Logger.d(LOG_TAG, "Please provide a non null ImageUploadHelper", new Object[0]);
            return;
        }
        HashMap<AttachmentState, List<File>> recentState = imageUploadHelper.getRecentState();
        if (recentState.containsKey(AttachmentState.UPLOADING)) {
            Iterator<File> it = recentState.get(AttachmentState.UPLOADING).iterator();
            while (it.hasNext()) {
                addAttachment(it.next());
            }
        }
        if (recentState.containsKey(AttachmentState.UPLOADED)) {
            for (File file : recentState.get(AttachmentState.UPLOADED)) {
                addAttachment(file);
                setAttachmentUploaded(file);
            }
        }
    }

    public void addAttachment(File file) {
        UiUtils.setVisibility(this.mParent, 0);
        AttachmentContainer attachmentContainer = new AttachmentContainer(getContext(), file, this, getOrientation());
        attachmentContainer.setId(file.hashCode());
        this.mAttachmentContainerList.add(attachmentContainer);
        addView(attachmentContainer);
    }

    public void setAttachmentUploaded(File file) {
        setAttachmentState(file, AttachmentState.UPLOADED);
    }

    public void setAttachmentState(File file, AttachmentState attachmentState) {
        for (AttachmentContainer attachmentContainer : this.mAttachmentContainerList) {
            if (attachmentContainer.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                attachmentContainer.setState(attachmentState);
            }
        }
    }

    public void removeAttachment(File file) {
        ArrayList arrayList = new ArrayList();
        for (AttachmentContainer attachmentContainer : this.mAttachmentContainerList) {
            if (!attachmentContainer.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                arrayList.add(attachmentContainer);
            }
        }
        this.mAttachmentContainerList = arrayList;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if ((childAt instanceof AttachmentContainer) && ((AttachmentContainer) childAt).getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                removeViewAt(i);
                break;
            }
        }
        if (getChildCount() < 1) {
            UiUtils.setVisibility(this.mParent, 8);
        }
    }

    public synchronized void removeAttachmentAndNotify(File file) {
        removeAttachment(file);
        AttachmentContainerListener attachmentContainerListener = this.mAttachmentContainerListener;
        if (attachmentContainerListener != null) {
            attachmentContainerListener.attachmentRemoved(file);
        }
    }

    public void setAttachmentsDeletable(boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof AttachmentContainer) {
                AttachmentContainer attachmentContainer = (AttachmentContainer) childAt;
                AttachmentState attachmentState = attachmentContainer.getAttachmentState();
                if (z && attachmentState == AttachmentState.DISABLE) {
                    attachmentContainer.setState(AttachmentState.UPLOADED);
                } else if (!z && attachmentState == AttachmentState.UPLOADED) {
                    attachmentContainer.setState(AttachmentState.DISABLE);
                }
            }
        }
    }

    public void reset() {
        UiUtils.setVisibility(this.mParent, 8);
        removeAllViews();
    }

    class AttachmentContainer extends FrameLayout {
        private AttachmentState mAttachmentState;
        private Button mDeleteButton;
        private File mFile;
        private ImageView mImageView;
        private int mOrientation;
        private ProgressBar mProgressBar;

        public AttachmentContainer(Context context, final File file, final AttachmentContainerHost attachmentContainerHost, int i) {
            super(context);
            this.mFile = file;
            this.mAttachmentState = AttachmentState.UPLOADING;
            this.mOrientation = i;
            View viewInflate = LayoutInflater.from(context).inflate(R.layout.view_attachment_container_item, (ViewGroup) this, false);
            this.mImageView = (ImageView) viewInflate.findViewById(R.id.attachment_image);
            this.mDeleteButton = (Button) viewInflate.findViewById(R.id.attachment_delete);
            ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.attachment_progress);
            this.mProgressBar = progressBar;
            progressBar.setIndeterminate(true);
            this.mDeleteButton.setOnClickListener(new View.OnClickListener() { // from class: com.zendesk.sdk.feedback.ui.AttachmentContainerHost.AttachmentContainer.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    attachmentContainerHost.removeAttachmentAndNotify(file);
                }
            });
            attachImage(this.mFile, this.mImageView, attachmentContainerHost, context);
            addView(viewInflate);
        }

        public void setState(AttachmentState attachmentState) {
            this.mAttachmentState = attachmentState;
            int i = AnonymousClass1.$SwitchMap$com$zendesk$sdk$feedback$ui$AttachmentContainerHost$AttachmentState[attachmentState.ordinal()];
            if (i == 1) {
                UiUtils.setVisibility(this.mDeleteButton, 8);
                UiUtils.setVisibility(this.mProgressBar, 0);
            } else if (i == 2) {
                UiUtils.setVisibility(this.mDeleteButton, 0);
                UiUtils.setVisibility(this.mProgressBar, 8);
            } else {
                if (i != 3) {
                    return;
                }
                UiUtils.setVisibility(this.mDeleteButton, 8);
                UiUtils.setVisibility(this.mProgressBar, 8);
            }
        }

        public AttachmentState getAttachmentState() {
            return this.mAttachmentState;
        }

        public File getFile() {
            return this.mFile;
        }

        private void attachImage(File file, ImageView imageView, ViewGroup viewGroup, Context context) {
            int i = this.mOrientation;
            if (i == 0) {
                loadFileIntoImageView(imageView, ZendeskPicassoTransformationFactory.INSTANCE.getResizeTransformationHeight(getResources().getDimensionPixelSize(R.dimen.attachment_container_host_horizontal_height) - (getResources().getDimensionPixelSize(R.dimen.attachment_container_image_margin) * 2)), file, context);
            } else if (i == 1) {
                int measuredWidth = viewGroup.getMeasuredWidth();
                if (measuredWidth == 0) {
                    measuredWidth = context.getResources().getDisplayMetrics().widthPixels;
                }
                loadFileIntoImageView(imageView, ZendeskPicassoTransformationFactory.INSTANCE.getResizeTransformationWidth(measuredWidth - (getResources().getDimensionPixelSize(R.dimen.attachment_container_image_margin) * 2)), file, context);
            }
            setState(this.mAttachmentState);
        }

        private void loadFileIntoImageView(ImageView imageView, Transformation transformation, File file, Context context) {
            Picasso.with(context).load(file).transform(transformation).noFade().into(imageView);
        }
    }

    /* JADX INFO: renamed from: com.zendesk.sdk.feedback.ui.AttachmentContainerHost$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$sdk$feedback$ui$AttachmentContainerHost$AttachmentState;

        static {
            int[] iArr = new int[AttachmentState.values().length];
            $SwitchMap$com$zendesk$sdk$feedback$ui$AttachmentContainerHost$AttachmentState = iArr;
            try {
                iArr[AttachmentState.UPLOADING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$feedback$ui$AttachmentContainerHost$AttachmentState[AttachmentState.UPLOADED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$feedback$ui$AttachmentContainerHost$AttachmentState[AttachmentState.DISABLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }
}
