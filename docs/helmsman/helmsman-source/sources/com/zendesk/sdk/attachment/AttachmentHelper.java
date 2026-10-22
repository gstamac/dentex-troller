package com.zendesk.sdk.attachment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.widget.Toast;
import com.zendesk.belvedere.BelvedereResult;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.ui.AttachmentContainerHost;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.service.ErrorResponse;
import com.zendesk.util.FileUtils;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class AttachmentHelper {
    private static final String DEFAULT_MIMETYPE = "application/octet-stream";
    private static final String LOG_TAG = "AttachmentHelper";

    public static boolean isAttachmentSupportEnabled(SafeMobileSettings safeMobileSettings) {
        return safeMobileSettings != null && safeMobileSettings.isAttachmentsEnabled();
    }

    public static boolean isFileEligibleForUpload(File file, SafeMobileSettings safeMobileSettings) {
        return file != null && file.exists() && safeMobileSettings != null && file.length() <= safeMobileSettings.getMaxAttachmentSize();
    }

    public static void showAttachmentTryAgainDialog(final Context context, final BelvedereResult belvedereResult, ErrorResponse errorResponse, final ImageUploadHelper imageUploadHelper, final AttachmentContainerHost attachmentContainerHost) {
        Logger.e(LOG_TAG, "Attachment failed to upload: %s", belvedereResult.getFile().getName());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.attachment_upload_error_upload_failed));
        builder.setCancelable(false);
        builder.setNegativeButton(context.getString(R.string.attachment_upload_error_cancel), new DialogInterface.OnClickListener() { // from class: com.zendesk.sdk.attachment.AttachmentHelper.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                attachmentContainerHost.removeAttachment(belvedereResult.getFile());
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(context.getString(R.string.attachment_upload_error_try_again), new DialogInterface.OnClickListener() { // from class: com.zendesk.sdk.attachment.AttachmentHelper.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ImageUploadHelper imageUploadHelper2 = imageUploadHelper;
                BelvedereResult belvedereResult2 = belvedereResult;
                imageUploadHelper2.uploadImage(belvedereResult2, AttachmentHelper.getMimeType(context, belvedereResult2.getUri()));
                attachmentContainerHost.setAttachmentState(belvedereResult.getFile(), AttachmentContainerHost.AttachmentState.UPLOADING);
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void processAndUploadSelectedFiles(List<BelvedereResult> list, ImageUploadHelper imageUploadHelper, Context context, AttachmentContainerHost attachmentContainerHost, SafeMobileSettings safeMobileSettings) {
        List<BelvedereResult> listRemoveDuplicateFilesFromList = imageUploadHelper.removeDuplicateFilesFromList(list);
        if (listRemoveDuplicateFilesFromList.size() != list.size()) {
            Toast.makeText(context, context.getString(R.string.attachment_upload_error_file_already_added), 1).show();
            Logger.e(LOG_TAG, "Files already added", new Object[0]);
        }
        Iterator<BelvedereResult> it = listRemoveDuplicateFilesFromList.iterator();
        while (it.hasNext()) {
            BelvedereResult next = it.next();
            if (next != null && next.getFile().exists()) {
                if (isFileEligibleForUpload(next.getFile(), safeMobileSettings)) {
                    imageUploadHelper.uploadImage(next, getMimeType(context, next.getUri()));
                    attachmentContainerHost.addAttachment(next.getFile());
                } else {
                    Toast.makeText(context, String.format(Locale.US, context.getString(R.string.attachment_upload_error_file_too_big), safeMobileSettings != null ? FileUtils.humanReadableFileSize(Long.valueOf(safeMobileSettings.getMaxAttachmentSize())) : ""), 1).show();
                    Logger.e(LOG_TAG, "File is too big: " + next.getFile().getName(), new Object[0]);
                }
            } else {
                Toast.makeText(context, context.getString(R.string.attachment_upload_error_file_not_found), 1).show();
                Logger.e(LOG_TAG, "File not found: " + (next == null ? "no filename" : next.getFile().getName()), new Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getMimeType(Context context, Uri uri) {
        return uri != null ? context.getContentResolver().getType(uri) : "application/octet-stream";
    }
}
