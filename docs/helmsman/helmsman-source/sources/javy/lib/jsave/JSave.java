package javy.lib.jsave;

import android.content.Context;
import android.util.Log;
import javy.lib.jsave.converter.JSaveConverter;
import javy.lib.jsave.converter.JSaveGsonConverter;

/* JADX INFO: loaded from: classes2.dex */
public class JSave {
    protected static JSaveConverter converter;
    public static Context mContext;
    protected static JSaveStorageManager saveStorageManager;

    private JSave() {
    }

    public static void init(Context context) {
        mContext = context;
        if (converter == null) {
            converter = new JSaveGsonConverter();
        }
        if (saveStorageManager == null) {
            saveStorageManager = new JSaveStorageManager();
        }
    }

    public static <T> boolean put(String str, T t) {
        Log.d("JSave ", "key:" + str + " value:" + t);
        if (str == null) {
            Log.e("null key", "Key must not be empty");
            return false;
        }
        if (t != null) {
            return saveStorageManager.save(str, t);
        }
        return saveStorageManager.delete(str);
    }

    public static boolean delete(String str) {
        return saveStorageManager.delete(str);
    }

    public static <T> T get(String str) {
        if (str == null) {
            return null;
        }
        return (T) saveStorageManager.read(str);
    }

    public static <T> T get(String str, T t) {
        if (str == null) {
            return null;
        }
        T t2 = (T) saveStorageManager.read(str);
        return t2 == null ? t : t2;
    }

    public static int deleteAll() {
        return saveStorageManager.deleteAll();
    }
}
