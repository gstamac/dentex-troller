package com.zhy.autolayout.utils;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.attr.HeightAttr;
import com.zhy.autolayout.attr.MarginAttr;
import com.zhy.autolayout.attr.MarginBottomAttr;
import com.zhy.autolayout.attr.MarginLeftAttr;
import com.zhy.autolayout.attr.MarginRightAttr;
import com.zhy.autolayout.attr.MarginTopAttr;
import com.zhy.autolayout.attr.MaxHeightAttr;
import com.zhy.autolayout.attr.MaxWidthAttr;
import com.zhy.autolayout.attr.MinHeightAttr;
import com.zhy.autolayout.attr.MinWidthAttr;
import com.zhy.autolayout.attr.PaddingAttr;
import com.zhy.autolayout.attr.PaddingBottomAttr;
import com.zhy.autolayout.attr.PaddingLeftAttr;
import com.zhy.autolayout.attr.PaddingRightAttr;
import com.zhy.autolayout.attr.PaddingTopAttr;
import com.zhy.autolayout.attr.TextSizeAttr;
import com.zhy.autolayout.attr.WidthAttr;
import com.zhy.autolayout.config.AutoLayoutConifg;

/* JADX INFO: loaded from: classes2.dex */
public class AutoLayoutHelper {
    private static final int INDEX_HEIGHT = 7;
    private static final int INDEX_MARGIN = 8;
    private static final int INDEX_MARGIN_BOTTOM = 12;
    private static final int INDEX_MARGIN_LEFT = 9;
    private static final int INDEX_MARGIN_RIGHT = 11;
    private static final int INDEX_MARGIN_TOP = 10;
    private static final int INDEX_MAX_HEIGHT = 14;
    private static final int INDEX_MAX_WIDTH = 13;
    private static final int INDEX_MIN_HEIGHT = 16;
    private static final int INDEX_MIN_WIDTH = 15;
    private static final int INDEX_PADDING = 1;
    private static final int INDEX_PADDING_BOTTOM = 5;
    private static final int INDEX_PADDING_LEFT = 2;
    private static final int INDEX_PADDING_RIGHT = 4;
    private static final int INDEX_PADDING_TOP = 3;
    private static final int INDEX_TEXT_SIZE = 0;
    private static final int INDEX_WIDTH = 6;
    private static final int[] LL = {R.attr.textSize, R.attr.padding, R.attr.paddingLeft, R.attr.paddingTop, R.attr.paddingRight, R.attr.paddingBottom, R.attr.layout_width, R.attr.layout_height, R.attr.layout_margin, R.attr.layout_marginLeft, R.attr.layout_marginTop, R.attr.layout_marginRight, R.attr.layout_marginBottom, R.attr.maxWidth, R.attr.maxHeight, R.attr.minWidth, R.attr.minHeight};
    private static AutoLayoutConifg mAutoLayoutConifg;
    private final ViewGroup mHost;

    public interface AutoLayoutParams {
        AutoLayoutInfo getAutoLayoutInfo();
    }

    public AutoLayoutHelper(ViewGroup viewGroup) {
        this.mHost = viewGroup;
        if (mAutoLayoutConifg == null) {
            initAutoLayoutConfig(viewGroup);
        }
    }

    private void initAutoLayoutConfig(ViewGroup viewGroup) {
        AutoLayoutConifg autoLayoutConifg = AutoLayoutConifg.getInstance();
        mAutoLayoutConifg = autoLayoutConifg;
        autoLayoutConifg.init(viewGroup.getContext());
    }

    public void adjustChildren() {
        AutoLayoutInfo autoLayoutInfo;
        AutoLayoutConifg.getInstance().checkParams();
        int childCount = this.mHost.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mHost.getChildAt(i);
            Object layoutParams = childAt.getLayoutParams();
            if ((layoutParams instanceof AutoLayoutParams) && (autoLayoutInfo = ((AutoLayoutParams) layoutParams).getAutoLayoutInfo()) != null) {
                autoLayoutInfo.fillAttrs(childAt);
            }
        }
    }

    public static AutoLayoutInfo getAutoLayoutInfo(Context context, AttributeSet attributeSet) {
        AutoLayoutInfo autoLayoutInfo = new AutoLayoutInfo();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, com.zhy.autolayout.R.styleable.AutoLayout_Layout);
        int i = typedArrayObtainStyledAttributes.getInt(com.zhy.autolayout.R.styleable.AutoLayout_Layout_layout_auto_basewidth, 0);
        int i2 = typedArrayObtainStyledAttributes.getInt(com.zhy.autolayout.R.styleable.AutoLayout_Layout_layout_auto_baseheight, 0);
        typedArrayObtainStyledAttributes.recycle();
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, LL);
        int indexCount = typedArrayObtainStyledAttributes2.getIndexCount();
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = typedArrayObtainStyledAttributes2.getIndex(i3);
            if (DimenUtils.isPxVal(typedArrayObtainStyledAttributes2.peekValue(index))) {
                try {
                    int dimensionPixelOffset = typedArrayObtainStyledAttributes2.getDimensionPixelOffset(index, 0);
                    switch (index) {
                        case 0:
                            autoLayoutInfo.addAttr(new TextSizeAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 1:
                            autoLayoutInfo.addAttr(new PaddingAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 2:
                            autoLayoutInfo.addAttr(new PaddingLeftAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 3:
                            autoLayoutInfo.addAttr(new PaddingTopAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 4:
                            autoLayoutInfo.addAttr(new PaddingRightAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 5:
                            autoLayoutInfo.addAttr(new PaddingBottomAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 6:
                            autoLayoutInfo.addAttr(new WidthAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 7:
                            autoLayoutInfo.addAttr(new HeightAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 8:
                            autoLayoutInfo.addAttr(new MarginAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 9:
                            autoLayoutInfo.addAttr(new MarginLeftAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 10:
                            autoLayoutInfo.addAttr(new MarginTopAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 11:
                            autoLayoutInfo.addAttr(new MarginRightAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 12:
                            autoLayoutInfo.addAttr(new MarginBottomAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 13:
                            autoLayoutInfo.addAttr(new MaxWidthAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 14:
                            autoLayoutInfo.addAttr(new MaxHeightAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 15:
                            autoLayoutInfo.addAttr(new MinWidthAttr(dimensionPixelOffset, i, i2));
                            break;
                        case 16:
                            autoLayoutInfo.addAttr(new MinHeightAttr(dimensionPixelOffset, i, i2));
                            break;
                    }
                } catch (Exception unused) {
                }
            }
        }
        typedArrayObtainStyledAttributes2.recycle();
        L.e(" getAutoLayoutInfo " + autoLayoutInfo.toString());
        return autoLayoutInfo;
    }
}
