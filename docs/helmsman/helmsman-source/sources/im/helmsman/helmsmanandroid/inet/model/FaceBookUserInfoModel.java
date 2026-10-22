package im.helmsman.helmsmanandroid.inet.model;

/* JADX INFO: loaded from: classes2.dex */
public class FaceBookUserInfoModel {
    private String email;
    private String id;
    private String link;
    private String name;
    private PictureBean picture;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String str) {
        this.link = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public PictureBean getPicture() {
        return this.picture;
    }

    public void setPicture(PictureBean pictureBean) {
        this.picture = pictureBean;
    }

    public static class PictureBean {
        private DataBean data;

        public DataBean getData() {
            return this.data;
        }

        public void setData(DataBean dataBean) {
            this.data = dataBean;
        }

        public static class DataBean {
            private boolean is_silhouette;
            private String url;

            public boolean isIs_silhouette() {
                return this.is_silhouette;
            }

            public void setIs_silhouette(boolean z) {
                this.is_silhouette = z;
            }

            public String getUrl() {
                return this.url;
            }

            public void setUrl(String str) {
                this.url = str;
            }
        }
    }
}
