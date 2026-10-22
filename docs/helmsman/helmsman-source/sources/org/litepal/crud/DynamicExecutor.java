package org.litepal.crud;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.litepal.exceptions.DataSupportException;

/* JADX INFO: loaded from: classes2.dex */
class DynamicExecutor {
    private DynamicExecutor() {
    }

    static Object send(Object obj, String str, Object[] objArr, Class<?> cls, Class<?>[] clsArr) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (objArr == null) {
            try {
                objArr = new Object[0];
            } catch (NoSuchMethodException e) {
                throw new DataSupportException(DataSupportException.noSuchMethodException(cls.getSimpleName(), str), e);
            }
        }
        if (clsArr == null) {
            clsArr = new Class[0];
        }
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        declaredMethod.setAccessible(true);
        return declaredMethod.invoke(obj, objArr);
    }

    static void set(Object obj, String str, Object obj2, Class<?> cls) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        declaredField.set(obj, obj2);
    }

    static void setField(Object obj, String str, Object obj2, Class<?> cls) throws IllegalAccessException, SecurityException, IllegalArgumentException {
        if (cls == DataSupport.class || cls == Object.class) {
            throw new DataSupportException(DataSupportException.noSuchFieldExceptioin(cls.getSimpleName(), str));
        }
        try {
            set(obj, str, obj2, cls);
        } catch (NoSuchFieldException unused) {
            setField(obj, str, obj2, cls.getSuperclass());
        }
    }

    static Object getField(Object obj, String str, Class<?> cls) throws IllegalAccessException, IllegalArgumentException {
        if (cls == DataSupport.class || cls == Object.class) {
            throw new DataSupportException(DataSupportException.noSuchFieldExceptioin(cls.getSimpleName(), str));
        }
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.get(obj);
        } catch (NoSuchFieldException unused) {
            return getField(obj, str, cls.getSuperclass());
        }
    }
}
