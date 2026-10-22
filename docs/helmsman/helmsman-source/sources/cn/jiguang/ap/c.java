package cn.jiguang.ap;

import cn.jiguang.api.utils.ProtocolUtil;
import java.nio.ByteBuffer;
import kotlin.jvm.internal.ShortCompanionObject;
import okhttp3.internal.ws.WebSocketProtocol;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    int a;
    int b;
    int c;
    Long d;
    int e;
    long f;
    private boolean g;

    public c(boolean z, int i, int i2, int i3, long j, int i4, long j2) {
        this.g = z;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = Long.valueOf(j);
        this.e = i4;
        this.f = j2;
    }

    public c(boolean z, int i, int i2, long j) {
        this(z, 0, i, i2, j, 0, 0L);
    }

    public c(boolean z, byte[] bArr) {
        this.g = z;
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        this.a = byteBufferWrap.getShort() & ShortCompanionObject.MAX_VALUE;
        this.b = byteBufferWrap.get();
        this.c = byteBufferWrap.get();
        Long lValueOf = Long.valueOf(byteBufferWrap.getLong());
        this.d = lValueOf;
        this.d = Long.valueOf(lValueOf.longValue() & WebSocketProtocol.PAYLOAD_SHORT_MAX);
        if (z) {
            this.e = byteBufferWrap.getInt();
        }
        this.f = byteBufferWrap.getLong();
    }

    public final int a() {
        return this.c;
    }

    public final void a(int i) {
        this.a = i;
    }

    public final void a(long j) {
        this.f = j;
    }

    public final Long b() {
        return this.d;
    }

    public final void b(int i) {
        this.e = i;
    }

    public final long c() {
        return this.f;
    }

    public final int d() {
        return this.e;
    }

    public final int e() {
        return this.b;
    }

    public final byte[] f() {
        if (this.a == 0) {
            throw new IllegalStateException("The head is not initialized yet.");
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
        byteBufferAllocate.putShort((short) this.a);
        byteBufferAllocate.put((byte) this.b);
        byteBufferAllocate.put((byte) this.c);
        byteBufferAllocate.putLong(this.d.longValue());
        if (this.g) {
            byteBufferAllocate.putInt(this.e);
        }
        byteBufferAllocate.putLong(this.f);
        byteBufferAllocate.flip();
        return ProtocolUtil.getBytesConsumed(byteBufferAllocate);
    }

    public final String toString() {
        return "[JHead] - len:" + this.a + ", version:" + this.b + ", command:" + this.c + ", rid:" + this.d + (this.g ? ", sid:" + this.e : "") + ", juid:" + this.f;
    }
}
