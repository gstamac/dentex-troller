package com.zendesk.belvedere;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import androidx.core.content.FileProvider;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class BelvedereStorage {
    private static final String ATTACHMENT_NAME = "attachment_%s";
    private static final String CAMERA_DATETIME_STRING_FORMAT = "yyyyMMddHHmmssSSS";
    private static final String CAMERA_IMAGE_DIR = "camera";
    private static final String CAMERA_IMG_NAME = "camera_image_%s";
    private static final String CAMERA_IMG_SUFFIX = ".jpg";
    private static final String GALLERY_IMAGE_DIR = "gallery";
    private static final String LOG_TAG = "BelvedereStorage";
    private static final String REQUEST_IMAGE_DIR = "request";
    private BelvedereConfig belvedereConfig;
    private BelvedereLogger log;

    @Retention(RetentionPolicy.SOURCE)
    public @interface IntentPermissions {
    }

    BelvedereStorage(BelvedereConfig belvedereConfig) {
        this.belvedereConfig = belvedereConfig;
        this.log = belvedereConfig.getBelvedereLogger();
    }

    void grantPermissionsForUri(Context context, Intent intent, Uri uri, int i) {
        Iterator<ResolveInfo> it = context.getPackageManager().queryIntentActivities(intent, 65536).iterator();
        while (it.hasNext()) {
            context.grantUriPermission(it.next().activityInfo.packageName, uri, i);
        }
    }

    void revokePermissionsFromUri(Context context, Uri uri, int i) {
        context.revokeUriPermission(uri, i);
    }

    Uri getFileProviderUri(Context context, File file) {
        String fileProviderAuthority = getFileProviderAuthority(context);
        try {
            return FileProvider.getUriForFile(context, fileProviderAuthority, file);
        } catch (IllegalArgumentException unused) {
            this.log.e(LOG_TAG, String.format(Locale.US, "The selected file can't be shared %s", file.toString()));
            return null;
        } catch (NullPointerException e) {
            String str = String.format(Locale.US, "=====================\nFileProvider failed to retrieve file uri. There might be an issue with the FileProvider \nPlease make sure that manifest-merger is working, and that you have defined the applicationId (package name) in the build.gradle\nManifest merger: http://tools.android.com/tech-docs/new-build-system/user-guide/manifest-merger\nIf your are not able to use gradle or the manifest merger, please add the following to your AndroidManifest.xml:\n        <provider\n            android:name=\"com.zendesk.belvedere.BelvedereFileProvider\"\n            android:authorities=\"${applicationId}${belvedereFileProviderAuthoritySuffix}\"\n            android:exported=\"false\"\n            android:grantUriPermissions=\"true\">\n            <meta-data\n                android:name=\"android.support.FILE_PROVIDER_PATHS\"\n                android:resource=\"@xml/belvedere_attachment_storage\" />\n        </provider>\n=====================", fileProviderAuthority);
            Log.e(LOG_TAG, str, e);
            this.log.e(LOG_TAG, str, e);
            return null;
        }
    }

    String getFileProviderAuthority(Context context) {
        return String.format(Locale.US, "%s%s", context.getPackageName(), context.getString(R.string.belvedere_sdk_fpa_suffix));
    }

    File getFileForCamera(Context context) {
        File attachmentDir = getAttachmentDir(context, CAMERA_IMAGE_DIR);
        if (attachmentDir == null) {
            this.log.w(LOG_TAG, "Error creating cache directory");
            return null;
        }
        return createTempFile(String.format(Locale.US, CAMERA_IMG_NAME, new SimpleDateFormat(CAMERA_DATETIME_STRING_FORMAT, Locale.US).format(new Date(System.currentTimeMillis()))), CAMERA_IMG_SUFFIX, attachmentDir);
    }

    File getTempFileForGalleryImage(Context context, Uri uri) {
        File attachmentDir = getAttachmentDir(context, GALLERY_IMAGE_DIR);
        String extension = null;
        if (attachmentDir == null) {
            this.log.w(LOG_TAG, "Error creating cache directory");
            return null;
        }
        String fileNameFromUri = getFileNameFromUri(context, uri);
        if (TextUtils.isEmpty(fileNameFromUri)) {
            fileNameFromUri = String.format(Locale.US, ATTACHMENT_NAME, new SimpleDateFormat(CAMERA_DATETIME_STRING_FORMAT, Locale.US).format(new Date(System.currentTimeMillis())));
            extension = getExtension(context, uri);
        }
        return createTempFile(fileNameFromUri, extension, attachmentDir);
    }

    File getTempFileForRequestAttachment(Context context, String str) {
        File attachmentDir = getAttachmentDir(context, "request");
        if (attachmentDir == null) {
            this.log.w(LOG_TAG, "Error creating cache directory");
            return null;
        }
        return createTempFile(str, null, attachmentDir);
    }

    void clearStorage(Context context) {
        File file = new File(getRootDir(context) + File.separator + this.belvedereConfig.getDirectoryName());
        if (file.isDirectory()) {
            clearDirectory(file);
        }
    }

    private void clearDirectory(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                clearDirectory(file2);
            }
        }
        file.delete();
    }

    private File createTempFile(String str, String str2, File file) {
        StringBuilder sbAppend = new StringBuilder().append(str);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        return new File(file, sbAppend.append(str2).toString());
    }

    private File getAttachmentDir(Context context, String str) {
        File file = new File(getRootDir(context) + File.separator + this.belvedereConfig.getDirectoryName() + File.separator + (!TextUtils.isEmpty(str) ? str + File.separator : ""));
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        if (file.isDirectory()) {
            return file;
        }
        return null;
    }

    private String getRootDir(Context context) {
        return context.getCacheDir().getAbsolutePath();
    }

    private String getExtension(Context context, Uri uri) {
        String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
        Locale locale = Locale.US;
        Object[] objArr = new Object[1];
        if (TextUtils.isEmpty(extensionFromMimeType)) {
            extensionFromMimeType = "tmp";
        }
        objArr[0] = extensionFromMimeType;
        return String.format(locale, ".%s", objArr);
    }

    private String getFileNameFromUri(Context context, Uri uri) {
        Cursor cursorQuery = context.getContentResolver().query(uri, new String[]{"_display_name"}, null, null, null);
        String string = "";
        if (cursorQuery != null) {
            try {
                string = cursorQuery.moveToFirst() ? cursorQuery.getString(0) : "";
            } finally {
                cursorQuery.close();
            }
        }
        return string;
    }
}
