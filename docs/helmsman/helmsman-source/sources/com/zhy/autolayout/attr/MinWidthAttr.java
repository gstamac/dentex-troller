package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class MinWidthAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 8192;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return true;
    }

    public MinWidthAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        view.setMinimumWidth(i);
    }

    public static int getMinWidth(View view) {
        return view.getMinimumWidth();
    }

    public static MinWidthAttr generate(int i, int i2) {
        MinWidthAttr minWidthAttr;
        if (i2 == 1) {
            minWidthAttr = new MinWidthAttr(i, 8192, 0);
        } else if (i2 == 2) {
            minWidthAttr = new MinWidthAttr(i, 0, 8192);
        } else {
            if (i2 != 3) {
                return null;
            }
            minWidthAttr = new MinWidthAttr(i, 0, 0);
        }
        return minWidthAttr;
    }
}
