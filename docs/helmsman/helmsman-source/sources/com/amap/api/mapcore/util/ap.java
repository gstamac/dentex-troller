package com.amap.api.mapcore.util;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: compiled from: OfflineMapDataVerify.java */
/* JADX INFO: loaded from: classes.dex */
public class ap extends Thread {
    private Context a;
    private bb b;

    public ap(Context context) {
        this.a = context;
        this.b = bb.a(context);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        a();
    }

    private aw a(File file) throws Throwable {
        String strA = dw.a(file);
        aw awVar = new aw();
        awVar.b(strA);
        return awVar;
    }

    @Override // java.lang.Thread
    public void destroy() {
        this.a = null;
        this.b = null;
    }

    private void a() {
        aw awVarA;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<aw> arrayListA = this.b.a();
        a(arrayList, "vmap/");
        a(arrayList, "map/");
        ArrayList<String> arrayListB = b();
        for (aw awVar : arrayListA) {
            if (awVar != null && awVar.d() != null) {
                if (awVar.l == 4 || awVar.l == 7) {
                    if (!arrayList.contains(awVar.h())) {
                        this.b.b(awVar);
                    }
                } else if (awVar.l == 0 || awVar.l == 1) {
                    if (!(arrayListB.contains(awVar.f()) || arrayListB.contains(awVar.h()))) {
                        this.b.b(awVar);
                    }
                } else if (awVar.l == 3 && awVar.g() != 0) {
                    if (!(arrayListB.contains(awVar.f()) || arrayListB.contains(awVar.h()))) {
                        this.b.b(awVar);
                    }
                }
            }
        }
        for (String str : arrayList) {
            if (!a(str, arrayListA) && (awVarA = a(str)) != null) {
                this.b.a(awVarA);
            }
        }
        am amVarA = am.a(this.a);
        if (amVarA != null) {
            amVarA.a((ArrayList<aw>) null);
        }
    }

    private aw a(String str) {
        if (str.equals("quanguo")) {
            str = "quanguogaiyaotu";
        }
        am amVarA = am.a(this.a);
        aw awVarA = null;
        if (amVarA != null) {
            String strF = amVarA.f(str);
            File[] fileArrListFiles = new File(dw.b(this.a)).listFiles();
            if (fileArrListFiles == null) {
                return null;
            }
            for (File file : fileArrListFiles) {
                if ((file.getName().contains(strF) || file.getName().contains(str)) && file.getName().endsWith(".zip.tmp.dt") && (awVarA = a(file)) != null && awVarA.d() != null) {
                    return awVarA;
                }
            }
        }
        return awVarA;
    }

    private boolean a(String str, ArrayList<aw> arrayList) {
        Iterator<aw> it = arrayList.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().h())) {
                return true;
            }
        }
        return false;
    }

    private void a(ArrayList<String> arrayList, String str) {
        File[] fileArrListFiles;
        String name;
        int iLastIndexOf;
        File file = new File(dw.a(this.a) + str);
        if (file.exists() && (fileArrListFiles = file.listFiles()) != null) {
            for (File file2 : fileArrListFiles) {
                if (file2.getName().endsWith(".dat") && (iLastIndexOf = (name = file2.getName()).lastIndexOf(46)) > -1 && iLastIndexOf < name.length()) {
                    String strSubstring = name.substring(0, iLastIndexOf);
                    if (!arrayList.contains(strSubstring)) {
                        arrayList.add(strSubstring);
                    }
                }
            }
        }
    }

    private ArrayList<String> b() {
        File[] fileArrListFiles;
        String name;
        int iLastIndexOf;
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(dw.b(this.a));
        if (!file.exists() || (fileArrListFiles = file.listFiles()) == null) {
            return arrayList;
        }
        for (File file2 : fileArrListFiles) {
            if (file2.getName().endsWith(".zip") && (iLastIndexOf = (name = file2.getName()).lastIndexOf(46)) > -1 && iLastIndexOf < name.length()) {
                arrayList.add(name.substring(0, iLastIndexOf));
            }
        }
        return arrayList;
    }
}
