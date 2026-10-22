package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.inet.model.CommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.DeleteCommentResultModel;

/* JADX INFO: loaded from: classes2.dex */
public interface OnCommentListener {
    void onDeleteCommentFaile(String str);

    void onDeleteCommentSuccess(DeleteCommentResultModel deleteCommentResultModel);

    void onGetCommentListFaile(String str);

    void onGetCommentListSuccess(CommentListModel commentListModel);

    void onSendCommentFailed(String str);

    void onSendCommentSuccess(CommentResultModel commentResultModel);
}
