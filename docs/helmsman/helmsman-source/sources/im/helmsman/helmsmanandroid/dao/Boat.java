package im.helmsman.helmsmanandroid.dao;

import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class Boat extends DataSupport {
    private long firmwareVersion;
    private String manuFacturer;
    private long mode;
    private String ssid = null;
    private int disconnectProtection = 1;

    public int getDisconnectProtection() {
        return this.disconnectProtection;
    }

    public void setDisconnectProtection(int i) {
        this.disconnectProtection = i;
    }

    public long getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public void setFirmwareVersion(long j) {
        this.firmwareVersion = j;
    }

    public String getManuFacturer() {
        return this.manuFacturer;
    }

    public void setManuFacturer(String str) {
        this.manuFacturer = str;
    }

    public long getMode() {
        return this.mode;
    }

    public void setMode(long j) {
        this.mode = j;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }
}
