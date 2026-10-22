package javy.lib.jsave.bean;

import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class JSaveLitePalBean extends DataSupport {
    private String strKey;
    private String strType;
    private String strValue;

    public String getStrKey() {
        return this.strKey;
    }

    public void setStrKey(String str) {
        this.strKey = str;
    }

    public String getStrValue() {
        return this.strValue;
    }

    public void setStrValue(String str) {
        this.strValue = str;
    }

    public String getStrType() {
        return this.strType;
    }

    public void setStrType(String str) {
        this.strType = str;
    }
}
