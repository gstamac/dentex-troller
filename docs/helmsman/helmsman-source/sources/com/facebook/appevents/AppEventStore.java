package com.facebook.appevents;

import android.content.Context;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.appevents.internal.AppEventUtility;
import com.facebook.internal.Utility;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

/* JADX INFO: loaded from: classes.dex */
class AppEventStore {
    private static final String PERSISTED_EVENTS_FILENAME = "AppEventsLogger.persistedevents";
    private static final String TAG = "com.facebook.appevents.AppEventStore";

    AppEventStore() {
    }

    public static synchronized void persistEvents(AccessTokenAppIdPair accessTokenAppIdPair, SessionEventsState sessionEventsState) {
        AppEventUtility.assertIsNotMainThread();
        PersistedEvents andClearStore = readAndClearStore();
        if (andClearStore.containsKey(accessTokenAppIdPair)) {
            andClearStore.get(accessTokenAppIdPair).addAll(sessionEventsState.getEventsToPersist());
        } else {
            andClearStore.addEvents(accessTokenAppIdPair, sessionEventsState.getEventsToPersist());
        }
        saveEventsToDisk(andClearStore);
    }

    public static synchronized void persistEvents(AppEventCollection appEventCollection) {
        AppEventUtility.assertIsNotMainThread();
        PersistedEvents andClearStore = readAndClearStore();
        for (AccessTokenAppIdPair accessTokenAppIdPair : appEventCollection.keySet()) {
            andClearStore.addEvents(accessTokenAppIdPair, appEventCollection.get(accessTokenAppIdPair).getEventsToPersist());
        }
        saveEventsToDisk(andClearStore);
    }

    /* JADX WARN: Code duplicated, block: B:41:0x008a A[Catch: all -> 0x0091, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:8:0x0021, B:9:0x0024, B:41:0x008a, B:12:0x002f, B:21:0x0047, B:22:0x004a, B:25:0x0055, B:26:0x0059, B:28:0x005e, B:29:0x0061, B:33:0x0073, B:32:0x006c, B:35:0x0075, B:36:0x0078, B:39:0x0083), top: B:48:0x0003, inners: #1, #4, #8, #10 }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v2, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r4v0 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARN: Type inference failed for: r4v18 */
    /* JADX WARN: Type inference failed for: r4v3, types: [java.io.Closeable] */
    public static synchronized PersistedEvents readAndClearStore() {
        PersistedEvents persistedEvents;
        ?? r4;
        Throwable th;
        MovedClassObjectInputStream movedClassObjectInputStream;
        String str;
        String str2;
        Object obj;
        Object obj2;
        AppEventUtility.assertIsNotMainThread();
        ?? applicationContext = FacebookSdk.getApplicationContext();
        persistedEvents = null;
        try {
            try {
                movedClassObjectInputStream = new MovedClassObjectInputStream(new BufferedInputStream(applicationContext.openFileInput(PERSISTED_EVENTS_FILENAME)));
                try {
                    PersistedEvents persistedEvents2 = (PersistedEvents) movedClassObjectInputStream.readObject();
                    Utility.closeQuietly(movedClassObjectInputStream);
                    try {
                        File fileStreamPath = applicationContext.getFileStreamPath(PERSISTED_EVENTS_FILENAME);
                        fileStreamPath.delete();
                        obj2 = fileStreamPath;
                        obj = movedClassObjectInputStream;
                    } catch (Exception e) {
                        Log.w(TAG, "Got unexpected exception when removing events file: ", e);
                        obj2 = e;
                        obj = "Got unexpected exception when removing events file: ";
                    }
                    persistedEvents = persistedEvents2;
                    applicationContext = obj2;
                    r4 = obj;
                } catch (FileNotFoundException unused) {
                    Utility.closeQuietly(movedClassObjectInputStream);
                    try {
                        applicationContext.getFileStreamPath(PERSISTED_EVENTS_FILENAME).delete();
                    } catch (Exception e2) {
                        e = e2;
                        str = TAG;
                        str2 = "Got unexpected exception when removing events file: ";
                        Log.w(str, str2, e);
                    }
                    if (persistedEvents == null) {
                        persistedEvents = new PersistedEvents();
                    }
                    return persistedEvents;
                } catch (Exception e3) {
                    e = e3;
                    Log.w(TAG, "Got unexpected exception while reading events: ", e);
                    Utility.closeQuietly(movedClassObjectInputStream);
                    try {
                        File fileStreamPath2 = applicationContext.getFileStreamPath(PERSISTED_EVENTS_FILENAME);
                        fileStreamPath2.delete();
                        applicationContext = fileStreamPath2;
                        r4 = movedClassObjectInputStream;
                    } catch (Exception e4) {
                        e = e4;
                        str = TAG;
                        str2 = "Got unexpected exception when removing events file: ";
                        Log.w(str, str2, e);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                Utility.closeQuietly(r4);
                try {
                    applicationContext.getFileStreamPath(PERSISTED_EVENTS_FILENAME).delete();
                } catch (Exception e5) {
                    Log.w(TAG, "Got unexpected exception when removing events file: ", e5);
                }
                throw th;
            }
        } catch (FileNotFoundException unused2) {
            movedClassObjectInputStream = null;
        } catch (Exception e6) {
            e = e6;
            movedClassObjectInputStream = null;
        } catch (Throwable th3) {
            r4 = 0;
            th = th3;
            Utility.closeQuietly(r4);
            applicationContext.getFileStreamPath(PERSISTED_EVENTS_FILENAME).delete();
            throw th;
        }
        if (persistedEvents == null) {
            persistedEvents = new PersistedEvents();
        }
        return persistedEvents;
    }

    private static void saveEventsToDisk(PersistedEvents persistedEvents) throws Throwable {
        Context applicationContext = FacebookSdk.getApplicationContext();
        ObjectOutputStream objectOutputStream = null;
        try {
            try {
                ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new BufferedOutputStream(applicationContext.openFileOutput(PERSISTED_EVENTS_FILENAME, 0)));
                try {
                    objectOutputStream2.writeObject(persistedEvents);
                    Utility.closeQuietly(objectOutputStream2);
                } catch (Exception e) {
                    e = e;
                    objectOutputStream = objectOutputStream2;
                    Log.w(TAG, "Got unexpected exception while persisting events: ", e);
                    try {
                        applicationContext.getFileStreamPath(PERSISTED_EVENTS_FILENAME).delete();
                    } catch (Exception unused) {
                    }
                    Utility.closeQuietly(objectOutputStream);
                } catch (Throwable th) {
                    th = th;
                    objectOutputStream = objectOutputStream2;
                    Utility.closeQuietly(objectOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private static class MovedClassObjectInputStream extends ObjectInputStream {
        private static final String ACCESS_TOKEN_APP_ID_PAIR_SERIALIZATION_PROXY_V1_CLASS_NAME = "com.facebook.appevents.AppEventsLogger$AccessTokenAppIdPair$SerializationProxyV1";
        private static final String APP_EVENT_SERIALIZATION_PROXY_V1_CLASS_NAME = "com.facebook.appevents.AppEventsLogger$AppEvent$SerializationProxyV1";

        public MovedClassObjectInputStream(InputStream inputStream) throws IOException {
            super(inputStream);
        }

        @Override // java.io.ObjectInputStream
        protected ObjectStreamClass readClassDescriptor() throws ClassNotFoundException, IOException {
            ObjectStreamClass classDescriptor = super.readClassDescriptor();
            if (classDescriptor.getName().equals(ACCESS_TOKEN_APP_ID_PAIR_SERIALIZATION_PROXY_V1_CLASS_NAME)) {
                return ObjectStreamClass.lookup(AccessTokenAppIdPair.SerializationProxyV1.class);
            }
            return classDescriptor.getName().equals(APP_EVENT_SERIALIZATION_PROXY_V1_CLASS_NAME) ? ObjectStreamClass.lookup(AppEvent.SerializationProxyV1.class) : classDescriptor;
        }
    }
}
