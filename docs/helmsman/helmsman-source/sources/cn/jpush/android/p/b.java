package cn.jpush.android.p;

import android.util.Log;
import dalvik.system.DexClassLoader;

/* JADX INFO: loaded from: classes.dex */
public class b extends DexClassLoader {
    private ClassLoader a;

    public b(String str, String str2, String str3, ClassLoader classLoader) {
        super(str, str2, str3, DexClassLoader.class.getClassLoader());
        this.a = classLoader;
        Log.d("PushDexClassLoader", "init jpush cl...");
    }

    @Override // java.lang.ClassLoader
    public Class<?> loadClass(String str) throws ClassNotFoundException {
        try {
            try {
                return super.loadClass(str);
            } catch (ClassNotFoundException unused) {
                return this.a.loadClass(str);
            }
        } catch (Throwable unused2) {
            throw new ClassNotFoundException("jpush not found " + str);
        }
    }
}
