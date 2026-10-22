package im.helmsman.helmsmanandroid.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.presenter.BindEmailPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.BindEmailView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class BindEmailActivity extends Mvp2BaseActivity<BindEmailView, BindEmailPresenter> implements BindEmailView {
    private String email;

    @BindView(R.id.et_bindemail_email)
    EditText etBindemailEmail;
    InputMethodManager imm;

    @BindView(R.id.iv_edit_new_userinfo_tick)
    ImageView ivEditNewUserinfoTick;

    @BindView(R.id.iv_green_tick)
    ImageView ivTrick;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public BindEmailPresenter initPresenter() {
        return new BindEmailPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_bind_email);
        setStatusBar(R.color.red_state);
        this.imm = (InputMethodManager) getSystemService("input_method");
        String stringExtra = getIntent().getStringExtra("email");
        this.email = stringExtra;
        if (stringExtra != null) {
            this.etBindemailEmail.setText(stringExtra);
            this.ivTrick.setVisibility(0);
            this.etBindemailEmail.setFocusable(false);
            this.ivEditNewUserinfoTick.setVisibility(8);
            return;
        }
        this.ivTrick.setVisibility(8);
        this.etBindemailEmail.setFocusable(true);
        this.ivEditNewUserinfoTick.setVisibility(0);
    }

    @OnClick({R.id.iv_edit_new_userinfo_tick})
    public void onClick() {
        ViewUtils.showProgressDialog(R.string.loading);
        ((BindEmailPresenter) this.presenter).bindEmail(this.etBindemailEmail.getText().toString());
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.BindEmailView
    public void emailFormatError() {
        ViewUtils.showSnackBar((View) this.etBindemailEmail, getString(R.string.email_format_error), true);
        this.imm.hideSoftInputFromWindow(this.etBindemailEmail.getWindowToken(), 0);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.BindEmailView
    public void bindEmailError(String str) {
        ViewUtils.showSnackBar((View) this.etBindemailEmail, getString(R.string.loaddatafailed) + str, true);
        this.imm.hideSoftInputFromWindow(this.etBindemailEmail.getWindowToken(), 0);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.BindEmailView
    public void bindEmailSuccess() {
        this.imm.hideSoftInputFromWindow(this.etBindemailEmail.getWindowToken(), 0);
        ViewUtils.showAlertDialog(R.string.email, R.string.verified_email, R.string.app_ok, 0, (DialogInterface.OnClickListener) null);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.BindEmailView
    public void dismissDialog() {
        ViewUtils.cancelProgressDialog();
    }
}
