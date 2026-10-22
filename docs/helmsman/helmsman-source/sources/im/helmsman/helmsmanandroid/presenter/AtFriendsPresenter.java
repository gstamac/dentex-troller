package im.helmsman.helmsmanandroid.presenter;

import android.text.TextUtils;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.inet.model.ContactResltModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.model.AtFriendsModel;
import im.helmsman.helmsmanandroid.model.imp.AtFriendsModelImp;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener;
import im.helmsman.helmsmanandroid.ui.view.AtFriendsView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AtFriendsPresenter extends BasePresenter<AtFriendsView> implements LoadContactListener {
    private List<ContactDao> contactDaos = new ArrayList();
    private AtFriendsModel atFriendsModel = new AtFriendsModelImp(this);

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void onLoadContactByDataBaseFailed() {
    }

    public void loadData() {
        this.contactDaos.clear();
        this.atFriendsModel.loadDataByDataBase();
        this.atFriendsModel.loadHistoryByDataBase();
    }

    public void updateContactsByNet() {
        this.atFriendsModel.updateContacts();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void onLoadContactByDataBaseSuccess(List<ContactDao> list) {
        if (list == null || list.size() == 0) {
            this.atFriendsModel.loadDaraByInet();
        } else if (this.view != 0) {
            this.contactDaos.addAll(list);
            ((AtFriendsView) this.view).updateListView(this.contactDaos);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void onLoadContactByInetSuccess(ContactResltModel contactResltModel) {
        List<ContactDao> listContactResltModelConverToCantactDao = ContactDao.ContactResltModelConverToCantactDao(contactResltModel.getUsers());
        ContactDao.deleteAll((Class<?>) ContactDao.class, new String[0]);
        ContactDao.saveAll(listContactResltModelConverToCantactDao);
        loadData();
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void onLoadContctByInetFailed(ErrorCode errorCode) {
        if (this.view != 0) {
            ((AtFriendsView) this.view).showLoadContactFailedMessage(MyApplication.getInstance().getString(R.string.loaddatafailed) + ":" + errorCode.getMessage());
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void onLoadContactHistorySuccess(List<ContactDao> list) {
        Iterator<ContactDao> it = list.iterator();
        while (it.hasNext()) {
            it.next().setHistory(true);
        }
        this.contactDaos.addAll(0, list);
        if (this.view != 0) {
            ((AtFriendsView) this.view).updateListView(this.contactDaos);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void onSearchContactHistorySuccess(List<ContactDao> list) {
        this.contactDaos.addAll(0, list);
        if (this.view != 0) {
            ((AtFriendsView) this.view).updateListView(this.contactDaos);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void onSearchContactByDataBaseSuccess(List<ContactDao> list) {
        if (this.view != 0) {
            this.contactDaos.addAll(list);
            ((AtFriendsView) this.view).updateListView(this.contactDaos);
        }
    }

    @Override // im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener
    public void reloadData() {
        loadData();
    }

    public void searchDataBaseByUserName(String str) {
        if (TextUtils.isEmpty(str)) {
            loadData();
        } else {
            this.contactDaos.clear();
            this.atFriendsModel.searchUserByUserName(str);
        }
    }
}
