package im.helmsman.helmsmanandroid.dao;

import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class KeyValueDao extends DataSupport {
    private String strClass;
    private String strKey;
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

    public String getStrClass() {
        return this.strClass;
    }

    public void setStrClass(String str) {
        this.strClass = str;
    }
}
