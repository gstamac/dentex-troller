package com.zendesk.sdk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.zendesk.logger.Logger;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskRequestStorage implements RequestStorage {
    private static final String LOG_TAG = "ZendeskRequestStorage";
    private static final String PREFS_COMMENT_COUNT_KEY_PREFIX = "request-id-cc";
    private static final String PREFS_NAME = "zendesk-authorization";
    private static final String REQUEST_KEY = "stored_requests";
    private final SharedPreferences storage;

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public String getCacheKey() {
        return PREFS_NAME;
    }

    ZendeskRequestStorage(Context context) {
        SharedPreferences sharedPreferences = context == null ? null : context.getSharedPreferences(PREFS_NAME, 0);
        this.storage = sharedPreferences;
        if (sharedPreferences == null) {
            Logger.w(LOG_TAG, "Storage was not initialised, requests will not be stored.", new Object[0]);
        }
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public List<String> getStoredRequestIds() {
        ArrayList arrayList = new ArrayList();
        SharedPreferences sharedPreferences = this.storage;
        if (sharedPreferences != null) {
            String string = sharedPreferences.getString(REQUEST_KEY, "");
            if (StringUtils.hasLength(string)) {
                arrayList.addAll(StringUtils.fromCsv(string));
            }
        }
        return arrayList;
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public void storeRequestId(String str) {
        if (this.storage == null || !StringUtils.hasLength(str)) {
            return;
        }
        List<String> storedRequestIds = getStoredRequestIds();
        if (!storedRequestIds.contains(str)) {
            storedRequestIds.add(str);
            this.storage.edit().putString(REQUEST_KEY, StringUtils.toCsvString(storedRequestIds)).apply();
        }
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public void setCommentCount(String str, int i) {
        if (!StringUtils.hasLength(str) || i < 0) {
            Logger.d(LOG_TAG, "Invalid requestId or commentCount provided", new Object[0]);
        } else {
            this.storage.edit().putInt(getCommentCountKey(str), i).apply();
        }
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public Integer getCommentCount(String str) {
        if (!StringUtils.hasLength(str)) {
            Logger.d(LOG_TAG, "Invalid requestId provided", new Object[0]);
            return null;
        }
        int i = this.storage.getInt(getCommentCountKey(str), -1);
        if (i != -1) {
            return Integer.valueOf(i);
        }
        return null;
    }

    private String getCommentCountKey(String str) {
        return String.format(Locale.US, "%s-%s", PREFS_COMMENT_COUNT_KEY_PREFIX, str);
    }

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public void clearUserData() {
        SharedPreferences sharedPreferences = this.storage;
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
    }
}
