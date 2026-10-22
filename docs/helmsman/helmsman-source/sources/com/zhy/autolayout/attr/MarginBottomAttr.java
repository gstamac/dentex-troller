package com.zhy.autolayout.attr;

import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes2.dex */
public class MarginBottomAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 256;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public MarginBottomAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin = i;
        }
    }

    public static MarginBottomAttr generate(int i, int i2) {
        MarginBottomAttr marginBottomAttr;
        if (i2 == 1) {
            marginBottomAttr = new MarginBottomAttr(i, 256, 0);
        } else if (i2 == 2) {
            marginBottomAttr = new MarginBottomAttr(i, 0, 256);
        } else {
            if (i2 != 3) {
                return null;
            }
            marginBottomAttr = new MarginBottomAttr(i, 0, 0);
        }
        return marginBottomAttr;
    }
}
