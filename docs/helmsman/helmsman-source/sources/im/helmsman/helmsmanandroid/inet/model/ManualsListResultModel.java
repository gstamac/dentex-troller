package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ManualsListResultModel {
    private List<UserManualsBean> user_manuals;

    public List<UserManualsBean> getUser_manuals() {
        return this.user_manuals;
    }

    public void setUser_manuals(List<UserManualsBean> list) {
        this.user_manuals = list;
    }

    public static class UserManualsBean {
        private String download_url;
        private int id;
        private String pub_timestamp;
        private String title;
        private String update_timestamp;

        public int getId() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public String getDownload_url() {
            return this.download_url;
        }

        public void setDownlaod_url(String str) {
            this.download_url = str;
        }

        public String getPub_timestamp() {
            return this.pub_timestamp;
        }

        public void setPub_timestamp(String str) {
            this.pub_timestamp = str;
        }

        public String getUpdate_timestamp() {
            return this.update_timestamp;
        }

        public void setUpdate_timestamp(String str) {
            this.update_timestamp = str;
        }
    }
}
