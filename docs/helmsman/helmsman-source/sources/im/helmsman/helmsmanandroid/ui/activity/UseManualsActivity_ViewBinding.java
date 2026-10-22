package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.GridView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class UseManualsActivity_ViewBinding implements Unbinder {
    private UseManualsActivity target;

    public UseManualsActivity_ViewBinding(UseManualsActivity useManualsActivity) {
        this(useManualsActivity, useManualsActivity.getWindow().getDecorView());
    }

    public UseManualsActivity_ViewBinding(UseManualsActivity useManualsActivity, View view) {
        this.target = useManualsActivity;
        useManualsActivity.gvManualsList = (GridView) Utils.findRequiredViewAsType(view, R.id.gv_manuals_list, "field 'gvManualsList'", GridView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        UseManualsActivity useManualsActivity = this.target;
        if (useManualsActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        useManualsActivity.gvManualsList = null;
    }
}
