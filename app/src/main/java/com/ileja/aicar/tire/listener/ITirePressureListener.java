package com.ileja.aicar.tire.listener;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface ITirePressureListener extends IInterface {

    public static class Default implements ITirePressureListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetFLPressure(double d, int i) {
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetFRPressure(double d, int i) {
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetLRPressure(double d, int i) {
        }

        @Override // com.ileja.aicar.tire.listener.ITirePressureListener
        public void onGetRRPressure(double d, int i) {
        }
    }

    public static abstract class Stub extends Binder implements ITirePressureListener {
        private static final String DESCRIPTOR = "com.ileja.aicar.tire.listener.ITirePressureListener";

        private static class Proxy implements ITirePressureListener {
            public static ITirePressureListener sDefaultImpl;
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

            @Override // com.ileja.aicar.tire.listener.ITirePressureListener
            public void onGetFLPressure(double d, int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeDouble(d);
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onGetFLPressure(d, i);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.tire.listener.ITirePressureListener
            public void onGetFRPressure(double d, int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeDouble(d);
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onGetFRPressure(d, i);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.tire.listener.ITirePressureListener
            public void onGetLRPressure(double d, int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeDouble(d);
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onGetLRPressure(d, i);
                    }
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.tire.listener.ITirePressureListener
            public void onGetRRPressure(double d, int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeDouble(d);
                    parcelObtain.writeInt(i);
                    if (this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) || Stub.getDefaultImpl() == null) {
                        parcelObtain2.readException();
                    } else {
                        Stub.getDefaultImpl().onGetRRPressure(d, i);
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

        public static ITirePressureListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof ITirePressureListener)) ? new Proxy(iBinder) : (ITirePressureListener) iInterfaceQueryLocalInterface;
        }

        public static ITirePressureListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ITirePressureListener iTirePressureListener) {
            if (Proxy.sDefaultImpl != null || iTirePressureListener == null) {
                return false;
            }
            Proxy.sDefaultImpl = iTirePressureListener;
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
                onGetFLPressure(parcel.readDouble(), parcel.readInt());
                parcel2.writeNoException();
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onGetFRPressure(parcel.readDouble(), parcel.readInt());
                parcel2.writeNoException();
                return true;
            }
            if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onGetRRPressure(parcel.readDouble(), parcel.readInt());
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
            onGetLRPressure(parcel.readDouble(), parcel.readInt());
            parcel2.writeNoException();
            return true;
        }
    }

    void onGetFLPressure(double d, int i);

    void onGetFRPressure(double d, int i);

    void onGetLRPressure(double d, int i);

    void onGetRRPressure(double d, int i);
}