package com.zendesk.sdk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.AuthenticationType;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.access.JwtIdentity;
import com.zendesk.util.StringUtils;
import java.util.Locale;
import java.util.UUID;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskIdentityStorage implements IdentityStorage {
    private static final String IDENTITY_KEY = "zendesk-identity";
    private static final String IDENTITY_TYPE_KEY = "zendesk-identity-type";
    private static final String LOG_TAG = "ZendeskIdentityStorage";
    private static final String PREFS_NAME = "zendesk-token";
    private static final String TOKEN_KEY = "stored_token";
    private static final String UUID_KEY = "uuid";
    private final Gson gson;
    private final SharedPreferences storage;

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public String getCacheKey() {
        return PREFS_NAME;
    }

    ZendeskIdentityStorage(Context context, Gson gson) {
        SharedPreferences sharedPreferences = context == null ? null : context.getSharedPreferences(PREFS_NAME, 0);
        this.storage = sharedPreferences;
        if (sharedPreferences == null) {
            Logger.w(LOG_TAG, "Storage was not initialised, user token will not be stored.", new Object[0]);
        }
        this.gson = gson;
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public void storeAccessToken(AccessToken accessToken) {
        if (this.storage == null || accessToken == null) {
            return;
        }
        this.storage.edit().putString(TOKEN_KEY, this.gson.toJson(accessToken)).apply();
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public AccessToken getStoredAccessToken() {
        SharedPreferences sharedPreferences = this.storage;
        if (sharedPreferences == null) {
            return null;
        }
        String string = sharedPreferences.getString(TOKEN_KEY, null);
        if (StringUtils.hasLength(string)) {
            return (AccessToken) this.gson.fromJson(string, AccessToken.class);
        }
        return null;
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public String getStoredAccessTokenAsBearerToken() {
        AccessToken storedAccessToken = getStoredAccessToken();
        if (storedAccessToken == null) {
            Logger.w(LOG_TAG, "There is no stored access token, have you initialised an identity and requested an access token?", new Object[0]);
        }
        if (storedAccessToken == null) {
            return null;
        }
        return String.format(Locale.US, "Bearer %s", storedAccessToken.getAccessToken());
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public String getUUID() {
        SharedPreferences sharedPreferences = this.storage;
        if (sharedPreferences != null && sharedPreferences.contains(UUID_KEY)) {
            Logger.d(LOG_TAG, "Fetching UUID from preferences store", new Object[0]);
            return this.storage.getString(UUID_KEY, "");
        }
        String str = LOG_TAG;
        Logger.d(str, "Generating new UUID", new Object[0]);
        String string = UUID.randomUUID().toString();
        if (this.storage != null) {
            Logger.d(str, "Storing new UUID in preference store", new Object[0]);
            this.storage.edit().putString(UUID_KEY, string).apply();
        }
        return string;
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public void storeIdentity(Identity identity) {
        String sdkGuid;
        String json;
        String authenticationType;
        if (this.storage == null) {
            Logger.e(LOG_TAG, "Storage is not initialised, will not store the identity", new Object[0]);
            return;
        }
        if (identity == null) {
            Logger.e(LOG_TAG, "identity is null, will not store the identity", new Object[0]);
            return;
        }
        if (identity instanceof AnonymousIdentity) {
            Logger.d(LOG_TAG, "Storing anonymous identity", new Object[0]);
            json = this.gson.toJson(identity, AnonymousIdentity.class);
            authenticationType = AuthenticationType.ANONYMOUS.getAuthenticationType();
            sdkGuid = ((AnonymousIdentity) identity).getSdkGuid();
        } else if (identity instanceof JwtIdentity) {
            Logger.d(LOG_TAG, "Storing jwt identity", new Object[0]);
            json = this.gson.toJson(identity, JwtIdentity.class);
            authenticationType = AuthenticationType.JWT.getAuthenticationType();
            sdkGuid = null;
        } else {
            Logger.e(LOG_TAG, "Unknown authentication type, identity will not be stored", new Object[0]);
            sdkGuid = null;
            json = null;
            authenticationType = null;
        }
        if (!StringUtils.hasLength(json) || authenticationType == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.storage.edit();
        editorEdit.putString(IDENTITY_KEY, json).putString(IDENTITY_TYPE_KEY, authenticationType);
        if (StringUtils.hasLength(sdkGuid)) {
            editorEdit.putString(UUID_KEY, sdkGuid);
        }
        editorEdit.apply();
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public Identity getIdentity() {
        SharedPreferences sharedPreferences = this.storage;
        if (sharedPreferences == null) {
            return null;
        }
        String string = sharedPreferences.getString(IDENTITY_KEY, null);
        String string2 = this.storage.getString(IDENTITY_TYPE_KEY, null);
        if (!StringUtils.hasLength(string) || !StringUtils.hasLength(string2)) {
            return null;
        }
        AuthenticationType authType = AuthenticationType.getAuthType(string2);
        if (AuthenticationType.JWT == authType) {
            Logger.d(LOG_TAG, "Loading Jwt identity", new Object[0]);
            return (Identity) this.gson.fromJson(string, JwtIdentity.class);
        }
        if (AuthenticationType.ANONYMOUS == authType) {
            Logger.d(LOG_TAG, "Loading Anonymous identity", new Object[0]);
            return (Identity) this.gson.fromJson(string, AnonymousIdentity.class);
        }
        Logger.e(LOG_TAG, "Unknown identity type, identity will be null", new Object[0]);
        return null;
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public Identity anonymiseIdentity() {
        if (this.storage == null) {
            Logger.e(LOG_TAG, "Cannot anonymise identity as the storage is null", new Object[0]);
            return null;
        }
        Identity identity = getIdentity();
        if (identity == null) {
            Logger.w(LOG_TAG, "Cannot anonymise a null identity.", new Object[0]);
            return identity;
        }
        if (identity instanceof AnonymousIdentity) {
            AnonymousIdentity anonymousIdentity = (AnonymousIdentity) identity;
            if (!(StringUtils.hasLength(anonymousIdentity.getEmail()) || StringUtils.hasLength(anonymousIdentity.getName()))) {
                return identity;
            }
            Identity identityBuild = new AnonymousIdentity.Builder().withExternalIdentifier(anonymousIdentity.getExternalId()).build();
            storeIdentity(identityBuild);
            return identityBuild;
        }
        Logger.d(LOG_TAG, "Identity is not anonymous, no anonymisation is required", new Object[0]);
        return identity;
    }

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public void clearUserData() {
        SharedPreferences sharedPreferences = this.storage;
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
    }
}
