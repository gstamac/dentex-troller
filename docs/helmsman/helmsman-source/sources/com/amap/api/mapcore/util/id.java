package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.HandlerThread;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import cn.jiguang.internal.JConstants;
import com.facebook.places.model.PlaceFields;
import com.zhy.http.okhttp.OkHttpUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import okhttp3.internal.ws.WebSocketProtocol;

/* JADX INFO: compiled from: CgiManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class id {
    TelephonyManager c;
    CellLocation e;
    private Context l;
    private ib p;
    private Object q;
    int a = 0;
    ArrayList<ic> b = new ArrayList<>();
    private String m = null;
    private ArrayList<ic> n = new ArrayList<>();
    private int o = -113;
    long d = 0;
    private int r = 0;
    private long s = 0;
    boolean f = false;
    PhoneStateListener g = null;
    String h = null;
    boolean i = false;
    StringBuilder j = null;
    HandlerThread k = null;
    private boolean t = false;
    private Object u = new Object();

    /* JADX INFO: compiled from: CgiManager.java */
    class a extends HandlerThread {
        public a(String str) {
            super(str);
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            try {
                super.onLooperPrepared();
                synchronized (id.this.u) {
                    if (!id.this.t) {
                        id.this.k();
                    }
                }
            } catch (Throwable unused) {
            }
        }

        @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                try {
                    super.run();
                } catch (Throwable unused) {
                    id.this.c.listen(id.this.g, 0);
                    id.this.g = null;
                    quit();
                }
            } catch (Throwable unused2) {
            }
        }
    }

    public id(Context context) {
        this.c = null;
        this.p = null;
        this.l = context;
        if (this.c == null) {
            this.c = (TelephonyManager) im.a(context, PlaceFields.PHONE);
        }
        j();
        this.p = new ib();
    }

    private CellLocation a(Object obj, String str, Object... objArr) {
        if (obj == null) {
            return null;
        }
        try {
            Object objA = ik.a(obj, str, objArr);
            CellLocation cellLocation = objA != null ? (CellLocation) objA : null;
            if (b(cellLocation)) {
                return cellLocation;
            }
            return null;
        } catch (Throwable unused) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:38:0x0079  */
    /* JADX WARN: Code duplicated, block: B:42:0x0081 A[Catch: all -> 0x00a6, TRY_LEAVE, TryCatch #1 {all -> 0x00a6, blocks: (B:40:0x007c, B:42:0x0081, B:45:0x0095), top: B:60:0x007c }] */
    /* JADX WARN: Code duplicated, block: B:45:0x0095 A[Catch: all -> 0x00a6, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x00a6, blocks: (B:40:0x007c, B:42:0x0081, B:45:0x0095), top: B:60:0x007c }] */
    /* JADX WARN: Code duplicated, block: B:60:0x007c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:66:0x00ab A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [android.telephony.CellLocation] */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r11v1, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r11v11 */
    /* JADX WARN: Type inference failed for: r11v12 */
    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v14 */
    /* JADX WARN: Type inference failed for: r11v15 */
    /* JADX WARN: Type inference failed for: r11v16 */
    /* JADX WARN: Type inference failed for: r11v2 */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r11v4, types: [com.amap.api.mapcore.util.ic] */
    /* JADX WARN: Type inference failed for: r11v6 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12, types: [android.telephony.cdma.CdmaCellLocation] */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r9v0 */
    private CellLocation a(List<CellInfo> list) {
        GsmCellLocation gsmCellLocation;
        ?? cdmaCellLocation;
        ic icVarA;
        ic icVarA2;
        ic icVarA3;
        ic icVarA4;
        ?? r0 = 0;
        gsmCellLocation = null;
        gsmCellLocation = null;
        r0 = 0;
        GsmCellLocation gsmCellLocation2 = null;
        r0 = 0;
        r0 = 0;
        if (list != null && !list.isEmpty()) {
            int i = 0;
            ?? r11 = list;
            while (true) {
                if (i >= r11.size()) {
                    r11 = 0;
                    if (r11 == 0) {
                        r11 = icVarA;
                        r11 = icVarA2;
                        r11 = icVarA3;
                        r11 = icVarA4;
                        gsmCellLocation = null;
                        break;
                    }
                    try {
                        r11 = icVarA;
                        r11 = icVarA2;
                        r11 = icVarA3;
                        r11 = icVarA4;
                        if (r11.k != 2) {
                            gsmCellLocation = new GsmCellLocation();
                            try {
                                gsmCellLocation.setLacAndCid(r11.c, r11.d);
                                break;
                            } catch (Throwable unused) {
                                cdmaCellLocation = 0;
                                gsmCellLocation2 = gsmCellLocation;
                                ?? r9 = cdmaCellLocation;
                                gsmCellLocation = gsmCellLocation2;
                                r0 = r9;
                                break;
                            }
                        }
                        cdmaCellLocation = new CdmaCellLocation();
                        try {
                            cdmaCellLocation.setCellLocationData(r11.i, r11.e, r11.f, r11.g, r11.h);
                            cdmaCellLocation = cdmaCellLocation;
                        } catch (Throwable unused2) {
                        }
                    } catch (Throwable unused3) {
                        cdmaCellLocation = gsmCellLocation2;
                    }
                    ?? r10 = cdmaCellLocation;
                    gsmCellLocation = gsmCellLocation2;
                    r0 = r10;
                    break;
                }
                CellInfo cellInfo = (CellInfo) r11.get(i);
                if (cellInfo != null) {
                    try {
                        boolean zIsRegistered = cellInfo.isRegistered();
                        if (!(cellInfo instanceof CellInfoCdma)) {
                            if (!(cellInfo instanceof CellInfoGsm)) {
                                if (!(cellInfo instanceof CellInfoWcdma)) {
                                    if (cellInfo instanceof CellInfoLte) {
                                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                                        if (a(cellInfoLte.getCellIdentity())) {
                                            icVarA = a(cellInfoLte, zIsRegistered);
                                        }
                                    } else {
                                        r11 = 0;
                                    }
                                    if (r11 == 0) {
                                        r11 = icVarA;
                                        r11 = icVarA2;
                                        r11 = icVarA3;
                                        r11 = icVarA4;
                                        gsmCellLocation = null;
                                        break;
                                    }
                                    r11 = icVarA;
                                    r11 = icVarA2;
                                    r11 = icVarA3;
                                    r11 = icVarA4;
                                    if (r11.k != 2) {
                                        gsmCellLocation = new GsmCellLocation();
                                        gsmCellLocation.setLacAndCid(r11.c, r11.d);
                                        break;
                                    }
                                    cdmaCellLocation = new CdmaCellLocation();
                                    cdmaCellLocation.setCellLocationData(r11.i, r11.e, r11.f, r11.g, r11.h);
                                    cdmaCellLocation = cdmaCellLocation;
                                    ?? r12 = cdmaCellLocation;
                                    gsmCellLocation = gsmCellLocation2;
                                    r0 = r12;
                                    break;
                                }
                                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                                if (a(cellInfoWcdma.getCellIdentity())) {
                                    icVarA2 = a(cellInfoWcdma, zIsRegistered);
                                    if (r11 == 0) {
                                        r11 = icVarA;
                                        r11 = icVarA2;
                                        r11 = icVarA3;
                                        r11 = icVarA4;
                                        gsmCellLocation = null;
                                        break;
                                    }
                                    r11 = icVarA;
                                    r11 = icVarA2;
                                    r11 = icVarA3;
                                    r11 = icVarA4;
                                    if (r11.k != 2) {
                                        gsmCellLocation = new GsmCellLocation();
                                        gsmCellLocation.setLacAndCid(r11.c, r11.d);
                                        break;
                                    }
                                    cdmaCellLocation = new CdmaCellLocation();
                                    cdmaCellLocation.setCellLocationData(r11.i, r11.e, r11.f, r11.g, r11.h);
                                    cdmaCellLocation = cdmaCellLocation;
                                    ?? r13 = cdmaCellLocation;
                                    gsmCellLocation = gsmCellLocation2;
                                    r0 = r13;
                                    break;
                                }
                            } else {
                                CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                                if (a(cellInfoGsm.getCellIdentity())) {
                                    icVarA3 = a(cellInfoGsm, zIsRegistered);
                                    if (r11 == 0) {
                                        r11 = icVarA;
                                        r11 = icVarA2;
                                        r11 = icVarA3;
                                        r11 = icVarA4;
                                        gsmCellLocation = null;
                                        break;
                                    }
                                    r11 = icVarA;
                                    r11 = icVarA2;
                                    r11 = icVarA3;
                                    r11 = icVarA4;
                                    if (r11.k != 2) {
                                        gsmCellLocation = new GsmCellLocation();
                                        gsmCellLocation.setLacAndCid(r11.c, r11.d);
                                        break;
                                    }
                                    cdmaCellLocation = new CdmaCellLocation();
                                    cdmaCellLocation.setCellLocationData(r11.i, r11.e, r11.f, r11.g, r11.h);
                                    cdmaCellLocation = cdmaCellLocation;
                                    ?? r14 = cdmaCellLocation;
                                    gsmCellLocation = gsmCellLocation2;
                                    r0 = r14;
                                    break;
                                }
                            }
                        } else {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                            if (a(cellInfoCdma.getCellIdentity())) {
                                icVarA4 = a(cellInfoCdma, zIsRegistered);
                                if (r11 == 0) {
                                    r11 = icVarA;
                                    r11 = icVarA2;
                                    r11 = icVarA3;
                                    r11 = icVarA4;
                                    gsmCellLocation = null;
                                    break;
                                }
                                r11 = icVarA;
                                r11 = icVarA2;
                                r11 = icVarA3;
                                r11 = icVarA4;
                                if (r11.k != 2) {
                                    gsmCellLocation = new GsmCellLocation();
                                    gsmCellLocation.setLacAndCid(r11.c, r11.d);
                                    break;
                                }
                                cdmaCellLocation = new CdmaCellLocation();
                                cdmaCellLocation.setCellLocationData(r11.i, r11.e, r11.f, r11.g, r11.h);
                                cdmaCellLocation = cdmaCellLocation;
                                ?? r15 = cdmaCellLocation;
                                gsmCellLocation = gsmCellLocation2;
                                r0 = r15;
                                break;
                            }
                        }
                    } catch (Throwable unused4) {
                        continue;
                    }
                } else {
                    continue;
                }
                i++;
                r11 = r11;
            }
            if (r0 == 0) {
                return gsmCellLocation;
            }
        }
        return r0;
    }

    private static ic a(int i, boolean z, int i2, int i3, int i4, int i5, int i6) {
        ic icVar = new ic(i, z);
        icVar.a = i2;
        icVar.b = i3;
        icVar.c = i4;
        icVar.d = i5;
        icVar.j = i6;
        return icVar;
    }

    private ic a(CellInfoCdma cellInfoCdma, boolean z) {
        int i;
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        String[] strArrA = im.a(this.c);
        int i2 = 0;
        try {
            i = Integer.parseInt(strArrA[0]);
            try {
                i2 = Integer.parseInt(strArrA[1]);
            } catch (Throwable unused) {
            }
        } catch (Throwable unused2) {
            i = 0;
        }
        ic icVarA = a(2, z, i, i2, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
        icVarA.g = cellIdentity.getSystemId();
        icVarA.h = cellIdentity.getNetworkId();
        icVarA.i = cellIdentity.getBasestationId();
        icVarA.e = cellIdentity.getLatitude();
        icVarA.f = cellIdentity.getLongitude();
        return icVarA;
    }

    private static ic a(CellInfoGsm cellInfoGsm, boolean z) {
        CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
        return a(1, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoGsm.getCellSignalStrength().getDbm());
    }

    private static ic a(CellInfoLte cellInfoLte, boolean z) {
        CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        return a(3, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getTac(), cellIdentity.getCi(), cellInfoLte.getCellSignalStrength().getDbm());
    }

    private static ic a(CellInfoWcdma cellInfoWcdma, boolean z) {
        CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
        return a(4, z, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoWcdma.getCellSignalStrength().getDbm());
    }

    private static ic a(NeighboringCellInfo neighboringCellInfo, String[] strArr) {
        try {
            ic icVar = new ic(1, false);
            icVar.a = Integer.parseInt(strArr[0]);
            icVar.b = Integer.parseInt(strArr[1]);
            icVar.c = ik.b(neighboringCellInfo, "getLac", new Object[0]);
            icVar.d = neighboringCellInfo.getCid();
            icVar.j = im.a(neighboringCellInfo.getRssi());
            return icVar;
        } catch (Throwable th) {
            ij.a(th, "CgiManager", "getGsm");
            return null;
        }
    }

    private void a(CellLocation cellLocation, String[] strArr) {
        ic icVarA;
        if (cellLocation == null || this.c == null) {
            return;
        }
        this.b.clear();
        if (b(cellLocation)) {
            this.a = 1;
            this.b.add(c(cellLocation, strArr));
            List<NeighboringCellInfo> neighboringCellInfo = this.c.getNeighboringCellInfo();
            if (neighboringCellInfo == null || neighboringCellInfo.isEmpty()) {
                return;
            }
            for (NeighboringCellInfo neighboringCellInfo2 : neighboringCellInfo) {
                if (neighboringCellInfo2 != null && a(neighboringCellInfo2.getLac(), neighboringCellInfo2.getCid()) && (icVarA = a(neighboringCellInfo2, strArr)) != null && !this.b.contains(icVarA)) {
                    this.b.add(icVarA);
                }
            }
        }
    }

    public static boolean a(int i) {
        return i > 0 && i <= 15;
    }

    private static boolean a(int i, int i2) {
        return (i == -1 || i == 0 || i > 65535 || i2 == -1 || i2 == 0 || i2 == 65535 || i2 >= 268435455) ? false : true;
    }

    private static boolean a(CellIdentityCdma cellIdentityCdma) {
        return cellIdentityCdma != null && cellIdentityCdma.getSystemId() > 0 && cellIdentityCdma.getNetworkId() >= 0 && cellIdentityCdma.getBasestationId() >= 0;
    }

    private static boolean a(CellIdentityGsm cellIdentityGsm) {
        return cellIdentityGsm != null && c(cellIdentityGsm.getLac()) && d(cellIdentityGsm.getCid());
    }

    private static boolean a(CellIdentityLte cellIdentityLte) {
        return cellIdentityLte != null && c(cellIdentityLte.getTac()) && d(cellIdentityLte.getCi());
    }

    private static boolean a(CellIdentityWcdma cellIdentityWcdma) {
        return cellIdentityWcdma != null && c(cellIdentityWcdma.getLac()) && d(cellIdentityWcdma.getCid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i) {
        ArrayList<ic> arrayList;
        if (i == -113) {
            this.o = -113;
            return;
        }
        this.o = i;
        int i2 = this.a;
        if ((i2 != 1 && i2 != 2) || (arrayList = this.b) == null || arrayList.isEmpty()) {
            return;
        }
        try {
            this.b.get(0).j = this.o;
        } catch (Throwable unused) {
        }
    }

    private void b(CellLocation cellLocation, String[] strArr) {
        boolean z;
        if (cellLocation == null) {
            return;
        }
        this.b.clear();
        if (im.c() < 5) {
            return;
        }
        try {
            if (this.q != null) {
                try {
                    Field declaredField = cellLocation.getClass().getDeclaredField("mGsmCellLoc");
                    if (!declaredField.isAccessible()) {
                        declaredField.setAccessible(true);
                    }
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) declaredField.get(cellLocation);
                    if (gsmCellLocation == null || !b(gsmCellLocation)) {
                        z = false;
                    } else {
                        a(gsmCellLocation, strArr);
                        z = true;
                    }
                } catch (Throwable unused) {
                }
                if (z) {
                    return;
                }
            }
            if (b(cellLocation)) {
                this.a = 2;
                ic icVar = new ic(2, true);
                icVar.a = Integer.parseInt(strArr[0]);
                icVar.b = Integer.parseInt(strArr[1]);
                icVar.g = ik.b(cellLocation, "getSystemId", new Object[0]);
                icVar.h = ik.b(cellLocation, "getNetworkId", new Object[0]);
                icVar.i = ik.b(cellLocation, "getBaseStationId", new Object[0]);
                icVar.j = this.o;
                icVar.e = ik.b(cellLocation, "getBaseStationLatitude", new Object[0]);
                icVar.f = ik.b(cellLocation, "getBaseStationLongitude", new Object[0]);
                if (icVar.e < 0 || icVar.f < 0 || icVar.e == Integer.MAX_VALUE || icVar.f == Integer.MAX_VALUE || (icVar.e == icVar.f && icVar.e > 0)) {
                    icVar.e = 0;
                    icVar.f = 0;
                }
                if (this.b.contains(icVar)) {
                    return;
                }
                this.b.add(icVar);
            }
        } catch (Throwable th) {
            ij.a(th, "CgiManager", "hdlCdmaLocChange");
        }
    }

    private boolean b(CellLocation cellLocation) {
        boolean zA = a(cellLocation);
        if (!zA) {
            this.a = 0;
        }
        return zA;
    }

    private int c(CellLocation cellLocation) {
        if (this.i || cellLocation == null) {
            return 0;
        }
        if (cellLocation instanceof GsmCellLocation) {
            return 1;
        }
        try {
            Class.forName("android.telephony.cdma.CdmaCellLocation");
            return 2;
        } catch (Throwable th) {
            ij.a(th, "Utils", "getCellLocT");
            return 0;
        }
    }

    private ic c(CellLocation cellLocation, String[] strArr) {
        GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
        ic icVar = new ic(1, true);
        icVar.a = Integer.parseInt(strArr[0]);
        icVar.b = Integer.parseInt(strArr[1]);
        icVar.c = gsmCellLocation.getLac();
        icVar.d = gsmCellLocation.getCid();
        icVar.j = this.o;
        return icVar;
    }

    private static boolean c(int i) {
        return (i == -1 || i == 0 || i > 65535) ? false : true;
    }

    private static boolean d(int i) {
        return (i == -1 || i == 0 || i == 65535 || i >= 268435455) ? false : true;
    }

    private void j() {
        Object objA;
        TelephonyManager telephonyManager = this.c;
        if (telephonyManager == null) {
            return;
        }
        try {
            this.a = c(telephonyManager.getCellLocation());
        } catch (SecurityException e) {
            this.h = e.getMessage();
        } catch (Throwable th) {
            this.h = null;
            ij.a(th, "CgiManager", "CgiManager");
            this.a = 0;
        }
        try {
            int iU = u();
            this.r = iU;
            if (iU != 1) {
                objA = im.a(iU != 2 ? this.l : this.l, "phone2");
            } else {
                objA = im.a(this.l, "phone_msim");
            }
            this.q = objA;
        } catch (Throwable unused) {
        }
        if (this.k == null) {
            a aVar = new a("listenerPhoneStateThread");
            this.k = aVar;
            aVar.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        int iB;
        this.g = new PhoneStateListener() { // from class: com.amap.api.mapcore.util.id.1
            @Override // android.telephony.PhoneStateListener
            public final void onCellLocationChanged(CellLocation cellLocation) {
                try {
                    if (id.this.a(cellLocation)) {
                        id.this.e = cellLocation;
                        id.this.f = true;
                        id.this.s = im.b();
                    }
                } catch (Throwable unused) {
                }
            }

            @Override // android.telephony.PhoneStateListener
            public final void onServiceStateChanged(ServiceState serviceState) {
                try {
                    int state = serviceState.getState();
                    if (state == 0) {
                        id.this.f();
                    } else {
                        if (state != 1) {
                            return;
                        }
                        id.this.h();
                    }
                } catch (Throwable unused) {
                }
            }

            @Override // android.telephony.PhoneStateListener
            public final void onSignalStrengthChanged(int i) {
                try {
                    int i2 = id.this.a;
                    id.this.b((i2 == 1 || i2 == 2) ? im.a(i) : -113);
                } catch (Throwable unused) {
                }
            }

            @Override // android.telephony.PhoneStateListener
            public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
                int iA;
                if (signalStrength == null) {
                    return;
                }
                try {
                    int i = id.this.a;
                    if (i != 1) {
                        iA = i != 2 ? -113 : signalStrength.getCdmaDbm();
                    } else {
                        iA = im.a(signalStrength.getGsmSignalStrength());
                    }
                    id.this.b(iA);
                } catch (Throwable unused) {
                }
            }
        };
        try {
            iB = ik.b("android.telephony.PhoneStateListener", im.c() < 7 ? "LISTEN_SIGNAL_STRENGTH" : "LISTEN_SIGNAL_STRENGTHS");
        } catch (Throwable unused) {
            iB = 0;
        }
        if (iB == 0) {
            try {
                this.c.listen(this.g, 16);
            } catch (Throwable unused2) {
            }
        } else {
            try {
                this.c.listen(this.g, iB | 16);
            } catch (Throwable unused3) {
            }
        }
    }

    private CellLocation l() {
        TelephonyManager telephonyManager = this.c;
        if (telephonyManager != null) {
            try {
                CellLocation cellLocation = telephonyManager.getCellLocation();
                this.h = null;
                if (b(cellLocation)) {
                    this.e = cellLocation;
                    return cellLocation;
                }
            } catch (SecurityException e) {
                this.h = e.getMessage();
            } catch (Throwable th) {
                this.h = null;
                ij.a(th, "CgiManager", "getCellLocation");
            }
        }
        return null;
    }

    private boolean m() {
        return !this.i && im.b() - this.d >= OkHttpUtils.DEFAULT_MILLISECONDS;
    }

    private void n() {
        h();
    }

    private void o() {
        int iD = d();
        if (iD == 1) {
            if (this.b.isEmpty()) {
                this.a = 0;
            }
        } else if (iD == 2 && this.b.isEmpty()) {
            this.a = 0;
        }
    }

    private void p() {
        if (!this.i && this.c != null) {
            CellLocation cellLocationQ = q();
            if (!b(cellLocationQ)) {
                cellLocationQ = r();
            }
            if (b(cellLocationQ)) {
                this.e = cellLocationQ;
                this.s = im.b();
            } else if (im.b() - this.s > JConstants.MIN) {
                this.e = null;
                this.b.clear();
                this.n.clear();
            }
        }
        CellLocation cellLocation = this.e;
        this.f = true;
        if (b(cellLocation)) {
            String[] strArrA = im.a(this.c);
            int iC = c(this.e);
            if (iC == 1) {
                a(this.e, strArrA);
            } else if (iC == 2) {
                b(this.e, strArrA);
            }
        }
        try {
            if (im.c() >= 18) {
                t();
            }
        } catch (Throwable unused) {
        }
        TelephonyManager telephonyManager = this.c;
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            this.m = networkOperator;
            if (TextUtils.isEmpty(networkOperator)) {
                return;
            }
            this.a |= 8;
        }
    }

    private CellLocation q() {
        TelephonyManager telephonyManager = this.c;
        CellLocation cellLocationA = null;
        if (telephonyManager == null) {
            return null;
        }
        CellLocation cellLocationL = l();
        if (b(cellLocationL)) {
            return cellLocationL;
        }
        if (im.c() >= 18) {
            try {
                cellLocationA = a(telephonyManager.getAllCellInfo());
            } catch (SecurityException e) {
                this.h = e.getMessage();
            }
        }
        if (cellLocationA != null) {
            return cellLocationA;
        }
        CellLocation cellLocationA2 = a(telephonyManager, "getCellLocationExt", 1);
        return cellLocationA2 != null ? cellLocationA2 : a(telephonyManager, "getCellLocationGemini", 1);
    }

    private CellLocation r() {
        Object obj = this.q;
        CellLocation cellLocationA = null;
        if (obj == null) {
            return null;
        }
        try {
            Class<?> clsS = s();
            if (clsS.isInstance(obj)) {
                Object objCast = clsS.cast(obj);
                CellLocation cellLocationA2 = a(objCast, "getCellLocation", new Object[0]);
                if (cellLocationA2 != null) {
                    return cellLocationA2;
                }
                CellLocation cellLocationA3 = a(objCast, "getCellLocation", 1);
                if (cellLocationA3 != null) {
                    return cellLocationA3;
                }
                CellLocation cellLocationA4 = a(objCast, "getCellLocationGemini", 1);
                if (cellLocationA4 != null) {
                    return cellLocationA4;
                }
                cellLocationA = a(objCast, "getAllCellInfo", 1);
                if (cellLocationA != null) {
                    return cellLocationA;
                }
            }
        } catch (Throwable th) {
            ij.a(th, "CgiManager", "getSim2Cgi");
        }
        return cellLocationA;
    }

    private Class<?> s() {
        String str;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        int i = this.r;
        if (i == 0) {
            str = "android.telephony.TelephonyManager";
        } else if (i != 1) {
            str = i != 2 ? null : "android.telephony.TelephonyManager2";
        } else {
            str = "android.telephony.MSimTelephonyManager";
        }
        try {
            return systemClassLoader.loadClass(str);
        } catch (Throwable th) {
            ij.a(th, "CgiManager", "getSim2TmClass");
            return null;
        }
    }

    private void t() {
        SecurityException e;
        List<CellInfo> allCellInfo;
        int size;
        ic icVarA;
        long jMin;
        TelephonyManager telephonyManager = this.c;
        if (telephonyManager == null) {
            return;
        }
        ArrayList<ic> arrayList = this.n;
        ib ibVar = this.p;
        try {
            allCellInfo = telephonyManager.getAllCellInfo();
            try {
                this.h = null;
            } catch (SecurityException e2) {
                e = e2;
                this.h = e.getMessage();
            }
        } catch (SecurityException e3) {
            e = e3;
            allCellInfo = null;
        }
        if (allCellInfo != null && (size = allCellInfo.size()) != 0) {
            if (arrayList != null) {
                arrayList.clear();
            }
            for (int i = 0; i < size; i++) {
                CellInfo cellInfo = allCellInfo.get(i);
                if (cellInfo != null) {
                    try {
                        boolean zIsRegistered = cellInfo.isRegistered();
                        if (cellInfo instanceof CellInfoCdma) {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                            if (a(cellInfoCdma.getCellIdentity())) {
                                icVarA = a(cellInfoCdma, zIsRegistered);
                                jMin = Math.min(WebSocketProtocol.PAYLOAD_SHORT_MAX, ibVar.a(icVarA));
                                icVarA.l = (short) jMin;
                                arrayList.add(icVarA);
                            }
                        } else if (cellInfo instanceof CellInfoGsm) {
                            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                            if (a(cellInfoGsm.getCellIdentity())) {
                                icVarA = a(cellInfoGsm, zIsRegistered);
                                jMin = Math.min(WebSocketProtocol.PAYLOAD_SHORT_MAX, ibVar.a(icVarA));
                                icVarA.l = (short) jMin;
                                arrayList.add(icVarA);
                            }
                        } else if (cellInfo instanceof CellInfoWcdma) {
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                            if (a(cellInfoWcdma.getCellIdentity())) {
                                icVarA = a(cellInfoWcdma, zIsRegistered);
                                jMin = Math.min(WebSocketProtocol.PAYLOAD_SHORT_MAX, ibVar.a(icVarA));
                                icVarA.l = (short) jMin;
                                arrayList.add(icVarA);
                            }
                        } else if (cellInfo instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                            if (a(cellInfoLte.getCellIdentity())) {
                                icVarA = a(cellInfoLte, zIsRegistered);
                                jMin = Math.min(WebSocketProtocol.PAYLOAD_SHORT_MAX, ibVar.a(icVarA));
                                icVarA.l = (short) jMin;
                                arrayList.add(icVarA);
                            }
                        }
                    } catch (Throwable unused) {
                    }
                }
            }
        }
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        this.a |= 4;
        ibVar.a(arrayList);
    }

    private int u() {
        try {
            Class.forName("android.telephony.MSimTelephonyManager");
            this.r = 1;
        } catch (Throwable unused) {
        }
        if (this.r == 0) {
            try {
                Class.forName("android.telephony.TelephonyManager2");
                this.r = 2;
            } catch (Throwable unused2) {
            }
        }
        return this.r;
    }

    public final ArrayList<ic> a() {
        return this.b;
    }

    final boolean a(CellLocation cellLocation) {
        String str;
        boolean z = false;
        if (cellLocation == null) {
            return false;
        }
        int iC = c(cellLocation);
        if (iC == 1) {
            try {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                return a(gsmCellLocation.getLac(), gsmCellLocation.getCid());
            } catch (Throwable th) {
                th = th;
                str = "cgiUseful Cgi.iGsmT";
            }
        } else {
            if (iC != 2) {
                return true;
            }
            try {
                if (ik.b(cellLocation, "getSystemId", new Object[0]) > 0 && ik.b(cellLocation, "getNetworkId", new Object[0]) >= 0 && ik.b(cellLocation, "getBaseStationId", new Object[0]) >= 0) {
                    z = true;
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
                str = "cgiUseful Cgi.iCdmaT";
            }
        }
        ij.a(th, "CgiManager", str);
        return true;
    }

    public final ArrayList<ic> b() {
        return this.n;
    }

    public final int c() {
        return this.a;
    }

    public final int d() {
        return this.a & 3;
    }

    public final TelephonyManager e() {
        return this.c;
    }

    public final void f() {
        try {
            this.i = im.a(this.l);
            if (m() || this.b.isEmpty()) {
                p();
                this.d = im.b();
            }
            if (this.i) {
                n();
            } else {
                o();
            }
        } catch (SecurityException e) {
            this.h = e.getMessage();
        } catch (Throwable th) {
            ij.a(th, "CgiManager", "refresh");
        }
    }

    public final void g() {
        PhoneStateListener phoneStateListener;
        this.p.a();
        this.s = 0L;
        synchronized (this.u) {
            this.t = true;
        }
        TelephonyManager telephonyManager = this.c;
        if (telephonyManager != null && (phoneStateListener = this.g) != null) {
            try {
                telephonyManager.listen(phoneStateListener, 0);
            } catch (Throwable th) {
                ij.a(th, "CgiManager", "destroy");
            }
        }
        this.g = null;
        HandlerThread handlerThread = this.k;
        if (handlerThread != null) {
            handlerThread.quit();
            this.k = null;
        }
        this.o = -113;
        this.c = null;
        this.q = null;
    }

    final void h() {
        this.h = null;
        this.e = null;
        this.a = 0;
        this.b.clear();
        this.n.clear();
    }

    public final String i() {
        return this.m;
    }
}
