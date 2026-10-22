package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.snackbar.Snackbar;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.CommentListAdapter;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.presenter.CommentPresenterImp;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.CommentView;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentActivity extends Mvp2BaseActivity<CommentView, CommentPresenterImp> implements TextWatcher, CommentView, CommentListAdapter.onItemClickListener {
    private CommentListAdapter adapter;

    @BindView(R.id.et_comment_comment)
    EditText etCommentComment;

    @BindView(R.id.iv_comment_tick)
    ImageView ivCommentTick;
    private int messageId;
    private String name;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void cleanEditText() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void hidePostProgressBar() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.adapter.CommentListAdapter.onItemClickListener
    public void onItemClickListener(CommentListModel.CommentsBean commentsBean) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void showPostProgressBar() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setStatusBar(R.color.red_state);
        Intent intent = getIntent();
        this.messageId = intent.getIntExtra("messageid", 0);
        this.name = intent.getStringExtra("username");
        initView();
        initEvent();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public CommentPresenterImp initPresenter() {
        return new CommentPresenterImp();
    }

    private void initView() {
        if (this.name != null) {
            this.etCommentComment.setHint("Reply:" + this.name);
        }
    }

    private void initEvent() {
        this.etCommentComment.addTextChangedListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void showCommentFailedMessage(String str) {
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void refreshRecyclerView(List<CommentListModel.CommentsBean> list) {
        CommentListAdapter commentListAdapter = new CommentListAdapter(this, list);
        this.adapter = commentListAdapter;
        commentListAdapter.setOnItemClickListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void showReSend() {
        Snackbar.make(this.etCommentComment, "SEND FAILED", -2).setAction("RESEND", new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.CommentActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ((CommentPresenterImp) CommentActivity.this.presenter).sendComment(CommentActivity.this.messageId, CommentActivity.this.etCommentComment.getText().toString());
            }
        }).show();
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void showNotCommentMessage() {
        ViewUtils.ShowToast(R.string.no_message);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void showDeleteFailedCommentMsg(String str) {
        ViewUtils.ShowToast(str);
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.CommentView
    public void finishActivity() {
        finish();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (editable.length() > 0) {
            this.ivCommentTick.setVisibility(0);
        } else {
            this.ivCommentTick.setVisibility(8);
        }
    }

    @OnClick({R.id.iv_comment_tick})
    public void onClick() {
        ((CommentPresenterImp) this.presenter).sendComment(this.messageId, this.etCommentComment.getText().toString());
    }
}
