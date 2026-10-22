package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class JpushQueryTagAndAlias {
    private String alias;
    private String mobile;
    private List<String> tags;

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String str) {
        this.alias = str;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String str) {
        this.mobile = str;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> list) {
        this.tags = list;
    }
}
