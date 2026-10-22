package com.facebook.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class AttributionIdentifiers {
    private static final String ANDROID_ID_COLUMN_NAME = "androidid";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final String ATTRIBUTION_ID_CONTENT_PROVIDER = "com.facebook.katana.provider.AttributionIdProvider";
    private static final String ATTRIBUTION_ID_CONTENT_PROVIDER_WAKIZASHI = "com.facebook.wakizashi.provider.AttributionIdProvider";
    private static final int CONNECTION_RESULT_SUCCESS = 0;
    private static final long IDENTIFIER_REFRESH_INTERVAL_MILLIS = 3600000;
    private static final String LIMIT_TRACKING_COLUMN_NAME = "limit_tracking";
    private static final String TAG = "com.facebook.internal.AttributionIdentifiers";
    private static AttributionIdentifiers recentlyFetchedIdentifiers;
    private String androidAdvertiserId;
    private String androidInstallerPackage;
    private String attributionId;
    private long fetchTime;
    private boolean limitTracking;

    private static AttributionIdentifiers getAndroidId(Context context) {
        AttributionIdentifiers androidIdViaReflection = getAndroidIdViaReflection(context);
        if (androidIdViaReflection != null) {
            return androidIdViaReflection;
        }
        AttributionIdentifiers androidIdViaService = getAndroidIdViaService(context);
        return androidIdViaService == null ? new AttributionIdentifiers() : androidIdViaService;
    }

    private static AttributionIdentifiers getAndroidIdViaReflection(Context context) {
        Method methodQuietly;
        Object objInvokeMethodQuietly;
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                throw new FacebookException("getAndroidId cannot be called on the main thread.");
            }
            Method methodQuietly2 = Utility.getMethodQuietly("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", (Class<?>[]) new Class[]{Context.class});
            if (methodQuietly2 == null) {
                return null;
            }
            Object objInvokeMethodQuietly2 = Utility.invokeMethodQuietly(null, methodQuietly2, context);
            if (!(objInvokeMethodQuietly2 instanceof Integer) || ((Integer) objInvokeMethodQuietly2).intValue() != 0 || (methodQuietly = Utility.getMethodQuietly("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", (Class<?>[]) new Class[]{Context.class})) == null || (objInvokeMethodQuietly = Utility.invokeMethodQuietly(null, methodQuietly, context)) == null) {
                return null;
            }
            Method methodQuietly3 = Utility.getMethodQuietly(objInvokeMethodQuietly.getClass(), "getId", (Class<?>[]) new Class[0]);
            Method methodQuietly4 = Utility.getMethodQuietly(objInvokeMethodQuietly.getClass(), "isLimitAdTrackingEnabled", (Class<?>[]) new Class[0]);
            if (methodQuietly3 != null && methodQuietly4 != null) {
                AttributionIdentifiers attributionIdentifiers = new AttributionIdentifiers();
                attributionIdentifiers.androidAdvertiserId = (String) Utility.invokeMethodQuietly(objInvokeMethodQuietly, methodQuietly3, new Object[0]);
                attributionIdentifiers.limitTracking = ((Boolean) Utility.invokeMethodQuietly(objInvokeMethodQuietly, methodQuietly4, new Object[0])).booleanValue();
                return attributionIdentifiers;
            }
            return null;
        } catch (Exception e) {
            Utility.logd("android_id", e);
            return null;
        }
    }

    private static AttributionIdentifiers getAndroidIdViaService(Context context) {
        GoogleAdServiceConnection googleAdServiceConnection = new GoogleAdServiceConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        if (context.bindService(intent, googleAdServiceConnection, 1)) {
            try {
                GoogleAdInfo googleAdInfo = new GoogleAdInfo(googleAdServiceConnection.getBinder());
                AttributionIdentifiers attributionIdentifiers = new AttributionIdentifiers();
                attributionIdentifiers.androidAdvertiserId = googleAdInfo.getAdvertiserId();
                attributionIdentifiers.limitTracking = googleAdInfo.isTrackingLimited();
                return attributionIdentifiers;
            } catch (Exception e) {
                Utility.logd("android_id", e);
                return null;
            } finally {
                context.unbindService(googleAdServiceConnection);
            }
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x006d A[Catch: all -> 0x00d3, Exception -> 0x00d5, TryCatch #4 {Exception -> 0x00d5, all -> 0x00d3, blocks: (B:11:0x0034, B:13:0x004b, B:19:0x0067, B:21:0x006d, B:23:0x0071, B:25:0x0076, B:15:0x0053, B:17:0x005f), top: B:64:0x0034 }] */
    /* JADX WARN: Code duplicated, block: B:23:0x0071 A[Catch: all -> 0x00d3, Exception -> 0x00d5, TryCatch #4 {Exception -> 0x00d5, all -> 0x00d3, blocks: (B:11:0x0034, B:13:0x004b, B:19:0x0067, B:21:0x006d, B:23:0x0071, B:25:0x0076, B:15:0x0053, B:17:0x005f), top: B:64:0x0034 }] */
    /* JADX WARN: Code duplicated, block: B:25:0x0076 A[Catch: all -> 0x00d3, Exception -> 0x00d5, TRY_LEAVE, TryCatch #4 {Exception -> 0x00d5, all -> 0x00d3, blocks: (B:11:0x0034, B:13:0x004b, B:19:0x0067, B:21:0x006d, B:23:0x0071, B:25:0x0076, B:15:0x0053, B:17:0x005f), top: B:64:0x0034 }] */
    /* JADX WARN: Code duplicated, block: B:29:0x0089  */
    /* JADX WARN: Code duplicated, block: B:30:0x008a A[Catch: all -> 0x00ca, Exception -> 0x00ce, TryCatch #5 {Exception -> 0x00ce, all -> 0x00ca, blocks: (B:27:0x0083, B:30:0x008a, B:33:0x00a0, B:35:0x00a6, B:40:0x00c0), top: B:63:0x0083 }] */
    /* JADX WARN: Code duplicated, block: B:37:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:42:0x00c6  */
    /* JADX WARN: Code duplicated, block: B:59:0x00f7  */
    /* JADX WARN: Code duplicated, block: B:63:0x0083 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static AttributionIdentifiers getAttributionIdentifiers(Context context) {
        Cursor cursor;
        Uri uri;
        Uri uri2;
        String installerPackageName;
        Cursor cursorQuery;
        int columnIndex;
        int columnIndex2;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.e(TAG, "getAttributionIdentifiers should not be called from the main thread");
        }
        if (recentlyFetchedIdentifiers != null) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            AttributionIdentifiers attributionIdentifiers = recentlyFetchedIdentifiers;
            if (jCurrentTimeMillis - attributionIdentifiers.fetchTime < 3600000) {
                return attributionIdentifiers;
            }
        }
        AttributionIdentifiers androidId = getAndroidId(context);
        Cursor cursor2 = null;
        try {
            String[] strArr = {ATTRIBUTION_ID_COLUMN_NAME, ANDROID_ID_COLUMN_NAME, LIMIT_TRACKING_COLUMN_NAME};
            if (context.getPackageManager().resolveContentProvider(ATTRIBUTION_ID_CONTENT_PROVIDER, 0) != null) {
                uri2 = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
            } else {
                if (context.getPackageManager().resolveContentProvider(ATTRIBUTION_ID_CONTENT_PROVIDER_WAKIZASHI, 0) != null) {
                    uri2 = Uri.parse("content://com.facebook.wakizashi.provider.AttributionIdProvider");
                } else {
                    uri = null;
                }
                installerPackageName = getInstallerPackageName(context);
                if (installerPackageName != null) {
                    androidId.androidInstallerPackage = installerPackageName;
                }
                if (uri == null) {
                    return cacheAndReturnIdentifiers(androidId);
                }
                cursorQuery = context.getContentResolver().query(uri, strArr, null, null, null);
                if (cursorQuery != null) {
                    try {
                        if (!cursorQuery.moveToFirst()) {
                            int columnIndex3 = cursorQuery.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME);
                            columnIndex = cursorQuery.getColumnIndex(ANDROID_ID_COLUMN_NAME);
                            columnIndex2 = cursorQuery.getColumnIndex(LIMIT_TRACKING_COLUMN_NAME);
                            androidId.attributionId = cursorQuery.getString(columnIndex3);
                            if (columnIndex > 0 && columnIndex2 > 0 && androidId.getAndroidAdvertiserId() == null) {
                                androidId.androidAdvertiserId = cursorQuery.getString(columnIndex);
                                androidId.limitTracking = Boolean.parseBoolean(cursorQuery.getString(columnIndex2));
                            }
                            if (cursorQuery != null) {
                                cursorQuery.close();
                            }
                            return cacheAndReturnIdentifiers(androidId);
                        }
                    } catch (Exception e) {
                        cursor = cursorQuery;
                        e = e;
                        try {
                            Utility.logd(TAG, "Caught unexpected exception in getAttributionId(): " + e.toString());
                            if (cursor != null) {
                                cursor.close();
                            }
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            cursor2 = cursor;
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        cursor2 = cursorQuery;
                        th = th2;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                }
                AttributionIdentifiers attributionIdentifiersCacheAndReturnIdentifiers = cacheAndReturnIdentifiers(androidId);
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return attributionIdentifiersCacheAndReturnIdentifiers;
            }
            uri = uri2;
            installerPackageName = getInstallerPackageName(context);
            if (installerPackageName != null) {
                androidId.androidInstallerPackage = installerPackageName;
            }
            if (uri == null) {
                return cacheAndReturnIdentifiers(androidId);
            }
            cursorQuery = context.getContentResolver().query(uri, strArr, null, null, null);
            if (cursorQuery != null) {
                if (!cursorQuery.moveToFirst()) {
                    int columnIndex4 = cursorQuery.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME);
                    columnIndex = cursorQuery.getColumnIndex(ANDROID_ID_COLUMN_NAME);
                    columnIndex2 = cursorQuery.getColumnIndex(LIMIT_TRACKING_COLUMN_NAME);
                    androidId.attributionId = cursorQuery.getString(columnIndex4);
                    if (columnIndex > 0) {
                        androidId.androidAdvertiserId = cursorQuery.getString(columnIndex);
                        androidId.limitTracking = Boolean.parseBoolean(cursorQuery.getString(columnIndex2));
                    }
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    return cacheAndReturnIdentifiers(androidId);
                }
            }
            AttributionIdentifiers attributionIdentifiersCacheAndReturnIdentifiers2 = cacheAndReturnIdentifiers(androidId);
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return attributionIdentifiersCacheAndReturnIdentifiers2;
        } catch (Exception e2) {
            e = e2;
            cursor = null;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static AttributionIdentifiers getCachedIdentifiers() {
        return recentlyFetchedIdentifiers;
    }

    private static AttributionIdentifiers cacheAndReturnIdentifiers(AttributionIdentifiers attributionIdentifiers) {
        attributionIdentifiers.fetchTime = System.currentTimeMillis();
        recentlyFetchedIdentifiers = attributionIdentifiers;
        return attributionIdentifiers;
    }

    public String getAttributionId() {
        return this.attributionId;
    }

    public String getAndroidAdvertiserId() {
        if (FacebookSdk.isInitialized() && FacebookSdk.getAdvertiserIDCollectionEnabled()) {
            return this.androidAdvertiserId;
        }
        return null;
    }

    public String getAndroidInstallerPackage() {
        return this.androidInstallerPackage;
    }

    public boolean isTrackingLimited() {
        return this.limitTracking;
    }

    private static String getInstallerPackageName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            return packageManager.getInstallerPackageName(context.getPackageName());
        }
        return null;
    }

    private static final class GoogleAdServiceConnection implements ServiceConnection {
        private AtomicBoolean consumed;
        private final BlockingQueue<IBinder> queue;

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        private GoogleAdServiceConnection() {
            this.consumed = new AtomicBoolean(false);
            this.queue = new LinkedBlockingDeque();
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder != null) {
                try {
                    this.queue.put(iBinder);
                } catch (InterruptedException unused) {
                }
            }
        }

        public IBinder getBinder() throws InterruptedException {
            if (this.consumed.compareAndSet(true, true)) {
                throw new IllegalStateException("Binder already consumed");
            }
            return this.queue.take();
        }
    }

    private static final class GoogleAdInfo implements IInterface {
        private static final int FIRST_TRANSACTION_CODE = 1;
        private static final int SECOND_TRANSACTION_CODE = 2;
        private IBinder binder;

        GoogleAdInfo(IBinder iBinder) {
            this.binder = iBinder;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.binder;
        }

        public String getAdvertiserId() throws RemoteException {
            Parcel parcelObtain = Parcel.obtain();
            Parcel parcelObtain2 = Parcel.obtain();
            try {
                parcelObtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.binder.transact(1, parcelObtain, parcelObtain2, 0);
                parcelObtain2.readException();
                return parcelObtain2.readString();
            } finally {
                parcelObtain2.recycle();
                parcelObtain.recycle();
            }
        }

        public boolean isTrackingLimited() throws RemoteException {
            Parcel parcelObtain = Parcel.obtain();
            Parcel parcelObtain2 = Parcel.obtain();
            try {
                parcelObtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                parcelObtain.writeInt(1);
                this.binder.transact(2, parcelObtain, parcelObtain2, 0);
                parcelObtain2.readException();
                return parcelObtain2.readInt() != 0;
            } finally {
                parcelObtain2.recycle();
                parcelObtain.recycle();
            }
        }
    }
}
