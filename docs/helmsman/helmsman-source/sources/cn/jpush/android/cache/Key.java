package cn.jpush.android.cache;

/* JADX INFO: loaded from: classes.dex */
public class Key<T> {
    String a;
    String b;
    T c;
    boolean d;

    private Key(String str, String str2, T t) {
        this.a = str;
        this.b = str2;
        if (t == null) {
            throw new IllegalArgumentException("default value can not be null");
        }
        this.c = t;
    }

    public static Key<Integer> BadgeCurNum() {
        return new Key("cn.jpush.config", "badgeCurNum", 0).a();
    }

    public static Key<String> FCM_ClearFlag() {
        return new Key("cn.jpush.config", "fcm_clear_flag", "").a();
    }

    public static Key<String> NotiCancel() {
        return new Key("cn.jpush.config", "NotiCancel", "").a();
    }

    public static Key<String> NotiSchedule() {
        return new Key("cn.jpush.config", "NotiSchedule", "").a();
    }

    public static Key<Integer> PushVerCode() {
        return new Key("cn.jpush.config", "versionCode", 0).a();
    }

    public static Key<String> ThirdPush_ClearFlag() {
        return new Key("cn.jpush.config", "third_push_clear_flag", "").a();
    }

    public static Key<String> ThirdPush_RegID(byte b) {
        return new Key("cn.jpush.config", "pluginPlatformRegid_v2" + ((int) b), "").a();
    }

    public static Key<Boolean> ThirdPush_RegUpload(byte b) {
        return new Key("cn.jpush.config", "pluginPlatformRegidupload" + ((int) b), false).a();
    }

    public static Key<Boolean> UPSRegister() {
        return new Key<>("cn.jpush.config", "upsRegister", true);
    }

    private Key<T> a() {
        this.d = true;
        return this;
    }

    public Key<T> file(String str) {
        this.a = str;
        return this;
    }

    public Key<T> name(String str) {
        this.b = str;
        return this;
    }

    public Key<T> set(T t) {
        this.c = t;
        return this;
    }
}
