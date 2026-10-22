package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.view.ViewStub;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class AddRouteActivity_ViewBinding implements Unbinder {
    private AddRouteActivity target;

    public AddRouteActivity_ViewBinding(AddRouteActivity addRouteActivity) {
        this(addRouteActivity, addRouteActivity.getWindow().getDecorView());
    }

    public AddRouteActivity_ViewBinding(AddRouteActivity addRouteActivity, View view) {
        this.target = addRouteActivity;
        addRouteActivity.vsGoogleMap = (ViewStub) Utils.findRequiredViewAsType(view, R.id.vs_google_map, "field 'vsGoogleMap'", ViewStub.class);
        addRouteActivity.vsGaodeMap = (ViewStub) Utils.findRequiredViewAsType(view, R.id.vs_gaode_map, "field 'vsGaodeMap'", ViewStub.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        AddRouteActivity addRouteActivity = this.target;
        if (addRouteActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        addRouteActivity.vsGoogleMap = null;
        addRouteActivity.vsGaodeMap = null;
    }
}
