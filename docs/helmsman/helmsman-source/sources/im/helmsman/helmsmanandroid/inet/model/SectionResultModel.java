package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SectionResultModel {
    private int count;
    private Object next_page;
    private int page;
    private int page_count;
    private int per_page;
    private Object previous_page;
    private List<SectionsBean> sections;
    private String sort_by;
    private String sort_order;

    public int getPage() {
        return this.page;
    }

    public void setPage(int i) {
        this.page = i;
    }

    public Object getPrevious_page() {
        return this.previous_page;
    }

    public void setPrevious_page(Object obj) {
        this.previous_page = obj;
    }

    public Object getNext_page() {
        return this.next_page;
    }

    public void setNext_page(Object obj) {
        this.next_page = obj;
    }

    public int getPer_page() {
        return this.per_page;
    }

    public void setPer_page(int i) {
        this.per_page = i;
    }

    public int getPage_count() {
        return this.page_count;
    }

    public void setPage_count(int i) {
        this.page_count = i;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public String getSort_by() {
        return this.sort_by;
    }

    public void setSort_by(String str) {
        this.sort_by = str;
    }

    public String getSort_order() {
        return this.sort_order;
    }

    public void setSort_order(String str) {
        this.sort_order = str;
    }

    public List<SectionsBean> getSections() {
        return this.sections;
    }

    public void setSections(List<SectionsBean> list) {
        this.sections = list;
    }

    public static class SectionsBean {
        private int category_id;
        private String created_at;
        private String description;
        private String html_url;
        private int id;
        private String locale;
        private String name;
        private boolean outdated;
        private int position;
        private String sorting;
        private String source_locale;
        private String updated_at;
        private String url;

        public int getId() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }

        public String getHtml_url() {
            return this.html_url;
        }

        public void setHtml_url(String str) {
            this.html_url = str;
        }

        public int getCategory_id() {
            return this.category_id;
        }

        public void setCategory_id(int i) {
            this.category_id = i;
        }

        public int getPosition() {
            return this.position;
        }

        public void setPosition(int i) {
            this.position = i;
        }

        public String getSorting() {
            return this.sorting;
        }

        public void setSorting(String str) {
            this.sorting = str;
        }

        public String getCreated_at() {
            return this.created_at;
        }

        public void setCreated_at(String str) {
            this.created_at = str;
        }

        public String getUpdated_at() {
            return this.updated_at;
        }

        public void setUpdated_at(String str) {
            this.updated_at = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String str) {
            this.description = str;
        }

        public String getLocale() {
            return this.locale;
        }

        public void setLocale(String str) {
            this.locale = str;
        }

        public String getSource_locale() {
            return this.source_locale;
        }

        public void setSource_locale(String str) {
            this.source_locale = str;
        }

        public boolean isOutdated() {
            return this.outdated;
        }

        public void setOutdated(boolean z) {
            this.outdated = z;
        }
    }
}
