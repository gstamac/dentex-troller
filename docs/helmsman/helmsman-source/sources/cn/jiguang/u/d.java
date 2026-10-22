package cn.jiguang.u;

import android.content.Context;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import com.facebook.places.model.PlaceFields;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class d {
    private Context a;
    private final TelephonyManager b;
    private cn.jiguang.v.a c;

    d(Context context) {
        this.a = context;
        this.b = (TelephonyManager) context.getSystemService(PlaceFields.PHONE);
    }

    final cn.jiguang.v.a a() {
        return this.c;
    }

    final void b() {
        if (this.b == null) {
            cn.jiguang.w.a.d("JLocationCell", "get telephonyManager failed");
            return;
        }
        cn.jiguang.v.a aVar = new cn.jiguang.v.a();
        this.c = aVar;
        aVar.a = cn.jiguang.d.b.b(this.a);
        String networkOperator = this.b.getNetworkOperator();
        if (networkOperator.length() > 3) {
            this.c.b = Integer.parseInt(networkOperator.substring(0, 3));
            this.c.c = Integer.parseInt(networkOperator.substring(3));
        }
        this.c.i = this.b.getNetworkOperatorName();
        cn.jiguang.v.a aVar2 = this.c;
        int networkType = this.b.getNetworkType();
        cn.jiguang.ad.d.a("TeleonyManagerUtils", "getRadioType - networkType:" + networkType);
        String str = (networkType == 4 || networkType == 7 || networkType == 5 || networkType == 6 || networkType == 12 || networkType == 14) ? "cdma" : networkType == 13 ? "lte" : "gsm";
        cn.jiguang.ad.d.a("TeleonyManagerUtils", "getRadioType - radioType:".concat(str));
        aVar2.g = str;
        this.c.h = cn.jiguang.f.j.a(this.a, this.b.getNetworkType());
        List<CellInfo> allCellInfo = this.b.getAllCellInfo();
        if (allCellInfo == null || allCellInfo.size() <= 0) {
            return;
        }
        for (CellInfo cellInfo : allCellInfo) {
            if (cellInfo != null) {
                if (cellInfo instanceof CellInfoLte) {
                    CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                    CellSignalStrengthLte cellSignalStrength = cellInfoLte.getCellSignalStrength();
                    CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
                    this.c.f = cellSignalStrength.getDbm();
                    this.c.e = cellIdentity.getCi();
                    this.c.d = cellIdentity.getTac();
                    if (this.c.e < 268435455) {
                        return;
                    }
                } else if (cellInfo instanceof CellInfoGsm) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                    CellSignalStrengthGsm cellSignalStrength2 = cellInfoGsm.getCellSignalStrength();
                    CellIdentityGsm cellIdentity2 = cellInfoGsm.getCellIdentity();
                    this.c.f = cellSignalStrength2.getDbm();
                    this.c.e = cellIdentity2.getCid();
                    this.c.d = cellIdentity2.getLac();
                    if (this.c.e < 65535) {
                        return;
                    }
                } else if (cellInfo instanceof CellInfoCdma) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                    CellSignalStrengthCdma cellSignalStrength3 = cellInfoCdma.getCellSignalStrength();
                    CellIdentityCdma cellIdentity3 = cellInfoCdma.getCellIdentity();
                    this.c.f = cellSignalStrength3.getDbm();
                    this.c.e = cellIdentity3.getBasestationId();
                    this.c.d = cellIdentity3.getNetworkId();
                    if (this.c.e < 65535) {
                        return;
                    }
                } else if (cellInfo instanceof CellInfoWcdma) {
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                    CellSignalStrengthWcdma cellSignalStrength4 = cellInfoWcdma.getCellSignalStrength();
                    CellIdentityWcdma cellIdentity4 = cellInfoWcdma.getCellIdentity();
                    this.c.f = cellSignalStrength4.getDbm();
                    this.c.e = cellIdentity4.getCid();
                    this.c.d = cellIdentity4.getLac();
                    if (this.c.e < 268435455) {
                        return;
                    }
                } else {
                    continue;
                }
            }
        }
    }
}
