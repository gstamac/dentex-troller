package javy.lib.jsave.bean;

import android.content.ContentValues;

/* JADX INFO: loaded from: classes2.dex */
public class JSaveDefaultBean {
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

    public ContentValues toContentValue() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("strKey", this.strKey);
        contentValues.put("strValue", this.strValue);
        contentValues.put("strType", this.strType);
        return contentValues;
    }
}
