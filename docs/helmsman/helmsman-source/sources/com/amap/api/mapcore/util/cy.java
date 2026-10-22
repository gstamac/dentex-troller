package com.amap.api.mapcore.util;

import androidx.constraintlayout.core.motion.utils.TypedValues;

/* JADX INFO: compiled from: StyleElementType.java */
/* JADX INFO: loaded from: classes.dex */
public enum cy {
    STYLE_ELEMENT_LABELFILL_OLD("labels.text.fill", 0),
    STYLE_ELEMENT_LABELSTROKE_OLD("labels.text.stroke", 1),
    STYLE_ELEMENT_GEOMETRYSTROKE_OLD("geometry.stroke", 2),
    STYLE_ELEMENT_GEOMETRYFILL_OLD("geometry.fill", 3),
    STYLE_ELEMENT_LABELFILL("textFillColor", 0),
    STYLE_ELEMENT_LABELSTROKE("textStrokeColor", 1),
    STYLE_ELEMENT_GEOMETRYSTROKE("strokeColor", 2),
    STYLE_ELEMENT_GEOMETRYFILL("fillColor", 3),
    STYLE_ELEMENT_GEOMETRYFILL1(TypedValues.Custom.S_COLOR, 3),
    STYLE_ELEMENT_BACKGROUNDFILL("backgroundColor", 4),
    STYLE_ELEMENT_VISIBLE("visible", 5);

    private String l;
    private int m;

    cy(String str, int i) {
        this.l = str;
        this.m = i;
    }

    public String a() {
        return this.l;
    }

    public static int a(String str) {
        for (cy cyVar : values()) {
            if (cyVar.a().equals(str)) {
                return cyVar.m;
            }
        }
        return -1;
    }
}
