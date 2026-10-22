package cn.jiguang.al;

import java.nio.ByteBuffer;
import kotlin.jvm.internal.ShortCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public int a;
    public int b;
    public int c;
    public byte d;
    public long e;
    public int f;
    public long g;
    private boolean h;

    public c(boolean z, byte[] bArr) {
        try {
            this.h = false;
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            this.a = byteBufferWrap.getShort() & ShortCompanionObject.MAX_VALUE;
            this.b = byteBufferWrap.get();
            this.c = byteBufferWrap.get();
            this.d = byteBufferWrap.get();
            byteBufferWrap.get();
            byteBufferWrap.getInt();
            this.e = byteBufferWrap.getShort();
            this.g = byteBufferWrap.getLong();
        } catch (Throwable unused) {
        }
    }

    public final String toString() {
        return "[JHead] - len:" + this.a + ", version:" + this.b + ", command:" + this.c + ", rid:" + this.e + (this.h ? ", sid:" + this.f : "") + ", juid:" + this.g;
    }
}
