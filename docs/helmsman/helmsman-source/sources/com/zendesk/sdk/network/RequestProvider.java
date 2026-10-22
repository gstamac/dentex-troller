package com.zendesk.sdk.network;

import com.zendesk.sdk.model.request.Comment;
import com.zendesk.sdk.model.request.CommentsResponse;
import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.sdk.model.request.EndUserComment;
import com.zendesk.sdk.model.request.Request;
import com.zendesk.service.ZendeskCallback;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface RequestProvider {
    void addComment(String str, EndUserComment endUserComment, ZendeskCallback<Comment> zendeskCallback);

    void createRequest(CreateRequest createRequest, ZendeskCallback<CreateRequest> zendeskCallback);

    void getAllRequests(ZendeskCallback<List<Request>> zendeskCallback);

    void getComments(String str, ZendeskCallback<CommentsResponse> zendeskCallback);

    void getRequest(String str, ZendeskCallback<Request> zendeskCallback);

    void getRequests(String str, ZendeskCallback<List<Request>> zendeskCallback);
}
