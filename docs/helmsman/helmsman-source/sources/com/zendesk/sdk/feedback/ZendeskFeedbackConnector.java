package com.zendesk.sdk.feedback;

import android.content.Context;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.model.DeviceInfo;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.sdk.model.request.CustomField;
import com.zendesk.sdk.network.RequestProvider;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.storage.IdentityStorage;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ZendeskFeedbackConnector implements FeedbackConnector {
    private static final String FALLBACK_REQUEST_SUBJECT = "App Ticket";
    private static final String LOG_TAG = "ZendeskFeedbackConnector";
    private final String additionalInfo;
    private final List<String> additionalTags;
    private final List<String> configurationTags;
    private final List<CustomField> customFields;
    private final IdentityStorage identityStorage;
    private final Map<String, String> metadata;
    private final RequestProvider requestProvider;
    private final String requestSubject;
    private final Long ticketFormId;

    public static ZendeskFeedbackConnector defaultConnector(Context context, ZendeskFeedbackConfiguration zendeskFeedbackConfiguration, List<String> list) {
        return new ZendeskFeedbackConnector(context, zendeskFeedbackConfiguration, list, ZendeskConfig.INSTANCE.getCustomFields(), ZendeskConfig.INSTANCE.getTicketFormId(), ZendeskConfig.INSTANCE.provider().requestProvider(), ZendeskConfig.INSTANCE.storage().identityStorage());
    }

    public ZendeskFeedbackConnector(Context context, ZendeskFeedbackConfiguration zendeskFeedbackConfiguration, List<String> list, List<CustomField> list2, Long l, RequestProvider requestProvider, IdentityStorage identityStorage) {
        this.additionalInfo = zendeskFeedbackConfiguration == null ? null : zendeskFeedbackConfiguration.getAdditionalInfo();
        String requestSubject = FALLBACK_REQUEST_SUBJECT;
        if (zendeskFeedbackConfiguration == null) {
            if (context != null) {
                requestSubject = context.getString(R.string.contact_fragment_request_subject);
            }
        } else if (zendeskFeedbackConfiguration.getRequestSubject() != null) {
            requestSubject = zendeskFeedbackConfiguration.getRequestSubject();
        }
        this.requestSubject = requestSubject;
        this.configurationTags = zendeskFeedbackConfiguration == null ? null : zendeskFeedbackConfiguration.getTags();
        this.metadata = context != null ? new DeviceInfo(context).getDeviceInfoAsMap() : null;
        this.additionalTags = list;
        this.customFields = list2;
        this.ticketFormId = l;
        this.requestProvider = requestProvider;
        this.identityStorage = identityStorage;
    }

    @Override // com.zendesk.sdk.feedback.FeedbackConnector
    public boolean isValid() {
        boolean z = this.requestProvider != null;
        IdentityStorage identityStorage = this.identityStorage;
        boolean z2 = (identityStorage == null || identityStorage.getIdentity() == null) ? false : true;
        boolean z3 = z && z2;
        if (!z3) {
            Logger.e(LOG_TAG, "Connector is invalid, unable to send feedback. " + String.format(Locale.US, "Provider available: %s, identity is valid: %s", Boolean.valueOf(z), Boolean.valueOf(z2)), new Object[0]);
        }
        return z3;
    }

    @Override // com.zendesk.sdk.feedback.FeedbackConnector
    public void sendFeedback(String str, List<String> list, final ZendeskCallback<CreateRequest> zendeskCallback) {
        if (!isValid()) {
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("Configuration is invalid, unable to send feedback."));
                return;
            }
            return;
        }
        Logger.d(LOG_TAG, "Configuration is valid, attempting to send feedback...", new Object[0]);
        final CreateRequest createRequest = new CreateRequest();
        setCustomFieldsAndMetadata(createRequest, this.customFields, this.ticketFormId, this.metadata);
        setEmailFromIdentity(createRequest, this.identityStorage.getIdentity());
        setSubjectAndDescription(createRequest, this.requestSubject, str, this.additionalInfo);
        setTags(createRequest, this.configurationTags, this.additionalTags);
        if (list != null) {
            createRequest.setAttachments(list);
        }
        this.requestProvider.createRequest(createRequest, new ZendeskCallback<CreateRequest>() { // from class: com.zendesk.sdk.feedback.ZendeskFeedbackConnector.1
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(CreateRequest createRequest2) {
                Logger.d(ZendeskFeedbackConnector.LOG_TAG, "Feedback submitted successfully.", new Object[0]);
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onSuccess(createRequest);
                }
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                Logger.e(ZendeskFeedbackConnector.LOG_TAG, errorResponse);
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onError(errorResponse);
                }
            }
        });
    }

    void setCustomFieldsAndMetadata(CreateRequest createRequest, List<CustomField> list, Long l, Map<String, String> map) {
        createRequest.setTicketFormId(l);
        createRequest.setCustomFields(list);
        createRequest.setMetadata(map);
    }

    void setTags(CreateRequest createRequest, List<String> list, List<String> list2) {
        List<String> listCombineLists = CollectionUtils.combineLists(list, list2);
        if (CollectionUtils.isNotEmpty(listCombineLists)) {
            Logger.d(LOG_TAG, "Adding tags to feedback...", new Object[0]);
            createRequest.setTags(listCombineLists);
        }
    }

    void setEmailFromIdentity(CreateRequest createRequest, Identity identity) {
        if (identity instanceof AnonymousIdentity) {
            AnonymousIdentity anonymousIdentity = (AnonymousIdentity) identity;
            if (StringUtils.hasLength(anonymousIdentity.getEmail())) {
                createRequest.setEmail(anonymousIdentity.getEmail());
            }
        }
    }

    void setSubjectAndDescription(CreateRequest createRequest, String str, String str2, String str3) {
        createRequest.setSubject(str);
        StringBuilder sb = new StringBuilder(100);
        sb.append(str2);
        if (StringUtils.hasLength(str3)) {
            Logger.d(LOG_TAG, "Additional info is present. Attaching to description.", new Object[0]);
            sb.append("\n\n------------------------------\n\n").append(str3);
        }
        createRequest.setDescription(sb.toString());
    }
}
