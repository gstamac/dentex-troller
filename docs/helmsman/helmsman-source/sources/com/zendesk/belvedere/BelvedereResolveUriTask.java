package com.zendesk.belvedere;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class BelvedereResolveUriTask extends AsyncTask<Uri, Void, List<BelvedereResult>> {
    private static final String LOG_TAG = "BelvedereResolveUriTask";
    final BelvedereStorage belvedereStorage;
    final BelvedereCallback<List<BelvedereResult>> callback;
    final Context context;
    final BelvedereLogger log;

    BelvedereResolveUriTask(Context context, BelvedereLogger belvedereLogger, BelvedereStorage belvedereStorage, BelvedereCallback<List<BelvedereResult>> belvedereCallback) {
        this.context = context;
        this.log = belvedereLogger;
        this.belvedereStorage = belvedereStorage;
        this.callback = belvedereCallback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code duplicated, block: B:102:0x00e7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:106:0x013c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:120:0x0127 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:121:0x0127 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:127:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:63:0x00f4 A[Catch: IOException -> 0x0120, TRY_ENTER, TRY_LEAVE, TryCatch #15 {IOException -> 0x0120, blocks: (B:22:0x006e, B:63:0x00f4, B:75:0x011c), top: B:108:0x006e }] */
    /* JADX WARN: Code duplicated, block: B:75:0x011c A[Catch: IOException -> 0x0120, TRY_ENTER, TRY_LEAVE, TryCatch #15 {IOException -> 0x0120, blocks: (B:22:0x006e, B:63:0x00f4, B:75:0x011c), top: B:108:0x006e }] */
    /* JADX WARN: Code duplicated, block: B:96:0x010f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:98:0x012f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // android.os.AsyncTask
    public List<BelvedereResult> doInBackground(Uri... uriArr) throws Throwable {
        Throwable th;
        InputStream inputStream;
        InputStream inputStream2;
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2;
        ArrayList arrayList = new ArrayList();
        for (Uri uri : uriArr) {
            try {
                InputStream inputStreamOpenInputStream = this.context.getContentResolver().openInputStream(uri);
                try {
                    File tempFileForGalleryImage = this.belvedereStorage.getTempFileForGalleryImage(this.context, uri);
                    if (inputStreamOpenInputStream == null || tempFileForGalleryImage == null) {
                        BelvedereLogger belvedereLogger = this.log;
                        Locale locale = Locale.US;
                        Object[] objArr = new Object[2];
                        objArr[0] = Boolean.valueOf(inputStreamOpenInputStream == null);
                        objArr[1] = Boolean.valueOf(tempFileForGalleryImage == null);
                        belvedereLogger.w(LOG_TAG, String.format(locale, "Unable to resolve uri. InputStream null = %s, File null = %s", objArr));
                        if (inputStreamOpenInputStream != null) {
                            try {
                                inputStreamOpenInputStream.close();
                            } catch (IOException e) {
                                this.log.e(LOG_TAG, "Error closing InputStream", e);
                            }
                        }
                    } else {
                        this.log.d(LOG_TAG, String.format(Locale.US, "Copying media file into private cache - Uri: %s - Dest: %s", uri, tempFileForGalleryImage));
                        FileOutputStream fileOutputStream3 = new FileOutputStream(tempFileForGalleryImage);
                        try {
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int i = inputStreamOpenInputStream.read(bArr);
                                if (i <= 0) {
                                    break;
                                }
                                fileOutputStream3.write(bArr, 0, i);
                            }
                            arrayList.add(new BelvedereResult(tempFileForGalleryImage, this.belvedereStorage.getFileProviderUri(this.context, tempFileForGalleryImage)));
                            if (inputStreamOpenInputStream != null) {
                                try {
                                    inputStreamOpenInputStream.close();
                                } catch (IOException e2) {
                                    this.log.e(LOG_TAG, "Error closing InputStream", e2);
                                }
                            }
                            try {
                                fileOutputStream3.close();
                            } catch (IOException e3) {
                                this.log.e(LOG_TAG, "Error closing FileOutputStream", e3);
                            }
                        } catch (FileNotFoundException e4) {
                            e = e4;
                            fileOutputStream2 = fileOutputStream3;
                            inputStream2 = inputStreamOpenInputStream;
                            this.log.e(LOG_TAG, String.format(Locale.US, "File not found error copying file, uri: %s", uri), e);
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (IOException e5) {
                                    this.log.e(LOG_TAG, "Error closing InputStream", e5);
                                }
                            }
                            if (fileOutputStream2 != null) {
                                fileOutputStream2.close();
                            }
                        } catch (IOException e6) {
                            e = e6;
                            fileOutputStream = fileOutputStream3;
                            inputStream = inputStreamOpenInputStream;
                            try {
                                this.log.e(LOG_TAG, String.format(Locale.US, "IO Error copying file, uri: %s", uri), e);
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e7) {
                                        this.log.e(LOG_TAG, "Error closing InputStream", e7);
                                    }
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e8) {
                                        this.log.e(LOG_TAG, "Error closing InputStream", e8);
                                    }
                                }
                                if (fileOutputStream == null) {
                                    throw th;
                                }
                                try {
                                    fileOutputStream.close();
                                    throw th;
                                } catch (IOException e9) {
                                    this.log.e(LOG_TAG, "Error closing FileOutputStream", e9);
                                    throw th;
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = fileOutputStream3;
                            inputStream = inputStreamOpenInputStream;
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (fileOutputStream == null) {
                                throw th;
                            }
                            fileOutputStream.close();
                            throw th;
                        }
                    }
                } catch (FileNotFoundException e10) {
                    e = e10;
                    inputStream2 = inputStreamOpenInputStream;
                    fileOutputStream2 = null;
                    this.log.e(LOG_TAG, String.format(Locale.US, "File not found error copying file, uri: %s", uri), e);
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                    if (fileOutputStream2 != null) {
                        fileOutputStream2.close();
                    }
                } catch (IOException e11) {
                    e = e11;
                    inputStream = inputStreamOpenInputStream;
                    fileOutputStream = null;
                    this.log.e(LOG_TAG, String.format(Locale.US, "IO Error copying file, uri: %s", uri), e);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (Throwable th4) {
                    th = th4;
                    inputStream = inputStreamOpenInputStream;
                    fileOutputStream = null;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream == null) {
                        throw th;
                    }
                    fileOutputStream.close();
                    throw th;
                }
            } catch (FileNotFoundException e12) {
                e = e12;
                inputStream2 = null;
            } catch (IOException e13) {
                e = e13;
                inputStream = null;
            } catch (Throwable th5) {
                th = th5;
                inputStream = null;
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(List<BelvedereResult> list) {
        super.onPostExecute(list);
        BelvedereCallback<List<BelvedereResult>> belvedereCallback = this.callback;
        if (belvedereCallback != null) {
            belvedereCallback.internalSuccess(list);
        }
    }
}
