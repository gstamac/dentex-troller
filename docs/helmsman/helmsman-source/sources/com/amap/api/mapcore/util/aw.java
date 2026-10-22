package com.amap.api.mapcore.util;

import android.content.Context;
import cn.jpush.android.api.JThirdPlatFormInterface;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.facebook.internal.ServerProtocol;
import com.qiniu.android.common.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import okhttp3.HttpUrl;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: UpdateItem.java */
/* JADX INFO: loaded from: classes.dex */
@fu(a = "update_item", b = true)
public class aw extends az {
    private String n = "";
    private Context o;

    public aw() {
    }

    public aw(OfflineMapCity offlineMapCity, Context context) {
        this.o = context;
        this.a = offlineMapCity.getCity();
        this.c = offlineMapCity.getAdcode();
        this.b = offlineMapCity.getUrl();
        this.g = offlineMapCity.getSize();
        this.e = offlineMapCity.getVersion();
        this.k = offlineMapCity.getCode();
        this.i = 0;
        this.l = offlineMapCity.getState();
        this.j = offlineMapCity.getcompleteCode();
        this.m = offlineMapCity.getPinyin();
        a();
    }

    public aw(OfflineMapProvince offlineMapProvince, Context context) {
        this.o = context;
        this.a = offlineMapProvince.getProvinceName();
        this.c = offlineMapProvince.getProvinceCode();
        this.b = offlineMapProvince.getUrl();
        this.g = offlineMapProvince.getSize();
        this.e = offlineMapProvince.getVersion();
        this.i = 1;
        this.l = offlineMapProvince.getState();
        this.j = offlineMapProvince.getcompleteCode();
        this.m = offlineMapProvince.getPinyin();
        a();
    }

    protected void a() {
        this.d = dw.b(this.o) + this.m + ".zip.tmp";
    }

    public String b() {
        return this.n;
    }

    public void a(String str) {
        this.n = str;
    }

    public void b(String str) {
        JSONObject jSONObject;
        if (str != null) {
            try {
                if (str.equals("") || (jSONObject = new JSONObject(str).getJSONObject("file")) == null) {
                    return;
                }
                this.a = jSONObject.optString("title");
                this.c = jSONObject.optString(JThirdPlatFormInterface.KEY_CODE);
                this.b = jSONObject.optString("url");
                this.d = jSONObject.optString("fileName");
                this.f = jSONObject.optLong("lLocalLength");
                this.g = jSONObject.optLong("lRemoteLength");
                this.l = jSONObject.optInt("mState");
                this.e = jSONObject.optString(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION);
                this.h = jSONObject.optString("localPath");
                this.n = jSONObject.optString("vMapFileNames");
                this.i = jSONObject.optInt("isSheng");
                this.j = jSONObject.optInt("mCompleteCode");
                this.k = jSONObject.optString("mCityCode");
                this.m = a(jSONObject, "pinyin");
                if (this.m.equals("")) {
                    String strSubstring = this.b.substring(this.b.lastIndexOf("/") + 1);
                    this.m = strSubstring.substring(0, strSubstring.lastIndexOf("."));
                }
            } catch (Throwable th) {
                fn.b(th, "UpdateItem", "readFileToJSONObject");
                th.printStackTrace();
            }
        }
    }

    public void c() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("title", this.a);
            jSONObject2.put(JThirdPlatFormInterface.KEY_CODE, this.c);
            jSONObject2.put("url", this.b);
            jSONObject2.put("fileName", this.d);
            jSONObject2.put("lLocalLength", this.f);
            jSONObject2.put("lRemoteLength", this.g);
            jSONObject2.put("mState", this.l);
            jSONObject2.put(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, this.e);
            jSONObject2.put("localPath", this.h);
            String str = this.n;
            if (str != null) {
                jSONObject2.put("vMapFileNames", str);
            }
            jSONObject2.put("isSheng", this.i);
            jSONObject2.put("mCompleteCode", this.j);
            jSONObject2.put("mCityCode", this.k);
            jSONObject2.put("pinyin", this.m);
            jSONObject.put("file", jSONObject2);
            File file = new File(this.d + ".dt");
            file.delete();
            OutputStreamWriter outputStreamWriter = null;
            try {
                try {
                    OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(new FileOutputStream(file, true), Constants.UTF_8);
                    try {
                        outputStreamWriter2.write(jSONObject.toString());
                        try {
                            outputStreamWriter2.close();
                        } catch (IOException e) {
                            e = e;
                            e.printStackTrace();
                        }
                    } catch (IOException e2) {
                        e = e2;
                        outputStreamWriter = outputStreamWriter2;
                        fn.b(e, "UpdateItem", "saveJSONObjectToFile");
                        e.printStackTrace();
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (IOException e3) {
                                e = e3;
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        outputStreamWriter = outputStreamWriter2;
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e = e5;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            fn.b(th3, "UpdateItem", "saveJSONObjectToFile parseJson");
            th3.printStackTrace();
        }
    }

    public static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject == null || !jSONObject.has(str) || jSONObject.getString(str).equals(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI)) ? "" : jSONObject.optString(str).trim();
    }
}
