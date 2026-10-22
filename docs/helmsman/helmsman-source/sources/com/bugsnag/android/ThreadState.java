package com.bugsnag.android;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class ThreadState implements JsonStream.Streamable {
    private static final String THREAD_TYPE = "android";
    final Configuration config;

    ThreadState(Configuration configuration) {
        this.config = configuration;
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        long id = Thread.currentThread().getId();
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        Object[] array = allStackTraces.keySet().toArray();
        Arrays.sort(array, new Comparator<Object>() { // from class: com.bugsnag.android.ThreadState.1
            @Override // java.util.Comparator
            public int compare(Object obj, Object obj2) {
                return Long.valueOf(((Thread) obj).getId()).compareTo(Long.valueOf(((Thread) obj2).getId()));
            }
        });
        jsonStream.beginArray();
        for (Object obj : array) {
            Thread thread = (Thread) obj;
            if (thread.getId() != id) {
                StackTraceElement[] stackTraceElementArr = allStackTraces.get(thread);
                jsonStream.beginObject();
                jsonStream.name("id").value(thread.getId());
                jsonStream.name("name").value(thread.getName());
                jsonStream.name("type").value("android");
                jsonStream.name("stacktrace").value(new Stacktrace(this.config, stackTraceElementArr));
                jsonStream.endObject();
            }
        }
        jsonStream.endArray();
    }
}
