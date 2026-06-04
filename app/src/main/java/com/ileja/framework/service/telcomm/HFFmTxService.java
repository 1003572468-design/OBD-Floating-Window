package com.ileja.framework.service.telcomm;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aitelcomm.fmtx.IFmTxFreqListener;
import com.ileja.aitelcomm.fmtx.IFmTxManager;
import com.ileja.aitelcomm.fmtx.listener.AIFmTxFreqListener;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public class HFFmTxService extends HFService {
    private static final String CLS_NAME = "com.ileja.aitelcomm.fmtx.AIFmTxService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private static final String TAG = "HFFmTxService";
    private IFmTxManager fmTxManager;
    private HFServiceCallback serviceConnection = new HFServiceCallback() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "fmtx service connected", LogLevel.RELEASE);
            HFFmTxService.this.fmTxManager = IFmTxManager.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            HFFmTxService.this.fmTxManager = null;
            AILog.m4029d(HudSystem.TAG, "fmtx service disconnected", LogLevel.RELEASE);
        }
    };

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to bind fmtx service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceConnection);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        if (this.fmTxManager != null) {
            m4406g(application);
        }
        m4402c();
    }

    public void fmTxPause() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFFmTxService.this.fmTxManager.fmTxPause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.fmtx.AIFmTxService[status_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.fmTxManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public int fmTxPowerOff() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFFmTxService.this.fmTxManager.fmTxPowerOff();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.fmtx.AIFmTxService[power_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        IFmTxManager iFmTxManager = this.fmTxManager;
        int iFmTxPowerOff = 0;
        if (iFmTxManager != null) {
            try {
                iFmTxPowerOff = iFmTxManager.fmTxPowerOff();
                AILog.m4029d(TAG, "power off res :" + iFmTxPowerOff, LogLevel.RELEASE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        m4403d(pendingAction);
        return iFmTxPowerOff;
    }

    public int fmTxPowerOn() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFFmTxService.this.fmTxManager.fmTxPowerOn();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.fmtx.AIFmTxService[power_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        IFmTxManager iFmTxManager = this.fmTxManager;
        int iFmTxPowerOn = 0;
        if (iFmTxManager != null) {
            try {
                iFmTxPowerOn = iFmTxManager.fmTxPowerOn();
                AILog.m4029d(TAG, "power on res :" + iFmTxPowerOn, LogLevel.RELEASE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        m4403d(pendingAction);
        return iFmTxPowerOn;
    }

    public void fmTxResume() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFFmTxService.this.fmTxManager.fmTxResume();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.fmtx.AIFmTxService[status_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        if (this.fmTxManager != null) {
            pendingAction.run();
        }
        m4403d(pendingAction);
    }

    public int fmTxSearchFreq() {
        IFmTxManager iFmTxManager = this.fmTxManager;
        if (iFmTxManager == null) {
            return 1080;
        }
        try {
            return iFmTxManager.fmTxSearchFreq();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 1080;
        }
    }

    public int fmTxSetFreq(final int i, final AIFmTxFreqListener aIFmTxFreqListener) {
        int iFmTxSetFreq;
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.8
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFFmTxService.this.fmTxManager.fmTxSetFreq(i, new IFmTxFreqListener.Stub() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.8.1
                        @Override // com.ileja.aitelcomm.fmtx.IFmTxFreqListener
                        public void onFreqChanged(int i2) {
                            AIFmTxFreqListener aIFmTxFreqListener2 = aIFmTxFreqListener;
                            if (aIFmTxFreqListener2 != null) {
                                aIFmTxFreqListener2.onFreqChanged(i2);
                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.fmtx.AIFmTxService[freq_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        IFmTxManager iFmTxManager = this.fmTxManager;
        if (iFmTxManager != null) {
            try {
                iFmTxSetFreq = iFmTxManager.fmTxSetFreq(i, new IFmTxFreqListener.Stub(this) { // from class: com.ileja.framework.service.telcomm.HFFmTxService.9
                    @Override // com.ileja.aitelcomm.fmtx.IFmTxFreqListener
                    public void onFreqChanged(int i2) {
                        AIFmTxFreqListener aIFmTxFreqListener2 = aIFmTxFreqListener;
                        if (aIFmTxFreqListener2 != null) {
                            aIFmTxFreqListener2.onFreqChanged(i2);
                        }
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
                iFmTxSetFreq = 0;
            }
        } else {
            iFmTxSetFreq = 0;
        }
        m4403d(pendingAction);
        return iFmTxSetFreq;
    }

    public int fmTxTurnOff() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFFmTxService.this.fmTxManager.fmTxTurnOff();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.fmtx.AIFmTxService[turn_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        IFmTxManager iFmTxManager = this.fmTxManager;
        int iFmTxTurnOff = -1;
        if (iFmTxManager != null) {
            try {
                iFmTxTurnOff = iFmTxManager.fmTxTurnOff();
                AILog.m4029d(TAG, "power off res :" + iFmTxTurnOff, LogLevel.RELEASE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        m4403d(pendingAction);
        return iFmTxTurnOff;
    }

    public int fmTxTurnOn() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.telcomm.HFFmTxService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFFmTxService.this.fmTxManager.fmTxTurnOn();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        pendingAction.setKey("com.ileja.aitelcomm.fmtx.AIFmTxService[turn_action]");
        pendingAction.setType(PendingAction.PendingActionType.Replace);
        IFmTxManager iFmTxManager = this.fmTxManager;
        int iFmTxTurnOn = -1;
        if (iFmTxManager != null) {
            try {
                iFmTxTurnOn = iFmTxManager.fmTxTurnOn();
                AILog.m4029d(TAG, "turn on res :" + iFmTxTurnOn, LogLevel.RELEASE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        m4403d(pendingAction);
        return iFmTxTurnOn;
    }

    public int getFmTxFreq() {
        IFmTxManager iFmTxManager = this.fmTxManager;
        if (iFmTxManager == null) {
            return 0;
        }
        try {
            return iFmTxManager.fmTxGetFreq();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getFmTxStatus() {
        IFmTxManager iFmTxManager = this.fmTxManager;
        if (iFmTxManager == null) {
            return 0;
        }
        try {
            return iFmTxManager.fmTxGetStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFFmTxService;
    }

    public final int getRealFmTxStatus() {
        IFmTxManager iFmTxManager = this.fmTxManager;
        if (iFmTxManager == null) {
            return 0;
        }
        try {
            return iFmTxManager.fmTxGetRealStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}