package org.bouncycastle.asn1;

import com.facebook.appevents.AppEventsConstants;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class DERGeneralizedTime extends ASN1Object {
    String time;

    public DERGeneralizedTime(String str) {
        this.time = str;
        try {
            getDate();
        } catch (ParseException e) {
            throw new IllegalArgumentException("invalid date string: " + e.getMessage());
        }
    }

    public DERGeneralizedTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = simpleDateFormat.format(date);
    }

    DERGeneralizedTime(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[length];
        for (int i = 0; i != length; i++) {
            cArr[i] = (char) (bArr[i] & UByte.MAX_VALUE);
        }
        this.time = new String(cArr);
    }

    private String calculateGMTOffset() {
        String str;
        TimeZone timeZone = TimeZone.getDefault();
        int rawOffset = timeZone.getRawOffset();
        if (rawOffset < 0) {
            rawOffset = -rawOffset;
            str = "-";
        } else {
            str = "+";
        }
        int i = rawOffset / 3600000;
        int i2 = (rawOffset - (((i * 60) * 60) * 1000)) / 60000;
        try {
            if (timeZone.useDaylightTime() && timeZone.inDaylightTime(getDate())) {
                i += str.equals("+") ? 1 : -1;
            }
        } catch (ParseException unused) {
        }
        return "GMT" + str + convert(i) + ":" + convert(i2);
    }

    private String convert(int i) {
        return i < 10 ? AppEventsConstants.EVENT_PARAM_VALUE_NO + i : Integer.toString(i);
    }

    public static DERGeneralizedTime getInstance(Object obj) {
        if (obj == null || (obj instanceof DERGeneralizedTime)) {
            return (DERGeneralizedTime) obj;
        }
        if (obj instanceof ASN1OctetString) {
            return new DERGeneralizedTime(((ASN1OctetString) obj).getOctets());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERGeneralizedTime getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(aSN1TaggedObject.getObject());
    }

    private byte[] getOctets() {
        char[] charArray = this.time.toCharArray();
        byte[] bArr = new byte[charArray.length];
        for (int i = 0; i != charArray.length; i++) {
            bArr[i] = (byte) charArray[i];
        }
        return bArr;
    }

    private boolean hasFractionalSeconds() {
        return this.time.indexOf(46) == 14;
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        if (dERObject instanceof DERGeneralizedTime) {
            return this.time.equals(((DERGeneralizedTime) dERObject).time);
        }
        return false;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        dEROutputStream.writeEncoded(24, getOctets());
    }

    /* JADX WARN: Code duplicated, block: B:27:0x0090  */
    /* JADX WARN: Code duplicated, block: B:38:0x00b2  */
    public Date getDate() throws ParseException {
        SimpleDateFormat simpleDateFormat;
        SimpleTimeZone simpleTimeZone;
        String strSubstring;
        int i;
        char cCharAt;
        String time = this.time;
        if (!time.endsWith("Z")) {
            if (this.time.indexOf(45) > 0 || this.time.indexOf(43) > 0) {
                time = getTime();
                simpleDateFormat = hasFractionalSeconds() ? new SimpleDateFormat("yyyyMMddHHmmss.SSSz") : new SimpleDateFormat("yyyyMMddHHmmssz");
                simpleTimeZone = new SimpleTimeZone(0, "Z");
            } else {
                simpleDateFormat = hasFractionalSeconds() ? new SimpleDateFormat("yyyyMMddHHmmss.SSS") : new SimpleDateFormat("yyyyMMddHHmmss");
                simpleDateFormat.setTimeZone(new SimpleTimeZone(0, TimeZone.getDefault().getID()));
            }
            if (hasFractionalSeconds()) {
                strSubstring = time.substring(14);
                i = 1;
                while (i < strSubstring.length() && '0' <= (cCharAt = strSubstring.charAt(i)) && cCharAt <= '9') {
                    i++;
                }
                if (i - 1 > 3) {
                    time = time.substring(0, 14) + (strSubstring.substring(0, 4) + strSubstring.substring(i));
                }
            }
            return simpleDateFormat.parse(time);
        }
        simpleDateFormat = hasFractionalSeconds() ? new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'") : new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        simpleTimeZone = new SimpleTimeZone(0, "Z");
        simpleDateFormat.setTimeZone(simpleTimeZone);
        if (hasFractionalSeconds()) {
            strSubstring = time.substring(14);
            i = 1;
            while (i < strSubstring.length()) {
                i++;
            }
            if (i - 1 > 3) {
                time = time.substring(0, 14) + (strSubstring.substring(0, 4) + strSubstring.substring(i));
            }
        }
        return simpleDateFormat.parse(time);
    }

    public String getTime() {
        StringBuilder sbAppend;
        String strSubstring;
        String str = this.time;
        if (str.charAt(str.length() - 1) == 'Z') {
            StringBuilder sb = new StringBuilder();
            String str2 = this.time;
            sbAppend = sb.append(str2.substring(0, str2.length() - 1));
            strSubstring = "GMT+00:00";
        } else {
            int length = this.time.length() - 5;
            char cCharAt = this.time.charAt(length);
            if (cCharAt == '-' || cCharAt == '+') {
                int i = length + 3;
                sbAppend = new StringBuilder().append(this.time.substring(0, length)).append("GMT").append(this.time.substring(length, i)).append(":");
                strSubstring = this.time.substring(i);
            } else {
                int length2 = this.time.length() - 3;
                char cCharAt2 = this.time.charAt(length2);
                if (cCharAt2 == '-' || cCharAt2 == '+') {
                    sbAppend = new StringBuilder().append(this.time.substring(0, length2)).append("GMT").append(this.time.substring(length2));
                    strSubstring = ":00";
                } else {
                    sbAppend = new StringBuilder().append(this.time);
                    strSubstring = calculateGMTOffset();
                }
            }
        }
        return sbAppend.append(strSubstring).toString();
    }

    public String getTimeString() {
        return this.time;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        return this.time.hashCode();
    }
}
