package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.inet.model.FavouriteNotifitcationMessageListResultModel;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface FavouriteMessageView extends MvpView {
    void disableLoadMore();

    void enableLoadMore();

    void initListView(List<FavouriteNotifitcationMessageListResultModel.MessagesBean> list);

    void showLoadDataFaileMessage(String str);
}
