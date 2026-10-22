package com.amap.api.mapcore.util;

import java.util.Locale;
import java.util.Random;

/* JADX INFO: compiled from: RandomUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class dq {
    private static String a = "0123456789";

    public static String a() {
        Random random = new Random();
        String str = String.format(Locale.US, "%05d", Integer.valueOf(random.nextInt(10)));
        int iNextInt = random.nextInt(10);
        int iNextInt2 = random.nextInt(100);
        return new a(a, iNextInt2).a(iNextInt, str) + String.format(Locale.US, "%01d", Integer.valueOf(iNextInt)) + String.format(Locale.US, "%02d", Integer.valueOf(iNextInt2));
    }

    /* JADX INFO: compiled from: RandomUtil.java */
    static class a {
        private String a;
        private int b;
        private int c;

        public a(String str, int i) {
            this.b = 1103515245;
            this.c = 12345;
            this.a = a(str, i, str.length());
        }

        public a() {
            this(11);
        }

        public a(int i) {
            this("ABCDEFGHIJKLMNOPQRSTUVWXYZ", i);
        }

        public char a(int i, boolean z) {
            int length = this.a.length();
            if (z) {
                i = length - i;
            }
            return this.a.charAt(i);
        }

        public int a(char c, boolean z) {
            int length = this.a.length();
            int iIndexOf = this.a.indexOf(c);
            return z ? length - iIndexOf : iIndexOf;
        }

        public int a(int i) {
            return (int) (((((long) i) * ((long) this.b)) + ((long) this.c)) & 2147483647L);
        }

        public String a(String str, int i, int i2) {
            StringBuffer stringBuffer = new StringBuffer(str);
            int length = str.length();
            for (int i3 = 0; i3 < i2; i3++) {
                int iA = a(i);
                int i4 = iA % length;
                i = a(iA);
                int i5 = i % length;
                char cCharAt = stringBuffer.charAt(i4);
                stringBuffer.setCharAt(i4, stringBuffer.charAt(i5));
                stringBuffer.setCharAt(i5, cCharAt);
            }
            return stringBuffer.toString();
        }

        public String a(boolean z, int i, String str) {
            StringBuilder sb = new StringBuilder();
            int length = this.a.length();
            int length2 = str.length();
            for (int i2 = 0; i2 < length2; i2++) {
                int iA = a(str.charAt(i2), z);
                if (iA < 0) {
                    break;
                }
                sb.append(a(((iA + i) + i2) % length, z));
            }
            if (sb.length() == length2) {
                return sb.toString();
            }
            return null;
        }

        public String a(int i, String str) {
            return a(false, i, str);
        }
    }
}
