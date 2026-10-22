package com.tencent.a.a.a.a;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/* JADX INFO: loaded from: classes2.dex */
final class b extends f {
    b(Context context) {
        super(context);
    }

    @Override // com.tencent.a.a.a.a.f
    protected final void a(String str) {
        synchronized (this) {
            Log.i("MID", "write mid to InternalStorage");
            a.d(Environment.getExternalStorageDirectory() + "/" + h.f("6X8Y4XdM2Vhvn0I="));
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(), h.f("6X8Y4XdM2Vhvn0KfzcEatGnWaNU="))));
                bufferedWriter.write(h.f("4kU71lN96TJUomD1vOU9lgj9Tw==") + "," + str);
                bufferedWriter.write("\n");
                bufferedWriter.close();
            } catch (Exception e) {
                Log.w("MID", e);
            }
        }
    }

    @Override // com.tencent.a.a.a.a.f
    protected final boolean a() {
        return h.a(this.a, "android.permission.WRITE_EXTERNAL_STORAGE") && Environment.getExternalStorageState().equals("mounted");
    }

    @Override // com.tencent.a.a.a.a.f
    protected final String b() {
        String str;
        synchronized (this) {
            Log.i("MID", "read mid from InternalStorage");
            try {
                Iterator<String> it = a.a(new File(Environment.getExternalStorageDirectory(), h.f("6X8Y4XdM2Vhvn0KfzcEatGnWaNU="))).iterator();
                while (it.hasNext()) {
                    String[] strArrSplit = it.next().split(",");
                    if (strArrSplit.length == 2 && strArrSplit[0].equals(h.f("4kU71lN96TJUomD1vOU9lgj9Tw=="))) {
                        Log.i("MID", "read mid from InternalStorage:" + strArrSplit[1]);
                        str = strArrSplit[1];
                    }
                }
            } catch (IOException e) {
                Log.w("MID", e);
            }
            str = null;
        }
        return str;
    }
}
