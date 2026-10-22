package com.tencent.mm.sdk.openapi;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.tencent.mm.sdk.a.a;
import com.tencent.mm.sdk.b.b;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage;
import com.tencent.mm.sdk.modelbiz.CreateChatroom;
import com.tencent.mm.sdk.modelbiz.JoinChatroom;
import com.tencent.mm.sdk.modelbiz.OpenWebview;
import com.tencent.mm.sdk.modelmsg.GetMessageFromWX;
import com.tencent.mm.sdk.modelmsg.LaunchFromWX;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.wxop.stat.MtaSDkException;
import com.tencent.wxop.stat.StatConfig;
import com.tencent.wxop.stat.StatReportStrategy;
import com.tencent.wxop.stat.StatService;

/* JADX INFO: loaded from: classes2.dex */
final class WXApiImplV10 implements IWXAPI {
    private static final String TAG = "MicroMsg.SDK.WXApiImplV10";
    private static ActivityLifecycleCb activityCb;
    private static String wxappPayEntryClassname;
    private String appId;
    private boolean checkSignature;
    private Context context;
    private boolean detached = false;

    private static final class ActivityLifecycleCb implements Application.ActivityLifecycleCallbacks {
        private static final int DELAYED = 800;
        private static final String TAG = "MicroMsg.SDK.WXApiImplV10.ActivityLifecycleCb";
        private Context context;
        private Handler handler;
        private boolean isForeground;
        private Runnable onPausedRunnable;
        private Runnable onResumedRunnable;

        private ActivityLifecycleCb(Context context) {
            this.isForeground = false;
            this.handler = new Handler(Looper.getMainLooper());
            this.onPausedRunnable = new Runnable() { // from class: com.tencent.mm.sdk.openapi.WXApiImplV10.ActivityLifecycleCb.1
                @Override // java.lang.Runnable
                public void run() {
                    if (WXApiImplV10.activityCb == null || !ActivityLifecycleCb.this.isForeground) {
                        return;
                    }
                    Log.v(ActivityLifecycleCb.TAG, "WXStat trigger onBackground");
                    StatService.trackCustomKVEvent(ActivityLifecycleCb.this.context, "onBackground_WX", null);
                    ActivityLifecycleCb.this.isForeground = false;
                }
            };
            this.onResumedRunnable = new Runnable() { // from class: com.tencent.mm.sdk.openapi.WXApiImplV10.ActivityLifecycleCb.2
                @Override // java.lang.Runnable
                public void run() {
                    if (WXApiImplV10.activityCb == null || ActivityLifecycleCb.this.isForeground) {
                        return;
                    }
                    Log.v(ActivityLifecycleCb.TAG, "WXStat trigger onForeground");
                    StatService.trackCustomKVEvent(ActivityLifecycleCb.this.context, "onForeground_WX", null);
                    ActivityLifecycleCb.this.isForeground = true;
                }
            };
            this.context = context;
        }

        public final void detach() {
            this.handler.removeCallbacks(this.onResumedRunnable);
            this.handler.removeCallbacks(this.onPausedRunnable);
            this.context = null;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityDestroyed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityPaused(Activity activity) {
            Log.v(TAG, activity.getComponentName().getClassName() + "  onActivityPaused");
            this.handler.removeCallbacks(this.onResumedRunnable);
            this.handler.postDelayed(this.onPausedRunnable, 800L);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityResumed(Activity activity) {
            Log.v(TAG, activity.getComponentName().getClassName() + "  onActivityResumed");
            this.handler.removeCallbacks(this.onPausedRunnable);
            this.handler.postDelayed(this.onResumedRunnable, 800L);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public final void onActivityStopped(Activity activity) {
        }
    }

    WXApiImplV10(Context context, String str, boolean z) {
        this.checkSignature = false;
        b.e(TAG, "<init>, appId = " + str + ", checkSignature = " + z);
        this.context = context;
        this.appId = str;
        this.checkSignature = z;
    }

    private boolean checkSumConsistent(byte[] bArr, byte[] bArr2) {
        String str;
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            str = "checkSumConsistent fail, invalid arguments";
        } else {
            if (bArr.length == bArr2.length) {
                for (int i = 0; i < bArr.length; i++) {
                    if (bArr[i] != bArr2[i]) {
                        return false;
                    }
                }
                return true;
            }
            str = "checkSumConsistent fail, length is different";
        }
        b.b(TAG, str);
        return false;
    }

    private boolean createChatroom(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/createChatroom"), null, null, new String[]{this.appId, bundle.getString("_wxapi_basereq_transaction"), bundle.getString("_wxapi_create_chatroom_group_id"), bundle.getString("_wxapi_create_chatroom_chatroom_name"), bundle.getString("_wxapi_create_chatroom_chatroom_nickname"), bundle.getString("_wxapi_create_chatroom_ext_msg")}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private void initMta(Context context, String str) {
        String str2 = "AWXOP" + str;
        StatConfig.setAppKey(context, str2);
        StatConfig.setEnableSmartReporting(true);
        StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        StatConfig.setSendPeriodMinutes(60);
        StatConfig.setInstallChannel(context, "Wechat_Sdk");
        try {
            StatService.startStatService(context, str2, "2.0.3");
        } catch (MtaSDkException e) {
            e.printStackTrace();
        }
    }

    private boolean joinChatroom(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/joinChatroom"), null, null, new String[]{this.appId, bundle.getString("_wxapi_basereq_transaction"), bundle.getString("_wxapi_join_chatroom_group_id"), bundle.getString("_wxapi_join_chatroom_chatroom_nickname"), bundle.getString("_wxapi_join_chatroom_ext_msg")}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendAddCardToWX(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/addCardToWX"), null, null, new String[]{this.appId, bundle.getString("_wxapi_add_card_to_wx_card_list"), bundle.getString("_wxapi_basereq_transaction")}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendJumpToBizProfileReq(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/jumpToBizProfile"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_biz_profile_req_to_user_name"), bundle.getString("_wxapi_jump_to_biz_profile_req_ext_msg"), new StringBuilder().append(bundle.getInt("_wxapi_jump_to_biz_profile_req_scene")).toString(), new StringBuilder().append(bundle.getInt("_wxapi_jump_to_biz_profile_req_profile_type")).toString()}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendJumpToBizTempSessionReq(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/jumpToBizTempSession"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_biz_webview_req_to_user_name"), bundle.getString("_wxapi_jump_to_biz_webview_req_session_from"), new StringBuilder().append(bundle.getInt("_wxapi_jump_to_biz_webview_req_show_type")).toString()}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendJumpToBizWebviewReq(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/jumpToBizProfile"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_biz_webview_req_to_user_name"), bundle.getString("_wxapi_jump_to_biz_webview_req_ext_msg"), new StringBuilder().append(bundle.getInt("_wxapi_jump_to_biz_webview_req_scene")).toString()}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendOpenBusiLuckyMoney(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/openBusiLuckyMoney"), null, null, new String[]{this.appId, bundle.getString("_wxapi_open_busi_lucky_money_timeStamp"), bundle.getString("_wxapi_open_busi_lucky_money_nonceStr"), bundle.getString("_wxapi_open_busi_lucky_money_signType"), bundle.getString("_wxapi_open_busi_lucky_money_signature"), bundle.getString("_wxapi_open_busi_lucky_money_package")}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendOpenRankListReq(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/openRankList"), null, null, new String[0], null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendOpenWebview(Context context, Bundle bundle) {
        Cursor cursorQuery = context.getContentResolver().query(Uri.parse("content://com.tencent.mm.sdk.comm.provider/openWebview"), null, null, new String[]{this.appId, bundle.getString("_wxapi_jump_to_webview_url"), bundle.getString("_wxapi_basereq_transaction")}, null);
        if (cursorQuery == null) {
            return true;
        }
        cursorQuery.close();
        return true;
    }

    private boolean sendPayReq(Context context, Bundle bundle) {
        if (wxappPayEntryClassname == null) {
            wxappPayEntryClassname = new MMSharedPreferences(context).getString("_wxapp_pay_entry_classname_", null);
            b.e(TAG, "pay, set wxappPayEntryClassname = " + wxappPayEntryClassname);
            if (wxappPayEntryClassname == null) {
                b.b(TAG, "pay fail, wxappPayEntryClassname is null");
                return false;
            }
        }
        a.C0028a c0028a = new a.C0028a();
        c0028a.Z = bundle;
        c0028a.W = ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME;
        c0028a.X = wxappPayEntryClassname;
        return a.a(context, c0028a);
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final void detach() {
        Application application;
        b.e(TAG, "detach");
        this.detached = true;
        if (activityCb != null) {
            Context context = this.context;
            if (context instanceof Activity) {
                application = ((Activity) context).getApplication();
            } else {
                if (context instanceof Service) {
                    application = ((Service) context).getApplication();
                }
                activityCb.detach();
            }
            application.unregisterActivityLifecycleCallbacks(activityCb);
            activityCb.detach();
        }
        this.context = null;
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final int getWXAppSupportAPI() {
        if (this.detached) {
            throw new IllegalStateException("getWXAppSupportAPI fail, WXMsgImpl has been detached");
        }
        if (isWXAppInstalled()) {
            return new MMSharedPreferences(this.context).getInt("_build_info_sdk_int_", 0);
        }
        b.b(TAG, "open wx app failed, not installed or signature check failed");
        return 0;
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final boolean handleIntent(Intent intent, IWXAPIEventHandler iWXAPIEventHandler) {
        try {
            if (!WXApiImplComm.isIntentFromWx(intent, ConstantsAPI.Token.WX_TOKEN_VALUE_MSG)) {
                b.d(TAG, "handleIntent fail, intent not from weixin msg");
                return false;
            }
            if (this.detached) {
                throw new IllegalStateException("handleIntent fail, WXMsgImpl has been detached");
            }
            String stringExtra = intent.getStringExtra(ConstantsAPI.CONTENT);
            int intExtra = intent.getIntExtra(ConstantsAPI.SDK_VERSION, 0);
            String stringExtra2 = intent.getStringExtra(ConstantsAPI.APP_PACKAGE);
            if (stringExtra2 != null && stringExtra2.length() != 0) {
                if (!checkSumConsistent(intent.getByteArrayExtra(ConstantsAPI.CHECK_SUM), com.tencent.mm.sdk.a.a.b.a(stringExtra, intExtra, stringExtra2))) {
                    b.b(TAG, "checksum fail");
                    return false;
                }
                int intExtra2 = intent.getIntExtra("_wxapi_command_type", 0);
                if (intExtra2 == 9) {
                    iWXAPIEventHandler.onResp(new AddCardToWXCardPackage.Resp(intent.getExtras()));
                    return true;
                }
                if (intExtra2 == 12) {
                    iWXAPIEventHandler.onResp(new OpenWebview.Resp(intent.getExtras()));
                    return true;
                }
                if (intExtra2 == 14) {
                    iWXAPIEventHandler.onResp(new CreateChatroom.Resp(intent.getExtras()));
                    return true;
                }
                if (intExtra2 == 15) {
                    iWXAPIEventHandler.onResp(new JoinChatroom.Resp(intent.getExtras()));
                    return true;
                }
                switch (intExtra2) {
                    case 1:
                        iWXAPIEventHandler.onResp(new SendAuth.Resp(intent.getExtras()));
                        return true;
                    case 2:
                        iWXAPIEventHandler.onResp(new SendMessageToWX.Resp(intent.getExtras()));
                        return true;
                    case 3:
                        iWXAPIEventHandler.onReq(new GetMessageFromWX.Req(intent.getExtras()));
                        return true;
                    case 4:
                        iWXAPIEventHandler.onReq(new ShowMessageFromWX.Req(intent.getExtras()));
                        return true;
                    case 5:
                        iWXAPIEventHandler.onResp(new PayResp(intent.getExtras()));
                        return true;
                    case 6:
                        iWXAPIEventHandler.onReq(new LaunchFromWX.Req(intent.getExtras()));
                        return true;
                    default:
                        b.b(TAG, "unknown cmd = " + intExtra2);
                        return false;
                }
            }
            b.b(TAG, "invalid argument");
            return false;
        } catch (Exception e) {
            b.a(TAG, "handleIntent fail, ex = %s", e.getMessage());
        }
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final boolean isWXAppInstalled() {
        if (this.detached) {
            throw new IllegalStateException("isWXAppInstalled fail, WXMsgImpl has been detached");
        }
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME, 64);
            if (packageInfo == null) {
                return false;
            }
            return WXApiImplComm.validateAppSignature(this.context, packageInfo.signatures, this.checkSignature);
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final boolean isWXAppSupportAPI() {
        if (this.detached) {
            throw new IllegalStateException("isWXAppSupportAPI fail, WXMsgImpl has been detached");
        }
        return getWXAppSupportAPI() >= 587268097;
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final boolean openWXApp() {
        String str;
        if (this.detached) {
            throw new IllegalStateException("openWXApp fail, WXMsgImpl has been detached");
        }
        if (isWXAppInstalled()) {
            try {
                Context context = this.context;
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage(ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME));
                return true;
            } catch (Exception e) {
                str = "startActivity fail, exception = " + e.getMessage();
            }
        } else {
            str = "open wx app failed, not installed or signature check failed";
        }
        b.b(TAG, str);
        return false;
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final boolean registerApp(String str) {
        Application application;
        if (this.detached) {
            throw new IllegalStateException("registerApp fail, WXMsgImpl has been detached");
        }
        if (!WXApiImplComm.validateAppSignatureForPackage(this.context, ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            b.b(TAG, "register app failed for wechat app signature check failed");
            return false;
        }
        if (activityCb == null) {
            Context context = this.context;
            if (context instanceof Activity) {
                initMta(context, str);
                activityCb = new ActivityLifecycleCb(this.context);
                application = ((Activity) this.context).getApplication();
            } else if (context instanceof Service) {
                initMta(context, str);
                activityCb = new ActivityLifecycleCb(this.context);
                application = ((Service) this.context).getApplication();
            } else {
                b.c(TAG, "context is not instanceof Activity or Service, disable WXStat");
            }
            application.registerActivityLifecycleCallbacks(activityCb);
        }
        b.e(TAG, "registerApp, appId = " + str);
        if (str != null) {
            this.appId = str;
        }
        b.e(TAG, "register app " + this.context.getPackageName());
        com.tencent.mm.sdk.a.a.a.C0029a c0029a = new com.tencent.mm.sdk.a.a.a.C0029a();
        c0029a.aa = ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME;
        c0029a.ab = ConstantsAPI.ACTION_HANDLE_APP_REGISTER;
        c0029a.Y = "weixin://registerapp?appid=" + this.appId;
        return com.tencent.mm.sdk.a.a.a.a(this.context, c0029a);
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final boolean sendReq(BaseReq baseReq) {
        String str;
        if (this.detached) {
            throw new IllegalStateException("sendReq fail, WXMsgImpl has been detached");
        }
        if (!WXApiImplComm.validateAppSignatureForPackage(this.context, ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            str = "sendReq failed for wechat app signature check failed";
        } else {
            if (baseReq.checkArgs()) {
                b.e(TAG, "sendReq, req type = " + baseReq.getType());
                Bundle bundle = new Bundle();
                baseReq.toBundle(bundle);
                if (baseReq.getType() == 5) {
                    return sendPayReq(this.context, bundle);
                }
                if (baseReq.getType() == 7) {
                    return sendJumpToBizProfileReq(this.context, bundle);
                }
                if (baseReq.getType() == 8) {
                    return sendJumpToBizWebviewReq(this.context, bundle);
                }
                if (baseReq.getType() == 10) {
                    return sendJumpToBizTempSessionReq(this.context, bundle);
                }
                if (baseReq.getType() == 9) {
                    return sendAddCardToWX(this.context, bundle);
                }
                if (baseReq.getType() == 11) {
                    return sendOpenRankListReq(this.context, bundle);
                }
                if (baseReq.getType() == 12) {
                    return sendOpenWebview(this.context, bundle);
                }
                if (baseReq.getType() == 13) {
                    return sendOpenBusiLuckyMoney(this.context, bundle);
                }
                if (baseReq.getType() == 14) {
                    return createChatroom(this.context, bundle);
                }
                if (baseReq.getType() == 15) {
                    return joinChatroom(this.context, bundle);
                }
                a.C0028a c0028a = new a.C0028a();
                c0028a.Z = bundle;
                c0028a.Y = "weixin://sendreq?appid=" + this.appId;
                c0028a.W = ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME;
                c0028a.X = ConstantsAPI.WXApp.WXAPP_MSG_ENTRY_CLASSNAME;
                return a.a(this.context, c0028a);
            }
            str = "sendReq checkArgs fail";
        }
        b.b(TAG, str);
        return false;
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final boolean sendResp(BaseResp baseResp) {
        String str;
        if (this.detached) {
            throw new IllegalStateException("sendResp fail, WXMsgImpl has been detached");
        }
        if (!WXApiImplComm.validateAppSignatureForPackage(this.context, ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            str = "sendResp failed for wechat app signature check failed";
        } else {
            if (baseResp.checkArgs()) {
                Bundle bundle = new Bundle();
                baseResp.toBundle(bundle);
                a.C0028a c0028a = new a.C0028a();
                c0028a.Z = bundle;
                c0028a.Y = "weixin://sendresp?appid=" + this.appId;
                c0028a.W = ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME;
                c0028a.X = ConstantsAPI.WXApp.WXAPP_MSG_ENTRY_CLASSNAME;
                return a.a(this.context, c0028a);
            }
            str = "sendResp checkArgs fail";
        }
        b.b(TAG, str);
        return false;
    }

    @Override // com.tencent.mm.sdk.openapi.IWXAPI
    public final void unregisterApp() {
        if (this.detached) {
            throw new IllegalStateException("unregisterApp fail, WXMsgImpl has been detached");
        }
        if (!WXApiImplComm.validateAppSignatureForPackage(this.context, ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME, this.checkSignature)) {
            b.b(TAG, "unregister app failed for wechat app signature check failed");
            return;
        }
        b.e(TAG, "unregisterApp, appId = " + this.appId);
        String str = this.appId;
        if (str == null || str.length() == 0) {
            b.b(TAG, "unregisterApp fail, appId is empty");
            return;
        }
        b.e(TAG, "unregister app " + this.context.getPackageName());
        com.tencent.mm.sdk.a.a.a.C0029a c0029a = new com.tencent.mm.sdk.a.a.a.C0029a();
        c0029a.aa = ConstantsAPI.WXApp.WXAPP_PACKAGE_NAME;
        c0029a.ab = ConstantsAPI.ACTION_HANDLE_APP_UNREGISTER;
        c0029a.Y = "weixin://unregisterapp?appid=" + this.appId;
        com.tencent.mm.sdk.a.a.a.a(this.context, c0029a);
    }
}
