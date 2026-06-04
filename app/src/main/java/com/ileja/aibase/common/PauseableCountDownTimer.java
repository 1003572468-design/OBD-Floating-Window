package com.ileja.aibase.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
public abstract class PauseableCountDownTimer {
    private static final String TAG = "PauseableCountDownTimer";
    private CountDownTimerExpand mCountDownTimerExpand;

    public abstract class AbsCountDownTimer {
        private static final int MSG = 1;
        private final long mCountdownInterval;
        private HandlerCountDown mHandler;
        private long mLeftTime = -2147483648L;
        private final long mMillisInFuture;
        private long mStopTimeInFuture;

        private class HandlerCountDown extends Handler {
            public HandlerCountDown(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                synchronized (AbsCountDownTimer.this) {
                    long jElapsedRealtime = AbsCountDownTimer.this.mStopTimeInFuture - SystemClock.elapsedRealtime();
                    if (jElapsedRealtime <= 0) {
                        AbsCountDownTimer.this.onFinish();
                    } else if (jElapsedRealtime < AbsCountDownTimer.this.mCountdownInterval) {
                        sendMessageDelayed(obtainMessage(1), jElapsedRealtime);
                    } else {
                        long jElapsedRealtime2 = SystemClock.elapsedRealtime();
                        AbsCountDownTimer.this.onTick(jElapsedRealtime);
                        long jElapsedRealtime3 = (jElapsedRealtime2 + AbsCountDownTimer.this.mCountdownInterval) - SystemClock.elapsedRealtime();
                        while (jElapsedRealtime3 < 0) {
                            jElapsedRealtime3 += AbsCountDownTimer.this.mCountdownInterval;
                        }
                        sendMessageDelayed(obtainMessage(1), jElapsedRealtime3);
                    }
                }
            }
        }

        public AbsCountDownTimer(long j, long j2) {
            this.mMillisInFuture = j;
            this.mCountdownInterval = j2;
        }

        public final void cancel() {
            this.mHandler.removeMessages(1);
        }

        public void init(Looper looper) {
            this.mHandler = new HandlerCountDown(looper);
        }

        public abstract void onFinish();

        public abstract void onTick(long j);

        public void pause() {
            cancel();
            this.mLeftTime = this.mStopTimeInFuture - SystemClock.elapsedRealtime();
        }

        public void resume() {
            long j = this.mLeftTime;
            if (j <= 0) {
                if (j != -2147483648L) {
                    onFinish();
                }
            } else {
                this.mStopTimeInFuture = SystemClock.elapsedRealtime() + this.mLeftTime;
                HandlerCountDown handlerCountDown = this.mHandler;
                handlerCountDown.sendMessage(handlerCountDown.obtainMessage(1));
                this.mLeftTime = 0L;
            }
        }

        public final synchronized AbsCountDownTimer start() {
            if (this.mMillisInFuture <= 0) {
                onFinish();
                return this;
            }
            this.mStopTimeInFuture = SystemClock.elapsedRealtime() + this.mMillisInFuture;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
            return this;
        }
    }

    class CountDownTimerExpand extends AbsCountDownTimer {
        private BugRunnable mBugRunnable;
        private Handler mHandler;
        private boolean mIsCountDownOn;
        private boolean mIsFinishPosted;
        private String mNumber;

        private class BugRunnable implements Runnable {
            private long millisUntilFinished;

            private BugRunnable() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (this.millisUntilFinished < 0) {
                    this.millisUntilFinished = 0L;
                }
                PauseableCountDownTimer.this.onTick(this.millisUntilFinished);
            }
        }

        public CountDownTimerExpand(long j, long j2) {
            super(j, j2);
            this.mIsCountDownOn = false;
            this.mIsFinishPosted = false;
            this.mNumber = null;
            Handler handler = new Handler(Looper.getMainLooper());
            this.mHandler = handler;
            super.init(handler.getLooper());
        }

        public void cancelTimer() {
            if (this.mIsCountDownOn) {
                super.cancel();
                this.mIsCountDownOn = false;
                this.mIsFinishPosted = true;
                this.mNumber = null;
                this.mHandler.post(new Runnable() { // from class: com.ileja.aibase.common.PauseableCountDownTimer.CountDownTimerExpand.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AILog.m4028d(PauseableCountDownTimer.TAG, "onCanceled");
                        PauseableCountDownTimer.this.onCanceled();
                    }
                });
            }
        }

        @Override // com.ileja.aibase.common.PauseableCountDownTimer.AbsCountDownTimer
        public void onFinish() {
            this.mIsCountDownOn = false;
            BugRunnable bugRunnable = this.mBugRunnable;
            if (bugRunnable != null) {
                this.mHandler.removeCallbacks(bugRunnable);
            }
            if (this.mIsFinishPosted) {
                return;
            }
            this.mIsFinishPosted = true;
            AILog.m4028d(PauseableCountDownTimer.TAG, "DelayCallCountDownTimer onFinish, num:" + this.mNumber);
            PauseableCountDownTimer.this.onFinish(this.mNumber);
            this.mNumber = null;
        }

        @Override // com.ileja.aibase.common.PauseableCountDownTimer.AbsCountDownTimer
        public void onTick(long j) {
            AILog.m4038v(PauseableCountDownTimer.TAG, "DelayCallCountDownTimer onTick:" + j);
            PauseableCountDownTimer.this.onTick(j);
            if (j <= 1000 || j >= 2000) {
                return;
            }
            BugRunnable bugRunnable = new BugRunnable();
            this.mBugRunnable = bugRunnable;
            bugRunnable.millisUntilFinished = j - 1000;
            this.mHandler.postDelayed(this.mBugRunnable, 1000L);
        }

        @Override // com.ileja.aibase.common.PauseableCountDownTimer.AbsCountDownTimer
        public void pause() {
            if (!this.mIsCountDownOn) {
                AILog.m4040w(PauseableCountDownTimer.TAG, "WARNING: CountDown Timer not start , no need pause !!");
            } else {
                super.pause();
                this.mHandler.post(new Runnable() { // from class: com.ileja.aibase.common.PauseableCountDownTimer.CountDownTimerExpand.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AILog.m4028d(PauseableCountDownTimer.TAG, "onPause");
                        PauseableCountDownTimer.this.onPaused();
                    }
                });
            }
        }

        @Override // com.ileja.aibase.common.PauseableCountDownTimer.AbsCountDownTimer
        public void resume() {
            if (!this.mIsCountDownOn) {
                AILog.m4040w(PauseableCountDownTimer.TAG, "WARNING: CountDown Timer not start , no need resume !!");
            } else {
                super.resume();
                this.mHandler.post(new Runnable() { // from class: com.ileja.aibase.common.PauseableCountDownTimer.CountDownTimerExpand.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AILog.m4028d(PauseableCountDownTimer.TAG, "onResume");
                        PauseableCountDownTimer.this.onResumed();
                    }
                });
            }
        }

        public void startTimer(String str) {
            if (this.mIsCountDownOn) {
                return;
            }
            this.mIsCountDownOn = true;
            this.mIsFinishPosted = false;
            this.mNumber = str;
            this.mHandler.post(new Runnable() { // from class: com.ileja.aibase.common.PauseableCountDownTimer.CountDownTimerExpand.1
                @Override // java.lang.Runnable
                public void run() {
                    AILog.m4028d(PauseableCountDownTimer.TAG, "onStart");
                    PauseableCountDownTimer.this.onStart();
                }
            });
            super.start();
        }
    }

    public PauseableCountDownTimer(long j, long j2) {
        this.mCountDownTimerExpand = new CountDownTimerExpand(j, j2);
    }

    public void cancelTimer() {
        this.mCountDownTimerExpand.cancelTimer();
    }

    public abstract void onCanceled();

    public abstract void onFinish(String str);

    public abstract void onPaused();

    public abstract void onResumed();

    public abstract void onStart();

    public abstract void onTick(long j);

    public void pauseTimer() {
        this.mCountDownTimerExpand.pause();
    }

    public void resumeTimer() {
        this.mCountDownTimerExpand.resume();
    }

    public void startTimer(String str) {
        this.mCountDownTimerExpand.startTimer(str);
    }
}