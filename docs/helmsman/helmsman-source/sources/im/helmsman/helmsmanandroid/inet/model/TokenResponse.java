package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class TokenResponse {
    private long expiration;
    private String token;

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public void setExpiration(long j) {
        this.expiration = j;
    }
}
