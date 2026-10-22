package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.SdkConfiguration;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.AuthenticationType;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.push.AnonymousPushRegistrationRequest;
import com.zendesk.sdk.model.push.JwtPushRegistrationRequest;
import com.zendesk.sdk.model.push.PushRegistrationRequest;
import com.zendesk.sdk.model.push.PushRegistrationRequestWrapper;
import com.zendesk.sdk.model.push.PushRegistrationResponse;
import com.zendesk.sdk.model.push.PushRegistrationResponseWrapper;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.PushRegistrationProvider;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.LocaleUtil;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskPushRegistrationProvider implements PushRegistrationProvider {
    private final BaseProvider baseProvider;
    private final Identity identity;
    private final ZendeskPushRegistrationService pushService;

    ZendeskPushRegistrationProvider(BaseProvider baseProvider, ZendeskPushRegistrationService zendeskPushRegistrationService, Identity identity) {
        this.baseProvider = baseProvider;
        this.pushService = zendeskPushRegistrationService;
        this.identity = identity;
    }

    @Override // com.zendesk.sdk.network.PushRegistrationProvider
    public void registerDeviceWithIdentifier(final String str, final Locale locale, final ZendeskCallback<PushRegistrationResponse> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskPushRegistrationProvider.1
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                AuthenticationType authenticationType = sdkConfiguration.getMobileSettings().getAuthenticationType();
                if (authenticationType == null) {
                    ZendeskCallback zendeskCallback2 = zendeskCallback;
                    if (zendeskCallback2 != null) {
                        zendeskCallback2.onError(new ErrorResponseAdapter("Authentication type is null."));
                        return;
                    }
                    return;
                }
                ZendeskPushRegistrationProvider.this.internalRegister(sdkConfiguration.getBearerAuthorizationHeader(), ZendeskPushRegistrationProvider.this.getPushRegistrationRequest(str, locale, authenticationType, TokenType.Identifier), zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.PushRegistrationProvider
    public void registerDeviceWithUAChannelId(final String str, final Locale locale, final ZendeskCallback<PushRegistrationResponse> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskPushRegistrationProvider.2
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                AuthenticationType authenticationType = sdkConfiguration.getMobileSettings().getAuthenticationType();
                if (authenticationType == null) {
                    ZendeskCallback zendeskCallback2 = zendeskCallback;
                    if (zendeskCallback2 != null) {
                        zendeskCallback2.onError(new ErrorResponseAdapter("Authentication type is null."));
                        return;
                    }
                    return;
                }
                ZendeskPushRegistrationProvider.this.internalRegister(sdkConfiguration.getBearerAuthorizationHeader(), ZendeskPushRegistrationProvider.this.getPushRegistrationRequest(str, locale, authenticationType, TokenType.UrbanAirshipChannelId), zendeskCallback);
            }
        });
    }

    @Override // com.zendesk.sdk.network.PushRegistrationProvider
    public void unregisterDevice(final String str, final ZendeskCallback<Void> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskPushRegistrationProvider.3
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskPushRegistrationProvider.this.pushService.unregisterDevice(sdkConfiguration.getBearerAuthorizationHeader(), str, zendeskCallback);
            }
        });
    }

    void internalRegister(String str, PushRegistrationRequest pushRegistrationRequest, final ZendeskCallback<PushRegistrationResponse> zendeskCallback) {
        PushRegistrationRequestWrapper pushRegistrationRequestWrapper = new PushRegistrationRequestWrapper();
        pushRegistrationRequestWrapper.setPushRegistrationRequest(pushRegistrationRequest);
        this.pushService.registerDevice(str, pushRegistrationRequestWrapper, new PassThroughErrorZendeskCallback<PushRegistrationResponseWrapper>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskPushRegistrationProvider.4
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(PushRegistrationResponseWrapper pushRegistrationResponseWrapper) {
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onSuccess(pushRegistrationResponseWrapper.getRegistrationResponse());
                }
            }
        });
    }

    /* JADX INFO: renamed from: com.zendesk.sdk.network.impl.ZendeskPushRegistrationProvider$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$sdk$model$access$AuthenticationType;

        static {
            int[] iArr = new int[AuthenticationType.values().length];
            $SwitchMap$com$zendesk$sdk$model$access$AuthenticationType = iArr;
            try {
                iArr[AuthenticationType.JWT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$model$access$AuthenticationType[AuthenticationType.ANONYMOUS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    PushRegistrationRequest getPushRegistrationRequest(String str, Locale locale, AuthenticationType authenticationType, TokenType tokenType) {
        int i = AnonymousClass5.$SwitchMap$com$zendesk$sdk$model$access$AuthenticationType[authenticationType.ordinal()];
        if (i == 1) {
            return decoratePushRegistrationRequest(str, locale, tokenType, new JwtPushRegistrationRequest());
        }
        if (i != 2) {
            return null;
        }
        AnonymousPushRegistrationRequest anonymousPushRegistrationRequest = (AnonymousPushRegistrationRequest) decoratePushRegistrationRequest(str, locale, tokenType, new AnonymousPushRegistrationRequest());
        Identity identity = this.identity;
        AnonymousIdentity anonymousIdentity = (AnonymousIdentity) identity;
        if (identity != null) {
            anonymousPushRegistrationRequest.setSdkGuid(anonymousIdentity.getSdkGuid());
        }
        return anonymousPushRegistrationRequest;
    }

    private <E extends PushRegistrationRequest> E decoratePushRegistrationRequest(String str, Locale locale, TokenType tokenType, E e) {
        e.setIdentifier(str);
        e.setLocale(LocaleUtil.toLanguageTag(locale));
        if (tokenType == TokenType.UrbanAirshipChannelId) {
            e.setTokenType(tokenType.name);
        }
        return e;
    }

    enum TokenType {
        Identifier(null),
        UrbanAirshipChannelId("urban_airship_channel_id");

        final String name;

        TokenType(String str) {
            this.name = str;
        }

        String getName() {
            return this.name;
        }
    }
}
