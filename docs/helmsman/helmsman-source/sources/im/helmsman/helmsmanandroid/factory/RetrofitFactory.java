package im.helmsman.helmsmanandroid.factory;

import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.inet.ApiServer;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* JADX INFO: loaded from: classes2.dex */
public class RetrofitFactory {
    public static final String BASE_URL_20_DEBUG = "https://platform.ihelmsman.com/api/v2.0/";
    public static final String BASE_URL_20_RELEASE = "https://helmsman.im/api/v2.0/";
    public static final String BASE_URL_21_DEBUG = "https://platform.ihelmsman.com/api/v2.1/";
    public static final String BASE_URL_21_RELEASE = "https://helmsman.im/api/v2.1/";
    public static final String BASE_URL_22_DEBUG = "https://platform.ihelmsman.com/api/v2.2/";
    public static final String BASE_URL_22_RELEASE = "https://helmsman.im/api/v2.2/";
    public static final String I20 = "20";
    public static final String I21 = "21";
    public static final String I22 = "22";
    public static final String JPUSH = "jpush";
    public static final String JPUSH_URL = "https://device.jpush.cn/";
    public static final String PUBLIC = "public";
    public static final String PUBLIC_URL = "https://helmsman.im/";
    public static final String WXACCESSTOKEN = "wxAccessToken";
    public static final String WXUSERINFO = "wxuserinfo";
    public static final String WX_GET_ACCECSS_TOKEN_BASE_ADDRESS = "https://api.weixin.qq.com/sns/oauth2/";
    public static final String WX_GET_USERINFO = "https://api.weixin.qq.com/sns/";
    public static final String ZENDESK = "zendesk";
    public static final String ZENDESK_BASE_URL = "https://helmsman.zendesk.com/";
    private static RetrofitFactory factory;
    private Map<String, ApiServer> serverMap = new HashMap();

    public static RetrofitFactory builder() {
        if (factory == null) {
            synchronized (RetrofitFactory.class) {
                if (factory == null) {
                    factory = new RetrofitFactory();
                }
            }
        }
        return factory;
    }

    public ApiServer getServer(String str) {
        ApiServer apiServer = this.serverMap.get(str);
        if (apiServer != null) {
            return apiServer;
        }
        ApiServer apiServerCreateServer = createServer(str);
        this.serverMap.put(str, apiServerCreateServer);
        return apiServerCreateServer;
    }

    private ApiServer createServer(String str) {
        ApiServer apiServer;
        OkHttpClient httpClient = OkHttpFactory.getInstance().getHttpClient(OkHttpFactory.TYPE_HTTPS);
        str.hashCode();
        switch (str) {
            case "wxAccessToken":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(WX_GET_ACCECSS_TOKEN_BASE_ADDRESS).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            case "public":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(PUBLIC_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            case "zendesk":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(ZENDESK_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            case "wxuserinfo":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(WX_GET_USERINFO).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            case "20":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(getBaseUrl20()).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            case "21":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(getBaseUrl21()).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            case "22":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(getBaseUrl22()).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            case "jpush":
                apiServer = (ApiServer) new Retrofit.Builder().callFactory(httpClient).baseUrl(JPUSH_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServer.class);
                break;
            default:
                throw new RuntimeException("没有相应的api版本");
        }
        if (apiServer != null) {
            return apiServer;
        }
        throw new NullPointerException("不能创建相应的api版本");
    }

    public static String getBaseUrl20() {
        return MyApplication.getInstance().isApkInDebug() ? BASE_URL_20_DEBUG : BASE_URL_20_RELEASE;
    }

    public static String getBaseUrl21() {
        return MyApplication.getInstance().isApkInDebug() ? BASE_URL_21_DEBUG : BASE_URL_21_RELEASE;
    }

    public static String getBaseUrl22() {
        return MyApplication.getInstance().isApkInDebug() ? BASE_URL_22_DEBUG : BASE_URL_22_RELEASE;
    }
}
