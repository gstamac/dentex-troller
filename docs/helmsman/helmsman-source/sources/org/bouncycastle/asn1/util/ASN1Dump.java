package org.bouncycastle.asn1.util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BERApplicationSpecific;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.BERConstructedSequence;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERConstructedSequence;
import org.bouncycastle.asn1.DERConstructedSet;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERExternal;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERUnknownTag;
import org.bouncycastle.asn1.DERVisibleString;
import org.bouncycastle.util.encoders.Hex;

/* JADX INFO: loaded from: classes2.dex */
public class ASN1Dump {
    private static final int SAMPLE_SIZE = 32;
    private static final String TAB = "    ";

    static void _dumpAsString(String str, boolean z, DERObject dERObject, StringBuffer stringBuffer) {
        StringBuilder sbAppend;
        String string;
        StringBuilder sbAppend2;
        BigInteger value;
        String str2;
        String strOutputApplicationSpecific;
        StringBuilder sbAppend3;
        StringBuilder sbAppend4;
        String time;
        StringBuilder sbAppend5;
        String str3;
        String property = System.getProperty("line.separator");
        if (dERObject instanceof ASN1Sequence) {
            Enumeration objects = ((ASN1Sequence) dERObject).getObjects();
            String str4 = str + TAB;
            stringBuffer.append(str);
            if (dERObject instanceof BERConstructedSequence) {
                str3 = "BER ConstructedSequence";
            } else if (dERObject instanceof DERConstructedSequence) {
                str3 = "DER ConstructedSequence";
            } else if (dERObject instanceof BERSequence) {
                str3 = "BER Sequence";
            } else {
                str3 = dERObject instanceof DERSequence ? "DER Sequence" : "Sequence";
            }
            stringBuffer.append(str3);
            while (true) {
                stringBuffer.append(property);
                while (objects.hasMoreElements()) {
                    Object objNextElement = objects.nextElement();
                    if (objNextElement == null || objNextElement.equals(new DERNull())) {
                        stringBuffer.append(str4);
                        stringBuffer.append("NULL");
                    } else {
                        _dumpAsString(str4, z, objNextElement instanceof DERObject ? (DERObject) objNextElement : ((DEREncodable) objNextElement).getDERObject(), stringBuffer);
                    }
                }
                return;
            }
        }
        if (!(dERObject instanceof DERTaggedObject)) {
            if (dERObject instanceof DERConstructedSet) {
                Enumeration objects2 = ((ASN1Set) dERObject).getObjects();
                String str5 = str + TAB;
                stringBuffer.append(str);
                stringBuffer.append("ConstructedSet");
                while (true) {
                    stringBuffer.append(property);
                    while (objects2.hasMoreElements()) {
                        Object objNextElement2 = objects2.nextElement();
                        if (objNextElement2 == null) {
                            stringBuffer.append(str5);
                            stringBuffer.append("NULL");
                        } else {
                            _dumpAsString(str5, z, objNextElement2 instanceof DERObject ? (DERObject) objNextElement2 : ((DEREncodable) objNextElement2).getDERObject(), stringBuffer);
                        }
                    }
                    return;
                }
            }
            if (dERObject instanceof BERSet) {
                Enumeration objects3 = ((ASN1Set) dERObject).getObjects();
                String str6 = str + TAB;
                stringBuffer.append(str);
                stringBuffer.append("BER Set");
                while (true) {
                    stringBuffer.append(property);
                    while (objects3.hasMoreElements()) {
                        Object objNextElement3 = objects3.nextElement();
                        if (objNextElement3 == null) {
                            stringBuffer.append(str6);
                            stringBuffer.append("NULL");
                        } else {
                            _dumpAsString(str6, z, objNextElement3 instanceof DERObject ? (DERObject) objNextElement3 : ((DEREncodable) objNextElement3).getDERObject(), stringBuffer);
                        }
                    }
                    return;
                }
            }
            if (dERObject instanceof DERSet) {
                Enumeration objects4 = ((ASN1Set) dERObject).getObjects();
                String str7 = str + TAB;
                stringBuffer.append(str);
                stringBuffer.append("DER Set");
                while (true) {
                    stringBuffer.append(property);
                    while (objects4.hasMoreElements()) {
                        Object objNextElement4 = objects4.nextElement();
                        if (objNextElement4 == null) {
                            stringBuffer.append(str7);
                            stringBuffer.append("NULL");
                        } else {
                            _dumpAsString(str7, z, objNextElement4 instanceof DERObject ? (DERObject) objNextElement4 : ((DEREncodable) objNextElement4).getDERObject(), stringBuffer);
                        }
                    }
                    return;
                }
            }
            if (dERObject instanceof DERObjectIdentifier) {
                sbAppend5 = new StringBuilder().append(str).append("ObjectIdentifier(").append(((DERObjectIdentifier) dERObject).getId());
            } else if (dERObject instanceof DERBoolean) {
                sbAppend5 = new StringBuilder().append(str).append("Boolean(").append(((DERBoolean) dERObject).isTrue());
            } else {
                if (dERObject instanceof DERInteger) {
                    sbAppend2 = new StringBuilder().append(str).append("Integer(");
                    value = ((DERInteger) dERObject).getValue();
                } else if (dERObject instanceof BERConstructedOctetString) {
                    ASN1OctetString aSN1OctetString = (ASN1OctetString) dERObject;
                    stringBuffer.append(str + "BER Constructed Octet String[" + aSN1OctetString.getOctets().length + "] ");
                    if (z) {
                        strOutputApplicationSpecific = dumpBinaryDataAsString(str, aSN1OctetString.getOctets());
                        stringBuffer.append(strOutputApplicationSpecific);
                        return;
                    }
                } else {
                    if (!(dERObject instanceof DEROctetString)) {
                        if (dERObject instanceof DERBitString) {
                            DERBitString dERBitString = (DERBitString) dERObject;
                            stringBuffer.append(str + "DER Bit String[" + dERBitString.getBytes().length + ", " + dERBitString.getPadBits() + "] ");
                            if (z) {
                                strOutputApplicationSpecific = dumpBinaryDataAsString(str, dERBitString.getBytes());
                            }
                        } else {
                            if (dERObject instanceof DERIA5String) {
                                sbAppend4 = new StringBuilder().append(str).append("IA5String(");
                                time = ((DERIA5String) dERObject).getString();
                            } else if (dERObject instanceof DERUTF8String) {
                                sbAppend4 = new StringBuilder().append(str).append("UTF8String(");
                                time = ((DERUTF8String) dERObject).getString();
                            } else if (dERObject instanceof DERPrintableString) {
                                sbAppend4 = new StringBuilder().append(str).append("PrintableString(");
                                time = ((DERPrintableString) dERObject).getString();
                            } else if (dERObject instanceof DERVisibleString) {
                                sbAppend4 = new StringBuilder().append(str).append("VisibleString(");
                                time = ((DERVisibleString) dERObject).getString();
                            } else if (dERObject instanceof DERBMPString) {
                                sbAppend4 = new StringBuilder().append(str).append("BMPString(");
                                time = ((DERBMPString) dERObject).getString();
                            } else if (dERObject instanceof DERT61String) {
                                sbAppend4 = new StringBuilder().append(str).append("T61String(");
                                time = ((DERT61String) dERObject).getString();
                            } else if (dERObject instanceof DERUTCTime) {
                                sbAppend4 = new StringBuilder().append(str).append("UTCTime(");
                                time = ((DERUTCTime) dERObject).getTime();
                            } else if (dERObject instanceof DERGeneralizedTime) {
                                sbAppend4 = new StringBuilder().append(str).append("GeneralizedTime(");
                                time = ((DERGeneralizedTime) dERObject).getTime();
                            } else {
                                if (dERObject instanceof DERUnknownTag) {
                                    DERUnknownTag dERUnknownTag = (DERUnknownTag) dERObject;
                                    sbAppend = new StringBuilder().append(str).append("Unknown ").append(Integer.toString(dERUnknownTag.getTag(), 16)).append(" ");
                                    string = new String(Hex.encode(dERUnknownTag.getData()));
                                } else {
                                    if (dERObject instanceof BERApplicationSpecific) {
                                        str2 = ASN1Encodable.BER;
                                    } else if (dERObject instanceof DERApplicationSpecific) {
                                        str2 = ASN1Encodable.DER;
                                    } else if (dERObject instanceof DEREnumerated) {
                                        sbAppend2 = new StringBuilder().append(str).append("DER Enumerated(");
                                        value = ((DEREnumerated) dERObject).getValue();
                                    } else {
                                        if (dERObject instanceof DERExternal) {
                                            DERExternal dERExternal = (DERExternal) dERObject;
                                            stringBuffer.append(str + "External " + property);
                                            String str8 = str + TAB;
                                            if (dERExternal.getDirectReference() != null) {
                                                stringBuffer.append(str8 + "Direct Reference: " + dERExternal.getDirectReference().getId() + property);
                                            }
                                            if (dERExternal.getIndirectReference() != null) {
                                                stringBuffer.append(str8 + "Indirect Reference: " + dERExternal.getIndirectReference().toString() + property);
                                            }
                                            if (dERExternal.getDataValueDescriptor() != null) {
                                                _dumpAsString(str8, z, dERExternal.getDataValueDescriptor(), stringBuffer);
                                            }
                                            stringBuffer.append(str8 + "Encoding: " + dERExternal.getEncoding() + property);
                                            _dumpAsString(str8, z, dERExternal.getExternalContent(), stringBuffer);
                                            return;
                                        }
                                        sbAppend = new StringBuilder().append(str);
                                        string = dERObject.toString();
                                    }
                                    strOutputApplicationSpecific = outputApplicationSpecific(str2, str, z, dERObject, property);
                                }
                                sbAppend3 = sbAppend.append(string);
                                strOutputApplicationSpecific = sbAppend3.append(property).toString();
                            }
                            sbAppend3 = sbAppend4.append(time).append(") ");
                            strOutputApplicationSpecific = sbAppend3.append(property).toString();
                        }
                        stringBuffer.append(strOutputApplicationSpecific);
                        return;
                    }
                    ASN1OctetString aSN1OctetString2 = (ASN1OctetString) dERObject;
                    stringBuffer.append(str + "DER Octet String[" + aSN1OctetString2.getOctets().length + "] ");
                    if (z) {
                        strOutputApplicationSpecific = dumpBinaryDataAsString(str, aSN1OctetString2.getOctets());
                        stringBuffer.append(strOutputApplicationSpecific);
                        return;
                    }
                }
                sbAppend5 = sbAppend2.append(value);
            }
            sbAppend3 = sbAppend5.append(")");
            strOutputApplicationSpecific = sbAppend3.append(property).toString();
            stringBuffer.append(strOutputApplicationSpecific);
            return;
        }
        String str9 = str + TAB;
        stringBuffer.append(str);
        stringBuffer.append(dERObject instanceof BERTaggedObject ? "BER Tagged [" : "Tagged [");
        DERTaggedObject dERTaggedObject = (DERTaggedObject) dERObject;
        stringBuffer.append(Integer.toString(dERTaggedObject.getTagNo()));
        stringBuffer.append(']');
        if (!dERTaggedObject.isExplicit()) {
            stringBuffer.append(" IMPLICIT ");
        }
        stringBuffer.append(property);
        if (!dERTaggedObject.isEmpty()) {
            _dumpAsString(str9, z, dERTaggedObject.getObject(), stringBuffer);
            return;
        } else {
            stringBuffer.append(str9);
            stringBuffer.append("EMPTY");
        }
        stringBuffer.append(property);
    }

    private static String calculateAscString(byte[] bArr, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = i; i3 != i + i2; i3++) {
            byte b = bArr[i3];
            if (b >= 32 && b <= 126) {
                stringBuffer.append((char) b);
            }
        }
        return stringBuffer.toString();
    }

    public static String dumpAsString(Object obj) {
        return dumpAsString(obj, false);
    }

    public static String dumpAsString(Object obj, boolean z) {
        DERObject dERObject;
        StringBuffer stringBuffer = new StringBuffer();
        if (obj instanceof DERObject) {
            dERObject = (DERObject) obj;
        } else {
            if (!(obj instanceof DEREncodable)) {
                return "unknown object type " + obj.toString();
            }
            dERObject = ((DEREncodable) obj).getDERObject();
        }
        _dumpAsString("", z, dERObject, stringBuffer);
        return stringBuffer.toString();
    }

    private static String dumpBinaryDataAsString(String str, byte[] bArr) {
        String strCalculateAscString;
        String property = System.getProperty("line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        String str2 = str + TAB;
        stringBuffer.append(property);
        for (int i = 0; i < bArr.length; i += 32) {
            int length = bArr.length - i;
            stringBuffer.append(str2);
            if (length > 32) {
                stringBuffer.append(new String(Hex.encode(bArr, i, 32)));
                stringBuffer.append(TAB);
                strCalculateAscString = calculateAscString(bArr, i, 32);
            } else {
                stringBuffer.append(new String(Hex.encode(bArr, i, bArr.length - i)));
                for (int length2 = bArr.length - i; length2 != 32; length2++) {
                    stringBuffer.append("  ");
                }
                stringBuffer.append(TAB);
                strCalculateAscString = calculateAscString(bArr, i, bArr.length - i);
            }
            stringBuffer.append(strCalculateAscString);
            stringBuffer.append(property);
        }
        return stringBuffer.toString();
    }

    private static String outputApplicationSpecific(String str, String str2, boolean z, DERObject dERObject, String str3) {
        DERApplicationSpecific dERApplicationSpecific = (DERApplicationSpecific) dERObject;
        StringBuffer stringBuffer = new StringBuffer();
        if (!dERApplicationSpecific.isConstructed()) {
            return str2 + str + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "] (" + new String(Hex.encode(dERApplicationSpecific.getContents())) + ")" + str3;
        }
        try {
            ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(dERApplicationSpecific.getObject(16));
            stringBuffer.append(str2 + str + " ApplicationSpecific[" + dERApplicationSpecific.getApplicationTag() + "]" + str3);
            Enumeration objects = aSN1Sequence.getObjects();
            while (objects.hasMoreElements()) {
                _dumpAsString(str2 + TAB, z, (DERObject) objects.nextElement(), stringBuffer);
            }
        } catch (IOException e) {
            stringBuffer.append(e);
        }
        return stringBuffer.toString();
    }
}
