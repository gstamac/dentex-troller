package cn.jiguang.o;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.l.h;
import java.nio.ByteBuffer;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static long a;

    static {
        cn.jiguang.af.b.a("JCommon");
        cn.jiguang.af.b.a("JLocation");
        cn.jiguang.af.b.a("JArp");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:103:0x0193  */
    /* JADX WARN: Code duplicated, block: B:139:0x0251 A[Catch: all -> 0x02a0, TryCatch #0 {all -> 0x02a0, blocks: (B:4:0x000b, B:6:0x0011, B:8:0x0017, B:10:0x001d, B:11:0x0026, B:41:0x0087, B:70:0x0130, B:72:0x0134, B:75:0x013b, B:79:0x0145, B:112:0x01a3, B:113:0x01ae, B:114:0x01b7, B:115:0x01bc, B:117:0x01c2, B:119:0x01fa, B:120:0x020c, B:134:0x0239, B:136:0x023d, B:137:0x0245, B:138:0x024d, B:139:0x0251, B:141:0x0257, B:142:0x025f, B:143:0x0268, B:82:0x014f, B:85:0x0159, B:88:0x0163, B:91:0x016b, B:94:0x0175, B:97:0x017f, B:100:0x0189, B:144:0x0285, B:146:0x0289, B:149:0x0290, B:152:0x029a, B:45:0x008f, B:46:0x00a8, B:48:0x00ad, B:49:0x00b6, B:51:0x00bf, B:53:0x00c6, B:56:0x00d9, B:58:0x00e2, B:60:0x00eb, B:62:0x00f8, B:63:0x00ff, B:65:0x0103, B:69:0x0111, B:13:0x002b, B:16:0x0036, B:19:0x0040, B:22:0x004a, B:25:0x0054, B:28:0x005e, B:31:0x0068, B:34:0x0072, B:37:0x007c), top: B:158:0x0009 }] */
    /* JADX WARN: Code duplicated, block: B:141:0x0257 A[Catch: all -> 0x02a0, TryCatch #0 {all -> 0x02a0, blocks: (B:4:0x000b, B:6:0x0011, B:8:0x0017, B:10:0x001d, B:11:0x0026, B:41:0x0087, B:70:0x0130, B:72:0x0134, B:75:0x013b, B:79:0x0145, B:112:0x01a3, B:113:0x01ae, B:114:0x01b7, B:115:0x01bc, B:117:0x01c2, B:119:0x01fa, B:120:0x020c, B:134:0x0239, B:136:0x023d, B:137:0x0245, B:138:0x024d, B:139:0x0251, B:141:0x0257, B:142:0x025f, B:143:0x0268, B:82:0x014f, B:85:0x0159, B:88:0x0163, B:91:0x016b, B:94:0x0175, B:97:0x017f, B:100:0x0189, B:144:0x0285, B:146:0x0289, B:149:0x0290, B:152:0x029a, B:45:0x008f, B:46:0x00a8, B:48:0x00ad, B:49:0x00b6, B:51:0x00bf, B:53:0x00c6, B:56:0x00d9, B:58:0x00e2, B:60:0x00eb, B:62:0x00f8, B:63:0x00ff, B:65:0x0103, B:69:0x0111, B:13:0x002b, B:16:0x0036, B:19:0x0040, B:22:0x004a, B:25:0x0054, B:28:0x005e, B:31:0x0068, B:34:0x0072, B:37:0x007c), top: B:158:0x0009 }] */
    /* JADX WARN: Code duplicated, block: B:40:0x0086  */
    public static Object a(Context context, String str, Object obj) {
        byte b;
        try {
            if (context == null) {
                cn.jiguang.w.a.d("JCommonActionHelper", "context is null,give up continue");
                return null;
            }
            if (TextUtils.isEmpty(str)) {
                cn.jiguang.w.a.d("JCommonActionHelper", "action is null,give up continue");
                return null;
            }
            switch (str) {
                case "periodtask":
                    cn.jiguang.i.b.a().c(context);
                    if (cn.jiguang.d.a.i(context)) {
                        cn.jiguang.u.a.a().c(context);
                    }
                    cn.jiguang.x.a.a(context, ((obj instanceof Bundle) && ((Bundle) obj).getBoolean("login")) ? 2 : 0);
                    cn.jiguang.m.b.a().b(context);
                    cn.jiguang.m.c.a().b(context);
                    cn.jiguang.z.a.a().b(context);
                    cn.jiguang.z.a.a().b(context, (JSONObject) null);
                    break;
                case "get_loc_info":
                    return cn.jiguang.u.a.a().e(context);
                case "getwakeenable":
                    return cn.jiguang.z.a.a().e(context);
                case "filter_pkg_list":
                    cn.jiguang.z.a.a();
                    if (obj instanceof List) {
                        return cn.jiguang.ab.c.a(cn.jiguang.ab.b.a(c.a(context, "bwc.catch")), (List<String>) obj);
                    }
                    return obj;
                case "deviceinfo":
                    return cn.jiguang.m.b.a().e(context);
                case "on_register":
                    cn.jiguang.z.a.a().g(context);
                    break;
                case "get_all_ids":
                    return cn.jiguang.q.a.a(context);
                case "service_create":
                    cn.jiguang.z.a.a().f(context);
                    cn.jiguang.an.b.a().b(3000, 3000L, new b());
                    break;
                case "get_imei":
                    return "";
            }
            Bundle bundle = obj instanceof Bundle ? (Bundle) obj : null;
            if (bundle != null) {
                switch (str) {
                    case "notification_state":
                        b = 7;
                        break;
                    case "init_local_ctrl":
                        b = 0;
                        break;
                    case "lbsenable":
                        b = 1;
                        break;
                    case "set_ctrl_url":
                        b = 3;
                        break;
                    case "cmd":
                        b = 4;
                        break;
                    case "waked":
                        b = 5;
                        break;
                    case "lbsforenry":
                        b = 2;
                        break;
                    case "set_sdktype_info":
                        b = 6;
                        break;
                    default:
                        b = -1;
                        break;
                }
                if (b == 1) {
                    boolean z = bundle.getBoolean("enable");
                    cn.jiguang.w.a.a("JCommonActionHelper", "[setLBSEnable] " + z);
                    cn.jiguang.l.e.b(context, "JLocation", z);
                } else if (b == 3) {
                    cn.jiguang.ab.b.a = bundle.getString("test_wake_controll_url");
                } else if (b == 4) {
                    JSONObject jSONObjectA = a(bundle);
                    if (jSONObjectA != null) {
                        int iOptInt = jSONObjectA.optInt("cmd");
                        long jA = d.a();
                        String strA = cn.jiguang.n.a.a(context, iOptInt);
                        byte[] bArrA = d.a(a, strA);
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("cmd", 25);
                        bundle2.putInt("ver", 1);
                        bundle2.putLong("rid", jA);
                        bundle2.putLong("timeout", 0L);
                        bundle2.putByteArray("body", bArrA);
                        if (!TextUtils.isEmpty(strA)) {
                            cn.jiguang.w.a.a("JCommonActionHelper", "tcp report deviceInfo:" + strA);
                        }
                        JCoreManager.onEvent(context, "JCOMMON", 16, null, bundle2, new Object[0]);
                        if (iOptInt != 4) {
                            if (iOptInt == 5) {
                                if (cn.jiguang.d.a.i(context)) {
                                    cn.jiguang.u.a.a().a(context, jSONObjectA);
                                }
                            } else if (iOptInt != 9 && iOptInt != 44) {
                                if (iOptInt != 45) {
                                    if (iOptInt != 54) {
                                        if (iOptInt != 55) {
                                            switch (iOptInt) {
                                                case 50:
                                                    cn.jiguang.z.a.a().b(context, jSONObjectA);
                                                    break;
                                                case 52:
                                                    cn.jiguang.i.b.a().a(context, jSONObjectA);
                                                    break;
                                            }
                                        } else {
                                            h.a(context, jSONObjectA);
                                        }
                                    }
                                } else if (cn.jiguang.d.a.i(context)) {
                                    cn.jiguang.u.a.a().a(context, jSONObjectA);
                                }
                            }
                        }
                    }
                } else if (b == 5) {
                    cn.jiguang.z.a.b(context, bundle);
                } else if (b == 6) {
                    cn.jiguang.y.a.a().a(context, bundle);
                } else if (b == 7) {
                    cn.jiguang.x.a.a(context, bundle.getInt("scence"));
                }
            }
            if ((obj instanceof Intent ? (Intent) obj : null) != null && str.hashCode() == 1563192504) {
                str.equals("get_receiver");
                return null;
            }
            return null;
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JCommonActionHelper", "onEvent throwable:" + th.getMessage());
            return null;
        }
    }

    private static JSONObject a(Bundle bundle) {
        try {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bundle.getByteArray("RESPONSE_BODY"));
            a = byteBufferWrap.getLong();
            byte[] bArr = new byte[byteBufferWrap.getShort()];
            byteBufferWrap.get(bArr);
            String str = new String(bArr, "UTF-8");
            cn.jiguang.w.a.a("JCommonActionHelper", "parseBundle2Json content: ".concat(str));
            return new JSONObject(str);
        } catch (Exception e) {
            cn.jiguang.w.a.d("JCommonActionHelper", "parseBundle2Json exception:" + e.getMessage());
            return null;
        }
    }
}
