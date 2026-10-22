package com.zendesk.belvedere;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.FragmentManager;
import java.io.File;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class Belvedere {
    private static final String LOG_TAG = "Belvedere";
    private final BelvedereStorage belvedereStorage;
    private final Context context;
    private final BelvedereImagePicker imagePicker;
    private final BelvedereLogger log;

    Belvedere(Context context, BelvedereConfig belvedereConfig) {
        this.context = context;
        BelvedereStorage belvedereStorage = new BelvedereStorage(belvedereConfig);
        this.belvedereStorage = belvedereStorage;
        this.imagePicker = new BelvedereImagePicker(belvedereConfig, belvedereStorage);
        BelvedereLogger belvedereLogger = belvedereConfig.getBelvedereLogger();
        this.log = belvedereLogger;
        belvedereLogger.d(LOG_TAG, "Belvedere initialized");
    }

    public static BelvedereConfig.Builder from(Context context) {
        if (context != null && context.getApplicationContext() != null) {
            return new BelvedereConfig.Builder(context.getApplicationContext());
        }
        throw new IllegalArgumentException("Invalid context provided");
    }

    public List<BelvedereIntent> getBelvedereIntents() {
        return this.imagePicker.getBelvedereIntents(this.context);
    }

    public void showDialog(FragmentManager fragmentManager) {
        BelvedereDialog.showDialog(fragmentManager, getBelvedereIntents());
    }

    public void getFilesFromActivityOnResult(int i, int i2, Intent intent, BelvedereCallback<List<BelvedereResult>> belvedereCallback) {
        this.imagePicker.getFilesFromActivityOnResult(this.context, i, i2, intent, belvedereCallback);
    }

    public BelvedereResult getFileRepresentation(String str) {
        Uri fileProviderUri;
        File tempFileForRequestAttachment = this.belvedereStorage.getTempFileForRequestAttachment(this.context, str);
        this.log.d(LOG_TAG, String.format(Locale.US, "Get internal File: %s", tempFileForRequestAttachment));
        if (tempFileForRequestAttachment == null || (fileProviderUri = this.belvedereStorage.getFileProviderUri(this.context, tempFileForRequestAttachment)) == null) {
            return null;
        }
        return new BelvedereResult(tempFileForRequestAttachment, fileProviderUri);
    }

    public void grantPermissionsForUri(Intent intent, Uri uri) {
        this.log.d(LOG_TAG, String.format(Locale.US, "Grant Permission - Intent: %s - Uri: %s", intent, uri));
        this.belvedereStorage.grantPermissionsForUri(this.context, intent, uri, 3);
    }

    public void revokePermissionsForUri(Uri uri) {
        this.log.d(LOG_TAG, String.format(Locale.US, "Revoke Permission - Uri: %s", uri));
        this.belvedereStorage.revokePermissionsFromUri(this.context, uri, 3);
    }

    public boolean oneOrMoreSourceAvailable() {
        return this.imagePicker.oneOrMoreSourceAvailable(this.context);
    }

    public boolean isFunctionalityAvailable(BelvedereSource belvedereSource) {
        return this.imagePicker.isFunctionalityAvailable(belvedereSource, this.context);
    }

    public void clear() {
        this.log.d(LOG_TAG, "Clear Belvedere cache");
        this.belvedereStorage.clearStorage(this.context);
    }
}
