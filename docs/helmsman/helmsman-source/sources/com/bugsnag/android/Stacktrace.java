package com.bugsnag.android;

import com.facebook.internal.AnalyticsEvents;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class Stacktrace implements JsonStream.Streamable {
    final Configuration config;
    final StackTraceElement[] stacktrace;

    Stacktrace(Configuration configuration, StackTraceElement[] stackTraceElementArr) {
        this.config = configuration;
        this.stacktrace = stackTraceElementArr;
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginArray();
        for (StackTraceElement stackTraceElement : this.stacktrace) {
            try {
                jsonStream.beginObject();
                jsonStream.name("method").value(stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
                jsonStream.name("file").value(stackTraceElement.getFileName() == null ? AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN : stackTraceElement.getFileName());
                jsonStream.name("lineNumber").value(stackTraceElement.getLineNumber());
                if (this.config.inProject(stackTraceElement.getClassName())) {
                    jsonStream.name("inProject").value(true);
                }
                jsonStream.endObject();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        jsonStream.endArray();
    }
}
