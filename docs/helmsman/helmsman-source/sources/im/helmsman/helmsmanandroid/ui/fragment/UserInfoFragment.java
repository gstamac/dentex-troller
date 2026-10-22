package im.helmsman.helmsmanandroid.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.juanpabloprado.countrypicker.CountryPickerListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import de.hdodenhof.circleimageview.CircleImageView;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.AccountSecuriytStateResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.UpLoadImageTokenResponse;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import im.helmsman.helmsmanandroid.ui.activity.AccountSecuriytActivity;
import im.helmsman.helmsmanandroid.ui.activity.AddFrinedsActivity;
import im.helmsman.helmsmanandroid.ui.activity.CaptureActivityVertical;
import im.helmsman.helmsmanandroid.ui.activity.FriendsActivity;
import im.helmsman.helmsmanandroid.ui.activity.NotificationSettingActivity;
import im.helmsman.helmsmanandroid.utils.BitMapUtils;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.MyCountryPicaker;
import im.helmsman.helmsmanandroid.view.TitleBar;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class UserInfoFragment extends Fragment implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener, AdapterView.OnItemClickListener {
    public static String ACCONT_INSTRUCTION = "accont_instruction";
    public static final int NONE = 0;
    public static final int NONULL = 2;
    public static final int NULL = 1;
    private static UserInfoFragment instance;
    private String email;
    private boolean emailIsVerified = false;
    private String facebookId;
    private int keyboardHeight;
    private Button mBtnLogout;
    private CircleImageView mCircleImageView;
    private UserMe mCurrentAccountInfo;
    private AlertDialog mDialog;
    private AlertDialog.Builder mDialogBuilder;
    private FragmentManager mFragManager;
    private FrameLayout mFrameChangePassword;
    private FrameLayout mFrameNitification;
    private FrameLayout mFrameQRCode;
    private LinearLayout mLinFollower;
    private LinearLayout mLinFollowing;
    private LinearLayout mLinOverScrollView;
    private LinearLayout mLinRoute;
    private PopupWindow mPopAddFriends;
    private RelativeLayout mRelCountry;
    private RelativeLayout mRelGender;
    private RelativeLayout mRelInstruction;
    private RelativeLayout mRelUser;
    private LinearLayout mRelUserInfo;
    private TextView mTvCountry;
    private TextView mTvFollowerCount;
    private TextView mTvFollowingCount;
    private TextView mTvGender;
    private TextView mTvInstruction;
    private TextView mTvRouteCount;
    private TextView mTvUser;
    private View mViewParent;
    private int passwordState;
    private int statusBarHeight;
    private TitleBar titleBar;
    private String wxUnioId;

    public static UserInfoFragment getInstance() {
        if (instance == null) {
            instance = new UserInfoFragment();
        }
        return instance;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mViewParent = layoutInflater.inflate(R.layout.fragment_user_info, (ViewGroup) null);
        initView();
        initEvent();
        ViewUtils.showProgressDialog(getString(R.string.userinfofragment_loading));
        RequestInetUtils.instance().getUserInfo(new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<UserMe> response) {
                UserInfoFragment.this.mCurrentAccountInfo = response.body();
                UserInfoFragment userInfoFragment = UserInfoFragment.this;
                userInfoFragment.setUserInfo(userInfoFragment.mCurrentAccountInfo);
                ViewUtils.cancelProgressDialog();
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (UserInfoFragment.this.getActivity() != null) {
                    Toast.makeText(UserInfoFragment.this.getActivity(), "获取用户信息失败:" + errorCode.getMessage(), 0).show();
                }
                ViewUtils.cancelProgressDialog();
            }
        });
        RequestInetUtils.instance().getAccountSecuriytState(new Callback<AccountSecuriytStateResultModel>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<AccountSecuriytStateResultModel> response) {
                UserInfoFragment.this.mFrameChangePassword.setEnabled(true);
                AccountSecuriytStateResultModel accountSecuriytStateResultModelBody = response.body();
                UserInfoFragment.this.email = accountSecuriytStateResultModelBody.getEmail();
                UserInfoFragment.this.facebookId = accountSecuriytStateResultModelBody.getFb_id();
                UserInfoFragment.this.wxUnioId = accountSecuriytStateResultModelBody.getWx_union_id();
                UserInfoFragment.this.emailIsVerified = accountSecuriytStateResultModelBody.isEmail_is_verified();
                if (accountSecuriytStateResultModelBody.isPassword_is_null()) {
                    UserInfoFragment.this.passwordState = 1;
                } else {
                    UserInfoFragment.this.passwordState = 2;
                }
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                UserInfoFragment.this.mFrameChangePassword.setEnabled(false);
            }
        });
        return this.mViewParent;
    }

    private void initView() {
        this.mFrameNitification = (FrameLayout) this.mViewParent.findViewById(R.id.fragment_natifiction);
        this.titleBar = (TitleBar) this.mViewParent.findViewById(R.id.titleBar);
        this.mFrameChangePassword = (FrameLayout) this.mViewParent.findViewById(R.id.fragment_changepassword);
        this.mRelUser = (RelativeLayout) this.mViewParent.findViewById(R.id.rel_user_info_user);
        this.mRelCountry = (RelativeLayout) this.mViewParent.findViewById(R.id.rel_user_info_country);
        this.mRelGender = (RelativeLayout) this.mViewParent.findViewById(R.id.rel_user_info_gender);
        this.mBtnLogout = (Button) this.mViewParent.findViewById(R.id.btn_user_info_logout);
        this.mCircleImageView = (CircleImageView) this.mViewParent.findViewById(R.id.civ_user_info);
        this.mTvUser = (TextView) this.mViewParent.findViewById(R.id.tv_user_info_user);
        this.mTvCountry = (TextView) this.mViewParent.findViewById(R.id.tv_user_info_country);
        this.mTvGender = (TextView) this.mViewParent.findViewById(R.id.tv_user_info_gender);
        this.mTvInstruction = (TextView) this.mViewParent.findViewById(R.id.tv_user_info_instruction_content);
        this.mRelInstruction = (RelativeLayout) this.mViewParent.findViewById(R.id.rel_user_info_instruction);
        this.mLinOverScrollView = (LinearLayout) this.mViewParent.findViewById(R.id.lin_user_info_overscroll);
        this.mRelUserInfo = (LinearLayout) this.mViewParent.findViewById(R.id.rel_user_info);
        this.statusBarHeight = getStatusBarHeight(getActivity().getApplicationContext());
        this.mTvFollowerCount = (TextView) this.mViewParent.findViewById(R.id.tv_user_info_follower_count);
        this.mTvFollowingCount = (TextView) this.mViewParent.findViewById(R.id.tv_user_info_following_count);
        this.mTvRouteCount = (TextView) this.mViewParent.findViewById(R.id.tv_user_info_route_count);
        this.mLinRoute = (LinearLayout) this.mViewParent.findViewById(R.id.lin_user_info_route);
        this.mLinFollowing = (LinearLayout) this.mViewParent.findViewById(R.id.lin_user_info_following);
        this.mLinFollower = (LinearLayout) this.mViewParent.findViewById(R.id.lin_user_info_follower);
        this.mFragManager = getFragmentManager();
        this.mFrameQRCode = (FrameLayout) this.mViewParent.findViewById(R.id.frame_user_info_qrcode);
    }

    private void initEvent() {
        this.mFrameNitification.setOnClickListener(this);
        this.titleBar.setRightButtonOnclickListener(this);
        this.mFrameChangePassword.setOnClickListener(this);
        this.mRelUser.setOnClickListener(this);
        this.mRelGender.setOnClickListener(this);
        this.mRelCountry.setOnClickListener(this);
        this.mBtnLogout.setOnClickListener(this);
        this.mCircleImageView.setOnClickListener(this);
        this.mRelInstruction.setOnClickListener(this);
        this.mLinOverScrollView.setOnClickListener(this);
        this.mLinFollowing.setOnClickListener(this);
        this.mLinFollower.setOnClickListener(this);
        this.mLinRoute.setOnClickListener(this);
        this.mRelUserInfo.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.mFrameQRCode.setOnClickListener(this);
    }

    private void showPopupWidnows(View view) {
        ArrayList arrayList = new ArrayList();
        HashMap map = new HashMap();
        map.put("menukey", MyApplication.getInstance().getString(R.string.add_friends));
        map.put("iconkey", Integer.valueOf(R.drawable.icon_search));
        arrayList.add(map);
        HashMap map2 = new HashMap();
        map2.put("menukey", MyApplication.getInstance().getString(R.string.scan_qrcode));
        map2.put("iconkey", Integer.valueOf(R.drawable.icon_user_info_scan));
        arrayList.add(map2);
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindows_layout, (ViewGroup) null);
        ListView listView = (ListView) viewInflate.findViewById(R.id.lv_main_popup);
        PopupWindow popupWindow = new PopupWindow(viewInflate);
        this.mPopAddFriends = popupWindow;
        popupWindow.setFocusable(true);
        this.mPopAddFriends.setBackgroundDrawable(new BitmapDrawable());
        listView.setAdapter((ListAdapter) new SimpleAdapter(getActivity(), arrayList, R.layout.item_mainpopupwindows, new String[]{"menukey", "iconkey"}, new int[]{R.id.tv_item_popupwindows_menutext, R.id.iv_item_popupwindows_menuicon}));
        listView.setOnItemClickListener(this);
        listView.measure(0, 0);
        this.mPopAddFriends.setWidth(listView.getMeasuredWidth());
        this.mPopAddFriends.setHeight(listView.getMeasuredHeight() * 3);
        this.mPopAddFriends.setOutsideTouchable(true);
        this.mPopAddFriends.showAsDropDown(view, 0, -10);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.mFrameNitification)) {
            startActivity(new Intent(getActivity(), (Class<?>) NotificationSettingActivity.class));
            return;
        }
        if (view.getId() == this.titleBar.getRightViewId()) {
            showPopupWidnows(view);
            return;
        }
        if (view.equals(this.mFrameChangePassword)) {
            Intent intent = new Intent(getActivity(), (Class<?>) AccountSecuriytActivity.class);
            intent.putExtra("passwordstate", this.passwordState);
            intent.putExtra("wxunioid", this.wxUnioId);
            intent.putExtra("facebookid", this.facebookId);
            intent.putExtra("email", this.email);
            intent.putExtra("emailIsVerified", this.emailIsVerified);
            startActivity(intent);
            return;
        }
        if (view.equals(this.mFrameQRCode)) {
            if (this.mCurrentAccountInfo != null) {
                QRCodeDialogFragment qRCodeDialogFragment = new QRCodeDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(QRCodeDialogFragment.QRCodeUrl, "http://helmsman.im/users/me/follow?id=" + this.mCurrentAccountInfo.getId());
                qRCodeDialogFragment.setArguments(bundle);
                qRCodeDialogFragment.show(getFragmentManager(), "QRCodeDialogFragment");
                return;
            }
            return;
        }
        if (view.equals(this.mLinRoute)) {
            FragmentTransaction fragmentTransactionBeginTransaction = this.mFragManager.beginTransaction();
            MyShareRouteFragment myShareRouteFragment = new MyShareRouteFragment();
            fragmentTransactionBeginTransaction.addToBackStack(null);
            fragmentTransactionBeginTransaction.hide(this);
            fragmentTransactionBeginTransaction.add(R.id.frame_account_base, myShareRouteFragment);
            fragmentTransactionBeginTransaction.commit();
            return;
        }
        if (view.equals(this.mLinFollower)) {
            Intent intent2 = new Intent(getActivity(), (Class<?>) FriendsActivity.class);
            intent2.putExtra("index", 0);
            startActivity(intent2);
            return;
        }
        if (view.equals(this.mLinFollowing)) {
            Intent intent3 = new Intent(getActivity(), (Class<?>) FriendsActivity.class);
            intent3.putExtra("index", 1);
            startActivity(intent3);
            return;
        }
        if (view.equals(this.mRelUser)) {
            this.mDialogBuilder = new AlertDialog.Builder(getActivity());
            View viewInflate = View.inflate(getActivity(), R.layout.item_account_dialog_edit_user, null);
            final EditText editText = (EditText) viewInflate.findViewById(R.id.et_account_dialog_edit_user);
            final TextView textView = (TextView) viewInflate.findViewById(R.id.tv_account_dialog_format);
            Button button = (Button) viewInflate.findViewById(R.id.btn_account_dialog_cancle);
            Button button2 = (Button) viewInflate.findViewById(R.id.btn_account_dialog_positive);
            final ImageView imageView = (ImageView) viewInflate.findViewById(R.id.iv_account_dialog_user_dismiss);
            imageView.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    editText.setText("");
                    view2.setVisibility(8);
                }
            });
            String string = this.mTvUser.getText().toString();
            if (!"".equals(string)) {
                editText.setText(string);
                editText.setSelection(string.length());
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
            button.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    UserInfoFragment.this.mDialog.dismiss();
                }
            });
            button2.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.5
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    String string2 = editText.getText().toString();
                    if (string2.length() < 5 || !StringUtils.isHaveSpecialCharacters(string2) || string2.length() > 10) {
                        textView.setVisibility(0);
                        return;
                    }
                    textView.setVisibility(8);
                    UserInfoFragment.this.mDialog.dismiss();
                    UserInfoFragment.this.saveUserNameAndUpload(string2);
                }
            });
            editText.addTextChangedListener(new TextWatcher() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.6
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    textView.setVisibility(8);
                    if (!"".equals(editText.getText().toString())) {
                        imageView.setVisibility(0);
                    } else {
                        imageView.setVisibility(8);
                    }
                }
            });
            this.mDialogBuilder.setTitle(R.string.userinfofragment_editusername);
            AlertDialog alertDialogCreate = this.mDialogBuilder.create();
            this.mDialog = alertDialogCreate;
            alertDialogCreate.setView(viewInflate);
            this.mDialog.show();
            return;
        }
        if (view.equals(this.mRelCountry)) {
            MyCountryPicaker.getInstance("Select Country", new CountryPickerListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.7
                @Override // com.juanpabloprado.countrypicker.CountryPickerListener
                public void onSelectCountry(String str, String str2) {
                    ((DialogFragment) UserInfoFragment.this.getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker")).dismiss();
                    UserInfoFragment.this.saveCountryAndUpload(str2);
                }
            }).show(getFragmentManager(), "CountryPicker");
            return;
        }
        if (view.equals(this.mRelGender)) {
            this.mDialogBuilder = new AlertDialog.Builder(getContext());
            View viewInflate2 = View.inflate(getContext(), R.layout.item_account_dialog_edit_gender, null);
            TextView textView2 = (TextView) viewInflate2.findViewById(R.id.tv_account_gender_man);
            TextView textView3 = (TextView) viewInflate2.findViewById(R.id.tv_account_gender_women);
            TextView textView4 = (TextView) viewInflate2.findViewById(R.id.tv_account_gender_unset);
            textView2.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.8
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    UserInfoFragment.this.mDialog.dismiss();
                    UserInfoFragment.this.saveGenderAndUpload(1);
                }
            });
            textView3.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.9
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    UserInfoFragment.this.mDialog.dismiss();
                    UserInfoFragment.this.saveGenderAndUpload(2);
                }
            });
            textView4.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.10
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    UserInfoFragment.this.mDialog.dismiss();
                    UserInfoFragment.this.saveGenderAndUpload(0);
                    UserInfoFragment.this.mTvGender.setText(R.string.secrecy);
                    UserInfoFragment.this.mDialog.dismiss();
                }
            });
            AlertDialog alertDialogCreate2 = this.mDialogBuilder.create();
            this.mDialog = alertDialogCreate2;
            alertDialogCreate2.setView(viewInflate2);
            this.mDialog.show();
            return;
        }
        if (view.equals(this.mBtnLogout)) {
            UserMe.loginOut();
            RequestInetUtils.instance().clearAliasName();
            getActivity().finish();
            return;
        }
        if (view.equals(this.mCircleImageView)) {
            Intent intent4 = new Intent("android.intent.action.GET_CONTENT", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent4.setType("image/*");
            startActivityForResult(intent4, 100);
        } else if (view.equals(this.mRelInstruction)) {
            FragmentTransaction fragmentTransactionBeginTransaction2 = this.mFragManager.beginTransaction();
            UserInstructionFragment userInstructionFragment = new UserInstructionFragment();
            userInstructionFragment.setTargetFragment(this, 2);
            Bundle bundle2 = new Bundle();
            bundle2.putString(ACCONT_INSTRUCTION, this.mTvInstruction.getText().toString());
            userInstructionFragment.setArguments(bundle2);
            fragmentTransactionBeginTransaction2.addToBackStack(null);
            fragmentTransactionBeginTransaction2.hide(this);
            fragmentTransactionBeginTransaction2.add(R.id.frame_account_base, userInstructionFragment);
            fragmentTransactionBeginTransaction2.commit();
        }
    }

    private void saveInstructionAndUpload(String str) {
        UserMe userMe = this.mCurrentAccountInfo;
        if (userMe != null) {
            userMe.setAbout_me(str);
            ViewUtils.showProgressDialog(getString(R.string.addroute_saving_route));
            RequestInetUtils.instance().updateUserInfo(this.mCurrentAccountInfo, new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.11
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<UserMe> response) {
                    UserInfoFragment.this.mTvInstruction.setText(response.body().getAbout_me());
                    ViewUtils.cancelProgressDialog();
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    Toast.makeText(UserInfoFragment.this.getActivity(), "保存失败:" + errorCode.getMessage(), 0).show();
                    ViewUtils.cancelProgressDialog();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCountryAndUpload(String str) {
        UserMe userMe = this.mCurrentAccountInfo;
        if (userMe != null) {
            userMe.setCountry_code(str);
            ViewUtils.showProgressDialog(getString(R.string.addroute_saving_route));
            RequestInetUtils.instance().updateUserInfo(this.mCurrentAccountInfo, new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.12
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<UserMe> response) {
                    UserInfoFragment.this.mTvCountry.setText(StringUtils.getCountryNameByCode(response.body().getCountry_code()));
                    ViewUtils.cancelProgressDialog();
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    Toast.makeText(UserInfoFragment.this.getActivity(), "保存失败:" + errorCode.getMessage(), 0).show();
                    ViewUtils.cancelProgressDialog();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveGenderAndUpload(int i) {
        UserMe userMe = this.mCurrentAccountInfo;
        if (userMe != null) {
            if (TextUtils.isEmpty(userMe.getAbout_me())) {
                this.mCurrentAccountInfo.setAbout_me(" ");
            }
            this.mCurrentAccountInfo.setSex(i);
            ViewUtils.showProgressDialog(getString(R.string.addroute_saving_route));
            RequestInetUtils.instance().updateUserInfo(this.mCurrentAccountInfo, new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.13
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<UserMe> response) {
                    ViewUtils.cancelProgressDialog();
                    int sex = response.body().getSex();
                    if (sex == 1) {
                        UserInfoFragment.this.mTvGender.setText(R.string.male);
                    } else if (sex == 2) {
                        UserInfoFragment.this.mTvGender.setText(R.string.female);
                    }
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    ViewUtils.cancelProgressDialog();
                    Toast.makeText(UserInfoFragment.this.getActivity(), UserInfoFragment.this.getString(R.string.save_failed), 0).show();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveUserNameAndUpload(String str) {
        UserMe userMe = this.mCurrentAccountInfo;
        if (userMe != null) {
            userMe.setUsername(str);
            ViewUtils.showProgressDialog(getString(R.string.addroute_saving_route));
            RequestInetUtils.instance().updateUserInfo(this.mCurrentAccountInfo, new Callback<UserMe>() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.14
                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onResponse(Response<UserMe> response) {
                    ViewUtils.cancelProgressDialog();
                    UserInfoFragment.this.mTvUser.setText(response.body().getUsername());
                }

                @Override // im.helmsman.helmsmanandroid.inet.Callback
                public void onFailure(ErrorCode errorCode) {
                    ViewUtils.cancelProgressDialog();
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 2) {
            if (intent != null) {
                saveInstructionAndUpload(intent.getStringExtra(UserInstructionFragment.MY_INSTRUCTION_CONTENT));
            }
        } else if (i == 100) {
            BitMapUtils.getPhotoURLByAlbum(getContext(), intent, new BitMapUtils.PhotoCallBack() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.15
                @Override // im.helmsman.helmsmanandroid.utils.BitMapUtils.PhotoCallBack
                public void onSuccess(String str) throws Throwable {
                    if (TextUtils.isEmpty(str)) {
                        onFailure();
                    } else {
                        final File fileScal = BitMapUtils.scal(Uri.parse(str));
                        RequestInetUtils.instance().uploadImage(fileScal.getPath(), (Callback<UpLoadImageTokenResponse>) null, new UpCompletionHandler() { // from class: im.helmsman.helmsmanandroid.ui.fragment.UserInfoFragment.15.1
                            @Override // com.qiniu.android.storage.UpCompletionHandler
                            public void complete(String str2, ResponseInfo responseInfo, JSONObject jSONObject) {
                                ViewUtils.ShowToast(R.string.main_success);
                                Log.e("Thread", Thread.currentThread().getName());
                                Glide.with(UserInfoFragment.this.getContext()).load(fileScal).apply((BaseRequestOptions<?>) new RequestOptions().override(ViewUtils.dpToPx(50), ViewUtils.dpToPx(50))).into(UserInfoFragment.this.mCircleImageView);
                            }
                        });
                    }
                }

                @Override // im.helmsman.helmsmanandroid.utils.BitMapUtils.PhotoCallBack
                public void onFailure() {
                    ViewUtils.showSnackBar((View) UserInfoFragment.this.mCircleImageView, R.string.picture_format_err, true);
                }
            });
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (i == 0) {
            startActivity(new Intent(getActivity(), (Class<?>) AddFrinedsActivity.class));
        } else if (i == 1) {
            if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") != 0) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CAMERA"}, 0);
            } else {
                startActivity(new Intent(getActivity(), (Class<?>) CaptureActivityVertical.class));
            }
        }
        PopupWindow popupWindow = this.mPopAddFriends;
        if (popupWindow == null || !popupWindow.isShowing()) {
            return;
        }
        this.mPopAddFriends.dismiss();
    }

    private void onShowKeyboard() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            Window window = alertDialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.gravity = 48;
            attributes.verticalMargin = 0.05f;
            window.setAttributes(attributes);
        }
    }

    private void onHideKeyboard() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            Window window = alertDialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.verticalMargin = 0.0f;
            attributes.gravity = 17;
            window.setAttributes(attributes);
        }
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        int i;
        Rect rect = new Rect();
        this.mRelUserInfo.getWindowVisibleDisplayFrame(rect);
        int height = (this.mRelUserInfo.getRootView().getHeight() - (rect.bottom - rect.top)) - ViewUtils.getNavigationBarHeight(MyApplication.getContext());
        if (this.keyboardHeight == 0 && height > (i = this.statusBarHeight)) {
            this.keyboardHeight = height - i;
        }
        if (height <= this.statusBarHeight) {
            onHideKeyboard();
        }
        if (height > this.statusBarHeight) {
            onShowKeyboard();
        }
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setUserInfo(UserMe userMe) {
        if (userMe != null) {
            String username = userMe.getUsername();
            int sex = userMe.getSex();
            String country_code = userMe.getCountry_code();
            String about_me = userMe.getAbout_me();
            String followers_count = userMe.getFollowers_count();
            String following_count = userMe.getFollowing_count();
            String avatar_url = userMe.getAvatar_url();
            int route_count = userMe.getRoute_count();
            if (username != null) {
                this.mTvUser.setText(username);
            }
            if (username != null) {
                this.mTvUser.setText(username);
            }
            if (sex == 1) {
                this.mTvGender.setText(R.string.male);
            } else if (sex == 2) {
                this.mTvGender.setText(R.string.female);
            } else {
                this.mTvGender.setText(R.string.secrecy);
            }
            if (country_code == null) {
                this.mTvCountry.setText("");
            } else {
                this.mTvCountry.setText(StringUtils.getCountryNameByCode(country_code));
            }
            if (about_me != null) {
                this.mTvInstruction.setText(about_me);
            }
            if (following_count != null) {
                this.mTvFollowingCount.setText(following_count);
            }
            if (followers_count != null) {
                this.mTvFollowerCount.setText(followers_count);
            }
            if (avatar_url != null && getContext() != null) {
                Glide.with(getContext()).load(avatar_url).apply((BaseRequestOptions<?>) new RequestOptions().override(ViewUtils.dpToPx(50), ViewUtils.dpToPx(50))).into(this.mCircleImageView);
            }
            this.mTvRouteCount.setText("" + route_count);
        }
    }
}
