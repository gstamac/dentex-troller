package com.zendesk.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class RegexUtils {
    private static final String QUANTIFIER_VALIDATION_REGEX = "^\\{\\d{1,},?\\d*\\}";
    private static final Pattern QUANTIFIER_PATTERN = Pattern.compile(QUANTIFIER_VALIDATION_REGEX);

    private RegexUtils() {
    }

    public static String escape(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char[] charArray = str.toCharArray();
        int length = 0;
        while (length < charArray.length) {
            char c = charArray[length];
            if (c == '{') {
                String strValidateQuantifierExpression = validateQuantifierExpression(str.substring(length));
                if (strValidateQuantifierExpression != null) {
                    sb.append(strValidateQuantifierExpression);
                    length += strValidateQuantifierExpression.length() - 1;
                } else {
                    sb.append(Pattern.quote(Character.toString(c)));
                }
            } else {
                sb.append(c);
            }
            length++;
        }
        return sb.toString();
    }

    static String validateQuantifierExpression(String str) {
        Matcher matcher = QUANTIFIER_PATTERN.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
