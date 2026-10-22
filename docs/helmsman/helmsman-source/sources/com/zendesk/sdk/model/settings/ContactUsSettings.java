package com.zendesk.sdk.model.settings;

import com.zendesk.util.CollectionUtils;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ContactUsSettings implements Serializable {
    private List<String> tags;

    public List<String> getTags() {
        return CollectionUtils.copyOf(this.tags);
    }
}
