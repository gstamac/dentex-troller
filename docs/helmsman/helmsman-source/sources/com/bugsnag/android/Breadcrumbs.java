package com.bugsnag.android;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/* JADX INFO: loaded from: classes.dex */
class Breadcrumbs implements JsonStream.Streamable {
    private static final int DEFAULT_MAX_SIZE = 20;
    private static final int MAX_PAYLOAD_SIZE = 4096;
    final Queue<Breadcrumb> store = new ConcurrentLinkedQueue();
    private int maxSize = 20;

    Breadcrumbs() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Breadcrumb implements JsonStream.Streamable {
        private static final String DEFAULT_NAME = "manual";
        private static final int MAX_MESSAGE_LENGTH = 140;
        private static final String MESSAGE_METAKEY = "message";
        private final String METADATA_KEY;
        private final String NAME_KEY;
        private final String TIMESTAMP_KEY;
        private final String TYPE_KEY;
        final Map<String, String> metadata;
        final String name;
        final String timestamp;
        final BreadcrumbType type;

        Breadcrumb(String str) {
            this.TIMESTAMP_KEY = "timestamp";
            this.NAME_KEY = "name";
            this.METADATA_KEY = "metaData";
            this.TYPE_KEY = "type";
            this.timestamp = DateUtils.toISO8601(new Date());
            this.type = BreadcrumbType.MANUAL;
            this.metadata = Collections.singletonMap("message", str.substring(0, Math.min(str.length(), 140)));
            this.name = DEFAULT_NAME;
        }

        Breadcrumb(String str, BreadcrumbType breadcrumbType, Map<String, String> map) {
            this.TIMESTAMP_KEY = "timestamp";
            this.NAME_KEY = "name";
            this.METADATA_KEY = "metaData";
            this.TYPE_KEY = "type";
            this.timestamp = DateUtils.toISO8601(new Date());
            this.type = breadcrumbType;
            this.metadata = map;
            this.name = str;
        }

        @Override // com.bugsnag.android.JsonStream.Streamable
        public void toStream(JsonStream jsonStream) throws IOException {
            jsonStream.beginObject();
            jsonStream.name("timestamp").value(this.timestamp);
            jsonStream.name("name").value(this.name);
            jsonStream.name("type").value(this.type.toString());
            jsonStream.name("metaData");
            jsonStream.beginObject();
            for (Map.Entry<String, String> entry : this.metadata.entrySet()) {
                jsonStream.name(entry.getKey()).value(entry.getValue());
            }
            jsonStream.endObject();
            jsonStream.endObject();
        }

        public int payloadSize() throws IOException {
            StringWriter stringWriter = new StringWriter();
            toStream(new JsonStream(stringWriter));
            return stringWriter.toString().length();
        }
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginArray();
        Iterator<Breadcrumb> it = this.store.iterator();
        while (it.hasNext()) {
            it.next().toStream(jsonStream);
        }
        jsonStream.endArray();
    }

    void add(String str) {
        addToStore(new Breadcrumb(str));
    }

    void add(String str, BreadcrumbType breadcrumbType, Map<String, String> map) {
        addToStore(new Breadcrumb(str, breadcrumbType, map));
    }

    void clear() {
        this.store.clear();
    }

    void setSize(int i) {
        if (i > this.store.size()) {
            this.maxSize = i;
        } else {
            while (this.store.size() > i) {
                this.store.poll();
            }
        }
    }

    private void addToStore(Breadcrumb breadcrumb) {
        try {
            if (breadcrumb.payloadSize() > 4096) {
                Logger.warn("Dropping breadcrumb because payload exceeds 4KB limit");
                return;
            }
            if (this.store.size() >= this.maxSize) {
                this.store.poll();
            }
            this.store.add(breadcrumb);
        } catch (IOException e) {
            Logger.warn("Dropping breadcrumb because it could not be serialized", e);
        }
    }
}
