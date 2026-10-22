package com.zendesk.sdk.rating.impl;

import com.zendesk.sdk.rating.RateMyAppRule;
import com.zendesk.util.CollectionUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppRules {
    private List<RateMyAppRule> mRules;

    public RateMyAppRules(RateMyAppRule... rateMyAppRuleArr) {
        if (CollectionUtils.isEmpty(rateMyAppRuleArr)) {
            this.mRules = Collections.emptyList();
        }
        this.mRules = Arrays.asList(rateMyAppRuleArr);
    }

    public boolean checkRules() {
        for (RateMyAppRule rateMyAppRule : this.mRules) {
            if (rateMyAppRule != null && !rateMyAppRule.permitsShowOfDialog()) {
                return false;
            }
        }
        return true;
    }
}
