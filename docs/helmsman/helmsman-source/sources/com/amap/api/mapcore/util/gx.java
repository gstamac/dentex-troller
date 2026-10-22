package com.amap.api.mapcore.util;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: BinaryRequest.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class gx extends hc {
    protected Context a;
    protected ff b;

    public abstract byte[] e();

    public abstract byte[] f();

    protected String h() {
        return "2.1";
    }

    public boolean i() {
        return true;
    }

    protected boolean l() {
        return false;
    }

    public gx(Context context, ff ffVar) {
        if (context != null) {
            this.a = context.getApplicationContext();
        }
        this.b = ffVar;
    }

    @Override // com.amap.api.mapcore.util.hc
    public Map<String, String> b() {
        String strF = ew.f(this.a);
        String strA = ez.a();
        String strA2 = ez.a(this.a, strA, "key=" + strF);
        HashMap map = new HashMap();
        map.put("ts", strA);
        map.put("key", strF);
        map.put("scode", strA2);
        return map;
    }

    @Override // com.amap.api.mapcore.util.hc
    public final byte[] g() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(d());
            byteArrayOutputStream.write(j());
            byteArrayOutputStream.write(o());
            byteArrayOutputStream.write(p());
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th) {
                fk.a(th, "BinaryRequest", "getEntityBytes");
            }
            return byteArray;
        } catch (Throwable th2) {
            try {
                fk.a(th2, "BinaryRequest", "getEntityBytes");
                try {
                    return null;
                } catch (Throwable th3) {
                    return null;
                }
            } finally {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    fk.a(th4, "BinaryRequest", "getEntityBytes");
                }
            }
        }
    }

    private byte[] d() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(fg.a("PANDORA$"));
            byteArrayOutputStream.write(new byte[]{1});
            byteArrayOutputStream.write(new byte[]{0});
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th) {
                fk.a(th, "BinaryRequest", "getBinaryHead");
            }
            return byteArray;
        } catch (Throwable th2) {
            try {
                fk.a(th2, "BinaryRequest", "getBinaryHead");
                try {
                    return null;
                } catch (Throwable th3) {
                    return null;
                }
            } finally {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    fk.a(th4, "BinaryRequest", "getBinaryHead");
                }
            }
        }
    }

    public byte[] j() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(new byte[]{3});
            if (i()) {
                byte[] bArrA = ez.a(this.a, l());
                byteArrayOutputStream.write(a(bArrA));
                byteArrayOutputStream.write(bArrA);
            } else {
                byteArrayOutputStream.write(new byte[]{0, 0});
            }
            byte[] bArrA2 = fg.a(h());
            if (bArrA2 == null || bArrA2.length <= 0) {
                byteArrayOutputStream.write(new byte[]{0, 0});
            } else {
                byteArrayOutputStream.write(a(bArrA2));
                byteArrayOutputStream.write(bArrA2);
            }
            byte[] bArrA3 = fg.a(k());
            if (bArrA3 == null || bArrA3.length <= 0) {
                byteArrayOutputStream.write(new byte[]{0, 0});
            } else {
                byteArrayOutputStream.write(a(bArrA3));
                byteArrayOutputStream.write(bArrA3);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th) {
                fk.a(th, "BinaryRequest", "getRequestEncryptData");
            }
            return byteArray;
        } catch (Throwable th2) {
            try {
                fk.a(th2, "BinaryRequest", "getPublicData");
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th3) {
                    fk.a(th3, "BinaryRequest", "getRequestEncryptData");
                }
                return new byte[]{0};
            } catch (Throwable th4) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th5) {
                    fk.a(th5, "BinaryRequest", "getRequestEncryptData");
                }
                throw th4;
            }
        }
    }

    public String k() {
        return String.format("platform=Android&sdkversion=%s&product=%s", this.b.c(), this.b.a());
    }

    protected byte[] a(byte[] bArr) {
        int length = bArr.length;
        return new byte[]{(byte) (length / 256), (byte) (length % 256)};
    }

    private byte[] o() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArrE = e();
            if (bArrE != null && bArrE.length != 0) {
                byteArrayOutputStream.write(new byte[]{1});
                byteArrayOutputStream.write(a(bArrE));
                byteArrayOutputStream.write(bArrE);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    fk.a(th, "BinaryRequest", "getRequestRawData");
                }
                return byteArray;
            }
            byteArrayOutputStream.write(new byte[]{0});
            byte[] byteArray2 = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
                fk.a(th2, "BinaryRequest", "getRequestRawData");
            }
            return byteArray2;
        } catch (Throwable th3) {
            try {
                fk.a(th3, "BinaryRequest", "getRequestRawData");
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    fk.a(th4, "BinaryRequest", "getRequestRawData");
                }
                return new byte[]{0};
            } catch (Throwable th5) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th6) {
                    fk.a(th6, "BinaryRequest", "getRequestRawData");
                }
                throw th5;
            }
        }
    }

    private byte[] p() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArrF = f();
            if (bArrF != null && bArrF.length != 0) {
                byteArrayOutputStream.write(new byte[]{1});
                byte[] bArrA = ez.a(this.a, bArrF);
                byteArrayOutputStream.write(a(bArrA));
                byteArrayOutputStream.write(bArrA);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    fk.a(th, "BinaryRequest", "getRequestEncryptData");
                }
                return byteArray;
            }
            byteArrayOutputStream.write(new byte[]{0});
            byte[] byteArray2 = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th2) {
                fk.a(th2, "BinaryRequest", "getRequestEncryptData");
            }
            return byteArray2;
        } catch (Throwable th3) {
            try {
                fk.a(th3, "BinaryRequest", "getRequestEncryptData");
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    fk.a(th4, "BinaryRequest", "getRequestEncryptData");
                }
                return new byte[]{0};
            } catch (Throwable th5) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th6) {
                    fk.a(th6, "BinaryRequest", "getRequestEncryptData");
                }
                throw th5;
            }
        }
    }
}
