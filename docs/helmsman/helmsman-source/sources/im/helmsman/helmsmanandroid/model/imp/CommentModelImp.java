package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.inet.model.CommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.DeleteCommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.model.CommentModel;
import im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class CommentModelImp implements CommentModel {
    private OnCommentListener onCommentListener;

    public CommentModelImp(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }

    @Override // im.helmsman.helmsmanandroid.model.CommentModel
    public void sendComment(int i, String str) {
        RequestInetUtils.instance().sendComment(i, str, new Callback<CommentResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.CommentModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<CommentResultModel> response) {
                CommentModelImp.this.onCommentListener.onSendCommentSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                CommentModelImp.this.onCommentListener.onSendCommentFailed(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.CommentModel
    public void getCommentListByMessageId(int i, int i2, int i3) {
        RequestInetUtils.instance().getAllCommentByMessageId(i, i2, i3, new Callback<CommentListModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.CommentModelImp.2
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<CommentListModel> response) {
                CommentModelImp.this.onCommentListener.onGetCommentListSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                CommentModelImp.this.onCommentListener.onGetCommentListFaile(errorCode.getMessage());
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.CommentModel
    public void deleteCommentByCommentId(int i) {
        RequestInetUtils.instance().deleteCommentById(i, new Callback<DeleteCommentResultModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.CommentModelImp.3
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<DeleteCommentResultModel> response) {
                CommentModelImp.this.onCommentListener.onDeleteCommentSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                CommentModelImp.this.onCommentListener.onDeleteCommentFaile(errorCode.getMessage());
            }
        });
    }
}
