package com.autonavi.ae.search;

import com.autonavi.ae.search.interfaces.OnSearchResultListener;
import com.autonavi.ae.search.log.GLog;
import com.autonavi.ae.search.model.GADAREAEXTRAINFO;
import com.autonavi.ae.search.model.GADAREAINFO;
import com.autonavi.ae.search.model.GObjectID;
import com.autonavi.ae.search.model.GPoiResult;
import com.autonavi.ae.search.model.SearchStatus;
import java.util.Hashtable;

/* JADX INFO: loaded from: classes.dex */
public class SearchEngine extends NativeSearchEngine {
    private static int mRequestID = 1;
    private Hashtable<Integer, OnSearchResultListener> mSearchCallBackListenerList = new Hashtable<>();
    private boolean mIsInit = false;
    private Object o = new Object();
    private long mNativeId = 0;
    private final String TAG = "SearchEngine";
    private INativeSearchObserver obServer = new INativeSearchObserver() { // from class: com.autonavi.ae.search.SearchEngine.1
        @Override // com.autonavi.ae.search.INativeSearchObserver
        public void onGetSearchResult(int i, int i2, GPoiResult gPoiResult) {
            OnSearchResultListener onSearchResultListener;
            if (GLog.isLogShow()) {
                if (gPoiResult != null) {
                    GLog.v("SearchEngine", "onGetSearchResult id=" + i + ",status=" + i2 + ",size=" + gPoiResult.getNumberOfItemGet());
                } else {
                    GLog.v("SearchEngine", "onGetSearchResult id=" + i + ",status=" + i2 + ",result=NULL");
                }
            }
            synchronized (SearchEngine.this.o) {
                onSearchResultListener = (OnSearchResultListener) SearchEngine.this.mSearchCallBackListenerList.remove(Integer.valueOf(i));
            }
            if (onSearchResultListener != null) {
                onSearchResultListener.onGetSearchResult(i2, gPoiResult);
            }
        }

        @Override // com.autonavi.ae.search.INativeSearchObserver
        public void onSetPoiParam(int i) {
            GLog.v("wmh", "SearchEngine.onSetPoiParam");
        }

        @Override // com.autonavi.ae.search.INativeSearchObserver
        public void onGetPoiParam(int i, int i2, int i3) {
            GLog.v("wmh", "SearchEngine.onGetPoiParam");
        }

        @Override // com.autonavi.ae.search.INativeSearchObserver
        public void onGetPoiCategoryList(int i, int i2) {
            GLog.v("wmh", "SearchEngine.onGetPoiCategoryList status=" + i + ",lNumberOfCategory=" + i2);
        }

        @Override // com.autonavi.ae.search.INativeSearchObserver
        public void onGetAdareaInfo(int i, int i2, GADAREAINFO[] gadareainfoArr) {
            GLog.v("wmh", "SearchEngine.onGetAdareaInfo");
        }

        @Override // com.autonavi.ae.search.INativeSearchObserver
        public void onGetSuggestArea() {
            GLog.v("wmh", "SearchEngine.onGetSuggestArea");
        }
    };

    public int init() {
        int iNativeCreateSearcher = nativeCreateSearcher();
        if (GLog.isLogShow()) {
            GLog.v("SearchEngine", "nativeCreateSearcher.naviid=" + iNativeCreateSearcher);
        }
        if (iNativeCreateSearcher == 0) {
            return -1;
        }
        int iNativeInit = nativeInit("", "", "", null, 20, 0);
        if (iNativeInit == 0) {
            this.mIsInit = true;
            return addSearchObserver(this.obServer);
        }
        this.mIsInit = false;
        return iNativeInit;
    }

    public int setMccPath(String str) {
        return nativeSetMccPath(str, getRequestID());
    }

    public int cancelQuery() {
        if (!isInit()) {
            return -2;
        }
        if (GLog.isLogShow()) {
            GLog.v("SearchEngine", "cancelQuery start");
        }
        return nativeAbortSearch();
    }

    private int addSearchObserver(INativeSearchObserver iNativeSearchObserver) {
        if (isInit()) {
            return nativeAddSearchObserver(iNativeSearchObserver);
        }
        return -2;
    }

    public boolean isInit() {
        return this.mIsInit && this.mNativeId != 0;
    }

    public int destroy() {
        if (GLog.isLogShow()) {
            GLog.v("SearchEngine", "destroy start");
        }
        synchronized (this.o) {
            this.mSearchCallBackListenerList.clear();
        }
        this.mIsInit = false;
        return nativeDestroy();
    }

    public int startSearch(int i, String str, int i2, float f, float f2, int i3, int i4, GObjectID[] gObjectIDArr) {
        return startSearch(i, str, i2, f, f2, i3, i4, gObjectIDArr, null);
    }

    public int preSearch(int i, String str, int i2, float f, float f2, int i3, OnSearchResultListener onSearchResultListener) {
        return startSearch(i, 1, str, i2, f, f2, i3, onSearchResultListener);
    }

    public int startSearch(int i, String str, int i2, float f, float f2, int i3, OnSearchResultListener onSearchResultListener) {
        return startSearch(i, 2, str, i2, f, f2, i3, onSearchResultListener);
    }

    public int startSearch(int i, int i2, String str, int i3, float f, float f2, int i4, OnSearchResultListener onSearchResultListener) {
        if (!isInit()) {
            return -2;
        }
        int i5 = (f <= 0.0f || f2 <= 0.0f) ? 8 : i;
        int requestID = getRequestID();
        if (onSearchResultListener != null) {
            synchronized (this.o) {
                this.mSearchCallBackListenerList.put(Integer.valueOf(requestID), onSearchResultListener);
            }
        }
        SearchStatus searchStatusNativeStartSearch = nativeStartSearch(i5, i2, str, i3, f, f2, 0, i4, requestID, 0, null);
        if (GLog.isLogShow()) {
            GLog.v("SearchEngine", "startSearch requestID=" + searchStatusNativeStartSearch.getEventId() + "strKey=" + str + ",nAdcode=" + i3 + ",status=" + searchStatusNativeStartSearch.getStatus());
        }
        return searchStatusNativeStartSearch.getStatus();
    }

    private static synchronized int getRequestID() {
        int i;
        if (mRequestID > 60000) {
            mRequestID = 1;
        }
        i = mRequestID;
        mRequestID = i + 1;
        return i;
    }

    public int startSearch(int i, String str, int i2, float f, float f2, int i3, int i4, GObjectID[] gObjectIDArr, OnSearchResultListener onSearchResultListener) {
        if (!isInit()) {
            return -2;
        }
        int requestID = getRequestID();
        if (onSearchResultListener != null) {
            synchronized (this.o) {
                this.mSearchCallBackListenerList.put(Integer.valueOf(requestID), onSearchResultListener);
            }
        }
        SearchStatus searchStatusNativeStartSearch = nativeStartSearch(i, 2, str, i2, f, f2, 0, i3, requestID, i4, gObjectIDArr);
        if (GLog.isLogShow()) {
            GLog.v("SearchEngine", "startSearch requestID=" + searchStatusNativeStartSearch.getEventId() + "strKey=" + str + ",nAdcode=" + i2 + ",status=" + searchStatusNativeStartSearch.getStatus());
        }
        return searchStatusNativeStartSearch.getStatus();
    }

    public int searchNearestPoi(float f, float f2, OnSearchResultListener onSearchResultListener) {
        return startSearch(9, 2, (String) null, 0, f, f2, 10, onSearchResultListener);
    }

    public int getPoiDetail(String str, float f, float f2, OnSearchResultListener onSearchResultListener) {
        return startSearch(10, 2, str, 0, f, f2, 10, onSearchResultListener);
    }

    public int setPoiParam(int i, int i2) {
        if (isInit()) {
            return nativeSetPoiParam(i, i2);
        }
        return -2;
    }

    public int getPoiParam(int i) {
        if (isInit()) {
            return nativeGetPoiParam(i);
        }
        return -2;
    }

    public boolean isExistByAdCode(int i) {
        if (isInit()) {
            return NativeSearchPub.DbExists(i);
        }
        return false;
    }

    public static synchronized String getDataVersion(int i) {
        return NativeSearchPub.GetDataVersion(i);
    }

    public static synchronized String getEngineVersion() {
        return NativeSearchPub.GetVersion();
    }

    public int getPoiCategoryList(int i) {
        if (isInit()) {
            return nativeGetPoiCategoryList(i);
        }
        return -2;
    }

    public GADAREAEXTRAINFO GetAdareaInfo(int i) {
        if (isInit()) {
            return NativeSearchPub.GetAdareaInfo(i);
        }
        return null;
    }

    public int searchAdareaInfo(String str) {
        if (isInit()) {
            return nativeSearchAdareaInfo(0, str);
        }
        return -2;
    }
}
