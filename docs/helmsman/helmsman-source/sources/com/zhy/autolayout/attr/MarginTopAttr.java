package com.zhy.autolayout.attr;

import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes2.dex */
public class MarginTopAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 64;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public MarginTopAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = i;
        }
    }

    public static MarginTopAttr generate(int i, int i2) {
        MarginTopAttr marginTopAttr;
        if (i2 == 1) {
            marginTopAttr = new MarginTopAttr(i, 64, 0);
        } else if (i2 == 2) {
            marginTopAttr = new MarginTopAttr(i, 0, 64);
        } else {
            if (i2 != 3) {
                return null;
            }
            marginTopAttr = new MarginTopAttr(i, 0, 0);
        }
        return marginTopAttr;
    }
}
