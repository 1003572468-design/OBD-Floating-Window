package com.ileja.framework.service.lbs;

import android.app.Application;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;
import com.ileja.aibase.common.AILog;
import com.ileja.aibase.common.logger.LogLevel;
import com.ileja.ailbs.bean.AILonlat;
import com.ileja.ailbs.bean.query.QueryOption;
import com.ileja.ailbs.bean.query.QuerySource;
import com.ileja.ailbs.bean.query.listener.QueryResultListener;
import com.ileja.ailbs.bean.query.option.GeocodeQueryOption;
import com.ileja.ailbs.bean.query.option.PoiNearbyQueryOption;
import com.ileja.ailbs.bean.query.option.PoiOnTheWayQueryOption;
import com.ileja.ailbs.bean.query.option.PoiQueryOption;
import com.ileja.ailbs.bean.query.option.ReverseGeocodeQueryOption;
import com.ileja.ailbs.bean.query.result.GeocodeQueryResult;
import com.ileja.ailbs.bean.query.result.PoiQueryResult;
import com.ileja.ailbs.bean.query.result.ReverseGeocodeQueryResult;
import com.ileja.ailbs.query.IQuery;
import com.ileja.ailbs.query.listener.IGeocodeQueryResultListener;
import com.ileja.ailbs.query.listener.IPoiKeywordQueryResultListener;
import com.ileja.ailbs.query.listener.IPoiNearbyQueryResultListener;
import com.ileja.ailbs.query.listener.IPoiOnTheWayQueryResultListener;
import com.ileja.ailbs.query.listener.IReverseGeocodeQueryResultListener;
import com.ileja.framework.HudSystem;
import com.ileja.framework.service.HFService;
import com.ileja.framework.service.HFServiceCallback;
import com.ileja.framework.service.PendingAction;

/* JADX INFO: loaded from: classes.dex */
public class HFQueryService extends HFService {
    private static final String CLS_NAME = "com.ileja.ailbs.query.AIQueryService";
    private static final String PKG_NAME = "com.ileja.aicore";
    private IQuery iQuery;
    private HFServiceCallback serviceCallback = new HFServiceCallback() { // from class: com.ileja.framework.service.lbs.HFQueryService.1
        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AILog.m4029d(HudSystem.TAG, "query service connected", LogLevel.RELEASE);
            HFQueryService.this.iQuery = IQuery.Stub.asInterface(iBinder);
        }

        @Override // com.ileja.framework.service.HFServiceCallback
        public void onServiceDisconnected(ComponentName componentName) {
            AILog.m4029d(HudSystem.TAG, "query service disconnected", LogLevel.RELEASE);
            HFQueryService.this.iQuery = null;
        }
    };

    private class HFGeocodeQueryResultListener extends IGeocodeQueryResultListener.Stub {
        private QueryResultListener listener;

        public HFGeocodeQueryResultListener(QueryResultListener queryResultListener) {
            this.listener = queryResultListener;
        }

        @Override // com.ileja.ailbs.query.listener.IGeocodeQueryResultListener
        public void onError(final GeocodeQueryOption geocodeQueryOption, final int i, final String str) {
            AILog.m4029d(HudSystem.TAG, "geocode query error, option:" + geocodeQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFGeocodeQueryResultListener.2
                @Override // java.lang.Runnable
                public void run() {
                    if (HFGeocodeQueryResultListener.this.listener != null) {
                        HFGeocodeQueryResultListener.this.listener.onError(geocodeQueryOption, i, str);
                        HFGeocodeQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "geocode query error, option:" + geocodeQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }

        @Override // com.ileja.ailbs.query.listener.IGeocodeQueryResultListener
        public void onSuccess(final GeocodeQueryOption geocodeQueryOption, final GeocodeQueryResult geocodeQueryResult) {
            AILog.m4029d(HudSystem.TAG, "geocode query success, option:" + geocodeQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFGeocodeQueryResultListener.1
                @Override // java.lang.Runnable
                public void run() {
                    if (HFGeocodeQueryResultListener.this.listener != null) {
                        HFGeocodeQueryResultListener.this.listener.onSuccess(geocodeQueryOption, geocodeQueryResult);
                        HFGeocodeQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "geocode query success, option:" + geocodeQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }
    }

    private class HFPoiKeywordQueryResultListener extends IPoiKeywordQueryResultListener.Stub {
        private QueryResultListener listener;

        public HFPoiKeywordQueryResultListener(QueryResultListener queryResultListener) {
            this.listener = queryResultListener;
        }

        @Override // com.ileja.ailbs.query.listener.IPoiKeywordQueryResultListener
        public void onError(final PoiQueryOption poiQueryOption, final int i, final String str) {
            AILog.m4029d(HudSystem.TAG, "keyword query error, option:" + poiQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFPoiKeywordQueryResultListener.2
                @Override // java.lang.Runnable
                public void run() {
                    if (HFPoiKeywordQueryResultListener.this.listener != null) {
                        HFPoiKeywordQueryResultListener.this.listener.onError(poiQueryOption, i, str);
                        HFPoiKeywordQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "keyword query error, option:" + poiQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }

        @Override // com.ileja.ailbs.query.listener.IPoiKeywordQueryResultListener
        public void onSuccess(final PoiQueryOption poiQueryOption, final PoiQueryResult poiQueryResult) {
            AILog.m4029d(HudSystem.TAG, "keyword query success, option:" + poiQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFPoiKeywordQueryResultListener.1
                @Override // java.lang.Runnable
                public void run() {
                    if (HFPoiKeywordQueryResultListener.this.listener != null) {
                        HFPoiKeywordQueryResultListener.this.listener.onSuccess(poiQueryOption, poiQueryResult);
                        HFPoiKeywordQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "keyword query success, option:" + poiQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }
    }

    private class HFPoiNearbyQueryResultListener extends IPoiNearbyQueryResultListener.Stub {
        private QueryResultListener listener;

        public HFPoiNearbyQueryResultListener(QueryResultListener queryResultListener) {
            this.listener = queryResultListener;
        }

        @Override // com.ileja.ailbs.query.listener.IPoiNearbyQueryResultListener
        public void onError(final PoiNearbyQueryOption poiNearbyQueryOption, final int i, final String str) {
            AILog.m4029d(HudSystem.TAG, "nearby query error, option:" + poiNearbyQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFPoiNearbyQueryResultListener.2
                @Override // java.lang.Runnable
                public void run() {
                    if (HFPoiNearbyQueryResultListener.this.listener != null) {
                        HFPoiNearbyQueryResultListener.this.listener.onError(poiNearbyQueryOption, i, str);
                        HFPoiNearbyQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "nearby query error, option:" + poiNearbyQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }

        @Override // com.ileja.ailbs.query.listener.IPoiNearbyQueryResultListener
        public void onSuccess(final PoiNearbyQueryOption poiNearbyQueryOption, final PoiQueryResult poiQueryResult) {
            AILog.m4029d(HudSystem.TAG, "nearby query success, option:" + poiNearbyQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFPoiNearbyQueryResultListener.1
                @Override // java.lang.Runnable
                public void run() {
                    if (HFPoiNearbyQueryResultListener.this.listener != null) {
                        HFPoiNearbyQueryResultListener.this.listener.onSuccess(poiNearbyQueryOption, poiQueryResult);
                        HFPoiNearbyQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "nearby query success, option:" + poiNearbyQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }
    }

    private class HFPoiOnTheWayQueryResultListener extends IPoiOnTheWayQueryResultListener.Stub {
        private QueryResultListener listener;

        public HFPoiOnTheWayQueryResultListener(QueryResultListener queryResultListener) {
            this.listener = queryResultListener;
        }

        @Override // com.ileja.ailbs.query.listener.IPoiOnTheWayQueryResultListener
        public void onError(final PoiOnTheWayQueryOption poiOnTheWayQueryOption, final int i, final String str) {
            AILog.m4029d(HudSystem.TAG, "OnTheWay query error, option:" + poiOnTheWayQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFPoiOnTheWayQueryResultListener.2
                @Override // java.lang.Runnable
                public void run() {
                    if (HFPoiOnTheWayQueryResultListener.this.listener != null) {
                        HFPoiOnTheWayQueryResultListener.this.listener.onError(poiOnTheWayQueryOption, i, str);
                        HFPoiOnTheWayQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "OnTheWay query error, option:" + poiOnTheWayQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }

        @Override // com.ileja.ailbs.query.listener.IPoiOnTheWayQueryResultListener
        public void onSuccess(final PoiOnTheWayQueryOption poiOnTheWayQueryOption, final PoiQueryResult poiQueryResult) {
            AILog.m4029d(HudSystem.TAG, "OnTheWay query success, option:" + poiOnTheWayQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFPoiOnTheWayQueryResultListener.1
                @Override // java.lang.Runnable
                public void run() {
                    if (HFPoiOnTheWayQueryResultListener.this.listener != null) {
                        HFPoiOnTheWayQueryResultListener.this.listener.onSuccess(poiOnTheWayQueryOption, poiQueryResult);
                        HFPoiOnTheWayQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "OnTheWay query success, option:" + poiOnTheWayQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }
    }

    private class HFReverseGeocodeQueryResultListener extends IReverseGeocodeQueryResultListener.Stub {
        private QueryResultListener listener;

        public HFReverseGeocodeQueryResultListener(QueryResultListener queryResultListener) {
            this.listener = queryResultListener;
        }

        @Override // com.ileja.ailbs.query.listener.IReverseGeocodeQueryResultListener
        public void onError(final ReverseGeocodeQueryOption reverseGeocodeQueryOption, final int i, final String str) {
            AILog.m4029d(HudSystem.TAG, "reverse geocode query error, option:" + reverseGeocodeQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFReverseGeocodeQueryResultListener.2
                @Override // java.lang.Runnable
                public void run() {
                    if (HFReverseGeocodeQueryResultListener.this.listener != null) {
                        HFReverseGeocodeQueryResultListener.this.listener.onError(reverseGeocodeQueryOption, i, str);
                        HFReverseGeocodeQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "reverse geocode query error, option:" + reverseGeocodeQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }

        @Override // com.ileja.ailbs.query.listener.IReverseGeocodeQueryResultListener
        public void onSuccess(final ReverseGeocodeQueryOption reverseGeocodeQueryOption, final ReverseGeocodeQueryResult reverseGeocodeQueryResult) {
            AILog.m4029d(HudSystem.TAG, "reverse geocode query success, option:" + reverseGeocodeQueryOption, LogLevel.RELEASE);
            ((HFService) HFQueryService.this).f6333a.post(new Runnable() { // from class: com.ileja.framework.service.lbs.HFQueryService.HFReverseGeocodeQueryResultListener.1
                @Override // java.lang.Runnable
                public void run() {
                    if (HFReverseGeocodeQueryResultListener.this.listener != null) {
                        HFReverseGeocodeQueryResultListener.this.listener.onSuccess(reverseGeocodeQueryOption, reverseGeocodeQueryResult);
                        HFReverseGeocodeQueryResultListener.this.listener = null;
                        return;
                    }
                    AILog.m4031e(HudSystem.TAG, "reverse geocode query success, option:" + reverseGeocodeQueryOption + " ,but listener is null!!!!", LogLevel.RELEASE);
                }
            });
        }
    }

    public void cancelGeocodeQuery() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.9
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.cancelGeocodeQuery();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real cancel geocode request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue cancel geocode request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void cancelKeywordsQuery() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.cancelPoiKeywordQuery();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real cancel poi request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue cancel poi request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void cancelNearbyQuery() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.cancelPoiNearbyQuery();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real cancel nearby request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue cancel nearby request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void cancelOnTheWayQuery() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.cancelPoiNearbyQuery();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real cancel nearby request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue cancel nearby request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void cancelReverseGeocodeQuery() {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.11
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.cancelReverseGeocodeQuery();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real cancel reverse geocode request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue cancel reverse geocode request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    @Override // com.ileja.framework.service.HFService
    public void create(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to bind query service", LogLevel.RELEASE);
        m4401b(application, "com.ileja.aicore", CLS_NAME, this.serviceCallback);
    }

    @Override // com.ileja.framework.service.HFService
    public void destroy(Application application) {
        AILog.m4029d(HudSystem.TAG, "try to unbind query service", LogLevel.RELEASE);
        m4406g(application);
        m4402c();
    }

    public void geocodeQuery(final GeocodeQueryOption geocodeQueryOption, final QueryResultListener queryResultListener, final QuerySource querySource) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.8
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.geocodeQuery(geocodeQueryOption, HFQueryService.this.new HFGeocodeQueryResultListener(queryResultListener), querySource.ordinal());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real geocode request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue geocode request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public String getCity(double d, double d2) {
        IQuery iQuery = this.iQuery;
        if (iQuery != null) {
            try {
                return iQuery.getCity(d, d2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override // com.ileja.framework.service.HFService
    public HFService.HFServiceName getName() {
        return HFService.HFServiceName.HFQueryService;
    }

    public void keyWordsQuery(final PoiQueryOption poiQueryOption, final QueryResultListener queryResultListener, final QuerySource querySource) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.poiKeywordQuery(poiQueryOption, HFQueryService.this.new HFPoiKeywordQueryResultListener(queryResultListener), querySource.ordinal());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real poi request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue poi request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void nearbyQuery(final PoiNearbyQueryOption poiNearbyQueryOption, final QueryResultListener queryResultListener, final QuerySource querySource) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.poiNearbyQuery(poiNearbyQueryOption, HFQueryService.this.new HFPoiNearbyQueryResultListener(queryResultListener), querySource.ordinal());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real nearby request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue nearby request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void onTheWayQuery(final PoiOnTheWayQueryOption poiOnTheWayQueryOption, final QueryResultListener queryResultListener, final QuerySource querySource) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.poiOnTheWayQuery(poiOnTheWayQueryOption, HFQueryService.this.new HFPoiOnTheWayQueryResultListener(queryResultListener), querySource.ordinal());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real OnTheWay request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue OnTheWay request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public void poiQuery(QueryOption queryOption, QueryResultListener queryResultListener, QuerySource querySource) {
        if (queryOption instanceof PoiQueryOption) {
            keyWordsQuery((PoiQueryOption) queryOption, queryResultListener, querySource);
        } else if (queryOption instanceof PoiOnTheWayQueryOption) {
            onTheWayQuery((PoiOnTheWayQueryOption) queryOption, queryResultListener, querySource);
        } else {
            nearbyQuery((PoiNearbyQueryOption) queryOption, queryResultListener, querySource);
        }
    }

    public void reverseGeocodeQuery(final ReverseGeocodeQueryOption reverseGeocodeQueryOption, final QueryResultListener queryResultListener, final QuerySource querySource) {
        PendingAction pendingAction = new PendingAction() { // from class: com.ileja.framework.service.lbs.HFQueryService.10
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HFQueryService.this.iQuery.reverseGeocodeQuery(reverseGeocodeQueryOption, HFQueryService.this.new HFReverseGeocodeQueryResultListener(queryResultListener), querySource.ordinal());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        if (this.iQuery != null) {
            AILog.m4029d(HudSystem.TAG, "real reverse geocode request action", LogLevel.RELEASE);
            pendingAction.run();
        } else {
            AILog.m4029d(HudSystem.TAG, "enqueue reverse geocode request action", LogLevel.RELEASE);
            m4403d(pendingAction);
        }
    }

    public AILonlat translatePoint(AILonlat aILonlat) {
        AILonlat aILonlatTranslate;
        IQuery iQuery = this.iQuery;
        if (iQuery != null) {
            try {
                aILonlatTranslate = iQuery.translate(aILonlat);
            } catch (RemoteException e) {
                e.printStackTrace();
                aILonlatTranslate = null;
            }
        } else {
            aILonlatTranslate = null;
        }
        return aILonlatTranslate == null ? aILonlat : aILonlatTranslate;
    }
}