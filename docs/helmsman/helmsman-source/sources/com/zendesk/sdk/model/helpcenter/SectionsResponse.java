package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SectionsResponse {
    public List<Section> sections;

    public List<Section> getSections() {
        return CollectionUtils.copyOf(this.sections);
    }
}
