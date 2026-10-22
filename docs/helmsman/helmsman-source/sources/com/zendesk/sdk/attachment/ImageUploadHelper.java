package com.zendesk.sdk.attachment;

import com.zendesk.belvedere.BelvedereResult;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.feedback.ui.AttachmentContainerHost;
import com.zendesk.sdk.model.request.Attachment;
import com.zendesk.sdk.model.request.UploadResponse;
import com.zendesk.sdk.network.UploadProvider;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class ImageUploadHelper {
    private static final String LOG_TAG = "ImageUploadHelper";
    private ImageUploadProgressListener progressListener;
    private final UploadProvider uploadProvider;
    private AtomicInteger uploadCounter = new AtomicInteger(0);
    private AtomicInteger uploadedCounter = new AtomicInteger(0);
    private Map<File, UploadResponse> uploadedAttachments = new HashMap();
    private List<File> mAddedForUpload = new ArrayList();
    private boolean mDeleteAllUploadsCalled = false;

    public interface ImageUploadProgressListener {
        void allImagesUploaded(Map<File, UploadResponse> map);

        void imageUploadError(ErrorResponse errorResponse, BelvedereResult belvedereResult);

        void imageUploaded(UploadResponse uploadResponse, BelvedereResult belvedereResult);
    }

    public ImageUploadHelper(ImageUploadProgressListener imageUploadProgressListener, UploadProvider uploadProvider) {
        this.progressListener = imageUploadProgressListener;
        this.uploadProvider = uploadProvider;
    }

    public void uploadImage(BelvedereResult belvedereResult, String str) {
        this.mAddedForUpload.add(belvedereResult.getFile());
        this.uploadCounter.incrementAndGet();
        this.uploadProvider.uploadAttachment(belvedereResult.getFile().getName(), belvedereResult.getFile(), str, newZendeskCallback(belvedereResult));
    }

    public boolean isImageUploadCompleted() {
        return this.uploadCounter.get() == this.uploadedCounter.get();
    }

    public List<String> getUploadTokens() {
        ArrayList arrayList = new ArrayList();
        Iterator<UploadResponse> it = this.uploadedAttachments.values().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getToken());
        }
        return arrayList;
    }

    public List<Attachment> getUploadedAttachments() {
        Collection<UploadResponse> collectionValues = this.uploadedAttachments.values();
        ArrayList arrayList = new ArrayList();
        Iterator<UploadResponse> it = collectionValues.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getAttachment());
        }
        return arrayList;
    }

    public HashMap<AttachmentContainerHost.AttachmentState, List<File>> getRecentState() {
        ArrayList arrayList = new ArrayList(this.uploadedAttachments.keySet());
        ArrayList arrayList2 = new ArrayList();
        for (File file : this.mAddedForUpload) {
            if (!arrayList.contains(file)) {
                arrayList2.add(file);
            }
        }
        HashMap<AttachmentContainerHost.AttachmentState, List<File>> map = new HashMap<>();
        map.put(AttachmentContainerHost.AttachmentState.UPLOADED, arrayList);
        map.put(AttachmentContainerHost.AttachmentState.UPLOADING, arrayList2);
        return map;
    }

    public List<BelvedereResult> removeDuplicateFilesFromList(List<BelvedereResult> list) {
        ArrayList arrayList = new ArrayList();
        for (BelvedereResult belvedereResult : list) {
            if (belvedereResult != null) {
                Iterator<File> it = this.mAddedForUpload.iterator();
                boolean z = false;
                while (it.hasNext()) {
                    if (it.next().getAbsolutePath().equals(belvedereResult.getFile().getAbsolutePath())) {
                        z = true;
                    }
                }
                if (!z) {
                    arrayList.add(belvedereResult);
                }
            }
        }
        return arrayList;
    }

    public void reset() {
        if (this.uploadCounter.get() == this.uploadedCounter.get()) {
            this.uploadCounter.set(0);
            this.uploadedCounter.set(0);
            this.uploadedAttachments.clear();
            this.mAddedForUpload.clear();
        }
    }

    public void setImageUploadProgressListener(ImageUploadProgressListener imageUploadProgressListener) {
        this.progressListener = imageUploadProgressListener;
    }

    public void removeImage(File file) {
        Map<File, UploadResponse> map = this.uploadedAttachments;
        if ((map == null || map.get(file) == null || !StringUtils.hasLength(this.uploadedAttachments.get(file).getToken())) ? false : true) {
            this.uploadProvider.deleteAttachment(this.uploadedAttachments.get(file).getToken(), null);
        }
        this.mAddedForUpload.remove(file);
        this.uploadedAttachments.remove(file);
    }

    public void deleteAllAttachmentsBeforeShutdown() {
        this.mDeleteAllUploadsCalled = true;
        List<String> uploadTokens = getUploadTokens();
        if (CollectionUtils.isNotEmpty(uploadTokens)) {
            Iterator<String> it = uploadTokens.iterator();
            while (it.hasNext()) {
                this.uploadProvider.deleteAttachment(it.next(), null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void checkAndNotifyState() {
        ImageUploadProgressListener imageUploadProgressListener;
        if (isImageUploadCompleted() && (imageUploadProgressListener = this.progressListener) != null) {
            imageUploadProgressListener.allImagesUploaded(this.uploadedAttachments);
        }
    }

    private ZendeskCallback<UploadResponse> newZendeskCallback(final BelvedereResult belvedereResult) {
        return new ZendeskCallback<UploadResponse>() { // from class: com.zendesk.sdk.attachment.ImageUploadHelper.1
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(UploadResponse uploadResponse) {
                if (uploadResponse != null && uploadResponse.getAttachment() != null) {
                    Logger.d(ImageUploadHelper.LOG_TAG, String.format(Locale.US, "Image successfully uploaded: %s", uploadResponse.getAttachment().getContentUrl()), new Object[0]);
                }
                ImageUploadHelper.this.uploadedCounter.incrementAndGet();
                ImageUploadHelper.this.uploadedAttachments.put(belvedereResult.getFile(), uploadResponse);
                if (ImageUploadHelper.this.progressListener != null) {
                    ImageUploadHelper.this.progressListener.imageUploaded(uploadResponse, belvedereResult);
                }
                if (ImageUploadHelper.this.mDeleteAllUploadsCalled && uploadResponse != null && StringUtils.hasLength(uploadResponse.getToken())) {
                    ImageUploadHelper.this.uploadProvider.deleteAttachment(uploadResponse.getToken(), null);
                }
                ImageUploadHelper.this.checkAndNotifyState();
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                Logger.e(ImageUploadHelper.LOG_TAG, String.format(Locale.US, "Error; Reason: %s, Status: %s, isNetworkError: %s", errorResponse.getReason(), Integer.valueOf(errorResponse.getStatus()), Boolean.valueOf(errorResponse.isNetworkError())), new Object[0]);
                ImageUploadHelper.this.uploadedCounter.incrementAndGet();
                ImageUploadHelper.this.mAddedForUpload.remove(belvedereResult.getFile());
                if (ImageUploadHelper.this.progressListener != null) {
                    ImageUploadHelper.this.progressListener.imageUploadError(errorResponse, belvedereResult);
                }
                ImageUploadHelper.this.checkAndNotifyState();
            }
        };
    }
}
