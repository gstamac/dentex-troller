package im.helmsman.helmsmanandroid.model;

/* JADX INFO: loaded from: classes2.dex */
public interface CommentModel {
    void deleteCommentByCommentId(int i);

    void getCommentListByMessageId(int i, int i2, int i3);

    void sendComment(int i, String str);
}
