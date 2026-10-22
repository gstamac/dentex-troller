package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class HeightAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 2;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public HeightAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        view.getLayoutParams().height = i;
    }

    public static HeightAttr generate(int i, int i2) {
        HeightAttr heightAttr;
        if (i2 == 1) {
            heightAttr = new HeightAttr(i, 2, 0);
        } else if (i2 == 2) {
            heightAttr = new HeightAttr(i, 0, 2);
        } else {
            if (i2 != 3) {
                return null;
            }
            heightAttr = new HeightAttr(i, 0, 0);
        }
        return heightAttr;
    }
}
