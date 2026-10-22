package cn.jpush.android.api;

import cn.jpush.android.helper.g;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class CallBackParams {
    public int action;
    public String alias;
    public int protoType;
    private long sendTime;
    public int sequence;
    public TagAliasCallback tagAliasCallBack;
    public Set<String> tags;

    public CallBackParams(int i, String str, long j, int i2, int i3) {
        this.sequence = i;
        this.alias = str;
        this.sendTime = j;
        this.protoType = i2;
        this.action = i3;
    }

    public CallBackParams(int i, Set<String> set, long j, int i2, int i3) {
        this.sequence = i;
        this.tags = set;
        this.sendTime = j;
        this.protoType = i2;
        this.action = i3;
    }

    public CallBackParams(String str, Set<String> set, TagAliasCallback tagAliasCallback, long j, int i, int i2) {
        this.alias = str;
        this.tags = set;
        this.tagAliasCallBack = tagAliasCallback;
        this.sendTime = j;
        this.protoType = i;
        this.action = i2;
        this.sequence = (int) g.a();
    }

    public boolean isTimeOut(long j) {
        return this.protoType == 0 && System.currentTimeMillis() - this.sendTime > j + OkHttpUtils.DEFAULT_MILLISECONDS;
    }

    public String toString() {
        return "CallBackParams{sendTime=" + this.sendTime + ", alias='" + this.alias + "', tags=" + this.tags + ", tagAliasCallBack=" + this.tagAliasCallBack + ", sequence=" + this.sequence + ", protoType=" + this.protoType + ", action=" + this.action + '}';
    }
}
