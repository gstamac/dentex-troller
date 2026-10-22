package com.zendesk.sdk.model.access;

/* JADX INFO: loaded from: classes2.dex */
public class AccessToken {
    private String accessToken;

    public String getAccessToken() {
        return this.accessToken;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        String str = this.accessToken;
        String str2 = ((AccessToken) obj).accessToken;
        if (str != null) {
            if (str.equals(str2)) {
                return true;
            }
        } else if (str2 == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        String str = this.accessToken;
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }
}
