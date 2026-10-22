package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class PaddingAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 8;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public PaddingAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    public void apply(View view) {
        if (useDefault()) {
            int percentWidthSize = getPercentWidthSize();
            int percentHeightSize = getPercentHeightSize();
            view.setPadding(percentWidthSize, percentHeightSize, percentWidthSize, percentHeightSize);
            return;
        }
        super.apply(view);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        view.setPadding(i, i, i, i);
    }
}
