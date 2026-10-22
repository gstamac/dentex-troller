package org.bouncycastle.asn1.x509;

/* JADX INFO: loaded from: classes2.dex */
public class X509NameTokenizer {
    private StringBuffer buf;
    private int index;
    private char seperator;
    private String value;

    public X509NameTokenizer(String str) {
        this(str, ',');
    }

    public X509NameTokenizer(String str, char c) {
        this.buf = new StringBuffer();
        this.value = str;
        this.index = -1;
        this.seperator = c;
    }

    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0059  */
    /* JADX WARN: Code duplicated, block: B:29:0x005f  */
    public String nextToken() {
        if (this.index == this.value.length()) {
            return null;
        }
        int i = this.index + 1;
        this.buf.setLength(0);
        boolean z = false;
        boolean z2 = false;
        while (i != this.value.length()) {
            char cCharAt = this.value.charAt(i);
            if (cCharAt == '\"') {
                if (!z) {
                    z2 = !z2;
                }
                z = false;
                i++;
            } else {
                if (z || z2) {
                    if (cCharAt == '#') {
                        StringBuffer stringBuffer = this.buf;
                        if (stringBuffer.charAt(stringBuffer.length() - 1) == '=') {
                            this.buf.append('\\');
                        } else if (cCharAt == '+' && this.seperator != '+') {
                            this.buf.append('\\');
                        }
                    } else if (cCharAt == '+') {
                        this.buf.append('\\');
                    }
                } else if (cCharAt == '\\') {
                    z = true;
                } else {
                    if (cCharAt == this.seperator) {
                        break;
                    }
                    this.buf.append(cCharAt);
                }
                i++;
            }
            this.buf.append(cCharAt);
            z = false;
            i++;
        }
        this.index = i;
        return this.buf.toString().trim();
    }
}
