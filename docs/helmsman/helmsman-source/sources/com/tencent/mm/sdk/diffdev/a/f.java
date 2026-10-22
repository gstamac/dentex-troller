package com.tencent.mm.sdk.diffdev.a;

import android.os.AsyncTask;
import android.util.Log;
import com.qiniu.android.common.Constants;
import com.tencent.mm.sdk.diffdev.OAuthErrCode;
import com.tencent.mm.sdk.diffdev.OAuthListener;
import com.zendesk.service.HttpConstants;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
final class f extends AsyncTask<Void, Void, a> {
    private OAuthListener an;
    private String aq;
    private int aw;
    private String url;

    static class a {
        public OAuthErrCode ap;
        public String ax;
        public int ay;

        a() {
        }

        public static a e(byte[] bArr) {
            OAuthErrCode oAuthErrCode;
            String str;
            OAuthErrCode oAuthErrCode2;
            a aVar = new a();
            if (bArr == null || bArr.length == 0) {
                Log.e("MicroMsg.SDK.NoopingResult", "parse fail, buf is null");
                oAuthErrCode = OAuthErrCode.WechatAuth_Err_NetworkErr;
            } else {
                try {
                    try {
                        JSONObject jSONObject = new JSONObject(new String(bArr, Constants.UTF_8));
                        int i = jSONObject.getInt("wx_errcode");
                        aVar.ay = i;
                        Log.d("MicroMsg.SDK.NoopingResult", String.format("nooping uuidStatusCode = %d", Integer.valueOf(i)));
                        int i2 = aVar.ay;
                        if (i2 == 408) {
                            oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_OK;
                        } else if (i2 != 500) {
                            switch (i2) {
                                case 402:
                                    oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_Timeout;
                                    break;
                                case 403:
                                    oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_Cancel;
                                    break;
                                case HttpConstants.HTTP_NOT_FOUND /* 404 */:
                                    oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_OK;
                                    break;
                                case HttpConstants.HTTP_BAD_METHOD /* 405 */:
                                    aVar.ap = OAuthErrCode.WechatAuth_Err_OK;
                                    aVar.ax = jSONObject.getString("wx_code");
                                    return aVar;
                                default:
                                    oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_NormalErr;
                                    break;
                            }
                        } else {
                            oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_NormalErr;
                        }
                        aVar.ap = oAuthErrCode2;
                        return aVar;
                    } catch (Exception e) {
                        str = String.format("parse json fail, ex = %s", e.getMessage());
                        Log.e("MicroMsg.SDK.NoopingResult", str);
                        oAuthErrCode = OAuthErrCode.WechatAuth_Err_NormalErr;
                        aVar.ap = oAuthErrCode;
                        return aVar;
                    }
                } catch (Exception e2) {
                    str = String.format("parse fail, build String fail, ex = %s", e2.getMessage());
                }
            }
            aVar.ap = oAuthErrCode;
            return aVar;
        }
    }

    public f(String str, OAuthListener oAuthListener) {
        this.aq = str;
        this.an = oAuthListener;
        this.url = String.format("https://long.open.weixin.qq.com/connect/l/qrconnect?f=json&uuid=%s", str);
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ a doInBackground(Void[] voidArr) {
        a aVar;
        OAuthErrCode oAuthErrCode;
        String str = this.aq;
        if (str == null || str.length() == 0) {
            Log.e("MicroMsg.SDK.NoopingTask", "run fail, uuid is null");
            aVar = new a();
            oAuthErrCode = OAuthErrCode.WechatAuth_Err_NormalErr;
        } else {
            while (!isCancelled()) {
                String str2 = this.url + (this.aw == 0 ? "" : "&last=" + this.aw);
                long jCurrentTimeMillis = System.currentTimeMillis();
                byte[] bArrB = e.b(str2, 60000);
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                a aVarE = a.e(bArrB);
                Log.d("MicroMsg.SDK.NoopingTask", String.format("nooping, url = %s, errCode = %s, uuidStatusCode = %d, time consumed = %d(ms)", str2, aVarE.ap.toString(), Integer.valueOf(aVarE.ay), Long.valueOf(jCurrentTimeMillis2 - jCurrentTimeMillis)));
                if (aVarE.ap != OAuthErrCode.WechatAuth_Err_OK) {
                    Log.e("MicroMsg.SDK.NoopingTask", String.format("nooping fail, errCode = %s, uuidStatusCode = %d", aVarE.ap.toString(), Integer.valueOf(aVarE.ay)));
                    return aVarE;
                }
                this.aw = aVarE.ay;
                if (aVarE.ay == g.UUID_SCANED.getCode()) {
                    this.an.onQrcodeScanned();
                } else if (aVarE.ay != g.UUID_KEEP_CONNECT.getCode() && aVarE.ay == g.UUID_CONFIRM.getCode()) {
                    if (aVarE.ax == null || aVarE.ax.length() == 0) {
                        Log.e("MicroMsg.SDK.NoopingTask", "nooping fail, confirm with an empty code!!!");
                        aVarE.ap = OAuthErrCode.WechatAuth_Err_NormalErr;
                    }
                    return aVarE;
                }
            }
            Log.i("MicroMsg.SDK.NoopingTask", "IDiffDevOAuth.stopAuth / detach invoked");
            aVar = new a();
            oAuthErrCode = OAuthErrCode.WechatAuth_Err_Auth_Stopped;
        }
        aVar.ap = oAuthErrCode;
        return aVar;
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ void onPostExecute(a aVar) {
        a aVar2 = aVar;
        this.an.onAuthFinish(aVar2.ap, aVar2.ax);
    }
}
