package im.helmsman.helmsmanandroid.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.LikeRouteSucessMessage;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SendCommentSuccessMessage;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.CommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteModel;
import im.helmsman.helmsmanandroid.presenter.SendMessagePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.SendMessageView;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.helmsmanandroid.view.TitleBar;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class SendMessageActivity extends Mvp2BaseActivity<SendMessageView, SendMessagePresenter> implements TextWatcher, SendMessageView {
    public static final short COMMENT_MODEL = 3;
    public static final short LIKE_MODEL = 1;
    public static final short REPLY_MODEL = 4;
    public static final short SHARE_MODEL = 2;
    private int commentMessageid;

    @BindView(R.id.et_share_description)
    EditText etShareDescription;
    private short model;
    private ProgressDialog pd;

    @BindView(R.id.rel_send_message_at)
    RelativeLayout relSendMessageAt;
    private int routeId;
    private String routeName;

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @BindView(R.id.iv_send)
    ImageView tvSend;

    @BindView(R.id.tv_send_message_text_count)
    TextView tvSendMessageTextCount;
    private String userName;
    private RouteModel wayPointList;
    private int likeUpdatePosition = -1;
    private int commentPosition = -1;
    private int beforeTextCount = 0;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public SendMessagePresenter initPresenter() {
        return new SendMessagePresenter();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_share_edit_description);
        setStatusBar(R.color.red_state);
        initModel();
        initView();
        initEvent();
    }

    private void initEvent() {
        this.etShareDescription.addTextChangedListener(this);
    }

    private void initModel() {
        Intent intent = getIntent();
        short shortExtra = intent.getShortExtra("model", (short) 1);
        this.model = shortExtra;
        if (shortExtra == 1) {
            this.routeId = intent.getIntExtra("routeid", 0);
            this.likeUpdatePosition = intent.getIntExtra("likeUpdatePosition", -1);
            return;
        }
        if (shortExtra == 2) {
            int intExtra = intent.getIntExtra("routeId", 0);
            this.routeName = intent.getStringExtra("routename");
            this.wayPointList = DaoUtil.getInstance().getWayPointListById(intExtra);
        } else {
            if (shortExtra != 3) {
                if (shortExtra != 4) {
                    return;
                } else {
                    this.userName = intent.getStringExtra("username");
                }
            }
            this.commentMessageid = intent.getIntExtra("messageid", 0);
            this.commentPosition = intent.getIntExtra("commentPosition", -1);
        }
    }

    @OnClick({R.id.iv_send, R.id.rel_send_message_at})
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.iv_send) {
            if (id != R.id.rel_send_message_at) {
                return;
            }
            atFriends();
            return;
        }
        String string = this.etShareDescription.getText().toString();
        if (TextUtils.isEmpty(string)) {
            ViewUtils.ShowToast(R.string.failure_to_fill_in_comments);
            return;
        }
        ((SendMessagePresenter) this.presenter).saveHistory(string);
        short s = this.model;
        if (s == 1) {
            RequestInetUtils.instance().likeRouteById(this.routeId, string, new LikeMessageListener(this.likeUpdatePosition));
            this.pd = ProgressDialog.show(this, getString(R.string.shareeditdescriptionactivity_likeing), getString(R.string.pulltorefreshlistadapter_pleasewait));
            return;
        }
        if (s == 2) {
            if (this.wayPointList == null) {
                ViewUtils.ShowToast(R.string.route_no_route);
                return;
            }
            if (TextUtils.isEmpty(this.routeName)) {
                this.routeName = getString(R.string.edit_no_name);
            }
            RequestInetUtils.instance().shareMyWayPoint(this.routeName, string, this.wayPointList, new ShareWayPointListener());
            this.pd = ProgressDialog.show(this, getString(R.string.shareeditdescriptionactivity_shareing), getString(R.string.pulltorefreshlistadapter_pleasewait));
            return;
        }
        if (s == 3) {
            RequestInetUtils.instance().sendComment(this.commentMessageid, string, new SendCommentListener(this.commentPosition));
            this.pd = ProgressDialog.show(this, getString(R.string.shareeditdescriptionactivity_sendcomment), getString(R.string.pulltorefreshlistadapter_pleasewait));
        } else {
            if (s != 4) {
                return;
            }
            RequestInetUtils.instance().sendComment(this.commentMessageid, string, new SendCommentListener(this.commentPosition));
            this.pd = ProgressDialog.show(this, getString(R.string.shareeditdescriptionactivity_reply), getString(R.string.pulltorefreshlistadapter_pleasewait));
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        Log.e("afterTextChanged", editable.length() + "afterTextChanged" + this.beforeTextCount);
        this.tvSendMessageTextCount.setText(editable.length() + " / 140");
        if (editable.length() > 0 && editable.length() > this.beforeTextCount) {
            if ("@".equals(editable.charAt(editable.length() - 1) + "")) {
                atFriends();
            }
            this.beforeTextCount = editable.length();
            this.etShareDescription.setText(StringUtils.userNameToBlueColor(editable.toString()));
            this.etShareDescription.setSelection(editable.length());
        }
        this.beforeTextCount = editable.length();
        if (this.model == 3) {
            if (editable.length() > 0) {
                this.tvSend.setEnabled(true);
            } else {
                this.tvSend.setEnabled(false);
            }
        }
    }

    class ShareWayPointListener extends Callback {
        ShareWayPointListener() {
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onResponse(Response response) {
            Log.e("response", response.body().toString());
            ViewUtils.ShowToast(R.string.shareeditdescriptionactivity_sharecompleted);
            SendMessageActivity.this.pd.dismiss();
            SendMessageActivity.this.onBackPressed();
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onFailure(ErrorCode errorCode) {
            ViewUtils.ShowToast(SendMessageActivity.this.getString(R.string.shareeditdescriptionactivity_sharefailed));
            SendMessageActivity.this.pd.dismiss();
        }
    }

    class LikeMessageListener extends Callback<LikeRouteModel> {
        private final int listdataPosition;

        public LikeMessageListener(int i) {
            this.listdataPosition = i;
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onResponse(Response<LikeRouteModel> response) {
            LikeRouteSucessMessage likeRouteSucessMessage = new LikeRouteSucessMessage();
            likeRouteSucessMessage.setPosition(this.listdataPosition);
            likeRouteSucessMessage.setLikeCount(response.body().getRoute_favourite_count());
            EventBus.getDefault().post(likeRouteSucessMessage);
            ViewUtils.ShowToast(R.string.like_message_success);
            SendMessageActivity.this.pd.dismiss();
            SendMessageActivity.this.finish();
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onFailure(ErrorCode errorCode) {
            ViewUtils.ShowToast(R.string.like_message_error);
            SendMessageActivity.this.pd.dismiss();
        }
    }

    class SendCommentListener extends Callback<CommentResultModel> {
        private final int position;

        public SendCommentListener(int i) {
            this.position = i;
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onResponse(Response<CommentResultModel> response) {
            SendMessageActivity.this.etShareDescription.setText("");
            SendCommentSuccessMessage sendCommentSuccessMessage = new SendCommentSuccessMessage();
            sendCommentSuccessMessage.setListdataPositionl(this.position);
            EventBus.getDefault().post(sendCommentSuccessMessage);
            SendMessageActivity.this.pd.dismiss();
            SendMessageActivity.this.finish();
        }

        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onFailure(ErrorCode errorCode) {
            ViewUtils.ShowToast(R.string.comment_failed);
            SendMessageActivity.this.pd.dismiss();
        }
    }

    private void initView() {
        short s = this.model;
        if (s == 1) {
            this.titleBar.setCenterViewText(R.string.like);
            return;
        }
        if (s == 2) {
            this.titleBar.setCenterViewText(R.string.share);
            return;
        }
        if (s == 3) {
            this.titleBar.setCenterViewText(R.string.comment_title);
            return;
        }
        if (s != 4) {
            return;
        }
        this.titleBar.setCenterViewText(R.string.reply);
        this.etShareDescription.setText(StringUtils.userNameToBlueColor(getString(R.string.reply_hint) + "@" + this.userName + " "));
        EditText editText = this.etShareDescription;
        editText.setSelection(editText.getText().length());
    }

    private void atFriends() {
        startActivityForResult(new Intent(this, (Class<?>) AtFriendsActivity.class), 1);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        String str;
        super.onActivityResult(i, i2, intent);
        if (intent == null) {
            return;
        }
        String string = this.etShareDescription.getText().toString();
        String str2 = string.length() > 0 ? string.charAt(string.length() - 1) + "" : "";
        String stringExtra = intent.getStringExtra("username");
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        if (str2.equals("@")) {
            str = string + stringExtra + " ";
        } else {
            str = string + "@" + stringExtra + " ";
        }
        this.etShareDescription.setText(str);
        this.etShareDescription.setSelection(str.length());
    }
}
