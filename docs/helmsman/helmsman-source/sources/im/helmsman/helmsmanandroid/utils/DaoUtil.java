package im.helmsman.helmsmanandroid.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.maps.model.LatLng;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.Boat;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.dao.RouteModel;
import im.helmsman.helmsmanandroid.dao.WayPoint;
import im.helmsman.helmsmanandroid.inet.model.GetFriendsMessageResultModel;
import im.helmsman.helmsmanandroid.inet.model.LikeRouteListModel;
import im.helmsman.helmsmanandroid.polyline.it.rambow.master.javautils.PolylineEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

/* JADX INFO: loaded from: classes2.dex */
public class DaoUtil {
    private static DaoUtil daoUtil;
    public static final String imagePath = MyApplication.getInstance().getApplicationContext().getFilesDir().getAbsolutePath();

    public static DaoUtil getInstance() {
        if (daoUtil == null) {
            daoUtil = new DaoUtil();
        }
        return daoUtil;
    }

    public int saveWayPointList(List<MLatLng> list) {
        return saveWayPointList(MyApplication.getInstance().getString(R.string.edit_no_name), list, null, null);
    }

    public int saveWayPointList(List<MLatLng> list, Bitmap bitmap) {
        return saveWayPointList((String) null, list, bitmap);
    }

    public int saveWayPointList(String str, List<MLatLng> list, Bitmap bitmap) {
        return saveWayPointList(str, list, bitmap, null);
    }

    public int saveWayPointList(String str, List<MLatLng> list) {
        return saveWayPointList(str, list, null, null);
    }

    public RouteModel copyRoute(RouteModel routeModel) {
        RouteModel routeModel2 = new RouteModel();
        String str = System.currentTimeMillis() + "";
        copyImage(routeModel.getPictureName(), str);
        routeModel2.setEncodePolyline(routeModel.getEncodePolyline());
        routeModel2.setPictureName(str);
        routeModel2.setName(routeModel.getName());
        routeModel2.setDate(System.currentTimeMillis());
        List<WayPoint> listLntLngToWayPointList = MLatLng.LntLngToWayPointList(wayPointListToLatLng(routeModel.getWaypoint()));
        routeModel2.setWaypoint(listLntLngToWayPointList);
        routeModel2.save();
        saveWayPoints(routeModel2.getId(), listLntLngToWayPointList);
        return routeModel2;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [im.helmsman.helmsmanandroid.utils.DaoUtil$1] */
    private void copyImage(final String str, final String str2) {
        new Thread() { // from class: im.helmsman.helmsmanandroid.utils.DaoUtil.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v1, types: [java.io.File] */
            /* JADX WARN: Type inference failed for: r0v10 */
            /* JADX WARN: Type inference failed for: r0v11, types: [java.io.OutputStream] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            /* JADX WARN: Type inference failed for: r0v14 */
            /* JADX WARN: Type inference failed for: r0v15 */
            /* JADX WARN: Type inference failed for: r0v16 */
            /* JADX WARN: Type inference failed for: r0v17 */
            /* JADX WARN: Type inference failed for: r0v18, types: [java.io.FileOutputStream, java.io.OutputStream] */
            /* JADX WARN: Type inference failed for: r0v19 */
            /* JADX WARN: Type inference failed for: r0v20 */
            /* JADX WARN: Type inference failed for: r0v3 */
            /* JADX WARN: Type inference failed for: r0v4 */
            /* JADX WARN: Type inference failed for: r0v5 */
            /* JADX WARN: Type inference failed for: r0v6 */
            /* JADX WARN: Type inference failed for: r0v7, types: [java.io.OutputStream] */
            /* JADX WARN: Type inference failed for: r0v9, types: [java.io.OutputStream] */
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws Throwable {
                if (str == null) {
                    return;
                }
                ?? file = new File(DaoUtil.imagePath, str);
                File file2 = new File(DaoUtil.imagePath, str2);
                FileInputStream fileInputStream = null;
                try {
                    try {
                        try {
                            FileInputStream fileInputStream2 = new FileInputStream((File) file);
                            try {
                                file = new FileOutputStream(file2);
                                try {
                                    byte[] bArr = new byte[64];
                                    while (true) {
                                        int i = fileInputStream2.read(bArr);
                                        if (i > 0) {
                                            file.write(bArr, 0, i);
                                        } else {
                                            try {
                                                break;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    fileInputStream2.close();
                                    file.close();
                                } catch (FileNotFoundException e2) {
                                    e = e2;
                                    fileInputStream = fileInputStream2;
                                    file = file;
                                    e.printStackTrace();
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                        } catch (IOException e3) {
                                            e3.printStackTrace();
                                        }
                                    }
                                    if (file != 0) {
                                        file.close();
                                    }
                                } catch (IOException e4) {
                                    e = e4;
                                    fileInputStream = fileInputStream2;
                                    file = file;
                                    e.printStackTrace();
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                        } catch (IOException e5) {
                                            e5.printStackTrace();
                                        }
                                    }
                                    if (file != 0) {
                                        file.close();
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    fileInputStream = fileInputStream2;
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                        } catch (IOException e6) {
                                            e6.printStackTrace();
                                        }
                                    }
                                    if (file == 0) {
                                        throw th;
                                    }
                                    try {
                                        file.close();
                                        throw th;
                                    } catch (IOException e7) {
                                        e7.printStackTrace();
                                        throw th;
                                    }
                                }
                            } catch (FileNotFoundException e8) {
                                e = e8;
                                file = 0;
                            } catch (IOException e9) {
                                e = e9;
                                file = 0;
                            } catch (Throwable th2) {
                                th = th2;
                                file = 0;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (FileNotFoundException e10) {
                        e = e10;
                        file = 0;
                    } catch (IOException e11) {
                        e = e11;
                        file = 0;
                    } catch (Throwable th4) {
                        th = th4;
                        file = 0;
                    }
                } catch (IOException e12) {
                    e12.printStackTrace();
                }
            }
        }.start();
    }

    public int saveWayPointList(String str, List<MLatLng> list, String str2) {
        return saveWayPointList(str, list, null, str2);
    }

    public int saveWayPointList(String str, List<MLatLng> list, Bitmap bitmap, String str2) {
        String strDpEncode = new PolylineEncoder().dpEncode(list);
        List<WayPoint> listLntLngToWayPointList = MLatLng.LntLngToWayPointList(list);
        RouteModel routeModel = new RouteModel();
        routeModel.setName(str);
        routeModel.setEncodePolyline(strDpEncode);
        routeModel.setDate(System.currentTimeMillis());
        routeModel.setIdentifier(str2);
        routeModel.setWaypoint(listLntLngToWayPointList);
        String str3 = strDpEncode + ".png";
        routeModel.setPictureName(str3);
        routeModel.save();
        int id = routeModel.getId();
        if (bitmap != null) {
            saveBitMapToSdCard(str3, imagePath, bitmap);
        }
        saveWayPoints(id, listLntLngToWayPointList);
        return id;
    }

    public int saveWayPointListByImageView(String str, List<MLatLng> list, ImageView imageView, String str2) {
        Bitmap bitmapCreateBitmap;
        imageView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = imageView.getDrawingCache();
        if (drawingCache != null) {
            bitmapCreateBitmap = Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), drawingCache.getHeight());
            imageView.setDrawingCacheEnabled(false);
            drawingCache.recycle();
        } else {
            bitmapCreateBitmap = null;
        }
        return saveWayPointList(str, list, bitmapCreateBitmap, str2);
    }

    public List<MLatLng> wayPointListToLatLng(List<WayPoint> list) {
        ArrayList arrayList = new ArrayList();
        for (WayPoint wayPoint : list) {
            arrayList.add(new MLatLng((float) wayPoint.getLatitude(), (float) wayPoint.getLongitude()));
        }
        return arrayList;
    }

    public List<MLatLng> wayPointBeanToLatLng(List<LikeRouteListModel.MessagesBean.RouteBean.WaypointsBean> list) {
        ArrayList arrayList = new ArrayList();
        for (LikeRouteListModel.MessagesBean.RouteBean.WaypointsBean waypointsBean : list) {
            arrayList.add(new MLatLng(waypointsBean.getLatitude(), waypointsBean.getLongitude()));
        }
        return arrayList;
    }

    public List<WayPoint> LntLngToList(List<LatLng> list) {
        ArrayList arrayList = new ArrayList();
        for (LatLng latLng : list) {
            WayPoint wayPoint = new WayPoint();
            wayPoint.setLatitude(latLng.latitude);
            wayPoint.setLongitude(latLng.longitude);
            arrayList.add(wayPoint);
        }
        return arrayList;
    }

    public void saveWayPoints(int i, List<WayPoint> list) {
        for (WayPoint wayPoint : list) {
            wayPoint.setId(i);
            wayPoint.save();
        }
    }

    public List<RouteModel> getAllWayPointList() {
        return DataSupport.order("date desc").find(RouteModel.class, true);
    }

    public RouteModel getWayPointListById(int i) {
        return (RouteModel) DataSupport.find(RouteModel.class, i, true);
    }

    public void saveBitMapToSdCard(String str, String str2, Bitmap bitmap) {
        File file = new File(str2, str);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteBitmap(String str) {
        File file = new File(imagePath, str);
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean deleteWayPointListById(int i) {
        return deleteWayPointList((RouteModel) DataSupport.find(RouteModel.class, i));
    }

    public boolean deleteWayPointList(RouteModel routeModel) {
        if (routeModel.delete() <= 0) {
            return false;
        }
        deleteBitmap(routeModel.getPictureName());
        return true;
    }

    public boolean isRepeatRoute(List<MLatLng> list) {
        return DataSupport.where("encodePolyline=?", new PolylineEncoder().dpEncode(list)).find(RouteModel.class).size() > 0;
    }

    public int coverSaveWayPointList(int i, List<MLatLng> list, Bitmap bitmap) {
        RouteModel wayPointListById = getWayPointListById(i);
        if (wayPointListById == null) {
            return 0;
        }
        String name = wayPointListById.getName();
        deleteWayPointListById(i);
        deleteBitmap(wayPointListById.getPictureName());
        return saveWayPointList(name, list, bitmap);
    }

    public void saveBoatToDataBase(Boat boat) {
        Log.d("saveToDatabase", Thread.currentThread().getName());
        boat.saveOrUpdateAsync("ssid = ?", boat.getSsid()).listen(new SaveCallback() { // from class: im.helmsman.helmsmanandroid.utils.DaoUtil.2
            @Override // org.litepal.crud.callback.SaveCallback
            public void onFinish(boolean z) {
            }
        });
    }

    public List<Boat> queryAllBoatList() {
        return DataSupport.findAll(Boat.class, new long[0]);
    }

    public Boat queryBoatBySSID(String str) {
        List listFind = DataSupport.where("ssid = ?", str).find(Boat.class);
        if (listFind.size() > 0) {
            return (Boat) listFind.get(0);
        }
        return null;
    }

    public void deleteAllLogFile() {
        File[] fileArrListFiles = new File(Environment.getExternalStorageDirectory().getPath() + "/helmsman").listFiles();
        if (fileArrListFiles == null) {
            return;
        }
        for (File file : fileArrListFiles) {
            file.delete();
        }
    }

    /* JADX WARN: Type inference failed for: r6v0, types: [im.helmsman.helmsmanandroid.utils.DaoUtil$3] */
    public void saveRouteByWaypointsBean(final String str, final String str2, final ImageView imageView, final List<LikeRouteListModel.MessagesBean.RouteBean.WaypointsBean> list) {
        new Thread() { // from class: im.helmsman.helmsmanandroid.utils.DaoUtil.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                ArrayList arrayList = new ArrayList();
                for (LikeRouteListModel.MessagesBean.RouteBean.WaypointsBean waypointsBean : list) {
                    arrayList.add(new MLatLng(waypointsBean.getLatitude(), waypointsBean.getLongitude()));
                }
                imageView.setDrawingCacheEnabled(true);
                Bitmap drawingCache = imageView.getDrawingCache();
                DaoUtil.this.saveWayPointList(str2, arrayList, drawingCache != null ? Bitmap.createBitmap(drawingCache, 0, 0, drawingCache.getWidth(), drawingCache.getHeight()) : null, str);
                imageView.setDrawingCacheEnabled(false);
            }
        }.start();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [im.helmsman.helmsmanandroid.utils.DaoUtil$4] */
    public void saveRouteByWaypointsBean(final String str, final List<GetFriendsMessageResultModel.MessagesBean.RouteBean.WaypointsBean> list) {
        new Thread() { // from class: im.helmsman.helmsmanandroid.utils.DaoUtil.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                ArrayList arrayList = new ArrayList();
                for (GetFriendsMessageResultModel.MessagesBean.RouteBean.WaypointsBean waypointsBean : list) {
                    arrayList.add(new MLatLng(waypointsBean.getLatitude(), waypointsBean.getLongitude()));
                }
                DaoUtil.this.saveWayPointList(str, arrayList, null, null);
            }
        }.start();
    }
}
