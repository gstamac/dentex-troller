package com.bugsnag.android;

import com.facebook.share.internal.ShareConstants;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class Exceptions implements JsonStream.Streamable {
    private final Configuration config;
    private Throwable exception;

    Exceptions(Configuration configuration, Throwable th) {
        this.config = configuration;
        this.exception = th;
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginArray();
        for (Throwable cause = this.exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof JsonStream.Streamable) {
                ((JsonStream.Streamable) cause).toStream(jsonStream);
            } else {
                exceptionToStream(jsonStream, getExceptionName(cause), cause.getLocalizedMessage(), cause.getStackTrace());
            }
        }
        jsonStream.endArray();
    }

    private String getExceptionName(Throwable th) {
        if (th instanceof BugsnagException) {
            return ((BugsnagException) th).getName();
        }
        return th.getClass().getName();
    }

    private void exceptionToStream(JsonStream jsonStream, String str, String str2, StackTraceElement[] stackTraceElementArr) throws IOException {
        Stacktrace stacktrace = new Stacktrace(this.config, stackTraceElementArr);
        jsonStream.beginObject();
        jsonStream.name("errorClass").value(str);
        jsonStream.name(ShareConstants.WEB_DIALOG_PARAM_MESSAGE).value(str2);
        jsonStream.name("type").value(this.config.defaultExceptionType);
        jsonStream.name("stacktrace").value(stacktrace);
        jsonStream.endObject();
    }
}
