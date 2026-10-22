package org.bouncycastle.asn1;

import im.helmsman.helmsmanandroid.factory.RetrofitFactory;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import kotlin.UByte;

/* JADX INFO: loaded from: classes2.dex */
public class DERUTCTime extends ASN1Object {
    String time;

    public DERUTCTime(String str) {
        this.time = str;
        try {
            getDate();
        } catch (ParseException e) {
            throw new IllegalArgumentException("invalid date string: " + e.getMessage());
        }
    }

    public DERUTCTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = simpleDateFormat.format(date);
    }

    DERUTCTime(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[length];
        for (int i = 0; i != length; i++) {
            cArr[i] = (char) (bArr[i] & UByte.MAX_VALUE);
        }
        this.time = new String(cArr);
    }

    public static DERUTCTime getInstance(Object obj) {
        if (obj == null || (obj instanceof DERUTCTime)) {
            return (DERUTCTime) obj;
        }
        if (obj instanceof ASN1OctetString) {
            return new DERUTCTime(((ASN1OctetString) obj).getOctets());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERUTCTime getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
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

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        if (dERObject instanceof DERUTCTime) {
            return this.time.equals(((DERUTCTime) dERObject).time);
        }
        return false;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        dEROutputStream.writeEncoded(23, getOctets());
    }

    public Date getAdjustedDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        return simpleDateFormat.parse(getAdjustedTime());
    }

    public String getAdjustedTime() {
        String time = getTime();
        return (time.charAt(0) < '5' ? new StringBuilder(RetrofitFactory.I20) : new StringBuilder("19")).append(time).toString();
    }

    public Date getDate() throws ParseException {
        return new SimpleDateFormat("yyMMddHHmmssz").parse(getTime());
    }

    public String getTime() {
        StringBuilder sbAppend;
        String strSubstring;
        if (this.time.indexOf(45) >= 0 || this.time.indexOf(43) >= 0) {
            int iIndexOf = this.time.indexOf(45);
            if (iIndexOf < 0) {
                iIndexOf = this.time.indexOf(43);
            }
            String str = this.time;
            if (iIndexOf == str.length() - 3) {
                str = str + "00";
            }
            if (iIndexOf == 10) {
                sbAppend = new StringBuilder().append(str.substring(0, 10)).append("00GMT").append(str.substring(10, 13)).append(":");
                strSubstring = str.substring(13, 15);
            } else {
                sbAppend = new StringBuilder().append(str.substring(0, 12)).append("GMT").append(str.substring(12, 15)).append(":");
                strSubstring = str.substring(15, 17);
            }
        } else if (this.time.length() == 11) {
            sbAppend = new StringBuilder().append(this.time.substring(0, 10));
            strSubstring = "00GMT+00:00";
        } else {
            sbAppend = new StringBuilder().append(this.time.substring(0, 12));
            strSubstring = "GMT+00:00";
        }
        return sbAppend.append(strSubstring).toString();
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        return this.time.hashCode();
    }

    public String toString() {
        return this.time;
    }
}
