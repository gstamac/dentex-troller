package cn.jpush.android.d;

import android.content.Context;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.t.f;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
public class c {
    public static Queue<a> a;

    public static synchronized void a(Context context, String str) {
        try {
            if (context == null) {
                Logger.w("MsgQueueUtils", "unexcepted , context is null");
                return;
            }
            File filesDir = context.getFilesDir();
            if (filesDir == null) {
                Logger.w("MsgQueueUtils", "can't get files dir");
                return;
            }
            File file = new File(filesDir, str);
            if (file.exists()) {
                file.delete();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public static synchronized void a(Context context, String str, ArrayList<a> arrayList) {
        Logger.v("MsgQueueUtils", "Action - saveObjects");
        if (context == null) {
            Logger.w("MsgQueueUtils", "unexcepted , context is null");
            return;
        }
        if (arrayList == null) {
            Logger.w("MsgQueueUtils", "mObjectList is null");
            return;
        }
        try {
            File file = new File(context.getFilesDir(), str);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                jSONArray.put(arrayList.get(i).a());
            }
            cn.jpush.android.t.c.a(file.getAbsolutePath(), jSONArray.toString());
        } catch (Throwable th) {
            Logger.ww("MsgQueueUtils", "save Objects  error:" + th.getMessage());
        }
    }

    public static boolean a(Context context, a aVar) {
        if (a == null) {
            a = new ConcurrentLinkedQueue();
            try {
                ArrayList<a> arrayListB = b(context, "msg_queue_v350");
                if (arrayListB != null && !arrayListB.isEmpty()) {
                    Iterator<a> it = arrayListB.iterator();
                    while (it.hasNext()) {
                        a.offer(it.next());
                    }
                }
            } catch (Exception e) {
                Logger.ww("MsgQueueUtils", "init lastMsgQueue failed:" + e.getMessage());
            }
        }
        if (context == null) {
            Logger.ww("MsgQueueUtils", "#unexcepted - context was null");
            return false;
        }
        if (aVar == null) {
            Logger.ww("MsgQueueUtils", "#unexcepted - entityKey was null");
        }
        if (a.contains(aVar)) {
            Logger.e("MsgQueueUtils", "Duplicated msg. Give up processing - " + aVar);
            return true;
        }
        if (a.size() >= 200) {
            a.poll();
        }
        a.offer(aVar);
        try {
            ArrayList<a> arrayListB2 = b(context, "msg_queue_v350");
            if (arrayListB2 == null) {
                arrayListB2 = new ArrayList<>();
            }
            if (arrayListB2.size() >= 50) {
                arrayListB2.remove(0);
            }
            arrayListB2.add(aVar);
            a(context, "msg_queue_v350", arrayListB2);
        } catch (Exception e2) {
            Logger.ww("MsgQueueUtils", "msg save in sp failed:" + e2.getMessage());
        }
        return false;
    }

    public static synchronized ArrayList<a> b(Context context, String str) {
        FileInputStream fileInputStream;
        Throwable th;
        FileInputStream fileInputStream2 = null;
        if (context == null) {
            Logger.w("MsgQueueUtils", "unexcepted , context is null");
            return null;
        }
        ArrayList<a> arrayList = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir(), str);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                try {
                    JSONArray jSONArray = new JSONArray(new String(f.a((InputStream) fileInputStream)));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        arrayList.add(a.a(jSONArray.getJSONObject(i)));
                    }
                    fileInputStream2 = fileInputStream;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        Logger.w("MsgQueueUtils", "load objects error:" + th.getMessage());
                        a(context, str);
                        f.a((Closeable) fileInputStream);
                    } catch (Throwable th3) {
                        f.a((Closeable) fileInputStream);
                        throw th3;
                    }
                }
            }
            f.a((Closeable) fileInputStream2);
        } catch (Throwable th4) {
            fileInputStream = null;
            th = th4;
        }
        return arrayList;
    }
}
