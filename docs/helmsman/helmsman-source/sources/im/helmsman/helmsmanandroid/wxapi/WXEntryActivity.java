package im.helmsman.helmsmanandroid.wxapi;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.presenter.WXEntryPresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.WXEntryView;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
public class WXEntryActivity extends Mvp2BaseActivity<WXEntryView, WXEntryPresenter> implements IWXAPIEventHandler, WXEntryView {
    public static final int FACEBOOK = 1;
    public static final int WECHAT = 2;

    @BindView(R.id.activity_edit_new_user_info)
    RelativeLayout activityEditNewUserInfo;
    private IWXAPI api;

    @BindView(R.id.civ_profile_avatar)
    CircleImageView civProfileAvatar;
    private String code;

    @BindView(R.id.et_edit_new_userinfo_contry)
    EditText etEditNewUserinfoContry;

    @BindView(R.id.et_edit_new_userinfo_sex)
    EditText etEditNewUserinfoSex;

    @BindView(R.id.iv_edit_new_userinfo_description)
    EditText ivEditNewUserinfoDescription;

    @BindView(R.id.iv_edit_new_userinfo_email)
    EditText ivEditNewUserinfoEmail;

    @BindView(R.id.iv_edit_new_userinfo_nickname)
    EditText ivEditNewUserinfoNickname;

    @BindView(R.id.iv_edit_new_userinfo_tick)
    ImageView ivEditNewUserinfoTick;

    @BindView(R.id.rel_edit_new_userinfo_contry)
    RelativeLayout relEditNewUserinfoContry;

    @BindView(R.id.rel_edit_new_userinfo_sex)
    RelativeLayout relEditNewUserinfoSex;
    private String countryCode = "BT";
    private int sexCode = 0;
    private int model = 2;
    private int successCount = 0;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void onBindEmailFailure() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void onSaveUserInfoFailure() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void showLoginFailedMessage(ErrorCode errorCode) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void uploadAvatarFailure() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public WXEntryPresenter initPresenter() {
        return new WXEntryPresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_new_user_info);
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra("model", 2);
        this.model = intExtra;
        if (intExtra == 1) {
            ((WXEntryPresenter) this.presenter).downloadAvatarBitmap(intent.getStringExtra("avatarurl"));
            ((WXEntryPresenter) this.presenter).getUserInfo();
        } else {
            IWXAPI wxApi = MyApplication.getWxApi();
            this.api = wxApi;
            wxApi.handleIntent(getIntent(), this);
            ViewUtils.showProgressDialog(R.string.loading);
        }
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPIEventHandler
    public void onReq(BaseReq baseReq) {
        int type = baseReq.getType();
        if (type == 3) {
            Log.e("onReq", "COMMAND_GETMESSAGE_FROM_WX");
        } else if (type == 4) {
            Log.e("onReq", "COMMAND_SHOWMESSAGE_FROM_WX");
        } else {
            Log.e("onReq", "default");
        }
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPIEventHandler
    public void onResp(BaseResp baseResp) {
        int i = baseResp.errCode;
        if (i == -4) {
            ViewUtils.ShowToast(R.string.user_denied);
            finishActivity();
        } else if (i != -2) {
            if (i == 0 && baseResp.getType() == 2) {
                finishActivity();
                return;
            }
        } else {
            ViewUtils.ShowToast(R.string.user_cancle);
            finishActivity();
        }
        handleIntent(getIntent());
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        Log.e("handlerIntent", resp.getType() + "");
        int type = resp.getType();
        if (type != 1) {
            if (type != 2) {
                return;
            }
            finishActivity();
        } else if (resp.errCode == 0) {
            this.code = resp.code;
            ((WXEntryPresenter) this.presenter).getAccessToken(this.code);
            ViewUtils.setProgressDialogText("Loading");
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void showLoadingProgressDialog() {
        ViewUtils.showProgressDialog("Loading");
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void dismissLoadingProgressDialog() {
        ViewUtils.cancelProgressDialog();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void finishActivity() {
        finish();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void loadAvatarToImageView(File file) {
        ViewUtils.ShowToast(R.string.main_success);
        Glide.with(getTopActivity()).load(file).into(this.civProfileAvatar);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void contryNotNull() {
        ViewUtils.showSnackBar((View) this.etEditNewUserinfoContry, R.string.contry_null, R.string.app_ok, false, (View.OnClickListener) null);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void nickNameFormatError() {
        ViewUtils.showSnackBar((View) this.etEditNewUserinfoContry, R.string.nickname_format_err, R.string.app_ok, false, (View.OnClickListener) null);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void emailFormatError() {
        ViewUtils.showSnackBar((View) this.etEditNewUserinfoContry, R.string.email_format_error, R.string.app_ok, false, (View.OnClickListener) null);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void showBindWXSuccessMessage() {
        ViewUtils.ShowToast(R.string.bind_success);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void showBindFailedMessage(String str) {
        finishActivity();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void setAvatar(Bitmap bitmap) {
        if (bitmap != null) {
            this.civProfileAvatar.setImageBitmap(bitmap);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void setUserInfo(UserMe userMe) {
        String email = userMe.getEmail();
        String about_me = userMe.getAbout_me();
        String country_code = userMe.getCountry_code();
        this.countryCode = country_code;
        if (TextUtils.isEmpty(country_code)) {
            this.countryCode = "BT";
        }
        this.sexCode = userMe.getSex();
        this.ivEditNewUserinfoNickname.setText(userMe.getUsername());
        int i = this.sexCode;
        if (i == 0) {
            this.etEditNewUserinfoSex.setText(R.string.secrecy);
        } else if (i == 1) {
            this.etEditNewUserinfoSex.setText(R.string.male);
        } else {
            this.etEditNewUserinfoSex.setText(R.string.female);
        }
        this.etEditNewUserinfoContry.setText(StringUtils.getCountryNameByCode(this.countryCode));
        this.ivEditNewUserinfoEmail.setText(email);
        this.ivEditNewUserinfoDescription.setText(about_me);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void uploadAvatarSuccess() {
        int i = this.successCount + 1;
        this.successCount = i;
        if (i >= 3) {
            finish();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void onSaveUserInfoSuccess() {
        int i = this.successCount + 1;
        this.successCount = i;
        if (i >= 3) {
            finish();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.WXEntryView
    public void onBindEmailSuccess() {
        int i = this.successCount + 1;
        this.successCount = i;
        if (i >= 3) {
            finish();
        }
    }

    @OnClick({R.id.iv_edit_new_userinfo_tick, R.id.civ_profile_avatar, R.id.rel_edit_new_userinfo_contry, R.id.et_edit_new_userinfo_contry, R.id.rel_edit_new_userinfo_sex, R.id.et_edit_new_userinfo_sex})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_profile_avatar /* 2131230920 */:
                Intent intent = new Intent("android.intent.action.GET_CONTENT", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
                break;
            case R.id.et_edit_new_userinfo_contry /* 2131231010 */:
            case R.id.rel_edit_new_userinfo_contry /* 2131231385 */:
                CountryPicker.getInstance("Select Country", new CountryPickerListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity.1
                    @Override // com.juanpabloprado.countrypicker.CountryPickerListener
                    public void onSelectCountry(String str, String str2) {
                        ((DialogFragment) WXEntryActivity.this.getSupportFragmentManager().findFragmentByTag("CountryPicker")).dismiss();
                        WXEntryActivity.this.countryCode = str2;
                        WXEntryActivity.this.etEditNewUserinfoContry.setText(StringUtils.getCountryNameByCode(WXEntryActivity.this.countryCode));
                    }
                }).show(getSupportFragmentManager(), "CountryPicker");
                break;
            case R.id.et_edit_new_userinfo_sex /* 2131231011 */:
            case R.id.rel_edit_new_userinfo_sex /* 2131231386 */:
                showSexDialog();
                break;
            case R.id.iv_edit_new_userinfo_tick /* 2131231135 */:
                this.successCount = 0;
                String string = this.ivEditNewUserinfoNickname.getText().toString();
                String string2 = this.ivEditNewUserinfoDescription.getText().toString();
                String string3 = this.ivEditNewUserinfoEmail.getText().toString();
                ((WXEntryPresenter) this.presenter).saveUserinfo(this.countryCode, string, string2, string3, this.sexCode);
                ((WXEntryPresenter) this.presenter).uploadAvatarByBitMap();
                ((WXEntryPresenter) this.presenter).bindEmail(string3);
                break;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ((WXEntryPresenter) this.presenter).getPhotoURLByAlbum(i, i2, intent);
    }

    private void showSexDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflate = View.inflate(this, R.layout.item_account_dialog_edit_gender, null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_account_gender_man);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.tv_account_gender_women);
        TextView textView3 = (TextView) viewInflate.findViewById(R.id.tv_account_gender_unset);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.setView(viewInflate);
        alertDialogCreate.show();
        textView.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                alertDialogCreate.dismiss();
                WXEntryActivity.this.etEditNewUserinfoSex.setText(R.string.male);
                WXEntryActivity.this.sexCode = 1;
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                alertDialogCreate.dismiss();
                WXEntryActivity.this.sexCode = 2;
                WXEntryActivity.this.etEditNewUserinfoSex.setText(R.string.female);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.wxapi.WXEntryActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                alertDialogCreate.dismiss();
                WXEntryActivity.this.sexCode = 0;
                WXEntryActivity.this.etEditNewUserinfoSex.setText(R.string.secrecy);
                alertDialogCreate.dismiss();
            }
        });
    }
}
