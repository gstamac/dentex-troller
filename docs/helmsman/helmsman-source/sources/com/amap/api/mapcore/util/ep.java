package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMapException;
import com.qiniu.android.common.Constants;
import java.util.Map;

/* JADX INFO: compiled from: BasicHandler.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class ep<T, V> extends hc {
    protected T a;
    protected Context d;
    protected String e;
    protected int b = 1;
    protected String c = "";
    private int i = 1;

    protected abstract V a(String str) throws eo;

    @Override // com.amap.api.mapcore.util.hc
    public Map<String, String> a() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.hc
    public Map<String, String> b() {
        return null;
    }

    protected V e() {
        return null;
    }

    public ep(Context context, T t) {
        a(context, t);
    }

    private void a(Context context, T t) {
        this.d = context;
        this.a = t;
        this.b = 1;
        b(30000);
        a(30000);
    }

    protected V a(byte[] bArr) throws eo {
        String str;
        try {
            str = new String(bArr, Constants.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
        }
        if (str == null || str.equals("")) {
            return null;
        }
        er.a(str, this.e);
        return a(str);
    }

    public V d() throws eo {
        if (this.a != null) {
            return f();
        }
        return null;
    }

    private V f() throws eo {
        V vB = null;
        int i = 0;
        while (i < this.b) {
            try {
                hb hbVarA = hb.a(false);
                a(fd.a(this.d));
                vB = b(a(1, hbVarA, this));
                i = this.b;
            } catch (eo e) {
                i++;
                if (i >= this.b) {
                    throw new eo(e.a());
                }
            } catch (ev e2) {
                i++;
                if (i < this.b) {
                    try {
                        Thread.sleep(this.i * 1000);
                    } catch (InterruptedException unused) {
                        if (AMapException.ERROR_CONNECTION.equals(e2.getMessage()) || AMapException.ERROR_SOCKET.equals(e2.getMessage()) || AMapException.ERROR_UNKNOW_SERVICE.equals(e2.getMessage())) {
                            throw new eo("http或socket连接失败 - ConnectionException");
                        }
                        throw new eo(e2.a());
                    }
                } else {
                    e();
                    if (AMapException.ERROR_CONNECTION.equals(e2.getMessage()) || AMapException.ERROR_SOCKET.equals(e2.getMessage()) || AMapException.ERROR_UNKNOWN.equals(e2.a()) || AMapException.ERROR_UNKNOW_SERVICE.equals(e2.getMessage())) {
                        throw new eo("http或socket连接失败 - ConnectionException");
                    }
                    throw new eo(e2.a());
                }
            } catch (Throwable unused2) {
                throw new eo("未知错误");
            }
        }
        return vB;
    }

    protected byte[] a(int i, hb hbVar, hc hcVar) throws ev {
        if (i == 1) {
            return hbVar.b(hcVar);
        }
        if (i == 2) {
            return hbVar.a(hcVar);
        }
        return null;
    }

    private V b(byte[] bArr) throws eo {
        return a(bArr);
    }
}
