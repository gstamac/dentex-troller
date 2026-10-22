package com.zhy.autolayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.attr.HeightAttr;
import com.zhy.autolayout.attr.MarginBottomAttr;
import com.zhy.autolayout.attr.MarginLeftAttr;
import com.zhy.autolayout.attr.MarginRightAttr;
import com.zhy.autolayout.attr.MarginTopAttr;
import com.zhy.autolayout.attr.MaxHeightAttr;
import com.zhy.autolayout.attr.MaxWidthAttr;
import com.zhy.autolayout.attr.MinHeightAttr;
import com.zhy.autolayout.attr.MinWidthAttr;
import com.zhy.autolayout.attr.PaddingBottomAttr;
import com.zhy.autolayout.attr.PaddingLeftAttr;
import com.zhy.autolayout.attr.PaddingRightAttr;
import com.zhy.autolayout.attr.PaddingTopAttr;
import com.zhy.autolayout.attr.TextSizeAttr;
import com.zhy.autolayout.attr.WidthAttr;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AutoLayoutInfo {
    private List<AutoAttr> autoAttrs = new ArrayList();

    public void addAttr(AutoAttr autoAttr) {
        this.autoAttrs.add(autoAttr);
    }

    public void fillAttrs(View view) {
        Iterator<AutoAttr> it = this.autoAttrs.iterator();
        while (it.hasNext()) {
            it.next().apply(view);
        }
    }

    public static AutoLayoutInfo getAttrFromView(View view, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            return null;
        }
        AutoLayoutInfo autoLayoutInfo = new AutoLayoutInfo();
        if ((i & 1) != 0 && layoutParams.width > 0) {
            autoLayoutInfo.addAttr(WidthAttr.generate(layoutParams.width, i2));
        }
        if ((i & 2) != 0 && layoutParams.height > 0) {
            autoLayoutInfo.addAttr(HeightAttr.generate(layoutParams.height, i2));
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            if ((i & 16) != 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                autoLayoutInfo.addAttr(MarginLeftAttr.generate(marginLayoutParams.leftMargin, i2));
                autoLayoutInfo.addAttr(MarginTopAttr.generate(marginLayoutParams.topMargin, i2));
                autoLayoutInfo.addAttr(MarginRightAttr.generate(marginLayoutParams.rightMargin, i2));
                autoLayoutInfo.addAttr(MarginBottomAttr.generate(marginLayoutParams.bottomMargin, i2));
            }
            if ((i & 32) != 0) {
                autoLayoutInfo.addAttr(MarginLeftAttr.generate(((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, i2));
            }
            if ((i & 64) != 0) {
                autoLayoutInfo.addAttr(MarginTopAttr.generate(((ViewGroup.MarginLayoutParams) layoutParams).topMargin, i2));
            }
            if ((i & 128) != 0) {
                autoLayoutInfo.addAttr(MarginRightAttr.generate(((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, i2));
            }
            if ((i & 256) != 0) {
                autoLayoutInfo.addAttr(MarginBottomAttr.generate(((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, i2));
            }
        }
        if ((i & 8) != 0) {
            autoLayoutInfo.addAttr(PaddingLeftAttr.generate(view.getPaddingLeft(), i2));
            autoLayoutInfo.addAttr(PaddingTopAttr.generate(view.getPaddingTop(), i2));
            autoLayoutInfo.addAttr(PaddingRightAttr.generate(view.getPaddingRight(), i2));
            autoLayoutInfo.addAttr(PaddingBottomAttr.generate(view.getPaddingBottom(), i2));
        }
        if ((i & 512) != 0) {
            autoLayoutInfo.addAttr(MarginLeftAttr.generate(view.getPaddingLeft(), i2));
        }
        if ((i & 1024) != 0) {
            autoLayoutInfo.addAttr(MarginTopAttr.generate(view.getPaddingTop(), i2));
        }
        if ((i & 2048) != 0) {
            autoLayoutInfo.addAttr(MarginRightAttr.generate(view.getPaddingRight(), i2));
        }
        if ((i & 4096) != 0) {
            autoLayoutInfo.addAttr(MarginBottomAttr.generate(view.getPaddingBottom(), i2));
        }
        if ((i & 8192) != 0) {
            autoLayoutInfo.addAttr(MinWidthAttr.generate(MinWidthAttr.getMinWidth(view), i2));
        }
        if ((i & 16384) != 0) {
            autoLayoutInfo.addAttr(MaxWidthAttr.generate(MaxWidthAttr.getMaxWidth(view), i2));
        }
        if ((32768 & i) != 0) {
            autoLayoutInfo.addAttr(MinHeightAttr.generate(MinHeightAttr.getMinHeight(view), i2));
        }
        if ((65536 & i) != 0) {
            autoLayoutInfo.addAttr(MaxHeightAttr.generate(MaxHeightAttr.getMaxHeight(view), i2));
        }
        if ((view instanceof TextView) && (i & 4) != 0) {
            autoLayoutInfo.addAttr(TextSizeAttr.generate((int) ((TextView) view).getTextSize(), i2));
        }
        return autoLayoutInfo;
    }

    public String toString() {
        return "AutoLayoutInfo{autoAttrs=" + this.autoAttrs + '}';
    }
}
