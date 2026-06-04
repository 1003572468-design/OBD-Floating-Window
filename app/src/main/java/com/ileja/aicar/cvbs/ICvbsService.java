package com.ileja.aicar.cvbs;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.ileja.aicar.cvbs.ICvbsCallback;

/* JADX INFO: loaded from: classes.dex */
public interface ICvbsService extends IInterface {

    public static class Default implements ICvbsService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.ileja.aicar.cvbs.ICvbsService
        public void send(String str, ICvbsCallback iCvbsCallback) {
        }
    }

    public static abstract class Stub extends Binder implements ICvbsService {
        private static final String DESCRIPTOR = "com.ileja.aicar.cvbs.ICvbsService";

        private static class Proxy implements ICvbsService {
            public static ICvbsService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.ileja.aicar.cvbs.ICvbsService
            public void send(String str, ICvbsCallback iCvbsCallback) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    parcelObtain.writeStrongBinder(iCvbsCallback != null ? iCvbsCallback.asBinder() : null);
                    if (this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().send(str, iCvbsCallback);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICvbsService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof ICvbsService)) ? new Proxy(iBinder) : (ICvbsService) iInterfaceQueryLocalInterface;
        }

        public static ICvbsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ICvbsService iCvbsService) {
            if (Proxy.sDefaultImpl != null || iCvbsService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iCvbsService;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i != 1) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            parcel.enforceInterface(DESCRIPTOR);
            send(parcel.readString(), ICvbsCallback.Stub.asInterface(parcel.readStrongBinder()));
            parcel2.writeNoException();
            return true;
        }
    }

    void send(String str, ICvbsCallback iCvbsCallback);
}