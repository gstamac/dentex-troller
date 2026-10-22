package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class BindEmailActivity_ViewBinding implements Unbinder {
    private BindEmailActivity target;
    private View view7f08019f;

    public BindEmailActivity_ViewBinding(BindEmailActivity bindEmailActivity) {
        this(bindEmailActivity, bindEmailActivity.getWindow().getDecorView());
    }

    public BindEmailActivity_ViewBinding(final BindEmailActivity bindEmailActivity, View view) {
        this.target = bindEmailActivity;
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_edit_new_userinfo_tick, "field 'ivEditNewUserinfoTick' and method 'onClick'");
        bindEmailActivity.ivEditNewUserinfoTick = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_edit_new_userinfo_tick, "field 'ivEditNewUserinfoTick'", ImageView.class);
        this.view7f08019f = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.BindEmailActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                bindEmailActivity.onClick();
            }
        });
        bindEmailActivity.etBindemailEmail = (EditText) Utils.findRequiredViewAsType(view, R.id.et_bindemail_email, "field 'etBindemailEmail'", EditText.class);
        bindEmailActivity.ivTrick = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_green_tick, "field 'ivTrick'", ImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        BindEmailActivity bindEmailActivity = this.target;
        if (bindEmailActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        bindEmailActivity.ivEditNewUserinfoTick = null;
        bindEmailActivity.etBindemailEmail = null;
        bindEmailActivity.ivTrick = null;
        this.view7f08019f.setOnClickListener(null);
        this.view7f08019f = null;
    }
}
