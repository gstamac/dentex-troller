package com.facebook.appevents.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.facebook.FacebookSdk;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
class InAppPurchaseEventManager {
    private static final String AS_INTERFACE = "asInterface";
    private static final int CACHE_CLEAR_TIME_LIMIT_SEC = 604800;
    private static final String DETAILS_LIST = "DETAILS_LIST";
    private static final String GET_PURCHASES = "getPurchases";
    private static final String GET_PURCHASE_HISTORY = "getPurchaseHistory";
    private static final String GET_SKU_DETAILS = "getSkuDetails";
    private static final String INAPP = "inapp";
    private static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    private static final String INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    private static final String IN_APP_BILLING_SERVICE = "com.android.vending.billing.IInAppBillingService";
    private static final String IN_APP_BILLING_SERVICE_STUB = "com.android.vending.billing.IInAppBillingService$Stub";
    private static final String IS_BILLING_SUPPORTED = "isBillingSupported";
    private static final String ITEM_ID_LIST = "ITEM_ID_LIST";
    private static final String LAST_CLEARED_TIME = "LAST_CLEARED_TIME";
    private static final String LAST_LOGGED_TIME_SEC = "LAST_LOGGED_TIME_SEC";
    private static final int MAX_QUERY_PURCHASE_NUM = 30;
    private static final int PURCHASE_EXPIRE_TIME_SEC = 43200;
    private static final int PURCHASE_STOP_QUERY_TIME_SEC = 1200;
    private static final String RESPONSE_CODE = "RESPONSE_CODE";
    private static final int SKU_DETAIL_EXPIRE_TIME_SEC = 43200;
    private static final String SUBSCRIPTION = "subs";
    private static final long SUBSCRIPTION_HARTBEAT_INTERVAL = 86400;
    private static final String TAG = "com.facebook.appevents.internal.InAppPurchaseEventManager";
    private static final HashMap<String, Method> methodMap = new HashMap<>();
    private static final HashMap<String, Class<?>> classMap = new HashMap<>();
    private static final String PACKAGE_NAME = FacebookSdk.getApplicationContext().getPackageName();
    private static final String SKU_DETAILS_STORE = "com.facebook.internal.SKU_DETAILS";
    private static final SharedPreferences skuDetailSharedPrefs = FacebookSdk.getApplicationContext().getSharedPreferences(SKU_DETAILS_STORE, 0);
    private static final String PURCHASE_INAPP_STORE = "com.facebook.internal.PURCHASE";
    private static final SharedPreferences purchaseInappSharedPrefs = FacebookSdk.getApplicationContext().getSharedPreferences(PURCHASE_INAPP_STORE, 0);
    private static final String PURCHASE_SUBS_STORE = "com.facebook.internal.PURCHASE_SUBS";
    private static final SharedPreferences purchaseSubsSharedPrefs = FacebookSdk.getApplicationContext().getSharedPreferences(PURCHASE_SUBS_STORE, 0);

    InAppPurchaseEventManager() {
    }

    public static Object asInterface(Context context, IBinder iBinder) {
        return invokeMethod(context, IN_APP_BILLING_SERVICE_STUB, AS_INTERFACE, null, new Object[]{iBinder});
    }

    public static Map<String, String> getSkuDetails(Context context, ArrayList<String> arrayList, Object obj, boolean z) {
        Map<String, String> skuDetailsFromCache = readSkuDetailsFromCache(arrayList);
        ArrayList arrayList2 = new ArrayList();
        for (String str : arrayList) {
            if (!skuDetailsFromCache.containsKey(str)) {
                arrayList2.add(str);
            }
        }
        skuDetailsFromCache.putAll(getSkuDetailsFromGoogle(context, arrayList2, obj, z));
        return skuDetailsFromCache;
    }

    private static Map<String, String> getSkuDetailsFromGoogle(Context context, ArrayList<String> arrayList, Object obj, boolean z) {
        HashMap map = new HashMap();
        if (obj != null && !arrayList.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ITEM_ID_LIST, arrayList);
            Object[] objArr = new Object[4];
            objArr[0] = 3;
            objArr[1] = PACKAGE_NAME;
            objArr[2] = z ? SUBSCRIPTION : INAPP;
            objArr[3] = bundle;
            Object objInvokeMethod = invokeMethod(context, IN_APP_BILLING_SERVICE, GET_SKU_DETAILS, obj, objArr);
            if (objInvokeMethod != null) {
                Bundle bundle2 = (Bundle) objInvokeMethod;
                if (bundle2.getInt(RESPONSE_CODE) == 0) {
                    ArrayList<String> stringArrayList = bundle2.getStringArrayList(DETAILS_LIST);
                    if (stringArrayList != null && arrayList.size() == stringArrayList.size()) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            map.put(arrayList.get(i), stringArrayList.get(i));
                        }
                    }
                    writeSkuDetailsToCache(map);
                }
            }
        }
        return map;
    }

    private static Map<String, String> readSkuDetailsFromCache(ArrayList<String> arrayList) {
        HashMap map = new HashMap();
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        for (String str : arrayList) {
            String string = skuDetailSharedPrefs.getString(str, null);
            if (string != null) {
                String[] strArrSplit = string.split(";", 2);
                if (jCurrentTimeMillis - Long.parseLong(strArrSplit[0]) < 43200) {
                    map.put(str, strArrSplit[1]);
                }
            }
        }
        return map;
    }

    private static void writeSkuDetailsToCache(Map<String, String> map) {
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        SharedPreferences.Editor editorEdit = skuDetailSharedPrefs.edit();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            editorEdit.putString(entry.getKey(), jCurrentTimeMillis + ";" + entry.getValue());
        }
        editorEdit.apply();
    }

    private static Boolean isBillingSupported(Context context, Object obj, String str) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        Object objInvokeMethod = invokeMethod(context, IN_APP_BILLING_SERVICE, IS_BILLING_SUPPORTED, obj, new Object[]{3, PACKAGE_NAME, str});
        if (objInvokeMethod != null && ((Integer) objInvokeMethod).intValue() == 0) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public static ArrayList<String> getPurchasesInapp(Context context, Object obj) {
        return filterPurchasesInapp(getPurchases(context, obj, INAPP));
    }

    public static ArrayList<String> getPurchasesSubsExpire(Context context, Object obj) {
        ArrayList<String> arrayList = new ArrayList<>();
        Map<String, ?> all = purchaseSubsSharedPrefs.getAll();
        if (all.isEmpty()) {
            return arrayList;
        }
        ArrayList<String> purchases = getPurchases(context, obj, SUBSCRIPTION);
        HashSet hashSet = new HashSet();
        Iterator<String> it = purchases.iterator();
        while (it.hasNext()) {
            try {
                hashSet.add(new JSONObject(it.next()).getString("productId"));
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing purchase json", e);
            }
        }
        HashSet<String> hashSet2 = new HashSet();
        Iterator<Map.Entry<String, ?>> it2 = all.entrySet().iterator();
        while (it2.hasNext()) {
            String key = it2.next().getKey();
            if (!hashSet.contains(key)) {
                hashSet2.add(key);
            }
        }
        SharedPreferences.Editor editorEdit = purchaseSubsSharedPrefs.edit();
        for (String str : hashSet2) {
            SharedPreferences sharedPreferences = purchaseSubsSharedPrefs;
            String string = sharedPreferences.getString(str, "");
            editorEdit.remove(str);
            if (!string.isEmpty()) {
                arrayList.add(sharedPreferences.getString(str, ""));
            }
        }
        editorEdit.apply();
        return arrayList;
    }

    public static Map<String, SubscriptionType> getPurchasesSubs(Context context, Object obj) {
        HashMap map = new HashMap();
        for (String str : getPurchases(context, obj, SUBSCRIPTION)) {
            SubscriptionType subsType = getSubsType(str);
            if (subsType != SubscriptionType.DUPLICATED && subsType != SubscriptionType.UNKNOWN) {
                map.put(str, subsType);
            }
        }
        return map;
    }

    private static SubscriptionType getSubsType(String str) {
        SubscriptionType subscriptionType;
        try {
            long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("productId");
            SharedPreferences sharedPreferences = purchaseSubsSharedPrefs;
            String string2 = sharedPreferences.getString(string, "");
            JSONObject jSONObject2 = string2.isEmpty() ? new JSONObject() : new JSONObject(string2);
            if (jSONObject2.optString("purchaseToken").equals(jSONObject.get("purchaseToken"))) {
                subscriptionType = null;
            } else {
                subscriptionType = jCurrentTimeMillis - (jSONObject.getLong("purchaseTime") / 1000) < 43200 ? SubscriptionType.NEW : SubscriptionType.HEARTBEAT;
            }
            if (subscriptionType == null && !string2.isEmpty()) {
                boolean z = jSONObject2.getBoolean("autoRenewing");
                boolean z2 = jSONObject.getBoolean("autoRenewing");
                if (!z2 && z) {
                    subscriptionType = SubscriptionType.CANCEL;
                } else if (!z && z2) {
                    subscriptionType = SubscriptionType.RESTORE;
                }
            }
            if (subscriptionType == null && !string2.isEmpty()) {
                if (jCurrentTimeMillis - jSONObject2.getLong(LAST_LOGGED_TIME_SEC) > SUBSCRIPTION_HARTBEAT_INTERVAL) {
                    subscriptionType = SubscriptionType.HEARTBEAT;
                } else {
                    subscriptionType = SubscriptionType.DUPLICATED;
                }
            }
            if (subscriptionType != SubscriptionType.DUPLICATED) {
                jSONObject.put(LAST_LOGGED_TIME_SEC, jCurrentTimeMillis);
                sharedPreferences.edit().putString(string, jSONObject.toString()).apply();
            }
            return subscriptionType;
        } catch (JSONException e) {
            Log.e(TAG, "parsing purchase failure: ", e);
            return SubscriptionType.UNKNOWN;
        }
    }

    /* JADX WARN: Code duplicated, block: B:15:0x004b  */
    /* JADX WARN: Code duplicated, block: B:18:0x0050 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:20:0x0052 A[EDGE_INSN: B:20:0x0052->B:19:0x0052 BREAK  A[LOOP:0: B:8:0x0015->B:23:?], SYNTHETIC] */
    private static ArrayList<String> getPurchases(Context context, Object obj, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (obj != null && isBillingSupported(context, obj, str).booleanValue()) {
            int size = 0;
            String string = null;
            do {
                Object objInvokeMethod = invokeMethod(context, IN_APP_BILLING_SERVICE, GET_PURCHASES, obj, new Object[]{3, PACKAGE_NAME, str, string});
                if (objInvokeMethod == null) {
                    string = null;
                    if (size < 30) {
                        break;
                        break;
                    }
                } else {
                    Bundle bundle = (Bundle) objInvokeMethod;
                    if (bundle.getInt(RESPONSE_CODE) == 0) {
                        ArrayList<String> stringArrayList = bundle.getStringArrayList(INAPP_PURCHASE_DATA_LIST);
                        if (stringArrayList == null) {
                            break;
                        }
                        size += stringArrayList.size();
                        arrayList.addAll(stringArrayList);
                        string = bundle.getString(INAPP_CONTINUATION_TOKEN);
                    } else {
                        string = null;
                    }
                    if (size < 30) {
                        break;
                    }
                }
            } while (string != null);
        }
        return arrayList;
    }

    public static ArrayList<String> getPurchaseHistoryInapp(Context context, Object obj) {
        Class<?> cls;
        ArrayList<String> arrayList = new ArrayList<>();
        return (obj == null || (cls = getClass(context, IN_APP_BILLING_SERVICE)) == null || getMethod(cls, GET_PURCHASE_HISTORY) == null) ? arrayList : filterPurchasesInapp(getPurchaseHistory(context, obj, INAPP));
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0090  */
    private static ArrayList<String> getPurchaseHistory(Context context, Object obj, String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (isBillingSupported(context, obj, str).booleanValue()) {
            int i = 0;
            Boolean bool = false;
            String string = null;
            do {
                Object objInvokeMethod = invokeMethod(context, IN_APP_BILLING_SERVICE, GET_PURCHASE_HISTORY, obj, new Object[]{6, PACKAGE_NAME, str, string, new Bundle()});
                if (objInvokeMethod != null) {
                    long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
                    Bundle bundle = (Bundle) objInvokeMethod;
                    if (bundle.getInt(RESPONSE_CODE) == 0) {
                        int i2 = i;
                        for (String str2 : bundle.getStringArrayList(INAPP_PURCHASE_DATA_LIST)) {
                            try {
                                if (jCurrentTimeMillis - (new JSONObject(str2).getLong("purchaseTime") / 1000) > 1200) {
                                    bool = true;
                                    break;
                                }
                                arrayList.add(str2);
                                i2++;
                            } catch (JSONException e) {
                                Log.e(TAG, "parsing purchase failure: ", e);
                            }
                        }
                        string = bundle.getString(INAPP_CONTINUATION_TOKEN);
                        i = i2;
                    } else {
                        string = null;
                    }
                } else {
                    string = null;
                }
                if (i >= 30 || string == null) {
                    break;
                }
            } while (!bool.booleanValue());
        }
        return arrayList;
    }

    private static ArrayList<String> filterPurchasesInapp(ArrayList<String> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        SharedPreferences.Editor editorEdit = purchaseInappSharedPrefs.edit();
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        for (String str : arrayList) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.getString("productId");
                long j = jSONObject.getLong("purchaseTime");
                String string2 = jSONObject.getString("purchaseToken");
                if (jCurrentTimeMillis - (j / 1000) <= 43200 && !purchaseInappSharedPrefs.getString(string, "").equals(string2)) {
                    editorEdit.putString(string, string2);
                    arrayList2.add(str);
                }
            } catch (JSONException e) {
                Log.e(TAG, "parsing purchase failure: ", e);
            }
        }
        editorEdit.apply();
        return arrayList2;
    }

    /* JADX WARN: Code duplicated, block: B:23:0x004a  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private static Method getMethod(Class<?> cls, String str) {
        byte b;
        Class<?>[] clsArr;
        HashMap<String, Method> map = methodMap;
        Method declaredMethod = map.get(str);
        if (declaredMethod != null) {
            return declaredMethod;
        }
        try {
            switch (str.hashCode()) {
                case -1801122596:
                    if (!str.equals(GET_PURCHASES)) {
                        b = -1;
                    } else {
                        b = 3;
                    }
                    break;
                case -1450694211:
                    if (!str.equals(IS_BILLING_SUPPORTED)) {
                        b = -1;
                    } else {
                        b = 2;
                    }
                    break;
                case -1123215065:
                    if (!str.equals(AS_INTERFACE)) {
                        b = -1;
                    } else {
                        b = 0;
                    }
                    break;
                case -594356707:
                    if (!str.equals(GET_PURCHASE_HISTORY)) {
                        b = -1;
                    } else {
                        b = 4;
                    }
                    break;
                case -573310373:
                    if (!str.equals(GET_SKU_DETAILS)) {
                        b = -1;
                    } else {
                        b = 1;
                    }
                    break;
                default:
                    b = -1;
                    break;
            }
            if (b == 0) {
                clsArr = new Class[]{IBinder.class};
            } else if (b == 1) {
                clsArr = new Class[]{Integer.TYPE, String.class, String.class, Bundle.class};
            } else if (b == 2) {
                clsArr = new Class[]{Integer.TYPE, String.class, String.class};
            } else if (b != 3) {
                clsArr = b != 4 ? null : new Class[]{Integer.TYPE, String.class, String.class, String.class, Bundle.class};
            } else {
                clsArr = new Class[]{Integer.TYPE, String.class, String.class, String.class};
            }
            declaredMethod = cls.getDeclaredMethod(str, clsArr);
            map.put(str, declaredMethod);
            return declaredMethod;
        } catch (NoSuchMethodException e) {
            Log.e(TAG, cls.getName() + "." + str + " method not found", e);
            return declaredMethod;
        }
    }

    private static Class<?> getClass(Context context, String str) {
        HashMap<String, Class<?>> map = classMap;
        Class<?> clsLoadClass = map.get(str);
        if (clsLoadClass != null) {
            return clsLoadClass;
        }
        try {
            clsLoadClass = context.getClassLoader().loadClass(str);
            map.put(str, clsLoadClass);
            return clsLoadClass;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, str + " is not available, please add " + str + " to the project.", e);
            return clsLoadClass;
        }
    }

    private static Object invokeMethod(Context context, String str, String str2, Object obj, Object[] objArr) {
        Method method;
        Class<?> cls = getClass(context, str);
        if (cls == null || (method = getMethod(cls, str2)) == null) {
            return null;
        }
        if (obj != null) {
            obj = cls.cast(obj);
        }
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Illegal access to method " + cls.getName() + "." + method.getName(), e);
            return null;
        } catch (InvocationTargetException e2) {
            Log.e(TAG, "Invocation target exception in " + cls.getName() + "." + method.getName(), e2);
            return null;
        }
    }

    public static void clearSkuDetailsCache() {
        long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
        SharedPreferences sharedPreferences = skuDetailSharedPrefs;
        long j = sharedPreferences.getLong(LAST_CLEARED_TIME, 0L);
        if (j == 0) {
            sharedPreferences.edit().putLong(LAST_CLEARED_TIME, jCurrentTimeMillis).apply();
        } else if (jCurrentTimeMillis - j > 604800) {
            sharedPreferences.edit().clear().putLong(LAST_CLEARED_TIME, jCurrentTimeMillis).apply();
        }
    }
}
