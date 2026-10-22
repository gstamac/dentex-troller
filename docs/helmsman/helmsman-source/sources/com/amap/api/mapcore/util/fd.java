package com.amap.api.mapcore.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.util.Locale;

/* JADX INFO: compiled from: ProxyUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class fd {
    public static Proxy a(Context context) {
        try {
            return a(context, new URI("http://restapi.amap.com"));
        } catch (Throwable th) {
            fk.a(th, "ProxyUtil", "getProxy");
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:100:0x0140 A[PHI: r11
  0x0140: PHI (r11v10 java.lang.String) = (r11v5 java.lang.String), (r11v12 java.lang.String) binds: [B:90:0x0126, B:87:0x011f] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:101:0x0143  */
    /* JADX WARN: Code duplicated, block: B:109:0x0156 A[Catch: all -> 0x0162, TRY_LEAVE, TryCatch #0 {all -> 0x0162, blocks: (B:107:0x0150, B:109:0x0156), top: B:122:0x0150 }] */
    /* JADX WARN: Code duplicated, block: B:125:0x0147 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:129:0x00d7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:51:0x00ac A[PHI: r10 r11
  0x00ac: PHI (r10v7 int) = (r10v6 int), (r10v9 int) binds: [B:49:0x00a9, B:29:0x0074] A[DONT_GENERATE, DONT_INLINE]
  0x00ac: PHI (r11v30 java.lang.String) = (r11v26 java.lang.String), (r11v34 java.lang.String) binds: [B:49:0x00a9, B:29:0x0074] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:56:0x00b5  */
    /* JADX WARN: Code duplicated, block: B:77:0x00f5 A[Catch: all -> 0x016a, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:79:0x0109 A[Catch: all -> 0x016a, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:81:0x010f A[Catch: all -> 0x016a, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:84:0x0117  */
    /* JADX WARN: Code duplicated, block: B:86:0x011b A[Catch: all -> 0x016a, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:88:0x0121  */
    /* JADX WARN: Code duplicated, block: B:89:0x0122 A[Catch: all -> 0x016a, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:91:0x0128 A[Catch: all -> 0x016a, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:93:0x012e A[Catch: all -> 0x016a, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Code duplicated, block: B:96:0x0138  */
    /* JADX WARN: Code duplicated, block: B:98:0x013b A[Catch: all -> 0x016a, TRY_LEAVE, TryCatch #2 {all -> 0x016a, blocks: (B:66:0x00cd, B:75:0x00ea, B:77:0x00f5, B:79:0x0109, B:81:0x010f, B:86:0x011b, B:89:0x0122, B:91:0x0128, B:93:0x012e, B:98:0x013b), top: B:124:0x0028 }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v12 */
    /* JADX WARN: Type inference failed for: r10v13 */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r10v5 */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v11 */
    /* JADX WARN: Type inference failed for: r15v5, types: [int] */
    /* JADX WARN: Type inference failed for: r15v6 */
    /* JADX WARN: Type inference failed for: r15v9 */
    /* JADX WARN: Type inference failed for: r17v0 */
    /* JADX WARN: Type inference failed for: r17v1 */
    /* JADX WARN: Type inference failed for: r17v10 */
    /* JADX WARN: Type inference failed for: r17v11 */
    /* JADX WARN: Type inference failed for: r17v12 */
    /* JADX WARN: Type inference failed for: r17v13 */
    /* JADX WARN: Type inference failed for: r17v14 */
    /* JADX WARN: Type inference failed for: r17v2 */
    /* JADX WARN: Type inference failed for: r17v3 */
    /* JADX WARN: Type inference failed for: r17v4 */
    /* JADX WARN: Type inference failed for: r17v5 */
    /* JADX WARN: Type inference failed for: r17v6 */
    /* JADX WARN: Type inference failed for: r17v7 */
    /* JADX WARN: Type inference failed for: r17v8 */
    /* JADX WARN: Type inference failed for: r17v9 */
    /* JADX WARN: Type inference failed for: r9v0, types: [android.content.ContentResolver, android.database.Cursor] */
    private static Proxy b(Context context) {
        Cursor cursorQuery;
        ?? r10;
        String strO;
        Throwable th;
        String lowerCase;
        String strA;
        int iB;
        boolean z;
        ?? r15;
        String lowerCase2;
        int iB2;
        boolean z2;
        boolean z3;
        if (c(context)) {
            Uri uri = Uri.parse("content://telephony/carriers/preferapn");
            ?? contentResolver = context.getContentResolver();
            String strA2 = null;
            boolean z4 = true;
            ?? r17 = 80;
            r17 = 80;
            r17 = 80;
            r17 = 80;
            r17 = 80;
            byte b = -1;
            b = -1;
            try {
                try {
                    cursorQuery = contentResolver.query(uri, null, null, null, null);
                    if (cursorQuery != null) {
                        try {
                            if (cursorQuery.moveToFirst()) {
                                String string = cursorQuery.getString(cursorQuery.getColumnIndex("apn"));
                                if (string != null) {
                                    lowerCase2 = string;
                                    r10 = uri;
                                    Locale locale = Locale.US;
                                    lowerCase2 = string.toLowerCase(locale);
                                    r10 = locale;
                                }
                                try {
                                    try {
                                        lowerCase2 = string;
                                        r10 = uri;
                                        if (lowerCase2 != null && lowerCase2.contains("ctwap")) {
                                            String strA3 = a();
                                            iB2 = b();
                                            if (TextUtils.isEmpty(strA3) || strA3.equals("null")) {
                                                strA2 = null;
                                                z3 = false;
                                            } else {
                                                strA2 = strA3;
                                                z3 = true;
                                            }
                                            if (!z3) {
                                                strA2 = a("QMTAuMC4wLjIwMA==");
                                            }
                                            if (iB2 != -1) {
                                                r17 = iB2;
                                            }
                                        } else if (lowerCase2 == null || !lowerCase2.contains("wap")) {
                                            strA2 = null;
                                            r17 = -1;
                                        } else {
                                            String strA4 = a();
                                            iB2 = b();
                                            if (TextUtils.isEmpty(strA4) || strA4.equals("null")) {
                                                strA2 = null;
                                                z2 = false;
                                            } else {
                                                strA2 = strA4;
                                                z2 = true;
                                            }
                                            if (!z2) {
                                                strA2 = a("QMTAuMC4wLjE3Mg==");
                                            }
                                            if (iB2 != -1) {
                                                r17 = iB2;
                                            }
                                        }
                                    } catch (SecurityException e) {
                                        e = e;
                                        strA2 = null;
                                        fk.a(e, "ProxyUtil", "getHostProxy");
                                        strO = fa.o(context);
                                        if (strO != null) {
                                            lowerCase = strO.toLowerCase(Locale.US);
                                            strA = a();
                                            iB = b();
                                            if (lowerCase.indexOf("ctwap") != -1) {
                                                if (!TextUtils.isEmpty(strA) || strA.equals("null")) {
                                                    z4 = false;
                                                } else {
                                                    strA2 = strA;
                                                }
                                                if (!z4) {
                                                    strA2 = a("QMTAuMC4wLjIwMA==");
                                                }
                                                if (iB != -1) {
                                                    r17 = iB;
                                                }
                                            } else if (lowerCase.indexOf("wap") != -1) {
                                                if (!TextUtils.isEmpty(strA) || strA.equals("null")) {
                                                    z = false;
                                                } else {
                                                    strA2 = strA;
                                                    z = true;
                                                }
                                                if (!z) {
                                                    strA2 = a("QMTAuMC4wLjE3Mg==");
                                                }
                                            } else {
                                                r17 = iB;
                                            }
                                        } else {
                                            r17 = r10;
                                        }
                                        if (cursorQuery != null) {
                                            try {
                                                cursorQuery.close();
                                            } catch (Throwable th2) {
                                                th = th2;
                                                r17 = r17;
                                                fk.a(th, "ProxyUtil", "getHostProxy2");
                                                th.printStackTrace();
                                            }
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                        strA2 = null;
                                        b = r10 == true ? 1 : 0;
                                        fk.a(th, "ProxyUtil", "getHostProxy1");
                                        th.printStackTrace();
                                        r15 = b;
                                        if (cursorQuery != null) {
                                            try {
                                                cursorQuery.close();
                                                r15 = b;
                                            } catch (Throwable th4) {
                                                fk.a(th4, "ProxyUtil", "getHostProxy2");
                                                th4.printStackTrace();
                                                r15 = b;
                                            }
                                        }
                                        if (a(strA2, (int) r15)) {
                                            return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(strA2, r15));
                                        }
                                        return null;
                                    }
                                } catch (SecurityException e2) {
                                    e = e2;
                                    fk.a(e, "ProxyUtil", "getHostProxy");
                                    strO = fa.o(context);
                                    if (strO != null) {
                                        lowerCase = strO.toLowerCase(Locale.US);
                                        strA = a();
                                        iB = b();
                                        if (lowerCase.indexOf("ctwap") != -1) {
                                            if (TextUtils.isEmpty(strA)) {
                                                z4 = false;
                                            } else {
                                                z4 = false;
                                            }
                                            if (!z4) {
                                                strA2 = a("QMTAuMC4wLjIwMA==");
                                            }
                                            if (iB != -1) {
                                                r17 = iB;
                                            }
                                        } else if (lowerCase.indexOf("wap") != -1) {
                                            if (TextUtils.isEmpty(strA)) {
                                                z = false;
                                            } else {
                                                z = false;
                                            }
                                            if (!z) {
                                                strA2 = a("QMTAuMC4wLjE3Mg==");
                                            }
                                        } else {
                                            r17 = iB;
                                        }
                                    } else {
                                        r17 = r10;
                                    }
                                    if (cursorQuery != null) {
                                        cursorQuery.close();
                                    }
                                } catch (Throwable th5) {
                                    th = th5;
                                    b = r10 == true ? 1 : 0;
                                    fk.a(th, "ProxyUtil", "getHostProxy1");
                                    th.printStackTrace();
                                    r15 = b;
                                    if (cursorQuery != null) {
                                        cursorQuery.close();
                                        r15 = b;
                                    }
                                    if (a(strA2, (int) r15)) {
                                        return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(strA2, r15));
                                    }
                                    return null;
                                }
                            } else {
                                strA2 = null;
                                r17 = -1;
                            }
                        } catch (SecurityException e3) {
                            e = e3;
                            strA2 = null;
                            r10 = -1;
                            fk.a(e, "ProxyUtil", "getHostProxy");
                            strO = fa.o(context);
                            if (strO != null) {
                                lowerCase = strO.toLowerCase(Locale.US);
                                strA = a();
                                iB = b();
                                if (lowerCase.indexOf("ctwap") != -1) {
                                    if (TextUtils.isEmpty(strA)) {
                                        z4 = false;
                                    } else {
                                        z4 = false;
                                    }
                                    if (!z4) {
                                        strA2 = a("QMTAuMC4wLjIwMA==");
                                    }
                                    if (iB != -1) {
                                        r17 = iB;
                                    }
                                } else if (lowerCase.indexOf("wap") != -1) {
                                    if (TextUtils.isEmpty(strA)) {
                                        z = false;
                                    } else {
                                        z = false;
                                    }
                                    if (!z) {
                                        strA2 = a("QMTAuMC4wLjE3Mg==");
                                    }
                                } else {
                                    r17 = iB;
                                }
                            } else {
                                r17 = r10;
                            }
                            if (cursorQuery != null) {
                                cursorQuery.close();
                            }
                            r15 = r17;
                            if (a(strA2, (int) r15)) {
                                return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(strA2, r15));
                            }
                            return null;
                        } catch (Throwable th6) {
                            th = th6;
                            strA2 = null;
                        }
                    } else {
                        strA2 = null;
                        r17 = -1;
                    }
                    if (cursorQuery != null) {
                        try {
                            cursorQuery.close();
                        } catch (Throwable th7) {
                            th = th7;
                            r17 = r17;
                            fk.a(th, "ProxyUtil", "getHostProxy2");
                            th.printStackTrace();
                        }
                    }
                } catch (Throwable th8) {
                    if (contentResolver == 0) {
                        throw th8;
                    }
                    try {
                        contentResolver.close();
                        throw th8;
                    } catch (Throwable th9) {
                        fk.a(th9, "ProxyUtil", "getHostProxy2");
                        th9.printStackTrace();
                        throw th8;
                    }
                }
            } catch (SecurityException e4) {
                e = e4;
                cursorQuery = null;
                strA2 = null;
            } catch (Throwable th10) {
                th = th10;
                cursorQuery = null;
                strA2 = null;
            }
            r15 = r17;
            try {
                if (a(strA2, (int) r15)) {
                    return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(strA2, r15));
                }
            } catch (Throwable th11) {
                fk.a(th11, "ProxyUtil", "getHostProxy2");
                th11.printStackTrace();
            }
        }
        return null;
    }

    public static String a(String str) {
        return fg.c(str);
    }

    private static boolean a(String str, int i) {
        return (str == null || str.length() <= 0 || i == -1) ? false : true;
    }

    private static String a() {
        String defaultHost;
        try {
            defaultHost = android.net.Proxy.getDefaultHost();
        } catch (Throwable th) {
            fk.a(th, "ProxyUtil", "getDefHost");
            defaultHost = null;
        }
        return defaultHost == null ? "null" : defaultHost;
    }

    private static Proxy a(Context context, URI uri) {
        Proxy proxy;
        if (c(context)) {
            try {
                List<Proxy> listSelect = ProxySelector.getDefault().select(uri);
                if (listSelect == null || listSelect.isEmpty() || (proxy = listSelect.get(0)) == null || proxy.type() == Proxy.Type.DIRECT) {
                    return null;
                }
                return proxy;
            } catch (Throwable th) {
                fk.a(th, "ProxyUtil", "getProxySelectorCfg");
            }
        }
        return null;
    }

    private static boolean c(Context context) {
        return fa.m(context) == 0;
    }

    private static int b() {
        try {
            return android.net.Proxy.getDefaultPort();
        } catch (Throwable th) {
            fk.a(th, "ProxyUtil", "getDefPort");
            return -1;
        }
    }
}
