package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class CommentResultModel {
    private int author_id;
    private String comment_text;
    private int id;
    private int message_id;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getComment_text() {
        return this.comment_text;
    }

    public void setComment_text(String str) {
        this.comment_text = str;
    }

    public int getAuthor_id() {
        return this.author_id;
    }

    public void setAuthor_id(int i) {
        this.author_id = i;
    }

    public int getMessage_id() {
        return this.message_id;
    }

    public void setMessage_id(int i) {
        this.message_id = i;
    }
}
