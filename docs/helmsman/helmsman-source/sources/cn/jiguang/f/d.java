package cn.jiguang.f;

import java.text.SimpleDateFormat;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class d extends ThreadLocal<SimpleDateFormat> {
    private String a;

    d(String str) {
        this.a = str;
    }

    @Override // java.lang.ThreadLocal
    protected final /* synthetic */ SimpleDateFormat initialValue() {
        return new SimpleDateFormat(this.a, Locale.ENGLISH);
    }
}
