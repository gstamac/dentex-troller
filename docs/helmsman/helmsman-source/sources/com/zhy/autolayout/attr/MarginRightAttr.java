package com.zhy.autolayout.attr;

import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes2.dex */
public class MarginRightAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 128;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return true;
    }

    public MarginRightAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).rightMargin = i;
        }
    }

    public static MarginRightAttr generate(int i, int i2) {
        MarginRightAttr marginRightAttr;
        if (i2 == 1) {
            marginRightAttr = new MarginRightAttr(i, 128, 0);
        } else if (i2 == 2) {
            marginRightAttr = new MarginRightAttr(i, 0, 128);
        } else {
            if (i2 != 3) {
                return null;
            }
            marginRightAttr = new MarginRightAttr(i, 0, 0);
        }
        return marginRightAttr;
    }
}
