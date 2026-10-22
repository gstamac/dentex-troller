package im.helmsman.helmsmanandroid.dao;

import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class UserManualsDao extends DataSupport {
    private String downloadUrl;
    private int manualsId;

    public UserManualsDao(String str, int i) {
        this.downloadUrl = str;
        this.manualsId = i;
    }

    public UserManualsDao() {
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String str) {
        this.downloadUrl = str;
    }

    public int getManualsId() {
        return this.manualsId;
    }

    public void setManualsId(int i) {
        this.manualsId = i;
    }
}
