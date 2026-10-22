package javy.lib.jsave.storage;

import java.util.List;
import javy.lib.jsave.bean.JSaveLitePalBean;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class JSaveLitepalStrorage implements JSaveStorage<JSaveLitePalBean> {
    @Override // javy.lib.jsave.storage.JSaveStorage
    public boolean save(JSaveLitePalBean jSaveLitePalBean) {
        return jSaveLitePalBean.saveOrUpdate("strkey=?", jSaveLitePalBean.getStrKey());
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public JSaveLitePalBean read(String str) {
        List listFind = DataSupport.where("strKey =?", str).find(JSaveLitePalBean.class);
        if (listFind.size() > 0) {
            return (JSaveLitePalBean) listFind.get(0);
        }
        return null;
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public int deleteAll() {
        return DataSupport.deleteAll("JSaveLitePalBean", new String[0]);
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public boolean delete(String str) {
        JSaveLitePalBean jSaveLitePalBean = read(str);
        return jSaveLitePalBean != null && jSaveLitePalBean.delete() > 0;
    }
}
