package im.helmsman.helmsmanandroid.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.internal.NativeProtocol;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.DrawerMenuRecyclerAdapter;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.model.DrawerMenuModel;
import im.helmsman.helmsmanandroid.presenter.LeftDrawerPresenterImp;
import im.helmsman.helmsmanandroid.ui.activity.AccountActivity;
import im.helmsman.helmsmanandroid.ui.activity.BoatListActivity;
import im.helmsman.helmsmanandroid.ui.activity.FriendsActivity;
import im.helmsman.helmsmanandroid.ui.activity.InstructionsActivity;
import im.helmsman.helmsmanandroid.ui.activity.SettingActivity;
import im.helmsman.helmsmanandroid.ui.common.BaseFragment;
import im.helmsman.helmsmanandroid.ui.view.LeftDrawerView;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class MainDrawerMenuFragment extends BaseFragment<LeftDrawerView, LeftDrawerPresenterImp> implements LeftDrawerView, View.OnClickListener, DrawerMenuRecyclerAdapter.OnItemClickListener, TextWatcher {
    public static final String WX_APPID = "wx6e8c1cff7f9272dd";
    private static MainDrawerMenuFragment fragment;
    private DrawerMenuRecyclerAdapter adapter;
    private Button btnLogin;
    private Button btnRegister;
    private CallbackManager callbackManager;
    private ImageView ivWxLogin;
    private LoginButton lBtnFaceBookLogin;
    private LinearLayout linFollower;
    private LinearLayout linFollowing;
    private LinearLayout linRoute;
    private View loginAfter;
    private View loginBefore;
    private ImageView lvFaceBookLogin;
    private Button mBtnDialogCancle;
    private Button mBtnDialogFinish;
    private Button mBtnDialogLogin;
    private Button mBtnDialogReset;
    private AlertDialog.Builder mBuilderLogin;
    private CircleImageView mCircleImage;
    private View mDialogContentView;
    private AlertDialog mDialogLogin;
    private EditText mEtLoginMail;
    private EditText mEtLoginPwd;
    private EditText mEtRegistConfimPwd;
    private EditText mEtRegistMail;
    private EditText mEtRegistPwd;
    private EditText mEtRegistUser;
    private EditText mEtResetMail;
    private ImageView mIvLoginMailDismiss;
    private ImageView mIvQuestion;
    private ImageView mIvRegistConfirmPwdDismiss;
    private ImageView mIvRegistMailDismiss;
    private ImageView mIvRegistPwdDismiss;
    private ImageView mIvRegistUserDismiss;
    private ImageView mIvResetMailDismiss;
    private RecyclerView mRecyclerView;
    private TextView tvFollower;
    private TextView tvFollowing;
    private TextView tvRoute;
    private TextView tvUserName;
    private View view;
    private int TAG_LOGIN_DIALOG = 1;
    private int TAG_REGISET_DIALOG = 2;
    private int TAG_RESET_DIALOG = 3;
    private int[] imageRec = {R.drawable.icon_help, R.drawable.icon_setting, R.drawable.icon_mydevices};
    private int[] menuStr = {R.string.setting_help, R.string.setting_setting, R.string.setting_my_devices};

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        fragment = this;
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_main_drawer_menu, (ViewGroup) null);
        initView();
        initEvent();
        this.callbackManager = CallbackManager.Factory.create();
        this.lBtnFaceBookLogin.setFragment(this);
        this.presenter = initPresenter();
        ((LeftDrawerPresenterImp) this.presenter).initFaceBookSdkLogin(this.lBtnFaceBookLogin, this.callbackManager);
        return this.view;
    }

    @Override // android.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.callbackManager.onActivityResult(i, i2, intent);
    }

    private void initView() {
        this.lvFaceBookLogin = (ImageView) this.view.findViewById(R.id.iv_drawer_facebook_login_button);
        this.lBtnFaceBookLogin = (LoginButton) this.view.findViewById(R.id.iv_drawer_facebook_login);
        this.ivWxLogin = (ImageView) this.view.findViewById(R.id.iv_drawer_weixin_login);
        this.mCircleImage = (CircleImageView) this.view.findViewById(R.id.circle_drawer_head);
        this.tvUserName = (TextView) this.view.findViewById(R.id.tv_drawer_username);
        this.mRecyclerView = (RecyclerView) this.view.findViewById(R.id.rec_drawer_menu_list);
        this.loginBefore = this.view.findViewById(R.id.layout_loginblock);
        this.loginAfter = this.view.findViewById(R.id.layout_userinfoblock);
        this.btnLogin = (Button) this.view.findViewById(R.id.btn_drawer_login);
        this.btnRegister = (Button) this.view.findViewById(R.id.btn_drawer_register);
        this.tvRoute = (TextView) this.view.findViewById(R.id.tv_drawer_route);
        this.tvFollower = (TextView) this.view.findViewById(R.id.tv_drawer_follower);
        this.tvFollowing = (TextView) this.view.findViewById(R.id.tv_drawer_following);
        this.linRoute = (LinearLayout) this.view.findViewById(R.id.linear_drawer_route);
        this.linFollower = (LinearLayout) this.view.findViewById(R.id.linear_drawer_follower);
        this.linFollowing = (LinearLayout) this.view.findViewById(R.id.linear_drawer_following);
    }

    private void initEvent() {
        this.lvFaceBookLogin.setOnClickListener(this);
        this.lBtnFaceBookLogin.setOnClickListener(this);
        this.ivWxLogin.setOnClickListener(this);
        this.btnLogin.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);
        this.mCircleImage.setOnClickListener(this);
        this.linRoute.setOnClickListener(this);
        this.linFollower.setOnClickListener(this);
        this.linFollowing.setOnClickListener(this);
    }

    private void initViewState() {
        if (UserMe.getUserToken() == null) {
            this.loginBefore.setVisibility(0);
            this.loginAfter.setVisibility(8);
            return;
        }
        this.loginBefore.setVisibility(8);
        this.loginAfter.setVisibility(0);
        UserMe user = UserMe.getUser();
        if (user == null) {
            return;
        }
        Glide.with(getActivity()).load(user.getAvatar_url()).into(this.mCircleImage);
        int iIntValue = Integer.valueOf(user.getFollowing_count()).intValue();
        int iIntValue2 = Integer.valueOf(user.getFollowers_count()).intValue();
        if (iIntValue >= 1000) {
            this.tvFollowing.setText(String.format("%.2f", Double.valueOf(((double) iIntValue2) / 1000.0d)) + "K");
        } else {
            this.tvFollowing.setText(user.getFollowing_count());
        }
        if (iIntValue2 >= 1000) {
            this.tvFollower.setText(String.format("%.2f", Double.valueOf(((double) iIntValue) / 1000.0d)) + "K");
        } else {
            this.tvFollower.setText(user.getFollowers_count());
        }
        this.tvUserName.setText(user.getUsername());
        this.tvRoute.setText(user.getRoute_count() + "");
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        initRecyclerViewData();
        initViewState();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initRecyclerViewData() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.imageRec.length; i++) {
            arrayList.add(new DrawerMenuModel(this.imageRec[i], this.menuStr[i]));
        }
        DrawerMenuRecyclerAdapter drawerMenuRecyclerAdapter = new DrawerMenuRecyclerAdapter(getActivity(), arrayList);
        this.adapter = drawerMenuRecyclerAdapter;
        this.mRecyclerView.setAdapter(drawerMenuRecyclerAdapter);
        this.adapter.setOnItemClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.ivWxLogin)) {
            IWXAPI wxApi = MyApplication.getWxApi();
            if (!wxApi.isWXAppInstalled()) {
                ViewUtils.showSnackBar((View) this.ivWxLogin, R.string.you_need_install_wx, true);
                return;
            }
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            wxApi.sendReq(req);
            return;
        }
        if (view.equals(this.lvFaceBookLogin)) {
            LoginManager.getInstance().logOut();
            try {
                this.lBtnFaceBookLogin.performClick();
            } catch (Exception e) {
                ViewUtils.showSnackBar((View) this.lBtnFaceBookLogin, R.string.cannot_using_facebook_login, false);
                e.printStackTrace();
            }
            ViewUtils.showProgressDialog(R.string.loading);
            return;
        }
        if (view.equals(this.btnLogin)) {
            showLogin();
            return;
        }
        if (view.equals(this.btnRegister)) {
            showRegister();
            return;
        }
        if (view.equals(this.mCircleImage)) {
            startActivity(new Intent(getActivity(), (Class<?>) AccountActivity.class));
            return;
        }
        if (view.equals(this.linFollower)) {
            Intent intent = new Intent(getActivity(), (Class<?>) FriendsActivity.class);
            intent.putExtra("index", 0);
            startActivity(intent);
            return;
        }
        if (view.equals(this.linFollowing)) {
            Intent intent2 = new Intent(getActivity(), (Class<?>) FriendsActivity.class);
            intent2.putExtra("index", 1);
            startActivity(intent2);
            return;
        }
        if (view.equals(this.linRoute)) {
            Intent intent3 = new Intent(getActivity(), (Class<?>) AccountActivity.class);
            intent3.putExtra(NativeProtocol.WEB_DIALOG_ACTION, 1);
            startActivity(intent3);
        }
        if (view.equals(this.mIvQuestion)) {
            AlertDialog alertDialog = this.mDialogLogin;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            this.mBuilderLogin = new AlertDialog.Builder(getActivity());
            View viewInflate = View.inflate(getActivity(), R.layout.item_reset, null);
            this.mDialogContentView = viewInflate;
            this.mEtResetMail = (EditText) viewInflate.findViewById(R.id.et_reset_mail);
            this.mIvResetMailDismiss = (ImageView) this.mDialogContentView.findViewById(R.id.iv_reset_mail_dismiss);
            this.mBtnDialogReset = (Button) this.mDialogContentView.findViewById(R.id.btn_dialog_reset);
            this.mBtnDialogCancle = (Button) this.mDialogContentView.findViewById(R.id.btn_dialog_cancel);
            this.mBuilderLogin.setTitle(R.string.resetpassword);
            this.mBtnDialogReset.setOnClickListener(this);
            this.mBtnDialogCancle.setOnClickListener(this);
            this.mIvResetMailDismiss.setOnClickListener(this);
            this.mIvResetMailDismiss.setVisibility(0);
            this.mIvResetMailDismiss.setEnabled(false);
            this.mEtResetMail.addTextChangedListener(this);
            this.mDialogLogin = this.mBuilderLogin.create();
            this.mEtResetMail.setText(this.mEtLoginMail.getText());
            this.mEtResetMail.setSelection(this.mEtLoginMail.length());
            this.mDialogLogin.setView(this.mDialogContentView);
            this.mDialogLogin.show();
            this.mDialogContentView.setTag(Integer.valueOf(this.TAG_RESET_DIALOG));
            return;
        }
        if (view.equals(this.mBtnDialogLogin)) {
            ViewUtils.showProgressDialog(getString(R.string.loading), getString(R.string.login_loading));
            ((LeftDrawerPresenterImp) this.presenter).signin(this.mEtLoginMail.getText().toString(), this.mEtLoginPwd.getText().toString());
            this.mDialogLogin.dismiss();
            return;
        }
        if (view.equals(this.mBtnDialogFinish)) {
            ((LeftDrawerPresenterImp) this.presenter).registerUser(this.mEtRegistUser.getText().toString(), this.mEtRegistMail.getText().toString(), this.mEtRegistPwd.getText().toString());
            this.mDialogLogin.dismiss();
            return;
        }
        if (view.equals(this.mBtnDialogCancle)) {
            AlertDialog alertDialog2 = this.mDialogLogin;
            if (alertDialog2 == null || !alertDialog2.isShowing()) {
                return;
            }
            this.mDialogLogin.dismiss();
            return;
        }
        if (view.equals(this.mBtnDialogReset)) {
            ((LeftDrawerPresenterImp) this.presenter).resetPassword(this.mEtResetMail.getText().toString());
            this.mDialogLogin.dismiss();
            return;
        }
        if (view.equals(this.mIvRegistConfirmPwdDismiss)) {
            this.mEtRegistConfimPwd.setText("");
            return;
        }
        if (view.equals(this.mIvRegistMailDismiss)) {
            this.mEtRegistMail.setText("");
            return;
        }
        if (view.equals(this.mIvRegistPwdDismiss)) {
            this.mEtRegistPwd.setText("");
        } else if (view.equals(this.mIvRegistUserDismiss)) {
            this.mEtRegistUser.setText("");
        } else if (view.equals(this.mIvLoginMailDismiss)) {
            this.mEtLoginMail.setText("");
        }
    }

    public void showLogin() {
        AlertDialog alertDialog = this.mDialogLogin;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        this.mBuilderLogin = new AlertDialog.Builder(getActivity());
        View viewInflate = View.inflate(getActivity(), R.layout.item_login, null);
        this.mDialogContentView = viewInflate;
        this.mEtLoginMail = (EditText) viewInflate.findViewById(R.id.et_login_mail);
        this.mIvQuestion = (ImageView) this.mDialogContentView.findViewById(R.id.iv_login_question);
        this.mEtLoginPwd = (EditText) this.mDialogContentView.findViewById(R.id.et_login_pwd);
        this.mBtnDialogLogin = (Button) this.mDialogContentView.findViewById(R.id.btn_dialog_login);
        ImageView imageView = (ImageView) this.mDialogContentView.findViewById(R.id.iv_login_mail_dismiss);
        this.mIvLoginMailDismiss = imageView;
        imageView.setOnClickListener(this);
        this.mEtLoginMail.addTextChangedListener(this);
        this.mEtLoginPwd.addTextChangedListener(this);
        this.mIvQuestion.setOnClickListener(this);
        this.mBuilderLogin.setTitle(R.string.login);
        this.mBtnDialogLogin.setOnClickListener(this);
        AlertDialog alertDialogCreate = this.mBuilderLogin.create();
        this.mDialogLogin = alertDialogCreate;
        alertDialogCreate.setView(this.mDialogContentView);
        this.mDialogLogin.show();
        this.mDialogContentView.setTag(Integer.valueOf(this.TAG_LOGIN_DIALOG));
        this.mDialogContentView.removeCallbacks(null);
    }

    public void showRegister() {
        AlertDialog alertDialog = this.mDialogLogin;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        this.mBuilderLogin = new AlertDialog.Builder(getActivity());
        View viewInflate = View.inflate(getActivity(), R.layout.item_regist, null);
        this.mDialogContentView = viewInflate;
        this.mEtRegistMail = (EditText) viewInflate.findViewById(R.id.et_regist_mail);
        this.mEtRegistConfimPwd = (EditText) this.mDialogContentView.findViewById(R.id.et_regist_confirm_pwd);
        this.mEtRegistPwd = (EditText) this.mDialogContentView.findViewById(R.id.et_regist_pwd);
        this.mEtRegistUser = (EditText) this.mDialogContentView.findViewById(R.id.et_regist_user);
        this.mBtnDialogFinish = (Button) this.mDialogContentView.findViewById(R.id.btn_dialog_finish);
        this.mIvRegistConfirmPwdDismiss = (ImageView) this.mDialogContentView.findViewById(R.id.iv_regist_confirm_pwd_dismiss);
        this.mIvRegistMailDismiss = (ImageView) this.mDialogContentView.findViewById(R.id.iv_regist_mail_dismiss);
        this.mIvRegistUserDismiss = (ImageView) this.mDialogContentView.findViewById(R.id.iv_regist_user_dismiss);
        this.mIvRegistPwdDismiss = (ImageView) this.mDialogContentView.findViewById(R.id.iv_regist_pwd_dismiss);
        this.mBtnDialogFinish.setOnClickListener(this);
        this.mIvRegistMailDismiss.setOnClickListener(this);
        this.mIvRegistPwdDismiss.setOnClickListener(this);
        this.mIvRegistUserDismiss.setOnClickListener(this);
        this.mIvRegistConfirmPwdDismiss.setOnClickListener(this);
        this.mEtRegistUser.addTextChangedListener(this);
        this.mEtRegistConfimPwd.addTextChangedListener(this);
        this.mEtRegistPwd.addTextChangedListener(this);
        this.mEtRegistMail.addTextChangedListener(this);
        this.mBuilderLogin.setTitle(R.string.register);
        AlertDialog alertDialogCreate = this.mBuilderLogin.create();
        this.mDialogLogin = alertDialogCreate;
        alertDialogCreate.setView(this.mDialogContentView);
        this.mDialogLogin.show();
        this.mDialogContentView.setTag(Integer.valueOf(this.TAG_REGISET_DIALOG));
    }

    @Override // im.helmsman.helmsmanandroid.adapter.DrawerMenuRecyclerAdapter.OnItemClickListener
    public void onItemClick(View view, int i) {
        if (i == 0) {
            startActivity(new Intent(getActivity(), (Class<?>) InstructionsActivity.class));
        } else if (i == 1) {
            startActivity(new Intent(getActivity(), (Class<?>) SettingActivity.class));
        } else {
            if (i != 2) {
                return;
            }
            startActivity(new Intent(getActivity(), (Class<?>) BoatListActivity.class));
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        int iIntValue = this.mDialogContentView.getTag() != null ? ((Integer) this.mDialogContentView.getTag()).intValue() : 0;
        if (this.TAG_LOGIN_DIALOG == iIntValue) {
            if (!this.mEtLoginMail.getText().toString().equals("")) {
                this.mIvLoginMailDismiss.setVisibility(0);
            } else {
                this.mIvLoginMailDismiss.setVisibility(8);
            }
            if (Patterns.EMAIL_ADDRESS.matcher(this.mEtLoginMail.getText()).matches()) {
                this.mIvLoginMailDismiss.setImageResource(R.drawable.icon_login_correct);
                this.mIvLoginMailDismiss.setEnabled(false);
                if (!this.mEtLoginPwd.getText().toString().equals("") && this.mEtLoginPwd.getText().length() >= 8) {
                    this.mBtnDialogLogin.setEnabled(true);
                    return;
                }
            } else {
                this.mIvLoginMailDismiss.setImageResource(R.drawable.icon_login_dismiss);
                this.mIvLoginMailDismiss.setEnabled(true);
            }
            this.mBtnDialogLogin.setEnabled(false);
            return;
        }
        if (this.TAG_REGISET_DIALOG == iIntValue) {
            String string = this.mEtRegistMail.getText().toString();
            this.mBtnDialogFinish.setEnabled(true);
            if (!this.mEtRegistMail.getText().toString().equals("")) {
                this.mIvRegistMailDismiss.setVisibility(0);
            } else {
                this.mIvRegistMailDismiss.setVisibility(8);
            }
            Pattern patternCompile = Pattern.compile("[A-Z]");
            if (Patterns.EMAIL_ADDRESS.matcher(string).matches() && !patternCompile.matcher(string).find()) {
                this.mIvRegistMailDismiss.setImageResource(R.drawable.icon_login_correct);
                this.mIvRegistMailDismiss.setEnabled(false);
            } else {
                this.mIvRegistMailDismiss.setImageResource(R.drawable.icon_login_dismiss);
                this.mIvRegistMailDismiss.setEnabled(true);
                this.mBtnDialogFinish.setEnabled(false);
            }
            if (!this.mEtRegistPwd.getText().toString().equals("")) {
                this.mIvRegistPwdDismiss.setVisibility(0);
            } else {
                this.mIvRegistPwdDismiss.setVisibility(8);
            }
            if (this.mEtRegistPwd.getText().length() >= 8 && StringUtils.isHaveSpecialCharacters(this.mEtRegistPwd.getText().toString())) {
                this.mIvRegistPwdDismiss.setImageResource(R.drawable.icon_login_correct);
                this.mIvRegistPwdDismiss.setEnabled(false);
            } else {
                this.mIvRegistPwdDismiss.setImageResource(R.drawable.icon_login_dismiss);
                this.mIvRegistPwdDismiss.setEnabled(true);
                this.mBtnDialogFinish.setEnabled(false);
            }
            if (!this.mEtRegistConfimPwd.getText().toString().equals("")) {
                this.mIvRegistConfirmPwdDismiss.setVisibility(0);
            } else {
                this.mIvRegistConfirmPwdDismiss.setVisibility(8);
            }
            if (this.mEtRegistConfimPwd.getText().length() >= 8 && this.mEtRegistConfimPwd.getText().toString().equals(this.mEtRegistPwd.getText().toString()) && StringUtils.isHaveSpecialCharacters(this.mEtRegistConfimPwd.getText().toString())) {
                this.mIvRegistConfirmPwdDismiss.setImageResource(R.drawable.icon_login_correct);
                this.mIvRegistConfirmPwdDismiss.setEnabled(false);
            } else {
                this.mIvRegistConfirmPwdDismiss.setImageResource(R.drawable.icon_login_dismiss);
                this.mIvRegistConfirmPwdDismiss.setEnabled(true);
                this.mBtnDialogFinish.setEnabled(false);
            }
            if (!"".equals(this.mEtRegistUser.getText().toString())) {
                this.mIvRegistUserDismiss.setVisibility(0);
                if (StringUtils.checkUserName(this.mEtRegistUser.getText().toString())) {
                    this.mIvRegistUserDismiss.setImageResource(R.drawable.icon_login_correct);
                    return;
                } else {
                    this.mIvRegistUserDismiss.setImageResource(R.drawable.icon_login_dismiss);
                    return;
                }
            }
            this.mIvRegistUserDismiss.setVisibility(8);
            return;
        }
        if (this.TAG_RESET_DIALOG == iIntValue) {
            if (!this.mEtResetMail.getText().toString().equals("")) {
                this.mIvResetMailDismiss.setVisibility(0);
            } else {
                this.mIvResetMailDismiss.setVisibility(8);
            }
            if (Patterns.EMAIL_ADDRESS.matcher(this.mEtResetMail.getText()).matches()) {
                this.mIvResetMailDismiss.setImageResource(R.drawable.icon_login_correct);
                this.mIvResetMailDismiss.setEnabled(false);
                this.mBtnDialogReset.setEnabled(true);
            } else {
                this.mIvResetMailDismiss.setImageResource(R.drawable.icon_login_dismiss);
                this.mIvResetMailDismiss.setEnabled(true);
                this.mBtnDialogReset.setEnabled(false);
            }
        }
    }

    public static MainDrawerMenuFragment getInstance() {
        return fragment;
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showRegisterSuccessMessage() {
        new RegisterSuccessInfoDialogFragment().show(getFragmentManager(), "registersuccess");
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showRegisterErrorMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.drawer_register_error) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showResetPasswordSuccess() {
        ViewUtils.ShowToast(getString(R.string.drawer_resetpassword_success));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showResetPasswordErrorMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.drawer_resetpassword_error) + str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showLoginSuccessMessage() {
        ViewUtils.ShowToast(R.string.loginsuccess);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void startAccountActivity() {
        startActivity(new Intent(getActivity(), (Class<?>) AccountActivity.class));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showLoginErrorMessage(String str) {
        ViewUtils.ShowToast(getString(R.string.loginerror));
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showFaceBookCancleMessage() {
        ViewUtils.ShowToast(R.string.user_cancle);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.LeftDrawerView
    public void showFaceBookError(String str) {
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment
    public LeftDrawerPresenterImp initPresenter() {
        return new LeftDrawerPresenterImp();
    }
}
