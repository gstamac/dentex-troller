package im.helmsman.helmsmanandroid.model.imp;

import im.helmsman.helmsmanandroid.dao.ContactDao;
import im.helmsman.helmsmanandroid.model.SendMessageModel;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class SendMessageModelImp implements SendMessageModel {
    @Override // im.helmsman.helmsmanandroid.model.SendMessageModel
    public void saveSendMessgeHistory(String str) {
        ContactDao contactDao = (ContactDao) DataSupport.where("username=?", str).findFirst(ContactDao.class);
        if (contactDao != null) {
            contactDao.setAttime(System.currentTimeMillis());
            contactDao.updateAll("username=?", str);
        }
    }
}
