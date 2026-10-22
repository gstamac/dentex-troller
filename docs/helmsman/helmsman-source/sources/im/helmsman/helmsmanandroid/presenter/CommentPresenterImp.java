package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.eventbusmessagemodel.SendCommentSuccessMessage;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.inet.model.CommentResultModel;
import im.helmsman.helmsmanandroid.inet.model.DeleteCommentResultModel;
import im.helmsman.helmsmanandroid.model.CommentModel;
import im.helmsman.helmsmanandroid.model.imp.CommentModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener;
import im.helmsman.helmsmanandroid.ui.view.CommentView;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* JADX INFO: loaded from: classes2.dex */
public class CommentPresenterImp extends BasePresenter<CommentView> implements OnCommentListener {
    private int messageId;
    private int page = 1;
    private int sinceId = 0;
    private List<CommentListModel.CommentsBean> commentsBeens = new ArrayList();
    private CommentModel commentModel = new CommentModelImp(this);

    public void sendComment(int i, String str) {
        if (str == null || str.equals("")) {
            ((CommentView) this.view).showCommentFailedMessage(MyApplication.getInstance().getString(R.string.comment_cannot_null));
        } else {
            this.commentModel.sendComment(i, str);
            ((CommentView) this.view).showPostProgressBar();
        }
    }

    public void getCommentListByMessageId(int i) {
        this.commentsBeens.clear();
        this.page = 1;
        this.sinceId = 0;
        this.commentModel.getCommentListByMessageId(i, 0, 1);
        this.messageId = i;
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener
    public void onSendCommentSuccess(CommentResultModel commentResultModel) {
        if (this.view != 0) {
            ((CommentView) this.view).hidePostProgressBar();
            ((CommentView) this.view).cleanEditText();
            ((CommentView) this.view).finishActivity();
        }
        getCommentListByMessageId(commentResultModel.getMessage_id());
        EventBus.getDefault().post(new SendCommentSuccessMessage());
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener
    public void onSendCommentFailed(String str) {
        ((CommentView) this.view).showCommentFailedMessage(MyApplication.getInstance().getString(R.string.comment_failed) + str);
        ((CommentView) this.view).showReSend();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener
    public void onGetCommentListSuccess(CommentListModel commentListModel) {
        if (commentListModel.getComments().size() <= 0 || this.view == 0) {
            return;
        }
        ((CommentView) this.view).refreshRecyclerView(this.commentsBeens);
        this.sinceId = commentListModel.getComments().get(0).getId();
        this.commentsBeens.addAll(commentListModel.getComments());
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener
    public void onGetCommentListFaile(String str) {
        if (this.view != 0) {
            ((CommentView) this.view).showCommentFailedMessage("获取评论列表失败:" + str);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener
    public void onDeleteCommentSuccess(DeleteCommentResultModel deleteCommentResultModel) {
        getCommentListByMessageId(this.messageId);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.OnCommentListener
    public void onDeleteCommentFaile(String str) {
        ((CommentView) this.view).showDeleteFailedCommentMsg(MyApplication.getContext().getString(R.string.deleteCommentFailed) + str);
    }
}
