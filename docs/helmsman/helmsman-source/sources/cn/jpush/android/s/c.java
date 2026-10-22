package cn.jpush.android.s;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.api.JCoreManager;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.cache.Key;
import cn.jpush.android.cache.Sp;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushConstants;
import cn.jpush.android.service.PushReceiver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.jvm.internal.ByteCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private static c e;
    private List<JThirdPlatFormInterface> a = new ArrayList();
    private ExecutorService c;
    private boolean d;
    private static Map<String, Byte> b = new HashMap();
    private static final Object f = new Object();

    class a {
        String a;
        int b;
        String c;
        byte d;

        a() {
        }

        public String toString() {
            return "ThirdMessage{msgid='" + this.a + "', notiId=" + this.b + ", content='" + this.c + "', platform=" + ((int) this.d) + '}';
        }
    }

    static {
        b.put("cn.jpush.android.thirdpush.meizu.MeizuPushManager", (byte) 3);
        b.put("cn.jpush.android.thirdpush.xiaomi.XMPushManager", (byte) 1);
        b.put("cn.jpush.android.thirdpush.huawei.HWPushManager", (byte) 2);
        b.put("cn.jpush.android.thirdpush.fcm.FCMPushManager", (byte) 8);
        b.put("cn.jpush.android.thridpush.oppo.OPushManager", (byte) 4);
        b.put("cn.jpush.android.thirdpush.vivo.VivoPushManager", (byte) 5);
        b.put(cn.jpush.android.asus.b.class.getName(), (byte) 6);
    }

    private c() {
        this.c = null;
        this.c = Executors.newSingleThreadExecutor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public a a(Bundle bundle) {
        a aVar;
        if (bundle != null) {
            aVar = new a();
            aVar.c = bundle.getString("data");
            aVar.a = bundle.getString(JThirdPlatFormInterface.KEY_MSG_ID);
            aVar.b = bundle.getInt(JThirdPlatFormInterface.KEY_NOTI_ID, 0);
            aVar.d = bundle.getByte(JThirdPlatFormInterface.KEY_PLATFORM, (byte) -1).byteValue();
        } else {
            aVar = null;
        }
        Logger.d("ThirdPushManager", "parse third messgae:" + aVar);
        return aVar;
    }

    public static c a() {
        if (e == null) {
            synchronized (f) {
                if (e == null) {
                    e = new c();
                }
            }
        }
        return e;
    }

    private void a(Context context, JThirdPlatFormInterface jThirdPlatFormInterface) {
        if (jThirdPlatFormInterface == null || !jThirdPlatFormInterface.isNeedClearToken(context)) {
            return;
        }
        byte romType = jThirdPlatFormInterface.getRomType(context);
        Sp.set(context, Key.ThirdPush_RegUpload(romType).set(false));
        Sp.set(context, Key.ThirdPush_RegID(romType).set(null));
    }

    private void a(Context context, JThirdPlatFormInterface jThirdPlatFormInterface, String str) {
        Logger.d("ThirdPushManager", "sendBroadCastToUploadToken:" + str);
        String token = jThirdPlatFormInterface.getToken(context);
        if (!TextUtils.isEmpty(token)) {
            b(context, jThirdPlatFormInterface.getRomType(context), token);
            return;
        }
        try {
            Intent intent = new Intent(context, (Class<?>) PushReceiver.class);
            intent.setAction(str);
            Bundle bundle = new Bundle();
            bundle.putString("sdktype", JPushConstants.SDK_TYPE);
            bundle.putByte(JThirdPlatFormInterface.KEY_PLATFORM, jThirdPlatFormInterface.getRomType(context));
            intent.putExtras(bundle);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        } catch (Throwable th) {
            Logger.ww("ThirdPushManager", "send " + str + " failed:" + th);
        }
    }

    private void b(Context context, byte b2, String str) {
        Logger.ii("ThirdPushManager", "[uploadInService] regid:" + str + ",romtype:" + ((int) b2));
        a(context);
        for (JThirdPlatFormInterface jThirdPlatFormInterface : this.a) {
            if (jThirdPlatFormInterface.getRomType(context) == b2) {
                a(context, jThirdPlatFormInterface);
                if (b(context, (int) b2, str)) {
                    c(context, b2, str);
                }
            }
        }
    }

    private boolean b(Context context, int i, String str) {
        String str2;
        byte b2 = (byte) i;
        if (!((Boolean) Sp.get(context, Key.ThirdPush_RegUpload(b2))).booleanValue()) {
            str2 = "need upload -- last upload failed or never upload success";
        } else {
            if (TextUtils.equals((String) Sp.get(context, Key.ThirdPush_RegID(b2)), str)) {
                Logger.ii("ThirdPushManager", "need not upload regId");
                return false;
            }
            str2 = "need upload -- regId changed";
        }
        Logger.ii("ThirdPushManager", str2);
        return true;
    }

    private void c(Context context, byte b2, String str) {
        Logger.d("ThirdPushManager", "sendUpdateRegIDRequest, WhichPlatform:" + ((int) b2) + ",regID:" + str);
        Sp.set(context, Key.ThirdPush_RegUpload(b2).set(false));
        Sp.set(context, Key.ThirdPush_RegID(b2).set(str));
        cn.jpush.android.s.a.a().a(context, b2, str);
    }

    private void j(Context context) {
        Object objNewInstance;
        Logger.d("ThirdPushManager", "[loadThirdPush]");
        for (Map.Entry<String, Byte> entry : b.entrySet()) {
            try {
                Class<?> clsLoadClass = context.getClassLoader().loadClass(entry.getKey());
                if (clsLoadClass != null && (objNewInstance = clsLoadClass.newInstance()) != null && (objNewInstance instanceof JThirdPlatFormInterface)) {
                    ((JThirdPlatFormInterface) objNewInstance).init(context);
                    if (((JThirdPlatFormInterface) objNewInstance).isSupport(context)) {
                        this.a.add((JThirdPlatFormInterface) objNewInstance);
                    } else {
                        Byte value = entry.getValue();
                        Sp.set(context, Key.ThirdPush_RegID(value.byteValue()).set(null));
                        Sp.set(context, Key.ThirdPush_RegUpload(value.byteValue()).set(false));
                    }
                }
            } catch (Throwable th) {
                Byte value2 = entry.getValue();
                Sp.set(context, Key.ThirdPush_RegID(value2.byteValue()).set(null));
                Sp.set(context, Key.ThirdPush_RegUpload(value2.byteValue()).set(false));
                if ((th instanceof RuntimeException) && th.getMessage().contains("Please check") && JCoreManager.getDebugMode()) {
                    new Thread(new Runnable() { // from class: cn.jpush.android.s.c.1
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new RuntimeException(th);
                        }
                    }).start();
                }
                Logger.w("ThirdPushManager", "loadThirdPush error:" + th.getMessage());
            }
        }
        if (this.a.isEmpty()) {
            return;
        }
        cn.jpush.android.o.a.a(context);
    }

    public synchronized void a(Context context) {
        if (this.d) {
            return;
        }
        if (context == null) {
            return;
        }
        try {
            Class.forName("cn.jpush.android.dy.JPushThirdDyManager").getDeclaredMethod("loadByJPush", Context.class).invoke(null, context);
        } catch (Throwable unused) {
        }
        j(context);
        this.d = true;
    }

    public void a(Context context, byte b2, String str) {
        if (cn.jpush.android.l.a.a()) {
            if (context == null) {
                context = JPushConstants.mApplicationContext;
            }
            if (context == null) {
                Logger.e("ThirdPushManager", "context was null");
                return;
            }
            Logger.ii("ThirdPushManager", "uploadRegID regid:" + str);
            a(context);
            Bundle bundle = new Bundle();
            bundle.putString(JThirdPlatFormInterface.KEY_TOKEN, str);
            bundle.putByte(JThirdPlatFormInterface.KEY_PLATFORM, b2);
            JCoreHelper.runActionWithService(context, JPushConstants.SDK_TYPE, "third_push_upload_regid", bundle);
        }
    }

    public void a(Context context, int i, String str) {
        try {
            Logger.dd("ThirdPushManager", "[callBackTokenToUser] platform:" + i + ",token:" + str);
            Bundle bundle = new Bundle();
            bundle.putInt(JThirdPlatFormInterface.KEY_PLATFORM, i);
            bundle.putString(JThirdPlatFormInterface.KEY_TOKEN, str);
            cn.jpush.android.o.b.a(context, 10000, 0, "", bundle);
        } catch (Throwable th) {
            Logger.ww("ThirdPushManager", "callBackTokenToUser failed:" + th.getMessage());
        }
    }

    public void a(Context context, Bundle bundle) {
        if (context == null) {
            context = JPushConstants.mApplicationContext;
        }
        if (context == null) {
            Logger.e("ThirdPushManager", "context was null");
        } else if (bundle == null) {
            Logger.ww("ThirdPushManager", "bundle is null");
        } else {
            b(context, bundle.getByte(JThirdPlatFormInterface.KEY_PLATFORM, (byte) 0).byteValue(), bundle.getString(JThirdPlatFormInterface.KEY_TOKEN));
        }
    }

    public void a(final Context context, final String str, final Bundle bundle) {
        try {
            Logger.dd("ThirdPushManager", "doAction,action:" + str + ",bundle:" + bundle + ",enable:" + cn.jpush.android.l.a.a());
            if (cn.jpush.android.l.a.a()) {
                this.c.execute(new Runnable() { // from class: cn.jpush.android.s.c.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Bundle bundle2;
                        try {
                            if (!TextUtils.isEmpty(str)) {
                                if (str.equals(JThirdPlatFormInterface.ACTION_NOTIFICATION_ARRIVED)) {
                                    a aVarA = c.this.a(bundle);
                                    if (aVarA != null) {
                                        b.a(context, aVarA.c, aVarA.a, aVarA.b, aVarA.d, false);
                                    }
                                } else if (str.equals(JThirdPlatFormInterface.ACTION_NOTIFICATION_CLCKED)) {
                                    a aVarA2 = c.this.a(bundle);
                                    if (aVarA2 != null) {
                                        b.a(context, aVarA2.c, aVarA2.a, aVarA2.b, aVarA2.d, true);
                                    }
                                } else if (!str.equals(JThirdPlatFormInterface.ACTION_NOTIFICATION_SHOW) && str.equals(JThirdPlatFormInterface.ACTION_REGISTER_TOKEN) && (bundle2 = bundle) != null) {
                                    c.this.a(context, bundle.getByte(JThirdPlatFormInterface.KEY_PLATFORM, (byte) -1).byteValue(), bundle2.getString(JThirdPlatFormInterface.KEY_TOKEN));
                                }
                            }
                        } catch (Throwable th) {
                            Logger.ww("ThirdPushManager", "doAction failed internal:" + th.getMessage());
                        }
                    }
                });
            }
        } catch (Throwable th) {
            Logger.ww("ThirdPushManager", "doAction failed:" + th.getMessage());
        }
    }

    public void b(Context context) {
        if (cn.jpush.android.l.a.a()) {
            a(context);
            if (cn.jpush.android.cache.a.d(context.getApplicationContext())) {
                Logger.d("ThirdPushManager", "push has close");
                return;
            }
            Iterator<JThirdPlatFormInterface> it = this.a.iterator();
            while (it.hasNext()) {
                try {
                    it.next().register(context);
                } catch (Throwable th) {
                    Logger.ww("ThirdPushManager", "Third push register failed#", th);
                }
            }
        }
    }

    public void b(Context context, Bundle bundle) {
        if (!cn.jpush.android.l.a.a()) {
            Logger.dd("ThirdPushManager", "[refreshToken] third disabled");
            return;
        }
        a(context);
        byte bByteValue = bundle.getByte(JThirdPlatFormInterface.KEY_PLATFORM, (byte) -1).byteValue();
        if (bByteValue <= 0) {
            Logger.w("ThirdPushManager", "refreshToken romtype is <= 0");
            return;
        }
        Logger.dd("ThirdPushManager", "[refreshToken] romType: " + ((int) bByteValue));
        for (JThirdPlatFormInterface jThirdPlatFormInterface : this.a) {
            if (jThirdPlatFormInterface.getRomType(context) == bByteValue) {
                String token = jThirdPlatFormInterface.getToken(context);
                if (TextUtils.isEmpty(token)) {
                    jThirdPlatFormInterface.register(context);
                } else {
                    a(context, bByteValue, token);
                }
            }
        }
    }

    public void c(Context context) {
        if (cn.jpush.android.l.a.a()) {
            a(context);
            Iterator<JThirdPlatFormInterface> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().resumePush(context);
            }
        }
    }

    public void d(Context context) {
        if (cn.jpush.android.l.a.a()) {
            a(context);
            Iterator<JThirdPlatFormInterface> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().stopPush(context);
            }
        }
    }

    public byte e(Context context) {
        int i;
        byte b2 = 0;
        if (!cn.jpush.android.l.a.a()) {
            return (byte) 0;
        }
        a(context);
        for (JThirdPlatFormInterface jThirdPlatFormInterface : this.a) {
            byte romType = jThirdPlatFormInterface.getRomType(context);
            b2 = (byte) (b2 | romType);
            byte b3 = romType;
            String str = (String) Sp.get(context, Key.ThirdPush_RegID(b3));
            boolean zBooleanValue = ((Boolean) Sp.get(context, Key.ThirdPush_RegUpload(b3))).booleanValue();
            if (jThirdPlatFormInterface.getRomType(context) == 8) {
                b2 = (byte) (b2 | 8);
                if (zBooleanValue && !TextUtils.isEmpty(str)) {
                    i = b2 | 32;
                    b2 = (byte) i;
                }
            } else {
                if (jThirdPlatFormInterface.getRomType(context) == 2) {
                    b2 = (byte) (b2 | 64);
                }
                if (!zBooleanValue || TextUtils.isEmpty(str)) {
                    i = b2 | ByteCompanionObject.MIN_VALUE;
                    b2 = (byte) i;
                }
            }
        }
        Logger.d("ThirdPushManager", "getRomType,romType:" + ((int) b2));
        return b2;
    }

    public String f(Context context) {
        if (!cn.jpush.android.l.a.a()) {
            return null;
        }
        for (JThirdPlatFormInterface jThirdPlatFormInterface : this.a) {
            if (jThirdPlatFormInterface.getRomType(context) != 8) {
                return (String) Sp.get(context, Key.ThirdPush_RegID(jThirdPlatFormInterface.getRomType(context)));
            }
        }
        return null;
    }

    public void g(Context context) {
        try {
            List<JThirdPlatFormInterface> list = this.a;
            if (list != null && !list.isEmpty()) {
                Iterator<JThirdPlatFormInterface> it = this.a.iterator();
                while (it.hasNext()) {
                    c(context, it.next().getRomType(context), null);
                }
                return;
            }
            Map<String, Byte> map = b;
            if (map != null) {
                for (Byte b2 : map.values()) {
                    Sp.set(context, Key.ThirdPush_RegUpload(b2.byteValue()).set(false));
                    Sp.set(context, Key.ThirdPush_RegID(b2.byteValue()).set(null));
                }
            }
        } catch (Throwable unused) {
        }
    }

    public void h(Context context) {
        if (cn.jpush.android.l.a.a()) {
            if (context == null) {
                context = JPushConstants.mApplicationContext;
            }
            if (context == null) {
                Logger.e("ThirdPushManager", "context was null");
                return;
            }
            a(context);
            Logger.ii("ThirdPushManager", "uploadRegIdAfterLogin");
            Iterator<JThirdPlatFormInterface> it = this.a.iterator();
            while (it.hasNext()) {
                a(context, it.next(), JThirdPlatFormInterface.ACTION_PLUGIN_PALTFORM_REFRESSH_REGID);
            }
        }
    }

    public void i(Context context) {
        Logger.dd("ThirdPushManager", "reRegisterInPushReceiver");
        a(context);
        Iterator<JThirdPlatFormInterface> it = this.a.iterator();
        while (it.hasNext()) {
            a(context, it.next(), JThirdPlatFormInterface.ACTION_PLUGIN_PALTFORM_REFRESSH_REGID);
        }
    }
}
