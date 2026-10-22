package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class MaxHeightAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 65536;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public MaxHeightAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        try {
            view.getClass().getMethod("setMaxHeight", Integer.TYPE).invoke(view, Integer.valueOf(i));
        } catch (Exception unused) {
        }
    }

    public static MaxHeightAttr generate(int i, int i2) {
        MaxHeightAttr maxHeightAttr;
        if (i2 == 1) {
            maxHeightAttr = new MaxHeightAttr(i, 65536, 0);
        } else if (i2 == 2) {
            maxHeightAttr = new MaxHeightAttr(i, 0, 65536);
        } else {
            if (i2 != 3) {
                return null;
            }
            maxHeightAttr = new MaxHeightAttr(i, 0, 0);
        }
        return maxHeightAttr;
    }

    public static int getMaxHeight(View view) {
        try {
            return ((Integer) view.getClass().getMethod("getMaxHeight", new Class[0]).invoke(view, new Object[0])).intValue();
        } catch (Exception unused) {
            return 0;
        }
    }
}
