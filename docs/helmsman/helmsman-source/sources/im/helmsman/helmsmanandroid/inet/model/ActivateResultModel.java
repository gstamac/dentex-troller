package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class ActivateResultModel {
    private int activate_count;
    private int id;
    private String last_activate;
    private String manufacturer;
    private int product_category;
    private String sn;
    private String ssid;
    private String ssid_confirmed;
    private String uuid;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }

    public int getProduct_category() {
        return this.product_category;
    }

    public void setProduct_category(int i) {
        this.product_category = i;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String str) {
        this.manufacturer = str;
    }

    public int getActivate_count() {
        return this.activate_count;
    }

    public void setActivate_count(int i) {
        this.activate_count = i;
    }

    public String getLast_activate() {
        return this.last_activate;
    }

    public void setLast_activate(String str) {
        this.last_activate = str;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String str) {
        this.sn = str;
    }

    public String getSsid_confirmed() {
        return this.ssid_confirmed;
    }

    public void setSsid_confirmed(String str) {
        this.ssid_confirmed = str;
    }
}
