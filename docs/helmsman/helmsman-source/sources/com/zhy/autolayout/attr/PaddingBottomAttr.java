package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class PaddingBottomAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 4096;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public PaddingBottomAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), i);
    }

    public static PaddingBottomAttr generate(int i, int i2) {
        PaddingBottomAttr paddingBottomAttr;
        if (i2 == 1) {
            paddingBottomAttr = new PaddingBottomAttr(i, 4096, 0);
        } else if (i2 == 2) {
            paddingBottomAttr = new PaddingBottomAttr(i, 0, 4096);
        } else {
            if (i2 != 3) {
                return null;
            }
            paddingBottomAttr = new PaddingBottomAttr(i, 0, 0);
        }
        return paddingBottomAttr;
    }
}
