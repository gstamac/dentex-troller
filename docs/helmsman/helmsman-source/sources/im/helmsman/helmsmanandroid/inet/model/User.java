package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class User {
    private String about_me;
    private String avatar_url;
    private String followed_posts;
    private int id;
    private String last_seen;
    private String member_since;
    private int post_count;
    private String posts;
    private String routes;
    private int sex;
    private String url;
    private String username;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
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

    public String getPosts() {
        return this.posts;
    }

    public void setPosts(String str) {
        this.posts = str;
    }

    public String getRoutes() {
        return this.routes;
    }

    public void setRoutes(String str) {
        this.routes = str;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int i) {
        this.sex = i;
    }

    public String getFollowed_posts() {
        return this.followed_posts;
    }

    public void setFollowed_posts(String str) {
        this.followed_posts = str;
    }

    public int getPost_count() {
        return this.post_count;
    }

    public void setPost_count(int i) {
        this.post_count = i;
    }

    public String getAbout_me() {
        return this.about_me;
    }

    public void setAbout_me(String str) {
        this.about_me = str;
    }

    public String toString() {
        return "User{id=" + this.id + ", url='" + this.url + "', username='" + this.username + "', avatar_url='" + this.avatar_url + "', member_since='" + this.member_since + "', last_seen='" + this.last_seen + "', posts='" + this.posts + "', routes='" + this.routes + "', sex=" + this.sex + ", followed_posts='" + this.followed_posts + "', post_count=" + this.post_count + ", about_me='" + this.about_me + "'}";
    }
}
