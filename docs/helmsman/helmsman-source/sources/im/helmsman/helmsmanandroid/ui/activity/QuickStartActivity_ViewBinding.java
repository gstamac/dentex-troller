package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ExpandableListView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class QuickStartActivity_ViewBinding implements Unbinder {
    private QuickStartActivity target;

    public QuickStartActivity_ViewBinding(QuickStartActivity quickStartActivity) {
        this(quickStartActivity, quickStartActivity.getWindow().getDecorView());
    }

    public QuickStartActivity_ViewBinding(QuickStartActivity quickStartActivity, View view) {
        this.target = quickStartActivity;
        quickStartActivity.titleBar = (TitleBar) Utils.findRequiredViewAsType(view, R.id.titleBar, "field 'titleBar'", TitleBar.class);
        quickStartActivity.elvInstructions = (ExpandableListView) Utils.findRequiredViewAsType(view, R.id.elv_instructions, "field 'elvInstructions'", ExpandableListView.class);
        quickStartActivity.swipeRefresh = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.swipe_refresh, "field 'swipeRefresh'", SwipeRefreshLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        QuickStartActivity quickStartActivity = this.target;
        if (quickStartActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        quickStartActivity.titleBar = null;
        quickStartActivity.elvInstructions = null;
        quickStartActivity.swipeRefresh = null;
    }
}
