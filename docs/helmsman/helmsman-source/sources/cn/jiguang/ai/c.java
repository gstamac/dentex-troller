package cn.jiguang.ai;

import java.io.IOException;
import java.nio.ByteBuffer;
import kotlin.UByte;
import kotlin.UShort;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    private ByteBuffer a;
    private int b = -1;
    private int c = -1;

    public c(byte[] bArr) {
        this.a = ByteBuffer.wrap(bArr);
    }

    private void c(int i) throws IOException {
        if (i > this.a.remaining()) {
            throw new IOException("end of input");
        }
    }

    public final int a() {
        return this.a.position();
    }

    public final void a(int i) {
        if (i > this.a.capacity() - this.a.position()) {
            throw new IllegalArgumentException("cannot set active region past end of input");
        }
        ByteBuffer byteBuffer = this.a;
        byteBuffer.limit(byteBuffer.position() + i);
    }

    public final void a(byte[] bArr, int i, int i2) throws IOException {
        c(i2);
        this.a.get(bArr, 1, i2);
    }

    public final int b() {
        return this.a.remaining();
    }

    public final void b(int i) {
        if (i >= this.a.capacity()) {
            throw new IllegalArgumentException("cannot jump past end of input");
        }
        this.a.position(i);
        ByteBuffer byteBuffer = this.a;
        byteBuffer.limit(byteBuffer.capacity());
    }

    public final void c() {
        ByteBuffer byteBuffer = this.a;
        byteBuffer.limit(byteBuffer.capacity());
    }

    public final void d() {
        this.b = this.a.position();
        this.c = this.a.limit();
    }

    public final void e() {
        int i = this.b;
        if (i < 0) {
            throw new IllegalStateException("no previous state");
        }
        this.a.position(i);
        this.a.limit(this.c);
        this.b = -1;
        this.c = -1;
    }

    public final int f() throws IOException {
        c(1);
        return this.a.get() & UByte.MAX_VALUE;
    }

    public final int g() throws IOException {
        c(2);
        return this.a.getShort() & UShort.MAX_VALUE;
    }

    public final long h() throws IOException {
        c(4);
        return ((long) this.a.getInt()) & 4294967295L;
    }
}
