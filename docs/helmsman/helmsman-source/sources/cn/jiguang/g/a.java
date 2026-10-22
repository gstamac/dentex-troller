package cn.jiguang.g;

import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
public final class a<T> {
    public static String a = "cn.jiguang.sdk.share.profile";
    public static String b = "cn.jpush.preferences.v2";
    String c;
    String d;
    T e;
    boolean f;
    boolean g;

    public a(String str, String str2, T t) {
        this.c = str;
        this.d = str2;
        if (t == null) {
            throw new IllegalArgumentException("default value can not be null");
        }
        this.e = t;
    }

    public static a<Boolean> A() {
        return new a<>("cn.jpush.android.user.profile", "is_tcp_close", false);
    }

    public static a<Integer> B() {
        a<Integer> aVar = new a<>("cn.jpush.android.user.profile", "jpush_register_code", -1);
        aVar.f = true;
        return aVar;
    }

    public static a<String> C() {
        return new a<>(b, "device_config_appkey", "");
    }

    public static a<String> D() {
        return new a<>(b, "i_new", "");
    }

    public static a<String> E() {
        return new a<>(b, "push_udid", "");
    }

    public static a<String> F() {
        return new a<>(b, "last_connection_type", "");
    }

    public static a<String> G() {
        a<String> aVar = new a<>(b, "sis_report_history", "");
        aVar.g = true;
        return aVar;
    }

    public static a<Long> H() {
        return new a<>("cn.jpush.preferences.v2.rid", "next_rid", -1L);
    }

    public static a<Integer> I() {
        return new a<>("cn.jpush.preferences.v2.rid", "seq_id", -1);
    }

    public static a<String> J() {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "ips_in_last_good_sis", "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> K() {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "ssl_ips_in_last_good_sis", "");
        aVar.g = true;
        return aVar;
    }

    public static a<Boolean> L() {
        return new a<>("cn.jiguang.sdk.address", "udp_data_report", false);
    }

    public static a<Long> M() {
        return new a<>("cn.jiguang.sdk.address", "sis_last_update", 0L);
    }

    public static a<Long> N() {
        return new a<>("cn.jiguang.sdk.address", "last_sis_report_time", 0L);
    }

    public static a<String> O() {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "default_sis_ips", "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> P() {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "default_conn", "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> Q() {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "default_conn_srv", "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> R() {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "tcp_report", "");
        aVar.f = true;
        aVar.g = true;
        return aVar;
    }

    public static a<String> S() {
        return new a<>("PrefsFile", "key", "");
    }

    public static a<String> a() {
        a<String> aVar = new a<>("cn.jpush.android.user.profile", "devcie_id_generated", "");
        aVar.f = true;
        return aVar;
    }

    public static a<String> a(String str) {
        return new a<>("cn.jpush.android.user.profile", "sdk_version_" + str, "");
    }

    public static a<String> a(boolean z) {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "default_https_report", "");
        aVar.f = true;
        aVar.g = true;
        return aVar;
    }

    public static a<Boolean> b() {
        return new a<>("cn.jpush.android.user.profile", "upload_crash", true);
    }

    public static a<String> b(String str) {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "dns_" + str, "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> b(boolean z) {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "last_good_sis_address".concat(z ? "_V4" : "_V6"), "");
        aVar.g = true;
        return aVar;
    }

    public static a<Long> c() {
        a<Long> aVar = new a<>("cn.jiguang.sdk.user.profile", "key_uid", 0L);
        aVar.f = true;
        return aVar;
    }

    public static a<Long> c(String str) {
        return new a<>("cn.jiguang.sdk.address", "dns_last_update_" + str, 0L);
    }

    public static a<String> c(boolean z) {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "last_good_conn".concat(z ? "_V4" : "_V6"), "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> d() {
        a<String> aVar = new a<>("cn.jiguang.sdk.user.profile", "key_rid", "");
        aVar.f = true;
        return aVar;
    }

    public static a<String> d(String str) {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", "srv_" + str, "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> d(boolean z) {
        a<String> aVar = new a<>("cn.jiguang.sdk.address", z ? "default_https_report" : "default_http_report", "");
        aVar.f = true;
        aVar.g = true;
        return aVar;
    }

    public static a<String> e() {
        a<String> aVar = new a<>("cn.jiguang.sdk.user.profile", "key_pwd", "");
        aVar.f = true;
        return aVar;
    }

    public static a<Long> e(String str) {
        return new a<>("cn.jiguang.sdk.address", "srv_last_update_" + str, 0L);
    }

    public static a<Integer> f() {
        a<Integer> aVar = new a<>("cn.jiguang.sdk.user.profile", "idc", -1);
        aVar.f = true;
        return aVar;
    }

    public static a<String> f(String str) {
        a<String> aVar = new a<>("IpInfos", str, "");
        aVar.g = true;
        return aVar;
    }

    public static a<Long> g() {
        return new a<>("cn.jiguang.sdk.user.profile", "login_local_time", -1L);
    }

    public static a<Integer> g(String str) {
        return new a<>("netinfo", str, 0);
    }

    public static a<Long> h() {
        return new a<>("cn.jiguang.sdk.user.profile", "login_server_time", -1L);
    }

    public static a<String> i() {
        a<String> aVar = new a<>(a, "key_share_process_uuid", "");
        aVar.f = true;
        return aVar;
    }

    public static a<Long> j() {
        a<Long> aVar = new a<>(a, "key_share_process_uuid_creattime", -1L);
        aVar.f = true;
        return aVar;
    }

    public static a<Integer> k() {
        a<Integer> aVar = new a<>(a, "sp_state", -1);
        aVar.f = true;
        return aVar;
    }

    public static a<String> l() {
        return new a<>("cn.jiguang.sdk.user.set.profile", "option_channel", "");
    }

    public static a<String> m() {
        return new a<>("cn.jiguang.sdk.user.set.profile", "analytics_account_id", "");
    }

    public static a<Boolean> n() {
        return new a<>("cn.jiguang.sdk.user.set.profile", "is_ups_register", true);
    }

    public static a<Long> o() {
        return new a<>("Push_Page_Config", "css", 0L);
    }

    public static a<Long> p() {
        return new a<>("Push_Page_Config", "cse", 0L);
    }

    public static a<Long> q() {
        return new a<>("Push_Page_Config", "last_pause", -1L);
    }

    public static a<String> r() {
        return new a<>("Push_Page_Config", "session_id", "");
    }

    public static a<String> s() {
        a<String> aVar = new a<>("cn.jiguang.sdk.report", "report_urls", "");
        aVar.g = true;
        return aVar;
    }

    public static a<String> t() {
        a<String> aVar = new a<>("cn.jiguang.sdk.report", "report_sis_urls", "");
        aVar.g = true;
        return aVar;
    }

    public static a<Long> u() {
        return new a<>("cn.jiguang.sdk.report", "last_update_report_urls", 0L);
    }

    public static a<Long> v() {
        return new a<>("cn.jiguang.sdk.report", "report_urls_ttl_millis", Long.valueOf(JConstants.HOUR));
    }

    public static a<String> w() {
        a<String> aVar = new a<>(b, "sdk_version", "");
        aVar.f = true;
        return aVar;
    }

    public static a<Long> x() {
        return new a<>(b, "first_init", 0L);
    }

    public static a<Long> y() {
        return new a<>(b, "lbs_delay", 0L);
    }

    public static a<Boolean> z() {
        return new a<>(b, "jcore_lbs_enable", true);
    }

    public final a<T> a(T t) {
        this.e = t;
        return this;
    }
}
