package com.zendesk.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class CollectionUtils {
    private CollectionUtils() {
    }

    public static <T> boolean isEmpty(T[] tArr) {
        return tArr == null || tArr.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] tArr) {
        return !isEmpty(tArr);
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    public static <T> List<T> ensureEmpty(List<T> list) {
        return isEmpty(list) ? new ArrayList() : list;
    }

    public static <T> List<T> unmodifiableList(List<T> list) {
        return Collections.unmodifiableList(ensureEmpty(list));
    }

    @SafeVarargs
    public static <T> List<T> combineLists(List<T>... listArr) {
        if (listArr == null || listArr.length == 0) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (List list : new CopyOnWriteArrayList(listArr)) {
            if (isNotEmpty(list)) {
                Iterator it = new CopyOnWriteArrayList(list).iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next());
                }
            }
        }
        return arrayList;
    }

    public static <T> List<T> copyOf(List<T> list) {
        if (list == null) {
            return new ArrayList();
        }
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList(list);
        ArrayList arrayList = new ArrayList(copyOnWriteArrayList.size());
        Iterator it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    public static <K, V> Map<K, V> copyOf(Map<K, V> map) {
        if (map == null) {
            return new HashMap();
        }
        Map<? extends K, ? extends V> mapSynchronizedMap = Collections.synchronizedMap(map);
        HashMap map2 = new HashMap();
        map2.putAll(mapSynchronizedMap);
        return map2;
    }
}
