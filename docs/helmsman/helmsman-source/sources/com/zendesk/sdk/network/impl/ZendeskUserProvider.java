package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.SdkConfiguration;
import com.zendesk.sdk.model.request.User;
import com.zendesk.sdk.model.request.UserField;
import com.zendesk.sdk.model.request.UserFieldRequest;
import com.zendesk.sdk.model.request.UserFieldResponse;
import com.zendesk.sdk.model.request.UserResponse;
import com.zendesk.sdk.model.request.UserTagRequest;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.UserProvider;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskUserProvider implements UserProvider {
    private final BaseProvider baseProvider;
    private final ZendeskUserService userService;

    ZendeskUserProvider(BaseProvider baseProvider, ZendeskUserService zendeskUserService) {
        this.baseProvider = baseProvider;
        this.userService = zendeskUserService;
    }

    @Override // com.zendesk.sdk.network.UserProvider
    public void addTags(final List<String> list, final ZendeskCallback<List<String>> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.1
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                UserTagRequest userTagRequest = new UserTagRequest();
                userTagRequest.setTags(CollectionUtils.ensureEmpty(list));
                ZendeskUserProvider.this.userService.addTags(sdkConfiguration.getBearerAuthorizationHeader(), userTagRequest, new PassThroughErrorZendeskCallback<UserResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.1.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(UserResponse userResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(ZendeskUserProvider.this.getTags(userResponse));
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.UserProvider
    public void deleteTags(final List<String> list, final ZendeskCallback<List<String>> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.2
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskUserProvider.this.userService.deleteTags(sdkConfiguration.getBearerAuthorizationHeader(), StringUtils.toCsvString((List<String>) CollectionUtils.ensureEmpty(list)), new PassThroughErrorZendeskCallback<UserResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.2.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(UserResponse userResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(ZendeskUserProvider.this.getTags(userResponse));
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.UserProvider
    public void getUserFields(final ZendeskCallback<List<UserField>> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.3
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskUserProvider.this.userService.getUserFields(sdkConfiguration.getBearerAuthorizationHeader(), new PassThroughErrorZendeskCallback<UserFieldResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.3.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(UserFieldResponse userFieldResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(userFieldResponse.getUserFields());
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.UserProvider
    public void setUserFields(final Map<String, String> map, final ZendeskCallback<Map<String, String>> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.4
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskUserProvider.this.userService.setUserFields(sdkConfiguration.getBearerAuthorizationHeader(), new UserFieldRequest(map), new PassThroughErrorZendeskCallback<UserResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.4.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(UserResponse userResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(ZendeskUserProvider.this.getUserFields(userResponse));
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.UserProvider
    public void getUser(final ZendeskCallback<User> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.5
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskUserProvider.this.userService.getUser(sdkConfiguration.getBearerAuthorizationHeader(), new PassThroughErrorZendeskCallback<UserResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUserProvider.5.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(UserResponse userResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(userResponse.getUser());
                        }
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> getTags(UserResponse userResponse) {
        return (userResponse == null || userResponse.getUser() == null) ? new ArrayList() : userResponse.getUser().getTags();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, String> getUserFields(UserResponse userResponse) {
        return (userResponse == null || userResponse.getUser() == null) ? new HashMap() : userResponse.getUser().getUserFields();
    }
}
