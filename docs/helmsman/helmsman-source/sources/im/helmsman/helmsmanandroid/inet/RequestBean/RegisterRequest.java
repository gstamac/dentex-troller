package im.helmsman.helmsmanandroid.inet.RequestBean;

/* JADX INFO: loaded from: classes2.dex */
public class RegisterRequest {
    private String email;
    private String password;
    private String username;

    public RegisterRequest(String str, String str2, String str3) {
        this.email = str;
        this.password = str2;
        this.username = str3;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }
}
