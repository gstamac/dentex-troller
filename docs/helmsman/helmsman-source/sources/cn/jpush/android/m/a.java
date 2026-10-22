package cn.jpush.android.m;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushConstants;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static volatile a a;
    private static ExecutorService b = Executors.newSingleThreadExecutor();
    private static final Object f = new Object();
    private Handler c;
    private Context d;
    private String e;

    private a(Context context) {
        this.c = null;
        this.d = null;
        this.e = "";
        Logger.d("JPushLocalNotificationCenter", "Constructor : JPushLocalNotificationCenter");
        this.c = new Handler(Looper.getMainLooper());
        this.d = context;
        this.e = context.getPackageName();
    }

    public static a a(Context context) {
        Logger.v("JPushLocalNotificationCenter", "getInstance");
        if (a == null) {
            synchronized (f) {
                if (a == null) {
                    a = new a(context);
                }
            }
        }
        return a;
    }

    private synchronized void a(final long j, long j2, int i) {
        Logger.d("JPushLocalNotificationCenter", "LocalNotification scheduleReadiedLN");
        if (this.c != null) {
            Runnable runnable = new Runnable() { // from class: cn.jpush.android.m.a.2
                @Override // java.lang.Runnable
                public void run() {
                    long jA;
                    int i2;
                    int i3;
                    int i4;
                    String strD;
                    long jF;
                    long jE;
                    try {
                        b bVarA = b.a(a.this.d);
                        c cVarA = bVarA.a(j, 0);
                        if (cVarA != null) {
                            if (cVarA.c() == 1) {
                                Logger.d("JPushLocalNotificationCenter", "remove ");
                                jA = cVarA.a();
                                i2 = 0;
                                i3 = 1;
                                i4 = 0;
                                strD = cVarA.d();
                                jF = cVarA.f();
                                jE = cVarA.e();
                            } else {
                                if (cVarA.b() > 1) {
                                    Logger.d("JPushLocalNotificationCenter", "repeat add");
                                    bVarA.b(cVarA.a(), cVarA.b() - 1, 0, 0, cVarA.d(), cVarA.f(), cVarA.e());
                                    return;
                                }
                                if (cVarA.b() != 1) {
                                    Logger.d("JPushLocalNotificationCenter", "already triggered");
                                    return;
                                }
                                Logger.d("JPushLocalNotificationCenter", "send broadcast");
                                if (cVarA.f() > System.currentTimeMillis()) {
                                    Logger.d("JPushLocalNotificationCenter", "repeat add");
                                    return;
                                }
                                a aVar = a.this;
                                aVar.a(aVar.d, cVarA.d());
                                jA = cVarA.a();
                                i2 = 0;
                                i3 = 0;
                                i4 = 0;
                                strD = cVarA.d();
                                jF = cVarA.f();
                                jE = cVarA.e();
                            }
                            bVarA.b(jA, i2, i3, i4, strD, jF, jE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.e("JPushLocalNotificationCenter", "exception:" + e.getMessage());
                    }
                }
            };
            if (j2 <= 0) {
                Logger.d("JPushLocalNotificationCenter", "post right now ");
                this.c.post(runnable);
            } else {
                Logger.d("JPushLocalNotificationCenter", "post delayed : " + j2);
                this.c.postDelayed(runnable, j2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Context context, String str) {
        Logger.d("JPushLocalNotificationCenter", "start LocalNotification broadCastToPushService");
        Bundle bundle = new Bundle();
        bundle.putString("local_notify_msg", str);
        JCoreHelper.runActionWithService(context, JPushConstants.SDK_TYPE, "show_local_notify", bundle);
        Logger.d("JPushLocalNotificationCenter", "end LocalNotification broadCastToPushService");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void e(Context context) {
        Logger.d("JPushLocalNotificationCenter", "triggerLNKillProcess");
        b bVarA = b.a(context);
        if (bVarA.a(false)) {
            Cursor cursor = null;
            try {
                try {
                    Cursor cursorA = bVarA.a(1, System.currentTimeMillis());
                    try {
                        if (cursorA.moveToFirst()) {
                            do {
                                c cVarA = b.a(cursorA);
                                if (cVarA != null) {
                                    a(context, cVarA.d());
                                    bVarA.b(cVarA.a(), 0, 0, 0, cVarA.d(), cVarA.f(), cVarA.e());
                                }
                            } while (cursorA.moveToNext());
                        }
                        if (cursorA != null) {
                            cursorA.close();
                        }
                    } catch (Exception e) {
                        e = e;
                        cursor = cursorA;
                        Logger.ww("JPushLocalNotificationCenter", "triggerLNKillProcess: " + e.getMessage());
                        if (cursor != null) {
                            cursor.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor = cursorA;
                        if (cursor != null) {
                            cursor.close();
                        }
                        bVarA.b(false);
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                }
                bVarA.b(false);
            } catch (Throwable th2) {
                th = th2;
            }
        } else {
            Logger.e("JPushLocalNotificationCenter", "init LocalNotification cast expt: db open failed");
        }
    }

    public synchronized boolean a(Context context, long j) {
        Logger.dd("JPushLocalNotificationCenter", "remove LocalNotification ");
        long j2 = (int) j;
        try {
            b bVarA = b.a(context);
            c cVarA = bVarA.a(j2, 0);
            if (cVarA == null) {
                return false;
            }
            Logger.d("JPushLocalNotificationCenter", "remove local count : " + cVarA.b());
            bVarA.a(j2);
            JPushInterface.clearNotificationById(this.d, (int) j2);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("JPushLocalNotificationCenter", "exception:" + e.getMessage());
            return false;
        }
        throw th;
    }

    public synchronized boolean a(Context context, JPushLocalNotification jPushLocalNotification) {
        Logger.dd("JPushLocalNotificationCenter", "add LocalNotification");
        long jCurrentTimeMillis = System.currentTimeMillis();
        long broadcastTime = jPushLocalNotification.getBroadcastTime() - jCurrentTimeMillis;
        if (cn.jpush.android.cache.a.d(context)) {
            Logger.d("JPushLocalNotificationCenter", "push has stopped");
        }
        long notificationId = jPushLocalNotification.getNotificationId();
        try {
            b bVarA = b.a(context);
            if (bVarA.a(notificationId, 0) != null) {
                bVarA.b(notificationId, 1, 0, 0, jPushLocalNotification.toJSON(), jPushLocalNotification.getBroadcastTime(), jCurrentTimeMillis);
            } else {
                bVarA.a(notificationId, 1, 0, 0, jPushLocalNotification.toJSON(), jPushLocalNotification.getBroadcastTime(), jCurrentTimeMillis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("JPushLocalNotificationCenter", "exception:" + e.getMessage());
        }
        if (broadcastTime >= 300000) {
            return true;
        }
        a(jPushLocalNotification.getNotificationId(), broadcastTime, 0);
        return true;
    }

    public synchronized void b(Context context) {
        Logger.dd("JPushLocalNotificationCenter", "clear all local notification ");
        int[] iArrA = b.a(context).a();
        if (iArrA != null && iArrA.length > 0) {
            Logger.d("JPushLocalNotificationCenter", " success");
            for (int i : iArrA) {
                JPushInterface.clearNotificationById(this.d, i);
            }
        }
    }

    public synchronized void c(Context context) {
        Logger.d("JPushLocalNotificationCenter", "init LocalNotification");
        b bVarA = b.a(context);
        if (bVarA.a(false)) {
            Cursor cursorA = null;
            try {
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    cursorA = bVarA.a(jCurrentTimeMillis, 300000L);
                    if (cursorA.moveToFirst()) {
                        do {
                            c cVarA = b.a(cursorA);
                            if (cVarA != null) {
                                a(cVarA.a(), cVarA.f() - jCurrentTimeMillis, 0);
                            }
                        } while (cursorA.moveToNext());
                    }
                    if (cursorA != null) {
                        cursorA.close();
                    }
                } catch (Exception e) {
                    Logger.e("JPushLocalNotificationCenter", "init LocalNotification cast expt:" + e);
                    if (cursorA != null) {
                        cursorA.close();
                    }
                }
                bVarA.b(false);
            } catch (Throwable th) {
                if (cursorA != null) {
                    cursorA.close();
                }
                bVarA.b(false);
                throw th;
            }
        } else {
            Logger.e("JPushLocalNotificationCenter", "init LocalNotification cast expt: db open failed");
        }
    }

    public void d(final Context context) {
        Logger.d("JPushLocalNotificationCenter", "LocalNotification onHeartBeat");
        b.execute(new Runnable() { // from class: cn.jpush.android.m.a.1
            @Override // java.lang.Runnable
            public void run() {
                a.this.e(context);
                a.this.c(context);
            }
        });
    }
}
