package com.bugsnag.android;

/* JADX INFO: loaded from: classes.dex */
public class BugsnagException extends Throwable {
    private String name;

    public BugsnagException(String str, String str2, StackTraceElement[] stackTraceElementArr) {
        super(str2);
        super.setStackTrace(stackTraceElementArr);
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
