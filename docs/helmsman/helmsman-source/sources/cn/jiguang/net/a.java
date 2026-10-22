package cn.jiguang.net;

import android.content.Context;
import android.os.AsyncTask;

/* JADX INFO: loaded from: classes.dex */
final class a extends AsyncTask<HttpRequest, Void, HttpResponse> {
    private HttpUtils.HttpListener a;
    private Context b;

    public a(Context context, HttpUtils.HttpListener httpListener) {
        this.a = httpListener;
        this.b = context;
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ HttpResponse doInBackground(HttpRequest[] httpRequestArr) {
        HttpRequest[] httpRequestArr2 = httpRequestArr;
        if (httpRequestArr2 == null || httpRequestArr2.length == 0) {
            return null;
        }
        return HttpUtils.httpGet(this.b, httpRequestArr2[0]);
    }

    @Override // android.os.AsyncTask
    protected final /* bridge */ /* synthetic */ void onPostExecute(HttpResponse httpResponse) {
    }

    @Override // android.os.AsyncTask
    protected final void onPreExecute() {
    }
}
