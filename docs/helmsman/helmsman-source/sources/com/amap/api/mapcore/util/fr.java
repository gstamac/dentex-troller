package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import cn.jpush.android.api.JThirdPlatFormInterface;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: LogProcessor.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class fr {
    static final List<b> a = Collections.synchronizedList(new ArrayList());
    private ff b;
    private int c;
    private gt d;
    private gs e;

    /* JADX INFO: compiled from: LogProcessor.java */
    public interface b {
        void a(Context context);
    }

    protected abstract String a(List<ff> list);

    protected abstract boolean a(Context context);

    public static List<b> a() {
        return a;
    }

    protected fr(int i) {
        this.c = i;
    }

    private void f(Context context) {
        try {
            this.e = b(context, b());
        } catch (Throwable th) {
            fk.a(th, "LogProcessor", "LogUpDateProcessor");
        }
    }

    void a(ff ffVar, Context context, Throwable th, String str, String str2, String str3) {
        a(ffVar, context, c(th), str, str2, str3);
    }

    void a(ff ffVar, Context context, String str, String str2, String str3, String str4) {
        a(ffVar);
        String strA = hk.a();
        String strA2 = a(context, ffVar);
        String strA3 = ew.a(context);
        if (str == null || "".equals(str)) {
            return;
        }
        int iC = c();
        StringBuilder sb = new StringBuilder();
        if (str3 != null) {
            sb.append("class:").append(str3);
        }
        if (str4 != null) {
            sb.append(" method:").append(str4).append("$<br/>");
        }
        sb.append(str2);
        String strA4 = a(str2);
        String strA5 = hk.a(strA3, strA2, strA, iC, str, sb.toString());
        if (strA5 == null || "".equals(strA5)) {
            return;
        }
        String strA6 = a(context, strA5);
        String strB = b();
        synchronized (Looper.getMainLooper()) {
            gb gbVar = new gb(context);
            a(gbVar, ffVar.a(), strA4, iC, a(context, strA4, strB, strA6, gbVar));
        }
    }

    public static String a(Context context, ff ffVar) {
        return hk.a(context, ffVar);
    }

    void a(Context context, Throwable th, String str, String str2) {
        String strA;
        List<ff> listG = g(context);
        if (listG == null || listG.size() == 0 || (strA = a(th)) == null || "".equals(strA)) {
            return;
        }
        for (ff ffVar : listG) {
            if (a(ffVar.g(), strA)) {
                a(ffVar, context, th, strA.replaceAll("\n", "<br/>"), str, str2);
                return;
            }
        }
        if (strA.contains("com.amap.api.col")) {
            try {
                a(fg.a(), context, th, strA, str, str2);
            } catch (ev e) {
                e.printStackTrace();
            }
        }
    }

    void b(Context context) {
        String strA;
        List<ff> listG = g(context);
        if (listG == null || listG.size() == 0 || (strA = a(listG)) == null || "".equals(strA)) {
            return;
        }
        String strA2 = hk.a();
        String strA3 = hk.a(context, this.b);
        String strA4 = ew.a(context);
        int iC = c();
        String strA5 = hk.a(strA4, strA3, strA2, iC, "ANR", strA);
        if (strA5 == null || "".equals(strA5)) {
            return;
        }
        String strA6 = a(strA);
        String strA7 = a(context, strA5);
        String strB = b();
        synchronized (Looper.getMainLooper()) {
            gb gbVar = new gb(context);
            a(gbVar, this.b.a(), strA6, iC, a(context, strA6, strB, strA7, gbVar));
        }
    }

    protected void a(ff ffVar) {
        this.b = ffVar;
    }

    private List<ff> g(Context context) {
        List<ff> listA = null;
        try {
            synchronized (Looper.getMainLooper()) {
                listA = new gd(context, false).a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return listA;
    }

    private void a(gb gbVar, String str, String str2, int i, boolean z) {
        gc gcVarB = fl.b(i);
        gcVarB.a(0);
        gcVarB.b(str);
        gcVarB.a(str2);
        gbVar.a(gcVarB);
    }

    protected String a(String str) {
        return fc.c(str);
    }

    protected gt a(gb gbVar) {
        try {
            if (this.d == null) {
                this.d = new a(gbVar);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this.d;
    }

    /* JADX INFO: compiled from: LogProcessor.java */
    class a implements gt {
        private gb b;

        a(gb gbVar) {
            this.b = gbVar;
        }

        @Override // com.amap.api.mapcore.util.gt
        public void a(String str) {
            try {
                this.b.b(str, fl.a(fr.this.c()));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private String a(Context context, String str) {
        try {
            return ez.e(context, fg.a(str));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    protected String a(Throwable th) {
        try {
            return b(th);
        } catch (Throwable th2) {
            th2.printStackTrace();
            return null;
        }
    }

    private String c(Throwable th) {
        return th.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r7v1, types: [com.amap.api.mapcore.util.gs] */
    /* JADX WARN: Type inference failed for: r7v10, types: [com.amap.api.mapcore.util.gs] */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v12 */
    /* JADX WARN: Type inference failed for: r7v2 */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v5, types: [com.amap.api.mapcore.util.gs] */
    /* JADX WARN: Type inference failed for: r7v6 */
    /* JADX WARN: Type inference failed for: r7v7 */
    /* JADX WARN: Type inference failed for: r7v8, types: [com.amap.api.mapcore.util.gs] */
    /* JADX WARN: Type inference failed for: r9v0, types: [com.amap.api.mapcore.util.gb] */
    /* JADX WARN: Type inference failed for: r9v1, types: [com.amap.api.mapcore.util.gs$b] */
    /* JADX WARN: Type inference failed for: r9v10 */
    /* JADX WARN: Type inference failed for: r9v12, types: [com.amap.api.mapcore.util.gs$b] */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5, types: [com.amap.api.mapcore.util.gs$b] */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v7 */
    /* JADX WARN: Type inference failed for: r9v8, types: [com.amap.api.mapcore.util.gs$b] */
    /* JADX WARN: Type inference failed for: r9v9 */
    private boolean a(Context context, String str, String str2, String str3, gb gbVar) {
        OutputStream outputStreamA = null;
        try {
            try {
                try {
                    File file = new File(fl.a(context, (String) str2));
                    if (!file.exists() && !file.mkdirs()) {
                        return false;
                    }
                    str2 = gs.a(file, 1, 1, 20480L);
                    try {
                        str2.a(a((gb) gbVar));
                        gbVar = str2.a(str);
                        if (gbVar != 0) {
                            if (gbVar != 0) {
                                try {
                                    gbVar.close();
                                } catch (Throwable th) {
                                    th.printStackTrace();
                                }
                            }
                            if (str2 != 0 && !str2.d()) {
                                try {
                                    str2.close();
                                } catch (Throwable th2) {
                                    th2.printStackTrace();
                                }
                            }
                            return false;
                        }
                        try {
                            byte[] bArrA = fg.a(str3);
                            gs.a aVarB = str2.b(str);
                            outputStreamA = aVarB.a(0);
                            outputStreamA.write(bArrA);
                            aVarB.a();
                            str2.e();
                            if (outputStreamA != null) {
                                try {
                                    outputStreamA.close();
                                } catch (Throwable th3) {
                                    th3.printStackTrace();
                                }
                            }
                            if (gbVar != 0) {
                                try {
                                    gbVar.close();
                                } catch (Throwable th4) {
                                    th4.printStackTrace();
                                }
                            }
                            if (str2 != 0 && !str2.d()) {
                                try {
                                    str2.close();
                                } catch (Throwable th5) {
                                    th5.printStackTrace();
                                }
                            }
                            return true;
                        } catch (IOException e) {
                            e = e;
                        } catch (Throwable th6) {
                            th = th6;
                            th.printStackTrace();
                            if (outputStreamA != null) {
                                try {
                                    outputStreamA.close();
                                } catch (Throwable th7) {
                                    th7.printStackTrace();
                                }
                            }
                            if (gbVar != 0) {
                                try {
                                    gbVar.close();
                                } catch (Throwable th8) {
                                    th8.printStackTrace();
                                }
                            }
                            if (str2 != 0 && !str2.d()) {
                                str2.close();
                                str2 = str2;
                                gbVar = gbVar;
                            }
                            return false;
                        }
                    } catch (IOException e2) {
                        e = e2;
                        gbVar = 0;
                    } catch (Throwable th9) {
                        th = th9;
                        gbVar = 0;
                    }
                } catch (Throwable th10) {
                    th10.printStackTrace();
                }
            } catch (IOException e3) {
                e = e3;
                str2 = 0;
                gbVar = 0;
            } catch (Throwable th11) {
                th = th11;
                str2 = 0;
                gbVar = 0;
            }
            e.printStackTrace();
            if (outputStreamA != null) {
                try {
                    outputStreamA.close();
                } catch (Throwable th12) {
                    th12.printStackTrace();
                }
            }
            if (gbVar != 0) {
                try {
                    gbVar.close();
                } catch (Throwable th13) {
                    th13.printStackTrace();
                }
            }
            if (str2 != 0 && !str2.d()) {
                str2.close();
                str2 = str2;
                gbVar = gbVar;
            }
            return false;
        } catch (Throwable th14) {
            if (outputStreamA != null) {
                try {
                    outputStreamA.close();
                } catch (Throwable th15) {
                    th15.printStackTrace();
                }
            }
            if (gbVar != 0) {
                try {
                    gbVar.close();
                } catch (Throwable th16) {
                    th16.printStackTrace();
                }
            }
            if (str2 == 0 || str2.d()) {
                throw th14;
            }
            try {
                str2.close();
                throw th14;
            } catch (Throwable th17) {
                th17.printStackTrace();
                throw th14;
            }
        }
    }

    public static boolean a(String[] strArr, String str) {
        if (strArr != null && str != null) {
            try {
                for (String str2 : str.split("\n")) {
                    if (b(strArr, str2.trim())) {
                        return true;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return false;
    }

    public static boolean b(String[] strArr, String str) {
        if (strArr != null && str != null) {
            try {
                for (String str2 : strArr) {
                    str = str.trim();
                    if (str.startsWith("at ") && str.contains(str2 + ".") && str.endsWith(")")) {
                        return true;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return false;
    }

    void c(Context context) {
        try {
            if (a(context)) {
                synchronized (Looper.getMainLooper()) {
                    f(context);
                    gb gbVar = new gb(context);
                    a(gbVar, c());
                    List<? extends gc> listA = gbVar.a(0, fl.a(c()));
                    if (listA != null && listA.size() != 0) {
                        String strA = a(listA, context);
                        if (strA == null) {
                            return;
                        }
                        if (b(strA) == 1) {
                            a(listA, gbVar, c());
                        }
                    }
                }
            }
        } catch (Throwable th) {
            fk.a(th, "LogProcessor", "processUpdateLog");
        }
    }

    private boolean c(String str) {
        gs gsVar = this.e;
        if (gsVar == null) {
            return false;
        }
        try {
            return gsVar.c(str);
        } catch (Throwable th) {
            fk.a(th, "LogUpdateProcessor", "deleteLogData-" + str);
            return false;
        }
    }

    protected String b() {
        return fl.c(this.c);
    }

    protected int c() {
        return this.c;
    }

    private void a(gb gbVar, int i) {
        try {
            a(gbVar.a(2, fl.a(i)), gbVar, i);
        } catch (Throwable th) {
            fk.a(th, "LogProcessor", "processDeleteFail");
        }
    }

    public static int b(String str) {
        fm fmVar = new fm(fg.c(fg.a(str)));
        int i = 1;
        try {
            byte[] bArrB = gw.a().b(fmVar);
            if (bArrB == null) {
                return 0;
            }
            try {
                JSONObject jSONObject = new JSONObject(fg.a(bArrB));
                i = jSONObject.has(JThirdPlatFormInterface.KEY_CODE) ? jSONObject.getInt(JThirdPlatFormInterface.KEY_CODE) : 0;
            } catch (JSONException e) {
                fk.a(e, "LogProcessor", "processUpdate");
            }
        } catch (ev e2) {
            if (e2.e() == 27) {
                i = 0;
            }
            fk.a(e2, "LogProcessor", "processUpdate");
        }
        return i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(List<? extends gc> list, gb gbVar, int i) {
        if (list == null || list.size() <= 0) {
            return;
        }
        for (gc gcVar : list) {
            if (c(gcVar.b())) {
                gbVar.a(gcVar.b(), (Class<? extends gc>) gcVar.getClass());
            } else {
                gcVar.a(2);
                gbVar.b(gcVar);
            }
        }
    }

    public static String d(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"key\":\"").append(ew.f(context)).append("\",\"platform\":\"android\",\"diu\":\"").append(fa.q(context)).append("\",\"pkg\":\"").append(ew.c(context)).append("\",\"model\":\"").append(Build.MODEL).append("\",\"appname\":\"").append(ew.b(context)).append("\",\"appversion\":\"").append(ew.d(context)).append("\",\"sysversion\":\"").append(Build.VERSION.RELEASE).append("\",");
        } catch (Throwable th) {
            fk.a(th, "CInfo", "getPublicJSONInfo");
        }
        return sb.toString();
    }

    public static String e(Context context) {
        try {
            String strD = d(context);
            if ("".equals(strD)) {
                return null;
            }
            return ez.b(context, fg.a(strD));
        } catch (Throwable th) {
            fk.a(th, "LogProcessor", "getPublicInfo");
            return null;
        }
    }

    private String a(List<? extends gc> list, Context context) {
        StringBuilder sb = new StringBuilder("{\"pinfo\":\"");
        sb.append(e(context)).append("\",\"els\":[");
        boolean z = true;
        for (gc gcVar : list) {
            String strD = d(gcVar.b());
            if (strD != null && !"".equals(strD)) {
                String str = strD + "||" + gcVar.c();
                if (z) {
                    z = false;
                } else {
                    sb.append(",");
                }
                sb.append("{\"log\":\"").append(str).append("\"}");
            }
        }
        if (z) {
            return null;
        }
        sb.append("]}");
        return sb.toString();
    }

    private String d(String str) {
        InputStream inputStreamA;
        ByteArrayOutputStream byteArrayOutputStream;
        gs.b bVarA;
        try {
            gs gsVar = this.e;
            if (gsVar == null || (bVarA = gsVar.a(str)) == null) {
                return null;
            }
            inputStreamA = bVarA.a(0);
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int i = inputStreamA.read(bArr);
                        if (i == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, i);
                    }
                    String strA = fg.a(byteArrayOutputStream.toByteArray());
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        fk.a(e, "LogProcessor", "readLog1");
                    }
                    if (inputStreamA != null) {
                        try {
                            inputStreamA.close();
                        } catch (IOException e2) {
                            fk.a(e2, "LogProcessor", "readLog2");
                        }
                    }
                    return strA;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
            inputStreamA = null;
            byteArrayOutputStream = null;
        }
        try {
            fk.a(th, "LogProcessor", "readLog");
            return null;
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e3) {
                    fk.a(e3, "LogProcessor", "readLog1");
                }
            }
            if (inputStreamA != null) {
                try {
                    inputStreamA.close();
                } catch (IOException e4) {
                    fk.a(e4, "LogProcessor", "readLog2");
                }
            }
        }
    }

    void d() {
        try {
            gs gsVar = this.e;
            if (gsVar == null || gsVar.d()) {
                return;
            }
            this.e.close();
        } catch (IOException e) {
            fk.a(e, "LogProcessor", "closeDiskLru");
        } catch (Throwable th) {
            fk.a(th, "LogProcessor", "closeDiskLru");
        }
    }

    private gs b(Context context, String str) {
        try {
            File file = new File(fl.a(context, str));
            if (file.exists() || file.mkdirs()) {
                return gs.a(file, 1, 1, 20480L);
            }
            return null;
        } catch (IOException e) {
            fk.a(e, "LogProcessor", "initDiskLru");
            return null;
        } catch (Throwable th) {
            fk.a(th, "LogProcessor", "initDiskLru");
            return null;
        }
    }

    public static String b(Throwable th) {
        return fg.a(th);
    }
}
