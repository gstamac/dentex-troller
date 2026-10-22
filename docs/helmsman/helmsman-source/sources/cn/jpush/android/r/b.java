package cn.jpush.android.r;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.service.TagAliasReceiver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b {
    private static volatile b a;
    private static final Object b = new Object();
    private ConcurrentHashMap<Long, a> c = new ConcurrentHashMap<>();

    private class a {
        public int a;
        public int b;
        public long c;
        public ArrayList<String> d;
        public String e;
        public int f = -1;
        public int g = -1;
        public int h = 0;
        public int i;

        public a(int i, int i2, long j, ArrayList<String> arrayList, String str) {
            this.i = 0;
            this.a = i;
            this.b = i2;
            this.c = j;
            this.d = arrayList;
            if (i == 1 && arrayList == null) {
                this.d = new ArrayList<>();
            }
            this.e = str;
            this.i = 1;
        }

        public String toString() {
            return "TagAliasCacheBean{protoType=" + this.a + ", actionType=" + this.b + ", seqID=" + this.c + ", tags=" + this.d + ", alias='" + this.e + "', totalPage=" + this.f + ", currPage=" + this.g + ", retryCount=" + this.h + '}';
        }
    }

    private a a(JSONObject jSONObject, a aVar) {
        Logger.d("TagAliasNewProtoRetryHelper", "action - onUpdateCacheNode,responseJson:" + jSONObject + ",tagAliasCacheNode:" + aVar);
        if (aVar == null) {
            Logger.w("TagAliasNewProtoRetryHelper", "tagAliasCacheNode was null");
            return null;
        }
        if (TextUtils.equals(jSONObject.optString("op"), "get")) {
            if (aVar.a == 1) {
                try {
                    JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("tags");
                    if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() != 0) {
                        ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                            arrayList.add(jSONArrayOptJSONArray.getString(i));
                        }
                        if (arrayList.size() > 0) {
                            aVar.d.addAll(arrayList);
                        }
                    }
                } catch (Throwable th) {
                    Logger.e("TagAliasNewProtoRetryHelper", "parse tag list failed - error:" + th);
                }
            } else {
                String strOptString = jSONObject.optString("alias");
                if (strOptString != null) {
                    aVar.e = strOptString;
                }
            }
        }
        return aVar;
    }

    public static b a() {
        if (a == null) {
            synchronized (b) {
                if (a == null) {
                    a = new b();
                }
            }
        }
        return a;
    }

    private boolean a(Context context, int i, a aVar) {
        Logger.d("TagAliasNewProtoRetryHelper", "action - CheckAndSendAgain, errorCode:" + i + ",tagAliasCacheNode:" + aVar);
        if (i != 1 || aVar.h != 0) {
            return false;
        }
        aVar.h++;
        if (a(context, aVar.a, aVar.c)) {
            return true;
        }
        return b(context, aVar);
    }

    private boolean a(Context context, a aVar) {
        String str;
        if (aVar == null) {
            str = "tagAlias cache was null";
        } else {
            Logger.d("TagAliasNewProtoRetryHelper", "action - onTagAliasResponse, tagAliasCacheNode:" + aVar);
            if (aVar.g < aVar.f) {
                return true;
            }
            str = "all tags info was loaded";
        }
        Logger.d("TagAliasNewProtoRetryHelper", str);
        return false;
    }

    private boolean b(Context context, a aVar) {
        String strA;
        String str;
        Logger.d("TagAliasNewProtoRetryHelper", "action - onSendAgain, tagAliasCacheNode:" + aVar);
        if (aVar == null) {
            Logger.w("TagAliasNewProtoRetryHelper", "onSendAgain - tagAliasCacheNode was null");
            return false;
        }
        if (aVar.a == 1) {
            strA = cn.jpush.android.r.a.a(context, aVar.d, aVar.c, aVar.b, aVar.f, aVar.g);
        } else {
            if (aVar.a != 2) {
                Logger.d("TagAliasNewProtoRetryHelper", "unsupport proto type");
                return false;
            }
            strA = cn.jpush.android.r.a.a(context, aVar.e, aVar.c, aVar.a);
        }
        if (strA == null) {
            return false;
        }
        if (aVar.h > 200) {
            this.c.remove(Long.valueOf(aVar.c));
            cn.jpush.android.r.a.a(context, aVar.a, JPushInterface.ErrorCode.ERROR_CODE_TOO_BUSY, aVar.c);
            str = "same tag/alias request times greate than 200";
        } else {
            cn.jpush.android.r.a.a(context, aVar.a, aVar.c, strA);
            aVar.h++;
            this.c.put(Long.valueOf(aVar.c), aVar);
            str = "send request success";
        }
        Logger.d("TagAliasNewProtoRetryHelper", str);
        return true;
    }

    public int a(int i, int i2) {
        if (i == 0) {
            return i2;
        }
        if (i2 == 17) {
            try {
                return JPushInterface.ErrorCode.ERROR_CODE_ALIAS_LIMIT;
            } catch (Throwable unused) {
                return JPushInterface.ErrorCode.ERROR_CODE_INVALIDREQ;
            }
        }
        if (i2 == 100) {
            return JPushInterface.ErrorCode.ERROR_CODE_SERVER_UNAVAILABLE;
        }
        switch (i2) {
            case 1:
            case 2:
                return JPushInterface.ErrorCode.ERROR_CODE_TOO_BUSY;
            case 3:
                return JPushInterface.ErrorCode.ERROR_CODE_BLACKLIST;
            case 4:
                return JPushInterface.ErrorCode.ERROR_CODE_INVALIDUSER;
            case 5:
                return JPushInterface.ErrorCode.ERROR_CODE_INVALIDREQ;
            case 6:
                return JPushInterface.ErrorCode.ERROR_CODE_TOO_MANY_TAGS;
            case 7:
            case 8:
                return JPushInterface.ErrorCode.ERROR_CODE_GET_FAILED;
            default:
                return i2;
        }
    }

    public int a(long j) {
        Logger.d("TagAliasNewProtoRetryHelper", "action - onTagAliasTimeOut :" + j);
        a aVarRemove = this.c.remove(Long.valueOf(j));
        Logger.d("TagAliasNewProtoRetryHelper", "onTagAliasTimeOut,removed cachenode:" + aVarRemove);
        if (aVarRemove != null) {
            return aVarRemove.a;
        }
        return 0;
    }

    public Intent a(Context context, long j, int i, JSONObject jSONObject, Intent intent) {
        String str;
        Logger.d("TagAliasNewProtoRetryHelper", "action - onTagAliasResponse, seqID:" + j + ",errorCode:" + i + ",intent:" + intent);
        a aVar = this.c.get(Long.valueOf(j));
        Logger.d("TagAliasNewProtoRetryHelper", "tagAliasCacheNode:" + aVar);
        this.c.remove(Long.valueOf(j));
        if (jSONObject == null) {
            Logger.w("TagAliasNewProtoRetryHelper", "responseJson was null");
            return intent;
        }
        if (aVar == null) {
            Logger.w("TagAliasNewProtoRetryHelper", "tagAliasCacheNode was null");
            return intent;
        }
        if (!a(context, i, aVar)) {
            if (i != 0) {
                if (i == 100) {
                    long jOptLong = jSONObject.optLong("wait", -1L);
                    Logger.ww("TagAliasNewProtoRetryHelper", "set tag/alias action will freeze " + jOptLong + " seconds");
                    if (jOptLong > 0) {
                        cn.jpush.android.cache.a.a(context, jOptLong);
                    }
                }
                int iA = a(aVar.a, i);
                intent.putExtra(TagAliasReceiver.KEY_TAGALIASOPERATOR_CALLBACKCODE, iA);
                Logger.d("TagAliasNewProtoRetryHelper", "mapped errorCode:" + iA);
                return intent;
            }
            aVar.h = 0;
            if (aVar.b == 5) {
                aVar.f = jSONObject.optInt("total", -1);
                aVar.g = jSONObject.optInt("curr", -1);
                a(jSONObject, aVar);
            }
            if (a(context, aVar)) {
                aVar.g++;
                Logger.d("TagAliasNewProtoRetryHelper", "load next page, currpage:" + aVar.g + ",totalPage:" + aVar.f);
                if (a(context, aVar.a, aVar.c)) {
                    return null;
                }
                str = b(context, aVar) ? "get next page request was sended" : "retry action was sended";
            }
            if (aVar.b == 5) {
                if (aVar.a == 1) {
                    if (aVar.d.size() > 0) {
                        intent.putStringArrayListExtra("tags", aVar.d);
                    }
                } else if (aVar.a == 2 && aVar.e != null) {
                    intent.putExtra("alias", aVar.e);
                }
            } else if (aVar.b == 6) {
                if (aVar.a == 1) {
                    intent.putExtra("validated", jSONObject.optBoolean("validated", false));
                } else {
                    Logger.w("TagAliasNewProtoRetryHelper", "unsupport  proto type");
                }
            }
            return intent;
        }
        Logger.d("TagAliasNewProtoRetryHelper", str);
        return null;
    }

    public void a(int i, int i2, long j, ArrayList<String> arrayList, String str) {
        a aVar = new a(i, i2, j, arrayList, str);
        Logger.d("TagAliasNewProtoRetryHelper", "action - createNewCacheNode, tagAliasCacheNode:" + aVar);
        this.c.put(Long.valueOf(j), aVar);
    }

    public boolean a(int i) {
        ConcurrentHashMap<Long, a> concurrentHashMap = this.c;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return true;
        }
        Iterator<Map.Entry<Long, a>> it = this.c.entrySet().iterator();
        while (it.hasNext()) {
            a value = it.next().getValue();
            if (value != null && value.a == i) {
                return false;
            }
        }
        return true;
    }

    protected boolean a(Context context, int i, long j) {
        if ((i != 1 && i != 2) || !cn.jpush.android.cache.a.j(context)) {
            return false;
        }
        Logger.w("TagAliasNewProtoRetryHelper", "tag/alias action was freezed");
        cn.jpush.android.r.a.a(context, i, JPushInterface.ErrorCode.ERROR_CODE_SERVER_UNAVAILABLE, j);
        return true;
    }
}
