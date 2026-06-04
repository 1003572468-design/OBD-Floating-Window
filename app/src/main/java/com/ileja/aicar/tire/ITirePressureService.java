package com.ileja.aicar.tire;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.ileja.aicar.tire.listener.ITirePressureListener;

/* JADX INFO: loaded from: classes.dex */
public interface ITirePressureService extends IInterface {

    public static class Default implements ITirePressureService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void bindTireDevice(String str) {
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void requestTirePressure(ITirePressureListener iTirePressureListener) {
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void stopTirePressure() {
        }

        @Override // com.ileja.aicar.tire.ITirePressureService
        public void unbindTireDevice() {
        }
    }

    public static abstract class Stub extends Binder implements ITirePressureService {
        private static final String DESCRIPTOR = "com.ileja.aicar.tire.ITirePressureService";

        private static class Proxy implements ITirePressureService {
            public static ITirePressureService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.ileja.aicar.tire.ITirePressureService
            public void bindTireDevice(String str) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    if (this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().bindTireDevice(str);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.ileja.aicar.tire.ITirePressureService
            public void requestTirePressure(ITirePressureListener iTirePressureListener) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeStrongBinder(iTirePressureListener != null ? iTirePressureListener.asBinder() : null);
                    if (this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().requestTirePressure(iTirePressureListener);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.tire.ITirePressureService
            public void stopTirePressure() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().stopTirePressure();
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.tire.ITirePressureService
            public void unbindTireDevice() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().unbindTireDevice();
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

        public static ITirePressureService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof ITirePressureService)) ? new Proxy(iBinder) : (ITirePressureService) iInterfaceQueryLocalInterface;
        }

        public static ITirePressureService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ITirePressureService iTirePressureService) {
            if (Proxy.sDefaultImpl != null || iTirePressureService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iTirePressureService;
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
                bindTireDevice(parcel.readString());
                parcel2.writeNoException();
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                unbindTireDevice();
                parcel2.writeNoException();
                return true;
            }
            if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                requestTirePressure(ITirePressureListener.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                return true;
            }
            if (i != 4) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            parcel.enforceInterface(DESCRIPTOR);
            stopTirePressure();
            parcel2.writeNoException();
            return true;
        }
    }

    void bindTireDevice(String str);

    void requestTirePressure(ITirePressureListener iTirePressureListener);

    void stopTirePressure();

    void unbindTireDevice();
}