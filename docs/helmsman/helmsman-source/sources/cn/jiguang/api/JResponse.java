package cn.jiguang.api;

import cn.jiguang.api.utils.ByteBufferUtils;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public abstract class JResponse extends JProtocol {
    public int code;

    public JResponse(int i, int i2, long j, long j2, int i3, String str) {
        super(false, i, i2, j, -1, j2);
        this.code = i3;
    }

    public JResponse(Object obj, ByteBuffer byteBuffer) {
        super(false, obj, byteBuffer);
    }

    public JResponse(ByteBuffer byteBuffer, byte[] bArr) {
        super(false, byteBuffer, bArr);
    }

    @Override // cn.jiguang.api.JProtocol
    protected void parseBody() {
        if (isNeedParseeErrorMsg()) {
            this.code = ByteBufferUtils.getShort(this.body, this);
        }
    }

    @Override // cn.jiguang.api.JProtocol
    public String toString() {
        return "JResponse{code=" + this.code + '}';
    }

    @Override // cn.jiguang.api.JProtocol
    protected void writeBody() {
        int i = this.code;
        if (i >= 0) {
            writeInt2(i);
        }
    }
}
