package cn.jpush.android.ups;

/* JADX INFO: loaded from: classes.dex */
public class TokenResult {
    private String actionType;
    private int returnCode;
    private String token;

    public TokenResult(String str, int i) {
        this.token = str;
        this.returnCode = i;
    }

    public TokenResult(String str, int i, String str2) {
        this.token = str;
        this.returnCode = i;
        this.actionType = str2;
    }

    public String getActionType() {
        return this.actionType;
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    public String getToken() {
        return this.token;
    }

    public void setActionType(String str) {
        this.actionType = str;
    }

    public void setReturnCode(int i) {
        this.returnCode = i;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public String toString() {
        return "TokenResult{token='" + this.token + "', returnCode=" + this.returnCode + ", actionType='" + this.actionType + "'}";
    }
}
