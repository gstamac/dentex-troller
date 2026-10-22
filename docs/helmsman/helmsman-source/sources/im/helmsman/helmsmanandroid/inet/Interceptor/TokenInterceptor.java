package im.helmsman.helmsmanandroid.inet.Interceptor;

import android.os.Build;
import android.util.Log;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.inet.model.UserMe;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/* JADX INFO: loaded from: classes2.dex */
public class TokenInterceptor implements Interceptor {
    private String agent = "Android/" + Build.VERSION.RELEASE + " App/" + MyApplication.getInstance().versionCode;

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        String userToken = UserMe.getUserToken();
        if (request.header("Authorization") != null) {
            return chain.proceed(request);
        }
        if (userToken != null) {
            Request requestBuild = request.newBuilder().addHeader("Authorization", userToken).addHeader("User-Agent", this.agent).build();
            Log.d("Authorization", userToken);
            Log.d("User-Agent", this.agent);
            return chain.proceed(requestBuild);
        }
        return chain.proceed(request.newBuilder().addHeader("User-Agent", this.agent).build());
    }
}
