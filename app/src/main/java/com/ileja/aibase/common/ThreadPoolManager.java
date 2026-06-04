package com.ileja.aibase.common;

import android.util.Log;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class ThreadPoolManager {
    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_POOL_SIZE = 1;
    private static int POOLS_COUNTS = 5;
    public static final int TYPE_FIFO = 0;
    public static final int TYPE_LIFO = 1;
    private static volatile ThreadPoolManager poolManager;
    private final String TAG = ThreadPoolManager.class.getName();
    private LinkedList<Runnable> asyncTasks;
    private PoolWorker mPoolWorker;
    private int poolSize;
    private ExecutorService threadPool;
    private int type;

    private class PoolWorker extends Thread {
        private boolean isLoop;

        private PoolWorker() {
            this.isLoop = true;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (this.isLoop) {
                synchronized (ThreadPoolManager.this.asyncTasks) {
                    Runnable asyncTask = ThreadPoolManager.this.getAsyncTask();
                    if (asyncTask == null) {
                        try {
                            Log.v(ThreadPoolManager.this.TAG, "PoolWorker wait");
                            ThreadPoolManager.this.asyncTasks.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (ThreadPoolManager.this.threadPool != null) {
                        Log.v(ThreadPoolManager.this.TAG, "PoolWorker execute");
                        ThreadPoolManager.this.threadPool.execute(asyncTask);
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

    private ThreadPoolManager(int i, int i2) {
        this.type = i == 0 ? 0 : 1;
        i2 = i2 < 1 ? 1 : i2;
        i2 = i2 > 10 ? 10 : i2;
        this.poolSize = i2;
        this.threadPool = Executors.newFixedThreadPool(i2);
        this.asyncTasks = new LinkedList<>();
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
    public Runnable getAsyncTask() {
        synchronized (this.asyncTasks) {
            if (this.asyncTasks.size() > 0) {
                return this.type == 0 ? this.asyncTasks.removeFirst() : this.asyncTasks.removeLast();
            }
            return null;
        }
    }

    public static ThreadPoolManager getInstance() {
        if (poolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (poolManager == null) {
                    poolManager = new ThreadPoolManager(0, POOLS_COUNTS);
                }
            }
        }
        return poolManager;
    }

    private void shutdownNow() {
        clearTasks();
        ExecutorService executorService = this.threadPool;
        if (executorService != null) {
            executorService.shutdownNow();
            this.threadPool = null;
        }
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

    public void addAsyncTask(Runnable runnable) {
        synchronized (this.asyncTasks) {
            this.asyncTasks.addLast(runnable);
            this.asyncTasks.notify();
        }
    }

    public synchronized void shutDownThreadPool() {
        if (poolManager != null) {
            poolManager.shutdownNow();
            poolManager = null;
        }
    }
}