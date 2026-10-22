package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ContactResltModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.model.AtFriendsModel;
import im.helmsman.helmsmanandroid.presenter.listener.LoadContactListener;
import im.helmsman.helmsmanandroid.utils.UIThreadUtil;
import java.util.List;
import org.litepal.crud.DataSupport;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class AtFriendsModelImp implements AtFriendsModel {
    private LoadContactListener listener;

    public AtFriendsModelImp(LoadContactListener loadContactListener) {
        this.listener = loadContactListener;
    }

    @Override // im.helmsman.helmsmanandroid.model.AtFriendsModel
    public void loadDataByDataBase() {
        this.listener.onLoadContactByDataBaseSuccess(DataSupport.where("userid>=0").order("upper(username) asc").find(ContactDao.class));
    }

    @Override // im.helmsman.helmsmanandroid.model.AtFriendsModel
    public void loadDaraByInet() {
        RequestInetUtils.instance().getContacts(new Callback<ContactResltModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.AtFriendsModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(Response<ContactResltModel> response) {
                AtFriendsModelImp.this.listener.onLoadContactByInetSuccess(response.body());
            }

            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
                AtFriendsModelImp.this.listener.onLoadContctByInetFailed(errorCode);
            }
        });
    }

    @Override // im.helmsman.helmsmanandroid.model.AtFriendsModel
    public void loadHistoryByDataBase() {
        this.listener.onLoadContactHistorySuccess(DataSupport.where("attime!=0").order("attime desc").limit(10).find(ContactDao.class));
    }

    @Override // im.helmsman.helmsmanandroid.model.AtFriendsModel
    public void searchUserByUserName(String str) {
        this.listener.onSearchContactByDataBaseSuccess(DataSupport.where("username like ?", "%" + str + "%").order("upper(username) asc").find(ContactDao.class));
    }

    /* JADX INFO: renamed from: im.helmsman.helmsmanandroid.model.imp.AtFriendsModelImp$2, reason: invalid class name */
    class AnonymousClass2 extends Callback<ContactResltModel> {
        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onFailure(ErrorCode errorCode) {
        }

        AnonymousClass2() {
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [im.helmsman.helmsmanandroid.model.imp.AtFriendsModelImp$2$1] */
        @Override // im.helmsman.helmsmanandroid.inet.Callback
        public void onResponse(final Response<ContactResltModel> response) {
            new Thread() { // from class: im.helmsman.helmsmanandroid.model.imp.AtFriendsModelImp.2.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    super.run();
                    List<ContactDao> listContactResltModelConverToCantactDao = ContactDao.ContactResltModelConverToCantactDao(((ContactResltModel) response.body()).getUsers());
                    for (ContactDao contactDao : listContactResltModelConverToCantactDao) {
                        ContactDao contactDao2 = (ContactDao) ContactDao.where("userid=?", contactDao.getUserid() + "").findFirst(ContactDao.class);
                        if (contactDao2 == null) {
                            contactDao.save();
                        } else {
                            contactDao.setAttime(contactDao2.getAttime());
                            contactDao.updateAll("userid=?", contactDao2.getUserid() + "");
                        }
                    }
                    for (ContactDao contactDao3 : DataSupport.findAll(ContactDao.class, new long[0])) {
                        for (int i = 0; i < listContactResltModelConverToCantactDao.size() && contactDao3.getUserid() >= 0 && contactDao3.getUserid() != listContactResltModelConverToCantactDao.get(i).getUserid(); i++) {
                            if (i >= listContactResltModelConverToCantactDao.size() - 1) {
                                contactDao3.delete();
                            }
                        }
                    }
                    UIThreadUtil.runOnUiThread(new Runnable() { // from class: im.helmsman.helmsmanandroid.model.imp.AtFriendsModelImp.2.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AtFriendsModelImp.this.listener.reloadData();
                        }
                    });
                }
            }.start();
        }
    }

    @Override // im.helmsman.helmsmanandroid.model.AtFriendsModel
    public void updateContacts() {
        RequestInetUtils.instance().getContacts(new AnonymousClass2());
    }
}
