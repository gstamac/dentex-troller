package im.helmsman.helmsmanandroid.inet.RequestBean;

/* JADX INFO: loaded from: classes2.dex */
public class SendFbInfoToServerRequest {
    private String access_token;
    private String email;
    private String id;
    private String name;
    private int sex;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String str) {
        this.access_token = str;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int i) {
        this.sex = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
