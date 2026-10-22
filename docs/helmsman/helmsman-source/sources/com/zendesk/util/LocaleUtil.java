package com.zendesk.util;

import com.zendesk.logger.Logger;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: classes2.dex */
public class LocaleUtil {
    private static final String LOG_TAG = "LocaleUtil";
    private static final List<String> NEW_ISO_CODES = Arrays.asList("he", "yi", "id");

    private LocaleUtil() {
    }

    public static String toLanguageTag(Locale locale) {
        if (locale == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(locale.getLanguage());
        if (StringUtils.hasLength(locale.getCountry())) {
            sb.append("-");
            sb.append(locale.getCountry().toLowerCase(Locale.US));
        }
        return sb.toString();
    }

    public static Locale forLanguageTag(String str) {
        Locale locale;
        String str2 = LOG_TAG;
        Logger.d(str2, "Assuming Locale.getDefault()", new Object[0]);
        Locale locale2 = Locale.getDefault();
        if (!StringUtils.hasLength(str)) {
            return locale2;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, "-");
        int iCountTokens = stringTokenizer.countTokens();
        if (iCountTokens == 1 || iCountTokens == 2) {
            if ((iCountTokens != 1 ? 5 : 2) != str.length()) {
                Logger.d(str2, "number of tokens is correct but the length of the locale string does not match the expected length", new Object[0]);
                return locale2;
            }
            String strNextToken = stringTokenizer.nextToken();
            String upperCase = (stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : "").toUpperCase(Locale.US);
            if (NEW_ISO_CODES.contains(strNextToken)) {
                Logger.d(str2, "New ISO-6390-Alpha3 locale detected trying to create new locale per reflection", new Object[0]);
                locale = createIso639Alpha3LocaleJdk(strNextToken, upperCase);
                if (locale == null) {
                    locale = createIso639Alpha3LocaleAndroid(strNextToken, upperCase);
                }
                if (locale == null) {
                    locale = new Locale(strNextToken, upperCase);
                }
            } else {
                locale = new Locale(strNextToken, upperCase);
            }
            return locale;
        }
        Logger.w(str2, "Unexpected number of tokens, must be at least one and at most two", new Object[0]);
        return locale2;
    }

    private static Locale createIso639Alpha3LocaleJdk(String str, String str2) {
        try {
            Method declaredMethod = Locale.class.getDeclaredMethod("createConstant", String.class, String.class);
            declaredMethod.setAccessible(true);
            return (Locale) declaredMethod.invoke(null, str, str2);
        } catch (Exception e) {
            Logger.e(LOG_TAG, "Unable to create ISO-6390-Alpha3 per reflection", e, new Object[0]);
            return null;
        }
    }

    private static Locale createIso639Alpha3LocaleAndroid(String str, String str2) {
        try {
            Constructor declaredConstructor = Locale.class.getDeclaredConstructor(Boolean.TYPE, String.class, String.class);
            declaredConstructor.setAccessible(true);
            return (Locale) declaredConstructor.newInstance(true, str, str2);
        } catch (Exception e) {
            Logger.e(LOG_TAG, "Unable to create ISO-6390-Alpha3 per reflection", e, new Object[0]);
            return null;
        }
    }
}
