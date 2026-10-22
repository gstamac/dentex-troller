package im.helmsman.helmsmanandroid.inet.RequestBean;

/* JADX INFO: loaded from: classes2.dex */
public class SendWxInfoToServerRequest {
    private String access_token;
    private String open_id;
    private String refresh_token;
    private int sex;
    private String union_id;

    public String getUnion_id() {
        return this.union_id;
    }

    public void setUnion_id(String str) {
        this.union_id = str;
    }

    public String getOpen_id() {
        return this.open_id;
    }

    public void setOpen_id(String str) {
        this.open_id = str;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String str) {
        this.access_token = str;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }

    public void setRefresh_token(String str) {
        this.refresh_token = str;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int i) {
        this.sex = i;
    }
}
