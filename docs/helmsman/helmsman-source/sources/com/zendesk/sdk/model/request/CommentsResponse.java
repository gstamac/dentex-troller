package com.zendesk.sdk.model.request;

import com.zendesk.sdk.model.network.ResponseWrapper;
import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public final class CommentsResponse extends ResponseWrapper {
    private List<CommentResponse> comments;
    private List<Organization> organizations;
    private List<User> users;

    public List<CommentResponse> getComments() {
        return CollectionUtils.copyOf(this.comments);
    }

    public List<User> getUsers() {
        return CollectionUtils.copyOf(this.users);
    }

    public List<Organization> getOrganizations() {
        return CollectionUtils.copyOf(this.organizations);
    }
}
