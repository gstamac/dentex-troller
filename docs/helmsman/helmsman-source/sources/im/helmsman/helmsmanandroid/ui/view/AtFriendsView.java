package im.helmsman.helmsmanandroid.ui.view;

import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface AtFriendsView extends MvpView {
    void showLoadContactFailedMessage(String str);

    void updateListView(List<ContactDao> list);
}
