package com.zendesk.sdk.model.request;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class UserFieldResponse {
    private List<UserField> userFields;

    public List<UserField> getUserFields() {
        return CollectionUtils.copyOf(this.userFields);
    }
}
