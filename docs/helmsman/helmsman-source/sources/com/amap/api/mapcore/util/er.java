package com.amap.api.mapcore.util;

import com.MAVLink.enums.MAV_CMD;
import com.akexorcist.roundcornerprogressbar.BuildConfig;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.facebook.appevents.AppEventsConstants;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: CoreUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class er {
    private static String[] a = {"com.amap.api.trace", "com.amap.api.trace.core"};

    public static void a(String str, String str2) throws eo {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("status") && jSONObject.has("infocode")) {
                String string = jSONObject.getString("status");
                int i = jSONObject.getInt("infocode");
                if (!string.equals("1") && string.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    switch (i) {
                        case GLMapStaticValue.AM_CALLBACK_CHANGEMAPLOGO /* 10001 */:
                            throw new eo("用户key不正确或过期");
                        case GLMapStaticValue.AM_CALLBACK_NEED_NEXTFRAME /* 10002 */:
                            throw new eo("请求服务不存在");
                        case 10003:
                            throw new eo("访问已超出日访问量");
                        case 10004:
                            throw new eo("用户访问过于频繁");
                        case 10005:
                            throw new eo("用户IP无效");
                        case 10006:
                            throw new eo("用户域名无效");
                        case 10007:
                            throw new eo("用户签名未通过");
                        case 10008:
                            throw new eo("用户MD5安全码未通过");
                        case 10009:
                            throw new eo("请求key与绑定平台不符");
                        case 10010:
                            throw new eo("IP访问超限");
                        case 10011:
                            throw new eo("服务不支持https请求");
                        case 10012:
                            throw new eo("权限不足，服务请求被拒绝");
                        case 10013:
                            throw new eo("开发者删除了key，key被删除后无法正常使用");
                        default:
                            switch (i) {
                                case 20000:
                                    throw new eo("请求参数非法");
                                case 20001:
                                    throw new eo("缺少必填参数");
                                case 20002:
                                    throw new eo("请求协议非法");
                                case BuildConfig.VERSION_CODE /* 20003 */:
                                    throw new eo("其他未知错误");
                                default:
                                    switch (i) {
                                        case 30000:
                                            throw new eo("请求服务响应错误");
                                        case MAV_CMD.MAV_CMD_PAYLOAD_PREPARE_DEPLOY /* 30001 */:
                                            throw new eo("引擎返回数据异常");
                                        case MAV_CMD.MAV_CMD_PAYLOAD_CONTROL_DEPLOY /* 30002 */:
                                            throw new eo("服务端请求链接超时");
                                        case 30003:
                                            throw new eo("读取服务结果超时");
                                        default:
                                            throw new eo(jSONObject.getString("info"));
                                    }
                            }
                    }
                }
            }
        } catch (JSONException unused) {
            throw new eo(AMapException.ERROR_PROTOCOL);
        }
    }

    public static int a(List<LatLng> list) {
        int i = 0;
        if (list == null || list.size() == 0) {
            return 0;
        }
        int iCalculateLineDistance = 0;
        while (i < list.size() - 1) {
            LatLng latLng = list.get(i);
            i++;
            LatLng latLng2 = list.get(i);
            if (latLng == null || latLng2 == null) {
                break;
            }
            iCalculateLineDistance = (int) (iCalculateLineDistance + AMapUtils.calculateLineDistance(latLng, latLng2));
        }
        return iCalculateLineDistance;
    }
}
