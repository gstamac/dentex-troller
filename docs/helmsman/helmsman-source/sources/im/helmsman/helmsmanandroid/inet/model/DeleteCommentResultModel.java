package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class DeleteCommentResultModel {
    private AuthorBean author;
    private String body;
    private int id;
    private int route_id;
    private String timestamp;

    public AuthorBean getAuthor() {
        return this.author;
    }

    public void setAuthor(AuthorBean authorBean) {
        this.author = authorBean;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String str) {
        this.body = str;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String str) {
        this.timestamp = str;
    }

    public int getRoute_id() {
        return this.route_id;
    }

    public void setRoute_id(int i) {
        this.route_id = i;
    }

    public static class AuthorBean {
        private String about_me;
        private String avatar_url;
        private int followers_count;
        private int following_count;
        private int id;
        private String last_seen;
        private String member_since;
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

        public String getMember_since() {
            return this.member_since;
        }

        public void setMember_since(String str) {
            this.member_since = str;
        }

        public String getLast_seen() {
            return this.last_seen;
        }

        public void setLast_seen(String str) {
            this.last_seen = str;
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
    }
}
