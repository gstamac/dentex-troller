package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class MaxWidthAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 16384;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return true;
    }

    public MaxWidthAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        try {
            view.getClass().getMethod("setMaxWidth", Integer.TYPE).invoke(view, Integer.valueOf(i));
        } catch (Exception unused) {
        }
    }

    public static MaxWidthAttr generate(int i, int i2) {
        MaxWidthAttr maxWidthAttr;
        if (i2 == 1) {
            maxWidthAttr = new MaxWidthAttr(i, 16384, 0);
        } else if (i2 == 2) {
            maxWidthAttr = new MaxWidthAttr(i, 0, 16384);
        } else {
            if (i2 != 3) {
                return null;
            }
            maxWidthAttr = new MaxWidthAttr(i, 0, 0);
        }
        return maxWidthAttr;
    }

    public static int getMaxWidth(View view) {
        try {
            return ((Integer) view.getClass().getMethod("getMaxWidth", new Class[0]).invoke(view, new Object[0])).intValue();
        } catch (Exception unused) {
            return 0;
        }
    }
}
