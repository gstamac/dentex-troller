package im.helmsman.helmsmanandroid.eventbusmessagemodel;

/* JADX INFO: loaded from: classes2.dex */
public class DowloadCompleteMessage {
    private int listDataPosition = -1;
    private int downLoadCount = 0;

    public int getDownLoadCount() {
        return this.downLoadCount;
    }

    public void setDownLoadCount(int i) {
        this.downLoadCount = i;
    }

    public int getListDataPosition() {
        return this.listDataPosition;
    }

    public void setListDataPosition(int i) {
        this.listDataPosition = i;
    }
}
