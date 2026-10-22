package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class UserRelationship {
    private boolean is_friend;
    private String timestamp;
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String str) {
        this.timestamp = str;
    }

    public boolean is_friend() {
        return this.is_friend;
    }

    public void setIs_friend(boolean z) {
        this.is_friend = z;
    }

    public String toString() {
        return "UserRelationship{user=" + this.user + ", timestamp='" + this.timestamp + "', is_friend=" + this.is_friend + '}';
    }
}
