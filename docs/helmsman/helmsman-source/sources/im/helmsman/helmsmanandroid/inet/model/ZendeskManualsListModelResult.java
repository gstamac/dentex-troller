package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ZendeskManualsListModelResult {
    private int count;
    private Object next_page;
    private int page;
    private int page_count;
    private int per_page;
    private Object previous_page;
    private List<ResultsBean> results;

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

    public List<ResultsBean> getResults() {
        return this.results;
    }

    public void setResults(List<ResultsBean> list) {
        this.results = list;
    }

    public static class ResultsBean {
        private long author_id;
        private String body;
        private boolean comments_disabled;
        private String created_at;
        private boolean draft;
        private String html_url;
        private int id;
        private List<String> label_names;
        private String locale;
        private String name;
        private boolean outdated;
        private int position;
        private boolean promoted;
        private String result_type;
        private int section_id;
        private String source_locale;
        private String title;
        private String updated_at;
        private String url;
        private int vote_count;
        private int vote_sum;

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

        public long getAuthor_id() {
            return this.author_id;
        }

        public void setAuthor_id(long j) {
            this.author_id = j;
        }

        public boolean isComments_disabled() {
            return this.comments_disabled;
        }

        public void setComments_disabled(boolean z) {
            this.comments_disabled = z;
        }

        public boolean isDraft() {
            return this.draft;
        }

        public void setDraft(boolean z) {
            this.draft = z;
        }

        public boolean isPromoted() {
            return this.promoted;
        }

        public void setPromoted(boolean z) {
            this.promoted = z;
        }

        public int getPosition() {
            return this.position;
        }

        public void setPosition(int i) {
            this.position = i;
        }

        public int getVote_sum() {
            return this.vote_sum;
        }

        public void setVote_sum(int i) {
            this.vote_sum = i;
        }

        public int getVote_count() {
            return this.vote_count;
        }

        public void setVote_count(int i) {
            this.vote_count = i;
        }

        public int getSection_id() {
            return this.section_id;
        }

        public void setSection_id(int i) {
            this.section_id = i;
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

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public String getBody() {
            return this.body;
        }

        public void setBody(String str) {
            this.body = str;
        }

        public String getSource_locale() {
            return this.source_locale;
        }

        public void setSource_locale(String str) {
            this.source_locale = str;
        }

        public String getLocale() {
            return this.locale;
        }

        public void setLocale(String str) {
            this.locale = str;
        }

        public boolean isOutdated() {
            return this.outdated;
        }

        public void setOutdated(boolean z) {
            this.outdated = z;
        }

        public String getResult_type() {
            return this.result_type;
        }

        public void setResult_type(String str) {
            this.result_type = str;
        }

        public List<String> getLabel_names() {
            return this.label_names;
        }

        public void setLabel_names(List<String> list) {
            this.label_names = list;
        }
    }
}
