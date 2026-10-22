package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ChangePasswordModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class ModifyPasswordActivity extends Base2Activity implements TextWatcher {

    @BindView(R.id.et_modify_confirm_password)
    EditText etModifyConfirmPassword;

    @BindView(R.id.et_modify_newpassword)
    EditText etModifyNewpassword;

    @BindView(R.id.et_modify_password)
    EditText etModifyPassword;

    @BindView(R.id.iv_confirmpassword_trip)
    ImageView ivConfirmpasswordTrip;

    @BindView(R.id.iv_edit_new_userinfo_tick)
    ImageView ivEditNewUserinfoTick;

    @BindView(R.id.iv_newpassword_trip)
    ImageView ivNewpasswordTrip;

    @BindView(R.id.iv_password_trip)
    ImageView ivPasswordTrip;
    private int passwordState;

    @BindView(R.id.tv_modify_password_hint)
    TextView tvModifyPasswordHint;

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_modify_password);
        setStatusBar(R.color.red_state);
        this.passwordState = getIntent().getIntExtra("passwordstate", 0);
        initEditTextState();
        initEvent();
    }

    private void initEvent() {
        this.etModifyPassword.addTextChangedListener(this);
        this.etModifyConfirmPassword.addTextChangedListener(this);
        this.etModifyNewpassword.addTextChangedListener(this);
    }

    private void initEditTextState() {
        int i = this.passwordState;
        if (i == 1) {
            this.tvModifyPasswordHint.setVisibility(8);
            this.etModifyPassword.setVisibility(8);
        } else {
            if (i != 2) {
                return;
            }
            this.tvModifyPasswordHint.setVisibility(0);
            this.etModifyPassword.setVisibility(0);
        }
    }

    @OnClick({R.id.iv_edit_new_userinfo_tick})
    public void onClick() {
        String string = this.etModifyPassword.getText().toString();
        String string2 = this.etModifyNewpassword.getText().toString();
        if (string2.equals(this.etModifyConfirmPassword.getText().toString())) {
            RequestInetUtils.instance().changePassword(string, string2, new ChangePasswordListener());
        } else {
            ViewUtils.showSnackBar((View) this.etModifyConfirmPassword, R.string.two_password_not_samle, R.string.app_ok, false, (View.OnClickListener) null);
        }
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0088  */
    /* JADX WARN: Code duplicated, block: B:36:0x00ba  */
    /* JADX WARN: Code duplicated, block: B:39:0x00c2  */
    /* JADX WARN: Code duplicated, block: B:40:0x00c8  */
    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        int i4;
        String string = this.etModifyPassword.getText().toString();
        String string2 = this.etModifyNewpassword.getText().toString();
        String string3 = this.etModifyConfirmPassword.getText().toString();
        if (string == null || "".equals(string)) {
            this.ivPasswordTrip.setVisibility(8);
        } else {
            if (string.length() >= 8 && StringUtils.isHaveSpecialCharacters(string)) {
                this.ivPasswordTrip.setVisibility(0);
                this.ivPasswordTrip.setImageResource(R.drawable.icon_login_correct);
                i4 = 1;
            } else {
                this.ivPasswordTrip.setVisibility(0);
                this.ivPasswordTrip.setImageResource(R.drawable.icon_login_dismiss);
            }
            if (string2 != null || "".equals(string2)) {
                this.ivNewpasswordTrip.setVisibility(8);
            } else if (string2.length() >= 8 && StringUtils.isHaveSpecialCharacters(string2)) {
                this.ivNewpasswordTrip.setVisibility(0);
                this.ivNewpasswordTrip.setImageResource(R.drawable.icon_login_correct);
                i4++;
            } else {
                this.ivNewpasswordTrip.setVisibility(0);
                this.ivNewpasswordTrip.setImageResource(R.drawable.icon_login_dismiss);
            }
            if (string3 != null || "".equals(string3)) {
                this.ivConfirmpasswordTrip.setVisibility(8);
            } else if (string3.length() >= 8 && StringUtils.isHaveSpecialCharacters(string3)) {
                this.ivConfirmpasswordTrip.setVisibility(0);
                this.ivConfirmpasswordTrip.setImageResource(R.drawable.icon_login_correct);
                i4++;
            } else {
                this.ivConfirmpasswordTrip.setVisibility(0);
                this.ivConfirmpasswordTrip.setImageResource(R.drawable.icon_login_dismiss);
            }
            if (i4 == 3) {
                this.ivEditNewUserinfoTick.setVisibility(0);
            } else {
                this.ivEditNewUserinfoTick.setVisibility(8);
            }
        }
        i4 = 0;
        if (string2 != null) {
            this.ivNewpasswordTrip.setVisibility(8);
        } else {
            this.ivNewpasswordTrip.setVisibility(8);
        }
        if (string3 != null) {
            this.ivConfirmpasswordTrip.setVisibility(8);
        } else {
            this.ivConfirmpasswordTrip.setVisibility(8);
        }
        if (i4 == 3) {
            this.ivEditNewUserinfoTick.setVisibility(0);
        } else {
            this.ivEditNewUserinfoTick.setVisibility(8);
        }
    }

    class ChangePasswordListener extends Callback<ChangePasswordModel> {
        ChangePasswordListener() {
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onResponse(Response<ChangePasswordModel> response) {
            ViewUtils.ShowToast(R.string.changepasswordactivity_changepasswordsuccess);
            UserMe.loginOut();
            ModifyPasswordActivity.this.finish();
            Base2Activity.finishActivity(AccountActivity.class);
            Base2Activity.finishActivity(AccountSecuriytActivity.class);
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onFailure(ErrorCode errorCode) {
            ViewUtils.ShowToast(ModifyPasswordActivity.this.getString(R.string.changepasswordactivity_changepasswordfaild));
        }
    }
}
