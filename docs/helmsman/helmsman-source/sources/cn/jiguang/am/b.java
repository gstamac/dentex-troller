package cn.jiguang.am;

import androidx.core.app.NotificationManagerCompat;
import cn.jiguang.ad.d;
import cn.jiguang.aj.f;
import cn.jiguang.f.k;
import com.qiniu.android.http.ResponseInfo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class b extends a {
    private final int g;
    private ByteBuffer h = ByteBuffer.allocate(8192);

    public b(int i, int i2) {
        this.g = i;
        this.f = i2;
    }

    private boolean b(byte[] bArr) {
        try {
            if (!a()) {
                d.b("NioSocketClient", "send error - connect was invalid");
                return false;
            }
            if (bArr != null && bArr.length > 0) {
                int iWrite = this.b.write(ByteBuffer.wrap(bArr));
                if (iWrite > 0) {
                    d.a("NioSocketClient", "isWritable has send len:" + iWrite);
                    return true;
                }
                if (iWrite >= 0) {
                    return true;
                }
                d.a("NioSocketClient", "isWritable error:" + iWrite);
                return false;
            }
            d.b("NioSocketClient", "send error - invalide buffer");
            return false;
        } catch (Exception e) {
            d.g("NioSocketClient", "send data error:" + e);
            close();
            return false;
        }
    }

    @Override // cn.jiguang.am.a
    public final synchronized int a(String str, int i) {
        super.a(str, i);
        try {
            this.b = SocketChannel.open();
            this.d = Selector.open();
            this.b.configureBlocking(false);
            this.b.connect(new InetSocketAddress(str, i));
            d.b("NioSocketClient", "tcp connecting...");
            long jCurrentTimeMillis = System.currentTimeMillis();
            while (!this.b.finishConnect()) {
                if (!this.e) {
                    d.b("NioSocketClient", "has close channel when connect...");
                    return -991;
                }
                Thread.sleep(10L);
                if (System.currentTimeMillis() - jCurrentTimeMillis > 3000) {
                    close();
                    return -994;
                }
            }
            if (!this.e) {
                d.b("NioSocketClient", "has close channel when connected...");
                return -991;
            }
            d.b("NioSocketClient", "tcp connected");
            this.b.register(this.d, 1);
            return 0;
        } catch (Throwable th) {
            d.g("NioSocketClient", "tcp connect has failed:" + th);
            close();
            if (th instanceof SocketTimeoutException) {
                return -994;
            }
            return NotificationManagerCompat.IMPORTANCE_UNSPECIFIED;
        }
    }

    @Override // cn.jiguang.am.a
    public final int a(byte[] bArr) {
        if (bArr == null) {
            d.b("NioSocketClient", "sendData failed, send data was null");
            return 103;
        }
        d.b("NioSocketClient", "send data length:" + bArr.length);
        if (bArr.length < this.g) {
            return b(bArr) ? 0 : 103;
        }
        d.b("NioSocketClient", "sendData failed, data length must less than " + this.g);
        return 6026;
    }

    @Override // cn.jiguang.am.a
    public final ByteBuffer a(int i) throws f {
        ByteBuffer byteBufferB;
        try {
            if (!a()) {
                throw new f(-991, "recv error,the connect is invalid");
            }
            int iB = b();
            if (iB > 0 && (byteBufferB = b(iB)) != null) {
                return byteBufferB;
            }
            int i2 = 0;
            int iB2 = 1048576;
            while (a() && this.c < iB2) {
                int iSelect = i > 0 ? this.d.select(i) : this.d.select();
                if (iSelect == 0) {
                    d.b("NioSocketClient", "readSelect:" + iSelect + ",time out:" + i);
                    if (i > 0) {
                        throw new f(-994, "recv time out");
                    }
                } else {
                    Iterator<SelectionKey> it = this.d.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey next = it.next();
                        SocketChannel socketChannel = (SocketChannel) next.channel();
                        if (next.isReadable()) {
                            int i3 = socketChannel.read(this.h);
                            if (i3 < 0) {
                                throw new f(-996, "read len < 0:" + i3);
                            }
                            this.h.flip();
                            int iLimit = this.h.limit();
                            if (this.a.remaining() < iLimit) {
                                throw new f(-996, "the total buf remaining less than readLen,remaining:" + this.a.remaining() + ",readLen:" + iLimit);
                            }
                            this.a.put(this.h);
                            this.c += iLimit;
                            this.h.compact();
                            if (this.c < this.f) {
                                d.b("NioSocketClient", "totalbuf can not parse head:" + this.c + ",peerNetData len:" + iLimit + ",read:" + i3);
                            } else {
                                iB2 = b();
                            }
                            i2 = iLimit;
                        } else {
                            next.isWritable();
                        }
                        it.remove();
                    }
                }
            }
            if (iB2 == 1048576) {
                throw new f(-997, "recv empty data or tcp has close");
            }
            d.b("NioSocketClient", "read len:" + i2 + ",recvTotalLen:" + this.c + ",shouldLen:" + iB2);
            ByteBuffer byteBufferB2 = b(iB2);
            if (byteBufferB2 != null) {
                return byteBufferB2;
            }
            throw new f(ResponseInfo.TimedOut, "parse error");
        } catch (Throwable th) {
            if (th instanceof SocketTimeoutException) {
                throw new f(-994, th.getMessage());
            }
            if (th instanceof f) {
                throw th;
            }
            throw new f(-997, th.getMessage());
        }
    }

    @Override // cn.jiguang.am.a, java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        d.b("NioSocketClient", "close this connect");
        super.close();
        if (this.d != null) {
            try {
                this.d.close();
            } catch (IOException unused) {
            }
        }
        k.a(this.b);
        this.b = null;
    }
}
