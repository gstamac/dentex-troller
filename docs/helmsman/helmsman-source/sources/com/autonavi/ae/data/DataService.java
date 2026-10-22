package com.autonavi.ae.data;

/* JADX INFO: loaded from: classes.dex */
public class DataService {
    private static DataService mService;
    long mPtr;

    public static native String getEngineVersion();

    private native int init();

    private native void unInit();

    public native int deleteCityDB(int i);

    public native int getAdminCode(int i, int i2);

    public native String getDataVersion(int i);

    public native int prepareUpdateCityDB(int i);

    public native int updateCityDBFinished(int i);

    private DataService() {
    }

    public static DataService getInstance() {
        if (mService == null) {
            DataService dataService = new DataService();
            mService = dataService;
            dataService.init();
        }
        return mService;
    }

    public static void destroy() {
        DataService dataService = mService;
        if (dataService != null) {
            dataService.unInit();
            mService = null;
        }
    }
}
