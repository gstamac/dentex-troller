package com.tencent.wxop.stat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.ServerProtocol;
import com.tencent.wxop.stat.common.StatLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class au {
    private static StatLogger h = com.tencent.wxop.stat.common.k.b();
    private static Context i = null;
    private static au j = null;
    private bc c;
    private bc d;
    private com.tencent.wxop.stat.common.e e;
    private String f;
    private String g;
    private ConcurrentHashMap<com.tencent.wxop.stat.a.e, String> l;
    volatile int a = 0;
    com.tencent.wxop.stat.common.a b = null;
    private int k = 0;
    private boolean m = false;
    private HashMap<String, String> n = new HashMap<>();

    private au(Context context) {
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = "";
        this.g = "";
        this.l = null;
        try {
            this.e = new com.tencent.wxop.stat.common.e();
            i = context.getApplicationContext();
            this.l = new ConcurrentHashMap<>();
            this.f = com.tencent.wxop.stat.common.k.r(context);
            this.g = "pri_" + com.tencent.wxop.stat.common.k.r(context);
            this.c = new bc(i, this.f);
            this.d = new bc(i, this.g);
            a(true);
            a(false);
            f();
            b(i);
            d();
            j();
        } catch (Throwable th) {
            h.e(th);
        }
    }

    public static au a(Context context) {
        if (j == null) {
            synchronized (au.class) {
                if (j == null) {
                    j = new au(context);
                }
            }
        }
        return j;
    }

    private String a(List<bd> list) {
        StringBuilder sb = new StringBuilder(list.size() * 3);
        sb.append("event_id in (");
        int size = list.size();
        Iterator<bd> it = list.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            sb.append(it.next().a);
            if (i2 != size - 1) {
                sb.append(",");
            }
            i2++;
        }
        sb.append(")");
        return sb.toString();
    }

    private synchronized void a(int i2, boolean z) {
        try {
            if (this.a > 0 && i2 > 0 && !StatServiceImpl.a()) {
                if (StatConfig.isDebugEnable()) {
                    h.i("Load " + this.a + " unsent events");
                }
                ArrayList arrayList = new ArrayList(i2);
                b(arrayList, i2, z);
                if (arrayList.size() > 0) {
                    if (StatConfig.isDebugEnable()) {
                        h.i("Peek " + arrayList.size() + " unsent events.");
                    }
                    a(arrayList, 2, z);
                    i.b(i).b(arrayList, new ba(this, arrayList, z));
                }
            }
        } catch (Throwable th) {
            h.e(th);
        }
    }

    private void a(com.tencent.wxop.stat.a.e eVar, h hVar, boolean z) {
        long jInsert;
        SQLiteDatabase sQLiteDatabase = null;
        try {
            SQLiteDatabase sQLiteDatabaseC = c(z);
            try {
                sQLiteDatabaseC.beginTransaction();
                if (!z && this.a > StatConfig.getMaxStoreEventCount()) {
                    h.warn("Too many events stored in db.");
                    this.a -= this.c.getWritableDatabase().delete("events", "event_id in (select event_id from events where timestamp in (select min(timestamp) from events) limit 1)", null);
                }
                ContentValues contentValues = new ContentValues();
                String strG = eVar.g();
                if (StatConfig.isDebugEnable()) {
                    h.i("insert 1 event, content:" + strG);
                }
                contentValues.put("content", com.tencent.wxop.stat.common.q.b(strG));
                contentValues.put("send_count", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                contentValues.put("status", Integer.toString(1));
                contentValues.put("timestamp", Long.valueOf(eVar.c()));
                jInsert = sQLiteDatabaseC.insert("events", null, contentValues);
                sQLiteDatabaseC.setTransactionSuccessful();
                if (sQLiteDatabaseC != null) {
                    try {
                        sQLiteDatabaseC.endTransaction();
                    } catch (Throwable th) {
                        h.e(th);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                sQLiteDatabase = sQLiteDatabaseC;
                try {
                    h.e(th);
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Throwable th3) {
                            h.e(th3);
                        }
                    }
                    jInsert = -1;
                } catch (Throwable th4) {
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Throwable th5) {
                            h.e(th5);
                        }
                    }
                    throw th4;
                }
            }
        } catch (Throwable th6) {
            th = th6;
        }
        if (jInsert <= 0) {
            h.error("Failed to store event:" + eVar.g());
            return;
        }
        this.a++;
        if (StatConfig.isDebugEnable()) {
            h.d("directStoreEvent insert event to db, event:" + eVar.g());
        }
        if (hVar != null) {
            hVar.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(List<bd> list, int i2, boolean z) {
        String str;
        if (list.size() == 0) {
            return;
        }
        int iB = b(z);
        SQLiteDatabase sQLiteDatabase = null;
        String str2 = null;
        try {
            SQLiteDatabase sQLiteDatabaseC = c(z);
            try {
                if (i2 == 2) {
                    str = "update events set status=" + i2 + ", send_count=send_count+1  where " + a(list);
                } else {
                    str = "update events set status=" + i2 + " where " + a(list);
                    str2 = this.k % 3 == 0 ? "delete from events where send_count>" + iB : null;
                    this.k++;
                }
                if (StatConfig.isDebugEnable()) {
                    h.i("update sql:" + str);
                }
                sQLiteDatabaseC.beginTransaction();
                sQLiteDatabaseC.execSQL(str);
                if (str2 != null) {
                    h.i("update for delete sql:" + str2);
                    sQLiteDatabaseC.execSQL(str2);
                    f();
                }
                sQLiteDatabaseC.setTransactionSuccessful();
                if (sQLiteDatabaseC != null) {
                    try {
                        sQLiteDatabaseC.endTransaction();
                    } catch (Throwable th) {
                        h.e(th);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                sQLiteDatabase = sQLiteDatabaseC;
                try {
                    h.e(th);
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Throwable th3) {
                            h.e(th3);
                        }
                    }
                } catch (Throwable th4) {
                    if (sQLiteDatabase != null) {
                        try {
                            sQLiteDatabase.endTransaction();
                        } catch (Throwable th5) {
                            h.e(th5);
                        }
                    }
                    throw th4;
                }
            }
        } catch (Throwable th6) {
            th = th6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(List<bd> list, boolean z) {
        SQLiteDatabase sQLiteDatabaseC;
        Throwable th;
        if (list.size() == 0) {
            return;
        }
        if (StatConfig.isDebugEnable()) {
            h.i("Delete " + list.size() + " events, important:" + z);
        }
        StringBuilder sb = new StringBuilder(list.size() * 3);
        sb.append("event_id in (");
        int size = list.size();
        Iterator<bd> it = list.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            sb.append(it.next().a);
            if (i2 != size - 1) {
                sb.append(",");
            }
            i2++;
        }
        sb.append(")");
        try {
            sQLiteDatabaseC = c(z);
            try {
                sQLiteDatabaseC.beginTransaction();
                int iDelete = sQLiteDatabaseC.delete("events", sb.toString(), null);
                if (StatConfig.isDebugEnable()) {
                    h.i("delete " + size + " event " + sb.toString() + ", success delete:" + iDelete);
                }
                this.a -= iDelete;
                sQLiteDatabaseC.setTransactionSuccessful();
                f();
                if (sQLiteDatabaseC != null) {
                    try {
                        sQLiteDatabaseC.endTransaction();
                    } catch (Throwable th2) {
                        h.e(th2);
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                try {
                    h.e(th);
                    if (sQLiteDatabaseC != null) {
                        try {
                            sQLiteDatabaseC.endTransaction();
                        } catch (Throwable th4) {
                            h.e(th4);
                        }
                    }
                } catch (Throwable th5) {
                    if (sQLiteDatabaseC != null) {
                        try {
                            sQLiteDatabaseC.endTransaction();
                        } catch (Throwable th6) {
                            h.e(th6);
                        }
                    }
                    throw th5;
                }
            }
        } catch (Throwable th7) {
            sQLiteDatabaseC = null;
            th = th7;
        }
    }

    private void a(boolean z) {
        SQLiteDatabase sQLiteDatabaseC;
        try {
            try {
                sQLiteDatabaseC = c(z);
                try {
                    sQLiteDatabaseC.beginTransaction();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("status", (Integer) 1);
                    int iUpdate = sQLiteDatabaseC.update("events", contentValues, "status=?", new String[]{Long.toString(2L)});
                    if (StatConfig.isDebugEnable()) {
                        h.i("update " + iUpdate + " unsent events.");
                    }
                    sQLiteDatabaseC.setTransactionSuccessful();
                    if (sQLiteDatabaseC != null) {
                        sQLiteDatabaseC.endTransaction();
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        h.e(th);
                        if (sQLiteDatabaseC != null) {
                            sQLiteDatabaseC.endTransaction();
                        }
                    } catch (Throwable th2) {
                        if (sQLiteDatabaseC != null) {
                            try {
                                sQLiteDatabaseC.endTransaction();
                            } catch (Throwable th3) {
                                h.e(th3);
                            }
                        }
                        throw th2;
                    }
                }
            } catch (Throwable th4) {
                h.e(th4);
            }
        } catch (Throwable th5) {
            th = th5;
            sQLiteDatabaseC = null;
        }
    }

    private int b(boolean z) {
        return !z ? StatConfig.getMaxSendRetryCount() : StatConfig.getMaxImportantDataSendRetryCount();
    }

    public static au b() {
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i2, boolean z) {
        if (i2 == -1) {
            i2 = !z ? g() : h();
        }
        if (i2 > 0) {
            int sendPeriodMinutes = StatConfig.getSendPeriodMinutes() * 60 * StatConfig.getNumEventsCommitPerSec();
            if (i2 > sendPeriodMinutes && sendPeriodMinutes > 0) {
                i2 = sendPeriodMinutes;
            }
            int iA = StatConfig.a();
            int i3 = i2 / iA;
            int i4 = i2 % iA;
            if (StatConfig.isDebugEnable()) {
                h.i("sentStoreEventsByDb sendNumbers=" + i2 + ",important=" + z + ",maxSendNumPerFor1Period=" + sendPeriodMinutes + ",maxCount=" + i3 + ",restNumbers=" + i4);
            }
            for (int i5 = 0; i5 < i3; i5++) {
                a(iA, z);
            }
            if (i4 > 0) {
                a(i4, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void b(com.tencent.wxop.stat.a.e eVar, h hVar, boolean z, boolean z2) {
        if (StatConfig.getMaxStoreEventCount() > 0) {
            if (StatConfig.m > 0 && !z && !z2) {
                if (StatConfig.m > 0) {
                    if (StatConfig.isDebugEnable()) {
                        h.i("cacheEventsInMemory.size():" + this.l.size() + ",numEventsCachedInMemory:" + StatConfig.m + ",numStoredEvents:" + this.a);
                        h.i("cache event:" + eVar.g());
                    }
                    this.l.put(eVar, "");
                    if (this.l.size() >= StatConfig.m) {
                        i();
                    }
                    if (hVar != null) {
                        if (this.l.size() > 0) {
                            i();
                        }
                        hVar.a();
                    }
                }
            }
            a(eVar, hVar, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void b(f fVar) {
        boolean z;
        long jInsert;
        Cursor cursor = null;
        try {
            String strA = fVar.a();
            String strA2 = com.tencent.wxop.stat.common.k.a(strA);
            ContentValues contentValues = new ContentValues();
            contentValues.put("content", fVar.b.toString());
            contentValues.put("md5sum", strA2);
            fVar.c = strA2;
            contentValues.put(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, Integer.valueOf(fVar.d));
            Cursor cursorQuery = this.c.getReadableDatabase().query("config", null, null, null, null, null, null);
            while (true) {
                try {
                    if (!cursorQuery.moveToNext()) {
                        z = false;
                        break;
                    } else if (cursorQuery.getInt(0) == fVar.a) {
                        z = true;
                        break;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = cursorQuery;
                    try {
                        h.e(th);
                        if (cursor != null) {
                            cursor.close();
                        }
                        try {
                            this.c.getWritableDatabase().endTransaction();
                            return;
                        } catch (Exception unused) {
                            return;
                        }
                    } catch (Throwable th2) {
                        if (cursor != null) {
                            cursor.close();
                        }
                        try {
                            this.c.getWritableDatabase().endTransaction();
                        } catch (Exception unused2) {
                        }
                        throw th2;
                    }
                }
            }
            this.c.getWritableDatabase().beginTransaction();
            if (true == z) {
                jInsert = this.c.getWritableDatabase().update("config", contentValues, "type=?", new String[]{Integer.toString(fVar.a)});
            } else {
                contentValues.put("type", Integer.valueOf(fVar.a));
                jInsert = this.c.getWritableDatabase().insert("config", null, contentValues);
            }
            if (jInsert == -1) {
                h.e("Failed to store cfg:" + strA);
            } else {
                h.d("Sucessed to store cfg:" + strA);
            }
            this.c.getWritableDatabase().setTransactionSuccessful();
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            try {
                this.c.getWritableDatabase().endTransaction();
            } catch (Exception unused3) {
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void b(List<bd> list, int i2, boolean z) {
        Cursor cursorQuery = null;
        try {
            cursorQuery = d(z).query("events", null, "status=?", new String[]{Integer.toString(1)}, null, null, null, Integer.toString(i2));
            while (cursorQuery.moveToNext()) {
                long j2 = cursorQuery.getLong(0);
                String string = cursorQuery.getString(1);
                if (!StatConfig.g) {
                    string = com.tencent.wxop.stat.common.q.a(string);
                }
                String str = string;
                int i3 = cursorQuery.getInt(2);
                int i4 = cursorQuery.getInt(3);
                bd bdVar = new bd(j2, str, i3, i4);
                if (StatConfig.isDebugEnable()) {
                    h.i("peek event, id=" + j2 + ",send_count=" + i4 + ",timestamp=" + cursorQuery.getLong(4));
                }
                list.add(bdVar);
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th) {
            try {
                h.e(th);
            } finally {
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            }
        }
    }

    private SQLiteDatabase c(boolean z) {
        return (!z ? this.c : this.d).getWritableDatabase();
    }

    private SQLiteDatabase d(boolean z) {
        return (!z ? this.c : this.d).getReadableDatabase();
    }

    private void f() {
        this.a = g() + h();
    }

    private int g() {
        return (int) DatabaseUtils.queryNumEntries(this.c.getReadableDatabase(), "events");
    }

    private int h() {
        return (int) DatabaseUtils.queryNumEntries(this.d.getReadableDatabase(), "events");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        StatLogger statLogger;
        if (this.m) {
            return;
        }
        synchronized (this.l) {
            if (this.l.size() == 0) {
                return;
            }
            this.m = true;
            if (StatConfig.isDebugEnable()) {
                h.i("insert " + this.l.size() + " events ,numEventsCachedInMemory:" + StatConfig.m + ",numStoredEvents:" + this.a);
            }
            SQLiteDatabase sQLiteDatabase = null;
            try {
                SQLiteDatabase writableDatabase = this.c.getWritableDatabase();
                try {
                    writableDatabase.beginTransaction();
                    Iterator<Map.Entry<com.tencent.wxop.stat.a.e, String>> it = this.l.entrySet().iterator();
                    while (it.hasNext()) {
                        com.tencent.wxop.stat.a.e key = it.next().getKey();
                        ContentValues contentValues = new ContentValues();
                        String strG = key.g();
                        if (StatConfig.isDebugEnable()) {
                            h.i("insert content:" + strG);
                        }
                        contentValues.put("content", com.tencent.wxop.stat.common.q.b(strG));
                        contentValues.put("send_count", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                        contentValues.put("status", Integer.toString(1));
                        contentValues.put("timestamp", Long.valueOf(key.c()));
                        writableDatabase.insert("events", null, contentValues);
                        it.remove();
                    }
                    writableDatabase.setTransactionSuccessful();
                    if (writableDatabase != null) {
                        try {
                            writableDatabase.endTransaction();
                            f();
                        } catch (Throwable th) {
                            th = th;
                            statLogger = h;
                            statLogger.e(th);
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    sQLiteDatabase = writableDatabase;
                    try {
                        h.e(th);
                        if (sQLiteDatabase != null) {
                            try {
                                sQLiteDatabase.endTransaction();
                                f();
                            } catch (Throwable th3) {
                                th = th3;
                                statLogger = h;
                                statLogger.e(th);
                            }
                        }
                    } catch (Throwable th4) {
                        if (sQLiteDatabase != null) {
                            try {
                                sQLiteDatabase.endTransaction();
                                f();
                            } catch (Throwable th5) {
                                h.e(th5);
                            }
                        }
                        throw th4;
                    }
                }
            } catch (Throwable th6) {
                th = th6;
            }
            this.m = false;
            if (StatConfig.isDebugEnable()) {
                h.i("after insert, cacheEventsInMemory.size():" + this.l.size() + ",numEventsCachedInMemory:" + StatConfig.m + ",numStoredEvents:" + this.a);
            }
        }
    }

    private void j() {
        Cursor cursorQuery = null;
        try {
            cursorQuery = this.c.getReadableDatabase().query("keyvalues", null, null, null, null, null, null);
            while (cursorQuery.moveToNext()) {
                this.n.put(cursorQuery.getString(0), cursorQuery.getString(1));
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th) {
            try {
                h.e(th);
            } finally {
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            }
        }
    }

    public int a() {
        return this.a;
    }

    void a(int i2) {
        this.e.a(new bb(this, i2));
    }

    void a(com.tencent.wxop.stat.a.e eVar, h hVar, boolean z, boolean z2) {
        com.tencent.wxop.stat.common.e eVar2 = this.e;
        if (eVar2 != null) {
            eVar2.a(new ay(this, eVar, hVar, z, z2));
        }
    }

    void a(f fVar) {
        if (fVar == null) {
            return;
        }
        this.e.a(new az(this, fVar));
    }

    void a(List<bd> list, int i2, boolean z, boolean z2) {
        com.tencent.wxop.stat.common.e eVar = this.e;
        if (eVar != null) {
            eVar.a(new av(this, list, i2, z, z2));
        }
    }

    void a(List<bd> list, boolean z, boolean z2) {
        com.tencent.wxop.stat.common.e eVar = this.e;
        if (eVar != null) {
            eVar.a(new aw(this, list, z, z2));
        }
    }

    /* JADX WARN: Code duplicated, block: B:42:0x00ac A[Catch: all -> 0x01cd, TryCatch #5 {all -> 0x01cd, blocks: (B:13:0x0033, B:15:0x003c, B:17:0x005a, B:21:0x006e, B:23:0x0078, B:24:0x007a, B:26:0x0082, B:28:0x0085, B:30:0x0089, B:42:0x00ac, B:44:0x00af, B:50:0x00ed, B:52:0x011f, B:54:0x0132, B:57:0x0141, B:59:0x014b, B:61:0x0151, B:63:0x016a, B:64:0x01ae, B:45:0x00c9, B:47:0x00cf, B:49:0x00d5, B:34:0x0094, B:36:0x009a, B:39:0x00a4), top: B:107:0x0033 }] */
    /* JADX WARN: Code duplicated, block: B:45:0x00c9 A[Catch: all -> 0x01cd, TryCatch #5 {all -> 0x01cd, blocks: (B:13:0x0033, B:15:0x003c, B:17:0x005a, B:21:0x006e, B:23:0x0078, B:24:0x007a, B:26:0x0082, B:28:0x0085, B:30:0x0089, B:42:0x00ac, B:44:0x00af, B:50:0x00ed, B:52:0x011f, B:54:0x0132, B:57:0x0141, B:59:0x014b, B:61:0x0151, B:63:0x016a, B:64:0x01ae, B:45:0x00c9, B:47:0x00cf, B:49:0x00d5, B:34:0x0094, B:36:0x009a, B:39:0x00a4), top: B:107:0x0033 }] */
    /* JADX WARN: Code duplicated, block: B:47:0x00cf A[Catch: all -> 0x01cd, TryCatch #5 {all -> 0x01cd, blocks: (B:13:0x0033, B:15:0x003c, B:17:0x005a, B:21:0x006e, B:23:0x0078, B:24:0x007a, B:26:0x0082, B:28:0x0085, B:30:0x0089, B:42:0x00ac, B:44:0x00af, B:50:0x00ed, B:52:0x011f, B:54:0x0132, B:57:0x0141, B:59:0x014b, B:61:0x0151, B:63:0x016a, B:64:0x01ae, B:45:0x00c9, B:47:0x00cf, B:49:0x00d5, B:34:0x0094, B:36:0x009a, B:39:0x00a4), top: B:107:0x0033 }] */
    /* JADX WARN: Code duplicated, block: B:52:0x011f A[Catch: all -> 0x01cd, TryCatch #5 {all -> 0x01cd, blocks: (B:13:0x0033, B:15:0x003c, B:17:0x005a, B:21:0x006e, B:23:0x0078, B:24:0x007a, B:26:0x0082, B:28:0x0085, B:30:0x0089, B:42:0x00ac, B:44:0x00af, B:50:0x00ed, B:52:0x011f, B:54:0x0132, B:57:0x0141, B:59:0x014b, B:61:0x0151, B:63:0x016a, B:64:0x01ae, B:45:0x00c9, B:47:0x00cf, B:49:0x00d5, B:34:0x0094, B:36:0x009a, B:39:0x00a4), top: B:107:0x0033 }] */
    /* JADX WARN: Code duplicated, block: B:54:0x0132 A[Catch: all -> 0x01cd, TryCatch #5 {all -> 0x01cd, blocks: (B:13:0x0033, B:15:0x003c, B:17:0x005a, B:21:0x006e, B:23:0x0078, B:24:0x007a, B:26:0x0082, B:28:0x0085, B:30:0x0089, B:42:0x00ac, B:44:0x00af, B:50:0x00ed, B:52:0x011f, B:54:0x0132, B:57:0x0141, B:59:0x014b, B:61:0x0151, B:63:0x016a, B:64:0x01ae, B:45:0x00c9, B:47:0x00cf, B:49:0x00d5, B:34:0x0094, B:36:0x009a, B:39:0x00a4), top: B:107:0x0033 }] */
    public synchronized com.tencent.wxop.stat.common.a b(Context context) {
        StatLogger statLogger;
        boolean z;
        String str;
        boolean z2;
        String strC;
        ContentValues contentValues;
        com.tencent.wxop.stat.common.a aVar = this.b;
        if (aVar != null) {
            return aVar;
        }
        Cursor cursor = null;
        try {
            this.c.getWritableDatabase().beginTransaction();
            if (StatConfig.isDebugEnable()) {
                h.i("try to load user info from db.");
            }
            Cursor cursorQuery = this.c.getReadableDatabase().query("user", null, null, null, null, null, null, null);
            try {
                if (cursorQuery.moveToNext()) {
                    String string = cursorQuery.getString(0);
                    String strA = com.tencent.wxop.stat.common.q.a(string);
                    z = true;
                    int i2 = cursorQuery.getInt(1);
                    String string2 = cursorQuery.getString(2);
                    long j2 = cursorQuery.getLong(3);
                    long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
                    int i3 = (i2 == 1 || com.tencent.wxop.stat.common.k.a(j2 * 1000).equals(com.tencent.wxop.stat.common.k.a(jCurrentTimeMillis * 1000))) ? i2 : 1;
                    if (!string2.equals(com.tencent.wxop.stat.common.k.n(context))) {
                        i3 |= 2;
                    }
                    String[] strArrSplit = strA.split(",");
                    if (strArrSplit == null || strArrSplit.length <= 0) {
                        strA = com.tencent.wxop.stat.common.k.b(context);
                        str = strA;
                    } else {
                        str = strArrSplit[0];
                        if (str == null || str.length() < 11) {
                            String strA2 = com.tencent.wxop.stat.common.q.a(context);
                            if (strA2 != null && strA2.length() > 10) {
                                str = strA2;
                            }
                            if (strArrSplit != null || strArrSplit.length < 2) {
                                strC = com.tencent.wxop.stat.common.k.c(context);
                                if (strC != null && strC.length() > 0) {
                                    strA = str + "," + strC;
                                    z2 = true;
                                }
                            } else {
                                strC = strArrSplit[1];
                                strA = str + "," + strC;
                            }
                            this.b = new com.tencent.wxop.stat.common.a(str, strC, i3);
                            contentValues = new ContentValues();
                            contentValues.put("uid", com.tencent.wxop.stat.common.q.b(strA));
                            contentValues.put("user_type", Integer.valueOf(i3));
                            contentValues.put("app_ver", com.tencent.wxop.stat.common.k.n(context));
                            contentValues.put("ts", Long.valueOf(jCurrentTimeMillis));
                            if (z2) {
                                this.c.getWritableDatabase().update("user", contentValues, "uid=?", new String[]{string});
                            }
                            if (i3 != i2) {
                                this.c.getWritableDatabase().replace("user", null, contentValues);
                            }
                        }
                        z2 = false;
                        if (strArrSplit != null) {
                            strC = com.tencent.wxop.stat.common.k.c(context);
                            if (strC != null) {
                                strA = str + "," + strC;
                                z2 = true;
                            }
                        } else {
                            strC = com.tencent.wxop.stat.common.k.c(context);
                            if (strC != null) {
                                strA = str + "," + strC;
                                z2 = true;
                            }
                        }
                        this.b = new com.tencent.wxop.stat.common.a(str, strC, i3);
                        contentValues = new ContentValues();
                        contentValues.put("uid", com.tencent.wxop.stat.common.q.b(strA));
                        contentValues.put("user_type", Integer.valueOf(i3));
                        contentValues.put("app_ver", com.tencent.wxop.stat.common.k.n(context));
                        contentValues.put("ts", Long.valueOf(jCurrentTimeMillis));
                        if (z2) {
                            this.c.getWritableDatabase().update("user", contentValues, "uid=?", new String[]{string});
                        }
                        if (i3 != i2) {
                            this.c.getWritableDatabase().replace("user", null, contentValues);
                        }
                    }
                    z2 = true;
                    if (strArrSplit != null) {
                        strC = com.tencent.wxop.stat.common.k.c(context);
                        if (strC != null) {
                            strA = str + "," + strC;
                            z2 = true;
                        }
                    } else {
                        strC = com.tencent.wxop.stat.common.k.c(context);
                        if (strC != null) {
                            strA = str + "," + strC;
                            z2 = true;
                        }
                    }
                    this.b = new com.tencent.wxop.stat.common.a(str, strC, i3);
                    contentValues = new ContentValues();
                    contentValues.put("uid", com.tencent.wxop.stat.common.q.b(strA));
                    contentValues.put("user_type", Integer.valueOf(i3));
                    contentValues.put("app_ver", com.tencent.wxop.stat.common.k.n(context));
                    contentValues.put("ts", Long.valueOf(jCurrentTimeMillis));
                    if (z2) {
                        this.c.getWritableDatabase().update("user", contentValues, "uid=?", new String[]{string});
                    }
                    if (i3 != i2) {
                        this.c.getWritableDatabase().replace("user", null, contentValues);
                    }
                } else {
                    z = false;
                }
                if (!z) {
                    String strB = com.tencent.wxop.stat.common.k.b(context);
                    String strC2 = com.tencent.wxop.stat.common.k.c(context);
                    String str2 = (strC2 == null || strC2.length() <= 0) ? strB : strB + "," + strC2;
                    long jCurrentTimeMillis2 = System.currentTimeMillis() / 1000;
                    String strN = com.tencent.wxop.stat.common.k.n(context);
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("uid", com.tencent.wxop.stat.common.q.b(str2));
                    contentValues2.put("user_type", (Integer) 0);
                    contentValues2.put("app_ver", strN);
                    contentValues2.put("ts", Long.valueOf(jCurrentTimeMillis2));
                    this.c.getWritableDatabase().insert("user", null, contentValues2);
                    this.b = new com.tencent.wxop.stat.common.a(strB, strC2, 0);
                }
                this.c.getWritableDatabase().setTransactionSuccessful();
                if (cursorQuery != null) {
                    try {
                        cursorQuery.close();
                    } catch (Throwable th) {
                        th = th;
                        statLogger = h;
                        statLogger.e(th);
                    }
                }
                this.c.getWritableDatabase().endTransaction();
            } catch (Throwable th2) {
                th = th2;
                cursor = cursorQuery;
                try {
                    h.e(th);
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Throwable th3) {
                            th = th3;
                            statLogger = h;
                            statLogger.e(th);
                        }
                    }
                    this.c.getWritableDatabase().endTransaction();
                } catch (Throwable th4) {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Throwable th5) {
                            h.e(th5);
                            throw th4;
                        }
                    }
                    this.c.getWritableDatabase().endTransaction();
                    throw th4;
                }
            }
        } catch (Throwable th6) {
            th = th6;
        }
        return this.b;
    }

    void c() {
        if (StatConfig.isEnableStatService()) {
            try {
                this.e.a(new ax(this));
            } catch (Throwable th) {
                h.e(th);
            }
        }
    }

    void d() {
        Cursor cursorQuery = null;
        try {
            cursorQuery = this.c.getReadableDatabase().query("config", null, null, null, null, null, null);
            while (cursorQuery.moveToNext()) {
                int i2 = cursorQuery.getInt(0);
                String string = cursorQuery.getString(1);
                String string2 = cursorQuery.getString(2);
                int i3 = cursorQuery.getInt(3);
                f fVar = new f(i2);
                fVar.a = i2;
                fVar.b = new JSONObject(string);
                fVar.c = string2;
                fVar.d = i3;
                StatConfig.a(i, fVar);
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        } catch (Throwable th) {
            try {
                h.e(th);
            } finally {
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            }
        }
    }
}
