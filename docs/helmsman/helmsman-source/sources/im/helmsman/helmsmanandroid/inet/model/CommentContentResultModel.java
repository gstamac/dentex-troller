package im.helmsman.helmsmanandroid.inet.model;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentContentResultModel {
    private AuthorBean author;
    private String description;
    private int id;
    private int message_comment_count;
    private RouteBean route;
    private int route_download_count;
    private int route_favourite_count;
    private String timestamp;
    private int type;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public RouteBean getRoute() {
        return this.route;
    }

    public void setRoute(RouteBean routeBean) {
        this.route = routeBean;
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

    public AuthorBean getAuthor() {
        return this.author;
    }

    public void setAuthor(AuthorBean authorBean) {
        this.author = authorBean;
    }

    public int getRoute_favourite_count() {
        return this.route_favourite_count;
    }

    public void setRoute_favourite_count(int i) {
        this.route_favourite_count = i;
    }

    public int getMessage_comment_count() {
        return this.message_comment_count;
    }

    public void setMessage_comment_count(int i) {
        this.message_comment_count = i;
    }

    public int getRoute_download_count() {
        return this.route_download_count;
    }

    public void setRoute_download_count(int i) {
        this.route_download_count = i;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public static class RouteBean {
        private CreatorBean creator;
        private float distance;
        private String encoded_polyline;
        private String identifier;
        private boolean is_favourite;
        private int route_download_count;
        private int route_favourite_count;
        private int route_id;
        private String route_name;
        private String static_image_url;
        private String timestamp;
        private int waypoint_count;
        private List<WaypointsBean> waypoints;

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

        public boolean isIs_favourite() {
            return this.is_favourite;
        }

        public void setIs_favourite(boolean z) {
            this.is_favourite = z;
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

        public int getRoute_favourite_count() {
            return this.route_favourite_count;
        }

        public void setRoute_favourite_count(int i) {
            this.route_favourite_count = i;
        }

        public int getRoute_download_count() {
            return this.route_download_count;
        }

        public void setRoute_download_count(int i) {
            this.route_download_count = i;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(String str) {
            this.identifier = str;
        }

        public String getEncoded_polyline() {
            return this.encoded_polyline;
        }

        public void setEncoded_polyline(String str) {
            this.encoded_polyline = str;
        }

        public List<WaypointsBean> getWaypoints() {
            return this.waypoints;
        }

        public void setWaypoints(List<WaypointsBean> list) {
            this.waypoints = list;
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

        public static class WaypointsBean {
            private String geohash;
            private int id;
            private int index;
            private float latitude;
            private float longitude;
            private int route_id;
            private String timestamp;

            public int getId() {
                return this.id;
            }

            public void setId(int i) {
                this.id = i;
            }

            public int getRoute_id() {
                return this.route_id;
            }

            public void setRoute_id(int i) {
                this.route_id = i;
            }

            public int getIndex() {
                return this.index;
            }

            public void setIndex(int i) {
                this.index = i;
            }

            public float getLongitude() {
                return this.longitude;
            }

            public void setLongitude(float f) {
                this.longitude = f;
            }

            public float getLatitude() {
                return this.latitude;
            }

            public void setLatitude(float f) {
                this.latitude = f;
            }

            public String getGeohash() {
                return this.geohash;
            }

            public void setGeohash(String str) {
                this.geohash = str;
            }

            public String getTimestamp() {
                return this.timestamp;
            }

            public void setTimestamp(String str) {
                this.timestamp = str;
            }
        }
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
}
