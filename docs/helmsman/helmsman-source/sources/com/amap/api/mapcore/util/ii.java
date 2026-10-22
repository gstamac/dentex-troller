package com.amap.api.mapcore.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.codeless.internal.Constants;
import java.util.ArrayList;
import java.util.zip.CRC32;

/* JADX INFO: compiled from: Req.java */
/* JADX INFO: loaded from: classes.dex */
public final class ii {
    public static String K;
    public String a = "1";
    public short b = 0;
    public String c = null;
    public String d = null;
    public String e = null;
    public String f = null;
    public String g = null;
    public String h = null;
    public String i = null;
    public String j = null;
    public String k = null;
    public String l = null;
    public String m = null;
    public String n = null;
    public String o = null;
    public String p = null;
    public String q = null;
    public String r = null;
    public String s = null;
    public String t = null;
    public String u = null;
    public String v = null;
    public String w = null;
    public String x = null;
    public String y = null;
    public int z = 0;
    public String A = null;
    public String B = null;
    public ArrayList<ic> C = new ArrayList<>();
    public String D = null;
    public String E = null;
    public ArrayList<ScanResult> F = new ArrayList<>();
    public ArrayList<ia> G = new ArrayList<>();
    public String H = null;
    public String I = null;
    public byte[] J = null;
    private byte[] M = null;
    private int N = 0;
    public String L = null;

    private String a(String str, int i) {
        String[] strArrSplit = this.B.split("\\*")[i].split(",");
        if (str.equals("lac")) {
            return strArrSplit[0];
        }
        if (str.equals("cellid")) {
            return strArrSplit[1];
        }
        if (str.equals("signal")) {
            return strArrSplit[2];
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:6:0x000f A[Catch: all -> 0x003e, TryCatch #0 {all -> 0x003e, blocks: (B:4:0x000c, B:10:0x001c, B:12:0x001f, B:14:0x0028, B:15:0x0030, B:6:0x000f, B:8:0x0014), top: B:20:0x000c }] */
    /* JADX WARN: Code duplicated, block: B:8:0x0014 A[Catch: all -> 0x003e, LOOP:0: B:7:0x0012->B:8:0x0014, LOOP_END, TryCatch #0 {all -> 0x003e, blocks: (B:4:0x000c, B:10:0x001c, B:12:0x001f, B:14:0x0028, B:15:0x0030, B:6:0x000f, B:8:0x0014), top: B:20:0x000c }] */
    private byte[] a(String str) {
        int i;
        String[] strArrSplit = str.split(":");
        byte[] bArr = new byte[6];
        if (strArrSplit != null) {
            try {
                if (strArrSplit.length != 6) {
                    strArrSplit = new String[6];
                    for (i = 0; i < 6; i++) {
                        strArrSplit[i] = AppEventsConstants.EVENT_PARAM_VALUE_NO;
                    }
                }
            } catch (Throwable th) {
                ij.a(th, "Req", "getMacBa " + str);
                return a("00:00:00:00:00:00");
            }
        } else {
            strArrSplit = new String[6];
            while (i < 6) {
                strArrSplit[i] = AppEventsConstants.EVENT_PARAM_VALUE_NO;
            }
        }
        for (int i2 = 0; i2 < strArrSplit.length; i2++) {
            if (strArrSplit[i2].length() > 2) {
                strArrSplit[i2] = strArrSplit[i2].substring(0, 2);
            }
            bArr[i2] = (byte) Integer.parseInt(strArrSplit[i2], 16);
        }
        return bArr;
    }

    private String b(String str) {
        if (!this.A.contains(str + ">")) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        return this.A.substring(this.A.indexOf(str + ">") + str.length() + 1, this.A.indexOf("</" + str));
    }

    private void b() {
        if (TextUtils.isEmpty(this.a)) {
            this.a = "";
        }
        if (TextUtils.isEmpty(this.c)) {
            this.c = "";
        }
        if (TextUtils.isEmpty(this.d)) {
            this.d = "";
        }
        if (TextUtils.isEmpty(this.e)) {
            this.e = "";
        }
        if (TextUtils.isEmpty(this.f)) {
            this.f = "";
        }
        if (TextUtils.isEmpty(this.g)) {
            this.g = "";
        }
        if (TextUtils.isEmpty(this.h)) {
            this.h = "";
        }
        if (TextUtils.isEmpty(this.i)) {
            this.i = "";
        }
        if (TextUtils.isEmpty(this.j) || (!this.j.equals("1") && !this.j.equals("2"))) {
            this.j = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        if (TextUtils.isEmpty(this.k) || (!this.k.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) && !this.k.equals("1"))) {
            this.k = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        if (TextUtils.isEmpty(this.l)) {
            this.l = "";
        }
        if (TextUtils.isEmpty(this.m)) {
            this.m = "";
        }
        if (TextUtils.isEmpty(this.n)) {
            this.n = "";
        }
        if (TextUtils.isEmpty(this.o)) {
            this.o = "";
        }
        if (TextUtils.isEmpty(this.p)) {
            this.p = "";
        }
        if (TextUtils.isEmpty(this.q)) {
            this.q = "";
        }
        if (TextUtils.isEmpty(this.r)) {
            this.r = "";
        }
        if (TextUtils.isEmpty(this.s)) {
            this.s = "";
        }
        if (TextUtils.isEmpty(this.t)) {
            this.t = "";
        }
        if (TextUtils.isEmpty(this.u)) {
            this.u = "";
        }
        if (TextUtils.isEmpty(this.v)) {
            this.v = "";
        }
        if (TextUtils.isEmpty(this.w)) {
            this.w = "";
        }
        if (TextUtils.isEmpty(this.x)) {
            this.x = "";
        }
        if (TextUtils.isEmpty(this.y) || (!this.y.equals("1") && !this.y.equals("2"))) {
            this.y = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        if (!id.a(this.z)) {
            this.z = 0;
        }
        if (TextUtils.isEmpty(this.A)) {
            this.A = "";
        }
        if (TextUtils.isEmpty(this.B)) {
            this.B = "";
        }
        if (TextUtils.isEmpty(this.E)) {
            this.E = "";
        }
        if (TextUtils.isEmpty(this.H)) {
            this.H = "";
        }
        if (TextUtils.isEmpty(this.I)) {
            this.I = "";
        }
        if (TextUtils.isEmpty(K)) {
            K = "";
        }
        if (this.J == null) {
            this.J = new byte[0];
        }
    }

    public final void a(Context context, boolean z, boolean z2, id idVar, ie ieVar, ConnectivityManager connectivityManager, String str, String str2) {
        String str3;
        String str4;
        NetworkInfo activeNetworkInfo;
        String strB;
        String str5;
        String string;
        StringBuilder sb;
        ArrayList<ScanResult> arrayList;
        int length;
        String strF = ew.f(context);
        int iF = im.f();
        this.L = str2;
        if (z2) {
            str3 = "api_serverSDK_130905";
            str4 = "S128DF1572465B890OE3F7A13167KLEI";
        } else {
            str3 = "UC_nlp_20131029";
            str4 = "BKZCHMBBSSUK7U8GLUKHBB56CCFF78U";
        }
        String str6 = str4;
        String str7 = str3;
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        int iC = idVar.c();
        int iD = idVar.d();
        TelephonyManager telephonyManagerE = idVar.e();
        ArrayList<ic> arrayListA = idVar.a();
        ArrayList<ic> arrayListB = idVar.b();
        ArrayList<ScanResult> arrayListA2 = ieVar.a();
        String str8 = iD == 2 ? "1" : AppEventsConstants.EVENT_PARAM_VALUE_NO;
        if (telephonyManagerE != null) {
            if (TextUtils.isEmpty(ij.d)) {
                try {
                    ij.d = fa.q(context);
                } catch (Throwable th) {
                    ij.a(th, "APS", "getApsReq part4");
                }
            }
            if (TextUtils.isEmpty(ij.d)) {
                ij.d = "888888888888888";
            }
            if (TextUtils.isEmpty(ij.e)) {
                try {
                    ij.e = telephonyManagerE.getSubscriberId();
                } catch (SecurityException unused) {
                } catch (Throwable th2) {
                    ij.a(th2, "APS", "getApsReq part2");
                }
            }
            if (TextUtils.isEmpty(ij.e)) {
                ij.e = "888888888888888";
            }
        } else {
            strF = strF;
            iF = iF;
        }
        try {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Throwable th3) {
            ij.a(th3, "APS", "getApsReq part");
            activeNetworkInfo = null;
        }
        WifiInfo wifiInfoF = ieVar.f();
        boolean zA = ie.a(wifiInfoF);
        if (im.a(activeNetworkInfo) != -1) {
            strB = im.b(telephonyManagerE);
            str5 = (zA && ieVar.e()) ? "2" : "1";
        } else {
            strB = "";
            str5 = strB;
        }
        String str9 = str5;
        String str10 = strB;
        if (arrayListA.isEmpty()) {
            sb3 = sb3;
            string = "";
        } else {
            StringBuilder sb4 = new StringBuilder();
            if (iD == 1) {
                ic icVar = arrayListA.get(0);
                sb4.delete(0, sb4.length());
                sb4.append("<mcc>").append(icVar.a).append("</mcc>");
                sb4.append("<mnc>").append(icVar.b).append("</mnc>");
                sb4.append("<lac>").append(icVar.c).append("</lac>");
                sb4.append("<cellid>").append(icVar.d);
                sb4.append("</cellid>");
                sb4.append("<signal>").append(icVar.j);
                sb4.append("</signal>");
                string = sb4.toString();
                for (int i = 1; i < arrayListA.size(); i++) {
                    ic icVar2 = arrayListA.get(i);
                    sb2.append(icVar2.c).append(",");
                    sb2.append(icVar2.d).append(",");
                    sb2.append(icVar2.j);
                    if (i < arrayListA.size() - 1) {
                        sb2.append("*");
                    }
                }
            } else if (iD != 2) {
                string = "";
            } else {
                ic icVar3 = arrayListA.get(0);
                sb4.delete(0, sb4.length());
                sb4.append("<mcc>").append(icVar3.a).append("</mcc>");
                sb4.append("<sid>").append(icVar3.g).append("</sid>");
                sb4.append("<nid>").append(icVar3.h).append("</nid>");
                sb4.append("<bid>").append(icVar3.i).append("</bid>");
                if (icVar3.f > 0 && icVar3.e > 0) {
                    sb4.append("<lon>").append(icVar3.f).append("</lon>");
                    sb4.append("<lat>").append(icVar3.e).append("</lat>");
                }
                sb4.append("<signal>").append(icVar3.j).append("</signal>");
                string = sb4.toString();
            }
            sb4.delete(0, sb4.length());
        }
        if ((iC & 4) != 4 || arrayListB.isEmpty()) {
            this.C.clear();
        } else {
            this.C.clear();
            this.C.addAll(arrayListB);
        }
        if (ieVar.e()) {
            if (zA) {
                sb = sb3;
                sb.append(ieVar.f().getBSSID()).append(",");
                int rssi = ieVar.f().getRssi();
                if (rssi < -128 || rssi > 127) {
                    rssi = 0;
                }
                sb.append(rssi).append(",");
                String ssid = wifiInfoF.getSSID();
                try {
                    length = wifiInfoF.getSSID().getBytes("UTF-8").length;
                } catch (Exception unused2) {
                    length = 32;
                }
                if (length >= 32) {
                    ssid = "unkwn";
                }
                sb.append(ssid.replace("*", "."));
            } else {
                sb = sb3;
            }
            if (arrayListA2 != 0 && (arrayList = this.F) != null) {
                arrayList.clear();
                this.F.addAll(arrayListA2);
            }
        } else {
            sb = sb3;
            ieVar.b();
            ArrayList<ScanResult> arrayList2 = this.F;
            if (arrayList2 != null) {
                arrayList2.clear();
            }
        }
        if (z) {
            this.b = (short) 0;
        } else {
            this.b = (short) 2;
        }
        this.c = str7;
        this.d = str6;
        this.f = im.d();
        this.g = Constants.PLATFORM + im.e();
        this.h = im.b(context);
        this.i = str8;
        this.j = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.k = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.l = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.m = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.n = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.o = strF;
        this.p = ij.d;
        this.q = ij.e;
        this.s = String.valueOf(iF);
        this.t = str;
        this.v = "3.6.0";
        this.w = null;
        this.u = "";
        this.x = str10;
        this.y = str9;
        this.z = iC;
        this.A = string;
        this.B = sb2.toString();
        this.D = idVar.i();
        this.H = ie.i();
        this.E = sb.toString();
        try {
            if (TextUtils.isEmpty(K)) {
                K = fa.f(context);
            }
        } catch (Throwable unused3) {
        }
        sb2.delete(0, sb2.length());
        sb.delete(0, sb.length());
    }

    public final byte[] a() {
        int length;
        int length2;
        int length3;
        int length4;
        int length5;
        int length6;
        int length7;
        int length8;
        int length9;
        int length10;
        int length11;
        int length12;
        int length13;
        int length14;
        int length15;
        int i;
        byte b;
        int length16;
        int length17;
        int i2;
        int length18;
        int i3;
        int i4;
        int length19;
        int i5;
        int length20;
        int i6;
        int length21;
        int i7;
        int length22;
        b();
        byte[] bArr = new byte[2];
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = this.J;
        int length23 = bArr3 != null ? 4096 + bArr3.length + 1 : 4096;
        byte[] bArr4 = this.M;
        if (bArr4 == null || length23 > this.N) {
            bArr4 = new byte[length23];
            this.M = bArr4;
            this.N = length23;
        }
        byte[] bArr5 = bArr4;
        bArr5[0] = Byte.parseByte(this.a);
        byte[] bArrA = im.a(this.b, (byte[]) null);
        System.arraycopy(bArrA, 0, bArr5, 1, bArrA.length);
        int length24 = bArrA.length + 1;
        try {
            byte[] bytes = this.c.getBytes("GBK");
            bArr5[length24] = (byte) bytes.length;
            length24++;
            System.arraycopy(bytes, 0, bArr5, length24, bytes.length);
            length = length24 + bytes.length;
        } catch (Throwable th) {
            ij.a(th, "Req", "buildV4Dot2");
            bArr5[length24] = 0;
            length = length24 + 1;
        }
        try {
            byte[] bytes2 = this.d.getBytes("GBK");
            bArr5[length] = (byte) bytes2.length;
            length++;
            System.arraycopy(bytes2, 0, bArr5, length, bytes2.length);
            length2 = length + bytes2.length;
        } catch (Throwable th2) {
            ij.a(th2, "Req", "buildV4Dot21");
            bArr5[length] = 0;
            length2 = length + 1;
        }
        try {
            byte[] bytes3 = this.o.getBytes("GBK");
            bArr5[length2] = (byte) bytes3.length;
            length2++;
            System.arraycopy(bytes3, 0, bArr5, length2, bytes3.length);
            length3 = length2 + bytes3.length;
        } catch (Throwable th3) {
            ij.a(th3, "Req", "buildV4Dot22");
            bArr5[length2] = 0;
            length3 = length2 + 1;
        }
        try {
            byte[] bytes4 = this.e.getBytes("GBK");
            bArr5[length3] = (byte) bytes4.length;
            length3++;
            System.arraycopy(bytes4, 0, bArr5, length3, bytes4.length);
            length4 = length3 + bytes4.length;
        } catch (Throwable th4) {
            ij.a(th4, "Req", "buildV4Dot23");
            bArr5[length3] = 0;
            length4 = length3 + 1;
        }
        try {
            byte[] bytes5 = this.f.getBytes("GBK");
            bArr5[length4] = (byte) bytes5.length;
            length4++;
            System.arraycopy(bytes5, 0, bArr5, length4, bytes5.length);
            length5 = length4 + bytes5.length;
        } catch (Throwable th5) {
            ij.a(th5, "Req", "buildV4Dot24");
            bArr5[length4] = 0;
            length5 = length4 + 1;
        }
        try {
            byte[] bytes6 = this.g.getBytes("GBK");
            bArr5[length5] = (byte) bytes6.length;
            length5++;
            System.arraycopy(bytes6, 0, bArr5, length5, bytes6.length);
            length6 = length5 + bytes6.length;
        } catch (Throwable th6) {
            ij.a(th6, "Req", "buildV4Dot25");
            bArr5[length5] = 0;
            length6 = length5 + 1;
        }
        try {
            byte[] bytes7 = this.u.getBytes("GBK");
            bArr5[length6] = (byte) bytes7.length;
            length6++;
            System.arraycopy(bytes7, 0, bArr5, length6, bytes7.length);
            length7 = length6 + bytes7.length;
        } catch (Throwable th7) {
            ij.a(th7, "Req", "buildV4Dot26");
            bArr5[length6] = 0;
            length7 = length6 + 1;
        }
        try {
            byte[] bytes8 = this.h.getBytes("GBK");
            bArr5[length7] = (byte) bytes8.length;
            length7++;
            System.arraycopy(bytes8, 0, bArr5, length7, bytes8.length);
            length8 = length7 + bytes8.length;
        } catch (Throwable th8) {
            ij.a(th8, "Req", "buildV4Dot27");
            bArr5[length7] = 0;
            length8 = length7 + 1;
        }
        try {
            byte[] bytes9 = this.p.getBytes("GBK");
            bArr5[length8] = (byte) bytes9.length;
            length8++;
            System.arraycopy(bytes9, 0, bArr5, length8, bytes9.length);
            length9 = length8 + bytes9.length;
        } catch (Throwable th9) {
            ij.a(th9, "Req", "buildV4Dot28");
            bArr5[length8] = 0;
            length9 = length8 + 1;
        }
        try {
            byte[] bytes10 = this.q.getBytes("GBK");
            bArr5[length9] = (byte) bytes10.length;
            length9++;
            System.arraycopy(bytes10, 0, bArr5, length9, bytes10.length);
            length10 = length9 + bytes10.length;
        } catch (Throwable th10) {
            ij.a(th10, "Req", "buildV4Dot29");
            bArr5[length9] = 0;
            length10 = length9 + 1;
        }
        try {
            if (TextUtils.isEmpty(this.t)) {
                bArr5[length10] = 0;
                length11 = length10 + 1;
            } else {
                byte[] bArrA2 = a(this.t);
                bArr5[length10] = (byte) bArrA2.length;
                int i8 = length10 + 1;
                System.arraycopy(bArrA2, 0, bArr5, i8, bArrA2.length);
                length11 = i8 + bArrA2.length;
            }
        } catch (Throwable th11) {
            ij.a(th11, "Req", "buildV4Dot219");
            bArr5[length10] = 0;
        }
        try {
            byte[] bytes11 = this.v.getBytes("GBK");
            bArr5[length11] = (byte) bytes11.length;
            length11++;
            System.arraycopy(bytes11, 0, bArr5, length11, bytes11.length);
            length12 = length11 + bytes11.length;
        } catch (Throwable th12) {
            ij.a(th12, "Req", "buildV4Dot211");
            bArr5[length11] = 0;
            length12 = length11 + 1;
        }
        try {
            byte[] bytes12 = this.w.getBytes("GBK");
            bArr5[length12] = (byte) bytes12.length;
            length12++;
            System.arraycopy(bytes12, 0, bArr5, length12, bytes12.length);
            length13 = length12 + bytes12.length;
        } catch (Throwable th13) {
            ij.a(th13, "Req", "buildV4Dot212");
            bArr5[length12] = 0;
            length13 = length12 + 1;
        }
        try {
            if (TextUtils.isEmpty(K)) {
                bArr5[length13] = 0;
                length14 = length13 + 1;
            } else {
                byte[] bytes13 = K.getBytes("GBK");
                bArr5[length13] = (byte) bytes13.length;
                int i9 = length13 + 1;
                System.arraycopy(bytes13, 0, bArr5, i9, bytes13.length);
                length14 = i9 + bytes13.length;
            }
        } catch (Throwable th14) {
            ij.a(th14, "Req", "buildV4Dot212");
            bArr5[length13] = 0;
        }
        try {
            byte[] bytes14 = this.x.getBytes("GBK");
            bArr5[length14] = (byte) bytes14.length;
            length14++;
            System.arraycopy(bytes14, 0, bArr5, length14, bytes14.length);
            length15 = length14 + bytes14.length;
        } catch (Throwable th15) {
            ij.a(th15, "Req", "buildV4Dot213");
            bArr5[length14] = 0;
            length15 = length14 + 1;
        }
        bArr5[length15] = Byte.parseByte(this.y);
        int i10 = length15 + 1;
        bArr5[i10] = Byte.parseByte(this.j);
        int i11 = i10 + 1;
        int i12 = this.z;
        int i13 = i12 & 3;
        bArr5[i11] = (byte) i12;
        int i14 = i11 + 1;
        int i15 = 127;
        if (i13 == 1) {
            byte[] bArrB = im.b(b("mcc"));
            System.arraycopy(bArrB, 0, bArr5, i14, bArrB.length);
            int length25 = i14 + bArrB.length;
            byte[] bArrB2 = im.b(b("mnc"));
            System.arraycopy(bArrB2, 0, bArr5, length25, bArrB2.length);
            int length26 = length25 + bArrB2.length;
            byte[] bArrB3 = im.b(b("lac"));
            System.arraycopy(bArrB3, 0, bArr5, length26, bArrB3.length);
            int length27 = length26 + bArrB3.length;
            byte[] bArrC = im.c(b("cellid"));
            System.arraycopy(bArrC, 0, bArr5, length27, bArrC.length);
            int length28 = length27 + bArrC.length;
            int i16 = Integer.parseInt(b("signal"));
            if (i16 > 127 || i16 < -128) {
                i16 = 0;
            }
            bArr5[length28] = (byte) i16;
            int i17 = length28 + 1;
            byte[] bArrA3 = im.a(0, bArr);
            System.arraycopy(bArrA3, 0, bArr5, i17, bArrA3.length);
            int i18 = i17 + 2;
            if (this.B.length() == 0) {
                bArr5[i18] = 0;
                i14 = i18 + 1;
            } else {
                int length29 = this.B.split("\\*").length;
                bArr5[i18] = (byte) length29;
                i14 = i18 + 1;
                for (int i19 = 0; i19 < length29; i19++) {
                    byte[] bArrB4 = im.b(a("lac", i19));
                    System.arraycopy(bArrB4, 0, bArr5, i14, bArrB4.length);
                    int length30 = i14 + bArrB4.length;
                    byte[] bArrC2 = im.c(a("cellid", i19));
                    System.arraycopy(bArrC2, 0, bArr5, length30, bArrC2.length);
                    int length31 = length30 + bArrC2.length;
                    int i20 = Integer.parseInt(a("signal", i19));
                    if (i20 > 127 || i20 < -128) {
                        i20 = 0;
                    }
                    bArr5[length31] = (byte) i20;
                    i14 = length31 + 1;
                }
            }
        } else if (i13 == 2) {
            byte[] bArrB5 = im.b(b("mcc"));
            System.arraycopy(bArrB5, 0, bArr5, i14, bArrB5.length);
            int length32 = i14 + bArrB5.length;
            byte[] bArrB6 = im.b(b("sid"));
            System.arraycopy(bArrB6, 0, bArr5, length32, bArrB6.length);
            int length33 = length32 + bArrB6.length;
            byte[] bArrB7 = im.b(b("nid"));
            System.arraycopy(bArrB7, 0, bArr5, length33, bArrB7.length);
            int length34 = length33 + bArrB7.length;
            byte[] bArrB8 = im.b(b("bid"));
            System.arraycopy(bArrB8, 0, bArr5, length34, bArrB8.length);
            int length35 = length34 + bArrB8.length;
            byte[] bArrC3 = im.c(b("lon"));
            System.arraycopy(bArrC3, 0, bArr5, length35, bArrC3.length);
            int length36 = length35 + bArrC3.length;
            byte[] bArrC4 = im.c(b("lat"));
            System.arraycopy(bArrC4, 0, bArr5, length36, bArrC4.length);
            int length37 = length36 + bArrC4.length;
            int i21 = Integer.parseInt(b("signal"));
            if (i21 > 127 || i21 < -128) {
                i21 = 0;
            }
            bArr5[length37] = (byte) i21;
            int i22 = length37 + 1;
            byte[] bArrA4 = im.a(0, bArr);
            System.arraycopy(bArrA4, 0, bArr5, i22, bArrA4.length);
            int i23 = i22 + 2;
            bArr5[i23] = 0;
            i14 = i23 + 1;
        }
        String str = this.D;
        if (str == null || (this.z & 8) != 8) {
            bArr5[i14] = 0;
            i = i14 + 1;
        } else {
            try {
                byte[] bytes15 = str.getBytes("GBK");
                int iMin = Math.min(bytes15.length, 60);
                bArr5[i14] = (byte) iMin;
                i14++;
                System.arraycopy(bytes15, 0, bArr5, i14, iMin);
                i = i14 + iMin;
            } catch (Exception unused) {
                bArr5[i14] = 0;
                i = i14 + 1;
            }
        }
        ArrayList<ic> arrayList = this.C;
        int size = arrayList.size();
        if ((this.z & 4) != 4 || size <= 0) {
            bArr5[i] = 0;
            b = 1;
            length16 = i + 1;
        } else {
            if (!arrayList.get(0).o) {
                size--;
            }
            bArr5[i] = (byte) size;
            byte b2 = 1;
            length16 = i + 1;
            int i24 = 0;
            while (i24 < size) {
                ic icVar = arrayList.get(i24);
                if (icVar.o) {
                    int i25 = 99;
                    if (icVar.k == b2 || icVar.k == 3 || icVar.k == 4) {
                        byte b3 = (byte) icVar.k;
                        if (icVar.n) {
                            b3 = (byte) (b3 | 8);
                        }
                        bArr5[length16] = b3;
                        int i26 = length16 + 1;
                        byte[] bArrA5 = im.a(icVar.a, bArr);
                        System.arraycopy(bArrA5, 0, bArr5, i26, bArrA5.length);
                        int length38 = i26 + bArrA5.length;
                        byte[] bArrA6 = im.a(icVar.b, bArr);
                        System.arraycopy(bArrA6, 0, bArr5, length38, bArrA6.length);
                        int length39 = length38 + bArrA6.length;
                        byte[] bArrA7 = im.a(icVar.c, bArr);
                        System.arraycopy(bArrA7, 0, bArr5, length39, bArrA7.length);
                        int length40 = length39 + bArrA7.length;
                        byte[] bArrA8 = im.a(icVar.d, bArr2);
                        System.arraycopy(bArrA8, 0, bArr5, length40, bArrA8.length);
                        int length41 = length40 + bArrA8.length;
                        int i27 = icVar.j;
                        if (i27 <= 127 && i27 >= -128) {
                            i25 = i27;
                        }
                        bArr5[length41] = (byte) i25;
                        i7 = length41 + 1;
                        byte[] bArrA9 = im.a(icVar.l, bArr);
                        System.arraycopy(bArrA9, 0, bArr5, i7, bArrA9.length);
                        length22 = bArrA9.length;
                    } else if (icVar.k == 2) {
                        byte b4 = (byte) icVar.k;
                        if (icVar.n) {
                            b4 = (byte) (b4 | 8);
                        }
                        bArr5[length16] = b4;
                        int i28 = length16 + 1;
                        byte[] bArrA10 = im.a(icVar.a, bArr);
                        System.arraycopy(bArrA10, 0, bArr5, i28, bArrA10.length);
                        int length42 = i28 + bArrA10.length;
                        byte[] bArrA11 = im.a(icVar.g, bArr);
                        System.arraycopy(bArrA11, 0, bArr5, length42, bArrA11.length);
                        int length43 = length42 + bArrA11.length;
                        byte[] bArrA12 = im.a(icVar.h, bArr);
                        System.arraycopy(bArrA12, 0, bArr5, length43, bArrA12.length);
                        int length44 = length43 + bArrA12.length;
                        byte[] bArrA13 = im.a(icVar.i, bArr);
                        System.arraycopy(bArrA13, 0, bArr5, length44, bArrA13.length);
                        int length45 = length44 + bArrA13.length;
                        byte[] bArrB9 = im.b(icVar.f, bArr2);
                        System.arraycopy(bArrB9, 0, bArr5, length45, bArrB9.length);
                        int length46 = length45 + bArrB9.length;
                        byte[] bArrB10 = im.b(icVar.e, bArr2);
                        System.arraycopy(bArrB10, 0, bArr5, length46, bArrB10.length);
                        int length47 = length46 + bArrB10.length;
                        int i29 = icVar.j;
                        if (i29 <= 127 && i29 >= -128) {
                            i25 = i29;
                        }
                        bArr5[length47] = (byte) i25;
                        i7 = length47 + 1;
                        byte[] bArrA14 = im.a(icVar.l, bArr);
                        System.arraycopy(bArrA14, 0, bArr5, i7, bArrA14.length);
                        length22 = bArrA14.length;
                    }
                    length16 = i7 + length22;
                }
                i24++;
                b2 = 1;
            }
            b = b2;
        }
        if (this.E.length() == 0) {
            bArr5[length16] = 0;
        } else {
            bArr5[length16] = b;
            length16 += b;
            try {
                String[] strArrSplit = this.E.split(",");
                byte[] bArrA15 = a(strArrSplit[0]);
                System.arraycopy(bArrA15, 0, bArr5, length16, bArrA15.length);
                length16 += bArrA15.length;
                try {
                    byte[] bytes16 = strArrSplit[2].getBytes("GBK");
                    bArr5[length16] = (byte) bytes16.length;
                    length16++;
                    System.arraycopy(bytes16, 0, bArr5, length16, bytes16.length);
                    length16 += bytes16.length;
                } catch (Throwable th16) {
                    ij.a(th16, "Req", "buildV4Dot214");
                    bArr5[length16] = 0;
                    length16++;
                }
                int i30 = Integer.parseInt(strArrSplit[1]);
                if (i30 > 127 || i30 < -128) {
                    i30 = 0;
                }
                bArr5[length16] = Byte.parseByte(String.valueOf(i30));
                b = 1;
            } catch (Throwable th17) {
                ij.a(th17, "Req", "buildV4Dot216");
                byte[] bArrA16 = a("00:00:00:00:00:00");
                System.arraycopy(bArrA16, 0, bArr5, length16, bArrA16.length);
                int length48 = length16 + bArrA16.length;
                bArr5[length48] = 0;
                b = 1;
                length16 = length48 + 1;
                bArr5[length16] = Byte.parseByte(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            }
        }
        int i31 = length16 + b;
        ArrayList<ScanResult> arrayList2 = this.F;
        int iMin2 = Math.min(arrayList2.size(), 25);
        if (iMin2 == 0) {
            bArr5[i31] = 0;
            length17 = i31 + b;
        } else {
            bArr5[i31] = (byte) iMin2;
            int length49 = i31 + b;
            boolean z = im.c() >= 17;
            long jB = z ? im.b() / 1000 : 0L;
            int i32 = 0;
            while (i32 < iMin2) {
                ScanResult scanResult = arrayList2.get(i32);
                byte[] bArrA17 = a(scanResult.BSSID);
                System.arraycopy(bArrA17, 0, bArr5, length49, bArrA17.length);
                int length50 = length49 + bArrA17.length;
                try {
                    byte[] bytes17 = scanResult.SSID.getBytes("GBK");
                    bArr5[length50] = (byte) bytes17.length;
                    length50++;
                    System.arraycopy(bytes17, 0, bArr5, length50, bytes17.length);
                    length18 = length50 + bytes17.length;
                    i2 = 1;
                } catch (Exception unused2) {
                    bArr5[length50] = 0;
                    i2 = 1;
                    length18 = length50 + 1;
                }
                int i33 = scanResult.level;
                if (i33 > i15 || i33 < -128) {
                    i33 = 0;
                }
                bArr5[length18] = Byte.parseByte(String.valueOf(i33));
                int i34 = length18 + i2;
                if (!z || (i3 = (int) (((jB - scanResult.timestamp) / 1000000) + 1)) < 0) {
                    i3 = 0;
                }
                byte[] bArrA18 = im.a(i3, bArr);
                System.arraycopy(bArrA18, 0, bArr5, i34, bArrA18.length);
                int length51 = i34 + bArrA18.length;
                byte[] bArrA19 = im.a(scanResult.frequency, bArr);
                System.arraycopy(bArrA19, 0, bArr5, length51, bArrA19.length);
                length49 = length51 + bArrA19.length;
                i32++;
                i15 = 127;
            }
            byte[] bArrA20 = im.a(Integer.parseInt(this.H), bArr);
            System.arraycopy(bArrA20, 0, bArr5, length49, bArrA20.length);
            length17 = length49 + bArrA20.length;
        }
        bArr5[length17] = 0;
        int i35 = length17 + 1;
        try {
            byte[] bytes18 = this.I.getBytes("GBK");
            if (bytes18.length > 127) {
                bytes18 = null;
            }
            if (bytes18 == null) {
                bArr5[i35] = 0;
                i4 = 1;
                length19 = i35 + i4;
            } else {
                bArr5[i35] = (byte) bytes18.length;
                int i36 = i35 + 1;
                System.arraycopy(bytes18, 0, bArr5, i36, bytes18.length);
                length19 = i36 + bytes18.length;
            }
        } catch (Throwable unused3) {
            bArr5[i35] = 0;
            i4 = 1;
        }
        byte[] bArrA21 = {0, 0};
        try {
            boolean zIsEmpty = TextUtils.isEmpty(this.L);
            if (!zIsEmpty) {
                bArrA21 = im.a(this.L.length(), bArr);
            }
            try {
                System.arraycopy(bArrA21, 0, bArr5, length19, 2);
                length20 = length19 + 2;
                if (!zIsEmpty) {
                    try {
                        byte[] bytes19 = this.L.getBytes("GBK");
                        System.arraycopy(bytes19, 0, bArr5, length20, bytes19.length);
                        length20 += bytes19.length;
                    } catch (Throwable unused4) {
                    }
                }
                i5 = 2;
            } catch (Throwable unused5) {
                i5 = 2;
                length20 = length19 + i5;
            }
        } catch (Throwable unused6) {
            i5 = 2;
        }
        ArrayList<ia> arrayList3 = this.G;
        int iMin3 = Math.min(arrayList3.size(), 65536);
        if (iMin3 == 0) {
            try {
                System.arraycopy(im.a(0, bArr), 0, bArr5, length20, i5);
            } catch (Throwable unused7) {
            }
            i6 = length20 + i5;
        } else {
            try {
                System.arraycopy(im.a(iMin3, bArr), 0, bArr5, length20, i5);
            } catch (Throwable unused8) {
            }
            i6 = length20 + i5;
            for (int i37 = 0; i37 < iMin3; i37++) {
                ia iaVar = arrayList3.get(i37);
                try {
                    byte[] bArr6 = iaVar.c;
                    System.arraycopy(bArr6, 0, bArr5, i6, bArr6.length);
                    length21 = i6 + bArr6.length;
                } catch (Throwable unused9) {
                    length21 = i6 + 6;
                }
                try {
                    byte[] bytes20 = iaVar.a.getBytes("GBK");
                    int length52 = 32;
                    if (bytes20.length <= 32) {
                        length52 = bytes20.length;
                    }
                    System.arraycopy(bytes20, 0, bArr5, length21, length52);
                } catch (Throwable unused10) {
                }
                int i38 = length21 + 32;
                try {
                    byte[] bytes21 = iaVar.d.getBytes("GBK");
                    System.arraycopy(bytes21, 0, bArr5, i38, bytes21.length);
                } catch (Throwable unused11) {
                }
                int i39 = i38 + 4;
                try {
                    byte[] bytes22 = iaVar.e.getBytes("GBK");
                    System.arraycopy(bytes22, 0, bArr5, i39, bytes22.length);
                } catch (Throwable unused12) {
                }
                int i40 = i39 + 4;
                try {
                    if (iaVar.g > 127) {
                        try {
                            iaVar.g = 0;
                        } catch (Throwable unused13) {
                        }
                    } else if (iaVar.g < -128) {
                        iaVar.g = 0;
                    }
                    try {
                        bArr5[i40] = (byte) iaVar.g;
                    } catch (Throwable unused14) {
                    }
                } catch (Throwable unused15) {
                }
                int i41 = i40 + 1;
                try {
                    byte[] bArrB11 = im.b(iaVar.f, new byte[4]);
                    System.arraycopy(bArrB11, 0, bArr5, i41, bArrB11.length);
                } catch (Throwable unused16) {
                }
                int i42 = i41 + 4;
                try {
                    byte[] bArrB12 = im.b(iaVar.i, new byte[4]);
                    System.arraycopy(bArrB12, 0, bArr5, i42, bArrB12.length);
                } catch (Throwable unused17) {
                }
                i6 = i42 + 4;
            }
        }
        byte[] bArr7 = this.J;
        int length53 = bArr7 != null ? bArr7.length : 0;
        byte[] bArrA22 = im.a(length53, (byte[]) null);
        System.arraycopy(bArrA22, 0, bArr5, i6, bArrA22.length);
        int length54 = i6 + bArrA22.length;
        if (length53 > 0) {
            byte[] bArr8 = this.J;
            System.arraycopy(bArr8, 0, bArr5, length54, bArr8.length);
            length54 += this.J.length;
        }
        byte[] bArr9 = new byte[length54];
        System.arraycopy(bArr5, 0, bArr9, 0, length54);
        CRC32 crc32 = new CRC32();
        crc32.update(bArr9);
        byte[] bArrA23 = im.a(crc32.getValue());
        byte[] bArr10 = new byte[bArrA23.length + length54];
        System.arraycopy(bArr9, 0, bArr10, 0, length54);
        System.arraycopy(bArrA23, 0, bArr10, length54, bArrA23.length);
        return bArr10;
    }
}
