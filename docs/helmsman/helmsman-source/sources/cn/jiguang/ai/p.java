package cn.jiguang.ai;

/* JADX INFO: loaded from: classes.dex */
public final class p {
    private static g a;
    private static String[] b;
    private static String[] c;

    static {
        g gVar = new g("Message Section", 3);
        a = gVar;
        b = new String[4];
        c = new String[4];
        gVar.a(3);
        a.a(true);
        a.a(0, "qd");
        a.a(1, "an");
        a.a(2, "au");
        a.a(3, "ad");
        String[] strArr = b;
        strArr[0] = "QUESTIONS";
        strArr[1] = "ANSWERS";
        strArr[2] = "AUTHORITY RECORDS";
        strArr[3] = "ADDITIONAL RECORDS";
        String[] strArr2 = c;
        strArr2[0] = "ZONE";
        strArr2[1] = "PREREQUISITES";
        strArr2[2] = "UPDATE RECORDS";
        strArr2[3] = "ADDITIONAL RECORDS";
    }

    public static String a(int i) {
        return a.b(i);
    }
}
