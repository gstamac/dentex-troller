package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentNotificationResultModel {
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
        private MessageBean message;
        private String timestamp;

        public AuthorBean getAuthor() {
            return this.author;
        }

        public void setAuthor(AuthorBean authorBean) {
            this.author = authorBean;
        }

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

        public MessageBean getMessage() {
            return this.message;
        }

        public void setMessage(MessageBean messageBean) {
            this.message = messageBean;
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
            private int followers_count;
            private int following_count;
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

            public int getFollowers_count() {
                return this.followers_count;
            }

            public void setFollowers_count(int i) {
                this.followers_count = i;
            }

            public int getFollowing_count() {
                return this.following_count;
            }

            public void setFollowing_count(int i) {
                this.following_count = i;
            }
        }

        public static class MessageBean {
            private AuthorBeanX author;
            private String description;
            private int id;
            private RouteBean route;
            private String timestamp;
            private int type;

            public int getId() {
                return this.id;
            }

            public void setId(int i) {
                this.id = i;
            }

            public int getType() {
                return this.type;
            }

            public void setType(int i) {
                this.type = i;
            }

            public String getTimestamp() {
                return this.timestamp;
            }

            public void setTimestamp(String str) {
                this.timestamp = str;
            }

            public AuthorBeanX getAuthor() {
                return this.author;
            }

            public void setAuthor(AuthorBeanX authorBeanX) {
                this.author = authorBeanX;
            }

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String str) {
                this.description = str;
            }

            public RouteBean getRoute() {
                return this.route;
            }

            public void setRoute(RouteBean routeBean) {
                this.route = routeBean;
            }

            public static class AuthorBeanX {
                private String about_me;
                private String avatar_url;
                private int followers_count;
                private int following_count;
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

                public int getFollowers_count() {
                    return this.followers_count;
                }

                public void setFollowers_count(int i) {
                    this.followers_count = i;
                }

                public int getFollowing_count() {
                    return this.following_count;
                }

                public void setFollowing_count(int i) {
                    this.following_count = i;
                }
            }

            public static class RouteBean {
                private CreatorBean creator;
                private float distance;
                private String encoded_polyline;
                private String identifier;
                private int route_id;
                private String route_name;
                private String static_image_url;
                private String timestamp;
                private int waypoint_count;

                public CreatorBean getCreator() {
                    return this.creator;
                }

                public void setCreator(CreatorBean creatorBean) {
                    this.creator = creatorBean;
                }

                public int getRoute_id() {
                    return this.route_id;
                }

                public void setRoute_id(int i) {
                    this.route_id = i;
                }

                public String getRoute_name() {
                    return this.route_name;
                }

                public void setRoute_name(String str) {
                    this.route_name = str;
                }

                public String getTimestamp() {
                    return this.timestamp;
                }

                public void setTimestamp(String str) {
                    this.timestamp = str;
                }

                public float getDistance() {
                    return this.distance;
                }

                public void setDistance(float f) {
                    this.distance = f;
                }

                public int getWaypoint_count() {
                    return this.waypoint_count;
                }

                public void setWaypoint_count(int i) {
                    this.waypoint_count = i;
                }

                public String getStatic_image_url() {
                    return this.static_image_url;
                }

                public void setStatic_image_url(String str) {
                    this.static_image_url = str;
                }

                public String getEncoded_polyline() {
                    return this.encoded_polyline;
                }

                public void setEncoded_polyline(String str) {
                    this.encoded_polyline = str;
                }

                public String getIdentifier() {
                    return this.identifier;
                }

                public void setIdentifier(String str) {
                    this.identifier = str;
                }

                public static class CreatorBean {
                    private String about_me;
                    private String avatar_url;
                    private int followers_count;
                    private int following_count;
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

                    public int getFollowers_count() {
                        return this.followers_count;
                    }

                    public void setFollowers_count(int i) {
                        this.followers_count = i;
                    }

                    public int getFollowing_count() {
                        return this.following_count;
                    }

                    public void setFollowing_count(int i) {
                        this.following_count = i;
                    }
                }
            }
        }
    }
}
