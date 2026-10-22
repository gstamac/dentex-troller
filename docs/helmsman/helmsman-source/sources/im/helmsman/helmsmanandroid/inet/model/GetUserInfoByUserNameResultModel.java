package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class GetUserInfoByUserNameResultModel {
    private String about_me;
    private String avatar_url;
    private int followers_count;
    private int following_count;
    private int id;
    private boolean is_followed_by;
    private boolean is_following;
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
}
