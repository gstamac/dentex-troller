package com.tencent.a.a.a.a;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
final class a {
    static List<String> a(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList arrayList = new ArrayList();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                fileReader.close();
                bufferedReader.close();
                return arrayList;
            }
            arrayList.add(line.trim());
        }
    }

    static File d(String str) {
        File file = new File(str);
        if (file.exists()) {
            return file;
        }
        if (!file.getParentFile().exists()) {
            d(file.getParentFile().getAbsolutePath());
        }
        file.mkdir();
        return file;
    }
}
