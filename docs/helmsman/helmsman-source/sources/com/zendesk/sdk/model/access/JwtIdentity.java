package com.zendesk.sdk.model.access;

import com.zendesk.logger.Logger;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
public class JwtIdentity implements Identity {
    private static final String LOG_TAG = "JwtIdentity";
    private String token;

    public JwtIdentity(String str) {
        if (StringUtils.isEmpty(str)) {
            Logger.e(LOG_TAG, "A null or empty JWT was specified. This will not work. Please check your initialisation of JwtIdentity!", new Object[0]);
        }
        this.token = str;
    }

    public String getJwtUserIdentifier() {
        return this.token;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        String str = this.token;
        String str2 = ((JwtIdentity) obj).token;
        return str == null ? str2 == null : str.equals(str2);
    }

    public int hashCode() {
        String str = this.token;
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }
}
