package im.helmsman.helmsmanandroid.dao;

import im.helmsman.helmsmanandroid.inet.model.ContactResltModel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class ContactDao extends DataSupport {
    private long attime;
    private String avatar_url;
    private String username;
    private int userid = -1;

    @Column(ignore = true)
    private boolean isHistory = false;

    @Column(ignore = true)
    public final String regex = "[A-Za-z]";

    public long getAttime() {
        return this.attime;
    }

    public void setAttime(long j) {
        this.attime = j;
    }

    public String getAvatar_url() {
        return this.avatar_url;
    }

    public void setAvatar_url(String str) {
        this.avatar_url = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int i) {
        this.userid = i;
    }

    public String getIndex() {
        if (this.isHistory) {
            return null;
        }
        String strValueOf = String.valueOf(this.username.charAt(0));
        return Pattern.compile("[A-Za-z]").matcher(strValueOf).matches() ? strValueOf.toUpperCase() : "#";
    }

    public boolean isHistory() {
        return this.isHistory;
    }

    public void setHistory(boolean z) {
        this.isHistory = z;
    }

    public static List<ContactDao> ContactResltModelConverToCantactDao(List<ContactResltModel.UsersBean> list) {
        ArrayList arrayList = new ArrayList();
        for (ContactResltModel.UsersBean usersBean : list) {
            ContactDao contactDao = new ContactDao();
            contactDao.setUserid(usersBean.getId());
            contactDao.setUsername(usersBean.getUsername());
            contactDao.setAvatar_url(usersBean.getAvatar_url());
            arrayList.add(contactDao);
        }
        return arrayList;
    }
}
