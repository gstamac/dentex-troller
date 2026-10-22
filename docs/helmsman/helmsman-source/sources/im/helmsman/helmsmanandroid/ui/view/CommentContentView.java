package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.CommentContentResultModel;
import im.helmsman.helmsmanandroid.inet.model.CommentListModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public interface CommentContentView extends MvpView {
    void diableLoadMore();

    void enableLoadMore();

    void getMessageSuccess(CommentContentResultModel commentContentResultModel);

    void initListView(CommentListModel commentListModel);

    void refreshListView();

    void showDeleteCommentFailed(String str);

    void showDownLoadRouteFailedMessage(String str);

    void showDownLoadRouteSuccessMessage();

    void showGetDatasFaileMessage(String str);
}
