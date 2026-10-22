package cn.jpush.android.service;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import cn.jiguang.ad.d;
import cn.jiguang.android.IDataShare;
import cn.jiguang.f.a;
import cn.jiguang.internal.JConstants;
import cn.jiguang.internal.JCoreInternalHelper;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DataShare extends IDataShare.Stub {
    private static final String TAG = "DataShare";
    private static final Map<String, IDataShare> aidlMap = new HashMap();
    private static boolean isBinding = false;

    public static IDataShare getInstance(String str) {
        return aidlMap.get(str);
    }

    public static void init(IDataShare iDataShare, String str) {
        if (iDataShare != getInstance(str)) {
            aidlMap.put(str, iDataShare);
            d.b(TAG, str + "'s aidl created");
            try {
                Context appContext = JConstants.getAppContext(null);
                if (appContext != null) {
                    String strA = a.a(appContext);
                    if (appContext.getPackageName().equals(strA)) {
                        iDataShare.bind(new DataShare(), strA);
                    }
                }
            } catch (RemoteException e) {
                d.h(TAG, "bind failed=" + e);
            }
        }
        isBinding = false;
    }

    public static boolean isBinding() {
        return isBinding;
    }

    public static void setBinding() {
        isBinding = true;
    }

    @Override // cn.jiguang.android.IDataShare
    public String bind(IDataShare iDataShare, String str) {
        aidlMap.put(str, iDataShare);
        d.b(TAG, str + "'s aidl bound");
        return a.a((Context) null);
    }

    @Override // cn.jiguang.android.IDataShare
    public Bundle execute(String str, String str2, Bundle bundle) {
        try {
            return JCoreInternalHelper.getInstance().directHandle(JConstants.mApplicationContext, str, str2, bundle);
        } catch (Throwable th) {
            d.g(TAG, "onAction error:" + th);
            return null;
        }
    }

    @Override // cn.jiguang.android.IDataShare
    public IBinder getBinderByType(String str, String str2) {
        return null;
    }

    @Override // cn.jiguang.android.IDataShare
    public void onAction(String str, String str2, Bundle bundle) {
        try {
            JCoreInternalHelper.getInstance().directHandle(JConstants.mApplicationContext, str, str2, bundle);
        } catch (Throwable th) {
            d.g(TAG, "onAction error:" + th);
        }
    }
}
