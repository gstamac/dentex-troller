package com.amap.api.mapcore.util;

import android.content.Context;
import com.qiniu.android.common.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: compiled from: AndroidAssets.java */
/* JADX INFO: loaded from: classes.dex */
public class dc {
    static dc b;
    Context a;

    public static String a(String str) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStreamB = b.b(str);
        if (inputStreamB == null) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamB, Constants.UTF_8));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void a(Context context) {
        b = new dc(context);
    }

    private dc(Context context) {
        this.a = context;
    }

    public InputStream b(String str) {
        try {
            return this.a.getAssets().open(str);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
