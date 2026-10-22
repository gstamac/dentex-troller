package com.zhy.autolayout.attr;

import android.view.View;
import android.widget.TextView;

/* JADX INFO: loaded from: classes2.dex */
public class TextSizeAttr extends AutoAttr {
    @Override // com.zhy.autolayout.attr.AutoAttr
    protected int attrVal() {
        return 4;
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected boolean defaultBaseWidth() {
        return false;
    }

    public TextSizeAttr(int i, int i2, int i3) {
        super(i, i2, i3);
    }

    @Override // com.zhy.autolayout.attr.AutoAttr
    protected void execute(View view, int i) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setIncludeFontPadding(false);
            textView.setTextSize(0, i);
        }
    }

    public static TextSizeAttr generate(int i, int i2) {
        TextSizeAttr textSizeAttr;
        if (i2 == 1) {
            textSizeAttr = new TextSizeAttr(i, 4, 0);
        } else if (i2 == 2) {
            textSizeAttr = new TextSizeAttr(i, 0, 4);
        } else {
            if (i2 != 3) {
                return null;
            }
            textSizeAttr = new TextSizeAttr(i, 0, 0);
        }
        return textSizeAttr;
    }
}
