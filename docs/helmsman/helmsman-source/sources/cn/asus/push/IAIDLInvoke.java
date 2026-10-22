package cn.asus.push;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface IAIDLInvoke extends IInterface {

    public class Default implements IAIDLInvoke {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // cn.asus.push.IAIDLInvoke
        public void asyncCall(DataBuffer dataBuffer, IAIDLCallback iAIDLCallback) {
        }

        @Override // cn.asus.push.IAIDLInvoke
        public void syncCall(DataBuffer dataBuffer) {
        }
    }

    public abstract class Stub extends Binder implements IAIDLInvoke {
        private static final String DESCRIPTOR = "cn.asus.push.IAIDLInvoke";
        static final int TRANSACTION_asyncCall = 2;
        static final int TRANSACTION_syncCall = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAIDLInvoke asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof IAIDLInvoke)) ? new c(iBinder) : (IAIDLInvoke) iInterfaceQueryLocalInterface;
        }

        public static IAIDLInvoke getDefaultImpl() {
            return c.a;
        }

        public static boolean setDefaultImpl(IAIDLInvoke iAIDLInvoke) {
            if (c.a != null || iAIDLInvoke == null) {
                return false;
            }
            c.a = iAIDLInvoke;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                syncCall(parcel.readInt() != 0 ? DataBuffer.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                asyncCall(parcel.readInt() != 0 ? DataBuffer.CREATOR.createFromParcel(parcel) : null, IAIDLCallback.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            }
            if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }
    }

    void asyncCall(DataBuffer dataBuffer, IAIDLCallback iAIDLCallback);

    void syncCall(DataBuffer dataBuffer);
}
