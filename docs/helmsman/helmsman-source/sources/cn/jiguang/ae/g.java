package cn.jiguang.ae;

import java.io.File;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
final class g implements Comparator<File> {
    g() {
    }

    @Override // java.util.Comparator
    public final /* synthetic */ int compare(File file, File file2) {
        long jLastModified = file.lastModified() - file2.lastModified();
        if (jLastModified < 0) {
            return 1;
        }
        return jLastModified == 0 ? 0 : -1;
    }
}
