package com.zendesk.sdk.requests;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.request.CommentResponse;
import com.zendesk.sdk.model.request.User;
import com.zendesk.util.CollectionUtils;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class CommentWithUser {
    private static final String LOG_TAG = "CommentWithUser";
    private final User mAuthor;
    private final CommentResponse mComment;

    private CommentWithUser(CommentResponse commentResponse, User user) {
        this.mComment = commentResponse;
        this.mAuthor = user;
    }

    public CommentResponse getComment() {
        return this.mComment;
    }

    public User getAuthor() {
        User user = this.mAuthor;
        if (user != null) {
            return user;
        }
        Logger.w(LOG_TAG, "Author is null, returning default author", new Object[0]);
        return new User();
    }

    public static CommentWithUser build(CommentResponse commentResponse, List<User> list) {
        User user = null;
        if (commentResponse == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(list)) {
            Iterator<User> it = list.iterator();
            User user2 = null;
            while (it.hasNext()) {
                User next = it.next();
                Long id = next == null ? null : next.getId();
                if (id != null && id.equals(commentResponse.getAuthorId())) {
                    user2 = next;
                }
            }
            user = user2;
        }
        return new CommentWithUser(commentResponse, user);
    }

    public static CommentWithUser build(CommentResponse commentResponse, User user) {
        if (commentResponse != null) {
            return new CommentWithUser(commentResponse, user);
        }
        return null;
    }
}
