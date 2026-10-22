package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.request.CommentsResponse;
import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.sdk.model.request.CreateRequestWrapper;
import com.zendesk.sdk.model.request.EndUserComment;
import com.zendesk.sdk.model.request.Request;
import com.zendesk.sdk.model.request.RequestResponse;
import com.zendesk.sdk.model.request.RequestsResponse;
import com.zendesk.sdk.model.request.UpdateRequestWrapper;
import com.zendesk.sdk.model.request.fields.RawTicketFormResponse;
import com.zendesk.sdk.network.RequestService;
import com.zendesk.service.RetrofitZendeskCallbackAdapter;
import com.zendesk.service.ZendeskCallback;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskRequestService {
    private static final String LOG_TAG = "ZendeskRequestService";
    private static final String TICKET_FIELDS_INCLUDE = "ticket_fields";
    private final RequestService requestService;

    ZendeskRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    public void createRequest(String str, String str2, CreateRequest createRequest, ZendeskCallback<Request> zendeskCallback) {
        CreateRequestWrapper createRequestWrapper = new CreateRequestWrapper();
        createRequestWrapper.setRequest(createRequest);
        this.requestService.createRequest(str, str2, createRequestWrapper).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<RequestResponse, Request>() { // from class: com.zendesk.sdk.network.impl.ZendeskRequestService.1
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public Request extract(RequestResponse requestResponse) {
                return requestResponse.getRequest();
            }
        }));
    }

    void getAllRequests(String str, String str2, String str3, ZendeskCallback<List<Request>> zendeskCallback) {
        this.requestService.getAllRequests(str, str2, str3).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<RequestsResponse, List<Request>>() { // from class: com.zendesk.sdk.network.impl.ZendeskRequestService.2
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public List<Request> extract(RequestsResponse requestsResponse) {
                return requestsResponse.getRequests();
            }
        }));
    }

    void getAllRequests(String str, String str2, String str3, String str4, ZendeskCallback<List<Request>> zendeskCallback) {
        this.requestService.getManyRequests(str, str2, str3, str4).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<RequestsResponse, List<Request>>() { // from class: com.zendesk.sdk.network.impl.ZendeskRequestService.3
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public List<Request> extract(RequestsResponse requestsResponse) {
                return requestsResponse.getRequests();
            }
        }));
    }

    void getComments(String str, String str2, ZendeskCallback<CommentsResponse> zendeskCallback) {
        this.requestService.getComments(str, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    void addComment(String str, String str2, EndUserComment endUserComment, ZendeskCallback<Request> zendeskCallback) {
        UpdateRequestWrapper updateRequestWrapper = new UpdateRequestWrapper();
        Request request = new Request();
        request.setComment(endUserComment);
        updateRequestWrapper.setRequest(request);
        this.requestService.addComment(str, str2, updateRequestWrapper).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, new RetrofitZendeskCallbackAdapter.RequestExtractor<UpdateRequestWrapper, Request>() { // from class: com.zendesk.sdk.network.impl.ZendeskRequestService.4
            @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
            public Request extract(UpdateRequestWrapper updateRequestWrapper2) {
                return updateRequestWrapper2.getRequest();
            }
        }));
    }

    void getRequest(String str, String str2, ZendeskCallback<RequestResponse> zendeskCallback) {
        this.requestService.getRequest(str, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    void getTicketFormsById(String str, String str2, String str3, ZendeskCallback<RawTicketFormResponse> zendeskCallback) {
        this.requestService.getTicketFormsById(str, str2, str3, TICKET_FIELDS_INCLUDE).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }
}
