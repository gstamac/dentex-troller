package com.chanven.lib.cptr;

import android.content.Context;
import android.util.AttributeSet;
import com.chanven.lib.cptr.loadmore.DefaultLoadMoreViewFooter;

/* JADX INFO: loaded from: classes.dex */
public class PtrClassicFrameLayout extends PtrFrameLayout {
    private PtrClassicDefaultHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    public void setHeaderText(int i, int i2) {
        this.mPtrClassicHeader.setHeadRefreshText(i, i2);
    }

    private void initViews() {
        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(getContext());
        this.mPtrClassicHeader = ptrClassicDefaultHeader;
        setHeaderView(ptrClassicDefaultHeader);
        addPtrUIHandler(this.mPtrClassicHeader);
        setFooterView(new DefaultLoadMoreViewFooter());
    }

    public PtrClassicDefaultHeader getHeader() {
        return this.mPtrClassicHeader;
    }

    public void setLastUpdateTimeKey(String str) {
        PtrClassicDefaultHeader ptrClassicDefaultHeader = this.mPtrClassicHeader;
        if (ptrClassicDefaultHeader != null) {
            ptrClassicDefaultHeader.setLastUpdateTimeKey(str);
        }
    }

    public void setLastUpdateTimeRelateObject(Object obj) {
        PtrClassicDefaultHeader ptrClassicDefaultHeader = this.mPtrClassicHeader;
        if (ptrClassicDefaultHeader != null) {
            ptrClassicDefaultHeader.setLastUpdateTimeRelateObject(obj);
        }
    }
}
