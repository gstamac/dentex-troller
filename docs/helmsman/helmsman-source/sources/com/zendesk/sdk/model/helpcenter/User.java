package com.zendesk.sdk.model.helpcenter;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class User implements Serializable {
    private Long id;
    private String name;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Long l = this.id;
        Long l2 = ((User) obj).id;
        if (l != null) {
            if (l.equals(l2)) {
                return true;
            }
        } else if (l2 == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        Long l = this.id;
        if (l != null) {
            return l.hashCode();
        }
        return 0;
    }
}
