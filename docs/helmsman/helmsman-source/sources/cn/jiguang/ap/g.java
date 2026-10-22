package cn.jiguang.ap;

import android.content.Context;
import android.os.Bundle;
import cn.jiguang.api.JAction;
import cn.jiguang.api.JActionExtra;
import cn.jiguang.api.JCoreInterface;
import cn.jiguang.api.JDispatchAction;
import cn.jiguang.api.SdkType;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class g extends JDispatchAction {
    private static JAction a(String str) {
        a.a();
        return a.a(str);
    }

    private static JActionExtra b(String str) {
        a.a();
        return a.b(str);
    }

    @Override // cn.jiguang.api.JDispatchAction
    public Object beforLogin(Context context, String str, int i, String str2) {
        JActionExtra jActionExtraB = b(str);
        if (jActionExtraB != null) {
            return jActionExtraB.beforLogin(context, i, str2);
        }
        return null;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public Object beforRegister(Context context, String str, int i, String str2) {
        JActionExtra jActionExtraB = b(str);
        if (jActionExtraB != null) {
            return jActionExtraB.beforRegister(context, i, str2);
        }
        return null;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public boolean checkAction(String str, int i) {
        JActionExtra jActionExtraB = b(str);
        if (jActionExtraB != null) {
            return jActionExtraB.checkAction(i);
        }
        return true;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public void dispatchMessage(Context context, String str, int i, int i2, long j, long j2, ByteBuffer byteBuffer) {
        JAction jActionA = a(str);
        if (jActionA != null) {
            d.b("SupportDispatchAction", "dispatchMessage ,command:" + i + ",ver:" + i2 + ",rid:" + j + ",reuqestId:" + j2);
            if (str.equals(SdkType.JMESSAGE.name())) {
                jActionA.dispatchMessage(context, 0L, i, new c(false, byteBuffer.limit() + 20, i2, i, j, JCoreInterface.getSid(), JCoreInterface.getUid()), byteBuffer);
            } else {
                jActionA.dispatchMessage(context, 0L, i, new c(false, i2, i, j2), byteBuffer);
            }
        }
    }

    @Override // cn.jiguang.api.JDispatchAction
    public void dispatchTimeOutMessage(Context context, String str, long j, int i) {
        JAction jActionA = a(str);
        if (jActionA != null) {
            jActionA.dispatchTimeOutMessage(context, 0L, j, i);
        }
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getLogPriority(String str) {
        if (str.equals(SdkType.JPUSH.name())) {
            return (short) 1;
        }
        if (str.equals(SdkType.JANALYTICS.name())) {
            return (short) 2;
        }
        if (str.equals(SdkType.JSHARE.name())) {
            return (short) 4;
        }
        if (str.equals(SdkType.JSSP.name())) {
            return (short) 5;
        }
        return str.equals(SdkType.JVERIFICATION.name()) ? (short) 3 : (short) 0;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getLoginFlag(String str) {
        if (str.equals(SdkType.JPUSH.name())) {
            return (short) 1;
        }
        if (str.equals(SdkType.JANALYTICS.name())) {
            return (short) 4;
        }
        if (str.equals(SdkType.JSHARE.name())) {
            return (short) 8;
        }
        if (str.equals(SdkType.JSSP.name())) {
            return (short) 128;
        }
        if (str.equals(SdkType.JMESSAGE.name())) {
            return (short) 32;
        }
        return str.equals(SdkType.JVERIFICATION.name()) ? (short) 256 : (short) 0;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getRegFlag(String str) {
        if (str.equals(SdkType.JPUSH.name())) {
            return (short) 1;
        }
        if (str.equals(SdkType.JANALYTICS.name())) {
            return (short) 4;
        }
        if (str.equals(SdkType.JSHARE.name())) {
            return (short) 8;
        }
        if (str.equals(SdkType.JSSP.name())) {
            return (short) 128;
        }
        if (str.equals(SdkType.JMESSAGE.name())) {
            return (short) 32;
        }
        return str.equals(SdkType.JVERIFICATION.name()) ? (short) 256 : (short) 0;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getRegPriority(String str) {
        if (str.equals(SdkType.JPUSH.name())) {
            return (short) 0;
        }
        if (str.equals(SdkType.JANALYTICS.name())) {
            return (short) 1;
        }
        if (str.equals(SdkType.JSHARE.name())) {
            return (short) 2;
        }
        if (str.equals(SdkType.JSSP.name())) {
            return (short) 4;
        }
        return str.equals(SdkType.JVERIFICATION.name()) ? (short) 5 : (short) 3;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public String getReportVersionKey(String str) {
        if (str.equals(SdkType.JPUSH.name())) {
            return "sdk_ver";
        }
        if (str.equals(SdkType.JANALYTICS.name())) {
            return "statistics_sdk_ver";
        }
        if (str.equals(SdkType.JSHARE.name())) {
            return "share_sdk_ver";
        }
        if (str.equals(SdkType.JSSP.name())) {
            return "ssp_sdk_ver";
        }
        if (str.equals(SdkType.JMESSAGE.name())) {
            return "im_sdk_ver";
        }
        if (str.equals(SdkType.JVERIFICATION.name())) {
            return "verification_sdk_ver";
        }
        return null;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public String getSdkVersion(String str) {
        JAction jActionA = a(str);
        return jActionA != null ? jActionA.getSdkVersion() : "";
    }

    @Override // cn.jiguang.api.JDispatchAction
    public short getUserCtrlProperty(String str) {
        if (str.equals(SdkType.JPUSH.name())) {
            return (short) 1;
        }
        if (str.equals(SdkType.JMESSAGE.name())) {
            return (short) 2;
        }
        if (str.equals(SdkType.JANALYTICS.name())) {
            return (short) 4;
        }
        if (str.equals(SdkType.JSHARE.name())) {
            return (short) 5;
        }
        if (str.equals(SdkType.JSSP.name())) {
            return (short) 9;
        }
        return str.equals(SdkType.JVERIFICATION.name()) ? (short) 10 : (short) 6;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public void handleMessage(Context context, String str, Object obj) {
        d.b("SupportDispatchAction", "handleMessage,sdkType:" + str);
        JAction jActionA = a(str);
        if (jActionA != null) {
            jActionA.handleMessage(context, 0L, obj);
        }
    }

    @Override // cn.jiguang.api.JDispatchAction
    public boolean isSupportedCMD(String str, int i) {
        JAction jActionA = a(str);
        if (jActionA != null) {
            return jActionA.isSupportedCMD(i);
        }
        return false;
    }

    @Override // cn.jiguang.api.JDispatchAction
    public void onActionRun(Context context, String str, String str2, Bundle bundle) {
        JAction jActionA = a(str);
        if (jActionA != null) {
            jActionA.onActionRun(context, 0L, bundle, null);
        }
    }

    @Override // cn.jiguang.api.JDispatchAction
    public void onEvent(Context context, String str, int i, int i2, String str2) {
        JAction jActionA = a(str);
        if (jActionA != null) {
            jActionA.onEvent(context, 0L, i);
        }
    }

    @Override // cn.jiguang.api.JDispatchAction
    public Object onSendData(Context context, String str, long j, int i, int i2) {
        JActionExtra jActionExtraB = b(str);
        if (jActionExtraB != null) {
            return jActionExtraB.onSendData(context, 0L, j, i, i2);
        }
        return null;
    }
}
