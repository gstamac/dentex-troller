package cn.jiguang.ae;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.internal.ActionManager;
import com.facebook.appevents.AppEventsConstants;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    private static String a;
    private static String b;
    private static ConcurrentHashMap<File, Boolean> c = new ConcurrentHashMap<>();
    private static final AtomicBoolean d = new AtomicBoolean(false);

    public static File a(Context context, String str, JSONObject jSONObject, boolean z) {
        File file = new File(e(context) + str + File.separator + UUID.randomUUID().toString());
        if (z) {
            b(file);
        }
        cn.jiguang.f.e.a(file, jSONObject.toString());
        return file;
    }

    public static void a(Context context) {
        cn.jiguang.f.f fVar = new cn.jiguang.f.f(false, true, "jpush_stat_history", 1);
        String strA = cn.jiguang.ac.a.a(context);
        File[] fileArrA = cn.jiguang.f.e.a(context.getFilesDir(), fVar, new e());
        if (fileArrA != null) {
            for (File file : fileArrA) {
                try {
                    cn.jiguang.f.e.c(new File(file, "nowrap"));
                    File file2 = new File(file.getParent() + File.separator + strA + File.separator + file.getName());
                    if (!file2.getParentFile().exists()) {
                        file2.getParentFile().mkdirs();
                    }
                    file.renameTo(file2);
                } catch (Throwable th) {
                    cn.jiguang.ad.d.b("updateByAppKey", "e=" + th);
                }
            }
        }
    }

    static /* synthetic */ void a(Context context, File file) {
        cn.jiguang.ad.d.b("ReportHistory", "upload space=" + e(file));
        c(context, file);
        d(file);
        b(context, file);
        c(file);
    }

    public static void a(Context context, String... strArr) {
        FileFilter[] fileFilterArr = new FileFilter[2];
        for (int i = 0; i <= 0; i++) {
            fileFilterArr[1] = cn.jiguang.f.f.a(strArr[0]);
        }
        fileFilterArr[0] = new cn.jiguang.f.f(false, true, "jpush_stat_history", 1);
        File[] fileArrA = cn.jiguang.f.e.a(context.getFilesDir(), fileFilterArr);
        if (fileArrA != null) {
            for (File file : fileArrA) {
                cn.jiguang.f.e.c(file);
            }
        }
    }

    public static void a(File file) {
        if (file != null) {
            c.remove(file);
        }
    }

    private static Set<String> b() {
        HashSet hashSet = new HashSet();
        hashSet.add("uid");
        hashSet.add("app_key");
        hashSet.add("sdk_ver");
        hashSet.add("channel");
        hashSet.add("app_version");
        JSONObject jSONObject = new JSONObject();
        ActionManager.getInstance().wrapSdkVersionInfo(jSONObject);
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            hashSet.add(itKeys.next());
        }
        return hashSet;
    }

    public static void b(Context context) {
        if (d.get()) {
            cn.jiguang.ad.d.b("ReportHistory", "isRunning, no need report");
        } else {
            cn.jiguang.aq.a.a("REPORT_HISTORY", new f(context));
        }
    }

    private static void b(Context context, File file) {
        if (((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.c())).longValue() == 0) {
            cn.jiguang.ad.d.b("ReportHistory", "can't get uid, skip upload history");
            return;
        }
        File[] fileArrA = cn.jiguang.f.e.a(file, cn.jiguang.f.f.a);
        if (fileArrA == null || fileArrA.length == 0) {
            return;
        }
        LinkedList<File> linkedList = new LinkedList();
        for (File file2 : fileArrA) {
            if (!Boolean.TRUE.equals(c.get(file2))) {
                linkedList.add(file2);
            }
        }
        if (linkedList.isEmpty()) {
            return;
        }
        cn.jiguang.ad.d.b("ReportHistory", "process space=" + e(file) + " history[" + linkedList.size() + "]");
        Collections.sort(linkedList, new g());
        for (File file3 : linkedList) {
            try {
                JSONObject jSONObjectA = c.a(file3);
                if (jSONObjectA == null) {
                    cn.jiguang.f.e.a(file3);
                } else {
                    b(file3);
                    Set<String> setA = k.a(jSONObjectA);
                    cn.jiguang.ad.d.b("ReportHistory", "report history types=" + setA);
                    if (k.a(context, setA, jSONObjectA, file3, null) == -2) {
                        return;
                    }
                }
            } catch (Throwable th) {
                cn.jiguang.ad.d.f("ReportHistory", "upload e:" + th);
            }
        }
    }

    private static void b(File file) {
        if (file != null) {
            c.put(file, true);
        }
    }

    private static void c(Context context, File file) {
        File[] fileArrA;
        try {
            JSONObject jSONObjectA = k.a(context);
            if (!(jSONObjectA == null || jSONObjectA.length() == 0) && (fileArrA = cn.jiguang.f.e.a(new File(file, "nowrap"), cn.jiguang.f.f.a)) != null && fileArrA.length != 0) {
                cn.jiguang.ad.d.b("ReportHistory", "process space=" + e(file) + " nowrap[" + fileArrA.length + "]");
                File file2 = new File(file, "tmp");
                c cVarA = c.a(fileArrA[0], jSONObjectA);
                for (int i = 1; i < fileArrA.length; i++) {
                    c cVarA2 = c.a(fileArrA[i], jSONObjectA);
                    if (cVarA2 != null && (cVarA == null || !cVarA.a(cVarA2, file2))) {
                        cVarA = cVarA2;
                    }
                }
                if (cVarA != null) {
                    cVarA.a((c) null, file2);
                }
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("ReportHistory", "processNowrap e:" + th);
        }
    }

    private static void c(File file) {
        int length = 0;
        File[] fileArrA = cn.jiguang.f.e.a(file, cn.jiguang.f.f.a);
        if (fileArrA == null || fileArrA.length <= 1) {
            return;
        }
        List<File> listAsList = Arrays.asList(fileArrA);
        Collections.sort(listAsList, new h());
        for (File file2 : listAsList) {
            length = (int) (((long) length) + file2.length());
            if (length > 1048576) {
                cn.jiguang.f.e.a(file2);
            }
        }
        if (listAsList.size() < fileArrA.length) {
            cn.jiguang.ad.d.b("ReportHistory", "trim space history=" + e(file) + "," + fileArrA.length + " to " + listAsList.size());
        }
    }

    private static synchronized String d(Context context) {
        if (!TextUtils.isEmpty(b)) {
            return b;
        }
        if (a == null) {
            String strA = cn.jiguang.f.a.a(context);
            if (!TextUtils.isEmpty(strA)) {
                a = strA.equals(context.getPackageName()) ? "" : strA.replaceFirst(context.getPackageName() + ":", "_");
            }
        }
        File fileA = cn.jiguang.f.e.a(context, a != null ? "jpush_stat_history" + a : "jpush_stat_history");
        if (fileA == null) {
            return "jpush_stat_history" + a;
        }
        String absolutePath = fileA.getAbsolutePath();
        b = absolutePath;
        return absolutePath;
    }

    private static void d(File file) {
        try {
            File[] fileArrA = cn.jiguang.f.e.a(new File(file, "tmp"), cn.jiguang.f.f.a);
            if (fileArrA != null && fileArrA.length != 0) {
                LinkedList linkedList = new LinkedList();
                for (File file2 : fileArrA) {
                    if (!Boolean.TRUE.equals(c.get(file2))) {
                        linkedList.add(file2);
                    }
                }
                if (linkedList.isEmpty()) {
                    return;
                }
                cn.jiguang.ad.d.b("ReportHistory", "process space=" + e(file) + " tmp[" + linkedList.size() + "]");
                Set<String> setB = b();
                HashMap map = new HashMap();
                c cVarA = c.a(cn.jiguang.f.e.a(cn.jiguang.f.e.a(file, cn.jiguang.f.f.a)), setB);
                if (cVarA != null) {
                    map.put(String.valueOf(cVarA.b), cVarA);
                }
                Iterator it = linkedList.iterator();
                while (it.hasNext()) {
                    c cVarA2 = c.a((File) it.next(), setB);
                    if (cVarA2 != null) {
                        String strValueOf = String.valueOf(cVarA2.b);
                        c cVar = (c) map.get(strValueOf);
                        if (cVar == null || !cVar.a(cVarA2, file)) {
                            map.put(strValueOf, cVarA2);
                        }
                    }
                }
                Iterator it2 = map.values().iterator();
                while (it2.hasNext()) {
                    ((c) it2.next()).a((c) null, file);
                }
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("ReportHistory", "processTmp e:" + th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String e(Context context) {
        String strA = cn.jiguang.ac.a.a(context);
        if (TextUtils.isEmpty(strA)) {
            strA = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        return d(context) + File.separator + strA;
    }

    private static String e(File file) {
        try {
            File parentFile = file.getParentFile();
            return parentFile.getParentFile().getName() + File.separator + parentFile.getName() + File.separator + file.getName();
        } catch (Throwable unused) {
            return file.getAbsolutePath();
        }
    }
}
