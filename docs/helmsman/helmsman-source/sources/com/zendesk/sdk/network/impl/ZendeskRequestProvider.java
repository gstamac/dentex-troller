package com.zendesk.sdk.network.impl;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.SdkConfiguration;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.AuthenticationType;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.request.Comment;
import com.zendesk.sdk.model.request.CommentsResponse;
import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.sdk.model.request.EndUserComment;
import com.zendesk.sdk.model.request.Request;
import com.zendesk.sdk.model.request.RequestResponse;
import com.zendesk.sdk.model.request.fields.RawTicketField;
import com.zendesk.sdk.model.request.fields.RawTicketForm;
import com.zendesk.sdk.model.request.fields.RawTicketFormResponse;
import com.zendesk.sdk.model.request.fields.TicketField;
import com.zendesk.sdk.model.request.fields.TicketForm;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.RequestProvider;
import com.zendesk.sdk.storage.RequestSessionCache;
import com.zendesk.sdk.storage.RequestStorage;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.LocaleUtil;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
final class ZendeskRequestProvider implements RequestProvider {
    private static final String ALL_REQUEST_STATUSES = "new,open,pending,hold,solved,closed";
    private static final String LOG_TAG = "ZendeskRequestProvider";
    private static final int MAX_TICKET_FIELDS = 5;
    private static final String SIDE_LOAD_PUBLIC_UPDATED_AT = "public_updated_at";
    private final BaseProvider baseProvider;
    private final Identity identity;
    private final Locale locale;
    private final ZendeskRequestService requestService;
    private final RequestSessionCache requestSessionCache;
    private final RequestStorage requestStorage;

    ZendeskRequestProvider(BaseProvider baseProvider, ZendeskRequestService zendeskRequestService, Identity identity, RequestStorage requestStorage, RequestSessionCache requestSessionCache, Locale locale) {
        this.baseProvider = baseProvider;
        this.requestService = zendeskRequestService;
        this.identity = identity;
        this.requestStorage = requestStorage;
        this.requestSessionCache = requestSessionCache;
        this.locale = locale;
    }

    @Override // com.zendesk.sdk.network.RequestProvider
    public void createRequest(final CreateRequest createRequest, final ZendeskCallback<CreateRequest> zendeskCallback) {
        if (!(createRequest != null)) {
            Logger.e(LOG_TAG, "configuration is invalid: request null", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("configuration is invalid: request null"));
                return;
            }
            return;
        }
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.1
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskRequestProvider.this.internalCreateRequest(sdkConfiguration.getBearerAuthorizationHeader(), createRequest, sdkConfiguration.getMobileSettings().getAuthenticationType(), zendeskCallback);
            }
        });
    }

    void internalCreateRequest(String str, final CreateRequest createRequest, final AuthenticationType authenticationType, final ZendeskCallback<CreateRequest> zendeskCallback) {
        Identity identity;
        PassThroughErrorZendeskCallback<Request> passThroughErrorZendeskCallback = new PassThroughErrorZendeskCallback<Request>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.2
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(Request request) {
                if (request.getId() == null) {
                    onError(new ErrorResponseAdapter("The request was created, but the ID is unknown."));
                    return;
                }
                ZendeskConfig.INSTANCE.getTracker().requestCreated();
                createRequest.setId(request.getId());
                if (authenticationType == AuthenticationType.ANONYMOUS) {
                    ZendeskRequestProvider.this.requestStorage.storeRequestId(request.getId());
                }
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onSuccess(createRequest);
                }
            }
        };
        if (authenticationType == AuthenticationType.ANONYMOUS && (identity = this.identity) != null && (identity instanceof AnonymousIdentity)) {
            this.requestService.createRequest(str, ((AnonymousIdentity) identity).getSdkGuid(), createRequest, passThroughErrorZendeskCallback);
        } else {
            this.requestService.createRequest(str, null, createRequest, passThroughErrorZendeskCallback);
        }
    }

    void getAllRequestsInternal(String str, String str2, AuthenticationType authenticationType, ZendeskCallback<List<Request>> zendeskCallback) {
        if (StringUtils.isEmpty(str2)) {
            str2 = ALL_REQUEST_STATUSES;
        }
        String str3 = str2;
        if (authenticationType == AuthenticationType.ANONYMOUS) {
            List<String> storedRequestIds = this.requestStorage.getStoredRequestIds();
            if (CollectionUtils.isEmpty(storedRequestIds)) {
                Logger.w(LOG_TAG, "getAllRequestsInternal: There are no requests to fetch", new Object[0]);
                if (zendeskCallback != null) {
                    zendeskCallback.onSuccess(new ArrayList());
                    return;
                }
                return;
            }
            this.requestService.getAllRequests(str, StringUtils.toCsvString(storedRequestIds), str3, SIDE_LOAD_PUBLIC_UPDATED_AT, zendeskCallback);
            return;
        }
        this.requestService.getAllRequests(str, str3, SIDE_LOAD_PUBLIC_UPDATED_AT, zendeskCallback);
    }

    @Override // com.zendesk.sdk.network.RequestProvider
    public void getAllRequests(ZendeskCallback<List<Request>> zendeskCallback) {
        getRequests(null, zendeskCallback);
    }

    @Override // com.zendesk.sdk.network.RequestProvider
    public void getRequests(final String str, final ZendeskCallback<List<Request>> zendeskCallback) {
        this.baseProvider.configureSdk(new ZendeskCallback<SdkConfiguration>() { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.3
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskRequestProvider.this.areConversationsEnabled(sdkConfiguration.getMobileSettings())) {
                    ZendeskRequestProvider.this.getAllRequestsInternal(sdkConfiguration.getBearerAuthorizationHeader(), str, sdkConfiguration.getMobileSettings().getAuthenticationType(), zendeskCallback);
                } else {
                    ZendeskRequestProvider.this.answerCallbackOnConversationsDisabled(zendeskCallback);
                }
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onError(errorResponse);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getComments(String str, String str2, ZendeskCallback<CommentsResponse> zendeskCallback) {
        this.requestService.getComments(str, str2, zendeskCallback);
    }

    @Override // com.zendesk.sdk.network.RequestProvider
    public void getComments(final String str, final ZendeskCallback<CommentsResponse> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.4
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskRequestProvider.this.areConversationsEnabled(sdkConfiguration.getMobileSettings())) {
                    ZendeskRequestProvider.this.getComments(sdkConfiguration.getBearerAuthorizationHeader(), str, zendeskCallback);
                } else {
                    ZendeskRequestProvider.this.answerCallbackOnConversationsDisabled(zendeskCallback);
                }
            }
        });
    }

    void addCommentInternal(String str, String str2, EndUserComment endUserComment, final ZendeskCallback<Comment> zendeskCallback) {
        this.requestService.addComment(str, str2, endUserComment, new PassThroughErrorZendeskCallback<Request>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.5
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(Request request) {
                ZendeskConfig.INSTANCE.getTracker().requestUpdated();
                if (request.getId() != null && request.getCommentCount() != null) {
                    ZendeskRequestProvider.this.requestStorage.setCommentCount(request.getId(), request.getCommentCount().intValue());
                } else {
                    Logger.w(ZendeskRequestProvider.LOG_TAG, "The ID and / or comment count was missing. Cannot store comment count.", new Object[0]);
                }
                Comment comment = new Comment();
                comment.setAuthorId(request.getRequesterId());
                comment.setCreatedAt(new Date());
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onSuccess(comment);
                }
            }
        });
    }

    @Override // com.zendesk.sdk.network.RequestProvider
    public void addComment(final String str, final EndUserComment endUserComment, final ZendeskCallback<Comment> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.6
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskRequestProvider.this.areConversationsEnabled(sdkConfiguration.getMobileSettings())) {
                    ZendeskRequestProvider.this.addCommentInternal(sdkConfiguration.getBearerAuthorizationHeader(), str, endUserComment, zendeskCallback);
                } else {
                    ZendeskRequestProvider.this.answerCallbackOnConversationsDisabled(zendeskCallback);
                }
            }
        });
    }

    @Override // com.zendesk.sdk.network.RequestProvider
    public void getRequest(final String str, final ZendeskCallback<Request> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.7
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                if (ZendeskRequestProvider.this.areConversationsEnabled(sdkConfiguration.getMobileSettings())) {
                    ZendeskRequestProvider.this.requestService.getRequest(sdkConfiguration.getBearerAuthorizationHeader(), str, new PassThroughErrorZendeskCallback<RequestResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.7.1
                        @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                        public void onSuccess(RequestResponse requestResponse) {
                            if (zendeskCallback != null) {
                                zendeskCallback.onSuccess(requestResponse.getRequest());
                            }
                        }
                    });
                } else {
                    ZendeskRequestProvider.this.answerCallbackOnConversationsDisabled(zendeskCallback);
                }
            }
        });
    }

    public void getTicketFormsById(List<Long> list, final ZendeskCallback<List<TicketForm>> zendeskCallback) {
        if (CollectionUtils.isEmpty(list)) {
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("Ticket forms must at least contain 1 Id"));
                return;
            }
            return;
        }
        final ArrayList arrayList = new ArrayList();
        if (list.size() > 5) {
            arrayList.addAll(list.subList(0, 5));
            Logger.d(LOG_TAG, "Maximum number of allowed ticket fields: %d.", 5);
        } else {
            arrayList.addAll(list);
        }
        if (!this.requestSessionCache.containsAllTicketForms(arrayList)) {
            this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.8
                @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                public void onSuccess(SdkConfiguration sdkConfiguration) {
                    if (sdkConfiguration.getMobileSettings().isTicketFormSupportAvailable()) {
                        String csvStringNumber = StringUtils.toCsvStringNumber((List<? extends Number>) arrayList);
                        ZendeskRequestProvider.this.requestService.getTicketFormsById(sdkConfiguration.getBearerAuthorizationHeader(), LocaleUtil.toLanguageTag(ZendeskRequestProvider.this.locale), csvStringNumber, new PassThroughErrorZendeskCallback<RawTicketFormResponse>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskRequestProvider.8.1
                            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                            public void onSuccess(RawTicketFormResponse rawTicketFormResponse) {
                                List<TicketForm> listConvertTicketFormResponse = ZendeskRequestProvider.this.convertTicketFormResponse(rawTicketFormResponse.getTicketForms(), rawTicketFormResponse.getTicketFields());
                                ZendeskRequestProvider.this.requestSessionCache.updateTicketFormCache(listConvertTicketFormResponse);
                                if (zendeskCallback != null) {
                                    zendeskCallback.onSuccess(listConvertTicketFormResponse);
                                }
                            }
                        });
                    } else {
                        ZendeskCallback zendeskCallback2 = zendeskCallback;
                        if (zendeskCallback2 != null) {
                            zendeskCallback2.onError(new ErrorResponseAdapter("Ticket form support disabled."));
                        }
                    }
                }
            });
        } else if (zendeskCallback != null) {
            zendeskCallback.onSuccess(this.requestSessionCache.getTicketFormsById(arrayList));
        }
    }

    List<TicketForm> convertTicketFormResponse(List<RawTicketForm> list, List<RawTicketField> list2) {
        ArrayList arrayList = new ArrayList();
        Map<Long, TicketField> mapCreateTicketFieldMap = createTicketFieldMap(list2);
        Iterator<RawTicketForm> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(createTicketFormFromResponse(it.next(), mapCreateTicketFieldMap));
        }
        return arrayList;
    }

    TicketForm createTicketFormFromResponse(RawTicketForm rawTicketForm, Map<Long, TicketField> map) {
        ArrayList arrayList = new ArrayList();
        for (Long l : rawTicketForm.getTicketFieldIds()) {
            if (l != null && map.get(l) != null) {
                arrayList.add(map.get(l));
            }
        }
        return TicketForm.create(rawTicketForm, arrayList);
    }

    Map<Long, TicketField> createTicketFieldMap(List<RawTicketField> list) {
        HashMap map = new HashMap(list.size());
        for (RawTicketField rawTicketField : list) {
            map.put(Long.valueOf(rawTicketField.getId()), TicketField.create(rawTicketField));
        }
        return map;
    }

    boolean areConversationsEnabled(SafeMobileSettings safeMobileSettings) {
        if (safeMobileSettings == null) {
            return false;
        }
        return safeMobileSettings.isConversationsEnabled();
    }

    void answerCallbackOnConversationsDisabled(ZendeskCallback zendeskCallback) {
        Logger.d(LOG_TAG, "Conversations disabled, this feature is not available on your plan or was disabled.", new Object[0]);
        if (zendeskCallback != null) {
            zendeskCallback.onError(new ErrorResponseAdapter("Access Denied"));
        }
    }
}
