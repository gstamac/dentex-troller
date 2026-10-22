package com.zendesk.sdk.model.settings;

import com.zendesk.sdk.model.access.AuthenticationType;
import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class SdkSettings implements Serializable {
    private String authentication;
    private ContactUsSettings contactUs;
    private ConversationsSettings conversations;
    private HelpCenterSettings helpCenter;
    private RateMyAppSettings rma;
    private TicketFormSettings ticketForms;
    private Date updatedAt;

    public RateMyAppSettings getRateMyAppSettings() {
        return this.rma;
    }

    public ConversationsSettings getConversationsSettings() {
        return this.conversations;
    }

    public HelpCenterSettings getHelpCenterSettings() {
        return this.helpCenter;
    }

    public ContactUsSettings getContactUsSettings() {
        return this.contactUs;
    }

    public Date getUpdatedAt() {
        if (this.updatedAt == null) {
            return null;
        }
        return new Date(this.updatedAt.getTime());
    }

    public AuthenticationType getAuthentication() {
        return AuthenticationType.getAuthType(this.authentication);
    }

    public TicketFormSettings getTicketFormSettings() {
        return this.ticketForms;
    }
}
