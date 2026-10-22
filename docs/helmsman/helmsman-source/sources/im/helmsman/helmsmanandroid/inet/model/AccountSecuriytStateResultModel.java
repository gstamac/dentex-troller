package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class AccountSecuriytStateResultModel {
    private String email;
    private boolean email_is_verified;
    private String fb_id;
    private boolean password_is_null;
    private String wx_union_id;

    public String getWx_union_id() {
        return this.wx_union_id;
    }

    public void setWx_union_id(String str) {
        this.wx_union_id = str;
    }

    public String getFb_id() {
        return this.fb_id;
    }

    public void setFb_id(String str) {
        this.fb_id = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public boolean isEmail_is_verified() {
        return this.email_is_verified;
    }

    public void setEmail_is_verified(boolean z) {
        this.email_is_verified = z;
    }

    public boolean isPassword_is_null() {
        return this.password_is_null;
    }

    public void setPassword_is_null(boolean z) {
        this.password_is_null = z;
    }
}
