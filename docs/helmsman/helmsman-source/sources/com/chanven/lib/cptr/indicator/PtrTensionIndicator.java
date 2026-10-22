package com.chanven.lib.cptr.indicator;

/* JADX INFO: loaded from: classes.dex */
public class PtrTensionIndicator extends PtrIndicator {
    private float mCurrentDragPercent;
    private float mDownPos;
    private float mDownY;
    private int mReleasePos;
    private float DRAG_RATE = 0.5f;
    private float mOneHeight = 0.0f;
    private float mReleasePercent = -1.0f;

    @Override // com.chanven.lib.cptr.indicator.PtrIndicator
    public void onPressDown(float f, float f2) {
        super.onPressDown(f, f2);
        this.mDownY = f2;
        this.mDownPos = getCurrentPosY();
    }

    @Override // com.chanven.lib.cptr.indicator.PtrIndicator
    public void onRelease() {
        super.onRelease();
        this.mReleasePos = getCurrentPosY();
        this.mReleasePercent = this.mCurrentDragPercent;
    }

    @Override // com.chanven.lib.cptr.indicator.PtrIndicator
    public void onUIRefreshComplete() {
        this.mReleasePos = getCurrentPosY();
        this.mReleasePercent = getOverDragPercent();
    }

    @Override // com.chanven.lib.cptr.indicator.PtrIndicator
    public void setHeaderHeight(int i) {
        super.setHeaderHeight(i);
        this.mOneHeight = (i * 4.0f) / 5.0f;
    }

    @Override // com.chanven.lib.cptr.indicator.PtrIndicator
    protected void processOnMove(float f, float f2, float f3, float f4) {
        float f5 = this.mDownY;
        if (f2 < f5) {
            super.processOnMove(f, f2, f3, f4);
            return;
        }
        float f6 = ((f2 - f5) * this.DRAG_RATE) + this.mDownPos;
        float f7 = f6 / this.mOneHeight;
        if (f7 < 0.0f) {
            setOffset(f3, 0.0f);
            return;
        }
        this.mCurrentDragPercent = f7;
        float fMin = Math.min(1.0f, Math.abs(f7));
        float f8 = this.mOneHeight;
        double dMax = Math.max(0.0f, Math.min(f6 - f8, f8 * 2.0f) / this.mOneHeight) / 4.0f;
        float fPow = ((float) (dMax - Math.pow(dMax, 2.0d))) * 2.0f;
        float f9 = this.mOneHeight;
        setOffset(f, ((int) ((f9 * fMin) + ((fPow * f9) / 2.0f))) - getCurrentPosY());
    }

    private float offsetToTarget(float f) {
        float f2 = f / this.mOneHeight;
        this.mCurrentDragPercent = f2;
        Math.min(1.0f, Math.abs(f2));
        float f3 = this.mOneHeight;
        Math.pow(Math.max(0.0f, Math.min(f - f3, f3 * 2.0f) / this.mOneHeight) / 4.0f, 2.0d);
        return 0.0f;
    }

    @Override // com.chanven.lib.cptr.indicator.PtrIndicator
    public int getOffsetToKeepHeaderWhileLoading() {
        return getOffsetToRefresh();
    }

    @Override // com.chanven.lib.cptr.indicator.PtrIndicator
    public int getOffsetToRefresh() {
        return (int) this.mOneHeight;
    }

    public float getOverDragPercent() {
        float currentPosY;
        int offsetToKeepHeaderWhileLoading;
        if (isUnderTouch()) {
            return this.mCurrentDragPercent;
        }
        float f = this.mReleasePercent;
        if (f <= 0.0f) {
            currentPosY = getCurrentPosY() * 1.0f;
            offsetToKeepHeaderWhileLoading = getOffsetToKeepHeaderWhileLoading();
        } else {
            currentPosY = f * getCurrentPosY();
            offsetToKeepHeaderWhileLoading = this.mReleasePos;
        }
        return currentPosY / offsetToKeepHeaderWhileLoading;
    }
}
