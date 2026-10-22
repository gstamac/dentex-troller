package cn.jiguang.ai;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public final class q {
    protected long a;
    protected SelectionKey b;

    private q(long j) throws Throwable {
        Selector selectorOpen;
        SocketChannel socketChannelOpen = SocketChannel.open();
        this.a = j;
        try {
            selectorOpen = Selector.open();
            try {
                socketChannelOpen.configureBlocking(false);
                this.b = socketChannelOpen.register(selectorOpen, 1);
            } catch (Throwable th) {
                th = th;
                if (selectorOpen != null) {
                    selectorOpen.close();
                }
                socketChannelOpen.close();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            selectorOpen = null;
        }
    }

    private void a() throws IOException {
        this.b.selector().close();
        this.b.channel().close();
    }

    private static void a(SelectionKey selectionKey, long j) throws IOException {
        int iSelectNow;
        long jCurrentTimeMillis = j - System.currentTimeMillis();
        if (jCurrentTimeMillis > 0) {
            iSelectNow = selectionKey.selector().select(jCurrentTimeMillis);
        } else {
            iSelectNow = jCurrentTimeMillis == 0 ? selectionKey.selector().selectNow() : 0;
        }
        if (iSelectNow == 0) {
            throw new SocketTimeoutException();
        }
    }

    private byte[] a(int i) {
        SocketChannel socketChannel = (SocketChannel) this.b.channel();
        byte[] bArr = new byte[i];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        this.b.interestOps(1);
        int i2 = 0;
        while (i2 < i) {
            try {
                if (this.b.isReadable()) {
                    long j = socketChannel.read(byteBufferWrap);
                    if (j < 0) {
                        throw new EOFException();
                    }
                    i2 += (int) j;
                    if (i2 < i && System.currentTimeMillis() > this.a) {
                        throw new SocketTimeoutException();
                    }
                } else {
                    a(this.b, this.a);
                }
            } catch (Throwable th) {
                if (this.b.isValid()) {
                    this.b.interestOps(0);
                }
                throw th;
            }
        }
        if (this.b.isValid()) {
            this.b.interestOps(0);
        }
        return bArr;
    }

    public static byte[] a(SocketAddress socketAddress, SocketAddress socketAddress2, byte[] bArr, long j) throws IOException {
        q qVar = new q(j);
        try {
            SocketChannel socketChannel = (SocketChannel) qVar.b.channel();
            if (!socketChannel.connect(socketAddress2)) {
                qVar.b.interestOps(8);
                while (!socketChannel.finishConnect()) {
                    try {
                        if (!qVar.b.isConnectable()) {
                            a(qVar.b, qVar.a);
                        }
                    } catch (Throwable th) {
                        if (qVar.b.isValid()) {
                            qVar.b.interestOps(0);
                        }
                        throw th;
                    }
                }
                if (qVar.b.isValid()) {
                    qVar.b.interestOps(0);
                }
            }
            SocketChannel socketChannel2 = (SocketChannel) qVar.b.channel();
            ByteBuffer[] byteBufferArr = {ByteBuffer.wrap(new byte[]{(byte) (bArr.length >>> 8), (byte) (bArr.length & 255)}), ByteBuffer.wrap(bArr)};
            qVar.b.interestOps(4);
            int i = 0;
            while (i < bArr.length + 2) {
                try {
                    if (qVar.b.isWritable()) {
                        long jWrite = socketChannel2.write(byteBufferArr);
                        if (jWrite < 0) {
                            throw new EOFException();
                        }
                        i += (int) jWrite;
                        if (i < bArr.length + 2 && System.currentTimeMillis() > qVar.a) {
                            throw new SocketTimeoutException();
                        }
                    } else {
                        a(qVar.b, qVar.a);
                    }
                } catch (Throwable th2) {
                    if (qVar.b.isValid()) {
                        qVar.b.interestOps(0);
                    }
                    throw th2;
                }
            }
            if (qVar.b.isValid()) {
                qVar.b.interestOps(0);
            }
            byte[] bArrA = qVar.a(2);
            byte[] bArrA2 = qVar.a(((bArrA[0] & UByte.MAX_VALUE) << 8) + (bArrA[1] & UByte.MAX_VALUE));
            qVar.b.channel();
            qVar.a();
            return bArrA2;
        } catch (Throwable th3) {
            qVar.a();
            throw th3;
        }
    }
}
