package im.helmsman.helmsmanandroid.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import im.helmsman.helmsmanandroid.R;

/* JADX INFO: loaded from: classes2.dex */
public class CommentContentActivity_ViewBinding implements Unbinder {
    private CommentContentActivity target;

    public CommentContentActivity_ViewBinding(CommentContentActivity commentContentActivity) {
        this(commentContentActivity, commentContentActivity.getWindow().getDecorView());
    }

    public CommentContentActivity_ViewBinding(CommentContentActivity commentContentActivity, View view) {
        this.target = commentContentActivity;
        commentContentActivity.ivCommentContentMap = (ImageView) Utils.findRequiredViewAsType(view, R.id.iv_comment_content_map, "field 'ivCommentContentMap'", ImageView.class);
        commentContentActivity.tvCommentContentUsername = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_comment_content_username, "field 'tvCommentContentUsername'", TextView.class);
        commentContentActivity.tvCommentContentContent = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_comment_content_content, "field 'tvCommentContentContent'", TextView.class);
        commentContentActivity.tvCommentContentTime = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_comment_content_time, "field 'tvCommentContentTime'", TextView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        CommentContentActivity commentContentActivity = this.target;
        if (commentContentActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        commentContentActivity.ivCommentContentMap = null;
        commentContentActivity.tvCommentContentUsername = null;
        commentContentActivity.tvCommentContentContent = null;
        commentContentActivity.tvCommentContentTime = null;
    }
}
