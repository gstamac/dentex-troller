package im.helmsman.helmsmanandroid.inet.model;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class CommentPagination<CommentToJson> {
    private ArrayList<CommentToJson> comments;
    private int count;
    private int current_page;
    private int next_page;
    private int prev_page;
    private int since_id;

    public ArrayList<CommentToJson> getComments() {
        return this.comments;
    }

    public void setComments(ArrayList<CommentToJson> arrayList) {
        this.comments = arrayList;
    }

    public int getSince_id() {
        return this.since_id;
    }

    public void setSince_id(int i) {
        this.since_id = i;
    }

    public int getCurrent_page() {
        return this.current_page;
    }

    public void setCurrent_page(int i) {
        this.current_page = i;
    }

    public int getPrev_page() {
        return this.prev_page;
    }

    public void setPrev_page(int i) {
        this.prev_page = i;
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

    public String toString() {
        return "CommentPagination{comments=" + this.comments + ", since_id=" + this.since_id + ", current_page=" + this.current_page + ", prev_page=" + this.prev_page + ", next_page=" + this.next_page + ", count=" + this.count + '}';
    }
}
