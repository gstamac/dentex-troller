package cn.jpush.android.r;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.api.CallBackParams;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.helper.g;
import cn.jpush.android.local.JPushConstants;
import cn.jpush.android.service.TagAliasReceiver;
import cn.jpush.android.t.d;
import com.facebook.share.internal.ShareConstants;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static ConcurrentLinkedQueue<Long> a = new ConcurrentLinkedQueue<>();

    private static int a(long j) {
        if (a.size() >= 10) {
            long jLongValue = j - a.element().longValue();
            if (jLongValue < 0) {
                a.clear();
                return 2;
            }
            if (jLongValue <= OkHttpUtils.DEFAULT_MILLISECONDS) {
                return 1;
            }
            while (a.size() >= 10) {
                a.poll();
            }
        }
        a.offer(Long.valueOf(j));
        return 0;
    }

    public static long a(Context context, String str, int i, long j) {
        Logger.d("TagAliasHelper", "action - onRecvTagAliasCallBack");
        try {
            JSONObject jSONObject = new JSONObject(str);
            int iOptInt = jSONObject.optInt(JThirdPlatFormInterface.KEY_CODE, JPushInterface.ErrorCode.UNKNOWN_ERROR);
            if (i == 0) {
                j = jSONObject.optLong("sequence");
            }
            Intent intent = new Intent();
            if (i == 0) {
                intent.setAction(TagAliasReceiver.ACTION_TAG_ALIAS_CALLBACK);
            } else {
                String messageReceiverClass = JPushConstants.getMessageReceiverClass(context);
                if (!TextUtils.isEmpty(messageReceiverClass)) {
                    intent = new Intent(context, Class.forName(messageReceiverClass));
                }
                intent.setAction("cn.jpush.android.intent.RECEIVE_MESSAGE");
                if (i == 1) {
                    intent.putExtra("message_type", 1);
                } else {
                    intent.putExtra("message_type", 2);
                }
            }
            Intent intent2 = intent;
            intent2.addCategory(context.getPackageName());
            intent2.putExtra("proto_type", i);
            intent2.setPackage(context.getPackageName());
            intent2.putExtra(TagAliasReceiver.KEY_TAGALIASOPERATOR_CALLBACKCODE, iOptInt);
            intent2.putExtra(TagAliasReceiver.KEY_TAGALIASOPERATOR_SEQID, j);
            Intent intentA = b.a().a(context, j, iOptInt, jSONObject, intent2);
            if (intentA != null) {
                context.sendBroadcast(intentA);
            }
            return j;
        } catch (Throwable unused) {
            Logger.w("TagAliasHelper", "tagalias msgContent:" + str);
            return -1L;
        }
    }

    protected static String a(Context context, String str, long j, int i) {
        if (c(i)) {
            try {
                JSONObject jSONObjectA = a(i);
                if (b(2, i)) {
                    if (TextUtils.isEmpty(str)) {
                        Logger.ee("TagAliasHelper", "alias was empty. Give up action.");
                        a(context, 2, JPushInterface.ErrorCode.NULL_TAGANDALIAS, j);
                        return null;
                    }
                    if (!a(context, 2, str, j)) {
                        return null;
                    }
                    jSONObjectA.put("alias", str);
                }
                String string = jSONObjectA.toString();
                Logger.d("TagAliasHelper", "alias str:" + string);
                if (!TextUtils.isEmpty(string)) {
                    return string;
                }
                Logger.d("TagAliasHelper", "alias request action was empty");
                return null;
            } catch (Throwable th) {
                Logger.w("TagAliasHelper", "alias exception:" + th);
                a(context, 2, JPushInterface.ErrorCode.UNKNOWN_ERROR, j);
            }
        } else {
            Logger.e("TagAliasHelper", "unsupport alias action type");
        }
        a(context, 2, JPushInterface.ErrorCode.UNKNOWN_ERROR, j);
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.lang.CharSequence, java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v4, types: [org.json.JSONArray] */
    protected static String a(Context context, List<String> list, long j, int i, int i2, int i3) {
        if (i != 0) {
            try {
                JSONObject jSONObjectA = a(i);
                if (a(1, i)) {
                    if (list != null && !list.isEmpty()) {
                        HashSet hashSet = new HashSet(list);
                        if (!a(context, 1, hashSet, j)) {
                            return null;
                        }
                        Object objB = b(hashSet);
                        if (!b(context, objB, j, 1)) {
                            return null;
                        }
                        if (i != 6) {
                            objB = new JSONArray();
                            Iterator<String> it = list.iterator();
                            while (it.hasNext()) {
                                objB.put(it.next());
                            }
                        } else if (TextUtils.isEmpty(objB)) {
                            Logger.ee("TagAliasHelper", "stags was empty. Give up action.");
                            a(context, 1, JPushInterface.ErrorCode.NULL_TAGANDALIAS, j);
                            return null;
                        }
                        jSONObjectA.put("tags", objB);
                    }
                    Logger.ee("TagAliasHelper", "tags was empty. Give up action.");
                    a(context, 1, JPushInterface.ErrorCode.NULL_TAGANDALIAS, j);
                    return null;
                }
                if (i == 5) {
                    if (i3 == -1) {
                        i3 = 1;
                    }
                    jSONObjectA.put("curr", i3);
                }
                String string = jSONObjectA.toString();
                Logger.d("TagAliasHelper", "tag str:" + string);
                if (!TextUtils.isEmpty(string)) {
                    return string;
                }
                Logger.d("TagAliasHelper", "tag request action was empty");
                return null;
            } catch (Throwable th) {
                Logger.w("TagAliasHelper", "tag exception:" + th);
                a(context, 1, JPushInterface.ErrorCode.UNKNOWN_ERROR, j);
            }
        } else {
            Logger.e("TagAliasHelper", "unsupport tag action type");
        }
        a(context, 1, JPushInterface.ErrorCode.UNKNOWN_ERROR, j);
        return null;
    }

    private static String a(Context context, List<String> list, String str, long j) {
        HashSet hashSet = list != null ? new HashSet(list) : null;
        if (str != null && !a(context, 0, str, j)) {
            return null;
        }
        if (hashSet != null && !a(context, 0, hashSet, j)) {
            return null;
        }
        String strB = b(hashSet);
        if (!b(context, strB, j, 0)) {
            return null;
        }
        if (strB == null && str == null) {
            Logger.ee("TagAliasHelper", "NULL alias and tags. Give up action.");
            a(context, 0, JPushInterface.ErrorCode.NULL_TAGANDALIAS, j);
            return null;
        }
        Logger.dd("TagAliasHelper", "action:setAliasAndTags - alias:" + str + ", tags:" + strB);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(JThirdPlatFormInterface.KEY_PLATFORM, "a");
            if (str != null) {
                jSONObject.put("alias", str);
            }
            if (hashSet != null) {
                jSONObject.put("tags", strB);
            }
            String string = jSONObject.toString();
            Logger.d("TagAliasHelper", "tagalias str:" + string);
            if (!TextUtils.isEmpty(string)) {
                return string;
            }
            Logger.d("TagAliasHelper", "tagalias request action was empty");
        } catch (Throwable th) {
            Logger.w("TagAliasHelper", "tagalias exception:" + th);
            a(context, 0, JPushInterface.ErrorCode.UNKNOWN_ERROR, j);
        }
        return null;
    }

    public static Set<String> a(Set<String> set) {
        if (set == null) {
            return null;
        }
        if (set.isEmpty()) {
            return set;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int i = 0;
        for (String str : set) {
            if (TextUtils.isEmpty(str) || !d.a(str)) {
                Logger.ee("TagAliasHelper", "Invalid tag : " + str);
            } else {
                linkedHashSet.add(str);
                i++;
                if (i >= 1000) {
                    Logger.ww("TagAliasHelper", "The lenght of tags maybe more than 1000.");
                    return linkedHashSet;
                }
            }
        }
        return linkedHashSet;
    }

    private static JSONObject a(int i) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(JThirdPlatFormInterface.KEY_PLATFORM, "a");
        jSONObject.put("op", b(i));
        return jSONObject;
    }

    public static void a(Context context, int i, int i2, long j) {
        try {
            Intent intent = new Intent();
            intent.addCategory(context.getPackageName());
            intent.setPackage(context.getPackageName());
            if (i == 0) {
                intent.setAction(TagAliasReceiver.ACTION_TAG_ALIAS_CALLBACK);
            } else {
                intent.setAction("cn.jpush.android.intent.RECEIVE_MESSAGE");
                intent.setClass(context, Class.forName(JPushConstants.getMessageReceiverClass(context)));
                if (i == 1) {
                    intent.putExtra("message_type", 1);
                } else {
                    intent.putExtra("message_type", 2);
                }
            }
            intent.putExtra(TagAliasReceiver.KEY_TAGALIASOPERATOR_CALLBACKCODE, i2);
            intent.putExtra(TagAliasReceiver.KEY_TAGALIASOPERATOR_SEQID, j);
            context.sendBroadcast(intent);
        } catch (Throwable th) {
            Logger.ww("TagAliasHelper", "NotifyTagAliasError:" + th.getMessage());
        }
    }

    protected static void a(Context context, int i, long j, String str) {
        int i2;
        Logger.dd("TagAliasHelper", "tagalias:" + str);
        if (str == null) {
            Logger.w("TagAliasHelper", "tagaliasRequest was null");
            return;
        }
        int i3 = 1;
        if (i == 0) {
            i2 = 10;
        } else {
            i2 = i == 1 ? 28 : 29;
        }
        int i4 = i2;
        if (i == 0) {
            i3 = 4;
        } else if (i != 1) {
            i3 = 2;
        }
        JCoreHelper.sendRequest(context, JPushConstants.SDK_TYPE, i4, i3, j, 20000L, cn.jpush.android.q.b.a(JCoreHelper.getAppKey(context), i4, str));
    }

    public static void a(Context context, Bundle bundle) {
        int i;
        int i2;
        String str;
        int i3;
        String strA;
        String strA2;
        int i4;
        String string = bundle.getString("alias");
        ArrayList<String> stringArrayList = bundle.getStringArrayList("tags");
        long j = bundle.getLong("seq", 0L);
        try {
            i = Integer.parseInt(bundle.getString("proto_type"));
        } catch (Throwable th) {
            Logger.w("TagAliasHelper", "load tag/alias proto type failed - error:" + th);
            a(context, 0, JPushInterface.ErrorCode.UNKNOWN_ERROR, j);
            i = 0;
        }
        try {
            i2 = Integer.parseInt(bundle.getString(ShareConstants.WEB_DIALOG_PARAM_ACTION_TYPE));
        } catch (Throwable th2) {
            Logger.w("TagAliasHelper", "load tag/alias action type failed - error:" + th2);
            a(context, i, JPushInterface.ErrorCode.UNKNOWN_ERROR, j);
            i2 = 0;
        }
        if (cn.jpush.android.cache.a.d(context)) {
            i4 = JPushInterface.ErrorCode.PUSH_STOPED;
        } else {
            int iA = a(System.currentTimeMillis());
            if (iA != 0) {
                Logger.w("TagAliasHelper", iA == 1 ? "set tags/alias too soon,over 10 times in 10s" : "set tags/alias failed,time shaft error，please try again");
                a(context, i, iA == 1 ? JPushInterface.ErrorCode.INVOKE_TOO_SOON : JPushInterface.ErrorCode.INCORRECT_TIME, j);
                return;
            }
            if (!b.a().a(context, i, j)) {
                if (i == 0) {
                    Logger.d("TagAliasHelper", "old tag/alias proto");
                    strA2 = a(context, stringArrayList, string, j);
                    str = "alias";
                    i3 = 2;
                } else {
                    if (i == 1) {
                        str = "alias";
                        i3 = 2;
                        strA = a(context, stringArrayList, j, i2, -1, -1);
                    } else {
                        str = "alias";
                        i3 = 2;
                        strA = i == 2 ? a(context, string, j, i2) : null;
                    }
                    strA2 = strA;
                }
                if (strA2 != null && (i == 1 || i == i3)) {
                    if (!b.a().a(i)) {
                        Logger.w("TagAliasHelper", (i == 1 ? "tag" : str).concat(" is operatoring, please wait last operator complete"));
                        a(context, i, i == 1 ? JPushInterface.ErrorCode.ERROR_CODE_TAG_OPERATORING : JPushInterface.ErrorCode.ERROR_CODE_ALIAS_OPERATORING, j);
                        return;
                    }
                    b.a().a(i, i2, j, stringArrayList, string);
                }
                a(context, i, j, strA2);
                return;
            }
            i4 = JPushInterface.ErrorCode.ERROR_CODE_SERVER_UNAVAILABLE;
        }
        a(context, i, i4, j);
    }

    public static void a(Context context, String str, Set<String> set, CallBackParams callBackParams) {
        long jA = g.a();
        if (callBackParams != null) {
            c.a().a(context, Long.valueOf(jA), callBackParams);
        }
        if (!(context instanceof Application)) {
            context = context.getApplicationContext();
        }
        if (callBackParams != null && callBackParams.protoType == 0) {
            c.a().a(context);
        }
        Bundle bundle = new Bundle();
        bundle.putString("alias", str);
        bundle.putStringArrayList("tags", set != null ? new ArrayList<>(set) : null);
        bundle.putLong("seq", jA);
        bundle.putString("proto_type", (callBackParams != null ? callBackParams.protoType : 0) + "");
        bundle.putString(ShareConstants.WEB_DIALOG_PARAM_ACTION_TYPE, (callBackParams != null ? callBackParams.action : 0) + "");
        JCoreHelper.runActionWithService(context, JPushConstants.SDK_TYPE, "tagalis", bundle);
    }

    private static boolean a(int i, int i2) {
        return i == 1 && (i2 == 1 || i2 == 2 || i2 == 3 || i2 == 6);
    }

    private static boolean a(Context context, int i, String str, long j) {
        int iB = d.b(str);
        if (iB == 0) {
            return true;
        }
        Logger.dd("TagAliasHelper", "Invalid alias: " + str + ", will not set alias this time.");
        a(context, i, iB, j);
        return false;
    }

    private static boolean a(Context context, int i, Set<String> set, long j) {
        int iA = d.a(set);
        if (iA == 0) {
            return true;
        }
        Logger.dd("TagAliasHelper", "Invalid tags, will not set tags this time.");
        a(context, i, iA, j);
        return false;
    }

    private static boolean a(String str, boolean z) {
        int length = !TextUtils.isEmpty(str) ? str.getBytes().length + 0 : 0;
        Logger.v("TagAliasHelper", "tags length:" + length);
        if (z) {
            if (length > 5000) {
                return false;
            }
        } else if (length > 7000) {
            return false;
        }
        return true;
    }

    private static String b(int i) {
        switch (i) {
            case 1:
                return "add";
            case 2:
                return "set";
            case 3:
                return "del";
            case 4:
                return "clean";
            case 5:
                return "get";
            case 6:
                return "valid";
            default:
                return null;
        }
    }

    public static String b(Set<String> set) {
        String str = null;
        if (set == null) {
            return null;
        }
        if (set.isEmpty()) {
            return "";
        }
        int i = 0;
        for (String str2 : set) {
            if (TextUtils.isEmpty(str2) || !d.a(str2)) {
                Logger.ee("TagAliasHelper", "Invalid tag: " + str2);
            } else {
                if (str != null) {
                    str2 = str + "," + str2;
                }
                i++;
                if (i >= 1000) {
                    return str2;
                }
                str = str2;
            }
        }
        return str;
    }

    private static boolean b(int i, int i2) {
        return i == 2 && i2 == 2;
    }

    private static boolean b(Context context, String str, long j, int i) {
        if (str != null) {
            if (!a(str.replaceAll(",", ""), i != 0)) {
                a(context, i, JPushInterface.ErrorCode.TOO_LONG_TAGALIAS, j);
                Logger.ww("TagAliasHelper", "The length of tags should be less than " + (i != 0 ? BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT : 7000) + " bytes.");
                return false;
            }
        }
        return true;
    }

    private static boolean c(int i) {
        return i == 2 || i == 3 || i == 5;
    }
}
