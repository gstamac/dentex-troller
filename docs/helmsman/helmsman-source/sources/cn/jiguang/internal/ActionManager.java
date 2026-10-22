package cn.jiguang.internal;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jiguang.ad.d;
import cn.jiguang.api.JDispatchAction;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ActionManager {
    private static final String TAG = "ActionManager";
    private static volatile ActionManager detachDataManager;
    private static final Object lock = new Object();
    private static HashMap<String, JDispatchAction> actionMap = new HashMap<>();
    private static HashMap<String, String> actionStringMap = new HashMap<>();

    public static HashMap<String, String> getActionMap() {
        return actionStringMap;
    }

    public static ActionManager getInstance() {
        if (detachDataManager == null) {
            synchronized (lock) {
                if (detachDataManager == null) {
                    detachDataManager = new ActionManager();
                }
            }
        }
        return detachDataManager;
    }

    public void addAction(String str, String str2) {
        Log.d(TAG, "addAction type:" + str + ",action:" + str2);
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        if (actionMap.containsKey(str)) {
            d.b(TAG, "has same type action");
            return;
        }
        try {
            Object objNewInstance = Class.forName(str2).newInstance();
            if (!(objNewInstance instanceof JDispatchAction)) {
                d.f(TAG, "this action is not a JDispatchAction,please check and extends JDispatchAction");
            } else {
                actionStringMap.put(str, str2);
                actionMap.put(str, (JDispatchAction) objNewInstance);
            }
        } catch (Throwable th) {
            d.g(TAG, "#unexcepted - instance " + str2 + " class failed:" + th);
        }
    }

    public void handleMessage(Context context, String str, Object obj) {
        d.a(TAG, "onSended type:" + str + ",actionMap size:" + actionMap.size());
        if (TextUtils.isEmpty(str)) {
            for (Map.Entry<String, JDispatchAction> entry : actionMap.entrySet()) {
                entry.getValue().handleMessage(context, entry.getKey(), obj);
            }
            return;
        }
        JDispatchAction jDispatchAction = actionMap.get(str);
        if (jDispatchAction != null) {
            jDispatchAction.handleMessage(context, str, obj);
        }
    }

    public Map<Integer, Bundle> loadPInfo() {
        int i;
        HashMap map = new HashMap();
        for (Map.Entry<String, JDispatchAction> entry : actionMap.entrySet()) {
            Bundle pInfo = entry.getValue().getPInfo(entry.getKey());
            if (pInfo != null && (i = pInfo.getInt("pid", 0)) > 0) {
                map.put(Integer.valueOf(i), pInfo);
            }
        }
        return map;
    }

    public boolean wrapSdkVersionInfo(JSONObject jSONObject) {
        if (jSONObject == null) {
            d.f(TAG, "wrapSdkVersionInfo failed ,container is null");
            return false;
        }
        try {
            jSONObject.put("core_sdk_ver", cn.jiguang.a.a.b);
            for (Map.Entry<String, JDispatchAction> entry : actionMap.entrySet()) {
                JDispatchAction value = entry.getValue();
                jSONObject.put(value.getReportVersionKey(entry.getKey()), value.getSdkVersion(entry.getKey()));
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }
}
