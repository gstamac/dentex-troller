package com.zendesk.sdk.network.impl;

import com.zendesk.util.StringUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
class HelpCenterLocaleConverter {
    private static final Map<String, String> forwardLookupMap;

    HelpCenterLocaleConverter() {
    }

    static {
        HashMap map = new HashMap();
        forwardLookupMap = map;
        map.put("iw", "he");
        map.put("nb", "no");
        map.put("in", "id");
        map.put("ji", "yi");
    }

    String toHelpCenterLocaleString(Locale locale) {
        if (!(locale != null && StringUtils.hasLength(locale.getLanguage()))) {
            locale = Locale.getDefault();
        }
        String language = forwardLookupMap.get(locale.getLanguage());
        if (!StringUtils.hasLength(language)) {
            language = locale.getLanguage();
        }
        StringBuilder sb = new StringBuilder(language);
        if (StringUtils.hasLength(locale.getCountry())) {
            sb.append("-").append(locale.getCountry());
        }
        return sb.toString();
    }
}
