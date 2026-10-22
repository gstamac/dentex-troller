package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.network.AccessService;
import com.zendesk.sdk.network.HelpCenterService;
import com.zendesk.sdk.network.PushRegistrationService;
import com.zendesk.sdk.network.RequestService;
import com.zendesk.sdk.network.SdkSettingsService;
import com.zendesk.sdk.network.UploadService;
import com.zendesk.sdk.network.UserService;

/* JADX INFO: loaded from: classes2.dex */
class ServiceInjector {
    ServiceInjector() {
    }

    private static AccessService injectAccessService(ApplicationScope applicationScope) {
        return (AccessService) RestAdapterInjector.injectCachedRestAdapter(applicationScope).create(AccessService.class);
    }

    static ZendeskAccessService injectZendeskAccessService(ApplicationScope applicationScope) {
        return new ZendeskAccessService(injectAccessService(applicationScope));
    }

    private static HelpCenterService injectHelpCenterService(ApplicationScope applicationScope) {
        return (HelpCenterService) RestAdapterInjector.injectCachedRestAdapter(applicationScope).create(HelpCenterService.class);
    }

    static ZendeskHelpCenterService injectZendeskHelpCenterService(ApplicationScope applicationScope) {
        return new ZendeskHelpCenterService(injectHelpCenterService(applicationScope));
    }

    private static PushRegistrationService injectPushRegistrationService(ApplicationScope applicationScope) {
        return (PushRegistrationService) RestAdapterInjector.injectCachedRestAdapter(applicationScope).create(PushRegistrationService.class);
    }

    static ZendeskPushRegistrationService injectZendeskPushRegistrationService(ApplicationScope applicationScope) {
        return new ZendeskPushRegistrationService(injectPushRegistrationService(applicationScope));
    }

    private static RequestService injectRequestService(ApplicationScope applicationScope) {
        return (RequestService) RestAdapterInjector.injectCachedRestAdapter(applicationScope).create(RequestService.class);
    }

    static ZendeskRequestService injectZendeskRequestService(ApplicationScope applicationScope) {
        return new ZendeskRequestService(injectRequestService(applicationScope));
    }

    private static SdkSettingsService injectSdkSettingsService(ApplicationScope applicationScope) {
        return (SdkSettingsService) RestAdapterInjector.injectCachedRestAdapter(applicationScope).create(SdkSettingsService.class);
    }

    static ZendeskSdkSettingsService injectZendeskSdkSettingsService(ApplicationScope applicationScope) {
        return new ZendeskSdkSettingsService(injectSdkSettingsService(applicationScope));
    }

    private static UploadService injectUploadService(ApplicationScope applicationScope) {
        return (UploadService) RestAdapterInjector.injectCachedRestAdapter(applicationScope).create(UploadService.class);
    }

    static ZendeskUploadService injectZendeskUploadService(ApplicationScope applicationScope) {
        return new ZendeskUploadService(injectUploadService(applicationScope));
    }

    private static UserService injectUserService(ApplicationScope applicationScope) {
        return (UserService) RestAdapterInjector.injectCachedRestAdapter(applicationScope).create(UserService.class);
    }

    static ZendeskUserService injectZendeskUserService(ApplicationScope applicationScope) {
        return new ZendeskUserService(injectUserService(applicationScope));
    }
}
