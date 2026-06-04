package com.ileja.aicar.obd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface OBDServer extends IInterface {

    public static class Default implements OBDServer {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.ileja.aicar.obd.OBDServer
        public int flush() {
            return 0;
        }

        @Override // com.ileja.aicar.obd.OBDServer
        public byte[] recvMsg() {
            return null;
        }

        @Override // com.ileja.aicar.obd.OBDServer
        public int sendCmd(byte[] bArr) {
            return 0;
        }

        @Override // com.ileja.aicar.obd.OBDServer
        public int setBaudRate(int i) {
            return 0;
        }

        @Override // com.ileja.aicar.obd.OBDServer
        public int wakeLock() {
            return 0;
        }

        @Override // com.ileja.aicar.obd.OBDServer
        public int wakeUnlock() {
            return 0;
        }
    }

    public static abstract class Stub extends Binder implements OBDServer {
        private static final String DESCRIPTOR = "com.ileja.aicar.obd.OBDServer";

        private static class Proxy implements OBDServer {
            public static OBDServer sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.ileja.aicar.obd.OBDServer
            public int flush() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(3, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().flush();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.ileja.aicar.obd.OBDServer
            public byte[] recvMsg() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().recvMsg();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.createByteArray();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.obd.OBDServer
            public int sendCmd(byte[] bArr) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeByteArray(bArr);
                    if (!this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendCmd(bArr);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.obd.OBDServer
            public int setBaudRate(int i) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    if (!this.mRemote.transact(4, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setBaudRate(i);
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.obd.OBDServer
            public int wakeLock() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(5, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().wakeLock();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.obd.OBDServer
            public int wakeUnlock() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(6, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().wakeUnlock();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readInt();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static OBDServer asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof OBDServer)) ? new Proxy(iBinder) : (OBDServer) iInterfaceQueryLocalInterface;
        }

        public static OBDServer getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(OBDServer oBDServer) {
            if (Proxy.sDefaultImpl != null || oBDServer == null) {
                return false;
            }
            Proxy.sDefaultImpl = oBDServer;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iSendCmd = sendCmd(parcel.createByteArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(iSendCmd);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    byte[] bArrRecvMsg = recvMsg();
                    parcel2.writeNoException();
                    parcel2.writeByteArray(bArrRecvMsg);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iFlush = flush();
                    parcel2.writeNoException();
                    parcel2.writeInt(iFlush);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    int baudRate = setBaudRate(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(baudRate);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iWakeLock = wakeLock();
                    parcel2.writeNoException();
                    parcel2.writeInt(iWakeLock);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    int iWakeUnlock = wakeUnlock();
                    parcel2.writeNoException();
                    parcel2.writeInt(iWakeUnlock);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int flush();

    byte[] recvMsg();

    int sendCmd(byte[] bArr);

    int setBaudRate(int i);

    int wakeLock();

    int wakeUnlock();
}