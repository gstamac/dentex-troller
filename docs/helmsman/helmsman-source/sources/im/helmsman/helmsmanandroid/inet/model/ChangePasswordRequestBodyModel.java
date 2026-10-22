package im.helmsman.helmsmanandroid.inet.model;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes2.dex */
public class ChangePasswordRequestBodyModel {

    @SerializedName("new")
    private String newX;
    private String old;

    public String getOld() {
        return this.old;
    }

    public void setOld(String str) {
        this.old = str;
    }

    public String getNewX() {
        return this.newX;
    }

    public void setNewX(String str) {
        this.newX = str;
    }
}
