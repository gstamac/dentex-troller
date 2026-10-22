package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.CommentNotificationResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface CommentNotificationView extends MvpView {
    void disableLoadMore();

    void enableLoadMore();

    void initListView(List<CommentNotificationResultModel.CommentsBean> list);

    void loadDataFaile(String str);

    void showLoadDataFaileMessage(String str);
}
