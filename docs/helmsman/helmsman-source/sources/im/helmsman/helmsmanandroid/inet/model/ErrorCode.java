package im.helmsman.helmsmanandroid.inet.model;

import com.google.gson.Gson;

/* JADX INFO: loaded from: classes2.dex */
public class ErrorCode {
    private int error_code;
    private String message;
    private String msg;
    private String msg_chn;
    private String tempMsg;
    private String tempTitle;
    private String title;
    private String title_chn;
    private int type;

    public ErrorCode(String str) {
        this.message = str;
    }

    public String getTempMsg() {
        return this.tempMsg;
    }

    public void setTempMsg(String str) {
        this.tempMsg = str;
    }

    public String getTempTitle() {
        return this.tempTitle;
    }

    public void setTempTitle(String str) {
        this.tempTitle = str;
    }

    public int getError_code() {
        return this.error_code;
    }

    public void setError_code(int i) {
        this.error_code = i;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getTitle_chn() {
        return this.title_chn;
    }

    public void setTitle_chn(String str) {
        this.title_chn = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getMsg_chn() {
        return this.msg_chn;
    }

    public void setMsg_chn(String str) {
        this.msg_chn = str;
    }

    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
