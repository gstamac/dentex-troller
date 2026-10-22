package im.helmsman.helmsmanandroid.presenter.listener;

import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.inet.model.ContactResltModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface LoadContactListener {
    void onLoadContactByDataBaseFailed();

    void onLoadContactByDataBaseSuccess(List<ContactDao> list);

    void onLoadContactByInetSuccess(ContactResltModel contactResltModel);

    void onLoadContactHistorySuccess(List<ContactDao> list);

    void onLoadContctByInetFailed(ErrorCode errorCode);

    void onSearchContactByDataBaseSuccess(List<ContactDao> list);

    void onSearchContactHistorySuccess(List<ContactDao> list);

    void reloadData();
}
