package im.helmsman.helmsmanandroid.inet.model;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class UserPagination<UserRelationship> {
    private int count;
    private boolean next;
    private boolean prev;
    private ArrayList<UserRelationship> users;

    public ArrayList<UserRelationship> getUsers() {
        return this.users;
    }

    public void setUsers(ArrayList<UserRelationship> arrayList) {
        this.users = arrayList;
    }

    public boolean isPrev() {
        return this.prev;
    }

    public void setPrev(boolean z) {
        this.prev = z;
    }

    public boolean isNext() {
        return this.next;
    }

    public void setNext(boolean z) {
        this.next = z;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public String toString() {
        return "UserPagination{users=" + this.users.toString() + ", prev=" + this.prev + ", next=" + this.next + ", count=" + this.count + '}';
    }
}
