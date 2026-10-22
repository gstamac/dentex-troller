package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.BindView;
import com.facebook.CallbackManager;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.AccountSecuriytAdapter;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.AccountSecuriytStateResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.presenter.AccountSecuriytPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.AccountSecuriytView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class AccountSecuriytActivity extends Mvp2BaseActivity<AccountSecuriytView, AccountSecuriytPresenter> implements AccountSecuriytView, AdapterView.OnItemClickListener {
    private CallbackManager callbackManager;
    private String email;
    private boolean emailIsVerifiled = false;
    private String facebookId;

    @BindView(R.id.lv_account_securiyt_list)
    ListView lvAccountSecuriytList;
    private int passwordState;
    private String wxUnioId;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public AccountSecuriytPresenter initPresenter() {
        return new AccountSecuriytPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_account_securiyt);
        setStatusBar(R.color.red_state);
        Intent intent = getIntent();
        this.passwordState = intent.getIntExtra("passwordstate", 0);
        this.email = intent.getStringExtra("email");
        this.wxUnioId = intent.getStringExtra("wxunioid");
        this.facebookId = intent.getStringExtra("facebookid");
        this.emailIsVerifiled = intent.getBooleanExtra("emailIsVerified", false);
        this.lvAccountSecuriytList.setAdapter((ListAdapter) new AccountSecuriytAdapter(TextUtils.isEmpty(this.wxUnioId), TextUtils.isEmpty(this.facebookId), this.emailIsVerifiled));
        this.lvAccountSecuriytList.setOnItemClickListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        RequestInetUtils.instance().getAccountSecuriytState(new Callback<AccountSecuriytStateResultModel>() { // from class: im.helmsman.helmsmanandroid.ui.activity.AccountSecuriytActivity.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<AccountSecuriytStateResultModel> response) {
                AccountSecuriytStateResultModel accountSecuriytStateResultModelBody = response.body();
                AccountSecuriytActivity.this.email = accountSecuriytStateResultModelBody.getEmail();
                AccountSecuriytActivity.this.facebookId = accountSecuriytStateResultModelBody.getFb_id();
                AccountSecuriytActivity.this.wxUnioId = accountSecuriytStateResultModelBody.getWx_union_id();
                AccountSecuriytActivity.this.emailIsVerifiled = accountSecuriytStateResultModelBody.isEmail_is_verified();
                if (accountSecuriytStateResultModelBody.isPassword_is_null()) {
                    AccountSecuriytActivity.this.passwordState = 1;
                } else {
                    AccountSecuriytActivity.this.passwordState = 2;
                }
                AccountSecuriytActivity.this.lvAccountSecuriytList.setAdapter((ListAdapter) new AccountSecuriytAdapter(TextUtils.isEmpty(AccountSecuriytActivity.this.wxUnioId), TextUtils.isEmpty(AccountSecuriytActivity.this.facebookId), AccountSecuriytActivity.this.emailIsVerifiled));
            }
        });
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (i == 0) {
            Intent intent = new Intent(this, (Class<?>) BindEmailActivity.class);
            if (this.emailIsVerifiled) {
                intent.putExtra("email", this.email);
            }
            startActivity(intent);
            return;
        }
        if (i == 1) {
            if (this.emailIsVerifiled) {
                Intent intent2 = new Intent(this, (Class<?>) ModifyPasswordActivity.class);
                intent2.putExtra("passwordstate", this.passwordState);
                startActivity(intent2);
                return;
            }
            ViewUtils.ShowToast(R.string.unbound_email_can_not_modifypassword);
            return;
        }
        if (i == 2) {
            if (TextUtils.isEmpty(this.facebookId)) {
                this.callbackManager = CallbackManager.Factory.create();
                ((AccountSecuriytPresenter) this.presenter).bindFaceBook(this.callbackManager);
                return;
            }
            return;
        }
        if (i == 3 && TextUtils.isEmpty(this.wxUnioId)) {
            IWXAPI wxApi = MyApplication.getWxApi();
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            wxApi.sendReq(req);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.callbackManager.onActivityResult(i, i2, intent);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AccountSecuriytView
    public void showFaceBookCancleMessage() {
        ViewUtils.ShowToast(R.string.user_cancle);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.AccountSecuriytView
    public void showFaceBookError(String str) {
        ViewUtils.ShowToast(str);
    }
}
