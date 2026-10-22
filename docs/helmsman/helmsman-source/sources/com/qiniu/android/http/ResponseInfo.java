package com.qiniu.android.http;

import com.qiniu.android.collect.Config;
import com.qiniu.android.collect.UploadInfoCollector;
import com.qiniu.android.common.Constants;
import com.qiniu.android.storage.UpToken;
import com.qiniu.android.utils.StringUtils;
import java.util.Locale;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class ResponseInfo {
    public static final int Cancelled = -2;
    public static final int CannotConnectToHost = -1004;
    public static final int InvalidArgument = -4;
    public static final int InvalidFile = -3;
    public static final int InvalidToken = -5;
    public static final int NetworkConnectionLost = -1005;
    public static final int NetworkError = -1;
    public static final int TimedOut = -1001;
    public static final int UnknownError = 0;
    public static final int UnknownHost = -1003;
    public static final int ZeroSizeFile = -6;
    public final long duration;
    public final String error;
    public final String host;
    public final String ip;
    public final String path;
    public final int port;
    public final String reqId;
    public final JSONObject response;
    public final long sent;
    public final int statusCode;
    public final long totalSize;
    public final UpToken upToken;
    public final String xlog;
    public final String xvia;
    public final String id = UserAgent.instance().id;
    public final long timeStamp = System.currentTimeMillis() / 1000;

    public static boolean isStatusCodeForBrokenNetwork(int i) {
        return i == -1 || i == -1003 || i == -1004 || i == -1001 || i == -1005;
    }

    private ResponseInfo(JSONObject jSONObject, int i, String str, String str2, String str3, String str4, String str5, String str6, int i2, long j, long j2, String str7, UpToken upToken, long j3) {
        this.response = jSONObject;
        this.statusCode = i;
        this.reqId = str;
        this.xlog = str2;
        this.xvia = str3;
        this.host = str4;
        this.path = str5;
        this.duration = j;
        this.error = str7;
        this.ip = str6;
        this.port = i2;
        this.sent = j2;
        this.upToken = upToken;
        this.totalSize = j3;
    }

    public static ResponseInfo create(JSONObject jSONObject, final int i, final String str, String str2, String str3, final String str4, final String str5, String str6, final int i2, final long j, final long j2, String str7, UpToken upToken, final long j3) {
        UpToken upToken2;
        String str8 = (str6 + "").split(":")[0];
        final String strSubstring = str8.substring(Math.max(0, str8.indexOf("/") + 1));
        ResponseInfo responseInfo = new ResponseInfo(jSONObject, i, str, str2, str3, str4, str5, strSubstring, i2, j, j2, str7, upToken, j3);
        if (Config.isRecord) {
            upToken2 = upToken;
        } else {
            upToken2 = upToken;
            if (upToken2 == null) {
                return responseInfo;
            }
        }
        final String str9 = responseInfo.timeStamp + "";
        UploadInfoCollector.handleHttp(upToken2, new UploadInfoCollector.RecordMsg() { // from class: com.qiniu.android.http.ResponseInfo.1
            @Override // com.qiniu.android.collect.UploadInfoCollector.RecordMsg
            public String toRecordMsg() {
                return StringUtils.join(new String[]{i + "", str, str4, strSubstring, i2 + "", j + "", str9, j2 + "", ResponseInfo.getUpType(str5), j3 + ""}, ",");
            }
        });
        return responseInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getUpType(String str) {
        if (str == null || !str.startsWith("/")) {
            return "";
        }
        if ("/".equals(str)) {
            return "form";
        }
        byte b = 1;
        int iIndexOf = str.indexOf(47, 1);
        if (iIndexOf < 1) {
            return "";
        }
        String strSubstring = str.substring(1, iIndexOf);
        strSubstring.hashCode();
        switch (strSubstring.hashCode()) {
            case -1072430054:
                b = !strSubstring.equals("mkfile") ? (byte) -1 : (byte) 0;
                break;
            case 111375:
                if (!strSubstring.equals("put")) {
                    b = -1;
                }
                break;
            case 3030893:
                b = !strSubstring.equals("bput") ? (byte) -1 : (byte) 2;
                break;
            case 103949059:
                b = !strSubstring.equals("mkblk") ? (byte) -1 : (byte) 3;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return "mkfile";
            case 1:
                return "put";
            case 2:
                return "bput";
            case 3:
                return "mkblk";
            default:
                return "";
        }
    }

    public static ResponseInfo zeroSize(UpToken upToken) {
        return create(null, -6, "", "", "", "", "", "", 80, 0L, 0L, "file or data size is zero", upToken, 0L);
    }

    public static ResponseInfo cancelled(UpToken upToken) {
        return create(null, -2, "", "", "", "", "", "", 80, -1L, -1L, "cancelled by user", upToken, 0L);
    }

    public static ResponseInfo invalidArgument(String str, UpToken upToken) {
        return create(null, -4, "", "", "", "", "", "", 80, 0L, 0L, str, upToken, 0L);
    }

    public static ResponseInfo invalidToken(String str) {
        return create(null, -5, "", "", "", "", "", "", 80, 0L, 0L, str, null, 0L);
    }

    public static ResponseInfo fileError(Exception exc, UpToken upToken) {
        return create(null, -3, "", "", "", "", "", "", 80, 0L, 0L, exc.getMessage(), upToken, 0L);
    }

    public static ResponseInfo networkError(int i, UpToken upToken) {
        return create(null, i, "", "", "", "", "", "", 80, 0L, 0L, "Network error during preQuery", upToken, 0L);
    }

    public boolean isCancelled() {
        return this.statusCode == -2;
    }

    public boolean isOK() {
        return this.statusCode == 200 && this.error == null && (hasReqId() || this.response != null);
    }

    public boolean isNetworkBroken() {
        int i = this.statusCode;
        return i == -1 || i == -1003 || i == -1004 || i == -1001 || i == -1005;
    }

    public boolean isServerError() {
        int i = this.statusCode;
        return (i >= 500 && i < 600 && i != 579) || i == 996;
    }

    public boolean needSwitchServer() {
        return isNetworkBroken() || isServerError();
    }

    public boolean needRetry() {
        int i;
        return !isCancelled() && (needSwitchServer() || (i = this.statusCode) == 406 || ((i == 200 && this.error != null) || (isNotQiniu() && !this.upToken.hasReturnUrl())));
    }

    public boolean isNotQiniu() {
        int i = this.statusCode;
        return i < 500 && i >= 200 && !hasReqId() && this.response == null;
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "{ver:%s,ResponseInfo:%s,status:%d, reqId:%s, xlog:%s, xvia:%s, host:%s, path:%s, ip:%s, port:%d, duration:%d s, time:%d, sent:%d,error:%s}", Constants.VERSION, this.id, Integer.valueOf(this.statusCode), this.reqId, this.xlog, this.xvia, this.host, this.path, this.ip, Integer.valueOf(this.port), Long.valueOf(this.duration), Long.valueOf(this.timeStamp), Long.valueOf(this.sent), this.error);
    }

    public boolean hasReqId() {
        return this.reqId != null;
    }
}
