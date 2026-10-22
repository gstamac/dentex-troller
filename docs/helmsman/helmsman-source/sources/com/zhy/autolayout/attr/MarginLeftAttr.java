package com.zhy.autolayout.attr;

import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes2.dex */
public class MarginLeftAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 32;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return true;
    }

    public MarginLeftAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).leftMargin = i;
        }
    }

    public static MarginLeftAttr generate(int i, int i2) {
        MarginLeftAttr marginLeftAttr;
        if (i2 == 1) {
            marginLeftAttr = new MarginLeftAttr(i, 32, 0);
        } else if (i2 == 2) {
            marginLeftAttr = new MarginLeftAttr(i, 0, 32);
        } else {
            if (i2 != 3) {
                return null;
            }
            marginLeftAttr = new MarginLeftAttr(i, 0, 0);
        }
        return marginLeftAttr;
    }
}
