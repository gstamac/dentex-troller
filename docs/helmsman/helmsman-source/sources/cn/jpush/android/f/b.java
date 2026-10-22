package cn.jpush.android.f;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    public String a;
    public long b;

    public b(String str, long j) {
        this.a = str;
        this.b = j;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            b bVar = (b) obj;
            if (this.b == bVar.b && Objects.equals(this.a, bVar.a)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(this.a, Long.valueOf(this.b));
    }
}
