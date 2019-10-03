package com.mobvoi.android.common.async;

import android.os.Handler;
import android.os.HandlerThread;

import com.mobvoi.android.common.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class ThreadManager {
    private static final String TAG = "ThreadManager";

    private static final long THREAD_ALIVE_TIME = 30; // 30 seconds

    private static volatile ThreadManager sInstance;

    private BlockingQueue<Runnable> ioTaskQueue = new LinkedBlockingQueue<>();
    private Executor ioExecutor;

    private BlockingQueue<Runnable> networkTaskQueue = new LinkedBlockingQueue<>();
    private Executor networkExecutor;

    private final Map<String, Handler> asyncHandlers = new HashMap<>();

    public static ThreadManager getInstance() {
        if (sInstance == null) {
            synchronized (ThreadManager.class) {
                if (sInstance == null) {
                    sInstance = new ThreadManager();
                }
            }
        }
        return sInstance;
    }

    private ThreadManager() {
        // TODO support app provided parameters
        ioExecutor = new ThreadPoolExecutor(2, 4, THREAD_ALIVE_TIME, TimeUnit.SECONDS,
                ioTaskQueue, new IoThreadFactory());
        networkExecutor = new ThreadPoolExecutor(1, 4, THREAD_ALIVE_TIME, TimeUnit.SECONDS,
                networkTaskQueue, new NetworkThreadFactory());
    }

    public void executeIoTask(@NonNull Runnable task) {
        ioExecutor.execute(task);
    }

    public void executeNetworkTask(@NonNull Runnable task) {
        networkExecutor.execute(task);
    }

    /**
     * Cache the result if possible to avoid bad lock performance
     */
    public Handler getAsyncHandler(String name) {
        synchronized (asyncHandlers) {
            Handler handler = asyncHandlers.get(name);
            if (handler == null) {
                HandlerThread thread = new HandlerThread(name);
                thread.start();
                handler = new Handler(thread.getLooper());
                asyncHandlers.put(name, handler);
            }
            return handler;
        }
    }

    public void dump() {
        LogUtil.d(TAG, "I/O queue size: %d", ioTaskQueue.size());
        LogUtil.d(TAG, "Network queue size: %d", networkTaskQueue.size());
    }

    private static class IoThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(@NonNull Runnable task) {
            return new Thread(task, "MC IoThread");
        }
    }

    private static class NetworkThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(@NonNull Runnable task) {
            return new Thread(task, "MC NetworkThread");
        }
    }
}
