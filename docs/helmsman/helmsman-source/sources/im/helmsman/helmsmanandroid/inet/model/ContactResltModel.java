package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class ContactResltModel {
    private List<UsersBean> users;

    public List<UsersBean> getUsers() {
        return this.users;
    }

    public void setUsers(List<UsersBean> list) {
        this.users = list;
    }

    public static class UsersBean extends DataSupport {
        private String avatar_url;
        private int id;
        private String username;

        public int getId() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String str) {
            this.username = str;
        }

        public String getAvatar_url() {
            return this.avatar_url;
        }

        public void setAvatar_url(String str) {
            this.avatar_url = str;
        }

        public String getIndex() {
            return this.username.charAt(0) + "";
        }
    }
}
