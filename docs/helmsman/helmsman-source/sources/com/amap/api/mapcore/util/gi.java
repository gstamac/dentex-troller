package com.amap.api.mapcore.util;

import android.content.Context;
import java.io.File;
import java.lang.reflect.Constructor;

/* JADX INFO: compiled from: InstanceFactory.java */
/* JADX INFO: loaded from: classes.dex */
public class gi {
    public static <T> T a(Context context, ff ffVar, String str, Class cls, Class[] clsArr, Object[] objArr) throws ev {
        T t = (T) a(b(context, ffVar), str, clsArr, objArr);
        if (t != null) {
            return t;
        }
        T t2 = (T) a(cls, clsArr, objArr);
        if (t2 != null) {
            return t2;
        }
        throw new ev("获取对象错误");
    }

    public static boolean a(Context context, ff ffVar) {
        try {
            File file = new File(gl.b(context, ffVar.a(), ffVar.b()));
            if (file.exists()) {
                return true;
            }
            gl.a(context, file, ffVar);
            return false;
        } catch (Throwable th) {
            gr.a(th, "IFactory", "isdowned");
            return false;
        }
    }

    private static gj b(Context context, ff ffVar) {
        if (context == null) {
            return null;
        }
        try {
            if (a(context, ffVar)) {
                return gq.a().a(context, ffVar);
            }
            return null;
        } catch (Throwable th) {
            gr.a(th, "IFactory", "gIns1");
            return null;
        }
    }

    private static boolean a(gj gjVar) {
        return gjVar != null && gjVar.a() && gjVar.d;
    }

    private static <T> T a(gj gjVar, String str, Class[] clsArr, Object[] objArr) {
        Class<?> clsLoadClass;
        try {
            if (!a(gjVar) || (clsLoadClass = gjVar.loadClass(str)) == null) {
                return null;
            }
            Constructor<?> declaredConstructor = clsLoadClass.getDeclaredConstructor(clsArr);
            declaredConstructor.setAccessible(true);
            return (T) declaredConstructor.newInstance(objArr);
        } catch (Throwable th) {
            gr.a(th, "IFactory", "getWrap");
            return null;
        }
    }

    private static <T> T a(Class cls, Class[] clsArr, Object[] objArr) {
        if (cls == null) {
            return null;
        }
        try {
            Constructor<T> declaredConstructor = cls.getDeclaredConstructor(clsArr);
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance(objArr);
        } catch (Throwable th) {
            gr.a(th, "IFactory", "gIns2()");
            return null;
        }
    }
}
