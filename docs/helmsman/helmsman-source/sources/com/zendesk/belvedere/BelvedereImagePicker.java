package com.zendesk.belvedere;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes2.dex */
class BelvedereImagePicker {
    private static final String LOG_TAG = "BelvedereImagePicker";
    private final BelvedereConfig belvedereConfig;
    private final BelvedereStorage belvedereStorage;
    private final Map<Integer, BelvedereResult> cameraImages = new HashMap();
    private final BelvedereLogger log;

    BelvedereImagePicker(BelvedereConfig belvedereConfig, BelvedereStorage belvedereStorage) {
        this.belvedereConfig = belvedereConfig;
        this.belvedereStorage = belvedereStorage;
        this.log = belvedereConfig.getBelvedereLogger();
    }

    List<BelvedereIntent> getBelvedereIntents(Context context) {
        BelvedereIntent galleryIntent;
        TreeSet<BelvedereSource> belvedereSources = this.belvedereConfig.getBelvedereSources();
        ArrayList arrayList = new ArrayList();
        Iterator<BelvedereSource> it = belvedereSources.iterator();
        while (it.hasNext()) {
            int i = AnonymousClass1.$SwitchMap$com$zendesk$belvedere$BelvedereSource[it.next().ordinal()];
            if (i == 1) {
                galleryIntent = getGalleryIntent(context);
            } else {
                galleryIntent = i != 2 ? null : getCameraIntent(context);
            }
            if (galleryIntent != null) {
                arrayList.add(galleryIntent);
            }
        }
        return arrayList;
    }

    /* JADX INFO: renamed from: com.zendesk.belvedere.BelvedereImagePicker$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$belvedere$BelvedereSource;

        static {
            int[] iArr = new int[BelvedereSource.values().length];
            $SwitchMap$com$zendesk$belvedere$BelvedereSource = iArr;
            try {
                iArr[BelvedereSource.Gallery.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$belvedere$BelvedereSource[BelvedereSource.Camera.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    BelvedereIntent getGalleryIntent(Context context) {
        if (hasGalleryApp(context)) {
            return new BelvedereIntent(getGalleryIntent(), this.belvedereConfig.getGalleryRequestCode(), BelvedereSource.Gallery);
        }
        return null;
    }

    private BelvedereIntent getCameraIntent(Context context) {
        if (canPickImageFromCamera(context)) {
            return pickImageFromCameraInternal(context);
        }
        return null;
    }

    private boolean canPickImageFromCamera(Context context) {
        if (hasCamera(context)) {
            if (!hasPermissionInManifest(context, "android.permission.CAMERA")) {
                return true;
            }
            if (ContextCompat.checkSelfPermission(context, "android.permission.CAMERA") == 0) {
                return true;
            }
            this.log.w(LOG_TAG, "Found Camera permission declared in AndroidManifest.xml and the user hasn't granted that permission. Not doing any further efforts to acquire that permission.");
        }
        return false;
    }

    private boolean hasCamera(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        PackageManager packageManager = context.getPackageManager();
        boolean z = packageManager.hasSystemFeature("android.hardware.camera") || packageManager.hasSystemFeature("android.hardware.camera.front");
        boolean zIsIntentResolvable = isIntentResolvable(intent, context);
        this.log.d(LOG_TAG, String.format(Locale.US, "Camera present: %b, Camera App present: %b", Boolean.valueOf(z), Boolean.valueOf(zIsIntentResolvable)));
        return z && zIsIntentResolvable;
    }

    private boolean hasGalleryApp(Context context) {
        return isIntentResolvable(getGalleryIntent(), context);
    }

    void getFilesFromActivityOnResult(Context context, int i, int i2, Intent intent, BelvedereCallback<List<BelvedereResult>> belvedereCallback) {
        ArrayList arrayList = new ArrayList();
        if (i == this.belvedereConfig.getGalleryRequestCode()) {
            BelvedereLogger belvedereLogger = this.log;
            Locale locale = Locale.US;
            Object[] objArr = new Object[1];
            objArr[0] = Boolean.valueOf(i2 == -1);
            belvedereLogger.d(LOG_TAG, String.format(locale, "Parsing activity result - Gallery - Ok: %s", objArr));
            if (i2 == -1) {
                List<Uri> listExtractUrisFromIntent = extractUrisFromIntent(intent);
                this.log.d(LOG_TAG, String.format(Locale.US, "Number of items received from gallery: %s", Integer.valueOf(listExtractUrisFromIntent.size())));
                new BelvedereResolveUriTask(context, this.log, this.belvedereStorage, belvedereCallback).execute(listExtractUrisFromIntent.toArray(new Uri[listExtractUrisFromIntent.size()]));
                return;
            }
        } else if (this.cameraImages.containsKey(Integer.valueOf(i))) {
            BelvedereLogger belvedereLogger2 = this.log;
            Locale locale2 = Locale.US;
            Object[] objArr2 = new Object[1];
            objArr2[0] = Boolean.valueOf(i2 == -1);
            belvedereLogger2.d(LOG_TAG, String.format(locale2, "Parsing activity result - Camera - Ok: %s", objArr2));
            BelvedereResult belvedereResult = this.cameraImages.get(Integer.valueOf(i));
            this.belvedereStorage.revokePermissionsFromUri(context, belvedereResult.getUri(), 3);
            if (i2 == -1) {
                arrayList.add(belvedereResult);
                this.log.d(LOG_TAG, String.format(Locale.US, "Image from camera: %s", belvedereResult.getFile()));
            }
            this.cameraImages.remove(Integer.valueOf(i));
        }
        if (belvedereCallback != null) {
            belvedereCallback.internalSuccess(arrayList);
        }
    }

    public boolean oneOrMoreSourceAvailable(Context context) {
        for (BelvedereSource belvedereSource : BelvedereSource.values()) {
            if (isFunctionalityAvailable(belvedereSource, context)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFunctionalityAvailable(BelvedereSource belvedereSource, Context context) {
        if (!this.belvedereConfig.getBelvedereSources().contains(belvedereSource)) {
            return false;
        }
        int i = AnonymousClass1.$SwitchMap$com$zendesk$belvedere$BelvedereSource[belvedereSource.ordinal()];
        if (i == 1) {
            return hasGalleryApp(context);
        }
        if (i != 2) {
            return false;
        }
        return canPickImageFromCamera(context);
    }

    private boolean isIntentResolvable(Intent intent, Context context) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    private List<Uri> extractUrisFromIntent(Intent intent) {
        ArrayList arrayList = new ArrayList();
        if (intent.getClipData() != null) {
            ClipData clipData = intent.getClipData();
            int itemCount = clipData.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                ClipData.Item itemAt = clipData.getItemAt(i);
                if (itemAt.getUri() != null) {
                    arrayList.add(itemAt.getUri());
                }
            }
        } else if (intent.getData() != null) {
            arrayList.add(intent.getData());
        }
        return arrayList;
    }

    private BelvedereIntent pickImageFromCameraInternal(Context context) {
        Set<Integer> setKeySet = this.cameraImages.keySet();
        int cameraRequestCodeEnd = this.belvedereConfig.getCameraRequestCodeEnd();
        for (int cameraRequestCodeStart = this.belvedereConfig.getCameraRequestCodeStart(); cameraRequestCodeStart < this.belvedereConfig.getCameraRequestCodeEnd(); cameraRequestCodeStart++) {
            if (!setKeySet.contains(Integer.valueOf(cameraRequestCodeStart))) {
                cameraRequestCodeEnd = cameraRequestCodeStart;
                break;
            }
        }
        File fileForCamera = this.belvedereStorage.getFileForCamera(context);
        if (fileForCamera == null) {
            this.log.w(LOG_TAG, "Camera Intent. Image path is null. There's something wrong with the storage.");
            return null;
        }
        Uri fileProviderUri = this.belvedereStorage.getFileProviderUri(context, fileForCamera);
        if (fileProviderUri == null) {
            this.log.w(LOG_TAG, "Camera Intent: Uri to file is null. There's something wrong with the storage or FileProvider configuration.");
            return null;
        }
        this.cameraImages.put(Integer.valueOf(cameraRequestCodeEnd), new BelvedereResult(fileForCamera, fileProviderUri));
        this.log.d(LOG_TAG, String.format(Locale.US, "Camera Intent: Request Id: %s - File: %s - Uri: %s", Integer.valueOf(cameraRequestCodeEnd), fileForCamera, fileProviderUri));
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", fileProviderUri);
        this.belvedereStorage.grantPermissionsForUri(context, intent, fileProviderUri, 3);
        return new BelvedereIntent(intent, cameraRequestCodeEnd, BelvedereSource.Camera);
    }

    private Intent getGalleryIntent() {
        this.log.d(LOG_TAG, "Gallery Intent, using 'ACTION_OPEN_DOCUMENT'");
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.setType(this.belvedereConfig.getContentType());
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", this.belvedereConfig.allowMultiple());
        return intent;
    }

    private boolean hasPermissionInManifest(Context context, String str) {
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null && strArr.length > 0) {
                for (String str2 : strArr) {
                    if (str2.equals(str)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            this.log.e(LOG_TAG, "Not able to find permissions in manifest", e);
        }
        return false;
    }
}
