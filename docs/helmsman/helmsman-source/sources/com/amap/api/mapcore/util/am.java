package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;

/* JADX INFO: compiled from: OfflineDownloadManager.java */
/* JADX INFO: loaded from: classes.dex */
public class am {
    public static String a = "";
    public static boolean b = false;
    public static String d = "";
    private static volatile am k;
    public aq f;
    as g;
    private Context i;
    private a l;
    private av m;
    private bb n;
    private boolean j = true;
    List<al> c = new Vector();
    private ExecutorService o = null;
    private ExecutorService p = null;
    private ExecutorService q = null;
    b e = null;
    ap h = null;
    private boolean r = true;

    /* JADX INFO: compiled from: OfflineDownloadManager.java */
    public interface a {
        void a();

        void a(al alVar);

        void b(al alVar);

        void c(al alVar);
    }

    private am(Context context) {
        this.i = context;
    }

    public static am a(Context context) {
        if (k == null) {
            synchronized (am.class) {
                if (k == null && !b) {
                    k = new am(context.getApplicationContext());
                }
            }
        }
        return k;
    }

    public void a() {
        this.n = bb.a(this.i.getApplicationContext());
        h();
        this.e = new b(this.i.getMainLooper());
        this.f = new aq(this.i, this.e);
        this.m = av.a(1);
        g(dw.b(this.i));
        try {
            i();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        synchronized (this.c) {
            Iterator<OfflineMapProvince> it = this.f.a().iterator();
            while (it.hasNext()) {
                for (OfflineMapCity offlineMapCity : it.next().getCityList()) {
                    if (offlineMapCity != null) {
                        this.c.add(new al(this.i, offlineMapCity));
                    }
                }
            }
        }
        ap apVar = new ap(this.i);
        this.h = apVar;
        apVar.start();
    }

    private void h() {
        try {
            aw awVarA = this.n.a("000001");
            if (awVarA != null) {
                this.n.c("000001");
                awVarA.c("100000");
                this.n.a(awVarA);
            }
        } catch (Throwable th) {
            fn.b(th, "OfflineDownloadManager", "changeBadCase");
        }
    }

    private void i() throws Throwable {
        String strC;
        if (dw.b(this.i).equals("")) {
            return;
        }
        File file = new File(dw.b(this.i) + "offlinemapv4.png");
        if (!file.exists()) {
            strC = bj.a(this.i, "offlinemapv4.png");
        } else {
            strC = bj.c(file);
        }
        if (strC != null) {
            try {
                h(strC);
            } catch (JSONException e) {
                if (file.exists()) {
                    file.delete();
                }
                fn.b(e, "MapDownloadManager", "paseJson io");
                e.printStackTrace();
            }
        }
    }

    private void h(String str) throws JSONException {
        aq aqVar;
        List<OfflineMapProvince> listA = bj.a(str, this.i.getApplicationContext());
        if (listA == null || listA.size() == 0 || (aqVar = this.f) == null) {
            return;
        }
        aqVar.a(listA);
    }

    private void j() {
        for (aw awVar : this.n.a()) {
            if (awVar != null && awVar.d() != null && awVar.f().length() >= 1) {
                if (awVar.l != 4 && awVar.l != 7 && awVar.l >= 0) {
                    awVar.l = 3;
                }
                al alVarI = i(awVar.d());
                if (alVarI != null) {
                    String strE = awVar.e();
                    if (strE != null && a(d, strE)) {
                        alVarI.a(7);
                    } else {
                        alVarI.a(awVar.l);
                        alVarI.setCompleteCode(awVar.g());
                    }
                    if (awVar.e().length() > 0) {
                        alVarI.setVersion(awVar.e());
                    }
                    List<String> listB = this.n.b(awVar.f());
                    StringBuffer stringBuffer = new StringBuffer();
                    Iterator<String> it = listB.iterator();
                    while (it.hasNext()) {
                        stringBuffer.append(it.next());
                        stringBuffer.append(";");
                    }
                    alVarI.a(stringBuffer.toString());
                    aq aqVar = this.f;
                    if (aqVar != null) {
                        aqVar.a(alVarI);
                    }
                }
            }
        }
    }

    public void a(ArrayList<aw> arrayList) {
        j();
        a aVar = this.l;
        if (aVar != null) {
            try {
                aVar.a();
            } catch (Throwable th) {
                fn.b(th, "OfflineDownloadManager", "verifyCallBack");
            }
        }
    }

    public void a(final String str) {
        try {
            if (str == null) {
                a aVar = this.l;
                if (aVar != null) {
                    aVar.b(null);
                    return;
                }
                return;
            }
            if (this.o == null) {
                this.o = Executors.newSingleThreadExecutor();
            }
            this.o.execute(new Runnable() { // from class: com.amap.api.mapcore.util.am.1
                @Override // java.lang.Runnable
                public void run() {
                    al alVarI = am.this.i(str);
                    if (alVarI != null) {
                        try {
                            if (!alVarI.c().equals(alVarI.c) && !alVarI.c().equals(alVarI.e)) {
                                String pinyin = alVarI.getPinyin();
                                if (pinyin.length() > 0) {
                                    String strD = am.this.n.d(pinyin);
                                    if (strD == null) {
                                        strD = alVarI.getVersion();
                                    }
                                    if (am.d.length() > 0 && strD != null && am.this.a(am.d, strD)) {
                                        alVarI.j();
                                    }
                                }
                            }
                            if (am.this.l != null) {
                                synchronized (am.this) {
                                    try {
                                        am.this.l.b(alVarI);
                                    } catch (Throwable th) {
                                        fn.b(th, "OfflineDownloadManager", "checkUpdatefinally");
                                    }
                                }
                                return;
                            }
                            return;
                        } catch (Exception unused) {
                            if (am.this.l != null) {
                                synchronized (am.this) {
                                    try {
                                        am.this.l.b(alVarI);
                                    } catch (Throwable th2) {
                                        fn.b(th2, "OfflineDownloadManager", "checkUpdatefinally");
                                    }
                                    return;
                                }
                            }
                            return;
                        } catch (Throwable th3) {
                            if (am.this.l != null) {
                                synchronized (am.this) {
                                    try {
                                        am.this.l.b(alVarI);
                                    } catch (Throwable th4) {
                                        fn.b(th4, "OfflineDownloadManager", "checkUpdatefinally");
                                    }
                                }
                            }
                            throw th3;
                        }
                    }
                    am.this.k();
                    an anVarC = new ao(am.this.i, am.d).c();
                    if (am.this.l != null) {
                        if (anVarC == null) {
                            if (am.this.l != null) {
                                synchronized (am.this) {
                                    try {
                                        am.this.l.b(alVarI);
                                    } catch (Throwable th5) {
                                        fn.b(th5, "OfflineDownloadManager", "checkUpdatefinally");
                                    }
                                }
                                return;
                            }
                            return;
                        }
                        if (anVarC.a()) {
                            am.this.b();
                        }
                    }
                    if (am.this.l != null) {
                        synchronized (am.this) {
                            try {
                                am.this.l.b(alVarI);
                            } catch (Throwable th6) {
                                fn.b(th6, "OfflineDownloadManager", "checkUpdatefinally");
                            }
                        }
                    }
                }
            });
        } catch (Throwable th) {
            fn.b(th, "OfflineDownloadManager", "checkUpdate");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() throws AMapException {
        if (!dw.c(this.i)) {
            throw new AMapException(AMapException.ERROR_CONNECTION);
        }
    }

    protected void b() throws AMapException {
        if (this.f == null) {
            return;
        }
        at atVar = new at(this.i, "");
        atVar.a(this.i);
        List<OfflineMapProvince> listC = atVar.c();
        if (this.c != null) {
            this.f.a(listC);
        }
        synchronized (this.c) {
            Iterator<OfflineMapProvince> it = this.f.a().iterator();
            while (it.hasNext()) {
                for (OfflineMapCity offlineMapCity : it.next().getCityList()) {
                    for (al alVar : this.c) {
                        if (offlineMapCity.getPinyin().equals(alVar.getPinyin())) {
                            String version = alVar.getVersion();
                            if (alVar.getState() == 4 && d.length() > 0 && a(d, version)) {
                                alVar.j();
                                alVar.setUrl(offlineMapCity.getUrl());
                            } else {
                                alVar.setCity(offlineMapCity.getCity());
                                alVar.setUrl(offlineMapCity.getUrl());
                                alVar.setAdcode(offlineMapCity.getAdcode());
                                alVar.setVersion(offlineMapCity.getVersion());
                                alVar.setSize(offlineMapCity.getSize());
                                alVar.setCode(offlineMapCity.getCode());
                                alVar.setJianpin(offlineMapCity.getJianpin());
                                alVar.setPinyin(offlineMapCity.getPinyin());
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(String str, String str2) {
        for (int i = 0; i < str2.length(); i++) {
            try {
                if (str.charAt(i) > str2.charAt(i)) {
                    return true;
                }
                if (str.charAt(i) < str2.charAt(i)) {
                    return false;
                }
            } catch (Throwable unused) {
            }
        }
        return false;
    }

    public boolean b(String str) {
        return i(str) != null;
    }

    public void c(String str) {
        al alVarI = i(str);
        if (alVarI == null) {
            a aVar = this.l;
            if (aVar != null) {
                try {
                    aVar.c(alVarI);
                    return;
                } catch (Throwable th) {
                    fn.b(th, "OfflineDownloadManager", "remove");
                    return;
                }
            }
            return;
        }
        d(alVarI);
        a(alVarI, true);
    }

    public void a(al alVar) {
        a(alVar, false);
    }

    private void a(final al alVar, final boolean z) {
        if (this.g == null) {
            this.g = new as(this.i);
        }
        if (this.p == null) {
            this.p = Executors.newSingleThreadExecutor();
        }
        try {
            this.p.execute(new Runnable() { // from class: com.amap.api.mapcore.util.am.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (alVar.c().equals(alVar.a)) {
                            if (am.this.l != null) {
                                am.this.l.c(alVar);
                                return;
                            }
                            return;
                        }
                        if (alVar.getState() != 7 && alVar.getState() != -1) {
                            am.this.g.a(alVar);
                            if (am.this.l != null) {
                                am.this.l.c(alVar);
                                return;
                            }
                            return;
                        }
                        am.this.g.a(alVar);
                        if (!z || am.this.l == null) {
                            return;
                        }
                        am.this.l.c(alVar);
                    } catch (Throwable th) {
                        fn.b(th, "requestDelete", "removeExcecRunnable");
                    }
                }
            });
        } catch (Throwable th) {
            fn.b(th, "requestDelete", "removeExcecRunnable");
        }
    }

    public void b(al alVar) {
        try {
            av avVar = this.m;
            if (avVar != null) {
                avVar.a(alVar, this.i, null);
            }
        } catch (ev e) {
            e.printStackTrace();
        }
    }

    public void c(al alVar) {
        aq aqVar = this.f;
        if (aqVar != null) {
            aqVar.a(alVar);
        }
        b bVar = this.e;
        if (bVar != null) {
            Message messageObtainMessage = bVar.obtainMessage();
            messageObtainMessage.obj = alVar;
            this.e.sendMessage(messageObtainMessage);
        }
    }

    public void c() {
        synchronized (this.c) {
            for (al alVar : this.c) {
                if (alVar.c().equals(alVar.c) || alVar.c().equals(alVar.b)) {
                    d(alVar);
                    alVar.g();
                }
            }
        }
    }

    public void d() {
        synchronized (this.c) {
            for (al alVar : this.c) {
                if (alVar.c().equals(alVar.c)) {
                    alVar.g();
                    break;
                }
            }
        }
    }

    public void e() {
        ExecutorService executorService = this.o;
        if (executorService != null && !executorService.isShutdown()) {
            this.o.shutdownNow();
        }
        ExecutorService executorService2 = this.q;
        if (executorService2 != null && !executorService2.isShutdown()) {
            this.q.shutdownNow();
        }
        ap apVar = this.h;
        if (apVar != null) {
            if (apVar.isAlive()) {
                this.h.interrupt();
            }
            this.h = null;
        }
        b bVar = this.e;
        if (bVar != null) {
            bVar.removeCallbacksAndMessages(null);
            this.e = null;
        }
        av avVar = this.m;
        if (avVar != null) {
            avVar.b();
        }
        aq aqVar = this.f;
        if (aqVar != null) {
            aqVar.g();
        }
        f();
        this.j = true;
        g();
    }

    public static void f() {
        k = null;
        b = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public al i(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }
        synchronized (this.c) {
            for (al alVar : this.c) {
                if (str.equals(alVar.getCity()) || str.equals(alVar.getPinyin())) {
                    return alVar;
                }
            }
            return null;
        }
    }

    private al j(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }
        synchronized (this.c) {
            for (al alVar : this.c) {
                if (str.equals(alVar.getCode())) {
                    return alVar;
                }
            }
            return null;
        }
    }

    public void d(String str) throws AMapException {
        al alVarI = i(str);
        if (str == null || str.length() < 1 || alVarI == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        f(alVarI);
    }

    public void e(String str) throws AMapException {
        al alVarJ = j(str);
        if (alVarJ != null) {
            f(alVarJ);
            return;
        }
        throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
    }

    private void f(final al alVar) throws AMapException {
        k();
        if (alVar == null) {
            throw new AMapException(AMapException.ERROR_INVALID_PARAMETER);
        }
        if (this.q == null) {
            this.q = Executors.newSingleThreadExecutor();
        }
        try {
            this.q.execute(new Runnable() { // from class: com.amap.api.mapcore.util.am.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (am.this.j) {
                            am.this.k();
                            an anVarC = new ao(am.this.i, am.d).c();
                            if (anVarC != null) {
                                am.this.j = false;
                                if (anVarC.a()) {
                                    am.this.b();
                                }
                            }
                        }
                        alVar.setVersion(am.d);
                        alVar.f();
                    } catch (AMapException e) {
                        e.printStackTrace();
                    } catch (Throwable th) {
                        fn.b(th, "OfflineDownloadManager", "startDownloadRunnable");
                    }
                }
            });
        } catch (Throwable th) {
            fn.b(th, "startDownload", "downloadExcecRunnable");
        }
    }

    public void d(al alVar) {
        av avVar = this.m;
        if (avVar != null) {
            avVar.a(alVar);
        }
    }

    public void e(al alVar) {
        av avVar = this.m;
        if (avVar != null) {
            avVar.b(alVar);
        }
    }

    public void a(a aVar) {
        this.l = aVar;
    }

    public void g() {
        synchronized (this) {
            this.l = null;
        }
    }

    /* JADX INFO: compiled from: OfflineDownloadManager.java */
    class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                message.getData();
                Object obj = message.obj;
                if (obj instanceof al) {
                    al alVar = (al) obj;
                    bj.a("OfflineMapHandler handleMessage CitObj  name: " + alVar.getCity() + " complete: " + alVar.getcompleteCode() + " status: " + alVar.getState());
                    if (am.this.l != null) {
                        am.this.l.a(alVar);
                    }
                } else {
                    bj.a("Do not callback by CityObject! ");
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public String f(String str) {
        al alVarI;
        return (str == null || (alVarI = i(str)) == null) ? "" : alVarI.getAdcode();
    }

    public static void g(String str) {
        a = str;
    }
}
