package com.bugsnag.android;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class MetaData extends Observable implements JsonStream.Streamable {
    private static final String FILTERED_PLACEHOLDER = "[FILTERED]";
    private static final String OBJECT_PLACEHOLDER = "[OBJECT]";
    private String[] filters;
    final Map<String, Object> store;

    public MetaData() {
        this.store = new ConcurrentHashMap();
    }

    public MetaData(Map<String, Object> map) {
        this.store = new ConcurrentHashMap(map);
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        objectToStream(this.store, jsonStream);
    }

    public void addToTab(String str, String str2, Object obj) {
        addToTab(str, str2, obj, true);
    }

    void addToTab(String str, String str2, Object obj, boolean z) {
        Map<String, Object> tab = getTab(str);
        if (obj != null) {
            tab.put(str2, obj);
        } else {
            tab.remove(str2);
        }
        notifyBugsnagObservers(NotifyType.META);
    }

    public void clearTab(String str) {
        this.store.remove(str);
        notifyBugsnagObservers(NotifyType.META);
    }

    Map<String, Object> getTab(String str) {
        Map<String, Object> map = (Map) this.store.get(str);
        if (map != null) {
            return map;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        this.store.put(str, concurrentHashMap);
        return concurrentHashMap;
    }

    void setFilters(String... strArr) {
        this.filters = strArr;
        notifyBugsnagObservers(NotifyType.FILTERS);
    }

    static MetaData merge(MetaData... metaDataArr) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (MetaData metaData : metaDataArr) {
            if (metaData != null) {
                arrayList.add(metaData.store);
                String[] strArr = metaData.filters;
                if (strArr != null) {
                    arrayList2.addAll(Arrays.asList(strArr));
                }
            }
        }
        MetaData metaData2 = new MetaData(mergeMaps((Map[]) arrayList.toArray(new Map[0])));
        metaData2.filters = (String[]) arrayList2.toArray(new String[arrayList2.size()]);
        return metaData2;
    }

    private static Map<String, Object> mergeMaps(Map<String, Object>... mapArr) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        for (Map<String, Object> map : mapArr) {
            if (map != null) {
                HashSet<String> hashSet = new HashSet(concurrentHashMap.keySet());
                hashSet.addAll(map.keySet());
                for (String str : hashSet) {
                    Object obj = concurrentHashMap.get(str);
                    Object obj2 = map.get(str);
                    if (obj2 != null) {
                        if (obj != null && (obj instanceof Map) && (obj2 instanceof Map)) {
                            concurrentHashMap.put(str, mergeMaps((Map) obj, (Map) obj2));
                        } else {
                            concurrentHashMap.put(str, obj2);
                        }
                    } else {
                        concurrentHashMap.put(str, obj);
                    }
                }
            }
        }
        return concurrentHashMap;
    }

    private void objectToStream(Object obj, JsonStream jsonStream) throws IOException {
        if (obj == null) {
            jsonStream.nullValue();
            return;
        }
        if (obj instanceof String) {
            jsonStream.value((String) obj);
            return;
        }
        if (obj instanceof Number) {
            jsonStream.value((Number) obj);
            return;
        }
        if (obj instanceof Boolean) {
            jsonStream.value((Boolean) obj);
            return;
        }
        if (obj instanceof Map) {
            jsonStream.beginObject();
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                if (key instanceof String) {
                    String str = (String) key;
                    jsonStream.name(str);
                    if (shouldFilter(str)) {
                        jsonStream.value(FILTERED_PLACEHOLDER);
                    } else {
                        objectToStream(entry.getValue(), jsonStream);
                    }
                }
            }
            jsonStream.endObject();
            return;
        }
        if (obj instanceof Collection) {
            jsonStream.beginArray();
            Iterator it = ((Collection) obj).iterator();
            while (it.hasNext()) {
                objectToStream(it.next(), jsonStream);
            }
            jsonStream.endArray();
            return;
        }
        if (obj.getClass().isArray()) {
            jsonStream.beginArray();
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                objectToStream(Array.get(obj, i), jsonStream);
            }
            jsonStream.endArray();
            return;
        }
        jsonStream.value(OBJECT_PLACEHOLDER);
    }

    private boolean shouldFilter(String str) {
        String[] strArr = this.filters;
        if (strArr != null && str != null) {
            for (String str2 : strArr) {
                if (str.contains(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void notifyBugsnagObservers(NotifyType notifyType) {
        setChanged();
        super.notifyObservers(notifyType.getValue());
    }
}
