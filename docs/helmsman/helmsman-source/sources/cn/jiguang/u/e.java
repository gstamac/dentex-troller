package cn.jiguang.u;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

/* JADX INFO: loaded from: classes.dex */
final class e extends PhoneStateListener {
    final /* synthetic */ d a;

    private e(d dVar) {
        this.a = dVar;
    }

    /* synthetic */ e(d dVar, byte b) {
        this(dVar);
    }

    @Override // android.telephony.PhoneStateListener
    public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
        cn.jiguang.v.a aVar;
        int networkId;
        try {
            super.onSignalStrengthsChanged(signalStrength);
            this.a.c.f = signalStrength.getGsmSignalStrength();
            CellLocation cellLocation = this.a.b.getCellLocation();
            if (cellLocation == null) {
                return;
            }
            if (!(cellLocation instanceof GsmCellLocation)) {
                if (cellLocation instanceof CdmaCellLocation) {
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                    this.a.c.e = cdmaCellLocation.getBaseStationId();
                    aVar = this.a.c;
                    networkId = cdmaCellLocation.getNetworkId();
                }
                this.a.b.listen(this, 0);
            }
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            this.a.c.e = gsmCellLocation.getCid();
            aVar = this.a.c;
            networkId = gsmCellLocation.getLac();
            aVar.d = networkId;
            this.a.b.listen(this, 0);
        } catch (Throwable unused) {
        }
    }
}
