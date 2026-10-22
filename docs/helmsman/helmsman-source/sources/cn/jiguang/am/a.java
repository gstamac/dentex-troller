package cn.jiguang.am;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import kotlin.jvm.internal.ShortCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class a implements Closeable {
    protected SocketChannel b;
    protected int c;
    protected Selector d;
    protected int f = 20;
    protected ByteBuffer a = ByteBuffer.allocate(49152);
    protected boolean e = false;

    public int a(String str, int i) {
        if (this.a == null) {
            this.a = ByteBuffer.allocate(49152);
        }
        this.a.clear();
        this.c = 0;
        this.e = true;
        return 0;
    }

    public abstract int a(byte[] bArr);

    public abstract ByteBuffer a(int i);

    public final boolean a() {
        SocketChannel socketChannel;
        return this.e && (socketChannel = this.b) != null && socketChannel.isConnected();
    }

    protected final int b() {
        if (this.c < this.f) {
            return 0;
        }
        int iPosition = this.a.position();
        this.a.position(0);
        int i = this.a.getShort() & ShortCompanionObject.MAX_VALUE;
        this.a.position(iPosition);
        return i;
    }

    protected final ByteBuffer b(int i) {
        int i2 = this.c;
        if (i2 < i) {
            return null;
        }
        this.c = i2 - i;
        byte[] bArr = new byte[i];
        this.a.flip();
        this.a.get(bArr, 0, i);
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        this.a.compact();
        return byteBufferWrap;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.e = false;
        ByteBuffer byteBuffer = this.a;
        if (byteBuffer != null) {
            byteBuffer.clear();
        }
        this.c = 0;
    }
}
