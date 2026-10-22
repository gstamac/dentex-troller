package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class SendWXInfoToServerResultModel {
    private int expiration;
    private boolean is_registered;
    private String token;

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public int getExpiration() {
        return this.expiration;
    }

    public void setExpiration(int i) {
        this.expiration = i;
    }

    public boolean isIs_registered() {
        return this.is_registered;
    }

    public void setIs_registered(boolean z) {
        this.is_registered = z;
    }
}
