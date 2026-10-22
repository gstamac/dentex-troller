package cn.jiguang.c;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface;
import cn.jiguang.ad.d;
import cn.jiguang.aj.e;
import cn.jiguang.aj.i;
import cn.jiguang.aj.o;
import cn.jiguang.aj.q;
import cn.jiguang.api.JDispatchAction;
import cn.jiguang.internal.JConstants;
import com.facebook.appevents.AppEventsConstants;
import java.nio.ByteBuffer;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a extends JDispatchAction {
    @Override // cn.jiguang.api.JDispatchAction
    public void dispatchMessage(Context context, String str, int i, int i2, long j, long j2, ByteBuffer byteBuffer) {
        try {
            if (i == 19) {
                i.a().a(context, "tcp_a18", null);
                return;
            }
            if (i == 30 || i == 32) {
                cn.jiguang.e.a.a().a(0, i);
                return;
            }
            if (i == 25) {
                Bundle bundle = new Bundle();
                bundle.putByteArray("RESPONSE_BODY", byteBuffer.array());
                e.a(context, "cmd", bundle);
            } else {
                if (i != 26) {
                    return;
                }
                short s = byteBuffer.getShort();
                if (s == 0) {
                    q.a().a(context, j2);
                } else {
                    q.a().a(j2, s);
                }
            }
        } catch (Throwable th) {
            d.f("JCoreDispatchAction", "dispatchMessage failed:" + th.getMessage());
        }
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getLogPriority(String str) {
        return (short) 0;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getLoginFlag(String str) {
        return (short) 0;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getRegFlag(String str) {
        return (short) 0;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getRegPriority(String str) {
        return (short) 3;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public String getReportVersionKey(String str) {
        return "core_sdk_ver";
    }

    @Override // cn.jiguang.api.JDispatchAction
    public String getSdkVersion(String str) {
        return cn.jiguang.a.a.b;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getUserCtrlProperty(String str) {
        return (short) 6;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public void handleMessage(Context context, String str, Object obj) {
    }

    @Override // cn.jiguang.api.JDispatchAction
    public boolean isSupportedCMD(String str, int i) {
        return i == 0 || i == 1 || i == 19 || i == 25 || i == 26 || i == 30 || i == 32;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public void onActionRun(Context context, String str, String str2, Bundle bundle) {
        if (bundle != null) {
            try {
                if (TextUtils.isEmpty(str2)) {
                    return;
                }
                if (str2.equals("asm")) {
                    cn.jiguang.e.a.a().a(context, bundle);
                    return;
                }
                if (!str2.equals("asmr")) {
                    if (str2.equals("lbsenable")) {
                        e.a(context, str2, bundle);
                        if (bundle != null) {
                            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.z().a(Boolean.valueOf(bundle.getBoolean("enable")))});
                            return;
                        }
                        return;
                    }
                    if (!str2.equals("lbsforenry")) {
                        if (str2.equals("notification_state")) {
                            e.a(context, str2, bundle);
                            return;
                        } else {
                            if (str2.equals("old_cmd")) {
                                e.a(context, str2, bundle);
                                return;
                            }
                            return;
                        }
                    }
                    try {
                        long j = bundle.getLong("forenry");
                        d.b("JCoreDispatchAction", "setLbsPermissionDialogShieldDelay=" + j);
                        if (((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.x())).longValue() <= 0) {
                            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.x().a(Long.valueOf(System.currentTimeMillis()))});
                        }
                        cn.jiguang.g.a[] aVarArr = new cn.jiguang.g.a[1];
                        aVarArr[0] = cn.jiguang.g.a.y().a(j > 0 ? Long.valueOf(j) : null);
                        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) aVarArr);
                        return;
                    } catch (Throwable th) {
                        d.h("JCoreDispatchAction", "onActionRun failed:" + th);
                        return;
                    }
                }
                cn.jiguang.e.a aVarA = cn.jiguang.e.a.a();
                try {
                    String string = bundle.getString("data");
                    if (!TextUtils.isEmpty(string)) {
                        JSONObject jSONObject = new JSONObject(string);
                        String strOptString = jSONObject.optString("mtmmi");
                        String strOptString2 = jSONObject.optString("ktmfp");
                        String strOptString3 = jSONObject.optString("ktma");
                        String strOptString4 = jSONObject.optString("ktmr");
                        String strOptString5 = jSONObject.optString("ktmu");
                        String strOptString6 = jSONObject.optString("asmrc", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                        d.b("ShareProcessManager", "msg response,msgId:" + strOptString + ",fromPkg:" + strOptString2 + ",appKey:" + strOptString3 + ",rid:" + strOptString4 + ",uid:" + strOptString5 + ",responseCode:" + strOptString6);
                        if (strOptString6.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO) && !TextUtils.isEmpty(strOptString5) && !TextUtils.isEmpty(strOptString) && !TextUtils.isEmpty(strOptString3) && !TextUtils.isEmpty(strOptString4)) {
                            d.b("ShareProcessManager", "response success,will send msg response to server");
                            cn.jiguang.af.b.b(context, JConstants.SDK_TYPE, 4, 2, o.b(), Long.parseLong(strOptString5), cn.jiguang.al.b.a(0, (byte) 0, Long.parseLong(strOptString), strOptString3));
                        } else if (strOptString6.equals("1") && !TextUtils.isEmpty(strOptString5)) {
                            aVarA.a(context, Long.parseLong(strOptString5));
                        } else if (!strOptString6.equals(ExifInterface.GPS_MEASUREMENT_3D) || TextUtils.isEmpty(strOptString5)) {
                            d.b("ShareProcessManager", "invalid msg response");
                        } else {
                            aVarA.a(context, Long.parseLong(strOptString5));
                            cn.jiguang.e.a.a(context, strOptString2);
                        }
                    }
                    return;
                } catch (Throwable th2) {
                    d.f("ShareProcessManager", "doMsgResponse failed:" + th2.getMessage());
                    return;
                }
                d.f("JCoreDispatchAction", "onActionRun failed:" + th.getMessage());
            } catch (Throwable th3) {
                d.f("JCoreDispatchAction", "onActionRun failed:" + th3.getMessage());
            }
        }
    }
}
