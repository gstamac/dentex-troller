package im.helmsman.helmsmanandroid.inet.model;

import android.util.Base64;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.bugsnag.android.Bugsnag;
import im.helmsman.helmsmanandroid.MyApplication;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javy.lib.jsave.JSave;

/* JADX INFO: loaded from: classes2.dex */
public class UserMe implements Serializable {
    private String about_me = " ";
    private String avatar_hash;
    private String avatar_url;
    private String country_code;
    private String email;
    private String followed_posts;
    private String followers_count;
    private String following_count;
    private int id;
    private String last_seen;
    private String member_since;
    private int post_count;
    private String posts;
    private String role;
    private int route_count;
    private String routes;
    private int sex;
    private String username;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
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

    public String getAvatar_hash() {
        return this.avatar_hash;
    }

    public void setAvatar_hash(String str) {
        this.avatar_hash = str;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String str) {
        this.role = str;
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

    public String getRoutes() {
        return this.routes;
    }

    public void setRoutes(String str) {
        this.routes = str;
    }

    public int getRoute_count() {
        return this.route_count;
    }

    public void setRoute_count(int i) {
        this.route_count = i;
    }

    public String getFollowing_count() {
        return this.following_count;
    }

    public void setFollowing_count(String str) {
        this.following_count = str;
    }

    public String getFollowers_count() {
        return this.followers_count;
    }

    public void setFollowers_count(String str) {
        this.followers_count = str;
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

    public String toString() {
        return "AccountInfo{id=" + this.id + ", email='" + this.email + "', username='" + this.username + "', avatar_url='" + this.avatar_url + "', avatar_hash='" + this.avatar_hash + "', role='" + this.role + "', member_since='" + this.member_since + "', last_seen='" + this.last_seen + "', posts='" + this.posts + "', followed_posts='" + this.followed_posts + "', post_count=" + this.post_count + ", routes='" + this.routes + "', route_count=" + this.route_count + ", following_count='" + this.following_count + "', followers_count='" + this.followers_count + "', sex=" + this.sex + ", about_me='" + this.about_me + "', country_code='" + this.country_code + "'}";
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public void setCountry_code(String str) {
        this.country_code = str;
    }

    public static UserMe getUser() {
        UserMe userMe = (UserMe) JSave.get("UserInfo");
        if (!MyApplication.getInstance().isApkInDebug()) {
            if (userMe != null) {
                Bugsnag.setUser(userMe.id + "", userMe.email, userMe.username);
            } else {
                Bugsnag.clearUser();
            }
        }
        return userMe;
    }

    public static boolean isMySelef(int i) {
        UserMe user = getUser();
        return user != null && user.getId() == i;
    }

    public static String getUserToken() {
        try {
            String str = (String) JSave.get(JThirdPlatFormInterface.KEY_TOKEN);
            if (str == null) {
                return null;
            }
            return encodeCredentialsForBasicAuthorizationByToken(str);
        } catch (NullPointerException unused) {
            return null;
        }
    }

    private static String encodeCredentialsForBasicAuthorizationByToken(String str) {
        return "Basic " + Base64.encodeToString((str + ":").getBytes(), 2);
    }

    public static boolean isLogin() {
        return getUserToken() != null;
    }

    public static boolean cleanToken() {
        return JSave.put(JThirdPlatFormInterface.KEY_TOKEN, null);
    }

    public static void cleanJpush() {
        JPushInterface.setAliasAndTags(MyApplication.getContext(), null, new HashSet(), new TagAliasCallback() { // from class: im.helmsman.helmsmanandroid.inet.model.UserMe.1
            @Override // cn.jpush.android.api.TagAliasCallback
            public void gotResult(int i, String str, Set<String> set) {
                if (i != 0) {
                    UserMe.cleanJpush();
                }
            }
        });
    }

    public static void loginOut() {
        cleanToken();
        cleanJpush();
        if (MyApplication.getInstance().isApkInDebug()) {
            return;
        }
        Bugsnag.clearUser();
    }
}
