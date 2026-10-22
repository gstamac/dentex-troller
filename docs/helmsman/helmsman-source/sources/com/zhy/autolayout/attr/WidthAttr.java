package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class WidthAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 1;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return true;
    }

    public WidthAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        view.getLayoutParams().width = i;
    }

    public static WidthAttr generate(int i, int i2) {
        WidthAttr widthAttr;
        if (i2 == 1) {
            widthAttr = new WidthAttr(i, 1, 0);
        } else if (i2 == 2) {
            widthAttr = new WidthAttr(i, 0, 1);
        } else {
            if (i2 != 3) {
                return null;
            }
            widthAttr = new WidthAttr(i, 0, 0);
        }
        return widthAttr;
    }
}
