package cn.jiguang.aj;

import cn.jiguang.api.ReportCallBack;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class n {
    int a;
    JSONObject b;
    byte[] c;
    File d;
    Set<String> e;
    int f = -1;
    cn.jiguang.ah.h g;
    LinkedHashSet<cn.jiguang.ah.h> h;
    boolean i;
    transient ReportCallBack j;

    public final String toString() {
        return "TcpReportData{types=" + this.e + ", seqId=" + this.f + ", ipPort=" + this.g + ", restUrls=" + this.h + '}';
    }
}
