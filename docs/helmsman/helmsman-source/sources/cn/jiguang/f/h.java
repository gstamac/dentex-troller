package cn.jiguang.f;

/* JADX INFO: loaded from: classes.dex */
public final class h {
    public static Object a(Class<?> cls, String str, Object[] objArr, Class[] clsArr) throws Exception {
        a(cls);
        a(clsArr, objArr);
        return cls.getMethod(str, clsArr).invoke(cls, objArr);
    }

    public static Object a(Object obj, String str, Object[] objArr, Class[] clsArr) throws Exception {
        a(obj);
        a(clsArr, objArr);
        return obj.getClass().getMethod(str, clsArr).invoke(obj, objArr);
    }

    private static void a(Object obj) throws Exception {
        if (obj == null) {
            throw new Exception("owner can not be null");
        }
    }

    private static void a(Class<?>[] clsArr, Object[] objArr) throws Exception {
        if ((objArr != null ? objArr.length : 0) != (clsArr != null ? clsArr.length : 0)) {
            throw new Exception("argClasses' size is not equal to args' size");
        }
    }
}
