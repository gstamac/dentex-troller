package im.helmsman.lib.net;

import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.facebook.internal.ServerProtocol;
import com.facebook.places.model.PlaceFields;
import com.qiniu.android.http.Client;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.util.MD5Util;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import kotlin.UByte;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class NetUtils {
    private static String activitedUrl = "http://192.168.199.131:8888/api/v1.0/h1/activation/confirmation?sn=";
    private static String activitionUrl = "http://192.168.199.131:8888/api/v1.0/h1/activation/verification?sn=";
    private static boolean isRequestAction = false;
    private String httpdownloadEphemerisFileUrl = "http://online-live1.services.u-blox.com/GetOnlineData.ashx?token=8_yZm-snnEq_q9_CIOXRkw;gnss=gps;datatype=eph,alm,aux,pos;format=aid;lat=22.1337987;lon=113.5379667;alt=20;";
    private String httpCheckFirmwareVersionUrl = "http://192.168.199.149:8888/api/v1.0/h1/boat_control/firmwares/latest";
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/helmsmanAidData";

    public interface ActivtionCallback {
        void onFailure(String str);

        void onSuccess(long j, String str);
    }

    public interface CheckFirmwareVersionCallback {
        public static final int FILE_CORRUPYION = 1;

        void onDownLoadFinish(byte[] bArr, int i, int i2);

        void onFailure(String str, int i);

        void onResult(int i, int i2);
    }

    public interface OnDownLoadEphemerisFileListener {
        void onComplete(String str, short[] sArr);

        void onFailure(int i);
    }

    public interface OndownloadDeclinationDataFileCallback {
        void onComplete(File file);

        void onError();
    }

    public void downloadEphemerisFile(String str, OnDownLoadEphemerisFileListener onDownLoadEphemerisFileListener) {
        LocationManager locationManager = (LocationManager) HelmsmanSDK.getContext().getSystemService(PlaceFields.LOCATION);
        List<String> providers = locationManager.getProviders(true);
        String str2 = "gps";
        if (!providers.contains("gps")) {
            str2 = "network";
            if (!providers.contains("network")) {
                onDownLoadEphemerisFileListener.onFailure(-4);
                return;
            }
        }
        if (ActivityCompat.checkSelfPermission(HelmsmanSDK.getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(HelmsmanSDK.getContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(str2);
            if (lastKnownLocation != null) {
                Log.e("lat", lastKnownLocation.getLatitude() + "");
                Log.e("lon", lastKnownLocation.getLongitude() + "");
                Log.e("alt", lastKnownLocation.getAltitude() + "");
                downloadEphemerisFile(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAltitude(), str, onDownLoadEphemerisFileListener);
                return;
            }
            Log.e("获取不到", "获取不到位置");
            return;
        }
        onDownLoadEphemerisFileListener.onFailure(-5);
    }

    public void downloadEphemerisFile(double d, double d2, double d3, String str, OnDownLoadEphemerisFileListener onDownLoadEphemerisFileListener) {
        String str2 = this.httpdownloadEphemerisFileUrl + "format=" + str + ";lat=" + d + ";lon=" + d2 + ";alt=" + d3 + ";";
        this.httpdownloadEphemerisFileUrl = str2;
        byte[] bArrDownloadFile = downloadFile(str2);
        saveFile(bArrDownloadFile, this.path, "gps.ubx");
        short[] sArr = new short[bArrDownloadFile.length];
        for (int i = 0; i < bArrDownloadFile.length; i++) {
            sArr[i] = (short) (bArrDownloadFile[i] & UByte.MAX_VALUE);
        }
        onDownLoadEphemerisFileListener.onComplete(this.path + "/gps.ubx", sArr);
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int i = inputStream.read(bArr);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                byteArrayOutputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [im.helmsman.lib.net.NetUtils$1] */
    public void checkFirmwareVersionAnddownLoadFile(final int i, final int i2, final CheckFirmwareVersionCallback checkFirmwareVersionCallback) {
        new Thread() { // from class: im.helmsman.lib.net.NetUtils.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(NetUtils.this.httpCheckFirmwareVersionUrl).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection.setRequestProperty(Client.ContentTypeHeader, "text/xml; charset=UTF-8");
                    if (httpURLConnection.getResponseCode() == 200) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            } else {
                                sb.append(line);
                            }
                        }
                        JSONObject jSONObject = new JSONObject(sb.toString());
                        int i3 = jSONObject.getInt("build");
                        int i4 = jSONObject.getInt(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION);
                        String string = jSONObject.getString("md5");
                        checkFirmwareVersionCallback.onResult(i4, i3);
                        if (i4 > i || i2 < i3) {
                            byte[] bArrDownloadFile = NetUtils.this.downloadFile("http://" + jSONObject.getString("download_url"));
                            if (string.equals(MD5Util.getMd5ByByte(bArrDownloadFile))) {
                                checkFirmwareVersionCallback.onDownLoadFinish(bArrDownloadFile, i4, i3);
                                NetUtils netUtils = NetUtils.this;
                                netUtils.saveFile(bArrDownloadFile, netUtils.path, "update.bin");
                                return;
                            } else {
                                checkFirmwareVersionCallback.onFailure("FILE_CORRUPYION", 1);
                                Log.d("FILE_CORRUPYION", "FILE_CORRUPYION");
                                return;
                            }
                        }
                        return;
                    }
                    Log.e("getResponseCode", httpURLConnection.getResponseCode() + "");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }.start();
    }

    public byte[] downloadFile(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            Log.e("NetUtils", str);
            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] inputStream2 = readInputStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return inputStream2;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new byte[0];
        } catch (IOException e2) {
            e2.printStackTrace();
            return new byte[0];
        }
    }

    public File saveFile(byte[] bArr, String str, String str2) {
        Log.d("saveFile", str + "/" + str2);
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file + File.separator + str2);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(bArr);
            fileOutputStream.close();
            return file2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Type inference failed for: r7v0, types: [im.helmsman.lib.net.NetUtils$2] */
    public void downloadDeclinationDataFile(final double d, final double d2, final OndownloadDeclinationDataFileCallback ondownloadDeclinationDataFileCallback) {
        new Thread() { // from class: im.helmsman.lib.net.NetUtils.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                super.run();
                byte[] bArrDownloadFile = NetUtils.this.downloadFile("https://www.ngdc.noaa.gov/geomag-web/calculators/calculateDeclination?lat1=" + d + "&lon1=" + d2 + "&resultFormat=xml");
                NetUtils netUtils = NetUtils.this;
                File fileSaveFile = netUtils.saveFile(bArrDownloadFile, netUtils.path, "DeclinationData.xml");
                OndownloadDeclinationDataFileCallback ondownloadDeclinationDataFileCallback2 = ondownloadDeclinationDataFileCallback;
                if (ondownloadDeclinationDataFileCallback2 != null) {
                    ondownloadDeclinationDataFileCallback2.onComplete(fileSaveFile);
                }
            }
        }.start();
    }

    public static void activitionRequest(String str, ActivtionCallback activtionCallback) {
        if (isRequestAction) {
            return;
        }
        isRequestAction = true;
        try {
            try {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(activitionUrl + str).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection.setRequestProperty(Client.ContentTypeHeader, "text/xml; charset=UTF-8");
                    if (httpURLConnection.getResponseCode() == 200) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            } else {
                                sb.append(line);
                            }
                        }
                        String string = sb.toString();
                        JSONObject jSONObject = new JSONObject(string);
                        Log.e("json", string);
                        activtionCallback.onSuccess(jSONObject.getLong("activation_timestamp"), jSONObject.getString("sn"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    activtionCallback.onFailure(e.getMessage());
                }
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
                activtionCallback.onFailure(e2.getMessage());
            } catch (IOException e3) {
                e3.printStackTrace();
                activtionCallback.onFailure(e3.getMessage());
            }
        } finally {
            isRequestAction = false;
        }
    }

    public void deviceActivated(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(activitedUrl + str).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty(Client.ContentTypeHeader, "text/xml; charset=UTF-8");
            if (httpURLConnection.getResponseCode() != 200) {
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    String string = sb.toString();
                    new JSONObject(string);
                    Log.e("json", string);
                    return;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
    }
}
