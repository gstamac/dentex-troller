package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class RoundCornerProgressBar extends BaseRoundCornerProgressBar {
    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void initStyleable(Context context, AttributeSet attributeSet) {
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void initView() {
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void onViewDraw() {
    }

    public RoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RoundCornerProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    public int initLayout() {
        return R.layout.layout_round_corner_progress_bar;
    }

    @Override // com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar
    protected void drawProgress(LinearLayout linearLayout, float f, float f2, float f3, int i, int i2, int i3, boolean z) {
        GradientDrawable gradientDrawableCreateGradientDrawable = createGradientDrawable(i3);
        float f4 = i - (i2 / 2);
        gradientDrawableCreateGradientDrawable.setCornerRadii(new float[]{f4, f4, f4, f4, f4, f4, f4, f4});
        linearLayout.setBackground(gradientDrawableCreateGradientDrawable);
        int i4 = (int) ((f3 - (i2 * 2)) / (f / f2));
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = i4;
        linearLayout.setLayoutParams(layoutParams);
    }
}
