package im.helmsman.helmsmanandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.capricorn.ArcMenu;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/* JADX INFO: loaded from: classes2.dex */
public class AutoArcMenu extends ArcMenu {
    private final AutoLayoutHelper mHelper;

    public AutoArcMenu(Context context) {
        super(context);
        this.mHelper = new AutoLayoutHelper(this);
    }

    public AutoArcMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHelper = new AutoLayoutHelper(this);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new RelativeLayout.LayoutParams(getContext(), attributeSet);
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (!isInEditMode()) {
            this.mHelper.adjustChildren();
        }
        super.onMeasure(i, i2);
    }
}
