package com.ileja.aicar.cvbs;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface CVBSServer extends IInterface {

    public static class Default implements CVBSServer {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.ileja.aicar.cvbs.CVBSServer
        public int flush() {
            return 0;
        }

        @Override // com.ileja.aicar.cvbs.CVBSServer
        public String recvMsg() {
            return null;
        }

        @Override // com.ileja.aicar.cvbs.CVBSServer
        public int sendCmd(String str) {
            return 0;
        }
    }

    public static abstract class Stub extends Binder implements CVBSServer {
        private static final String DESCRIPTOR = "com.ileja.aicar.cvbs.CVBSServer";

        private static class Proxy implements CVBSServer {
            public static CVBSServer sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.ileja.aicar.cvbs.CVBSServer
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

            @Override // com.ileja.aicar.cvbs.CVBSServer
            public String recvMsg() {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(2, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().recvMsg();
                    }
                    parcelObtain2.readException();
                    return parcelObtain2.readString();
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // com.ileja.aicar.cvbs.CVBSServer
            public int sendCmd(String str) {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeString(str);
                    if (!this.mRemote.transact(1, parcelObtain, parcelObtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendCmd(str);
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

        public static CVBSServer asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (iInterfaceQueryLocalInterface == null || !(iInterfaceQueryLocalInterface instanceof CVBSServer)) ? new Proxy(iBinder) : (CVBSServer) iInterfaceQueryLocalInterface;
        }

        public static CVBSServer getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(CVBSServer cVBSServer) {
            if (Proxy.sDefaultImpl != null || cVBSServer == null) {
                return false;
            }
            Proxy.sDefaultImpl = cVBSServer;
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
                int iSendCmd = sendCmd(parcel.readString());
                parcel2.writeNoException();
                parcel2.writeInt(iSendCmd);
                return true;
            }
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                String strRecvMsg = recvMsg();
                parcel2.writeNoException();
                parcel2.writeString(strRecvMsg);
                return true;
            }
            if (i != 3) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            parcel.enforceInterface(DESCRIPTOR);
            int iFlush = flush();
            parcel2.writeNoException();
            parcel2.writeInt(iFlush);
            return true;
        }
    }

    int flush();

    String recvMsg();

    int sendCmd(String str);
}