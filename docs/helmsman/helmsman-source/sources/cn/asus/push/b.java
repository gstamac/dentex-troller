package cn.asus.push;

import android.os.IBinder;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
final class b implements IAIDLCallback {
    public static IAIDLCallback a;
    private IBinder b;

    b(IBinder iBinder) {
        this.b = iBinder;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.b;
    }

    @Override // cn.asus.push.IAIDLCallback
    public final void call(DataBuffer dataBuffer) {
        Parcel parcelObtain = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("cn.asus.push.IAIDLCallback");
            if (dataBuffer != null) {
                parcelObtain.writeInt(1);
                dataBuffer.writeToParcel(parcelObtain, 0);
            } else {
                parcelObtain.writeInt(0);
            }
            if (this.b.transact(1, parcelObtain, null, 1) || IAIDLCallback.Stub.getDefaultImpl() == null) {
                return;
            }
            IAIDLCallback.Stub.getDefaultImpl().call(dataBuffer);
        } finally {
            parcelObtain.recycle();
        }
    }
}
