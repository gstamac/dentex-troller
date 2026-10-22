package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.AuthenticationType;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.access.JwtIdentity;

/* JADX INFO: loaded from: classes2.dex */
class AuthenticationLoggerHelper {
    private final String logMessage;

    AuthenticationLoggerHelper(AuthenticationType authenticationType, Identity identity) {
        StringBuilder sb = new StringBuilder(160);
        sb.append("The expected type of authentication is ");
        if (authenticationType == null) {
            sb.append("null. Check that settings have been downloaded.");
        } else if (authenticationType == AuthenticationType.ANONYMOUS) {
            sb.append("anonymous.");
        } else if (authenticationType == AuthenticationType.JWT) {
            sb.append("jwt.");
        }
        sb.append("\nThe local identity is");
        if (identity == null) {
            sb.append(" not");
        }
        sb.append(" present.\n");
        if (identity != null) {
            sb.append("The local identity is ");
            if (identity instanceof AnonymousIdentity) {
                sb.append("anonymous.");
            } else if (identity instanceof JwtIdentity) {
                sb.append("jwt.");
            } else {
                sb.append("unknown.");
            }
        }
        this.logMessage = sb.toString();
    }

    public String getLogMessage() {
        return this.logMessage;
    }
}
