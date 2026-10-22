package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: OfflineMapDownloadList.java */
/* JADX INFO: loaded from: classes.dex */
public class aq {
    public ArrayList<OfflineMapProvince> a = new ArrayList<>();
    private bb b;
    private Context c;
    private Handler d;

    private boolean a(int i, int i2) {
        return i2 != 1 || i <= 2 || i >= 98;
    }

    private boolean b(int i) {
        return i == 4;
    }

    public boolean a(int i) {
        return i == 0 || i == 2 || i == 3 || i == 1 || i == 102 || i == 101 || i == 103 || i == -1;
    }

    public aq(Context context, Handler handler) {
        this.c = context;
        this.d = handler;
        this.b = bb.a(context);
    }

    private void a(aw awVar) {
        bb bbVar = this.b;
        if (bbVar == null || awVar == null) {
            return;
        }
        bbVar.a(awVar);
    }

    private void b(aw awVar) {
        bb bbVar = this.b;
        if (bbVar != null) {
            bbVar.b(awVar);
        }
    }

    private boolean a(OfflineMapProvince offlineMapProvince) {
        if (offlineMapProvince == null) {
            return false;
        }
        Iterator<OfflineMapCity> it = offlineMapProvince.getCityList().iterator();
        while (it.hasNext()) {
            if (it.next().getState() != 4) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<OfflineMapProvince> a() {
        ArrayList<OfflineMapProvince> arrayList = new ArrayList<>();
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
        }
        return arrayList;
    }

    public OfflineMapCity a(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                for (OfflineMapCity offlineMapCity : it.next().getCityList()) {
                    if (offlineMapCity.getCode().equals(str)) {
                        return offlineMapCity;
                    }
                }
            }
            return null;
        }
    }

    public OfflineMapCity b(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                for (OfflineMapCity offlineMapCity : it.next().getCityList()) {
                    if (offlineMapCity.getCity().trim().equalsIgnoreCase(str.trim())) {
                        return offlineMapCity;
                    }
                }
            }
            return null;
        }
    }

    public OfflineMapProvince c(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        synchronized (this.a) {
            for (OfflineMapProvince offlineMapProvince : this.a) {
                if (offlineMapProvince.getProvinceName().trim().equalsIgnoreCase(str.trim())) {
                    return offlineMapProvince;
                }
            }
            return null;
        }
    }

    public ArrayList<OfflineMapCity> b() {
        ArrayList<OfflineMapCity> arrayList = new ArrayList<>();
        synchronized (this.a) {
            Iterator<OfflineMapProvince> it = this.a.iterator();
            while (it.hasNext()) {
                Iterator<OfflineMapCity> it2 = it.next().getCityList().iterator();
                while (it2.hasNext()) {
                    arrayList.add(it2.next());
                }
            }
        }
        return arrayList;
    }

    public void a(List<OfflineMapProvince> list) {
        OfflineMapProvince next;
        OfflineMapCity next2;
        synchronized (this.a) {
            if (this.a.size() > 0) {
                for (int i = 0; i < this.a.size(); i++) {
                    OfflineMapProvince offlineMapProvince = this.a.get(i);
                    Iterator<OfflineMapProvince> it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            next = null;
                            break;
                        }
                        next = it.next();
                        if (offlineMapProvince.getPinyin().equals(next.getPinyin())) {
                            break;
                        }
                        if (offlineMapProvince.getPinyin().equals("quanguogaiyaotu") || offlineMapProvince.getProvinceCode().equals("000001") || offlineMapProvince.getProvinceCode().equals("100000")) {
                            if (next.getPinyin().equals("quanguogaiyaotu")) {
                                break;
                            }
                        }
                    }
                    if (next != null) {
                        a(offlineMapProvince, next);
                        ArrayList<OfflineMapCity> cityList = offlineMapProvince.getCityList();
                        ArrayList<OfflineMapCity> cityList2 = next.getCityList();
                        for (int i2 = 0; i2 < cityList.size(); i2++) {
                            OfflineMapCity offlineMapCity = cityList.get(i2);
                            Iterator<OfflineMapCity> it2 = cityList2.iterator();
                            do {
                                if (!it2.hasNext()) {
                                    next2 = null;
                                    break;
                                }
                                next2 = it2.next();
                            } while (!offlineMapCity.getPinyin().equals(next2.getPinyin()));
                            if (next2 != null) {
                                a(offlineMapCity, next2);
                            }
                        }
                    }
                }
            } else {
                Iterator<OfflineMapProvince> it3 = list.iterator();
                while (it3.hasNext()) {
                    this.a.add(it3.next());
                }
            }
        }
    }

    private void a(OfflineMapCity offlineMapCity, OfflineMapCity offlineMapCity2) {
        offlineMapCity.setUrl(offlineMapCity2.getUrl());
        offlineMapCity.setVersion(offlineMapCity2.getVersion());
        offlineMapCity.setSize(offlineMapCity2.getSize());
        offlineMapCity.setCode(offlineMapCity2.getCode());
        offlineMapCity.setPinyin(offlineMapCity2.getPinyin());
        offlineMapCity.setJianpin(offlineMapCity2.getJianpin());
    }

    private void a(OfflineMapProvince offlineMapProvince, OfflineMapProvince offlineMapProvince2) {
        offlineMapProvince.setUrl(offlineMapProvince2.getUrl());
        offlineMapProvince.setVersion(offlineMapProvince2.getVersion());
        offlineMapProvince.setSize(offlineMapProvince2.getSize());
        offlineMapProvince.setPinyin(offlineMapProvince2.getPinyin());
        offlineMapProvince.setJianpin(offlineMapProvince2.getJianpin());
    }

    public ArrayList<OfflineMapCity> c() {
        ArrayList<OfflineMapCity> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            for (OfflineMapProvince offlineMapProvince : this.a) {
                if (offlineMapProvince != null) {
                    for (OfflineMapCity offlineMapCity : offlineMapProvince.getCityList()) {
                        if (offlineMapCity.getState() == 4 || offlineMapCity.getState() == 7) {
                            arrayList.add(offlineMapCity);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public ArrayList<OfflineMapProvince> d() {
        ArrayList<OfflineMapProvince> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            for (OfflineMapProvince offlineMapProvince : this.a) {
                if (offlineMapProvince != null && (offlineMapProvince.getState() == 4 || offlineMapProvince.getState() == 7)) {
                    arrayList.add(offlineMapProvince);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<OfflineMapCity> e() {
        ArrayList<OfflineMapCity> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            for (OfflineMapProvince offlineMapProvince : this.a) {
                if (offlineMapProvince != null) {
                    for (OfflineMapCity offlineMapCity : offlineMapProvince.getCityList()) {
                        if (a(offlineMapCity.getState())) {
                            arrayList.add(offlineMapCity);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public ArrayList<OfflineMapProvince> f() {
        ArrayList<OfflineMapProvince> arrayList;
        synchronized (this.a) {
            arrayList = new ArrayList<>();
            for (OfflineMapProvince offlineMapProvince : this.a) {
                if (offlineMapProvince != null && a(offlineMapProvince.getState())) {
                    arrayList.add(offlineMapProvince);
                }
            }
        }
        return arrayList;
    }

    public void a(al alVar) {
        String pinyin = alVar.getPinyin();
        synchronized (this.a) {
            loop0: for (OfflineMapProvince offlineMapProvince : this.a) {
                if (offlineMapProvince != null) {
                    for (OfflineMapCity offlineMapCity : offlineMapProvince.getCityList()) {
                        if (offlineMapCity.getPinyin().trim().equals(pinyin.trim())) {
                            a(alVar, offlineMapCity);
                            a(alVar, offlineMapProvince);
                            break loop0;
                        }
                    }
                }
            }
        }
    }

    private void a(al alVar, OfflineMapCity offlineMapCity) {
        int iB = alVar.c().b();
        if (alVar.c().equals(alVar.a)) {
            b(alVar.x());
        } else {
            if (alVar.c().equals(alVar.f)) {
                bj.a("saveJSONObjectToFile  CITY " + alVar.getCity());
                b(alVar);
                alVar.x().c();
            }
            if (a(alVar.getcompleteCode(), alVar.c().b())) {
                a(alVar.x());
            }
        }
        offlineMapCity.setState(iB);
        offlineMapCity.setCompleteCode(alVar.getcompleteCode());
    }

    private void b(al alVar) {
        File[] fileArrListFiles = new File(dw.b(this.c)).listFiles();
        if (fileArrListFiles == null) {
            return;
        }
        for (File file : fileArrListFiles) {
            if (file.isFile() && file.exists() && file.getName().contains(alVar.getAdcode()) && file.getName().endsWith(".zip.tmp.dt")) {
                file.delete();
            }
        }
    }

    private void a(al alVar, OfflineMapProvince offlineMapProvince) {
        aw awVar;
        int iB = alVar.c().b();
        if (iB == 6) {
            offlineMapProvince.setState(iB);
            offlineMapProvince.setCompleteCode(0);
            b(new aw(offlineMapProvince, this.c));
            try {
                bj.b(offlineMapProvince.getProvinceCode(), this.c);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        if (b(iB) && a(offlineMapProvince)) {
            if (alVar.getPinyin().equals(offlineMapProvince.getPinyin())) {
                offlineMapProvince.setState(iB);
                offlineMapProvince.setCompleteCode(alVar.getcompleteCode());
                offlineMapProvince.setVersion(alVar.getVersion());
                offlineMapProvince.setUrl(alVar.getUrl());
                awVar = new aw(offlineMapProvince, this.c);
                awVar.a(alVar.a());
                awVar.d(alVar.getCode());
            } else {
                offlineMapProvince.setState(iB);
                offlineMapProvince.setCompleteCode(100);
                awVar = new aw(offlineMapProvince, this.c);
            }
            awVar.c();
            a(awVar);
            bj.a("saveJSONObjectToFile  province " + awVar.d());
        }
    }

    public void g() {
        h();
        this.d = null;
        this.b = null;
        this.c = null;
    }

    public void h() {
        ArrayList<OfflineMapProvince> arrayList = this.a;
        if (arrayList != null) {
            synchronized (arrayList) {
                this.a.clear();
            }
        }
    }
}
