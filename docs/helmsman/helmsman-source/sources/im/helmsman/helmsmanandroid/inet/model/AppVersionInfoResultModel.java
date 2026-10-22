package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class AppVersionInfoResultModel {
    private String app_name;
    private String author;
    private String download_url;
    private String last_version_description;
    private boolean locale;
    private String package_name;
    private String publish_date;
    private String publish_date_formated;
    private boolean status;
    private String version;

    public String getDownload_url() {
        return this.download_url;
    }

    public void setDownload_url(String str) {
        this.download_url = str;
    }

    public String getApp_name() {
        return this.app_name;
    }

    public void setApp_name(String str) {
        this.app_name = str;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String str) {
        this.author = str;
    }

    public String getLast_version_description() {
        return this.last_version_description;
    }

    public void setLast_version_description(String str) {
        this.last_version_description = str;
    }

    public boolean isLocale() {
        return this.locale;
    }

    public void setLocale(boolean z) {
        this.locale = z;
    }

    public String getPackage_name() {
        return this.package_name;
    }

    public void setPackage_name(String str) {
        this.package_name = str;
    }

    public String getPublish_date() {
        return this.publish_date;
    }

    public void setPublish_date(String str) {
        this.publish_date = str;
    }

    public String getPublish_date_formated() {
        return this.publish_date_formated;
    }

    public void setPublish_date_formated(String str) {
        this.publish_date_formated = str;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean z) {
        this.status = z;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }
}
