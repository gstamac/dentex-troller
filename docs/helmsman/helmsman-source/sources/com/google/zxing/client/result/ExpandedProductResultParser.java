package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.HashMap;
import org.bouncycastle.math.ec.Tnaf;

/* JADX INFO: loaded from: classes2.dex */
public final class ExpandedProductResultParser extends ResultParser {
    @Override // com.google.zxing.client.result.ResultParser
    public ExpandedProductParsedResult parse(Result result) {
        ExpandedProductParsedResult expandedProductParsedResult = null;
        if (result.getBarcodeFormat() != BarcodeFormat.RSS_EXPANDED) {
            return null;
        }
        String massagedText = getMassagedText(result);
        HashMap map = new HashMap();
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        String str9 = null;
        String strSubstring = null;
        String str10 = null;
        String strSubstring2 = null;
        String strSubstring3 = null;
        int i = 0;
        while (i < massagedText.length()) {
            String strFindAIvalue = findAIvalue(i, massagedText);
            if (strFindAIvalue == null) {
                return expandedProductParsedResult;
            }
            byte b = 2;
            int length = i + strFindAIvalue.length() + 2;
            String strFindValue = findValue(length, massagedText);
            int length2 = length + strFindValue.length();
            strFindAIvalue.hashCode();
            i = length2;
            switch (strFindAIvalue.hashCode()) {
                case 1536:
                    b = !strFindAIvalue.equals("00") ? (byte) -1 : (byte) 0;
                    break;
                case 1537:
                    b = !strFindAIvalue.equals("01") ? (byte) -1 : (byte) 1;
                    break;
                case 1567:
                    if (!strFindAIvalue.equals("10")) {
                        b = -1;
                    }
                    break;
                case 1568:
                    b = !strFindAIvalue.equals("11") ? (byte) -1 : (byte) 3;
                    break;
                case 1570:
                    b = !strFindAIvalue.equals("13") ? (byte) -1 : (byte) 4;
                    break;
                case 1572:
                    b = !strFindAIvalue.equals("15") ? (byte) -1 : (byte) 5;
                    break;
                case 1574:
                    b = !strFindAIvalue.equals("17") ? (byte) -1 : (byte) 6;
                    break;
                case 1567966:
                    b = !strFindAIvalue.equals("3100") ? (byte) -1 : (byte) 7;
                    break;
                case 1567967:
                    b = !strFindAIvalue.equals("3101") ? (byte) -1 : (byte) 8;
                    break;
                case 1567968:
                    b = !strFindAIvalue.equals("3102") ? (byte) -1 : (byte) 9;
                    break;
                case 1567969:
                    b = !strFindAIvalue.equals("3103") ? (byte) -1 : (byte) 10;
                    break;
                case 1567970:
                    b = !strFindAIvalue.equals("3104") ? (byte) -1 : (byte) 11;
                    break;
                case 1567971:
                    b = !strFindAIvalue.equals("3105") ? (byte) -1 : (byte) 12;
                    break;
                case 1567972:
                    b = !strFindAIvalue.equals("3106") ? (byte) -1 : (byte) 13;
                    break;
                case 1567973:
                    b = !strFindAIvalue.equals("3107") ? (byte) -1 : (byte) 14;
                    break;
                case 1567974:
                    b = !strFindAIvalue.equals("3108") ? (byte) -1 : (byte) 15;
                    break;
                case 1567975:
                    b = !strFindAIvalue.equals("3109") ? (byte) -1 : Tnaf.POW_2_WIDTH;
                    break;
                case 1568927:
                    b = !strFindAIvalue.equals("3200") ? (byte) -1 : (byte) 17;
                    break;
                case 1568928:
                    b = !strFindAIvalue.equals("3201") ? (byte) -1 : (byte) 18;
                    break;
                case 1568929:
                    b = !strFindAIvalue.equals("3202") ? (byte) -1 : (byte) 19;
                    break;
                case 1568930:
                    b = !strFindAIvalue.equals("3203") ? (byte) -1 : (byte) 20;
                    break;
                case 1568931:
                    b = !strFindAIvalue.equals("3204") ? (byte) -1 : (byte) 21;
                    break;
                case 1568932:
                    b = !strFindAIvalue.equals("3205") ? (byte) -1 : (byte) 22;
                    break;
                case 1568933:
                    b = !strFindAIvalue.equals("3206") ? (byte) -1 : (byte) 23;
                    break;
                case 1568934:
                    b = !strFindAIvalue.equals("3207") ? (byte) -1 : (byte) 24;
                    break;
                case 1568935:
                    b = !strFindAIvalue.equals("3208") ? (byte) -1 : (byte) 25;
                    break;
                case 1568936:
                    b = !strFindAIvalue.equals("3209") ? (byte) -1 : (byte) 26;
                    break;
                case 1575716:
                    b = !strFindAIvalue.equals("3920") ? (byte) -1 : (byte) 27;
                    break;
                case 1575717:
                    b = !strFindAIvalue.equals("3921") ? (byte) -1 : (byte) 28;
                    break;
                case 1575718:
                    b = !strFindAIvalue.equals("3922") ? (byte) -1 : (byte) 29;
                    break;
                case 1575719:
                    b = !strFindAIvalue.equals("3923") ? (byte) -1 : (byte) 30;
                    break;
                case 1575747:
                    b = !strFindAIvalue.equals("3930") ? (byte) -1 : (byte) 31;
                    break;
                case 1575748:
                    b = !strFindAIvalue.equals("3931") ? (byte) -1 : (byte) 32;
                    break;
                case 1575749:
                    b = !strFindAIvalue.equals("3932") ? (byte) -1 : (byte) 33;
                    break;
                case 1575750:
                    b = !strFindAIvalue.equals("3933") ? (byte) -1 : (byte) 34;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    str2 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 1:
                    str = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 2:
                    str3 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 3:
                    str4 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 4:
                    str5 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 5:
                    str6 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 6:
                    str7 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                    strSubstring = strFindAIvalue.substring(3);
                    str9 = ExpandedProductParsedResult.KILOGRAM;
                    str8 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                    strSubstring = strFindAIvalue.substring(3);
                    str9 = ExpandedProductParsedResult.POUND;
                    str8 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 27:
                case 28:
                case 29:
                case 30:
                    strSubstring2 = strFindAIvalue.substring(3);
                    str10 = strFindValue;
                    expandedProductParsedResult = null;
                    break;
                case 31:
                case 32:
                case 33:
                case 34:
                    if (strFindValue.length() < 4) {
                        return null;
                    }
                    String strSubstring4 = strFindValue.substring(3);
                    strSubstring3 = strFindValue.substring(0, 3);
                    str10 = strSubstring4;
                    expandedProductParsedResult = null;
                    strSubstring2 = strFindAIvalue.substring(3);
                    break;
                default:
                    map.put(strFindAIvalue, strFindValue);
                    expandedProductParsedResult = null;
                    break;
            }
        }
        return new ExpandedProductParsedResult(massagedText, str, str2, str3, str4, str5, str6, str7, str8, str9, strSubstring, str10, strSubstring2, strSubstring3, map);
    }

    private static String findAIvalue(int i, String str) {
        if (str.charAt(i) != '(') {
            return null;
        }
        String strSubstring = str.substring(i + 1);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strSubstring.length(); i2++) {
            char cCharAt = strSubstring.charAt(i2);
            if (cCharAt == ')') {
                return sb.toString();
            }
            if (cCharAt < '0' || cCharAt > '9') {
                return null;
            }
            sb.append(cCharAt);
        }
        return sb.toString();
    }

    private static String findValue(int i, String str) {
        StringBuilder sb = new StringBuilder();
        String strSubstring = str.substring(i);
        for (int i2 = 0; i2 < strSubstring.length(); i2++) {
            char cCharAt = strSubstring.charAt(i2);
            if (cCharAt == '(') {
                if (findAIvalue(i2, strSubstring) != null) {
                    break;
                }
                sb.append('(');
            } else {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }
}
