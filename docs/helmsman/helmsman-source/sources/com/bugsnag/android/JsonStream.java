package com.bugsnag.android;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;

/* JADX INFO: loaded from: classes.dex */
public class JsonStream extends JsonWriter {
    private final Writer out;

    public interface Streamable {
        void toStream(JsonStream jsonStream) throws IOException;
    }

    JsonStream(Writer writer) {
        super(writer);
        setSerializeNulls(false);
        this.out = writer;
    }

    @Override // com.bugsnag.android.JsonWriter
    public JsonStream name(String str) throws IOException {
        super.name(str);
        return this;
    }

    public void value(Streamable streamable) throws IOException {
        if (streamable == null) {
            nullValue();
        } else {
            streamable.toStream(this);
        }
    }

    public void value(File file) throws Throwable {
        super.flush();
        FileReader fileReader = null;
        try {
            FileReader fileReader2 = new FileReader(file);
            try {
                IOUtils.copy(fileReader2, this.out);
                IOUtils.closeQuietly(fileReader2);
                this.out.flush();
            } catch (Throwable th) {
                th = th;
                fileReader = fileReader2;
                IOUtils.closeQuietly(fileReader);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
