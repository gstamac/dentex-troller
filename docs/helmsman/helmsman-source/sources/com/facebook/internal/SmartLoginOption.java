package com.facebook.internal;

import java.util.EnumSet;

/* JADX INFO: loaded from: classes.dex */
public enum SmartLoginOption {
    None(0),
    Enabled(1),
    RequireConfirm(2);

    public static final EnumSet<SmartLoginOption> ALL = EnumSet.allOf(SmartLoginOption.class);
    private final long mValue;

    public static EnumSet<SmartLoginOption> parseOptions(long j) {
        EnumSet<SmartLoginOption> enumSetNoneOf = EnumSet.noneOf(SmartLoginOption.class);
        for (SmartLoginOption smartLoginOption : ALL) {
            if ((smartLoginOption.getValue() & j) != 0) {
                enumSetNoneOf.add(smartLoginOption);
            }
        }
        return enumSetNoneOf;
    }

    SmartLoginOption(long j) {
        this.mValue = j;
    }

    public long getValue() {
        return this.mValue;
    }
}
