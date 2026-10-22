package com.zendesk.sdk.model.settings;

import com.zendesk.util.CollectionUtils;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppSettings implements Serializable {
    private String androidStoreUrl;
    private long delay;
    private long duration;
    private boolean enabled;
    private List<String> tags;
    private int visits;

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getVisits() {
        return this.visits;
    }

    public long getDuration() {
        return TimeUnit.DAYS.toMillis(this.duration);
    }

    public long getDelay() {
        return TimeUnit.SECONDS.toMillis(this.delay);
    }

    public List<String> getTags() {
        return CollectionUtils.copyOf(this.tags);
    }

    public String getAndroidStoreUrl() {
        return this.androidStoreUrl;
    }
}
