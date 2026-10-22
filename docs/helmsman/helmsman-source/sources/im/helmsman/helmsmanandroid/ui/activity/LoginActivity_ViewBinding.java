package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class LoginActivity_ViewBinding implements Unbinder {
    private LoginActivity target;
    private View view7f0800a7;
    private View view7f0800a8;
    private View view7f0801a3;
    private View view7f0801e6;

    public LoginActivity_ViewBinding(LoginActivity loginActivity) {
        this(loginActivity, loginActivity.getWindow().getDecorView());
    }

    public LoginActivity_ViewBinding(final LoginActivity loginActivity, View view) {
        this.target = loginActivity;
        loginActivity.etLoginMail = (EditText) Utils.findRequiredViewAsType(view, R.id.et_login_mail, "field 'etLoginMail'", EditText.class);
        loginActivity.etLoginPassword = (EditText) Utils.findRequiredViewAsType(view, R.id.et_login_password, "field 'etLoginPassword'", EditText.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.btn_sinin, "field 'btnSinin' and method 'onClick'");
        loginActivity.btnSinin = (Button) Utils.castView(viewFindRequiredView, R.id.btn_sinin, "field 'btnSinin'", Button.class);
        this.view7f0800a7 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.LoginActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) throws Throwable {
                loginActivity.onClick(view2);
            }
        });
        View viewFindRequiredView2 = Utils.findRequiredView(view, R.id.btn_sinup, "field 'btnSinup' and method 'onClick'");
        loginActivity.btnSinup = (Button) Utils.castView(viewFindRequiredView2, R.id.btn_sinup, "field 'btnSinup'", Button.class);
        this.view7f0800a8 = viewFindRequiredView2;
        viewFindRequiredView2.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.LoginActivity_ViewBinding.2
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) throws Throwable {
                loginActivity.onClick(view2);
            }
        });
        View viewFindRequiredView3 = Utils.findRequiredView(view, R.id.iv_facebook_login, "field 'ivFacebookLogin' and method 'onClick'");
        loginActivity.ivFacebookLogin = (ImageView) Utils.castView(viewFindRequiredView3, R.id.iv_facebook_login, "field 'ivFacebookLogin'", ImageView.class);
        this.view7f0801a3 = viewFindRequiredView3;
        viewFindRequiredView3.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.LoginActivity_ViewBinding.3
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) throws Throwable {
                loginActivity.onClick(view2);
            }
        });
        View viewFindRequiredView4 = Utils.findRequiredView(view, R.id.iv_weixin_login, "field 'ivWeixinLogin' and method 'onClick'");
        loginActivity.ivWeixinLogin = (ImageView) Utils.castView(viewFindRequiredView4, R.id.iv_weixin_login, "field 'ivWeixinLogin'", ImageView.class);
        this.view7f0801e6 = viewFindRequiredView4;
        viewFindRequiredView4.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.LoginActivity_ViewBinding.4
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) throws Throwable {
                loginActivity.onClick(view2);
            }
        });
        loginActivity.ivLoginMailDismiss = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_login_mail_dismiss, "field 'ivLoginMailDismiss'", ImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        LoginActivity loginActivity = this.target;
        if (loginActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        loginActivity.etLoginMail = null;
        loginActivity.etLoginPassword = null;
        loginActivity.btnSinin = null;
        loginActivity.btnSinup = null;
        loginActivity.ivFacebookLogin = null;
        loginActivity.ivWeixinLogin = null;
        loginActivity.ivLoginMailDismiss = null;
        this.view7f0800a7.setOnClickListener(null);
        this.view7f0800a7 = null;
        this.view7f0800a8.setOnClickListener(null);
        this.view7f0800a8 = null;
        this.view7f0801a3.setOnClickListener(null);
        this.view7f0801a3 = null;
        this.view7f0801e6.setOnClickListener(null);
        this.view7f0801e6 = null;
    }
}
