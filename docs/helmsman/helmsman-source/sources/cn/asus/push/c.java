package cn.asus.push;

import android.os.IBinder;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
final class c implements IAIDLInvoke {
    public static IAIDLInvoke a;
    private IBinder b;

    c(IBinder iBinder) {
        this.b = iBinder;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.b;
    }

    @Override // cn.asus.push.IAIDLInvoke
    public final void asyncCall(DataBuffer dataBuffer, IAIDLCallback iAIDLCallback) {
        Parcel parcelObtain = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("cn.asus.push.IAIDLInvoke");
            if (dataBuffer != null) {
                parcelObtain.writeInt(1);
                dataBuffer.writeToParcel(parcelObtain, 0);
            } else {
                parcelObtain.writeInt(0);
            }
            parcelObtain.writeStrongBinder(iAIDLCallback != null ? iAIDLCallback.asBinder() : null);
            if (this.b.transact(2, parcelObtain, null, 1) || IAIDLInvoke.Stub.getDefaultImpl() == null) {
                return;
            }
            IAIDLInvoke.Stub.getDefaultImpl().asyncCall(dataBuffer, iAIDLCallback);
        } finally {
            parcelObtain.recycle();
        }
    }

    @Override // cn.asus.push.IAIDLInvoke
    public final void syncCall(DataBuffer dataBuffer) {
        Parcel parcelObtain = Parcel.obtain();
        Parcel parcelObtain2 = Parcel.obtain();
        try {
            parcelObtain.writeInterfaceToken("cn.asus.push.IAIDLInvoke");
            if (dataBuffer != null) {
                parcelObtain.writeInt(1);
                dataBuffer.writeToParcel(parcelObtain, 0);
            } else {
                parcelObtain.writeInt(0);
            }
            if (this.b.transact(1, parcelObtain, parcelObtain2, 0) || IAIDLInvoke.Stub.getDefaultImpl() == null) {
                parcelObtain2.readException();
            } else {
                IAIDLInvoke.Stub.getDefaultImpl().syncCall(dataBuffer);
            }
        } finally {
            parcelObtain2.recycle();
            parcelObtain.recycle();
        }
    }
}
