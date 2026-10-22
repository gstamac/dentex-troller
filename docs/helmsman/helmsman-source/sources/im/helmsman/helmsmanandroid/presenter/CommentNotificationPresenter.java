package im.helmsman.helmsmanandroid.presenter;

import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.CommentNotificationResultModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.CommentNotificationView;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class CommentNotificationPresenter extends BasePresenter<CommentNotificationView> {
    private int page = 1;
    private int sinceId = 0;

    public void getCommentNotification() {
        RequestInetUtils.instance().getCommentsNotification(this.sinceId, this.page, new Callback<CommentNotificationResultModel>() { // from class: im.helmsman.helmsmanandroid.presenter.CommentNotificationPresenter.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<CommentNotificationResultModel> response) {
                if (CommentNotificationPresenter.this.view == null) {
                    return;
                }
                CommentNotificationResultModel commentNotificationResultModelBody = response.body();
                CommentNotificationPresenter.this.sinceId = commentNotificationResultModelBody.getSince_id();
                if (commentNotificationResultModelBody.getNext_page() > CommentNotificationPresenter.this.page) {
                    ((CommentNotificationView) CommentNotificationPresenter.this.view).enableLoadMore();
                } else {
                    ((CommentNotificationView) CommentNotificationPresenter.this.view).disableLoadMore();
                }
                CommentNotificationPresenter.this.page++;
                ((CommentNotificationView) CommentNotificationPresenter.this.view).initListView(commentNotificationResultModelBody.getComments());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                if (CommentNotificationPresenter.this.view != null) {
                    ((CommentNotificationView) CommentNotificationPresenter.this.view).loadDataFaile(errorCode.getMessage());
                    ((CommentNotificationView) CommentNotificationPresenter.this.view).showLoadDataFaileMessage(errorCode.getMessage());
                }
            }
        });
    }

    public void refresh() {
        this.page = 1;
        this.sinceId = 0;
        getCommentNotification();
    }

    public void loadMore() {
        getCommentNotification();
    }
}
