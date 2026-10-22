package com.zhy.autolayout.attr;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public class MinHeightAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 32768;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public MinHeightAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        try {
            view.setMinimumHeight(i);
        } catch (Exception unused) {
        }
    }

    public static MinHeightAttr generate(int i, int i2) {
        MinHeightAttr minHeightAttr;
        if (i2 == 1) {
            minHeightAttr = new MinHeightAttr(i, 32768, 0);
        } else if (i2 == 2) {
            minHeightAttr = new MinHeightAttr(i, 0, 32768);
        } else {
            if (i2 != 3) {
                return null;
            }
            minHeightAttr = new MinHeightAttr(i, 0, 0);
        }
        return minHeightAttr;
    }

    public static int getMinHeight(View view) {
        return view.getMinimumHeight();
    }
}
