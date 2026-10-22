package cn.jpush.android.q;

import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushResponse;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class e extends JPushResponse {
    int a;
    String b;

    public e(int i, long j, long j2, ByteBuffer byteBuffer) {
        super(i, j, j2, byteBuffer);
        parseBody();
    }

    public e(JPushResponse jPushResponse) {
        this(jPushResponse.getCmd(), jPushResponse.getRid(), jPushResponse.rquestId, jPushResponse.getBody());
    }

    public String a() {
        return this.b;
    }

    @Override // cn.jpush.android.local.JPushResponse
    protected void parseBody() {
        try {
            if (this.cmd == 10) {
                this.a = this.body.getShort();
            }
            if (this.a > 0) {
                Logger.e("TagaliasResponse", "Response error - code:" + this.a);
                return;
            }
            byte[] bArr = new byte[this.body.getShort()];
            this.body.get(bArr);
            this.b = new String(bArr, "UTF-8");
        } catch (Throwable th) {
            Logger.ww("TagaliasResponse", "parse failed:" + th.getMessage());
        }
    }

    @Override // cn.jpush.android.local.JPushResponse
    public String toString() {
        return "[TagaliasResponse] - action:" + this.b + " - " + super.toString();
    }
}
