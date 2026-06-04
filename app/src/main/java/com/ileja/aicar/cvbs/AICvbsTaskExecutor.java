package com.ileja.aicar.cvbs;

import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class AICvbsTaskExecutor {
    private static volatile AICvbsTaskExecutor taskExecutor;
    private final String TAG = AICvbsTaskExecutor.class.getName();
    private LinkedList<Runnable> asyncTasks = new LinkedList<>();
    private PoolWorker mPoolWorker;

    private class PoolWorker extends Thread {
        private boolean isLoop;

        private PoolWorker() {
            this.isLoop = true;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (this.isLoop) {
                synchronized (AICvbsTaskExecutor.this.asyncTasks) {
                    Runnable cvbsTask = AICvbsTaskExecutor.this.getCvbsTask();
                    if (cvbsTask == null) {
                        try {
                            AICvbsTaskExecutor.this.asyncTasks.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        cvbsTask.run();
                    }
                }
            }
        }

        public void stopLoop() {
            this.isLoop = false;
            try {
                interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private AICvbsTaskExecutor() {
        start();
    }

    private void clearTasks() {
        LinkedList<Runnable> linkedList = this.asyncTasks;
        if (linkedList != null) {
            synchronized (linkedList) {
                this.asyncTasks.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Runnable getCvbsTask() {
        synchronized (this.asyncTasks) {
            if (this.asyncTasks.size() <= 0) {
                return null;
            }
            return this.asyncTasks.removeFirst();
        }
    }

    public static AICvbsTaskExecutor getInst() {
        if (taskExecutor == null) {
            synchronized (AICvbsTaskExecutor.class) {
                if (taskExecutor == null) {
                    taskExecutor = new AICvbsTaskExecutor();
                }
            }
        }
        return taskExecutor;
    }

    private void shutdownNow() {
        clearTasks();
        PoolWorker poolWorker = this.mPoolWorker;
        if (poolWorker != null) {
            poolWorker.stopLoop();
            this.mPoolWorker = null;
        }
    }

    private void start() {
        if (this.mPoolWorker == null) {
            PoolWorker poolWorker = new PoolWorker();
            this.mPoolWorker = poolWorker;
            poolWorker.start();
        }
    }

    public void addCvbsTask(Runnable runnable) {
        synchronized (this.asyncTasks) {
            this.asyncTasks.addLast(runnable);
            this.asyncTasks.notify();
        }
    }

    public synchronized void shutDownThreadPool() {
        if (taskExecutor != null) {
            taskExecutor.shutdownNow();
            taskExecutor = null;
        }
    }
}