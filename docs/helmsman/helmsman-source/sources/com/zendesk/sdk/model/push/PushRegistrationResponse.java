package com.zendesk.sdk.model.push;

import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class PushRegistrationResponse {
    private boolean active;
    private Date createdAt;
    private String deviceType;
    private int id;
    private String identifier;
    private Date updatedAt;
    private String url;

    public String getIdentifier() {
        return this.identifier;
    }
}
