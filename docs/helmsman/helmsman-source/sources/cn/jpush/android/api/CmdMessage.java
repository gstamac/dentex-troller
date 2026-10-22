package cn.jpush.android.api;

import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public class CmdMessage {
    public int cmd;
    public int errorCode;
    public Bundle extra;
    public String msg;

    public CmdMessage(int i, int i2) {
        this.cmd = i;
        this.errorCode = i2;
    }

    public CmdMessage(int i, int i2, String str) {
        this.cmd = i;
        this.errorCode = i2;
        this.msg = str;
    }

    public CmdMessage(int i, int i2, String str, Bundle bundle) {
        this.cmd = i;
        this.errorCode = i2;
        this.msg = str;
        this.extra = bundle;
    }

    public String toString() {
        return "CmdMessage{cmd=" + this.cmd + ", errorCode=" + this.errorCode + ", msg='" + this.msg + "', extra=" + this.extra + '}';
    }
}
