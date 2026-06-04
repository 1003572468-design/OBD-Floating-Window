package com.ileja.framework.service.lbs;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.aibase.utils.AshmemUtils;
import com.ileja.aibase.utils.GsonUtils;
import com.ileja.ailbs.bean.query.QuerySource;
import com.ileja.ailbs.bean.query.listener.QueryResultListener;
import com.ileja.ailbs.bean.route.option.RouteSearchOption;
import com.ileja.ailbs.bean.route.result.RouteSearchResult;
import com.ileja.ailbs.route.IRoute;
import com.ileja.ailbs.route.listener.IRouteQueryResultListener;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public class HFRouteService extends HFService {
    private static final String CLS_NAME = "com.ileja.ailbs.route.AIRouteService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private IRoute iRoute;
    private HFServiceCallback serviceCallback = new HFServiceCallback() { // from class: com.ileja.framework.service.lbs.HFRouteService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "route service connected", LogLevel.RELEASE);
            HFRouteService.this.iRoute = IRoute.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            AILog.m4029d(HudSystem.TAG, "route service disconnected", LogLevel.RELEASE);
            HFRouteService.this.iRoute = null;
        }
    };

    private class HFRouteQueryResultListener extends IRouteQueryResultListener.Stub {
        private QueryResultListener listener;

        public HFRouteQueryResultListener(QueryResultListener queryResultListener) {
            this.listener = queryResultListener;
        }

        @Override // com.ileja.ailbs.route.listener.IRouteQueryResultListener
        public void onError(final RouteSearchOption routeSearchOption, final int i, final String str) {
            AILog.m4029d(HudSystem.TAG, "route error, option:" + routeSearchOption, LogLevel.RELEASE);
            ((HFService) HFRouteService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFRouteService.HFRouteQueryResultListener.3
                @Override // java.lang.Runnable
                public void run() {
                    if (HFRouteQueryResultListener.this.listener != null) {
                        HFRouteQueryResultListener.this.listener.onError(routeSearchOption, i, str);
                        HFRouteQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "route error, option:" + routeSearchOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }

        @Override // com.ileja.ailbs.route.listener.IRouteQueryResultListener
        public void onSuccess(final RouteSearchOption routeSearchOption, final RouteSearchResult routeSearchResult) {
            AILog.m4029d(HudSystem.TAG, "route success, option:" + routeSearchOption, LogLevel.RELEASE);
            ((HFService) HFRouteService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFRouteService.HFRouteQueryResultListener.1
                @Override // java.lang.Runnable
                public void run() {
                    if (HFRouteQueryResultListener.this.listener != null) {
                        HFRouteQueryResultListener.this.listener.onSuccess(routeSearchOption, routeSearchResult);
                        HFRouteQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "route success, option:" + routeSearchOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }

        @Override // com.ileja.ailbs.route.listener.IRouteQueryResultListener
        public void onSuccessParcel(final RouteSearchOption routeSearchOption, ParcelFileDescriptor parcelFileDescriptor) {
            AILog.m4029d(HudSystem.TAG, "route success, option:" + routeSearchOption, LogLevel.RELEASE);
            try {
                final RouteSearchResult routeSearchResult = (RouteSearchResult) GsonUtils.gsonResolve((String) AshmemUtils.fromSHaredMemoryFD(parcelFileDescriptor), RouteSearchResult.class);
                AILog.m4028d(HudSystem.TAG, "onSuccessParcel routeSearchResult:" + routeSearchResult);
                ((HFService) HFRouteService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFRouteService.HFRouteQueryResultListener.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (HFRouteQueryResultListener.this.listener != null) {
                            HFRouteQueryResultListener.this.listener.onSuccess(routeSearchOption, routeSearchResult);
                            HFRouteQueryResultListener.this.listener = null;
                            return;
                        }
                        AILog.m4031e(HudSystem.TAG, "route success, option:" + routeSearchOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelReroute() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFRouteService.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFRouteService.this.iRoute.cancelReRoute();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iRoute != null) {
            AILog.m4029d(HudSystem.TAG, "real cancel re route request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue cancel re route request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void cancelRoute() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFRouteService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFRouteService.this.iRoute.cancelRoute();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iRoute != null) {
            AILog.m4029d(HudSystem.TAG, "real cancel route request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue cancel route request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to bind route service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceCallback);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to unbind route service", LogLevel.RELEASE);
        m4406g(application);
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFRouteService;
    }

    public void reroute(final RouteSearchOption routeSearchOption, final QueryResultListener queryResultListener, final QuerySource querySource) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFRouteService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFRouteService.this.iRoute.requery(routeSearchOption, HFRouteService.this.new HFRouteQueryResultListener(queryResultListener), querySource.ordinal());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iRoute != null) {
            AILog.m4029d(HudSystem.TAG, "real route request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue route request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void route(final RouteSearchOption routeSearchOption, final QueryResultListener queryResultListener, final QuerySource querySource) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFRouteService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFRouteService.this.iRoute.query(routeSearchOption, HFRouteService.this.new HFRouteQueryResultListener(queryResultListener), querySource.ordinal());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iRoute != null) {
            AILog.m4029d(HudSystem.TAG, "real route request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue route request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }
}