package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class CommentActivity_ViewBinding implements Unbinder {
    private CommentActivity target;
    private View view7f080192;

    public CommentActivity_ViewBinding(CommentActivity commentActivity) {
        this(commentActivity, commentActivity.getWindow().getDecorView());
    }

    public CommentActivity_ViewBinding(final CommentActivity commentActivity, View view) {
        this.target = commentActivity;
        commentActivity.etCommentComment = (EditText) Utils.findRequiredViewAsType(view, R.id.et_comment_comment, "field 'etCommentComment'", EditText.class);
        View viewFindRequiredView = Utils.findRequiredView(view, R.id.iv_comment_tick, "field 'ivCommentTick' and method 'onClick'");
        commentActivity.ivCommentTick = (ImageView) Utils.castView(viewFindRequiredView, R.id.iv_comment_tick, "field 'ivCommentTick'", ImageView.class);
        this.view7f080192 = viewFindRequiredView;
        viewFindRequiredView.setOnClickListener(new DebouncingOnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.CommentActivity_ViewBinding.1
            @Override // butterknife.internal.DebouncingOnClickListener
            public void doClick(View view2) {
                commentActivity.onClick();
            }
        });
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        CommentActivity commentActivity = this.target;
        if (commentActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        commentActivity.etCommentComment = null;
        commentActivity.ivCommentTick = null;
        this.view7f080192.setOnClickListener(null);
        this.view7f080192 = null;
    }
}
