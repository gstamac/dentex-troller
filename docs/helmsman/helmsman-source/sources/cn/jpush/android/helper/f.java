package cn.jpush.android.helper;

import android.util.SparseArray;
import androidx.core.view.PointerIconCompat;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;

/* JADX INFO: loaded from: classes.dex */
public class f {
    private static final SparseArray<String> a;

    static {
        SparseArray<String> sparseArray = new SparseArray<>();
        a = sparseArray;
        sparseArray.put(1060, "need not show notification by foreground");
        sparseArray.put(1061, "need not show notification by isNeedShowNotification return false");
        sparseArray.put(1050, "ssp buildId error");
        sparseArray.put(1051, "ssp but no alert");
        sparseArray.put(1032, "MSG already cancle");
        sparseArray.put(1033, "overide msg already cancle");
        sparseArray.put(1034, "MSG already end");
        sparseArray.put(1035, "msg delay show");
        sparseArray.put(1036, "notification disabled");
        sparseArray.put(972, "Deep link set small icon failed");
        sparseArray.put(973, "Deep link set large icon failed");
        sparseArray.put(974, "Deep link- device in blacklist");
        sparseArray.put(975, "Deep link source error");
        sparseArray.put(976, "Deep link jump success");
        sparseArray.put(977, "Deep link jump failed");
        sparseArray.put(978, "Fail Deep link jump success");
        sparseArray.put(979, "Fail Deep link jump failed");
        sparseArray.put(981, "Deep link notify not show");
        sparseArray.put(980, "Deep link notify show");
        sparseArray.put(983, "wrong with resource file push_notification.xml");
        sparseArray.put(993, "target app uninstall,not found target app small icon");
        sparseArray.put(985, "Deep link source is not empty and pkgname is empty but deeplink error");
        sparseArray.put(986, "Message is not in push time");
        sparseArray.put(988, "Deep link target app uninstalled");
        sparseArray.put(995, "Message JSON parsing succeed");
        sparseArray.put(996, "Message JSON parsing failed");
        sparseArray.put(997, "Message already received, give up");
        sparseArray.put(998, "Message already received, still process");
        sparseArray.put(1000, "User clicked and opened the Message");
        sparseArray.put(GLMapStaticValue.AM_PARAMETERNAME_SCENIC_WIDGET_FILTER_INDEX, "User clicked and opened the Message from JPushInterface.reportNotificationOpened");
        sparseArray.put(1001, "Message download succeed");
        sparseArray.put(1002, "Message received succeed");
        sparseArray.put(1003, "Message silence download succeed");
        sparseArray.put(1004, "Video silence downlaod succeed");
        sparseArray.put(1005, "User clicked video and jumped to url Message (browser)");
        sparseArray.put(PointerIconCompat.TYPE_TEXT, "Video is force closed by user");
        sparseArray.put(1007, "User clicked 'OK'");
        sparseArray.put(1006, "User clicked 'Cancel'");
        sparseArray.put(1011, "Download failed");
        sparseArray.put(PointerIconCompat.TYPE_NO_DROP, "User clicked to download again");
        sparseArray.put(PointerIconCompat.TYPE_ALL_SCROLL, "The file already exist and same size. Don't download again.");
        sparseArray.put(1100, "Invalid param or unexpected result.");
        sparseArray.put(PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, "Failed to preload required resource");
        sparseArray.put(PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, "User clicked install alert on status bar after downloading finished.");
        sparseArray.put(PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, "User clicked the webview's url");
        sparseArray.put(PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, "User clicked call action");
        sparseArray.put(PointerIconCompat.TYPE_ZOOM_IN, "The Message show in the status bar");
        sparseArray.put(PointerIconCompat.TYPE_ZOOM_OUT, "Click applist and show the Message");
        sparseArray.put(PointerIconCompat.TYPE_GRAB, "Down image failed");
        sparseArray.put(1021, "Down html failed");
        sparseArray.put(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_3DOBJECT, "Down Message failed");
        sparseArray.put(1030, "Discard the message because it is not in the push time");
        sparseArray.put(1031, "Stop push service");
        sparseArray.put(1032, "Resume push service");
        sparseArray.put(1052, "Message count limit");
        sparseArray.put(1053, "Message interval limit");
    }

    public static String a(int i) {
        SparseArray<String> sparseArray = a;
        if (sparseArray.get(i) != null) {
            return sparseArray.get(i);
        }
        Logger.d("StatusCode", "Unknown report code - " + i);
        return "";
    }
}
