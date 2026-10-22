package im.helmsman.lib;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DataChangeSubscribe {
    private static List<DataManager.DataChangeObserver> subList = new ArrayList();

    public static void subscribe(DataManager.DataChangeObserver dataChangeObserver) {
        synchronized (subList) {
            if (subList.contains(dataChangeObserver)) {
                return;
            }
            subList.add(dataChangeObserver);
            Log.e("subscribe", subList.size() + "");
        }
    }

    public static void unSubscrible(DataManager.DataChangeObserver dataChangeObserver) {
        synchronized (subList) {
            if (subList.contains(dataChangeObserver)) {
                Iterator<DataManager.DataChangeObserver> it = subList.iterator();
                while (it.hasNext()) {
                    if (it.next().equals(dataChangeObserver)) {
                        it.remove();
                    }
                }
                Log.e("unsubscribe", subList.size() + "");
            }
        }
    }

    protected static List<DataManager.DataChangeObserver> getSubList() {
        return subList;
    }
}
