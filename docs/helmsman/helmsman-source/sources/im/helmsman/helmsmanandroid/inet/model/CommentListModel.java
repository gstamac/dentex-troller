package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentListModel {
    private List<CommentsBean> comments;
    private int count;
    private int current_page;
    private int next_page;
    private int prev_page;
    private int since_id;

    public int getSince_id() {
        return this.since_id;
    }

    public void setSince_id(int i) {
        this.since_id = i;
    }

    public int getCurrent_page() {
        return this.current_page;
    }

    public void setCurrent_page(int i) {
        this.current_page = i;
    }

    public int getPrev_page() {
        return this.prev_page;
    }

    public void setPrev_page(int i) {
        this.prev_page = i;
    }

    public int getNext_page() {
        return this.next_page;
    }

    public void setNext_page(int i) {
        this.next_page = i;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public List<CommentsBean> getComments() {
        return this.comments;
    }

    public void setComments(List<CommentsBean> list) {
        this.comments = list;
    }

    public static class CommentsBean {
        private AuthorBean author;
        private String comment_text;
        private int id;
        private int message_id;
        private String timestamp;

        public int getId() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

        public String getComment_text() {
            return this.comment_text;
        }

        public void setComment_text(String str) {
            this.comment_text = str;
        }

        public AuthorBean getAuthor() {
            return this.author;
        }

        public void setAuthor(AuthorBean authorBean) {
            this.author = authorBean;
        }

        public int getMessage_id() {
            return this.message_id;
        }

        public void setMessage_id(int i) {
            this.message_id = i;
        }

        public String getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(String str) {
            this.timestamp = str;
        }

        public static class AuthorBean {
            private String about_me;
            private String avatar_url;
            private int id;
            private String last_seen;
            private String member_since;
            private int sex;
            private String username;

            public int getId() {
                return this.id;
            }

            public void setId(int i) {
                this.id = i;
            }

            public String getUsername() {
                return this.username;
            }

            public void setUsername(String str) {
                this.username = str;
            }

            public String getAvatar_url() {
                return this.avatar_url;
            }

            public void setAvatar_url(String str) {
                this.avatar_url = str;
            }

            public String getMember_since() {
                return this.member_since;
            }

            public void setMember_since(String str) {
                this.member_since = str;
            }

            public String getLast_seen() {
                return this.last_seen;
            }

            public void setLast_seen(String str) {
                this.last_seen = str;
            }

            public int getSex() {
                return this.sex;
            }

            public void setSex(int i) {
                this.sex = i;
            }

            public String getAbout_me() {
                return this.about_me;
            }

            public void setAbout_me(String str) {
                this.about_me = str;
            }
        }
    }
}
