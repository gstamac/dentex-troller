package cn.jiguang.net;

import android.content.Context;
import android.os.AsyncTask;

/* JADX INFO: loaded from: classes.dex */
final class b extends AsyncTask<String, Void, HttpResponse> {
    private HttpUtils.HttpListener a;
    private Context b;

    public b(HttpUtils.HttpListener httpListener, Context context) {
        this.a = httpListener;
        this.b = context;
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ HttpResponse doInBackground(String[] strArr) {
        String[] strArr2 = strArr;
        if (strArr2 == null || strArr2.length == 0) {
            return null;
        }
        return HttpUtils.httpGet(this.b, strArr2[0]);
    }

    @Override // android.os.AsyncTask
    protected final /* bridge */ /* synthetic */ void onPostExecute(HttpResponse httpResponse) {
    }

    @Override // android.os.AsyncTask
    protected final void onPreExecute() {
    }
}
