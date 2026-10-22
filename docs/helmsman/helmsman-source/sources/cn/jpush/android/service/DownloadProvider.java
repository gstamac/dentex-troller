package cn.jpush.android.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import cn.jiguang.ad.d;
import cn.jiguang.aj.e;
import cn.jiguang.e.a;
import cn.jiguang.internal.JCoreInternalHelper;

/* JADX INFO: loaded from: classes.dex */
public class DownloadProvider extends ContentProvider {
    private static final String TAG = "DownloadProvider";

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        try {
            d.b(TAG, "method:" + str + ", arg=" + str2);
            return JCoreInternalHelper.getInstance().directHandle(getContext(), str, str2, bundle);
        } catch (Throwable th) {
            d.f(TAG, "call e:" + th);
            return null;
        }
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return a.a(getContext(), uri);
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return false;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        d.b(TAG, "DownloadProvider query:" + uri);
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 4);
            e.a(getContext(), "waked", bundle);
            return null;
        } catch (Throwable th) {
            d.f(TAG, "wake error:" + th.getMessage());
            return null;
        }
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
