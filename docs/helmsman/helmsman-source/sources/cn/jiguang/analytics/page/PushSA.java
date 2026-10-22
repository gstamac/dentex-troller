package cn.jiguang.analytics.page;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import cn.jiguang.ae.k;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.f.i;
import cn.jiguang.internal.JConstants;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class PushSA {
    public static final String CACHE_PATH = "push_stat_cache.json";
    private static final String KEY_SESSION_ID = "session_id";
    public static final String REPORT_ACTIVE_LAUNCH = "active_launch";
    public static final String REPORT_ACTIVE_TERMINATE = "active_terminate";
    private static final String SESSION_END_MILLIS = "cse";
    private static final String SESSION_START_MILLIS = "css";
    private static final String TAG = "PushSA";
    private static volatile PushSA instance = null;
    public static boolean isOnPauseInvoke = false;
    public static boolean isOnResumeInvoke = false;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private String cur_activity = null;
    private String cur_session_id = null;
    private long interval = 30;
    private long latestResumeTime = 0;
    private long latestPauseTime = 0;
    private boolean firstResume = true;
    private boolean activitySwitch = false;
    private boolean stat_enable = true;
    private long tempTimelong = 0;
    private JSONObject flow_cache = null;
    private final Object cur_session_file_lock = new Object();

    private PushSA() {
    }

    private void clearCurrentLogFile(Context context) {
        k.a(context, CACHE_PATH, (JSONObject) null);
    }

    private JSONObject createNewSession(Context context, long j) {
        this.cur_session_id = generateSessionID(context, j);
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.o().a(Long.valueOf(this.latestResumeTime)), cn.jiguang.g.a.r().a(this.cur_session_id)});
        JSONObject jSONObject = new JSONObject();
        try {
            wrapDate(jSONObject);
            cn.jiguang.d.a.a(context, jSONObject, REPORT_ACTIVE_LAUNCH);
            jSONObject.put(KEY_SESSION_ID, this.cur_session_id);
            return jSONObject;
        } catch (JSONException unused) {
            return null;
        }
    }

    private String generateSessionID(Context context, long j) {
        StringBuilder sb = new StringBuilder();
        String strA = cn.jiguang.ac.a.a(context);
        if (!TextUtils.isEmpty(strA)) {
            sb.append(strA);
        }
        sb.append(j);
        return i.d(sb.toString());
    }

    private JSONObject getFlowCache(Context context) {
        if (this.flow_cache == null) {
            this.flow_cache = cn.jiguang.d.a.a(context, CACHE_PATH);
        }
        return this.flow_cache;
    }

    public static PushSA getInstance() {
        if (instance == null) {
            synchronized (PushSA.class) {
                instance = new PushSA();
            }
        }
        return instance;
    }

    private boolean invokeCheck(Context context, String str) {
        if (!this.stat_enable) {
            cn.jiguang.ad.d.d(TAG, "stat function has been disabled");
            return false;
        }
        if (context == null) {
            cn.jiguang.ad.d.d(TAG, "context is null");
            return false;
        }
        if (!(context instanceof Application)) {
            return true;
        }
        cn.jiguang.ad.d.i(TAG, "Context should be an Activity on this method : " + str);
        return false;
    }

    private boolean isNewSession(Context context) {
        if (this.firstResume) {
            this.firstResume = false;
            cn.jiguang.ad.d.b(TAG, "statistics start");
            long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.q())).longValue();
            cn.jiguang.ad.d.b(TAG, "lastPause:" + jLongValue + ",latestResumeTime:" + this.latestResumeTime + ",interval:" + (this.interval * 1000) + ",a:" + (this.latestResumeTime - jLongValue));
            if (jLongValue > 0 && this.latestResumeTime - jLongValue <= this.interval * 1000) {
                return false;
            }
        } else if (this.latestResumeTime - this.latestPauseTime <= this.interval * 1000) {
            return false;
        }
        return true;
    }

    private void saveCurrentLog(Context context, JSONObject jSONObject) {
        k.a(context, CACHE_PATH, jSONObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveLogRoutine(Context context) {
        if (context == null) {
            return;
        }
        synchronized (this.cur_session_file_lock) {
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.q().a(Long.valueOf(this.latestPauseTime)), cn.jiguang.g.a.p().a(Long.valueOf(this.latestPauseTime))});
            JSONObject flowCache = getFlowCache(context);
            if (flowCache == null) {
                flowCache = new JSONObject();
            }
            try {
                updateFlowInfo(flowCache, context);
            } catch (Exception unused) {
            }
            updateFlowCache(flowCache);
            saveCurrentLog(context, flowCache);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLogRoutine(Context context) {
        JSONObject flowCache;
        if (!isNewSession(context)) {
            this.cur_session_id = (String) cn.jiguang.g.b.b(context, cn.jiguang.g.a.r());
            return;
        }
        cn.jiguang.ad.d.d(TAG, "new statistics session");
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObjectCreateNewSession = createNewSession(context, this.latestResumeTime);
        if (jSONObjectCreateNewSession != null) {
            jSONArray.put(jSONObjectCreateNewSession);
        }
        synchronized (this.cur_session_file_lock) {
            flowCache = getFlowCache(context);
            if (flowCache != null && flowCache.length() > 0) {
                try {
                    cn.jiguang.d.a.a(context, flowCache, REPORT_ACTIVE_TERMINATE);
                } catch (Exception unused) {
                }
                clearCurrentLogFile(context);
                this.flow_cache = null;
            }
        }
        if (flowCache != null && flowCache.length() > 0) {
            jSONArray.put(flowCache);
        }
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 14, null, null, jSONArray);
    }

    private void updateFlowCache(JSONObject jSONObject) {
        this.flow_cache = jSONObject;
    }

    private void updateFlowInfo(JSONObject jSONObject, Context context) throws JSONException {
        long j;
        long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.o())).longValue();
        if (jLongValue <= 0) {
            long j2 = this.latestPauseTime - this.tempTimelong;
            j = j2 > 0 ? j2 / 1000 : 10L;
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.o().a(Long.valueOf(this.tempTimelong))});
        } else {
            j = (this.latestPauseTime - jLongValue) / 1000;
        }
        jSONObject.put(TypedValues.TransitionType.S_DURATION, j);
        jSONObject.put("itime", System.currentTimeMillis() / 1000);
        jSONObject.put(KEY_SESSION_ID, this.cur_session_id);
        wrapDate(jSONObject);
    }

    private void wrapDate(JSONObject jSONObject) throws JSONException {
        String strA = cn.jiguang.f.c.a();
        String str = strA.split("_")[0];
        String str2 = strA.split("_")[1];
        jSONObject.put("date", str);
        jSONObject.put("time", str2);
    }

    public long getInterval() {
        return this.interval;
    }

    public boolean isStatEnable() {
        return this.stat_enable;
    }

    public void onFragmentPause(Context context, String str) {
        if (!this.activitySwitch) {
            cn.jiguang.ad.d.b(TAG, "JCoreInterface.onPause() must be called after called JCoreInterface.onResume() in this Activity or Fragment");
            return;
        }
        this.activitySwitch = false;
        String str2 = this.cur_activity;
        if (str2 == null || !str2.equals(str)) {
            cn.jiguang.ad.d.i(TAG, "page name didn't match the last one passed by onResume");
            return;
        }
        this.latestPauseTime = System.currentTimeMillis();
        try {
            this.executor.execute(new b(this, context.getApplicationContext()));
        } catch (Throwable unused) {
        }
    }

    public void onFragmentResume(Context context, String str) {
        if (this.activitySwitch) {
            cn.jiguang.ad.d.b(TAG, "JCoreInterface.onResume() must be called after called JCoreInterface.onPause() in last Activity or Fragment");
            return;
        }
        this.activitySwitch = true;
        this.cur_activity = str;
        this.latestResumeTime = System.currentTimeMillis();
        try {
            this.executor.execute(new a(this, context.getApplicationContext()));
        } catch (Throwable unused) {
        }
    }

    public void onKillProcess(Context context) {
        try {
            if (this.cur_activity == null || !this.activitySwitch) {
                return;
            }
            this.latestPauseTime = System.currentTimeMillis();
            this.executor.execute(new c(this, context.getApplicationContext()));
        } catch (Throwable unused) {
        }
    }

    public void onPause(Context context) {
        if (invokeCheck(context, "onPause")) {
            isOnPauseInvoke = true;
            try {
                this.activitySwitch = true;
            } catch (ClassCastException unused) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.activitySwitch) {
                this.activitySwitch = false;
                String str = this.cur_activity;
                if (str == null || !str.equals(context.getClass().getName())) {
                    cn.jiguang.ad.d.d(TAG, "the activity pass by onPause didn't match last one passed by onResume");
                    return;
                }
                this.latestPauseTime = System.currentTimeMillis();
                this.tempTimelong = this.latestResumeTime;
                try {
                    this.executor.execute(new d(false, context.getApplicationContext(), this));
                } catch (Throwable unused2) {
                }
            }
        }
    }

    public void onResume(Context context) {
        if (invokeCheck(context, "onResume")) {
            isOnResumeInvoke = true;
            try {
                this.activitySwitch = false;
            } catch (ClassCastException | Exception unused) {
            }
            if (this.activitySwitch) {
                return;
            }
            this.activitySwitch = true;
            this.latestResumeTime = System.currentTimeMillis();
            this.cur_activity = context.getClass().getName();
            try {
                this.executor.execute(new d(true, context.getApplicationContext(), this));
            } catch (Throwable unused2) {
            }
        }
    }

    public void setInterval(long j) {
        this.interval = j;
    }

    public void setStatEnable(boolean z) {
        this.stat_enable = z;
    }
}
