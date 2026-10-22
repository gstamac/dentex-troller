package com.bugsnag.android;

/* JADX INFO: loaded from: classes.dex */
public enum NotifyType {
    ALL(1),
    USER(2),
    APP(3),
    DEVICE(4),
    CONTEXT(5),
    RELEASE_STAGES(6),
    FILTERS(7),
    BREADCRUMB(8),
    META(9);

    private Integer intValue;

    NotifyType(Integer num) {
        this.intValue = num;
    }

    public Integer getValue() {
        return this.intValue;
    }

    public static NotifyType fromInt(Integer num) {
        if (num == null) {
            return null;
        }
        for (NotifyType notifyType : values()) {
            if (num.equals(notifyType.getValue())) {
                return notifyType;
            }
        }
        return null;
    }
}
