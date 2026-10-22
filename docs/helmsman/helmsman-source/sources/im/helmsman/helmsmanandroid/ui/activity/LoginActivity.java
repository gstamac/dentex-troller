package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.presenter.LoginPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.fragment.MainDrawerMenuFragment;
import im.helmsman.helmsmanandroid.ui.view.LoginView;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public class LoginActivity extends Mvp2BaseActivity<LoginView, LoginPresenter> implements LoginView, TextWatcher {

    @BindView(R.id.btn_sinin)
    Button btnSinin;

    @BindView(R.id.btn_sinup)
    Button btnSinup;
    private CallbackManager callbackManager;

    @BindView(R.id.et_login_mail)
    EditText etLoginMail;

    @BindView(R.id.et_login_password)
    EditText etLoginPassword;

    @BindView(R.id.iv_facebook_login)
    ImageView ivFacebookLogin;

    @BindView(R.id.iv_login_mail_dismiss)
    ImageView ivLoginMailDismiss;

    @BindView(R.id.iv_weixin_login)
    ImageView ivWeixinLogin;

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        this.etLoginMail.addTextChangedListener(this);
        this.etLoginPassword.addTextChangedListener(this);
    }

    @OnClick({R.id.btn_sinin, R.id.btn_sinup, R.id.iv_facebook_login, R.id.iv_weixin_login})
    public void onClick(View view) throws Throwable {
        switch (view.getId()) {
            case R.id.btn_sinin /* 2131230887 */:
                showLoading(getString(R.string.loading), getString(R.string.login_loading));
                ((LoginPresenter) this.presenter).login(this.etLoginMail.getText().toString(), this.etLoginPassword.getText().toString());
                break;
            case R.id.btn_sinup /* 2131230888 */:
                finish();
                finishActivity(MyFollowingRouteActivity.class);
                MainDrawerMenuFragment.getInstance().showRegister();
                break;
            case R.id.iv_facebook_login /* 2131231139 */:
                LoginManager.getInstance().logOut();
                this.callbackManager = CallbackManager.Factory.create();
                LoginButton loginButton = new LoginButton(this);
                loginButton.performClick();
                ((LoginPresenter) this.presenter).initFaceBookSdkLogin(loginButton, this.callbackManager);
                break;
            case R.id.iv_weixin_login /* 2131231206 */:
                if (!MyApplication.getWxApi().isWXAppInstalled()) {
                    ViewUtils.showSnackBar((View) this.etLoginMail, R.string.you_need_install_wx, true);
                } else {
                    IWXAPI wxApi = MyApplication.getWxApi();
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    wxApi.sendReq(req);
                }
                break;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.callbackManager.onActivityResult(i, i2, intent);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LoginView
    public void showFaceBookCancleMessage() {
        ViewUtils.ShowToast(R.string.user_cancle);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LoginView
    public void showFaceBookError(String str) {
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LoginView
    public void showLogininFailedMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.loginerror) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LoginView
    public void showLoginSuccessMessafe() {
        ViewUtils.ShowToast(R.string.loginsuccess);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LoginView
    public void dismissProgress() {
        ViewUtils.cancelProgressDialog();
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.ivLoginMailDismiss.setVisibility(0);
        if (StringUtils.isEmail(this.etLoginMail.getText().toString())) {
            this.ivLoginMailDismiss.setImageResource(R.drawable.icon_login_correct);
            this.btnSinin.setEnabled(true);
        } else {
            this.ivLoginMailDismiss.setImageResource(R.drawable.icon_login_dismiss);
            this.btnSinin.setEnabled(false);
        }
        if (this.etLoginPassword.getText().length() > 0) {
            this.btnSinin.setEnabled(true);
        } else {
            this.btnSinin.setEnabled(false);
        }
    }
}
