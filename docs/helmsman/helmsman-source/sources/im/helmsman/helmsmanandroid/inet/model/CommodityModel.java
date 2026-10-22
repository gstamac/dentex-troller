package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class CommodityModel {
    private int[] imgRes;
    private String link;
    private String name;
    private int price;

    public CommodityModel() {
    }

    public CommodityModel(int[] iArr, String str, String str2, int i) {
        this.imgRes = iArr;
        this.link = str;
        this.name = str2;
        this.price = i;
    }

    public int[] getImgRes() {
        return this.imgRes;
    }

    public void setImgRes(int[] iArr) {
        this.imgRes = iArr;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String str) {
        this.link = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int i) {
        this.price = i;
    }
}
