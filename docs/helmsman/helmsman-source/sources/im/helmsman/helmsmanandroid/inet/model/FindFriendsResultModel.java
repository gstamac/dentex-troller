package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class FindFriendsResultModel {
    private int count;
    private int current_page;
    private int next_page;
    private int prev_age;
    private int since_id;
    private List<UsersBean> users;

    public int getPrev_age() {
        return this.prev_age;
    }

    public void setPrev_age(int i) {
        this.prev_age = i;
    }

    public int getNext_page() {
        return this.next_page;
    }

    public void setNext_page(int i) {
        this.next_page = i;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public int getCurrent_page() {
        return this.current_page;
    }

    public void setCurrent_page(int i) {
        this.current_page = i;
    }

    public int getSince_id() {
        return this.since_id;
    }

    public void setSince_id(int i) {
        this.since_id = i;
    }

    public List<UsersBean> getUsers() {
        return this.users;
    }

    public void setUsers(List<UsersBean> list) {
        this.users = list;
    }

    public static class UsersBean {
        private String about_me;
        private String avatar_url;
        private int followers_count;
        private int following_count;
        private int id;
        private boolean is_followed_by;
        private boolean is_following;
        private boolean isloading = false;
        private int sex;
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

        public int getSex() {
            return this.sex;
        }

        public void setSex(int i) {
            this.sex = i;
        }

        public String getAbout_me() {
            return this.about_me;
        }

        public void setAbout_me(String str) {
            this.about_me = str;
        }

        public int getFollowers_count() {
            return this.followers_count;
        }

        public void setFollowers_count(int i) {
            this.followers_count = i;
        }

        public int getFollowing_count() {
            return this.following_count;
        }

        public void setFollowing_count(int i) {
            this.following_count = i;
        }

        public boolean isIs_following() {
            return this.is_following;
        }

        public void setIs_following(boolean z) {
            this.is_following = z;
        }

        public boolean isIs_followed_by() {
            return this.is_followed_by;
        }

        public void setIs_followed_by(boolean z) {
            this.is_followed_by = z;
        }

        public boolean isloading() {
            return this.isloading;
        }

        public void setIsloading(boolean z) {
            this.isloading = z;
        }
    }
}
