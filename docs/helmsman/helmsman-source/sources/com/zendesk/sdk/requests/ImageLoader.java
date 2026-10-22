package com.zendesk.sdk.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.zendesk.belvedere.Belvedere;
import com.zendesk.belvedere.BelvedereResult;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.request.Attachment;
import com.zendesk.sdk.network.impl.ZendeskPicassoProvider;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.StringUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
enum ImageLoader {
    INSTANCE;

    private static final String LOG_TAG = "ImageLoader";
    private Target mTarget;

    synchronized void loadAndShowImage(final Attachment attachment, Context context, final ZendeskCallback<Uri> zendeskCallback, final Belvedere belvedere) {
        BelvedereResult fileRepresentation = belvedere.getFileRepresentation(String.format(Locale.US, "%s-%s", attachment.getId(), attachment.getFileName()));
        if (fileRepresentation != null && fileRepresentation.getFile().isFile() && fileRepresentation.getFile().length() > 0 && zendeskCallback != null) {
            zendeskCallback.onSuccess(fileRepresentation.getUri());
            return;
        }
        RequestCreator requestCreatorLoad = ZendeskPicassoProvider.getInstance(context).load(attachment.getContentUrl());
        Target target = new Target() { // from class: com.zendesk.sdk.requests.ImageLoader.1
            @Override // com.squareup.picasso.Target
            public void onPrepareLoad(Drawable drawable) {
            }

            @Override // com.squareup.picasso.Target
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                ImageLoader.this.new DownloadImageToExternalStorage(zendeskCallback, belvedere).execute(ImageLoader.this.new TaskData(attachment, bitmap));
            }

            @Override // com.squareup.picasso.Target
            public void onBitmapFailed(Drawable drawable) {
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onError(new ErrorResponseAdapter("Error loading attachment"));
                }
            }
        };
        this.mTarget = target;
        requestCreatorLoad.into(target);
    }

    class DownloadImageToExternalStorage extends AsyncTask<TaskData, Void, Result<Uri>> {
        private final Belvedere mBelvedere;
        private final ZendeskCallback<Uri> mCallback;

        public DownloadImageToExternalStorage(ZendeskCallback<Uri> zendeskCallback, Belvedere belvedere) {
            this.mCallback = zendeskCallback;
            this.mBelvedere = belvedere;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Result<Uri> doInBackground(TaskData... taskDataArr) throws Throwable {
            Bitmap bitmap = taskDataArr[0].bitmap;
            Attachment attachment = taskDataArr[0].attachment;
            String contentType = (attachment == null || StringUtils.isEmpty(attachment.getContentType())) ? "" : attachment.getContentType();
            FileOutputStream fileOutputStream = null;
            Long id = attachment == null ? null : attachment.getId();
            if (!contentType.startsWith("image/")) {
                return ImageLoader.this.new Result<>((ErrorResponse) new ErrorResponseAdapter("attachment is not an image"));
            }
            if (id == null) {
                return ImageLoader.this.new Result<>((ErrorResponse) new ErrorResponseAdapter("attachment does not have an id"));
            }
            BelvedereResult fileRepresentation = this.mBelvedere.getFileRepresentation(String.format(Locale.US, "%s-%s", id, attachment.getFileName()));
            if (fileRepresentation == null) {
                return ImageLoader.this.new Result<>((ErrorResponse) new ErrorResponseAdapter("Error creating tmp file"));
            }
            try {
                try {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(fileRepresentation.getFile());
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 42, fileOutputStream2);
                        fileOutputStream2.flush();
                        Result<Uri> result = ImageLoader.this.new Result<>(fileRepresentation.getUri());
                        try {
                            fileOutputStream2.close();
                        } catch (IOException e) {
                            Logger.e(ImageLoader.LOG_TAG, "Couldn't close fileoutputstream", e, new Object[0]);
                        }
                        return result;
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        Result<Uri> result2 = ImageLoader.this.new Result<>(new ErrorResponseAdapter(e.getMessage()));
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e3) {
                                Logger.e(ImageLoader.LOG_TAG, "Couldn't close fileoutputstream", e3, new Object[0]);
                            }
                        }
                        return result2;
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e4) {
                                Logger.e(ImageLoader.LOG_TAG, "Couldn't close fileoutputstream", e4, new Object[0]);
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e = e5;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Result<Uri> result) {
            if (this.mCallback != null) {
                if (!result.isError()) {
                    this.mCallback.onSuccess(result.getResult());
                } else {
                    this.mCallback.onError(result.getErrorResponse());
                }
            }
        }
    }

    class TaskData {
        private Attachment attachment;
        private Bitmap bitmap;

        TaskData(Attachment attachment, Bitmap bitmap) {
            this.attachment = attachment;
            this.bitmap = bitmap;
        }
    }

    class Result<E> {
        private ErrorResponse mErrorResponse;
        private boolean mIsError = true;
        private E mResult;

        Result(E e) {
            this.mResult = e;
        }

        Result(ErrorResponse errorResponse) {
            this.mErrorResponse = errorResponse;
        }

        public E getResult() {
            if (this.mIsError) {
                return null;
            }
            return this.mResult;
        }

        ErrorResponse getErrorResponse() {
            if (this.mIsError) {
                return this.mErrorResponse;
            }
            return null;
        }

        boolean isError() {
            return this.mIsError;
        }
    }
}
