package com.facebook.places.model;

import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class PlaceInfoRequestParams {
    private final Set<String> fields;
    private final String placeId;

    private PlaceInfoRequestParams(Builder builder) {
        HashSet hashSet = new HashSet();
        this.fields = hashSet;
        this.placeId = builder.placeId;
        hashSet.addAll(builder.fields);
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public Set<String> getFields() {
        return this.fields;
    }

    public static class Builder {
        private final Set<String> fields = new HashSet();
        private String placeId;

        public Builder setPlaceId(String str) {
            this.placeId = str;
            return this;
        }

        public Builder addField(String str) {
            this.fields.add(str);
            return this;
        }

        public Builder addFields(String[] strArr) {
            for (String str : strArr) {
                this.fields.add(str);
            }
            return this;
        }

        public PlaceInfoRequestParams build() {
            return new PlaceInfoRequestParams(this);
        }
    }
}
