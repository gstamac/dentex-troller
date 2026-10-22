package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.inet.Callback;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.inet.model.ContactResltModel;
import im.helmsman.helmsmanandroid.inet.model.ErrorCode;
import im.helmsman.helmsmanandroid.model.StartModel;
import java.util.Iterator;
import java.util.List;
import org.litepal.crud.DataSupport;
import retrofit2.Response;

/* JADX INFO: loaded from: classes2.dex */
public class StartModelImp implements StartModel {
    @Override // im.helmsman.helmsmanandroid.model.StartModel
    public void downloadContacts() {
        RequestInetUtils.instance().getContacts(new Callback<ContactResltModel>() { // from class: im.helmsman.helmsmanandroid.model.imp.StartModelImp.1
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onFailure(ErrorCode errorCode) {
            }

            /* JADX WARN: Type inference failed for: r0v0, types: [im.helmsman.helmsmanandroid.model.imp.StartModelImp$1$1] */
            @Override // im.helmsman.helmsmanandroid.inet.Callback
            public void onResponse(final Response<ContactResltModel> response) {
                new Thread() { // from class: im.helmsman.helmsmanandroid.model.imp.StartModelImp.1.1
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
                    }
                }.start();
                StartModelImp.this.deleteSpilthHistory();
            }
        });
    }

    public void deleteSpilthHistory() {
        List listFind = DataSupport.where("attime!=0&userid=-1").order("attime desc").find(ContactDao.class);
        if (listFind.size() > 10) {
            Iterator it = listFind.subList(10, listFind.size()).iterator();
            while (it.hasNext()) {
                ((ContactDao) it.next()).delete();
            }
        }
    }
}
