package cn.jiguang.k;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import cn.jiguang.o.e;

/* JADX INFO: loaded from: classes.dex */
final class b {
    HandlerThread a;
    Handler b;
    private int c = 300;
    private String d;

    b(String str, int i) {
        this.d = str;
        HandlerThread handlerThread = new HandlerThread("ping timer");
        this.a = handlerThread;
        handlerThread.start();
        this.b = new Handler(this.a.getLooper(), new c(this));
    }

    final void a(byte[] bArr, int i, int i2) {
        byte[] bArr2 = {bArr[0], bArr[1], bArr[2], 0};
        Thread threadCurrentThread = Thread.currentThread();
        for (int i3 = 0; i3 < 255; i3++) {
            byte b = (byte) i3;
            bArr2[3] = b;
            if (b != bArr[3]) {
                String strA = a.a(bArr2);
                if (!strA.equalsIgnoreCase(this.d)) {
                    this.b.removeCallbacksAndMessages(null);
                    Message messageObtainMessage = this.b.obtainMessage(1);
                    messageObtainMessage.obj = threadCurrentThread;
                    Bundle bundle = new Bundle();
                    bundle.putString("ip", strA);
                    messageObtainMessage.setData(bundle);
                    this.b.sendMessageDelayed(messageObtainMessage, this.c);
                    e.a(new String[]{"ping -c 1 -w 1 " + strA}, 0);
                }
            }
        }
        this.a.quit();
    }
}
