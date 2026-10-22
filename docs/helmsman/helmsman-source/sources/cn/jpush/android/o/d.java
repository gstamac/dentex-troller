package cn.jpush.android.o;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import cn.jpush.android.cache.Key;
import cn.jpush.android.cache.Sp;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.service.SchedulerReceiver;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class d {
    private static volatile d a;
    private LinkedList<a> b;
    private final Comparator<a> c = new Comparator<a>() { // from class: cn.jpush.android.o.d.1
        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(a aVar, a aVar2) {
            if (aVar.a < aVar2.a) {
                return -1;
            }
            return aVar.a == aVar2.a ? 0 : 1;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public static class a {
        private long a;
        private boolean b;
        private cn.jpush.android.d.d c;
        private int d;

        public a(long j, int i) {
            this.a = j;
            this.b = false;
            this.d = i;
        }

        public a(long j, cn.jpush.android.d.d dVar) {
            this.a = j;
            this.b = true;
            this.c = dVar;
        }

        public a(JSONObject jSONObject) {
            try {
                this.a = jSONObject.getLong("operationTime");
                this.b = jSONObject.optBoolean("showOrDismiss");
                String strOptString = jSONObject.optString("pushEntity");
                if (!TextUtils.isEmpty(strOptString)) {
                    this.c = cn.jpush.android.d.d.a(strOptString);
                }
                this.d = jSONObject.optInt("notifyId");
            } catch (Throwable unused) {
            }
        }

        public JSONObject a() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("operationTime", this.a);
                jSONObject.put("showOrDismiss", this.b);
                if (this.b) {
                    cn.jpush.android.d.d dVar = this.c;
                    jSONObject.put("pushEntity", dVar != null ? dVar.c() : null);
                } else {
                    jSONObject.put("notifyId", this.d);
                }
                return jSONObject;
            } catch (Throwable unused) {
                return null;
            }
        }

        public String toString() {
            return "Item{operationTime=" + this.a + ", showOrDismiss=" + this.b + ", pushEntity=" + this.c + ", notifyId=" + this.d + '}';
        }
    }

    private d() {
    }

    public static d a() {
        if (a == null) {
            synchronized (d.class) {
                if (a == null) {
                    a = new d();
                }
            }
        }
        return a;
    }

    private static void a(Context context, LinkedList<a> linkedList, long j, a aVar) throws Throwable {
        if (aVar == null) {
            return;
        }
        if (aVar.a > j) {
            linkedList.add(aVar);
            return;
        }
        if (!aVar.b) {
            Logger.d("NotificationScheduler", "cancelNotification:" + aVar);
            b.c(context, aVar.d);
            return;
        }
        if (b.c(context, aVar.c)) {
            Logger.d("NotificationScheduler", "item: " + aVar + " already cancel");
            return;
        }
        long jB = cn.jpush.android.t.b.b(aVar.c.al);
        int iA = b.a(aVar.c);
        if (jB <= 0) {
            Logger.d("NotificationScheduler", "handleNotification:" + aVar);
            b.a.a(context, aVar.c);
        } else if (jB <= j) {
            Logger.d("NotificationScheduler", "cancelNotification:" + aVar);
            b.c(context, iA);
        } else {
            Logger.d("NotificationScheduler", "handleNotification:" + aVar);
            b.a.a(context, aVar.c);
            linkedList.add(new a(jB, iA));
        }
    }

    private void b(Context context, a aVar) {
        try {
            Logger.d("NotificationScheduler", "schedule item=" + aVar);
            Intent intent = new Intent(context, (Class<?>) SchedulerReceiver.class);
            intent.setAction(SchedulerReceiver.DELAY_NOTIFY);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, 1000, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarmManager != null) {
                alarmManager.setWindow(0, aVar.a, 300L, broadcast);
                Logger.d("NotificationScheduler", "setAlarm at=" + cn.jpush.android.t.b.a("yyyy-MM-dd HH:mm:ss").format(new Date(aVar.a)));
            }
        } catch (Throwable th) {
            Logger.ww("NotificationScheduler", "can't trigger alarm cause by exception:" + th);
        }
    }

    public void a(Context context) {
        this.b = new LinkedList<>();
        String str = (String) Sp.get(context, Key.NotiSchedule());
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONArray jSONArray = new JSONArray(str);
            for (int i = 0; i < jSONArray.length(); i++) {
                this.b.add(new a(jSONArray.getJSONObject(i)));
            }
        } catch (Throwable unused) {
        }
    }

    public synchronized void a(Context context, a aVar) {
        a(context);
        LinkedList<a> linkedList = new LinkedList<>();
        long jCurrentTimeMillis = System.currentTimeMillis();
        boolean z = aVar != null;
        for (a aVar2 : this.b) {
            if (z && aVar2.a > aVar.a) {
                a(context, linkedList, jCurrentTimeMillis, aVar);
                z = false;
            }
            a(context, linkedList, jCurrentTimeMillis, aVar2);
        }
        if (z) {
            a(context, linkedList, jCurrentTimeMillis, aVar);
        }
        this.b = linkedList;
        b(context);
        if (!linkedList.isEmpty()) {
            b(context, linkedList.getFirst());
        }
    }

    public void b(Context context) {
        String string;
        LinkedList<a> linkedList = this.b;
        if (linkedList == null || linkedList.isEmpty()) {
            string = null;
        } else {
            JSONArray jSONArray = new JSONArray();
            Iterator<a> it = this.b.iterator();
            while (it.hasNext()) {
                jSONArray.put(it.next().a());
            }
            string = jSONArray.toString();
        }
        Collections.sort(this.b, this.c);
        Sp.set(context, Key.NotiSchedule().set(string));
    }
}
