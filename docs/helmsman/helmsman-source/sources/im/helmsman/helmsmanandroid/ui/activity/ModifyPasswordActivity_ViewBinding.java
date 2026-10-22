package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class ModifyPasswordActivity_ViewBinding implements Unbinder {
    private ModifyPasswordActivity target;
    private View view7f08019f;

    public ModifyPasswordActivity_ViewBinding(ModifyPasswordActivity modifyPasswordActivity) {
        this(modifyPasswordActivity, modifyPasswordActivity.getWindow().getDecorView());
    }

    public ModifyPasswordActivity_ViewBinding(final ModifyPasswordActivity modifyPasswordActivity, View view) {
        this.target = modifyPasswordActivity;
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_edit_new_userinfo_tick, "field 'ivEditNewUserinfoTick' and method 'onClick'");
        modifyPasswordActivity.ivEditNewUserinfoTick = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_edit_new_userinfo_tick, "field 'ivEditNewUserinfoTick'", ImageView.class);
        this.view7f08019f = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.ModifyPasswordActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                modifyPasswordActivity.onClick();
            }
        });
        modifyPasswordActivity.etModifyPassword = (EditText) Utils.findRequiredViewAsType(view, R.id.et_modify_password, "field 'etModifyPassword'", EditText.class);
        modifyPasswordActivity.etModifyNewpassword = (EditText) Utils.findRequiredViewAsType(view, R.id.et_modify_newpassword, "field 'etModifyNewpassword'", EditText.class);
        modifyPasswordActivity.etModifyConfirmPassword = (EditText) Utils.findRequiredViewAsType(view, R.id.et_modify_confirm_password, "field 'etModifyConfirmPassword'", EditText.class);
        modifyPasswordActivity.tvModifyPasswordHint = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_modify_password_hint, "field 'tvModifyPasswordHint'", TextView.class);
        modifyPasswordActivity.ivPasswordTrip = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_password_trip, "field 'ivPasswordTrip'", ImageView.class);
        modifyPasswordActivity.ivNewpasswordTrip = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_newpassword_trip, "field 'ivNewpasswordTrip'", ImageView.class);
        modifyPasswordActivity.ivConfirmpasswordTrip = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_confirmpassword_trip, "field 'ivConfirmpasswordTrip'", ImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        ModifyPasswordActivity modifyPasswordActivity = this.target;
        if (modifyPasswordActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        modifyPasswordActivity.ivEditNewUserinfoTick = null;
        modifyPasswordActivity.etModifyPassword = null;
        modifyPasswordActivity.etModifyNewpassword = null;
        modifyPasswordActivity.etModifyConfirmPassword = null;
        modifyPasswordActivity.tvModifyPasswordHint = null;
        modifyPasswordActivity.ivPasswordTrip = null;
        modifyPasswordActivity.ivNewpasswordTrip = null;
        modifyPasswordActivity.ivConfirmpasswordTrip = null;
        this.view7f08019f.setOnClickListener(null);
        this.view7f08019f = null;
    }
}
