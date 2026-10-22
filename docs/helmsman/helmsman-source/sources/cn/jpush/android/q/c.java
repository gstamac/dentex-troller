package cn.jpush.android.q;

import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushResponse;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class c extends JPushResponse {
    int a;
    long b;
    String c;

    public c(int i, long j, long j2, ByteBuffer byteBuffer) {
        super(i, j, j2, byteBuffer);
        parseBody();
    }

    public c(JPushResponse jPushResponse) {
        this(jPushResponse.getCmd(), jPushResponse.getRid(), jPushResponse.rquestId, jPushResponse.getBody());
    }

    public int a() {
        return this.a;
    }

    public long b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    @Override // cn.jpush.android.local.JPushResponse
    protected void parseBody() {
        try {
            this.a = this.body.get();
            this.b = this.body.getLong();
            byte[] bArr = new byte[this.body.getShort()];
            this.body.get(bArr);
            this.c = new String(bArr, "UTF-8");
        } catch (Throwable unused) {
            Logger.ww("MessagePush", "parse msg content failed");
        }
    }

    @Override // cn.jpush.android.local.JPushResponse
    public String toString() {
        return "[MessagePush] - msgType:" + this.a + ", msgId:" + this.b + ", msgContent:" + this.c + " - " + super.toString();
    }
}
