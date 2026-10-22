package javy.lib.jsave;

import android.graphics.Bitmap;
import javy.lib.jsave.bean.FileBean;
import javy.lib.jsave.bean.JSaveLitePalBean;
import javy.lib.jsave.converter.JSaveBitmapConverter;
import javy.lib.jsave.converter.JSaveConverter;
import javy.lib.jsave.converter.JSaveGsonConverter;
import javy.lib.jsave.storage.JSaveFileStorage;
import javy.lib.jsave.storage.JSaveLitepalStrorage;
import javy.lib.jsave.storage.JSaveSqliteStorage;
import javy.lib.jsave.storage.JSaveStorage;

/* JADX INFO: loaded from: classes2.dex */
public class JSaveStorageManager<T> {
    private final String BITMAP_TYPE = "android.graphics.Bitmap";
    private JSaveSqliteStorage sqliteStorage = new JSaveSqliteStorage();
    private JSaveStorage<JSaveLitePalBean> storage = new JSaveLitepalStrorage();
    private JSaveConverter converter = new JSaveGsonConverter();
    private JSaveBitmapConverter bitmapConverter = new JSaveBitmapConverter();
    private JSaveFileStorage fileStorage = new JSaveFileStorage();

    JSaveStorageManager() {
        this.sqliteStorage.convertToLitePal();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean save(String str, T t) {
        JSaveLitePalBean jSaveLitePalBean = new JSaveLitePalBean();
        String name = t.getClass().getName();
        name.hashCode();
        if (name.equals("android.graphics.Bitmap")) {
            byte[] bArrBitmapToByteArray = this.bitmapConverter.bitmapToByteArray((Bitmap) t);
            jSaveLitePalBean.setStrKey(str);
            jSaveLitePalBean.setStrType(t.getClass().getName());
            if (this.fileStorage.save(new FileBean(str, bArrBitmapToByteArray))) {
                return this.storage.save(jSaveLitePalBean);
            }
            return false;
        }
        String string = this.converter.toString(t);
        jSaveLitePalBean.setStrKey(str);
        jSaveLitePalBean.setStrValue(string);
        jSaveLitePalBean.setStrType(t.getClass().getName());
        return this.storage.save(jSaveLitePalBean);
    }

    public T read(String str) {
        JSaveLitePalBean jSaveLitePalBean;
        if (str == null || (jSaveLitePalBean = this.storage.read(str)) == null) {
            return null;
        }
        String strType = jSaveLitePalBean.getStrType();
        strType.hashCode();
        if (strType.equals("android.graphics.Bitmap")) {
            FileBean fileBean = this.fileStorage.read(str);
            if (fileBean == null) {
                return null;
            }
            return (T) this.bitmapConverter.byteArrayToBitmap(fileBean.getDatas());
        }
        return (T) this.converter.fromString(jSaveLitePalBean.getStrValue(), jSaveLitePalBean.getStrType());
    }

    public boolean delete(String str) {
        this.fileStorage.delete(str);
        return this.storage.delete(str);
    }

    public int deleteAll() {
        this.fileStorage.deleteAll();
        return this.storage.deleteAll();
    }
}
