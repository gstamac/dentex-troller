package cn.jiguang.aa;

import cn.jiguang.internal.JConstants;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public boolean a = true;
    public boolean b = true;
    public boolean c = true;
    public boolean d = true;
    public long e = JConstants.HOUR;
    public long f = JConstants.HOUR;
    public long g = JConstants.HOUR;
    public String h = "disable";
    public List<String> i;
    public List<String> j;

    public final String toString() {
        return "JWakeConfigInfo{wakeEnableByAppKey=" + this.a + ", beWakeEnableByAppKey=" + this.b + ", wakeEnableByUId=" + this.c + ", beWakeEnableByUId=" + this.d + ", wakeInterval=" + this.e + ", wakeConfigInterval=" + this.f + ", wakeReportInterval=" + this.g + ", config='" + this.h + "', pkgList=" + this.i + ", blackPackageList=" + this.j + '}';
    }
}
