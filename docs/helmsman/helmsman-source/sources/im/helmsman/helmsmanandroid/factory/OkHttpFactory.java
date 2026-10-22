package im.helmsman.helmsmanandroid.factory;

import android.support.v4.media.session.PlaybackStateCompat;
import com.zhy.http.okhttp.https.HttpsUtils;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.inet.Interceptor.TokenInterceptor;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/* JADX INFO: loaded from: classes2.dex */
public class OkHttpFactory {
    public static final String TYPE_HTTP = "http";
    public static final String TYPE_HTTPS = "https";
    private static OkHttpFactory instance;

    private OkHttpFactory() {
    }

    public static OkHttpFactory getInstance() {
        if (instance == null) {
            instance = new OkHttpFactory();
        }
        return instance;
    }

    public OkHttpClient getHttpClient(String str) {
        str.hashCode();
        if (str.equals(TYPE_HTTP) || str.equals(TYPE_HTTPS)) {
            File file = new File(MyApplication.getContext().getCacheDir().getAbsolutePath() + "/okhttpCache");
            if (!file.exists()) {
                file.mkdirs();
            }
            OkHttpClient.Builder builderWriteTimeout = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor()).connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS);
            if (file.exists()) {
                builderWriteTimeout.cache(new Cache(file, PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE));
            }
            return builderWriteTimeout.build();
        }
        throw new RuntimeException("无效的类型");
    }

    public SSLSocketFactory loadCerFile(String str) {
        try {
            return HttpsUtils.getSslSocketFactory(new InputStream[]{MyApplication.getContext().getAssets().open(str)}, null, null).sSLSocketFactory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
