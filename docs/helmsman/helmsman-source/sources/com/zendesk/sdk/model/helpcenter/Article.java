package com.zendesk.sdk.model.helpcenter;

import com.zendesk.logger.Logger;
import com.zendesk.util.CollectionUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Article implements Serializable {
    private static final String LOG_TAG = "Article";
    public static final int UNKNOWN_VOTE_COUNT = -1;
    private User author;
    private Long authorId;
    private String body;
    private boolean commentsDisabled;
    private Date createdAt;
    private boolean draft;
    private String htmlUrl;
    private Long id;
    private List<String> labelNames;
    private String locale;
    private boolean outdated;
    private Long sectionId;
    private String sourceLocale;
    private String title;
    private Date updatedAt;
    private String url;
    private Integer voteCount;
    private Integer voteSum;

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public boolean isCommentsDisabled() {
        return this.commentsDisabled;
    }

    public List<String> getLabelNames() {
        return CollectionUtils.copyOf(this.labelNames);
    }

    public boolean isDraft() {
        return this.draft;
    }

    public int getVoteSum() {
        Integer num = this.voteSum;
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public int getVoteCount() {
        Integer num = this.voteCount;
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public Long getSectionId() {
        return this.sectionId;
    }

    public Date getCreatedAt() {
        if (this.createdAt == null) {
            return null;
        }
        return new Date(this.createdAt.getTime());
    }

    public Date getUpdatedAt() {
        if (this.updatedAt == null) {
            return null;
        }
        return new Date(this.updatedAt.getTime());
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public String getSourceLocale() {
        return this.sourceLocale;
    }

    public String getLocale() {
        return this.locale;
    }

    public boolean isOutdated() {
        return this.outdated;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public int getUpvoteCount() {
        Integer num = this.voteSum;
        if (num == null || this.voteCount == null) {
            Logger.e(LOG_TAG, "Cannot determine vote count because vote_sum and / or vote_count are null", new Object[0]);
            return -1;
        }
        return (num.intValue() + this.voteCount.intValue()) / 2;
    }

    public int getDownvoteCount() {
        Integer num = this.voteSum;
        if (num == null || this.voteCount == null) {
            Logger.e(LOG_TAG, "Cannot determine vote count because vote_sum and / or vote_count are null", new Object[0]);
            return -1;
        }
        return Math.abs(num.intValue() - this.voteCount.intValue()) / 2;
    }
}
