package im.helmsman.helmsmanandroid.utils;

import android.util.Log;
import com.google.gson.Gson;
import im.helmsman.helmsmanandroid.dao.KeyValueDao;
import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class JSave {
    public static <T> boolean put(String str, T t) {
        Log.d("Jsave ", "key:" + str + " value:" + t);
        if (str == null) {
            Log.e("null key", "Key must not be empty");
            return false;
        }
        if (t == null) {
            List<T> listFind = DataSupport.where("strKey=?", str).find(KeyValueDao.class);
            return listFind == null || listFind.size() <= 0 || ((KeyValueDao) listFind.get(0)).delete() > 0;
        }
        String json = new Gson().toJson(t);
        if (json == null) {
            return false;
        }
        KeyValueDao keyValueDao = new KeyValueDao();
        keyValueDao.setStrKey(str);
        keyValueDao.setStrValue(json);
        keyValueDao.setStrClass(t.getClass().getName());
        return keyValueDao.saveOrUpdate("strKey=?", str);
    }

    public static boolean delete(String str) {
        return put("key", null);
    }

    public static <T> T get(String str) {
        List<T> listFind;
        Object objFromJson;
        T t = null;
        if (str == null || (listFind = DataSupport.where("strKey=?", str).find(KeyValueDao.class)) == null || listFind.size() == 0) {
            return null;
        }
        KeyValueDao keyValueDao = (KeyValueDao) listFind.get(0);
        keyValueDao.getStrKey();
        keyValueDao.getStrValue();
        try {
            if (keyValueDao.getStrClass() == null) {
                objFromJson = new Gson().fromJson(keyValueDao.getStrValue(), (Class<Object>) Object.class);
            } else {
                objFromJson = new Gson().fromJson(keyValueDao.getStrValue(), (Class<Object>) Class.forName(keyValueDao.getStrClass()));
            }
            t = (T) objFromJson;
            return t;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return t;
        }
    }

    public static boolean deleteAll() {
        List listFindAll = DataSupport.findAll(KeyValueDao.class, new long[0]);
        return listFindAll == null || listFindAll.size() == 0 || DataSupport.deleteAll((Class<?>) KeyValueDao.class, new String[0]) > 0;
    }

    public static void moveDataToNewJSave() {
        for (KeyValueDao keyValueDao : DataSupport.findAll(KeyValueDao.class, new long[0])) {
            try {
                javy.lib.jsave.JSave.put(keyValueDao.getStrKey(), new Gson().fromJson(keyValueDao.getStrValue(), (Class) Class.forName(keyValueDao.getStrClass())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        deleteAll();
    }
}
