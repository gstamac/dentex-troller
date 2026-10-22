package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface CommentView extends MvpView {
    void cleanEditText();

    void finishActivity();

    void hidePostProgressBar();

    void refreshRecyclerView(List<CommentListModel.CommentsBean> list);

    void showCommentFailedMessage(String str);

    void showDeleteFailedCommentMsg(String str);

    void showNotCommentMessage();

    void showPostProgressBar();

    void showReSend();
}
