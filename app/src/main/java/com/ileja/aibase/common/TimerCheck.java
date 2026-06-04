package com.ileja.aibase.common;

/* JADX INFO: loaded from: classes.dex */
public abstract class TimerCheck {
    private int mCount = 0;
    private int mTimeOutCount = 1;
    private int mSleepTime = 1000;
    private boolean mExitFlag = false;
    private Thread mThread = null;

    static /* synthetic */ int access$108(TimerCheck timerCheck) {
        int i = timerCheck.mCount;
        timerCheck.mCount = i + 1;
        return i;
    }

    public abstract void doTimeOutWork();

    public abstract void doTimerCheckWork();

    public void exit() {
        this.mExitFlag = true;
    }

    public void start(int i, int i2) {
        this.mTimeOutCount = i;
        this.mSleepTime = i2;
        if (this.mThread == null) {
            Thread thread = new Thread(new Runnable() { // from class: com.ileja.aibase.common.TimerCheck.1
                @Override // java.lang.Runnable
                public void run() {
                    while (!TimerCheck.this.mExitFlag) {
                        TimerCheck.access$108(TimerCheck.this);
                        if (TimerCheck.this.mCount < TimerCheck.this.mTimeOutCount || TimerCheck.this.mTimeOutCount == -1) {
                            TimerCheck.this.doTimerCheckWork();
                            try {
                                Thread.sleep(TimerCheck.this.mSleepTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                TimerCheck.this.exit();
                            }
                        } else {
                            TimerCheck.this.doTimeOutWork();
                        }
                    }
                }
            });
            this.mThread = thread;
            thread.setName("TimerCheck");
            this.mThread.start();
        }
    }
}