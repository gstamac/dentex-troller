package im.helmsman.lib.property;

/* JADX INFO: loaded from: classes2.dex */
public abstract class WifiProperty {
    private String ssid;

    public abstract void changeWifiPassword(String str);

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }
}
