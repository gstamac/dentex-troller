package cn.jiguang.ad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
final class c implements Runnable {
    final /* synthetic */ ArrayList a;

    c(ArrayList arrayList) {
        this.a = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        BufferedWriter bufferedWriter = null;
        try {
            try {
                String str = b.c + "-" + cn.jiguang.f.c.b() + "_1.txt";
                File file = new File(str);
                file.getParentFile().mkdirs();
                int i = 2;
                while (file.exists()) {
                    str = b.c + "-" + cn.jiguang.f.c.b() + "_" + i + ".txt";
                    file = new File(str);
                    if (i > 10) {
                        d.f("Logger", "Unexpected error here, so many existed error file.");
                        break;
                    }
                    i++;
                }
                d.a("Logger", "Write log file: " + file.getName());
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(str));
                try {
                    Iterator it = this.a.iterator();
                    while (it.hasNext()) {
                        bufferedWriter2.write(((String) it.next()) + "\n");
                    }
                    b.e.clear();
                    bufferedWriter2.close();
                } catch (Throwable th) {
                    bufferedWriter = bufferedWriter2;
                    th = th;
                    try {
                        d.a("Logger", "write logs to file error", th);
                        b.e.clear();
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                    } catch (Throwable th2) {
                        try {
                            b.e.clear();
                            if (bufferedWriter != null) {
                                bufferedWriter.close();
                            }
                        } catch (Throwable th3) {
                            d.b("Logger", "close file stream error", th3);
                        }
                        throw th2;
                    }
                }
            } catch (Throwable th4) {
                d.b("Logger", "close file stream error", th4);
            }
        } catch (Throwable th5) {
            th = th5;
        }
        b.c();
    }
}
